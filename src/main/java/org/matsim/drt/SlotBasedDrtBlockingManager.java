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

import org.apache.log4j.Logger;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.core.config.Config;
import org.matsim.core.mobsim.framework.MobsimTimer;

import java.util.*;

class SlotBasedDrtBlockingManager implements DrtBlockingManager {

    private final MobsimTimer timer;
    private List<Set<DvrpVehicle>> blockedVehicles;
    private final int[] maximumNumberOfBlockings;
    private final Config config;
    private static Logger log = Logger.getLogger(SlotBasedDrtBlockingManager.class);
    private int endTime;

    SlotBasedDrtBlockingManager(Config config, int[] maximumNumberOfBlockings, MobsimTimer timer) {
        if(maximumNumberOfBlockings.length > config.qsim().getEndTime() / (60*5) ){
            //TODO be a bit more expressive here...
            throw new RuntimeException("Please do not define slots for DrtBlocking management that are shorter than 5 minutes.. ");
        }
        this.config = config;
        this.timer = timer;
        this.maximumNumberOfBlockings = maximumNumberOfBlockings;
        initializeEndTime(config);
        initializeVehicleList();
    }

    SlotBasedDrtBlockingManager(Config config, int maximumNumberOfBlockedVehiclesAtATime, MobsimTimer timer) {
        this.config = config;
        this.timer = timer;
        log.info("no slot width defined. will work with 15 minutes time slots. the maximum amount of drt blockings\n" +
                "for each time slot is set to " + maximumNumberOfBlockedVehiclesAtATime);
        initializeEndTime(config);
        this.maximumNumberOfBlockings = new int[endTime / (15*60)];
        Arrays.fill(this.maximumNumberOfBlockings, maximumNumberOfBlockedVehiclesAtATime);
        initializeVehicleList();
    }

    private void initializeEndTime(Config config) {
        this.endTime = (int) config.qsim().getEndTime();
        if (this.endTime <= 0){
            log.warn("no end time for qsim is defined. We will split 30 hours into time slots." +
                    "This might lead to breakdown later if qsim runs longer than 30 hours");
            this.endTime = 30*3600;
        }
    }

    private void initializeVehicleList() {
        this.blockedVehicles = new ArrayList<>();
        if(maximumNumberOfBlockings.length == 0) throw new IllegalStateException();
        for(int i = 0; i < maximumNumberOfBlockings.length; i++){
            this.blockedVehicles.add(new HashSet<>());
        }
    }

    @Override
    public boolean isVehicleBlocked(DvrpVehicle vehicle, double time) {
        int slot = (int) Math.floor(time / (this.endTime/ maximumNumberOfBlockings.length));
        return (this.blockedVehicles.get(slot).contains(vehicle));
    }

    @Override
    public boolean blockVehicle(DvrpVehicle vehicle, DrtBlockingRequest request){
        //TODO currently, the start of a blocking in the future is not supported yet (because of hickups with conventional request insertion)
        return this.blockVehicleIfPossible(vehicle, Math.min(request.getStartTime(), timer.getTimeOfDay()), request.getEndTime());
    }

    private boolean blockVehicleIfPossible(DvrpVehicle vehicle, double startTime, double endTime) {
        int startSlot = (int) Math.floor(startTime / (this.endTime/ maximumNumberOfBlockings.length));
        //TODO: this probably breaks if endTime == config.qsim().getEndTime() ....
        int endSlot = (int) Math.floor(endTime / (this.endTime/ maximumNumberOfBlockings.length));
        if (! isBlockIsPossible(startSlot, endSlot, vehicle)) return false;

        //actually block the vehicle
        for(int i = startSlot; i <= endSlot; i++){
            this.blockedVehicles.get(i).add(vehicle);
        }

        return true;
    }

    private boolean isBlockIsPossible(int startSlot, int endSlot, DvrpVehicle vehicle) {
        //check if vehicle can get blocked
        for(int i = startSlot; i <= endSlot; i++){
            if(maximumNumberOfBlockings[i] < blockedVehicles.get(i).size()){
                //this can happen through blocking extension
                log.warn("the maximum number blockings is exceeded for slot " + i + ". This can happen through blocking extension.");
//                throw new IllegalStateException("the amount of vehicles that are blocked for for slot " + i + " is greater than the allowed value!\n" +
//                        "allowed value = " + maximumNumberOfBlockings[i] + "\n" +
//                        "amount of blocked vehicles = " + blockedVehicles.get(i).size());

            }
            if(maximumNumberOfBlockings[i] <= blockedVehicles.get(i).size() || blockedVehicles.get(i).contains(vehicle)){
                return false;
            }
        }
        return true;
    }

    @Override
    public void unblockVehicleAfterTime(DvrpVehicle vehicle, double time) {
        int startSlot = (int) Math.ceil(time / (this.endTime/ maximumNumberOfBlockings.length));
        for(int i = startSlot; i < maximumNumberOfBlockings.length; i++){
            this.blockedVehicles.get(i).remove(vehicle);
        }
    }

    /**
     * this implementation always returns true. blockings can be extended in all cases..
     */
    @Override
    public boolean extendBlocking(DvrpVehicle vehicle, double updatedBlockingEnd) {
        int startSlot = (int) Math.floor(vehicle.getSchedule().getCurrentTask().getBeginTime() / (this.endTime/ maximumNumberOfBlockings.length));
        //TODO: this probably breaks if endTime == config.qsim().getEndTime() ....
        int endSlot = (int) Math.floor(updatedBlockingEnd / (this.endTime/ maximumNumberOfBlockings.length));

        //actually block the vehicle
        for(int i = startSlot; i <= endSlot; i++){
            blockedVehicles.get(i).add(vehicle);
        }
        return true;
    }
}
