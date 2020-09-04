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

import com.google.common.collect.ImmutableSet;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.network.algorithms.MultimodalNetworkCleaner;
import org.matsim.core.utils.collections.Tuple;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.run.drt.BerlinShpUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

class CarBannedScenarioPreparation {

	private static Logger log = Logger.getLogger(CarBannedScenarioPreparation.class);

	//this file contains some link ids of the A10 Berliner Ring that still should remain car links...
	//the A100 (Stadtautobahn) is explicitly supposed to be turned into non car links though (will be usable for drt)
	private static final String INPUT_HIGHWAYLINKSINBERLIN = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/highwayLinksInsideBerlinShp.txt";

	/**
	 * Firstly, attempts to load the drt service area shape file form {@code drtConfigGroup}.
	 * Then, removes TransportMode.car from the allowedModes set for each link within the drt service area and adds drtConfigGroup.getMode() instead.
	 * Finally, runs MultiModalNetworkCleaner..
	 * @param scenario
	 * @param drtConfigGroup
	 */
	static final void banCarFromDRTServiceArea(Scenario scenario, DrtConfigGroup drtConfigGroup) {

		Set<Id<Link>> highwayLinks = parseHighwayLinksInBerlin();

		String drtServiceAreaShapeFile = drtConfigGroup.getDrtServiceAreaShapeFile();
		if (drtServiceAreaShapeFile == null || drtServiceAreaShapeFile.equals("") || drtServiceAreaShapeFile.equals("null")) {
			throw new IllegalArgumentException("if you want to ban cars from the drt service area you must provide a service area shape file in the DrtConfigGroup!");
		}

		log.info("Read service area shape file.");
		BerlinShpUtils shpUtils = new BerlinShpUtils( drtServiceAreaShapeFile );

		log.info("collect car links in service area");
		Set<Id<Link>> serviceAreaLinks = scenario.getNetwork().getLinks().values()
				.parallelStream()
				.filter(link -> link.getAllowedModes().contains(TransportMode.car))
				.filter(link -> (shpUtils.isCoordInDrtServiceArea(link.getFromNode().getCoord())
						|| shpUtils.isCoordInDrtServiceArea(link.getToNode().getCoord()))
						&& ! highwayLinks.contains(link.getId()) ) //we do not want to convert some highway links into drt links (A10 Berliner Ring)
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
		log.info("clean drt network");
		new MultimodalNetworkCleaner(scenario.getNetwork()).run(modes);
		log.info("finished");
		log.info("clean car network"); //we need to make sure that both individual mode networks are strongly connected, this is why we clean them separately. Otherwise, we will run into routing failures at the borders...
		modes.clear();
		modes.add(TransportMode.car);
		new MultimodalNetworkCleaner(scenario.getNetwork()).run(modes);
		log.info("finished");
	}

