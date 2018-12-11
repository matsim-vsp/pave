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
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.dvrp.path.DivertedVrpPath;
import org.matsim.contrib.dvrp.path.VrpPath;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.schedule.AbstractTask;
import org.matsim.contrib.dvrp.schedule.DriveTask;
import org.matsim.contrib.dvrp.schedule.DriveTaskImpl;
import org.matsim.contrib.taxi.schedule.TaxiTask;
import org.matsim.core.mobsim.framework.DriverAgent;
import org.matsim.core.mobsim.qsim.interfaces.MobsimVehicle;
import org.matsim.vehicles.Vehicle;

/**
 * @author tschlenther
 *
 */
public class TaxiFreightServiceDriveTask extends DriveTaskImpl implements TaxiTask {

	public TaxiFreightServiceDriveTask(VrpPathWithTravelData path) {
		super(path);
		// TODO Auto-generated constructor stub
	}

	@Override
	public TaxiTaskType getTaxiTaskType() {
		return TaxiTaskType.OCCUPIED_DRIVE;
	} 

	
	@Override
	protected String commonToString() {
		return "[" + getTaxiTaskType().name() + "_SERVICE]" + super.commonToString();
	}

}
