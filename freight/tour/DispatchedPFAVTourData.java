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

package freight.tour;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.freight.carrier.TimeWindow;
import privateAV.schedule.PFAVServiceTask;
import privateAV.vehicle.MustReturnLinkTimePair;
import privateAV.vehicle.PFAVehicle;

import java.util.Objects;

public class DispatchedPFAVTourData {

    private final MustReturnLinkTimePair mustReturnLog;

    private final Id<DvrpVehicle> vehicleId;
    private PFAVTourData tourData;
    private final double dispatchTime;
    private final Id<Link> requestLink;

    private final double plannedTourLength;
    private final double plannedEmptyMeters;
    private final double distanceToDepot;
    private final double distanceBackToOwner;

    private double actualTourDuration = Double.NEGATIVE_INFINITY;
    private double actualTourLength = Double.NEGATIVE_INFINITY;
    private double actualEmptyMeters = Double.NEGATIVE_INFINITY;
    private int actualServedCapacityDemand = Integer.MIN_VALUE;

    private double totalServiceDelay = 0;

    private DispatchedPFAVTourData(DispatchedPFAVTourData.Builder builder) {
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

    public static DispatchedPFAVTourData.Builder newBuilder() {
        return new DispatchedPFAVTourData.Builder();
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

    public void addToActualServedCapacityDemand(int actualServedCapacityDemandToAdd) {
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

    public Link getDepotLink() {
        return tourData.getDepotLink();
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

    public MustReturnLinkTimePair getMustReturnLog() {
        return mustReturnLog;
    }

    public double getTotalServiceDelay() {
        return totalServiceDelay;
    }


    public static final class Builder {
        private Id<DvrpVehicle> vehicleId;
        private MustReturnLinkTimePair mustReturnLog;
        private double dispatchTime;

        private Id<Link> requestLink;
        private double distanceToDepot;
        private double plannedEmptyMeters;

        //        private Id<Link> depotLink;
        //        private double plannedTourDuration;
        private double plannedTourLength;
        //private int plannedTotalCapacityDemand;
        private PFAVTourData data;
        private double distanceBackToOwner;

        private Builder() {
        }

        public DispatchedPFAVTourData.Builder vehicle(PFAVehicle val) {
            vehicleId = val.getId();
            mustReturnLog = val.getMustReturnToOwnerLinkTimePairs().peek();
            return this;
        }

        public DispatchedPFAVTourData.Builder dispatchTime(double val) {
            dispatchTime = val;
            return this;
        }

//        public DispatchedPFAVTourData.Builder depotLink(Id<Link> val) {
//            depotLink = val;
//            return this;
//        }

        public DispatchedPFAVTourData.Builder requestLink(Id<Link> val) {
            requestLink = val;
            return this;
        }

        public DispatchedPFAVTourData.Builder tourData(PFAVTourData val) {
            this.data = val;
            return this;
        }

        //        public DispatchedPFAVTourData.Builder plannedTourDuration(double val) {
//            plannedTourDuration = val;
//            return this;
//        }
//
        public DispatchedPFAVTourData.Builder plannedTourLength(double val) {
            plannedTourLength = val;
            return this;
        }

        public DispatchedPFAVTourData.Builder plannedEmptyMeters(double val) {
            plannedEmptyMeters = val;
            return this;
        }

        //
        public DispatchedPFAVTourData.Builder distanceToDepot(double val) {
            distanceToDepot = val;
            return this;
        }

        public DispatchedPFAVTourData.Builder distanceBackToOwner(double returnDistance) {
            distanceBackToOwner = returnDistance;
            return this;
        }

        public DispatchedPFAVTourData build() {
            return new DispatchedPFAVTourData(this);
        }
    }
}
