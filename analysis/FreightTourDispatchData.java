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

package analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;

import java.util.Objects;

public class FreightTourDispatchData {

    private final Id<DvrpVehicle> vehicleId;
    private final double dispatchTime;

    private final Id<Link> depotLink;
    private final Id<Link> requestLink;

    private final double plannedTourDuration;
    private final double plannedTourLength;
    private final double plannedEmptyMeters;
    private final double distanceToDepot;
    private final int plannedTotalCapacityDemand;
    private double actualTourDuration = Double.NEGATIVE_INFINITY;
    private double actualTourLength = Double.NEGATIVE_INFINITY;
    private double actualEmptyMeters = Double.NEGATIVE_INFINITY;
    private int actualServedCapacityDemand = Integer.MIN_VALUE;

//    public FreightTourDispatchData(Id<DvrpVehicle> vehicleId, Id<Link> depotLink, Id<Link> requestLink, double dispatchTime, double plannedTourDuration, double plannedTourLength, double plannedEmptyMeters, double distanceToDepot, int plannedTotalCapacityDemand) {
//        this.vehicleId = vehicleId;
//        this.depotLink = depotLink;
//        this.dispatchTime = dispatchTime;
//        this.requestLink = requestLink;
//        this.plannedTourDuration = plannedTourDuration;
//        this.plannedTourLength = plannedTourLength;
//        this.plannedEmptyMeters = plannedEmptyMeters;
//        this.distanceToDepot = distanceToDepot;
//        this.plannedTotalCapacityDemand = plannedTotalCapacityDemand;
//    }

    private FreightTourDispatchData(FreightTourDispatchData.Builder builder) {
        this.vehicleId = Objects.requireNonNull(builder.vehicleId);
        this.depotLink = Objects.requireNonNull(builder.depotLink);
        this.dispatchTime = builder.dispatchTime;
        this.requestLink = Objects.requireNonNull(builder.requestLink);
        this.plannedTourDuration = builder.plannedTourDuration;
        this.plannedTourLength = builder.plannedTourLength;
        this.plannedEmptyMeters = builder.plannedEmptyMeters;
        this.distanceToDepot = builder.distanceToDepot;
        this.plannedTotalCapacityDemand = builder.plannedTotalCapacityDemand;
    }

    public static FreightTourDispatchData.Builder newBuilder() {
        return new FreightTourDispatchData.Builder();
    }

    void addToActualTourLength(double metersToAdd) {
        if (this.actualTourLength == Double.NEGATIVE_INFINITY) actualTourLength = metersToAdd;
        else this.actualTourLength += metersToAdd;
    }

    void addToActualEmptyMeters(double metersToAdd) {
        if (this.actualEmptyMeters == Double.NEGATIVE_INFINITY) this.actualEmptyMeters = metersToAdd;
        else this.actualEmptyMeters += metersToAdd;
    }

    void addToActualServedCapacityDemand(int actualServedCapacityDemandToAdd) {
        if (this.actualServedCapacityDemand == Integer.MIN_VALUE) this.actualServedCapacityDemand = actualServedCapacityDemandToAdd;
        else this.actualServedCapacityDemand += actualServedCapacityDemandToAdd;
    }

    double getPlannedTourDuration() {
        return plannedTourDuration;
    }

    double getActualTourDuration() {
        return actualTourDuration;
    }

    void setActualTourDuration(double actualTourDuration) {
        this.actualTourDuration = actualTourDuration;
    }

    double getPlannedTourLength() {
        return plannedTourLength;
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

    int getPlannedTotalCapacityDemand() {
        return plannedTotalCapacityDemand;
    }

    int getActualServedCapacityDemand() {
        return actualServedCapacityDemand;
    }

    public Id<DvrpVehicle> getVehicleId() {
        return vehicleId;
    }

    double getDispatchTime() {
        return dispatchTime;
    }

    Id<Link> getDepotLink() {
        return depotLink;
    }

    Id<Link> getRequestLink() {
        return requestLink;
    }

    public static final class Builder {
        private Id<DvrpVehicle> vehicleId;
        private double dispatchTime;

        private Id<Link> depotLink;
        private Id<Link> requestLink;

        private double plannedTourDuration;
        private double plannedTourLength;
        private double plannedEmptyMeters;
        private double distanceToDepot;
        private int plannedTotalCapacityDemand;

        private Builder() {
        }

        public FreightTourDispatchData.Builder vehicleId(Id<DvrpVehicle> val) {
            vehicleId = val;
            return this;
        }

        public FreightTourDispatchData.Builder dispatchTime(double val) {
            dispatchTime = val;
            return this;
        }

        public FreightTourDispatchData.Builder depotLink(Id<Link> val) {
            depotLink = val;
            return this;
        }

        public FreightTourDispatchData.Builder requestLink(Id<Link> val) {
            requestLink = val;
            return this;
        }

        public FreightTourDispatchData.Builder plannedTourDuration(double val) {
            plannedTourDuration = val;
            return this;
        }

        public FreightTourDispatchData.Builder plannedTourLength(double val) {
            plannedTourLength = val;
            return this;
        }

        public FreightTourDispatchData.Builder plannedEmptyMeters(double val) {
            plannedEmptyMeters = val;
            return this;
        }

        public FreightTourDispatchData.Builder distanceToDepot(double val) {
            distanceToDepot = val;
            return this;
        }

        public FreightTourDispatchData.Builder plannedTotalCapacityDemand(int val) {
            plannedTotalCapacityDemand = val;
            return this;
        }

        public FreightTourDispatchData build() {
            return new FreightTourDispatchData(this);
        }
    }
}
