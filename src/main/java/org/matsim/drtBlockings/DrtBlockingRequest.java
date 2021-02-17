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
    private final double submissionTime;
    private final Id<Carrier> carrierId;
    private final double duration;
    private final String mode;
    private final double startTime;

    private List<Task> tasks;

    //TODO maybe add CarrierVehId as well?
//    DrtBlockingRequest(final Id<Request> id, final String mode, final Id<Carrier> carrierId,
//                       final double submissionTime, final double startTime, final double endTime,
//                       List<Task> dvrpVehicleTasks) {

    DrtBlockingRequest(Builder builder) {
        this.id = Id.create(builder.id, Request.class);
        this.mode = builder.mode;
        this.submissionTime = builder.submissionTime;
        this.tasks = builder.tasks;
        this.carrierId = builder.carrierId;
        this.duration = builder.duration;
        this.startTime = builder.startTime;
    }

    public static Builder newBuilder() { return new Builder(); }

    public static Builder newBuilder(DrtBlockingRequest copy) {
        Builder builder = new Builder();
        builder.id = Id.create(copy.getId(), Request.class);
        builder.mode = copy.getMode();
        builder.carrierId = copy.getCarrierId();
        builder.submissionTime = copy.getSubmissionTime();
        builder.tasks = copy.getTasks();
        builder.duration = copy.duration;
        builder.startTime = copy.startTime;

        return builder;
    }

    @Override
    public Id<Request> getId() {
        return id;
    }

    @Override
    public double getSubmissionTime() {
        return this.submissionTime;
    }

    double getPlannedBlockingDuration() { return this.duration; }

    List<Task> getTasks() {
        return this.tasks;
    }

    public Link getStartLink() {
        return Tasks.getEndLink(this.tasks.get(0));
    }

    public Link getEndLink() {
        return Tasks.getEndLink(this.tasks.get(tasks.size() - 1));
    }

    public Id<Carrier> getCarrierId() { return carrierId; }

    public String getMode() { return this.mode; }

    public double getStartTime() { return this.startTime; }

    public static final class Builder {

        private Id<Request> id;
        private String mode;
        private Double submissionTime;
        private Double duration;
        private Id<Carrier> carrierId;
        private List<Task> tasks;
        private Double startTime;

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

        public Builder duration(Double val) {
            duration = val;
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

        public Builder startTime(Double val) {
            startTime = val;
            return this;
        }

        public DrtBlockingRequest build() {
            return new DrtBlockingRequest(this);
        }
    }
}
