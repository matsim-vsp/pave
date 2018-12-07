/* *********************************************************************** *
 * project: org.matsim.*
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2018 by the members listed in the COPYING,        *
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
  
package org.matsim.run;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierCapabilities;
import org.matsim.contrib.freight.carrier.CarrierImpl;
import org.matsim.contrib.freight.carrier.CarrierPlan;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlWriterV2;
import org.matsim.contrib.freight.carrier.CarrierService;
import org.matsim.contrib.freight.carrier.CarrierShipment;
import org.matsim.contrib.freight.carrier.CarrierVehicle;
import org.matsim.contrib.freight.carrier.CarrierVehicleType;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypeLoader;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypes;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.carrier.TimeWindow;
import org.matsim.contrib.freight.carrier.CarrierCapabilities.FleetSize;
import org.matsim.contrib.freight.jsprit.MatsimJspritFactory;
import org.matsim.contrib.freight.jsprit.NetworkBasedTransportCosts;
import org.matsim.contrib.freight.jsprit.NetworkRouter;
import org.matsim.contrib.freight.jsprit.NetworkBasedTransportCosts.Builder;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.controler.OutputDirectoryLogging;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;
import org.matsim.vehicles.EngineInformationImpl;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.EngineInformation.FuelType;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.SchrimpfFactory;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.io.problem.VrpXMLWriter;

public class RunFreight {
	private static final Logger log = Logger.getLogger(RunFreight.class);

	
//	private static final String INPUT_DIR = "../../shared-svn/projects/freight/studies/MA_Turner-Kai/input/Grid_Szenario/" ; //TODO: Define INPUT
	final static URL context = ExamplesUtils.getTestScenarioURL("freight-chessboard-9x9"); //Scenario...
	
//	Config config = ConfigUtils.loadConfig(configFileName );
	private static final String OUTPUT_DIR = "output/runFreight/";
	private static final String LOG_DIR = OUTPUT_DIR + "Logs/";
	
	public static void main(String[] args) throws IOException {
		/*
		 * some preparation - set logging level
		 */
