package ovgu.pave.core.algorithm.lns.insertion;

import java.util.Comparator;
import java.util.HashMap;

import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;

import ovgu.pave.core.algorithm.lns.utilities.RouteUpdater;
import ovgu.pave.handler.Handler;
import ovgu.pave.handler.modelHandler.AlgorithmObjectsHandler;
import ovgu.pave.handler.modelHandler.SolutionHandler;
import ovgu.pave.model.algorithmObjects.InsertionInformation;
import ovgu.pave.model.algorithmObjects.InsertionOption;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.input.RequestActivity;
import ovgu.pave.model.solution.Route;

public class InsertionMethods {

	public static InsertionInformation computeInsertionInformation(Request request) {
	
		InsertionInformation insertionInformation = AlgorithmObjectsHandler.createInsertionInformation(request);
		InsertionOption requestSchedule;

		for (Route route : Handler.getSolution().getRoutes()) {
			requestSchedule = computeInsertionOption(route, request);
			if (requestSchedule != null) {
				insertionInformation.getInsertionOptions().add(requestSchedule);
			}
		}

		if (insertionInformation.getInsertionOptions().isEmpty())
			return null;
		
		ECollections.sort(insertionInformation.getInsertionOptions(), new InsertionMethods().new CostsComparator()); 
		return insertionInformation;
	}

	public static void updateInsertionInformation(Route route, InsertionInformation insertionInformation) {

		EList<InsertionOption> insertionOption = insertionInformation.getInsertionOptions();
		Request request = insertionInformation.getRequest();

		for (int i = 0; i < insertionOption.size(); i++) {
			if (insertionOption.get(i).getRoute() == route) {
				insertionOption.remove(i);
				InsertionOption requestSchedule = computeInsertionOption(route, request);
				if (requestSchedule != null) {
					insertionOption.add(requestSchedule);
					ECollections.sort(insertionOption, new InsertionMethods().new CostsComparator()); 
				}
				break;
			}
		}
	}

	public static InsertionOption computeInsertionOption(Route route, Request request) {

		FeasibilityCheck feasibilityCheck = new FeasibilityCheck();
		InsertionOption requestSchedule = AlgorithmObjectsHandler.createInsertionOption();
		requestSchedule.setRoute(route);
		requestSchedule.setCosts(Long.MAX_VALUE);
		
		RequestActivity originActivity = request.getOriginActivity();
		RequestActivity destinationActivity = request.getDestinationActivity();

		long insertionCosts = Long.MAX_VALUE;
		for (int originIndex = 1; originIndex < route.getRouteElements().size(); originIndex++) {
			originIndex = getNextInsertionIndex(route, originActivity, originIndex);
			if (originIndex == -1) break;
			if (feasibilityCheck.checkOriginActivity(route, originActivity, originIndex, route.getRouteElements().size()-1)) {
				long originInsertionCosts = Handler.getNetwork().getInsertionDuration(
						route.getRouteElement(originIndex - 1),
						SolutionHandler.createRequestActivityRouteElement(originActivity),
						route.getRouteElement(originIndex));
				if (destinationActivity == null && requestSchedule.getCosts() > originInsertionCosts) {
					requestSchedule.setOriginIndex(originIndex);
					requestSchedule.setCosts(originInsertionCosts);
				} else if (destinationActivity != null) {
					for (int destinationIndex = originIndex; destinationIndex < route.getRouteElements().size(); destinationIndex++) {
						destinationIndex = getNextInsertionIndex(route, destinationActivity, destinationIndex);
						if (destinationIndex == -1)	break;
						if (feasibilityCheck.checkDestinationActivity(route, request, originIndex, destinationIndex)) {
							if (originIndex == destinationIndex) {
								insertionCosts = Handler.getNetwork().getInsertionDurationIfIndexesEqual(
										route.getRouteElement(originIndex - 1),
										SolutionHandler.createRequestActivityRouteElement(originActivity),
										SolutionHandler.createRequestActivityRouteElement(destinationActivity),
										route.getRouteElement(originIndex));
							} else {
								insertionCosts = originInsertionCosts + Handler.getNetwork().getInsertionDuration(
										route.getRouteElement(destinationIndex - 1),
										SolutionHandler.createRequestActivityRouteElement(destinationActivity),
										route.getRouteElement(destinationIndex));
							}
							if (requestSchedule.getCosts() > insertionCosts) {
								requestSchedule.setCosts(insertionCosts);
								requestSchedule.setOriginIndex(originIndex);
								requestSchedule.setDestinationIndex(destinationIndex);
							}
						}
					}
				}
			}
		}

		if (requestSchedule.getCosts() == Long.MAX_VALUE) {
			return null;
		}
		return requestSchedule;
	}

	private static int getNextInsertionIndex(Route route, RequestActivity requestActivity, int startIndex) {

		for (int i = startIndex; i < route.getRouteElements().size(); i++) {
			if (!SolutionHandler.isRequestActivityRouteElement(route.getRouteElement(i))
					|| requestActivity.getEarliestArrival() < SolutionHandler
							.getRequestActivity(route.getRouteElement(i)).getLatestArrival()) {
				return i;
			} else if (SolutionHandler.isRequestActivityRouteElement(route.getRouteElement(i-1)) && SolutionHandler.getRequestActivity(route.getRouteElement(i - 1))
					.getEarliestArrival() > requestActivity.getLatestArrival()) {
				break;
			}
		}
		return -1;
	}

	public static void insertRequest(InsertionInformation insertionInformation) {

		Request request = insertionInformation.getRequest();
		Route route = insertionInformation.getInsertionOptions().get(0).getRoute();

		int indexOrigin = insertionInformation.getInsertionOptions().get(0).getOriginIndex();
		int indexDestination = insertionInformation.getInsertionOptions().get(0).getDestinationIndex();

		SolutionHandler.insertRequest(route, request, indexOrigin, indexDestination);
		Handler.getSolution().getIntegratedRequests().add(request);
		Handler.getSolution().getUnintegratedRequests().remove(request);
		
		RouteUpdater.updateServiceBeginnAll(route, indexOrigin);
		updateLowestCostPerRequest(request, insertionInformation.getInsertionOptions().get(0).getCosts());
	}

	private static void updateLowestCostPerRequest(Request request, long insertionCosts) {

		HashMap<Request, Long> lowestCostPerRequest = Handler.getAlgorithmObjects().getLowestCostsPerRequest();

		if (!lowestCostPerRequest.containsKey(request) || lowestCostPerRequest.get(request) > insertionCosts) {
			lowestCostPerRequest.put(request, insertionCosts);
		}
	}
	class CostsComparator implements Comparator<InsertionOption> {
	    @Override
	    public int compare(InsertionOption rs1, InsertionOption rs2) {
	        return Long.compare(rs1.getCosts(), rs2.getCosts());
	    }
	}
}
