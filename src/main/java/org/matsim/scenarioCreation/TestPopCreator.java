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
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.router.TripStructureUtils;
import org.matsim.core.scenario.ScenarioUtils;

public class TestPopCreator {

	public static void main(String[] args) {
//		create1PersonTestPop();

		Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());

		String berlin1pctPlans = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.5-1pct/input/berlin-v5.5-1pct.plans.xml.gz";
		PopulationUtils.readPopulation(scenario.getPopulation(),berlin1pctPlans);
//		PopulationUtils.sampleDown(scenario.getPopulation(), 0.1);
		scenario.getPopulation().getPersons().values().stream()
				.flatMap(person -> person.getPlans().stream())
				.flatMap(plan -> TripStructureUtils.getLegs(plan).stream())
				.forEach(leg -> leg.setRoute(null));

		PopulationUtils.writePopulation(scenario.getPopulation(), "scenarios/berlin-v5.5-1pct/input/berlin-v5.5-1pct-woRoutes.xml.gz");
	}

	private static void create1PersonTestPop() {
		Population population = ScenarioUtils.createScenario(ConfigUtils.createConfig()).getPopulation();

		PopulationFactory fact = population.getFactory();

		Person person = fact.createPerson(Id.createPersonId("testperson"));

		Plan plan = fact.createPlan();

		Activity act1 = fact.createActivityFromLinkId("home_48000", Id.createLinkId(5293));
		act1.setEndTime(8*3600);
		plan.addActivity(act1);

		plan.addLeg(fact.createLeg("car"));

		Activity act2 = fact.createActivityFromLinkId("home_48000", Id.createLinkId(102270));
		act2.setMaximumDuration(5*60);
		plan.addActivity(act2);

		plan.addLeg(fact.createLeg("car"));

		plan.addActivity(fact.createActivityFromLinkId("home_48000", Id.createLinkId(5293)));

		person.addPlan(plan);

		PopulationUtils.putSubpopulation(person, "person_fixed_car");

		population.addPerson(person);

		PopulationUtils.writePopulation(population,"scenarios/berlin-v5.5-1pct/input/drt/bannedAreaTestPop.xml");
	}
}
