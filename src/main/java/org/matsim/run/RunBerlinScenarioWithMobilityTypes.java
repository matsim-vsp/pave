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

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.config.Config;
import org.matsim.core.controler.Controler;

import java.util.HashMap;
import java.util.Map;

public class RunBerlinScenarioWithMobilityTypes {


    private static final String BERLIN_V5_5_CONFIG = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.5-1pct/input/berlin-v5.5-1pct.config.xml\n";

    public static void main(String[] args) {

        Config config = RunBerlinScenario.prepareConfig(new String[]{BERLIN_V5_5_CONFIG});

        //this is not set by RunBerlinScenario
        config.planCalcScore().setFractionOfIterationsToStartScoreMSA(0.8);

        PAVEBerlinModifier.configureMobilityTypeSubPopulations(config);
        config.controler().setOutputDirectory("output/berlin5.4_1pct_pave");
        config.controler().setLastIteration(0);

        Scenario scenario = RunBerlinScenario.prepareScenario(config);
        PAVEBerlinModifier.randomlyAssignMobilityTypes(scenario.getPopulation(), PAVEBerlinModifier.getMobilityTypesWithDefaulWeights());

        Controler controler = RunBerlinScenario.prepareControler(scenario);
        controler.run();

    }




}
