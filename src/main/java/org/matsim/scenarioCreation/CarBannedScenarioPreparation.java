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

package org.matsim.scenarioCreation;

import com.google.common.collect.ImmutableSet;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.core.network.algorithms.MultimodalNetworkCleaner;
import org.matsim.run.drt.BerlinShpUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class CarBannedScenarioPreparation {

	private static Logger log = Logger.getLogger(CarBannedScenarioPreparation.class);

	/**
	 * TODO: javadoc
	 * @param scenario
	 * @param drtConfigGroup
	 */
	static void banCarFromDRTServiceArea(Scenario scenario, DrtConfigGroup drtConfigGroup) {

		String drtServiceAreaShapeFile = checkConfigAndRetrieveServiceAreaFilePath(scenario, drtConfigGroup);

		log.info("Read service area shape file.");
		BerlinShpUtils shpUtils = new BerlinShpUtils( drtServiceAreaShapeFile );

		log.info("collect car links in service area");
		Set<Id<Link>> serviceAreaLinks = scenario.getNetwork().getLinks().values()
				.parallelStream()
				.filter(link -> link.getAllowedModes().contains(TransportMode.car))
				.filter(link -> shpUtils.isCoordInDrtServiceArea(link.getFromNode().getCoord())
						|| shpUtils.isCoordInDrtServiceArea(link.getToNode().getCoord()))
				.map(link -> link.getId())
				.collect(Collectors.toSet());

		log.info("found " + serviceAreaLinks.size() + " car links in service area");

		log.info("Start adjusting network. That means, car will be prohibited and " + drtConfigGroup.getMode() + " will be allowed. All other modes are not touched..");
		//i am not sure whether we could integrate the following step into the procedure above, as the manipulation of allowedModes could mean interference...
		serviceAreaLinks.parallelStream().forEach(linkId -> {
			Link link = scenario.getNetwork().getLinks().get(linkId);
			Set<String> allowedModes = new HashSet<>(link.getAllowedModes());
			allowedModes.remove(TransportMode.car);
			allowedModes.add(drtConfigGroup.getMode());
			link.setAllowedModes(allowedModes);
		});

		Set<String> modes = new HashSet<>();
		modes.add(drtConfigGroup.getMode());
		log.info("clean network");
		new MultimodalNetworkCleaner(scenario.getNetwork()).run(modes);
		log.info("finished");
	}

	private static String checkConfigAndRetrieveServiceAreaFilePath(Scenario scenario, DrtConfigGroup drtConfigGroup) {
		String drtServiceAreaShapeFile = drtConfigGroup.getDrtServiceAreaShapeFile();
		if (drtServiceAreaShapeFile == null || drtServiceAreaShapeFile.equals("") || drtServiceAreaShapeFile.equals("null")) {
			throw new IllegalArgumentException("if you want to ban cars from the drt service area you must provide a service area shape file in the DrtConfigGroup!");
		}

		DvrpConfigGroup dvrpConfigGroup = DvrpConfigGroup.get(scenario.getConfig());
		if(! dvrpConfigGroup.getNetworkModes().contains(drtConfigGroup.getMode()) ){
			log.warn("the drt mode " + drtConfigGroup.getMode() + " is not registered as network mode for dvrp - which is necessary in a bannedCarInDRTServiceArea scenario");
			log.warn("adding mode " + drtConfigGroup.getMode() + " as network mode for dvrp... ");
			dvrpConfigGroup.setNetworkModes(ImmutableSet.<String>builder()
					.addAll(dvrpConfigGroup.getNetworkModes())
					.add(drtConfigGroup.getMode())
					.build());
		}

		if(! drtConfigGroup.isUseModeFilteredSubnetwork()){
			log.warn("setting drtConfigGroup.isUseModeFilteredSubnetwork() to true...");
			drtConfigGroup.setUseModeFilteredSubnetwork(true);
		}
		return drtServiceAreaShapeFile;
	}

}
