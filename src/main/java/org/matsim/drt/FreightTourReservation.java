package org.matsim.drt;

import org.matsim.api.core.v01.Id;
import org.matsim.contrib.drt.schedule.DrtTask;
import org.matsim.contrib.dvrp.schedule.DriveTask;
import org.matsim.contrib.freight.carrier.ScheduledTour;
import org.matsim.contrib.freight.carrier.Tour;

import java.util.PriorityQueue;

class FreightTourReservation implements Reservation {

    private final Id<Reservation> id;
    private final double reservationValidityEndTime;
    private final double reservationValidityStartTime;
    private final double submissionTime;

    private PriorityQueue<DrtTask> tasks;

    FreightTourReservation(final Id<Reservation> id, final ScheduledTour freightTour, final double submissionTime) {
        this.id = id;
        Tour.Leg lastLeg = ((Tour.Leg) freightTour.getTour().getTourElements().get(freightTour.getTour().getTourElements().size() - 1));
        this.reservationValidityEndTime = lastLeg.getExpectedDepartureTime() + lastLeg.getExpectedTransportTime();
        this.reservationValidityStartTime = freightTour.getTour().getStart().getExpectedArrival();
        this.submissionTime = submissionTime;
        this.tasks = convertFreightTourToDvrpTasks(freightTour);
    }

    private PriorityQueue<DrtTask> convertFreightTourToDvrpTasks(ScheduledTour freightTour) {
        return null;
    }

    @Override
    public double getReservationValidityEndTime() {
        return this.reservationValidityEndTime;
    }

    @Override
    public double getReservationValidityStartTime() {
        for(DrtTask t : this.tasks){
            if (t instanceof DriveTask) return t.getBeginTime();
        }
        throw new IllegalStateException();
    }

    @Override
    public double getSubmissionTime() {
        return this.submissionTime;
    }

    @Override
    public PriorityQueue<DrtTask> getTasks() {
        return this.tasks;
    }

    @Override
    public Id<Reservation> getId() {
        return id;
    }

}
