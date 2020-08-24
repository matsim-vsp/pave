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


import org.checkerframework.checker.units.qual.C;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.Config;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.controler.events.StartupEvent;
import org.matsim.core.controler.listener.StartupListener;
import org.matsim.core.router.TripStructureUtils;
import org.matsim.core.utils.geometry.transformations.GK4toWGS84;
import org.matsim.viz.CSVSnapshotWriterFactory;

import java.util.Collections;

public class RunBerlinScenarioWithCSVSnapshots {

	//MINX,MAXX,MINY,MAXY
	private static final double[] BERLINER_STR_CARREE = new double[] {4589900,4590248,5817804,5817993};
	private static final double[] BERLINER_STR_CARREE_LANG = new double[] {45899798,4590248,5817804,5817993};
	private static final double[] BERLINER_STR_CARREE_GROSS = new double[] {4589800,4590515,5817700,5818000};
	private static final double[] MITTE_GROSS = 			  new double[] {4593480,4595600,5820000,5821700};

	private static final double START_MONITORING = 8*3600;
	private static final double END_MONITORING = 10*3600;

	public static void main(String[] args) {
		printBoundingBoxInWGS84(MITTE_GROSS);
		run(MITTE_GROSS);
	}

	private static void run(double[] boundingBox) {
		Config config = RunBerlinScenario.prepareConfig(new String[]{"scenarios/berlin-v5.5-1pct/input/berlin-v5.5-1pct.config.xml"});

		{
			String runId = "detNet.linkWidthZero.laneWidthZero";
			config.controler().setRunId(runId);
			config.controler().setOutputDirectory("output/berlin-v5.5-1pct-snapshots-" + runId);

			config.network().setInputFile("D:/svn/shared-svn/studies/countries/de/open_berlin_scenario/be_5/network/berlin-car_be_5_withVspAdjustments2020-08-21_keepPaths-true_network.xml.gz");


//			config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.overwriteExistingFiles);
//			config.plans().setInputFile("1personTestPop.xml");

			//do not simulate transit
			config.transit().setUseTransit(false);

			config.planCalcScore().addActivityParams(new PlanCalcScoreConfigGroup.ActivityParams(TripStructureUtils.createStageActivityType("pt")).setScoringThisActivityAtAll(false));

			config.qsim().setStartTime(8 * 3600);
			config.qsim().setEndTime(10 * 3600);
			config.qsim().setSimEndtimeInterpretation(QSimConfigGroup.EndtimeInterpretation.onlyUseEndtime);

			//set the link width to zero so agents are drawn directly on the link
			config.qsim().setLinkWidthForVis(0);

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
				addSnapshotWriterBinding().toProvider(new CSVSnapshotWriterFactory("8am10am_mitte_snapshots_WGS84", boundingBox, new GK4toWGS84(), START_MONITORING,END_MONITORING));
				addSnapshotWriterBinding().toProvider(new CSVSnapshotWriterFactory("8am10am_mitte_snapshots_DHDNGK4", boundingBox, null, START_MONITORING,END_MONITORING));
			}
		});

		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				addControlerListenerBinding().toInstance((StartupListener) event -> event.getServices().getScenario().getNetwork().setEffectiveLaneWidth(0));
			}
		});

		controler.run();
	}


	private static void printBoundingBoxInWGS84(double[] boundingBox){

		GK4toWGS84 tt = new GK4toWGS84();

		Coord min = tt.transform(new Coord(boundingBox[0],boundingBox[2]));
		Coord max = tt.transform(new Coord(boundingBox[1],boundingBox[3]));

		System.out.println("Min:\tx="+min.getX() + "\ty=" + min.getY());
		System.out.println("Max:\tx="+max.getX() + "\ty=" + max.getY());


	}

}
