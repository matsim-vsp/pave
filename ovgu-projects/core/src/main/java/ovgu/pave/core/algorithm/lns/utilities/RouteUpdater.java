package ovgu.pave.core.algorithm.lns.utilities;

import ovgu.pave.handler.Handler;
import ovgu.pave.handler.modelHandler.SolutionHandler;
import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.RouteElement;

public class RouteUpdater {
	public static void updateServiceBeginnAll(Route route, int index) {
		for (int i = index; i < route.getRouteElements().size(); i++) {
			updateServiceBeginn(route, i);
		}
	}

	private static void updateServiceBeginn(Route route, int index) {
		RouteElement prevElement = route.getRouteElements().get(index - 1);
		RouteElement currentElement = route.getRouteElements().get(index);
		boolean isRequestActivity = SolutionHandler.isRequestActivityRouteElement(currentElement);
	
		long newServiceBegin = SolutionHandler.getServiceEnd(route, index - 1);
		if (isRequestActivity && newServiceBegin < SolutionHandler.getRequest(currentElement).getReceivingTime()) {
			newServiceBegin =  SolutionHandler.getRequest(currentElement).getReceivingTime();
		}
					
		newServiceBegin += Handler.getNetwork().getTravelDuration(prevElement, currentElement);
		
		if (isRequestActivity) {
			if (newServiceBegin < SolutionHandler.getRequestActivity(currentElement).getEarliestArrival()) {
				currentElement.setServiceBegin(SolutionHandler.getRequestActivity(currentElement).getEarliestArrival());
			} else {
				currentElement.setServiceBegin(newServiceBegin);
			}
		}
	}
}

