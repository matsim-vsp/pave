package org.matsim.drt;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Identifiable;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.schedule.Task;

import java.util.PriorityQueue;

class DrtBlockingRequest implements Request {

    private final Id<Request> id;
    private final double endTime;
    private final double startTime;
    private final double submissionTime;

    private PriorityQueue<Task> tasks;

    DrtBlockingRequest(final Id<Request> id, final double submissionTime, final double startTime, final double endTime, PriorityQueue<Task> dvrpVehicleTasks) {
        this.id = id;
        this.submissionTime = submissionTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tasks = dvrpVehicleTasks;
    }

    @Override
    public Id<Request> getId() {
        return id;
    }

    @Override
    public double getSubmissionTime() {
        return this.submissionTime;
    }

    double getEndTime() {
        return this.endTime;
    }

    double getStartTime() {
        return this.startTime;
    }

    PriorityQueue<Task> getTasks() {
        return this.tasks;
    }
}
