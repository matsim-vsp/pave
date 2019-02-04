package ovgu.utilities;

import java.util.ArrayList;

import ovgu.data.entity.RouteElement;

public class RouteHandler {

	// print errors
	public  void ckeckRoute(ArrayList<ArrayList<RouteElement>> routes) {

		for (int r = 0; r < routes.size(); r++) {
			for (int i = 1; i < routes.get(r).size() - 1; i++) {
				if (routes.get(r).get(i - 1).getServiceEnd() > routes.get(r).get(i).getServiceBegin()) {
					System.out.println("ERROR_1!!! Route: " + r + " Request: " + routes.get(r).get(i).getId());
					printRoute(routes);
				}
				if (routes.get(r).get(i - 1).getServiceEnd() + routes.get(r).get(i - 1).getTravelTimeFrom() != routes
						.get(r).get(i).getServiceBegin()
						& routes.get(r).get(i).getStartTimeWindow() != routes.get(r).get(i).getServiceBegin()) {
					System.out.println("ERROR_2!!!");
				}
				if (routes.get(r).get(i).getStartTimeWindow() - 0.01 > routes.get(r).get(i).getServiceBegin()
						|| routes.get(r).get(i).getEndTimeWindow() + 0.01 < routes.get(r).get(i).getServiceBegin()) {
					System.out.println("ERROR_3!!!");
				}
			}
		}
	}

	// print routes
	public static void printRoute(ArrayList<ArrayList<RouteElement>> routes) {

		System.out.println("");
		for (int route = 0; route < routes.size(); route++) {

			System.out.println("");
			System.out.println("Route " + route);

			for (int c = 0; c < routes.get(route).size(); c++) {

				System.out.println(

						routes.get(route).get(c).getId() + " " + routes.get(route).get(c).getPositionDistanceMatrix()
								+ "     ;WaitingTime;   " + (routes.get(route).get(c).getWaitingTime()) + "     ;TravelTimeTo;    "
								+ (routes.get(route).get(c).getTravelTimeTo()) + "     ;BeginService;   "
								+ (routes.get(route).get(c).getServiceBegin()) + "     ;EndService;   "
								+ (routes.get(route).get(c).getServiceEnd()) + "     ;TravelTimeFrom;    "
								+ (routes.get(route).get(c).getTravelTimeFrom()) + "	;TimeWindow.KEY;    "
								+ (routes.get(route).get(c).getTimeWindowKey()) + "     ;TimeWindow(begin - end);   "
								+ (routes.get(route).get(c).getStartTimeWindow()) + "-"
								+ (routes.get(route).get(c).getEndTimeWindow()));
			}
		}
	}
}
