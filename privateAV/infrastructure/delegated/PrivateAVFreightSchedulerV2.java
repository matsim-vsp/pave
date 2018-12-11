package privateAV.infrastructure.delegated;

import java.util.List;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.data.Fleet;
import org.matsim.contrib.dvrp.data.Vehicle;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelDataImpl;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.schedule.DriveTask;
import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.dvrp.schedule.Schedules;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.dvrp.schedule.StayTaskImpl;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.dvrp.schedule.Task.TaskStatus;
import org.matsim.contrib.dvrp.schedule.Schedule.ScheduleStatus;
import org.matsim.contrib.dvrp.tracker.OnlineDriveTaskTracker;
import org.matsim.contrib.dvrp.tracker.TaskTrackers;
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
import org.matsim.core.utils.misc.Time;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import freight.manager.ListBasedFreightTourManager;
import freight.manager.ListBasedFreightTourManagerImpl;
import freight.manager.PrivateAVFreightTourManager;
import freight.manager.SimpleFreightTourManager;
import privateAV.Task.TaxiFreightServiceDriveTask;
import privateAV.Task.TaxiFreightServiceTask;
import privateAV.Task.TaxiFreightStartTask;
import privateAV.infrastructure.ConvertFreightTourForDvrp;

public class PrivateAVFreightSchedulerV2 implements TaxiScheduleInquiry {

	private static final Logger log = Logger.getLogger(PrivateAVFreightSchedulerV2.class);
	
	private TaxiScheduler delegate;
	private Link DEPOT_LINK;
	private LeastCostPathCalculator router;
	private TravelTime travelTime;
	private MobsimTimer timer;
	private TaxiConfigGroup taxiCfg;

////	@Inject
//	SimpleFreightTourManager freightManager;
	
