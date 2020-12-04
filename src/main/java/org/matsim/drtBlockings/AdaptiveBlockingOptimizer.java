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

package org.matsim.drtBlockings;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.drt.optimizer.DefaultDrtOptimizer;
import org.matsim.contrib.drt.schedule.DrtDriveTask;
import org.matsim.contrib.drt.schedule.DrtStayTask;
import org.matsim.contrib.drt.scheduler.DrtScheduleInquiry;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.dvrp.router.TimeAsTravelDisutility;
import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.dvrp.schedule.Tasks;
import org.matsim.contrib.freight.carrier.CarrierVehicle;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.mobsim.framework.MobsimTimer;
import org.matsim.core.mobsim.framework.events.MobsimBeforeSimStepEvent;
import org.matsim.core.router.FastAStarEuclideanFactory;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.utils.misc.OptionalTime;
import org.matsim.drtBlockings.BlockingOptimizer;
import org.matsim.drtBlockings.DrtBlockingManager;
import org.matsim.drtBlockings.DrtBlockingRequest;
import org.matsim.drtBlockings.DrtBlockingRequestDispatcher;
import org.matsim.drtBlockings.events.DrtBlockingEndedEvent;
import org.matsim.drtBlockings.events.DrtBlockingRequestRejectedEvent;
import org.matsim.drtBlockings.events.DrtBlockingRequestScheduledEvent;
import org.matsim.drtBlockings.events.DrtBlockingRequestSubmittedEvent;
import org.matsim.drtBlockings.tasks.FreightRetoolTask;

import java.util.*;
import java.util.stream.Collectors;

class AdaptiveBlockingOptimizer implements BlockingOptimizer {

    private static final Logger log = Logger.getLogger(BlockingOptimizer.class);


    private final DefaultDrtOptimizer optimizer;
    private final Fleet fleet;

    private final DrtScheduleInquiry scheduleInquiry;
    private final DrtBlockingManager blockingManager;
    private DrtBlockingRequestDispatcher dispatcher;
    private final EventsManager eventsManager;

    private final LeastCostPathCalculator router;
    private final MobsimTimer timer;
    private final TravelTime travelTime;
    Random rnd;

    private final Map<DvrpVehicle, DrtBlockingRequest> blockedVehicles;
    private final Map<DvrpVehicle, DrtBlockingRequest> allVehicles;

    private PriorityQueue<DrtBlockingRequest> blockingRequests;

    AdaptiveBlockingOptimizer(DefaultDrtOptimizer optimizer, Fleet fleet, DrtScheduleInquiry scheduleInquiry, DrtBlockingManager blockingManager,
							  DrtBlockingRequestDispatcher dispatcher, TravelTime travelTime, EventsManager eventsManager, Network modalNetwork, MobsimTimer timer) {
        this.optimizer = optimizer;
        this.fleet = fleet;
        this.scheduleInquiry = scheduleInquiry;
        this.blockingManager = blockingManager;
        this.dispatcher = dispatcher;
        this.eventsManager = eventsManager;

        this.timer = timer;
        this.travelTime = travelTime;
        this.rnd = MatsimRandom.getLocalInstance();
        this.router = new FastAStarEuclideanFactory().createPathCalculator(modalNetwork, new TimeAsTravelDisutility(travelTime),
                travelTime);

        this.blockingRequests = new PriorityQueue<>(Comparator.comparing(DrtBlockingRequest::getSubmissionTime));
        this.blockedVehicles = new HashMap<>();
        this.allVehicles = new HashMap<>();
    }


//    @Override
    public void blockingRequestSubmitted(DrtBlockingRequest drtBlockingRequest) {
        this.blockingRequests.add(drtBlockingRequest);
        eventsManager.processEvent(new DrtBlockingRequestSubmittedEvent(timer.getTimeOfDay(), drtBlockingRequest.getMode(),
                drtBlockingRequest.getId(), drtBlockingRequest.getStartLink().getId(), drtBlockingRequest.getEndLink().getId(),
                drtBlockingRequest.getStartTime(), drtBlockingRequest.getEndTime()));
    }

