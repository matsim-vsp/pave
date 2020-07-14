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

package org.matsim.run;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.drtSpeedUp.DrtSpeedUpModule;
import org.matsim.optDRT.MultiModeOptDrtConfigGroup;
import org.matsim.optDRT.OptDrt;
import org.matsim.run.drt.RunDrtOpenBerlinScenario;
import org.matsim.scenarioCreation.ProhibitCarInDRTServiceAreaModule;

public class RunBerlinScenarioWithMobilityTypesAndDrtSpeedUp {


    private static final Logger log = Logger.getLogger(RunBerlinScenarioWithMobilityTypesAndDrtSpeedUp.class );

//    private static final String BERLIN_V5_5_CONFIG = "scenarios/berlin-v5.5-1pct/input/drt/pave-berlin-drt-v5.5-1pct.config-transsims.xml";
    private static final String BERLIN_V5_5_CONFIG = "scenarios/berlin-v5.5-1pct/input/drt/berlin-drt-v5.5-1pct.config_banCarTest.xml";


    private static final boolean BAN_CAR_FROM_DRT_SERVICE_AREA = true;

    /**
     * @param args array containing program arguments in the following order: global sensitivity factor for mobility types (as numeric argument), path to config, custom modules to load
     */
    public static void main(String[] args) {

        for (String arg : args) {
            log.info( arg );
        }

        double sensitivityFactor = 0.1;
        String[] configArgs;
        if ( args.length==0 ) {
            configArgs = new String[]{BERLIN_V5_5_CONFIG, "--config:controler.outputDirectory", "output/berlin5.5_10pct_pave_TEST_snapshots"};
        } else {
            sensitivityFactor = Double.parseDouble(args[0]);
            configArgs = new String[args.length-1];
            for(int i = 1; i < args.length; i++){
                configArgs[i-1] = args[i];
            }
        }

        Config config = RunDrtOpenBerlinScenario.prepareConfig(configArgs);
        DrtSpeedUpModule.addTeleportedDrtMode(config);

        PAVEMobilityTypesForBerlin.configureMobilityTypeSubPopulations(config, sensitivityFactor);

        //baseCase: set drt constant to very bad value
//        {
//            config.planCalcScore().getScoringParametersPerSubpopulation().values().forEach(scoringParameterSet -> {
//                scoringParameterSet.getOrCreateModeParams("drt").setConstant(-100);
//                scoringParameterSet.getOrCreateModeParams("drt_teleportation").setConstant(-100);
//            });
//        }

        Scenario scenario = RunDrtOpenBerlinScenario.prepareScenario(config);

        /**
         * the default input population contains persons that are already assigned to mobility types.
         * if you want to run another population but with mobility types, uncomment the following line
         */
//        PAVEMobilityTypesForBerlin.randomlyAssignMobilityTypes(scenario.getPopulation(), PAVEMobilityTypesForBerlin.getMobilityTypesWithDefaulWeights());

        Controler controler = RunDrtOpenBerlinScenario.prepareControler(scenario);
        controler.addOverridingModule(new DrtSpeedUpModule());
        OptDrt.addAsOverridingModule(controler, ConfigUtils.addOrGetModule(scenario.getConfig(), MultiModeOptDrtConfigGroup.class));

        if(BAN_CAR_FROM_DRT_SERVICE_AREA){
            controler.addOverridingModule(new ProhibitCarInDRTServiceAreaModule(DrtConfigGroup.getSingleModeDrtConfig(config)));
        }

        controler.run();

        RunBerlinScenario.runAnalysis(controler);
    }


}
