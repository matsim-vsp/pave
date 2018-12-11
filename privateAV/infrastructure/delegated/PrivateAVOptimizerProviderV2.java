/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
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
package privateAV.infrastructure.delegated;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.MapConfiguration;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.data.Fleet;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.router.TimeAsTravelDisutility;
import org.matsim.contrib.dvrp.trafficmonitoring.DvrpTravelTimeModule;
import org.matsim.contrib.taxi.data.validator.TaxiRequestValidator;
import org.matsim.contrib.taxi.optimizer.TaxiOptimizer;
import org.matsim.contrib.taxi.optimizer.rules.RuleBasedTaxiOptimizerParams;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.contrib.taxi.scheduler.TaxiScheduleInquiry;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.mobsim.framework.MobsimTimer;
import org.matsim.core.router.DijkstraFactory;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import privateAV.infrastructure.inherited.PrivateAV4FreightScheduler;
import privateAV.infrastructure.inherited.PrivateAVTaxiInheritedRequestInserter;
import privateAV.infrastructure.inherited.TSPrivateAVTaxiDispatcher;

/**
 * @author tschlenther
 *
 */
public class PrivateAVOptimizerProviderV2 implements Provider<TaxiOptimizer> {
	private TaxiConfigGroup taxiCfg;
	private Fleet fleet;
	private TaxiScheduleInquiry scheduler;
	private TaxiRequestValidator requestValidator;
	private EventsManager events;
	private MobsimTimer timer;
	private TravelTime travelTime;
	private TravelDisutility travelDisutility;
	private Network network;

	@Inject
	public PrivateAVOptimizerProviderV2(TaxiConfigGroup taxiCfg, Fleet fleet,
			TaxiScheduleInquiry scheduler, MobsimTimer timer,
			@Named(DvrpRoutingNetworkProvider.DVRP_ROUTING) Network network,
			@Named(DvrpTravelTimeModule.DVRP_ESTIMATED) TravelTime travelTime,
			TaxiRequestValidator requestValidator, EventsManager events) {
			this.taxiCfg = taxiCfg;
			this.fleet = fleet;
			this.scheduler = scheduler;
			this.timer = timer;
			this.network = network;
			this.travelTime = travelTime;
			this.travelDisutility = new TimeAsTravelDisutility(travelTime);
			this.requestValidator = requestValidator;
			this.events = events;
	}

	@Override
	public TaxiOptimizer get() {
		Configuration optimizerConfig = new MapConfiguration(taxiCfg.getOptimizerConfigGroup().getParams());
//		LeastCostPathCalculator router = new DijkstraFactory().createPathCalculator(network, travelDisutility,
//				travelTime);
		
		if(!(scheduler instanceof PrivateAVFreightSchedulerV2)) {
			throw new IllegalArgumentException("this OptimizerPRovider can only work with a scheduler of type " + PrivateAVFreightSchedulerV2.class);
		}
		
		
		return new PrivateAV4FreightOptimizer(taxiCfg, fleet, (PrivateAVFreightSchedulerV2) scheduler,
				new RuleBasedTaxiOptimizerParams(optimizerConfig), requestValidator, events);
	}
	
}
