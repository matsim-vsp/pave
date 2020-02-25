package org.matsim.drt;

import org.apache.log4j.Logger;
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
import org.matsim.contrib.dvrp.schedule.*;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.mobsim.framework.MobsimTimer;
import org.matsim.core.mobsim.framework.events.MobsimBeforeSimStepEvent;
import org.matsim.core.router.FastAStarEuclideanFactory;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.trafficmonitoring.FreeSpeedTravelTime;

import java.util.*;

class DefaultReservationOptimizer implements ReservationOptimizer {

    Logger log = Logger.getLogger(DefaultReservationOptimizer.class);

    private final DefaultDrtOptimizer optimizer;
    private final DrtScheduleInquiry scheduleInquiry;
    private final MobsimTimer timer;

    private final List<DvrpVehicle> idleVehicles;
    private final Map<DrtBlocking, DvrpVehicle> reservedVehicles;

    private final LeastCostPathCalculator router;
    private final TravelTime travelTime;


    private PriorityQueue<DrtBlocking> storedDrtBlockings;
    private PriorityQueue<DrtBlocking> freshSubmittedDrtBlockings;

    Random rnd;

    DefaultReservationOptimizer(DefaultDrtOptimizer optimizer, DrtScheduleInquiry scheduleInquiry, Network modalNetwork, MobsimTimer timer) {
        this.optimizer = optimizer;
        this.scheduleInquiry = scheduleInquiry;
        this.timer = timer;
        
        this.idleVehicles = new ArrayList<>();
        this.reservedVehicles = new TreeMap<>(Comparator.comparing(DrtBlocking::getReservationValidityEndTime));
        this.storedDrtBlockings = new PriorityQueue<>(Comparator.comparing(DrtBlocking::getReservationValidityStartTime));
        this.freshSubmittedDrtBlockings = new PriorityQueue<>(Comparator.comparing(DrtBlocking::getSubmissionTime));
        this.rnd = MatsimRandom.getLocalInstance();

        this.travelTime = new FreeSpeedTravelTime();

        //TODO
        this.router = new FastAStarEuclideanFactory().createPathCalculator(modalNetwork, new TimeAsTravelDisutility(travelTime),
                travelTime);
    }

     @Override
    public void requestSubmitted(Request request) {
        optimizer.requestSubmitted(request);
    }

    @Override
    public void nextTask(DvrpVehicle vehicle) {
        optimizer.nextTask(vehicle);

        //TODO check if it is a service task and whether service time window is met. otherwise update the schedule.

        if(scheduleInquiry.isIdle(vehicle)){
            this.idleVehicles.add(vehicle);
        } else {
            this.idleVehicles.remove(vehicle);
        }
    }

    @Override
    public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent e) {
        checkReservationValidities();
        optimizer.notifyMobsimBeforeSimStep(e);
        handleStoredReservations();

        Iterator<DrtBlocking> freshReservationsIterator = this.freshSubmittedDrtBlockings.iterator();
        while(freshReservationsIterator.hasNext()){
            DrtBlocking drtBlocking = freshReservationsIterator.next();
            if(! tryInsertingReservation(drtBlocking)) this.storedDrtBlockings.add(drtBlocking);
            freshReservationsIterator.remove();
        }
    }

    void handleStoredReservations() {
        Iterator<DrtBlocking> storedReservationsIterator = this.storedDrtBlockings.iterator();
        while(storedReservationsIterator.hasNext()){
            DrtBlocking drtBlocking = storedReservationsIterator.next();
            if(timer.getTimeOfDay() > drtBlocking.getReservationValidityStartTime()){
                log.warn("Reservation " + drtBlocking + " could not be handled before reservation start time = " + drtBlocking.getReservationValidityStartTime() +". It is rejected and deleted");
                //TODO throw RejectedEvent
                //TODO agent stuck??? => rather let it open a conventional request
                storedReservationsIterator.remove();
            } else {
                if(tryInsertingReservation(drtBlocking)) storedReservationsIterator.remove();
            }
        }
    }

    void checkReservationValidities() {
        Iterator<DrtBlocking> reservedVehiclesIterator = this.reservedVehicles.keySet().iterator();
        while (reservedVehiclesIterator.hasNext()){
            DrtBlocking drtBlocking = reservedVehiclesIterator.next();
            //makes vehicle available for drt optimizer again.
            //at the moment, freight tours = reservation.getTasks() can not be interrupted. This means, we need to wait for the vehicle to complete ALL reservation tasks before unreserving
            if(timer.getTimeOfDay() >= drtBlocking.getReservationValidityEndTime() && scheduleInquiry.isIdle(this.reservedVehicles.get(drtBlocking))) reservedVehiclesIterator.remove();
            else break;
        }
    }

    boolean tryInsertingReservation(DrtBlocking drtBlocking){
        if(this.idleVehicles.isEmpty()){
            return false;
        } else{
            //TODO: find suitable idle vehicle instead of random
            DvrpVehicle vehicle = this.idleVehicles.remove(rnd.nextInt(this.idleVehicles.size()));
            this.reservedVehicles.put(drtBlocking, vehicle);
            scheduleReservation(drtBlocking, vehicle);
            return true;
        }
    }


    @Override
    public void reservationSubmitted(DrtBlocking drtBlocking) {
        freshSubmittedDrtBlockings.add(drtBlocking);
    }

    @Override
    public boolean isVehicleReserved(DvrpVehicle vehicle) {
        return this.reservedVehicles.values().contains(vehicle);
    }

    private void scheduleReservation(DrtBlocking drtBlocking, DvrpVehicle vehicle) {
        Schedule schedule = vehicle.getSchedule();
        DrtStayTask stayTask = (DrtStayTask)schedule.getCurrentTask();
        if (stayTask.getTaskIdx() != schedule.getTaskCount() - 1) {
            throw new IllegalStateException("The current STAY task is not last. Not possible without prebooking");
        }
        stayTask.setEndTime(timer.getTimeOfDay()); // finish STAY

        VrpPathWithTravelData pathToReservationStart = VrpPaths.calcAndCreatePath(stayTask.getLink(), Tasks.getBeginLink(drtBlocking.getTasks().peek()), stayTask.getEndTime(), router,
                travelTime);

        Task previousTask = new DrtDriveTask(pathToReservationStart);
        schedule.addTask(previousTask);

        for (Task task : drtBlocking.getTasks()) {
            double duration = task.getEndTime() - task.getBeginTime();
            task.setBeginTime(previousTask.getEndTime());
            task.setEndTime(previousTask.getEndTime() + duration);
            schedule.addTask(task);
            previousTask = task;
        }

        schedule.addTask(new DrtStayTask(previousTask.getEndTime(), vehicle.getServiceEndTime(), Tasks.getEndLink(previousTask)));
    }




}
