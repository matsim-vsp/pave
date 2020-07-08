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

import static org.matsim.contrib.taxi.schedule.TaxiTaskBaseType.DROPOFF;

import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.freight.carrier.CarrierService;
import org.matsim.contrib.taxi.schedule.TaxiTaskType;

/**
 * @author tschlenther
 */
class PFAVServiceTask extends StayTask {
	public static final TaxiTaskType TYPE = new TaxiTaskType("SERVICE", DROPOFF);

	CarrierService service;

	/**
	 * @param beginTime
	 * @param endTime
	 * @param link
	 */
	PFAVServiceTask(double beginTime, double endTime, Link link, CarrierService service) {
		super(TYPE, beginTime, endTime, link);
		this.service = service;
	}

	CarrierService getCarrierService() {
		return this.service;
	}
}
