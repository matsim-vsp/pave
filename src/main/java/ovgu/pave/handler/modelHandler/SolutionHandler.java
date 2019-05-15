package ovgu.pave.handler.modelHandler;

import java.util.List;

import ovgu.pave.model.input.Location;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.input.RequestActivity;
import ovgu.pave.model.solution.SolutionFactory;
import ovgu.pave.model.solution.SupportRouteElement;
import ovgu.pave.model.solution.UseVehicleType;
import ovgu.pave.model.solution.WayPointRouteElement;
import ovgu.pave.model.solution.RequestActivityRouteElement;
import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.RouteElement;
import ovgu.pave.model.solution.Solution;
import ovgu.pave.model.input.Vehicle;
import ovgu.pave.model.input.VehicleType;
import ovgu.pave.model.input.WayPoint;

public class SolutionHandler {

	/*
	 * 
	 * getter and setter
	 * 
	 */

	private Solution solution;

	public SolutionHandler() {
		resetSolution();
	}

	public void resetSolution() {
		solution = SolutionFactory.eINSTANCE.createSolution();
		solution.setUseVehicleType(SolutionFactory.eINSTANCE.createUseVehicleType());
	}

	public List<Request> getUnintegratedRequests() {
		return solution.getUnintegratedRequests();
	}

	public List<Request> getIntegratedRequests() {
		return solution.getIntegratedRequests();
	}

	public List<Route> getRoutes() {
		return solution.getRoutes();
	}

	public void initEmptyRoutesForVehicles(List<Vehicle> vehicles) {
		for (Vehicle vehicle : vehicles)
			solution.getRoutes().add(SolutionHandler.createRoute(vehicle));
	}

	public void setSolution(Solution solution) {
		this.solution = solution;
	}

	public void setScore(long value) {
		this.solution.setScore(value);
	}

	public long getScore() {
		return this.solution.getScore();
	}

	public void saveSolution(String configOutputFolder, String filename) {
		IOHandler.saveSolution(solution, configOutputFolder, filename);
	}

	public void loadSolution(String configOutputFolder, String filename, InputHandler inputHandler) {
		solution = IOHandler.loadSolution(configOutputFolder, filename, inputHandler.getInput());
	}

	public boolean areVehicleTypesUsed() {
		return solution.getUseVehicleType().isUseVehicleTypeInsteadVehicle();
	}

	/*
	 * 
	 * instance methods
	 * 
	 */

	public void initVehicleTypes(List<VehicleType> vehicleTypes) {
		for (VehicleType vehicleType : vehicleTypes) {
			solution.getUseVehicleType().getVehicleTypes().add(vehicleType);
			solution.getUseVehicleType().getVehicleTypeIDs().add(vehicleType.getId());

			// TODO: initial quantities ?
			solution.getUseVehicleType().getQuantities().add(0);
		}
		solution.getUseVehicleType().setUseVehicleTypeInsteadVehicle(true);
	}

	public List<VehicleType> getVehicleTypes() {
		return solution.getUseVehicleType().getVehicleTypes();
	}

	public int getVehicleTypeQuantity(VehicleType vehicleType) {
		for (int i = 0; i < solution.getUseVehicleType().getVehicleTypes().size(); i++) {
			if (solution.getUseVehicleType().getVehicleTypes().get(i).equals(vehicleType)) {
				return solution.getUseVehicleType().getQuantities().get(i);
			}
		}
		return -1;
	}

	public void changeVehicleTypeQuantity(VehicleType vehicleType, int quantity) {
		for (int i = 0; i < solution.getUseVehicleType().getVehicleTypes().size(); i++) {
			if (solution.getUseVehicleType().getVehicleTypes().get(i).equals(vehicleType)) {
				solution.getUseVehicleType().getQuantities().set(i, quantity);
			}
		}
	}

	public Solution copySolution() {
		return copySolution(false);
	}

	public Solution copyEmptySolution() {
		return copySolution(true);
	}

	private Solution copySolution(boolean reset) {
		Solution copy = SolutionFactory.eINSTANCE.createSolution();
		copy.setUseVehicleType(copyUseVehicleType(solution.getUseVehicleType()));

		for (Route route : solution.getRoutes())
			copy.getRoutes().add(copyRoute(route, copy.getUseVehicleType().isUseVehicleTypeInsteadVehicle(), reset));

		if (!reset) {
			copy.setScore(solution.getScore());
			copy.getUnintegratedRequests().addAll(solution.getUnintegratedRequests());
		}
		return copy;
	}

