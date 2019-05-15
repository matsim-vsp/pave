package ovgu.pave.handler.shortestPath;

import java.util.List;

import ovgu.pave.model.input.Location;
import ovgu.pave.model.input.WayPoint;

public interface ShortestPath {
	
	public void generateShortestPath(double fromLat, double fromLon, double toLat, double toLon);

	public double getDistance();

	public long getDuration();
	
	public List<WayPoint> getWayPoints(String idPrefix);

	public default void generateShortestPath(Location locationFrom, Location locationTo) {
		generateShortestPath(locationFrom.getLat(), locationFrom.getLon(), locationTo.getLat(), locationTo.getLon());
	}

	public default void generateShortestPath(double fromLat, double fromLon, Location locationTo) {
		generateShortestPath(fromLat, fromLon, locationTo.getLat(), locationTo.getLon());
	}

	public default void generateShortestPath(Location locationFrom, double toLat, double toLon) {
		generateShortestPath(locationFrom.getLat(), locationFrom.getLon(), toLat, toLon);
	}
}
