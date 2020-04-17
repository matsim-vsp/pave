package org.matsim.ovgu.berlin.evaluation;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.input.InputBuffer;
import org.matsim.ovgu.berlin.eventHandling.TourEventsHandler;
import org.matsim.ovgu.berlin.input.InputExpectedTravelTime;
import org.matsim.ovgu.berlin.input.InputTimeWindows;
import org.matsim.ovgu.berlin.input.InputTour;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class Version1_BufferEvaluation {

	public static void run() {


		run("05M", InputBuffer.buffer05M);
		run("5P", InputBuffer.buffer5P);
		run("SD", InputBuffer.bufferSD);
		run("SDcum", InputBuffer.bufferSDcum);
		run("SDcumStep", InputBuffer.bufferSDcumStep);

		run("1P", InputBuffer.buffer1P);
		run("2P", InputBuffer.buffer2P);
		run("3P", InputBuffer.buffer3P);
		run("4P", InputBuffer.buffer4P);

		run("Min_3P", InputBuffer.bufferMin_3P);
		run("SD_3P", InputBuffer.bufferSD_3P);
		run("SDcum_3P", InputBuffer.bufferSDcum_3P);
		run("SDcumStep_3P", InputBuffer.bufferSDcumStep_3P);

		run("5P_05M", InputBuffer.buffer5P_05M);
		run("SD_05M", InputBuffer.bufferSD_05M);
		run("SDcum_05M", InputBuffer.bufferSDcum_05M);
		run("SDcumStep_05M", InputBuffer.bufferSDcumStep_05M);

	}

	private static void run(String bufferName, double[] usedBuffer) {

		Settings settings = new Settings();
		settings.tour = InputTour.tour;
		settings.depot = InputTour.depot;
		settings.directory += "/evaluation/" + bufferName + "/";
		settings.buffer = usedBuffer;

		String directory = settings.directory;

		runTravelTimeEvaluation(directory + "min", settings, InputExpectedTravelTime.minTT, false);
		runTravelTimeEvaluation(directory + "avg", settings, InputExpectedTravelTime.avgTT, false);
		runTravelTimeEvaluation(directory + "max", settings, InputExpectedTravelTime.maxTT, true);
	}

	private static void runTravelTimeEvaluation(String directory, Settings settings, double[] expectedTravelTime,
			boolean subtractBuffer) {

		settings.subtractBuffer = subtractBuffer;
		settings.expectedTravelTime = expectedTravelTime;
		settings.directory = directory;
		runStandardPremiumEvaluation(settings);

	}

	private static void runStandardPremiumEvaluation(Settings settings) {
		String directory = settings.directory;

		runSimulation(directory + "Standard/", settings, InputTimeWindows.standardTWs);
		runSimulation(directory + "Premium/", settings, InputTimeWindows.premiumTWs);
	}

	private static void runSimulation(String directory, Settings settings, double[] timeWindow) {

		settings.timeWindow = timeWindow;
		settings.directory = directory;
		new FreightOnlyMatsim(settings);
		generateCSV(settings);
	}

	private static void generateCSV(Settings settings) {

		TourEventsHandler handler = new TourEventsHandler();
		EventsManager manager = EventsUtils.createEventsManager();
		manager.addHandler(handler);
		MatsimEventsReader reader = new MatsimEventsReader(manager);

		reader.readFile(settings.directory + "/output_events.xml.gz");
		System.out.println("Events file read!");

		handler.compareExpectedArrivals(settings);
		System.out.println("post calculations finished!");

		handler.printCSV(settings.directory + "result.csv");
		System.out.println("CSV finished!");

	}
}
