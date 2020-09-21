package org.matsim.pfav.privateAV;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.events.MatsimEventsReader;

import java.util.*;

public class SortedToDoListTest {

	private static String OUTPUT;
	private int vehicleCapacity = 3;

	@BeforeClass
	public static void runChessboardScenario() {
		RunChessboardScenarioForTest testRunner = new RunChessboardScenarioForTest(SortedToDoListTest.class, 2,
				CarrierCapabilities.FleetSize.INFINITE);
		OUTPUT = testRunner.getOutputDir();
		testRunner.run();
	}

	@Test
	public final void testSortedToDoList() {

		String eventsFile = OUTPUT + "output_events.xml.gz";

		EventsManager events = new EventsManagerImpl();
		ServiceActStartHandler eventHandler = new ServiceActStartHandler();
		events.addHandler(eventHandler);
		MatsimEventsReader reader = PFAVEventsReader.create(events);
		reader.readFile(eventsFile);

		List<Id<CarrierService>> serviceList = eventHandler.lastServices;
		Iterator<Id<CarrierService>> serviceListItr = serviceList.iterator();
		int numberOfService = serviceList.size();
		int count = 1;

		Carriers carriers = new Carriers();

		CarrierPlanXmlReader carrierReader = new CarrierPlanXmlReader(carriers);
		carrierReader.readFile(OUTPUT + "ITERS/it.1/carriers_it1.xml");

		Carrier carrier = carriers.getCarriers().get(Id.create("carrier1", Carrier.class));


		for (int i = 0; i < eventHandler.lastServices.size() - 1 ; i++) {
			CarrierService firstService = carrier.getServices().get(eventHandler.lastServices.get(i));
			Double firstEnd = firstService.getServiceStartTimeWindow().getEnd();

			CarrierService secondService = carrier.getServices().get(eventHandler.lastServices.get(i + 1));
			Double secondEnd = secondService.getServiceStartTimeWindow().getEnd();
			Assert.assertTrue(
					"The end time of the time window of the last service of a tour must be less than the end time of the time window of last service of the very next tour",
					firstEnd <= secondEnd);
		}

	}

	class ServiceActStartHandler implements FreightTourRequestEventHandler, ActivityStartEventHandler {

		List<Id<CarrierService>> lastServices = new LinkedList<>();

		Map<Id<DvrpVehicle>, Id<CarrierService>> lastStartedServiceActsPerVeh = new HashMap<>();
		Set<Id<DvrpVehicle>> vehiclesOnTour = new HashSet<>();

		@Override
		public void handleEvent(ActivityStartEvent event) {
			String actType = event.getActType();
			String activity = "service";
			if (actType.contains(activity)) {
				Id<DvrpVehicle> vehicleId = Id.create(event.getPersonId().toString(), DvrpVehicle.class);
				if(!this.vehiclesOnTour.contains(vehicleId)) throw new RuntimeException("why does somebody start a service act who has not started a tour yet?");

				lastStartedServiceActsPerVeh.put(vehicleId, Id.create(actType.split("_")[1], CarrierService.class));
			}

		}

		@Override
		public void handleEvent(EventFreightTourScheduled event) {
			vehiclesOnTour.add(event.getVehicleId());
		}

		@Override
		public void handleEvent(EventFreightTourRequestRejected event) {

		}

		@Override
		public void handleEvent(EventFreightTourCompleted event) {
			this.lastServices.add(this.lastStartedServiceActsPerVeh.get(event.getVehicleId()));
			this.vehiclesOnTour.remove(event.getVehicleId());
		}
	}
}
