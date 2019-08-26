/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
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

import com.google.inject.Singleton;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.core.router.util.LeastCostPathCalculator;

import java.util.List;

/**
 * @author tschlenther
 */
@Singleton
public interface FreightTourManagerListBased {
    List<FreightTourDataPlanned> getPFAVTours();

    FreightTourDataPlanned getRandomPFAVTour();

    FreightTourDataPlanned vehicleRequestedFreightTour(PFAVehicle vehicle, LeastCostPathCalculator router);

    FreightTourDataPlanned vehicleRequestedFreightTourAtDepot(PFAVehicle vehicle, Link depotLink, LeastCostPathCalculator router);

    FreightTourDataPlanned vehicleRequestedFreightTourExcludingDepot(PFAVehicle vehicle, Link depotLink, LeastCostPathCalculator router);

    boolean isEnoughTimeLeftToPerformFreightTour(PFAVehicle vehicle, VrpPathWithTravelData pathFromCurrTaskToDepot,
                                                 double waitTimeAtDepot, FreightTourDataPlanned freightTour, LeastCostPathCalculator router);
}
