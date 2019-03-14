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

//TODO: write a builder
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

    public FreightTourDispatchData(Id<DvrpVehicle> vehicleId, Id<Link> depotLink, Id<Link> requestLink, double dispatchTime, double plannedTourDuration, double plannedTourLength, double plannedEmptyMeters, double distanceToDepot, int plannedTotalCapacityDemand) {
        this.vehicleId = vehicleId;
        this.depotLink = depotLink;
        this.dispatchTime = dispatchTime;
        this.requestLink = requestLink;
        this.plannedTourDuration = plannedTourDuration;
        this.plannedTourLength = plannedTourLength;
        this.plannedEmptyMeters = plannedEmptyMeters;
        this.distanceToDepot = distanceToDepot;
        this.plannedTotalCapacityDemand = plannedTotalCapacityDemand;
    }

    public void addToActualTourLength(double metersToAdd) {
        if (this.actualTourLength == Double.NEGATIVE_INFINITY) actualTourLength = metersToAdd;
        else this.actualTourLength += metersToAdd;
    }

    public void addToActualEmptyMeters(double metersToAdd) {
        if (this.actualEmptyMeters == Double.NEGATIVE_INFINITY) this.actualEmptyMeters = metersToAdd;
        else this.actualEmptyMeters += metersToAdd;
    }

    public void addToActualServedCapacityDemand(int actualServedCapacityDemandToAdd) {
        if (this.actualServedCapacityDemand == Integer.MIN_VALUE) this.actualServedCapacityDemand = actualServedCapacityDemandToAdd;
        else this.actualServedCapacityDemand += actualServedCapacityDemandToAdd;
    }

    public double getPlannedTourDuration() {
        return plannedTourDuration;
    }

    public double getActualTourDuration() {
        return actualTourDuration;
    }

    public void setActualTourDuration(double actualTourDuration) {
        this.actualTourDuration = actualTourDuration;
    }

    public double getPlannedTourLength() {
        return plannedTourLength;
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

    public int getPlannedTotalCapacityDemand() {
        return plannedTotalCapacityDemand;
    }

    public int getActualServedCapacityDemand() {
        return actualServedCapacityDemand;
    }

    public Id<DvrpVehicle> getVehicleId() {
        return vehicleId;
    }

    public double getDispatchTime() {
        return dispatchTime;
    }

    public Id<Link> getDepotLink() {
        return depotLink;
    }

    public Id<Link> getRequestLink() {
        return requestLink;
    }
}
