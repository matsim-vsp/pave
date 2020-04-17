package org.matsim.ovgu.berlin.evaluation;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.eventHandling.TourEventsHandler;
import org.matsim.ovgu.berlin.input.InputBuffer;
import org.matsim.ovgu.berlin.input.InputExpectedTravelTime;
import org.matsim.ovgu.berlin.input.InputTimeWindows;
import org.matsim.ovgu.berlin.input.InputTour;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class Version2_SITWA_Evaluation {
	private static Settings settings = new Settings();
	private static String baseDirectory = settings.directory + "/evaluation/SITWA/";
	
	public static void run() {
		settings.tour = InputTour.tour;
		settings.depot = InputTour.depot;
		settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;

		run("BASE_1W", InputBuffer.bufferNone, InputTimeWindows.all1Min);
		run("BASE_2W", InputBuffer.bufferNone, InputTimeWindows.all2Min);
		run("BASE_3W", InputBuffer.bufferNone, InputTimeWindows.all3Min);
		run("BASE_4W", InputBuffer.bufferNone, InputTimeWindows.all4Min);
		run("BASE_5W", InputBuffer.bufferNone, InputTimeWindows.all5Min);
		run("BASE_6W", InputBuffer.bufferNone, InputTimeWindows.all6Min);
		run("BASE_7W", InputBuffer.bufferNone, InputTimeWindows.all7Min);
		run("BASE_8W", InputBuffer.bufferNone, InputTimeWindows.all8Min);
		run("BASE_9W", InputBuffer.bufferNone, InputTimeWindows.all9Min);
		run("BASE_10W", InputBuffer.bufferNone, InputTimeWindows.all10Min);

		run("SITWA_1W", InputBuffer.bufferNewSITWA_1W, InputTimeWindows.all1Min);
		run("SITWA_2W", InputBuffer.bufferNewSITWA_2W, InputTimeWindows.all2Min);
		run("SITWA_3W", InputBuffer.bufferNewSITWA_3W, InputTimeWindows.all3Min);
		run("SITWA_4W", InputBuffer.bufferNewSITWA_4W, InputTimeWindows.all4Min);
		run("SITWA_5W", InputBuffer.bufferNewSITWA_5W, InputTimeWindows.all5Min);
		run("SITWA_6W", InputBuffer.bufferNewSITWA_6W, InputTimeWindows.all6Min);
		run("SITWA_7W", InputBuffer.bufferNewSITWA_7W, InputTimeWindows.all7Min);
		run("SITWA_8W", InputBuffer.bufferNewSITWA_8W, InputTimeWindows.all8Min);
		run("SITWA_9W", InputBuffer.bufferNewSITWA_9W, InputTimeWindows.all9Min);
		run("SITWA_10W", InputBuffer.bufferNewSITWA_10W, InputTimeWindows.all10Min);

		run("6P_1W", InputBuffer.bufferNew6P, InputTimeWindows.all1Min);
		run("6P_2W", InputBuffer.bufferNew6P, InputTimeWindows.all2Min);
		run("6P_3W", InputBuffer.bufferNew6P, InputTimeWindows.all3Min);
		run("6P_4W", InputBuffer.bufferNew6P, InputTimeWindows.all4Min);
		run("6P_5W", InputBuffer.bufferNew6P, InputTimeWindows.all5Min);
		run("6P_6W", InputBuffer.bufferNew6P, InputTimeWindows.all6Min);
		run("6P_7W", InputBuffer.bufferNew6P, InputTimeWindows.all7Min);
		run("6P_8W", InputBuffer.bufferNew6P, InputTimeWindows.all8Min);
		run("6P_9W", InputBuffer.bufferNew6P, InputTimeWindows.all9Min);
		run("6P_10W", InputBuffer.bufferNew6P, InputTimeWindows.all10Min);
		
		run("SD_1W", InputBuffer.bufferNewSD, InputTimeWindows.all1Min);
		run("SD_2W", InputBuffer.bufferNewSD, InputTimeWindows.all2Min);
		run("SD_3W", InputBuffer.bufferNewSD, InputTimeWindows.all3Min);
		run("SD_4W", InputBuffer.bufferNewSD, InputTimeWindows.all4Min);
		run("SD_5W", InputBuffer.bufferNewSD, InputTimeWindows.all5Min);
		run("SD_6W", InputBuffer.bufferNewSD, InputTimeWindows.all6Min);
		run("SD_7W", InputBuffer.bufferNewSD, InputTimeWindows.all7Min);
		run("SD_8W", InputBuffer.bufferNewSD, InputTimeWindows.all8Min);
		run("SD_9W", InputBuffer.bufferNewSD, InputTimeWindows.all9Min);
		run("SD_10W", InputBuffer.bufferNewSD, InputTimeWindows.all10Min);

		run("SITWA_1W_40", InputBuffer.bufferSITWA_1W_40, InputTimeWindows.all1Min);
		run("SITWA_2W_40", InputBuffer.bufferSITWA_2W_40, InputTimeWindows.all2Min);
		run("SITWA_3W_40", InputBuffer.bufferSITWA_3W_40, InputTimeWindows.all3Min);
		run("SITWA_4W_40", InputBuffer.bufferSITWA_4W_40, InputTimeWindows.all4Min);
		run("SITWA_5W_40", InputBuffer.bufferSITWA_5W_40, InputTimeWindows.all5Min);
		run("SITWA_6W_40", InputBuffer.bufferSITWA_6W_40, InputTimeWindows.all6Min);
		run("SITWA_7W_40", InputBuffer.bufferSITWA_7W_40, InputTimeWindows.all7Min);
		run("SITWA_8W_40", InputBuffer.bufferSITWA_8W_40, InputTimeWindows.all8Min);
		run("SITWA_9W_40", InputBuffer.bufferSITWA_9W_40, InputTimeWindows.all9Min);
		run("SITWA_10W_40", InputBuffer.bufferSITWA_10W_40, InputTimeWindows.all10Min);

		run("SITWA_1W_405", InputBuffer.bufferSITWA_1W_405, InputTimeWindows.all1Min);
		run("SITWA_2W_405", InputBuffer.bufferSITWA_2W_405, InputTimeWindows.all2Min);
		run("SITWA_3W_405", InputBuffer.bufferSITWA_3W_405, InputTimeWindows.all3Min);
		run("SITWA_4W_405", InputBuffer.bufferSITWA_4W_405, InputTimeWindows.all4Min);
		run("SITWA_5W_405", InputBuffer.bufferSITWA_5W_405, InputTimeWindows.all5Min);
		run("SITWA_6W_405", InputBuffer.bufferSITWA_6W_405, InputTimeWindows.all6Min);
		run("SITWA_7W_405", InputBuffer.bufferSITWA_7W_405, InputTimeWindows.all7Min);
		run("SITWA_8W_405", InputBuffer.bufferSITWA_8W_405, InputTimeWindows.all8Min);
		run("SITWA_9W_405", InputBuffer.bufferSITWA_9W_405, InputTimeWindows.all9Min);
		run("SITWA_10W_405", InputBuffer.bufferSITWA_10W_405, InputTimeWindows.all10Min);

		run("SITWA_1W_41", InputBuffer.bufferSITWA_1W_41, InputTimeWindows.all1Min);
		run("SITWA_2W_41", InputBuffer.bufferSITWA_2W_41, InputTimeWindows.all2Min);
		run("SITWA_3W_41", InputBuffer.bufferSITWA_3W_41, InputTimeWindows.all3Min);
		run("SITWA_4W_41", InputBuffer.bufferSITWA_4W_41, InputTimeWindows.all4Min);
		run("SITWA_5W_41", InputBuffer.bufferSITWA_5W_41, InputTimeWindows.all5Min);
		run("SITWA_6W_41", InputBuffer.bufferSITWA_6W_41, InputTimeWindows.all6Min);
		run("SITWA_7W_41", InputBuffer.bufferSITWA_7W_41, InputTimeWindows.all7Min);
		run("SITWA_8W_41", InputBuffer.bufferSITWA_8W_41, InputTimeWindows.all8Min);
		run("SITWA_9W_41", InputBuffer.bufferSITWA_9W_41, InputTimeWindows.all9Min);
		run("SITWA_10W_41", InputBuffer.bufferSITWA_10W_41, InputTimeWindows.all10Min);

		run("SITWA_1W_415", InputBuffer.bufferSITWA_1W_415, InputTimeWindows.all1Min);
		run("SITWA_2W_415", InputBuffer.bufferSITWA_2W_415, InputTimeWindows.all2Min);
		run("SITWA_3W_415", InputBuffer.bufferSITWA_3W_415, InputTimeWindows.all3Min);
		run("SITWA_4W_415", InputBuffer.bufferSITWA_4W_415, InputTimeWindows.all4Min);
		run("SITWA_5W_415", InputBuffer.bufferSITWA_5W_415, InputTimeWindows.all5Min);
		run("SITWA_6W_415", InputBuffer.bufferSITWA_6W_415, InputTimeWindows.all6Min);
		run("SITWA_7W_415", InputBuffer.bufferSITWA_7W_415, InputTimeWindows.all7Min);
		run("SITWA_8W_415", InputBuffer.bufferSITWA_8W_415, InputTimeWindows.all8Min);
		run("SITWA_9W_415", InputBuffer.bufferSITWA_9W_415, InputTimeWindows.all9Min);
		run("SITWA_10W_415", InputBuffer.bufferSITWA_10W_415, InputTimeWindows.all10Min);

		run("SITWA_1W_42", InputBuffer.bufferSITWA_1W_42, InputTimeWindows.all1Min);
		run("SITWA_2W_42", InputBuffer.bufferSITWA_2W_42, InputTimeWindows.all2Min);
		run("SITWA_3W_42", InputBuffer.bufferSITWA_3W_42, InputTimeWindows.all3Min);
		run("SITWA_4W_42", InputBuffer.bufferSITWA_4W_42, InputTimeWindows.all4Min);
		run("SITWA_5W_42", InputBuffer.bufferSITWA_5W_42, InputTimeWindows.all5Min);
		run("SITWA_6W_42", InputBuffer.bufferSITWA_6W_42, InputTimeWindows.all6Min);
		run("SITWA_7W_42", InputBuffer.bufferSITWA_7W_42, InputTimeWindows.all7Min);
		run("SITWA_8W_42", InputBuffer.bufferSITWA_8W_42, InputTimeWindows.all8Min);
		run("SITWA_9W_42", InputBuffer.bufferSITWA_9W_42, InputTimeWindows.all9Min);
		run("SITWA_10W_42", InputBuffer.bufferSITWA_10W_42, InputTimeWindows.all10Min);

		run("SITWA_1W_425", InputBuffer.bufferSITWA_1W_425, InputTimeWindows.all1Min);
		run("SITWA_2W_425", InputBuffer.bufferSITWA_2W_425, InputTimeWindows.all2Min);
		run("SITWA_3W_425", InputBuffer.bufferSITWA_3W_425, InputTimeWindows.all3Min);
		run("SITWA_4W_425", InputBuffer.bufferSITWA_4W_425, InputTimeWindows.all4Min);
		run("SITWA_5W_425", InputBuffer.bufferSITWA_5W_425, InputTimeWindows.all5Min);
		run("SITWA_6W_425", InputBuffer.bufferSITWA_6W_425, InputTimeWindows.all6Min);
		run("SITWA_7W_425", InputBuffer.bufferSITWA_7W_425, InputTimeWindows.all7Min);
		run("SITWA_8W_425", InputBuffer.bufferSITWA_8W_425, InputTimeWindows.all8Min);
		run("SITWA_9W_425", InputBuffer.bufferSITWA_9W_425, InputTimeWindows.all9Min);
		run("SITWA_10W_425", InputBuffer.bufferSITWA_10W_425, InputTimeWindows.all10Min);

		run("SITWA_1W_43", InputBuffer.bufferSITWA_1W_43, InputTimeWindows.all1Min);
		run("SITWA_2W_43", InputBuffer.bufferSITWA_2W_43, InputTimeWindows.all2Min);
		run("SITWA_3W_43", InputBuffer.bufferSITWA_3W_43, InputTimeWindows.all3Min);
		run("SITWA_4W_43", InputBuffer.bufferSITWA_4W_43, InputTimeWindows.all4Min);
		run("SITWA_5W_43", InputBuffer.bufferSITWA_5W_43, InputTimeWindows.all5Min);
		run("SITWA_6W_43", InputBuffer.bufferSITWA_6W_43, InputTimeWindows.all6Min);
		run("SITWA_7W_43", InputBuffer.bufferSITWA_7W_43, InputTimeWindows.all7Min);
		run("SITWA_8W_43", InputBuffer.bufferSITWA_8W_43, InputTimeWindows.all8Min);
		run("SITWA_9W_43", InputBuffer.bufferSITWA_9W_43, InputTimeWindows.all9Min);
		run("SITWA_10W_43", InputBuffer.bufferSITWA_10W_43, InputTimeWindows.all10Min);
	}
	

	private static void run(String bufferName, double[] usedBuffer, double[] timeWindow) {
		settings.directory = baseDirectory + bufferName + "/";
		settings.buffer = usedBuffer;
		settings.timeWindow = timeWindow;
		new FreightOnlyMatsim(settings);
		generateCSV(settings, bufferName);
	}

	private static void generateCSV(Settings settings, String bufferName) {

		TourEventsHandler handler = new TourEventsHandler();
		EventsManager manager = EventsUtils.createEventsManager();
		manager.addHandler(handler);
		MatsimEventsReader reader = new MatsimEventsReader(manager);

		reader.readFile(settings.directory + "/output_events.xml.gz");
		System.out.println("Events file read!");

		handler.compareExpectedArrivals(settings);
		System.out.println("post calculations finished!");

		handler.printCSV(baseDirectory + bufferName + "_result.csv");
		System.out.println("CSV finished!");

	}

}
