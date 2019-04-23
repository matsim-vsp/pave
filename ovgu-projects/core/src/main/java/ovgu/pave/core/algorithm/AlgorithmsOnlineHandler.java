package ovgu.pave.core.algorithm;

import java.util.List;

import org.eclipse.emf.common.util.EList;

import ovgu.pave.core.algorithm.lns.LMNS;
import ovgu.pave.core.algorithm.lns.LMNSInsertion;
import ovgu.pave.core.algorithm.lns.insertion.InsertionHeuristics;
import ovgu.pave.core.algorithm.lns.utilities.InitializeRoutes;
import ovgu.pave.core.algorithm.lns.utilities.SolutionEvaluator;
import ovgu.pave.handler.Handler;
import ovgu.pave.handler.modelHandler.NetworkHandler;
import ovgu.pave.handler.modelHandler.SolutionHandler;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.solution.RequestActivityRouteElement;
import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.RouteElement;
import ovgu.pave.model.solution.Solution;

public class AlgorithmsOnlineHandler {

	public enum Algorithm {
		ALNS, LMNS, LMNSINSERTION, INSERTION
	}

	public static void run(String algorthim, boolean movingVehicles) {

		Solution currentSolution = Handler.getInput().getSolution();

		if (currentSolution == null) {
			initializeRoutes(algorthim);
		} else {
			Handler.getSolution().setSolution(currentSolution);
		}

		Solution finalSolution = Handler.getSolution().copyEmptySolution();
		currentSolution = Handler.getSolution().copySolution();
		
		boolean accepted;
		while(!Handler.getInput().getNewRequests().isEmpty()) {
			Request request = Handler.getInput().getNewRequests().remove(0);
			if (movingVehicles) {
				updateVehiclePositions(finalSolution, request);
				currentSolution = Handler.getSolution().copySolution();
			}
			
			Handler.getSolution().getUnintegratedRequests().add(request);
			accepted = true;
			switch (Algorithm.valueOf(algorthim)) {
			case INSERTION:
				accepted = runInsertion();
				break;
			case LMNSINSERTION:
				accepted = runLMNSInsertion();
				break;
			case LMNS:
				accepted = runLMNS();
				break;
			default:
				System.out.println("The selected algorithm is not available for an online VRP");
				break;
			}		
			if (accepted) {
				Handler.getInput().getAcceptedRequests().add(request);
			} else {
				Handler.getInput().getRejectedRequests().add(request);
				Handler.getSolution().setSolution(currentSolution);
			}
			currentSolution = Handler.getSolution().copySolution();
		}

		if (movingVehicles) {
			finalizeSolution(finalSolution);
		}
		Handler.getSolution().setScore(SolutionEvaluator.getTotalTravelTime());
	}

	private static void initializeRoutes(String algorthim) {
		switch (Algorithm.valueOf(algorthim)) {
		case INSERTION:
			InitializeRoutes.depotStartEnd();
			break;
		case LMNSINSERTION:
			InitializeRoutes.depotStartEnd();
			break;
		case LMNS:
			InitializeRoutes.depotStartEnd();
			break;
		default:
			System.out.println("The selected algorithm is not available for a static VRP");
			break;
		}
	}

	private static boolean runInsertion() {
		InsertionHeuristics.regretInsertion(0, 0);
		if (Handler.getSolution().getUnintegratedRequests().isEmpty())
			return true;
		return false;
	}

	private static boolean runLMNSInsertion() {
		return LMNSInsertion.performLMNSInsertion();
	}

	private static boolean runLMNS() {
		Solution bestSolution = LMNS.performLMNS();
		if (bestSolution == null)
			return false;
		Handler.getSolution().setSolution(bestSolution);
		return true;
	}

	private static void updateVehiclePositions(Solution solution, Request request) {
		Route route;
		for (int i = 0; i < Handler.getSolution().getRoutes().size(); i++) {
			route = Handler.getSolution().getRoutes().get(i);
			while (route.getRouteElements().size() > 2
					&& SolutionHandler.getServiceEnd(route, 0) < request.getReceivingTime()) {
				route.setUtilisation(route.getUtilisation() + SolutionHandler.getChangeInQuantity(route.getRouteElement(0)));	
				solution.getRoutes().get(i).getRouteElements().add(route.getRouteElements().remove(0));
			}
		}
	}

	private static void finalizeSolution(Solution solution) {
		for (int i = 0; i < Handler.getSolution().getRoutes().size(); i++) {
			Route route = Handler.getSolution().getRoutes().get(i);
			while (route.getRouteElements().size() > 0) {
				solution.getRoutes().get(i).getRouteElements().add(route.getRouteElements().remove(0));
			}
		}
		Handler.getSolution().setSolution(solution);
	}
	
	public static void printRoutes(List<Route> routes, NetworkHandler networkHandler) {

		long totalCosts = 0;
		int count = 0;

		System.out.println("");
		for (int route = 0; route < routes.size(); route++) {

			EList<RouteElement> currentRouteElements = routes.get(route).getRouteElements();

			System.out.println("");
			System.out.println("Route " + route);

			for (int trip = 1; trip < currentRouteElements.size() - 1; trip++) {

				totalCosts += networkHandler.getTravelDuration(currentRouteElements.get(trip - 1),
						currentRouteElements.get(trip));
				count++;
				RequestActivityRouteElement rARElement = ((RequestActivityRouteElement) currentRouteElements.get(trip));
				System.out.println(

						"requestId: " + rARElement.getRequestActivity().getRequest().getId() 
								+ "\t tripId: " + rARElement.getRequestActivity().getLocation().getId() 
								+ "\t TravelTimeTo: "
								+ networkHandler.getTravelDuration(currentRouteElements.get(trip - 1),
										currentRouteElements.get(trip)) / 1000 / 60 
								+ "\t RequestTime: " + rARElement.getRequestActivity().getRequest().getReceivingTime()  / 1000 / 60 
								+ "\t EarliestArrival: " + rARElement.getRequestActivity().getEarliestArrival() / 1000 / 60 
								+ "\t BeginService: " + (currentRouteElements.get(trip).getServiceBegin()  / 1000 / 60 )
								+ "\t ServiceDuration: " + rARElement.getRequestActivity().getServiceDuration() / 1000 / 60 
								+ "\t EndService: "
								+ (rARElement.getServiceBegin() + rARElement.getRequestActivity().getServiceDuration())
										/ 1000 / 60
								+ "\t LatestArrival: " + rARElement.getRequestActivity().getLatestArrival() / 1000 / 60
								+ "\t Quantity: " + rARElement.getRequestActivity().getRequest().getQuantity());
			}
		}

		System.out.println("Count: " + count + "\nTotalCosts: " + totalCosts);
	}
	
}
