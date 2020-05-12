package org.matsim.ovgu.berlin.createTravelTimeMatrix;

import java.io.FileWriter;
import java.io.IOException;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.eventHandling.MatrixEventsHandler;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class Version3_RunSimReadEvents {

	public static void run(String[] tour, boolean runSimulation, boolean writeCSV) {
		Settings settings = new Settings();
		settings.directory += "/Version3_RunSimReadEvents/";

		// Run Simulation for each trip
		if (runSimulation)
			runMatrixTour(settings, tour);

		// Read Events from trips
		if (writeCSV)
			generateCSVMatrix(settings, tour);
	}

	public static double[][] run(String[] tour, String workingDirectory, boolean runSimulation, boolean writeCSV,
			boolean returnDoubleMatrix) {
		Settings settings = new Settings();
		settings.directory = workingDirectory + "/";

		// Run Simulation for each trip
		if (runSimulation)
			runMatrixTour(settings, tour);

		// Read Events from trips
		if (writeCSV)
			generateCSVMatrix(settings, tour);

		double[][] matrix = null;
		if (returnDoubleMatrix)
			matrix = getDoubleMatrix(settings, tour);

		return matrix;
	}

	private static double[][] getDoubleMatrix(Settings settings, String[] tour) {

		// for each element in tour get values for 24 hours
		double[][] matrix = new double[tour.length - 1][24];

		System.out.println("Version3_RunSimReadEvents.generateMatrix() start double matrix calculation");

		for (int i = 0; i < matrix.length; i++)
			matrix[i] = readEventsAsArray(settings.directory + "/" + (i + 1) + "/");

		return matrix;
	}

	private static void runMatrixTour(Settings settings, String[] tour) {
		settings.serviceTime = 0.0;
		String directory = settings.directory;

		for (int x = 1; x < tour.length; x++) {
			settings.depot = tour[x - 1];
			settings.tour = new String[] { tour[x - 1], tour[x] };
			settings.directory = directory + "/" + x + "/";

			new FreightOnlyMatsim(settings);
		}
		settings.directory = directory;
	}

	private static void generateCSVMatrix(Settings settings, String[] tour) {
		try {
			System.out.println("Version3_RunSimReadEvents.generateMatrix() start csv matrix calculation");
			FileWriter csvWriter = new FileWriter(settings.directory + "matrix.csv");
			String head = "from; to; 0; 1; 2; 3; 4; 5; 6; 7; 8; 9; 10; 11; 12; 13; 14; 15; 16; 17; 18; 19; 20; 21; 22; 23;";
			csvWriter.append(head + "\n");

			for (int i = 1; i < tour.length; i++)
				csvWriter.append(readEventsAsString(settings.directory + "/" + i + "/").replace(".", ",") + "\n");

			csvWriter.flush();
			csvWriter.close();
			System.out.println("Version3_RunSimReadEvents.generateMatrix() finished matrix calculation");

		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String readEventsAsString(String eventsDirectory) {
		String eventsFile = eventsDirectory + "/output_events.xml.gz";
		MatrixEventsHandler handler = new MatrixEventsHandler();
		EventsManager manager = EventsUtils.createEventsManager();
		manager.addHandler(handler);
		MatsimEventsReader reader = new MatsimEventsReader(manager);
		reader.readFile(eventsFile);
		return handler.getTravelTimeString();
	}

	private static double[] readEventsAsArray(String eventsDirectory) {
		String eventsFile = eventsDirectory + "/output_events.xml.gz";
		MatrixEventsHandler handler = new MatrixEventsHandler();
		EventsManager manager = EventsUtils.createEventsManager();
		manager.addHandler(handler);
		MatsimEventsReader reader = new MatsimEventsReader(manager);
		reader.readFile(eventsFile);
		return handler.getTravelTimeArray();
	}
}
