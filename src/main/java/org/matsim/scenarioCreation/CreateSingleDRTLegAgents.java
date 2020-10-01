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

package org.matsim.scenarioCreation;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.router.TripStructureUtils;

import java.util.List;

public class CreateSingleDRTLegAgents {


	private static final String INPUT_POPULATION = "C:/Users/Tilmann/tubCloud/VSP_WiMi/MA-Meinhardt/berlin-v5.5-10pct/output/blckBase1.output_plans.xml.gz";
	private static final String INPUT_CONFIG = "scenarios/berlin-v5.5-10pct/input/drtBlocking/blckBase1.output_config.xml";
	private static final String OUTPUT_DRTPOP = "scenarios/berlin-v5.5-10pct/input/drtBlocking/blckBase1.output_plans_drtOnly_splitAgents.xml.gz";


	public static void main(String[] args) {

		Population originalPop = PopulationUtils.readPopulation(INPUT_POPULATION);
		Population drtPop = PopulationUtils.createPopulation(ConfigUtils.loadConfig(INPUT_CONFIG));
		PopulationFactory fac = drtPop.getFactory();
		originalPop.getPersons().values().parallelStream()
				.map(person -> person.getSelectedPlan())
				.forEach(plan -> {
					List<TripStructureUtils.Trip> trips = TripStructureUtils.getTrips(plan);
					int cnt = 1;
					for (TripStructureUtils.Trip trip : trips) {
						if(TripStructureUtils.getRoutingMode(trip.getLegsOnly().get(0)).equals("drt")){
							Person copy = fac.createPerson(Id.createPersonId(plan.getPerson().getId().toString() + "_" + cnt));
							Plan drtPlan = fac.createPlan();
							drtPlan.addActivity(trip.getOriginActivity());
							drtPlan.addLeg(fac.createLeg("drt"));
							Activity dest = trip.getDestinationActivity();
							dest.setEndTimeUndefined();
							drtPlan.addActivity(dest);
							copy.addPlan(drtPlan);
							copy.setSelectedPlan(drtPlan);
							drtPop.addPerson(copy);
							cnt ++;
						}
					}
				});
		PopulationUtils.writePopulation(drtPop, OUTPUT_DRTPOP);
	}

}
