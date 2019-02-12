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
package privateAV.vrpagent;

import org.matsim.contrib.dynagent.FirstLastSimStepDynActivity;

import privateAV.schedule.PFAVServiceTask;

/**
 * @author tschlenther
 *
 */
public class PFAVServiceActivity extends FirstLastSimStepDynActivity {

	private final double departureTime;
	/**
	 * @param activityType
	 */
	public PFAVServiceActivity(String activityType, PFAVServiceTask serviceTask) {
		super(activityType);
		this.departureTime = serviceTask.getEndTime();
	}

	/* (non-Javadoc)
	 * @see org.matsim.contrib.dynagent.FirstLastSimStepDynActivity#isLastStep(double)
	 */
	@Override
	protected boolean isLastStep(double now) {
		return now >= departureTime;
	}
	
	@Override
	protected void beforeFirstStep(double now) {
		// TODO do we want/need wo simulate something here
		super.beforeFirstStep(now);
	}

}
