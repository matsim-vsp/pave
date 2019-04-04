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

package privateAV.vehicle;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;

/**
 * i know there is {@link org.matsim.contrib.dvrp.util.LinkTimePair} already, but for the reference
 * to must return location in the PFAVehicle, we can not work with Link but only with Id<Link> since the
 * network cannot be injected into PFAVFleetStatsCalculator (it's logic is run before mobsim).
 * Furthermore, to be sure
 */
public class MustReturnLinkTimePair implements Comparable<MustReturnLinkTimePair> {

    final double time;
    final Id<Link> linkId;

    MustReturnLinkTimePair(double time, Id<Link> linkId) {
        this.time = time;
        this.linkId = linkId;
    }

    public double getTime() {
        return time;
    }

    public Id<Link> getLinkId() {
        return linkId;
    }

    @Override
    public int compareTo(MustReturnLinkTimePair other) {
        return Double.compare(time, other.time);
    }

    @Override
    public String toString() {
        return "[link=" + linkId + "][time=" + time + "]";
    }
}
