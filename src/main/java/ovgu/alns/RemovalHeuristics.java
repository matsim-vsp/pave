package ovgu.alns;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import ovgu.data.entity.RouteElement;
import ovgu.timewindows.TimeWindow;
import ovgu.timewindows.TimeWindowList;
import ovgu.utilities.DistanceMatrix;
import ovgu.utilities.Settings;

public class RemovalHeuristics {

	private Random random = new Random();
	private DistanceMatrix matrix;
	private double lowerBound;
	private double upperBound;
	private int numberToRemove;
	private ArrayList<RouteElement> includedRequests;
	private ArrayList<RouteElement> openRequests;
	private Comparator<RouteElement> rankingValueComparator = Comparator.comparing(RouteElement::getRankingValue);
	private Comparator<RouteElement> rankingComparatorReversed = rankingValueComparator.reversed();
	private ArrayList<TimeWindow> timeWindowList = new ArrayList<TimeWindow>();

	public RemovalHeuristics(DistanceMatrix matrix, ArrayList<RouteElement> includedRequests,
			ArrayList<RouteElement> openRequests) {

		this.matrix = matrix;
		this.includedRequests = includedRequests;
		this.openRequests = openRequests;
		TimeWindowList timeWindowList120 = new TimeWindowList(timeWindowList);
		timeWindowList120.addTimeWindow(timeWindowList);
	}

	// performs selected heuristic
	public void removeElements(int removalHeuristic, ArrayList<ArrayList<RouteElement>> routes) {
		
		if (Settings.removeLarge1 <= includedRequests.size() * Settings.removeSmall1) lowerBound = Settings.removeLarge1;
		else lowerBound = Math.floor(includedRequests.size() * Settings.removeSmall1); 
		
		if (Settings.removeLarge2 <= includedRequests.size() * Settings.removeSmall2) upperBound = Settings.removeLarge2 - lowerBound;
		else upperBound = Math.ceil(includedRequests.size() * Settings.removeSmall2) - lowerBound; 
	
		numberToRemove = (random.nextInt((int) upperBound) + (int) lowerBound);

		switch (removalHeuristic) {
		case 0:
			randomRemoval();
			break;
		case 1:
			timeWindowRemoval();
			break;
		case 2:
			worstRemoval();
			break;
		case 3:
			historicalRemoval();
			break;
		case 4:
			shawRemoval();
			break;
		case 5:
			clusterRemoval(routes);
			break;
		}
	}
	
	private void randomRemoval() {

		ArrayList<RouteElement> route;
		RouteElement currentElement;
		int index;

		for (int i = 0; i < numberToRemove; i++) {
			index = random.nextInt(includedRequests.size());
			currentElement = includedRequests.get(index);
			includedRequests.remove(index);
			openRequests.add(currentElement);
			route = currentElement.getRoute();
			index = route.indexOf(currentElement);
			route.remove(index);
			currentElement.setRoute(null);
			updateRoute(route, index);
		}
	}
	
	private void timeWindowRemoval() {

		ArrayList<RouteElement> route;
		ArrayList<RouteElement> selectableElements = new ArrayList<RouteElement>();
		
		int selectedTimeWindow = random.nextInt(timeWindowList.size());
		float selectedTimeWindowBegin = timeWindowList.get(selectedTimeWindow).getBegin();
		
		for (RouteElement currentElement : includedRequests) {
			if (selectedTimeWindowBegin == currentElement.getStartTimeWindow())
				selectableElements.add(currentElement);
		}
		
		int index;	
		RouteElement currentElement;
		for (int i = 0; i < numberToRemove & i < selectableElements.size(); i++) {
			index = random.nextInt(selectableElements.size());
			currentElement = selectableElements.get(index);
			selectableElements.remove(index);
			includedRequests.remove(currentElement);
			openRequests.add(currentElement);
			route = currentElement.getRoute();
			currentElement.setRoute(null);
			index = route.indexOf(currentElement);
			route.remove(index);
			updateRoute(route, index);
		}
	}

	private void worstRemoval() {

		ArrayList<RouteElement> route;
		int index;

		for (RouteElement currentElement : includedRequests) {
			currentElement.setRankingValue(currentElement.getTravelTimeTo() + currentElement.getTravelTimeFrom());
		}

		includedRequests.sort(rankingComparatorReversed);

		RouteElement currentElement;
		for (int i = 0; i < numberToRemove; i++) {
			index = (int) Math.pow(random.nextDouble(), 3) * includedRequests.size();; 
			currentElement = includedRequests.get(index);
			includedRequests.remove(index);
			openRequests.add(currentElement);
			route = currentElement.getRoute();
			currentElement.setRoute(null);
			index = route.indexOf(currentElement);
			route.remove(index);
			updateRoute(route, index);
		}
	}

