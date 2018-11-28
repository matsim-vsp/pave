/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2017 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

/**
 * 
 */
package privateAV.infrastructure;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.data.Fleet;
import org.matsim.contrib.dvrp.data.Vehicle;
import org.matsim.contrib.dvrp.path.VrpPath;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelDataImpl;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.schedule.DriveTask;
import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.dvrp.schedule.Schedules;
import org.matsim.contrib.dvrp.tracker.OnlineDriveTaskTracker;
import org.matsim.contrib.dvrp.trafficmonitoring.DvrpTravelTimeModule;
import org.matsim.contrib.dvrp.util.LinkTimePair;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.taxi.optimizer.DefaultTaxiOptimizerProvider;
import org.matsim.contrib.taxi.run.Taxi;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.contrib.taxi.schedule.TaxiEmptyDriveTask;
import org.matsim.contrib.taxi.schedule.TaxiPickupTask;
import org.matsim.contrib.taxi.schedule.TaxiTask;
import org.matsim.contrib.taxi.scheduler.TaxiScheduler;
import org.matsim.core.mobsim.framework.MobsimTimer;
import org.matsim.core.router.FastAStarEuclideanFactory;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.utils.misc.Time;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import privateAV.infrastructure.Task.TaxiFreightServiceTask;
import privateAV.infrastructure.Task.TaxiFreightStartTask;

/**
 * @author jbischoff
 *
 */
public class PrivateAV4FreightScheduler extends TaxiScheduler {

	private Link DEPOT_LINK;
	private LeastCostPathCalculator router;
	private TravelTime travelTime;
	

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
	public PrivateAV4FreightScheduler(TaxiConfigGroup taxiCfg, @Taxi Fleet fleet, @Named(DvrpRoutingNetworkProvider.DVRP_ROUTING) Network network,
			MobsimTimer timer, @Named(DvrpTravelTimeModule.DVRP_ESTIMATED) TravelTime travelTime,
			@Named(DefaultTaxiOptimizerProvider.TAXI_OPTIMIZER) TravelDisutility travelDisutility) {
		super(taxiCfg, fleet, network, timer, travelTime, travelDisutility);
		DEPOT_LINK = network.getLinks().get(Id.createLinkId("560"));
		
		//the router and traveltime variables are initialized the same way in the super class; but the fields are set to private over there. Since i'm not working on a snapshot, i can't change
		//this. So i choose the sloppy version to have another instance of the router and the travel time field variable
		router = new FastAStarEuclideanFactory(taxiCfg.getAStarEuclideanOverdoFactor()).createPathCalculator(network,
				travelDisutility, travelTime);
		this.travelTime = travelTime;
	}

	public void moveIdleVehicle(Vehicle vehicle, VrpPathWithTravelData vrpPath) {
		Schedule schedule = vehicle.getSchedule();
		divertOrAppendDrive(schedule, vrpPath);
		appendStayTask(vehicle);
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
	
	@Override
	protected void appendTasksAfterDropoff(Vehicle vehicle) {
//		addDriveToDepotTask(vehicle);
//		appendStayTask(vehicle);
		appendEmptyDriveTask();
	}

	private void appendEmptyDriveTask() {
		
		TaxiEmptyDriveTask task = new TaxiEmptyDriveTask(path)
	}

	private void addDriveToDepotTask(Vehicle vehicle) {
//		LinkTimePair beginOfIdleness = super.getEarliestIdleness(vehicle);
		Link lastLinkInSchedule = Schedules.getLastLinkInSchedule(vehicle);
		
		VrpPathWithTravelData path = VrpPaths.calcAndCreatePath(Schedules.getLastLinkInSchedule(vehicle), 
				DEPOT_LINK, Schedules.getLastTask(vehicle.getSchedule()).getEndTime(), this.router, this.travelTime);
		
		 vehicle.getSchedule().addTask(new TaxiEmptyDriveTask(path));
	}
	
	
	/*
	 * 
	 * we need to overide this method because the calculation of the FreightStartTask (which has the TaxiTaskType.Stay) annot depend on a request, since it has none
	 * 
	 * @see org.matsim.contrib.taxi.scheduler.TaxiScheduler#calcNewEndTime(org.matsim.contrib.dvrp.data.Vehicle, org.matsim.contrib.taxi.schedule.TaxiTask, double)
	 */
	@Override
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
				double t0 = newBeginTime;
				if(task instanceof TaxiPickupTask) {
					t0 = ((TaxiPickupTask)task).getRequest().getEarliestStartTime();
				} else if(task instanceof TaxiFreightStartTask) {
					t0 = ((TaxiFreightStartTask)task).getEarliestStartTime();
				}
				// the actual pickup starts at max(t, t0)
				return Math.max(newBeginTime, t0) + taxiCfg.getPickupDuration();
			}
			case DROPOFF: {
				//if the newBeginTime is later than the serviceStartTimeWindow, the service can't be done // delivery must be skipped... this should be handled somehow
				//e.g. by giving at least a warning and maybe by forcing the taxi to drive back to the depot to return the non-delivered package
				if(task instanceof TaxiFreightServiceTask) {
					
				}
				
				// cannot be shortened/lengthen, therefore must be moved forward/backward
				return newBeginTime + taxiCfg.getDropoffDuration();
			}
	
			default:
				throw new IllegalStateException();
		}
	}
}