	private static Set<Id<Link>> parseHighwayLinksInBerlin() {
		Set<Id<Link>> linkIds = new LinkedHashSet<>();
		BufferedReader reader = IOUtils.getBufferedReader(INPUT_HIGHWAYLINKSINBERLIN);
		try {
			String line = reader.readLine();
			while (line != "" && line != null){
				linkIds.add(Id.createLinkId(line));
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return linkIds;
	}

	//need the specific DrtConfigGroup, this is why this is not converted to a proper ConfigConsistencyChecker yet...

	/**
	 *
	 * @param config
	 * @param drtConfigGroup the DrtConfigGroup for the drt mode which will be used in intermodal car+drt trip within the carBanned area
	 * @param additionalModes the modes that represent the mode chains with drt as access or egress mode to car, respectively. Most typically: 'walkCarDrt' and 'drtCarWalk'
	 */
	static final void prepareConfig(Config config, DrtConfigGroup drtConfigGroup, Tuple<String,String> additionalModes) {
		checkRoutingConfiguration(config);
		configureDVRPAndDRT(config, drtConfigGroup);
		configureScoringForNewModes(config, additionalModes);
		configureSubtourModeChoice(config, additionalModes);
	}

	private static final void checkRoutingConfiguration(Config config) {
		if(! config.plansCalcRoute().getAccessEgressType().equals(PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLink) ||
				config.plansCalcRoute().getAccessEgressType().equals(PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLinkPlusTimeConstant)){
			throw new IllegalArgumentException("if you want to simulate a carBannedFromCity scenario using intermodal car+drt trips, you must configure network routing for access and egress by setting " +
					"PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLink or PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLinkPlusTimeConstant!!!"
			);
		}
	}

	private static final void configureDVRPAndDRT(Config config, DrtConfigGroup drtConfigGroup) {
		DvrpConfigGroup dvrpConfigGroup = DvrpConfigGroup.get(config);
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
	}

	private static final void configureSubtourModeChoice(Config config, Tuple<String, String> additionalModes) {
		String[] oldModeChoiceModes = config.subtourModeChoice().getModes();
		String[] newModeChoiceModes = new String[oldModeChoiceModes.length + 2];
		for (int i = 0; i<= oldModeChoiceModes.length - 1; i++){
			newModeChoiceModes[i] = oldModeChoiceModes[i];
		}
		newModeChoiceModes[oldModeChoiceModes.length] = additionalModes.getFirst();
		newModeChoiceModes[oldModeChoiceModes.length + 1] = additionalModes.getSecond();
		config.subtourModeChoice().setModes(newModeChoiceModes);

		String[] chainBasedModes = config.subtourModeChoice().getChainBasedModes();
		for (int i = 0; i<= chainBasedModes.length - 1; i++){
			if(chainBasedModes[i].equals(TransportMode.car)) chainBasedModes[i] = "";
			break;
		}
	}

	private static final void configureScoringForNewModes(Config config, Tuple<String, String> additionalModes) {
		log.info("configure new modes for car trips originating or destined to the banned area. will copy scoring parameters from mode TransportMode.car");
		{
			//add scoring parameters for new modes - copied from car
			Map<String, PlanCalcScoreConfigGroup.ScoringParameterSet> scoringParams = config.planCalcScore().getScoringParametersPerSubpopulation();
			scoringParams.values().forEach(scoringParameterSet -> {
				PlanCalcScoreConfigGroup.ModeParams carParams = scoringParameterSet.getOrCreateModeParams(TransportMode.car);
				configureScoringForMode(config, scoringParameterSet, carParams, additionalModes.getFirst());
				configureScoringForMode(config, scoringParameterSet, carParams, additionalModes.getSecond());
			});
		}
	}

	private static final void configureScoringForMode(Config config, PlanCalcScoreConfigGroup.ScoringParameterSet scoringParameterSet, PlanCalcScoreConfigGroup.ModeParams carParams, String newMode) {
		PlanCalcScoreConfigGroup.ModeParams pp = new PlanCalcScoreConfigGroup.ModeParams(newMode);
		copyModeParams(carParams, pp);
		scoringParameterSet.addModeParams(pp);
		//add scoring params for stage activity
		scoringParameterSet.addActivityParams(new PlanCalcScoreConfigGroup.ActivityParams(PlanCalcScoreConfigGroup.createStageActivityType(newMode))
				.setScoringThisActivityAtAll(false));
	}

	private static final void copyModeParams(PlanCalcScoreConfigGroup.ModeParams fromModeParams, PlanCalcScoreConfigGroup.ModeParams toModeParams) {
		toModeParams.setConstant(fromModeParams.getConstant());
		toModeParams.setMarginalUtilityOfTraveling(fromModeParams.getMarginalUtilityOfTraveling());
		toModeParams.setMarginalUtilityOfDistance(fromModeParams.getMarginalUtilityOfDistance());
		toModeParams.setDailyMonetaryConstant(fromModeParams.getDailyMonetaryConstant());
		toModeParams.setDailyUtilityConstant(fromModeParams.getDailyUtilityConstant());
		toModeParams.setMonetaryDistanceRate(fromModeParams.getMonetaryDistanceRate());
	}

}
