package privateAV.modules;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.*;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.fleet.FleetSpecification;
import org.matsim.contrib.dvrp.run.QSimScopeObjectListener;
import org.matsim.core.controler.events.BeforeMobsimEvent;
import org.matsim.core.controler.listener.BeforeMobsimListener;
import privateAV.PFAVUtils;
import privateAV.vehicle.PFAVSpecification;

import java.util.*;

public class PFAVFleetStatsCalculator implements QSimScopeObjectListener<Fleet>, BeforeMobsimListener {

    private final FleetSpecification fleetSpecification;
    private final Population population;
    private Set<Id<DvrpVehicle>> oldVehicles = new HashSet<Id<DvrpVehicle>>();
    private final String mode;

    public PFAVFleetStatsCalculator(FleetSpecification fleetSpecification, Scenario scenario, String mode){
        this.fleetSpecification = fleetSpecification;
        this.population= scenario.getPopulation();
        this.mode = mode;
    }

    @Override
    public void notifyBeforeMobsim(BeforeMobsimEvent event) {
        List<PFAVSpecification> vehiclesForIteration = determinePFAVsForIteration();
        for (PFAVSpecification sp : vehiclesForIteration) {
            if (oldVehicles.contains(sp.getId())) {
                fleetSpecification.replaceVehicleSpecification(sp);
                oldVehicles.remove(sp.getId());
            } else {
                fleetSpecification.addVehicleSpecification(sp);
            }
        }
        //delete all vehicle specifications that are no longer needed (agents try out different mode)
        for (Id<DvrpVehicle> id : oldVehicles) {
            fleetSpecification.removeVehicleSpecification(id);
        }
    }

    private List<PFAVSpecification> determinePFAVsForIteration() {
        List<PFAVSpecification> vehiclesForIteration = new ArrayList<>();
        for(Person p : this.population.getPersons().values()) {
            Id<Link> vehicleStartLink = null;
            Queue<Double> actEndTimesAfterModeLegs = new LinkedList<>();
            Queue<Double> actEndTimesBeforeModeLegs = new LinkedList<>();


            //TODO: test this
            Plan plan = p.getSelectedPlan();
            for (int i = 0; i < plan.getPlanElements().size(); i++) {
                PlanElement pe = plan.getPlanElements().get(i);
                if (pe instanceof Leg) {
                    if (((Leg) pe).getMode().equals(mode)) {
                        if(vehicleStartLink == null) vehicleStartLink = ((Leg) pe).getRoute().getStartLinkId();
                        actEndTimesAfterModeLegs.add(getActivityEndTimeOfNextNonStageActivity(plan, i));
                        actEndTimesBeforeModeLegs.add(getActivityEndTimeOfPreviousNonStageActivity(plan, i));
                    }
                }
            }
            if (vehicleStartLink != null) {
                Id<DvrpVehicle> vehicleId = Id.create(p.getId().toString() + PFAVUtils.PFAV_ID_SUFFIX, DvrpVehicle.class);
                PFAVSpecification specification = PFAVSpecification.newBuilder()
                        .serviceBeginTime(0.)
                        .serviceEndTime(36 * 3600)
                        .startLinkId(vehicleStartLink)
                        .id(vehicleId)
                        .capacity(PFAVUtils.DEFAULT_PFAV_CAPACITY)
//                        .actEndTimes(actEndTimesAfterModeLegs)
                        .actEndTimes(actEndTimesBeforeModeLegs)
                        .build();
                vehiclesForIteration.add(specification);
            }
        }
        return vehiclesForIteration;
    }