	private UseVehicleType copyUseVehicleType(UseVehicleType useVehicleType) {
		UseVehicleType copy = SolutionFactory.eINSTANCE.createUseVehicleType();
		copy.setUseVehicleTypeInsteadVehicle(useVehicleType.isUseVehicleTypeInsteadVehicle());
		copy.getVehicleTypes().addAll(useVehicleType.getVehicleTypes());
		copy.getVehicleTypeIDs().addAll(useVehicleType.getVehicleTypeIDs());
		copy.getQuantities().addAll(useVehicleType.getQuantities());
		return copy;
	}

	/*
	 * 
	 * static methods
	 * 
	 */

	public static Route createRoute(Vehicle vehicle) {
		Route route = SolutionFactory.eINSTANCE.createRoute();
		route.setVehicle(vehicle);
		route.setVehicleID(vehicle.getId());
		return route;
	}

	public static Route createRoute(VehicleType vehicleType) {
		Route route = SolutionFactory.eINSTANCE.createRoute();
		route.setVehicleType(vehicleType);
		route.setVehicleTypeID(vehicleType.getId());
		return route;
	}

	public static RequestActivityRouteElement createRequestActivityRouteElement(RequestActivity activity) {
		RequestActivityRouteElement routeElement = SolutionFactory.eINSTANCE.createRequestActivityRouteElement();
		routeElement.setRequestActivity(activity);
		routeElement.setServiceDuration(activity.getServiceDuration());
		routeElement.setRequestActivityID(activity.getId());
		return routeElement;
	}

	public static SupportRouteElement createSupportRouteElement(Location location) {
		SupportRouteElement routeElement = SolutionFactory.eINSTANCE.createSupportRouteElement();
		routeElement.setLocation(location);
		routeElement.setLocationID(location.getId());
		return routeElement;
	}

	public static WayPointRouteElement createWaypointRouteElement(WayPoint wayPoint) {
		WayPointRouteElement routeElement = SolutionFactory.eINSTANCE.createWayPointRouteElement();
		routeElement.setWayPoint(wayPoint);
		routeElement.setWayPointID(wayPoint.getId());
		return routeElement;
	}

	protected static Route copyRoute(Route route, boolean useVehicleType, boolean reset) {
		Route copy;
		if (useVehicleType)
			copy = createRoute(route.getVehicleType());
		else
			copy = createRoute(route.getVehicle());

		if (!reset) {
			copy.setUtilisation(route.getUtilisation());
			for (RouteElement routeElement : route.getRouteElements())
				copy.getRouteElements().add(copyRouteElement(routeElement));
		}
		return copy;
	}

	private static RouteElement copyRouteElement(RouteElement routeElement) {
		RouteElement copy = SolutionFactory.eINSTANCE.createRouteElement();
		if (isRequestActivityRouteElement(routeElement)) {
			RequestActivity activity = ((RequestActivityRouteElement) routeElement).getRequestActivity();
			copy = createRequestActivityRouteElement(activity);
		} else if (isSupportRouteElement(routeElement)) {
			Location location = ((SupportRouteElement) routeElement).getLocation();
			copy = createSupportRouteElement(location);
		} else if (isWayPointRouteElement(routeElement)) {
			WayPoint wayPoint = ((WayPointRouteElement) routeElement).getWayPoint();
			copy = createWaypointRouteElement(wayPoint);
		}

		copy.setServiceBegin(routeElement.getServiceBegin());
		copy.setServiceDuration(routeElement.getServiceDuration());
		return copy;
	}

	public static int getRouteElementIndex(Route route, RouteElement routeElement) {
		for (int i = 0; i < route.getRouteElements().size(); i++)
			if (route.getRouteElements().get(i) == routeElement)
				return i;
		return -1;
	}

	public static void insertActivity(Route route, RequestActivity activity, int index) {
		route.getRouteElements().add(index, createRequestActivityRouteElement(activity));
	}

	public static void insertRouteElement(Route route, RouteElement routeElement, int index) {
		route.getRouteElements().add(index, routeElement);
	}

