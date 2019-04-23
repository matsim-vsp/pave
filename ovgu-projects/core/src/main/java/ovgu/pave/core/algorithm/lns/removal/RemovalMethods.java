package ovgu.pave.core.algorithm.lns.removal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import ovgu.pave.core.algorithm.lns.utilities.RouteUpdater;
import ovgu.pave.handler.Handler;
import ovgu.pave.handler.modelHandler.AlgorithmObjectsHandler;
import ovgu.pave.handler.modelHandler.InputHandler;
import ovgu.pave.handler.modelHandler.SolutionHandler;
import ovgu.pave.model.algorithmObjects.RemovalOption;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.RouteElement;

public class RemovalMethods {
	public static ArrayList<RemovalOption> collectRemovalInformation() {

		ArrayList<RemovalOption> removelInformation = new ArrayList<RemovalOption>();

		RouteElement routeElement;
		RemovalOption removalOption;

		for (Route route : Handler.getSolution().getRoutes()) {
			int searchStartIndex = removelInformation.size();
			for (int i = 1; i < route.getRouteElements().size() - 1; i++) {
				routeElement = route.getRouteElement(i);
				if (SolutionHandler.isRequestActivityRouteElement(routeElement)) {
					if (InputHandler.isDestinationRequestActivity(SolutionHandler.getRequestActivity(routeElement))) {
						removalOption = collectRemoveDestinationInformation(routeElement, removelInformation, searchStartIndex);
						if (removalOption != null) {
							removalOption.setDestinationRouteElement(routeElement);
							if (SolutionHandler.getRequest(route.getRouteElement(i-1)) == SolutionHandler.getRequest(routeElement)) 
								removalOption.setCosts(Handler.getNetwork().getInsertionDurationIfIndexesEqual(
										route.getRouteElement(i - 2), removalOption.getOriginRouteElement(),
										routeElement, route.getRouteElement(i + 1)));
							else
								removalOption.setCosts(removalOption.getCosts()
										+ (Handler.getNetwork().getInsertionDuration(route.getRouteElement(i - 1),
												routeElement, route.getRouteElement(i + 1))));
						}
					} else {
						removalOption = AlgorithmObjectsHandler.createRemovalOption();
						removalOption.setRoute(route);
						removalOption.setOriginRouteElement(routeElement);
						removalOption.setCosts(Handler.getNetwork().getInsertionDuration(route.getRouteElement(i - 1),
								routeElement, route.getRouteElement(i + 1)));
						removelInformation.add(removalOption);
					}
				}
			}
		}
		return removelInformation;
	}

	private static RemovalOption collectRemoveDestinationInformation(RouteElement routeElement,
			ArrayList<RemovalOption> removelInformation, int searchStartIndex) {
		for (int i = searchStartIndex; i < removelInformation.size(); i++) {
			if (SolutionHandler.getRequest(routeElement) == SolutionHandler
					.getRequest(removelInformation.get(i).getOriginRouteElement())) {
				return removelInformation.get(i);
			}
		}
		return null;
	}

	public static int calculateNumberOfRequestsToBeRemoved(int numberOfIntegratedRequests) {

		if (numberOfIntegratedRequests == 0)
			return 0;
		double minSmallRequestSet = Handler.getInput().getConfig().getAlgorithm().getLns().getMinSmallRequestSet();
		double maxSmallRequestSet = Handler.getInput().getConfig().getAlgorithm().getLns().getMaxSmallRequestSet();

		int minLargeRequestSet = Handler.getInput().getConfig().getAlgorithm().getLns().getMinLargeRequestSet();
		int maxLargeRequestSet = Handler.getInput().getConfig().getAlgorithm().getLns().getMaxLargeRequestSet();

		int lowerBound;
		int upperBound;

		if (minLargeRequestSet <= numberOfIntegratedRequests * minSmallRequestSet)
			lowerBound = minLargeRequestSet;
		else {
			lowerBound = (int) Math.floor(numberOfIntegratedRequests * minSmallRequestSet);
		}
		if (maxLargeRequestSet <= numberOfIntegratedRequests * maxSmallRequestSet)
			upperBound = maxLargeRequestSet;
		else {
			upperBound = (int) Math.ceil(numberOfIntegratedRequests * maxSmallRequestSet);
		}
		return (Handler.getRandom().nextInt(upperBound - lowerBound) + lowerBound);
	}

	public static void removeRequests(ArrayList<RemovalOption> removeInformation, int numberToRemove, boolean random,
			int noise) {

		HashMap<Route, Integer> updateRoutes = new HashMap<Route, Integer>();

		for (int i = 0; i < numberToRemove && i < removeInformation.size(); i++) {

			int index = 0;
			if (random)
				index = Handler.getRandom().nextInt(removeInformation.size());
			else if (noise > 0)
				index = (int) (Math.pow(Handler.getRandom().nextDouble(), (10 - noise)) * removeInformation.size());

			Request request = SolutionHandler.getRequest(removeInformation.get(index).getOriginRouteElement());
			Handler.getSolution().getUnintegratedRequests().add(request);
			Handler.getSolution().getIntegratedRequests().remove(request);

			Route route = removeInformation.get(index).getRoute();
			if (removeInformation.get(index).getDestinationRouteElement() != null) {
				route.getRouteElements().remove(removeInformation.get(index).getDestinationRouteElement());
			}

			int originIndex = SolutionHandler.getRouteElementIndex(route,
					removeInformation.get(index).getOriginRouteElement());
			route.getRouteElements().remove(originIndex);

			if (!updateRoutes.containsKey(route) || updateRoutes.get(route) > originIndex)
				updateRoutes.put(route, originIndex);

			removeInformation.remove(index);
		}

		for (Entry<Route, Integer> entry : updateRoutes.entrySet())
			RouteUpdater.updateServiceBeginnAll(entry.getKey(), entry.getValue());
	}
}
