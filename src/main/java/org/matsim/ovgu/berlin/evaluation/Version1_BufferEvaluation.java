package org.matsim.ovgu.berlin.evaluation;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.ovgu.berlin.Input;
import org.matsim.ovgu.berlin.eventHandling.TourEventsHandler;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class Version1_BufferEvaluation {

	public static void run() {

		run("05M", Input.buffer05M);
		run("5P", Input.buffer5P);
		run("SD", Input.bufferSD);
		run("SDcum", Input.bufferSDcum);
		run("SDcumStep", Input.bufferSDcumStep);
		
		run("1P", Input.buffer1P);
		run("2P", Input.buffer2P);
		run("3P", Input.buffer3P);
		run("4P", Input.buffer4P);
		
		run("Min_3P", Input.bufferMin_3P);
		run("SD_3P", Input.bufferSD_3P);
		run("SDcum_3P", Input.bufferSDcum_3P);
		run("SDcumStep_3P", Input.bufferSDcumStep_3P);

		run("5P_05M", Input.buffer5P_05M);
		run("SD_05M", Input.bufferSD_05M);
		run("SDcum_05M", Input.bufferSDcum_05M);
		run("SDcumStep_05M", Input.bufferSDcumStep_05M);

	}

	private static void run(String bufferName, double[] usedBuffer) {
		Input.usedBuffer = usedBuffer;
		
		Input.subtractBuffer = false;
		runStandardPremiumEvaluation(bufferName, Input.tour, Input.minTT, "min");
		runStandardPremiumEvaluation(bufferName, Input.tour, Input.avgTT, "avg");

		Input.subtractBuffer = true;
		runStandardPremiumEvaluation(bufferName, Input.tour, Input.maxTT, "max");
		
		
		// want work because arrays are not compatible
//		runStandardPremiumEvaluation(bufferName, Input.lTour, Input.lMinTT, "min");
//		runStandardPremiumEvaluation(bufferName, Input.lTour, Input.lAvgTT, "avg");
//		runStandardPremiumEvaluation(bufferName, Input.lTour, Input.lMaxTT, "max");
	}

	private static void runStandardPremiumEvaluation(String bufferName, String[] tour, double[] travelTime,
			String prefix) {
		runSimEvaluation(prefix + "Standard", bufferName, tour, travelTime, Input.standardTWs);
		runSimEvaluation(prefix + "Premium", bufferName, tour, travelTime, Input.premiumTWs);
	}

	private static void runSimEvaluation(String mode, String bufferName, String[] tour, double[] travelTime,
			double[] timewindows) {
		String simFolder = Input.directory + "/" + bufferName + "/" + mode + bufferName;
		new FreightOnlyMatsim(Input.pathChangeEvents, simFolder, tour, travelTime, timewindows);
		generateCSV(simFolder, mode + bufferName, travelTime, timewindows);
	}
	

	private static void generateCSV(String eventsDirectory, String name, double[] expectedTT, double[] timewindows) {

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
