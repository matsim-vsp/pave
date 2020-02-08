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
import org.matsim.contrib.dvrp.schedule.DriveTask;
import org.matsim.contrib.taxi.schedule.TaxiTask;

import java.util.List;
import java.util.Objects;

final class FreightTourDataPlanned {

    private List<TaxiTask> tourTasks;
    private DriveTask accessDriveTask = null;

    private final Link depotLink;
    private final int plannedTotalCapacityDemand;
    private final double travelTimeToLastService;

    private Double plannedTourDuration;
    private int amountOfRejections = 0;


    FreightTourDataPlanned(List<TaxiTask> tourTasks, Link depotLinkId, double plannedTourDuration, double travelTimeToLastService, int plannedTotalCapacityDemand) {
        this.tourTasks = Objects.requireNonNull(tourTasks);
        this.depotLink = Objects.requireNonNull(depotLinkId);
        this.plannedTourDuration = plannedTourDuration;
        this.travelTimeToLastService = travelTimeToLastService;
        this.plannedTotalCapacityDemand = plannedTotalCapacityDemand;
    }

    double getPlannedTourDuration() {
        return plannedTourDuration;
    }

    void setPlannedTourDuration(Double plannedTourDuration) {
        this.plannedTourDuration = plannedTourDuration;
    }

    List<TaxiTask> getTourTasks() {
        return tourTasks;
    }

    int getPlannedTotalCapacityDemand() {
        return plannedTotalCapacityDemand;
    }

    Link getDepotLink() {
        return depotLink;
    }

    DriveTask getAccessDriveTask() {
        return this.accessDriveTask;
    }

    void setAccessDriveTask(DriveTask driveTask) {
        this.accessDriveTask = driveTask;
    }

    int getAmountOfRejections() {
        return amountOfRejections;
    }

    void incrementAmountOfRejections() {
        this.amountOfRejections++;
    }

    double getLatestArrivalAtLastService() {
        if (!(tourTasks.get(tourTasks.size() - 3) instanceof PFAVServiceTask) || !(tourTasks.get(tourTasks.size() - 1) instanceof PFAVRetoolTask)) {
            throw new IllegalStateException();
        }
        return ((PFAVServiceTask) tourTasks.get(tourTasks.size() - 3)).getCarrierService().getServiceStartTimeWindow().getEnd();
    }

    double getTravelTimeToLastService() {
        return this.travelTimeToLastService;
    }
}
