package org.matsim.drtBlockings;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.drt.passenger.DrtRequest;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.dvrp.schedule.Tasks;
import org.matsim.contrib.freight.carrier.Carrier;

import java.util.List;

public class DrtBlockingRequest implements Request {

    private final Id<Request> id;
    private double endTime;
    private double startTime;
    private final double submissionTime;
    private final Id<Carrier> carrierId;

    private final String mode;

    private List<Task> tasks;

    //TODO maybe add CarrierVehId as well?
//    DrtBlockingRequest(final Id<Request> id, final String mode, final Id<Carrier> carrierId,
//                       final double submissionTime, final double startTime, final double endTime,
//                       List<Task> dvrpVehicleTasks) {

    DrtBlockingRequest(Builder builder) {

//        Builder builder = new Builder();

        this.id = Id.create(builder.id, Request.class);

        this.mode = builder.mode;

        this.submissionTime = builder.submissionTime;
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.tasks = builder.tasks;
        checkStartAndEndTimeConsistency();
        this.carrierId = builder.carrierId;
    }

    public static Builder newBuilder() { return new Builder(); }

    public static Builder newBuilder(DrtBlockingRequest copy) {
        Builder builder = new Builder();
        builder.id = Id.create(copy.getId(), Request.class);
        builder.mode = copy.getMode();
        builder.carrierId = copy.getCarrierId();
        builder.submissionTime = copy.getSubmissionTime();
        builder.startTime = copy.getStartTime();
        builder.endTime = copy.getEndTime();
        builder.tasks = copy.getTasks();

        return builder;
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

    double getBlockingDuration() { return this.endTime - this.startTime; }

    List<Task> getTasks() {
        return this.tasks;
    }

    void setEndTime(double endTime) {
        this.endTime = endTime;
    }

    void setStartTime(double startTime) { this.startTime = startTime; }

    public Link getStartLink() {
        return Tasks.getEndLink(this.tasks.get(0));
    }

    public Link getEndLink() {
        return Tasks.getEndLink(this.tasks.get(tasks.size() - 1));
    }

    public Id<Carrier> getCarrierId() { return carrierId; }

    public String getMode() { return this.mode; }

    public static final class Builder {

        private Id<Request> id;
        private String mode;
        private Double submissionTime;
        private Double startTime;
        private Double endTime;
        private Id<Carrier> carrierId;
        private List<Task> tasks;

        public Builder id(Id<Request> val) {
            id = val;
            return this;
        }

        public  Builder mode(String val) {
            mode = val;
            return this;
        }

        public Builder submissionTime(Double val) {
            submissionTime = val;
            return this;
        }

        public Builder startTime(Double val) {
            startTime = val;
            return this;
        }

        public Builder endTime(Double val) {
            endTime = val;
            return this;
        }

        public Builder carrierId(Id<Carrier> val) {
            carrierId = val;
            return this;
        }

        public Builder tasks(List<Task> val) {
            tasks = val;
            return this;
        }

        public DrtBlockingRequest build() {
            return new DrtBlockingRequest(this);
        }
    }
}
