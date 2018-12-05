/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
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
package privateAV.infrastructure;

import java.util.Iterator;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.data.Vehicle;
import org.matsim.contrib.dvrp.data.VehicleImpl;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelDataImpl;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.dvrp.schedule.ScheduleImpl;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.freight.carrier.CarrierVehicle;
import org.matsim.contrib.freight.carrier.ScheduledTour;
import org.matsim.contrib.freight.carrier.Tour.*;
import org.matsim.contrib.taxi.passenger.TaxiRequestCreator;
import org.matsim.contrib.taxi.schedule.TaxiDropoffTask;
import org.matsim.contrib.taxi.schedule.TaxiEmptyDriveTask;
import org.matsim.contrib.taxi.schedule.TaxiOccupiedDriveTask;
import org.matsim.contrib.taxi.schedule.TaxiPickupTask;
import org.matsim.contrib.taxi.schedule.TaxiStayTask;
import org.matsim.contrib.taxi.schedule.TaxiTask;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import privateAV.Task.TaxiFreightServiceDriveTask;
import privateAV.Task.TaxiFreightServiceTask;
import privateAV.Task.TaxiFreightStartTask;

/**
 * @author tschlenther
 *
 */
public class FreightTourToDvrpSchedule {

	@Named(DvrpRoutingNetworkProvider.DVRP_ROUTING)
	private static Network network;
	
	
	public static Schedule convert(ScheduledTour freightTour) {

		
		Schedule dvrpSchedule = new ScheduleImpl(convertVehicle(freightTour));
		
		Iterator<TourElement> it = freightTour.getTour().getTourElements().iterator();
		
		CarrierVehicle carrierVehicle = freightTour.getVehicle();
		double earliestStart = carrierVehicle.getEarliestStartTime();
		
		
		//get the first element - this should be the start activity
		TourElement currentElement = it.next();
		if(!(currentElement instanceof Start)) {
			throw new IllegalArgumentException("the first activity of the ScheduledTour " + freightTour.toString() + " is not a 'Start' activity");
		} else {
			Start startAct = (Start) currentElement;
			
			/*it's not yet clear what time points we put in here.... but maybe it's not too important since they get updated anyways, as soon as the schedule is assigned to a taxi
			*furthermore, the duarion of a 'Start' activitiy in the freight contrib is currently hardcoded to 0  - this should get changed in the future
			*tschlenther 11/2018
			*/
			TaxiFreightStartTask startTask = new TaxiFreightStartTask(earliestStart, earliestStart + startAct.getDuration(),
																		network.getLinks().get(startAct.getLocation())); 
			startTask.setEarliestStartTime(carrierVehicle.getEarliestStartTime());
			
			dvrpSchedule.addTask(startTask);
		}
		
		
		while(it.hasNext()) {
			
			Leg lastLeg = null;
			Task task;
			
			/*
			 * wir kriegen aus dem freight leg keine netzwerkRoute, also keine Linksequenz.
			 * Außerdem haben wir hier (zur Zeit!) keinen dvrp router, können also keinen vrpPath erzeugen.
			 * Also convertieren wir erstmal nur die activities in entsprechende Tasks und verlagern das Erzeugen der DriveTasks in den Scheduler 
			 */
//			if (currentElement instanceof Leg) {
//				lastLeg = (Leg) currentElement;
//					lastLeg.getExpectedDepartureTime();
//					lastLeg.getExpectedTransportTime();
//					lastLeg.getRoute();
//					
//					
//					VrpPathWithTravelDataImpl path = new VrpPathWithTravelDataImpl(lastLeg.getExpectedDepartureTime(), lastLeg.getExpectedTransportTime(), , linkTTs)
//					task = new TaxiFreightServiceDriveTask(path);
//					
//			}

				
				
			if(currentElement instanceof ServiceActivity) {
				ServiceActivity serviceActivity = (ServiceActivity) currentElement;
				double start = serviceActivity.getExpectedArrival();
				task = new TaxiFreightServiceTask(start, start + serviceActivity.getDuration(),
																				network.getLinks().get(serviceActivity.getLocation()), serviceActivity.getService());
				
				dvrpSchedule.addTask(task);
			} else if(currentElement instanceof End) {
				End endAct = (End) currentElement;
				
				/*drive back to depot
				* should this really be an empty drive? because the vehicle inside still is configured for freight purpose
				* and the vehicle NEEDS to be reconfigured in depot before returnin to 'normal/private' duty
				*/
//				task = new TaxiEmptyDriveTask(path)
				
				
				//change the location to the depot link id if an empty drive task was inserted before!
				task = new TaxiStayTask(endAct.getExpectedArrival(), carrierVehicle.getLatestEndTime(), network.getLinks().get(endAct.getLocation()));
				dvrpSchedule.addTask(task);
			}
				
		}
		return dvrpSchedule;
	}
	
	
	private static Vehicle convertVehicle(ScheduledTour freightTour) {
		/*we need a Link and not only the id - we could inject our dvrp network here but that seems to me a bit too much for the moment
		 * as we assign the schedule to be created to an existing dvrp-vehicle at some other point anyways...
		 * so basically the vehicle can be null in the schedule to be created as long as it is not assigned yet
		 * 
		 *capacity could be retrieved from the service-Activities.... or just set to any value 
		 */
		
		CarrierVehicle freightVehicle = freightTour.getVehicle();
		double tStart = freightTour.getTour().getStart().getTimeWindow().getStart();
		double tEnd = freightTour.getTour().getEnd().getTimeWindow().getEnd();
		String vehID = freightVehicle.getVehicleId().toString();
		int capacity = 0;
		
//		Vehicle dvrpVehicle = new VehicleImpl(Id.create(vehID,Vehicle.class), freightTour.getTour().getStartLinkId(), capacity , tStart, tEnd);		
		
		return null;
	}
}
