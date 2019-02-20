package freight;

import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypes;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelTime;

public interface FreightTourCalculator {

    Carriers runTourPlanningForCarriers(Carriers carriers, CarrierVehicleTypes vehicleTypes, Network network, TravelTime travelTime);

    void routeCarriersSelectedPlans(Carriers carriers, LeastCostPathCalculator router, Network network, TravelTime travelTime);

}
