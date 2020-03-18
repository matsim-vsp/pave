package org.matsim.pfav.privateAV;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.Event;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.contrib.freight.carrier.CarrierCapabilities;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsManagerImpl;

import java.util.*;

public class PerformToursInaRowSwitchTest {

	private static String OUTPUT;

	@BeforeClass
	public static void runChessboardScenario() {
		RunChessboardScenarioForTest testRunner = new RunChessboardScenarioForTest(PerformToursInaRowSwitchTest.class, 2,
				CarrierCapabilities.FleetSize.INFINITE);
		OUTPUT = testRunner.getOutputDir();
		testRunner.run();
	}

	@Test
	public final void testFreightTourDispatch() {
		String eventsFile = OUTPUT + "output_events.xml.gz";
		EventsManager events = new EventsManagerImpl();

		PFAVRequestHandler eventHandler = new PFAVRequestHandler();
		events.addHandler(eventHandler);
		PFAVEventsReader reader = new PFAVEventsReader(events);
		reader.readFile(eventsFile);

		double firstDropOffTime = 0;
		double secondPickupTime = 0;

		HashSet<String> vehicles = eventHandler.vehicles;
		Iterator<String> vhlItr = vehicles.iterator();

		while (vhlItr.hasNext()) {

			String vehicle = vhlItr.next();
			List<ActivityStartEvent> dropOffEvent = eventHandler.veh2dropOffs.get(vehicle);
			Iterator<ActivityStartEvent> dropOffEventItr = dropOffEvent.iterator();

			List<ActivityStartEvent> vehPickUpEvent = eventHandler.vehPickUp.get(vehicle);
			Iterator<ActivityStartEvent> vehPickUpEventItr = vehPickUpEvent.iterator();
            List<EventFreightTourScheduled> sheduledEvents = eventHandler.scheduledEvents.get(vehicle);
			int n = 0;
			while (vehPickUpEventItr.hasNext() && dropOffEventItr.hasNext()) {
				if (n != 1) {
					firstDropOffTime = dropOffEventItr.next().getTime();
				}

				ActivityStartEvent vehPickupEvnt = vehPickUpEventItr.next();
				if (n != 0) {
					secondPickupTime = vehPickupEvnt.getTime();

					int tourCount = numberOfTours(firstDropOffTime, secondPickupTime, sheduledEvents);
					Assert.assertTrue(
							"the number of freight tours to be driven between a dropoff and pickup should be equal to 2",
							tourCount == 2);
				}
				n++;
			}

		}

	}

	private int numberOfTours(double firstDropOffTime, double secondPickupTime,
                              List<EventFreightTourScheduled> sheduledEvents) {

		int count = 0;
        Iterator<EventFreightTourScheduled> schdlEvntItr = sheduledEvents.iterator();

		while (schdlEvntItr.hasNext()) {
			double schlTime = schdlEvntItr.next().getTime();
			if (schlTime >= firstDropOffTime && schlTime <= secondPickupTime) {
				count++;
			}
		}
		return count;
	}

	/**
	 * event handler for PerformToursInARowSwitch.class
	 */
	class PFAVRequestHandler implements FreightTourRequestEventHandler, ActivityStartEventHandler {
        Map<String, List<EventFreightTourScheduled>> scheduledEvents = new HashMap<>();
		Map<String, List<ActivityStartEvent>> veh2dropOffs = new HashMap<>();
		Map<String, List<ActivityStartEvent>> vehPickUp = new HashMap<>();
		HashSet<String> vehicles = new HashSet<>();

		Map<String, List<Event>> events = new HashMap<>();

		@Override
        public void handleEvent(EventFreightTourScheduled event) {
            if (scheduledEvents.containsKey(event.getAttributes().get(EventFreightTourScheduled.ATTRIBUTE_VEHICLE))) {
                scheduledEvents.get(event.getAttributes().get(EventFreightTourScheduled.ATTRIBUTE_VEHICLE)).add(event);
                vehicles.add(event.getAttributes().get(EventFreightTourScheduled.ATTRIBUTE_VEHICLE));
			} else {
                List<EventFreightTourScheduled> list = new ArrayList<>();
				list.add(event);
                scheduledEvents.put(event.getAttributes().get(EventFreightTourScheduled.ATTRIBUTE_VEHICLE), list);
                vehicles.add(event.getAttributes().get(EventFreightTourScheduled.ATTRIBUTE_VEHICLE));
			}
		}

		@Override
		public void handleEvent(ActivityStartEvent activityStartEvent) {

			switch (activityStartEvent.getActType()) {

			case PFAVActionCreator.DROPOFF_ACTIVITY_TYPE:
				if (veh2dropOffs.containsKey(activityStartEvent.getPersonId().toString())) {
					veh2dropOffs.get(activityStartEvent.getPersonId().toString()).add(activityStartEvent);
					vehicles.add(activityStartEvent.getPersonId().toString());
					break;
				} else {
					List<ActivityStartEvent> list = new ArrayList<>();
					list.add(activityStartEvent);
					veh2dropOffs.put(activityStartEvent.getPersonId().toString(), list);
					vehicles.add(activityStartEvent.getPersonId().toString());
					break;
				}
			case PFAVActionCreator.PICKUP_ACTIVITY_TYPE:
				if (vehPickUp.containsKey(activityStartEvent.getPersonId().toString())) {
					vehPickUp.get(activityStartEvent.getPersonId().toString()).add(activityStartEvent);
					vehicles.add(activityStartEvent.getPersonId().toString());
					break;
				} else {
					List<ActivityStartEvent> list = new ArrayList<>();
					list.add(activityStartEvent);
					vehPickUp.put(activityStartEvent.getPersonId().toString(), list);
					vehicles.add(activityStartEvent.getPersonId().toString());
					break;
				}
			}
		}

		@Override
        public void handleEvent(EventFreightTourRequestRejected event) {
		}

		@Override
        public void handleEvent(EventFreightTourCompleted event) {
		}
	}

}
