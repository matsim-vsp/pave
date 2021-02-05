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

package org.matsim.drtBlockings.tasks;

import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.schedule.DrtStayTaskEndTimeCalculator;
import org.matsim.contrib.drt.util.DrtEventsReaders;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.TimeWindow;

public class FreightTaskEndTimeCalculator extends DrtStayTaskEndTimeCalculator {

    private final FreightConfigGroup.TimeWindowHandling timeWindowHandling;

    public FreightTaskEndTimeCalculator(DrtConfigGroup drtConfigGroup, FreightConfigGroup freightConfigGroup) {
        super(drtConfigGroup);
        this.timeWindowHandling = freightConfigGroup.getTimeWindowHandling();
    }

    @Override
    public double calcNewEndTime(DvrpVehicle vehicle, StayTask task, double newBeginTime) {
        double duration = task.getEndTime() - task.getBeginTime();
        if(task.getTaskType().equals(FreightDeliveryTask.FREIGHT_DELIVERY_TASK_TYPE)
                && timeWindowHandling.equals(FreightConfigGroup.TimeWindowHandling.enforceBeginnings)){
                TimeWindow timeWindow = ((FreightDeliveryTask) task).getTimeWindow();
                duration = ((FreightDeliveryTask) task).getDeliveryDuration();
                if(newBeginTime <= timeWindow.getStart()){
                    return timeWindow.getStart() + duration;
                } else if(newBeginTime > timeWindow.getEnd()){
                    //TODO do something less restrictive
                    throw new RuntimeException("vehicle " + vehicle.getId() + " has to reschedule delivery " + task.toString() + " but it will come too late for that");
                }
        } else if(task.getTaskType().equals(FreightPickupTask.FREIGHT_PICKUP_TASK_TYPE)
            || task.getTaskType().equals(FreightRetoolTask.RETOOL_TASK_TYPE)){
            return newBeginTime + duration;
        }
        return super.calcNewEndTime(vehicle, task, newBeginTime);
    }
}
