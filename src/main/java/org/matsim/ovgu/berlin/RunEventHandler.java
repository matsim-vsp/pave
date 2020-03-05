package org.matsim.ovgu.berlin;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.ovgu.berlin.eventHandling.EventsHandler;
import org.matsim.ovgu.berlin.input.Input;

public class RunEventHandler {
	public static void main(String[] args) {

		generateCSV("avgStandard", 1, Input.avgTT, Input.serviceTime, Input.standardTW);
		generateCSV("minStandard", 1, Input.minTT, Input.serviceTime, Input.standardTW);
		generateCSV("maxStandard", 1, Input.maxTT, Input.serviceTime, Input.standardTW);
		
		generateCSV("avgPremium", 1, Input.avgTT, Input.serviceTime, Input.premiumTW);
		generateCSV("minPremium", 1, Input.minTT, Input.serviceTime, Input.premiumTW);
		generateCSV("maxPremium", 1, Input.maxTT, Input.serviceTime, Input.premiumTW);
		
		
		generateCSV("avgStandard", 10, Input.avgTT, Input.serviceTime, Input.standardTW);
		generateCSV("minStandard", 10, Input.minTT, Input.serviceTime, Input.standardTW);
		generateCSV("maxStandard", 10, Input.maxTT, Input.serviceTime, Input.standardTW);
		
		generateCSV("avgPremium", 10, Input.avgTT, Input.serviceTime, Input.premiumTW);
		generateCSV("minPremium", 10, Input.minTT, Input.serviceTime, Input.premiumTW);
		generateCSV("maxPremium", 10, Input.maxTT, Input.serviceTime, Input.premiumTW);
	}

	public static void generateCSV(String name, int percent, double[] expectedTT, double serviceTime, double timewindow) {
		System.out.println("Read " + percent + "pc " + name + " ...");
		String dir = System.getProperty("user.dir");
		String eventsFile = dir + "/OutputKMT/OVGU/" + percent + "pc/" + name + "/output_events.xml.gz";

		EventsHandler handler = new EventsHandler();
		EventsManager manager = EventsUtils.createEventsManager();
		manager.addHandler(handler);
		MatsimEventsReader reader = new MatsimEventsReader(manager);

		reader.readFile(eventsFile);
		System.out.println("Events file read!");

		handler.compareExpectedArrivals(expectedTT, serviceTime, timewindow);
		System.out.println("post calculations finished!");

		handler.printCSV(dir + "/OutputKMT/OVGU/" + percent + "pc/" + name + ".csv");
		System.out.println("CSV finished!");

	}
}