	private void historicalRemoval() {

		ArrayList<RouteElement> route;
		int index;

		for (RouteElement currentElement : includedRequests) {
			currentElement
					.setRankingValue(currentElement.getBestHistoricalTravelTime() - currentElement.getTravelTimeFrom());
		}

		includedRequests.sort(rankingValueComparator);
		
		RouteElement currentElement;
		for (int i = 0; i < numberToRemove; i++) {
			index = 0;
			currentElement = includedRequests.get(index);
			includedRequests.remove(index);
			openRequests.add(currentElement);
			route = currentElement.getRoute();
			currentElement.setRoute(null);
			index = route.indexOf(currentElement);
			route.remove(index);
			updateRoute(route, index);
		}
	}

	public void shawRemoval() {

		ArrayList<RouteElement> route;

		int index = random.nextInt(includedRequests.size());
		RouteElement selectedElement = includedRequests.get(index);
		includedRequests.remove(index);
		openRequests.add(selectedElement);
		route = selectedElement.getRoute();
		selectedElement.setRoute(null);
		index = route.indexOf(selectedElement);
		route.remove(index);
		updateRoute(route, index);
		numberToRemove--;

		float travelTime;
		float serviceTime;
		float minServiceTime = Float.MAX_VALUE;
		float maxServiceTime = 0;
		float minTravelTime = Float.MAX_VALUE; 
		float maxTravelTime = 0;
		
		for (RouteElement currentElement : includedRequests) {	
		
			travelTime = matrix.getTravelTime(selectedElement, currentElement);
			serviceTime =  Math.abs(selectedElement.getServiceBegin() - currentElement.getServiceBegin());
			
			if (minServiceTime > serviceTime) minServiceTime = serviceTime;
			if (maxServiceTime < serviceTime) maxServiceTime = serviceTime;
			if (minTravelTime > travelTime) minTravelTime = travelTime;
			if (maxTravelTime < travelTime) maxTravelTime = travelTime;
		}
		

		for (RouteElement currentElement : includedRequests) {
			currentElement.setRankingValue(((9 * (matrix.getTravelTime(selectedElement, currentElement)-minTravelTime)/(maxTravelTime-minTravelTime))) +
					(3 * ((Math.abs(selectedElement.getServiceBegin() - currentElement.getServiceBegin())-minServiceTime)/(maxServiceTime-minServiceTime))));
		}

		includedRequests.sort(rankingValueComparator);

		RouteElement currentElement;
		for (int i = 0; i < numberToRemove; i++) {
			index = (int) Math.pow(random.nextDouble(), 6) * includedRequests.size();;
			currentElement = includedRequests.get(index);
			includedRequests.remove(index);
			openRequests.add(currentElement);
			route = currentElement.getRoute();
			currentElement.setRoute(null);
			index = route.indexOf(currentElement);
			route.remove(index);
			updateRoute(route, index);
		}
	}

	public void clusterRemoval(ArrayList<ArrayList<RouteElement>> routes) {

		ArrayList<RouteElement> route;
		
		while (numberToRemove > 0) {
			
			int index = random.nextInt(includedRequests.size());
			RouteElement selectedElement = includedRequests.get(index);
			includedRequests.remove(index);
			openRequests.add(selectedElement);
			route = selectedElement.getRoute();
			selectedElement.setRoute(null);
			index = route.indexOf(selectedElement);
			route.remove(index);
			updateRoute(route, index);
			numberToRemove--;

			ArrayList<RouteElement> selectableElements = new ArrayList<RouteElement>();
			RouteElement currentElement;
			for (int i = 1; i < route.size() - 1; i++) {
				currentElement = route.get(i);
				currentElement.setRankingValue(matrix.getTravelTime(selectedElement, currentElement));
				selectableElements.add(currentElement);
			}

			selectableElements.sort(rankingValueComparator);

			while (numberToRemove > 0 && selectableElements.size() > 0) {

				index = 0;
				currentElement = selectableElements.get(index);
				selectableElements.remove(index);

				currentElement.setRoute(null);
				index = route.indexOf(currentElement);
				route.remove(index);
				updateRoute(route, index);

				includedRequests.remove(currentElement);
				openRequests.add(currentElement);
				numberToRemove--;
			}
		}
	}

	private void updateRoute(ArrayList<RouteElement> route, int checkFromHere) {

		RouteElement fromElement = route.get(checkFromHere - 1);
		RouteElement toElement = route.get(checkFromHere);

		float travelTime = matrix.getTravelTime(fromElement, toElement);

		fromElement.setTravelTimeFrom(travelTime);
		toElement.setTravelTimeTo(travelTime);

		for (int i = checkFromHere; i < route.size(); i++) {
			if (route.get(i).updateElement(route.get(i - 1)))
				break;
		}
	}
}
