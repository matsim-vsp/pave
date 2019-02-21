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
import org.matsim.contrib.freight.jsprit.NetworkRouter;
import org.matsim.core.router.util.TravelTime;

import java.util.Collection;

/**
 * @author martins-turner, tschlenther
 *
 * TODO: make the logic of this class static again!?
 *
 */
public class FreightTourCalculatorImpl implements FreightTourCalculator {

    private static final Logger log = Logger.getLogger(FreightTourCalculatorImpl.class);

    private int timeSlice = 1800;

	public Carriers runTourPlanningForCarriers(Carriers carriers, CarrierVehicleTypes vehicleTypes, Network network, TravelTime travelTime) {
        log.info("time slice is set to " + this.timeSlice);

		Builder netBuilder = NetworkBasedTransportCosts.Builder.newInstance( network, vehicleTypes.getVehicleTypes().values() );
        netBuilder.setTimeSliceWidth(timeSlice); // !!!! otherwise it will not do anything.
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
			CarrierPlan carrierPlan = MatsimJspritFactory.createPlan(carrier, bestSolution);


			/* calculate the route - we need this because otherwise we only have the duration of the service task and do not have a clue about tour duration
			 * BUT: the routes themselves cannot be used later. please also see ConvertFreightForDvrp.convertToList()
			 */

			//if we use this default method, a router is created by leastCostPathCalculatorFactory.createPathCalculator(network, travelDisutility, travelTime);
			NetworkRouter.routePlan(carrierPlan, netBasedCosts);

			carrier.setSelectedPlan(carrierPlan);
			
		}
		return carriers;
	}

    public int getTimeSlice() {
        return this.timeSlice;
    }

    public void setTimeSlice(int timeSlice) {
        this.timeSlice = timeSlice;
	}

}
