package ovgu.alns;

import java.util.ArrayList;
import java.util.Comparator;

import ovgu.data.entity.InsertionPosition;
import ovgu.data.entity.RouteElement;
import ovgu.utilities.DistanceMatrix;

public class InsertionMethods {

	private DistanceMatrix matrix;
	private Comparator<InsertionPosition> travelTimeComparator = Comparator.comparing(InsertionPosition::getTravelTime);
	private ArrayList<RouteElement> openRequests;
	private ArrayList<RouteElement> includedRequests;

	public InsertionMethods(DistanceMatrix matrix, ArrayList<RouteElement> includedRequests,
			ArrayList<RouteElement> openRequests) {
		this.matrix = matrix;
		this.includedRequests = includedRequests;
		this.openRequests = openRequests;
	}

	public void computeCostsForAllRoutes(RouteElement currentElement, ArrayList<ArrayList<RouteElement>> routes) {
		ArrayList<InsertionPosition> insertionPositions = new ArrayList<InsertionPosition>();
		currentElement.setInsertionPositions(insertionPositions);

		for (ArrayList<RouteElement> currentRoute : routes) {
			insertionPositions.add(computeInsertionCosts(currentRoute, currentElement));
		}
		insertionPositions.sort(travelTimeComparator);
		if (insertionPositions.get(0).isInsertable())
			currentElement.setInsertable(true);
		else
			currentElement.setInsertable(false);
	}

	public InsertionPosition computeInsertionCosts(ArrayList<RouteElement> route, RouteElement currentElement) {

		InsertionPosition insertionPosition = new InsertionPosition();
		insertionPosition.setRoute(route);
		float currentElementStartTimeWindow = currentElement.getStartTimeWindow();
		float distanceIx;
		float distanceXj;
		float travelTime;
		int insertBevor = 0;

		do {
			insertBevor++;
			if (route.get(insertBevor).getEndTimeWindow() > currentElementStartTimeWindow
					|| insertBevor == route.size() - 1) {
				distanceIx = matrix.getTravelTime(route.get(insertBevor - 1), currentElement);
				distanceXj = matrix.getTravelTime(currentElement, route.get(insertBevor));
				travelTime = distanceIx + distanceXj - route.get(insertBevor).getTravelTimeTo();
				if (travelTime < insertionPosition.getTravelTime()) {
					insertionPosition.setInsertBevor(insertBevor);
					insertionPosition.setTravelTime(travelTime);
					insertionPosition.setDistanceIx(distanceIx);
					insertionPosition.setDistanceXj(distanceXj);
				}
			}
		} while (insertBevor < route.size() - 1
				&& route.get(insertBevor).getStartTimeWindow() < currentElement.getEndTimeWindow());
		if (!checkFeasibility(currentElement, insertionPosition)) {
			insertionPosition.setTravelTime(Float.MAX_VALUE);
			insertionPosition.setInsertable(false);
		}
		return insertionPosition;
	}

	public boolean checkFeasibility(RouteElement currentElement, InsertionPosition insertionPosition) {

		ArrayList<RouteElement> currentRoute = insertionPosition.getRoute();
		float newServiceBegin = (float) (currentRoute.get(insertionPosition.getInsertBevor() - 1).getServiceEnd()
				+ insertionPosition.getDistanceIx());

		if (newServiceBegin > currentElement.getEndTimeWindow())
			return false;
		else if (newServiceBegin >= currentElement.getStartTimeWindow())
			newServiceBegin = (float) (newServiceBegin + currentElement.getServiceTime()
					+ insertionPosition.getDistanceXj());
		else
			newServiceBegin = (float) (currentElement.getStartTimeWindow() + currentElement.getServiceTime()
					+ insertionPosition.getDistanceXj());

		for (int i = insertionPosition.getInsertBevor(); i < currentRoute.size() - 1; i++) {
			if (newServiceBegin <= currentRoute.get(i).getServiceBegin())
				break;
			else if (newServiceBegin > currentRoute.get(i).getEndTimeWindow())
				return false;
			else
				newServiceBegin = newServiceBegin + currentRoute.get(i).getTravelTimeFrom()
						+ currentRoute.get(i).getServiceTime();
		}
		return true;
	}

	public void insertElement(RouteElement currentElement) {

		openRequests.remove(currentElement);
		includedRequests.add(currentElement);

		InsertionPosition insertionPosition = currentElement.getInsertionPositions().get(0);
		currentElement.setInsertionPositions(null);

		ArrayList<RouteElement> route = insertionPosition.getRoute();
		int insertBevor = insertionPosition.getInsertBevor();

		if (insertionPosition.getTravelTime() < currentElement.getBestHistoricalTravelTime()) {
			currentElement.setBestHistoricalTravelTime(insertionPosition.getTravelTime());
		}

		route.add(insertBevor, currentElement);
		currentElement.setRoute(route);
		currentElement.setTravelTimeTo(insertionPosition.getDistanceIx());
		currentElement.setTravelTimeFrom(insertionPosition.getDistanceXj());
		currentElement.setServiceBegin(0F);
		route.get(insertBevor - 1).setTravelTimeFrom(insertionPosition.getDistanceIx());
		route.get(insertBevor + 1).setTravelTimeTo(insertionPosition.getDistanceXj());

		for (int i = insertBevor; i < route.size(); i++) {
			if (route.get(i).updateElement(route.get(i - 1)))
				break;
		}
	}
}
