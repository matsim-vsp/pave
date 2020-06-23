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

import org.matsim.api.core.v01.Coord;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.vis.snapshotwriters.AgentSnapshotInfo;

public class AgentSnapshotCoordInBoundingBoxFilter implements AgentSnapshotFilter{

	double[] boundingBox = new double[4];

	AgentSnapshotCoordInBoundingBoxFilter(double[] boundingBox) {
		this.boundingBox = boundingBox;
	}

	public AgentSnapshotCoordInBoundingBoxFilter(double minX, double maxX, double minY, double maxY) {
		this.boundingBox = new double[]{minX,maxX,minY,maxY};
	}

	@Override
	public boolean include(AgentSnapshotInfo agentSnapshotInfo) {
		return agentSnapshotInfo.getEasting() >= boundingBox[0] && agentSnapshotInfo.getEasting() <= boundingBox[1]
				&& agentSnapshotInfo.getNorthing() >= boundingBox[2] && agentSnapshotInfo.getNorthing() <= boundingBox[3];
	}

}
