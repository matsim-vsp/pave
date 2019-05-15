package ovgu.pave.handler.modelHandler;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ovgu.pave.handler.modelHandler.InputHandler;
import ovgu.pave.model.input.DestinationRequestActivity;
import ovgu.pave.model.input.Edge;
import ovgu.pave.model.input.Location;
import ovgu.pave.model.input.OriginRequestActivity;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.input.RequestActivity;
import ovgu.pave.model.input.Vehicle;
import ovgu.pave.model.input.VehicleType;
import ovgu.pave.model.input.WayPoint;

public class InputHandlerTests extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public InputHandlerTests(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(InputHandlerTests.class);
	}

	/**
	 * Rigourous Test :-)
	 */

	public void test_findWayPoint() {
		InputHandler iH = new InputHandler();
		List<Location> locations = new ArrayList<Location>();
		locations.add(InputHandler.createLocation(0, 11, 12));
		locations.add(InputHandler.createLocation(1, 21, 22));
		locations.add(InputHandler.createLocation(2, 31, 32));
		locations.add(InputHandler.createLocation(3, 41, 42));

		iH.getLocations().addAll(locations);
		List<Edge> edges = new ArrayList<Edge>();

		for (int i = 0; i < locations.size(); i++)
			for (int j = 0; j < locations.size(); j++) {
				Edge edge = InputHandler.createEdge(locations.get(i), locations.get(j));
				edge.getWaypoints().add(InputHandler.createWayPoint(edge.getId(), 0, 0, 0, 0, 0));
				edge.getWaypoints().add(InputHandler.createWayPoint(edge.getId(), 1, 0, 0, 0, 0));
				edge.getWaypoints().add(InputHandler.createWayPoint(edge.getId(), 2, 0, 0, 0, 0));
				edge.getWaypoints().add(InputHandler.createWayPoint(edge.getId(), 3, 0, 0, 0, 0));
				edge.getWaypoints().add(InputHandler.createWayPoint(edge.getId(), 4, 0, 0, 0, 0));
				edge.getWaypoints().add(InputHandler.createWayPoint(edge.getId(), 5, 0, 0, 0, 0));
				edges.add(edge);
				iH.addEdge(edge);
			}


		WayPoint wp1 = edges.get(0).getWaypoints().get(2);
		WayPoint wp2 = edges.get(4).getWaypoints().get(0);
		WayPoint wp3 = edges.get(13).getWaypoints().get(5);
		

		assertEquals(wp1, iH.findWayPoint(wp1.getId()));
		assertEquals(wp2, iH.findWayPoint(wp2.getId()));
		assertEquals(wp3, iH.findWayPoint(wp3.getId()));
		assertEquals(edges.get(0).getWaypoints().get(2), iH.findWayPoint(wp1.getId()));
		assertEquals(wp2, iH.findWayPoint(edges.get(4).getWaypoints().get(0).getId()));
		assertEquals(edges.get(13).getWaypoints().get(5), iH.findWayPoint(edges.get(13).getWaypoints().get(5).getId()));

		assertNotNull(iH.findWayPoint("f.0.t.0-wp-0"));
		assertNotNull(iH.findWayPoint("f.3.t.1-wp-2"));
		assertNotNull(iH.findWayPoint("f.1.t.3-wp-2"));
		assertNull(iH.findWayPoint("f.0.t.0-wp-23"));
		assertNull(iH.findWayPoint("f.1.t.2-wp5165"));
		assertNull(iH.findWayPoint("f.1.t.2wp5165"));
		assertNull(iH.findWayPoint("22.o"));
		assertNull(iH.findWayPoint("2.2d"));
		assertNull(iH.findWayPoint("hu.hu"));
		assertNull(iH.findWayPoint("2.2"));
		assertNull(iH.findWayPoint("22o"));
		assertNull(iH.findWayPoint("22d"));
		assertNull(iH.findWayPoint("2-2d"));
		assertNull(iH.findWayPoint("d-sfd"));
		assertNull(iH.findWayPoint("22-22"));
		assertNull(iH.findWayPoint("huhu"));
		assertNull(iH.findWayPoint("22"));
		
		
		
	}

	public void test_findRequestActivity() {

		Location l1 = InputHandler.createLocation(2, 0.0, 0.0);
		Location l2 = InputHandler.createLocation(5, 0.0, 0.0);
		Location l3 = InputHandler.createLocation(0, 0.0, 0.0);
		Location l4 = InputHandler.createLocation(6, 0.0, 0.0);
		Location l5 = InputHandler.createLocation(7, 0.0, 0.0);
		Location l6 = InputHandler.createLocation(9, 0.0, 0.0);
		Location l7 = InputHandler.createLocation(1, 0.0, 0.0);
		Location l8 = InputHandler.createLocation(10, 0.0, 0.0);
		Location l9 = InputHandler.createLocation(8, 0.0, 0.0);
		Location l10 = InputHandler.createLocation(4, 0.0, 0.0);
		Location l11 = InputHandler.createLocation(3, 0.0, 0.0);
		Location l12 = InputHandler.createLocation(11, 0.0, 0.0);
		Location l13 = InputHandler.createLocation(12, 0.0, 0.0);
		Location l14 = InputHandler.createLocation(13, 0.0, 0.0);

		Request r1 = InputHandler.createRequest(4, l1, l8);
		Request r2 = InputHandler.createRequest(5, l2, l9);
		Request r3 = InputHandler.createRequest(0, l3, l10);
		Request r4 = InputHandler.createRequest(6, l4, l11);
		Request r5 = InputHandler.createRequest(7, l5, l12);
		Request r6 = InputHandler.createRequest(9, l6, l13);
		Request r7 = InputHandler.createRequest(1, l7, l14);

		InputHandler iH = new InputHandler();

		iH.getAcceptedRequests().add(r1);
		iH.getAcceptedRequests().add(r2);
		iH.getAcceptedRequests().add(r3);
		iH.getAcceptedRequests().add(r4);
		iH.getAcceptedRequests().add(r5);
		iH.getAcceptedRequests().add(r6);
		iH.getAcceptedRequests().add(r7);

		assertEquals(r2.getDestinationActivity(), iH.findAcceptedRequestActivity(r2.getDestinationActivity().getId()));
		assertEquals(r7.getDestinationActivity(), iH.findAcceptedRequestActivity(r7.getDestinationActivity().getId()));
		assertEquals(r5.getOriginActivity(), iH.findAcceptedRequestActivity(r5.getOriginActivity().getId()));
		assertEquals(r3.getDestinationActivity(), iH.findAcceptedRequestActivity(r3.getDestinationActivity().getId()));
		assertEquals(r3.getOriginActivity(), iH.findAcceptedRequestActivity(r3.getOriginActivity().getId()));
		assertEquals(r6.getDestinationActivity(), iH.findAcceptedRequestActivity(r6.getDestinationActivity().getId()));
		assertEquals(r6.getOriginActivity(), iH.findAcceptedRequestActivity(r6.getOriginActivity().getId()));
		assertNull(iH.findAcceptedRequestActivity("22o"));
		assertNull(iH.findAcceptedRequestActivity("22d"));
		assertNull(iH.findAcceptedRequestActivity("2-2d"));
		assertNull(iH.findAcceptedRequestActivity("d-sfd"));
		assertNull(iH.findAcceptedRequestActivity("22-22"));
		assertNull(iH.findAcceptedRequestActivity("huhu"));
		assertNull(iH.findAcceptedRequestActivity("22"));

	}

	public void test_findRequest() {
		Request r1 = InputHandler.createRequest(2, null, null);
		Request r2 = InputHandler.createRequest(5, null, null);
		Request r3 = InputHandler.createRequest(0, null, null);
		Request r4 = InputHandler.createRequest(6, null, null);
		Request r5 = InputHandler.createRequest(7, null, null);
		Request r6 = InputHandler.createRequest(9, null, null);
		Request r7 = InputHandler.createRequest(1, null, null);

		InputHandler iH = new InputHandler();

		iH.getAcceptedRequests().add(r1);
		iH.getAcceptedRequests().add(r2);
		iH.getAcceptedRequests().add(r3);
		iH.getAcceptedRequests().add(r4);
		iH.getAcceptedRequests().add(r5);
		iH.getAcceptedRequests().add(r6);
		iH.getAcceptedRequests().add(r7);

		assertEquals(r3, iH.findAcceptedRequest(0));
		assertEquals(r2, iH.findAcceptedRequest(5));
		assertEquals(r6, iH.findAcceptedRequest(9));
		assertNull(iH.findAcceptedRequest(22));
	}

	public void test_findVehicleType() {
		VehicleType vT1 = InputHandler.createVehicleType(2, 0);
		VehicleType vT2 = InputHandler.createVehicleType(5, 0);
		VehicleType vT3 = InputHandler.createVehicleType(0, 0);
		VehicleType vT4 = InputHandler.createVehicleType(6, 0);
		VehicleType vT5 = InputHandler.createVehicleType(7, 0);
		VehicleType vT6 = InputHandler.createVehicleType(9, 0);
		VehicleType vT7 = InputHandler.createVehicleType(1, 0);

		InputHandler iH = new InputHandler();

		iH.getVehicleTypes().add(vT1);
		iH.getVehicleTypes().add(vT2);
		iH.getVehicleTypes().add(vT3);
		iH.getVehicleTypes().add(vT4);
		iH.getVehicleTypes().add(vT5);
		iH.getVehicleTypes().add(vT6);
		iH.getVehicleTypes().add(vT7);

		assertEquals(vT3, iH.findVehicleType(0));
		assertEquals(vT4, iH.findVehicleType(6));
		assertEquals(vT6, iH.findVehicleType(9));
		assertNull(iH.findVehicleType(22));
	}

	public void test_findVehicle() {
		Vehicle v1 = InputHandler.createVehicle(2, null, null, null);
		Vehicle v2 = InputHandler.createVehicle(5, null, null, null);
		Vehicle v3 = InputHandler.createVehicle(0, null, null, null);
		Vehicle v4 = InputHandler.createVehicle(6, null, null, null);
		Vehicle v5 = InputHandler.createVehicle(7, null, null, null);
		Vehicle v6 = InputHandler.createVehicle(9, null, null, null);
		Vehicle v7 = InputHandler.createVehicle(1, null, null, null);

		InputHandler iH = new InputHandler();

		iH.getVehicles().add(v1);
		iH.getVehicles().add(v2);
		iH.getVehicles().add(v3);
		iH.getVehicles().add(v4);
		iH.getVehicles().add(v5);
		iH.getVehicles().add(v6);
		iH.getVehicles().add(v7);

		assertEquals(v3, iH.findVehicle(0));
		assertEquals(v5, iH.findVehicle(7));
		assertEquals(v6, iH.findVehicle(9));
		assertNull(iH.findVehicle(22));
	}

	public void test_findLocation() {
		Location l1 = InputHandler.createLocation(2, 0.0, 0.0);
		Location l2 = InputHandler.createLocation(5, 0.0, 0.0);
		Location l3 = InputHandler.createLocation(0, 0.0, 0.0);
		Location l4 = InputHandler.createLocation(6, 0.0, 0.0);
		Location l5 = InputHandler.createLocation(7, 0.0, 0.0);
		Location l6 = InputHandler.createLocation(9, 0.0, 0.0);
		Location l7 = InputHandler.createLocation(1, 0.0, 0.0);

		InputHandler iH = new InputHandler();

		iH.getLocations().add(l1);
		iH.getLocations().add(l2);
		iH.getLocations().add(l3);
		iH.getLocations().add(l4);
		iH.getLocations().add(l5);
		iH.getLocations().add(l6);
		iH.getLocations().add(l7);

		assertEquals(l3, iH.findLocation(0));
		assertEquals(l2, iH.findLocation(5));
		assertEquals(l6, iH.findLocation(9));
		assertNull(iH.findLocation(22));
	}

	public void test_createRequestActivity() {

		RequestActivity ra = InputHandler.createRequestActivity(RequestActivity.class);
		OriginRequestActivity ora = InputHandler.createRequestActivity(OriginRequestActivity.class);
		DestinationRequestActivity dra = InputHandler.createRequestActivity(DestinationRequestActivity.class);

		assertTrue(InputHandler.isRequestActivity(ra));
		assertTrue(InputHandler.isRequestActivity(ora));
		assertTrue(InputHandler.isRequestActivity(dra));
		assertTrue(InputHandler.isOriginRequestActivity(ora));
		assertTrue(InputHandler.isDestinationRequestActivity(dra));

		assertFalse(InputHandler.isOriginRequestActivity(ra));
		assertFalse(InputHandler.isDestinationRequestActivity(ra));
		assertFalse(InputHandler.isDestinationRequestActivity(ora));
		assertFalse(InputHandler.isOriginRequestActivity(dra));

	}
}
