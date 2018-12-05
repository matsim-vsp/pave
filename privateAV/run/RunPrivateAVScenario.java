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

/**
 * 
 */
package privateAV.run;

import java.util.Collections;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.dvrp.passenger.PassengerEngineQSimModule;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.taxi.optimizer.TaxiOptimizer;
import org.matsim.contrib.taxi.run.TaxiConfigConsistencyChecker;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.contrib.taxi.run.TaxiQSimModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup.SnapshotStyle;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.scenario.ScenarioUtils;

import privateAV.infrastructure.*;
import privateAV.infrastructure.inherited.TSPrivateAVOptimizerProvider;
import privateAV.modules.FreightAVQSimModule;
import privateAV.modules.PrivateFreightAVModule;

/**
 * @author tschlenther
 *
 */
public class RunPrivateAVScenario {

	public static final String CONFIG_FILE_RULEBASED = "C:/TU Berlin/MasterArbeit/input/mielec_taxi_config_rulebased.xml";
	public static final String CONFIG_FILE_ASSIGNMENT = "C:/TU Berlin/MasterArbeit/input/mielec_taxi_config_assigment.xml";
	
	/**
	 * 
	 */
	public RunPrivateAVScenario() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		TaxiConfigGroup taxiCfg = new TaxiConfigGroup();
		
		/*
		 * very important: we assume that destination of trips are known in advance.
		 * that leads to the occupiedDriveTask and the TaxiDropoffTask to be inserted at the same time as the PickUpTask (when the request gets scheduled).
		 * in our scenario, this is realistic, since users must have defined their working location before the agreed on having their AV making freight trips.
		 * 
		 */
		
		taxiCfg.setDestinationKnown(true);
		
		Config config = ConfigUtils.loadConfig(CONFIG_FILE_RULEBASED, new DvrpConfigGroup(), taxiCfg);
		config.controler().setOverwriteFileSetting(OverwriteFileSetting.deleteDirectoryIfExists);
		
		config.controler().setLastIteration(0);
		config.controler().setOutputDirectory("output/testOhneStayTask/");
//		config.qsim().setStartTime(0);
//		config.qsim().setSimStarttimeInterpretation(StarttimeInterpretation.onlyUseStarttime);
		config.qsim().setSnapshotStyle(SnapshotStyle.withHoles);
//		config.global().setNumberOfThreads(8);
		config.addConfigConsistencyChecker(new TaxiConfigConsistencyChecker());
		config.checkConsistency();
		
		final Scenario scenario = ScenarioUtils.loadScenario(config);
		Controler controler = new Controler(scenario);

		controler.addOverridingModule(new PrivateFreightAVModule(scenario));
		controler.addQSimModule(new FreightAVQSimModule(TSPrivateAVOptimizerProvider.class));
//		controler.addQSimModule(new PassengerEngineQSimModule(taxiCfg.getMode()));
		
		
		controler.addOverridingModule(DvrpModule.createModule(taxiCfg.getMode(),
				Collections.singleton(TaxiOptimizer.class)));
		
	
        
        
//		TSPrivateAVFleetGenerator  fleet = new TSPrivateAVFleetGenerator(scenario);  
//        controler.addOverridingModule(new AbstractModule() {
//			
//			@Override
//			public void install() {
//				bind(org.matsim.contrib.dvrp.data.Fleet.class).annotatedWith(Names.named(taxiCfg.getMode())).toInstance(fleet);
//			}
//		});
	
        controler.run();
	}
	
	

}
