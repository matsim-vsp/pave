package privateAV.vehicle;

import java.util.LinkedList;
import java.util.Queue;

import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicleImpl;
import org.matsim.contrib.dvrp.fleet.DvrpVehicleSpecification;

import com.google.common.base.MoreObjects;

public class PFAVehicle extends DvrpVehicleImpl {

    Queue<MustReturnLinkTimePair> mustReturnToOwnerLinkTimePairs;

    //TODO: actually, the ownerActEndTimes are now contained twice, once in the specification, once in the vehicle.
    // at least for debugging that is nice, but maybe the list should be removed from here..
    public PFAVehicle(PFAVSpecification specification, Link startLink) {
        super(specification, startLink);
        this.mustReturnToOwnerLinkTimePairs = new LinkedList<>();
        this.mustReturnToOwnerLinkTimePairs.addAll(specification.mustReturnToOwnerLinkTimePairs);
    }

    public static DvrpVehicleImpl createWithLinkProvider(DvrpVehicleSpecification specification, Link startLink) {
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
}
