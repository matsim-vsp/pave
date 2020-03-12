package org.matsim.drt;

import org.matsim.api.core.v01.Id;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.schedule.Task;

import java.util.List;

class DrtBlockingRequest implements Request {

    private final Id<Request> id;
    private double endTime;
    private final double startTime;
    private final double submissionTime;

    private List<Task> tasks;

    DrtBlockingRequest(final Id<Request> id, final double submissionTime, final double startTime, final double endTime, List<Task> dvrpVehicleTasks) {
        this.id = id;
        this.submissionTime = submissionTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tasks = dvrpVehicleTasks;
        checkStartAndEndTimeConsistency();
    }

    private void checkStartAndEndTimeConsistency() {
        if(this.startTime > this.tasks.get(0).getBeginTime()){
            throw new IllegalArgumentException("start time of the blocking should be less than or equal to the begin time of the first corresponding task");
        }
        if(this.endTime < this.tasks.get(this.tasks.size() -1).getEndTime()){
            throw new IllegalArgumentException("end time of the blocking should be greater than or equal to the end time of the last corresponding task");
        }
        if(this.submissionTime > this.startTime){
            throw new IllegalArgumentException("submission time should be less than or equal to startTime");
        }
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

    List<Task> getTasks() {
        return this.tasks;
    }

    void setEndTime(double endTime) {
        this.endTime = endTime;
    }
}
