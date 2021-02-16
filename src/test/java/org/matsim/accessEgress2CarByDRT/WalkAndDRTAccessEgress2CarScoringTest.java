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

package org.matsim.accessEgress2CarByDRT;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.config.Config;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;
import org.matsim.testcases.MatsimTestUtils;

import java.net.URL;

public class WalkAndDRTAccessEgress2CarScoringTest {

	@Rule
	public MatsimTestUtils utils = new MatsimTestUtils();

	private Scenario scenario;

	@Test
	public void testThatModeParamsOfIntermodalRoutingModesDoNotMatter(){
		URL configUrl = IOUtils.extendUrl(ExamplesUtils.getTestScenarioURL("gridCarRestrictedInCenter"), "gridCarRestrictedInCenter_config.xml");
		Config config = WalkAndDRTAccessEgress2CarTest.prepareConfig(configUrl, utils.getOutputDirectory());

		PlanCalcScoreConfigGroup.ModeParams modeParams1 = config.planCalcScore().getOrCreateModeParams("drtCarWalk");
		modeParams1.setDailyMonetaryConstant(999999999);
		modeParams1.setDailyUtilityConstant(40000);
		modeParams1.setConstant(10000);
		modeParams1.setMarginalUtilityOfTraveling(999);
		modeParams1.setMarginalUtilityOfDistance(67557);
		PlanCalcScoreConfigGroup.ModeParams modeParams2 = config.planCalcScore().getOrCreateModeParams("walkCarDrt");
		modeParams2.setDailyMonetaryConstant(999999999);
		modeParams2.setDailyUtilityConstant(40000);
		modeParams2.setConstant(10000);
		modeParams2.setMarginalUtilityOfTraveling(999);
		modeParams2.setMarginalUtilityOfDistance(67557);

		Scenario scenario = WalkAndDRTAccessEgress2CarTest.prepareScenario(config);

		scenario.getPopulation().getPersons().clear();
		Person person =  createIntermodalSuperTraveler(scenario.getPopulation().getFactory());
		scenario.getPopulation().addPerson(person);

		Controler controler = WalkAndDRTAccessEgress2CarTest.prepareControler(scenario);
		controler.run();

		Assert.assertTrue("modeParams of intermodal routing modes should not matter", person.getSelectedPlan().getScore() < 0);
	}

	private Person createIntermodalSuperTraveler(PopulationFactory factory) {
		Person person = factory.createPerson(Id.createPersonId("intermodalSuperTraveler"));
		Plan plan = factory.createPlan();

		Id<Link> left = Id.createLinkId(113);
		Coord center = new Coord(0,-500);
		Id<Link> right = Id.createLinkId(118);


		Activity act1 = factory.createActivityFromLinkId("dummy", left);
		act1.setEndTime(1 * 1800);
		plan.addActivity(act1);

		plan.addLeg(factory.createLeg("car"));

		Activity act2 = factory.createActivityFromLinkId("dummy", right);
		act2.setEndTime(1.5 * 1800);
		plan.addActivity(act2);

		plan.addLeg(factory.createLeg("walkCarDrt"));

		Activity act3 = factory.createActivityFromCoord("dummy", center);
		act3.setEndTime(2 * 1800);
		plan.addActivity(act3);

		plan.addLeg(factory.createLeg("walk"));

		Activity act4 = factory.createActivityFromLinkId("dummy", left);
		act4.setEndTime(2 * 1800);
		plan.addActivity(act4);

		plan.addLeg(factory.createLeg("car"));

		Activity act5 = factory.createActivityFromLinkId("dummy", right);
		act5.setEndTime(2.5 * 1800);
		plan.addActivity(act5);

		person.addPlan(plan);
		return person;
	}

}
