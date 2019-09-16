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
import com.graphhopper.jsprit.core.problem.job.Shipment;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity.JobActivity;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.io.problem.VrpXMLWriter;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.carrier.CarrierCapabilities.FleetSize;
import org.matsim.contrib.freight.carrier.Tour.Leg;
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
import org.matsim.core.population.PersonUtils;
import org.matsim.core.router.Dijkstra;
import org.matsim.core.router.DijkstraFactory;
import org.matsim.core.router.costcalculators.FreespeedTravelTimeAndDisutility;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.trafficmonitoring.FreeSpeedTravelTime;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;
import org.matsim.utils.leastcostpathtree.LeastCostPathTree;
import org.matsim.vehicles.EngineInformation.FuelType;
import org.matsim.vehicles.EngineInformationImpl;
import org.matsim.vehicles.VehicleImpl;
import org.matsim.vehicles.VehicleType;

import org.matsim.vehicles.VehicleUtils;
import ovgu.pave.core.Core;
import ovgu.pave.handler.Handler;
import ovgu.pave.handler.modelHandler.InputHandler;
import ovgu.pave.model.input.Edge;
import ovgu.pave.model.input.FirstRequestActivity;
import ovgu.pave.model.input.Input;
import ovgu.pave.model.input.InputFactory;
import ovgu.pave.model.input.Location;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.input.RequestActivity;
import ovgu.pave.model.input.Requests;
import ovgu.pave.model.input.SecondRequestActivity;
import ovgu.pave.model.input.Vehicle;
import ovgu.pave.model.input.impl.LocationImpl;
import ovgu.pave.model.solution.RequestActivityRouteElement;
import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.RouteElement;
import ovgu.pave.model.solution.Solution;
import ovgu.pave.model.solution.SupportRouteElement;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import javax.management.RuntimeErrorException;
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
	private static HashMap<Id<Link>, Integer> matsimLinkIdToOVGULocationID= new HashMap<Id<Link>, Integer>();
	private static HashMap<Integer, Id<Link>> OVGULocationIdToMatsimLinkId= new HashMap<Integer, Id<Link>>();


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
		Logger.getRootLogger().setLevel(Level.DEBUG);
		//		Logger.getRootLogger().setLevel(Level.INFO);

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
				clearMaps();
				Core core = new Core();

				//load default config
				core.initConfig("./scenarios/ovgu/defaultConfig.xml");

				Input input = InputFactory.eINSTANCE.createInput();

				Requests requests = InputFactory.eINSTANCE.createRequests();

				log.info("prepare carrierShipments/requests");
				for (CarrierShipment carrierShipment : carrier.getShipments()) {
					//TODO: OVGU hat integer als Ids. -> Übersetzungstabelle bauen. kmt/aug19
					//TODO: OVGU hat integer als Ids für Locations. -> Übersetzungstabelle bauen zu den Link Ids. kmt/aug19
					Location firstActivityLocation = getOVGULocation(carrierShipment.getFrom(), network, input);
					Location secondActivityLocation = getOVGULocation(carrierShipment.getTo(), network, input);
					Request request = InputHandler.createRequest(Integer.parseInt(carrierShipment.getId().toString()), firstActivityLocation, secondActivityLocation, carrierShipment.getSize());

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

						Location depot = getOVGULocation(cVehicle.getLocation(), network, input);
						ovgu.pave.model.input.VehicleType vehicleType = getOVGUVehicleType(matSimToOVGUVehicleType, cVehicle.getVehicleTypeId(), input, cVehicle.getVehicleType().getCarrierVehicleCapacity());//TODO: Eigentlich nur, wenn noch nicht existent.
						Vehicle ovguVehicle = getOVGUVehicle(matSimToOVGUVehicle, cVehicle.getVehicleId(), input, vehicleType, depot, depot);


					}
				} else {
					log.fatal("Missing FleetSize defintion", new RuntimeException());
				}

				log.info("prepare network");
				//TODO Netzwerk übergeben/erstellen
				ovgu.pave.model.network.Network ovguNetwork = null;

				//TODO: Haben derzeit nur ein beliebeigen FZGTyp und ein beliebiges Fahrzeug!!! kmt/aug19
				CarrierVehicleType vehTypeOne = (CarrierVehicleType) carrier.getCarrierCapabilities().getVehicleTypes().toArray()[0];
				CarrierVehicle vehOne = (CarrierVehicle) carrier.getCarrierCapabilities().getCarrierVehicles().toArray()[0];
				org.matsim.vehicles.Vehicle vehicleOne = new VehicleImpl(vehOne.getVehicleId(), vehOne.getVehicleType());
				log.debug("maxVelocityOf vehicleOne: " + vehicleOne.getType().getMaximumVelocity());

				//				double costPerMeter = vehTypeOne.getVehicleCostInformation().getPerDistanceUnit();
				//				double costPerSecond = vehTypeOne.getVehicleCostInformation().getPerTimeUnit();

				//				FreespeedTravelTimeAndDisutility ttCostCalculator = new FreespeedTravelTimeAndDisutility(costPerMeter, costPerSecond, costPerSecond); // TODO: Welchen nehmen wir hier eigentlich -> in freight schauen kmt/aug19
				//				DijkstraFactory dijkstraFactory = new DijkstraFactory();
				//				LeastCostPathCalculator costCalculator = dijkstraFactory.createPathCalculator(network, ttCostCalculator , ttCostCalculator); //Abfrage erstmal für um 8

				//TODO: Das nachfolgende funktioniert. Ist aber derzeit nur a) die Freespeed TravelTime und b)Fzg-Typ/Eigenschaften UNABHÄNGIG
				//calculate travel time
				List<Id<Link>> listOfLinkIds = new ArrayList<>() ;
				for( Id<Link> linkId : OVGULocationIdToMatsimLinkId.values() ){
					listOfLinkIds.add(linkId) ;
				}
				Collections.sort( listOfLinkIds ) ;

				TravelTime tt = new FreeSpeedTravelTime();
				TravelDisutility tc = new FreespeedTravelTimeAndDisutility( config.planCalcScore() );
				LeastCostPathTree tree = new LeastCostPathTree( tt, tc );

				for(Location from : input.getLocations()) {
					Id<Link> fromLinkId = OVGULocationIdToMatsimLinkId.get(from.getId());
					Node fromNode = network.getLinks().get(fromLinkId).getToNode() ;
					double starttime = 8.0*3600; //Rechne mit 08:00 Fahrzeiten.
					tree.calculate(scenario.getNetwork(), fromNode, starttime);
					Map<Id<Node>, LeastCostPathTree.NodeData> result = tree.getTree();

					for (Location to: input.getLocations()) {
						Id<Link> toLinkId = OVGULocationIdToMatsimLinkId.get(to.getId());
						Node toNode = network.getLinks().get(toLinkId).getToNode() ;
						log.debug("asking for Traveltimes from: "+ fromNode + " to: " + toNode);
						LeastCostPathTree.NodeData abc = result.get( toNode.getId() );

						long travelTime = (long) abc.getTime();

						log.debug("travelTime: " + travelTime);
						//add value to ovgu data
						Edge edge = InputHandler.createEdge(from, to);
						edge.setDuration(travelTime *1000);						//OVGU rechnet in ms
						input.getEdges().add(edge);
					}
				}

				log.info("run algorithm");
				core.initInput(input);
				//					InputHandler ih = new InputHandler();
				//					ih.setInput(input);
				//					ih.saveInput("test.xml");
				core.initNetwork(ovguNetwork);
				core.run();
				log.info("handle alg solution");

				//TODO: WIP, nur Notizen, was was ist. Muss noch eingebaut werden in MATSim Freight language -> Tourenplan. kmt/aug19.
				Solution solution = core.getSolution();
				Collection<ScheduledTour> tours = new ArrayList<ScheduledTour>();
				for (Route route : solution.getRoutes()) {
					int vehicleId = route.getVehicle().getId(); 	//OVGU vehId
					double tourDepTime = Double.NaN;
					Tour.Builder tourBuilder = Tour.Builder.newInstance();

					for(RouteElement re : route.getRouteElements()) {
						log.debug("handle RouteElement:" + re.toString());
						re.getServiceBegin(); //Wann er laut Torurnplan an der Location wäre.
						re.getServiceDuration(); //Dauer, wie lange er sich an der Location aufhält --> Dopplung zu Request -> Definition  sollte in Auftragsplan vorahnden sein, wegen online kommen ggf. noch zusätzlich Locations ohne Auftrag hinzu.
						if (re instanceof RequestActivityRouteElement) {
							//Dies ist ein eigentlicher Request
							RequestActivity requestActivity = ((RequestActivityRouteElement) re).getRequestActivity();
							int requestId = requestActivity.getRequest().getId(); //Request-Id
							log.debug("handle Activity: " + requestActivity.getId() + " ; requestId: " + requestId);
							if (requestActivity instanceof FirstRequestActivity) {
								//Pickup
								log.debug("Found FirstRequestActivity: " + requestActivity.getId() + " at location " +  requestActivity.getLocation().getId() + " : lat " + requestActivity.getLocation().getLat() + " long: "  + requestActivity.getLocation().getLon() );
								CarrierShipment carrierShipment = findShipment(carrier, requestId);
								tourBuilder.addLeg(new Leg());
								tourBuilder.schedulePickup(carrierShipment);
							}
							if (requestActivity instanceof SecondRequestActivity) {
								//Delivery
								log.debug("Found SecondRequestActivity: " + requestActivity.getId() + " at location " +  requestActivity.getLocation().getId() + " : lat " + requestActivity.getLocation().getLat() + " long: "  + requestActivity.getLocation().getLon() );
								CarrierShipment carrierShipment = findShipment(carrier, requestId);
								tourBuilder.addLeg(new Leg());
								tourBuilder.scheduleDelivery(carrierShipment);
							}
							//	TODO: Note: Not implemented; Wenn request keine Second Activity hat (=null), dann ist es "Service".
						}

						//"Start" und "Ziel" -> Depot. Jede Route verfügt über ein (requestActivity instanceof SupportRouteElement) am Anfang (Start) und am Ende (Ziel) einer Route/Tour
						if (re instanceof SupportRouteElement) {
							if (route.getRouteElements().indexOf(re) == 0 ) {
								//Start Element
								log.debug("Route Start Element begin: "+ re.getServiceBegin() );
								tourDepTime = re.getServiceBegin()+re.getServiceDuration();
								tourBuilder.scheduleStart(OVGULocationIdToMatsimLinkId.get(((SupportRouteElement) re).getLocation().getId()));
							}
							if (route.getRouteElements().indexOf(re) == route.getRouteElements().size()-1 ) {
								tourBuilder.addLeg(new Leg());
								tourBuilder.scheduleEnd(OVGULocationIdToMatsimLinkId.get(((SupportRouteElement) re).getLocation().getId()));
								//Ziel Element
							}
						}
					}
					//build MATSim-Tour
					org.matsim.contrib.freight.carrier.Tour vehicleTour = tourBuilder.build();
					ScheduledTour sTour = ScheduledTour.newInstance(vehicleTour, carrierVehicle, tourDepTime);
					//					assert route.getDepartureTime() == sTour.getDeparture() : "departureTime of both route and scheduledTour must be equal";
					assert sTour.getTour().getStartLinkId() == sTour.getTour().getEndLinkId(); //TODO: Vielleicht für andere Cases raus. 
					tours.add(sTour);
				} //route/tour
				CarrierPlan carrierPlan = new CarrierPlan(carrier, tours);
				carrierPlan.setScore((double) (solution.getScore()*(-1)));
				NetworkRouter.routePlan(carrierPlan,netBasedCosts) ;
				carrier.setSelectedPlan(carrierPlan) ;
			} //carrier
			
		} //ovgu

		new CarrierPlanXmlWriterV2(carriers).write( config.controler().getOutputDirectory()+ "/servicesAndShipments_plannedCarriers.xml") ;


		//--------- now start a MATsim run:




		//		Controler controler = new Controler(scenario);

		//		controler.run();

		log.info("#### Finished ####");

	}


	private static CarrierShipment findShipment(Carrier carrier, int requestId) {
		for(CarrierShipment shipment : carrier.getShipments()) {
			if (Integer.parseInt(shipment.getId().toString()) == requestId)
				return shipment;
		}
		throw new RuntimeException("Shipment with id not found in carrier: " + requestId + "; " + carrier.getId());
	}

	private static void clearMaps() {
		matsimLinkIdToOVGULocationID.clear();
		OVGULocationIdToMatsimLinkId.clear();
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

	private static Location getOVGULocation(Id<Link> link, Network network, Input input){
		boolean isNew = false;

		if (!matsimLinkIdToOVGULocationID.containsKey(link)){
			Integer ovguLocId = matsimLinkIdToOVGULocationID.size();
			matsimLinkIdToOVGULocationID.put(link, ovguLocId);
			OVGULocationIdToMatsimLinkId.put(ovguLocId,link);
			isNew = true;
		}

		Location location = InputHandler.createLocation(matsimLinkIdToOVGULocationID.get(link), network.getLinks().get(link).getCoord().getX(), network.getLinks().get(link).getCoord().getY());

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