	public static void insertRequest(Route route, Request request, int indexOrigin, int currentIndexDestination) {

		if (request.getDestinationActivity() != null) {
			insertActivity(route, request.getDestinationActivity(), currentIndexDestination);
		}
		insertActivity(route, request.getOriginActivity(), indexOrigin);
	}

	public static int findRouteElementIndex(Route route, RequestActivity requestActivity) {

		for (int i = 0; i < route.getRouteElements().size(); i++) {
			RouteElement routeElement = route.getRouteElements().get(i);
			if (isRequestActivityRouteElement(routeElement)) {
				RequestActivity foundRequestActivity = ((RequestActivityRouteElement) routeElement)
						.getRequestActivity();
				if (foundRequestActivity == requestActivity)
					return i;
			}
		}
		return -1;
	}

	public static RouteElement findRouteElement(Route route, RequestActivity requestActivity) {
		int index = findRouteElementIndex(route, requestActivity);
		if (index == -1)
			return null;
		else
			return route.getRouteElements().get(index);
	}

	public static int removeRequestFromRoute(Request request, Route route) {

		int indexOriginActivity = findRouteElementIndex(route, request.getOriginActivity());
		route.getRouteElements().remove(indexOriginActivity);

		if (request.getDestinationActivity() != null) {
			int indexOriginDestination = findRouteElementIndex(route, request.getDestinationActivity());
			route.getRouteElements().remove(indexOriginDestination);
		}
		return indexOriginActivity;
	}

	public static RequestActivity getRequestActivity(RouteElement routeElement) {
		if (isRequestActivityRouteElement(routeElement))
			return ((RequestActivityRouteElement) routeElement).getRequestActivity();
		else
			return null;
	}

	public static Request getRequest(RouteElement routeElement) {
		if (isRequestActivityRouteElement(routeElement))
			return getRequestActivity(routeElement).getRequest();
		else
			return null;
	}

	public static Location getLocation(RouteElement routeElement) {
		if (isRequestActivityRouteElement(routeElement))
			return ((RequestActivityRouteElement) routeElement).getRequestActivity().getLocation();
		else if (isSupportRouteElement(routeElement))
			return ((SupportRouteElement) routeElement).getLocation();
		else
			return null;
	}

	public static double getLatitude(RouteElement routeElement) {
		if (isRequestActivityRouteElement(routeElement))
			return ((RequestActivityRouteElement) routeElement).getRequestActivity().getLocation().getLat();
		else if (isSupportRouteElement(routeElement))
			return ((SupportRouteElement) routeElement).getLocation().getLat();
		else if (isWayPointRouteElement(routeElement))
			return ((WayPointRouteElement) routeElement).getWayPoint().getLat();
		else
			return -1;
	}

	public static double getLongitude(RouteElement routeElement) {
		if (isRequestActivityRouteElement(routeElement))
			return ((RequestActivityRouteElement) routeElement).getRequestActivity().getLocation().getLon();
		else if (isSupportRouteElement(routeElement))
			return ((SupportRouteElement) routeElement).getLocation().getLon();
		else if (isWayPointRouteElement(routeElement))
			return ((WayPointRouteElement) routeElement).getWayPoint().getLon();
		else
			return -1;
	}

	public static boolean isRequestActivityRouteElement(RouteElement routeElement) {
		return routeElement instanceof RequestActivityRouteElement;
	}

	public static boolean isWayPointRouteElement(RouteElement routeElement) {
		return routeElement instanceof WayPointRouteElement;
	}

	public static boolean isSupportRouteElement(RouteElement routeElement) {
		return routeElement instanceof SupportRouteElement;
	}

	public static long getServiceEnd(Route route, int index) {
		return route.getRouteElements().get(index).getServiceBegin()
				+ route.getRouteElements().get(index).getServiceDuration();
	}

	public static long getChangeInQuantity(RouteElement routeElement) {
		long quantity = 0;
		if (isRequestActivityRouteElement(routeElement)) {
			RequestActivity activity = SolutionHandler.getRequestActivity(routeElement);
			if (InputHandler.isOriginRequestActivity(activity)) {
				quantity += activity.getRequest().getQuantity();
			} else {
				quantity -= activity.getRequest().getQuantity();
			}
		}
		return quantity;
	}

	public static long getUtilization(Route route, int index) {
		long utilization = route.getUtilisation();
		for (int i = 0; i <= index; i++) {
			utilization += getChangeInQuantity(route.getRouteElement(i));
		}
		return utilization;
	}
}
