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
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dynagent.FirstLastSimStepDynActivity;
import org.matsim.contrib.freight.carrier.TimeWindow;

/**
 * @author tschlenther
 *
 */
class PFAVServiceActivity extends FirstLastSimStepDynActivity {

	private final double departureTime;
    private final TimeWindow timeWindow;
    private final Id<DvrpVehicle> vehicleId;
	
	/**
	 * @param activityType
	 */
	PFAVServiceActivity(String activityType, PFAVServiceTask serviceTask, Id<DvrpVehicle> vehicleId) {
		super(activityType + "_" + serviceTask.getCarrierService().getId());
		this.departureTime = serviceTask.getEndTime();
        timeWindow = serviceTask.getCarrierService().getServiceStartTimeWindow();
        this.vehicleId = vehicleId;
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
		super.beforeFirstStep(now);
	}

	TimeWindow getTimeWindow() {
        return timeWindow;
    }

	Id<DvrpVehicle> getVehicleId() {
        return vehicleId;
    }

}
