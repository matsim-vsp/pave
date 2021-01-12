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

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import org.matsim.core.config.Config;
import org.matsim.core.config.groups.StrategyConfigGroup;
import org.matsim.core.controler.events.IterationStartsEvent;
import org.matsim.core.controler.listener.IterationStartsListener;
import org.matsim.core.replanning.PlanStrategy;
import org.matsim.core.replanning.strategies.DefaultPlanStrategiesModule;

import java.util.Map;

class TuneModeChoice implements IterationStartsListener {

	private final Logger log = Logger.getLogger(TuneModeChoice.class);

	private boolean reset = false;

	@Inject
	private Map<StrategyConfigGroup.StrategySettings, PlanStrategy> planStrategies;

	@Inject
	private Config config;

	@Override
	public void notifyIterationStarts(IterationStartsEvent iterationStartsEvent) {

		StrategyConfigGroup.StrategySettings subtourModeChoice = null;
		StrategyConfigGroup.StrategySettings changeExpBeta = null;

		for (StrategyConfigGroup.StrategySettings strategySettings : planStrategies.keySet()) {
			if (strategySettings.getStrategyName().equals(DefaultPlanStrategiesModule.DefaultStrategy.SubtourModeChoice) &&
					strategySettings.getSubpopulation().equals("person")) {
				subtourModeChoice = strategySettings;
			} else if (strategySettings.getStrategyName().equals("ChangeExpBeta") &&
					strategySettings.getSubpopulation().equals("person")) {
				changeExpBeta = strategySettings;
			}
		}
		if (iterationStartsEvent.getIteration() == 0) {
			if (changeExpBeta != null && subtourModeChoice != null) {
				changeExpBeta.setWeight(changeExpBeta.getWeight() - subtourModeChoice.getWeight());
				subtourModeChoice.setWeight(2 * subtourModeChoice.getWeight());
			}
		} else if( !reset &&
				( iterationStartsEvent.getIteration() == 100 ||
				iterationStartsEvent.getIteration() >= 0.2 * (config.controler().getLastIteration() - config.controler().getFirstIteration()) * config.strategy().getFractionOfIterationsToDisableInnovation() ) ) {
			if (changeExpBeta != null && subtourModeChoice != null) {
				subtourModeChoice.setWeight(0.5 * subtourModeChoice.getWeight());
				changeExpBeta.setWeight(changeExpBeta.getWeight() + subtourModeChoice.getWeight());
			}
			reset = true;
		}

		log.info("subtourModeChoice weight in iteration=" + iterationStartsEvent.getIteration() + " is = " + subtourModeChoice.getWeight());
		log.info("changeExpBeta weight in iteration=" + iterationStartsEvent.getIteration() + " is = " + changeExpBeta.getWeight());
	}
}
