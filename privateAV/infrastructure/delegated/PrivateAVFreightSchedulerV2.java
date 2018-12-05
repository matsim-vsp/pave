package privateAV.infrastructure.delegated;

import java.util.List;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.data.Fleet;
import org.matsim.contrib.dvrp.data.Vehicle;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelDataImpl;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.dvrp.schedule.Schedules;
import org.matsim.contrib.dvrp.schedule.StayTaskImpl;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.dvrp.schedule.Schedule.ScheduleStatus;
import org.matsim.contrib.dvrp.tracker.OnlineDriveTaskTracker;
import org.matsim.contrib.dvrp.trafficmonitoring.DvrpTravelTimeModule;
import org.matsim.contrib.dvrp.util.LinkTimePair;
import org.matsim.contrib.taxi.data.TaxiRequest;
import org.matsim.contrib.taxi.data.TaxiRequest.TaxiRequestStatus;
import org.matsim.contrib.taxi.optimizer.DefaultTaxiOptimizerProvider;
import org.matsim.contrib.taxi.run.Taxi;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.contrib.taxi.schedule.TaxiDropoffTask;
import org.matsim.contrib.taxi.schedule.TaxiEmptyDriveTask;
import org.matsim.contrib.taxi.schedule.TaxiOccupiedDriveTask;
import org.matsim.contrib.taxi.schedule.TaxiPickupTask;
import org.matsim.contrib.taxi.schedule.TaxiStayTask;
import org.matsim.contrib.taxi.schedule.TaxiTask;
import org.matsim.contrib.taxi.schedule.TaxiTask.TaxiTaskType;
import org.matsim.contrib.taxi.scheduler.TaxiScheduleInquiry;
import org.matsim.contrib.taxi.scheduler.TaxiScheduler;
import org.matsim.core.mobsim.framework.MobsimTimer;
import org.matsim.core.router.FastAStarEuclideanFactory;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import freight.manager.PrivateAVFreightTourManager;
import privateAV.Task.TaxiFreightServiceDriveTask;
import privateAV.Task.TaxiFreightServiceTask;
import privateAV.Task.TaxiFreightStartTask;

public class PrivateAVFreightSchedulerV2 implements TaxiScheduleInquiry {

	private TaxiScheduler delegate;
	private Link DEPOT_LINK;
	private LeastCostPathCalculator router;
	private TravelTime travelTime;
	private MobsimTimer timer;
	private TaxiConfigGroup taxiCfg;

	@Inject
	PrivateAVFreightTourManager freightManager;
	
	/**
	 * @param taxiCfg
	 * @param network
	 * @param fleet
	 * @param timer
	 * @param params
	 * @param travelTime
	 * @param travelDisutility
	 */
	@Inject
	public PrivateAVFreightSchedulerV2(TaxiConfigGroup taxiCfg, @Taxi Fleet fleet, @Named(DvrpRoutingNetworkProvider.DVRP_ROUTING) Network network,
			MobsimTimer timer, @Named(DvrpTravelTimeModule.DVRP_ESTIMATED) TravelTime travelTime,
			@Named(DefaultTaxiOptimizerProvider.TAXI_OPTIMIZER) TravelDisutility travelDisutility) {
		
		DEPOT_LINK = network.getLinks().get(Id.createLinkId("560"));
		this.taxiCfg = taxiCfg;
		this.router = new FastAStarEuclideanFactory(taxiCfg.getAStarEuclideanOverdoFactor()).createPathCalculator(network,
				travelDisutility, travelTime);
		this.travelTime = travelTime;
		this.timer = timer;
	
		delegate = new TaxiScheduler(taxiCfg, fleet, network, timer, travelTime, travelDisutility);
	}
	
	public void stopCruisingVehicle(Vehicle vehicle) {
		if (!taxiCfg.isVehicleDiversion()) {
			throw new RuntimeException("Diversion must be on");
		}

		Schedule schedule = vehicle.getSchedule();
		TaxiEmptyDriveTask driveTask = (TaxiEmptyDriveTask)Schedules.getNextToLastTask(schedule);
		schedule.removeLastTask();
		OnlineDriveTaskTracker tracker = (OnlineDriveTaskTracker)driveTask.getTaskTracker();
		LinkTimePair stopPoint = tracker.getDiversionPoint();
		tracker.divertPath(
				new VrpPathWithTravelDataImpl(stopPoint.time, 0, new Link[] { stopPoint.link }, new double[] { 0 }));

		appendStayTask(vehicle);
		
	}
	

