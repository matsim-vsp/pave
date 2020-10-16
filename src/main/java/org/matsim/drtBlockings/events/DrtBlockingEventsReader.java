package org.matsim.drtBlockings.events;

import com.google.common.collect.Lists;
import org.matsim.contrib.drt.passenger.events.DrtRequestSubmittedEvent;
import org.matsim.contrib.drt.util.DrtEventsReaders;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.dvrp.util.DvrpEventsReaders;
import org.matsim.contrib.dvrp.vrpagent.TaskEndedEvent;
import org.matsim.contrib.dvrp.vrpagent.TaskStartedEvent;
import org.matsim.contrib.taxi.schedule.TaxiTaskType;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.drtBlockings.tasks.FreightDeliveryTask;
import org.matsim.drtBlockings.tasks.FreightDriveTask;
import org.matsim.drtBlockings.tasks.FreightPickupTask;
import org.matsim.drtBlockings.tasks.FreightRetoolTask;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class DrtBlockingEventsReader {


    static final Map<String, Task.TaskType> CUSTOM_TASK_TYPE_MAP = List.of(FreightDeliveryTask.FREIGHT_DELIVERY_TASK_TYPE,
            FreightDriveTask.FREIGHT_DRIVE_TASK_TYPE,
            FreightPickupTask.FREIGHT_PICKUP_TASK_TYPE,
            FreightRetoolTask.RETOOL_TASK_TYPE).stream().collect(toMap(Task.TaskType::name, type -> type));

    public static MatsimEventsReader create(EventsManager eventsManager) {
        MatsimEventsReader reader = new MatsimEventsReader(eventsManager);

        reader.addCustomEventMapper(DrtBlockingEndedEvent.EVENT_TYPE, DrtBlockingEndedEvent::convert);
        reader.addCustomEventMapper(DrtBlockingRequestRejectedEvent.EVENT_TYPE, DrtBlockingRequestRejectedEvent::convert);
        reader.addCustomEventMapper(DrtBlockingRequestScheduledEvent.EVENT_TYPE, DrtBlockingRequestScheduledEvent::convert);
        reader.addCustomEventMapper(DrtRequestSubmittedEvent.EVENT_TYPE, DrtRequestSubmittedEvent::convert);

        Map<String, Task.TaskType> taskTypeMap = CUSTOM_TASK_TYPE_MAP;
        taskTypeMap.putAll(DrtEventsReaders.TASK_TYPE_MAP);
        Map<String, MatsimEventsReader.CustomEventMapper> eventMappers = DvrpEventsReaders.createCustomEventMappers(taskTypeMap::get);
        eventMappers.forEach(reader::addCustomEventMapper);
        return reader;
    }
}
