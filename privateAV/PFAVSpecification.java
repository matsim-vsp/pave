package privateAV;

import com.google.common.base.MoreObjects;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.DvrpVehicleSpecification;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * this is basically a copy of the final class {@link org.matsim.contrib.dvrp.fleet.ImmutableDvrpVehicleSpecification} with the addition of
 * a list of the activity end times of the vehicle owner for the dispatch of freight tours
 *
 * @author tschlenther
 */
class PFAVSpecification implements DvrpVehicleSpecification {

    private final Id<DvrpVehicle> id;
    private final Id<Link> startLinkId;
    private final int capacity;

    // time window
    private final double serviceBeginTime;
    private final double serviceEndTime;
    public LinkedList<PFAVehicle.MustReturnLinkTimePair> mustReturnToOwnerLinkTimePairs;

    private PFAVSpecification(PFAVSpecification.Builder builder) {
        id = Objects.requireNonNull(builder.id);
        startLinkId = Objects.requireNonNull(builder.startLinkId);
        capacity = Objects.requireNonNull(builder.capacity);
        serviceBeginTime = Objects.requireNonNull(builder.serviceBeginTime);
        serviceEndTime = Objects.requireNonNull(builder.serviceEndTime);
        mustReturnToOwnerLinkTimePairs = Objects.requireNonNull(builder.mustReturnToOwnerLinkTimePairs);
    }

    public static PFAVSpecification.Builder newBuilder() {
        return new PFAVSpecification.Builder();
    }

    public static PFAVSpecification.Builder newBuilder(PFAVSpecification copy) {
        PFAVSpecification.Builder builder = new PFAVSpecification.Builder();
        builder.id = copy.getId();
        builder.startLinkId = copy.getStartLinkId();
        builder.capacity = copy.getCapacity();
        builder.serviceBeginTime = copy.getServiceBeginTime();
        builder.serviceEndTime = copy.getServiceEndTime();
        builder.mustReturnToOwnerLinkTimePairs = copy.mustReturnToOwnerLinkTimePairs;
        return builder;
    }

    @Override
    public Id<DvrpVehicle> getId() {
        return id;
    }

    @Override
    public Id<Link> getStartLinkId() {
        return startLinkId;
    }

    @Override
    public int getCapacity() {
        return capacity;
    }

    @Override
    public double getServiceBeginTime() {
        return serviceBeginTime;
    }

    @Override
    public double getServiceEndTime() {
        return serviceEndTime;
    }

    public LinkedList<PFAVehicle.MustReturnLinkTimePair> getMustReturnToOwnerLinkTimePairs() {
        return mustReturnToOwnerLinkTimePairs;
    }

    @Override
    public String toString() {
        String mustReturnLogs = "";
        for (PFAVehicle.MustReturnLinkTimePair pair : mustReturnToOwnerLinkTimePairs) {
            mustReturnLogs += pair.toString() + "; ";
        }
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("startLinkId", startLinkId)
                .add("capacity", capacity)
                .add("serviceBeginTime", serviceBeginTime)
                .add("serviceEndTime", serviceEndTime)
                .add("mustReturnLogs", mustReturnLogs)
                .toString();
    }

    public static final class Builder {
        public Queue<Double> actEndTimes;
        public LinkedList<PFAVehicle.MustReturnLinkTimePair> mustReturnToOwnerLinkTimePairs;
        private Id<DvrpVehicle> id;
        private Id<Link> startLinkId;
        private Integer capacity;
        private Double serviceBeginTime;
        private Double serviceEndTime;

        private Builder() {
        }

        public PFAVSpecification.Builder id(Id<DvrpVehicle> val) {
            id = val;
            return this;
        }

        public PFAVSpecification.Builder startLinkId(Id<Link> val) {
            startLinkId = val;
            return this;
        }

        public PFAVSpecification.Builder capacity(int val) {
            capacity = val;
            return this;
        }

        public PFAVSpecification.Builder serviceBeginTime(double val) {
            serviceBeginTime = val;
            return this;
        }

        public PFAVSpecification.Builder serviceEndTime(double val) {
            serviceEndTime = val;
            return this;
        }

        public PFAVSpecification.Builder actEndTimes(Queue<Double> list) {
            actEndTimes = list;
            return this;
        }

        public PFAVSpecification.Builder mustReturnToOwnerLinkTimePairs(LinkedList<PFAVehicle.MustReturnLinkTimePair> list) {
            mustReturnToOwnerLinkTimePairs = list;
            return this;
        }

        public PFAVSpecification build() {
            return new PFAVSpecification(this);
        }
    }
}
