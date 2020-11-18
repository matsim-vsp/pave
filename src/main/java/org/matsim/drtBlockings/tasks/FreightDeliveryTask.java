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

import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.drt.schedule.DrtTaskType;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.freight.carrier.CarrierService;
import org.matsim.contrib.freight.carrier.TimeWindow;
import org.matsim.contrib.freight.carrier.Tour;

import static org.matsim.contrib.drt.schedule.DrtTaskBaseType.STAY;

public class    FreightDeliveryTask extends StayTask {

    private final Tour.TourActivity tourActivity;

    public static final DrtTaskType FREIGHT_DELIVERY_TASK_TYPE = new DrtTaskType("DELIVERY", STAY);

    public FreightDeliveryTask(Tour.TourActivity deliveryActivity, double start, double end, Link location){
        super(FREIGHT_DELIVERY_TASK_TYPE, start, end, location);
        if(location.getId() != deliveryActivity.getLocation()) throw new IllegalArgumentException();

        if(! (deliveryActivity instanceof Tour.ShipmentBasedActivity || deliveryActivity instanceof Tour.ServiceActivity) ){
            throw new IllegalArgumentException();
        }
        this.tourActivity = deliveryActivity;
    }

    public TimeWindow getTimeWindow() {
        return this.tourActivity.getTimeWindow();
    }

    public double getDeliveryDuration(){
        return this.tourActivity.getDuration();
    }

    // TODO consider to change this class into a FreightServiceTask and only return the service (and not a more general Activity)
    public Tour.TourActivity getTourActivity() {
        return tourActivity;
    }
}
