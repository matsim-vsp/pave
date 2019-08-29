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
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;
import privateAV.FreightAVConfigGroup;
import privateAV.PFAVModeModule;
import privateAV.PFAVUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author tschlenther
 */
public class RunPFAVScenario {

	private static final String CONFIG_FILE_RULEBASED = "scenarios/mielec/mielec_taxi_config_rulebased.xml/";
	private static final String CONFIG_FILE_ASSIGNMENT = "input/mielecScenario/mielec_taxi_config_assigment.xml";

	private static final String CARRIERS_FILE = "scenarios/mielec/freight/upToDate/2carriers_a_1vehicles_INFINITE.xml";

	private static final String OUTPUT_DIR = "output/test/" + new SimpleDateFormat("YYYY-MM-dd_HH.mm").format(
			new Date()) + "/";
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
			carriers = CARRIERS_FILE;
			vehTypes = PFAVUtils.DEFAULT_VEHTYPES_FILE;
			output = OUTPUT_DIR;
		}

		TaxiConfigGroup taxiCfg = new TaxiConfigGroup();
		FreightAVConfigGroup pfavConfig = new FreightAVConfigGroup(FreightAVConfigGroup.GROUP_NAME, carriers, vehTypes);
		taxiCfg.setBreakSimulationIfNotAllRequestsServed(false);
		/*
		 * very important: we assume that destinations of trips are known in advance.
		 * that leads to the occupiedDriveTask and the TaxiDropoffTask to be inserted at the same time as the PickUpTask (when the request gets scheduled).
		 * in our scenario, this is realistic, since users must have defined their working location before the agreement on having their AV make freight trips.
		 *
		 */
		taxiCfg.setDestinationKnown(true);

		Config config = ConfigUtils.loadConfig(configFile, new DvrpConfigGroup(), taxiCfg, pfavConfig);
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
		//		config.controler().setLastIteration(0);
		config.controler().setOutputDirectory(output);

		//		config.plans().setInputFile("plans_only_taxi_4.0_slightly_modified.xml");

		taxiCfg.setTimeProfiles(true);

		String mode = taxiCfg.getMode();

		// load scenario
		Scenario scenario = ScenarioUtils.loadScenario(config);

		// setup controler
		Controler controler = new Controler(scenario);
		controler.addOverridingModule(new DvrpModule());
		//		controler.addOverridingModule(new TaxiModule());
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

}
