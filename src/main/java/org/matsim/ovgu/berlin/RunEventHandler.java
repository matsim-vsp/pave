package org.matsim.ovgu.berlin;

import java.io.FileWriter;
import java.io.IOException;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.ovgu.berlin.eventHandling.MatrixEventsHandler;
import org.matsim.ovgu.berlin.eventHandling.TourEventsHandler;

public class RunEventHandler {
	public static void main(String[] args) {
		RunEventHandler.generateMatrix();
	}

	private static void generateMatrix() {
		String dir = System.getProperty("user.dir");
		try {
			FileWriter csvWriter = new FileWriter(dir + "/OutputKMT/OVGU/10pc/Matrix/Matrix.csv");
			String head = "from; to; 0; 1; 2; 3; 4; 5; 6; 7; 8; 9; 10; 11; 12; 13; 14; 15; 16; 17; 18; 19; 20; 21; 22; 23;";
			csvWriter.append(head + "\n");

			for (int i = 1; i < 20; i++)
				csvWriter.append(readEvents(i, dir).replace(".", ",") + "\n");

			csvWriter.flush();
			csvWriter.close();

		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String readEvents(int number, String dir) {
		String eventsFile = dir + "/OutputKMT/OVGU/10pc/Matrix/" + number + "/output_events.xml.gz";
		MatrixEventsHandler handler = new MatrixEventsHandler();
		EventsManager manager = EventsUtils.createEventsManager();
		manager.addHandler(handler);
		MatsimEventsReader reader = new MatsimEventsReader(manager);
		reader.readFile(eventsFile);
		return handler.getTravelTimeString();
	}

	public static void generateCSV(String eventsDirectory, String name, double[] expectedTT, double[] timewindows) {

		String dir = System.getProperty("user.dir");
		String eventsFile = dir + "/" + eventsDirectory + "/output_events.xml.gz";

		TourEventsHandler handler = new TourEventsHandler();
		EventsManager manager = EventsUtils.createEventsManager();
		manager.addHandler(handler);
		MatsimEventsReader reader = new MatsimEventsReader(manager);

		reader.readFile(eventsFile);
		System.out.println("Events file read!");

		handler.compareExpectedArrivals(expectedTT, timewindows);
		System.out.println("post calculations finished!");

		handler.printCSV(dir + "/" + eventsDirectory + "/../" + name + ".csv");
		System.out.println("CSV finished!");

	}
}
