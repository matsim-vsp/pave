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

import freight.tour.DispatchedPFAVTourData;
import org.matsim.api.core.v01.events.Event;

import java.util.Map;

public class FreightTourScheduledEvent extends Event {
    public static final String EVENT_TYPE = "freightTourScheduled";

    public static final String ATTRIBUTE_VEHICLE = "vehicle";
    public static final String ATTRIBUTE_REQUEST_LINK = "requestLink";
    public static final String ATTRIBUTE_FREIGHT_TOUR_DURATION = "freightTourDuration";
    public static final String ATTRIBUTE_FREIGHT_TOUR_DISTANCE = "freightTourDistance";
    public static final String ATTRIBUTE_MUST_RETURN_TIME = "mustReturnTime";

    private DispatchedPFAVTourData tourData;

    public FreightTourScheduledEvent(DispatchedPFAVTourData data) {
        super(data.getDispatchTime());
        this.tourData = data;
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
        attr.put(ATTRIBUTE_VEHICLE, tourData.getVehicleId() + "");
        attr.put(ATTRIBUTE_REQUEST_LINK, tourData.getRequestLink() + "");
        attr.put(ATTRIBUTE_FREIGHT_TOUR_DURATION, tourData.getPlannedTourDuration() + "");
        attr.put(ATTRIBUTE_FREIGHT_TOUR_DISTANCE, tourData.getPlannedTourLength() + "");
        attr.put(ATTRIBUTE_MUST_RETURN_TIME, tourData.getMustReturnLog() + "");
        return attr;
    }

    public DispatchedPFAVTourData getTourData() {
        return this.tourData;
    }

}
