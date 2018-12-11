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
package freight.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.data.Vehicle;
import org.matsim.contrib.dvrp.data.VehicleImpl;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelDataImpl;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.dvrp.schedule.ScheduleImpl;
import org.matsim.contrib.dvrp.schedule.StayTask;
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
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelTime;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import privateAV.schedule.TaxiFreightServiceDriveTask;
import privateAV.schedule.TaxiFreightServiceTask;
import privateAV.schedule.TaxiFreightStartTask;

/**
 * @author tschlenther
 *
 */
public class ConvertFreightTourForDvrp {
	
	
	public static List<StayTask> convertToList(ScheduledTour freightTour, Network network){
	
		//as far as it seems, the Start and End activities are not part of ScheduledTour.getTour.getTourElements();
		//otherwise, this method could be shortened by two thirds
		
		List<StayTask> dvrpList = new ArrayList<StayTask>();
		
		double tBegin = freightTour.getTour().getStart().getExpectedArrival();
		
		double tEnd = tBegin + 60;	//trying to harcde the duration of the FreightStartTask in order to debug...
		
		
		
//		double tEnd = tBegin + freightTour.getTour().getStart().getDuration();
		
		
		
		Link location = network.getLinks().get(freightTour.getTour().getStart().getLocation());
		
		dvrpList.add(new TaxiFreightStartTask(tBegin, tEnd, location));
		
		for(int i = 0 ; i < freightTour.getTour().getTourElements().size() ; i++) {
			TourElement currentElement = freightTour.getTour().getTourElements().get(i);
			
			if(currentElement instanceof ServiceActivity) {
				tBegin = ((ServiceActivity) currentElement).getExpectedArrival();
				tEnd = tBegin + ((ServiceActivity) currentElement).getDuration();
				location = network.getLinks().get(((ServiceActivity) currentElement).getLocation());
				
				dvrpList.add(new TaxiFreightServiceTask(tBegin, tEnd, location, ((ServiceActivity) currentElement).getService()));
			}
		}
		tBegin = freightTour.getTour().getEnd().getExpectedArrival();
		tEnd = tBegin + freightTour.getTour().getEnd().getDuration();
		
		location = network.getLinks().get(freightTour.getTour().getEnd().getLocation());
		
		dvrpList.add(new TaxiStayTask(tBegin, tEnd, location));
		
		return dvrpList;
		
	}
	
	public static Schedule convertOnlyActivitiesToSchedule(ScheduledTour freightTour, Network network) {
		
		Schedule dvrpSchedule = new ScheduleImpl(convertVehicle(freightTour, network));
		
		Iterator<TourElement> it = freightTour.getTour().getTourElements().iterator();
		
		CarrierVehicle carrierVehicle = freightTour.getVehicle();
		double earliestStart = carrierVehicle.getEarliestStartTime();
		
		Start startAct = freightTour.getTour().getStart();
		
		if(startAct == null) {
			throw new RuntimeException();
		}
		if(network == null) {
			throw new RuntimeException();
		}
		/*it's not yet clear what time points we put in here.... but maybe it's not too important since they get updated anyways, as soon as the schedule is assigned to a taxi
		*furthermore, the duration of a 'Start' activitiy in the freight contrib is currently hardcoded to 0  - this should get changed in the future
		*tschlenther 11/2018<
		*/
		TaxiFreightStartTask startTask = new TaxiFreightStartTask(earliestStart, earliestStart + startAct.getDuration(),
																	network.getLinks().get(startAct.getLocation())); 
		startTask.setEarliestStartTime(carrierVehicle.getEarliestStartTime());
		
		dvrpSchedule.addTask(startTask);
		
		//the first element - this is actually the first leg
		while(it.hasNext()) {
			TourElement currentElement = it.next();
			
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
	
	
	private static Vehicle convertVehicle(ScheduledTour freightTour, Network network) {
		/*TODO: capacity could be retrieved from the service-Activities.... or just set to any value 
		 */
		
		CarrierVehicle freightVehicle = freightTour.getVehicle();
		double tStart = freightTour.getTour().getStart().getTimeWindow().getStart();
		double tEnd = freightTour.getTour().getEnd().getTimeWindow().getEnd();
		String vehID = freightVehicle.getVehicleId().toString();
		int capacity = 0;
		
		Vehicle dvrpVehicle = new VehicleImpl(Id.create(vehID,Vehicle.class), network.getLinks().get(freightTour.getTour().getStartLinkId()), capacity , tStart, tEnd);		
		
		return dvrpVehicle;
	}


	public static Schedule convert2(ScheduledTour freightSchedule, Network network, LeastCostPathCalculator router, TravelTime traveltime) {
		Start startAct = freightSchedule.getTour().getStart();
		
		Schedule dvrpSchedule = new ScheduleImpl(convertVehicle(freightSchedule, network));
		
		double beginStart = startAct.getExpectedArrival();
		TaxiFreightStartTask startTask = new TaxiFreightStartTask(beginStart, beginStart + startAct.getDuration(),
																	network.getLinks().get(startAct.getLocation()));
		
		dvrpSchedule.addTask(startTask);
		
		for(TourElement currentElement : freightSchedule.getTour().getTourElements()){
			if(currentElement instanceof Leg) {
				Leg current = (Leg) currentElement;
				
				Link fromLink = network.getLinks().get(current.getRoute().getStartLinkId());
				Link toLink = network.getLinks().get(current.getRoute().getEndLinkId());
				
				VrpPathWithTravelData path = VrpPaths.calcAndCreatePath(fromLink, toLink, current.getExpectedDepartureTime(), router, traveltime);
				dvrpSchedule.addTask(new TaxiFreightServiceDriveTask(path));
			} else if(currentElement instanceof ServiceActivity) {
				
				ServiceActivity act = (ServiceActivity) currentElement;
				
				double begin = act.getExpectedArrival();
				Link location = network.getLinks().get(act.getLocation());
				
				dvrpSchedule.addTask(new TaxiFreightServiceTask(begin, begin + act.getDuration(), location, act.getService()));
			}else if(currentElement instanceof End) {
				
				End act = (End) currentElement;
				
				double begin = act.getExpectedArrival();
				Link location = network.getLinks().get(act.getLocation());
				
				dvrpSchedule.addTask(new TaxiStayTask(begin, begin + act.getDuration(), location));
			}
		}
		return dvrpSchedule;
	}
	
	
}
