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

import com.graphhopper.jsprit.analysis.toolbox.Plotter;
import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.SchrimpfFactory;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.io.problem.VrpXMLWriter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.carrier.CarrierCapabilities.FleetSize;
import org.matsim.contrib.freight.jsprit.MatsimJspritFactory;
import org.matsim.contrib.freight.jsprit.NetworkBasedTransportCosts;
import org.matsim.contrib.freight.jsprit.NetworkBasedTransportCosts.Builder;
import org.matsim.contrib.freight.jsprit.NetworkRouter;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.controler.OutputDirectoryLogging;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.router.costcalculators.FreespeedTravelTimeAndDisutility;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.trafficmonitoring.FreeSpeedTravelTime;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;
import org.matsim.utils.leastcostpathtree.LeastCostPathTree;
import org.matsim.vehicles.EngineInformation.FuelType;
import org.matsim.vehicles.EngineInformationImpl;
import org.matsim.vehicles.VehicleType;

import ovgu.pave.core.Core;
import ovgu.pave.handler.Handler;
import ovgu.pave.handler.modelHandler.InputHandler;
import ovgu.pave.model.input.Edge;
import ovgu.pave.model.input.FirstRequestActivity;
import ovgu.pave.model.input.Input;
import ovgu.pave.model.input.InputFactory;
import ovgu.pave.model.input.Location;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.input.Requests;
import ovgu.pave.model.input.SecondRequestActivity;
import ovgu.pave.model.input.Vehicle;
import ovgu.pave.model.input.impl.LocationImpl;
import ovgu.pave.model.solution.RouteElement;

import java.io.IOException;
import java.net.URL;
import java.util.*;
//import ovgu.data.entity.RouteElement;
//import ovgu.utilities.DistanceMatrix;
//import ovgu.utilities.RouteHandler;
//import ovgu.utilities.Settings;
//import ovgu.vrptw.vrpSolver;

class RunFreight {
	/*
	 * todos:<ul> <li> do not overwrite output dir by matsim </li>
	 */
	private static final Logger log = Logger.getLogger(RunFreight.class);

	enum Optim {
		jsprit, ovgu
	}

	final static Optim optim = Optim.ovgu;

	private static URL scenarioUrl;
	static {
		scenarioUrl = ExamplesUtils.getTestScenarioURL("freight-chessboard-9x9");
	}

	// Config config = ConfigUtils.loadConfig(configFileName );

