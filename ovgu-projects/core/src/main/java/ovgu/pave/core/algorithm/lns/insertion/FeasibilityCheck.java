package ovgu.pave.core.algorithm.lns.insertion;

import ovgu.pave.handler.Handler;
import ovgu.pave.handler.modelHandler.SolutionHandler;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.input.RequestActivity;
import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.RouteElement;

public class FeasibilityCheck {

	private  Route route;
	private  long time;
	private  long capacity;
	private  long utilization;

	public  boolean checkOriginActivity(Route currentRoute, RequestActivity originActivity, int startIndex,
			int endIndex) {

		RouteElement originActivityRouteElement = SolutionHandler
				.createRequestActivityRouteElement(originActivity);

		route = currentRoute;
		capacity = currentRoute.getVehicle().getType().getCapacity();
		utilization = SolutionHandler.getUtilization(route, startIndex - 1);
		time = SolutionHandler.getServiceEnd(route, startIndex - 1);
		
		if (!updateTime(route.getRouteElement(startIndex - 1), originActivityRouteElement))
			return false;
		if (startIndex == endIndex)
			return true;
		if (!updateTime(originActivityRouteElement, route.getRouteElement(startIndex)))
			return false;
		if (!checkRouteElements(startIndex, endIndex))
			return false;
	
		return true;
	}

	public boolean checkDestinationActivity(Route currentRoute, Request request, int originIndex,
			int destinationIndex) {

		RouteElement originActivityRouteElement = SolutionHandler
				.createRequestActivityRouteElement(request.getOriginActivity());
		RouteElement destinationActivityRouteElement = SolutionHandler
				.createRequestActivityRouteElement(request.getDestinationActivity());

		if (!checkOriginActivity(currentRoute, request.getOriginActivity(), originIndex, destinationIndex))
			return false;
		if (originIndex == destinationIndex) {
			if(!updateTime(originActivityRouteElement, destinationActivityRouteElement)) return false;
		} else {
			if(!updateTime(route.getRouteElement(destinationIndex - 1), destinationActivityRouteElement)) return false;
		}
		if (!updateTime(destinationActivityRouteElement, route.getRouteElement(destinationIndex))) return false;
		if (!checkRouteElements(destinationIndex, route.getRouteElements().size() - 1)) return false;
		
		return true;
	}

	private boolean checkRouteElements(int startIndex, int endIndex) {

		for (int i = startIndex; i < endIndex - 1; i++) {
			if (!updateTime(route.getRouteElement(i), route.getRouteElement(i + 1))) {
				return false;
			}
		}
		return true;
	}

	private boolean updateTime(RouteElement currentRouteElement, RouteElement nextRouteElement) {
		
		boolean isRequestActivity = SolutionHandler.isRequestActivityRouteElement(nextRouteElement);
		
		if (isRequestActivity) { 	
			utilization += SolutionHandler.getChangeInQuantity(nextRouteElement);
			if(utilization > capacity) return false;
		
			if (time < SolutionHandler.getRequest(nextRouteElement).getReceivingTime())
				time = SolutionHandler.getRequest(nextRouteElement).getReceivingTime();
			}

		time += Handler.getNetwork().getTravelDuration(currentRouteElement, nextRouteElement);
	
		if (isRequestActivity) {			
			if (time > SolutionHandler.getRequestActivity(nextRouteElement).getLatestArrival())
				return false;
			else if (time < SolutionHandler.getRequestActivity(nextRouteElement).getEarliestArrival())
				time = SolutionHandler.getRequestActivity(nextRouteElement).getEarliestArrival();
			time += SolutionHandler.getRequestActivity(nextRouteElement).getServiceDuration();
		}
		return true;
	}
}
