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
import org.matsim.contrib.dvrp.schedule.DriveTaskImpl;
import org.matsim.contrib.taxi.schedule.TaxiTask;

import java.util.List;
import java.util.Objects;

public final class FreightTourDataPlanned {

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

    public void setPlannedTourDuration(Double plannedTourDuration) {
        this.plannedTourDuration = plannedTourDuration;
    }

    public double getPlannedTourDuration() {
        return plannedTourDuration;
    }

    List<TaxiTask> getTourTasks() {
        return tourTasks;
    }

    public int getPlannedTotalCapacityDemand() {
        return plannedTotalCapacityDemand;
    }

    public Link getDepotLink() {
        return depotLink;
    }

    DriveTask getAccessDriveTask() {
        return this.accessDriveTask;
    }

    public int getAmountOfRejections() {
        return amountOfRejections;
    }

    void setAccessDriveTask(DriveTaskImpl driveTask) {
        this.accessDriveTask = driveTask;
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
