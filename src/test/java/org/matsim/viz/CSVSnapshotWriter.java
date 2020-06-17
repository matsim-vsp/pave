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

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.matsim.vis.snapshotwriters.AgentSnapshotInfo;
import org.matsim.vis.snapshotwriters.SnapshotWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public final class CSVSnapshotWriter implements SnapshotWriter {

	private CSVPrinter printer;
	Collection<AgentSnapshotFilter> filters = new ArrayList<>();
	private Collection<AgentSnapshotInfo> infos = new ArrayList<>();
	private double time;

	public CSVSnapshotWriter(String fileName) {
		try {
			printer = new CSVPrinter(new FileWriter(fileName), CSVFormat.newFormat(';').withFirstRecordAsHeader());
			printer.printRecord("time", "id", "northing", "easting", "agentState");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("could not instantiate CSVPrinter. See StackTrace above");
		}
	}

	@Override
	public void beginSnapshot(double time) {
		this.time = time;
		this.infos.clear();
	}

	@Override
	public void endSnapshot() {
		try {
			for (AgentSnapshotInfo info : this.infos) {
				printer.printRecord(time, info.getId(), info.getNorthing(), info.getEasting(), info.getAgentState());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addAgent(AgentSnapshotInfo info) {
		for (AgentSnapshotFilter filter : filters) {
			if (! filter.include(info)) return;
		}
		this.infos.add(info);
	}

	@Override
	public void finish() {
		try {
			printer.close(true);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("could not close output stream. See StackTrace above");
		}
	}

	public void addFilter(AgentSnapshotFilter agentSnapshotFilter){
		this.filters.add(agentSnapshotFilter);
	}
}
