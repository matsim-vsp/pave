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

import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.freight.carrier.ScheduledTour;
import org.matsim.contrib.freight.carrier.Tour.ServiceActivity;
import org.matsim.contrib.freight.carrier.Tour.TourElement;
import privateAV.PFAVUtils;
import privateAV.schedule.PFAVServiceTask;
import privateAV.schedule.PFAVStartTask;

import java.util.ArrayList;
import java.util.List;

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
		double tEnd = tBegin + PFAVUtils.RETOOL_TIME_FOR_PFAVEHICLES;
		
//		double tEnd = tBegin + freightTour.getTour().getStart().getDuration();
		
		Link location = network.getLinks().get(freightTour.getTour().getStart().getLocation());
		
		dvrpList.add(new PFAVStartTask(tBegin, tEnd, location));
		
		for(int i = 0 ; i < freightTour.getTour().getTourElements().size() ; i++) {
			TourElement currentElement = freightTour.getTour().getTourElements().get(i);
			
			if(currentElement instanceof ServiceActivity) {
				tBegin = ((ServiceActivity) currentElement).getExpectedArrival();
				tEnd = tBegin + ((ServiceActivity) currentElement).getDuration();
				location = network.getLinks().get(((ServiceActivity) currentElement).getLocation());
				dvrpList.add(new PFAVServiceTask(tBegin, tEnd, location, ((ServiceActivity) currentElement).getService()));
			}
		}

		//somehow, the expected arrival of the end activity is always 0. Therefore we don't include the end activity the taxiTaskList
//		tBegin = freightTour.getTour().getEnd().getExpectedArrival();
//
//		tEnd = tBegin + PFAVUtils.RETOOL_TIME_FOR_PFAVEHICLES;
//
//		location = network.getLinks().get(freightTour.getTour().getEnd().getLocation());
//
//		dvrpList.add(new TaxiStayTask(tBegin, tEnd, location));
		
		return dvrpList;
		
	}

}
