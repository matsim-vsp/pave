package ovgu.data.entity;

import java.util.ArrayList;


public class InsertionPosition {

	public InsertionPosition() {
	}
	
	private float distanceIx;
	private float distanceXj;
	private float travelTime = Float.MAX_VALUE;
	private int insertBevor;
	private ArrayList<RouteElement> route;
	private boolean insertable = true;
	
	public InsertionPosition(float distanceIx, float distanceXj, float travelTime, int insertBevor, ArrayList<RouteElement> route) {
		this.distanceIx = distanceIx;
		this.distanceXj = distanceXj;
		this.travelTime = travelTime;
        this.insertBevor = insertBevor;
        this.route = route;
	}

	public float getDistanceIx() {
		return distanceIx;
	}
	
	public void setDistanceIx(float distanceIx) {
		this.distanceIx = distanceIx;
	}
	
	public float getDistanceXj() {
		return distanceXj;
	}
	
	public void setDistanceXj(float distanceXj) {
		this.distanceXj = distanceXj;
	}
		
	public float getTravelTime() {
		return travelTime;
	}
	
	public void setTravelTime(float travelTime) {
		this.travelTime = travelTime;
	}

	public int getInsertBevor() {
		return insertBevor;
	}
	
	public void setInsertBevor(int insertBevor) {
		this.insertBevor = insertBevor;
	}
	
	public ArrayList<RouteElement> getRoute() {
		return route;
	}
	
	public void setRoute(ArrayList<RouteElement> route) {
		this.route = route;
	}

	public boolean isInsertable() {
		return insertable;
	}

	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}
}
