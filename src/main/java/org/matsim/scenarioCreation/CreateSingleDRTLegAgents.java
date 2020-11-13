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
import org.matsim.core.router.PlanRouter;
import org.matsim.core.router.TripStructureUtils;

import java.util.List;

public class CreateSingleDRTLegAgents {


//	private static final String INPUT_POPULATION = "C:/Users/Tilmann/tubCloud/VSP_WiMi/MA-Meinhardt/berlin-v5.5-10pct/output/blckBase1.output_plans.xml.gz";
	private static final String INPUT_POPULATION = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/berlin-v5.5-10pct/output/blckBase1.output_plans.xml.gz";
	private static final String INPUT_CONFIG = "scenarios/berlin-v5.5-10pct/input/drtBlocking/blckBase1.output_config.xml";
	private static final String OUTPUT_DRTPOP = "scenarios/berlin-v5.5-10pct/input/drtBlocking/blckBase1.output_plans_drtOnly_splitAgents_neu.xml.gz";


	public static void main(String[] args) {

		Population originalPop = PopulationUtils.readPopulation(INPUT_POPULATION);
		Config config = ConfigUtils.loadConfig(INPUT_CONFIG);
		Population drtPop = PopulationUtils.createPopulation(config);
		PopulationFactory fac = drtPop.getFactory();
		originalPop.getPersons().values().parallelStream()
				.map(HasPlansAndId::getSelectedPlan)
				.forEach(plan -> {
					List<TripStructureUtils.Trip> trips = TripStructureUtils.getTrips(plan);
					int cnt = 1;
					for (TripStructureUtils.Trip trip : trips) {

						if(trip.getLegsOnly().stream().anyMatch(leg -> leg.getMode().equals("drt"))){
							Person copy = fac.createPerson(Id.createPersonId(plan.getPerson().getId().toString() + "_" + cnt));
							Plan drtPlan = fac.createPlan();
							Activity origin = trip.getOriginActivity();

							Activity start = origin.getCoord() == null ? fac.createActivityFromCoord(origin.getType(), origin.getCoord()) : fac.createActivityFromLinkId(origin.getType(), origin.getLinkId());
							double endTime = PlanRouter.calcEndOfActivity(origin, plan, config);
							start.setEndTime(endTime);
//							origin.setMaximumDurationUndefined();
							drtPlan.addActivity(start);
							drtPlan.addLeg(fac.createLeg("drt"));
							Activity dest = trip.getDestinationActivity();
							Activity end = origin.getCoord() == null ? fac.createActivityFromCoord(dest.getType(), dest.getCoord()) : fac.createActivityFromLinkId(dest.getType(), dest.getLinkId());
							end.setEndTimeUndefined();
							end.setMaximumDurationUndefined();
							drtPlan.addActivity(end);
							copy.addPlan(drtPlan);
							copy.setSelectedPlan(drtPlan);
							drtPop.addPerson(copy);
							cnt ++;
						}
					}
				});

//		drtPop.getPersons().values().parallelStream()
//				.map(person -> person.getSelectedPlan().getPlanElements())
//				.forEach(plan -> {
//					if(!((Activity) plan.get(0)).getEndTime().isDefined()){
//						System.out.println("--- why does activity = " +  plan.get(0) + " have no end time??");
//						throw new RuntimeException();
//					}
//					((Activity) plan.get(0)).setMaximumDurationUndefined();
//					((Activity) plan.get(2)).setMaximumDurationUndefined();
//					((Activity) plan.get(2)).setEndTimeUndefined();
//				});

		if(drtPop.getPersons().values().parallelStream()
				.map(HasPlansAndId::getSelectedPlan)
				.map(plan -> (Activity) plan.getPlanElements().get(0))
				.filter(activity -> activity.getEndTime().isUndefined())
				.findAny().isPresent()
		) {
			throw new RuntimeException();
		}


		PopulationUtils.writePopulation(drtPop, OUTPUT_DRTPOP);
		System.out.println("New Drt only pop was written to " + OUTPUT_DRTPOP);
	}

}
