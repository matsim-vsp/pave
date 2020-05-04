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

public class DrtBlockingBerlin {

	private static final String BERLIN_V5_4_CONFIG = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.4-1pct/input/berlin-v5.4-1pct.config.xml";

	public static void main(String[] args) {

		Config config = RunDrtOpenBerlinScenario.prepareConfig(new String[]{BERLIN_V5_4_CONFIG});

		config.controler().setLastIteration(0);

		//this is not set by RunBerlinScenario, but vsp consistency checker needs it...
		config.planCalcScore().setFractionOfIterationsToStartScoreMSA(0.8);

		config.controler().setOutputDirectory("output/berlin5.4_1pct_pave_drtBlocking");
		config.controler().setLastIteration(0);

		Scenario scenario = RunBerlinScenario.prepareScenario(config);

		Controler controler = RunBerlinScenario.prepareControler(scenario);
		controler.run();

	}


}
