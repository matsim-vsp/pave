package ovgu.lmns;

import java.util.ArrayList;
import java.util.Random;

import ovgu.data.entity.RouteElement;
import ovgu.utilities.DistanceMatrix;
import ovgu.utilities.Settings;

public class Improvment {

	Random random = new Random();

	long startTime; 
	int iterations;
	long startComputingTime;

	private DistanceMatrix matrix;
	
	private RemovalHeuristics removalHeuristics;
	private InsertionHeuristics insertionHeuristics;

	private ArrayList<ArrayList<RouteElement>> bestRoutes;
	private ArrayList<ArrayList<RouteElement>> backupRoutes;
	private ArrayList<ArrayList<RouteElement>> currentRoutes;

	private ArrayList<RouteElement> includedRequests;
	private ArrayList<RouteElement> openRequests;

	public Improvment(DistanceMatrix matrix) {
		this.matrix = matrix;
	}
	
	public ArrayList<ArrayList<RouteElement>> performALNI(ArrayList<ArrayList<RouteElement>> routes) {

		includedRequests = new ArrayList<RouteElement>();
		openRequests = new ArrayList<RouteElement>();

		removalHeuristics = new RemovalHeuristics(matrix, includedRequests, openRequests);
		insertionHeuristics = new InsertionHeuristics(matrix, includedRequests, openRequests);

		bestRoutes = routes;
		backupRoutes = copyCurrentRoutes(routes);
		currentRoutes = copyBackupRoutes(routes, includedRequests);

		double performance = performance(backupRoutes);
		double backupPerformance = performance;
		double bestPerformance = performance;
		double currentPerformance = performance;
		int reasonForAcceptance;

		startComputingTime = System.nanoTime();
		while (System.nanoTime() - startComputingTime <= Settings.maxTime)  {
			reasonForAcceptance = 0;
			removalHeuristics.removeElements(random.nextInt(6), currentRoutes);
			insertionHeuristics.regretInsertion(1, currentRoutes);

			if (openRequests.isEmpty()) {
				currentPerformance = performance(currentRoutes);
				if (currentPerformance < bestPerformance)
					reasonForAcceptance = 1;
				else if (currentPerformance < backupPerformance)
					reasonForAcceptance = 2;
				else if (Math.exp(-(currentPerformance - backupPerformance)
						/ (currentPerformance * Settings.deteriorationDegree / Math.log(2))) > random.nextDouble())
					reasonForAcceptance = 3;
			}

			if (reasonForAcceptance > 0) {
				if (reasonForAcceptance == 1) {
					bestRoutes = copyCurrentRoutes(currentRoutes);
					bestPerformance = currentPerformance;
				}
				backupRoutes = copyCurrentRoutes(currentRoutes);
				backupPerformance = currentPerformance;
			} else {
				openRequests.clear();
				includedRequests.clear();
				currentRoutes = copyBackupRoutes(backupRoutes, includedRequests);
			}		
		}
	
		return bestRoutes;
	}

	// Generates copy of a given solution
	public ArrayList<ArrayList<RouteElement>> copyCurrentRoutes(ArrayList<ArrayList<RouteElement>> routes) {
		ArrayList<ArrayList<RouteElement>> routesCopies = new ArrayList<ArrayList<RouteElement>>();

		for (ArrayList<RouteElement> currentRoute : routes) {
			ArrayList<RouteElement> routeCopy = new ArrayList<RouteElement>();
			routesCopies.add(routeCopy);

			for (int i = 0; i < currentRoute.size(); i++) {
				routeCopy.add(currentRoute.get(i).copyRouteElement(routeCopy));
			}
		}
		return routesCopies;
	}

	public ArrayList<ArrayList<RouteElement>> copyBackupRoutes(ArrayList<ArrayList<RouteElement>> routes,
			ArrayList<RouteElement> includedRequests) {
		ArrayList<ArrayList<RouteElement>> routesCopies = new ArrayList<ArrayList<RouteElement>>();
		RouteElement elementCopy;
		for (ArrayList<RouteElement> currentRoute : routes) {
			ArrayList<RouteElement> routeCopy = new ArrayList<RouteElement>();
			routesCopies.add(routeCopy);

			for (int i = 0; i < currentRoute.size(); i++) {
				elementCopy = currentRoute.get(i).copyRouteElement(routeCopy);
				routeCopy.add(elementCopy);
				if (elementCopy.getId() != 999999)
					includedRequests.add(elementCopy);
			}
		}
		return routesCopies;
	}
	
	public double performance(ArrayList<ArrayList<RouteElement>> routes) {
		double overallTravelTime = 0;
		for (ArrayList<RouteElement> currentRoute : routes) {
			for (RouteElement currentElement : currentRoute) {
				overallTravelTime = overallTravelTime + currentElement.getTravelTimeTo();
			}
		}
		return overallTravelTime;
	}
}
