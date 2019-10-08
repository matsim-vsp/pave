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
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.taxi.schedule.TaxiDropoffTask;

/**
 * @author tschlenther
 *
 */
public final class PFAVUtils {

	public static final String PFAV_ID_SUFFIX = "_PFAV";

	public static final int DEFAULT_PFAV_CAPACITY = 1;

	static TaxiDropoffTask getLastPassengerDropOff(Schedule schedule) {
		for (int i = schedule.getTasks().size() - 1; i >= 0; i--) {
			Task task = schedule.getTasks().get(i);
			if (task instanceof TaxiDropoffTask)
				return (TaxiDropoffTask) task;
		}
		return null;
	}

	public static Id<DvrpVehicle> generatePFAVIdFromPersonId(Id<Person> personId) {
		return Id.create(personId.toString() + PFAVUtils.PFAV_ID_SUFFIX, DvrpVehicle.class);
	}

	static int timeSlice() {
		return 1800;
	}

}
