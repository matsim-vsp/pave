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

package run;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.contrib.taxi.optimizer.rules.RuleBasedTaxiOptimizerParams;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.run.RunBerlinScenario;
import privateAV.PFAVModeModule;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RunPFAVInBerlin {

	private static final String CONFIG_v53_1pct = "input/BerlinScenario/5.3/berlin-v5.3-1pct.config.xml";
	private static final String OUTPUTDIR = "output/Berlin/test/" + new SimpleDateFormat("YYYY-MM-dd_HH.mm").format(
			new Date()) + "/";
	private static final String CARRIERS_FILE = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/PFAVScenario/test_onlyOneCarrier_only100services.xml";
	private static final String VEHTYPES_FILE = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/PFAVScenario/baseCaseVehicleTypes.xml";

	private static final String NETWORK_CHANGE_EVENTS = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/BerlinScenario/Network/changeevents-v5.3.xml.gz";

	//only for test purposes
	private static final String SMALL_PLANS_FILE = "C:/Users/Work/git/freightAV/input/BerlinScenario/5.3/berlin100PersonsPerMode.xml";

	private static final int LAST_ITERATION = 0;

	public static void main(String[] args) {
		String configPath, output, carriers, vehTypes, population, networkChangeEvents;
		int maxIter;
		boolean increaseCapacities;
		if (args.length > 0) {
			configPath = args[0];
			carriers = args[1];
			vehTypes = args[2];
			output = args[3];
			maxIter = Integer.valueOf(args[4]);
			population = args[5];
			networkChangeEvents = args[6];
			increaseCapacities = Boolean.valueOf(args[7]);
		} else {
			configPath = CONFIG_v53_1pct;
			carriers = CARRIERS_FILE;
			vehTypes = VEHTYPES_FILE;
			output = OUTPUTDIR;
			maxIter = LAST_ITERATION;
			population = SMALL_PLANS_FILE;
			networkChangeEvents = NETWORK_CHANGE_EVENTS;
			increaseCapacities = false;
		}

		RunBerlinScenario berlin = new RunBerlinScenario(new String[]{configPath});

		//setup config
		Config config = berlin.prepareConfig();
		TaxiConfigGroup taxiCfg = prepareTaxiConfigGroup();
		String mode = taxiCfg.getMode();
		config.addModule(taxiCfg);
		adjustConfigParameters(output, population, networkChangeEvents, maxIter, increaseCapacities, config);

		Scenario scenario = berlin.prepareScenario();

		// setup controler
		Controler controler = berlin.prepareControler(new DvrpModule());
		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				install(new PFAVModeModule(taxiCfg, scenario, carriers, vehTypes));
			}
		});
		controler.configureQSimComponents(DvrpQSimComponents.activateModes(mode));

		// run simulation
		berlin.run();
	}

	private static void adjustConfigParameters(String output, String population, String networkChangeEvents, int maxIter, boolean increaseCapacities, Config config) {
		config.addModule(new DvrpConfigGroup());
		config.strategy().setFractionOfIterationsToDisableInnovation(0);
		PlanCalcScoreConfigGroup.ModeParams taxiModeParams = new PlanCalcScoreConfigGroup.ModeParams("taxi");
		taxiModeParams.setMarginalUtilityOfTraveling(0.);       // car also has 0.0 in berlin scenario????
		config.planCalcScore().addModeParams(taxiModeParams);
		config.controler()
				.setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
		config.controler().setLastIteration(maxIter);
		config.controler().setOutputDirectory(output);

		config.qsim().setSimStarttimeInterpretation(QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime);
		config.qsim().setNumberOfThreads(1);
		config.network().setChangeEventsInputFile(networkChangeEvents);
		config.network().setTimeVariantNetwork(true);
		config.plans().setInputFile(population);
		if (increaseCapacities) {
			config.qsim().setFlowCapFactor(1.5);
		}
	}

	private static TaxiConfigGroup prepareTaxiConfigGroup() {
		TaxiConfigGroup taxiCfg = new TaxiConfigGroup();
		taxiCfg.setDestinationKnown(true);
		taxiCfg.setPickupDuration(60);
		taxiCfg.setDropoffDuration(60);
		taxiCfg.setBreakSimulationIfNotAllRequestsServed(false); //for test purposes, set this to false in order to get error stack trace
		taxiCfg.setTimeProfiles(true);  //write out occupancy plot
		taxiCfg.addParameterSet(new RuleBasedTaxiOptimizerParams());
		return taxiCfg;
	}

}
