package ovgu.lmns;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import ovgu.data.entity.InsertionPosition;
import ovgu.data.entity.RouteElement;
import ovgu.utilities.DistanceMatrix;

public class InsertionHeuristics {

	private Random random = new Random();
	private InsertionMethods insertionMethods;
	private ArrayList<RouteElement> openRequests;
	private Comparator<RouteElement> rankingValueComparator = Comparator.comparing(RouteElement::getRankingValue);
	private Comparator<RouteElement> rankingComparatorReversed = rankingValueComparator.reversed();
	private Comparator<InsertionPosition> travelTimeComparator = Comparator.comparing(InsertionPosition::getTravelTime);

	public InsertionHeuristics(DistanceMatrix matrix, ArrayList<RouteElement> includedRequests,
			ArrayList<RouteElement> openRequests) {
		this.insertionMethods = new InsertionMethods(matrix, includedRequests, openRequests);
		this.openRequests = openRequests;
	}


	public void regretInsertion(int regretNumber, ArrayList<ArrayList<RouteElement>> routes) {

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

	public void greedyInsertion(ArrayList<ArrayList<RouteElement>> routes) {
		ArrayList<RouteElement> selectableReqeusts = new ArrayList<RouteElement>();
		selectableReqeusts.addAll(openRequests);
		RouteElement currentElement;
		int index;
		
		for (int i = 0; i < selectableReqeusts.size(); i++) {

			if (selectableReqeusts.size() == 1)
				index = 0;
			else
				index = random.nextInt(selectableReqeusts.size());

			currentElement = selectableReqeusts.get(index);
			selectableReqeusts.remove(index);

			insertionMethods.computeCostsForAllRoutes(currentElement, routes);
			if (currentElement.isInsertable()) {
				insertionMethods.insertElement(currentElement);
			}
		}
	}
}
