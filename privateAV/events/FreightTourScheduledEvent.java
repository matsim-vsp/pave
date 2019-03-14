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
import org.matsim.contrib.dvrp.schedule.DriveTaskImpl;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.dvrp.schedule.Task;
import privateAV.vehicle.PFAVehicle;

import java.util.List;
import java.util.Map;

public class FreightTourScheduledEvent extends Event {
    public static final String EVENT_TYPE = "freightTourScheduled";

    public static final String ATTRIBUTE_VEHICLE = "vehicle";
    public static final String ATTRIBUTE_REQUEST_LINK = "requestLink";
    public static final String ATTRIBUTE_FREIGHT_TOUR_DURATION = "freightTourDuration";
    public static final String ATTRIBUTE_FREIGHT_TOUR_DISTANCE = "freightTourDistance";
    public static final String ATTRIBUTE_MUST_RETURN_TIME = "mustReturnTime";

    private final Id<DvrpVehicle> vehicleId;
    private final Id<Link> requestLink;
    private final double freightTourDuration;
    private final double mustReturnTime;
    private final double freightTourDistance;
    private final List<Task> freightTour;

    public FreightTourScheduledEvent(PFAVehicle vehicle, double timeOfDay, Id<Link> requestLink, double duration, double totalDistance, List<Task> freightTour) {
        super(timeOfDay);
        vehicleId = vehicle.getId();
        this.requestLink = requestLink;
        this.mustReturnTime = vehicle.getOwnerActEndTimes().peek();

        this.freightTourDuration = duration;

        this.freightTourDistance = totalDistance;
        this.freightTour = freightTour;
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
        attr.put(ATTRIBUTE_FREIGHT_TOUR_DISTANCE, freightTourDistance + "");
        attr.put(ATTRIBUTE_MUST_RETURN_TIME, mustReturnTime + "");
        return attr;
    }

    public Id<DvrpVehicle> getVehicleId() {
        return vehicleId;
    }

    public Id<Link> getRequestLink() {
        return requestLink;
    }

    public double getFreightTourDuration() {
        return freightTourDuration;
    }

    public double getMustReturnTime() {
        return mustReturnTime;
    }

    public double getFreightTourDistance() {
        return freightTourDistance;
    }

    public List<Task> getFreightTour() {
        return freightTour;
    }
}
