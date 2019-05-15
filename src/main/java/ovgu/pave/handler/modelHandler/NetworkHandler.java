package ovgu.pave.handler.modelHandler;

import java.util.List;

import ovgu.pave.handler.shortestPath.GraphHopperShortestPath;
import ovgu.pave.handler.shortestPath.ShortestPath;
import ovgu.pave.model.input.Edge;
import ovgu.pave.model.input.InputFactory;
import ovgu.pave.model.input.RequestActivity;
import ovgu.pave.model.input.Location;
import ovgu.pave.model.network.NetworkFactory;
import ovgu.pave.model.solution.RouteElement;
import ovgu.pave.model.network.Network;

public class NetworkHandler {

	/*
	 * 
	 * getter and setter
	 * 
	 */

	private Network network = NetworkFactory.eINSTANCE.createNetwork();
	private ShortestPath shortestPath;

	public void setNetwork(Network network) {
		this.network = network;
	}

	public Edge[][] getEdges() {
		return network.getEdges();
	}

	/**
	 * 
	 * @param      <T1> is Location, RequestActivity or RouteElement
	 * @param      <T2> is ovgu.pave.model.input.Location,
	 *             ovgu.pave.model.input.RequestActivity or
	 *             ovgu.pave.model.solution.RouteElement
	 * @param from is T1
	 * @param to   is T2
	 * 
	 * @return long
	 */
	public <T1, T2> long getTravelDuration(T1 from, T2 to) {
		return getEdge(from, to).getDuration();
	}

	/**
	 * 
	 * @param      <T1> is Location, RequestActivity or RouteElement
	 * @param      <T2> is ovgu.pave.model.input.Location,
	 *             ovgu.pave.model.input.RequestActivity or
	 *             ovgu.pave.model.solution.RouteElement
	 * @param from is T1
	 * @param to   is T2
	 * 
	 * @return long
	 */
	public <T1, T2> long getTravelCost(T1 from, T2 to) {
		return getEdge(from, to).getCost();
	}

	/**
	 * 
	 * @param      <T1> is Location, RequestActivity or RouteElement
	 * @param      <T2> is ovgu.pave.model.input.Location,
	 *             ovgu.pave.model.input.RequestActivity or
	 *             ovgu.pave.model.solution.RouteElement
	 * @param from is T1
	 * @param to   is T2
	 * 
	 * @return double
	 */
	public <T1, T2> double getTravelDistance(T1 from, T2 to) {
		return getEdge(from, to).getDistance();
	}

	/*
	 * 
	 * instance methods
	 * 
	 */

	public long getInsertionDuration(RouteElement from, RouteElement over, RouteElement to) {
		long insertionCosts = getTravelDuration(from, over) + getTravelDuration(over, to) - getTravelDuration(from, to);
		return insertionCosts;
	}

	public long getInsertionDurationIfIndexesEqual(RouteElement from, RouteElement overOrgin,
			RouteElement overDestination, RouteElement to) {
		long insertionCosts = getTravelDuration(from, overOrgin) + getTravelDuration(overOrgin, overDestination)
				+ getTravelDuration(overDestination, to) - getTravelDuration(from, to);
		return insertionCosts;
	}

	/**
	 * 
	 * @param      <T1> is Location, RequestActivity or RouteElement
	 * @param      <T2> is ovgu.pave.model.input.Location,
	 *             ovgu.pave.model.input.RequestActivity or
	 *             ovgu.pave.model.solution.RouteElement
	 * @param from is T1
	 * @param to   is T2
	 * 
	 * @return ovgu.pave.model.input.Edge
	 */
	public <T1, T2> Edge getEdge(T1 from, T2 to) {
		Location fromLocation = getLocation(from);
		Location toLocation = getLocation(to);

		if (fromLocation != null && toLocation != null) {
			return getEdge(network, fromLocation, toLocation);
		} else {
			double fromLat = getLatitude(from);
			double fromLon = getLongitude(from);
			double toLat = getLatitude(to);
			double toLon = getLongitude(to);
			if (fromLat == -1 || fromLon == -1 || toLat == -1 || toLon == -1)
				return null;
			else
				return getEdge(fromLat, fromLon, toLat, toLon);
		}
	}

	public void initShortestPath(ovgu.pave.model.config.ShortestPath shortestPathConfig) {
		switch (shortestPathConfig.getUse()) {
		case "graphhopper":
			shortestPath = new GraphHopperShortestPath(shortestPathConfig.getGraphhopper());
		}
	}

	public void initNetwork(List<Location> locations) {
		network.getLocations().clear();
		network.getLocations().addAll(locations);
		initEdges(locations);
	}

