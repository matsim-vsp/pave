package org.matsim.pfav.privateAV;

import org.matsim.core.events.handler.EventHandler;

public interface PFAVWaitTimesListener extends EventHandler {
    void handleEvent(final EventPFAVOwnerWaitsForVehicle event);
}
