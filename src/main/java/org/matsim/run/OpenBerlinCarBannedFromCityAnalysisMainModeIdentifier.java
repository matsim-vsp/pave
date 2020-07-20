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

package org.matsim.run;

import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.core.router.AnalysisMainModeIdentifier;
import org.matsim.run.drt.OpenBerlinIntermodalPtDrtRouterAnalysisModeIdentifier;

import java.util.List;

import static org.matsim.run.RunBerlinCarBannedFromCityScenarioWithMobilityTypesAndDrtSpeedUp.DRT_ACCESS_DRT_WALK_MODE;
import static org.matsim.run.RunBerlinCarBannedFromCityScenarioWithMobilityTypesAndDrtSpeedUp.WALK_ACCESS_DRT_EGRESS_MODE;

final class OpenBerlinCarBannedFromCityAnalysisMainModeIdentifier implements AnalysisMainModeIdentifier {

	private final OpenBerlinIntermodalPtDrtRouterAnalysisModeIdentifier delegate  = new OpenBerlinIntermodalPtDrtRouterAnalysisModeIdentifier();

	@Override
	public String identifyMainMode(List<? extends PlanElement> tripElements) {
		try {
			return delegate.identifyMainMode(tripElements);
		} catch (RuntimeException exception) {
			for (PlanElement pe : tripElements) {
				String mode;
				if (pe instanceof Leg) {
					Leg leg = (Leg) pe;
					mode = leg.getMode();
					if (mode.equals(WALK_ACCESS_DRT_EGRESS_MODE) || mode.equals(DRT_ACCESS_DRT_WALK_MODE)) return "intermodalCar" /*TransportMode.car*/ ;
				} else {
					continue;
				}
			}
		}
		throw new RuntimeException("could not identify main mode for trip " + tripElements.toString());
	}
}