	public void setupNetwork(InputHandler inputHandler) {
		int networkSize = getHighestID(inputHandler.getLocations()) + 1;
		network.setEdges(new Edge[networkSize][networkSize]);
		setupEdges(inputHandler.getEdges());
	}

	public void addLocation(Location location) {
		addLocationFeasibilityCheck(location);
		if (location.getId() >= network.getEdges().length)
			expandNetwork(location.getId() + 1);

		calculateEdges(location);
		network.getLocations().add(location);
	}

	private int getHighestID(List<Location> locations) {
		int highestID = 0;
		for (Location location : locations)
			if (location.getId() > highestID)
				highestID = location.getId();
		return highestID;
	}

	private void setupEdges(List<Edge> edges) {
		for (Edge edge : edges) {
			int idFrom = edge.getStartLocation().getId();
			int idTo = edge.getEndLocation().getId();
			network.getEdges()[idFrom][idTo] = edge;
		}
	}

	private void initEdges(List<Location> locations) {
		int networkSize = getHighestID(locations) + 1;
		network.setEdges(new Edge[networkSize][networkSize]);
		for (int i = 0; i < locations.size(); i++) {
			for (int j = 0; j < locations.size(); j++) {
				Location locationFrom = locations.get(i);
				Location locationTo = locations.get(j);
				int idFrom = locationFrom.getId();
				int idTo = locationTo.getId();

				Edge edge = InputHandler.createEdge(locationFrom, locationTo);
				network.getEdges()[idFrom][idTo] = edge;
			}
		}
	}

	private void addLocationFeasibilityCheck(Location location) {
		if (network.getLocations().contains(location))
			System.out.println(">>>> NetworkHandler addLocationFeasibilityCheck: location already in network");
		else
			for (Location l : network.getLocations())
				if (l.getId() == location.getId())
					System.out.println(
							">>>> NetworkHandler addLocationFeasibilityCheck: location with same ID in network");
	}

	private void calculateEdges(Location newLocation) {
		for (Location location : network.getLocations()) {
			network.getEdges()[newLocation.getId()][location.getId()] = InputHandler.createEdge(newLocation, location);
			network.getEdges()[location.getId()][newLocation.getId()] = InputHandler.createEdge(location, newLocation);
		}
	}

	private void expandNetwork(int networkSize) {

		Edge[][] saveEdges = network.getEdges();
		network.setEdges(new Edge[networkSize][networkSize]);

		for (int x = 0; x < saveEdges.length; x++) {
			for (int y = 0; y < saveEdges.length; y++) {
				network.getEdges()[x][y] = saveEdges[x][y];
			}
		}

	}

	private Edge getEdge(double fromLat, double fromLon, double toLat, double toLon) {
		Edge edge = InputFactory.eINSTANCE.createEdge();
		shortestPath.generateShortestPath(fromLat, fromLon, toLat, toLon);
		edge.setDistance(shortestPath.getDistance());
		edge.setDuration(shortestPath.getDuration());
		edge.getWaypoints().addAll(shortestPath.getWayPoints("onTheFly_"));
		return edge;
	}

	/*
	 * 
	 * static methods
	 * 
	 */

	private static Edge getEdge(Network network, Location from, Location to) {
		return network.getEdges()[from.getId()][to.getId()];
	}

	private static <T> boolean isLocation(T object) {
		return object instanceof Location;
	}

	private static <T> boolean isRouteElement(T object) {
		return object instanceof RouteElement;
	}

	private static <T> boolean isRequestActivity(T object) {
		return object instanceof RequestActivity;
	}

	private static <T> Location getLocation(T object) {
		if (isLocation(object))
			return (Location) object;
		else if (isRequestActivity(object))
			return ((RequestActivity) object).getLocation();
		else if (isRouteElement(object))
			return SolutionHandler.getLocation((RouteElement) object);
		else
			return null;
	}

	private static <T> double getLatitude(T object) {
		if (isLocation(object))
			return ((Location) object).getLat();
		else if (isRequestActivity(object))
			return (((RequestActivity) object).getLocation()).getLat();
		else if (isRouteElement(object))
			return SolutionHandler.getLatitude((RouteElement) object);
		else
			return -1;
	}

	private static <T> double getLongitude(T object) {
		if (isLocation(object))
			return ((Location) object).getLon();
		else if (isRequestActivity(object))
			return (((RequestActivity) object).getLocation()).getLon();
		else if (isRouteElement(object))
			return SolutionHandler.getLongitude((RouteElement) object);
		else
			return -1;
	}
}
