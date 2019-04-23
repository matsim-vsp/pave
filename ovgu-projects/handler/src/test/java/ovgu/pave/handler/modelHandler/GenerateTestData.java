package ovgu.pave.handler.modelHandler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import junit.framework.TestCase;
import ovgu.pave.model.config.Config;
import ovgu.pave.model.config.ConfigFactory;
import ovgu.pave.model.config.Heuristic;
import ovgu.pave.model.input.Edge;
import ovgu.pave.model.input.Input;
import ovgu.pave.model.input.InputFactory;
import ovgu.pave.model.input.Location;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.input.Vehicle;
import ovgu.pave.model.input.VehicleType;
import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.Solution;
import ovgu.pave.model.solution.SolutionFactory;

public class GenerateTestData  extends TestCase {

	protected static Route generateTestRoute() {
		Request request = InputHandler.createRequest(1, InputHandler.createLocation(0, 10, 20),
				InputHandler.createLocation(1, 11, 21), 1);

		Route route = SolutionHandler.createRoute(InputHandler.createVehicle(1, null, null, null));
		route.getRouteElements().add(SolutionHandler.createRequestActivityRouteElement(request.getOriginActivity()));
		route.getRouteElements().add(SolutionHandler.createSupportRouteElement(InputHandler.createLocation(2, 12, 22)));
		route.getRouteElements()
				.add(SolutionHandler.createWaypointRouteElement(InputHandler.createWayPoint("a", 0, 4, 5, 6, 7)));
		route.getRouteElements()
				.add(SolutionHandler.createRequestActivityRouteElement(request.getDestinationActivity()));
		route.getRouteElements().add(SolutionHandler.createSupportRouteElement(InputHandler.createLocation(3, 13, 23)));
		return route;
	}

	protected static List<VehicleType> generateTestVehicleTypes() {
		List<VehicleType> vehicleTypes = new ArrayList<VehicleType>();
		vehicleTypes.add(InputHandler.createVehicleType(0, 0));
		vehicleTypes.add(InputHandler.createVehicleType(2, 0));
		vehicleTypes.add(InputHandler.createVehicleType(3, 0));
		vehicleTypes.add(InputHandler.createVehicleType(4, 0));
		return vehicleTypes;
	}

