package org.matsim.ovgu.berlin.createTravelTimeMatrix;

import java.io.FileWriter;
import java.io.IOException;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.ovgu.berlin.Input;
import org.matsim.ovgu.berlin.eventHandling.MatrixEventsHandler;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class Version3_RunSimReadEvents {

	public static void run() {
		// Run Simulation for each trip
		runMatrixTour(Input.pathChangeEvents, Input.directory);
		
		// Read Events from trips
		generateMatrix();
	}

	private static void runMatrixTour(String pathChangeEvents, String pathOutput) {

		for (int x = 1; x < Input.tour.length; x++) {
			String depot = Input.tour[x - 1];
			String[] myTour = new String[] { Input.tour[x - 1], Input.tour[x] };
			double[] myTT = new double[] { Input.avgTT[x - 1], Input.avgTT[x] };
			double[] myTW = new double[] { 0.0, 0.0 };
			double serviceTime = 0.0;
			new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/" + x + "/", myTour, myTT, myTW, serviceTime, depot);
		}
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
}
