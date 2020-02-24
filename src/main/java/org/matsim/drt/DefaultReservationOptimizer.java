package org.matsim.drt;

import com.google.inject.name.Named;
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
import org.matsim.core.router.DijkstraFactory;
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
    private final Map<Reservation, DvrpVehicle> reservedVehicles;

    private final LeastCostPathCalculator router;
    private final TravelTime travelTime;


    private PriorityQueue<Reservation> storedReservations;
    private PriorityQueue<Reservation> freshSubmittedReservations;

    Random rnd;

    DefaultReservationOptimizer(DefaultDrtOptimizer optimizer, DrtScheduleInquiry scheduleInquiry, Network modalNetwork, MobsimTimer timer) {
        this.optimizer = optimizer;
        this.scheduleInquiry = scheduleInquiry;
        this.timer = timer;
        
        this.idleVehicles = new ArrayList<>();
        this.reservedVehicles = new TreeMap<>(Comparator.comparing(Reservation::getReservationValidityEndTime));
        this.storedReservations = new PriorityQueue<>(Comparator.comparing(Reservation::getReservationValidityStartTime));
        this.freshSubmittedReservations = new PriorityQueue<>(Comparator.comparing(Reservation::getSubmissionTime));
        this.rnd = MatsimRandom.getLocalInstance();

        this.travelTime = new FreeSpeedTravelTime();
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
        Iterator<Reservation> it = this.reservedVehicles.keySet().iterator();
        while (it.hasNext()){
            Reservation reservation = it.next();
            //makes vehicle available for drt optimizer again.
            //TODO: regarding freight, this could be a problem when freight tour is not finished yet
            if(timer.getTimeOfDay() >= reservation.getReservationValidityEndTime() || scheduleInquiry.isIdle(this.reservedVehicles.get(reservation))) it.remove();
            else break;
        }

        optimizer.notifyMobsimBeforeSimStep(e);

        it = this.storedReservations.iterator();
        while(it.hasNext()){
            Reservation reservation = it.next();
            if(timer.getTimeOfDay() > reservation.getReservationValidityStartTime()){
                log.warn("Reservation " + reservation + " could not be handled before reservation start time = " + reservation.getReservationValidityStartTime() +". It is rejected and deleted");
                //TODO throw RejectedEvent
                //TODO agent stuck???
                it.remove();
            } else {
                if(tryInsertingReservation(reservation)) it.remove();
            }
        }

        it = this.freshSubmittedReservations.iterator();
        while(it.hasNext()){
            Reservation reservation = it.next();
            if(! tryInsertingReservation(reservation)) this.storedReservations.add(reservation);
            it.remove();
        }
    }

    boolean tryInsertingReservation(Reservation reservation){
        if(this.idleVehicles.isEmpty()){
            if(this.storedReservations.contains(reservation)) this.storedReservations.add(reservation);
            return false;
        } else{
            //TODO: find suitable idle vehicle instead of random
            DvrpVehicle vehicle = this.idleVehicles.remove(rnd.nextInt(this.idleVehicles.size()));
            this.reservedVehicles.put(reservation, vehicle);
            scheduleReservation(reservation, vehicle);
            return true;
        }
    }


    @Override
    public void reservationSubmitted(Reservation reservation) {
        freshSubmittedReservations.add(reservation);
    }

    @Override
    public boolean isVehicleReserved(DvrpVehicle vehicle) {
        return this.reservedVehicles.values().contains(vehicle);
    }

    private void scheduleReservation(Reservation reservation, DvrpVehicle vehicle) {
        Schedule schedule = vehicle.getSchedule();
        DrtStayTask stayTask = (DrtStayTask)schedule.getCurrentTask();
        if (stayTask.getTaskIdx() != schedule.getTaskCount() - 1) {
            throw new IllegalStateException("The current STAY task is not last. Not possible without prebooking");
        }
        stayTask.setEndTime(timer.getTimeOfDay()); // finish STAY

        VrpPathWithTravelData pathToReservationStart = VrpPaths.calcAndCreatePath(stayTask.getLink(), Tasks.getBeginLink(reservation.getTasks().peek()), stayTask.getEndTime(), router,
                travelTime);

        Task previousTask = new DrtDriveTask(pathToReservationStart);
        schedule.addTask(previousTask);

        for (Task task : reservation.getTasks()) {
            double duration = task.getEndTime() - task.getBeginTime();
            task.setBeginTime(previousTask.getEndTime());
            task.setEndTime(previousTask.getEndTime() + duration);
            schedule.addTask(task);
            previousTask = task;
        }

        schedule.addTask(new DrtStayTask(previousTask.getEndTime(), vehicle.getServiceEndTime(), Tasks.getEndLink(previousTask)));
    }




}