	protected static Config generateTestConfig() {
		/*
		 * variables
		 */

		String configPath = "";
		String inputFolder = "../resources/input";
		String outputFolder = "../resources/output";
		int randomSeet = 99;
		String inputFilename = "inputDARP.zip";
		String shortestPath_Use = "graphhopper";
		String shortestPath_Graphhopper_VehicleType = "car";
		String shortestPath_Graphhopper_Weighting = "fastest";
		String shortestPath_Graphhopper_GraphFolder = "../resources/graphhopper/data";
		String shortestPath_Graphhopper_OsmFilePath = "../resources/graphhopper/Berlin100320192115.osm.pbf";
		String algorithm_Algorithm = "LMNS";
		int algorithm_Lns_MaxCalculationTime = 20;
		int algorithm_Lns_MaxIterations = Integer.MAX_VALUE;
		int algorithm_Lns_MinLargeRequestSet = 10;
		int algorithm_Lns_MaxLargeRequestSet = 60;
		double algorithm_Lns_MinSmallRequestSet = 0.1;
		double algorithm_Lns_MaxSmallRequestSet = 0.4;
		List<Heuristic> algorithm_Lns_InsertionHeuristics = new ArrayList<Heuristic>();
		algorithm_Lns_InsertionHeuristics.add(InputHandler.createHeuristic("REGRET2", 0));
		List<Heuristic> algorithm_Lns_RemovalHeuristics = new ArrayList<Heuristic>();
		algorithm_Lns_RemovalHeuristics.add(InputHandler.createHeuristic("RANDOMREMOVAL", 0));

		/*
		 * create config
		 */

		Config config = ConfigFactory.eINSTANCE.createConfig();
		config.setExperiment(ConfigFactory.eINSTANCE.createExperiment());
		config.setAlgorithm(ConfigFactory.eINSTANCE.createAlgorithm());
		config.getAlgorithm().setLns(ConfigFactory.eINSTANCE.createLNS());
		config.setShortestPath(ConfigFactory.eINSTANCE.createShortestPath());
		config.getShortestPath().setGraphhopper(ConfigFactory.eINSTANCE.createGraphHopper());

		/*
		 * setup config
		 */

		config.setConfigPath(configPath);
		config.setInputFolder(inputFolder);
		config.getExperiment().setOutputFolder(outputFolder);
		config.getAlgorithm().setRandomSeet(randomSeet);
		config.setInputFilename(inputFilename);
		config.getShortestPath().setUse(shortestPath_Use);
		config.getShortestPath().getGraphhopper().setVehicleType(shortestPath_Graphhopper_VehicleType);
		config.getShortestPath().getGraphhopper().setWeighting(shortestPath_Graphhopper_Weighting);
		config.getShortestPath().getGraphhopper().setGraphFolder(shortestPath_Graphhopper_GraphFolder);
		config.getShortestPath().getGraphhopper().setOsmFilePath(shortestPath_Graphhopper_OsmFilePath);
		config.getAlgorithm().setAlgorithm(algorithm_Algorithm);
		config.getAlgorithm().getLns().setMaxCalculationTime(algorithm_Lns_MaxCalculationTime);
		config.getAlgorithm().getLns().setMaxIterations(algorithm_Lns_MaxIterations);
		config.getAlgorithm().getLns().setMinLargeRequestSet(algorithm_Lns_MinLargeRequestSet);
		config.getAlgorithm().getLns().setMaxLargeRequestSet(algorithm_Lns_MaxLargeRequestSet);
		config.getAlgorithm().getLns().setMinSmallRequestSet(algorithm_Lns_MinSmallRequestSet);
		config.getAlgorithm().getLns().setMaxSmallRequestSet(algorithm_Lns_MaxSmallRequestSet);
		config.getAlgorithm().getLns().getInsertionHeuristics().addAll(algorithm_Lns_InsertionHeuristics);
		config.getAlgorithm().getLns().getRemovalHeuristics().addAll(algorithm_Lns_RemovalHeuristics);

		/*
		 * test variables equals config
		 */

		assertEquals(configPath, config.getConfigPath());
		assertEquals(inputFolder, config.getInputFolder());
		assertEquals(outputFolder, config.getExperiment().getOutputFolder());
		assertEquals(randomSeet, config.getAlgorithm().getRandomSeet());
		assertEquals(inputFilename, config.getInputFilename());
		assertEquals(shortestPath_Use, config.getShortestPath().getUse());
		assertEquals(shortestPath_Graphhopper_VehicleType, config.getShortestPath().getGraphhopper().getVehicleType());
		assertEquals(shortestPath_Graphhopper_Weighting, config.getShortestPath().getGraphhopper().getWeighting());
		assertEquals(shortestPath_Graphhopper_GraphFolder, config.getShortestPath().getGraphhopper().getGraphFolder());
		assertEquals(shortestPath_Graphhopper_OsmFilePath, config.getShortestPath().getGraphhopper().getOsmFilePath());
		assertEquals(algorithm_Algorithm, config.getAlgorithm().getAlgorithm());
		assertEquals(algorithm_Lns_MaxCalculationTime, config.getAlgorithm().getLns().getMaxCalculationTime());
		assertEquals(algorithm_Lns_MaxIterations, config.getAlgorithm().getLns().getMaxIterations());
		assertEquals(algorithm_Lns_MinLargeRequestSet, config.getAlgorithm().getLns().getMinLargeRequestSet());
		assertEquals(algorithm_Lns_MaxLargeRequestSet, config.getAlgorithm().getLns().getMaxLargeRequestSet());
		assertEquals(algorithm_Lns_MinSmallRequestSet, config.getAlgorithm().getLns().getMinSmallRequestSet());
		assertEquals(algorithm_Lns_MaxSmallRequestSet, config.getAlgorithm().getLns().getMaxSmallRequestSet());

		assertEquals(algorithm_Lns_InsertionHeuristics.size(),
				config.getAlgorithm().getLns().getInsertionHeuristics().size());
		for (int i = 0; i < algorithm_Lns_InsertionHeuristics.size(); i++) {
			assertEquals(algorithm_Lns_InsertionHeuristics.get(i).getHeuristicName(),
					config.getAlgorithm().getLns().getInsertionHeuristics().get(i).getHeuristicName());
			assertEquals(algorithm_Lns_InsertionHeuristics.get(i).getNoise(),
					config.getAlgorithm().getLns().getInsertionHeuristics().get(i).getNoise());
		}

		assertEquals(algorithm_Lns_RemovalHeuristics.size(),
				config.getAlgorithm().getLns().getRemovalHeuristics().size());
		for (int i = 0; i < algorithm_Lns_RemovalHeuristics.size(); i++) {
			assertEquals(algorithm_Lns_RemovalHeuristics.get(i).getHeuristicName(),
					config.getAlgorithm().getLns().getRemovalHeuristics().get(i).getHeuristicName());
			assertEquals(algorithm_Lns_RemovalHeuristics.get(i).getNoise(),
					config.getAlgorithm().getLns().getRemovalHeuristics().get(i).getNoise());
		}
		
		return config;
	}

