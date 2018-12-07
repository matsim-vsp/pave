package ovgu.alns;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import ovgu.data.entity.RouteElement;
import ovgu.utilities.DistanceMatrix;
import ovgu.utilities.Settings;

public class ALNS {

	public int iterations = 0;

	private Random random = new Random();

	private RouletteWheelSelection removalHeuristicSelection = new RouletteWheelSelection(6);
	private RouletteWheelSelection insertionHeuristicSelection = new RouletteWheelSelection(4);
	private double peneltyTerm = 100;

	private RemovalHeuristics removalHeuristics;
	private InsertionHeuristics insertionHeuristics;

	private ArrayList<ArrayList<RouteElement>> bestRoutes;
	private ArrayList<ArrayList<RouteElement>> backupRoutes;
	private ArrayList<ArrayList<RouteElement>> currentRoutes;

	private ArrayList<RouteElement> includedRequests = new ArrayList<RouteElement>();
	private ArrayList<RouteElement> openRequests = new ArrayList<RouteElement>();
	private ArrayList<RouteElement> backupOpenRequests = new ArrayList<RouteElement>();

	public ALNS(DistanceMatrix matrix) {
		this.removalHeuristics = new RemovalHeuristics(matrix, includedRequests, openRequests);
		this.insertionHeuristics = new InsertionHeuristics(matrix, includedRequests, openRequests);
	}

	public ArrayList<ArrayList<RouteElement>> performALNS(ArrayList<ArrayList<RouteElement>> routes,
			ArrayList<RouteElement> allRequests) {

		openRequests.addAll(allRequests);
		insertionHeuristics.insertElements(1, routes);

		backupOpenRequests.addAll(openRequests);
		includedRequests.clear();
		backupRoutes = copyCurrentRoutes(routes);
		currentRoutes = copyBackupRoutes(routes, includedRequests);
		
		double performance = performance(backupRoutes);
		double temperature = performance * Settings.temperatureControlParameter / Math.log(2);
		double backupPerformance = performance + performance(currentRoutes) + (peneltyTerm * openRequests.size());
		double bestPerformance = performance + performance(currentRoutes) + (peneltyTerm * openRequests.size());
		double currentPerformance = performance + performance(currentRoutes) + (peneltyTerm * openRequests.size());
		int reasonForAcceptance;

		HashSet<Double> objectiveValues = new HashSet<Double>();

		for (int i = 0; i < Settings.iterations; i++) {

			reasonForAcceptance = 0;

			removalHeuristics.removeElements(removalHeuristicSelection.selectHeuristic(), currentRoutes);
			insertionHeuristics.insertElements(insertionHeuristicSelection.selectHeuristic(), currentRoutes);

			currentPerformance = performance(currentRoutes) + (peneltyTerm * openRequests.size());
			if (currentPerformance < bestPerformance)
				reasonForAcceptance = 1;
			if (currentPerformance < backupPerformance)
				reasonForAcceptance = 2;
			else if (Math.exp(-(currentPerformance - backupPerformance) / temperature) > random.nextDouble())
				reasonForAcceptance = 3;

			// update after Iteration
			if (reasonForAcceptance > 0) {
				if (reasonForAcceptance == 1) {
					bestRoutes = copyCurrentRoutes(currentRoutes);
					bestPerformance = currentPerformance;
				}
				backupOpenRequests.clear();
				backupOpenRequests.addAll(openRequests);
				backupRoutes = copyCurrentRoutes(currentRoutes);
				backupPerformance = currentPerformance;
				if (!objectiveValues.contains(currentPerformance)) {
					objectiveValues.add(currentPerformance);
					removalHeuristicSelection.updateScore(reasonForAcceptance);
					insertionHeuristicSelection.updateScore(reasonForAcceptance);
				}
			} else {
				openRequests.clear();
				includedRequests.clear();
				openRequests.addAll(backupOpenRequests);
				currentRoutes = copyBackupRoutes(backupRoutes, includedRequests);
			}

			temperature = temperature * Settings.coolingRate;
			if (i % 100 == 0) {
				removalHeuristicSelection.updateWheel();
				insertionHeuristicSelection.updateWheel();
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

	// computes current performance
	public double performance(ArrayList<ArrayList<RouteElement>> routes) {
		double overallTravelTime = 0;
		for (ArrayList<RouteElement> currentRoute : routes) {
			for (RouteElement currentElement : currentRoute) {
				overallTravelTime = overallTravelTime + currentElement.getTravelTimeTo();
			}
		}
		return overallTravelTime;
	}

	public ArrayList<RouteElement> getIncludedRequests() {
		return includedRequests;
	}
}