    @Override
    public void requestSubmitted(Request request) {
        optimizer.requestSubmitted(request);
    }

    @Override
    public void nextTask(DvrpVehicle vehicle) {

        optimizer.nextTask(vehicle);
        //if we do not check for the start time, it might be that the vehicle is idle before the start of the blocking tasks (for instance at iteration start)
        if(this.blockedVehicles.containsKey(vehicle) && this.timer.getTimeOfDay() > this.blockedVehicles.get(vehicle).getStartTime()){
            updateBlocking(vehicle);
        }

    }

    private void updateBlocking(DvrpVehicle vehicle) {
        if(scheduleInquiry.isIdle(vehicle)){ //TODO actually we could unblock the vehicle already when the last retooling has begun. What happens if we call eventsManager.processEvent(futureTime) ?
            //if the blocking request has started and the vehicle is idle then we can unblock the vehicle..
            this.blockedVehicles.remove(vehicle);
            this.allVehicles.remove(vehicle);
            this.blockingManager.unblockVehicleAfterTime(vehicle, timer.getTimeOfDay());
            this.eventsManager.processEvent(
                    new DrtBlockingEndedEvent(timer.getTimeOfDay(), vehicle.getId(), Tasks.getEndLink(vehicle.getSchedule().getCurrentTask()).getId()));
        } else {
            updateBlockingEndTime(vehicle);
        }
    }

    @Override
    public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent e) {
        if(timer.getTimeOfDay() >= LATEST_BLOCKING_SCHEDULING_TIME){
            rejectAllRemainingBlockingRequests();
            return;
        }

        if(idleVehicles.size() / fleet.getVehicles().size() >= minIdleVehicleRatio){
            int nrOfAvailableVehicles = idleVehicles.size() - minIdleVehicleRatio * fleet.getVehicles().size();


            for(int i = 0 ; i < nrOfAvailableVehicles; i++){
                //iterate over blocking requests (sorted by blocking duration - long to short

                // (does current blocking request fit (based on historic fleet occupancy data?))

                //find vehicle to dispatch
                //schedule request

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

            Task previousTask = new DrtDriveTask(pathToReservationStart, DrtDriveTask.TYPE);
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

    private void updateBlockingEndTime(DvrpVehicle vehicle){
        Schedule schedule = vehicle.getSchedule();

        OptionalTime updatedBlockingEndTime = retrieveEndTimeOfBlocking(schedule);
        if(updatedBlockingEndTime.isUndefined()){
            throw new IllegalArgumentException("cannot update the blocking end time for vehicle " + vehicle);
        }
        if(updatedBlockingEndTime.seconds() > this.blockedVehicles.get(vehicle).getEndTime()){
            if(! (blockingManager).extendBlocking(vehicle, updatedBlockingEndTime.seconds())){
                throw new RuntimeException("the blocking for vehicle is supposed to be extended until " + updatedBlockingEndTime.seconds() + "." +
                        " But that is not possible (probably because the maximum amount of blockings would be exceeded." +
                        "Currently, an exception is thrown as freight tours are shall not be interrupted.");
            }
            this.blockedVehicles.get(vehicle).setEndTime(updatedBlockingEndTime.seconds());
        }

    }

    private OptionalTime retrieveEndTimeOfBlocking(Schedule schedule) {
        OptionalTime updatedBlockingEndTime = OptionalTime.undefined();
        for(int i = schedule.getTaskCount() - 1; i >= schedule.getCurrentTask().getTaskIdx(); i--){
            if(schedule.getTasks().get(i) instanceof FreightRetoolTask){
                //end times just got updated by ScheduleTimingUpdater which is called from the delegate optimizer
                return OptionalTime.defined(schedule.getTasks().get(i).getEndTime());
            }
        }
        return updatedBlockingEndTime;
    }
}
