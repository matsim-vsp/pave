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
import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.drt.optimizer.DrtOptimizer;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.mobsim.framework.events.MobsimAfterSimStepEvent;
import org.matsim.core.mobsim.framework.events.MobsimInitializedEvent;
import org.matsim.core.mobsim.framework.listeners.MobsimAfterSimStepListener;
import org.matsim.core.mobsim.framework.listeners.MobsimInitializedListener;

import java.util.Comparator;
import java.util.PriorityQueue;

class BlockingRequestEngine implements MobsimInitializedListener, MobsimAfterSimStepListener {

    private Scenario scenario;
    private BlockingOptimizer optimizer;
    private BlockingRequestCreator blockingRequestCreator;

    BlockingRequestEngine(Scenario scenario, BlockingOptimizer optimizer, BlockingRequestCreator blockingRequestCreator) {
        this.scenario = scenario;
        this.optimizer = optimizer;
        this.blockingRequestCreator = blockingRequestCreator;
    }

    private final PriorityQueue<DrtBlockingRequest> requests = new PriorityQueue<>(Comparator.comparing(Request::getSubmissionTime));

    @Override
    public void notifyMobsimInitialized(MobsimInitializedEvent event) {
        this.requests.clear();
        this.requests.addAll(blockingRequestCreator.createBlockingRequests(FreightUtils.getCarriers(scenario)));
    }

    @Override
    public void notifyMobsimAfterSimStep(MobsimAfterSimStepEvent e) {
        while (isReadyForSubmission(requests.peek(), e.getSimulationTime())) {
            optimizer.blockingRequestSubmitted(requests.poll());
        }
    }

    private boolean isReadyForSubmission(DrtBlockingRequest request, double currentTime) {
        return request != null && request.getSubmissionTime() <= currentTime;
    }
}
