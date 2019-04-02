package privateAV;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import freight.manager.ListBasedFreightTourManager;
import freight.tour.DispatchedPFAVTourData;
import freight.tour.PFAVTourData;
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
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.mobsim.framework.MobsimTimer;
import org.matsim.core.router.FastAStarEuclideanFactory;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;
import privateAV.events.FreightTourCompletedEvent;
import privateAV.events.FreightTourRequestDeniedEvent;
import privateAV.events.FreightTourScheduledEvent;
import privateAV.schedule.PFAVRetoolTask;
import privateAV.schedule.PFAVServiceDriveTask;
import privateAV.schedule.PFAVServiceTask;
import privateAV.vehicle.PFAVehicle;

import java.util.*;

public class PFAVScheduler implements TaxiScheduleInquiry {

	private static final Logger log = Logger.getLogger(PFAVScheduler.class);

    private final TaxiScheduler delegate;
    private final LeastCostPathCalculator router;
    private final TravelTime travelTime;
    private final MobsimTimer timer;
    private final TaxiConfigGroup taxiCfg;
    private final EventsManager eventsManager;

	private Set<DvrpVehicle> vehiclesOnFreightTour = new HashSet();

    private final ListBasedFreightTourManager freightManager;
	private Map<Id<DvrpVehicle>, Double> requestedVehicles = new HashMap<>();

