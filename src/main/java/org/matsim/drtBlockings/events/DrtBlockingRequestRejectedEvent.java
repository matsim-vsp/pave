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
import org.matsim.api.core.v01.events.GenericEvent;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.freight.carrier.Carrier;

import java.util.Map;

public class DrtBlockingRequestRejectedEvent extends Event {

    public static final String EVENT_TYPE = "DrtBlockingRequest rejected";

    public static final String ATTRIBUTE_REQUEST_ID = "requestId";
    public static final String ATTRIBUTE_SUBMISSION_TIME = "submissionTime";
    public static final String ATTRIBUTE_CARRIER = "carrier";
    private final Id<Request> requestId;
    private final double submissionTime;
    private Id<Carrier> carrierId;

    public DrtBlockingRequestRejectedEvent(double time, Id<Request> requestId, Id<Carrier> carrierId, double submissionTime) {
        super(time);
        this.requestId = requestId;
        this.carrierId = carrierId;
        this.submissionTime = submissionTime;
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attr = super.getAttributes();
        attr.put(ATTRIBUTE_REQUEST_ID, requestId + "");
        attr.put(ATTRIBUTE_CARRIER, carrierId + "");
        attr.put(ATTRIBUTE_SUBMISSION_TIME, submissionTime + "");
        return attr;
    }

    public Id<Carrier> getCarrierId() { return carrierId; }

    public static DrtBlockingRequestRejectedEvent convert(GenericEvent event) {
        Map<String, String> attributes = event.getAttributes();
        double time = Double.parseDouble(attributes.get(ATTRIBUTE_TIME));
        Id<Request> requestId = Id.create(attributes.get(ATTRIBUTE_REQUEST_ID), Request.class);
        Id<Carrier> carrierId = Id.create(attributes.get(ATTRIBUTE_CARRIER), Carrier.class);
        double submissionTime = Double.parseDouble(attributes.get(ATTRIBUTE_SUBMISSION_TIME));

        return new DrtBlockingRequestRejectedEvent(time, requestId, carrierId, submissionTime);
    }

}
