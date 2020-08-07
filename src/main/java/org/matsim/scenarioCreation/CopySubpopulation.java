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

import org.matsim.api.core.v01.population.Population;
import org.matsim.core.population.PopulationUtils;

public class CopySubpopulation {

	public static void main(String[] args) {
		Population original = PopulationUtils.readPopulation("https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.5-10pct/input/berlin-v5.5-10pct.plans-selectedPlans-woRoutes.xml.gz");

		Population hasMobTypes = PopulationUtils.readPopulation("https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/berlin-drt-v5.5-10pct/input/berlin-v5.5-10pct.plans.mobilityTypes.xml.gz");


		original.getPersons().forEach( (id,person) -> {
			String mobType = PopulationUtils.getSubpopulation(hasMobTypes.getPersons().get(id));
			if(mobType != null && ! mobType.equals(PopulationUtils.getSubpopulation(person)))
			PopulationUtils.putSubpopulation(person, mobType);
		});

		PopulationUtils.writePopulation(original, "D:/svn/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/berlin-drt-v5.5-10pct/input/berlin-v5.5-10pct.selected-plans.mobilityTypes-woRoutes.xml.gz");

	}
}
