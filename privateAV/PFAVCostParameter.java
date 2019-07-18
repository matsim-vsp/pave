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

final class PFAVCostParameter {

    private double beta_perDay;
    private double beta_perMeter;
    private double beta_perSecond;

    PFAVCostParameter(double beta_perDay, double beta_perMeter, double beta_perSecond) {
        this.beta_perDay = beta_perDay;
        this.beta_perMeter = beta_perMeter;
        this.beta_perSecond = beta_perSecond;
    }

    double getBeta_perSecond() {
        return beta_perSecond;
    }

    double getBeta_perMeter() {
        return beta_perMeter;
    }

    public double getBeta_perDay() {
        return beta_perDay;
    }

}
