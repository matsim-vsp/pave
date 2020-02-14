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

package org.matsim.pfav.privateAV;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.Event;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;

import java.util.Map;

class EventFreightTourRequestRejected extends Event {

    static final String EVENT_TYPE = "rejectedFreightTourRequest";

    static final String ATTRIBUTE_VEHICLE = "vehicle";
    static final String ATTRIBUTE_REQUEST_LINK = "requestLink";
    static final String ATTRIBUTE_MUST_RETURN_LINK = "mustReturnLink";
    static final String ATTRIBUTE_MUST_RETURN_TIME = "mustReturnTime";

    private final Id<DvrpVehicle> vehicleId;
    private final Id<Link> requestLink;
    private final Id<Link> mustReturnLink;
    private final double mustReturnTime;


    EventFreightTourRequestRejected(PFAVehicle vehicle, Id<Link> requestLink, double timeOfDay) {
        super(timeOfDay);
        vehicleId = vehicle.getId();
        this.requestLink = requestLink;
        PFAVehicle.MustReturnLinkTimePair returnLog = vehicle.getMustReturnToOwnerLinkTimePairs().peek();
        this.mustReturnLink = returnLog.getLinkId();
        this.mustReturnTime = returnLog.getTime();
    }

    EventFreightTourRequestRejected(Id<DvrpVehicle> vehicle, Id<Link> requestLink, double timeOfDay, Id<Link> mustReturnLink, double mustReturnTime) {
        super(timeOfDay);
        this.vehicleId = vehicle;
        this.requestLink = requestLink;
        this.mustReturnLink = mustReturnLink;
        this.mustReturnTime = mustReturnTime;
    }

    /**
     * @return a unique, descriptive name for this event type, used to identify event types in files.
     */
    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attr = super.getAttributes();
        attr.put(ATTRIBUTE_VEHICLE, vehicleId + "");
        attr.put(ATTRIBUTE_REQUEST_LINK, requestLink + "");
//        attr.put(ATTRIBUTE_MUST_RETURN_LINK_TIME_PAIR, returnLog.toString());
        attr.put(ATTRIBUTE_MUST_RETURN_LINK, mustReturnLink + "");
        attr.put(ATTRIBUTE_MUST_RETURN_TIME, mustReturnTime + "");
        return attr;
    }

    Id<DvrpVehicle> getVehicleId() {
        return vehicleId;
    }

    Id<Link> getRequestLink() {
        return requestLink;
    }

    Id<Link> getMustReturnLink() {
        return mustReturnLink;
    }

    double getMustReturnTime() {
        return mustReturnTime;
    }
}
