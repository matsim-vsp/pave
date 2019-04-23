package ovgu.pave.experiments.utils;

import java.util.List;

import org.eclipse.emf.common.util.EList;

import ovgu.pave.handler.modelHandler.NetworkHandler;
import ovgu.pave.handler.modelHandler.SolutionHandler;
import ovgu.pave.model.solution.RequestActivityRouteElement;
import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.RouteElement;

public class RoutesConsoleOutput {

	public RoutesConsoleOutput(List<Route> routes, NetworkHandler networkHandler) {
		printRoutes(routes, networkHandler);
		validateServiceBegin(routes, networkHandler);
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
								+ "\t RequestTime: " + rARElement.getRequestActivity().getRequest().getReceivingTime()  / 1000 /60
								+ "\t IdleTimeTo: " + ((rARElement.getRequestActivity().getRequest().getReceivingTime() - SolutionHandler.getServiceEnd(routes.get(route), trip-1)) / 1000 / 60) 
								+ "\t TravelTimeTo: "	+ networkHandler.getTravelDuration(currentRouteElements.get(trip - 1),
										currentRouteElements.get(trip)) / 1000 / 60 
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

	public static void validateServiceBegin(List<Route> routes, NetworkHandler networkHandler) {

		System.out.println("");
		for (int route = 0; route < routes.size(); route++) {

			Route currentRoute = routes.get(route);
			long capacity = currentRoute.getVehicle().getType().getCapacity();
			long utilisation = 0;
			for (int trip = 1; trip < currentRoute.getRouteElements().size() - 1; trip++) {
				RequestActivityRouteElement rARElement = ((RequestActivityRouteElement) currentRoute.getRouteElements()
						.get(trip));
				if ((rARElement.getServiceBegin() != (SolutionHandler.getServiceEnd(currentRoute, trip - 1)
						+ networkHandler.getTravelDuration(currentRoute.getRouteElement(trip - 1),
								currentRoute.getRouteElement(trip))))
						&& (rARElement.getServiceBegin() != rARElement.getRequestActivity().getEarliestArrival())
						&& (rARElement
								.getServiceBegin() != rARElement.getRequestActivity().getRequest().getReceivingTime()
										+ networkHandler.getTravelDuration(currentRoute.getRouteElement(trip - 1),
												currentRoute.getRouteElement(trip)))) {
					System.out.println("Error in Route: " + route + " element: "
							+ rARElement.getRequestActivity().getLocation().getId()
							+ " bei der Berechenung des Servic begins.");
				}

				if ((rARElement.getServiceBegin() < rARElement.getRequestActivity().getEarliestArrival())
						|| (rARElement.getServiceBegin() > rARElement.getRequestActivity().getLatestArrival())) {
					System.out.println("Error in Route: " + route + " element: "
							+ rARElement.getRequestActivity().getLocation().getId()
							+ " das Zeitfenster wird nicht eingehalten.");
				}

				if (((rARElement.getServiceBegin() - networkHandler.getTravelDuration(
						currentRoute.getRouteElement(trip - 1), currentRoute.getRouteElement(trip))) < rARElement
								.getRequestActivity().getRequest().getReceivingTime())) {
					System.out.println("Error in Route: " + route + " element: "
							+ rARElement.getRequestActivity().getLocation().getId()
							+ " die Anfahrt beginnt bevor die Anfrage bekannnt ist.");
				}

				utilisation += SolutionHandler.getChangeInQuantity(currentRoute.getRouteElement(trip));
				if (utilisation > capacity) {
					System.out.println("Error in Route: " + route + " element: "
							+ rARElement.getRequestActivity().getLocation().getId()
							+ " die Kapazität des Fahrzeugs wird überschritten.");

				}
			}
		}
	}
}