	protected static Input generateTestInput() {

		/*
		 * variables
		 */

		List<Location> locations = new ArrayList<Location>();
		List<VehicleType> vehicleTypes = new ArrayList<VehicleType>();
		List<Vehicle> vehicles = new ArrayList<Vehicle>();
		List<Request> requests = new ArrayList<Request>();
		List<Edge> edges = new ArrayList<Edge>();

		locations.add(InputHandler.createLocation(0, 10.0, 10.4));
		locations.add(InputHandler.createLocation(2, 20.1, 20.3));
		locations.add(InputHandler.createLocation(4, 30.2, 30.2));
		locations.add(InputHandler.createLocation(6, 40.3, 40.1));
		locations.add(InputHandler.createLocation(8, 50.4, 50.0));
		locations.add(InputHandler.createLocation(10, 60, 60));
		vehicleTypes.add(InputHandler.createVehicleType(1,  0));
		vehicleTypes.add(InputHandler.createVehicleType(3, 0));
		vehicles.add(InputHandler.createVehicle(1, vehicleTypes.get(0), locations.get(0), locations.get(5)));
		vehicles.add(InputHandler.createVehicle(3, vehicleTypes.get(1), locations.get(0), locations.get(5)));
		vehicles.add(InputHandler.createVehicle(0, vehicleTypes.get(0), locations.get(0), locations.get(5)));
		vehicles.add(InputHandler.createVehicle(2, vehicleTypes.get(1), locations.get(0), locations.get(5)));
		requests.add(InputHandler.createRequest(0, locations.get(0), locations.get(4), 1));
		requests.add(InputHandler.createRequest(2, locations.get(1), locations.get(3), 0));
		requests.add(InputHandler.createRequest(4, locations.get(2), locations.get(2), 2));
		requests.add(InputHandler.createRequest(6, locations.get(3), locations.get(1), 3));
		requests.add(InputHandler.createRequest(8, locations.get(4), locations.get(0), 5));
		for (Request request : requests) {
			request.setReceivingTime(99);
			request.getOriginActivity().setEarliestArrival(999);
			request.getOriginActivity().setServiceDuration(9);
			request.getOriginActivity().setLatestArrival(99999);
			request.getDestinationActivity().setEarliestArrival(111);
			request.getDestinationActivity().setServiceDuration(1);
			request.getDestinationActivity().setLatestArrival(11111);
		}
		edges.add(InputHandler.createEdge(locations.get(0), locations.get(4)));
		edges.add(InputHandler.createEdge(locations.get(1), locations.get(3)));
		edges.add(InputHandler.createEdge(locations.get(2), locations.get(2)));
		edges.add(InputHandler.createEdge(locations.get(3), locations.get(1)));
		edges.add(InputHandler.createEdge(locations.get(4), locations.get(0)));
		for (Edge edge : edges) {
			edge.setCost(555);
			edge.setDistance(55);
			edge.setDuration(5555);
			edge.getWaypoints().add(InputHandler.createWayPoint(edge.getId(), 0, 10.4, 10.0, 11, 111));
			edge.getWaypoints().add(InputHandler.createWayPoint(edge.getId(), 1, 20.3, 20.1, 22, 222));
			edge.getWaypoints().add(InputHandler.createWayPoint(edge.getId(), 2, 30.2, 30.2, 33, 333));
			edge.getWaypoints().add(InputHandler.createWayPoint(edge.getId(), 3, 40.1, 40.3, 44, 444));
		}

		/*
		 * create input
		 */

		Input input = InputFactory.eINSTANCE.createInput();
		input.setRequests(InputFactory.eINSTANCE.createRequests());
		

		/*
		 * setup input
		 */

		input.getVehicleTypes().addAll(vehicleTypes);
		input.getVehicles().addAll(vehicles);
		input.getLocations().addAll(locations);
		input.getRequests().getNew().addAll(requests);
		input.getEdges().addAll(edges);

		/*
		 * test variables equals input
		 */

		assertTrue(EcoreUtil.equals(locations, input.getLocations()));
		assertTrue(EcoreUtil.equals(vehicleTypes, input.getVehicleTypes()));
		assertTrue(EcoreUtil.equals(vehicles, input.getVehicles()));
		assertTrue(EcoreUtil.equals(requests, input.getRequests().getNew()));
		assertTrue(EcoreUtil.equals(edges, input.getEdges()));
		
		return input;
	}

