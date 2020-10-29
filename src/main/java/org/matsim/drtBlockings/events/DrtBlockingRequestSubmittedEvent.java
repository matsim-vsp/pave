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
    public static final String ATTRIBUTE_MODE = "mode";
    public static final String ATTRIBUTE_REQUEST = "request";
    public static final String ATTRIBUTE_FROM_LINK = "fromLink";
    public static final String ATTRIBUTE_TO_LINK = "toLink";
    public static final String ATTRIBUTE_BLOCKING_START = "blockingStart";
    public static final String ATTRIBUTE_BLOCKING_END = "blockingEnd";


    private final String mode;
    private final Id<Request> requestId;
    private final Id<Link> fromLinkId;
    private final Id<Link> toLinkId;
    private final Double blockingStart;
    private final Double blockingEnd;


    public DrtBlockingRequestSubmittedEvent(double time, String mode, Id<Request> requestId,
                                            Id<Link> fromLinkId, Id<Link> toLinkId, Double blockingStart, Double blockingEnd) {
        super(time);
        this.mode = mode;
        this.requestId = requestId;
        this.fromLinkId = fromLinkId;
        this.toLinkId = toLinkId;
        this.blockingStart = blockingStart;
        this.blockingEnd = blockingEnd;
    }

    @Override
    public String getEventType() {
        return EVENT_TYPE;
    }

    public final Id<Link> getFromLinkId() {
        return fromLinkId;
    }

    public final Id<Link> getToLinkId() {
        return toLinkId;
    }

    public final Id<Request> getRequestId() { return requestId; }

    public final Double getBlockingStart() { return blockingStart; }

    public final Double getBlockingEnd() { return blockingEnd; }

    @Override
    public Map<String, String> getAttributes() {
        Map<String, String> attr = super.getAttributes();
        attr.put(ATTRIBUTE_MODE, mode);
        attr.put(ATTRIBUTE_REQUEST, requestId + "");
        attr.put(ATTRIBUTE_FROM_LINK, fromLinkId + "");
        attr.put(ATTRIBUTE_TO_LINK, toLinkId + "");
        attr.put(ATTRIBUTE_BLOCKING_START, blockingStart + "");
        attr.put(ATTRIBUTE_BLOCKING_END, blockingEnd + "");
        return attr;
    }

    public static DrtBlockingRequestSubmittedEvent convert(GenericEvent event) {
        Map<String, String> attributes = event.getAttributes();
        double time = Double.parseDouble(attributes.get(ATTRIBUTE_TIME));
        String mode = Objects.requireNonNull(attributes.get(ATTRIBUTE_MODE));
        Id<Request> requestId = Id.create(attributes.get(ATTRIBUTE_REQUEST), Request.class);
        Id<Link> fromLinkId = Id.createLinkId(attributes.get(ATTRIBUTE_FROM_LINK));
        Id<Link> toLinkId = Id.createLinkId(attributes.get(ATTRIBUTE_TO_LINK));
        double blockingStart = Double.parseDouble(attributes.get(ATTRIBUTE_BLOCKING_START));
        double blockingEnd = Double.parseDouble(attributes.get(ATTRIBUTE_BLOCKING_END));

        return new DrtBlockingRequestSubmittedEvent(time, mode, requestId, fromLinkId, toLinkId, blockingStart, blockingEnd);
    }
}
