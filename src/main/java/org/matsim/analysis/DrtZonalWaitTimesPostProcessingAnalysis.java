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

package org.matsim.analysis;

import org.apache.log4j.Logger;
import org.geotools.util.URLs;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.drt.analysis.DrtRequestAnalyzer;
import org.matsim.contrib.drt.analysis.zonal.DrtZonalSystem;
import org.matsim.contrib.drt.analysis.zonal.DrtZonalWaitTimesAnalyzer;
import org.matsim.contrib.drt.passenger.events.DrtPassengerEventsReader;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.algorithms.TransportModeNetworkFilter;
import org.matsim.utils.gis.shp2matsim.ShpGeometryUtils;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class DrtZonalWaitTimesPostProcessingAnalysis {

	private static Logger log = Logger.getLogger(DrtZonalWaitTimesPostProcessingAnalysis.class);

	public static void main(String[] args) {

		String pathToConfig = "D:/rebalancingStudy/output/pave/pave109a/pave109a.output_config.xml";
		String pathToEvents = "D:/rebalancingStudy/output/pave/pave109a/pave109a.output_events.xml.gz";

//		String pathToZonesShapeFile = "D:/rebalancingStudy/shp/planungsraum_analyse.shp";
		String pathToZonesShapeFile = "D:/svn/public-svn/matsim/scenarios/countries/de/berlin/projects/avoev/shp-files/shp-berlin-bezirksregionen/berlin-bezirksregion_GK4_fixed.shp";

//		String outputCSVFilePath = "D:/rebalancingStudy/output/pave/pave109a/pave109a.output_waitStats_drt_zonal_planungsraum.csv";
		String outputCSVFilePath = "D:/rebalancingStudy/output/pave/pave109a/pave109a.output_waitStats_drt_zonal_bezirksregionen.csv";


		Config config = ConfigUtils.loadConfig(pathToConfig, new MultiModeDrtConfigGroup(), new DvrpConfigGroup());

		Network drtNetwork = DrtZonalWaitTimesPostProcessingAnalysis.getDrtModeNetwork(config);

		final List<PreparedGeometry> preparedGeometries = ShpGeometryUtils.loadPreparedGeometries(URLs.fileToUrl(new File(pathToZonesShapeFile)));
		Map<String, Geometry> geoms = new HashMap<>();
		for (int i = 0; i < preparedGeometries.size(); i++) {
			geoms.put("" + (i + 1), preparedGeometries.get(i).getGeometry());
		}

		DrtZonalSystem zones = new DrtZonalSystem(drtNetwork, geoms);
		DrtZonalWaitTimesPostProcessingAnalysis.analyzeRun(config, pathToEvents, drtNetwork, zones, outputCSVFilePath);

	}


	static void analyzeRun(Config config, String pathToEvents, Network drtNetwork, DrtZonalSystem zones, String outputCSVFilePath){

		EventsManager eventsManager = EventsUtils.createEventsManager(config);

		DrtConfigGroup drtConfig = DrtConfigGroup.getSingleModeDrtConfig(config);
		DrtRequestAnalyzer requestAnalyzer = new DrtRequestAnalyzer(drtNetwork, drtConfig);
		eventsManager.addHandler(requestAnalyzer);
		DrtZonalWaitTimesAnalyzer analyzer = new DrtZonalWaitTimesAnalyzer(drtConfig, requestAnalyzer, zones);
		eventsManager.addHandler(analyzer);

		DrtPassengerEventsReader reader = new DrtPassengerEventsReader(eventsManager);
		reader.readFile(pathToEvents);

		log.info("start writing output to " + outputCSVFilePath);
		analyzer.write(outputCSVFilePath);

		log.info("finished.");
	}

	static Network getDrtModeNetwork(Config config) {
		DrtConfigGroup drtConfig = DrtConfigGroup.getSingleModeDrtConfig(config);
		Network network = NetworkUtils.readNetwork(config.network().getInputFile());
		Network drtNetwork = NetworkUtils.createNetwork();
		if(drtConfig.isUseModeFilteredSubnetwork()){
			new TransportModeNetworkFilter(network).filter(drtNetwork, Collections.singleton(drtConfig.getMode()));
		} else {
			new TransportModeNetworkFilter(network).filter(drtNetwork, DvrpConfigGroup.get(config).getNetworkModes());
		}
		return drtNetwork;
	}

}
