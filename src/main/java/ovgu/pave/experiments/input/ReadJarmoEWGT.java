package ovgu.pave.experiments.input;

import java.util.List;

import ovgu.pave.experiments.utils.ReadCSV;
import ovgu.pave.handler.Handler;
import ovgu.pave.handler.modelHandler.InputHandler;
import ovgu.pave.model.input.Location;
import ovgu.pave.model.input.Request;

public class ReadJarmoEWGT {

	private static String folderPath;

	public static void readData(String folderPath) {
		ReadJarmoEWGT.folderPath = folderPath;

		System.out.println("read locations ...");
		readLocations("EWGT_Locations.csv");
		for (Location location : Handler.getInput().getLocations()) {
			System.out.println(location.getLon() + "\t" + location.getLat() + "\t" + location.getId());
		}

		System.out.println("read requests ...");
		readRequests("EWGT_Requests.csv");
		for (Request request : Handler.getInput().getNewRequests()) {
			request.getOriginActivity().setServiceDuration(
					Handler.getInput().getConfig().getExperiment().getServiceDuration() * 60 * 1000);
			request.getDestinationActivity().setServiceDuration(
					Handler.getInput().getConfig().getExperiment().getServiceDuration() * 60 * 1000);
		}

		System.out.println("read recieving sequence of requests ...");

		for (Request request : Handler.getInput().getNewRequests()) {
			System.out.println(request.getOriginActivity().getLocation().getId());
		}

		System.out.println("read recieving time of requests ...");
		readRecievingTime("EWGT_RequestTimes.csv");

		for (Request request : Handler.getInput().getNewRequests()) {
			System.out.println(request.getReceivingTime() / 60 / 1000);
		}

		readMatrix("EWGT_Matrix.csv");

//		System.out.println("build network ...");
//		buildNetwork();

		setDestionationTimeWindowEnd(1);
	}

	private static void setDestionationTimeWindowEnd(double maxDelayFaktor) {
		for (Request request : Handler.getInput().getNewRequests()) {
			long originLatestArrival = request.getOriginActivity().getLatestArrival();
			long originServiceDuration = request.getOriginActivity().getServiceDuration();
			long travelDuration = Handler.getNetwork().getTravelDuration(request.getOriginActivity(),
					request.getDestinationActivity());

			long destinationLatestArrival = originLatestArrival + originServiceDuration
					+ (long) (travelDuration * maxDelayFaktor);

			request.getDestinationActivity().setLatestArrival(destinationLatestArrival);
		}
	}

	/*
	 * Backup for using GraphHopperShortestPath private static void buildNetwork() {
	 * List<Location> locations = Handler.getInput().getLocations();
	 * Handler.getNetwork().initNetwork(locations); ShortestPath sp = new
	 * GraphHopperShortestPath(
	 * Handler.getInput().getConfig().getShortestPath().getGraphhopper()); int
	 * counter = 1; for (Location locationFrom : locations) { for (Location
	 * locationTo : locations) { sp.generateShortestPath(locationFrom, locationTo);
	 * Edge edge = Handler.getNetwork().getEdge(locationFrom, locationTo);
	 * edge.setDistance(sp.getDistance()); edge.setDuration(sp.getDuration());
	 * edge.getWaypoints().addAll(sp.getWayPoints(edge.getId()));
	 * Handler.getInput().addEdge(edge); } System.out.println("finished " + counter
	 * + "/" + (locations.size())); counter++; } }
	 */

	private static void readMatrix(String filename) {
		List<List<Long>> matrix;
		matrix = ReadCSV.read(getPath(filename), ",", long.class, true);

		Handler.getNetwork().initNetwork(Handler.getInput().getLocations());

		for (int x = 0; x < matrix.size(); x++) {
			for (int y = 0; y < matrix.size(); y++) {
				if (Handler.getNetwork().getEdges()[x][y] == null)
					System.out.println("NEIN: "+  x + " " + y );
				if (matrix.get(x).get(y) == null)
					System.out.println("JA: " + x+ " "+y );
				Handler.getNetwork().getEdges()[x][y].setDuration(matrix.get(x).get(y));
				Handler.getInput().addEdge(Handler.getNetwork().getEdges()[x][y]);
			}
		}
	}

	private static void readRecievingTime(String recievingTimeFile) {
		List<List<Integer>> intervals = ReadCSV.read(getPath(recievingTimeFile), ",", int.class, true);

		for (int i = 0; i < Handler.getInput().getNewRequests().size() / 2; i++) {
			long receivingTime = intervals.get(0).get(i) * 1000;
			Handler.getInput().getNewRequests().get(i).setReceivingTime(receivingTime);
		}

	}

	private static void readLocations(String locationsFile) {
		List<List<Double>> locations = ReadCSV.read(getPath(locationsFile), ",", double.class, true);

		for (List<Double> location : locations) {
			Location l = InputHandler.createLocation(location.get(2).intValue(), location.get(1), location.get(0));
			Handler.getInput().getLocations().add(l);
		}
	}

	private static void readRequests(String filename) {
		List<List<Integer>> requests = ReadCSV.read(getPath(filename), ",", int.class, true);

		for (int i = 0; i < requests.size(); i++) {
			List<Integer> request = requests.get(i);
			Location originLocation = Handler.getInput().getLocations().get(request.get(1));
			Location destinationLocation = Handler.getInput().getLocations().get(request.get(2));
			Request r = InputHandler.createRequest(i, originLocation, destinationLocation, request.get(3));
			Handler.getInput().getNewRequests().add(r);
		}
	}

	private static String getPath(String filename) {
		return folderPath + filename;
	}
}
