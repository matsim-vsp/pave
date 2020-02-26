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
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.core.controler.events.BeforeMobsimEvent;
import org.matsim.core.controler.listener.BeforeMobsimListener;
import org.matsim.core.mobsim.framework.events.MobsimAfterSimStepEvent;
import org.matsim.core.mobsim.framework.listeners.MobsimAfterSimStepListener;

import java.util.Comparator;
import java.util.PriorityQueue;

class BlockingRequestEngine implements BeforeMobsimListener, MobsimAfterSimStepListener {

    @Inject
    private BlockingOptimizer optimizer;
    @Inject
    private BlockingRequestCreator blockingRequestCreator;

    private final PriorityQueue<DrtBlockingRequest> requests = new PriorityQueue<>(Comparator.comparing(Request::getSubmissionTime));

    @Override
    public void notifyBeforeMobsim(BeforeMobsimEvent event) {
        Scenario scenario = event.getServices().getScenario();
        this.requests.clear();
        this.requests.addAll(blockingRequestCreator.createRequestsForIteration(scenario));
    }

    @Override
    public void notifyMobsimAfterSimStep(MobsimAfterSimStepEvent e) {
        while (isReadyForSubmission(requests.peek(), e.getSimulationTime())) {
            optimizer.requestSubmitted(requests.poll());
        }
    }

    private boolean isReadyForSubmission(DrtBlockingRequest request, double currentTime) {
        return request != null && request.getSubmissionTime() <= currentTime;
    }
}
