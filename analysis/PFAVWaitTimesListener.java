package analysis;

import org.matsim.core.events.handler.EventHandler;
import privateAV.events.PFAVOwnerWaitsForVehicleEvent;

public interface PFAVWaitTimesListener extends EventHandler {
    void handleEvent(final PFAVOwnerWaitsForVehicleEvent event);
}
