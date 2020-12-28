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

interface DrtBlockingManager {

    /**
     * Returns whether [@code vehicle} is blocked. This method is called by {@link BlockingVehicleDataEntryFactory#create(DvrpVehicle, double)} during the request insertion process.
     *
     * @param vehicle
     * @return
     */
    boolean isVehicleBlocked(DvrpVehicle vehicle);

// TODO possibly move all the DrtBlockingRequest handling from the DrtBlockingOptimizer to here

//    /**
//     * Processes the {@code request}. <br>
//     * (1) Checks whether blocking (in general) is currently allowed <br>
//     * (2) possibly dispatches and blocks a vehicle and returns it. <br>
//     *
//     * @param request
//     * @return the vehicle that got assigned to the {@code request}, or null if no vehicle got blocked and assigned.
//     */
//    @Nullable
//    DvrpVehicle handleBlockingRequest(DrtBlockingRequest request);

    /**
     * Vehicle gets blocked (unavailable for passenger 'normal' transport). In other words, it is not eligible for conventianal {@link org.matsim.contrib.drt.passenger.DrtRequest}<br>
     *
     * TODO
     * Currently, it is not supported that the blocking can take place in the future.
     *
     * If the blocking can not be executed, the {@code return} value is false, otherwise it is true.
     * @param vehicle
     * @param request
     * @return
     */
    boolean blockVehicle(DvrpVehicle vehicle, DrtBlockingRequest request);

    /**
     * Unblocks the {@code vehicle}.
     * That means that the {@code vehicle} is available for conventional passenger requests/transport again.
     * @param vehicle
     */
    void unblockVehicle(DvrpVehicle vehicle);

}
