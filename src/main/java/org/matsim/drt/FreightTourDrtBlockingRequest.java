package org.matsim.drt;

import org.matsim.api.core.v01.Id;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.freight.carrier.ScheduledTour;
import org.matsim.contrib.freight.carrier.Tour;

import java.util.PriorityQueue;

class FreightTourDrtBlockingRequest implements DrtBlockingRequest {

    private final Id<Request> id;
    private final double endTime;
    private final double startTime;
    private final double submissionTime;

    private PriorityQueue<Task> tasks;

    FreightTourDrtBlockingRequest(final Id<Request> id, final ScheduledTour freightTour, final double submissionTime) {
        this.id = id;
        Tour.Leg lastLeg = ((Tour.Leg) freightTour.getTour().getTourElements().get(freightTour.getTour().getTourElements().size() - 1));
        //TODO add a buffer for start and end time?
        this.endTime = lastLeg.getExpectedDepartureTime() + lastLeg.getExpectedTransportTime();
        this.startTime = freightTour.getTour().getStart().getExpectedArrival();
        this.submissionTime = submissionTime;
        this.tasks = convertFreightTourToDvrpTasks(freightTour);
    }

    private PriorityQueue<Task> convertFreightTourToDvrpTasks(ScheduledTour freightTour) {
        return null;
    }

    @Override
    public double getEndTime() {
        return this.endTime;
    }

    @Override
    public double getStartTime() {
        return this.startTime;
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
    public Id<Request> getId() {
        return id;
    }

}
