/* *********************************************************************** *
 * project: org.matsim.*
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2018 by the members listed in the COPYING,        *
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

package run;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.contrib.taxi.run.MultiModeTaxiConfigGroup;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.scenario.ScenarioUtils;

import privateAV.FreightAVConfigGroup;
import privateAV.PFAVModeModule;

/**
 * @author tschlenther
 */
public class RunChessboardScenario {

	private static final String CONFIG_FILE = "scenarios/chessboard/chessboard_config.xml";
	private static final String NETWORK_FILE = "network_speed10.xml";

	private static final boolean SIMULATE_CASE1 = false;

	private static final String CARRIERS_FILE_CASE1 = "scenarios/chessboard/chessboard_carriersCase1.xml";
	private static final String VEHTYPES_FILE_CASE1 = "scenarios/chessboard/chessboard_vehicleTypesCase1.xml";

	private static final String CARRIERS_FILE_CASE2 = "scenarios/chessboard/secondCase/chessboard_carriersCase2_INFINITE_routed.xml";
	private static final String VEHTYPES_FILE_CASE2 = "scenarios/chessboard/secondCase/chessboard_vehicleTypesCase2.xml";

	private static final String OUTPUT_DIR = "output/travelTimeTest/" + new SimpleDateFormat("YYYY-MM-dd_HH.mm").format(
			new Date()) + "/";
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String carriersFile;
		String vehTypesFile;

		if (SIMULATE_CASE1) {
			carriersFile = CARRIERS_FILE_CASE1;
			vehTypesFile = VEHTYPES_FILE_CASE1;
		} else {
			carriersFile = CARRIERS_FILE_CASE2;
			vehTypesFile = VEHTYPES_FILE_CASE2;
		}


		Config config = ConfigUtils.loadConfig(CONFIG_FILE, new DvrpConfigGroup(), new MultiModeTaxiConfigGroup());

		//add pfav config group
		ConfigUtils.addOrGetModule(config, FreightAVConfigGroup.class);

		//add taxi config gorup
		TaxiConfigGroup taxiCfg = TaxiConfigGroup.getSingleModeTaxiConfig(config);
		taxiCfg.setBreakSimulationIfNotAllRequestsServed(false);
		taxiCfg.setDestinationKnown(true);
		taxiCfg.setPickupDuration(60);
		taxiCfg.setDropoffDuration(60);
		taxiCfg.setTaxisFile("something");
		taxiCfg.setTimeProfiles(true);
		System.out.println("" + taxiCfg.getPickupDuration());
		taxiCfg.setDropoffDuration(60);

		//add freight config group
		FreightConfigGroup freightConfig = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
		freightConfig.setCarriersFile(carriersFile);
		freightConfig.setCarriersVehicleTypesFile(vehTypesFile);

		config.qsim().setSimStarttimeInterpretation(QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime);
		config.qsim().setEndTime(20 * 3600);

		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
		config.controler().setLastIteration(0);

		config.controler().setOutputDirectory(OUTPUT_DIR);
		config.network().setInputFile(NETWORK_FILE);

		String mode = taxiCfg.getMode();

		// load scenario
		Scenario scenario = ScenarioUtils.loadScenario(config);

		//load carriers and vehicle types
		FreightUtils.loadCarriersAccordingToFreightConfig(scenario);

		//create population
		if (SIMULATE_CASE1) {
			createpersonsForCase1(scenario.getPopulation());
		} else {
			createpersonsForCase2(scenario.getPopulation());
		}

