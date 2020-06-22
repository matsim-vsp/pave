/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2015 by the members listed in the COPYING,        *
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

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.IdSet;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.config.Config;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.router.RoutingModule;
import org.matsim.core.router.TripRouter;
import org.matsim.core.router.TripStructureUtils;
import org.matsim.facilities.FacilitiesUtils;
import org.matsim.facilities.Facility;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tschlenther
 */
final class BannedAreaSubstitutionRoutingModule implements RoutingModule {

//	private final String mode;
//	private final PopulationFactory populationFactory;
//
//	private final Network network;
//	private final LeastCostPathCalculator routeAlgo;


	private final static Logger log = Logger.getLogger(BannedAreaSubstitutionRoutingModule.class);

	Config config;

	private final String bannedMode;

	private RoutingModule bannedModeRoutingModule;
	private final Network bannedModeNetwork;
	private BannedAreaLinkProvider bannedAreaLinkProvider;

	private RoutingModule substitutionModeRoutingModule;
	private final Network substitutionNetwork;

	BannedAreaSubstitutionRoutingModule(
			Config config,
			RoutingModule bannedModeRoutingModule,
			RoutingModule substitutionModeRoutingModule,
			final String bannedMode,
			final Network bannedModeNetwork,
			final BannedAreaLinkProvider bannedAreaLinkProvider,
			final Network substitutionNetwork) {
		this.config = config;
		this.bannedModeRoutingModule = bannedModeRoutingModule;
		this.substitutionModeRoutingModule = substitutionModeRoutingModule;
		this.bannedMode = bannedMode;
		Gbl.assertNotNull(bannedModeNetwork);
		this.bannedModeNetwork = bannedModeNetwork;
		Gbl.assertNotNull(substitutionNetwork);
		this.substitutionNetwork = substitutionNetwork;
		Gbl.assertNotNull(bannedAreaLinkProvider);
		this.bannedAreaLinkProvider = bannedAreaLinkProvider;
	}

	@Override
	public List<? extends PlanElement> calcRoute(final Facility fromFacility, final Facility toFacility, final double departureTime,
												 final Person person) {

		List<? extends PlanElement> originallyPlannedTrip;
		try {
			//route with the banned mode - could be that the mode is actually technically allowed within the banned area
			originallyPlannedTrip = this.bannedModeRoutingModule.calcRoute(fromFacility, toFacility, departureTime, person);
		} catch (RuntimeException e) {
			//try to route with the substitution mode
			//TODO find location to shift modes, then route with both bannedModeRoutingModule and substitutionModeRoutingModule
			return this.substitutionModeRoutingModule.calcRoute(fromFacility, toFacility, departureTime, person);
		}

		List<Leg> legsToSubstitute = getLegsWithPartsInBannedArea(originallyPlannedTrip, departureTime);
		if(legsToSubstitute.size() > 1){
			log.warn("multiple legs of a trip contain at least one segment within the banned area.");
			log.warn("it is not clear how to deal with that..");
			log.warn("will attempt to replace the whole trip and route it with the substitution routing module " + this.substitutionModeRoutingModule);
			return this.substitutionModeRoutingModule.calcRoute(fromFacility, toFacility, departureTime, person);
		}

		if(! legsToSubstitute.isEmpty()){
			List<PlanElement> result = new ArrayList<>();
			Link startLink = null;
			Link endLink = null;

			double now = departureTime;

			for (PlanElement planElement : originallyPlannedTrip) {

				if (planElement instanceof Activity) {
					result.add(planElement);
				} else {
					Leg leg = (Leg) planElement;

					if (! legsToSubstitute.contains(leg)) {
						result.add(leg);
						continue;
					} else {
						NetworkRoute networkRoute = (NetworkRoute) leg.getRoute();

						Integer indexOfLastConsecutiveBannedLink = null;

						if(this.bannedAreaLinkProvider.isLinkBanned(networkRoute.getStartLinkId(),now, bannedMode)){
							startLink = substitutionNetwork.getLinks().get(networkRoute.getStartLinkId()); //this could be a problem if the link does not allow for both modes..
						}
						if(this.bannedAreaLinkProvider.isLinkBanned(networkRoute.getStartLinkId(),now, bannedMode)){
							endLink = substitutionNetwork.getLinks().get(networkRoute.getEndLinkId()); //this could be a problem if the link does not allow for both modes..
						}

						for (int i = 0; i < networkRoute.getLinkIds().size(); i++) {
							if (this.bannedAreaLinkProvider.isLinkBanned(networkRoute.getStartLinkId(),now, bannedMode)) {

								if(startLink == null){
									startLink = substitutionNetwork.getLinks().get(networkRoute.getLinkIds().get(i));
								} else if(indexOfLastConsecutiveBannedLink == null || indexOfLastConsecutiveBannedLink == i-1){
									indexOfLastConsecutiveBannedLink = i;
								} else {
									// There multiple segments of this one leg that go through the banned area.
									// ATM, we handle this zigzagging by handing over the whole leg to the substitution router
									log.warn("multiple segments of original route go through banned area. replacing the entire leg by routing it with the substitutional mode..");
									endLink = substitutionNetwork.getLinks().get(networkRoute.getEndLinkId()); //this could be a problem if the link does not allow for both modes..
									break;
								}
							}
						}

						if(endLink == null) {
							endLink = substitutionNetwork.getLinks().get(networkRoute.getLinkIds().get(indexOfLastConsecutiveBannedLink)); //this could be a problem if the link does not allow for both modes..
						}

						now = substitute(departureTime, person, result, startLink, endLink, leg);

						//reroute the rest
						result.addAll(this.bannedModeRoutingModule.calcRoute(FacilitiesUtils.wrapLink(endLink), toFacility, now, person));
						break;
					}
				}

				now = TripRouter.calcEndOfPlanElement(now, planElement, config);

			}
			return result;
		}
		return originallyPlannedTrip;
	}

