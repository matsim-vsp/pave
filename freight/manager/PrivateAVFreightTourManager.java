package freight.manager;

import java.util.List;

import org.matsim.contrib.dvrp.data.Vehicle;
import org.matsim.contrib.dvrp.schedule.Schedule;

public interface PrivateAVFreightTourManager  {

	public List<Schedule> getAVFreightTours();
	
	public Schedule getRandomAVFreightTour();
	
	public Schedule getBestAVFreightTourForVehicle(Vehicle vehicle);
	
}
