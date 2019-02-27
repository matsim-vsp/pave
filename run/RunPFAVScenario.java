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

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.contrib.taxi.run.TaxiConfigConsistencyChecker;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;
import privateAV.PFAVUtils;
import privateAV.modules.PFAVModeModule;
import privateAV.modules.PFAVQSimModule;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tschlenther
 *
 */
public class RunPFAVScenario {

    private static final String CONFIG_FILE_RULEBASED = "input/mielecScenario/mielec_taxi_config_rulebased.xml";
    private static final String CONFIG_FILE_ASSIGNMENT = "input/mielecScenario/mielec_taxi_config_assigment.xml";

    private static final String SMALL_CARRIERS_FILE = "input/mielecScenario/freight/neu/carrierPlans_routed_finite2.xml";

	private static final String OUTPUT_DIR = "output/test/" + new SimpleDateFormat("YYYY-MM-dd_HH.mm").format(new Date()) + "/";

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String configFile = "";
		String carriers = "";
		String vehTypes = "";
		String output = "";


		if (args.length > 0) {
			configFile = args[0];
			carriers = args[1];
			vehTypes = args[2];
			output = args[3];
		} else {
			configFile = CONFIG_FILE_RULEBASED;
			carriers = SMALL_CARRIERS_FILE;
			vehTypes = PFAVUtils.DEFAULT_VEHTYPES_FILE;
			output = OUTPUT_DIR;
		}
		
		TaxiConfigGroup taxiCfg = new TaxiConfigGroup();
		taxiCfg.setBreakSimulationIfNotAllRequestsServed(false);
		/*
		 * very important: we assume that destinations of trips are known in advance.
		 * that leads to the occupiedDriveTask and the TaxiDropoffTask to be inserted at the same time as the PickUpTask (when the request gets scheduled).
		 * in our scenario, this is realistic, since users must have defined their working location before the agreement on having their AV make freight trips.
		 * 
		 */
		taxiCfg.setDestinationKnown(true);

		Config config = ConfigUtils.loadConfig(configFile, new DvrpConfigGroup(), taxiCfg);
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
//		config.controler().setLastIteration(0);
		config.controler().setOutputDirectory(output);

//		config.plans().setInputFile("plans_only_taxi_4.0_slightly_modified.xml");

		config.addConfigConsistencyChecker(new TaxiConfigConsistencyChecker());
		config.checkConsistency();
		String mode = taxiCfg.getMode();

		// load scenario
		Scenario scenario = ScenarioUtils.loadScenario(config);

		// setup controler
		Controler controler = new Controler(scenario);
		controler.addOverridingModule(new DvrpModule());
//		controler.addOverridingModule(new TaxiModule());
		String finalCarriers = carriers;
		String finalVehTypes = vehTypes;
		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				install(new PFAVModeModule(taxiCfg, scenario, finalCarriers, finalVehTypes));
				installQSimModule(new PFAVQSimModule(taxiCfg));
			}
		});
		controler.configureQSimComponents(DvrpQSimComponents.activateModes(mode));


		// run simulation
		controler.run();
		
		
		
		
		
		
		
		
		
		
		
		
////		config.plans().setInputFile("input/Scenarios/mielec/plans_only_taxi_ONEAGENT.xml");
//		
////		config.qsim().setStartTime(0);
////		config.qsim().setSimStarttimeInterpretation(StarttimeInterpretation.onlyUseStarttime);
//		config.qsim().setSnapshotStyle(SnapshotStyle.withHoles);
////		config.global().setNumberOfThreads(8);
//		config.addConfigConsistencyChecker(new TaxiConfigConsistencyChecker());
//		config.checkConsistency();
//		
//		final Scenario scenario = ScenarioUtils.loadScenario(config);
//		Controler controler = new Controler(scenario);
//
////		controler.addQSimModule(new FreightAVQSimModule(TSPrivateAVOptimizerProvider.class,PrivateAV4FreightScheduler.class));
//		
////		controler.addQSimModule(new PFAVQSimModule(PFAVProvider.class,PFAVScheduler.class));
//		//null für direktes binden des optimizers ohne provider klasse
//		
//		controler.addQSimModule(new PFAVQSimModule(null,PFAVScheduler.class));
//
//		controler.addOverridingModule(new PFAVModule(scenario));
//		//if PFAVProvider is used, we need to bind our optimizer as controller listener
//		
////		controler.addQSimModule(new PassengerEngineQSimModule(taxiCfg.getMode()));
//		
//		
//		controler.addOverridingModule(DvrpModule.createModule(taxiCfg.getMode(),
//				Collections.singleton(TaxiOptimizer.class)));
//			
//        controler.run();
	}
	
	

}