	private double substitute(double now, Person person, List<PlanElement> result, Link startLink, Link endLink, Leg leg) {
//		double now =  departureTime;
//		for (PlanElement pE: result) {
//			now = TripRouter.calcEndOfPlanElement( now, pE, config ) ;
//		}

		//first cut the leg and reroute it
		List<? extends PlanElement> cutLeg = this.bannedModeRoutingModule.calcRoute(FacilitiesUtils.wrapLink(bannedModeNetwork.getLinks().get(leg.getRoute().getStartLinkId())),
				FacilitiesUtils.wrapLink(startLink),
				now,  //may lead to exception
				person);

		now = addSubTripToResultAndCalcEndTime(cutLeg, startLink, now, result);

		//then substitute
		List<? extends PlanElement> substitution = this.substitutionModeRoutingModule.calcRoute(FacilitiesUtils.wrapLink(startLink),
				FacilitiesUtils.wrapLink(endLink),
				now, //may lead to exception
				person);

		now = addSubTripToResultAndCalcEndTime(substitution, endLink, now, result);
		return now;
	}

	private double addSubTripToResultAndCalcEndTime(List<? extends PlanElement> subTrip, Link endLinkOfSubTrip, double departureTime, List<PlanElement> result) {
		for (PlanElement pE : subTrip) {
			departureTime = TripRouter.calcEndOfPlanElement(departureTime, pE, config);
		}
		result.addAll(subTrip);

		//stage activity
		if (subTrip.get(subTrip.size() - 1) instanceof Leg) {
			final Activity interactionActivity = createInteractionActivity(endLinkOfSubTrip.getToNode().getCoord(), endLinkOfSubTrip.getId(), bannedMode);
			result.add(interactionActivity);
			departureTime = TripRouter.calcEndOfPlanElement(departureTime, interactionActivity, config);
		} else {
			// don't add another (interaction) activity
			// TODO: assuming that this is an interaction activity, e.g. walk - drt interaction - walk
			// Not clear what we should do if it is not an interaction activity (and how that could happen).
		}
		return departureTime;
	}


	private static Activity createInteractionActivity(final Coord interactionCoord, final Id<Link> interactionLink, final String mode ) {
		Activity act = PopulationUtils.createStageActivityFromCoordLinkIdAndModePrefix(interactionCoord, interactionLink, mode);
		act.setMaximumDuration(0.0);
		return act;
	}

	@Override
	public String toString() {
		return "[BannedAreaSubstitutionRoutingModule: bannedModeRoutingModule=" + this.bannedModeRoutingModule + " substitutionModeModule= " + this.substitutionModeRoutingModule + "]";
	}

	private List<Leg> getLegsWithPartsInBannedArea(List<? extends PlanElement> trip, double tripDepartureTime){
		return TripStructureUtils.getLegs(trip).stream()
				.filter(leg ->	doesRouteOfLegHavePartsInBannedArea(leg, tripDepartureTime)).collect(Collectors.toList());
	}

	private boolean doesRouteOfLegHavePartsInBannedArea(Leg leg, double tripDepartureTime) {
		if (!(leg.getRoute() instanceof NetworkRoute) || !leg.getMode().equals(this.bannedMode)) {
			//teleported leg (probably access or egress) so we do not check if the route contains banned links
			return false;
		}

		NetworkRoute networkRoute = (NetworkRoute) leg.getRoute();
		if ( this.bannedAreaLinkProvider.isLinkBanned(networkRoute.getStartLinkId(),leg.getDepartureTime().orElse(tripDepartureTime), bannedMode)
				|| this.bannedAreaLinkProvider.isLinkBanned(networkRoute.getEndLinkId(),leg.getDepartureTime().orElse(tripDepartureTime), bannedMode) )
			return true;
		for (int i = 0; i < networkRoute.getLinkIds().size(); i++) {
			if (this.bannedAreaLinkProvider.isLinkBanned(networkRoute.getLinkIds().get(i), leg.getDepartureTime().orElse(tripDepartureTime), bannedMode))
				return true;
		}
		return false;
	}
}
