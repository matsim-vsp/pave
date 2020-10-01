/* *********************************************************************** *
 * project: org.matsim.*
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

package org.matsim.drtBlockings.events;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.Event;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.freight.carrier.Carrier;

import java.util.Map;

public class DrtBlockingRequestScheduledEvent extends Event {

    public static final String EVENT_TYPE = "DrtBlockingRequest scheduled";
    public static final String ATTRIBUTE_VEHICLE = "vehicle";
    public static final String ATTRIBUTE_CARRIER = "carrier";
    public static final String ATTRIBUTE_REQUEST = "request";
    private final Id<Request> requestId;

    private final Id<DvrpVehicle> vehicleId;
    private final Id<Carrier> carrierId;

    public DrtBlockingRequestScheduledEvent(double timeOfDay, Id<Request> requestId, Id<Carrier> carrierId, Id<DvrpVehicle> vehicleId) {
        super(timeOfDay);
        this.requestId = requestId;
        this.vehicleId = vehicleId;
        this.carrierId = carrierId;
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attr = super.getAttributes();
        attr.put(ATTRIBUTE_REQUEST, requestId + "");
        attr.put(ATTRIBUTE_CARRIER, carrierId + "");
        attr.put(ATTRIBUTE_VEHICLE, vehicleId + "");
        return attr;
    }

    public Id<Request> getRequestId() {
        return requestId;
    }

    public Id<DvrpVehicle> getVehicleId() {
        return vehicleId;
    }

    public Id<Carrier> getCarrierId() { return carrierId; }
}