//		Logger.getRootLogger().setLevel(Level.DEBUG);
		Logger.getRootLogger().setLevel(Level.INFO);

		/*
		 * some preparation - create output folder
		 */
		OutputDirectoryLogging.initLoggingWithOutputDirectory(LOG_DIR);
		
		/*
		 * Some Preparation for MATSim 
		 */
		Config config = ConfigUtils.createConfig();
		config.setContext(context);
		config.network().setInputFile("grid9x9.xml");
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
		config.controler().setLastIteration(1);
		Scenario scenario = ScenarioUtils.createScenario(config);
			
		/*
		 * Prepare and run jasprit
		 */
		
		//Create carrier with services
		Carriers carriers = new Carriers() ;
		Carrier carrierWServices = CarrierImpl.newInstance(Id.create("carrier", Carrier.class));
		carrierWServices.getServices().add(createMatsimService("Service1", "i(3,9)", 2));
		carrierWServices.getServices().add(createMatsimService("Service2", "i(4,9)", 2));
		
		//Create vehicle and assign it to the Carrier
		CarrierVehicleType carrierVehType = createCarrierVehType();
		CarrierVehicleTypes vehicleTypes = new CarrierVehicleTypes() ;
		vehicleTypes.getVehicleTypes().put(carrierVehType.getId(), carrierVehType);
		
		CarrierVehicle carrierVehicle = CarrierVehicle.Builder.newInstance(Id.create("gridVehicle", org.matsim.vehicles.Vehicle.class), Id.createLinkId("i(6,0)")).setEarliestStart(0.0).setLatestEnd(36000.0).setTypeId(carrierVehType.getId()).build();
		CarrierCapabilities.Builder ccBuilder = CarrierCapabilities.Builder.newInstance() 
				.addType(carrierVehType)
				.addVehicle(carrierVehicle)
				.setFleetSize(FleetSize.INFINITE);				
		carrierWServices.setCarrierCapabilities(ccBuilder.build());

		// Add carrier to carriers
		carriers.addCarrier(carrierWServices);

		// assign vehicle types to the carriers
		new CarrierVehicleTypeLoader(carriers).loadVehicleTypes(vehicleTypes) ;

		//load Network and build netbasedCosts for jsprit
		Network network = NetworkUtils.createNetwork();
		new MatsimNetworkReader(network).readFile(IOUtils.newUrl(context ,"grid9x9.xml").getFile()); 
		Builder netBuilder = NetworkBasedTransportCosts.Builder.newInstance( network, vehicleTypes.getVehicleTypes().values() );
		final NetworkBasedTransportCosts netBasedCosts = netBuilder.build() ;
		netBuilder.setTimeSliceWidth(1800) ; // !!!!, otherwise it will not do anything.
		
		for (Carrier carrier : carriers.getCarriers().values()) {
			//Build VRP for jsprit
			VehicleRoutingProblem.Builder vrpBuilder = MatsimJspritFactory.createRoutingProblemBuilder(carrier, network);
			vrpBuilder.setRoutingCost(netBasedCosts) ;
			VehicleRoutingProblem problem = vrpBuilder.build();
	
				// get the algorithm out-of-the-box, search solution with jsprit and get the best one.
			VehicleRoutingAlgorithm algorithm = new SchrimpfFactory().createAlgorithm(problem);
			Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();
			VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);
	
				//Routing bestPlan to Network
			CarrierPlan carrierPlanServicesAndShipments = MatsimJspritFactory.createPlan(carrier, bestSolution) ;
			NetworkRouter.routePlan(carrierPlanServicesAndShipments,netBasedCosts) ;
			carrier.setSelectedPlan(carrierPlanServicesAndShipments) ;
			
			new VrpXMLWriter(problem, solutions).write(OUTPUT_DIR + "servicesAndShipments_solutions_" + carrier.getId().toString() + ".xml");
		}
		new CarrierPlanXmlWriterV2(carriers).write( OUTPUT_DIR + "servicesAndShipments_jsprit_plannedCarriers.xml") ; 
		
		
		//--------- now start a MATsim run:
		

		
		
		Controler controler = new Controler(scenario);
		
		controler.run();
		log.info("#### Finished ####");
		/*
		 * close logging
		 */
		OutputDirectoryLogging.closeOutputDirLogging();
	}

	/**
	 * Create vehicleType 
	 * @return CarrierVehicleType
	 */
	private static CarrierVehicleType createCarrierVehType() {
		CarrierVehicleType carrierVehType = CarrierVehicleType.Builder.newInstance(Id.create("gridType", VehicleType.class))
				.setCapacity(3)
				.setMaxVelocity(10)
				.setCostPerDistanceUnit(0.0001)
				.setCostPerTimeUnit(0.001)
				.setFixCost(130)
				.setEngineInformation(new EngineInformationImpl(FuelType.diesel, 0.015))
				.build();
		return carrierVehType;
	}
	
	private static CarrierShipment createMatsimShipment(String id, String from, String to, int size) {
		Id<CarrierShipment> shipmentId = Id.create(id, CarrierShipment.class);
		Id<Link> fromLinkId = null; 
		Id<Link> toLinkId= null;

		if(from != null ) {
			fromLinkId = Id.create(from, Link.class);
		} 
		if(to != null ) {
			toLinkId = Id.create(to, Link.class);
		}

		return CarrierShipment.Builder.newInstance(shipmentId, fromLinkId, toLinkId, size)
				.setDeliveryServiceTime(30.0)
				.setDeliveryTimeWindow(TimeWindow.newInstance(3600.0, 36000.0))
				.setPickupServiceTime(5.0)
				.setPickupTimeWindow(TimeWindow.newInstance(0.0, 7200.0))
				.build();
	}

	private static CarrierService createMatsimService(String id, String to, int size) {
		return CarrierService.Builder.newInstance(Id.create(id, CarrierService.class), Id.create(to, Link.class))
				.setCapacityDemand(size)
				.setServiceDuration(31.0)
				.setServiceStartTimeWindow(TimeWindow.newInstance(3601.0, 36001.0))
				.build();
	}

}
