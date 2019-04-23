package ovgu.pave.handler;

import ovgu.pave.handler.modelHandler.SolutionHandler;
import ovgu.pave.handler.shortestPath.GraphHopperShortestPath;
import ovgu.pave.handler.shortestPath.ShortestPath;
import ovgu.pave.model.solution.RequestActivityRouteElement;
import ovgu.pave.model.solution.Route;

public class TestSomething {

	private static final String _DefaultConfigPath = "../config.xml";

	public static void main(String[] args) {
		System.out.println("run TestSomething in core");

		Handler.getInput().loadConfig(_DefaultConfigPath);
//		testGraphHopper();
		readInputFile("inputDARP.zip");
//		writeSolutionFile("solution.xml");
//		readSolutionFile("solution.xml");

		System.out.println("TestSomething in core is finished");
	}

	private static void readSolutionFile(String filename) {

		Handler.getInput().loadInput("inputDARP.zip");
		Handler.getSolution().loadSolution(Handler.getInput().getConfig().getExperiment().getOutputFolder(), filename, Handler.getInput());
		SolutionHandler s = Handler.getSolution();
		System.out.println(s.getScore());
		System.out.println(s.getRoutes().get(0).getVehicleID());
		System.out.println(((RequestActivityRouteElement)s.getRoutes().get(0).getRouteElements().get(0)).getRequestActivityID());
		System.out.println(((RequestActivityRouteElement)s.getRoutes().get(0).getRouteElements().get(1)).getRequestActivityID());
		System.out.println(s.getRoutes().get(0).getVehicle().getId());
		System.out.println(((RequestActivityRouteElement)s.getRoutes().get(0).getRouteElements().get(0)).getRequestActivity().getId());
		System.out.println(((RequestActivityRouteElement)s.getRoutes().get(0).getRouteElements().get(1)).getRequestActivity().getId());
		
	}

	private static void writeSolutionFile(String filename) {
		SolutionHandler s = Handler.getSolution();
		s.setScore(555);
		Handler.getInput().loadInput("inputDARP.zip");
		
		Route r = SolutionHandler.createRoute(Handler.getInput().getVehicles().get(1));
		SolutionHandler.insertRequest(r, Handler.getInput().getAcceptedRequests().get(5), 0, 0);
		Handler.getSolution().getRoutes().add(r);
		
		
		Handler.getSolution().saveSolution(Handler.getInput().getConfig().getExperiment().getOutputFolder(), filename);
		
	}

	private static void testGraphHopper() {

		ShortestPath sp = new GraphHopperShortestPath(
				Handler.getInput().getConfig().getShortestPath().getGraphhopper());

		sp.generateShortestPath(52.5438, 13.3848, 52.5054, 13.428);
		System.out.println("Distance: " + sp.getDistance() + "\tDuration: " + sp.getDuration());
	}

	private static void readInputFile(String inputFileName) {
		long started = System.nanoTime();
		Handler.getInput().loadInput(inputFileName);
		long finishedReading = System.nanoTime();
		Handler.getNetwork().setupNetwork(Handler.getInput());
		long finishedBuilding = System.nanoTime();

		double timeToRead = ((double) finishedReading - started) / 1000000000;
		double timeToBuild = ((double) finishedBuilding - finishedReading) / 1000000000;
		System.out.println(timeToRead + " seconds to read input and " + timeToBuild + " to build network");
	}

}
