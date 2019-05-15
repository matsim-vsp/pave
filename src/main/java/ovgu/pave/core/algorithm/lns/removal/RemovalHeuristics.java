package ovgu.pave.core.algorithm.lns.removal;

import java.util.ArrayList;
import java.util.Comparator;

import ovgu.pave.handler.Handler;
import ovgu.pave.handler.modelHandler.SolutionHandler;
import ovgu.pave.model.algorithmObjects.RemovalOption;
import ovgu.pave.model.config.Heuristic;
import ovgu.pave.model.solution.RouteElement;

public class RemovalHeuristics {

	public enum Heuristics {
		RANDOMREMOVAL, TIMEWINDOWREMOVAL, WORSTREMOVAL, HISTORICALREMOVAL, SHAWREMOVAL, CLUSTERREMOVAL
	}

	private final static Comparator<RemovalOption> scoreComparator = Comparator.comparing(RemovalOption::getCosts);
	private final static Comparator<RemovalOption> rankingComparatorReversed = scoreComparator.reversed();


	public static void removeRequests(Heuristic heuristic) {

		ArrayList<RemovalOption> removalInformation = RemovalMethods.collectRemovalInformation();	
		int numberToRemove = RemovalMethods.calculateNumberOfRequestsToBeRemoved(removalInformation.size());
		
		if (numberToRemove > 0) {
			
			Handler.getAlgorithmObjects().updateExchangeableRequests(removalInformation.size());
	
			switch (Heuristics.valueOf(heuristic.getHeuristicName())) {
			case RANDOMREMOVAL:
				randomRemoval(removalInformation, numberToRemove);
				break;
			case TIMEWINDOWREMOVAL:
				timeWindowRemoval(removalInformation, numberToRemove);
				break;
			case WORSTREMOVAL:
				worstRemoval(removalInformation, numberToRemove, heuristic.getNoise());
				break;
			case HISTORICALREMOVAL:
				historicalRemoval(removalInformation, numberToRemove, heuristic.getNoise());
				break;
			case SHAWREMOVAL:
				shawRemoval(removalInformation, numberToRemove, heuristic.getNoise());
				break;
			case CLUSTERREMOVAL:
				clusterRemoval(removalInformation, numberToRemove, heuristic.getNoise());
				break;
			}
		}
	}

	public static void randomRemoval(ArrayList<RemovalOption> removalInformation, int numberToRemove) {
		RemovalMethods.removeRequests(removalInformation, numberToRemove, true, -1);
	}

	public static void timeWindowRemoval(ArrayList<RemovalOption> removalInformation, int numberToRemove) {
		RemovalOption removalOption = removalInformation.get(Handler.getRandom().nextInt(removalInformation.size()));

		long selectedEarliestArrival = SolutionHandler.getRequestActivity(removalOption.getOriginRouteElement())
				.getEarliestArrival();
		long selectedLatestArrival = SolutionHandler.getRequestActivity(removalOption.getOriginRouteElement())
				.getLatestArrival();

		for (int i = 0; i < removalInformation.size(); i++) {
			removalOption = removalInformation.get(i);
			if (selectedLatestArrival <= SolutionHandler.getRequestActivity(removalOption.getOriginRouteElement())
					.getEarliestArrival()
					|| selectedEarliestArrival >= SolutionHandler
							.getRequestActivity(removalOption.getOriginRouteElement()).getLatestArrival()) {
				removalInformation.remove(i);
				i--;
			}
		}
		RemovalMethods.removeRequests(removalInformation, numberToRemove, true, -1);
	}

	public static void worstRemoval(ArrayList<RemovalOption> removalInformation, int numberToRemove, int noise) {
		removalInformation.sort(rankingComparatorReversed);
		RemovalMethods.removeRequests(removalInformation, numberToRemove, false, noise);
	}

