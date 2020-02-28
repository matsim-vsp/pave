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

package org.matsim.drt.tasks;

import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.schedule.DrtStayTaskEndTimeCalculator;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.schedule.StayTask;

public class FreightTaskEndTimeCalculator extends DrtStayTaskEndTimeCalculator {

    public FreightTaskEndTimeCalculator(DrtConfigGroup drtConfigGroup) {
        super(drtConfigGroup);
    }

    @Override
    public double calcNewEndTime(DvrpVehicle vehicle, StayTask task, double newBeginTime) {
        if(task.getTaskType() instanceof FreightTaskType){
            double duration = task.getEndTime() - task.getBeginTime();
            return newBeginTime + duration;
        } else {
            return super.calcNewEndTime(vehicle, task, newBeginTime);
        }
    }
}
