package privateAV.modules;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.*;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.fleet.FleetSpecification;
import org.matsim.contrib.dvrp.fleet.FleetStatsCalculator;
import org.matsim.core.controler.events.BeforeMobsimEvent;
import org.matsim.core.controler.listener.BeforeMobsimListener;
import privateAV.PFAVUtils;
import privateAV.vehicle.PFAVSpecification;

import java.util.*;

public class PFAVFleetStatsCalculator implements FleetStatsCalculator, BeforeMobsimListener {

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
        List<PFAVSpecification> vehiclesForIteration = new ArrayList<>();
        for(Person p : this.population.getPersons().values()) {
            Id<Link> vehicleStartLink = null;
            Queue<Double>  actEndTimesAfterModeLegs = new LinkedList<>();

            Plan plan = p.getSelectedPlan();
            for (int i = 0; i < plan.getPlanElements().size(); i++) {
                PlanElement pe = plan.getPlanElements().get(i);
                if (pe instanceof Leg) {
                    if (((Leg) pe).getMode().equals(mode)) {
                        if(vehicleStartLink == null) vehicleStartLink = ((Leg) pe).getRoute().getStartLinkId();
                        actEndTimesAfterModeLegs.add(((Activity) plan.getPlanElements().get(i+1)).getEndTime());
                    }
                }
            }
            if (vehicleStartLink != null) { //we need a PFAVehicle
                Id<DvrpVehicle> vehicleId = Id.create(p.getId().toString() + "_av", DvrpVehicle.class);
                PFAVSpecification specification = PFAVSpecification.newBuilder()
                        .serviceBeginTime(0.)
                        .serviceEndTime(36 * 3600)
                        .startLinkId(vehicleStartLink)
                        .id(vehicleId)
                        .capacity(PFAVUtils.DEFAULT_PFAV_CAPACITY)
                        .actEndTimes(actEndTimesAfterModeLegs)
                        .build();
                vehiclesForIteration.add(specification);
            }
        }
        for(PFAVSpecification sp : vehiclesForIteration){
            if(oldVehicles.contains(sp.getId())){
                fleetSpecification.replaceVehicleSpecification(sp);
                oldVehicles.remove(sp.getId());
            } else {
                fleetSpecification.addVehicleSpecification(sp);
            }
        }
        //delete all vehicle specifications that are no longer needed (agents try out different mode)
        for(Id<DvrpVehicle> id : oldVehicles){
            fleetSpecification.removeVehicleSpecification(id);
        }
    }

    @Override
    public void updateStats(Fleet fleet) {
        oldVehicles.clear();
        oldVehicles.addAll(fleet.getVehicles().keySet());
//        oldVehicles = fleet.getVehicles()
//                .values()
//                .stream()
//                .map(v -> ImmutableDvrpVehicleSpecification.newBuilder()
//                        .id(v.getId())
//                        .startLinkId(Schedules.getLastLinkInSchedule(v).getId())
//                        .capacity(v.getCapacity())
//                        .serviceBeginTime(v.getServiceBeginTime())
//                        .serviceEndTime(v.getServiceEndTime())
//                        .build())
//                .collect(Collectors.toList());
    }

}