	public static void historicalRemoval(ArrayList<RemovalOption> removalInformation, int numberToRemove, int noise) {
		for (RemovalOption removalOption : removalInformation) {
			removalOption.setCosts(removalOption.getCosts() - Handler.getAlgorithmObjects().getLowestCostsPerRequest()
					.get(SolutionHandler.getRequest(removalOption.getOriginRouteElement())));
		}

		removalInformation.sort(rankingComparatorReversed);
		RemovalMethods.removeRequests(removalInformation, numberToRemove, false, noise);
	}

	public static void shawRemoval(ArrayList<RemovalOption> removalInformation, int numberToRemove, int noise) {

		double distanceWeight = Handler.getInput().getConfig().getAlgorithm().getLns().getShawDistanceWeight();
		double serviceBeginWeight = Handler.getInput().getConfig().getAlgorithm().getLns().getShawBeginWeight();

		RemovalOption selectedRemovalOption = removalInformation
				.get(Handler.getRandom().nextInt(removalInformation.size()));

		RouteElement selectedOrigin = selectedRemovalOption.getOriginRouteElement();
		RouteElement selectedDestination = selectedRemovalOption.getDestinationRouteElement();

		long maxDistance = 0;
		long maxAbsServiceBegin = 0;

		for (RemovalOption removalOption : removalInformation) {

			long distance = Handler.getNetwork().getTravelDuration(selectedOrigin,
					removalOption.getOriginRouteElement());
			long absServiceBegin = Math.abs(selectedOrigin.getServiceBegin()
					- removalOption.getOriginRouteElement().getServiceBegin());

			if (selectedDestination != null) {
				distance += Handler.getNetwork().getTravelDuration(selectedDestination,
						removalOption.getDestinationRouteElement());
				absServiceBegin += Math.abs(selectedDestination.getServiceBegin()
						- removalOption.getDestinationRouteElement().getServiceBegin());
			}

			removalOption.setDistance(distance);
			removalOption.setAbsServiceBegin(absServiceBegin);

			if (maxAbsServiceBegin < absServiceBegin)
				maxAbsServiceBegin = absServiceBegin;
			if (maxDistance < distance)
				maxDistance = distance;
		}
		
		for (RemovalOption removalOption : removalInformation) {
			double distance =  ((double) removalOption.getDistance() / maxDistance) * distanceWeight;
			double absServiceBegin = ((double) removalOption.getAbsServiceBegin() / maxAbsServiceBegin) * serviceBeginWeight;
			
			long costs = (long) ((distance + absServiceBegin)*1000);
			removalOption.setCosts(costs);
		}

		removalInformation.sort(scoreComparator);
		RemovalMethods.removeRequests(removalInformation, numberToRemove, false, noise);
	}

	public static void clusterRemoval(ArrayList<RemovalOption> removalInformation, int numberToRemove, int noise) {
		double percentagePerRoute = Handler.getInput().getConfig().getAlgorithm().getLns().getClusterPercentagePerRoute();

		ArrayList<RemovalOption> backupRemovelInformation = new ArrayList<RemovalOption>();
		backupRemovelInformation.addAll(removalInformation);
		removalInformation.clear();

		while (numberToRemove > 0) {
			RemovalOption removalOption = backupRemovelInformation
					.get(Handler.getRandom().nextInt(backupRemovelInformation.size()));
			for (int i = 0; i < backupRemovelInformation.size(); i++) {
				if (backupRemovelInformation.get(i).getRoute() == removalOption.getRoute()) {
					backupRemovelInformation.get(i)
							.setCosts(Handler.getNetwork().getTravelDuration(
									backupRemovelInformation.get(i).getOriginRouteElement(),
									removalOption.getOriginRouteElement()));
					removalInformation.add(backupRemovelInformation.get(i));
					backupRemovelInformation.remove(i);
				}
			}

			removalInformation.sort(scoreComparator);
			for (int i = (int) (removalInformation.size() * percentagePerRoute); i < removalInformation.size(); i++) {
				removalInformation.remove(i);
			}

			RemovalMethods.removeRequests(removalInformation, numberToRemove, false, noise);
		}
	}
}
