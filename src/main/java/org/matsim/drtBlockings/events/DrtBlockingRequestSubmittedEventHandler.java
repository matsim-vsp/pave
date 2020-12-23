package org.matsim.drtBlockings.events;

import org.matsim.core.events.handler.EventHandler;

public interface DrtBlockingRequestSubmittedEventHandler extends EventHandler {
    void handleEvent(DrtBlockingRequestSubmittedEvent event);
}