	ListBasedFreightTourManager freightManager;
	
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
			@Named(DefaultTaxiOptimizerProvider.TAXI_OPTIMIZER) TravelDisutility travelDisutility){
		
		DEPOT_LINK = network.getLinks().get(Id.createLinkId("560"));
		this.taxiCfg = taxiCfg;
		this.router = new FastAStarEuclideanFactory(taxiCfg.getAStarEuclideanOverdoFactor()).createPathCalculator(network,
				travelDisutility, travelTime);
		this.travelTime = travelTime;
		this.timer = timer;
		
		this.freightManager = new ListBasedFreightTourManagerImpl(network, null);
		
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

		updateTimeline(vehicle);

		TaxiTask currentTask = (TaxiTask)schedule.getCurrentTask();
	
		switch(currentTask.getTaxiTaskType()) {
		
		case PICKUP:
			if (!taxiCfg.isDestinationKnown()) {
				appendResultingTasksAfterPickup(vehicle);
			}
			break;
		case DROPOFF:
				System.out.println("Ich (vehicle " + vehicle.getId() + ") bin fertig mit PASSENGER Dropoff");
//				Schedule freightTour = freightManager.getBestAVFreightTourForVehicle(vehicle); 
//				if(freightTour != null) {
//					log.info("++++++++++++++ Vehicle " + vehicle.getId() + " is assigned to a freight tour!++++++");
//					insertFreightSchedule(vehicle, freightTour, (TaxiDropoffTask) currentTask);
//				} else {
//					throw new IllegalArgumentException();
//				}
				
				List<StayTask> freightTour = freightManager.getBestAVFreightTourForVehicle(vehicle); 
				if(freightTour != null) {
					log.info("Vehicle " + vehicle.getId() + " requests a freight tour");
					scheduleFreightTour(vehicle, freightTour);
				} else {
					log.info("+++++ vehicle " + vehicle.getId() + " requested a freight tour but the manager returned NULL ++++++");
				}
			break;
			
		case STAY: {
			break;
		}
		case EMPTY_DRIVE:
			break;
		case OCCUPIED_DRIVE: 
			break;
		default:{
			throw new IllegalStateException();
		}	
			
		}
	}
	

	private void insertFreightSchedule(Vehicle vehicle, Schedule freightTour, TaxiDropoffTask currentTask) {

		//schedule just got updated.. current task should be a dropOff, nextTast should be a STAY task
		Schedule schedule = vehicle.getSchedule();
		
		
		if( ((TaxiTask) Schedules.getNextTask(schedule)).getTaxiTaskType() != TaxiTaskType.STAY) {
			throw new IllegalStateException(
					"if a freight tour shall be inserted - the next vehicle task must be STAY. That is not the case for vehicle " + vehicle.getId() + " at " + timer.getTimeOfDay());
		} else {
			
			//remove the stay task
			schedule.removeLastTask();
			
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
	
	private void scheduleFreightTour(Vehicle vehicle, List<StayTask> freightActivities) {
		
		//schedule just got updated.. current task should be a dropOff, nextTast should be a STAY task
		Schedule schedule = vehicle.getSchedule();
		
		if( ((TaxiTask) Schedules.getNextTask(schedule)).getTaxiTaskType() != TaxiTaskType.STAY) {
			throw new IllegalStateException(
					"if a freight tour shall be inserted - the next vehicle task must be STAY. That is not the case for vehicle " + vehicle.getId() + " at " + timer.getTimeOfDay());
		} else {
			//remove the stay task
			schedule.removeLastTask();
			
			StayTask previousTask = (StayTaskImpl) schedule.getCurrentTask();
			
			for(int i = 0; i < freightActivities.size(); i++) {
				StayTask currentTask = freightActivities.get(i);
				
				VrpPathWithTravelData path = VrpPaths.calcAndCreatePath(previousTask.getLink(), currentTask.getLink(), previousTask.getEndTime(), router, travelTime);
				
				if(i == 0 || i == freightActivities.size() - 1) {
					schedule.addTask(new TaxiEmptyDriveTask(path));
				} else {
					schedule.addTask(new TaxiFreightServiceDriveTask(path));
					
				}
				
				double duration = currentTask.getEndTime() - currentTask.getBeginTime();
				currentTask.setBeginTime(path.getArrivalTime());
				currentTask.setEndTime(path.getArrivalTime() + duration);
				
				if(currentTask instanceof TaxiFreightStartTask) {
					((TaxiFreightStartTask) currentTask).setVehicle(vehicle.getId());
				}
				
				schedule.addTask(currentTask);
				
//				//we need the vehicle to drive back to the depot after the freight tour
//				if(i == freightActivities.size() - 1) {
//					//assumes that first activity in the freightActivities is at the depot (should be the Start activity)
//					Link depotLink = freightActivities.get(0).getLink();
//					path = VrpPaths.calcAndCreatePath(currentTask.getLink(), depotLink, currentTask.getEndTime(), router, travelTime);
//					schedule.addTask(new TaxiEmptyDriveTask(path));
//					appendStayTask(vehicle);
//				}
				previousTask = currentTask;
			}
			
		}
		log.info("vehicle " + vehicle.getId() + " got assigned to a freight schedule");
	}
	
	
	protected void scheduleRequest(Vehicle vehicle, TaxiRequest request) {
		if (request.getStatus() != TaxiRequestStatus.UNPLANNED) {
			throw new IllegalStateException();
		}

		Schedule schedule = vehicle.getSchedule();
		TaxiTask lastTask = (TaxiTask)Schedules.getLastTask(schedule);

		if( ((TaxiTask) lastTask).getTaxiTaskType() != TaxiTaskType.STAY ) {
			throw new IllegalStateException();
		} else {
			
			double departureTime = Math.max(lastTask.getBeginTime(), timer.getTimeOfDay());
			
			VrpPathWithTravelData path = VrpPaths.calcAndCreatePath(Schedules.getLastLinkInSchedule(vehicle), 
					request.getFromLink(), departureTime, router, travelTime);
			
			scheduleDrive(schedule, (TaxiStayTask)lastTask, path);
			
			double pickupEndTime = Math.max(path.getArrivalTime(), request.getEarliestStartTime())
					+ taxiCfg.getPickupDuration();
			schedule.addTask(new TaxiPickupTask(path.getArrivalTime(), pickupEndTime, request));
			
			if(taxiCfg.isDestinationKnown()) {
				appendResultingTasksAfterPickup(vehicle);

			}
		}
		
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
		// TODO: cancel the freight tour and make vehicle return to depot. insert STAY task at the depot
		throw new RuntimeException("currently not implemented");
	}
	
//	public SimpleFreightTourManager getFreightManager() {
//		return this.freightManager;
//	}
	
	public ListBasedFreightTourManager getFreightManager() {
		return this.freightManager;
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

	public void stopVehicle(Vehicle vehicle) {
		delegate.stopVehicle(vehicle);
	}

//	public void updateTimeline(Vehicle vehicle) {
//		delegate.updateTimeline(vehicle);
//	}
	
	public void updateTimeline(Vehicle vehicle) {
		Schedule schedule = vehicle.getSchedule();
		if (schedule.getStatus() != ScheduleStatus.STARTED) {
			return;
		}

		double predictedEndTime = TaskTrackers.predictEndTime(schedule.getCurrentTask(), timer.getTimeOfDay());
		updateTimelineImpl(vehicle, predictedEndTime);
	}

	private void updateTimelineImpl(Vehicle vehicle, double newEndTime) {
		Schedule schedule = vehicle.getSchedule();
		Task currentTask = schedule.getCurrentTask();
		if (currentTask.getEndTime() == newEndTime) {
			return;
		}

		currentTask.setEndTime(newEndTime);

		List<? extends Task> tasks = schedule.getTasks();
		int startIdx = currentTask.getTaskIdx() + 1;
		double newBeginTime = newEndTime;

		for (int i = startIdx; i < tasks.size(); i++) {
			TaxiTask task = (TaxiTask)tasks.get(i);
			
			if(task instanceof TaxiFreightStartTask) {
				System.out.println("about to update the times of the freight start task of vehicle " + vehicle.getId());
			}
			
			double calcEndTime = calcNewEndTime(vehicle, task, newBeginTime);

			if (calcEndTime == Time.UNDEFINED_TIME) {
				schedule.removeTask(task);
				i--;
			} else if (calcEndTime < newBeginTime) {// 0 s is fine (e.g. last 'wait')
				throw new IllegalStateException();
			} else {
				if(task.getStatus() != TaskStatus.PLANNED) {
					System.out.println("!++++++problem");
				}
				task.setBeginTime(newBeginTime);
				task.setEndTime(calcEndTime);
				newBeginTime = calcEndTime;
			}
		}
	}
		

		protected double calcNewEndTime(Vehicle vehicle, TaxiTask task, double newBeginTime) {
			switch (task.getTaxiTaskType()) {
				case STAY: {
					if (Schedules.getLastTask(vehicle.getSchedule()).equals(task)) {// last task
						// even if endTime=beginTime, do not remove this task!!! A taxi schedule should end with WAIT
						return Math.max(newBeginTime, vehicle.getServiceEndTime());
					} else {
						// if this is not the last task then some other task (e.g. DRIVE or PICKUP)
						// must have been added at time submissionTime <= t
						double oldEndTime = task.getEndTime();
						if (oldEndTime <= newBeginTime) {// may happen if the previous task is delayed
							return Time.UNDEFINED_TIME;// remove the task
						} else {
							return oldEndTime;
						}
					}
				}

				case EMPTY_DRIVE:
				case OCCUPIED_DRIVE: {
					// cannot be shortened/lengthen, therefore must be moved forward/backward
					VrpPathWithTravelData path = (VrpPathWithTravelData)((DriveTask)task).getPath();
					// TODO one may consider recalculation of SP!!!!
					return newBeginTime + path.getTravelTime();
				}

				case PICKUP: {
					double t0 = ((TaxiPickupTask)task).getRequest().getEarliestStartTime();
					// the actual pickup starts at max(t, t0)
					return Math.max(newBeginTime, t0) + taxiCfg.getPickupDuration();
				}
				case DROPOFF: {
					// cannot be shortened/lengthen, therefore must be moved forward/backward
					return newBeginTime + taxiCfg.getDropoffDuration();
				}

				default:
					throw new IllegalStateException();
			}
		}


}