	/**
	 * @param taxiCfg
	 * @param fleet
     * @param network
     * @param timer
	 * @param travelTime
	 * @param travelDisutility
     * @param eventsManager
	 */
	@Inject
	public PFAVScheduler(TaxiConfigGroup taxiCfg, Fleet fleet, @Named(DvrpRoutingNetworkProvider.DVRP_ROUTING) Network network,
                         MobsimTimer timer, @Named(DvrpTravelTimeModule.DVRP_ESTIMATED) TravelTime travelTime,
                         TravelDisutility travelDisutility, EventsManager eventsManager, ListBasedFreightTourManager tourManager) {
        this.freightManager = tourManager;
        this.eventsManager = eventsManager;
		this.taxiCfg = taxiCfg;
		this.router = new FastAStarEuclideanFactory(taxiCfg.getAStarEuclideanOverdoFactor()).createPathCalculator(network,
				travelDisutility, travelTime);
		this.travelTime = travelTime;
		this.timer = timer;
		delegate = new TaxiScheduler(taxiCfg, fleet, network, timer, travelTime, travelDisutility);

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
				//remove the must return time from vehicle specification
				log.warn("removed must return time = " + ((PFAVehicle) vehicle).getOwnerActEndTimes().remove() + " for vehicle " + vehicle.getId());
				break;
			case DROPOFF:
				if (currentTask instanceof TaxiDropoffTask) {
					if (vehicle instanceof PFAVehicle) {
						requestFreightTour(vehicle, false);
					} else {
						//in future, there could be usecases where we have both, DvrpVehicles (supertype) and PFAVehicles, so we would not throw an axception here and just keep going
						throw new RuntimeException("currently, all DvrpVehicles should be of type PFAVehicle");
					}
				} else if (currentTask instanceof PFAVServiceTask
						&& Schedules.getNextTask(schedule) instanceof TaxiEmptyDriveTask
						&& !PFAVUtils.IMMEDIATE_RETURN_AFTER_FREIGHT_TOUR) {
					//vehicle just performed the last service task and can now demand the next freight tour if owner has not submitted a request yet
					if (!requestedVehicles.keySet().contains(vehicle.getId())) {
						requestFreightTour(vehicle, true);
					}
				} else if (currentTask instanceof PFAVRetoolTask && Schedules.getNextTask(schedule) instanceof TaxiStayTask) {
					//we are at the end of a freight tour (otherwise next task would be instanceof PFAVServiceDriveTask
					if (!this.vehiclesOnFreightTour.contains(vehicle)) throw new IllegalStateException("freight tour of vehicle " +
							vehicle.getId() + "ends and scheduler did not even mark it as being on freight tour");

					this.eventsManager.processEvent(new FreightTourCompletedEvent(vehicle, timer.getTimeOfDay()));
					this.vehiclesOnFreightTour.remove(vehicle);
					log.warn("vehicle " + vehicle.getId() + " returns to it's owner at time=" + timer.getTimeOfDay());
					scheduleDriveToOwner(vehicle, schedule, (PFAVRetoolTask) currentTask);
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

	private void scheduleDriveToOwner(DvrpVehicle vehicle, Schedule schedule, PFAVRetoolTask currentTask) {
		TaxiTask lastTask = (TaxiTask) Schedules.getLastTask(schedule);
		double departureTime = lastTask.getBeginTime();
		VrpPathWithTravelData path = VrpPaths.calcAndCreatePath(currentTask.getLink(),
				PFAVUtils.getLastPassengerDropOff(schedule).getLink(),
				departureTime, router, travelTime);

		scheduleDrive(schedule, (TaxiStayTask) lastTask, path);
		appendStayTask(vehicle);
	}

	private void requestFreightTour(DvrpVehicle vehicle, boolean isComingFromAnotherFreightTour) {
        log.info("Vehicle " + vehicle.getId() + " requests a freight tour at " + timer.getTimeOfDay() + " on link " + ((StayTaskImpl) vehicle.getSchedule().getCurrentTask()).getLink().getId());
		PFAVTourData tourData = freightManager.getBestPFAVTourForVehicle((PFAVehicle) vehicle, router);
		if (tourData != null) {
            log.info("vehicle " + vehicle.getId() + " requested a freight tour and received one by the manager");
			if (isComingFromAnotherFreightTour) eventsManager.processEvent(new FreightTourCompletedEvent(vehicle, timer.getTimeOfDay()));
			scheduleFreightTour(vehicle, tourData);
		} else {
			Link requestLink = ((StayTaskImpl) vehicle.getSchedule().getCurrentTask()).getLink();
			eventsManager.processEvent(new FreightTourRequestDeniedEvent((PFAVehicle) vehicle, requestLink.getId(), timer.getTimeOfDay()));
			log.info("request is denied");
		}
	}

	private void scheduleFreightTour(DvrpVehicle vehicle, PFAVTourData tourData) {
		//schedule just got updated.. current task should be a dropOff, nextTast should be a STAY task
		Schedule schedule = vehicle.getSchedule();
		Link requestLink = ((StayTaskImpl) vehicle.getSchedule().getCurrentTask()).getLink();
		List<StayTask> freightActivities = tourData.getTourTasks();

		cleanScheduleBeforeInsertingFreightTour(vehicle, schedule);

		List<Task> freightTour = new ArrayList<>(); //for analysis
		double totalDistance = 0.;  //for analysis
		double emptyMeters = 0.;    //for analysis
		double distanceToDepot = 0; //for analysis

		StayTask previousTask = (StayTaskImpl) schedule.getCurrentTask();
		double tourDuration = previousTask.getEndTime();  //for analysis

		for (int i = 0; i < freightActivities.size(); i++) {
			StayTask currentTask = freightActivities.get(i);

			VrpPathWithTravelData path = VrpPaths.calcAndCreatePath(previousTask.getLink(), currentTask.getLink(), previousTask.getEndTime(), router, travelTime);

			DriveTask driveTask = null;
			//first activity could be PFAVRetoolTask or TaxiStayTask
			if (i == 0 || i == freightActivities.size() - 1) {
				driveTask = new TaxiEmptyDriveTask(path);
			} else {
				//if we inserted a stay task at the depot before the start of freight tour, we don't want to schedule a drive task, as vehicle is already at the depot
				if (!(currentTask instanceof PFAVRetoolTask)) driveTask = new PFAVServiceDriveTask(path);
			}
			if (driveTask != null) {
				schedule.addTask(driveTask);

				freightTour.add(driveTask); //analysis
                //do not compute the first link since it's not really travelled but just where the vehicle is inserted
				for (int z = 1; z < driveTask.getPath().getLinkCount(); z++) {
					double distance = driveTask.getPath().getLink(z).getLength();
					totalDistance += distance; //analysis
					if (driveTask instanceof TaxiEmptyDriveTask) emptyMeters += distance;
					if (i == 0) distanceToDepot += distance;
				}
			}

			double duration = currentTask.getEndTime() - currentTask.getBeginTime();
			currentTask.setBeginTime(path.getArrivalTime());
			currentTask.setEndTime(path.getArrivalTime() + duration);

			if (currentTask instanceof PFAVRetoolTask) {
				((PFAVRetoolTask) currentTask).setVehicle(vehicle.getId());
			}
			schedule.addTask(currentTask);
			freightTour.add(currentTask);

			previousTask = currentTask;
		}
		appendStayTask(vehicle);
		//mark this vehicle's state as "on freight tour"
		this.vehiclesOnFreightTour.add(vehicle);
		log.info("vehicle " + vehicle.getId() + " got assigned to a freight schedule");
		tourDuration = previousTask.getEndTime() - tourDuration;

		//the following is only for analysis
		tourData.setPlannedTourDuration(tourDuration);
//		tourData.setPlannedTourLength(totalDistance);

		DispatchedPFAVTourData dispatchData = DispatchedPFAVTourData.newBuilder()
				.vehicle((PFAVehicle) vehicle)
				.dispatchTime(timer.getTimeOfDay())
				.requestLink(requestLink.getId())
				.tourData(tourData)
				.distanceToDepot(distanceToDepot)
				.plannedEmptyMeters(emptyMeters)
                .plannedTourLength(totalDistance)
				.build();

		eventsManager.processEvent(new FreightTourScheduledEvent(dispatchData));
	}

	private void cleanScheduleBeforeInsertingFreightTour(DvrpVehicle vehicle, Schedule schedule) {
//        StringBuilder scheduleStr = new StringBuilder();
//        for (Task t : schedule.getTasks()) scheduleStr.append(t.toString()).append("\n");
//        log.warn("schedule " + schedule.toString() + " before clean up: \n" + scheduleStr.toString());

		switch (((TaxiTask) Schedules.getNextTask(schedule)).getTaxiTaskType()) {
			case STAY:
				//vehicle requested freight tour after passenger dropoff
				//remove the stay task at the owner's activity location
				schedule.removeLastTask();
				break;
			case EMPTY_DRIVE:
				/*TODO: calling same method 3 times feels dirty - extract method !?
				 *we cannot use delegate.removeAwaitingRequests cause this method inserts another stay task
				 *
				 *vehicle requested freight tour after having performed another one
				 *we have to remove the following awaiting tasks in the schedule
				 */
				schedule.removeLastTask();    //empty drive to depot
				schedule.removeLastTask();    //retool task at depot
				schedule.removeLastTask();    //stay at depot
				break;
			default:
				throw new IllegalStateException(
						"if a freight tour shall be inserted - the next vehicle task has to be STAY or EMPTY_DRIVE. That is not the case for vehicle " + vehicle.getId() + " at time " + timer.getTimeOfDay());
		}

//        scheduleStr = new StringBuilder();
//		for (Task t : schedule.getTasks()) {
//            scheduleStr.append(t.toString()).append("\n");
//		}
//		log.warn("schedule " + schedule.toString() + " after clean up: \n" + scheduleStr);
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

    public void stopCruisingVehicle(DvrpVehicle vehicle) {
        if (!taxiCfg.isVehicleDiversion()) {
            throw new RuntimeException("Diversion must be on");
        }

        Schedule schedule = vehicle.getSchedule();
        TaxiEmptyDriveTask driveTask = (TaxiEmptyDriveTask) Schedules.getNextToLastTask(schedule);
        schedule.removeLastTask();
        OnlineDriveTaskTracker tracker = (OnlineDriveTaskTracker) driveTask.getTaskTracker();
        LinkTimePair stopPoint = tracker.getDiversionPoint();
        tracker.divertPath(
                new VrpPathWithTravelDataImpl(stopPoint.time, 0, new Link[]{stopPoint.link}, new double[]{0}));
        appendStayTask(vehicle);

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
