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

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.matsim.accessEgress2CarByDRT.DRTAccessWalkEgress2CarModule;
import org.matsim.accessEgress2CarByDRT.WalkAccessDRTEgress2CarModule;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.contrib.drt.run.*;
import org.matsim.core.config.Config;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.vehicles.Vehicle;
import org.matsim.vehicles.VehicleUtils;

import java.util.*;

public class ProhibitCarInDRTServiceAreaModule extends AbstractModule {

	@Inject
	Scenario scenario;

	final DrtConfigGroup drtConfigGroup;

	private static Logger log = Logger.getLogger(ProhibitCarInDRTServiceAreaModule.class);
	private final String WALK_ACCESS_DRT_EGRESS_MODE;
	private final String DRT_ACCESS_DRT_WALK_MODE;

	public ProhibitCarInDRTServiceAreaModule(DrtConfigGroup drtConfigGroup) {
		this.drtConfigGroup = drtConfigGroup;
		this.WALK_ACCESS_DRT_EGRESS_MODE = "walkCar" + drtConfigGroup.getMode();
		this.DRT_ACCESS_DRT_WALK_MODE = drtConfigGroup + "CarWalk";
	}

	@Override
	public void install() {
		Config config = scenario.getConfig();
		if(! config.plansCalcRoute().getAccessEgressType().equals(PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLink) ||
				config.plansCalcRoute().getAccessEgressType().equals(PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLinkPlusTimeConstant)){
			throw new IllegalArgumentException("if you want to simulate use " + this.getClass() + " you must configure network routing for access and egress by setting " +
					"PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLink or PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLinkPlusTimeConstant!!!"
			);
		}
		configureScoringForNewModes(config);
		configureVehicleIdsForNewModes();
		CarBannedScenarioPreparation.banCarFromDRTServiceArea(scenario, drtConfigGroup);
		install(new WalkAccessDRTEgress2CarModule(WALK_ACCESS_DRT_EGRESS_MODE, drtConfigGroup));
		install(new DRTAccessWalkEgress2CarModule(DRT_ACCESS_DRT_WALK_MODE, drtConfigGroup));
	}

	private void configureVehicleIdsForNewModes() {
		//add mode vehicle id's
		scenario.getPopulation().getPersons().values().parallelStream().forEach(person -> {
			Map<String, Id<Vehicle>> vehicleIdMap = new HashMap<>();
			Id<Vehicle> vehicleId = Id.createVehicleId(person.getId().toString());
			vehicleIdMap.put(WALK_ACCESS_DRT_EGRESS_MODE, vehicleId);
			vehicleIdMap.put(DRT_ACCESS_DRT_WALK_MODE, vehicleId);
			VehicleUtils.insertVehicleIdsIntoAttributes(person, vehicleIdMap);
		});
	}

	private void configureScoringForNewModes(Config config) {
		log.info("configure new modes for car trips originating or destined to the banned area. will copy scoring parameters from mode TransportMode.car");
		{
			//add scoring parameters for new modes - copied from car
			Map<String, PlanCalcScoreConfigGroup.ScoringParameterSet> scoringParams = config.planCalcScore().getScoringParametersPerSubpopulation();
			scoringParams.values().forEach(scoringParameterSet -> {
				PlanCalcScoreConfigGroup.ModeParams carParams = scoringParameterSet.getOrCreateModeParams(TransportMode.car);
				configureScoringForMode(config, scoringParameterSet, carParams, WALK_ACCESS_DRT_EGRESS_MODE);
				configureScoringForMode(config, scoringParameterSet, carParams, DRT_ACCESS_DRT_WALK_MODE);
			});
		}
	}

	private void configureScoringForMode(Config config, PlanCalcScoreConfigGroup.ScoringParameterSet scoringParameterSet, PlanCalcScoreConfigGroup.ModeParams carParams, String newMode) {
		PlanCalcScoreConfigGroup.ModeParams pp = new PlanCalcScoreConfigGroup.ModeParams(newMode);
		copyModeParams(carParams, pp);
		scoringParameterSet.addModeParams(pp);
		//add scoring params for stage activity
		config.planCalcScore().addActivityParams(new PlanCalcScoreConfigGroup.ActivityParams(PlanCalcScoreConfigGroup.createStageActivityType(newMode))
				.setScoringThisActivityAtAll(false));
	}

	private void copyModeParams(PlanCalcScoreConfigGroup.ModeParams fromModeParams, PlanCalcScoreConfigGroup.ModeParams toModeParams) {
		toModeParams.setConstant(fromModeParams.getConstant());
		toModeParams.setMarginalUtilityOfTraveling(fromModeParams.getMarginalUtilityOfTraveling());
		toModeParams.setMarginalUtilityOfDistance(fromModeParams.getMarginalUtilityOfDistance());
		toModeParams.setDailyMonetaryConstant(fromModeParams.getDailyMonetaryConstant());
		toModeParams.setDailyUtilityConstant(fromModeParams.getDailyUtilityConstant());
		toModeParams.setMonetaryDistanceRate(fromModeParams.getMonetaryDistanceRate());
	}
}
