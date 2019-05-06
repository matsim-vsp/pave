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
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.Population;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierPlan;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlReaderV2;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlWriterV2;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypeLoader;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypeReader;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypes;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.controler.CarrierModule;
import org.matsim.contrib.freight.replanning.CarrierPlanStrategyManagerFactory;
import org.matsim.contrib.freight.scoring.CarrierScoringFunctionFactory;
import org.matsim.contrib.freight.usecases.analysis.CarrierScoreStats;
import org.matsim.contrib.freight.usecases.analysis.LegHistogram;
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

import analysis.BaseCaseFreightTourStatsListener;
import analysis.OverallTravelTimeAndDistanceListener;
import privateAV.modules.PFAVModeModule;
import privateAV.modules.PFAVQSimModule;

/**
 * @author tschlenther
 */
public class RunChessboardScenarioWithNormalFreight {

	private static final String CONFIG_FILE = "scenarios/chessboard/chessboard_config.xml";
	private static final String NETWORK_FILE = "network_speed10.xml";

	private static final boolean SIMULATE_CASE1 = false;

	private static final String CARRIERS_FILE_CASE1 = "scenarios/chessboard/carrier1_routed.xml";
	private static final String VEHTYPES_FILE_CASE1 = "scenarios/chessboard/chessboard_vehicleTypesCase1.xml";

	//    private static final String CARRIERS_FILE_CASE2 = "scenarios/chessboard/secondCase/chessboard_carriersCase2_INFINITE.xml";
	private static final String CARRIERS_FILE_CASE2 = "scenarios/chessboard/carrier1_empty.xml";

	private static final String VEHTYPES_FILE_CASE2 = "scenarios/chessboard/secondCase/chessboard_vehicleTypesCase2.xml";
	;
	private static final String OUTPUT_DIR = "output/combinedPFAVAndCarrierTest/" + new SimpleDateFormat(
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

		TaxiConfigGroup taxiCfg = new TaxiConfigGroup();
		taxiCfg.setBreakSimulationIfNotAllRequestsServed(false);
		taxiCfg.setDestinationKnown(true);
		taxiCfg.setPickupDuration(60);
		taxiCfg.setDropoffDuration(60);
		taxiCfg.setTaxisFile("something");

		taxiCfg.setTimeProfiles(true);

		Config config = ConfigUtils.loadConfig(CONFIG_FILE, new DvrpConfigGroup(), taxiCfg);
		//		config.addModule(new DvrpConfigGroup());
		//		config.addModule(taxiCfg);

		config.qsim().setSimStarttimeInterpretation(QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime);
		config.qsim().setEndTime(20 * 3600);

		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
		config.controler().setLastIteration(0);

		config.controler().setOutputDirectory(OUTPUT_DIR);
		config.network().setInputFile(NETWORK_FILE);

		String mode = taxiCfg.getMode();

		// load scenario
		Scenario scenario = ScenarioUtils.loadScenario(config);

		createPop(scenario.getPopulation());
		//        addPFAVOwner(scenario.getPopulation());

		// setup controler
		Controler controler = new Controler(scenario);

		//        configureControlerForPFAV(carriersFile, vehTypesFile, taxiCfg, mode, scenario, controler);

		final Carriers carriers = new Carriers();
		new CarrierPlanXmlReaderV2(carriers).readFile(CARRIERS_FILE_CASE1);

		CarrierVehicleTypes types = new CarrierVehicleTypes();
		new CarrierVehicleTypeReader(types).readFile(VEHTYPES_FILE_CASE1);
		new CarrierVehicleTypeLoader(carriers).loadVehicleTypes(types);

		CarrierPlanStrategyManagerFactory strategyManagerFactory = createStrategyManagerFactory(types, controler);
		CarrierScoringFunctionFactory scoringFunctionFactory = createScoringFunctionFactory(scenario.getNetwork());

		CarrierModule carrierController = new CarrierModule(carriers, strategyManagerFactory, scoringFunctionFactory);
		//        CarrierModule carrierController = new CarrierModule(carriers, null, null);

		controler.addOverridingModule(carrierController);
		prepareFreightOutputDataAndStats(scenario, controler.getEvents(), controler, carriers);

		BaseCaseFreightTourStatsListener analyser = new BaseCaseFreightTourStatsListener(scenario.getNetwork(),
				carriers);
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
			String mode, Scenario scenario, Controler controler) {
		controler.addOverridingModule(new DvrpModule());
		//		controler.addOverridingModule(new TaxiModule());
		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				install(new PFAVModeModule(taxiCfg, scenario, carriersFile, vehTypesFile));
				installQSimModule(new PFAVQSimModule(taxiCfg));
			}
		});
		controler.configureQSimComponents(DvrpQSimComponents.activateModes(mode));
	}

	//    private void xxx(){
	//
	//        String configFile = "input/usecases/chessboard/passenger/config.xml";
	//        Config config = ConfigUtils.loadConfig(configFile);
	//        Scenario scenario = ScenarioUtils.loadScenario(config);
	//
	//        Controler controler = new Controler(config);
	//        final Carriers carriers = new Carriers();
	//        new CarrierPlanXmlReaderV2(carriers).readFile("input/usecases/chessboard/freight/carrierPlans.xml");
	//
	//        CarrierVehicleTypes types = new CarrierVehicleTypes();
	//        new CarrierVehicleTypeReader(types).readFile("input/usecases/chessboard/freight/vehicleTypes.xml");
	//        new CarrierVehicleTypeLoader(carriers).loadVehicleTypes(types);
	//
	//        CarrierPlanStrategyManagerFactory strategyManagerFactory = createStrategyManagerFactory(types, controler);
	//        CarrierScoringFunctionFactory scoringFunctionFactory = createScoringFunctionFactory(scenario.getNetwork());
	//
	//        CarrierModule carrierController = new CarrierModule(carriers, strategyManagerFactory, scoringFunctionFactory);
	//
	//        controler.addOverridingModule(carrierController);
	//        prepareFreightOutputDataAndStats(scenario, controler.getEvents(), controler, carriers);
	//
	//        controler.run();
	//    }

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

	private static CarrierScoringFunctionFactory createScoringFunctionFactory(final Network network) {
		return carrier -> {
			SumScoringFunction sf = new SumScoringFunction();
			//            CarrierScoringFunctionFactoryImpl.DriversLegScoring driverLegScoring = new CarrierScoringFunctionFactoryImpl.DriversLegScoring(carrier, network);
			//            CarrierScoringFunctionFactoryImpl.DriversActivityScoring actScoring = new CarrierScoringFunctionFactoryImpl.DriversActivityScoring();
			//            sf.addScoringFunction(driverLegScoring);
			//            sf.addScoringFunction(actScoring);
			return sf;
		};
	}

	private static CarrierPlanStrategyManagerFactory createStrategyManagerFactory(final CarrierVehicleTypes types,
			final MatsimServices controler) {
		return () -> {
			final GenericStrategyManager<CarrierPlan, Carrier> strategyManager = new GenericStrategyManager<>();
			strategyManager.addStrategy(new GenericPlanStrategyImpl<>(new BestPlanSelector<>()), null, 1);
			//            strategyManager.addStrategy(new SelectBestPlanAndOptimizeItsVehicleRouteFactory(controler.getScenario().getNetwork(), types, controler.getLinkTravelTimes()).createStrategy(), null, 0.05);
			return strategyManager;
		};
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
