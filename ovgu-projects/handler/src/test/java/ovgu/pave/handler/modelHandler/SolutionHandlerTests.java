package ovgu.pave.handler.modelHandler;

import java.util.List;

import org.eclipse.emf.ecore.util.EcoreUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import ovgu.pave.model.input.DestinationRequestActivity;
import ovgu.pave.model.input.Location;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.input.RequestActivity;
import ovgu.pave.model.input.VehicleType;
import ovgu.pave.model.input.WayPoint;
import ovgu.pave.model.solution.RequestActivityRouteElement;
import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.RouteElement;
import ovgu.pave.model.solution.Solution;

public class SolutionHandlerTests extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */
	public SolutionHandlerTests(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(SolutionHandlerTests.class);
	}

	/**
	 * Rigourous Test :-)
	 */

	public void test_initVehicleTypes() {
		List<VehicleType> vehicleTypes = GenerateTestData.generateTestVehicleTypes();
		SolutionHandler sH = new SolutionHandler();
		sH.initVehicleTypes(vehicleTypes);

		assertEquals(vehicleTypes, sH.getVehicleTypes());
	}

	public void test_get_change_VehicleTypeQuantity() {
		List<VehicleType> vehicleTypes = GenerateTestData.generateTestVehicleTypes();
		VehicleType vehicleType1 = vehicleTypes.get(1);
		VehicleType vehicleType2 = InputHandler.createVehicleType(7, 0);
		SolutionHandler sH = new SolutionHandler();
		sH.initVehicleTypes(vehicleTypes);

		int qunatity1 = sH.getVehicleTypeQuantity(vehicleType1);
		int qunatity2 = sH.getVehicleTypeQuantity(vehicleType2);

		int newQuantity1 = 2;
		int newQuantity2 = 3;

		sH.changeVehicleTypeQuantity(vehicleType1, newQuantity1);
		sH.changeVehicleTypeQuantity(vehicleType2, newQuantity2);

		// change quantity of existing type
		assertNotSame(qunatity1, sH.getVehicleTypeQuantity(vehicleType1));
		assertEquals(newQuantity1, sH.getVehicleTypeQuantity(vehicleType1));

		// change quantity of non existing type
		assertEquals(-1, sH.getVehicleTypeQuantity(vehicleType2));
		assertEquals(qunatity2, sH.getVehicleTypeQuantity(vehicleType2));
		assertNotSame(newQuantity2, sH.getVehicleTypeQuantity(vehicleType2));

	}

	public void test_copySolution_copyDynamicSolution() {
		copySolution_Test(true);
		copySolution_Test(false);
		copyDynamicSolution_Test(true);
		copyDynamicSolution_Test(false);
	}

	private void copySolution_Test(boolean useVehicleType) {
		SolutionHandler sH = new SolutionHandler();
		Solution solution = GenerateTestData.generateTestSolution(IOTests.loadInput(), useVehicleType);
		sH.setSolution(solution);

		Solution copy1 = sH.copySolution();
		Solution copy2 = sH.copySolution();

		assertTrue(EcoreUtil.equals(copy2, solution));
		assertTrue(EcoreUtil.equals(copy2, copy1));

		copy2.getRoutes().get(0).getRouteElements().get(0).setServiceDuration(4444444);

		assertTrue(EcoreUtil.equals(copy1, solution));
		assertFalse(EcoreUtil.equals(copy2, solution));
	}

	private void copyDynamicSolution_Test(boolean useVehicleType) {
		SolutionHandler sH = new SolutionHandler();
		Solution solution = GenerateTestData.generateTestSolution(IOTests.loadInput(), useVehicleType);
		sH.setSolution(solution);

		Solution copy1 = sH.copyEmptySolution();
		Solution copy2 = sH.copyEmptySolution();

		assertFalse(EcoreUtil.equals(copy1, solution));
		assertFalse(EcoreUtil.equals(copy2, solution));
		assertTrue(EcoreUtil.equals(copy2, copy1));

		solution.setScore(0);
		solution.getUnintegratedRequests().clear();
		for (Route route : solution.getRoutes())
			route.getRouteElements().clear();

		assertTrue(EcoreUtil.equals(copy1, solution));
		assertTrue(EcoreUtil.equals(copy2, solution));
	}

	public void test_getRouteElementIndex() {
		int index1 = 0;
		int index2 = 3;
		Route route = GenerateTestData.generateTestRoute();
		RouteElement routeElement1 = route.getRouteElement(index1);
		RouteElement routeElement2 = route.getRouteElement(index2);

		assertEquals(index1, SolutionHandler.getRouteElementIndex(route, routeElement1));
		assertEquals(index2, SolutionHandler.getRouteElementIndex(route, routeElement2));
		assertEquals(index1, SolutionHandler.getRouteElementIndex(route, routeElement1));

		assertNotSame(index2, SolutionHandler.getRouteElementIndex(route, routeElement1));
		assertNotSame(index1, SolutionHandler.getRouteElementIndex(route, routeElement2));
	}

	public void test_insertActivity_insertRouteElement() {
		insert_Activity_RouteElement_Test(0, false);
		insert_Activity_RouteElement_Test(2, false);
		insert_Activity_RouteElement_Test(5, false);

		insert_Activity_RouteElement_Test(0, true);
		insert_Activity_RouteElement_Test(3, true);
		insert_Activity_RouteElement_Test(5, true);
	}

	public void insert_Activity_RouteElement_Test(int index, boolean useRouteElement) {
		Route route1 = GenerateTestData.generateTestRoute();
		Route route2 = SolutionHandler.copyRoute(route1, false, false);
		RequestActivity activity = InputHandler.createRequestActivity(DestinationRequestActivity.class);
		RouteElement routeElement = SolutionHandler.createSupportRouteElement(InputHandler.createLocation(1, 33, 44));

		if (useRouteElement)
			SolutionHandler.insertRouteElement(route1, routeElement, index);
		else
			SolutionHandler.insertActivity(route1, activity, index);

		assertEquals(route1.getRouteElements().size(), route2.getRouteElements().size() + 1);
		for (int i = 0; i < route1.getRouteElements().size(); i++) {
			if (i < index)
				assertTrue(EcoreUtil.equals(route1.getRouteElements().get(i), route2.getRouteElements().get(i)));
			else if (i == index)
				if (useRouteElement)
					assertTrue(EcoreUtil.equals(routeElement, route1.getRouteElements().get(i)));
				else
					assertTrue(EcoreUtil.equals(activity,
							((RequestActivityRouteElement) route1.getRouteElements().get(i)).getRequestActivity()));
			else if (i > index)
				assertTrue(EcoreUtil.equals(route1.getRouteElements().get(i), route2.getRouteElements().get(i - 1)));
		}
	}

	public void test_insertRequest() {
		Route route1 = GenerateTestData.generateTestRoute();
		Route route2 = SolutionHandler.copyRoute(route1, false, false);
		Request request1 = InputHandler.createRequest(2, InputHandler.createLocation(1, 33, 44),
				InputHandler.createLocation(1, 22, 55));
		Request request2 = InputHandler.createRequest(2, InputHandler.createLocation(1, 33, 44), null);
		int indexOrigin1 = 1;
		int currentIndexDestination1 = 2;
		int indexOrigin2 = 0;
		int currentIndexDestination2 = 3;

		SolutionHandler.insertRequest(route1, request1, indexOrigin1, currentIndexDestination1);
		SolutionHandler.insertRequest(route1, request2, indexOrigin2, currentIndexDestination2);

		assertEquals(route1.getRouteElements().size(), route2.getRouteElements().size() + 3);
		for (int i = 0; i < route1.getRouteElements().size(); i++) {
			if (i == 0) // indexOrigin2
				assertTrue(EcoreUtil.equals(request2.getOriginActivity(),
						((RequestActivityRouteElement) route1.getRouteElements().get(i)).getRequestActivity()));
			else if (i == 1)
				assertTrue(EcoreUtil.equals(route1.getRouteElements().get(i), route2.getRouteElements().get(i - 1)));
			else if (i == 2) // indexOrigin1 + 1 (because of origin 2 is before)
				assertTrue(EcoreUtil.equals(request1.getOriginActivity(),
						((RequestActivityRouteElement) route1.getRouteElements().get(i)).getRequestActivity()));
			else if (i == 3)
				assertTrue(EcoreUtil.equals(route1.getRouteElements().get(i), route2.getRouteElements().get(i - 2)));
			else if (i == 4) // currentIndexDestination1 + 1 (Origin1) + 1 (Origin2)
				assertTrue(EcoreUtil.equals(request1.getDestinationActivity(),
						((RequestActivityRouteElement) route1.getRouteElements().get(i)).getRequestActivity()));
			else if (i > 3)
				assertTrue(EcoreUtil.equals(route1.getRouteElements().get(i), route2.getRouteElements().get(i - 3)));
		}
	}

	public void test_findRouteElementIndex() {
		Route route = GenerateTestData.generateTestRoute();
		int routeElementIndex = 3;

		RequestActivity requestActivity = ((RequestActivityRouteElement) route.getRouteElements()
				.get(routeElementIndex)).getRequestActivity();
		int index = SolutionHandler.findRouteElementIndex(route, requestActivity);

		assertEquals(routeElementIndex, index);
	}

	public void test_findRouteElement() {
		Route route = GenerateTestData.generateTestRoute();
		RouteElement routeElement = route.getRouteElements().get(3);

		RequestActivity requestActivity = ((RequestActivityRouteElement) routeElement).getRequestActivity();
		RouteElement rE = SolutionHandler.findRouteElement(route, requestActivity);

		assertEquals(routeElement, rE);
	}

	public void test_removeRequestFromRoute() {
		Route route1 = GenerateTestData.generateTestRoute();
		Request request = ((RequestActivityRouteElement) route1.getRouteElements().get(3)).getRequestActivity()
				.getRequest();

		SolutionHandler.removeRequestFromRoute(request, route1);

		for (RouteElement routeElement : route1.getRouteElements()) {
			if (SolutionHandler.isRequestActivityRouteElement(routeElement)) {
				assertNotSame(request, ((RequestActivityRouteElement) routeElement).getRequestActivity().getRequest());
			}
		}

		Route route2 = GenerateTestData.generateTestRoute();
		assertEquals(route2.getRouteElements().size() - 2, route1.getRouteElements().size());

		int indexRoute1 = 0;
		int indexRoute2 = 0;
		do {
			RouteElement re1 = route1.getRouteElements().get(indexRoute1);
			RouteElement re2 = route2.getRouteElements().get(indexRoute2);

			if (SolutionHandler.isRequestActivityRouteElement(re2)) {
				Request thisRequest = ((RequestActivityRouteElement) re2).getRequestActivity().getRequest();
				if (EcoreUtil.equals(thisRequest, request)) {
					indexRoute2++;
					break;
				}
			} else
				assertTrue(EcoreUtil.equals(re2, re1));
			indexRoute1++;
			indexRoute2++;
		} while (indexRoute1 < route2.getRouteElements().size() && indexRoute2 < route2.getRouteElements().size());
	}

	public void test_getRequestActivity_getRequest_getLocation_getLatitude_getLongitude_isWhichRouteElement() {

		double lat1 = 111;
		double lat2 = 222;
		double lat3 = 333;
		double lon1 = 11;
		double lon2 = 22;
		double lon3 = 33;
		Location location1 = InputHandler.createLocation(0, lat1, lon1);
		Location location2 = InputHandler.createLocation(2, lat2, lon2);
		Request request1 = InputHandler.createRequest(2, location1, null);
		RequestActivity ra1 = request1.getOriginActivity();

		WayPoint wp3 = InputHandler.createWayPoint("", 0, lat3, lon3, 0, 0);
		RouteElement routeElement1 = SolutionHandler.createRequestActivityRouteElement(ra1);
		RouteElement routeElement2 = SolutionHandler.createSupportRouteElement(location2);
		RouteElement routeElement3 = SolutionHandler.createWaypointRouteElement(wp3);

		// test getRequestActivity
		assertEquals(ra1, SolutionHandler.getRequestActivity(routeElement1));
		assertNull(SolutionHandler.getRequestActivity(routeElement2));
		assertNull(SolutionHandler.getRequestActivity(routeElement3));

		// test getRequest
		assertEquals(request1, SolutionHandler.getRequest(routeElement1));
		assertNull(SolutionHandler.getRequest(routeElement2));
		assertNull(SolutionHandler.getRequest(routeElement3));

		// test getLocation
		assertEquals(location1, SolutionHandler.getLocation(routeElement1));
		assertEquals(location2, SolutionHandler.getLocation(routeElement2));
		assertNull(SolutionHandler.getLocation(routeElement3));

		// test getLatitude
		assertEquals(lat1, SolutionHandler.getLatitude(routeElement1));
		assertEquals(lat2, SolutionHandler.getLatitude(routeElement2));
		assertEquals(lat3, SolutionHandler.getLatitude(routeElement3));

		// test getLongitude
		assertEquals(lon1, SolutionHandler.getLongitude(routeElement1));
		assertEquals(lon2, SolutionHandler.getLongitude(routeElement2));
		assertEquals(lon3, SolutionHandler.getLongitude(routeElement3));

		// test isWhichRouteElement
		assertTrue(SolutionHandler.isRequestActivityRouteElement(routeElement1));
		assertFalse(SolutionHandler.isRequestActivityRouteElement(routeElement2));
		assertFalse(SolutionHandler.isRequestActivityRouteElement(routeElement3));

		assertFalse(SolutionHandler.isSupportRouteElement(routeElement1));
		assertTrue(SolutionHandler.isSupportRouteElement(routeElement2));
		assertFalse(SolutionHandler.isSupportRouteElement(routeElement3));

		assertFalse(SolutionHandler.isWayPointRouteElement(routeElement1));
		assertFalse(SolutionHandler.isWayPointRouteElement(routeElement2));
		assertTrue(SolutionHandler.isWayPointRouteElement(routeElement3));
	}

}
