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
import org.matsim.api.core.v01.TransportMode;
import org.matsim.bannedArea.SubstituteWithDRTWithinBannedAreaModule;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.population.PopulationUtils;
import org.matsim.bannedArea.BannedAreaNetworkRoutingProvider;
import org.matsim.run.drt.RunDrtOpenBerlinScenario;

public class BannedAreaTest {


	public static void main(String[] args) {

		if ( args.length==0 ) {
			args = new String[] {"scenarios/berlin-v5.5-1pct/input/drt/berlin-drt-v5.5-1pct.config.xml"}  ;
		}

		Config config = RunDrtOpenBerlinScenario.prepareConfig(args);

		config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.overwriteExistingFiles);
		config.controler().setLastIteration(5);

		Scenario scenario = RunDrtOpenBerlinScenario.prepareScenario(config);

//		PopulationUtils.sampleDown(scenario.getPopulation(), 0.01);

		Controler controler = RunDrtOpenBerlinScenario.prepareControler(scenario);

		String bannedAreaShape = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.5-10pct/input/berlin-shp/berlin.shp";

		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				install(new SubstituteWithDRTWithinBannedAreaModule(TransportMode.car, bannedAreaShape, DrtConfigGroup.getSingleModeDrtConfig(config)));
			}
		});

		controler.run();


	}
}
