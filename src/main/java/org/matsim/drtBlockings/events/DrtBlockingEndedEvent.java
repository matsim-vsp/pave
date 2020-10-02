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

package org.matsim.drtBlockings.events;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.Event;
import org.matsim.api.core.v01.events.GenericEvent;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;

import java.util.Map;

public class DrtBlockingEndedEvent extends Event {

    public static final String EVENT_TYPE = "DrtBlocking ended";
    public static final String ATTRIBUTE_VEHICLE_ID = "vehicleId";
    public static final String ATTRIBUTE_LINK_ID = "linkId";
    private final Id<DvrpVehicle> vehicleId;
    private final Id<Link> linkId;

    public DrtBlockingEndedEvent(double timeOfDay, Id<DvrpVehicle> vehicleId, Id<Link> linkId) {
        super(timeOfDay);
        this.vehicleId = vehicleId;
        this.linkId = linkId;
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attr = super.getAttributes();
        attr.put(ATTRIBUTE_VEHICLE_ID, vehicleId + "");
        attr.put(ATTRIBUTE_LINK_ID, linkId + "");
        return attr;
    }

    public Id<DvrpVehicle> getVehicleId() {
        return vehicleId;
    }

    public Id<Link> getLinkId() { return linkId; }

    public static DrtBlockingEndedEvent convert(GenericEvent event) {
        Map<String, String> attributes = event.getAttributes();
        double time = Double.parseDouble(attributes.get(ATTRIBUTE_TIME));
//        String type = Objects.requireNonNull(attributes.get(ATTRIBUTE_TYPE));

        Id<DvrpVehicle> vehicleId = Id.create(attributes.get(ATTRIBUTE_VEHICLE_ID), DvrpVehicle.class);
        Id<Link> linkId = Id.createLinkId(ATTRIBUTE_LINK_ID);

        //I'd like to save the x and y coords as coord but dont know how to convert them from string to coord, HOW?
//        String yCoord = Objects.requireNonNull(attributes.get(ATTRIBUTE_Y));
//        String xCoord = Objects.requireNonNull(attributes.get(ATTRIBUTE_X));
//        return new DrtBlockingEndedEvent1(time, type, vehicleId, yCoord, xCoord);
        return new DrtBlockingEndedEvent(time, vehicleId, linkId);

    }

}
