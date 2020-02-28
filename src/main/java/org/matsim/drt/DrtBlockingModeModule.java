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

package org.matsim.drt;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeModule;
import org.matsim.contrib.dvrp.run.ModalProviders;
import org.matsim.contrib.dvrp.trafficmonitoring.DvrpTravelTimeModule;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.router.util.TravelTime;

public class DrtBlockingModeModule extends AbstractDvrpModeModule {

    private final DrtConfigGroup drtCfg;

    public DrtBlockingModeModule(DrtConfigGroup drtCfg) {
        super(drtCfg.getMode());
        this.drtCfg = drtCfg;
    }

    @Override
    public void install() {
        addMobsimListenerBinding().to(BlockingRequestEngine.class);
        addControlerListenerBinding().to(BlockingRequestEngine.class);
        bindModal(BlockingRequestEngine.class).to(BlockingRequestEngine.class);

        bindModal(BlockingRequestCreator.class).toProvider(new ModalProviders.AbstractProvider<BlockingRequestCreator>(getMode()) {
            @Inject
            @Named(DvrpTravelTimeModule.DVRP_ESTIMATED)
            private TravelTime travelTime;

            @Override
            public BlockingRequestCreator get() {
                return new FreightBlockingRequestCreator(getModalInstance(Network.class), travelTime, drtCfg);
            }
        });
    }
}
