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

package org.matsim.pfav.run;

import org.matsim.pfav.analysis.BaseCaseFreightTourStatsListener;
import org.matsim.pfav.analysis.OverallTravelTimeAndDistanceListener;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.*;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.contrib.freight.Freight;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.controler.CarrierModule;
import org.matsim.contrib.freight.usecases.analysis.CarrierScoreStats;
import org.matsim.contrib.freight.usecases.analysis.LegHistogram;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.contrib.taxi.run.MultiModeTaxiConfigGroup;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.MatsimServices;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.replanning.GenericPlanStrategyImpl;
import org.matsim.core.replanning.GenericStrategyManager;
import org.matsim.core.replanning.selectors.BestPlanSelector;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.scoring.SumScoringFunction;
import org.matsim.pfav.privateAV.FreightAVConfigGroup;
import org.matsim.pfav.privateAV.PFAVModeModule;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tschlenther
 */
public class RunChessboardScenarioWithNormalFreight {

	private static final String CONFIG_FILE = "scenarios/chessboard/chessboard_config.xml";
	private static final String NETWORK_FILE = "network_speed10.xml";

	private static final boolean SIMULATE_CASE1 = false;

	private static final String CARRIERS_FILE_CASE1 = "carrier1_routed.xml";
	private static final String VEHTYPES_FILE_CASE1 = "chessboard_vehicleTypesCase1.xml";

	//    private static final String CARRIERS_FILE_CASE2 = "scenarios/chessboard/secondCase/chessboard_carriersCase2_INFINITE.xml";
	private static final String CARRIERS_FILE_CASE2 = "secondCase/chessboard_carriersCase2_INFINITE_routed.xml";

	private static final String VEHTYPES_FILE_CASE2 = "secondCase/chessboard_vehicleTypesCase2.xml";
	private static final String OUTPUT_DIR = "../../output/combinedPFAVAndCarrierTest/" + new SimpleDateFormat(
			"YYYY-MM-dd_HH.mm").format(new Date()) + "/";

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

		TaxiConfigGroup taxiCfg = TaxiConfigGroup.getSingleModeTaxiConfig(config);
		taxiCfg.setBreakSimulationIfNotAllRequestsServed(false);
		taxiCfg.setDestinationKnown(true);
		taxiCfg.setPickupDuration(60);
		taxiCfg.setDropoffDuration(60);
		taxiCfg.setTaxisFile("something");

		taxiCfg.setTimeProfiles(true);
		config.qsim().setSimStarttimeInterpretation(QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime);
		config.qsim().setEndTime(20 * 3600);

		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
		config.controler().setLastIteration(0);

		config.controler().setOutputDirectory(OUTPUT_DIR);
		config.network().setInputFile(NETWORK_FILE);

		String mode = taxiCfg.getMode();

		FreightConfigGroup freightConfigGroup = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
		freightConfigGroup.setCarriersFile(carriersFile);
		freightConfigGroup.setCarriersVehicleTypesFile(vehTypesFile);

		// load scenario
		Scenario scenario = ScenarioUtils.loadScenario(config);
		FreightUtils.loadCarriersAccordingToFreightConfig(scenario);

		createPop(scenario.getPopulation());
		//        addPFAVOwner(scenario.getPopulation());

		// setup controler
		Controler controler = new Controler(scenario);

		Freight.configure(controler); //not sure if this is what we want here.. tschlenther, nov'19

		prepareFreightOutputDataAndStats(scenario, controler.getEvents(), controler, FreightUtils.getCarriers(scenario));

		BaseCaseFreightTourStatsListener analyser = new BaseCaseFreightTourStatsListener(scenario.getNetwork(),
				FreightUtils.getCarriers(scenario));
		OverallTravelTimeAndDistanceListener generalListener = new OverallTravelTimeAndDistanceListener(
				scenario.getNetwork());
		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				addControlerListenerBinding().toInstance(analyser);
				addEventHandlerBinding().toInstance(analyser);

				addEventHandlerBinding().toInstance(generalListener);
				addControlerListenerBinding().toInstance(generalListener);
			}
		});

		// run simulation
		controler.run();
	}

	private static void configureControlerForPFAV(String carriersFile, String vehTypesFile, TaxiConfigGroup taxiCfg,
			String mode, Scenario scenario, Controler controler, FreightAVConfigGroup pfavConfig) {
		controler.addOverridingModule(new DvrpModule());
		//		controler.addOverridingModule(new MultiModeTaxiModule());
		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				install(new PFAVModeModule(mode, scenario));
			}
		});
		controler.configureQSimComponents(DvrpQSimComponents.activateModes(mode));
	}

	private static void prepareFreightOutputDataAndStats(Scenario scenario, EventsManager eventsManager,
			MatsimServices controler, final Carriers carriers) {
		final LegHistogram freightOnly = new LegHistogram(900);
		freightOnly.setPopulation(scenario.getPopulation());
		freightOnly.setInclPop(false);
		final LegHistogram withoutFreight = new LegHistogram(900);
		withoutFreight.setPopulation(scenario.getPopulation());

		CarrierScoreStats scores = new CarrierScoreStats(carriers, "output/carrier_scores", true);

		eventsManager.addHandler(withoutFreight);
		eventsManager.addHandler(freightOnly);
		controler.addControlerListener(scores);
		controler.addControlerListener((IterationEndsListener)event -> {
			//write plans
			String dir = event.getServices().getControlerIO().getIterationPath(event.getIteration());
			new CarrierPlanXmlWriterV2(carriers).write(dir + "/" + event.getIteration() + ".carrierPlans.xml");

			//write stats
			freightOnly.writeGraphic(dir + "/" + event.getIteration() + ".legHistogram_freight.png");
			freightOnly.reset(event.getIteration());

			withoutFreight.writeGraphic(dir + "/" + event.getIteration() + ".legHistogram_withoutFreight.png");
			withoutFreight.reset(event.getIteration());
		});

	}

	private static void createPop(Population population) {
		PopulationFactory factory = population.getFactory();

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

	private static void addPFAVOwner(Population population) {
		PopulationFactory factory = population.getFactory();

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
		work.setEndTime(16 * 3600);
		work.setStartTime(6 * 3600);
		plan.addActivity(work);

		leg = factory.createLeg("car");
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

}
