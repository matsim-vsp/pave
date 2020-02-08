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
package privateAV;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.taxi.schedule.TaxiTask;

/**
 * @author tschlenther
 *
 */
class PFAVRetoolTask extends StayTask implements TaxiTask {

	double earliestStartTime = 0.0;
	Id<DvrpVehicle> vehicle = null;
	
	/**
	 * @param beginTime
	 * @param endTime
	 * @param link
	 */
    PFAVRetoolTask(double beginTime, double endTime, Link link) {
		super(beginTime, endTime, link, PFAVActionCreator.RETOOL_ACTIVITY_TYPE);
	}
	
	/**
	 * if not set at some point, this will return 0.0
	 * @return
	 */
	double getEarliestStartTime() {
		return this.earliestStartTime;
	}

	void setEarliestStartTime(double earliestStartTime) {
		this.earliestStartTime = earliestStartTime;
	}

	void setVehicle(Id<DvrpVehicle> vehicle) {
		this.vehicle = vehicle;
	}
	
	@Override
	public TaxiTaskType getTaxiTaskType() {
		/*
		 * we model this as a dropoff task, since retooling is closest to "picking a new car body up" or "dropping the old car body".
		 * but pickup task type is somehow used for TaxiStatsCalculator in correspondence with a request (which we do not have here, so we use dropoff type.
		 * furthermore, if it was of type stay, the task could be removed by the taxischeduler if there is delay in the schedule
		 */
		return TaxiTaskType.DROPOFF;
	}

	@Override
	public String toString() {
		return super.toString() + " [" + this.getStatus() + "] " + "vehicle=" + this.vehicle;
	}
}
