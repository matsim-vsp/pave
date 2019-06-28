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

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.freight.carrier.TimeWindow;

import java.util.Objects;

public class PFAVTourDataDispatched {

    private final PFAVehicle.MustReturnLinkTimePair mustReturnLog;

    private final Id<DvrpVehicle> vehicleId;
    private PFAVTourDataPlanned tourData;
    private final double dispatchTime;
    private final Id<Link> requestLink;

    private int amountOfServicesHandled = 0;

    private final double plannedTourLength;
    private final double plannedEmptyMeters;
    private final double distanceToDepot;
    private final double distanceBackToOwner;

    private double actualTourDuration = Double.NEGATIVE_INFINITY;
    private double actualTourLength = Double.NEGATIVE_INFINITY;
    private double actualEmptyMeters = Double.NEGATIVE_INFINITY;
    private int actualServedCapacityDemand = Integer.MIN_VALUE;

    private double totalServiceDelay = 0;
    private double waitTimeAtDepot = 0;

    private PFAVTourDataDispatched(PFAVTourDataDispatched.Builder builder) {
        this.vehicleId = Objects.requireNonNull(builder.vehicleId);
        this.mustReturnLog = Objects.requireNonNull(builder.mustReturnLog);
        this.dispatchTime = builder.dispatchTime;
        this.requestLink = Objects.requireNonNull(builder.requestLink);
        this.plannedTourLength = builder.plannedTourLength;
        this.plannedEmptyMeters = builder.plannedEmptyMeters;
        this.distanceToDepot = builder.distanceToDepot;
        this.distanceBackToOwner = builder.distanceBackToOwner;
        this.tourData = builder.data;
    }

    public static PFAVTourDataDispatched.Builder newBuilder() {
        return new PFAVTourDataDispatched.Builder();
    }

    public void addToActualTourLength(double metersToAdd) {
        if (this.actualTourLength == Double.NEGATIVE_INFINITY) actualTourLength = metersToAdd;
        else this.actualTourLength += metersToAdd;
    }

    public void addToActualEmptyMeters(double metersToAdd) {
        if (this.actualEmptyMeters == Double.NEGATIVE_INFINITY) this.actualEmptyMeters = metersToAdd;
        else this.actualEmptyMeters += metersToAdd;
    }

    private void computeAndNoteDelay(TimeWindow timeWindow, double serviceStartTime) {
        if (timeWindow.getEnd() < serviceStartTime) {
            this.totalServiceDelay += serviceStartTime - timeWindow.getEnd();
        }
    }

    void addToActualServedCapacityDemand(int actualServedCapacityDemandToAdd) {
        if (this.actualServedCapacityDemand == Integer.MIN_VALUE) this.actualServedCapacityDemand = actualServedCapacityDemandToAdd;
        else this.actualServedCapacityDemand += actualServedCapacityDemandToAdd;
    }

    public void notifyNextServiceTaskStarted(double startTime) {
        for (int i = this.tourData.getTourTasks().size() - 1; i >= 0; i--) {
            StayTask task = this.tourData.getTourTasks().get(i);
            if (task instanceof PFAVServiceTask && (task.getStatus() == Task.TaskStatus.PERFORMED || task.getStatus() == Task.TaskStatus.STARTED)) {
                PFAVServiceTask serviceTask = (PFAVServiceTask) task;
                this.addToActualServedCapacityDemand(serviceTask.getCarrierService().getCapacityDemand());
                this.computeAndNoteDelay(serviceTask.getCarrierService().getServiceStartTimeWindow(), startTime);
                this.amountOfServicesHandled++;
                return;
            }
        }
    }

    public double getActualTourDuration() {
        return actualTourDuration;
    }

    public void setActualTourDuration(double actualTourDuration) {
        this.actualTourDuration = actualTourDuration;
    }

