package freight.manager;

import java.util.List;

import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.schedule.Schedule;

public interface PrivateAVFreightTourManager  {

	public List<Schedule> getAVFreightTours();
	
	public Schedule getRandomAVFreightTour();
	
	public Schedule getBestAVFreightTourForVehicle(DvrpVehicle vehicle);
	
}
