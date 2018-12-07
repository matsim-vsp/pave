
package ovgu.alns;

import java.util.ArrayList;
import java.util.Comparator;

import ovgu.data.entity.InsertionPosition;
import ovgu.data.entity.RouteElement;
import ovgu.utilities.DistanceMatrix;

public class InsertionHeuristics {

	private InsertionMethods insertionMethods;
	private ArrayList<RouteElement> openRequests;
	private ArrayList<ArrayList<RouteElement>> routes;
	private Comparator<RouteElement> rankingValueComparator = Comparator.comparing(RouteElement::getRankingValue);
	private Comparator<RouteElement> rankingComparatorReversed = rankingValueComparator.reversed();
	private Comparator<InsertionPosition> travelTimeComparator = Comparator.comparing(InsertionPosition::getTravelTime);

	public InsertionHeuristics(DistanceMatrix matrix, ArrayList<RouteElement> includedRequests,
			ArrayList<RouteElement> openRequests) {
		this.insertionMethods = new InsertionMethods(matrix, includedRequests, openRequests);
		this.openRequests = openRequests;
	}

	public void insertElements(int insertionHeuristic, ArrayList<ArrayList<RouteElement>> routes) {

		this.routes = routes;

		switch (insertionHeuristic) {
		case 0:
			regretInsertion(0);
			break;
		case 1:
			regretInsertion(1);
			break;
		case 2:
			regretInsertion(2);
			break;
		case 3:
			regretInsertion(3);
			break;
		}
	}

	private void regretInsertion(int regretNumber) {

		ArrayList<InsertionPosition> insertionPositions;
		ArrayList<RouteElement> updatedRoute;

		// compute insertion positions for all removed Elements
		for (RouteElement currentElement : openRequests) {

			insertionMethods.computeCostsForAllRoutes(currentElement, routes);
			if (currentElement.isInsertable())
				currentElement.setRankingValue(currentElement.getInsertionPositions().get(regretNumber).getTravelTime()
						- currentElement.getInsertionPositions().get(0).getTravelTime());
			else
				currentElement.setRankingValue(-1F);
		}

		openRequests.sort(rankingComparatorReversed);

		// insert Elements and update positions
		RouteElement insetableElement;
		while (!openRequests.isEmpty() && openRequests.get(0).isInsertable()) {

			insetableElement = openRequests.get(0);

			insertionMethods.insertElement(insetableElement);
			updatedRoute = insetableElement.getRoute();

			// Update insertion positions
			for (RouteElement currentElement : openRequests) {
				insertionPositions = currentElement.getInsertionPositions();
				for (int i = 0; i < insertionPositions.size(); i++) {
					if (!insertionPositions.get(i).isInsertable())
						break;
					else if (insertionPositions.get(i).getRoute() == updatedRoute) {
						insertionPositions.remove(i);
						insertionPositions.add(insertionMethods.computeInsertionCosts(updatedRoute, currentElement));
						insertionPositions.sort(travelTimeComparator);
						if (!currentElement.getInsertionPositions().get(0).isInsertable()) {
							currentElement.setInsertable(false);
							currentElement.setRankingValue(-1F);
						} else {
							currentElement.setRankingValue(
									currentElement.getInsertionPositions().get(regretNumber).getTravelTime()
											- currentElement.getInsertionPositions().get(0).getTravelTime());
						}
						break;
					}
				}
			}
			openRequests.sort(rankingComparatorReversed);
		}
	}
}