package org.matsim.drtBlockings.events;

import java.util.Map;
import java.util.Objects;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.Event;
import org.matsim.api.core.v01.events.GenericEvent;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.drtBlockings.DrtBlockingRequest;

public class DrtBlockingRequestSubmittedEvent extends Event {
    public static final String EVENT_TYPE = "DrtBlockingRequest submitted";
    public static final String ATTRIBUTE_VEHICLE = "vehicle";
    public static final String ATTRIBUTE_MODE = "mode";
    public static final String ATTRIBUTE_REQUEST = "request";
    public static final String ATTRIBUTE_FROM_LINK = "fromLink";
    public static final String ATTRIBUTE_TO_LINK = "toLink";

    private final Id<DvrpVehicle> vehicleId;
    private final String mode;
    private final Id<DrtBlockingRequest> requestId;
    private final Id<Link> fromLinkId;
    private final Id<Link> toLinkId;


    public DrtBlockingRequestSubmittedEvent(double time, Id<DvrpVehicle> vehicleId, String mode, Id<DrtBlockingRequest> requestId,
                                            Id<Link> fromLinkId, Id<Link> toLinkId) {
        super(time);
        this.vehicleId = vehicleId;
        this.mode = mode;
        this.requestId = requestId;
        this.fromLinkId = fromLinkId;
        this.toLinkId = toLinkId;
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    public Id<DvrpVehicle> getVehicleId() { return vehicleId;}

    public final Id<Link> getFromLinkId() {
        return fromLinkId;
    }

    public final Id<Link> getToLinkId() {
        return toLinkId;
    }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attr = super.getAttributes();
        attr.put(ATTRIBUTE_VEHICLE, vehicleId + "");
        attr.put(ATTRIBUTE_MODE, mode);
        attr.put(ATTRIBUTE_REQUEST, requestId + "");
        attr.put(ATTRIBUTE_FROM_LINK, fromLinkId + "");
        attr.put(ATTRIBUTE_TO_LINK, toLinkId + "");
        return attr;
    }

    public static DrtBlockingRequestSubmittedEvent convert(GenericEvent event) {
        Map<String, String> attributes = event.getAttributes();
        double time = Double.parseDouble(attributes.get(ATTRIBUTE_TIME));
        Id<DvrpVehicle> vehicleId = Id.create(attributes.get(ATTRIBUTE_VEHICLE), DvrpVehicle.class);
        String mode = Objects.requireNonNull(attributes.get(ATTRIBUTE_MODE));
        Id<DrtBlockingRequest> requestId = Id.create(attributes.get(ATTRIBUTE_REQUEST), DrtBlockingRequest.class);
        Id<Link> fromLinkId = Id.createLinkId(attributes.get(ATTRIBUTE_FROM_LINK));
        Id<Link> toLinkId = Id.createLinkId(attributes.get(ATTRIBUTE_TO_LINK));
        return new DrtBlockingRequestSubmittedEvent(time, vehicleId, mode, requestId, fromLinkId, toLinkId);
    }
}
