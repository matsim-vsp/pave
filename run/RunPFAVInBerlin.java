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

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.contrib.taxi.optimizer.rules.RuleBasedRequestInserter;
import org.matsim.contrib.taxi.optimizer.rules.RuleBasedTaxiOptimizerParams;
import org.matsim.contrib.taxi.run.TaxiConfigConsistencyChecker;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigGroup;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.run.RunBerlinScenario;
import privateAV.modules.PFAVModeModule;
import privateAV.modules.PFAVQSimModule;

import java.text.SimpleDateFormat;
import java.util.*;

public class RunPFAVInBerlin {

    private static final String CONFIG_v53_1pct = "input/BerlinScenario/5.3/berlin-v5.3-1pct.config.xml";
    private static final String OUTPUTDIR = "output/Berlin/test/" + new SimpleDateFormat("YYYY-MM-dd_HH.mm").format(new Date()) + "/";
    private static final String CARRIERS_FILE = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/PFAVScenario/test_onlyOneCarrier_only100services.xml";
    private static final String VEHTYPES_FILE = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/PFAVScenario/baseCaseVehicleTypes.xml";
    //only for test purposes
    private static final String SMALL_PLANS_FILE = "C:/Users/Work/git/freightAV/input/BerlinScenario/5.3/berlin100PersonsPerMode.xml";
    private static final int LAST_ITERATION = 0;
    private static final double PERCENTAGE_OF_PFAV_OWNERS = 0.01;
    private static final double DOWN_SAMPLE_SIZE = 0.5;
    private static Logger log = Logger.getLogger(RunPFAVInBerlin.class);
    private static Set<Id<Person>> PFAV_owners = new HashSet<>();


    public static void main(String[] args) {
        String configPath, output, carriers, vehTypes;
        int maxIter;

        if (args.length > 0) {
            configPath = args[0];
            carriers = args[1];
            vehTypes = args[2];
            output = args[3];
            maxIter = Integer.valueOf(args[4]);
        } else {
            configPath = CONFIG_v53_1pct;
            carriers = CARRIERS_FILE;
            vehTypes = VEHTYPES_FILE;
            output = OUTPUTDIR;
            maxIter = LAST_ITERATION;
        }

        RunBerlinScenario berlin = new RunBerlinScenario(configPath, null);

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
        taxiCfg.setPickupDuration(120);
        taxiCfg.setDropoffDuration(60);
        taxiCfg.setTaxisFile("something");

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

        config.addConfigConsistencyChecker(new TaxiConfigConsistencyChecker());
        config.checkConsistency();

//        //only for test purposes
//        config.plans().setInputFile(SMALL_PLANS_FILE);

        Scenario scenario = berlin.prepareScenario();

        log.warn("number of persons : " + scenario.getPopulation().getPersons().size());
        convertAgentsToPFAVOwners(scenario);
        log.warn("number of PFAV owners : " + PFAV_owners.size());

        log.warn("------ SAMPLING POPULATION DOWN --------");
        downsample(scenario.getPopulation().getPersons(), DOWN_SAMPLE_SIZE);

        // setup controler
        Controler controler = berlin.prepareControler(new DvrpModule());
        controler.addOverridingModule(new AbstractModule() {
            @Override
            public void install() {
                install(new PFAVModeModule(taxiCfg, scenario, carriers, vehTypes));
                installQSimModule(new PFAVQSimModule(taxiCfg));
            }
        });
        controler.configureQSimComponents(DvrpQSimComponents.activateModes(mode));

        // run simulation
        berlin.run();

    }


    private static void convertAgentsToPFAVOwners(Scenario scenario) {
        log.info("start converting car legs to taxi legs with the probability " + PERCENTAGE_OF_PFAV_OWNERS);
        final Random rnd = MatsimRandom.getLocalInstance();
        for (Person p : scenario.getPopulation().getPersons().values()) {
            if (rnd.nextDouble() <= PERCENTAGE_OF_PFAV_OWNERS) {
                //TODO: i only care about the selected plans, right?
                PFAV_owners.add(p.getId());
                for (PlanElement pe : p.getSelectedPlan().getPlanElements()) {
                    if (pe instanceof Leg) {
                        if (((Leg) pe).getMode().equals("car")) ((Leg) pe).setMode("taxi");
                    }
                }
            }
        }
    }

    private static void downsample(final Map map, final double sample) {
        final Random rnd = MatsimRandom.getLocalInstance();
        log.warn("map size before=" + map.size());
        map.values().removeIf(person -> rnd.nextDouble() > sample && !PFAV_owners.contains(((Person) person).getId()));
        log.warn("map size after=" + map.size());
    }
}
