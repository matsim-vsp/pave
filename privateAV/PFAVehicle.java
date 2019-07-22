package privateAV;

import com.google.common.base.MoreObjects;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicleImpl;
import org.matsim.contrib.dvrp.fleet.DvrpVehicleSpecification;

import java.util.LinkedList;
import java.util.Queue;

public final class PFAVehicle extends DvrpVehicleImpl {

    private Queue<MustReturnLinkTimePair> mustReturnToOwnerLinkTimePairs;

    //TODO: actually, the ownerActEndTimes are now contained twice, once in the specification, once in the vehicle.
    // at least for debugging that is nice, but maybe the list should be removed from here..
    private PFAVehicle(PFAVSpecification specification, Link startLink) {
        super(specification, startLink);
        this.mustReturnToOwnerLinkTimePairs = new LinkedList<>();
        this.mustReturnToOwnerLinkTimePairs.addAll(specification.mustReturnToOwnerLinkTimePairs);
    }

    static DvrpVehicleImpl createWithLinkProvider(DvrpVehicleSpecification specification, Link startLink) {
        if (specification instanceof PFAVSpecification) {
            PFAVSpecification s = (PFAVSpecification) specification;
            return new PFAVehicle(s, startLink);
        } else {
            //TODO : we could insert a "normal" DvrpVehicle here. Maybe there is a usecase in future where we would want to have a mix of PFAVehicles and DvrpVehicles
            throw new IllegalArgumentException();
        }
    }

    public Queue<MustReturnLinkTimePair> getMustReturnToOwnerLinkTimePairs() {
        return this.mustReturnToOwnerLinkTimePairs;
    }

    @Override
    public String toString() {
        String mustReturnLogs = "";
        for (MustReturnLinkTimePair pair : mustReturnToOwnerLinkTimePairs) {
            mustReturnLogs += pair.toString() + "; ";
        }
        return MoreObjects.toStringHelper(this)
                .add("id", getId())
                .add("startLinkId", getStartLink())
                .add("capacity", getCapacity())
                .add("serviceBeginTime", getStartLink())
                .add("serviceEndTime", getServiceEndTime())
                .add("mustReturnLogs", mustReturnLogs)
                .toString();
    }

    /**
     * i know there is {@link org.matsim.contrib.dvrp.util.LinkTimePair} already, but for the reference
     * to must return location in the PFAVehicle, we can not work with Link but only with Id<Link> since the
     * network cannot be injected into PFAVFleetStatsCalculator (it's logic is run before mobsim).
     * Furthermore, to be sure..
     */
    public static final class MustReturnLinkTimePair implements Comparable<MustReturnLinkTimePair> {

        final double time;
        final Id<Link> linkId;

        MustReturnLinkTimePair(double time, Id<Link> linkId) {
            this.time = time;
            this.linkId = linkId;
        }

        public double getTime() {
            return time;
        }

        public Id<Link> getLinkId() {
            return linkId;
        }

        @Override
        public int compareTo(MustReturnLinkTimePair other) {
            return Double.compare(time, other.time);
        }

        @Override
        public String toString() {
            return "[link=" + linkId + "][time=" + time + "]";
        }
    }
}
