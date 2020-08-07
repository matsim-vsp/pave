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

import org.matsim.contrib.drt.schedule.DrtTaskType;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.schedule.DriveTask;

import static org.matsim.contrib.drt.schedule.DrtTaskBaseType.DRIVE;

//TODO maybe we should differ loaded from empty drives ?
public class FreightDriveTask extends DriveTask {

    public static final DrtTaskType FREIGHT_DRIVE_TASK_TYPE = new DrtTaskType("FREIGHTDRIVE", DRIVE);

    public FreightDriveTask(VrpPathWithTravelData path) {
        super(FREIGHT_DRIVE_TASK_TYPE, path);
    }
}
