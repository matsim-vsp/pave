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
package privateAV.schedule;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.schedule.StayTaskImpl;
import org.matsim.contrib.freight.carrier.FreightConstants;
import org.matsim.contrib.taxi.schedule.TaxiTask;

import privateAV.vrpagent.PFAVActionCreator;

/**
 * @author tschlenther
 *
 */
public class PFAVStartTask extends StayTaskImpl implements TaxiTask {

	double earliestStartTime = 0.0;
	Id<DvrpVehicle> vehicle = null;
	
	/**
	 * @param beginTime
	 * @param endTime
	 * @param link
	 */
	public PFAVStartTask(double beginTime, double endTime, Link link) {
		super(beginTime, endTime, link, PFAVActionCreator.START_ACTIVITY_TYPE);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * if not set at some point, this will return 0.0
	 * @return
	 */
	public double getEarliestStartTime() {
		return this.earliestStartTime;
	}
	
	public void setEarliestStartTime(double earliestStartTime) {
		this.earliestStartTime = earliestStartTime;
	}

	public void setVehicle(Id<DvrpVehicle> vehicle) {
		this.vehicle = vehicle;
	}
	
	@Override
	public TaxiTaskType getTaxiTaskType() {
		return TaxiTaskType.STAY;
	}

}
