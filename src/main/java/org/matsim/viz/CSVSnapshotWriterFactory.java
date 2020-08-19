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

import com.google.inject.Inject;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.replanning.ReplanningContext;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.vis.snapshotwriters.AgentSnapshotInfo;
import org.matsim.vis.snapshotwriters.SnapshotWriter;

import javax.annotation.Nullable;
import javax.inject.Provider;

public class CSVSnapshotWriterFactory implements Provider<SnapshotWriter> {

	private final double[] boundingBox;
	private final double start;
	private final double end;
	private final CoordinateTransformation transformation;
	private final String iterationFileName;

	@Inject
	private OutputDirectoryHierarchy controlerIO;
	@Inject
	private ReplanningContext replanningContext;

	public CSVSnapshotWriterFactory(String iterationFileName, @Nullable double[] coordBoundingBox, @Nullable CoordinateTransformation outputTransformation, double startMonitoringTime, double endMonitoringTime) {
		this.boundingBox = coordBoundingBox;
		this.start = startMonitoringTime;
		this.end = endMonitoringTime;
		this.transformation = outputTransformation;
		this.iterationFileName = iterationFileName;
	}

	@Override
	public SnapshotWriter get() {
		String fileName = controlerIO.getIterationFilename(replanningContext.getIteration(), iterationFileName + ".csv");
		CSVSnapshotWriter snapshotWriter = new CSVSnapshotWriter(fileName);
		if(this.boundingBox != null) snapshotWriter.addFilter(new AgentSnapshotCoordInBoundingBoxFilter(this.boundingBox));
		snapshotWriter.addFilter(agentSnapshotInfo -> ! agentSnapshotInfo.getAgentState().equals(AgentSnapshotInfo.AgentState.PERSON_AT_ACTIVITY));
		snapshotWriter.addFilter(agentSnapshotInfo -> ! agentSnapshotInfo.getAgentState().equals(AgentSnapshotInfo.AgentState.TRANSIT_DRIVER));
		snapshotWriter.setStartTime(start);
		snapshotWriter.setEndTime(end);
		snapshotWriter.setCoordinateTransformation(transformation);
		return snapshotWriter;
	}


	}
