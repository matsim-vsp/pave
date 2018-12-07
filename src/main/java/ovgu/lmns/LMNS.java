package ovgu.lmns;

import java.util.ArrayList;
import java.util.Random;

import ovgu.data.entity.RouteElement;
import ovgu.utilities.DistanceMatrix;
import ovgu.utilities.Settings;

public class LMNS {

	Random random = new Random();

	long startTime; 
	int iterations;
	long startComputingTime;
	Improvment improvment; 
	
	private RemovalHeuristics removalHeuristics;
	private InsertionHeuristics insertionHeuristics;

	private ArrayList<ArrayList<RouteElement>> backupRoutes;
	private ArrayList<ArrayList<RouteElement>> currentRoutes;

	private ArrayList<RouteElement> includedRequests = new ArrayList<RouteElement>();
	private ArrayList<RouteElement> openRequests = new ArrayList<RouteElement>();
	private ArrayList<RouteElement> backupOpenRequests = new ArrayList<RouteElement>();

	public LMNS(DistanceMatrix matrix) {
		this.removalHeuristics = new RemovalHeuristics(matrix, includedRequests, openRequests);
		this.insertionHeuristics = new InsertionHeuristics(matrix, includedRequests, openRequests);
		improvment = new Improvment(matrix);
	}
	
	public ArrayList<ArrayList<RouteElement>> performLMNS(ArrayList<ArrayList<RouteElement>> routes,
			RouteElement requests) {

		openRequests.clear();
		backupOpenRequests.clear();
		includedRequests.clear();
				
		openRequests.add(requests);
		insertionHeuristics.greedyInsertion(routes);
		if (openRequests.isEmpty()) {
			routes = improvment.performALNI(routes);
			return routes;
		}
		
		backupOpenRequests.add(requests);	
		backupRoutes = copyCurrentRoutes(routes);
		currentRoutes = copyBackupRoutes(routes, includedRequests);
		
		iterations = 0;
		startComputingTime = System.nanoTime();
		while (System.nanoTime() - startComputingTime <= Settings.maxTime)  {
			iterations++;
			removalHeuristics.removeElements(random.nextInt(6), currentRoutes);
			insertionHeuristics.regretInsertion(1, currentRoutes);
		
			if (openRequests.isEmpty()) {
				return currentRoutes;
			}
			
			if (openRequests.size() == Settings.acceptanceCriteria) {
				backupOpenRequests.clear();
				backupOpenRequests.addAll(openRequests);
				backupRoutes = copyCurrentRoutes(currentRoutes);
			} else {
				openRequests.clear();
				includedRequests.clear();
				openRequests.addAll(backupOpenRequests);
				currentRoutes = copyBackupRoutes(backupRoutes, includedRequests);
			}
		}
		
		return routes;
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
}
