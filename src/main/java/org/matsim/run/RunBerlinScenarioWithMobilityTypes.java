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
import org.matsim.core.config.Config;
import org.matsim.core.controler.Controler;

public class RunBerlinScenarioWithMobilityTypes {


    private static final Logger log = Logger.getLogger(RunBerlinScenarioWithMobilityTypes.class );

//    private static final String BERLIN_V5_5_CONFIG = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.5-10pct/input/berlin-v5.5-10pct.config.xml";
    private static final String BERLIN_V5_5_CONFIG = "scenarios/berlin-v5.5-10pct/input/pave-berlin-v5.5-10pct.config.xml";


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
            configArgs = new String[]{BERLIN_V5_5_CONFIG, "--config:controler.outputDirectory", "output/berlin5.5_10pct_pave_TEST"};
        } else {
            sensitivityFactor = Double.parseDouble(args[0]);
            configArgs = new String[args.length-1];
            for(int i = 1; i < args.length; i++){
                configArgs[i-1] = args[i];
            }
        }

        Config config = RunBerlinScenario.prepareConfig(configArgs);

//        this is not set by RunBerlinScenario
//        config.planCalcScore().setFractionOfIterationsToStartScoreMSA(0.8);

        PAVEMobilityTypesForBerlin.configureMobilityTypeSubPopulations(config, sensitivityFactor);
        Scenario scenario = RunBerlinScenario.prepareScenario(config);
//        PAVEMobilityTypesForBerlin.randomlyAssignMobilityTypes(scenario.getPopulation(), PAVEMobilityTypesForBerlin.getMobilityTypesWithDefaulWeights());

        Controler controler = RunBerlinScenario.prepareControler(scenario);
        controler.run();

        RunBerlinScenario.runAnalysis(controler);

    }


}
