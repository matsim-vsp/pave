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
import org.matsim.contrib.dvrp.schedule.StayTask;
import privateAV.vehicle.PFAVehicle;

import java.util.List;
import java.util.Map;

public class FreightTourDispatchedEvent extends Event {
    public static final String EVENT_TYPE = "freightTourDispatched";

    public static final String ATTRIBUTE_VEHICLE = "vehicle";
    public static final String ATTRIBUTE_REQUEST_LINK = "requestLink";
    public static final String ATTRIBUTE_FREIGHT_TOUR_DURATION = "freightTourDuration";
    public static final String ATTRIBUTE_MUST_RETURN_TIME = "mustReturnTime";

    private final Id<DvrpVehicle> vehicleId;
    private final Link requestLink;
    private final double freightTourDuration;
    private final double mustReturnTime;


    public FreightTourDispatchedEvent(PFAVehicle vehicle, Link requestLink, List<StayTask> freightTour, double timeOfDay) {
        super(timeOfDay);
        vehicleId = vehicle.getId();
        this.requestLink = requestLink;

        StayTask start = freightTour.get(0);
        StayTask end = freightTour.get(freightTour.size() - 1);
        this.freightTourDuration = end.getEndTime() - start.getBeginTime();
        this.mustReturnTime = vehicle.getOwnerActEndTimes().peek();
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
        attr.put(ATTRIBUTE_FREIGHT_TOUR_DURATION, freightTourDuration + "");
        attr.put(ATTRIBUTE_MUST_RETURN_TIME, mustReturnTime + "");
        return attr;
    }
}
