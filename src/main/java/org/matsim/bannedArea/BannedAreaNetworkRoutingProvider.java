
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

 package org.matsim.bannedArea;

import com.google.inject.name.Named;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.IdSet;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.contrib.dvrp.router.DvrpRoutingModuleProvider;
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

public class BannedAreaNetworkRoutingProvider implements Provider<RoutingModule> {
	private static final Logger log = Logger.getLogger( BannedAreaNetworkRoutingProvider.class ) ;

	private final String bannedRoutingMode;
	private final String bannedMode;
	private final String substitutionRoutingMode;
	private final String substitutionMode;

	@Inject Map<String, TravelTime> travelTimes;
	@Inject Map<String, TravelDisutilityFactory> travelDisutilityFactories;
	@Inject
	SingleModeNetworksCache singleModeNetworksCache;
	@Inject PlansCalcRouteConfigGroup plansCalcRouteConfigGroup;
	@Inject Network network;
	@Inject PopulationFactory populationFactory;
	@Inject LeastCostPathCalculatorFactory leastCostPathCalculatorFactory;
	@Inject Scenario scenario ;
	@Inject
	@Named(TransportMode.walk)
	private RoutingModule walkRouter;

	@Inject BannedAreaLinkProvider bannedAreaLinkProvider;


	/**
	 * This refers to the older (and still more standard) constructor.
	 *  For each of the parameters, a routingModule is created where mode and routingMode is the same
	 *
	 * @param bannedMode, substitutionMode
	 */
	BannedAreaNetworkRoutingProvider(String bannedMode, String substitutionMode, BannedAreaLinkProvider bannedAreaLinkProvider) {
		this( bannedMode, bannedMode, substitutionMode, substitutionMode) ;
	}

	/**
	 * The effect of this constructor is a router configured for "routingMode" will be used for routing, but the route
	 * will then have the mode "mode".   So one can, for example, have an uncongested and a congested within-day router,
	 * for travellers who first might be unaware, but then switch on some help, and the both produce a route of type "car".
	 *
	 * @param bannedMode
	 * @param bannedRoutingMode
	 * @param substitutionMode
	 * @param substitutionRoutingMode
	 */
	BannedAreaNetworkRoutingProvider(String bannedMode, String bannedRoutingMode, String substitutionMode, String substitutionRoutingMode) {
//		log.setLevel(Level.DEBUG);
		
		this.bannedMode = bannedMode;
		this.bannedRoutingMode = bannedRoutingMode ;
		this.substitutionMode = substitutionMode;
		this.substitutionRoutingMode = substitutionRoutingMode;
	}


	@Override
	public RoutingModule get() {

		Network bannedModeNetwork = getFilteredNetwork(bannedMode);
		RoutingModule bannedModeROutingModule = getRoutingModule(bannedMode, bannedRoutingMode, bannedModeNetwork);

		Network substitutionModeNetwork = getFilteredNetwork(substitutionMode);
		RoutingModule substitutionModeROutingModule = getRoutingModule(substitutionMode, substitutionRoutingMode, substitutionModeNetwork);

		return new BannedAreaSubstitutionRoutingModule(scenario.getConfig(), bannedModeROutingModule, substitutionModeROutingModule,
				bannedMode, bannedModeNetwork, bannedAreaLinkProvider, substitutionModeNetwork);
	}

	private RoutingModule getRoutingModule(String mode, String routingMode, Network modefilteredNetwork ){

		if(mode.contains(TransportMode.drt)){
			return new DvrpRoutingModuleProvider(mode).get();
		}

		log.debug( "requesting network routing module with routingMode="
				+ routingMode + ";\tmode=" + mode) ;

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
						modefilteredNetwork,
						travelDisutilityFactory.createTravelDisutility(travelTime),
						travelTime);

		// the following again refers to the (transport)mode, since it will determine the mode of the leg on the network:
		if ( plansCalcRouteConfigGroup.isInsertingAccessEgressWalk() ) {
			/*
			 * All network modes should fall back to the TransportMode.walk RoutingModule for access/egress to the Network.
			 * However, TransportMode.walk cannot fallback on itself for access/egress to the Network, so don't pass an
			 * accessEgressToNetworkRouter RoutingModule.
			 */
			if (mode.equals(TransportMode.walk)) {
				return DefaultRoutingModules.createAccessEgressNetworkRouter(mode, routeAlgo, scenario, modefilteredNetwork, null ) ;
			} else {
				return DefaultRoutingModules.createAccessEgressNetworkRouter(mode, routeAlgo, scenario, modefilteredNetwork,
						walkRouter) ;
			}

		} else {
			return DefaultRoutingModules.createPureNetworkRouter(bannedMode, populationFactory, modefilteredNetwork, routeAlgo);
		}

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
