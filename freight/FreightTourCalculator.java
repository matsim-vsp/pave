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

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierCapabilities.FleetSize;
import org.matsim.contrib.freight.carrier.CarrierPlan;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlReaderV2;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlWriterV2;
import org.matsim.contrib.freight.carrier.CarrierVehicleType;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypeLoader;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypeReader;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypeWriter;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypes;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.jsprit.MatsimJspritFactory;
import org.matsim.contrib.freight.jsprit.NetworkBasedTransportCosts;
import org.matsim.contrib.freight.jsprit.NetworkRouter;
import org.matsim.contrib.freight.jsprit.NetworkBasedTransportCosts.Builder;
import org.matsim.core.config.groups.TravelTimeCalculatorConfigGroup;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.trafficmonitoring.TravelTimeCalculator;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.SchrimpfFactory;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.util.Solutions;

import privateAV.PFAVUtils;

/**
 * @author martins-turner, tschlenther
 *
 * TODO:
 * make the logic of this class static again!?
 * or create a static run method that gets all the parameters needed
 *
 */
public class FreightTourCalculator {

	private static final Logger log = Logger.getLogger(FreightTourCalculator.class);
	
	private Network network;
	Carriers carriers;

	private int timeSlice = 1800;
	
	
	private static final String INPUT_NETWORK = "input/Scenarios/mielec/network.xml"; 
	
	public FreightTourCalculator(Network network, Carriers carriers, CarrierVehicleTypes vTypes) {
		this.network = network;
		this.carriers = carriers;
		log.info("loading carrier vehicle types..");
		new CarrierVehicleTypeLoader(this.carriers).loadVehicleTypes(vTypes) ;
		log.info("time slice is set to 1800");
	}
	
	public FreightTourCalculator(Network network, String inputCarriersPlanFile, String inputVehicleTypesFile) {
		this.network = network;
		carriers = new Carriers();
		CarrierPlanXmlReaderV2 reader = new CarrierPlanXmlReaderV2(carriers);
		reader.readFile(inputCarriersPlanFile);
		log.info("loading carrier vehicle types..");
		new CarrierVehicleTypeLoader(carriers).loadVehicleTypes(readVehicleTypes(inputVehicleTypesFile));
		log.info("time slice is set to 1800");
	}
	
	private CarrierVehicleTypes readVehicleTypes(String input) {
		CarrierVehicleTypes vTypes = new CarrierVehicleTypes();
		CarrierVehicleTypeReader reader = new CarrierVehicleTypeReader(vTypes);
		reader.readFile(input);
		return vTypes;
	}
	
	public Carriers run(TravelTime travelTime) {
		
		Builder netBuilder = NetworkBasedTransportCosts.Builder.newInstance( this.network, CarrierVehicleTypes.getVehicleTypes(carriers).getVehicleTypes().values() );
		netBuilder.setTimeSliceWidth(timeSlice) ; // !!!!, otherwise it will not do anything.
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
			
				//calculate the route
			NetworkRouter.routePlan(carrierPlanServicesAndShipments,netBasedCosts) ;
			
			carrier.setSelectedPlan(carrierPlanServicesAndShipments) ;
			
		}
		return carriers;
	}
	
	public void writeCarriers(String outputDir) {
		CarrierPlanXmlWriterV2 planwriter = new CarrierPlanXmlWriterV2(carriers);
		planwriter.write(outputDir);
	}
	
	public Carriers getCarriers() {
		return this.carriers;
	}
	
	public int getTimeSlice() {
		return this.timeSlice;
	}
	
	public void setNetwork(Network network) {
		this.network = network;
	}
	
	public void setTimeSlice(int timeSlice) {
		this.timeSlice = timeSlice;
	}
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		Network network = NetworkUtils.createNetwork();
		new MatsimNetworkReader(network).readFile(INPUT_NETWORK);
		
		CarrierVehicleType privateAVCarrierVehType = FreightSetUp.createPrivateFreightAVVehicleType(); 
		CarrierVehicleTypes vTypes = new CarrierVehicleTypes();
		vTypes.getVehicleTypes().put(privateAVCarrierVehType.getId(), privateAVCarrierVehType);
		
		
		
		int nrOfCarriers = 2;
		int nrOfVehPerCarrierPerVehType = 3;
		
		Carriers carriers = FreightSetUp.createCarriersWithRandomDepotAnd10Services(vTypes.getVehicleTypes().values(), FleetSize.FINITE, network, nrOfCarriers, nrOfVehPerCarrierPerVehType);
		
		FreightTourCalculator calculator = new FreightTourCalculator(network, carriers, vTypes);
		
		calculator.run(new TravelTimeCalculator(network, new TravelTimeCalculatorConfigGroup()).getLinkTravelTimes());
		calculator.writeCarriers(PFAVUtils.DEFAULT_CARRIERS_FILE);
		new CarrierVehicleTypeWriter(vTypes).write(PFAVUtils.DEFAULT_VEHTYPES_FILE);
	}

}
