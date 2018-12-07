package ovgu.utilities;

import java.io.IOException;
import java.util.ArrayList;

import ovgu.data.entity.RouteElement;

/**
 * Start of routes, adding depot to begin and end of each route
 * @author J. Haferkamp
 *
 */

public class RouteStart {

	ArrayList<RouteElement> notAcceptedRequests = new ArrayList<RouteElement>();
	
	/**
	 * create start of routes, add Depot at begin and end of routes
	 * @throws IOException 
	 */
	public ArrayList<ArrayList<RouteElement>> createRouteStart()  {
		
		ArrayList<ArrayList<RouteElement>> routes = new ArrayList<ArrayList<RouteElement>>();
		
		for(int i = 0; i < Settings.numberOfVehicles; i++) { 
			
			ArrayList<RouteElement> newRoute = new ArrayList<RouteElement>();
		
			RouteElement depotStart = new RouteElement();
			depotStart.setId(999999);
			depotStart.setTravelTimeFrom(0f);
			depotStart.setTravelTimeTo(0f);
			depotStart.setPositionDistanceMatrix(1);
			depotStart.setWaitingTime(0f); 
			depotStart.setServiceBegin(480f);
			depotStart.setServiceEnd(480f);
			depotStart.setServiceTime(0f);
			depotStart.setTimeWindowKey(-1);
			depotStart.setStartTimeWindow(0f);
			depotStart.setEndTimeWindow(5000f);		
			depotStart.setRankingValue(100F);
			depotStart.setRoute(newRoute);	
			
			RouteElement depotEnd = new RouteElement();
			depotEnd.setId(999999);
			depotEnd.setTravelTimeFrom(0f);
			depotEnd.setTravelTimeTo(0f);
			depotEnd.setPositionDistanceMatrix(1);
			depotEnd.setWaitingTime(0f); 
			depotEnd.setServiceBegin(480f);
			depotEnd.setServiceEnd(480f);
			depotEnd.setServiceTime(0f);
			depotEnd.setStartTimeWindow(0f);
			depotEnd.setTimeWindowKey(-1);
			depotEnd.setEndTimeWindow(5000f);
			depotEnd.setRankingValue(100F);
			depotEnd.setRoute(newRoute);	
			
			
			newRoute.add(depotStart);
			newRoute.add(depotEnd);
			routes.add(newRoute);	
		} 
		
		return (routes);
	}
}
