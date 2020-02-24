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

package org.matsim.pfav.privateAV;

import org.matsim.pfav.analysis.OverallTravelTimeAndDistanceListener;
import org.matsim.pfav.analysis.PFAVWaitTimesAnalyzer;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeModule;
import org.matsim.contrib.dvrp.run.QSimScopeObjectListenerModule;

final class PFAVModuleAnalysis extends AbstractDvrpModeModule {

    private final Network network;

    PFAVModuleAnalysis(String mode, Network network) {
        super(mode);
        this.network = network;
    }

    @Override
    public void install() {
        //analysis
        FreightTourDispatchAnalyzer analyzer = new FreightTourDispatchAnalyzer();

        bindModal(FreightTourDispatchAnalyzer.class).toInstance(analyzer);
        addControlerListenerBinding().to(modalKey(FreightTourDispatchAnalyzer.class));
        installQSimModule(QSimScopeObjectListenerModule.createSimplifiedModule(getMode(), Fleet.class, FreightTourDispatchAnalyzer.class));

        addEventHandlerBinding().toInstance(analyzer);

        OverallTravelTimeAndDistanceListener generalListener = new OverallTravelTimeAndDistanceListener(network);
        addEventHandlerBinding().toInstance(generalListener);
        addControlerListenerBinding().toInstance(generalListener);

        PFAVWaitTimesAnalyzer waitTimeHandler = new PFAVWaitTimesAnalyzer();
        addEventHandlerBinding().toInstance(waitTimeHandler);
        addControlerListenerBinding().toInstance(waitTimeHandler);
    }
}