	public void updateBeforeNextTask(Vehicle vehicle) {
		Schedule schedule = vehicle.getSchedule();
		// Assumption: there is no delay as long as the schedule has not been started (PLANNED)
		if (schedule.getStatus() != ScheduleStatus.STARTED) {
			return;
		}

		delegate.updateTimeline(vehicle);

		TaxiTask currentTask = (TaxiTask)schedule.getCurrentTask();
	
		switch(currentTask.getTaxiTaskType()) {
		
		case PICKUP:
			if (!taxiCfg.isDestinationKnown()) {
				appendResultingTasksAfterPickup(vehicle);
			}
		case DROPOFF:
			if(currentTask instanceof TaxiDropoffTask) {
				Schedule freightTour = freightManager.getBestAVFreightTourForVehicle(vehicle); 
				if(freightTour != null) {
					insertFreightSchedule(vehicle, freightTour, (TaxiDropoffTask) currentTask);
				} else {
					throw new IllegalArgumentException();
				}
			}
			
		}
	}
	

	private void insertFreightSchedule(Vehicle vehicle, Schedule freightTour, TaxiDropoffTask currentTask) {
		Schedule schedule = vehicle.getSchedule();
		
		if( ((TaxiTask) Schedules.getNextTask(schedule)).getTaxiTaskType() != TaxiTaskType.STAY) {
			throw new IllegalStateException(
					"if a freight tour shall be inserted - the next vehicle task must be STAY. That is not the case for vehicle " + vehicle.getId() + " at " + timer.getTimeOfDay());
		} else {
			
			StayTaskImpl previousTask = (StayTaskImpl) freightTour.getTasks().get(0);
			
			
			//add EmptyDriveTask
			VrpPathWithTravelData pathToStart = VrpPaths.calcAndCreatePath(currentTask.getLink(), previousTask.getLink(), currentTask.getEndTime(), router, travelTime);
			schedule.addTask(new TaxiEmptyDriveTask(pathToStart));
			//add start task
			schedule.addTask(freightTour.getTasks().get(0));
			
			for(int i= 1; i<freightTour.getTaskCount(); i++) {
				Task currentFreightTask = freightTour.getTasks().get(i);
				if(currentFreightTask instanceof StayTaskImpl) {
					StayTaskImpl stayTask = (StayTaskImpl) currentFreightTask;
					VrpPathWithTravelData path = VrpPaths.calcAndCreatePath(previousTask.getLink(), stayTask.getLink(), previousTask.getEndTime(), router, travelTime);
					schedule.addTask(new TaxiFreightServiceDriveTask(path));
					schedule.addTask(currentFreightTask);
					previousTask = stayTask;
				} else {
					throw new IllegalStateException();
				}
			}
		}
	}

	public void scheduleRequest(Vehicle vehicle, TaxiRequest request, VrpPathWithTravelData vrpPath) {
		if (request.getStatus() != TaxiRequestStatus.UNPLANNED) {
			throw new IllegalStateException();
		}

		Schedule schedule = vehicle.getSchedule();
		divertOrAppendDrive(schedule, vrpPath);

		double pickupEndTime = Math.max(vrpPath.getArrivalTime(), request.getEarliestStartTime())
				+ taxiCfg.getPickupDuration();
		schedule.addTask(new TaxiPickupTask(vrpPath.getArrivalTime(), pickupEndTime, request));
		
		appendResultingTasksAfterPickup(vehicle);
	}

	private void appendResultingTasksAfterPickup(Vehicle vehicle) {
		appendOccupiedDriveAndDropoff(vehicle.getSchedule());
		appendStayTask(vehicle);
	}
	
