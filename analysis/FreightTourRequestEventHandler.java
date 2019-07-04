package analysis;

import org.matsim.core.events.handler.EventHandler;
import privateAV.events.FreightTourCompletedEvent;
import privateAV.events.FreightTourRequestRejectedEvent;
import privateAV.events.FreightTourScheduledEvent;

public interface FreightTourRequestEventHandler extends EventHandler {
    void handleEvent(final FreightTourScheduledEvent event);

    void handleEvent(final FreightTourRequestRejectedEvent event);

    void handleEvent(final FreightTourCompletedEvent event);
}