	public static void main(String[] args) throws IOException {
		/*
		 * some preparation - set logging level
		 */
		//		Logger.getRootLogger().setLevel(Level.DEBUG);
		Logger.getRootLogger().setLevel(Level.DEBUG);

		/*
		 * Some Preparation for MATSim
		 */
		Config config = prepareConfig();
		Scenario scenario = ScenarioUtils.createScenario(config);
		
		OutputDirectoryLogging.initLoggingWithOutputDirectory(config.controler().getOutputDirectory() +"/Logs");

		//Create carrier with services
		Carriers carriers = new Carriers() ;
		Carrier carrierWShipments = CarrierImpl.newInstance(Id.create("carrier", Carrier.class));
		//TODO: Geht derzeit nur als "int" für ovgu... kmt/aug19
		carrierWShipments.getShipments().add(createMatsimShipment("1", "i(6,0)", "i(3,9)R", 2));
		carrierWShipments.getShipments().add(createMatsimShipment("2", "i(6,0)", "i(4,9)R", 2));

		//Create vehicle type
		CarrierVehicleType carrierVehType = createCarrierVehType();
		CarrierVehicleTypes vehicleTypes = new CarrierVehicleTypes() ;
		vehicleTypes.getVehicleTypes().put(carrierVehType.getId(), carrierVehType);

		//create vehicle
		final Id<Link> depotLinkId = Id.createLinkId( "i(6,0)" );
		CarrierVehicle carrierVehicle = CarrierVehicle.Builder.newInstance(Id.create("gridVehicle", org.matsim.vehicles.Vehicle.class ),
			  depotLinkId ).setEarliestStart(0.0 ).setLatestEnd(36000.0 ).setTypeId(carrierVehType.getId() ).build();
		
		
		// capabilities -> assign vehicles or vehicle types to carrier
		CarrierCapabilities.Builder ccBuilder = CarrierCapabilities.Builder.newInstance()
													 .addType(carrierVehType)
													 .addVehicle(carrierVehicle)
													 .setFleetSize(FleetSize.FINITE);
		carrierWShipments.setCarrierCapabilities(ccBuilder.build());

		// Add carrier to carriers
		carriers.addCarrier(carrierWShipments);

		// load vehicle types for the carriers
		new CarrierVehicleTypeLoader(carriers).loadVehicleTypes(vehicleTypes) ;

		//load Network and build netbasedCosts
		Network network = NetworkUtils.createNetwork();
		new MatsimNetworkReader(network).readURL(IOUtils.newUrl(scenarioUrl ,"grid9x9.xml"));

		new CarrierPlanXmlWriterV2( carriers ).write( config.controler().getOutputDirectory() + "/carriers-wo-plans.xml" );
		new CarrierVehicleTypeWriter( CarrierVehicleTypes.getVehicleTypes( carriers ) ).write( config.controler().getOutputDirectory() + "/carrierTypes.xml" );

		// matrix costs between locations (cost matrix)
		Builder netBuilder = NetworkBasedTransportCosts.Builder.newInstance( network, vehicleTypes.getVehicleTypes().values() );
		final NetworkBasedTransportCosts netBasedCosts = netBuilder.build() ;
		
		
		// time dependent network (1800 = 30 min) --> (option live request)
		netBuilder.setTimeSliceWidth(1800) ; // !!!!, otherwise it will not do anything.

		switch( optim ) {
			case jsprit:
				log.info("Starting with jsprit algorithm");
				/*
				 * Prepare and run jsprit
				 */

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

					new VrpXMLWriter(problem, solutions).write(config.controler().getOutputDirectory()+ "/servicesAndShipments_solutions_" + carrier.getId().toString() + ".xml");
					new Plotter( problem, bestSolution ).plot( config.controler().getOutputDirectory()+ "/solution_" + carrier.getId().toString() + ".png", carrier.getId().toString() );
				}
				break;
			case ovgu:
				log.info("Starting with OVGU algorithm");
				for ( Carrier carrier : carriers.getCarriers().values() ){
					Core core = new Core();
					
					//load default config
					core.initConfig("./scenarios/ovgu/defaultConfig.xml");
					
					Input input = InputFactory.eINSTANCE.createInput();
					
					Requests requests = InputFactory.eINSTANCE.createRequests();
					
					//Map ID<Link>, ovguLocation ?
					HashMap<Id<Link>, Integer> locationToLink= new HashMap<Id<Link>, Integer>();

					log.info("prepare carrierShipments/requests");
					for (CarrierShipment carrierShipment :carrier.getShipments()) {
						//TODO: OVGU hat integer als Ids. -> Übersetzungstabelle bauen. kmt/aug19
						//TODO: OVGU hat integer als Ids für Locations. -> Übersetzungstabelle bauen zu den Link Ids. kmt/aug19
						Location firstActivityLocation = getOVGULocation(locationToLink, carrierShipment.getFrom(), network, input);
						Location secondActivityLocation = getOVGULocation(locationToLink, carrierShipment.getTo(), network, input);
						Request request = InputHandler.createRequest(Integer.parseInt(carrierShipment.getId().toString()), firstActivityLocation, secondActivityLocation, carrierShipment.getSize());
						
						request.setPredicted(true); //da "offline" sind die Anfragen bereits bekannt (= true).
						request.setRequestTime(0); //da "offline" kommen Anfragen alle zur Sekunde 0 an ;) // sollte aber irgendwo auch alleine so gesetzt werden... kmt/aug19

						//From
						//OVGU hat Millisekunden TODO: Testen ob es auch alles in Sekunden geht.);
						request.getFirstActivity().setEarliestArrival((long) carrierShipment.getPickupTimeWindow().getStart()*1000);
						request.getFirstActivity().setLatestArrival((long) carrierShipment.getPickupTimeWindow().getEnd()*1000); 
						request.getFirstActivity().setServiceDuration((long) carrierShipment.getPickupServiceTime() *1000); 
						
						//To
						//OVGU hat Millisekunden TODO: Testen ob es auch alles in Sekunden geht.);
						request.getSecondActivity().setEarliestArrival((long) carrierShipment.getDeliveryTimeWindow().getStart()*1000); 
						request.getSecondActivity().setLatestArrival((long) carrierShipment.getDeliveryTimeWindow().getEnd()*1000);
						request.getSecondActivity().setServiceDuration((long) carrierShipment.getDeliveryServiceTime() *1000);

						requests.getNew().add(request);
						
					}					
					input.setRequests(requests);
					
					
					log.info("prepare vehicles / vehicle types");
					HashMap<Id<org.matsim.vehicles.Vehicle>, Integer> matSimToOVGUVehicle= new HashMap<Id<org.matsim.vehicles.Vehicle>, Integer>();
					HashMap<Id<org.matsim.vehicles.VehicleType>, Integer> matSimToOVGUVehicleType= new HashMap<Id<org.matsim.vehicles.VehicleType>, Integer>();
					
					//TODO Fahrzeuge, Fahrzeugtypen übergeben/erstellen
					if (carrier.getCarrierCapabilities().getFleetSize() == FleetSize.INFINITE) {
						log.fatal("Not implemented", new RuntimeException()); //Derzeit auch bei OVGU nicht drin.
					} else if  (carrier.getCarrierCapabilities().getFleetSize() == FleetSize.FINITE) {
						for (CarrierVehicle cVehicle : carrier.getCarrierCapabilities().getCarrierVehicles()) {
							if (!matSimToOVGUVehicle.containsKey(cVehicle.getVehicleId())){
								matSimToOVGUVehicle.put(cVehicle.getVehicleId(), matSimToOVGUVehicle.size());
							}
							if (!matSimToOVGUVehicleType.containsKey(cVehicle.getVehicleTypeId())){
								matSimToOVGUVehicleType.put(cVehicle.getVehicleTypeId(), matSimToOVGUVehicleType.size());
							}
							if (!locationToLink.containsKey(cVehicle.getLocation())){
								locationToLink.put(cVehicle.getLocation(), locationToLink.size());
							}
							log.debug(locationToLink.get(cVehicle.getLocation()));
							log.debug(network.getLinks().get(cVehicle.getLocation()).getCoord().getX());
							log.debug(network.getLinks().get(cVehicle.getLocation()).getCoord().getY());
							Location depot = InputHandler.createLocation(locationToLink.get(cVehicle.getLocation()), network.getLinks().get(cVehicle.getLocation()).getCoord().getX(), network.getLinks().get(cVehicle.getLocation()).getCoord().getY());
							ovgu.pave.model.input.VehicleType vehicleType = InputHandler.createVehicleType(matSimToOVGUVehicleType.get(cVehicle.getVehicleTypeId()), cVehicle.getVehicleType().getCarrierVehicleCapacity()); //TODO: Eigentlich nur, wenn noch nicht existent.
							Vehicle ovguVehicle = InputHandler.createVehicle(matSimToOVGUVehicle.get(cVehicle.getVehicleId()), vehicleType, depot, depot);
							
							//add Vehicle to VRP
							input.getVehicleTypes().add(vehicleType);
							input.getVehicles().add(ovguVehicle);
							
						}
					} else {
						log.fatal("Missing FleetSize defintion", new RuntimeException());
					}
					
					log.info("prepare network");
					//TODO Netzwerk übergeben/erstellen
					ovgu.pave.model.network.Network ovguNetwork = null;

					
					log.info("run algorithm");
					core.initInput(input);
//					InputHandler ih = new InputHandler();
//					ih.setInput(input);
//					ih.saveInput("test.xml");
					core.initNetwork(ovguNetwork);
					core.run();
					log.info("handle alg solution");
					core.getSolution();
					
					//TODO Ergebnis zurück übersetzen
				}

		}

