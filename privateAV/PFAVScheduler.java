package privateAV;

import java.util.List;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelDataImpl;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.dvrp.schedule.Schedules;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.dvrp.schedule.StayTaskImpl;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.dvrp.schedule.Schedule.ScheduleStatus;
import org.matsim.contrib.dvrp.tracker.OnlineDriveTaskTracker;
import org.matsim.contrib.dvrp.trafficmonitoring.DvrpTravelTimeModule;
import org.matsim.contrib.dvrp.util.LinkTimePair;
import org.matsim.contrib.taxi.passenger.TaxiRequest;
import org.matsim.contrib.taxi.passenger.TaxiRequest.TaxiRequestStatus;
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

import freight.manager.ListBasedFreightTourManager;
import freight.manager.ListBasedFreightTourManagerImpl;
import privateAV.schedule.PFAVServiceDriveTask;
import privateAV.schedule.PFAVServiceTask;
import privateAV.schedule.PFAVStartTask;

public class PFAVScheduler implements TaxiScheduleInquiry {

	private static final Logger log = Logger.getLogger(PFAVScheduler.class);
	
	private TaxiScheduler delegate;
	private Link DEPOT_LINK;
	private LeastCostPathCalculator router;
	private TravelTime travelTime;
	private MobsimTimer timer;
	private TaxiConfigGroup taxiCfg;

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
	public PFAVScheduler(TaxiConfigGroup taxiCfg, Fleet fleet, @Named(DvrpRoutingNetworkProvider.DVRP_ROUTING) Network network,
			MobsimTimer timer, @Named(DvrpTravelTimeModule.DVRP_ESTIMATED) TravelTime travelTime,
			TravelDisutility travelDisutility){
		
		DEPOT_LINK = network.getLinks().get(Id.createLinkId("560"));
		this.taxiCfg = taxiCfg;
		this.router = new FastAStarEuclideanFactory(taxiCfg.getAStarEuclideanOverdoFactor()).createPathCalculator(network,
				travelDisutility, travelTime);
		this.travelTime = travelTime;
		this.timer = timer;
		
		this.freightManager = new ListBasedFreightTourManagerImpl(network, PFAVUtils.DEFAULT_CARRIERS_FILE, PFAVUtils.DEFAULT_VEHTYPES_FILE);
		
		delegate = new TaxiScheduler(taxiCfg, fleet, network, timer, travelTime, travelDisutility);
	}
	
