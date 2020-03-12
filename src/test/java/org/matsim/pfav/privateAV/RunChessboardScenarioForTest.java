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

package org.matsim.pfav.privateAV;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.*;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.CarrierCapabilities;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.contrib.taxi.run.MultiModeTaxiConfigGroup;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.scenario.ScenarioUtils;

class RunChessboardScenarioForTest {

	private String input;
	private String output;
	private String configFile;
	private String networkFile;
	private String carriersFile;
	private String vehtypesFile;
	private int nrOfIters;

	RunChessboardScenarioForTest(Class testClass, int nrOfIters, CarrierCapabilities.FleetSize carrierFleetSize) {
		setInputAndOutput(testClass);
		configFile = input + "chessboard_pfav_config.xml";
		networkFile = "network_speed10.xml";
		carriersFile = "chessboard_carriers_" + carrierFleetSize + ".xml";
		vehtypesFile = "chessboard_vehicleTypes.xml";
		this.nrOfIters = nrOfIters;
	}

	void run() {
		runScenario(nrOfIters);
	}

	//copied out of MatsimTestUtils which we can not use in a static context
	private void setInputAndOutput(Class testClass) {
		String classDirectory = testClass.getCanonicalName().replace('.', '/') + "/";
		String inputDir = "test/input/" + classDirectory;
		String packageInputDirectory = inputDir.substring(0, inputDir.lastIndexOf(47));
//		input = packageInputDirectory.substring(0, packageInputDirectory.lastIndexOf(47) + 1);
		input = "chessboard/";
		output = "test/output/" + classDirectory;
	}

	private void runScenario(int nrOfIters) {
		FreightAVConfigGroup pfavConfig = new FreightAVConfigGroup();
		pfavConfig.setRunTourPlanningBeforeFirstIteration(true);
		Config config = prepareConfig(nrOfIters, pfavConfig);
		FreightConfigGroup freightCfg = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
		freightCfg.setCarriersFile(carriersFile);
		freightCfg.setCarriersVehicleTypesFile(vehtypesFile);
		String mode = TaxiConfigGroup.getSingleModeTaxiConfig(config).getMode();

		// load scenario
		Scenario scenario = ScenarioUtils.loadScenario(config);
		//load carriers
		FreightUtils.loadCarriersAccordingToFreightConfig(scenario);

		createPopulation(scenario.getPopulation());

		// setup controler
		Controler controler = new Controler(scenario);
		controler.addOverridingModule(new DvrpModule());
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

	private void prepareTaxiConfigGroup(TaxiConfigGroup taxiCfg) {
		taxiCfg.setBreakSimulationIfNotAllRequestsServed(true);
		taxiCfg.setDestinationKnown(true);
		taxiCfg.setPickupDuration(120);
		taxiCfg.setDropoffDuration(60);
		taxiCfg.setTaxisFile("something");
	}

	private Config prepareConfig(int nrOfIters, FreightAVConfigGroup pfavConfig) {
		Config config = ConfigUtils.loadConfig(configFile, new DvrpConfigGroup(), new MultiModeTaxiConfigGroup(),
				pfavConfig);
		prepareTaxiConfigGroup(TaxiConfigGroup.getSingleModeTaxiConfig(config));
		config.qsim().setSimStarttimeInterpretation(QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime);
		config.qsim().setEndTime(20 * 3600);
		config.controler()
				.setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.overwriteExistingFiles);
		config.controler().setLastIteration(nrOfIters - 1);
		config.controler().setOutputDirectory(output);
		config.network().setInputFile(networkFile);
		return config;
	}

	private void createPopulation(Population population) {
		PopulationFactory factory = population.getFactory();
		{
			Person person = factory.createPerson(Id.createPersonId("1"));
			Plan plan = factory.createPlan();

			Id<Link> home1LinkId = Id.createLinkId("1");
			Id<Link> home2LinkId = Id.createLinkId("1");
			Id<Link> workLinkId = Id.createLinkId("5");
			Activity home = factory.createActivityFromLinkId("home", home1LinkId);
			home.setEndTime(6 * 3600);
			plan.addActivity(home);

			Leg leg = factory.createLeg("taxi");
			plan.addLeg(leg);

			Activity work = factory.createActivityFromLinkId("work", workLinkId);
			work.setEndTime(18 * 3600);
			work.setStartTime(6 * 3600);
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

	String getOutputDir() {
		return output;
	}
}
