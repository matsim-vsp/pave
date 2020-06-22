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

package org.matsim.bannedArea;

import com.google.inject.Inject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Coordinates;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.IdSet;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.algorithms.TransportModeNetworkFilter;
import org.matsim.core.router.SingleModeNetworksCache;
import org.matsim.core.utils.geometry.GeometryUtils;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.matsim.utils.gis.shp2matsim.ShpGeometryUtils;
import org.opengis.feature.simple.SimpleFeature;

import java.net.URL;
import java.util.*;

class ShapeFileBasedBannedAreaLinkProvider implements BannedAreaLinkProvider{

	private final double end;
	private final double start;
	private final Set<String> modes;

	SingleModeNetworksCache singleModeNetworksCache;

	Network network;

	private IdSet<Link> bannedLinks = new IdSet<>(Link.class);

	ShapeFileBasedBannedAreaLinkProvider(URL shapeFileURL, Set<String> bannedModes, double start, double end, Network network, SingleModeNetworksCache singleModeNetworksCache){
		this.start = start;
		this.end = end;
		this.modes = bannedModes;
		this.network = network;
		this.singleModeNetworksCache = singleModeNetworksCache;
		initialize(shapeFileURL, bannedModes);
	}

	private void initialize(URL shapeFileURL, Set<String> bannedModes) {
		Network modeFilteredNetwork = null;
		TransportModeNetworkFilter filter = new TransportModeNetworkFilter(network);
		modeFilteredNetwork = NetworkUtils.createNetwork();
		filter.filter(modeFilteredNetwork, bannedModes);

		List<PreparedGeometry> preparedGeometries = ShpGeometryUtils.loadPreparedGeometries(shapeFileURL);

		modeFilteredNetwork.getLinks().values().parallelStream().forEach(link -> {
			if(ShpGeometryUtils.isCoordInPreparedGeometries(link.getCoord(), preparedGeometries)) bannedLinks.add(link.getId());
		});
	}

	@Override
	public boolean isLinkBanned(Id<Link> linkId, double time, String mode) {
		return (this.modes.contains(mode) && this.start <= time && this.end >= time && this.bannedLinks.contains(linkId));
	}

	private Network getFilteredNetwork (String mode){
		// the network refers to the (transport)mode:
		Network modefilteredNetwork = null;

		// Ensure this is not performed concurrently by multiple threads!
		synchronized (this.singleModeNetworksCache.getSingleModeNetworksCache()) {
			modefilteredNetwork = this.singleModeNetworksCache.getSingleModeNetworksCache().get(mode);
			if (modefilteredNetwork == null) {
				TransportModeNetworkFilter filter = new TransportModeNetworkFilter(network);
				Set<String> modes = new HashSet<>();
				modes.add(mode);
				modefilteredNetwork = NetworkUtils.createNetwork();
				filter.filter(modefilteredNetwork, modes);
				this.singleModeNetworksCache.getSingleModeNetworksCache().put(mode, modefilteredNetwork);
			}
		}
		return modefilteredNetwork;
	}

}
