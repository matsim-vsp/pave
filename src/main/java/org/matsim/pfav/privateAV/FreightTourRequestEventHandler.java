package privateAV;

import org.matsim.core.events.handler.EventHandler;

public interface FreightTourRequestEventHandler extends EventHandler {
    void handleEvent(final EventFreightTourScheduled event);

    void handleEvent(final EventFreightTourRequestRejected event);

    void handleEvent(final EventFreightTourCompleted event);
}
