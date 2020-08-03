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


import org.matsim.core.config.Config;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.utils.geometry.transformations.GK4toWGS84;
import org.matsim.viz.CSVSnapshotWriterFactory;

import java.util.Collections;

public class RunBerlinScenarioWithCSVSnapshots {

	private static final double[] BERLINER_STR_CARREE = new double[] {4589900,4590248,5817804,5817993};
	private static final double[] BERLINER_STR_CARREE_LANG = new double[] {45899798,4590248,5817804,5817993};
	private static final double[] BERLINER_STR_CARREE_GROSS = new double[] {4589800,4590515,5817700,5818000};



	private static final double START_MONITORING = 8*3600;
	private static final double END_MONITORING = 10*3600;

	public static void main(String[] args) {

		Config config = RunBerlinScenario.prepareConfig(new String[]{"scenarios/berlin-v5.5-1pct/input/berlin-v5.5-1pct.config.xml"});

		{
			config.controler().setOutputDirectory("output/berlin-v5.5-1pct-snapshots-1testperson");
			config.plans().setInputFile("1personTestPop.xml");
			config.qsim().setStartTime(8 * 3600);
			config.qsim().setEndTime(8.5 * 3600);
			config.qsim().setSimEndtimeInterpretation(QSimConfigGroup.EndtimeInterpretation.onlyUseEndtime);
		}

		config.controler().setLastIteration(0);
//		config.controler().setSnapshotFormat(Collections.emptyList());
		config.controler().setWriteSnapshotsInterval(1);
		config.qsim().setSnapshotStyle(QSimConfigGroup.SnapshotStyle.kinematicWaves);
		config.qsim().setSnapshotPeriod(1);

		Controler controler  = RunBerlinScenario.prepareControler(RunBerlinScenario.prepareScenario(config));
		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				addSnapshotWriterBinding().toProvider(new CSVSnapshotWriterFactory(BERLINER_STR_CARREE_GROSS, new GK4toWGS84(), START_MONITORING,END_MONITORING));
			}
		});
		controler.run();
	}


}
