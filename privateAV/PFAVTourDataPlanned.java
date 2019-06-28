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

package privateAV;

import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.schedule.StayTask;

import java.util.List;
import java.util.Objects;

public class PFAVTourDataPlanned {

    private final Link depotLink;
    private final int plannedTotalCapacityDemand;
    private final double travelTimeToLastService;

    private Double plannedTourDuration;

    private int amountOfRejections = 0;

    private List<StayTask> tourTasks;

    public PFAVTourDataPlanned(List<StayTask> tourTasks, Link depotLinkId, double plannedTourDuration, double travelTimeToLastService, int plannedTotalCapacityDemand) {
        this.tourTasks = Objects.requireNonNull(tourTasks);
        this.depotLink = Objects.requireNonNull(depotLinkId);
        this.plannedTourDuration = plannedTourDuration;
        this.travelTimeToLastService = travelTimeToLastService;
        this.plannedTotalCapacityDemand = plannedTotalCapacityDemand;
    }

    public double getPlannedTourDuration() {
        return plannedTourDuration;
    }

    public void setPlannedTourDuration(Double plannedTourDuration) {
        this.plannedTourDuration = plannedTourDuration;
    }

    public int getPlannedTotalCapacityDemand() {
        return plannedTotalCapacityDemand;
    }

    public Link getDepotLink() {
        return depotLink;
    }

    public List<StayTask> getTourTasks() {
        return tourTasks;
    }

    public int getAmountOfRejections() {
        return amountOfRejections;
    }

    public void incrementAmountOfRejections() {
        this.amountOfRejections++;
    }

    public double getLatestArrivalAtLastService() {
        if (!(tourTasks.get(tourTasks.size() - 2) instanceof PFAVServiceTask) || !(tourTasks.get(tourTasks.size() - 1) instanceof PFAVRetoolTask)) {
            throw new IllegalStateException();
        }
        return ((PFAVServiceTask) tourTasks.get(tourTasks.size() - 2)).getCarrierService().getServiceStartTimeWindow().getEnd();
    }

    public double getTravelTimeToLastService() {
        return this.travelTimeToLastService;
    }

}
