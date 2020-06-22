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

package org.matsim.viz;

import org.matsim.api.core.v01.Scenario;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.events.algorithms.SnapshotGenerator;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.vis.snapshotwriters.AgentSnapshotInfo;

public class Event2AgentPositionCSV {

	private static final String INPUT_CONFIG = "D:/git/pave/scenarios/equil/config.xml";
	private static final String INPUT_EVENTS = "D:/git/pave/output/equil/drtBlocking/output_events.xml.gz";

	private static final String OUTPUT_CSV = INPUT_EVENTS.substring(0,INPUT_EVENTS.lastIndexOf('/') + 1) + "output_agentSnapshots.csv";

	private static final double MINX = Double.NEGATIVE_INFINITY;
	private static final double MAXX = Double.POSITIVE_INFINITY;
	private static final double MINY = Double.NEGATIVE_INFINITY;
	private static final double MAXY = Double.POSITIVE_INFINITY;
	private static final double START = Double.NEGATIVE_INFINITY;
	private static final double END = Double.POSITIVE_INFINITY;

	public static void main(String[] args) {
		Scenario scenario = ScenarioUtils.loadScenario(ConfigUtils.loadConfig(INPUT_CONFIG));

		CSVSnapshotWriter snapshotWriter = new CSVSnapshotWriter(OUTPUT_CSV);
		snapshotWriter.addFilter(agentSnapshotInfo -> agentSnapshotInfo.getEasting() >= MINX && agentSnapshotInfo.getEasting() <= MAXX
				&& agentSnapshotInfo.getNorthing() >= MINY && agentSnapshotInfo.getNorthing() <= MAXY);
		snapshotWriter.addFilter(agentSnapshotInfo -> ! agentSnapshotInfo.getAgentState().equals(AgentSnapshotInfo.AgentState.PERSON_AT_ACTIVITY));

		snapshotWriter.setStartTime(START);
		snapshotWriter.setEndTime(END);

		 convert(scenario, INPUT_EVENTS,1, snapshotWriter);
	}

	public static void convert(Scenario scenario, String eventFileName, double interval_s, CSVSnapshotWriter writer) {
		EventsManager events = EventsUtils.createEventsManager();
		SnapshotGenerator generator = new SnapshotGenerator(scenario.getNetwork(), interval_s, scenario.getConfig().qsim());
		generator.skipUntil(writer.getStartTime());
		generator.addSnapshotWriter(writer);
		events.addHandler(generator);
		new MatsimEventsReader(events).readFile(eventFileName);
		generator.finish();
		writer.finish();
	}

}
