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
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.router.TripStructureUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectDRTUsers {


//	private static final String INPUT_POPULATION = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/1pct/noIncDRT.output_plans.xml.gz";
	//	private static final String INPUT_CONFIG = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/1pct/noIncDRT.output_config.xml";
	private static final String INPUT_POPULATION = "C:/Users/Tilmann/tubCloud/VSP_WiMi/MA-Meinhardt/10pct/p2-23/p2-23.output_plans.xml.gz";
	private static final String INPUT_CONFIG = "C:/Users/Tilmann/tubCloud/VSP_WiMi/MA-Meinhardt/10pct/p2-23/p2-23.output_config.xml";

	private static final String OUTPUT_DRTPOP = "C:/Users/Tilmann/tubCloud/VSP_WiMi/MA-Meinhardt/10pct/p2-23/p2-23.output_plans_drtUsersOnly.xml.gz";


	public static void main(String[] args) {
		Population originalPop = PopulationUtils.readPopulation(INPUT_POPULATION);
		Config config = ConfigUtils.loadConfig(INPUT_CONFIG);
		List<Id<Person>> personsToDelete = new ArrayList<>();
		originalPop.getPersons().values().stream()
				.map(HasPlansAndId::getSelectedPlan)
				.forEach(plan -> {
					boolean hasDrtLeg = TripStructureUtils.getLegs(plan).stream()
							.filter(leg -> leg.getMode().equals("drt")).findAny().isPresent();
					if(!hasDrtLeg) personsToDelete.add(plan.getPerson().getId());
				});

		personsToDelete.forEach(personId -> originalPop.getPersons().remove(personId));
		PopulationUtils.writePopulation(originalPop, OUTPUT_DRTPOP);
	}
}