	protected static Solution generateTestSolution(Input input, boolean useVehicleType) {

		/*
		 * variables
		 */

		long score = 464;

		boolean useVehicleTypeInsteadVehicle = useVehicleType;

		List<VehicleType> vehicleTypes = new ArrayList<VehicleType>();
		if (useVehicleTypeInsteadVehicle) {
			vehicleTypes.add(input.getVehicleTypes().get(0));
			vehicleTypes.add(input.getVehicleTypes().get(1));
		}
		int vehicleTypeQuantities = 2;

		long experimentalValues_computationStarted = 456489;
		long experimentalValues_computationFinished = 489411465;

		List<Route> routes = new ArrayList<Route>();
		if (useVehicleTypeInsteadVehicle) {
			routes.add(SolutionHandler.createRoute(vehicleTypes.get(0)));
			routes.add(SolutionHandler.createRoute(vehicleTypes.get(1)));
			routes.add(SolutionHandler.createRoute(vehicleTypes.get(0)));
			routes.add(SolutionHandler.createRoute(vehicleTypes.get(1)));
		} else {
			routes.add(SolutionHandler.createRoute(input.getVehicles().get(0)));
			routes.add(SolutionHandler.createRoute(input.getVehicles().get(1)));
			routes.add(SolutionHandler.createRoute(input.getVehicles().get(2)));
			routes.add(SolutionHandler.createRoute(input.getVehicles().get(3)));
		}

		for (Route route : routes) {
			route.getRouteElements().add(
					SolutionHandler.createRequestActivityRouteElement(input.getRequests().getNew().get(2).getOriginActivity()));
			route.getRouteElements().add(SolutionHandler.createSupportRouteElement(input.getLocations().get(0)));
			route.getRouteElements()
					.add(SolutionHandler.createWaypointRouteElement(input.getEdges().get(4).getWaypoints().get(2)));
			route.getRouteElements().add(SolutionHandler
					.createRequestActivityRouteElement(input.getRequests().getNew().get(2).getDestinationActivity()));
			route.getRouteElements().add(SolutionHandler.createSupportRouteElement(input.getLocations().get(4)));
		}

		/*
		 * create solution
		 */

		Solution solution = SolutionFactory.eINSTANCE.createSolution();
		solution.setUseVehicleType(SolutionFactory.eINSTANCE.createUseVehicleType());

		/*
		 * setup solution
		 */

		solution.setScore(score);
		solution.getUseVehicleType().setUseVehicleTypeInsteadVehicle(useVehicleTypeInsteadVehicle);

		for (VehicleType vehicleType : vehicleTypes) {
			solution.getUseVehicleType().getVehicleTypes().add(vehicleType);
			solution.getUseVehicleType().getVehicleTypeIDs().add(vehicleType.getId());
			solution.getUseVehicleType().getQuantities().add(vehicleTypeQuantities);
		}
		
		solution.getRoutes().addAll(routes);

		/*
		 * test variables equals solution
		 */

		assertEquals(score, solution.getScore());
		assertEquals(useVehicleTypeInsteadVehicle, solution.getUseVehicleType().isUseVehicleTypeInsteadVehicle());

		assertTrue(EcoreUtil.equals(vehicleTypes, solution.getUseVehicleType().getVehicleTypes()));
		assertEquals(vehicleTypes.size(), solution.getUseVehicleType().getVehicleTypeIDs().size());
		assertEquals(vehicleTypes.size(), solution.getUseVehicleType().getQuantities().size());
		for (int i = 0; i < solution.getUseVehicleType().getVehicleTypes().size(); i++) {
			assertEquals(vehicleTypes.get(i).getId(), (int) solution.getUseVehicleType().getVehicleTypeIDs().get(i));
			assertEquals(vehicleTypeQuantities, (int) solution.getUseVehicleType().getQuantities().get(i));
		}

		assertTrue(EcoreUtil.equals(routes, solution.getRoutes()));

		return solution;
	}
}
