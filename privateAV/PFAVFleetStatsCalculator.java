package privateAV;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.Population;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.fleet.FleetSpecification;
import org.matsim.contrib.dvrp.run.QSimScopeObjectListener;
import org.matsim.contrib.dvrp.util.LinkTimePair;
import org.matsim.core.controler.events.BeforeMobsimEvent;
import org.matsim.core.controler.listener.BeforeMobsimListener;

final class PFAVFleetStatsCalculator implements QSimScopeObjectListener<Fleet>, BeforeMobsimListener {

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
            LinkedList<PFAVehicle.MustReturnLinkTimePair> mustReturnLinkTimePairs = new LinkedList<>();
            //TODO: test this
            Plan plan = p.getSelectedPlan();
            for (int i = 0; i < plan.getPlanElements().size(); i++) {
                PlanElement pe = plan.getPlanElements().get(i);
                if (pe instanceof Leg) {
                    if (((Leg) pe).getMode().equals(mode)) {
                        if(vehicleStartLink == null) vehicleStartLink = ((Leg) pe).getRoute().getStartLinkId();
                        mustReturnLinkTimePairs.add(getMustReturnLinkTimePairFromPreviousNonStageActivity(plan, i));
                    }
                }
            }
            //add a time stamp representing the end of the day after the last taxi leg of the owner.
            // meaning that the vehicle has time until end of simulation to perform freight tours
            mustReturnLinkTimePairs.add(new PFAVehicle.MustReturnLinkTimePair(Double.POSITIVE_INFINITY, null));
            //just to be sure that list really is sorted. as we go through the plan consecutively, this should be unnecessary..
            Collections.sort(mustReturnLinkTimePairs);

            if (vehicleStartLink != null) {
                Id<DvrpVehicle> vehicleId = PFAVUtils.generatePFAVIdFromPersonId(p.getId());
                PFAVSpecification specification = PFAVSpecification.newBuilder()
                        .serviceBeginTime(0.)
                        .serviceEndTime(36 * 3600)
                        .startLinkId(vehicleStartLink)
                        .id(vehicleId)
                        .capacity(PFAVUtils.DEFAULT_PFAV_CAPACITY)
                        .mustReturnToOwnerLinkTimePairs(mustReturnLinkTimePairs)
                        .build();
                vehiclesForIteration.add(specification);
            }
        }
        return vehiclesForIteration;
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
                            //no it does not, so we need to calculate the must return time by computing end time of the activity element by element
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

    private PFAVehicle.MustReturnLinkTimePair getMustReturnLinkTimePairFromPreviousNonStageActivity(Plan plan, int startIndex) {
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
                    Double returnTime = null;
                    if (act.getEndTime() == Double.NEGATIVE_INFINITY) {
                        if (lastLeg.getDepartureTime() == Double.NEGATIVE_INFINITY) {
                            if (act.getMaximumDuration() == Double.NEGATIVE_INFINITY) {
                                throw new RuntimeException("cannot compute must return time for PFAVehicle of person " + plan.getPerson().getId() + " for activity " + act.toString());
                            }
                            //does the leg *before* the activity have a departure time and a travel time?
                            Leg legBeforeAct = (Leg) plan.getPlanElements().get(i - 1);
                            if (legBeforeAct.getDepartureTime() != Double.NEGATIVE_INFINITY && legBeforeAct.getRoute().getTravelTime() != Double.NEGATIVE_INFINITY) {
                                returnTime = legBeforeAct.getDepartureTime() + legBeforeAct.getRoute().getTravelTime() + act.getMaximumDuration();
                            } else {
                                //no it does not, so we need to calculate the must return time by computing end time of the activity element by element
                                returnTime = computeMustReturnTimeConsecutivelyFromTheStart(plan, i);
                            }
                        } else {
                            returnTime = lastLeg.getDepartureTime();
                        }
                    } else {
                        returnTime = act.getEndTime();
                    }
                    return new PFAVehicle.MustReturnLinkTimePair(returnTime, act.getLinkId());
                }
            }
        }
        throw new IllegalStateException("could not compute must return time for agent " + plan.getPerson().getId() + " before leg " + plan.getPlanElements().get(startIndex));
    }

    private double computeMustReturnTimeConsecutivelyFromTheStart(Plan plan, int i) {
        double time = ((Activity) plan.getPlanElements().get(0)).getEndTime();
        if (Double.isInfinite(time)) throw new IllegalStateException("end time of first activity of agent " + plan.getPerson().getId()
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

    class LinkTimePairComparator implements java.util.Comparator<LinkTimePair> {

        @Override
        public int compare(LinkTimePair arg1, LinkTimePair arg2) {
            return Double.compare(arg1.time, arg2.time);
        }
    }

}
