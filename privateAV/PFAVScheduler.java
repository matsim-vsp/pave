package privateAV;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import freight.manager.ListBasedFreightTourManager;
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
import org.matsim.contrib.dvrp.schedule.*;
import org.matsim.contrib.dvrp.schedule.Schedule.ScheduleStatus;
import org.matsim.contrib.dvrp.tracker.OnlineDriveTaskTracker;
import org.matsim.contrib.dvrp.trafficmonitoring.DvrpTravelTimeModule;
import org.matsim.contrib.dvrp.util.LinkTimePair;
import org.matsim.contrib.taxi.passenger.TaxiRequest;
import org.matsim.contrib.taxi.passenger.TaxiRequest.TaxiRequestStatus;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.contrib.taxi.schedule.*;
import org.matsim.contrib.taxi.schedule.TaxiTask.TaxiTaskType;
import org.matsim.contrib.taxi.scheduler.TaxiScheduleInquiry;
import org.matsim.contrib.taxi.scheduler.TaxiScheduler;
import org.matsim.core.mobsim.framework.MobsimTimer;
import org.matsim.core.router.FastAStarEuclideanFactory;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;
import privateAV.schedule.PFAVRetoolTask;
import privateAV.schedule.PFAVServiceDriveTask;
import privateAV.schedule.PFAVServiceTask;
import privateAV.vehicle.PFAVehicle;

import java.util.*;

public class PFAVScheduler implements TaxiScheduleInquiry {

	private static final Logger log = Logger.getLogger(PFAVScheduler.class);
	
	private TaxiScheduler delegate;
	private LeastCostPathCalculator router;
	private TravelTime travelTime;
	private MobsimTimer timer;
	private TaxiConfigGroup taxiCfg;

	private Set<DvrpVehicle> vehiclesOnFreightTour = new HashSet();

    ListBasedFreightTourManager freightManager;
	private Map<Id<DvrpVehicle>, Double> requestedVehicles = new HashMap<>();

