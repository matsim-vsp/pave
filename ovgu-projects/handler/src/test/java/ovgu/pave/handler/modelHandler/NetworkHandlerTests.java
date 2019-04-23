package ovgu.pave.handler.modelHandler;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ovgu.pave.handler.modelHandler.InputHandler;
import ovgu.pave.handler.modelHandler.NetworkHandler;
import ovgu.pave.handler.modelHandler.SolutionHandler;
import ovgu.pave.model.input.DestinationRequestActivity;
import ovgu.pave.model.input.Edge;
import ovgu.pave.model.input.InputFactory;
import ovgu.pave.model.input.Location;
import ovgu.pave.model.input.OriginRequestActivity;
import ovgu.pave.model.input.RequestActivity;
import ovgu.pave.model.input.Vehicle;
import ovgu.pave.model.input.WayPoint;
import ovgu.pave.model.solution.RequestActivityRouteElement;
import ovgu.pave.model.solution.RouteElement;
import ovgu.pave.model.solution.WayPointRouteElement;

/**
 * Unit test for NetworkHandler.
 */
public class NetworkHandlerTests extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public NetworkHandlerTests(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(NetworkHandlerTests.class);
	}

	/**
	 * Rigourous Test :-)
	 */

	public void test_getEdge_wrongInput() {
		Location l1 = InputHandler.createLocation(0, 52.5438, 13.3848);
		Location l2 = InputHandler.createLocation(2, 52.5054, 13.428);
		Vehicle v1 = InputHandler.createVehicle(5, null, null, null);
		Vehicle v2 = InputHandler.createVehicle(2, null, null, null);

		List<Location> locations = new ArrayList<Location>();
		locations.add(l1);
		locations.add(l2);
		
		NetworkHandler nH = new NetworkHandler();
		nH.initNetwork(locations);


		assertNotNull(nH.getEdge(l1, l2));
		assertNotNull(nH.getEdge(l2, l1));
		
		assertNull(nH.getEdge(l1, v1));
		assertNull(nH.getEdge(l1, v2));
		assertNull(nH.getEdge(l2, v1));
		assertNull(nH.getEdge(l2, v2));
		assertNull(nH.getEdge(v1, l1));
		assertNull(nH.getEdge(v1, l2));
		assertNull(nH.getEdge(v1, v2));
		assertNull(nH.getEdge(v2, l1));
		assertNull(nH.getEdge(v2, l2));
		assertNull(nH.getEdge(v2, v1));
	}

	public void test_getEdge_withLocations() {
		Location l1 = InputHandler.createLocation(0, 52.5438, 13.3848);
		Location l2 = InputHandler.createLocation(2, 52.5054, 13.428);
		Location l3 = InputHandler.createLocation(6, 6, 6);
		Location l4 = InputHandler.createLocation(4, 4, 4);

		List<Location> locations = new ArrayList<Location>();
		locations.add(l1);
		locations.add(l2);
		locations.add(l3);
		locations.add(l4);

		NetworkHandler nH = new NetworkHandler();
		nH.initNetwork(locations);

		checkEdgeWithLocations(nH, l1, l2);
		checkEdgeWithLocations(nH, l1, l3);
		checkEdgeWithLocations(nH, l1, l4);
		checkEdgeWithLocations(nH, l2, l1);
		checkEdgeWithLocations(nH, l2, l3);
		checkEdgeWithLocations(nH, l2, l4);
		checkEdgeWithLocations(nH, l3, l1);
		checkEdgeWithLocations(nH, l3, l2);
		checkEdgeWithLocations(nH, l3, l4);
		checkEdgeWithLocations(nH, l4, l1);
		checkEdgeWithLocations(nH, l4, l2);
		checkEdgeWithLocations(nH, l4, l3);
		
		
	}

	public void test_getEdge_withRouteElementensWithoutWayPoints() {
		Location l1 = InputHandler.createLocation(0, 52.5438, 13.3848);
		Location l2 = InputHandler.createLocation(2, 52.5054, 13.428);
		Location l3 = InputHandler.createLocation(6, 6, 6);
		Location l4 = InputHandler.createLocation(4, 4, 4);

		List<Location> locations = new ArrayList<Location>();
		locations.add(l1);
		locations.add(l2);
		locations.add(l3);
		locations.add(l4);

		NetworkHandler nH = new NetworkHandler();
		nH.initNetwork(locations);

		RouteElement rE1 = SolutionHandler.createRequestActivityRouteElement(InputHandler.createRequestActivity(OriginRequestActivity.class));
		((RequestActivityRouteElement)rE1).getRequestActivity().setLocation(l1);
		RouteElement rE2 = SolutionHandler.createSupportRouteElement(l2);
		RouteElement rE3 = SolutionHandler.createRequestActivityRouteElement(InputHandler.createRequestActivity(DestinationRequestActivity.class));
		((RequestActivityRouteElement)rE3).getRequestActivity().setLocation(l1);
		RouteElement rE4 = SolutionHandler.createSupportRouteElement(l4);

		checkEdgeWithRouteElementensWithoutWayPoints(nH, rE1, rE2);
		checkEdgeWithRouteElementensWithoutWayPoints(nH, rE1, rE3);
		checkEdgeWithRouteElementensWithoutWayPoints(nH, rE1, rE4);
		checkEdgeWithRouteElementensWithoutWayPoints(nH, rE2, rE1);
		checkEdgeWithRouteElementensWithoutWayPoints(nH, rE2, rE3);
		checkEdgeWithRouteElementensWithoutWayPoints(nH, rE2, rE4);
		checkEdgeWithRouteElementensWithoutWayPoints(nH, rE3, rE1);
		checkEdgeWithRouteElementensWithoutWayPoints(nH, rE3, rE2);
		checkEdgeWithRouteElementensWithoutWayPoints(nH, rE3, rE4);
		checkEdgeWithRouteElementensWithoutWayPoints(nH, rE4, rE1);
		checkEdgeWithRouteElementensWithoutWayPoints(nH, rE4, rE2);
		checkEdgeWithRouteElementensWithoutWayPoints(nH, rE4, rE3);
	}

	public void test_getEdge_withRequestActivity() {
		Location l1 = InputHandler.createLocation(0, 52.5438, 13.3848);
		Location l2 = InputHandler.createLocation(2, 52.5054, 13.428);
		Location l3 = InputHandler.createLocation(2, 52.5054, 13.428);

		List<Location> locations = new ArrayList<Location>();
		locations.add(l1);
		locations.add(l2);
		locations.add(l3);

		NetworkHandler nH = new NetworkHandler();
		nH.initNetwork(locations);

		RequestActivity rA1 = InputHandler.createRequestActivity(OriginRequestActivity.class);
		rA1.setLocation(l1);
		RequestActivity rA2 = InputHandler.createRequestActivity(DestinationRequestActivity.class);
		rA2.setLocation(l2);
		RequestActivity rA3 = InputHandler.createRequestActivity(RequestActivity.class);
		rA3.setLocation(l3);

		checkEdgeWithRequestActivity(nH, rA1, rA2);
		checkEdgeWithRequestActivity(nH, rA2, rA1);
		checkEdgeWithRequestActivity(nH, rA1, rA3);
		checkEdgeWithRequestActivity(nH, rA3, rA1);
		checkEdgeWithRequestActivity(nH, rA2, rA3);
		checkEdgeWithRequestActivity(nH, rA3, rA2);
	}

	public void test_initNetwork_withDifferentLocationOrder() {
		NetworkHandler nH1 = new NetworkHandler();
		NetworkHandler nH2 = new NetworkHandler();
		List<Location> locations1 = CreateLocations1();
		List<Location> locations2 = CreateLocations2();
		nH1.initNetwork(locations1);
		nH2.initNetwork(locations2);

		for (int i = 0; i < nH1.getEdges().length; i++)
			for (int j = 0; j < nH1.getEdges().length; j++) {
				assertEquals(nH1.getEdges()[i][j].getCost(), nH2.getEdges()[i][j].getCost());
				assertEquals(nH1.getEdges()[i][j].getDistance(), nH2.getEdges()[i][j].getDistance());
				assertEquals(nH1.getEdges()[i][j].getDuration(), nH2.getEdges()[i][j].getDuration());
			}
	}

	public void test_initNetwork_withMissingIDs() {
		NetworkHandler nH = new NetworkHandler();
		List<Location> locations = CreateLocations3();
		nH.initNetwork(locations);

		assertTrue(existsLocationWithID(locations, nH.getEdges().length - 1));

		for (int i = 0; i < nH.getEdges().length; i++)
			for (int j = 0; j < nH.getEdges().length; j++) {
				if (nH.getEdges()[i][j] == null) {
					assertFalse(existsLocationWithID(locations, i) && existsLocationWithID(locations, j));
				} else {
					assertTrue(existsLocationWithID(locations, i));
					assertTrue(existsLocationWithID(locations, j));
				}
			}
	}

	public void test_addLocation_butLocationOrIdExistsAlredy() {
		
		NetworkHandler nH = new NetworkHandler();
		List<Location> locations = CreateLocations1();
		nH.initNetwork(locations);
		Location location = InputHandler.createLocation(2, 0.21, 0.22);
		int length = nH.getEdges().length;

		SuppressConsolOutput.turnON();
		nH.addLocation(locations.get(1));
		nH.addLocation(location);
		SuppressConsolOutput.turnOFF();
		
		assertEquals(length, nH.getEdges().length);
	}

	public void test_addLocation_withoutExpand() {
		NetworkHandler nH = new NetworkHandler();
		List<Location> locations = CreateLocations3();
		nH.initNetwork(locations);
		Location location = InputHandler.createLocation(5, 0.51, 0.52);

		int length = nH.getEdges().length;

		nH.addLocation(location);

		assertEquals(length, nH.getEdges().length);
	}

	public void test_addLocation_withExpand() {
		NetworkHandler nH = new NetworkHandler();
		List<Location> locations = CreateLocations1();
		nH.initNetwork(locations);
		Location location = InputHandler.createLocation(4, 0.51, 0.52);

		int length = nH.getEdges().length;

		nH.addLocation(location);

		assertEquals(length + 1, nH.getEdges().length);
	}

	public void test_addLocation_withExpandMoreThanOne() {
		NetworkHandler nH = new NetworkHandler();
		List<Location> locations = CreateLocations1();
		nH.initNetwork(locations);

		int id = 7;
		Location location = InputHandler.createLocation(id, 0.51, 0.52);

		nH.addLocation(location);

		assertEquals(id + 1, nH.getEdges().length);
	}

	public void test_setupNetwork_withAllEdges() {
		NetworkHandler nH = new NetworkHandler();
		nH.setupNetwork(CreateInputHandlerWithLocationsAndEdges());
	}

	public void test_SetupNetwork_WithEmptyEdges() {
		NetworkHandler nH = new NetworkHandler();
		List<Location> locations = CreateLocations3();
		nH.initNetwork(locations);

		InputHandler iH = new InputHandler();
		iH.getLocations().addAll(locations);

		assertTrue(existsLocationWithID(locations, nH.getEdges().length - 1));

		for (int i = 0; i < nH.getEdges().length; i++)
			for (int j = 0; j < nH.getEdges().length; j++) {
				if (nH.getEdges()[i][j] == null) {
					assertFalse(existsLocationWithID(locations, i) && existsLocationWithID(locations, j));
				} else {

					iH.addEdge(nH.getEdges()[i][j]);
				}
			}
		NetworkHandler nH2 = new NetworkHandler();
		nH2.setupNetwork(iH);

		for (int i = 0; i < nH.getEdges().length; i++)
			for (int j = 0; j < nH.getEdges().length; j++) {
				if (nH.getEdges()[i][j] == null) {
					assertFalse(existsLocationWithID(locations, i) && existsLocationWithID(locations, j));
				} else {
					assertTrue(existsLocationWithID(locations, i));
					assertTrue(existsLocationWithID(locations, j));
				}
			}
	}

	public void test_initShortestPath_withGraphhopper() {

		new SolutionHandler();
		NetworkHandler nH = new NetworkHandler();
		InputHandler iH = new InputHandler();
		iH.loadConfig("../config.xml");

		SuppressConsolOutput.turnON();
		nH.initShortestPath(iH.getConfig().getShortestPath());
		SuppressConsolOutput.turnOFF();

		WayPoint wayPoint1 = InputFactory.eINSTANCE.createWayPoint();
		wayPoint1.setLat(52.5438);
		wayPoint1.setLon(13.3848);
		WayPoint wayPoint2 = InputFactory.eINSTANCE.createWayPoint();
		wayPoint2.setLat(52.5054);
		wayPoint2.setLon(13.428);

		WayPointRouteElement wpRE1 = SolutionHandler.createWaypointRouteElement(wayPoint1);
		WayPointRouteElement wpRE2 = SolutionHandler.createWaypointRouteElement(wayPoint2);

		Edge edge = nH.getEdge(wpRE1, wpRE2);

		assertEquals(6814.5860438757, edge.getDistance());
		assertEquals(576797, edge.getDuration());

	}
	
	/*
	 * 
	 * check functions
	 * 
	 */
	
	private void checkEdgeWithLocations(NetworkHandler nH, Location l1, Location l2) {

		assertNotNull(nH.getEdge(l1, l2));
		assertEquals(l1.getId(), nH.getEdge(l1, l2).getStartLocation().getId());
		assertEquals(l2.getId(), nH.getEdge(l1, l2).getEndLocation().getId());
	}
	
	private void checkEdgeWithRequestActivity(NetworkHandler nH, RequestActivity rA1, RequestActivity rA2) {

		assertNotNull(nH.getEdge(rA1, rA2));
		assertEquals(rA1.getLocation().getId(), nH.getEdge(rA1, rA2).getStartLocation().getId());
		assertEquals(rA2.getLocation().getId(), nH.getEdge(rA1, rA2).getEndLocation().getId());
	}
	
	private void checkEdgeWithRouteElementensWithoutWayPoints(NetworkHandler nH, RouteElement rE1, RouteElement rE2) {

		assertNotNull(nH.getEdge(rE1, rE2));
		int id1 = SolutionHandler.getLocation(rE1).getId();
		int id2 = SolutionHandler.getLocation(rE2).getId();
		assertEquals(id1, nH.getEdge(rE1, rE2).getStartLocation().getId());
		assertEquals(id2, nH.getEdge(rE1, rE2).getEndLocation().getId());
	}
	
	/*
	 * 
	 * Create Objects
	 * 
	 */

	private List<Location> CreateLocations1() {
		List<Location> locations = new ArrayList<Location>();
		locations.add(InputHandler.createLocation(0, 0.11, 0.12));
		locations.add(InputHandler.createLocation(1, 0.21, 0.22));
		locations.add(InputHandler.createLocation(2, 0.31, 0.32));
		locations.add(InputHandler.createLocation(3, 0.41, 0.42));
		return locations;
	}

	private List<Location> CreateLocations2() {
		List<Location> locations = new ArrayList<Location>();
		locations.add(InputHandler.createLocation(2, 0.31, 0.32));
		locations.add(InputHandler.createLocation(0, 0.11, 0.12));
		locations.add(InputHandler.createLocation(3, 0.41, 0.42));
		locations.add(InputHandler.createLocation(1, 0.21, 0.22));
		return locations;
	}

	private List<Location> CreateLocations3() {
		List<Location> locations = new ArrayList<Location>();
		locations.add(InputHandler.createLocation(1, 0.11, 0.12));
		locations.add(InputHandler.createLocation(3, 0.21, 0.22));
		locations.add(InputHandler.createLocation(4, 0.31, 0.32));
		locations.add(InputHandler.createLocation(6, 0.41, 0.42));
		return locations;
	}

	private InputHandler CreateInputHandlerWithLocationsAndEdges() {
		InputHandler iH = new InputHandler();
		List<Location> locations = CreateLocations1();

		iH.getLocations().addAll(locations);

		for (int i = 0; i < locations.size(); i++)
			for (int j = 0; j < locations.size(); j++) {
				iH.addEdge(InputHandler.createEdge(locations.get(i), locations.get(j)));
			}

		return iH;
	}

	private boolean existsLocationWithID(List<Location> locations, int id) {
		for (Location location : locations)
			if (location.getId() == id)
				return true;
		return false;
	}
}
