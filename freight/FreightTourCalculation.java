/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package freight;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.SchrimpfFactory;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.util.Solutions;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierPlan;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypes;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.jsprit.MatsimJspritFactory;
import org.matsim.contrib.freight.jsprit.NetworkBasedTransportCosts;
import org.matsim.contrib.freight.jsprit.NetworkBasedTransportCosts.Builder;
import org.matsim.contrib.freight.router.TimeAndSpacePlanRouter;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelTime;

import java.util.Collection;

/**
 * @author martins-turner, tschlenther
 *
 */
public class FreightTourCalculation {

	private static final Logger log = Logger.getLogger(FreightTourCalculation.class);

	private static final int TIMESLICE = 1800;

	public static Carriers runTourPlanningForCarriers(Carriers carriers, CarrierVehicleTypes vehicleTypes, Network network, TravelTime travelTime) {
		log.info("time slice is set to " + TIMESLICE);

		Builder netBuilder = NetworkBasedTransportCosts.Builder.newInstance( network, vehicleTypes.getVehicleTypes().values() );
		netBuilder.setTimeSliceWidth(TIMESLICE); // !!!! otherwise it will not do anything.
		netBuilder.setTravelTime(travelTime);
		final NetworkBasedTransportCosts netBasedCosts = netBuilder.build() ;

		for (Carrier carrier : carriers.getCarriers().values()) {
			//Build VRP
			VehicleRoutingProblem.Builder vrpBuilder = MatsimJspritFactory.createRoutingProblemBuilder(carrier, network);
			vrpBuilder.setRoutingCost(netBasedCosts) ;
			VehicleRoutingProblem problem = vrpBuilder.build();
	
				// get the algorithm out-of-the-box, search solution and get the best one.
			VehicleRoutingAlgorithm algorithm = new SchrimpfFactory().createAlgorithm(problem);
			Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();
			VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
	
				//get the CarrierPlan
			CarrierPlan carrierPlanServicesAndShipments = MatsimJspritFactory.createPlan(carrier, bestSolution) ;


			//TODO: we don't need to route the plan, do we?
			//TODO: here, another route is used than for the PFAVehicles.. this leads to different travel times: fix this!!
			//calculate the route
//			NetworkRouter.routePlan(carrierPlanServicesAndShipments,netBasedCosts) ;

//			//let's use the router that we hand through all the way from the scheduler over the freightTourManager to here
//			new TimeAndSpacePlanRouter(router, network, travelTime).run(carrierPlanServicesAndShipments);

			carrier.setSelectedPlan(carrierPlanServicesAndShipments) ;
			
		}
		return carriers;
	}

	public static void routeCarriersSelectedPlans(Carriers carriers, LeastCostPathCalculator router, Network network, TravelTime travelTime) {
		for (Carrier carrier : carriers.getCarriers().values()) {
			new TimeAndSpacePlanRouter(router, network, travelTime).run(carrier.getSelectedPlan());
		}
	}

}
