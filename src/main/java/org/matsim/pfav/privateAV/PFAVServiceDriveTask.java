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
package org.matsim.pfav.privateAV;

import static org.matsim.contrib.taxi.schedule.TaxiTaskBaseType.OCCUPIED_DRIVE;

import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.schedule.DriveTask;
import org.matsim.contrib.taxi.schedule.TaxiTaskType;

/**
 * @author tschlenther
 */
class PFAVServiceDriveTask extends DriveTask {
	public static final TaxiTaskType TYPE = PFAVTaskTypes.SERVICE_DRIVE;

	PFAVServiceDriveTask(VrpPathWithTravelData path) {
		super(TYPE, path);
	}
}
