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

package org.matsim.run.drtBlocking;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.controler.Controler;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.router.TripStructureUtils;
import org.matsim.run.RunBerlinScenario;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RunDrtBlockingBerlin {

	private static final String BERLIN_V5_5_1PCT_DRT_CONFIG = "../../svn/shared-svn/projects/pave/matsim-input-files/S7_fleetMultiUse/berlin-drt-v5.5-1pct.config.xml";
	private static final String CARRIERS_PLANS_PLANNED = "berlin5.5_1pct_pave_drtBlockingcarriers_planned.xml";
	private static final String CARRIERS_PLANS = "berlin-carriers.xml";
	private static final String CARRIER_VEHICLE_TYPES = "berlin-vehicleTypes.xml";
	private static final boolean RUN_TOURPLANNING = false;

	private static final String OUTPUT_DIR = "./output/berlin-v5.5-1pct/drtBlockingTest";

	public static void main(String[] args) {

		Scenario scenario = RunDrtBlocking.prepareScenario(BERLIN_V5_5_1PCT_DRT_CONFIG, CARRIERS_PLANS_PLANNED, CARRIER_VEHICLE_TYPES, false);
		scenario.getConfig().controler().setOutputDirectory(OUTPUT_DIR);

		makePeopleUseDRTForRandomLegs(scenario.getPopulation());
		Controler controler = RunDrtBlocking.prepareControler(scenario);
		controler.run();

//		RunBerlinScenario.runAnalysis(controler);
	}

	private static void makePeopleUseDRTForRandomLegs(Population population){
		List<? extends Person> persons = population.getPersons().values().stream()
				.filter(person -> person.getAttributes().getAttribute("subpopulation").equals("person"))
				.collect(Collectors.toList());

		Random random = MatsimRandom.getLocalInstance();
		for (int i = 0; i <= 200; i++){
			Person person = persons.get(random.nextInt(persons.size()));
			persons.remove(person);
			TripStructureUtils.getTrips(person.getSelectedPlan()).forEach(trip -> {
				if(trip.getTripElements().size() == 1){
					Leg leg = trip.getLegsOnly().get(0);
					leg.setMode("drt");
					leg.setRoute(null);
					TripStructureUtils.setRoutingMode(leg, "drt");
				}
			});
//			TripStructureUtils.getLegs(person.getSelectedPlan()).forEach(leg -> {
//				leg.setMode("drt");
//				leg.getAttributes().putAttribute("routingMode", "drt");
//			});
		}
	}


}