		new CarrierPlanXmlWriterV2(carriers).write( config.controler().getOutputDirectory()+ "/servicesAndShipments_plannedCarriers.xml") ;


		//--------- now start a MATsim run:




//		Controler controler = new Controler(scenario);

//		controler.run();

		log.info("#### Finished ####");

	}
	
	private static ovgu.pave.model.input.VehicleType getOVGUVehicleType(HashMap<Id<org.matsim.vehicles.VehicleType>, Integer> matSimToOVGUVehicleType, Id<org.matsim.vehicles.VehicleType> vehicleType, Input input, int capacity){
		boolean isNew = false;

		if (!matSimToOVGUVehicleType.containsKey(vehicleType)){
			matSimToOVGUVehicleType.put(vehicleType, matSimToOVGUVehicleType.size());
			isNew=true;
		}

		ovgu.pave.model.input.VehicleType ovguVehicleType = InputHandler.createVehicleType(matSimToOVGUVehicleType.get(vehicleType), capacity); //TODO: Eigentlich nur, wenn noch nicht existent.
		
		if (isNew)
			input.getVehicleTypes().add(ovguVehicleType);
		
		return ovguVehicleType;
	}
	
	private static Vehicle getOVGUVehicle(HashMap<Id<org.matsim.vehicles.Vehicle>, Integer> matSimToOVGUVehicle, Id<org.matsim.vehicles.Vehicle> vehicle, Input input, ovgu.pave.model.input.VehicleType vehicleType, Location startLocation, Location endLocation){
		boolean isNew = false;

		if (!matSimToOVGUVehicle.containsKey(vehicle)){
			matSimToOVGUVehicle.put(vehicle, matSimToOVGUVehicle.size());
			isNew = true;
		}

		Vehicle ovguVehicle = InputHandler.createVehicle(matSimToOVGUVehicle.get(vehicle), vehicleType, startLocation, endLocation);
		
		if (isNew)
			input.getVehicles().add(ovguVehicle);
		
		return ovguVehicle;
	}
	
	private static Location getOVGULocation(HashMap<Id<Link>, Integer> locationToLink, Id<Link> link, Network network, Input input){
		boolean isNew = false;

		if (!locationToLink.containsKey(link)){
			locationToLink.put(link, locationToLink.size());
			isNew = true;
		}

		Location location = InputHandler.createLocation(locationToLink.get(link), network.getLinks().get(link).getCoord().getX(), network.getLinks().get(link).getCoord().getY());
		
		if (isNew)
			input.getLocations().add(location);
		
		return location;
	}

	/**
	 * @return
	 */
	private static Config prepareConfig() {
		Config config = ConfigUtils.createConfig();
		config.setContext(scenarioUrl);
		config.network().setInputFile("grid9x9.xml");

		config.controler().setOutputDirectory("./output/freight");
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
		new OutputDirectoryHierarchy(config.controler().getOutputDirectory(), config.controler().getRunId(),
				config.controler().getOverwriteFileSetting());
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.overwriteExistingFiles);
		// (the directory structure is needed for jsprit output, which is before the
		// controler starts. Maybe there is a better alternative ...)

		config.global().setRandomSeed(4177);

		config.controler().setLastIteration(1);
		return config;
	}

	/**
	 * Create vehicleType
	 * 
	 * @return CarrierVehicleType
	 */
	private static CarrierVehicleType createCarrierVehType() {
		CarrierVehicleType carrierVehType = CarrierVehicleType.Builder
				.newInstance(Id.create("gridType", VehicleType.class)).setCapacity(3).setMaxVelocity(10) // m/s
				.setCostPerDistanceUnit(0.0001).setCostPerTimeUnit(0.001).setFixCost(130)
				.setEngineInformation(new EngineInformationImpl(FuelType.diesel, 0.015)).build();
		return carrierVehType;
	}

	private static CarrierShipment createMatsimShipment(String id, String from, String to, int size) {
		Id<CarrierShipment> shipmentId = Id.create(id, CarrierShipment.class);
		Id<Link> fromLinkId = null;
		Id<Link> toLinkId = null;

		if (from != null) {
			fromLinkId = Id.create(from, Link.class);
		}
		if (to != null) {
			toLinkId = Id.create(to, Link.class);
		}

		return CarrierShipment.Builder.newInstance(shipmentId, fromLinkId, toLinkId, size).setDeliveryServiceTime(30.0)
				.setDeliveryTimeWindow(TimeWindow.newInstance(3600.0, 36000.0)).setPickupServiceTime(5.0)
				.setPickupTimeWindow(TimeWindow.newInstance(0.0, 7200.0)).build();
	}

	private static CarrierService createMatsimService(String id, String to, int size) {
		return CarrierService.Builder.newInstance(Id.create(id, CarrierService.class), Id.create(to, Link.class))
				.setCapacityDemand(size).setServiceDuration(31.0)
				.setServiceStartTimeWindow(TimeWindow.newInstance(3601.0, 36001.0)).build();
	}

}
