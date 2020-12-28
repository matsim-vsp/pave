/* *********************************************************************** *
 * project: org.matsim.*
 * Controler.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
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

package org.matsim.drtBlockings;

import org.matsim.contrib.dvrp.fleet.DvrpVehicle;

import java.util.HashSet;
import java.util.Set;

class SimpleBlockingManager implements DrtBlockingManager{

	Set<DvrpVehicle> blockedVehicles = new HashSet<>();

	@Override
	public boolean isVehicleBlocked(DvrpVehicle vehicle) {
		return this.blockedVehicles.contains(vehicle);
	}

	@Override
	public boolean blockVehicle(DvrpVehicle vehicle, DrtBlockingRequest request) {
		return this.blockedVehicles.add(vehicle);
	}

	@Override
	public void unblockVehicle(DvrpVehicle vehicle) {
		this.blockedVehicles.remove(vehicle);
	}

}
