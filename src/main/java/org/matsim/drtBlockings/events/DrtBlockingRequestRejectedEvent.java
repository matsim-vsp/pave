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
import org.matsim.contrib.dvrp.optimizer.Request;

import java.util.Map;

public class DrtBlockingRequestRejectedEvent extends Event {

    public static final String EVENT_TYPE = "DrtBlockingRequest rejected";

    public static final String ATTRIBUTE_REQUEST_ID = "requestId";
    public static final String ATTRIBUTE_SUBMISSION_TIME = "submissionTime";
    private final Id<Request> requestId;
    private final double submissionTime;

    public DrtBlockingRequestRejectedEvent(double time, Id<Request> requestId, double submissionTime) {
        super(time);
        this.requestId = requestId;
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
        attr.put(ATTRIBUTE_SUBMISSION_TIME, submissionTime + "");
        return attr;
    }
}
