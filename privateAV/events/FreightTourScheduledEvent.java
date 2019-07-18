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

package privateAV.events;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.Event;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import privateAV.FreightTourDataDispatched;

import java.util.Map;

public class FreightTourScheduledEvent extends Event {
    static final String EVENT_TYPE = "freightTourScheduled";

    public static final String ATTRIBUTE_VEHICLE = "vehicle";
    static final String ATTRIBUTE_REQUEST_LINK = "requestLink";
    static final String ATTRIBUTE_FREIGHT_TOUR_DURATION = "freightTourDuration";
    static final String ATTRIBUTE_FREIGHT_TOUR_DISTANCE = "freightTourDistance";
    static final String ATTRIBUTE_MUST_RETURN_TIME = "mustReturnTime";

    private final Id<DvrpVehicle> vehicleId;
    private final Id<Link> requestLink;
    private final double mustReturnTime;
    private final double tourDuration;
    private final double tourLength;

    private FreightTourDataDispatched tourData = null;

    public FreightTourScheduledEvent(double time, Id<DvrpVehicle> vehicleId, Id<Link> requestLink, double mustReturnTime, double tourDuration, double tourLength) {
        super(time);
        this.vehicleId = vehicleId;
        this.requestLink = requestLink;
        this.mustReturnTime = mustReturnTime;
        this.tourDuration = tourDuration;
        this.tourLength = tourLength;
    }

    public FreightTourScheduledEvent(FreightTourDataDispatched data) {
        super(data.getDispatchTime());
        this.tourData = data;
        this.vehicleId = data.getVehicleId();
        this.requestLink = data.getRequestLink();
        this.tourDuration = data.getPlannedTourDuration();
        this.tourLength = data.getPlannedTourLength();
        this.mustReturnTime = data.getMustReturnLog().getTime();
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
        attr.put(ATTRIBUTE_FREIGHT_TOUR_DURATION, tourDuration + "");
        attr.put(ATTRIBUTE_FREIGHT_TOUR_DISTANCE, tourLength + "");
        attr.put(ATTRIBUTE_MUST_RETURN_TIME, mustReturnTime + "");
        return attr;
    }

    public FreightTourDataDispatched getTourData() {
        return this.tourData;
    }

}
