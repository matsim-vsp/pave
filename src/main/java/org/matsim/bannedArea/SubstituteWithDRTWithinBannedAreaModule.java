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

package org.matsim.bannedArea;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.dvrp.router.DvrpRoutingModule;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeModule;
import org.matsim.contrib.dvrp.run.ModalProviders;
import org.matsim.core.router.RoutingModule;

import java.util.HashSet;
import java.util.Set;

public class SubstituteWithDRTWithinBannedAreaModule extends AbstractDvrpModeModule {


	private final DrtConfigGroup drtCfg;
	private final String bannedMode;
	private final String areaShape;

	public SubstituteWithDRTWithinBannedAreaModule(String modeBannedWithinArea, String pathToBannedAreaShape, DrtConfigGroup drtCfg) {
		super(drtCfg.getMode());
		this.drtCfg = drtCfg;
		this.bannedMode = modeBannedWithinArea;
		this.areaShape = pathToBannedAreaShape;
	}

	@Override
	public void install() {

		Provider<RoutingModule> drtRoutingModuleProvider = binder().getProvider(Key.get(RoutingModule.class, Names.named(drtCfg.getMode())));

//		addRoutingModuleBinding(bannedMode).toProvider(new ModalProviders.AbstractProvider<>(getMode()) {
//			@Override
//			public RoutingModule get() {
//
//				RoutingModule dvrpRoutingModule = drtRoutingModuleProvider.get();
//				return new BannedAreaDRTSubstitutionNetworkRoutingProvider(bannedMode,drtCfg.getMode(), (DvrpRoutingModule) dvrpRoutingModule).get();
//			}
//		});

		bind(BannedAreaLinkProvider.class).toProvider(new Provider<BannedAreaLinkProvider>() {
			@Override
			public BannedAreaLinkProvider get() {
				Set<String> bannedModes = new HashSet<>();
				bannedModes.add(bannedMode);
				return new ShapeFileBasedBannedAreaLinkProvider(areaShape,bannedModes, 0d, 36*3600d );
			}
		});

		addRoutingModuleBinding(bannedMode).toProvider(new BannedAreaDRTSubstitutionNetworkRoutingProvider(bannedMode,drtCfg.getMode(), drtRoutingModuleProvider));


	}
}
