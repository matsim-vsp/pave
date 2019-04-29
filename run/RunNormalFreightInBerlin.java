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

import analysis.FreightTourStatsListener;
import analysis.OverallTravelTimeAndDistanceListener;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.controler.CarrierModule;
import org.matsim.contrib.freight.replanning.CarrierPlanStrategyManagerFactory;
import org.matsim.contrib.freight.scoring.CarrierScoringFunctionFactory;
import org.matsim.contrib.freight.usecases.analysis.CarrierScoreStats;
import org.matsim.contrib.freight.usecases.analysis.LegHistogram;
import org.matsim.contrib.taxi.optimizer.rules.RuleBasedRequestInserter;
import org.matsim.contrib.taxi.optimizer.rules.RuleBasedTaxiOptimizerParams;
import org.matsim.contrib.taxi.run.TaxiConfigConsistencyChecker;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigGroup;
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
import java.util.Date;

public class RunNormalFreightInBerlin {

    private static final String CONFIG_v53_1pct = "input/BerlinScenario/5.3/berlin-v5.3-1pct.config.xml";
    private static final String OUTPUTDIR = "output/Berlin/test/" + new SimpleDateFormat("YYYY-MM-dd_HH.mm").format(new Date()) + "/";
    private static final String CARRIERS_FILE = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/PFAVScenario/test_onlyOneCarrier_only100services.xml";
    private static final String VEHTYPES_FILE = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/PFAVScenario/baseCaseVehicleTypes.xml";

    private static final String NETWORK_CHANGE_EVENTS = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/BerlinScenario/Network/changeevents-v5.3.xml.gz";

    //only for test purposes
    private static final String SMALL_PLANS_FILE = "C:/Users/Work/git/freightAV/input/BerlinScenario/5.3/berlin100PersonsPerMode.xml";

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

        RunBerlinScenario berlin = new RunBerlinScenario(configPath);
        Config config = berlin.prepareConfig();
        TaxiConfigGroup taxiCfg = new TaxiConfigGroup();

        taxiCfg.setBreakSimulationIfNotAllRequestsServed(false); //for test purposes, set this to false in order to get error stack trace
        /*
         * very important: we assume that destinations of trips are known in advance.
         * that leads to the occupiedDriveTask and the TaxiDropoffTask to be inserted at the same time as the PickUpTask (when the request gets scheduled).
         * in our scenario, this is realistic, since users must have defined their working location before the agreement on having their AV make freight trips.
         *
         */
        taxiCfg.setDestinationKnown(true);
        taxiCfg.setPickupDuration(60);
        taxiCfg.setDropoffDuration(60);
        taxiCfg.setTaxisFile("something");

        taxiCfg.setTimeProfiles(true);

        ConfigGroup optimizerCfg = new ConfigGroup("optimizer");
        optimizerCfg.addParam("type", "RULE_BASED");
        optimizerCfg.addParam(RuleBasedTaxiOptimizerParams.GOAL, RuleBasedRequestInserter.Goal.MIN_WAIT_TIME.toString());
        optimizerCfg.addParam(RuleBasedTaxiOptimizerParams.NEAREST_REQUESTS_LIMIT, "99999");
        optimizerCfg.addParam(RuleBasedTaxiOptimizerParams.NEAREST_VEHICLES_LIMIT, "99999");
        optimizerCfg.addParam(RuleBasedTaxiOptimizerParams.CELL_SIZE, "1000"); //according to RuleBasedTaxiOptimizerParams 1000m was tested for Berlin
        taxiCfg.setOptimizerConfigGroup(optimizerCfg);

        String mode = taxiCfg.getMode();
        config.addModule(taxiCfg);

        config.addModule(new DvrpConfigGroup());

        config.strategy().setFractionOfIterationsToDisableInnovation(0);        //  ???

        PlanCalcScoreConfigGroup.ModeParams taxiModeParams = new PlanCalcScoreConfigGroup.ModeParams("taxi");
        taxiModeParams.setMarginalUtilityOfTraveling(0.);       // car also has 0.0 ????
        config.planCalcScore().addModeParams(taxiModeParams);

        config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
        config.controler().setLastIteration(maxIter);
        config.controler().setOutputDirectory(output);

        config.qsim().setSimStarttimeInterpretation(QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime);
        config.qsim().setNumberOfThreads(1);

//        for test purposes
//        config.qsim().setEndTime(12 * 3600);

        config.network().setChangeEventsInputFile(networkChangeEvents);
        config.network().setTimeVariantNetwork(true);

        config.addConfigConsistencyChecker(new TaxiConfigConsistencyChecker());
        config.checkConsistency();

        config.plans().setInputFile(population);

        if (increaseCapacities) {
            config.qsim().setFlowCapFactor(1.5);
        }
        Scenario scenario = berlin.prepareScenario();

        // setup controler
        Controler controler = berlin.prepareControler(new DvrpModule());

        final Carriers carriers = new Carriers();
        new CarrierPlanXmlReaderV2(carriers).readFile(carriersFile);

        CarrierVehicleTypes types = new CarrierVehicleTypes();
        new CarrierVehicleTypeReader(types).readFile(vehTypesFile);
        new CarrierVehicleTypeLoader(carriers).loadVehicleTypes(types);

        CarrierPlanStrategyManagerFactory strategyManagerFactory = createStrategyManagerFactory(types, controler);
        CarrierScoringFunctionFactory scoringFunctionFactory = createScoringFunctionFactory(scenario.getNetwork());

        CarrierModule carrierController = new CarrierModule(carriers, strategyManagerFactory, scoringFunctionFactory);
//        CarrierModule carrierController = new CarrierModule(carriers, null, null);

        controler.addOverridingModule(carrierController);
        prepareFreightOutputDataAndStats(scenario, controler.getEvents(), controler, carriers);

        FreightTourStatsListener analyser = new FreightTourStatsListener(scenario.getNetwork());
        OverallTravelTimeAndDistanceListener generalListener = new OverallTravelTimeAndDistanceListener(scenario.getNetwork());
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
        berlin.run();
    }

    private static void prepareFreightOutputDataAndStats(Scenario scenario, EventsManager eventsManager, MatsimServices controler, final Carriers carriers) {
        final LegHistogram freightOnly = new LegHistogram(900);
        freightOnly.setPopulation(scenario.getPopulation());
        freightOnly.setInclPop(false);
        final LegHistogram withoutFreight = new LegHistogram(900);
        withoutFreight.setPopulation(scenario.getPopulation());

        CarrierScoreStats scores = new CarrierScoreStats(carriers, "output/carrier_scores", true);

        eventsManager.addHandler(withoutFreight);
        eventsManager.addHandler(freightOnly);
        controler.addControlerListener(scores);
        controler.addControlerListener((IterationEndsListener) event -> {
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


    private static CarrierPlanStrategyManagerFactory createStrategyManagerFactory(final CarrierVehicleTypes types, final MatsimServices controler) {
        return () -> {
            final GenericStrategyManager<CarrierPlan, Carrier> strategyManager = new GenericStrategyManager<>();
            strategyManager.addStrategy(new GenericPlanStrategyImpl<>(new BestPlanSelector<>()), null, 1);
//            strategyManager.addStrategy(new SelectBestPlanAndOptimizeItsVehicleRouteFactory(controler.getScenario().getNetwork(), types, controler.getLinkTravelTimes()).createStrategy(), null, 0.05);
            return strategyManager;
        };
    }


}
