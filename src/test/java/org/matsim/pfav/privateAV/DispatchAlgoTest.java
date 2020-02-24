package org.matsim.pfav.privateAV;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.carrier.CarrierCapabilities;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsManagerImpl;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;

import java.util.ArrayList;
import java.util.List;

public class DispatchAlgoTest {

	private static String OUTPUT;

	@BeforeClass
	public static void runChessboardScenario() {
		RunChessboardScenarioForTest testRunner = new RunChessboardScenarioForTest(DispatchAlgoTest.class, 2,
				CarrierCapabilities.FleetSize.INFINITE);
		OUTPUT = testRunner.getOutputDir();
		testRunner.run();
	}

	@Test
	public final void testDispatchAlgoTest() {

		String eventsFile = OUTPUT + "output_events.xml.gz";
		String networkPath = OUTPUT + "output_network.xml.gz";

		EventsManager events = new EventsManagerImpl();
		PFAVRequestHandler eventHandler = new PFAVRequestHandler();
		events.addHandler(eventHandler);
		PFAVEventsReader reader = new PFAVEventsReader(events);
		reader.readFile(eventsFile);

		Network network = NetworkUtils.createNetwork();
		new MatsimNetworkReader(network).readFile(networkPath);

		List<Id<Link>> freightTourScheduleLink = eventHandler.freightTourScheduleLink;
		List<Id<Link>> retoolLink = eventHandler.retoolLink;

		Id<Link> firstFreigtTourScheduledLink = freightTourScheduleLink.get(0);
		Id<Link> firstRetool = retoolLink.get(0);
		Id<Link> thirdRetool = retoolLink.get(2);

		double firstDepotnDistance = getDistance(network.getLinks().get(firstFreigtTourScheduledLink).getCoord(),
				network.getLinks().get(firstRetool).getCoord());

		double secondDepotDistance = getDistance(network.getLinks().get(firstFreigtTourScheduledLink).getCoord(),
				network.getLinks().get(thirdRetool).getCoord());
		Assert.assertTrue(
				"The euclidean distance from the first dropoff link to the first depot(retool) location should be less than the distance from first dropoff to second depot(retool) location",
				firstDepotnDistance < secondDepotDistance);
	}

	class PFAVRequestHandler implements FreightTourRequestEventHandler, ActivityStartEventHandler {

		// take only values in even position, eg : 0, 2, 4...
		List<Id<Link>> retoolLink = new ArrayList<>();
		List<Id<Link>> freightTourScheduleLink = new ArrayList<>();
		String reTool = "retool";

		@Override
		public void handleEvent(ActivityStartEvent event) {
			if (event.getActType().equals(reTool)) {
				retoolLink.add(event.getLinkId());
			}

		}

		@Override
        public void handleEvent(EventFreightTourScheduled event) {
			if (freightTourScheduleLink.size() < 1) {
				freightTourScheduleLink.add(event.getRequestLink());
				System.out.println("freight tour scheduled request link " + event.getRequestLink());
			}

		}

		@Override
        public void handleEvent(EventFreightTourRequestRejected event) {

		}

		@Override
        public void handleEvent(EventFreightTourCompleted event) {
		}

	}

	double getDistance(Coord coordOne, Coord coordTwo) {

		double distance = NetworkUtils.getEuclideanDistance(coordOne, coordTwo);

		return distance;
	}
}