		// setup controler
		Controler controler = new Controler(scenario);
		controler.addOverridingModule(new DvrpModule());
		//		controler.addOverridingModule(new MultiModeTaxiModule());

		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				install(new PFAVModeModule(mode, scenario));
			}
		});
		controler.configureQSimComponents(DvrpQSimComponents.activateModes(mode));

		// run simulation
		controler.run();
	}

	private static void createpersonsForCase1(Population population) {
		PopulationFactory factory = population.getFactory();
		{
			Person person = factory.createPerson(Id.createPersonId("1"));
			Plan plan = factory.createPlan();

			Id<Link> home1LinkId = Id.createLinkId("96");
			Id<Link> home2LinkId = Id.createLinkId("94");
			Id<Link> workLinkId = Id.createLinkId("93");
			Activity home = factory.createActivityFromLinkId("home", home1LinkId);
			home.setEndTime(6 * 3600);
			plan.addActivity(home);

			Leg leg = factory.createLeg("taxi");
			plan.addLeg(leg);

			Activity work = factory.createActivityFromLinkId("work", workLinkId);
			work.setStartTime(6 * 3600);
			work.setEndTime(16 * 3600);
			plan.addActivity(work);

			leg = factory.createLeg("taxi");
			plan.addLeg(leg);

			Activity home2 = factory.createActivityFromLinkId("home", home2LinkId);
			home2.setStartTime(18 * 3600);
			home2.setEndTime(20 * 3600);
			plan.addActivity(home2);

			person.addPlan(plan);
			population.addPerson(person);
		}
		for (int i = 2; i <= 100; i++) {
			Person person = factory.createPerson(Id.createPersonId(i));
			Plan plan = factory.createPlan();

			int homeLink;
			int workLink;
			double endHomeTime;
			double rnd = MatsimRandom.getRandom().nextDouble();
			if (rnd <= 0.33) {
				homeLink = 12;
				workLink = 14;
				endHomeTime = 6 * 3600;
			} else if (rnd > 0.33 && rnd <= 0.66) {
				homeLink = 129;
				workLink = 128;
				endHomeTime = 8.5 * 3600;
			} else {
				homeLink = 17;
				workLink = 128;
				endHomeTime = 8 * 3600;
			}

			Activity home = factory.createActivityFromLinkId("home", Id.createLinkId(homeLink));
			home.setEndTime(endHomeTime);
			home.setMaximumDuration(6 * 3600);
			plan.addActivity(home);

			plan.addLeg(factory.createLeg("car"));

			Activity work = factory.createActivityFromLinkId("work", Id.createLinkId(workLink));
			work.setEndTime(18 * 3600);
			work.setStartTime(6 * 3600);
			plan.addActivity(work);
			//
			//			plan.addLeg(factory.createLeg("taxi"));
			//
			//			Activity home2 = factory.createActivityFromLinkId("home", Id.createLinkId(homeLink));
			//			home2.setStartTime(18 * 3600);
			//			home2.setEndTime(20 * 3600);
			//			plan.addActivity(home2);

			person.addPlan(plan);
			population.addPerson(person);
		}

	}

	private static void createpersonsForCase2(Population population) {
		PopulationFactory factory = population.getFactory();
		{
			Person person = factory.createPerson(Id.createPersonId("1"));
			Plan plan = factory.createPlan();

			Id<Link> home1LinkId = Id.createLinkId("1");
			Id<Link> home2LinkId = Id.createLinkId("1");
			Id<Link> workLinkId = Id.createLinkId("5");
			Id<Link> workLinkId2 = Id.createLinkId("7");
			Activity home = factory.createActivityFromLinkId("home", home1LinkId);
			home.setEndTime(6 * 3600);
			plan.addActivity(home);

			Leg leg = factory.createLeg("taxi");
			plan.addLeg(leg);

			Activity work = factory.createActivityFromLinkId("work", workLinkId);
			work.setStartTime(6 * 3600);
			work.setEndTime(12 * 3600);
			plan.addActivity(work);

			leg = factory.createLeg("taxi");
			plan.addLeg(leg);

			Activity work2 = factory.createActivityFromLinkId("work", workLinkId2);
			work2.setEndTime(18 * 3600);
			work2.setStartTime(7 * 3600);
			plan.addActivity(work2);

			leg = factory.createLeg("taxi");
			plan.addLeg(leg);

			Activity home2 = factory.createActivityFromLinkId("home", home2LinkId);
			home2.setStartTime(18 * 3600);
			home2.setEndTime(20 * 3600);
			plan.addActivity(home2);

			person.addPlan(plan);
			population.addPerson(person);
		}
		for (int i = 2; i <= 100; i++) {
			Person person = factory.createPerson(Id.createPersonId(i));
			Plan plan = factory.createPlan();

			int homeLink;
			int workLink;
			double endHomeTime = 5.5 * 3600;
			double rnd = MatsimRandom.getRandom().nextDouble();
			if (rnd <= 0.33) {
				homeLink = 32;
				workLink = 36;
				//                endHomeTime = 6 * 3600;
			} else if (rnd > 0.33 && rnd <= 0.66) {
				homeLink = 33;
				workLink = 36;
				//                endHomeTime = 8.5 * 3600;
			} else {
				homeLink = 34;
				workLink = 36;
				//                endHomeTime = 8 * 3600;
			}

			Activity home = factory.createActivityFromLinkId("home", Id.createLinkId(homeLink));
			home.setEndTime(endHomeTime);
			home.setMaximumDuration(6 * 3600);
			plan.addActivity(home);

			plan.addLeg(factory.createLeg("car"));

			Activity work = factory.createActivityFromLinkId("work", Id.createLinkId(workLink));
			work.setEndTime(18 * 3600);
			work.setStartTime(6 * 3600);
			plan.addActivity(work);
			//
			//			plan.addLeg(factory.createLeg("taxi"));
			//
			//			Activity home2 = factory.createActivityFromLinkId("home", Id.createLinkId(homeLink));
			//			home2.setStartTime(18 * 3600);
			//			home2.setEndTime(20 * 3600);
			//			plan.addActivity(home2);

			person.addPlan(plan);
			population.addPerson(person);
		}
	}

}
