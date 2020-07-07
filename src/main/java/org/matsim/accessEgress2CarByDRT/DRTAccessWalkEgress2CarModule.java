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

package org.matsim.accessEgress2CarByDRT;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeModule;
import org.matsim.core.router.RoutingModule;

public class DRTAccessWalkEgress2CarModule extends AbstractDvrpModeModule {


	private final DrtConfigGroup drtCfg;
	private final String mode;


	/**
	 * This module will bind a routing module that uses the given drt mode for access and walk for egress while the main trip is routed based on the car disutility, car travel time and on the car network.<br>
	 * That means, the {@code mode} is a representation for the modeChain drtMode->car->walk where drtMode is equal to {@code drtCfg.getMode()}. Note that, in standard configuration, drt uses walk for access and egress itself, so the trip might end up
	 * as walk->drtMode->walk->car->walk
	 *
	 * @param mode the mode representing drtMode->car->walk
	 * @param drtCfg the corresponding {@code DrtConfigGroup} for the access drt trip
	 */
	public DRTAccessWalkEgress2CarModule(String mode, DrtConfigGroup drtCfg) {
		super(drtCfg.getMode());
		this.drtCfg = drtCfg;
		this.mode = mode;
	}

	@Override
	public void install() {

		Provider<RoutingModule> drtRoutingModuleProvider = binder().getProvider(Key.get(RoutingModule.class, Names.named(drtCfg.getMode())));
		addRoutingModuleBinding(mode).toProvider(new DRTAccessWalkEgress2CarRoutingModuleProvider(mode, drtCfg, drtRoutingModuleProvider));

	}
}
