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

package org.matsim.pfav.privateAV;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.freight.carrier.TimeWindow;

import java.util.HashMap;
import java.util.Objects;

public final class FreightTourDataDispatched {

    private final PFAVehicle.MustReturnLinkTimePair mustReturnLog;

    private final Id<DvrpVehicle> vehicleId;
    private FreightTourDataPlanned tourData;
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
    private double totalServiceTime = 0;
    private double totalServiceWaitTime = 0;
    private HashMap<PFAVServiceTask, Double> currentServiceTaskStartTimes = new HashMap<>();

    private FreightTourDataDispatched(FreightTourDataDispatched.Builder builder) {
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

    static FreightTourDataDispatched.Builder newBuilder() {
        return new FreightTourDataDispatched.Builder();
    }

    void addToActualTourLength(double metersToAdd) {
        if (this.actualTourLength == Double.NEGATIVE_INFINITY) actualTourLength = metersToAdd;
        else this.actualTourLength += metersToAdd;
    }

    void addToActualEmptyMeters(double metersToAdd) {
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

    PFAVServiceTask getLastStartedServiceTask(){
        for (int i = this.tourData.getTourTasks().size() - 1; i >= 0; i--) {
            Task task = this.tourData.getTourTasks().get(i);
            if (task instanceof PFAVServiceTask && (task.getStatus() == Task.TaskStatus.PERFORMED || task.getStatus() == Task.TaskStatus.STARTED)) {
                return (PFAVServiceTask) task;
            }
        } return null;
    }

    void notifyNextServiceTaskStarted(PFAVServiceTask serviceTask, double startTime) {
        if(! this.tourData.getTourTasks().contains(serviceTask) || serviceTask.getStatus() == Task.TaskStatus.PLANNED) throw new IllegalStateException();
        this.addToActualServedCapacityDemand(serviceTask.getCarrierService().getCapacityDemand());
        this.computeAndNoteDelay(serviceTask.getCarrierService().getServiceStartTimeWindow(), startTime);
        this.amountOfServicesHandled++;

        if(this.currentServiceTaskStartTimes.get(serviceTask) != null) throw new IllegalStateException();
        this.currentServiceTaskStartTimes.put(serviceTask, startTime);
    }

    void addToTotalServiceTime(double time){
        this.totalServiceTime += time;
    }

    double getTotalServiceTime() {
        return totalServiceTime;
    }

    double getTotalServiceWaitTime() {
        return totalServiceWaitTime;
    }

    void addToTotalServiceWaitTime(double time){
        this.totalServiceWaitTime += time;
    }

    double getActualTourDuration() {
        return actualTourDuration;
    }

    void setActualTourDuration(double actualTourDuration) {
        this.actualTourDuration = actualTourDuration;
    }

    int getAmountOfServicesPlanned() {
        int services = 0;
        for (Task t : this.tourData.getTourTasks()) {
            if (t instanceof PFAVServiceTask) services++;
        }
        return services;
    }

    double getActualTourLength() {
        return actualTourLength;
    }

    double getPlannedEmptyMeters() {
        return plannedEmptyMeters;
    }

    double getActualEmptyMeters() {
        return actualEmptyMeters;
    }

    double getDistanceToDepot() {
        return distanceToDepot;
    }

    double getDistanceBackToOwner() {
        return distanceBackToOwner;
    }

    int getActualServedCapacityDemand() {
        return actualServedCapacityDemand;
    }

    public Id<Link> getDepotLinkId() {
        return tourData.getDepotLink().getId();
    }

    double getPlannedTourDuration() {
        return tourData.getPlannedTourDuration();
    }

    double getPlannedTourLength() {
        return plannedTourLength;
    }

    int getPlannedTotalCapacityDemand() {
        return tourData.getPlannedTotalCapacityDemand();
    }

    public Id<DvrpVehicle> getVehicleId() {
        return vehicleId;
    }

    double getDispatchTime() {
        return dispatchTime;
    }

    Id<Link> getRequestLink() {
        return requestLink;
    }

    PFAVehicle.MustReturnLinkTimePair getMustReturnLog() {
        return mustReturnLog;
    }

    double getTotalServiceDelay() {
        return totalServiceDelay;
    }

    int getAmountOfServicesHandled() {
        return amountOfServicesHandled;
    }

    double getWaitTimeAtDepot() {
        return this.waitTimeAtDepot;
    }

    void setWaitTimeAtDepot(double v) {
        this.waitTimeAtDepot = v;
    }


    static final class Builder {
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
        private FreightTourDataPlanned data;
        private double distanceBackToOwner;

        private Builder() {
        }

        FreightTourDataDispatched.Builder dispatchTime(double val) {
            dispatchTime = val;
            return this;
        }

//        public FreightTourDataDispatched.Builder depotLink(Id<Link> val) {
//            depotLink = val;
//            return this;
//        }

        FreightTourDataDispatched.Builder requestLink(Id<Link> val) {
            requestLink = val;
            return this;
        }

        FreightTourDataDispatched.Builder tourData(FreightTourDataPlanned val) {
            this.data = val;
            return this;
        }

        //        public FreightTourDataDispatched.Builder plannedTourDuration(double val) {
//            plannedTourDuration = val;
//            return this;
//        }
//
        FreightTourDataDispatched.Builder plannedTourLength(double val) {
            plannedTourLength = val;
            return this;
        }

        FreightTourDataDispatched.Builder plannedEmptyMeters(double val) {
            plannedEmptyMeters = val;
            return this;
        }

        FreightTourDataDispatched.Builder distanceToDepot(double val) {
            distanceToDepot = val;
            return this;
        }

        FreightTourDataDispatched.Builder distanceBackToOwner(double returnDistance) {
            distanceBackToOwner = returnDistance;
            return this;
        }

        FreightTourDataDispatched.Builder mustReturnLog(PFAVehicle.MustReturnLinkTimePair val) {
            mustReturnLog = val;
            return this;
        }

        FreightTourDataDispatched.Builder vehicleId(Id<DvrpVehicle> val) {
            vehicleId = val;
            return this;
        }

        FreightTourDataDispatched build() {
            return new FreightTourDataDispatched(this);
        }
    }
}
