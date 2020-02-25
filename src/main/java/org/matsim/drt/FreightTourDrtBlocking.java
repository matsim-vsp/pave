package org.matsim.drt;

import org.matsim.api.core.v01.Id;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.freight.carrier.ScheduledTour;
import org.matsim.contrib.freight.carrier.Tour;

import java.util.PriorityQueue;

class FreightTourDrtBlocking implements DrtBlocking {

    private final Id<DrtBlocking> id;
    private final double reservationValidityEndTime;
    private final double reservationValidityStartTime;
    private final double submissionTime;

    private PriorityQueue<Task> tasks;

    FreightTourDrtBlocking(final Id<DrtBlocking> id, final ScheduledTour freightTour, final double submissionTime) {
        this.id = id;
        Tour.Leg lastLeg = ((Tour.Leg) freightTour.getTour().getTourElements().get(freightTour.getTour().getTourElements().size() - 1));
        //TODO add a buffer for start and end time?
        this.reservationValidityEndTime = lastLeg.getExpectedDepartureTime() + lastLeg.getExpectedTransportTime();
        this.reservationValidityStartTime = freightTour.getTour().getStart().getExpectedArrival();
        this.submissionTime = submissionTime;
        this.tasks = convertFreightTourToDvrpTasks(freightTour);
    }

    private PriorityQueue<Task> convertFreightTourToDvrpTasks(ScheduledTour freightTour) {
        return null;
    }

    @Override
    public double getReservationValidityEndTime() {
        return this.reservationValidityEndTime;
    }

    @Override
    public double getReservationValidityStartTime() {
        return this.reservationValidityStartTime;
    }

    @Override
    public double getSubmissionTime() {
        return this.submissionTime;
    }

    @Override
    public PriorityQueue<Task> getTasks() {
        return this.tasks;
    }

    @Override
    public Id<DrtBlocking> getId() {
        return id;
    }

}
