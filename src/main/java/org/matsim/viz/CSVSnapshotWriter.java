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
import org.matsim.api.core.v01.Coord;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.vis.snapshotwriters.AgentSnapshotInfo;
import org.matsim.vis.snapshotwriters.SnapshotWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

final class CSVSnapshotWriter implements SnapshotWriter {

	private CSVPrinter printer;
	private Collection<AgentSnapshotFilter> filters = new ArrayList<>();
	private Collection<AgentSnapshotInfo> infos = new ArrayList<>();
	private double time;
	private double startTime = Double.NEGATIVE_INFINITY;
	private double endTime = Double.POSITIVE_INFINITY;
	private CoordinateTransformation coordinateTransformation = null;

	public CSVSnapshotWriter(String fileName) {
		try {
			printer = new CSVPrinter(new FileWriter(fileName), CSVFormat.newFormat(';').withFirstRecordAsHeader().withRecordSeparator("\n"));
			printer.printRecord("time", "id", "easting", "northing", "agentState");
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
				double x = info.getEasting();
				double y = info.getNorthing();
				if(this.coordinateTransformation != null){
					Coord coord = coordinateTransformation.transform(new Coord(x,y));
					x = coord.getX();
					y = coord.getY();
				}
				printer.printRecord(time, info.getId(), x, y, info.getAgentState());
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addAgent(AgentSnapshotInfo info) {
		if(this.time < startTime || this.time > endTime) return;
		for (AgentSnapshotFilter filter : filters) {
			if (! filter.include(info)) return;
		}
		this.infos.add(info);
	}

	@Override
	public void finish() {
		try {
			printer.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("could not close output stream. See StackTrace above");
		}
	}

	public void addFilter(AgentSnapshotFilter agentSnapshotFilter){
		this.filters.add(agentSnapshotFilter);
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}

	public double getStartTime() {
		return startTime;
	}

	public void setCoordinateTransformation(CoordinateTransformation coordinateTransformation) {
		this.coordinateTransformation = coordinateTransformation;
	}
}
