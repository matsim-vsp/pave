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

import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.drt.optimizer.DefaultDrtOptimizer;
import org.matsim.contrib.drt.schedule.DrtDriveTask;
import org.matsim.contrib.drt.schedule.DrtStayTask;
import org.matsim.contrib.drt.scheduler.DrtScheduleInquiry;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.dvrp.router.TimeAsTravelDisutility;
import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.dvrp.schedule.Tasks;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.mobsim.framework.MobsimTimer;
import org.matsim.core.mobsim.framework.events.MobsimBeforeSimStepEvent;
import org.matsim.core.router.FastAStarEuclideanFactory;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.trafficmonitoring.FreeSpeedTravelTime;

import java.util.*;
import org.apache.log4j.Logger;

class DefaultBlockingOptimizer implements BlockingOptimizer {

    private static final Logger log = Logger.getLogger(BlockingOptimizer.class);


    private final DefaultDrtOptimizer optimizer;

    private final DrtScheduleInquiry scheduleInquiry;
    private final DrtBlockingManager blockingManager;
    private final EventsManager eventsManager;

    private final LeastCostPathCalculator router;
    private final MobsimTimer timer;
    private final TravelTime travelTime;
    Random rnd;

    private final List<DvrpVehicle> idleVehicles;
    private final Map<DrtBlockingRequest, DvrpVehicle> blockedVehicles;

    private PriorityQueue<DrtBlockingRequest> blockingRequests;

    DefaultBlockingOptimizer(DefaultDrtOptimizer optimizer, DrtScheduleInquiry scheduleInquiry, DrtBlockingManager blockingManager,
                             EventsManager eventsManager, Network modalNetwork, MobsimTimer timer) {
        this.optimizer = optimizer;
        this.scheduleInquiry = scheduleInquiry;
        this.blockingManager = blockingManager;
        this.eventsManager = eventsManager;

        this.timer = timer;
        this.travelTime = new FreeSpeedTravelTime(); //TODO USE UPDATED TRAVEL TIMES..
        this.rnd = MatsimRandom.getLocalInstance();
        this.router = new FastAStarEuclideanFactory().createPathCalculator(modalNetwork, new TimeAsTravelDisutility(travelTime),
                travelTime);

        this.idleVehicles = new ArrayList<>();
        this.blockingRequests = new PriorityQueue<>(Comparator.comparing(DrtBlockingRequest::getSubmissionTime));
        this.blockedVehicles = new TreeMap<>(Comparator.comparing(DrtBlockingRequest::getEndTime));
    }


    @Override
    public void blockingRequestSubmitted(DrtBlockingRequest drtBlockingRequest) {
        this.blockingRequests.add(drtBlockingRequest);
    }

    @Override
    public void requestSubmitted(Request request) {
        optimizer.requestSubmitted(request);
    }

    @Override
    public void nextTask(DvrpVehicle vehicle) {

        optimizer.nextTask(vehicle);

        if(scheduleInquiry.isIdle(vehicle)){
            this.idleVehicles.add(vehicle);

            //unblock the vehicle
            this.blockedVehicles.remove(vehicle);
            this.blockingManager.unblockVehicleAfterTime(vehicle, timer.getTimeOfDay());
        } else {
            this.idleVehicles.remove(vehicle);
        }

    }

    @Override
    public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent e) {

        optimizer.notifyMobsimBeforeSimStep(e);

        Iterator<DrtBlockingRequest> blockingRequestsIterator = this.blockingRequests.iterator();
        while(blockingRequestsIterator.hasNext()){
            DrtBlockingRequest drtBlockingRequest = blockingRequestsIterator.next();

            if(drtBlockingRequest.getStartTime() < timer.getTimeOfDay() /* + 1 TODO ???*/){
                log.warn("drt blocking request " + drtBlockingRequest + " could not be assigned in time. It is denied.");
//                eventsManager.processEvent(new BlockingRequestDeniedEvent()); //TODO implement BlockingRequestDeniedEvent
                blockingRequestsIterator.remove();
                //TODO: what to do with the unassigned blocking tasks (=freight tour) ? ReSubmit? RePlan?
            } else{
                if(!this.idleVehicles.isEmpty()){

                    //TODO dispatch a suitable vehicle instead of any random one..
                    DvrpVehicle vehicle = this.idleVehicles.get(rnd.nextInt(this.idleVehicles.size()));
                    if(blockingManager.blockVehicleIfPossible(vehicle, drtBlockingRequest.getStartTime(), drtBlockingRequest.getEndTime())){

                        this.idleVehicles.remove(vehicle);
                        scheduleTasksForBlockedVehicle(drtBlockingRequest, vehicle);
//                        eventsManager.processEvent(new BlockingRequestAcceptedEvent()); //TODO BlockingRequestAcceptedEvent
                        blockingRequestsIterator.remove();
                    }
                } else{
                    break;
                }
            }
        }
    }

    private void scheduleTasksForBlockedVehicle(DrtBlockingRequest drtBlockingRequest, DvrpVehicle vehicle) {
            Schedule schedule = vehicle.getSchedule();
            DrtStayTask stayTask = (DrtStayTask)schedule.getCurrentTask();
            if (stayTask.getTaskIdx() != schedule.getTaskCount() - 1) {
                throw new IllegalStateException("The current STAY task is not last. Not possible without prebooking");
            }
            stayTask.setEndTime(timer.getTimeOfDay()); // finish STAY

            VrpPathWithTravelData pathToReservationStart = VrpPaths.calcAndCreatePath(stayTask.getLink(), Tasks.getBeginLink(drtBlockingRequest.getTasks().get(0)), stayTask.getEndTime(), router,
                    travelTime);

            Task previousTask = new DrtDriveTask(pathToReservationStart);
            schedule.addTask(previousTask);

            for (Task task : drtBlockingRequest.getTasks()) {
                double duration = task.getEndTime() - task.getBeginTime();
                task.setBeginTime(previousTask.getEndTime());
                task.setEndTime(previousTask.getEndTime() + duration);
                schedule.addTask(task);
                previousTask = task;
            }

            schedule.addTask(new DrtStayTask(previousTask.getEndTime(), vehicle.getServiceEndTime(), Tasks.getEndLink(previousTask)));
    }
}