	public void stopCruisingVehicle(DvrpVehicle vehicle) {
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
	

	public void updateBeforeNextTask(DvrpVehicle vehicle) {
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
			
				if(currentTask instanceof PFAVServiceTask) break;
				
				log.info("Vehicle " + vehicle.getId() + " requests a freight tour");
				List<StayTask> freightTour = freightManager.getBestPFAVTourForVehicle(vehicle); 
				if(freightTour != null) {
					scheduleFreightTour(vehicle, freightTour);
				} else {
//					log.info("+++++ vehicle " + vehicle.getId() + " requested a freight tour but the manager returned NULL ++++++");
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

	private void scheduleFreightTour(DvrpVehicle vehicle, List<StayTask> freightActivities) {
		
		//schedule just got updated.. current task should be a dropOff, nextTast should be a STAY task
		Schedule schedule = vehicle.getSchedule();
		
		if( ((TaxiTask) Schedules.getNextTask(schedule)).getTaxiTaskType() != TaxiTaskType.STAY) {
			throw new IllegalStateException(
					"if a freight tour shall be inserted - the next vehicle task has to be STAY. That is not the case for vehicle " + vehicle.getId() + " at time " + timer.getTimeOfDay());
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
					schedule.addTask(new PFAVServiceDriveTask(path));
					
				}
				
				double duration = currentTask.getEndTime() - currentTask.getBeginTime();
				currentTask.setBeginTime(path.getArrivalTime());
				currentTask.setEndTime(path.getArrivalTime() + duration);
				
				if(currentTask instanceof PFAVStartTask) {
					((PFAVStartTask) currentTask).setVehicle(vehicle.getId());
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
	
	
	public void scheduleRequest(DvrpVehicle vehicle, TaxiRequest request) {
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
	

	private void appendResultingTasksAfterPickup(DvrpVehicle vehicle) {
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
	
	private void appendStayTask(DvrpVehicle vehicle) {
		Schedule schedule = vehicle.getSchedule();
		double tBegin = schedule.getEndTime();
		double tEnd = Math.max(tBegin, vehicle.getServiceEndTime());// even 0-second WAIT
		Link link = Schedules.getLastLinkInSchedule(vehicle);
		schedule.addTask(new TaxiStayTask(tBegin, tEnd, link));		
	}
	

	public void cancelFreightTour(DvrpVehicle veh) {
		// TODO: cancel the freight tour and make vehicle return to depot. insert STAY task at the depot
		//		- first check if freight tour is started in the first place! otherwise throw exception!
		throw new RuntimeException("currently not implemented");
	}
	
//	public SimpleFreightTourManager getFreightManager() {
//		return this.freightManager;
//	}
	
	public ListBasedFreightTourManager getFreightManager() {
		return this.freightManager;
	}
	
	public void calculateFreightTours() {
		((ListBasedFreightTourManagerImpl) this.freightManager).runTourPlanning(travelTime);
	}

	
	//---------------------------------------------------- DELEGATE METHODS --------------
	
//	@TODO: sort out methods / clean the code
	
	
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
	
	
	
	public boolean isIdle(DvrpVehicle vehicle) {
		return delegate.isIdle(vehicle);
	}

	public LinkTimePair getImmediateDiversionOrEarliestIdleness(DvrpVehicle veh) {
		return delegate.getImmediateDiversionOrEarliestIdleness(veh);
	}

	public LinkTimePair getEarliestIdleness(DvrpVehicle veh) {
		return delegate.getEarliestIdleness(veh);
	}

	public LinkTimePair getImmediateDiversion(DvrpVehicle veh) {
		return delegate.getImmediateDiversion(veh);
	}

	public void stopVehicle(DvrpVehicle vehicle) {
		delegate.stopVehicle(vehicle);
	}

	public void updateTimeline(DvrpVehicle vehicle) {
		delegate.updateTimeline(vehicle);
	}
	
//	public void updateTimeline(Vehicle vehicle) {
//		Schedule schedule = vehicle.getSchedule();
//		if (schedule.getStatus() != ScheduleStatus.STARTED) {
//			return;
//		}
//
//		double predictedEndTime = TaskTrackers.predictEndTime(schedule.getCurrentTask(), timer.getTimeOfDay());
//		updateTimelineImpl(vehicle, predictedEndTime);
//	}
//
//	private void updateTimelineImpl(Vehicle vehicle, double newEndTime) {
//		Schedule schedule = vehicle.getSchedule();
//		Task currentTask = schedule.getCurrentTask();
//		if (currentTask.getEndTime() == newEndTime) {
//			return;
//		}
//
//		currentTask.setEndTime(newEndTime);
//
//		List<? extends Task> tasks = schedule.getTasks();
//		int startIdx = currentTask.getTaskIdx() + 1;
//		double newBeginTime = newEndTime;
//
//		for (int i = startIdx; i < tasks.size(); i++) {
//			TaxiTask task = (TaxiTask)tasks.get(i);
//			
//			if(task instanceof PFAVStartTask) {
//				System.out.println("about to update the times of the freight start task of vehicle " + vehicle.getId());
//			}
//			
//			double calcEndTime = calcNewEndTime(vehicle, task, newBeginTime);
//
//			if (calcEndTime == Time.UNDEFINED_TIME) {
//				schedule.removeTask(task);
//				i--;
//			} else if (calcEndTime < newBeginTime) {// 0 s is fine (e.g. last 'wait')
//				throw new IllegalStateException();
//			} else {
//				if(task.getStatus() != TaskStatus.PLANNED) {
//					System.out.println("!++++++problem");
//				}
//				task.setBeginTime(newBeginTime);
//				task.setEndTime(calcEndTime);
//				newBeginTime = calcEndTime;
//			}
//		}
//	}

//	protected double calcNewEndTime(Vehicle vehicle, TaxiTask task, double newBeginTime) {
//			switch (task.getTaxiTaskType()) {
//				case STAY: {
//					if (Schedules.getLastTask(vehicle.getSchedule()).equals(task)) {// last task
//						// even if endTime=beginTime, do not remove this task!!! A taxi schedule should end with WAIT
//						return Math.max(newBeginTime, vehicle.getServiceEndTime());
//					} else {
//						// if this is not the last task then some other task (e.g. DRIVE or PICKUP)
//						// must have been added at time submissionTime <= t
//						double oldEndTime = task.getEndTime();
//						if (oldEndTime <= newBeginTime) {// may happen if the previous task is delayed
//							return Time.UNDEFINED_TIME;// remove the task
//						} else {
//							return oldEndTime;
//						}
//					}
//				}
//
//				case EMPTY_DRIVE:
//				case OCCUPIED_DRIVE: {
//					// cannot be shortened/lengthen, therefore must be moved forward/backward
//					VrpPathWithTravelData path = (VrpPathWithTravelData)((DriveTask)task).getPath();
//					// TODO one may consider recalculation of SP!!!!
//					return newBeginTime + path.getTravelTime();
//				}
//
//				case PICKUP: {
//					double t0 = ((TaxiPickupTask)task).getRequest().getEarliestStartTime();
//					// the actual pickup starts at max(t, t0)
//					return Math.max(newBeginTime, t0) + taxiCfg.getPickupDuration();
//				}
//				case DROPOFF: {
//					// cannot be shortened/lengthen, therefore must be moved forward/backward
//					return newBeginTime + taxiCfg.getDropoffDuration();
//				}
//
//				default:
//					throw new IllegalStateException();
//			}
//		}



}
