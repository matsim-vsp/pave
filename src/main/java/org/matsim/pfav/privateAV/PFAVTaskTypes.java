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

package org.matsim.pfav.privateAV;

import com.google.common.collect.ImmutableList;
import org.matsim.contrib.taxi.schedule.TaxiTaskType;

import java.util.List;

import static org.matsim.contrib.taxi.schedule.TaxiTaskBaseType.*;

class PFAVTaskTypes {

	static final TaxiTaskType ACCESS_TO_DEPOT = new TaxiTaskType("accessToDepot",EMPTY_DRIVE);
	static final TaxiTaskType RETOOL = new TaxiTaskType("RETOOL", DROPOFF);
	static final TaxiTaskType SERVICE =  new TaxiTaskType("SERVICE", DROPOFF);
	static final TaxiTaskType SERVICE_DRIVE = new TaxiTaskType("SERVICE_DRIVE", OCCUPIED_DRIVE);
	static final TaxiTaskType RETURN_TO_DEPOT =new TaxiTaskType("returnToDepot", EMPTY_DRIVE);
	static final TaxiTaskType EGRESS_FROM_DEPOT = new TaxiTaskType("egressFromDepot",EMPTY_DRIVE);

	static final List<TaxiTaskType> TYPES = ImmutableList.of(ACCESS_TO_DEPOT,
			RETOOL,
			SERVICE,
			SERVICE_DRIVE,
			RETURN_TO_DEPOT,
			EGRESS_FROM_DEPOT);
}
