
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
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.algorithms.TransportModeNetworkFilter;
import org.matsim.core.router.DefaultRoutingModules;
import org.matsim.core.router.FallbackRoutingModule;
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

class WalkAccessDRTEgressRoutingModuleProvider implements Provider<RoutingModule> {
	private static final Logger log = Logger.getLogger( WalkAccessDRTEgressRoutingModuleProvider.class ) ;

	private final String mode;
	private final String routingMode;

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

	@Inject
	FallbackRoutingModule fallbackRoutingModule;


//	/**
//	 * This refers to the older (and still more standard) constructor.
//	 *
//	 * @param mode
//	 */
//	WalkAccessDRTEgressRoutingModuleProvider(String mode,
//											 com.google.inject.Provider<RoutingModule> drtRoutingModuleProvider) {
//		this( mode, mode, drtRoutingModuleProvider) ;
//	}

	/**
	 * The effect of this constructor is a router configured for "routingMode" will be used for routing, but the route
	 * will then have the mode "mode".   So one can, for example, have an uncongested and a congested within-day router,
	 * for travellers who first might be unaware, but then switch on some help, and the both produce a route of type "car".
	 *
	 * @param mode
	 * @param routingMode
	 */
	WalkAccessDRTEgressRoutingModuleProvider(String mode,
											 String routingMode,
											 com.google.inject.Provider<RoutingModule> drtRoutingModuleProvider) {
		this.mode = mode;
		this.routingMode = routingMode;
		this.drtRoutingModuleProvider = drtRoutingModuleProvider;
	}


	@Override
	public RoutingModule get() {

		Network filteredNetwork = getFilteredNetwork(mode);

		// the travel time & disutility refer to the routing mode:
		TravelDisutilityFactory travelDisutilityFactory = this.travelDisutilityFactories.get(routingMode);
		if (travelDisutilityFactory == null) {
			throw new RuntimeException("No TravelDisutilityFactory bound for mode "+routingMode+".");
		}
		TravelTime travelTime = travelTimes.get(routingMode);
		if (travelTime == null) {
			throw new RuntimeException("No TravelTime bound for mode "+routingMode+".");
		}
		LeastCostPathCalculator routeAlgo =
				leastCostPathCalculatorFactory.createPathCalculator(
						filteredNetwork,
						travelDisutilityFactory.createTravelDisutility(travelTime),
						travelTime);

		if ( !plansCalcRouteConfigGroup.isInsertingAccessEgressWalk() ) {
			throw new IllegalArgumentException("plansCalcRouteConfigGroup.isInsertingAccessEgressWalk() is not set to true. However, this module is relying on access walks." +
					"Please set the aforementioned parameter to true.");
		}

		return DefaultRoutingModules.createAccessEgressNetworkRouter(mode, routeAlgo, scenario, filteredNetwork, walkRouter, drtRoutingModuleProvider.get(), fallbackRoutingModule);
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
