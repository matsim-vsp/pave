package ovgu.pave.core.algorithm.lns.utilities;

import ovgu.pave.handler.Handler;
import ovgu.pave.handler.modelHandler.SolutionHandler;
import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.SupportRouteElement;

public class InitializeRoutes {

	public static void depotStartEnd() {

		Handler.getSolution().initEmptyRoutesForVehicles(Handler.getInput().getVehicles());

		for (Route route : Handler.getSolution().getRoutes()) {
			SupportRouteElement morning = SolutionHandler.createSupportRouteElement(route.getVehicle().getStartLocation());
			SupportRouteElement evening = SolutionHandler.createSupportRouteElement(route.getVehicle().getEndLocation());
			SolutionHandler.insertRouteElement(route, morning, 0);
			SolutionHandler.insertRouteElement(route, evening, 1);
		}
		
	}
}
