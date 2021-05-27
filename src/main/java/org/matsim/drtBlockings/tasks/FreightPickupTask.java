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

import com.graphhopper.jsprit.core.problem.job.Shipment;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.drt.schedule.DrtTaskType;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.freight.carrier.CarrierShipment;
import org.matsim.contrib.freight.carrier.TimeWindow;
import org.matsim.contrib.freight.carrier.Tour;

import static org.matsim.contrib.drt.schedule.DrtTaskBaseType.STAY;

public class FreightPickupTask extends StayTask {

    public static final DrtTaskType FREIGHT_PICKUP_TASK_TYPE = new DrtTaskType("PICKUP", STAY);

    private CarrierShipment shipment;

    public FreightPickupTask(Tour.Pickup pickupActivity, double start, double end, Link location){
        super(FREIGHT_PICKUP_TASK_TYPE, start, end, location);
        if(location.getId() != pickupActivity.getLocation()) throw new IllegalArgumentException();
        this.shipment = pickupActivity.getShipment();
    }

    public TimeWindow getTimeWindow() {
        return this.shipment.getPickupTimeWindow();
    }

    public CarrierShipment getShipment() {return this.shipment;}

}
