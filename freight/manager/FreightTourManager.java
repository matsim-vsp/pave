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
package freight.manager;

import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.core.router.util.LeastCostPathCalculator;
import privateAV.vehicle.PFAVehicle;

/**
 * @author tschlenther
 *
 */
public interface FreightTourManager {

	Schedule getBestPFAVTourForVehicle(PFAVehicle vehicle, LeastCostPathCalculator router);

	void runTourPlanning();

	void routeCarrierPlans(LeastCostPathCalculator router);
}
