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

package org.matsim.drt.events;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.Event;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.drt.DrtBlockingRequest;

import java.util.Map;

public class DrtBlockingRequestRejectedEvent extends Event {

    public static final String EVENT_TYPE = "BlockingRequest rejected";

    public static final String ATTRIBUTE_REQUEST = "request";
    public static final String ATTRIBUTE_SUBMISSION_TIME = "submissionTime";
    private final Id<Request> requestId;
    private final double submissionTime;

    public DrtBlockingRequestRejectedEvent(double time, DrtBlockingRequest request) {
        super(time);
        this.requestId = request.getId();
        this.submissionTime = request.getSubmissionTime();
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attr = super.getAttributes();
        attr.put(ATTRIBUTE_REQUEST, requestId + "");
        attr.put(ATTRIBUTE_SUBMISSION_TIME, submissionTime + "");
        return attr;
    }
}
