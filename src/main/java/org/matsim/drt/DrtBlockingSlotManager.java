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

package org.matsim.drt;

import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.core.config.Config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class DrtBlockingSlotManager implements DrtBlockingManager {

    private final List<Set<DvrpVehicle>> blockedVehicles;
    private final int[] maximumNumberOfBlockings;

    private final Config config;


    public DrtBlockingSlotManager(Config config, int[] maximumNumberOfBlockings) {
        if(maximumNumberOfBlockings.length > config.qsim().getEndTime() / (60*5) ){
            //TODO be a bit more expressive here...
            throw new RuntimeException("Please do not define slots for DrtBlocking management that are shorter than 5 minutes.. ");
        }
        this.config = config;
        this.maximumNumberOfBlockings = maximumNumberOfBlockings;
        this.blockedVehicles = new ArrayList<>();
        for(int i = 0; i < maximumNumberOfBlockings.length; i++){
            this.blockedVehicles.add(new HashSet<>());
        }
    }

    @Override
    public boolean isVehicleBlocked(DvrpVehicle vehicle, double time) {
        int slot = (int) Math.floor(time / (config.qsim().getEndTime()/ maximumNumberOfBlockings.length));
        return (blockedVehicles.get(slot).contains(vehicle));
    }

    @Override
    public boolean blockVehicleIfPossible(DvrpVehicle vehicle, double startTime, double endTime) {
        int startSlot = (int) Math.floor(startTime / (config.qsim().getEndTime()/ maximumNumberOfBlockings.length));

        //TODO: this probably breaks if endTime == config.qsim().getEndTime() ....
        int endSlot = (int) Math.floor(endTime / (config.qsim().getEndTime()/ maximumNumberOfBlockings.length));
        if (! isBlockIsPossible(startSlot, endSlot)) return false;

        //actually block the vehicle
        for(int i = startSlot; i <= endSlot; i++){
            blockedVehicles.get(i).add(vehicle);
        }

        return true;
    }

    private boolean isBlockIsPossible(int startSlot, int endSlot) {
        //check if vehicle can get blocked
        for(int i = startSlot; i <= endSlot; i++){
            if(maximumNumberOfBlockings[i] < blockedVehicles.get(i).size()){
                throw new IllegalStateException("the amount of vehicles that are blocked for for slot " + i + " is greater than the allowed value!\n" +
                        "allowed value = " + maximumNumberOfBlockings[i] + "\n" +
                        "amount of blocked vehicles = " + blockedVehicles.get(i).size());
            }
            if(maximumNumberOfBlockings[i] == blockedVehicles.get(i).size()){
                return false;
            }
        }
        return true;
    }

    @Override
    public void unblockVehicleAfterTime(DvrpVehicle vehicle, double time) {
        int startSlot = (int) Math.ceil(time / (config.qsim().getEndTime()/ maximumNumberOfBlockings.length));
        for(int i = startSlot; i < maximumNumberOfBlockings.length; i++){
            this.blockedVehicles.remove(new HashSet<>());
        }
    }
}
