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

import analysis.BaseCaseFreightTourStatsListener;
import analysis.OverallTravelTimeAndDistanceListener;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.freight.Freight;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.controler.CarrierModule;
import org.matsim.contrib.freight.usecases.analysis.CarrierScoreStats;
import org.matsim.contrib.freight.usecases.analysis.LegHistogram;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.contrib.taxi.optimizer.rules.RuleBasedTaxiOptimizerParams;
import org.matsim.contrib.taxi.run.MultiModeTaxiConfigGroup;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.MatsimServices;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.replanning.GenericPlanStrategyImpl;
import org.matsim.core.replanning.GenericStrategyManager;
import org.matsim.core.replanning.selectors.BestPlanSelector;
import org.matsim.core.scoring.SumScoringFunction;
import org.matsim.run.RunBerlinScenario;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RunNormalFreightInBerlin {

    private static final String CONFIG_v53_1pct = "C:/Users/tschlenther/Desktop/input/berlin-v5.3-1pct.config_usingLocalInputFiles.xml";
    private static final String OUTPUTDIR = "output/Berlin/test/" + new SimpleDateFormat("YYYY-MM-dd_HH.mm").format(
			new Date()) + "/";
    private static final String CARRIERS_FILE = "freight/revisedVehCosts_112019/carriers_gzBerlin_TruckWithDriver.xml";
	private static final String VEHTYPES_FILE = "freight/revisedVehCosts_112019/vehicleTypes_PFAV_Revised112019.xml";

	private static final String NETWORK_CHANGE_EVENTS = "changeevents-v5.3.xml.gz";

	//only for test purposes
	private static final String SMALL_PLANS_FILE = "population/13000CarUsers.xml";

	private static final int LAST_ITERATION = 0;


    public static void main(String[] args) {
        String configPath, output, carriersFile, vehTypesFile, population, networkChangeEvents;
        int maxIter;
        boolean increaseCapacities;
        if (args.length > 0) {
            configPath = args[0];
            carriersFile = args[1];
            vehTypesFile = args[2];
            output = args[3];
            maxIter = Integer.valueOf(args[4]);
            population = args[5];
            networkChangeEvents = args[6];
            increaseCapacities = Boolean.valueOf(args[7]);
        } else {
            configPath = CONFIG_v53_1pct;
            carriersFile = CARRIERS_FILE;
            vehTypesFile = VEHTYPES_FILE;
            output = OUTPUTDIR;
            maxIter = LAST_ITERATION;
            population = SMALL_PLANS_FILE;
            networkChangeEvents = NETWORK_CHANGE_EVENTS;
            increaseCapacities = false;
        }

        Config config = RunBerlinScenario.prepareConfig(new String[]{configPath});

        TaxiConfigGroup taxiCfg = prepareTaxiConfigGroup();
        String mode = taxiCfg.getMode();
		MultiModeTaxiConfigGroup multiTaxiCfg = new MultiModeTaxiConfigGroup();
		multiTaxiCfg.addParameterSet(taxiCfg);
		config.addModule(multiTaxiCfg);

        prepareConfig(output, population, networkChangeEvents, maxIter, increaseCapacities, config);

        FreightConfigGroup freightConfig = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
        freightConfig.setCarriersVehicleTypesFile(vehTypesFile);
        freightConfig.setCarriersFile(carriersFile);

        //load scenario and carriers
        Scenario scenario = RunBerlinScenario.prepareScenario(config);
        FreightUtils.loadCarriersAccordingToFreightConfig(scenario);

        // setup controler
        Controler controler = RunBerlinScenario.prepareControler(scenario);

        controler.addOverridingModule(new DvrpModule());


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

    private static void prepareConfig(String output, String population, String networkChangeEvents, int maxIter, boolean increaseCapacities, Config config) {
        //delete ride mode
        config.qsim().getMainModes().remove(TransportMode.ride);
        config.plansCalcRoute().removeModeRoutingParams(TransportMode.ride);
        ArrayList<String> netModes = new ArrayList<>(config.plansCalcRoute().getNetworkModes());
        netModes.remove(TransportMode.ride);
        config.plansCalcRoute().setNetworkModes(netModes);

        config.addModule(new DvrpConfigGroup());

        config.strategy().setFractionOfIterationsToDisableInnovation(0);        //  ???
        PlanCalcScoreConfigGroup.ModeParams taxiModeParams = new PlanCalcScoreConfigGroup.ModeParams("taxi");
        taxiModeParams.setMarginalUtilityOfTraveling(0.);       // car also has 0.0 ????
        config.planCalcScore().addModeParams(taxiModeParams);

        config.controler()
                .setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
        config.controler().setLastIteration(maxIter);
        config.controler().setOutputDirectory(output);

        config.qsim().setSimStarttimeInterpretation(QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime);
        config.qsim().setNumberOfThreads(1);

        //        for test purposes
        //        config.qsim().setEndTime(12 * 3600);

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
        taxiCfg.setBreakSimulationIfNotAllRequestsServed(
                false); //for test purposes, set this to false in order to get error stack trace
        taxiCfg.setDestinationKnown(true);
        taxiCfg.setPickupDuration(60);
        taxiCfg.setDropoffDuration(60);
        taxiCfg.setTaxisFile("something");
        taxiCfg.setTimeProfiles(true);
        taxiCfg.addParameterSet(new RuleBasedTaxiOptimizerParams());
        return taxiCfg;
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

}
