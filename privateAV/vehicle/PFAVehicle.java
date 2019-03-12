package privateAV.vehicle;

import com.google.common.base.MoreObjects;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicleImpl;
import org.matsim.contrib.dvrp.fleet.DvrpVehicleSpecification;
import org.matsim.contrib.util.LinkProvider;

import java.util.LinkedList;
import java.util.Queue;

public class PFAVehicle extends DvrpVehicleImpl {

    Queue<Double> ownerActEndTimes;

    //TODO: actually, the ownerActEndTimes are now contained twice, once in the specification, once in the vehicle.
    // at least for debugging that is nice, but maybe the list should be removed from here..
    public PFAVehicle(PFAVSpecification specification, Link startLink) {
        super(specification, startLink);
        this.ownerActEndTimes = new LinkedList<>();
        ownerActEndTimes.addAll(specification.ownerActEndTimes);
    }

    public static DvrpVehicleImpl createWithLinkProvider(DvrpVehicleSpecification specification, LinkProvider<Id<Link>> linkProvider) {
        if (specification instanceof PFAVSpecification) {
            PFAVSpecification s = (PFAVSpecification) specification;
            return new PFAVehicle(s, linkProvider.apply((s).getStartLinkId()));
        } else {
            //TODO : we could insert a "normal" DvrpVehicle here. Maybe there is a usecase in future where we would want to have a mix of PFAVehicles and DvrpVehicles
            throw new RuntimeException("möööööööööööööööööööööööp");
        }
    }

    public Queue<Double> getOwnerActEndTimes() {
        return this.ownerActEndTimes;
    }

    @Override
    public String toString() {
        String endTimes = "";
        for (Double s : ownerActEndTimes) {
            endTimes += s + ";";
        }
        return MoreObjects.toStringHelper(this)
                .add("id", getId())
                .add("startLinkId", getStartLink())
                .add("capacity", getCapacity())
                .add("serviceBeginTime", getStartLink())
                .add("serviceEndTime", getServiceEndTime())
                .add("ownerActEndTimes", endTimes)
                .toString();
    }
}
