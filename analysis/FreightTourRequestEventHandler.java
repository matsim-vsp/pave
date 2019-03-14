package analysis;

import org.matsim.core.events.handler.EventHandler;
import privateAV.events.FreightTourCompletedEvent;
import privateAV.events.FreightTourRequestDeniedEvent;
import privateAV.events.FreightTourScheduledEvent;

public interface FreightTourRequestEventHandler extends EventHandler {
    void handleEvent(final FreightTourScheduledEvent event);

    void handleEvent(final FreightTourRequestDeniedEvent event);

    void handleEvent(final FreightTourCompletedEvent event);
}
