package org.matsim.drtBlockings.events;

import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.dvrp.vrpagent.TaskEndedEvent;
import org.matsim.contrib.dvrp.vrpagent.TaskStartedEvent;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.MatsimEventsReader;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class DrtBlockingEventsReaders {

    public static Map<String, MatsimEventsReader.CustomEventMapper> createCustomEventMappers(
            Function<String, Task.TaskType> stringToTaskTypeConverter) {
        return Map.of(DrtBlockingEndedEvent.EVENT_TYPE, DrtBlockingEndedEvent::convert,//
                DrtBlockingRequestRejectedEvent.EVENT_TYPE, DrtBlockingRequestRejectedEvent::convert,//
                DrtBlockingRequestScheduledEvent.EVENT_TYPE, DrtBlockingRequestScheduledEvent::convert,//
                TaskStartedEvent.EVENT_TYPE, e -> TaskStartedEvent.convert(e, stringToTaskTypeConverter),
                TaskEndedEvent.EVENT_TYPE, e -> TaskEndedEvent.convert(e, stringToTaskTypeConverter)
        );

    }

    public static MatsimEventsReader createEventsReader(EventsManager eventsManager,
                                                        Function<String, Task.TaskType> stringToTaskTypeConverter) {
        MatsimEventsReader reader = new MatsimEventsReader(eventsManager);
        createCustomEventMappers(stringToTaskTypeConverter).forEach(reader::addCustomEventMapper);
        return reader;
    }
}