    private double getActivityEndTimeOfNextNonStageActivity(Plan plan, int startIndex) {
        Leg lastLeg = null;
        for (int i = startIndex; i < plan.getPlanElements().size(); i++) {
            //last activity in the plan, agent does not need the vehicle back before end of iteration
            if (i == plan.getPlanElements().size() - 1) return Double.POSITIVE_INFINITY;
            PlanElement pe = plan.getPlanElements().get(i);
            if (pe instanceof Leg)
                lastLeg = (Leg) pe;
            if (pe instanceof Activity) {
                //filter stage activities
                if (!((Activity) pe).getType().contains("interaction")) {
                    //activity could be something like leisure in open berlin scenario which does not have an end time set
                    Activity act = (Activity) pe;
                    if (act.getMaximumDuration() == Double.NEGATIVE_INFINITY) {
                        if (act.getEndTime() == Double.NEGATIVE_INFINITY) {
                            throw new RuntimeException("cannot compute must return time for PFAVehicle of person " + plan.getPerson().getId() + " for activity " + act.toString());
                        }
                        return act.getEndTime();
                    }
                    if (act.getEndTime() == Double.NEGATIVE_INFINITY) {
                        return lastLeg.getDepartureTime() + lastLeg.getRoute().getTravelTime() + act.getMaximumDuration();
                    }
                    return Math.min(((Activity) pe).getEndTime(), lastLeg.getDepartureTime() + lastLeg.getRoute().getTravelTime() + act.getMaximumDuration());
                }
            }
        }
        return Double.NEGATIVE_INFINITY;
    }

    private double getActivityEndTimeOfPreviousNonStageActivity(Plan plan, int startIndex) {
        Leg lastLeg = null;
        for (int i = startIndex; i >= 0; i--) {
            PlanElement pe = plan.getPlanElements().get(i);
            if (pe instanceof Leg)
                lastLeg = (Leg) pe;
            if (pe instanceof Activity) {
                //filter stage activities - TODO: this can be surely done more properly but works so far..
//                TODO: should we allow stage activities for PFAV owner in the first place? or delete acces and egress walks straight from the plans?
                if (!((Activity) pe).getType().contains("interaction")) {
                    //activity could be something like leisure in open berlin scenario which does not have an end time set
                    Activity act = (Activity) pe;
                    if (act.getEndTime() == Double.NEGATIVE_INFINITY) {
                        if (lastLeg.getDepartureTime() == Double.NEGATIVE_INFINITY) {
                            if (act.getMaximumDuration() == Double.NEGATIVE_INFINITY) {
                                throw new RuntimeException("cannot compute must return time for PFAVehicle of person " + plan.getPerson().getId() + " for activity " + act.toString());
                            }
                            //does the leg *before* the activity have a departure time and a travel time?
                            Leg legBeforeAct = (Leg) plan.getPlanElements().get(i - 1);
                            if (legBeforeAct.getDepartureTime() != Double.NEGATIVE_INFINITY && legBeforeAct.getRoute().getTravelTime() != Double.NEGATIVE_INFINITY) {
                                return legBeforeAct.getDepartureTime() + legBeforeAct.getRoute().getTravelTime() + act.getMaximumDuration();
                            }
                            //no it does not, so we need to calculate the must return time by computing end time of the activity piece by piece
                            return computeMustReturnTimeConsecutivelyFromTheStart(plan, i);
                        } else {
                            return lastLeg.getDepartureTime();
                        }
                    } else {
                        return act.getEndTime();
                    }
                }
            }
        }
        throw new IllegalStateException("could not compute must return time for agent " + plan.getPerson().getId() + " before leg " + plan.getPlanElements().get(startIndex));
    }

    private double computeMustReturnTimeConsecutivelyFromTheStart(Plan plan, int i) {
        double time = ((Activity) plan.getPlanElements().get(0)).getEndTime();
        if (time == Double.NEGATIVE_INFINITY) throw new IllegalStateException("end time of first activity of agent " + plan.getPerson().getId()
                + " is not set.");
        for (int z = 1; z <= i; z++) {
            PlanElement current = plan.getPlanElements().get(z);
            if (current instanceof Activity) {
                if (((Activity) current).getEndTime() != Double.NEGATIVE_INFINITY) {
                    time = ((Activity) current).getEndTime();
                } else {
                    time += ((Activity) current).getMaximumDuration();
                }
            } else if (current instanceof Leg) {
                if (((Leg) current).getDepartureTime() != Double.NEGATIVE_INFINITY) {
                    time = ((Leg) current).getDepartureTime();
                } else {
                    time += ((Leg) current).getRoute().getTravelTime();
                }
            }
        }
        return time;
    }

    @Override
    public void objectCreated(Fleet fleet) {
        this.oldVehicles.clear();
        this.oldVehicles.addAll(fleet.getVehicles().keySet());
    }

}
