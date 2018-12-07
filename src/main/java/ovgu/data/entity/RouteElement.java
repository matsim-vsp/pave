package ovgu.data.entity;

import java.util.ArrayList;


public class RouteElement{

	private Integer id;
	private Integer timeWindowIDRandom;
	private Integer timeWindowIDAllyouneed;
	private Integer timeWindowKey;
	private Float travelTimeTo = 0f;  // travel time to location
	private Float travelTimeFrom = 0f; // travel time from location
	private Float waitingTime = 0f;
	private Float serviceBegin = 0f;
	private Float serviceTime = 12f;
	private boolean insertable = false;
	
	
	// New for ALNS //
	private ArrayList<RouteElement> route = null; 
	private Float serviceEnd = 0f;
	private Float startTimeWindow;
	private Float endTimeWindow;
	private Float bestHistoricalTravelTime = Float.MAX_VALUE;
	
	private ArrayList<InsertionPosition> insertionPositions; //All possible insertion position, only short-term relevant
	private Float rankingValue  = Float.MAX_VALUE;;  //only short-term relevant
	
	private int positionDistanceMatrix ; //which node in distance matrix
	
	public RouteElement(int id, int timeWindowIDRandom, int timeWindowIDAllyouneed) {	       
		this.id = id;
		this.positionDistanceMatrix = id;
        this.setTimeWindowIDRandom(timeWindowIDRandom);
        this.setTimeWindowIDAllyouneed(timeWindowIDAllyouneed);
    }
	
	public RouteElement() {
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTimeWindowKey() {
		return timeWindowKey;
	}
	public void setTimeWindowKey(Integer timeWindowKey) {
		this.timeWindowKey = timeWindowKey;
	}
	
	public Float getTravelTimeTo() {
		return travelTimeTo;
	}
	public void setTravelTimeTo(Float travelTimeTo) {
		this.travelTimeTo = travelTimeTo;
	}
	
	public Float getTravelTimeFrom() {
		return travelTimeFrom;
	}
	public void setTravelTimeFrom(Float travelTimeFrom) {
		this.travelTimeFrom = travelTimeFrom;
	}

	public Float getWaitingTime() {
		return waitingTime;
	}
	public void setWaitingTime(Float waitingTime) {
		this.waitingTime = waitingTime;
	}
	public Float getServiceBegin() {
		return serviceBegin;
	}
	public void setServiceBegin(Float serviceBegin) {
		this.serviceBegin = serviceBegin;
	}
	public Float getServiceTime() {
		return serviceTime;
	}
	public void setServiceTime(Float serviceTime) {
		this.serviceTime = serviceTime;
	}
	
	// New for LMNS //
	
	public ArrayList<RouteElement> getRoute() {
		return route;
	}
	public void setRoute(ArrayList<RouteElement> route) {
		this.route = route;
	}
	
	public Float getServiceEnd() {
		return serviceEnd;
	}
	public void setServiceEnd(Float serviceEnd) {
		this.serviceEnd = serviceEnd;
	}
	
	public Float getStartTimeWindow() {
		return startTimeWindow;
	}
	public void setStartTimeWindow(Float startTimeWindow) {
		this.startTimeWindow = startTimeWindow;
	}
	
	public Float getEndTimeWindow() {
		return endTimeWindow;
	}
	public void setEndTimeWindow(Float endTimeWindow) {
		this.endTimeWindow = endTimeWindow;
	}
	
	public Float getBestHistoricalTravelTime() {
		return bestHistoricalTravelTime;
	}
	public void setBestHistoricalTravelTime(Float bestHistoricalTravelTime) {
		this.bestHistoricalTravelTime = bestHistoricalTravelTime;
	}
	
	public ArrayList<InsertionPosition> getInsertionPositions() {
		return insertionPositions;
	}
	public void setInsertionPositions(ArrayList<InsertionPosition> insertionPositions) {
		this.insertionPositions = insertionPositions;
	}
	
	public Float getRankingValue() {
		return rankingValue ;
	}
	public void setRankingValue(Float rankingValue ) {
		this.rankingValue  = rankingValue;
	}
	
	public RouteElement copyRouteElement(ArrayList<RouteElement> routeCopy) {
			
		RouteElement elementCopy = new RouteElement();		
		
		elementCopy.id = this.id;
		elementCopy.timeWindowKey = this.timeWindowKey;
		elementCopy.insertable = this.insertable;
		
		elementCopy.travelTimeTo = this.travelTimeTo;
		elementCopy.travelTimeFrom = this.travelTimeFrom;
		elementCopy.waitingTime = this.waitingTime;
		elementCopy.serviceBegin = this.serviceBegin;
		elementCopy.serviceTime = this.serviceTime;
		
		elementCopy.route = routeCopy;	
		elementCopy.serviceEnd = this.serviceEnd;	
		elementCopy.startTimeWindow = this.startTimeWindow;
		elementCopy.endTimeWindow = this.endTimeWindow;			
		elementCopy.bestHistoricalTravelTime = this.bestHistoricalTravelTime;
		elementCopy.positionDistanceMatrix = this.positionDistanceMatrix;
		
		return elementCopy;
	}
		
		
	public boolean updateElement(RouteElement previousElement) {
		
		float newServiceBegin = previousElement.getServiceEnd() + travelTimeTo;
	
		 if (newServiceBegin  == serviceBegin) return true; 
		 if (newServiceBegin < startTimeWindow) {
			serviceBegin = startTimeWindow;
			waitingTime = startTimeWindow - newServiceBegin;
		}
		else {
			serviceBegin = newServiceBegin;
			waitingTime = 0f;
		}
		
		serviceEnd = serviceBegin + serviceTime;
	
		return false;
	}
	
	public int getPositionDistanceMatrix() {
		return positionDistanceMatrix;
	}
	public void setPositionDistanceMatrix(int positionDistanceMatrix) {
		this.positionDistanceMatrix = positionDistanceMatrix;
	}

	public Integer getTimeWindowIDRandom() {
		return timeWindowIDRandom;
	}

	public void setTimeWindowIDRandom(Integer timeWindowIDRandom) {
		this.timeWindowIDRandom = timeWindowIDRandom;
	}

	public Integer getTimeWindowIDAllyouneed() {
		return timeWindowIDAllyouneed;
	}

	public void setTimeWindowIDAllyouneed(Integer timeWindowIDAllyouneed) {
		this.timeWindowIDAllyouneed = timeWindowIDAllyouneed;
	}

	public boolean isInsertable() {
		return insertable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}
}
