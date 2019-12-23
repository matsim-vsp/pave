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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.contrib.taxi.optimizer.rules.RuleBasedTaxiOptimizerParams;
import org.matsim.contrib.taxi.run.MultiModeTaxiConfigGroup;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.run.RunBerlinScenario;

import privateAV.FreightAVConfigGroup;
import privateAV.PFAVModeModule;

public class RunPFAVInBerlin {


	private static final String runID = "";

	private static final String CONFIG_v53_1pct = "D:/local_runs/pfav/input/berlin-v5.3-1pct.config_usingLocalInputFiles.xml";
	private static final String OUTPUTDIR = "D:/local_runs/pfav/output/ " + runID +  new SimpleDateFormat("YYYY-MM-dd_HH.mm").format(
			new Date()) + "/";

	private static boolean RUN_TOURPLANNING = false;

	//	private static final String CARRIERS_FILE = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/PFAVScenario/test_onlyOneCarrier_only100services.xml";
	private static final String CARRIERS_FILE = "freight/revisedVehCosts_112019/carriers_gzBerlin_AutonomousTruck_ROUTED.xml";

	private static final String VEHTYPES_FILE = "freight/revisedVehCosts_112019/vehicleTypes_PFAV_Revised112019.xml";

	private static final String NETWORK_CHANGE_EVENTS = "changeevents-v5.3.xml.gz";

	//only for test purposes
//	private static final String SMALL_PLANS_FILE = "C:/Users/Work/git/freightAV/input/BerlinScenario/5.3/berlin100PersonsPerMode.xml";
    private static final String SMALL_PLANS_FILE = "population/13000PFAVOwners.xml";


	private static final int LAST_ITERATION = 0;

	public static void main(String[] args) {
		String configPath, output, carriers, vehTypes, pfavType, population, networkChangeEvents;
		int maxIter;
		boolean runTourPlanning, increaseCapacities;
		if (args.length > 0) {
			configPath = args[0];
			runTourPlanning = Boolean.valueOf(args[1]);
			carriers = args[2];
			vehTypes = args[3];
			pfavType = args[4];
			output = args[5];
			maxIter = Integer.valueOf(args[6]);
			population = args[7];
			networkChangeEvents = args[8];
			increaseCapacities = Boolean.valueOf(args[9]);
		} else {
			configPath = CONFIG_v53_1pct;
			runTourPlanning = RUN_TOURPLANNING;
			carriers = CARRIERS_FILE;
			vehTypes = VEHTYPES_FILE;
			pfavType = "AutonomousTruck";
			output = OUTPUTDIR;
			maxIter = LAST_ITERATION;
			population = SMALL_PLANS_FILE;
			networkChangeEvents = NETWORK_CHANGE_EVENTS;
			increaseCapacities = false;
		}

		//setup config
		Config config = RunBerlinScenario.prepareConfig(new String[]{configPath});

		//add pfav config group
		FreightAVConfigGroup pfavCfg = ConfigUtils.addOrGetModule(config, FreightAVConfigGroup.class);
		pfavCfg.setPfavType(pfavType);
		pfavCfg.setRunTourPlanningBeforeFirstIteration(runTourPlanning); //run tour planning before iteration 0


		//add taxi config group
		TaxiConfigGroup taxiCfg = prepareTaxiConfigGroup();
		String mode = taxiCfg.getMode();
		MultiModeTaxiConfigGroup multiTaxiCfg = new MultiModeTaxiConfigGroup();
		multiTaxiCfg.addParameterSet(taxiCfg);
		config.addModule(multiTaxiCfg);

		//add DvrpConfigGroup
		ConfigUtils.addOrGetModule(config, DvrpConfigGroup.class);
		adjustConfigParameters(output, population, networkChangeEvents, maxIter, increaseCapacities, config);

		//add FreightConfigGroup
		FreightConfigGroup freightCfg = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
		freightCfg.setCarriersFile(carriers);
		freightCfg.setCarriersVehicleTypesFile(vehTypes);

		Scenario scenario = RunBerlinScenario.prepareScenario(config);

		//add carriers and carrierVehicleTypes
		FreightUtils.loadCarriersAccordingToFreightConfig(scenario);

		// setup controler
		Controler controler = RunBerlinScenario.prepareControler(scenario);
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

	private static void adjustConfigParameters(String output, String population, String networkChangeEvents, int maxIter, boolean increaseCapacities, Config config) {

		//delete ride mode
		config.qsim().getMainModes().remove(TransportMode.ride);
		config.plansCalcRoute().removeModeRoutingParams(TransportMode.ride);
		ArrayList<String> netModes = new ArrayList<>(config.plansCalcRoute().getNetworkModes());
		netModes.remove(TransportMode.ride);
		config.plansCalcRoute().setNetworkModes(netModes);

		//TODO remove this. this is just here in order to get this setup running once again for trb2020.
		//TODO update everything to matsim-berlin 5.4; that means input plans, changeEvents, config and everything else
		PlanCalcScoreConfigGroup.ActivityParams ptInteractionParams = new PlanCalcScoreConfigGroup.ActivityParams();
		ptInteractionParams.setActivityType("pt interaction");
		ptInteractionParams.setScoringThisActivityAtAll(false);
		config.planCalcScore().addActivityParams(ptInteractionParams);

		PlanCalcScoreConfigGroup.ActivityParams rideInteractionParams = new PlanCalcScoreConfigGroup.ActivityParams();
		rideInteractionParams.setActivityType("ride interaction");
		rideInteractionParams.setScoringThisActivityAtAll(false);
		config.planCalcScore().addActivityParams(rideInteractionParams);

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
			config.qsim().setFlowCapFactor(2);
		}

		config.plans().setInsistingOnUsingDeprecatedPersonAttributeFile(true); //TODO this needs to be done for berlinv5.3 but not for berlinv5.5

	}

	private static TaxiConfigGroup prepareTaxiConfigGroup() {
		TaxiConfigGroup taxiCfg = new TaxiConfigGroup();
		taxiCfg.setDestinationKnown(true);
		taxiCfg.setPickupDuration(60);
		taxiCfg.setDropoffDuration(60);
		taxiCfg.setBreakSimulationIfNotAllRequestsServed(false); //for test purposes, set this to false in order to get error stack trace
		taxiCfg.setTimeProfiles(true);  //write out occupancy plot
		taxiCfg.addParameterSet(new RuleBasedTaxiOptimizerParams());
		taxiCfg.setTaxisFile("something");
		return taxiCfg;
	}

}
