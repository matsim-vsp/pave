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

    boolean isVehicleBlocked(DvrpVehicle vehicle, double time);

    /**
     * vehicle is blocked for conventional requests starting for the time window specified in the {@code request}.
     *
     * TODO
     * Currently, it is not supported that the blocking can take place in the future.
     * Implementations should set the blocking start to min(request.submissionTime, request.startTime).
     *
     * if the blocking can not be executed, the return value is false; otherwise it is true.
     * @param vehicle
     * @param request
     * @return
     */
    boolean blockVehicle(DvrpVehicle vehicle, DrtBlockingRequest request);

    /**
     * Unblocks the vehicle after the specified {@code time}.
     * That means the vehicle is available for conventional requests again.
     * @param vehicle
     * @param time the time after which the vehicle should be available for conventional requests again
     */
    void unblockVehicleAfterTime(DvrpVehicle vehicle, double time);

    boolean extendBlocking(DvrpVehicle vehicle, double updatedBlockingEnd);

}
