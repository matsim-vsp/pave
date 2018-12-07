package ovgu.vrptw;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;

import ovgu.alns.ALNS;
import ovgu.data.entity.RouteElement;
import ovgu.lmns.InsertionHeuristics;
import ovgu.lmns.LMNS;
import ovgu.utilities.DistanceMatrix;
import ovgu.utilities.RouteStart;

public class vrpSolver {

	DistanceMatrix matrix;
	long startTime;
	ThreadMXBean bean = ManagementFactory.getThreadMXBean();

	public vrpSolver(DistanceMatrix matrix) {
		this.matrix = matrix;
	}

	public ArrayList<ArrayList<RouteElement>> startInsertion(ArrayList<RouteElement> requests) {

		RouteStart routeStart = new RouteStart();
		ArrayList<ArrayList<RouteElement>> routes = routeStart.createRouteStart();
		ArrayList<RouteElement> openRequests = new ArrayList<RouteElement>();
		ArrayList<RouteElement> includedRequests = new ArrayList<RouteElement>();
		InsertionHeuristics insertionHeuristics = new InsertionHeuristics(matrix, includedRequests, openRequests);

		for (RouteElement currentRequest : requests) {
			startTime = System.nanoTime();
			openRequests.add(currentRequest);
			insertionHeuristics.greedyInsertion(routes);
			openRequests.clear();
		}
		return routes;
	}

	public ArrayList<ArrayList<RouteElement>> startLMNS(ArrayList<RouteElement> requests) {

		RouteStart routeStart = new RouteStart();
		ArrayList<ArrayList<RouteElement>> routes = routeStart.createRouteStart();
		
		LMNS lmns = new LMNS(matrix);

		for (RouteElement currentRequest : requests) {
			routes = lmns.performLMNS(routes, currentRequest);
		}
		return routes;
	}


	public ArrayList<ArrayList<RouteElement>> startALNS(ArrayList<RouteElement> requests) {

		ALNS alns = new ALNS(matrix);
		RouteStart routeStart = new RouteStart();

		ArrayList<ArrayList<RouteElement>> routes = routeStart.createRouteStart();
		routes = alns.performALNS(routes, requests);

		return routes;
	}
}
