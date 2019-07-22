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
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;
import org.matsim.vehicles.Vehicle;

/**
 *
 */
class VehTypeVariableTravelDisutility implements TravelDisutility {

    private PFAVCostParameter costParameters;

    private TravelTime travelTime;

    //
    VehTypeVariableTravelDisutility(TravelTime travelTime) {
        this.travelTime = travelTime;
    }

    VehTypeVariableTravelDisutility(TravelTime travelTime, PFAVCostParameter costParameters) {
        this.travelTime = travelTime;
        this.costParameters = costParameters;
    }

    @Override
    public double getLinkTravelDisutility(Link link, double time, Person person, Vehicle vehicle) {
        double tt = this.travelTime.getLinkTravelTime(link, time, person, vehicle);
        return costParameters.getBeta_perMeter() * link.getLength() + costParameters.getBeta_perSecond() * tt;
    }

    @Override
    public double getLinkMinimumTravelDisutility(Link link) {
        double free_tt = link.getLength() / link.getFreespeed();
        return costParameters.getBeta_perMeter() * link.getLength() + costParameters.getBeta_perSecond() * free_tt;
    }

}
