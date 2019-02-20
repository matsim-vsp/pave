package privateAV;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import freight.manager.FreightTourManager;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelDataImpl;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.dvrp.schedule.Schedule.ScheduleStatus;
import org.matsim.contrib.dvrp.schedule.Schedules;
import org.matsim.contrib.dvrp.schedule.Task;
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
import privateAV.vehicle.PFAVehicle;

/**
 * @author tschlenther
 */
public class PFAVScheduler implements TaxiScheduleInquiry {

	private static final Logger log = Logger.getLogger(PFAVScheduler.class);
	
	private TaxiScheduler delegate;
	private LeastCostPathCalculator router;
	private TravelTime travelTime;
	private MobsimTimer timer;
	private TaxiConfigGroup taxiCfg;

	private FreightTourManager freightManager;
	
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
						 TravelDisutility travelDisutility, FreightTourManager tourManager) {

		this.taxiCfg = taxiCfg;
		this.router = new FastAStarEuclideanFactory(taxiCfg.getAStarEuclideanOverdoFactor()).createPathCalculator(network,
				travelDisutility, travelTime);
		this.travelTime = travelTime;
		this.timer = timer;
		delegate = new TaxiScheduler(taxiCfg, fleet, network, timer, travelTime, travelDisutility);

		this.freightManager = tourManager;
		freightManager.routeCarrierPlans(router);

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
					if(currentTask instanceof TaxiDropoffTask){
						if(vehicle instanceof PFAVehicle){
//						log.info("Vehicle " + vehicle.getId() + " requests a freight tour");
							Schedule freightTour = freightManager.getBestPFAVTourForVehicle((PFAVehicle) vehicle, router);
							if(freightTour != null) {
								scheduleFreightTour(vehicle, freightTour);
								//TODO: should we throw some kind of event here, to make analysis easier/possible (on how many freightTour requests there were etc.)
							} else {
								//TODO: should we throw some kind of event here, to make analysis easier/possible (on how many freightTour requests there were etc.)

								//log.info("+++++ vehicle " + vehicle.getId() + " requested a freight tour but the manager returned NULL ++++++");
							}
						} else {
							//in future, there could be usecases where we have both, DvrpVehicles (supertype) and PFAVehicles, so we would not throw an axception here and just keep going
							throw  new RuntimeException("currently, all DvrpVehicles should be of type PFAVehicle");
						}
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

	/**
	 * Here i had a bit of a design issue. First, i had the manager return only a list of the freight service task and in this method i would construct the resulting schedule
	 * sequence (with the drive tasks). But as i need to compute the paths to and from the depot in the manager (in order to evaluate if there is enough time to perform the freight tour)
	 * i put the schedule construction logic into the manager and made it return a schedule. this avoids computing the mentioned paths twice.
	 * <p>
	 * So, if i compute a schedule in the manager, why don't i manipulate the vehicle's schedule directly in there?
	 * I feel like the final 'puttogether' of the freight tour and the vehicle's existing schedule should happen here in the scheduler...
	 * tschlenther, feb' 2019
	 *
	 * @param vehicle
	 * @param freightTasks
	 */
	private void scheduleFreightTour(DvrpVehicle vehicle, Schedule freightTasks) {
		
		//schedule just got updated.. current task should be a dropOff, nextTast should be a STAY task
		Schedule schedule = vehicle.getSchedule();
		
		if( ((TaxiTask) Schedules.getNextTask(schedule)).getTaxiTaskType() != TaxiTaskType.STAY) {
			throw new IllegalStateException(
					"if a freight tour shall be inserted - the next vehicle task has to be STAY. That is not the case for vehicle " + vehicle.getId() + " at time " + timer.getTimeOfDay());
		} else {
			//remove the stay task
			schedule.removeLastTask();

			for (Task taskToInsert : freightTasks.getTasks()) {
				schedule.addTask(taskToInsert);
			}
			appendStayTask(vehicle);
		}
		log.info("vehicle " + vehicle.getId() + " got assigned to a freight schedule");
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
