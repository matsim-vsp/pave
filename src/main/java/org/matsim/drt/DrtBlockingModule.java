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

import org.matsim.contrib.drt.optimizer.DefaultDrtOptimizer;
import org.matsim.contrib.drt.optimizer.DrtOptimizer;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.run.DrtModeQSimModule;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeQSimModule;

public class DrtBlockingModule extends AbstractDvrpModeQSimModule {

    private final DrtConfigGroup drtCfg;

    DrtBlockingModule(DrtConfigGroup drtConfigGroup){
        super(drtConfigGroup.getMode());
        this.drtCfg = drtConfigGroup;
    }

    @Override
    protected void configureQSim() {
        install(new DrtModeQSimModule(drtCfg));


        addModalComponent(DrtOptimizer.class, modalProvider(
                getter -> new DefaultBlockingOptimizer(new DefaultDrtOptimizer())

    }
}