    public int getAmountOfServicesPlanned() {
        int services = 0;
        for (StayTask t : this.tourData.getTourTasks()) {
            if (t instanceof PFAVServiceTask) services++;
        }
        return services;
    }

    public double getActualTourLength() {
        return actualTourLength;
    }

    public double getPlannedEmptyMeters() {
        return plannedEmptyMeters;
    }

    public double getActualEmptyMeters() {
        return actualEmptyMeters;
    }

    public double getDistanceToDepot() {
        return distanceToDepot;
    }

    public double getDistanceBackToOwner() {
        return distanceBackToOwner;
    }

    public int getActualServedCapacityDemand() {
        return actualServedCapacityDemand;
    }

    public Id<Link> getDepotLinkId() {
        return tourData.getDepotLink().getId();
    }

    public double getPlannedTourDuration() {
        return tourData.getPlannedTourDuration();
    }

    public double getPlannedTourLength() {
        return plannedTourLength;
    }

    public int getPlannedTotalCapacityDemand() {
        return tourData.getPlannedTotalCapacityDemand();
    }

    public Id<DvrpVehicle> getVehicleId() {
        return vehicleId;
    }

    public double getDispatchTime() {
        return dispatchTime;
    }

    public Id<Link> getRequestLink() {
        return requestLink;
    }

    public PFAVehicle.MustReturnLinkTimePair getMustReturnLog() {
        return mustReturnLog;
    }

    public double getTotalServiceDelay() {
        return totalServiceDelay;
    }

    public int getAmountOfServicesHandled() {
        return amountOfServicesHandled;
    }

    public double getWaitTimeAtDepot() {
        return this.waitTimeAtDepot;
    }

    public void setWaitTimeAtDepot(double v) {
        this.waitTimeAtDepot = v;
    }


    public static final class Builder {
        private Id<DvrpVehicle> vehicleId;
        private PFAVehicle.MustReturnLinkTimePair mustReturnLog;
        private double dispatchTime;

        private Id<Link> requestLink;
        private double distanceToDepot;
        private double plannedEmptyMeters;

        //        private Id<Link> depotLink;
        //        private double plannedTourDuration;
        private double plannedTourLength;
        //private int plannedTotalCapacityDemand;
        private PFAVTourDataPlanned data;
        private double distanceBackToOwner;

        private Builder() {
        }

        public PFAVTourDataDispatched.Builder dispatchTime(double val) {
            dispatchTime = val;
            return this;
        }

//        public PFAVTourDataDispatched.Builder depotLink(Id<Link> val) {
//            depotLink = val;
//            return this;
//        }

        public PFAVTourDataDispatched.Builder requestLink(Id<Link> val) {
            requestLink = val;
            return this;
        }

        public PFAVTourDataDispatched.Builder tourData(PFAVTourDataPlanned val) {
            this.data = val;
            return this;
        }

        //        public PFAVTourDataDispatched.Builder plannedTourDuration(double val) {
//            plannedTourDuration = val;
//            return this;
//        }
//
        public PFAVTourDataDispatched.Builder plannedTourLength(double val) {
            plannedTourLength = val;
            return this;
        }

        public PFAVTourDataDispatched.Builder plannedEmptyMeters(double val) {
            plannedEmptyMeters = val;
            return this;
        }

        //
        public PFAVTourDataDispatched.Builder distanceToDepot(double val) {
            distanceToDepot = val;
            return this;
        }

        public PFAVTourDataDispatched.Builder distanceBackToOwner(double returnDistance) {
            distanceBackToOwner = returnDistance;
            return this;
        }

        public PFAVTourDataDispatched.Builder mustReturnLog(PFAVehicle.MustReturnLinkTimePair val) {
            mustReturnLog = val;
            return this;
        }

        public PFAVTourDataDispatched.Builder vehicleId(Id<DvrpVehicle> val) {
            vehicleId = val;
            return this;
        }

        public PFAVTourDataDispatched build() {
            return new PFAVTourDataDispatched(this);
        }
    }
}
