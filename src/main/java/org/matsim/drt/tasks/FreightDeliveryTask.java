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

import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.freight.carrier.TimeWindow;
import org.matsim.contrib.freight.carrier.Tour;

public class FreightDeliveryTask extends StayTask {

    private final TimeWindow timeWindow;
    private final int capacity;

    public FreightDeliveryTask(Tour.ServiceActivity serviceActivity, double start, double end, Link location) {
        super(FreightTaskType.DELIVERY, start,  end, location);
        if(location.getId() != serviceActivity.getLocation()) throw new IllegalArgumentException();
        this.timeWindow = serviceActivity.getTimeWindow();
        this.capacity = serviceActivity.getService().getCapacityDemand();
    }

    public FreightDeliveryTask(Tour.Delivery deliveryActivity, double start, double end, Link location){
        super(FreightTaskType.DELIVERY, start, end, location);
        if(location.getId() != deliveryActivity.getLocation()) throw new IllegalArgumentException();
        this.timeWindow = deliveryActivity.getTimeWindow();
        this.capacity = deliveryActivity.getShipment().getSize();
    }

    public TimeWindow getTimeWindow() {
        return timeWindow;
    }
}
