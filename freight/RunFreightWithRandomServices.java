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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.plaf.basic.BasicTreeUI.CellEditorHandler;

import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierCapabilities.FleetSize;
import org.matsim.contrib.freight.carrier.CarrierPlan;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlWriterV2;
import org.matsim.contrib.freight.carrier.CarrierVehicleType;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypeLoader;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypes;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.jsprit.MatsimJspritFactory;
import org.matsim.contrib.freight.jsprit.NetworkBasedTransportCosts;
import org.matsim.contrib.freight.jsprit.NetworkRouter;
import org.matsim.contrib.freight.jsprit.NetworkBasedTransportCosts.Builder;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.SchrimpfFactory;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.util.Solutions;

/**
 * @author martins-turner, tschlenther
 *
 */
public class RunFreightWithRandomServices {

	private static final String INPUT_NETWORK = "C:/TU Berlin/MasterArbeit/input/Scenarios/mielec/network.xml"; 
	private static final String OUTPUT_CARRIERS_PLANS = "C:/TU Berlin/MasterArbeit/input/Scenarios/mielec/freight/carrierPlans_routed_finite2.xml";
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Network network = NetworkUtils.createNetwork();
		new MatsimNetworkReader(network).readFile(INPUT_NETWORK);
		
		
		List<CarrierVehicleType> vehTypes = new ArrayList<CarrierVehicleType>();
		CarrierVehicleType privateAVCarrierVehType = FreightSetUp.createPrivateFreightAVVehicleType(); 
		vehTypes.add(privateAVCarrierVehType);
		
		int nrOfCarriers = 2;
		int nrOfVehPerCarrierPerVehType = 3;
		
		Carriers carriers = FreightSetUp.createCarriersWithRandomDepotAnd10Services(vehTypes, FleetSize.FINITE, network, nrOfCarriers, nrOfVehPerCarrierPerVehType);
		
		//no need to do this since i did in my FreightSetup-helper-class .... but should be aware of
//		new CarrierVehicleTypeLoader(carriers).loadVehicleTypes(new CarrierVehicleTypes()) ;
		
		
		Builder netBuilder = NetworkBasedTransportCosts.Builder.newInstance( network, vehTypes );
		final NetworkBasedTransportCosts netBasedCosts = netBuilder.build() ;
		netBuilder.setTimeSliceWidth(1800) ; // !!!!, otherwise it will not do anything.
		

		
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
			
				//calculate the route
			NetworkRouter.routePlan(carrierPlanServicesAndShipments,netBasedCosts) ;
			
			carrier.setSelectedPlan(carrierPlanServicesAndShipments) ;
			
		}
		
		CarrierPlanXmlWriterV2 planwriter = new CarrierPlanXmlWriterV2(carriers);
		planwriter.write(OUTPUT_CARRIERS_PLANS);
		
		
	}

}