	protected void appendOccupiedDriveAndDropoff(Schedule schedule) {
		TaxiPickupTask pickupStayTask = (TaxiPickupTask)Schedules.getLastTask(schedule);

		// add DELIVERY after SERVE
		TaxiRequest req = ((TaxiPickupTask)pickupStayTask).getRequest();
		Link reqFromLink = req.getFromLink();
		Link reqToLink = req.getToLink();
		double t3 = pickupStayTask.getEndTime();
		VrpPathWithTravelData path = VrpPaths.calcAndCreatePath(reqFromLink, reqToLink, t3, router, travelTime);
		schedule.addTask(new TaxiOccupiedDriveTask(path, req));

		double t4 = path.getArrivalTime();
		double t5 = t4 + taxiCfg.getDropoffDuration();
		schedule.addTask(new TaxiDropoffTask(t4, t5, req));
	}
	
	private void appendStayTask(Vehicle vehicle) {
		Schedule schedule = vehicle.getSchedule();
		double tBegin = schedule.getEndTime();
		double tEnd = Math.max(tBegin, vehicle.getServiceEndTime());// even 0-second WAIT
		Link link = Schedules.getLastLinkInSchedule(vehicle);
		schedule.addTask(new TaxiStayTask(tBegin, tEnd, link));		
	}
	

	protected void cancelFreightTour(Vehicle veh) {
		// TODO Auto-generated method stub
		
	}
	
	//---------------------------------------------------- DELEGATE METHODS --------------
	
	
	//-------------------------------COPIES

	protected void divertOrAppendDrive(Schedule schedule, VrpPathWithTravelData vrpPath) {
		TaxiTask lastTask = (TaxiTask)Schedules.getLastTask(schedule);
		switch (lastTask.getTaxiTaskType()) {
			case EMPTY_DRIVE:				
				divertDrive((TaxiEmptyDriveTask)lastTask, vrpPath);
				return;

			case STAY:
				scheduleDrive(schedule, (TaxiStayTask)lastTask, vrpPath);
				return;

			default:
				throw new IllegalStateException();
		}
	}
	
	
	//this should be unnecessary - since the last task should always be a stay task
	protected void divertDrive(TaxiEmptyDriveTask lastTask, VrpPathWithTravelData vrpPath) {
		if (!taxiCfg.isVehicleDiversion()) {
			throw new IllegalStateException();
		}

		((OnlineDriveTaskTracker)lastTask.getTaskTracker()).divertPath(vrpPath);
	}
	
	protected void scheduleDrive(Schedule schedule, TaxiStayTask lastTask, VrpPathWithTravelData vrpPath) {
		switch (lastTask.getStatus()) {
			case PLANNED:
				if (lastTask.getBeginTime() == vrpPath.getDepartureTime()) { // waiting for 0 seconds!!!
					schedule.removeLastTask();// remove WaitTask
				} else {
					// actually this WAIT task will not be performed
					lastTask.setEndTime(vrpPath.getDepartureTime());// shortening the WAIT task
				}
				break;

			case STARTED:
				lastTask.setEndTime(vrpPath.getDepartureTime());// shortening the WAIT task
				break;

			case PERFORMED:
			default:
				throw new IllegalStateException();
		}

		if (vrpPath.getLinkCount() > 1) {
			schedule.addTask(new TaxiEmptyDriveTask(vrpPath));
		}
	}
	
	
	//-------------------------TRUE DELEGATES-----------------------------------
	
	
	
	public boolean isIdle(Vehicle vehicle) {
		return delegate.isIdle(vehicle);
	}

	public LinkTimePair getImmediateDiversionOrEarliestIdleness(Vehicle veh) {
		return delegate.getImmediateDiversionOrEarliestIdleness(veh);
	}

	public LinkTimePair getEarliestIdleness(Vehicle veh) {
		return delegate.getEarliestIdleness(veh);
	}

	public LinkTimePair getImmediateDiversion(Vehicle veh) {
		return delegate.getImmediateDiversion(veh);
	}


	public void stopAllAimlessDriveTasks() {
		delegate.stopAllAimlessDriveTasks();
	}

	public void stopVehicle(Vehicle vehicle) {
		delegate.stopVehicle(vehicle);
	}

	public void updateTimeline(Vehicle vehicle) {
		delegate.updateTimeline(vehicle);
	}

	public List<TaxiRequest> removeAwaitingRequestsFromAllSchedules() {
		return delegate.removeAwaitingRequestsFromAllSchedules();
	}

	public List<TaxiRequest> removeAwaitingRequests(Vehicle vehicle) {
		return delegate.removeAwaitingRequests(vehicle);
	}



}
