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

import org.apache.log4j.Logger;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.schedule.DrtStayTaskEndTimeCalculator;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.TimeWindow;


public class FreightTaskEndTimeCalculator extends DrtStayTaskEndTimeCalculator {

    private static final Logger log = Logger.getLogger(DrtStayTaskEndTimeCalculator.class);

    private final FreightConfigGroup.TimeWindowHandling timeWindowHandling;

    public FreightTaskEndTimeCalculator(DrtConfigGroup drtConfigGroup, FreightConfigGroup freightConfigGroup) {
        super(drtConfigGroup);
        this.timeWindowHandling = freightConfigGroup.getTimeWindowHandling();
    }

    @Override
    public double calcNewEndTime(DvrpVehicle vehicle, StayTask task, double newBeginTime) {
        double duration;
        if(timeWindowHandling.equals(FreightConfigGroup.TimeWindowHandling.enforceBeginnings) &&
                (task.getTaskType().equals(FreightServiceTask.FREIGHT_SERVICE_TASK_TYPE) || task.getTaskType().equals(FreightPickupTask.FREIGHT_PICKUP_TASK_TYPE) ) ) {
            TimeWindow timeWindow = task instanceof FreightServiceTask ? ((FreightServiceTask) task).getTimeWindow() : ((FreightPickupTask) task).getTimeWindow();
            duration = task instanceof FreightServiceTask ? ((FreightServiceTask) task).getCarrierService().getServiceDuration() : ((FreightPickupTask) task).getShipment().getPickupServiceTime();

            if(newBeginTime <= timeWindow.getStart()){
                log.info("vehicle " + vehicle.getId() + " is too early at service " + ((FreightServiceTask) task).getCarrierService().getId() + ". newBeginTime = " + newBeginTime);
                return timeWindow.getStart() + duration;
            } else if(newBeginTime > timeWindow.getEnd()){
                //this has been proved to be to restrictive.
//                throw new RuntimeException("vehicle " + vehicle.getId() + " has to reschedule delivery " + task.toString() + " but it will come too late for that");
            }

        }
        if (FreightServiceTask.FREIGHT_SERVICE_TASK_TYPE.equals(task.getTaskType())) {
            return newBeginTime + ((FreightServiceTask) task).getCarrierService().getServiceDuration();
        } else if (FreightPickupTask.FREIGHT_PICKUP_TASK_TYPE.equals(task.getTaskType())) {
            return newBeginTime + ((FreightPickupTask) task).getShipment().getPickupServiceTime();
        } else if (FreightRetoolTask.RETOOL_TASK_TYPE.equals(task.getTaskType())) {
            return newBeginTime + FreightRetoolTask.RETOOL_DURATION;
        }
        return super.calcNewEndTime(vehicle, task, newBeginTime);

    }
}
