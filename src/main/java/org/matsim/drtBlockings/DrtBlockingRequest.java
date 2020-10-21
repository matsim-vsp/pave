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
    private final double startTime;
    private final double submissionTime;
    private final Id<Carrier> carrierId;

    private final String mode;
    private final Id<DvrpVehicle> vehicleId;

    private List<Task> tasks;

    //TODO maybe add CarrierVehId as well?
    DrtBlockingRequest(final Id<Request> id, final String mode, final Id<Carrier> carrierId,
                       final double submissionTime, final double startTime, final double endTime,
                       List<Task> dvrpVehicleTasks) {

        Builder builder = new Builder();

        this.id = id;

        this.mode = builder.mode;
        this.vehicleId = builder.vehicleId;

        this.submissionTime = submissionTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.tasks = dvrpVehicleTasks;
        checkStartAndEndTimeConsistency();
        this.carrierId = carrierId;
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

    public Link getStartLink() {
        return Tasks.getEndLink(this.tasks.get(0));
    }

    public Link getEndLink() { return Tasks.getEndLink(this.tasks.get(tasks.size() - 1)); }

    public Id<Carrier> getCarrierId() { return carrierId; }

    public String getMode() { return mode; }

    public Id<DvrpVehicle> getVehicleId() { return vehicleId; }

    public static final class Builder {

        private String mode;
        private Id<DvrpVehicle> vehicleId;

        public  Builder mode(String val) {
            mode = val;
            return this;
        }

        public Builder vehicleId(Id<DvrpVehicle> val) {
            vehicleId = val;
            return this;
        }
    }
}
