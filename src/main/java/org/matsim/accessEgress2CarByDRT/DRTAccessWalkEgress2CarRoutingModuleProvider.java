
/* *********************************************************************** *
 * project: org.matsim.*
 * NetworkRoutingProvider.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2019 by the members listed in the COPYING,        *
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

 package org.matsim.accessEgress2CarByDRT;

import com.google.inject.name.Named;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.algorithms.TransportModeNetworkFilter;
import org.matsim.core.router.DefaultRoutingModules;
import org.matsim.core.router.RoutingModule;
import org.matsim.core.router.SingleModeNetworksCache;
import org.matsim.core.router.costcalculators.TravelDisutilityFactory;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.LeastCostPathCalculatorFactory;
import org.matsim.core.router.util.TravelTime;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


//TODO consolidate with WalkAccessDRTEgressRoutingModuleProvider (classes are almost identical ATM)
class DRTAccessWalkEgress2CarRoutingModuleProvider implements Provider<RoutingModule> {
	private static final Logger log = Logger.getLogger( DRTAccessWalkEgress2CarRoutingModuleProvider.class ) ;

	private final String mode = TransportMode.car;

	private final com.google.inject.Provider<RoutingModule> drtRoutingModuleProvider;

	@Inject	PlansCalcRouteConfigGroup plansCalcRouteConfigGroup;
	@Inject Map<String, TravelTime> travelTimes;
	@Inject Map<String, TravelDisutilityFactory> travelDisutilityFactories;
	@Inject SingleModeNetworksCache singleModeNetworksCache;
	@Inject Network network;
	@Inject LeastCostPathCalculatorFactory leastCostPathCalculatorFactory;
	@Inject Scenario scenario ;

	@Inject
	@Named(TransportMode.walk)
	private RoutingModule walkRouter;

	/**
	 * This provider will return a routing module that uses drt for access and walk for egress while the main trip is routed based on the car disutility, car travel time and on the car network.<br>
	 * Note that, in standard configuration, drt uses walk for access and egress itself, so the trip might end up
	 * as walk->drt->walk->car->walk
	 *
	 */
	DRTAccessWalkEgress2CarRoutingModuleProvider(com.google.inject.Provider<RoutingModule> drtRoutingModuleProvider) {
		this.drtRoutingModuleProvider = drtRoutingModuleProvider;
	}


	@Override
	public RoutingModule get() {

		//we use the car network for routing here....
		Network filteredNetwork = getFilteredNetwork(mode);

		// the travel time & disutility refer to the routing mode:
		TravelDisutilityFactory travelDisutilityFactory = this.travelDisutilityFactories.get(mode);
		if (travelDisutilityFactory == null) {
			throw new RuntimeException("No TravelDisutilityFactory bound for mode "+ mode +".");
		}
		TravelTime travelTime = travelTimes.get(mode);
		if (travelTime == null) {
			throw new RuntimeException("No TravelTime bound for mode "+ mode +".");
		}
		LeastCostPathCalculator routeAlgo =
				leastCostPathCalculatorFactory.createPathCalculator(
						filteredNetwork,
						travelDisutilityFactory.createTravelDisutility(travelTime),
						travelTime);

		if ( plansCalcRouteConfigGroup.getAccessEgressType().equals(PlansCalcRouteConfigGroup.AccessEgressType.none) ||
				plansCalcRouteConfigGroup.getAccessEgressType().equals(PlansCalcRouteConfigGroup.AccessEgressType.walkConstantTimeToLink)) {
			throw new IllegalArgumentException("plansCalcRouteConfigGroup.getAccessEgressType() is not set to use network routing for access/egress. However, this module is relying on network routing for access/egress." +
					"Please set the aforementioned parameter to either " + PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLink + " or " + PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLinkPlusTimeConstant);
		}

		return DefaultRoutingModules.createAccessEgressNetworkRouter(mode, routeAlgo, scenario, filteredNetwork, drtRoutingModuleProvider.get(), walkRouter);
	}

	private Network getFilteredNetwork (String mode){
		// the network refers to the (transport)mode:
		Network modeFilteredNetwork = null;

		// Ensure this is not performed concurrently by multiple threads!
		synchronized (this.singleModeNetworksCache.getSingleModeNetworksCache()) {
			modeFilteredNetwork = this.singleModeNetworksCache.getSingleModeNetworksCache().get(mode);
			if (modeFilteredNetwork == null) {
				TransportModeNetworkFilter filter = new TransportModeNetworkFilter(network);
				Set<String> modes = new HashSet<>();
				modes.add(mode);
				modeFilteredNetwork = NetworkUtils.createNetwork();
				filter.filter(modeFilteredNetwork, modes);
				this.singleModeNetworksCache.getSingleModeNetworksCache().put(mode, modeFilteredNetwork);
			}
		}
		return modeFilteredNetwork;
	}

}
