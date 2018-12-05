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
package privateAV.infrastructure.Task;

import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.schedule.StayTaskImpl;
import org.matsim.contrib.freight.carrier.FreightConstants;
import org.matsim.contrib.taxi.schedule.TaxiTask;

/**
 * @author tschlenther
 *
 */
public class TaxiFreightStartTask extends StayTaskImpl implements TaxiTask {

	double earliestStartTime = 0.0;
	
	/**
	 * @param beginTime
	 * @param endTime
	 * @param link
	 */
	public TaxiFreightStartTask(double beginTime, double endTime, Link link) {
		super(beginTime, endTime, link, FreightConstants.START);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.matsim.contrib.taxi.schedule.TaxiTask#getTaxiTaskType()
	 */
	@Override
	public TaxiTaskType getTaxiTaskType() {
		return TaxiTaskType.STAY;
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

}