	/**
	 * @param taxiCfg
	 * @param network
	 * @param fleet
	 * @param timer
	 * @param travelTime
	 * @param travelDisutility
	 */
	@Inject
	public PFAVScheduler(TaxiConfigGroup taxiCfg, Fleet fleet, @Named(DvrpRoutingNetworkProvider.DVRP_ROUTING) Network network,
                         MobsimTimer timer, @Named(DvrpTravelTimeModule.DVRP_ESTIMATED) TravelTime travelTime,
                         TravelDisutility travelDisutility, ListBasedFreightTourManager tourManager) {

        this.freightManager = tourManager;
		this.taxiCfg = taxiCfg;
		this.router = new FastAStarEuclideanFactory(taxiCfg.getAStarEuclideanOverdoFactor()).createPathCalculator(network,
				travelDisutility, travelTime);
		this.travelTime = travelTime;
		this.timer = timer;
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
				if (!requestedVehicles.keySet().contains(vehicle.getId())) {
					throw new RuntimeException("vehicle performed a passenger pick up but was not on the requested vehicles list..");
				}
				requestedVehicles.remove(vehicle.getId());
				break;
			case DROPOFF:
				if (currentTask instanceof TaxiDropoffTask) {
					if (vehicle instanceof PFAVehicle) {
//						log.info("Vehicle " + vehicle.getId() + " requests a freight tour");
						requestFreightTour(vehicle);
					} else {
						//in future, there could be usecases where we have both, DvrpVehicles (supertype) and PFAVehicles, so we would not throw an axception here and just keep going
						throw new RuntimeException("currently, all DvrpVehicles should be of type PFAVehicle");
					}
				} else if (currentTask instanceof PFAVServiceTask
						&& Schedules.getNextTask(schedule) instanceof TaxiEmptyDriveTask
						&& !PFAVUtils.IMMEDIATE_RETURN_AFTER_FREIGHT_TOUR) {
					//vehicle just performed the last service task and can now demand the next freight tour
					requestFreightTour(vehicle);
				} else if (currentTask instanceof PFAVRetoolTask && Schedules.getNextTask(schedule) instanceof TaxiEmptyDriveTask) {
					//we are at the end of a freight tour (otherwise next task would be instanceof PFAVServiceDriveTask
					if (!this.vehiclesOnFreightTour.contains(vehicle)) throw new IllegalStateException("freight tour of vehicle " +
							vehicle.getId() + "ends and scheduler did not even mark it as being on freight tour");
					this.vehiclesOnFreightTour.remove(vehicle);
					//remove the must return time from vehicle specification (as vehicle will now return)
					log.warn("vehicle " + vehicle.getId() + " returns to it's owner at time=" + timer.getTimeOfDay() + ". actEndTimes size before removal= "
							+ ((PFAVehicle) vehicle).getOwnerActEndTimes().size());
					log.warn("removed time = " + ((PFAVehicle) vehicle).getOwnerActEndTimes().remove());
					log.warn("actEndTimes size after removal= "
							+ ((PFAVehicle) vehicle).getOwnerActEndTimes().size());
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

	private void requestFreightTour(DvrpVehicle vehicle) {
		List<StayTask> freightTour = freightManager.getBestPFAVTourForVehicle((PFAVehicle) vehicle, router);
		if (freightTour != null) {

			log.info("vehicle " + vehicle.getId() + " requested a freight tour at " + timer.getTimeOfDay() + " and received one by the manager");
			if (!requestedVehicles.keySet().contains(vehicle.getId())) {
				scheduleFreightTour(vehicle, freightTour);
			} else {
				//TODO: move the check (whether the owner already requested the PFAV) further up to updateBeforeNextTask(). it is here only for test purposes...
				//especially because the freight tour will now not be performed but the manager has already deleted it from it's set
				log.warn("vehicle " + vehicle.getId() +
						" thinks that it can manage a freight tour in time but the owner has already requested the vehicle with submission time = " + requestedVehicles.get(vehicle.getId()));
				freightManager.getPFAVTours().add(freightTour);
			}

			//TODO: should we throw some kind of event here? to make analysis easier/possible (on how many freightTour requests there were etc.)
		} else {
			//TODO: should we throw some kind of event here? to make analysis easier/possible (on how many freightTour requests there were etc.)
//					log.info("+++++ vehicle " + vehicle.getId() + " requested a freight tour but the manager returned NULL ++++++");
		}
	}

	private void scheduleFreightTour(DvrpVehicle vehicle, List<StayTask> freightActivities) {
		//schedule just got updated.. current task should be a dropOff, nextTast should be a STAY task
		Schedule schedule = vehicle.getSchedule();

		cleanScheduleBeforeInsertingFreightTour(vehicle, schedule);

		StayTask previousTask = (StayTaskImpl) schedule.getCurrentTask();
		for (int i = 0; i < freightActivities.size(); i++) {
			StayTask currentTask = freightActivities.get(i);

			VrpPathWithTravelData path = VrpPaths.calcAndCreatePath(previousTask.getLink(), currentTask.getLink(), previousTask.getEndTime(), router, travelTime);

			//alternatively one could say: if(currentTask instanceof PFAVRetoolTask) cause these should be only at the ends of the tour
			if (i == 0 || i == freightActivities.size() - 1) {
				schedule.addTask(new TaxiEmptyDriveTask(path));
			} else {
				schedule.addTask(new PFAVServiceDriveTask(path));
			}

			double duration = currentTask.getEndTime() - currentTask.getBeginTime();
			currentTask.setBeginTime(path.getArrivalTime());
			currentTask.setEndTime(path.getArrivalTime() + duration);

			if (currentTask instanceof PFAVRetoolTask) {
				((PFAVRetoolTask) currentTask).setVehicle(vehicle.getId());
			}
			schedule.addTask(currentTask);

			/*always insert the drive task back to the owner although another freight tour might be performed before.
			/*we need to have the last link in the schedule point to the owner's activity to be able to compute whether there is enough time
			/*to perform the freight tour in the manager
			 */
			if (i == freightActivities.size() - 1) {

				//assumes that last activity in the freightActivities is at the depot (should be the End activity)
				Link depotLink = freightActivities.get(i).getLink();
                Link ownerLink = getLastPassengerDropOff(schedule).getLink();
				path = VrpPaths.calcAndCreatePath(depotLink, ownerLink, currentTask.getEndTime(), router, travelTime);
				schedule.addTask(new TaxiEmptyDriveTask(path));
			}
			previousTask = currentTask;
		}
		appendStayTask(vehicle);
		//mark this vehicle's state as "on freight tour"
		this.vehiclesOnFreightTour.add(vehicle);
		log.info("vehicle " + vehicle.getId() + " got assigned to a freight schedule");
	}

    private TaxiDropoffTask getLastPassengerDropOff(Schedule schedule) {
        for (int i = schedule.getTasks().size() - 1; i >= 0; i--) {
            Task task = schedule.getTasks().get(i);
            if (task instanceof TaxiDropoffTask) return (TaxiDropoffTask) task;
        }
        return null;
    }

	private void cleanScheduleBeforeInsertingFreightTour(DvrpVehicle vehicle, Schedule schedule) {
		String scheduleStr = "";
		for (Task t : schedule.getTasks()) {
			scheduleStr += t.toString() + "\n";
		}
		log.warn("schedule " + schedule.toString() + " before clean up: \n" + scheduleStr);

		switch (((TaxiTask) Schedules.getNextTask(schedule)).getTaxiTaskType()) {
			case STAY:
				//vehicle requested freight tour after passenger dropoff
				//remove the stay task at the owner's activity location
				schedule.removeLastTask();
				break;
			case EMPTY_DRIVE:
				/*TODO: calling same method 4 times feels dirty - extract method !?
				 *we cannot use delegate.removeAwaitingRequests cause this method inserts another stay task
				 *
				 *vehicle requested freight tour after having performed another one
				 *we have to remove the following awaiting tasks in the schedule
				 */
				schedule.removeLastTask();    //empty drive to depot
				schedule.removeLastTask();    //retool task at depot
				schedule.removeLastTask(); // empty drive to owner
				schedule.removeLastTask(); // stay at owner's activity location
				break;
			default:
				throw new IllegalStateException(
						"if a freight tour shall be inserted - the next vehicle task has to be STAY or EMPTY_DRIVE. That is not the case for vehicle " + vehicle.getId() + " at time " + timer.getTimeOfDay());
		}

		scheduleStr = "";
		for (Task t : schedule.getTasks()) {
			scheduleStr += t.toString() + "\n";
		}
		log.warn("schedule " + schedule.toString() + " after clean up: \n" + scheduleStr);
	}

	public void scheduleRequest(DvrpVehicle vehicle, TaxiRequest request) {
		if (request.getStatus() != TaxiRequestStatus.UNPLANNED) {
			throw new IllegalStateException();
		}

		Schedule schedule = vehicle.getSchedule();
		TaxiTask lastTask = (TaxiTask)Schedules.getLastTask(schedule);

		if( lastTask.getTaxiTaskType() != TaxiTaskType.STAY ) {
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

		requestedVehicles.put(vehicle.getId(), request.getSubmissionTime());
	}

	private void appendResultingTasksAfterPickup(DvrpVehicle vehicle) {
		appendOccupiedDriveAndDropoff(vehicle.getSchedule());
		appendStayTask(vehicle);
	}
	
	private void appendOccupiedDriveAndDropoff(Schedule schedule) {
		TaxiPickupTask pickupStayTask = (TaxiPickupTask)Schedules.getLastTask(schedule);

		// add DELIVERY after SERVE
		TaxiRequest req = (pickupStayTask).getRequest();
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

	public boolean isCurrentlyOnOrWillPerformPerformFreightTour(DvrpVehicle veh) {
		return this.vehiclesOnFreightTour.contains(veh);
	}
	
	public void cancelFreightTour(DvrpVehicle veh) {
		// TODO: cancel the freight tour and make vehicle return to depot. insert STAY task at the depot
		//		- first check if freight tour is started in the first place! otherwise throw exception!
		throw new RuntimeException("currently not implemented");
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
	private void divertDrive(TaxiEmptyDriveTask lastTask, VrpPathWithTravelData vrpPath) {
		if (!taxiCfg.isVehicleDiversion()) {
			throw new IllegalStateException();
		}

		((OnlineDriveTaskTracker)lastTask.getTaskTracker()).divertPath(vrpPath);
	}
	
	private void scheduleDrive(Schedule schedule, TaxiStayTask lastTask, VrpPathWithTravelData vrpPath) {
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

}
