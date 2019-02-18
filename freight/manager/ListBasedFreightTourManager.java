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

import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.core.router.util.LeastCostPathCalculator;
import privateAV.vehicle.PFAVehicle;

import java.util.List;

/**
 * @author tschlenther
 *
 */
public interface ListBasedFreightTourManager {
	public List<List<StayTask>> getPFAVTours();
	
	public List<StayTask> getRandomPFAVTour();
	
	public List<StayTask> getBestPFAVTourForVehicle(PFAVehicle vehicle, LeastCostPathCalculator router);

}
