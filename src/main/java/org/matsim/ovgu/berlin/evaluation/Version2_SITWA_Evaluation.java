package org.matsim.ovgu.berlin.evaluation;

import java.io.FileWriter;
import java.io.IOException;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.eventHandling.Summary;
import org.matsim.ovgu.berlin.eventHandling.TourEventsHandler;
import org.matsim.ovgu.berlin.input.InputBuffer050520;
import org.matsim.ovgu.berlin.input.InputBufferNew;
import org.matsim.ovgu.berlin.input.InputExpectedTravelTime;
import org.matsim.ovgu.berlin.input.InputOBS;
import org.matsim.ovgu.berlin.input.InputTimeWindows;
import org.matsim.ovgu.berlin.input.InputTour;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class Version2_SITWA_Evaluation {
	private static Settings settings = new Settings();
	private static String baseDirectory = settings.directory + "/evaluation/SITWA-06052020/";
	private static FileWriter csvWriter;

	private static boolean runMATSim = false;
	private static boolean generateResults = true;

	public static void run() {
		settings.tour = InputTour.tour;
		settings.depot = InputTour.depot;

//		settings.expectedTravelTime = InputExpectedTravelTime.newMinTT;
//		settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;
//		settings.timeWindowMethod = "PlusMinusArrival";
//		settings.timeWindowMethod = "AfterArrival";
		run06052020();
	}

	private static void run(String name, double[] usedBuffer, double[] timeWindow) {
		settings.directory = baseDirectory + name + "/";
		settings.buffer = usedBuffer;
		settings.timeWindow = timeWindow;
		if (runMATSim)
			new FreightOnlyMatsim(settings);
		if (generateResults)
			generateCSV(settings, name);
	}

	private static void generateCSV(Settings settings, String name) {

		TourEventsHandler handler = new TourEventsHandler();
		EventsManager manager = EventsUtils.createEventsManager();
		manager.addHandler(handler);
		MatsimEventsReader reader = new MatsimEventsReader(manager);

		reader.readFile(settings.directory + "/output_events.xml.gz");
		System.out.println("Events file read!");

		handler.compareExpectedArrivals(settings);
		System.out.println("post calculations finished!");

		handler.writeCSV(baseDirectory + name + "_result.csv");
		System.out.println("CSV finished!");

		appendSummary(handler.getSummary(), name, settings.buffer);

	}

	private static void appendSummary(Summary summary, String name, double[] usedBuffer) {
		appendNewLine();
		String[] identifiers = name.split("_");
		String str = "";
		if (identifiers.length == 2)
			str += identifiers[0] + ";;;;;;;" + identifiers[1] + ";";
		if (identifiers.length == 7)
			str += identifiers[0] + ";no;" + identifiers[1] + ";" + identifiers[2] + ";" + identifiers[3] + ";"
					+ identifiers[4] + ";" + identifiers[5] + ";" + identifiers[6] + ";";
		if (identifiers.length == 8)
			str += identifiers[0] + ";" + identifiers[1] + ";" + identifiers[2] + ";" + identifiers[3] + ";"
					+ identifiers[4] + ";" + identifiers[5] + ";" + identifiers[6] + ";" + identifiers[7] + ";";

		str += summary.percent_oBeforeTW + ";" + summary.percent_oInTW + ";" + summary.percent_oAfterTW + ";"
				+ summary.avg_oEarlyTW / 60 + ";" + summary.avg_oLateTW / 60 + ";" + summary.percent_dBeforeTW + ";"
				+ summary.percent_dInTW + ";" + summary.percent_dAfterTW + ";" + summary.avg_dEarlyTW / 60 + ";"
				+ summary.avg_dLateTW / 60 + ";" + summary.avg_tourDuration / 60 / 1440 + ";";

		for (double value : usedBuffer)
			str += ";" + value;

		str = str.replace(".", ",");
		try {
			csvWriter.append(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void appendNewLine() {
		if (generateResults)
			try {
				csvWriter.append("\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private static void startCSVwriter(String fileName) {
		if (generateResults)
			try {
				csvWriter = new FileWriter(fileName + ".csv");
				csvWriter.append("type;mod;vers;se;b;t;w;simTW;oBeforeTW;oInTW;oAfterTW;oEarlyTW;oLateTW;"
						+ "dBeforeTW;dInTW;dAfterTW;dEarlyTW;dLateTW;" + "tourDuration");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private static void endCSVwriter() {
		if (generateResults)
			try {
				csvWriter.flush();
				csvWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	private static void run06052020() {
		String rootDirectory = settings.directory;
		baseDirectory = rootDirectory + "/evaluation/SITWA-06052020/OBS/";
		settings.pathChangeEvents = "input/networkChangeEventsFromKai.xml.gz";
		startCSVwriter(baseDirectory + "OBS-Summary" + System.currentTimeMillis());

		runOBS_SD();
		runOBS_A();
		runOBS_B();
		runOBS_C();

		runOBS_SDaa();
		runOBS_Aaa();
		runOBS_Baa();
		runOBS_Caa();

		endCSVwriter();

		baseDirectory = rootDirectory + "/evaluation/SITWA-06052020/A2/";
		settings.pathChangeEvents = "input/scenario-A.15.networkChangeEvents.xml.gz";
		startCSVwriter(baseDirectory + "A2-Summary" + System.currentTimeMillis());

		runSD();
		runA_05052020();
		runB_05052020();
		runC_05052020();

		runSDaa();
		runAaa_05052020();
		runBaa_05052020();
		runCaa_05052020();

		endCSVwriter();

//		settings.expectedTravelTime = InputExpectedTravelTime.newMinTT;
//		settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;
//		settings.timeWindowMethod = "PlusMinusArrival";
//		settings.timeWindowMethod = "AfterArrival";
	}

	private static void runTW1to10(String name, double[][] buffers) {

		run(name + "_W10_1W", buffers[0], InputTimeWindows.all1Min);
		run(name + "_W20_2W", buffers[1], InputTimeWindows.all2Min);
		run(name + "_W30_3W", buffers[2], InputTimeWindows.all3Min);
		run(name + "_W40_4W", buffers[3], InputTimeWindows.all4Min);
		run(name + "_W50_5W", buffers[4], InputTimeWindows.all5Min);
		run(name + "_W60_6W", buffers[5], InputTimeWindows.all6Min);
		run(name + "_W70_7W", buffers[6], InputTimeWindows.all7Min);
		run(name + "_W80_8W", buffers[7], InputTimeWindows.all8Min);
		run(name + "_W90_9W", buffers[8], InputTimeWindows.all9Min);
		run(name + "_W100_10W", buffers[9], InputTimeWindows.all10Min);

	}

	private static void runCaa_05052020() {
		settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;
		settings.timeWindowMethod = "AfterArrival";

		runTW1to10("aaSITWA_x_vC_se1506979167_b10_t1000",
				new double[][] { InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W10,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W20,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W30,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W40,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W50,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W60,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W70,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W80,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W90,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W100 });

		runTW1to10("aaSITWA_x_vC_se1506979167_b10_t3000",
				new double[][] { InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W10,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W20,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W30,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W40,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W50,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W60,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W70,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W80,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W90,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W100 });

		runTW1to10("aaSITWA_x_vC_se1506979167_b10_t5000",
				new double[][] { InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W10,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W20,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W30,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W40,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W50,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W60,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W70,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W80,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W90,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W100 });
	}

	private static void runBaa_05052020() {
		settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;
		settings.timeWindowMethod = "AfterArrival";

		runTW1to10("aaSITWA_x_vB_se1506979167_b10_t1000",
				new double[][] { InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W10,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W20,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W30,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W40,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W50,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W60,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W70,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W80,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W90,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W100 });

		runTW1to10("aaSITWA_x_vB_se1506979167_b10_t3000",
				new double[][] { InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W10,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W20,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W30,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W40,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W50,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W60,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W70,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W80,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W90,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W100 });

		runTW1to10("aaSITWA_x_vB_se1506979167_b10_t5000",
				new double[][] { InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W10,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W20,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W30,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W40,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W50,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W60,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W70,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W80,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W90,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W100 });
	}

	private static void runAaa_05052020() {
		settings.expectedTravelTime = InputExpectedTravelTime.newMinTT;
		settings.timeWindowMethod = "AfterArrival";

		runTW1to10("aaSITWA_x_vA_se137810_b10_t1000",
				new double[][] { InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W10,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W20,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W30,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W40,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W50,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W60,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W70,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W80,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W90,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W100 });

		runTW1to10("aaSITWA_x_vA_se137810_b10_t3000",
				new double[][] { InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W10,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W20,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W30,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W40,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W50,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W60,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W70,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W80,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W90,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W100 });

		runTW1to10("aaSITWA_x_vA_se137810_b10_t5000",
				new double[][] { InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W10,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W20,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W30,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W40,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W50,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W60,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W70,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W80,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W90,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W100 });
	}

	private static void runC_05052020() {
		settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;
		settings.timeWindowMethod = "PlusMinusArrival";

		runTW1to10("SITWA_x_vC_se1506979167_b10_t1000",
				new double[][] { InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W10,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W20,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W30,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W40,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W50,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W60,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W70,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W80,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W90,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t1000_W100 });

		runTW1to10("SITWA_x_vC_se1506979167_b10_t3000",
				new double[][] { InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W10,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W20,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W30,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W40,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W50,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W60,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W70,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W80,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W90,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t3000_W100 });

		runTW1to10("SITWA_x_vC_se1506979167_b10_t5000",
				new double[][] { InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W10,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W20,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W30,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W40,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W50,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W60,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W70,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W80,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W90,
						InputBuffer050520.SITWA_x_vC_se1506979167_b10_t5000_W100 });
	}

	private static void runB_05052020() {
		settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;
		settings.timeWindowMethod = "PlusMinusArrival";

		runTW1to10("SITWA_x_vB_se1506979167_b10_t1000",
				new double[][] { InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W10,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W20,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W30,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W40,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W50,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W60,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W70,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W80,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W90,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t1000_W100 });

		runTW1to10("SITWA_x_vB_se1506979167_b10_t3000",
				new double[][] { InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W10,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W20,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W30,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W40,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W50,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W60,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W70,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W80,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W90,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t3000_W100 });

		runTW1to10("SITWA_x_vB_se1506979167_b10_t5000",
				new double[][] { InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W10,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W20,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W30,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W40,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W50,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W60,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W70,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W80,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W90,
						InputBuffer050520.SITWA_x_vB_se1506979167_b10_t5000_W100 });
	}

	private static void runA_05052020() {
		settings.expectedTravelTime = InputExpectedTravelTime.newMinTT;
		settings.timeWindowMethod = "PlusMinusArrival";

		runTW1to10("SITWA_x_vA_se137810_b10_t1000",
				new double[][] { InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W10,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W20,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W30,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W40,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W50,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W60,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W70,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W80,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W90,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t1000_W100 });

		runTW1to10("SITWA_x_vA_se137810_b10_t3000",
				new double[][] { InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W10,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W20,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W30,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W40,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W50,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W60,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W70,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W80,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W90,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t3000_W100 });

		runTW1to10("SITWA_x_vA_se137810_b10_t5000",
				new double[][] { InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W10,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W20,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W30,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W40,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W50,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W60,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W70,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W80,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W90,
						InputBuffer050520.SITWA_x_vA_se137810_b10_t5000_W100 });
	}

	private static void runSD() {
		settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;
		settings.timeWindowMethod = "PlusMinusArrival";
		run("SD_1W", InputBufferNew.bufferNewSD, InputTimeWindows.all1Min);
		run("SD_2W", InputBufferNew.bufferNewSD, InputTimeWindows.all2Min);
		run("SD_3W", InputBufferNew.bufferNewSD, InputTimeWindows.all3Min);
		run("SD_4W", InputBufferNew.bufferNewSD, InputTimeWindows.all4Min);
		run("SD_5W", InputBufferNew.bufferNewSD, InputTimeWindows.all5Min);
		run("SD_6W", InputBufferNew.bufferNewSD, InputTimeWindows.all6Min);
		run("SD_7W", InputBufferNew.bufferNewSD, InputTimeWindows.all7Min);
		run("SD_8W", InputBufferNew.bufferNewSD, InputTimeWindows.all8Min);
		run("SD_9W", InputBufferNew.bufferNewSD, InputTimeWindows.all9Min);
		run("SD_10W", InputBufferNew.bufferNewSD, InputTimeWindows.all10Min);
	}

	private static void runSDaa() {
		settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;
		settings.timeWindowMethod = "AfterArrival";
		run("aaSD_1W", InputBufferNew.bufferNewSD, InputTimeWindows.all1Min);
		run("aaSD_2W", InputBufferNew.bufferNewSD, InputTimeWindows.all2Min);
		run("aaSD_3W", InputBufferNew.bufferNewSD, InputTimeWindows.all3Min);
		run("aaSD_4W", InputBufferNew.bufferNewSD, InputTimeWindows.all4Min);
		run("aaSD_5W", InputBufferNew.bufferNewSD, InputTimeWindows.all5Min);
		run("aaSD_6W", InputBufferNew.bufferNewSD, InputTimeWindows.all6Min);
		run("aaSD_7W", InputBufferNew.bufferNewSD, InputTimeWindows.all7Min);
		run("aaSD_8W", InputBufferNew.bufferNewSD, InputTimeWindows.all8Min);
		run("aaSD_9W", InputBufferNew.bufferNewSD, InputTimeWindows.all9Min);
		run("aaSD_10W", InputBufferNew.bufferNewSD, InputTimeWindows.all10Min);
	}

	private static void runOBS_SD() {
		settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;
		settings.timeWindowMethod = "PlusMinusArrival";
		run("OBS-SD_1W", InputOBS.SD_buffer, InputTimeWindows.all1Min);
		run("OBS-SD_2W", InputOBS.SD_buffer, InputTimeWindows.all2Min);
		run("OBS-SD_3W", InputOBS.SD_buffer, InputTimeWindows.all3Min);
		run("OBS-SD_4W", InputOBS.SD_buffer, InputTimeWindows.all4Min);
		run("OBS-SD_5W", InputOBS.SD_buffer, InputTimeWindows.all5Min);
		run("OBS-SD_6W", InputOBS.SD_buffer, InputTimeWindows.all6Min);
		run("OBS-SD_7W", InputOBS.SD_buffer, InputTimeWindows.all7Min);
		run("OBS-SD_8W", InputOBS.SD_buffer, InputTimeWindows.all8Min);
		run("OBS-SD_9W", InputOBS.SD_buffer, InputTimeWindows.all9Min);
		run("OBS-SD_10W", InputOBS.SD_buffer, InputTimeWindows.all10Min);
	}

	private static void runOBS_SDaa() {
		settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;
		settings.timeWindowMethod = "AfterArrival";
		run("aaOBS-SD_1W", InputOBS.SD_buffer, InputTimeWindows.all1Min);
		run("aaOBS-SD_2W", InputOBS.SD_buffer, InputTimeWindows.all2Min);
		run("aaOBS-SD_3W", InputOBS.SD_buffer, InputTimeWindows.all3Min);
		run("aaOBS-SD_4W", InputOBS.SD_buffer, InputTimeWindows.all4Min);
		run("aaOBS-SD_5W", InputOBS.SD_buffer, InputTimeWindows.all5Min);
		run("aaOBS-SD_6W", InputOBS.SD_buffer, InputTimeWindows.all6Min);
		run("aaOBS-SD_7W", InputOBS.SD_buffer, InputTimeWindows.all7Min);
		run("aaOBS-SD_8W", InputOBS.SD_buffer, InputTimeWindows.all8Min);
		run("aaOBS-SD_9W", InputOBS.SD_buffer, InputTimeWindows.all9Min);
		run("aaOBS-SD_10W", InputOBS.SD_buffer, InputTimeWindows.all10Min);
	}

	private static void runOBS_C() {
		settings.expectedTravelTime = InputOBS.avgTT;
		settings.timeWindowMethod = "PlusMinusArrival";

		runTW1to10("SITWA_x_OBSvC_se150430_b10_t1000",
				new double[][] { InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W10,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W20, InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W30,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W40, InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W50,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W60, InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W70,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W80, InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W90,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W100 });

		runTW1to10("SITWA_x_OBSvC_se150430_b10_t3000",
				new double[][] { InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W10,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W20, InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W30,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W40, InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W50,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W60, InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W70,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W80, InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W90,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W100 });

		runTW1to10("SITWA_x_OBSvC_se150430_b10_t5000",
				new double[][] { InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W10,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W20, InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W30,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W40, InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W50,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W60, InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W70,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W80, InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W90,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W100 });
	}

	private static void runOBS_B() {
		settings.expectedTravelTime = InputOBS.avgTT;
		settings.timeWindowMethod = "PlusMinusArrival";

		runTW1to10("SITWA_x_OBSvB_se150430_b10_t1000",
				new double[][] { InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W10,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W20, InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W30,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W40, InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W50,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W60, InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W70,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W80, InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W90,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W100 });

		runTW1to10("SITWA_x_OBSvB_se150430_b10_t3000",
				new double[][] { InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W10,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W20, InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W30,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W40, InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W50,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W60, InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W70,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W80, InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W90,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W100 });

		runTW1to10("SITWA_x_OBSvB_se150430_b10_t5000",
				new double[][] { InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W10,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W20, InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W30,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W40, InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W50,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W60, InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W70,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W80, InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W90,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W100 });
	}

	private static void runOBS_A() {
		settings.expectedTravelTime = InputOBS.minTT;
		settings.timeWindowMethod = "PlusMinusArrival";

		runTW1to10("SITWA_x_vA_se137810_b10_t1000",
				new double[][] { InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W10,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W20, InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W30,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W40, InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W50,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W60, InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W70,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W80, InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W90,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W100 });

		runTW1to10("SITWA_x_OBSvA_se137900_b10_t3000",
				new double[][] { InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W10,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W20, InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W30,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W40, InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W50,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W60, InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W70,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W80, InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W90,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W100 });

		runTW1to10("SITWA_x_OBSvA_se137900_b10_t5000",
				new double[][] { InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W10,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W20, InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W30,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W40, InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W50,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W60, InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W70,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W80, InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W90,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W100 });
	}

	private static void runOBS_Caa() {
		settings.expectedTravelTime = InputOBS.avgTT;
		settings.timeWindowMethod = "AfterArrival";

		runTW1to10("aaSITWA_x_OBSvC_se150430_b10_t1000",
				new double[][] { InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W10,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W20, InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W30,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W40, InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W50,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W60, InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W70,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W80, InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W90,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t1000_W100 });

		runTW1to10("aaSITWA_x_OBSvC_se150430_b10_t3000",
				new double[][] { InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W10,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W20, InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W30,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W40, InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W50,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W60, InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W70,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W80, InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W90,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t3000_W100 });

		runTW1to10("aaSITWA_x_OBSvC_se150430_b10_t5000",
				new double[][] { InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W10,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W20, InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W30,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W40, InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W50,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W60, InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W70,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W80, InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W90,
						InputOBS.SITWA_x_OBSvC_se150430_b10_t5000_W100 });
	}

	private static void runOBS_Baa() {
		settings.expectedTravelTime = InputOBS.avgTT;
		settings.timeWindowMethod = "AfterArrival";

		runTW1to10("aaSITWA_x_OBSvB_se150430_b10_t1000",
				new double[][] { InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W10,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W20, InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W30,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W40, InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W50,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W60, InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W70,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W80, InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W90,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t1000_W100 });

		runTW1to10("aaSITWA_x_OBSvB_se150430_b10_t3000",
				new double[][] { InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W10,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W20, InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W30,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W40, InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W50,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W60, InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W70,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W80, InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W90,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t3000_W100 });

		runTW1to10("aaSITWA_x_OBSvB_se150430_b10_t5000",
				new double[][] { InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W10,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W20, InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W30,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W40, InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W50,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W60, InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W70,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W80, InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W90,
						InputOBS.SITWA_x_OBSvB_se150430_b10_t5000_W100 });
	}

	private static void runOBS_Aaa() {
		settings.expectedTravelTime = InputOBS.minTT;
		settings.timeWindowMethod = "AfterArrival";

		runTW1to10("aaSITWA_x_vA_se137810_b10_t1000",
				new double[][] { InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W10,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W20, InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W30,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W40, InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W50,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W60, InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W70,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W80, InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W90,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t1000_W100 });

		runTW1to10("aaSITWA_x_OBSvA_se137900_b10_t3000",
				new double[][] { InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W10,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W20, InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W30,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W40, InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W50,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W60, InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W70,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W80, InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W90,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t3000_W100 });

		runTW1to10("aaSITWA_x_OBSvA_se137900_b10_t5000",
				new double[][] { InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W10,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W20, InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W30,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W40, InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W50,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W60, InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W70,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W80, InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W90,
						InputOBS.SITWA_x_OBSvA_se137900_b10_t5000_W100 });
	}

	private static void runApril2020() {
		run("SITWA10_vA_W00_se1585246474_1W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all1Min);
		run("SITWA20_vA_W00_se1585246474_1W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all1Min);
		run("SITWA10_x_vA_W00_se1585246474_1W", InputBufferNew.SITWA10_x_vA_W00_se1585246474, InputTimeWindows.all1Min);
		run("SITWA20_x_vA_W00_se1585246474_1W", InputBufferNew.SITWA20_x_vA_W00_se1585246474, InputTimeWindows.all1Min);
		run("SITWA10_vA_W00_se1585246474_2W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all2Min);
		run("SITWA20_vA_W00_se1585246474_2W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all2Min);
		run("SITWA10_x_vA_W00_se1585246474_2W", InputBufferNew.SITWA10_x_vA_W00_se1585246474, InputTimeWindows.all2Min);
		run("SITWA20_x_vA_W00_se1585246474_2W", InputBufferNew.SITWA20_x_vA_W00_se1585246474, InputTimeWindows.all2Min);
		run("SITWA10_vA_W00_se1585246474_3W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all3Min);
		run("SITWA20_vA_W00_se1585246474_3W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all3Min);
		run("SITWA10_x_vA_W00_se1585246474_3W", InputBufferNew.SITWA10_x_vA_W00_se1585246474, InputTimeWindows.all3Min);
		run("SITWA20_x_vA_W00_se1585246474_3W", InputBufferNew.SITWA20_x_vA_W00_se1585246474, InputTimeWindows.all3Min);
		run("SITWA10_vA_W00_se1585246474_4W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all4Min);
		run("SITWA20_vA_W00_se1585246474_4W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all4Min);
		run("SITWA10_x_vA_W00_se1585246474_4W", InputBufferNew.SITWA10_x_vA_W00_se1585246474, InputTimeWindows.all4Min);
		run("SITWA20_x_vA_W00_se1585246474_4W", InputBufferNew.SITWA20_x_vA_W00_se1585246474, InputTimeWindows.all4Min);
		run("SITWA10_vA_W00_se1585246474_5W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all5Min);
		run("SITWA20_vA_W00_se1585246474_5W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all5Min);
		run("SITWA10_x_vA_W00_se1585246474_5W", InputBufferNew.SITWA10_x_vA_W00_se1585246474, InputTimeWindows.all5Min);
		run("SITWA20_x_vA_W00_se1585246474_5W", InputBufferNew.SITWA20_x_vA_W00_se1585246474, InputTimeWindows.all5Min);
		run("SITWA10_vA_W00_se1585246474_6W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all6Min);
		run("SITWA20_vA_W00_se1585246474_6W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all6Min);
		run("SITWA10_x_vA_W00_se1585246474_6W", InputBufferNew.SITWA10_x_vA_W00_se1585246474, InputTimeWindows.all6Min);
		run("SITWA20_x_vA_W00_se1585246474_6W", InputBufferNew.SITWA20_x_vA_W00_se1585246474, InputTimeWindows.all6Min);
		run("SITWA10_vA_W00_se1585246474_7W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all7Min);
		run("SITWA20_vA_W00_se1585246474_7W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all7Min);
		run("SITWA10_x_vA_W00_se1585246474_7W", InputBufferNew.SITWA10_x_vA_W00_se1585246474, InputTimeWindows.all7Min);
		run("SITWA20_x_vA_W00_se1585246474_7W", InputBufferNew.SITWA20_x_vA_W00_se1585246474, InputTimeWindows.all7Min);
		run("SITWA10_vA_W00_se1585246474_8W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all8Min);
		run("SITWA20_vA_W00_se1585246474_8W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all8Min);
		run("SITWA10_x_vA_W00_se1585246474_8W", InputBufferNew.SITWA10_x_vA_W00_se1585246474, InputTimeWindows.all8Min);
		run("SITWA20_x_vA_W00_se1585246474_8W", InputBufferNew.SITWA20_x_vA_W00_se1585246474, InputTimeWindows.all8Min);
		run("SITWA10_vA_W00_se1585246474_9W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all9Min);
		run("SITWA20_vA_W00_se1585246474_9W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all9Min);
		run("SITWA10_x_vA_W00_se1585246474_9W", InputBufferNew.SITWA10_x_vA_W00_se1585246474, InputTimeWindows.all9Min);
		run("SITWA20_x_vA_W00_se1585246474_9W", InputBufferNew.SITWA20_x_vA_W00_se1585246474, InputTimeWindows.all9Min);
		run("SITWA10_vA_W00_se1585246474_10W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all10Min);
		run("SITWA20_vA_W00_se1585246474_10W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all10Min);
		run("SITWA10_x_vA_W00_se1585246474_10W", InputBufferNew.SITWA10_x_vA_W00_se1585246474,
				InputTimeWindows.all10Min);
		run("SITWA20_x_vA_W00_se1585246474_10W", InputBufferNew.SITWA20_x_vA_W00_se1585246474,
				InputTimeWindows.all10Min);

		run("SITWA10_vA_W10_se1585246474_1W", InputBufferNew.SITWA10_vA_W10_se1585246474, InputTimeWindows.all1Min);
		run("SITWA20_vA_W10_se1585246474_1W", InputBufferNew.SITWA20_vA_W10_se1585246474, InputTimeWindows.all1Min);
		run("SITWA10_x_vA_W10_se1585246474_1W", InputBufferNew.SITWA10_x_vA_W10_se1585246474, InputTimeWindows.all1Min);
		run("SITWA20_x_vA_W10_se1585246474_1W", InputBufferNew.SITWA20_x_vA_W10_se1585246474, InputTimeWindows.all1Min);
		run("SITWA10_vA_W20_se1585246474_2W", InputBufferNew.SITWA10_vA_W20_se1585246474, InputTimeWindows.all2Min);
		run("SITWA20_vA_W20_se1585246474_2W", InputBufferNew.SITWA20_vA_W20_se1585246474, InputTimeWindows.all2Min);
		run("SITWA10_x_vA_W20_se1585246474_2W", InputBufferNew.SITWA10_x_vA_W20_se1585246474, InputTimeWindows.all2Min);
		run("SITWA20_x_vA_W20_se1585246474_2W", InputBufferNew.SITWA20_x_vA_W20_se1585246474, InputTimeWindows.all2Min);
		run("SITWA10_vA_W30_se1585246474_3W", InputBufferNew.SITWA10_vA_W30_se1585246474, InputTimeWindows.all3Min);
		run("SITWA20_vA_W30_se1585246474_3W", InputBufferNew.SITWA20_vA_W30_se1585246474, InputTimeWindows.all3Min);
		run("SITWA10_x_vA_W30_se1585246474_3W", InputBufferNew.SITWA10_x_vA_W30_se1585246474, InputTimeWindows.all3Min);
		run("SITWA20_x_vA_W30_se1585246474_3W", InputBufferNew.SITWA20_x_vA_W30_se1585246474, InputTimeWindows.all3Min);
		run("SITWA10_vA_W40_se1585246474_4W", InputBufferNew.SITWA10_vA_W40_se1585246474, InputTimeWindows.all4Min);
		run("SITWA20_vA_W40_se1585246474_4W", InputBufferNew.SITWA20_vA_W40_se1585246474, InputTimeWindows.all4Min);
		run("SITWA10_x_vA_W40_se1585246474_4W", InputBufferNew.SITWA10_x_vA_W40_se1585246474, InputTimeWindows.all4Min);
		run("SITWA20_x_vA_W40_se1585246474_4W", InputBufferNew.SITWA20_x_vA_W40_se1585246474, InputTimeWindows.all4Min);
		run("SITWA10_vA_W50_se1585246474_5W", InputBufferNew.SITWA10_vA_W50_se1585246474, InputTimeWindows.all5Min);
		run("SITWA20_vA_W50_se1585246474_5W", InputBufferNew.SITWA20_vA_W50_se1585246474, InputTimeWindows.all5Min);
		run("SITWA10_x_vA_W50_se1585246474_5W", InputBufferNew.SITWA10_x_vA_W50_se1585246474, InputTimeWindows.all5Min);
		run("SITWA20_x_vA_W50_se1585246474_5W", InputBufferNew.SITWA20_x_vA_W50_se1585246474, InputTimeWindows.all5Min);
		run("SITWA10_vA_W60_se1585246474_6W", InputBufferNew.SITWA10_vA_W60_se1585246474, InputTimeWindows.all6Min);
		run("SITWA20_vA_W60_se1585246474_6W", InputBufferNew.SITWA20_vA_W60_se1585246474, InputTimeWindows.all6Min);
		run("SITWA10_x_vA_W60_se1585246474_6W", InputBufferNew.SITWA10_x_vA_W60_se1585246474, InputTimeWindows.all6Min);
		run("SITWA20_x_vA_W60_se1585246474_6W", InputBufferNew.SITWA20_x_vA_W60_se1585246474, InputTimeWindows.all6Min);
		run("SITWA10_vA_W70_se1585246474_7W", InputBufferNew.SITWA10_vA_W70_se1585246474, InputTimeWindows.all7Min);
		run("SITWA20_vA_W70_se1585246474_7W", InputBufferNew.SITWA20_vA_W70_se1585246474, InputTimeWindows.all7Min);
		run("SITWA10_x_vA_W70_se1585246474_7W", InputBufferNew.SITWA10_x_vA_W70_se1585246474, InputTimeWindows.all7Min);
		run("SITWA20_x_vA_W70_se1585246474_7W", InputBufferNew.SITWA20_x_vA_W70_se1585246474, InputTimeWindows.all7Min);
		run("SITWA10_vA_W80_se1585246474_8W", InputBufferNew.SITWA10_vA_W80_se1585246474, InputTimeWindows.all8Min);
		run("SITWA20_vA_W80_se1585246474_8W", InputBufferNew.SITWA20_vA_W80_se1585246474, InputTimeWindows.all8Min);
		run("SITWA10_x_vA_W80_se1585246474_8W", InputBufferNew.SITWA10_x_vA_W80_se1585246474, InputTimeWindows.all8Min);
		run("SITWA20_x_vA_W80_se1585246474_8W", InputBufferNew.SITWA20_x_vA_W80_se1585246474, InputTimeWindows.all8Min);
		run("SITWA10_vA_W90_se1585246474_9W", InputBufferNew.SITWA10_vA_W90_se1585246474, InputTimeWindows.all9Min);
		run("SITWA20_vA_W90_se1585246474_9W", InputBufferNew.SITWA20_vA_W90_se1585246474, InputTimeWindows.all9Min);
		run("SITWA10_x_vA_W90_se1585246474_9W", InputBufferNew.SITWA10_x_vA_W90_se1585246474, InputTimeWindows.all9Min);
		run("SITWA20_x_vA_W90_se1585246474_9W", InputBufferNew.SITWA20_x_vA_W90_se1585246474, InputTimeWindows.all9Min);
		run("SITWA10_vA_W100_se1585246474_10W", InputBufferNew.SITWA10_vA_W100_se1585246474, InputTimeWindows.all10Min);
		run("SITWA20_vA_W100_se1585246474_10W", InputBufferNew.SITWA20_vA_W100_se1585246474, InputTimeWindows.all10Min);
		run("SITWA10_x_vA_W100_se1585246474_10W", InputBufferNew.SITWA10_x_vA_W100_se1585246474,
				InputTimeWindows.all10Min);
		run("SITWA20_x_vA_W100_se1585246474_10W", InputBufferNew.SITWA20_x_vA_W100_se1585246474,
				InputTimeWindows.all10Min);

		settings.timeWindowMethod = "AfterArrival";

		run("aaSITWA10_vA_W00_se1585246474_1W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all1Min);
		run("aaSITWA20_vA_W00_se1585246474_1W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all1Min);
		run("aaSITWA10_x_vA_W00_se1585246474_1W", InputBufferNew.SITWA10_x_vA_W00_se1585246474,
				InputTimeWindows.all1Min);
		run("aaSITWA20_x_vA_W00_se1585246474_1W", InputBufferNew.SITWA20_x_vA_W00_se1585246474,
				InputTimeWindows.all1Min);
		run("aaSITWA10_vA_W00_se1585246474_2W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all2Min);
		run("aaSITWA20_vA_W00_se1585246474_2W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all2Min);
		run("aaSITWA10_x_vA_W00_se1585246474_2W", InputBufferNew.SITWA10_x_vA_W00_se1585246474,
				InputTimeWindows.all2Min);
		run("aaSITWA20_x_vA_W00_se1585246474_2W", InputBufferNew.SITWA20_x_vA_W00_se1585246474,
				InputTimeWindows.all2Min);
		run("aaSITWA10_vA_W00_se1585246474_3W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all3Min);
		run("aaSITWA20_vA_W00_se1585246474_3W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all3Min);
		run("aaSITWA10_x_vA_W00_se1585246474_3W", InputBufferNew.SITWA10_x_vA_W00_se1585246474,
				InputTimeWindows.all3Min);
		run("aaSITWA20_x_vA_W00_se1585246474_3W", InputBufferNew.SITWA20_x_vA_W00_se1585246474,
				InputTimeWindows.all3Min);
		run("aaSITWA10_vA_W00_se1585246474_4W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all4Min);
		run("aaSITWA20_vA_W00_se1585246474_4W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all4Min);
		run("aaSITWA10_x_vA_W00_se1585246474_4W", InputBufferNew.SITWA10_x_vA_W00_se1585246474,
				InputTimeWindows.all4Min);
		run("aaSITWA20_x_vA_W00_se1585246474_4W", InputBufferNew.SITWA20_x_vA_W00_se1585246474,
				InputTimeWindows.all4Min);
		run("aaSITWA10_vA_W00_se1585246474_5W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all5Min);
		run("aaSITWA20_vA_W00_se1585246474_5W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all5Min);
		run("aaSITWA10_x_vA_W00_se1585246474_5W", InputBufferNew.SITWA10_x_vA_W00_se1585246474,
				InputTimeWindows.all5Min);
		run("aaSITWA20_x_vA_W00_se1585246474_5W", InputBufferNew.SITWA20_x_vA_W00_se1585246474,
				InputTimeWindows.all5Min);
		run("aaSITWA10_vA_W00_se1585246474_6W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all6Min);
		run("aaSITWA20_vA_W00_se1585246474_6W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all6Min);
		run("aaSITWA10_x_vA_W00_se1585246474_6W", InputBufferNew.SITWA10_x_vA_W00_se1585246474,
				InputTimeWindows.all6Min);
		run("aaSITWA20_x_vA_W00_se1585246474_6W", InputBufferNew.SITWA20_x_vA_W00_se1585246474,
				InputTimeWindows.all6Min);
		run("aaSITWA10_vA_W00_se1585246474_7W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all7Min);
		run("aaSITWA20_vA_W00_se1585246474_7W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all7Min);
		run("aaSITWA10_x_vA_W00_se1585246474_7W", InputBufferNew.SITWA10_x_vA_W00_se1585246474,
				InputTimeWindows.all7Min);
		run("aaSITWA20_x_vA_W00_se1585246474_7W", InputBufferNew.SITWA20_x_vA_W00_se1585246474,
				InputTimeWindows.all7Min);
		run("aaSITWA10_vA_W00_se1585246474_8W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all8Min);
		run("aaSITWA20_vA_W00_se1585246474_8W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all8Min);
		run("aaSITWA10_x_vA_W00_se1585246474_8W", InputBufferNew.SITWA10_x_vA_W00_se1585246474,
				InputTimeWindows.all8Min);
		run("aaSITWA20_x_vA_W00_se1585246474_8W", InputBufferNew.SITWA20_x_vA_W00_se1585246474,
				InputTimeWindows.all8Min);
		run("aaSITWA10_vA_W00_se1585246474_9W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all9Min);
		run("aaSITWA20_vA_W00_se1585246474_9W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all9Min);
		run("aaSITWA10_x_vA_W00_se1585246474_9W", InputBufferNew.SITWA10_x_vA_W00_se1585246474,
				InputTimeWindows.all9Min);
		run("aaSITWA20_x_vA_W00_se1585246474_9W", InputBufferNew.SITWA20_x_vA_W00_se1585246474,
				InputTimeWindows.all9Min);
		run("aaSITWA10_vA_W00_se1585246474_10W", InputBufferNew.SITWA10_vA_W00_se1585246474, InputTimeWindows.all10Min);
		run("aaSITWA20_vA_W00_se1585246474_10W", InputBufferNew.SITWA20_vA_W00_se1585246474, InputTimeWindows.all10Min);
		run("aaSITWA10_x_vA_W00_se1585246474_10W", InputBufferNew.SITWA10_x_vA_W00_se1585246474,
				InputTimeWindows.all10Min);
		run("aaSITWA20_x_vA_W00_se1585246474_10W", InputBufferNew.SITWA20_x_vA_W00_se1585246474,
				InputTimeWindows.all10Min);

		run("aaSITWA10_vA_W10_se1585246474_1W", InputBufferNew.SITWA10_vA_W10_se1585246474, InputTimeWindows.all1Min);
		run("aaSITWA20_vA_W10_se1585246474_1W", InputBufferNew.SITWA20_vA_W10_se1585246474, InputTimeWindows.all1Min);
		run("aaSITWA10_x_vA_W10_se1585246474_1W", InputBufferNew.SITWA10_x_vA_W10_se1585246474,
				InputTimeWindows.all1Min);
		run("aaSITWA20_x_vA_W10_se1585246474_1W", InputBufferNew.SITWA20_x_vA_W10_se1585246474,
				InputTimeWindows.all1Min);
		run("aaSITWA10_vA_W20_se1585246474_2W", InputBufferNew.SITWA10_vA_W20_se1585246474, InputTimeWindows.all2Min);
		run("aaSITWA20_vA_W20_se1585246474_2W", InputBufferNew.SITWA20_vA_W20_se1585246474, InputTimeWindows.all2Min);
		run("aaSITWA10_x_vA_W20_se1585246474_2W", InputBufferNew.SITWA10_x_vA_W20_se1585246474,
				InputTimeWindows.all2Min);
		run("aaSITWA20_x_vA_W20_se1585246474_2W", InputBufferNew.SITWA20_x_vA_W20_se1585246474,
				InputTimeWindows.all2Min);
		run("aaSITWA10_vA_W30_se1585246474_3W", InputBufferNew.SITWA10_vA_W30_se1585246474, InputTimeWindows.all3Min);
		run("aaSITWA20_vA_W30_se1585246474_3W", InputBufferNew.SITWA20_vA_W30_se1585246474, InputTimeWindows.all3Min);
		run("aaSITWA10_x_vA_W30_se1585246474_3W", InputBufferNew.SITWA10_x_vA_W30_se1585246474,
				InputTimeWindows.all3Min);
		run("aaSITWA20_x_vA_W30_se1585246474_3W", InputBufferNew.SITWA20_x_vA_W30_se1585246474,
				InputTimeWindows.all3Min);
		run("aaSITWA10_vA_W40_se1585246474_4W", InputBufferNew.SITWA10_vA_W40_se1585246474, InputTimeWindows.all4Min);
		run("aaSITWA20_vA_W40_se1585246474_4W", InputBufferNew.SITWA20_vA_W40_se1585246474, InputTimeWindows.all4Min);
		run("aaSITWA10_x_vA_W40_se1585246474_4W", InputBufferNew.SITWA10_x_vA_W40_se1585246474,
				InputTimeWindows.all4Min);
		run("aaSITWA20_x_vA_W40_se1585246474_4W", InputBufferNew.SITWA20_x_vA_W40_se1585246474,
				InputTimeWindows.all4Min);
		run("aaSITWA10_vA_W50_se1585246474_5W", InputBufferNew.SITWA10_vA_W50_se1585246474, InputTimeWindows.all5Min);
		run("aaSITWA20_vA_W50_se1585246474_5W", InputBufferNew.SITWA20_vA_W50_se1585246474, InputTimeWindows.all5Min);
		run("aaSITWA10_x_vA_W50_se1585246474_5W", InputBufferNew.SITWA10_x_vA_W50_se1585246474,
				InputTimeWindows.all5Min);
		run("aaSITWA20_x_vA_W50_se1585246474_5W", InputBufferNew.SITWA20_x_vA_W50_se1585246474,
				InputTimeWindows.all5Min);
		run("aaSITWA10_vA_W60_se1585246474_6W", InputBufferNew.SITWA10_vA_W60_se1585246474, InputTimeWindows.all6Min);
		run("aaSITWA20_vA_W60_se1585246474_6W", InputBufferNew.SITWA20_vA_W60_se1585246474, InputTimeWindows.all6Min);
		run("aaSITWA10_x_vA_W60_se1585246474_6W", InputBufferNew.SITWA10_x_vA_W60_se1585246474,
				InputTimeWindows.all6Min);
		run("aaSITWA20_x_vA_W60_se1585246474_6W", InputBufferNew.SITWA20_x_vA_W60_se1585246474,
				InputTimeWindows.all6Min);
		run("aaSITWA10_vA_W70_se1585246474_7W", InputBufferNew.SITWA10_vA_W70_se1585246474, InputTimeWindows.all7Min);
		run("aaSITWA20_vA_W70_se1585246474_7W", InputBufferNew.SITWA20_vA_W70_se1585246474, InputTimeWindows.all7Min);
		run("aaSITWA10_x_vA_W70_se1585246474_7W", InputBufferNew.SITWA10_x_vA_W70_se1585246474,
				InputTimeWindows.all7Min);
		run("aaSITWA20_x_vA_W70_se1585246474_7W", InputBufferNew.SITWA20_x_vA_W70_se1585246474,
				InputTimeWindows.all7Min);
		run("aaSITWA10_vA_W80_se1585246474_8W", InputBufferNew.SITWA10_vA_W80_se1585246474, InputTimeWindows.all8Min);
		run("aaSITWA20_vA_W80_se1585246474_8W", InputBufferNew.SITWA20_vA_W80_se1585246474, InputTimeWindows.all8Min);
		run("aaSITWA10_x_vA_W80_se1585246474_8W", InputBufferNew.SITWA10_x_vA_W80_se1585246474,
				InputTimeWindows.all8Min);
		run("aaSITWA20_x_vA_W80_se1585246474_8W", InputBufferNew.SITWA20_x_vA_W80_se1585246474,
				InputTimeWindows.all8Min);
		run("aaSITWA10_vA_W90_se1585246474_9W", InputBufferNew.SITWA10_vA_W90_se1585246474, InputTimeWindows.all9Min);
		run("aaSITWA20_vA_W90_se1585246474_9W", InputBufferNew.SITWA20_vA_W90_se1585246474, InputTimeWindows.all9Min);
		run("aaSITWA10_x_vA_W90_se1585246474_9W", InputBufferNew.SITWA10_x_vA_W90_se1585246474,
				InputTimeWindows.all9Min);
		run("aaSITWA20_x_vA_W90_se1585246474_9W", InputBufferNew.SITWA20_x_vA_W90_se1585246474,
				InputTimeWindows.all9Min);
		run("aaSITWA10_vA_W100_se1585246474_10W", InputBufferNew.SITWA10_vA_W100_se1585246474,
				InputTimeWindows.all10Min);
		run("aaSITWA20_vA_W100_se1585246474_10W", InputBufferNew.SITWA20_vA_W100_se1585246474,
				InputTimeWindows.all10Min);
		run("aaSITWA10_x_vA_W100_se1585246474_10W", InputBufferNew.SITWA10_x_vA_W100_se1585246474,
				InputTimeWindows.all10Min);
		run("aaSITWA20_x_vA_W100_se1585246474_10W", InputBufferNew.SITWA20_x_vA_W100_se1585246474,
				InputTimeWindows.all10Min);

		settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;
		settings.timeWindowMethod = "PlusMinusArrival";

		run("SD_1W", InputBufferNew.bufferNewSD, InputTimeWindows.all1Min);
		run("SD_2W", InputBufferNew.bufferNewSD, InputTimeWindows.all2Min);
		run("SD_3W", InputBufferNew.bufferNewSD, InputTimeWindows.all3Min);
		run("SD_4W", InputBufferNew.bufferNewSD, InputTimeWindows.all4Min);
		run("SD_5W", InputBufferNew.bufferNewSD, InputTimeWindows.all5Min);
		run("SD_6W", InputBufferNew.bufferNewSD, InputTimeWindows.all6Min);
		run("SD_7W", InputBufferNew.bufferNewSD, InputTimeWindows.all7Min);
		run("SD_8W", InputBufferNew.bufferNewSD, InputTimeWindows.all8Min);
		run("SD_9W", InputBufferNew.bufferNewSD, InputTimeWindows.all9Min);
		run("SD_10W", InputBufferNew.bufferNewSD, InputTimeWindows.all10Min);

		run("SITWA10_vB_W00_se1585246474_1W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all1Min);
		run("SITWA20_vB_W00_se1585246474_1W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all1Min);
		run("SITWA10_x_vB_W00_se1585246474_1W", InputBufferNew.SITWA10_x_vB_W00_se1585246474, InputTimeWindows.all1Min);
		run("SITWA20_x_vB_W00_se1585246474_1W", InputBufferNew.SITWA20_x_vB_W00_se1585246474, InputTimeWindows.all1Min);
		run("SITWA10_vB_W00_se1585246474_2W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all2Min);
		run("SITWA20_vB_W00_se1585246474_2W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all2Min);
		run("SITWA10_x_vB_W00_se1585246474_2W", InputBufferNew.SITWA10_x_vB_W00_se1585246474, InputTimeWindows.all2Min);
		run("SITWA20_x_vB_W00_se1585246474_2W", InputBufferNew.SITWA20_x_vB_W00_se1585246474, InputTimeWindows.all2Min);
		run("SITWA10_vB_W00_se1585246474_3W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all3Min);
		run("SITWA20_vB_W00_se1585246474_3W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all3Min);
		run("SITWA10_x_vB_W00_se1585246474_3W", InputBufferNew.SITWA10_x_vB_W00_se1585246474, InputTimeWindows.all3Min);
		run("SITWA20_x_vB_W00_se1585246474_3W", InputBufferNew.SITWA20_x_vB_W00_se1585246474, InputTimeWindows.all3Min);
		run("SITWA10_vB_W00_se1585246474_4W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all4Min);
		run("SITWA20_vB_W00_se1585246474_4W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all4Min);
		run("SITWA10_x_vB_W00_se1585246474_4W", InputBufferNew.SITWA10_x_vB_W00_se1585246474, InputTimeWindows.all4Min);
		run("SITWA20_x_vB_W00_se1585246474_4W", InputBufferNew.SITWA20_x_vB_W00_se1585246474, InputTimeWindows.all4Min);
		run("SITWA10_vB_W00_se1585246474_5W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all5Min);
		run("SITWA20_vB_W00_se1585246474_5W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all5Min);
		run("SITWA10_x_vB_W00_se1585246474_5W", InputBufferNew.SITWA10_x_vB_W00_se1585246474, InputTimeWindows.all5Min);
		run("SITWA20_x_vB_W00_se1585246474_5W", InputBufferNew.SITWA20_x_vB_W00_se1585246474, InputTimeWindows.all5Min);
		run("SITWA10_vB_W00_se1585246474_6W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all6Min);
		run("SITWA20_vB_W00_se1585246474_6W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all6Min);
		run("SITWA10_x_vB_W00_se1585246474_6W", InputBufferNew.SITWA10_x_vB_W00_se1585246474, InputTimeWindows.all6Min);
		run("SITWA20_x_vB_W00_se1585246474_6W", InputBufferNew.SITWA20_x_vB_W00_se1585246474, InputTimeWindows.all6Min);
		run("SITWA10_vB_W00_se1585246474_7W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all7Min);
		run("SITWA20_vB_W00_se1585246474_7W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all7Min);
		run("SITWA10_x_vB_W00_se1585246474_7W", InputBufferNew.SITWA10_x_vB_W00_se1585246474, InputTimeWindows.all7Min);
		run("SITWA20_x_vB_W00_se1585246474_7W", InputBufferNew.SITWA20_x_vB_W00_se1585246474, InputTimeWindows.all7Min);
		run("SITWA10_vB_W00_se1585246474_8W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all8Min);
		run("SITWA20_vB_W00_se1585246474_8W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all8Min);
		run("SITWA10_x_vB_W00_se1585246474_8W", InputBufferNew.SITWA10_x_vB_W00_se1585246474, InputTimeWindows.all8Min);
		run("SITWA20_x_vB_W00_se1585246474_8W", InputBufferNew.SITWA20_x_vB_W00_se1585246474, InputTimeWindows.all8Min);
		run("SITWA10_vB_W00_se1585246474_9W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all9Min);
		run("SITWA20_vB_W00_se1585246474_9W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all9Min);
		run("SITWA10_x_vB_W00_se1585246474_9W", InputBufferNew.SITWA10_x_vB_W00_se1585246474, InputTimeWindows.all9Min);
		run("SITWA20_x_vB_W00_se1585246474_9W", InputBufferNew.SITWA20_x_vB_W00_se1585246474, InputTimeWindows.all9Min);
		run("SITWA10_vB_W00_se1585246474_10W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all10Min);
		run("SITWA20_vB_W00_se1585246474_10W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all10Min);
		run("SITWA10_x_vB_W00_se1585246474_10W", InputBufferNew.SITWA10_x_vB_W00_se1585246474,
				InputTimeWindows.all10Min);
		run("SITWA20_x_vB_W00_se1585246474_10W", InputBufferNew.SITWA20_x_vB_W00_se1585246474,
				InputTimeWindows.all10Min);

		run("SITWA10_vB_W10_se1585246474_1W", InputBufferNew.SITWA10_vB_W10_se1585246474, InputTimeWindows.all1Min);
		run("SITWA20_vB_W10_se1585246474_1W", InputBufferNew.SITWA20_vB_W10_se1585246474, InputTimeWindows.all1Min);
		run("SITWA10_x_vB_W10_se1585246474_1W", InputBufferNew.SITWA10_x_vB_W10_se1585246474, InputTimeWindows.all1Min);
		run("SITWA20_x_vB_W10_se1585246474_1W", InputBufferNew.SITWA20_x_vB_W10_se1585246474, InputTimeWindows.all1Min);
		run("SITWA10_vB_W20_se1585246474_2W", InputBufferNew.SITWA10_vB_W20_se1585246474, InputTimeWindows.all2Min);
		run("SITWA20_vB_W20_se1585246474_2W", InputBufferNew.SITWA20_vB_W20_se1585246474, InputTimeWindows.all2Min);
		run("SITWA10_x_vB_W20_se1585246474_2W", InputBufferNew.SITWA10_x_vB_W20_se1585246474, InputTimeWindows.all2Min);
		run("SITWA20_x_vB_W20_se1585246474_2W", InputBufferNew.SITWA20_x_vB_W20_se1585246474, InputTimeWindows.all2Min);
		run("SITWA10_vB_W30_se1585246474_3W", InputBufferNew.SITWA10_vB_W30_se1585246474, InputTimeWindows.all3Min);
		run("SITWA20_vB_W30_se1585246474_3W", InputBufferNew.SITWA20_vB_W30_se1585246474, InputTimeWindows.all3Min);
		run("SITWA10_x_vB_W30_se1585246474_3W", InputBufferNew.SITWA10_x_vB_W30_se1585246474, InputTimeWindows.all3Min);
		run("SITWA20_x_vB_W30_se1585246474_3W", InputBufferNew.SITWA20_x_vB_W30_se1585246474, InputTimeWindows.all3Min);
		run("SITWA10_vB_W40_se1585246474_4W", InputBufferNew.SITWA10_vB_W40_se1585246474, InputTimeWindows.all4Min);
		run("SITWA20_vB_W40_se1585246474_4W", InputBufferNew.SITWA20_vB_W40_se1585246474, InputTimeWindows.all4Min);
		run("SITWA10_x_vB_W40_se1585246474_4W", InputBufferNew.SITWA10_x_vB_W40_se1585246474, InputTimeWindows.all4Min);
		run("SITWA20_x_vB_W40_se1585246474_4W", InputBufferNew.SITWA20_x_vB_W40_se1585246474, InputTimeWindows.all4Min);
		run("SITWA10_vB_W50_se1585246474_5W", InputBufferNew.SITWA10_vB_W50_se1585246474, InputTimeWindows.all5Min);
		run("SITWA20_vB_W50_se1585246474_5W", InputBufferNew.SITWA20_vB_W50_se1585246474, InputTimeWindows.all5Min);
		run("SITWA10_x_vB_W50_se1585246474_5W", InputBufferNew.SITWA10_x_vB_W50_se1585246474, InputTimeWindows.all5Min);
		run("SITWA20_x_vB_W50_se1585246474_5W", InputBufferNew.SITWA20_x_vB_W50_se1585246474, InputTimeWindows.all5Min);
		run("SITWA10_vB_W60_se1585246474_6W", InputBufferNew.SITWA10_vB_W60_se1585246474, InputTimeWindows.all6Min);
		run("SITWA20_vB_W60_se1585246474_6W", InputBufferNew.SITWA20_vB_W60_se1585246474, InputTimeWindows.all6Min);
		run("SITWA10_x_vB_W60_se1585246474_6W", InputBufferNew.SITWA10_x_vB_W60_se1585246474, InputTimeWindows.all6Min);
		run("SITWA20_x_vB_W60_se1585246474_6W", InputBufferNew.SITWA20_x_vB_W60_se1585246474, InputTimeWindows.all6Min);
		run("SITWA10_vB_W70_se1585246474_7W", InputBufferNew.SITWA10_vB_W70_se1585246474, InputTimeWindows.all7Min);
		run("SITWA20_vB_W70_se1585246474_7W", InputBufferNew.SITWA20_vB_W70_se1585246474, InputTimeWindows.all7Min);
		run("SITWA10_x_vB_W70_se1585246474_7W", InputBufferNew.SITWA10_x_vB_W70_se1585246474, InputTimeWindows.all7Min);
		run("SITWA20_x_vB_W70_se1585246474_7W", InputBufferNew.SITWA20_x_vB_W70_se1585246474, InputTimeWindows.all7Min);
		run("SITWA10_vB_W80_se1585246474_8W", InputBufferNew.SITWA10_vB_W80_se1585246474, InputTimeWindows.all8Min);
		run("SITWA20_vB_W80_se1585246474_8W", InputBufferNew.SITWA20_vB_W80_se1585246474, InputTimeWindows.all8Min);
		run("SITWA10_x_vB_W80_se1585246474_8W", InputBufferNew.SITWA10_x_vB_W80_se1585246474, InputTimeWindows.all8Min);
		run("SITWA20_x_vB_W80_se1585246474_8W", InputBufferNew.SITWA20_x_vB_W80_se1585246474, InputTimeWindows.all8Min);
		run("SITWA10_vB_W90_se1585246474_9W", InputBufferNew.SITWA10_vB_W90_se1585246474, InputTimeWindows.all9Min);
		run("SITWA20_vB_W90_se1585246474_9W", InputBufferNew.SITWA20_vB_W90_se1585246474, InputTimeWindows.all9Min);
		run("SITWA10_x_vB_W90_se1585246474_9W", InputBufferNew.SITWA10_x_vB_W90_se1585246474, InputTimeWindows.all9Min);
		run("SITWA20_x_vB_W90_se1585246474_9W", InputBufferNew.SITWA20_x_vB_W90_se1585246474, InputTimeWindows.all9Min);
		run("SITWA10_vB_W100_se1585246474_10W", InputBufferNew.SITWA10_vB_W100_se1585246474, InputTimeWindows.all10Min);
		run("SITWA20_vB_W100_se1585246474_10W", InputBufferNew.SITWA20_vB_W100_se1585246474, InputTimeWindows.all10Min);
		run("SITWA10_x_vB_W100_se1585246474_10W", InputBufferNew.SITWA10_x_vB_W100_se1585246474,
				InputTimeWindows.all10Min);
		run("SITWA20_x_vB_W100_se1585246474_10W", InputBufferNew.SITWA20_x_vB_W100_se1585246474,
				InputTimeWindows.all10Min);

		run("SITWA10_vC_W00_se1585246474_1W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all1Min);
		run("SITWA20_vC_W00_se1585246474_1W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all1Min);
		run("SITWA10_x_vC_W00_se1585246474_1W", InputBufferNew.SITWA10_x_vC_W00_se1585246474, InputTimeWindows.all1Min);
		run("SITWA20_x_vC_W00_se1585246474_1W", InputBufferNew.SITWA20_x_vC_W00_se1585246474, InputTimeWindows.all1Min);
		run("SITWA10_vC_W00_se1585246474_2W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all2Min);
		run("SITWA20_vC_W00_se1585246474_2W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all2Min);
		run("SITWA10_x_vC_W00_se1585246474_2W", InputBufferNew.SITWA10_x_vC_W00_se1585246474, InputTimeWindows.all2Min);
		run("SITWA20_x_vC_W00_se1585246474_2W", InputBufferNew.SITWA20_x_vC_W00_se1585246474, InputTimeWindows.all2Min);
		run("SITWA10_vC_W00_se1585246474_3W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all3Min);
		run("SITWA20_vC_W00_se1585246474_3W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all3Min);
		run("SITWA10_x_vC_W00_se1585246474_3W", InputBufferNew.SITWA10_x_vC_W00_se1585246474, InputTimeWindows.all3Min);
		run("SITWA20_x_vC_W00_se1585246474_3W", InputBufferNew.SITWA20_x_vC_W00_se1585246474, InputTimeWindows.all3Min);
		run("SITWA10_vC_W00_se1585246474_4W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all4Min);
		run("SITWA20_vC_W00_se1585246474_4W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all4Min);
		run("SITWA10_x_vC_W00_se1585246474_4W", InputBufferNew.SITWA10_x_vC_W00_se1585246474, InputTimeWindows.all4Min);
		run("SITWA20_x_vC_W00_se1585246474_4W", InputBufferNew.SITWA20_x_vC_W00_se1585246474, InputTimeWindows.all4Min);
		run("SITWA10_vC_W00_se1585246474_5W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all5Min);
		run("SITWA20_vC_W00_se1585246474_5W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all5Min);
		run("SITWA10_x_vC_W00_se1585246474_5W", InputBufferNew.SITWA10_x_vC_W00_se1585246474, InputTimeWindows.all5Min);
		run("SITWA20_x_vC_W00_se1585246474_5W", InputBufferNew.SITWA20_x_vC_W00_se1585246474, InputTimeWindows.all5Min);
		run("SITWA10_vC_W00_se1585246474_6W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all6Min);
		run("SITWA20_vC_W00_se1585246474_6W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all6Min);
		run("SITWA10_x_vC_W00_se1585246474_6W", InputBufferNew.SITWA10_x_vC_W00_se1585246474, InputTimeWindows.all6Min);
		run("SITWA20_x_vC_W00_se1585246474_6W", InputBufferNew.SITWA20_x_vC_W00_se1585246474, InputTimeWindows.all6Min);
		run("SITWA10_vC_W00_se1585246474_7W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all7Min);
		run("SITWA20_vC_W00_se1585246474_7W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all7Min);
		run("SITWA10_x_vC_W00_se1585246474_7W", InputBufferNew.SITWA10_x_vC_W00_se1585246474, InputTimeWindows.all7Min);
		run("SITWA20_x_vC_W00_se1585246474_7W", InputBufferNew.SITWA20_x_vC_W00_se1585246474, InputTimeWindows.all7Min);
		run("SITWA10_vC_W00_se1585246474_8W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all8Min);
		run("SITWA20_vC_W00_se1585246474_8W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all8Min);
		run("SITWA10_x_vC_W00_se1585246474_8W", InputBufferNew.SITWA10_x_vC_W00_se1585246474, InputTimeWindows.all8Min);
		run("SITWA20_x_vC_W00_se1585246474_8W", InputBufferNew.SITWA20_x_vC_W00_se1585246474, InputTimeWindows.all8Min);
		run("SITWA10_vC_W00_se1585246474_9W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all9Min);
		run("SITWA20_vC_W00_se1585246474_9W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all9Min);
		run("SITWA10_x_vC_W00_se1585246474_9W", InputBufferNew.SITWA10_x_vC_W00_se1585246474, InputTimeWindows.all9Min);
		run("SITWA20_x_vC_W00_se1585246474_9W", InputBufferNew.SITWA20_x_vC_W00_se1585246474, InputTimeWindows.all9Min);
		run("SITWA10_vC_W00_se1585246474_10W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all10Min);
		run("SITWA20_vC_W00_se1585246474_10W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all10Min);
		run("SITWA10_x_vC_W00_se1585246474_10W", InputBufferNew.SITWA10_x_vC_W00_se1585246474,
				InputTimeWindows.all10Min);
		run("SITWA20_x_vC_W00_se1585246474_10W", InputBufferNew.SITWA20_x_vC_W00_se1585246474,
				InputTimeWindows.all10Min);

		run("SITWA10_vC_W10_se1585246474_1W", InputBufferNew.SITWA10_vC_W10_se1585246474, InputTimeWindows.all1Min);
		run("SITWA20_vC_W10_se1585246474_1W", InputBufferNew.SITWA20_vC_W10_se1585246474, InputTimeWindows.all1Min);
		run("SITWA10_x_vC_W10_se1585246474_1W", InputBufferNew.SITWA10_x_vC_W10_se1585246474, InputTimeWindows.all1Min);
		run("SITWA20_x_vC_W10_se1585246474_1W", InputBufferNew.SITWA20_x_vC_W10_se1585246474, InputTimeWindows.all1Min);
		run("SITWA10_vC_W20_se1585246474_2W", InputBufferNew.SITWA10_vC_W20_se1585246474, InputTimeWindows.all2Min);
		run("SITWA20_vC_W20_se1585246474_2W", InputBufferNew.SITWA20_vC_W20_se1585246474, InputTimeWindows.all2Min);
		run("SITWA10_x_vC_W20_se1585246474_2W", InputBufferNew.SITWA10_x_vC_W20_se1585246474, InputTimeWindows.all2Min);
		run("SITWA20_x_vC_W20_se1585246474_2W", InputBufferNew.SITWA20_x_vC_W20_se1585246474, InputTimeWindows.all2Min);
		run("SITWA10_vC_W30_se1585246474_3W", InputBufferNew.SITWA10_vC_W30_se1585246474, InputTimeWindows.all3Min);
		run("SITWA20_vC_W30_se1585246474_3W", InputBufferNew.SITWA20_vC_W30_se1585246474, InputTimeWindows.all3Min);
		run("SITWA10_x_vC_W30_se1585246474_3W", InputBufferNew.SITWA10_x_vC_W30_se1585246474, InputTimeWindows.all3Min);
		run("SITWA20_x_vC_W30_se1585246474_3W", InputBufferNew.SITWA20_x_vC_W30_se1585246474, InputTimeWindows.all3Min);
		run("SITWA10_vC_W40_se1585246474_4W", InputBufferNew.SITWA10_vC_W40_se1585246474, InputTimeWindows.all4Min);
		run("SITWA20_vC_W40_se1585246474_4W", InputBufferNew.SITWA20_vC_W40_se1585246474, InputTimeWindows.all4Min);
		run("SITWA10_x_vC_W40_se1585246474_4W", InputBufferNew.SITWA10_x_vC_W40_se1585246474, InputTimeWindows.all4Min);
		run("SITWA20_x_vC_W40_se1585246474_4W", InputBufferNew.SITWA20_x_vC_W40_se1585246474, InputTimeWindows.all4Min);
		run("SITWA10_vC_W50_se1585246474_5W", InputBufferNew.SITWA10_vC_W50_se1585246474, InputTimeWindows.all5Min);
		run("SITWA20_vC_W50_se1585246474_5W", InputBufferNew.SITWA20_vC_W50_se1585246474, InputTimeWindows.all5Min);
		run("SITWA10_x_vC_W50_se1585246474_5W", InputBufferNew.SITWA10_x_vC_W50_se1585246474, InputTimeWindows.all5Min);
		run("SITWA20_x_vC_W50_se1585246474_5W", InputBufferNew.SITWA20_x_vC_W50_se1585246474, InputTimeWindows.all5Min);
		run("SITWA10_vC_W60_se1585246474_6W", InputBufferNew.SITWA10_vC_W60_se1585246474, InputTimeWindows.all6Min);
		run("SITWA20_vC_W60_se1585246474_6W", InputBufferNew.SITWA20_vC_W60_se1585246474, InputTimeWindows.all6Min);
		run("SITWA10_x_vC_W60_se1585246474_6W", InputBufferNew.SITWA10_x_vC_W60_se1585246474, InputTimeWindows.all6Min);
		run("SITWA20_x_vC_W60_se1585246474_6W", InputBufferNew.SITWA20_x_vC_W60_se1585246474, InputTimeWindows.all6Min);
		run("SITWA10_vC_W70_se1585246474_7W", InputBufferNew.SITWA10_vC_W70_se1585246474, InputTimeWindows.all7Min);
		run("SITWA20_vC_W70_se1585246474_7W", InputBufferNew.SITWA20_vC_W70_se1585246474, InputTimeWindows.all7Min);
		run("SITWA10_x_vC_W70_se1585246474_7W", InputBufferNew.SITWA10_x_vC_W70_se1585246474, InputTimeWindows.all7Min);
		run("SITWA20_x_vC_W70_se1585246474_7W", InputBufferNew.SITWA20_x_vC_W70_se1585246474, InputTimeWindows.all7Min);
		run("SITWA10_vC_W80_se1585246474_8W", InputBufferNew.SITWA10_vC_W80_se1585246474, InputTimeWindows.all8Min);
		run("SITWA20_vC_W80_se1585246474_8W", InputBufferNew.SITWA20_vC_W80_se1585246474, InputTimeWindows.all8Min);
		run("SITWA10_x_vC_W80_se1585246474_8W", InputBufferNew.SITWA10_x_vC_W80_se1585246474, InputTimeWindows.all8Min);
		run("SITWA20_x_vC_W80_se1585246474_8W", InputBufferNew.SITWA20_x_vC_W80_se1585246474, InputTimeWindows.all8Min);
		run("SITWA10_vC_W90_se1585246474_9W", InputBufferNew.SITWA10_vC_W90_se1585246474, InputTimeWindows.all9Min);
		run("SITWA20_vC_W90_se1585246474_9W", InputBufferNew.SITWA20_vC_W90_se1585246474, InputTimeWindows.all9Min);
		run("SITWA10_x_vC_W90_se1585246474_9W", InputBufferNew.SITWA10_x_vC_W90_se1585246474, InputTimeWindows.all9Min);
		run("SITWA20_x_vC_W90_se1585246474_9W", InputBufferNew.SITWA20_x_vC_W90_se1585246474, InputTimeWindows.all9Min);
		run("SITWA10_vC_W100_se1585246474_10W", InputBufferNew.SITWA10_vC_W100_se1585246474, InputTimeWindows.all10Min);
		run("SITWA20_vC_W100_se1585246474_10W", InputBufferNew.SITWA20_vC_W100_se1585246474, InputTimeWindows.all10Min);
		run("SITWA10_x_vC_W100_se1585246474_10W", InputBufferNew.SITWA10_x_vC_W100_se1585246474,
				InputTimeWindows.all10Min);
		run("SITWA20_x_vC_W100_se1585246474_10W", InputBufferNew.SITWA20_x_vC_W100_se1585246474,
				InputTimeWindows.all10Min);

		settings.timeWindowMethod = "AfterArrival";

		run("aaSD_1W", InputBufferNew.bufferNewSD, InputTimeWindows.all1Min);
		run("aaSD_2W", InputBufferNew.bufferNewSD, InputTimeWindows.all2Min);
		run("aaSD_3W", InputBufferNew.bufferNewSD, InputTimeWindows.all3Min);
		run("aaSD_4W", InputBufferNew.bufferNewSD, InputTimeWindows.all4Min);
		run("aaSD_5W", InputBufferNew.bufferNewSD, InputTimeWindows.all5Min);
		run("aaSD_6W", InputBufferNew.bufferNewSD, InputTimeWindows.all6Min);
		run("aaSD_7W", InputBufferNew.bufferNewSD, InputTimeWindows.all7Min);
		run("aaSD_8W", InputBufferNew.bufferNewSD, InputTimeWindows.all8Min);
		run("aaSD_9W", InputBufferNew.bufferNewSD, InputTimeWindows.all9Min);
		run("aaSD_10W", InputBufferNew.bufferNewSD, InputTimeWindows.all10Min);

		run("aaSITWA10_vB_W00_se1585246474_1W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all1Min);
		run("aaSITWA20_vB_W00_se1585246474_1W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all1Min);
		run("aaSITWA10_x_vB_W00_se1585246474_1W", InputBufferNew.SITWA10_x_vB_W00_se1585246474,
				InputTimeWindows.all1Min);
		run("aaSITWA20_x_vB_W00_se1585246474_1W", InputBufferNew.SITWA20_x_vB_W00_se1585246474,
				InputTimeWindows.all1Min);
		run("aaSITWA10_vB_W00_se1585246474_2W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all2Min);
		run("aaSITWA20_vB_W00_se1585246474_2W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all2Min);
		run("aaSITWA10_x_vB_W00_se1585246474_2W", InputBufferNew.SITWA10_x_vB_W00_se1585246474,
				InputTimeWindows.all2Min);
		run("aaSITWA20_x_vB_W00_se1585246474_2W", InputBufferNew.SITWA20_x_vB_W00_se1585246474,
				InputTimeWindows.all2Min);
		run("aaSITWA10_vB_W00_se1585246474_3W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all3Min);
		run("aaSITWA20_vB_W00_se1585246474_3W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all3Min);
		run("aaSITWA10_x_vB_W00_se1585246474_3W", InputBufferNew.SITWA10_x_vB_W00_se1585246474,
				InputTimeWindows.all3Min);
		run("aaSITWA20_x_vB_W00_se1585246474_3W", InputBufferNew.SITWA20_x_vB_W00_se1585246474,
				InputTimeWindows.all3Min);
		run("aaSITWA10_vB_W00_se1585246474_4W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all4Min);
		run("aaSITWA20_vB_W00_se1585246474_4W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all4Min);
		run("aaSITWA10_x_vB_W00_se1585246474_4W", InputBufferNew.SITWA10_x_vB_W00_se1585246474,
				InputTimeWindows.all4Min);
		run("aaSITWA20_x_vB_W00_se1585246474_4W", InputBufferNew.SITWA20_x_vB_W00_se1585246474,
				InputTimeWindows.all4Min);
		run("aaSITWA10_vB_W00_se1585246474_5W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all5Min);
		run("aaSITWA20_vB_W00_se1585246474_5W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all5Min);
		run("aaSITWA10_x_vB_W00_se1585246474_5W", InputBufferNew.SITWA10_x_vB_W00_se1585246474,
				InputTimeWindows.all5Min);
		run("aaSITWA20_x_vB_W00_se1585246474_5W", InputBufferNew.SITWA20_x_vB_W00_se1585246474,
				InputTimeWindows.all5Min);
		run("aaSITWA10_vB_W00_se1585246474_6W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all6Min);
		run("aaSITWA20_vB_W00_se1585246474_6W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all6Min);
		run("aaSITWA10_x_vB_W00_se1585246474_6W", InputBufferNew.SITWA10_x_vB_W00_se1585246474,
				InputTimeWindows.all6Min);
		run("aaSITWA20_x_vB_W00_se1585246474_6W", InputBufferNew.SITWA20_x_vB_W00_se1585246474,
				InputTimeWindows.all6Min);
		run("aaSITWA10_vB_W00_se1585246474_7W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all7Min);
		run("aaSITWA20_vB_W00_se1585246474_7W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all7Min);
		run("aaSITWA10_x_vB_W00_se1585246474_7W", InputBufferNew.SITWA10_x_vB_W00_se1585246474,
				InputTimeWindows.all7Min);
		run("aaSITWA20_x_vB_W00_se1585246474_7W", InputBufferNew.SITWA20_x_vB_W00_se1585246474,
				InputTimeWindows.all7Min);
		run("aaSITWA10_vB_W00_se1585246474_8W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all8Min);
		run("aaSITWA20_vB_W00_se1585246474_8W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all8Min);
		run("aaSITWA10_x_vB_W00_se1585246474_8W", InputBufferNew.SITWA10_x_vB_W00_se1585246474,
				InputTimeWindows.all8Min);
		run("aaSITWA20_x_vB_W00_se1585246474_8W", InputBufferNew.SITWA20_x_vB_W00_se1585246474,
				InputTimeWindows.all8Min);
		run("aaSITWA10_vB_W00_se1585246474_9W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all9Min);
		run("aaSITWA20_vB_W00_se1585246474_9W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all9Min);
		run("aaSITWA10_x_vB_W00_se1585246474_9W", InputBufferNew.SITWA10_x_vB_W00_se1585246474,
				InputTimeWindows.all9Min);
		run("aaSITWA20_x_vB_W00_se1585246474_9W", InputBufferNew.SITWA20_x_vB_W00_se1585246474,
				InputTimeWindows.all9Min);
		run("aaSITWA10_vB_W00_se1585246474_10W", InputBufferNew.SITWA10_vB_W00_se1585246474, InputTimeWindows.all10Min);
		run("aaSITWA20_vB_W00_se1585246474_10W", InputBufferNew.SITWA20_vB_W00_se1585246474, InputTimeWindows.all10Min);
		run("aaSITWA10_x_vB_W00_se1585246474_10W", InputBufferNew.SITWA10_x_vB_W00_se1585246474,
				InputTimeWindows.all10Min);
		run("aaSITWA20_x_vB_W00_se1585246474_10W", InputBufferNew.SITWA20_x_vB_W00_se1585246474,
				InputTimeWindows.all10Min);

		run("aaSITWA10_vB_W10_se1585246474_1W", InputBufferNew.SITWA10_vB_W10_se1585246474, InputTimeWindows.all1Min);
		run("aaSITWA20_vB_W10_se1585246474_1W", InputBufferNew.SITWA20_vB_W10_se1585246474, InputTimeWindows.all1Min);
		run("aaSITWA10_x_vB_W10_se1585246474_1W", InputBufferNew.SITWA10_x_vB_W10_se1585246474,
				InputTimeWindows.all1Min);
		run("aaSITWA20_x_vB_W10_se1585246474_1W", InputBufferNew.SITWA20_x_vB_W10_se1585246474,
				InputTimeWindows.all1Min);
		run("aaSITWA10_vB_W20_se1585246474_2W", InputBufferNew.SITWA10_vB_W20_se1585246474, InputTimeWindows.all2Min);
		run("aaSITWA20_vB_W20_se1585246474_2W", InputBufferNew.SITWA20_vB_W20_se1585246474, InputTimeWindows.all2Min);
		run("aaSITWA10_x_vB_W20_se1585246474_2W", InputBufferNew.SITWA10_x_vB_W20_se1585246474,
				InputTimeWindows.all2Min);
		run("aaSITWA20_x_vB_W20_se1585246474_2W", InputBufferNew.SITWA20_x_vB_W20_se1585246474,
				InputTimeWindows.all2Min);
		run("aaSITWA10_vB_W30_se1585246474_3W", InputBufferNew.SITWA10_vB_W30_se1585246474, InputTimeWindows.all3Min);
		run("aaSITWA20_vB_W30_se1585246474_3W", InputBufferNew.SITWA20_vB_W30_se1585246474, InputTimeWindows.all3Min);
		run("aaSITWA10_x_vB_W30_se1585246474_3W", InputBufferNew.SITWA10_x_vB_W30_se1585246474,
				InputTimeWindows.all3Min);
		run("aaSITWA20_x_vB_W30_se1585246474_3W", InputBufferNew.SITWA20_x_vB_W30_se1585246474,
				InputTimeWindows.all3Min);
		run("aaSITWA10_vB_W40_se1585246474_4W", InputBufferNew.SITWA10_vB_W40_se1585246474, InputTimeWindows.all4Min);
		run("aaSITWA20_vB_W40_se1585246474_4W", InputBufferNew.SITWA20_vB_W40_se1585246474, InputTimeWindows.all4Min);
		run("aaSITWA10_x_vB_W40_se1585246474_4W", InputBufferNew.SITWA10_x_vB_W40_se1585246474,
				InputTimeWindows.all4Min);
		run("aaSITWA20_x_vB_W40_se1585246474_4W", InputBufferNew.SITWA20_x_vB_W40_se1585246474,
				InputTimeWindows.all4Min);
		run("aaSITWA10_vB_W50_se1585246474_5W", InputBufferNew.SITWA10_vB_W50_se1585246474, InputTimeWindows.all5Min);
		run("aaSITWA20_vB_W50_se1585246474_5W", InputBufferNew.SITWA20_vB_W50_se1585246474, InputTimeWindows.all5Min);
		run("aaSITWA10_x_vB_W50_se1585246474_5W", InputBufferNew.SITWA10_x_vB_W50_se1585246474,
				InputTimeWindows.all5Min);
		run("aaSITWA20_x_vB_W50_se1585246474_5W", InputBufferNew.SITWA20_x_vB_W50_se1585246474,
				InputTimeWindows.all5Min);
		run("aaSITWA10_vB_W60_se1585246474_6W", InputBufferNew.SITWA10_vB_W60_se1585246474, InputTimeWindows.all6Min);
		run("aaSITWA20_vB_W60_se1585246474_6W", InputBufferNew.SITWA20_vB_W60_se1585246474, InputTimeWindows.all6Min);
		run("aaSITWA10_x_vB_W60_se1585246474_6W", InputBufferNew.SITWA10_x_vB_W60_se1585246474,
				InputTimeWindows.all6Min);
		run("aaSITWA20_x_vB_W60_se1585246474_6W", InputBufferNew.SITWA20_x_vB_W60_se1585246474,
				InputTimeWindows.all6Min);
		run("aaSITWA10_vB_W70_se1585246474_7W", InputBufferNew.SITWA10_vB_W70_se1585246474, InputTimeWindows.all7Min);
		run("aaSITWA20_vB_W70_se1585246474_7W", InputBufferNew.SITWA20_vB_W70_se1585246474, InputTimeWindows.all7Min);
		run("aaSITWA10_x_vB_W70_se1585246474_7W", InputBufferNew.SITWA10_x_vB_W70_se1585246474,
				InputTimeWindows.all7Min);
		run("aaSITWA20_x_vB_W70_se1585246474_7W", InputBufferNew.SITWA20_x_vB_W70_se1585246474,
				InputTimeWindows.all7Min);
		run("aaSITWA10_vB_W80_se1585246474_8W", InputBufferNew.SITWA10_vB_W80_se1585246474, InputTimeWindows.all8Min);
		run("aaSITWA20_vB_W80_se1585246474_8W", InputBufferNew.SITWA20_vB_W80_se1585246474, InputTimeWindows.all8Min);
		run("aaSITWA10_x_vB_W80_se1585246474_8W", InputBufferNew.SITWA10_x_vB_W80_se1585246474,
				InputTimeWindows.all8Min);
		run("aaSITWA20_x_vB_W80_se1585246474_8W", InputBufferNew.SITWA20_x_vB_W80_se1585246474,
				InputTimeWindows.all8Min);
		run("aaSITWA10_vB_W90_se1585246474_9W", InputBufferNew.SITWA10_vB_W90_se1585246474, InputTimeWindows.all9Min);
		run("aaSITWA20_vB_W90_se1585246474_9W", InputBufferNew.SITWA20_vB_W90_se1585246474, InputTimeWindows.all9Min);
		run("aaSITWA10_x_vB_W90_se1585246474_9W", InputBufferNew.SITWA10_x_vB_W90_se1585246474,
				InputTimeWindows.all9Min);
		run("aaSITWA20_x_vB_W90_se1585246474_9W", InputBufferNew.SITWA20_x_vB_W90_se1585246474,
				InputTimeWindows.all9Min);
		run("aaSITWA10_vB_W100_se1585246474_10W", InputBufferNew.SITWA10_vB_W100_se1585246474,
				InputTimeWindows.all10Min);
		run("aaSITWA20_vB_W100_se1585246474_10W", InputBufferNew.SITWA20_vB_W100_se1585246474,
				InputTimeWindows.all10Min);
		run("aaSITWA10_x_vB_W100_se1585246474_10W", InputBufferNew.SITWA10_x_vB_W100_se1585246474,
				InputTimeWindows.all10Min);
		run("aaSITWA20_x_vB_W100_se1585246474_10W", InputBufferNew.SITWA20_x_vB_W100_se1585246474,
				InputTimeWindows.all10Min);

		run("aaSITWA10_vC_W00_se1585246474_1W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all1Min);
		run("aaSITWA20_vC_W00_se1585246474_1W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all1Min);
		run("aaSITWA10_x_vC_W00_se1585246474_1W", InputBufferNew.SITWA10_x_vC_W00_se1585246474,
				InputTimeWindows.all1Min);
		run("aaSITWA20_x_vC_W00_se1585246474_1W", InputBufferNew.SITWA20_x_vC_W00_se1585246474,
				InputTimeWindows.all1Min);
		run("aaSITWA10_vC_W00_se1585246474_2W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all2Min);
		run("aaSITWA20_vC_W00_se1585246474_2W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all2Min);
		run("aaSITWA10_x_vC_W00_se1585246474_2W", InputBufferNew.SITWA10_x_vC_W00_se1585246474,
				InputTimeWindows.all2Min);
		run("aaSITWA20_x_vC_W00_se1585246474_2W", InputBufferNew.SITWA20_x_vC_W00_se1585246474,
				InputTimeWindows.all2Min);
		run("aaSITWA10_vC_W00_se1585246474_3W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all3Min);
		run("aaSITWA20_vC_W00_se1585246474_3W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all3Min);
		run("aaSITWA10_x_vC_W00_se1585246474_3W", InputBufferNew.SITWA10_x_vC_W00_se1585246474,
				InputTimeWindows.all3Min);
		run("aaSITWA20_x_vC_W00_se1585246474_3W", InputBufferNew.SITWA20_x_vC_W00_se1585246474,
				InputTimeWindows.all3Min);
		run("aaSITWA10_vC_W00_se1585246474_4W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all4Min);
		run("aaSITWA20_vC_W00_se1585246474_4W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all4Min);
		run("aaSITWA10_x_vC_W00_se1585246474_4W", InputBufferNew.SITWA10_x_vC_W00_se1585246474,
				InputTimeWindows.all4Min);
		run("aaSITWA20_x_vC_W00_se1585246474_4W", InputBufferNew.SITWA20_x_vC_W00_se1585246474,
				InputTimeWindows.all4Min);
		run("aaSITWA10_vC_W00_se1585246474_5W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all5Min);
		run("aaSITWA20_vC_W00_se1585246474_5W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all5Min);
		run("aaSITWA10_x_vC_W00_se1585246474_5W", InputBufferNew.SITWA10_x_vC_W00_se1585246474,
				InputTimeWindows.all5Min);
		run("aaSITWA20_x_vC_W00_se1585246474_5W", InputBufferNew.SITWA20_x_vC_W00_se1585246474,
				InputTimeWindows.all5Min);
		run("aaSITWA10_vC_W00_se1585246474_6W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all6Min);
		run("aaSITWA20_vC_W00_se1585246474_6W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all6Min);
		run("aaSITWA10_x_vC_W00_se1585246474_6W", InputBufferNew.SITWA10_x_vC_W00_se1585246474,
				InputTimeWindows.all6Min);
		run("aaSITWA20_x_vC_W00_se1585246474_6W", InputBufferNew.SITWA20_x_vC_W00_se1585246474,
				InputTimeWindows.all6Min);
		run("aaSITWA10_vC_W00_se1585246474_7W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all7Min);
		run("aaSITWA20_vC_W00_se1585246474_7W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all7Min);
		run("aaSITWA10_x_vC_W00_se1585246474_7W", InputBufferNew.SITWA10_x_vC_W00_se1585246474,
				InputTimeWindows.all7Min);
		run("aaSITWA20_x_vC_W00_se1585246474_7W", InputBufferNew.SITWA20_x_vC_W00_se1585246474,
				InputTimeWindows.all7Min);
		run("aaSITWA10_vC_W00_se1585246474_8W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all8Min);
		run("aaSITWA20_vC_W00_se1585246474_8W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all8Min);
		run("aaSITWA10_x_vC_W00_se1585246474_8W", InputBufferNew.SITWA10_x_vC_W00_se1585246474,
				InputTimeWindows.all8Min);
		run("aaSITWA20_x_vC_W00_se1585246474_8W", InputBufferNew.SITWA20_x_vC_W00_se1585246474,
				InputTimeWindows.all8Min);
		run("aaSITWA10_vC_W00_se1585246474_9W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all9Min);
		run("aaSITWA20_vC_W00_se1585246474_9W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all9Min);
		run("aaSITWA10_x_vC_W00_se1585246474_9W", InputBufferNew.SITWA10_x_vC_W00_se1585246474,
				InputTimeWindows.all9Min);
		run("aaSITWA20_x_vC_W00_se1585246474_9W", InputBufferNew.SITWA20_x_vC_W00_se1585246474,
				InputTimeWindows.all9Min);
		run("aaSITWA10_vC_W00_se1585246474_10W", InputBufferNew.SITWA10_vC_W00_se1585246474, InputTimeWindows.all10Min);
		run("aaSITWA20_vC_W00_se1585246474_10W", InputBufferNew.SITWA20_vC_W00_se1585246474, InputTimeWindows.all10Min);
		run("aaSITWA10_x_vC_W00_se1585246474_10W", InputBufferNew.SITWA10_x_vC_W00_se1585246474,
				InputTimeWindows.all10Min);
		run("aaSITWA20_x_vC_W00_se1585246474_10W", InputBufferNew.SITWA20_x_vC_W00_se1585246474,
				InputTimeWindows.all10Min);

		run("aaSITWA10_vC_W10_se1585246474_1W", InputBufferNew.SITWA10_vC_W10_se1585246474, InputTimeWindows.all1Min);
		run("aaSITWA20_vC_W10_se1585246474_1W", InputBufferNew.SITWA20_vC_W10_se1585246474, InputTimeWindows.all1Min);
		run("aaSITWA10_x_vC_W10_se1585246474_1W", InputBufferNew.SITWA10_x_vC_W10_se1585246474,
				InputTimeWindows.all1Min);
		run("aaSITWA20_x_vC_W10_se1585246474_1W", InputBufferNew.SITWA20_x_vC_W10_se1585246474,
				InputTimeWindows.all1Min);
		run("aaSITWA10_vC_W20_se1585246474_2W", InputBufferNew.SITWA10_vC_W20_se1585246474, InputTimeWindows.all2Min);
		run("aaSITWA20_vC_W20_se1585246474_2W", InputBufferNew.SITWA20_vC_W20_se1585246474, InputTimeWindows.all2Min);
		run("aaSITWA10_x_vC_W20_se1585246474_2W", InputBufferNew.SITWA10_x_vC_W20_se1585246474,
				InputTimeWindows.all2Min);
		run("aaSITWA20_x_vC_W20_se1585246474_2W", InputBufferNew.SITWA20_x_vC_W20_se1585246474,
				InputTimeWindows.all2Min);
		run("aaSITWA10_vC_W30_se1585246474_3W", InputBufferNew.SITWA10_vC_W30_se1585246474, InputTimeWindows.all3Min);
		run("aaSITWA20_vC_W30_se1585246474_3W", InputBufferNew.SITWA20_vC_W30_se1585246474, InputTimeWindows.all3Min);
		run("aaSITWA10_x_vC_W30_se1585246474_3W", InputBufferNew.SITWA10_x_vC_W30_se1585246474,
				InputTimeWindows.all3Min);
		run("aaSITWA20_x_vC_W30_se1585246474_3W", InputBufferNew.SITWA20_x_vC_W30_se1585246474,
				InputTimeWindows.all3Min);
		run("aaSITWA10_vC_W40_se1585246474_4W", InputBufferNew.SITWA10_vC_W40_se1585246474, InputTimeWindows.all4Min);
		run("aaSITWA20_vC_W40_se1585246474_4W", InputBufferNew.SITWA20_vC_W40_se1585246474, InputTimeWindows.all4Min);
		run("aaSITWA10_x_vC_W40_se1585246474_4W", InputBufferNew.SITWA10_x_vC_W40_se1585246474,
				InputTimeWindows.all4Min);
		run("aaSITWA20_x_vC_W40_se1585246474_4W", InputBufferNew.SITWA20_x_vC_W40_se1585246474,
				InputTimeWindows.all4Min);
		run("aaSITWA10_vC_W50_se1585246474_5W", InputBufferNew.SITWA10_vC_W50_se1585246474, InputTimeWindows.all5Min);
		run("aaSITWA20_vC_W50_se1585246474_5W", InputBufferNew.SITWA20_vC_W50_se1585246474, InputTimeWindows.all5Min);
		run("aaSITWA10_x_vC_W50_se1585246474_5W", InputBufferNew.SITWA10_x_vC_W50_se1585246474,
				InputTimeWindows.all5Min);
		run("aaSITWA20_x_vC_W50_se1585246474_5W", InputBufferNew.SITWA20_x_vC_W50_se1585246474,
				InputTimeWindows.all5Min);
		run("aaSITWA10_vC_W60_se1585246474_6W", InputBufferNew.SITWA10_vC_W60_se1585246474, InputTimeWindows.all6Min);
		run("aaSITWA20_vC_W60_se1585246474_6W", InputBufferNew.SITWA20_vC_W60_se1585246474, InputTimeWindows.all6Min);
		run("aaSITWA10_x_vC_W60_se1585246474_6W", InputBufferNew.SITWA10_x_vC_W60_se1585246474,
				InputTimeWindows.all6Min);
		run("aaSITWA20_x_vC_W60_se1585246474_6W", InputBufferNew.SITWA20_x_vC_W60_se1585246474,
				InputTimeWindows.all6Min);
		run("aaSITWA10_vC_W70_se1585246474_7W", InputBufferNew.SITWA10_vC_W70_se1585246474, InputTimeWindows.all7Min);
		run("aaSITWA20_vC_W70_se1585246474_7W", InputBufferNew.SITWA20_vC_W70_se1585246474, InputTimeWindows.all7Min);
		run("aaSITWA10_x_vC_W70_se1585246474_7W", InputBufferNew.SITWA10_x_vC_W70_se1585246474,
				InputTimeWindows.all7Min);
		run("aaSITWA20_x_vC_W70_se1585246474_7W", InputBufferNew.SITWA20_x_vC_W70_se1585246474,
				InputTimeWindows.all7Min);
		run("aaSITWA10_vC_W80_se1585246474_8W", InputBufferNew.SITWA10_vC_W80_se1585246474, InputTimeWindows.all8Min);
		run("aaSITWA20_vC_W80_se1585246474_8W", InputBufferNew.SITWA20_vC_W80_se1585246474, InputTimeWindows.all8Min);
		run("aaSITWA10_x_vC_W80_se1585246474_8W", InputBufferNew.SITWA10_x_vC_W80_se1585246474,
				InputTimeWindows.all8Min);
		run("aaSITWA20_x_vC_W80_se1585246474_8W", InputBufferNew.SITWA20_x_vC_W80_se1585246474,
				InputTimeWindows.all8Min);
		run("aaSITWA10_vC_W90_se1585246474_9W", InputBufferNew.SITWA10_vC_W90_se1585246474, InputTimeWindows.all9Min);
		run("aaSITWA20_vC_W90_se1585246474_9W", InputBufferNew.SITWA20_vC_W90_se1585246474, InputTimeWindows.all9Min);
		run("aaSITWA10_x_vC_W90_se1585246474_9W", InputBufferNew.SITWA10_x_vC_W90_se1585246474,
				InputTimeWindows.all9Min);
		run("aaSITWA20_x_vC_W90_se1585246474_9W", InputBufferNew.SITWA20_x_vC_W90_se1585246474,
				InputTimeWindows.all9Min);
		run("aaSITWA10_vC_W100_se1585246474_10W", InputBufferNew.SITWA10_vC_W100_se1585246474,
				InputTimeWindows.all10Min);
		run("aaSITWA20_vC_W100_se1585246474_10W", InputBufferNew.SITWA20_vC_W100_se1585246474,
				InputTimeWindows.all10Min);
		run("aaSITWA10_x_vC_W100_se1585246474_10W", InputBufferNew.SITWA10_x_vC_W100_se1585246474,
				InputTimeWindows.all10Min);
		run("aaSITWA20_x_vC_W100_se1585246474_10W", InputBufferNew.SITWA20_x_vC_W100_se1585246474,
				InputTimeWindows.all10Min);

	}

}

//run("BASE_1W", InputBuffer.bufferNone, InputTimeWindows.all1Min);
//run("BASE_2W", InputBuffer.bufferNone, InputTimeWindows.all2Min);
//run("BASE_3W", InputBuffer.bufferNone, InputTimeWindows.all3Min);
//run("BASE_4W", InputBuffer.bufferNone, InputTimeWindows.all4Min);
//run("BASE_5W", InputBuffer.bufferNone, InputTimeWindows.all5Min);
//run("BASE_6W", InputBuffer.bufferNone, InputTimeWindows.all6Min);
//run("BASE_7W", InputBuffer.bufferNone, InputTimeWindows.all7Min);
//run("BASE_8W", InputBuffer.bufferNone, InputTimeWindows.all8Min);
//run("BASE_9W", InputBuffer.bufferNone, InputTimeWindows.all9Min);
//run("BASE_10W", InputBuffer.bufferNone, InputTimeWindows.all10Min);
//
//run("SITWA_1W", InputBuffer.bufferNewSITWA_1W, InputTimeWindows.all1Min);
//run("SITWA_2W", InputBuffer.bufferNewSITWA_2W, InputTimeWindows.all2Min);
//run("SITWA_3W", InputBuffer.bufferNewSITWA_3W, InputTimeWindows.all3Min);
//run("SITWA_4W", InputBuffer.bufferNewSITWA_4W, InputTimeWindows.all4Min);
//run("SITWA_5W", InputBuffer.bufferNewSITWA_5W, InputTimeWindows.all5Min);
//run("SITWA_6W", InputBuffer.bufferNewSITWA_6W, InputTimeWindows.all6Min);
//run("SITWA_7W", InputBuffer.bufferNewSITWA_7W, InputTimeWindows.all7Min);
//run("SITWA_8W", InputBuffer.bufferNewSITWA_8W, InputTimeWindows.all8Min);
//run("SITWA_9W", InputBuffer.bufferNewSITWA_9W, InputTimeWindows.all9Min);
//run("SITWA_10W", InputBuffer.bufferNewSITWA_10W, InputTimeWindows.all10Min);
//
//run("6P_1W", InputBuffer.bufferNew6P, InputTimeWindows.all1Min);
//run("6P_2W", InputBuffer.bufferNew6P, InputTimeWindows.all2Min);
//run("6P_3W", InputBuffer.bufferNew6P, InputTimeWindows.all3Min);
//run("6P_4W", InputBuffer.bufferNew6P, InputTimeWindows.all4Min);
//run("6P_5W", InputBuffer.bufferNew6P, InputTimeWindows.all5Min);
//run("6P_6W", InputBuffer.bufferNew6P, InputTimeWindows.all6Min);
//run("6P_7W", InputBuffer.bufferNew6P, InputTimeWindows.all7Min);
//run("6P_8W", InputBuffer.bufferNew6P, InputTimeWindows.all8Min);
//run("6P_9W", InputBuffer.bufferNew6P, InputTimeWindows.all9Min);
//run("6P_10W", InputBuffer.bufferNew6P, InputTimeWindows.all10Min);
//
//run("SD_1W", InputBuffer.bufferNewSD, InputTimeWindows.all1Min);
//run("SD_2W", InputBuffer.bufferNewSD, InputTimeWindows.all2Min);
//run("SD_3W", InputBuffer.bufferNewSD, InputTimeWindows.all3Min);
//run("SD_4W", InputBuffer.bufferNewSD, InputTimeWindows.all4Min);
//run("SD_5W", InputBuffer.bufferNewSD, InputTimeWindows.all5Min);
//run("SD_6W", InputBuffer.bufferNewSD, InputTimeWindows.all6Min);
//run("SD_7W", InputBuffer.bufferNewSD, InputTimeWindows.all7Min);
//run("SD_8W", InputBuffer.bufferNewSD, InputTimeWindows.all8Min);
//run("SD_9W", InputBuffer.bufferNewSD, InputTimeWindows.all9Min);
//run("SD_10W", InputBuffer.bufferNewSD, InputTimeWindows.all10Min);
//
//run("SITWA_1W_40", InputBuffer.bufferSITWA_1W_40, InputTimeWindows.all1Min);
//run("SITWA_2W_40", InputBuffer.bufferSITWA_2W_40, InputTimeWindows.all2Min);
//run("SITWA_3W_40", InputBuffer.bufferSITWA_3W_40, InputTimeWindows.all3Min);
//run("SITWA_4W_40", InputBuffer.bufferSITWA_4W_40, InputTimeWindows.all4Min);
//run("SITWA_5W_40", InputBuffer.bufferSITWA_5W_40, InputTimeWindows.all5Min);
//run("SITWA_6W_40", InputBuffer.bufferSITWA_6W_40, InputTimeWindows.all6Min);
//run("SITWA_7W_40", InputBuffer.bufferSITWA_7W_40, InputTimeWindows.all7Min);
//run("SITWA_8W_40", InputBuffer.bufferSITWA_8W_40, InputTimeWindows.all8Min);
//run("SITWA_9W_40", InputBuffer.bufferSITWA_9W_40, InputTimeWindows.all9Min);
//run("SITWA_10W_40", InputBuffer.bufferSITWA_10W_40, InputTimeWindows.all10Min);
//
//run("SITWA_1W_405", InputBuffer.bufferSITWA_1W_405, InputTimeWindows.all1Min);
//run("SITWA_2W_405", InputBuffer.bufferSITWA_2W_405, InputTimeWindows.all2Min);
//run("SITWA_3W_405", InputBuffer.bufferSITWA_3W_405, InputTimeWindows.all3Min);
//run("SITWA_4W_405", InputBuffer.bufferSITWA_4W_405, InputTimeWindows.all4Min);
//run("SITWA_5W_405", InputBuffer.bufferSITWA_5W_405, InputTimeWindows.all5Min);
//run("SITWA_6W_405", InputBuffer.bufferSITWA_6W_405, InputTimeWindows.all6Min);
//run("SITWA_7W_405", InputBuffer.bufferSITWA_7W_405, InputTimeWindows.all7Min);
//run("SITWA_8W_405", InputBuffer.bufferSITWA_8W_405, InputTimeWindows.all8Min);
//run("SITWA_9W_405", InputBuffer.bufferSITWA_9W_405, InputTimeWindows.all9Min);
//run("SITWA_10W_405", InputBuffer.bufferSITWA_10W_405, InputTimeWindows.all10Min);
//
//run("SITWA_1W_41", InputBuffer.bufferSITWA_1W_41, InputTimeWindows.all1Min);
//run("SITWA_2W_41", InputBuffer.bufferSITWA_2W_41, InputTimeWindows.all2Min);
//run("SITWA_3W_41", InputBuffer.bufferSITWA_3W_41, InputTimeWindows.all3Min);
//run("SITWA_4W_41", InputBuffer.bufferSITWA_4W_41, InputTimeWindows.all4Min);
//run("SITWA_5W_41", InputBuffer.bufferSITWA_5W_41, InputTimeWindows.all5Min);
//run("SITWA_6W_41", InputBuffer.bufferSITWA_6W_41, InputTimeWindows.all6Min);
//run("SITWA_7W_41", InputBuffer.bufferSITWA_7W_41, InputTimeWindows.all7Min);
//run("SITWA_8W_41", InputBuffer.bufferSITWA_8W_41, InputTimeWindows.all8Min);
//run("SITWA_9W_41", InputBuffer.bufferSITWA_9W_41, InputTimeWindows.all9Min);
//run("SITWA_10W_41", InputBuffer.bufferSITWA_10W_41, InputTimeWindows.all10Min);
//
//run("SITWA_1W_415", InputBuffer.bufferSITWA_1W_415, InputTimeWindows.all1Min);
//run("SITWA_2W_415", InputBuffer.bufferSITWA_2W_415, InputTimeWindows.all2Min);
//run("SITWA_3W_415", InputBuffer.bufferSITWA_3W_415, InputTimeWindows.all3Min);
//run("SITWA_4W_415", InputBuffer.bufferSITWA_4W_415, InputTimeWindows.all4Min);
//run("SITWA_5W_415", InputBuffer.bufferSITWA_5W_415, InputTimeWindows.all5Min);
//run("SITWA_6W_415", InputBuffer.bufferSITWA_6W_415, InputTimeWindows.all6Min);
//run("SITWA_7W_415", InputBuffer.bufferSITWA_7W_415, InputTimeWindows.all7Min);
//run("SITWA_8W_415", InputBuffer.bufferSITWA_8W_415, InputTimeWindows.all8Min);
//run("SITWA_9W_415", InputBuffer.bufferSITWA_9W_415, InputTimeWindows.all9Min);
//run("SITWA_10W_415", InputBuffer.bufferSITWA_10W_415, InputTimeWindows.all10Min);
//
//run("SITWA_1W_42", InputBuffer.bufferSITWA_1W_42, InputTimeWindows.all1Min);
//run("SITWA_2W_42", InputBuffer.bufferSITWA_2W_42, InputTimeWindows.all2Min);
//run("SITWA_3W_42", InputBuffer.bufferSITWA_3W_42, InputTimeWindows.all3Min);
//run("SITWA_4W_42", InputBuffer.bufferSITWA_4W_42, InputTimeWindows.all4Min);
//run("SITWA_5W_42", InputBuffer.bufferSITWA_5W_42, InputTimeWindows.all5Min);
//run("SITWA_6W_42", InputBuffer.bufferSITWA_6W_42, InputTimeWindows.all6Min);
//run("SITWA_7W_42", InputBuffer.bufferSITWA_7W_42, InputTimeWindows.all7Min);
//run("SITWA_8W_42", InputBuffer.bufferSITWA_8W_42, InputTimeWindows.all8Min);
//run("SITWA_9W_42", InputBuffer.bufferSITWA_9W_42, InputTimeWindows.all9Min);
//run("SITWA_10W_42", InputBuffer.bufferSITWA_10W_42, InputTimeWindows.all10Min);
//
//run("SITWA_1W_425", InputBuffer.bufferSITWA_1W_425, InputTimeWindows.all1Min);
//run("SITWA_2W_425", InputBuffer.bufferSITWA_2W_425, InputTimeWindows.all2Min);
//run("SITWA_3W_425", InputBuffer.bufferSITWA_3W_425, InputTimeWindows.all3Min);
//run("SITWA_4W_425", InputBuffer.bufferSITWA_4W_425, InputTimeWindows.all4Min);
//run("SITWA_5W_425", InputBuffer.bufferSITWA_5W_425, InputTimeWindows.all5Min);
//run("SITWA_6W_425", InputBuffer.bufferSITWA_6W_425, InputTimeWindows.all6Min);
//run("SITWA_7W_425", InputBuffer.bufferSITWA_7W_425, InputTimeWindows.all7Min);
//run("SITWA_8W_425", InputBuffer.bufferSITWA_8W_425, InputTimeWindows.all8Min);
//run("SITWA_9W_425", InputBuffer.bufferSITWA_9W_425, InputTimeWindows.all9Min);
//run("SITWA_10W_425", InputBuffer.bufferSITWA_10W_425, InputTimeWindows.all10Min);
//
//run("SITWA_1W_43", InputBuffer.bufferSITWA_1W_43, InputTimeWindows.all1Min);
//run("SITWA_2W_43", InputBuffer.bufferSITWA_2W_43, InputTimeWindows.all2Min);
//run("SITWA_3W_43", InputBuffer.bufferSITWA_3W_43, InputTimeWindows.all3Min);
//run("SITWA_4W_43", InputBuffer.bufferSITWA_4W_43, InputTimeWindows.all4Min);
//run("SITWA_5W_43", InputBuffer.bufferSITWA_5W_43, InputTimeWindows.all5Min);
//run("SITWA_6W_43", InputBuffer.bufferSITWA_6W_43, InputTimeWindows.all6Min);
//run("SITWA_7W_43", InputBuffer.bufferSITWA_7W_43, InputTimeWindows.all7Min);
//run("SITWA_8W_43", InputBuffer.bufferSITWA_8W_43, InputTimeWindows.all8Min);
//run("SITWA_9W_43", InputBuffer.bufferSITWA_9W_43, InputTimeWindows.all9Min);
//run("SITWA_10W_43", InputBuffer.bufferSITWA_10W_43, InputTimeWindows.all10Min);

//run("SITWA_vD_1W_1se", InputBuffer.SITWA_vD_1W_1se, InputTimeWindows.all1Min);
//run("SITWA_vD_2W_1se", InputBuffer.SITWA_vD_2W_1se, InputTimeWindows.all2Min);
//run("SITWA_vD_3W_1se", InputBuffer.SITWA_vD_3W_1se, InputTimeWindows.all3Min);
//run("SITWA_vD_4W_1se", InputBuffer.SITWA_vD_4W_1se, InputTimeWindows.all4Min);
//run("SITWA_vD_5W_1se", InputBuffer.SITWA_vD_5W_1se, InputTimeWindows.all5Min);
//run("SITWA_vD_6W_1se", InputBuffer.SITWA_vD_6W_1se, InputTimeWindows.all6Min);
//run("SITWA_vD_7W_1se", InputBuffer.SITWA_vD_7W_1se, InputTimeWindows.all7Min);
//run("SITWA_vD_8W_1se", InputBuffer.SITWA_vD_8W_1se, InputTimeWindows.all8Min);
//run("SITWA_vD_9W_1se", InputBuffer.SITWA_vD_9W_1se, InputTimeWindows.all9Min);
//run("SITWA_vD_10W_1se", InputBuffer.SITWA_vD_10W_1se, InputTimeWindows.all10Min);
//
//run("SITWA_vD_1W_2se", InputBuffer.SITWA_vD_1W_2se, InputTimeWindows.all1Min);
//run("SITWA_vD_2W_2se", InputBuffer.SITWA_vD_2W_2se, InputTimeWindows.all2Min);
//run("SITWA_vD_3W_2se", InputBuffer.SITWA_vD_3W_2se, InputTimeWindows.all3Min);
//run("SITWA_vD_4W_2se", InputBuffer.SITWA_vD_4W_2se, InputTimeWindows.all4Min);
//run("SITWA_vD_5W_2se", InputBuffer.SITWA_vD_5W_2se, InputTimeWindows.all5Min);
//run("SITWA_vD_6W_2se", InputBuffer.SITWA_vD_6W_2se, InputTimeWindows.all6Min);
//run("SITWA_vD_7W_2se", InputBuffer.SITWA_vD_7W_2se, InputTimeWindows.all7Min);
//run("SITWA_vD_8W_2se", InputBuffer.SITWA_vD_8W_2se, InputTimeWindows.all8Min);
//run("SITWA_vD_9W_2se", InputBuffer.SITWA_vD_9W_2se, InputTimeWindows.all9Min);
//run("SITWA_vD_10W_2se", InputBuffer.SITWA_vD_10W_2se, InputTimeWindows.all10Min);
//
//run("SITWA_vD_1W_3se", InputBuffer.SITWA_vD_1W_3se, InputTimeWindows.all1Min);
//run("SITWA_vD_2W_3se", InputBuffer.SITWA_vD_2W_3se, InputTimeWindows.all2Min);
//run("SITWA_vD_3W_3se", InputBuffer.SITWA_vD_3W_3se, InputTimeWindows.all3Min);
//run("SITWA_vD_4W_3se", InputBuffer.SITWA_vD_4W_3se, InputTimeWindows.all4Min);
//run("SITWA_vD_5W_3se", InputBuffer.SITWA_vD_5W_3se, InputTimeWindows.all5Min);
//run("SITWA_vD_6W_3se", InputBuffer.SITWA_vD_6W_3se, InputTimeWindows.all6Min);
//run("SITWA_vD_7W_3se", InputBuffer.SITWA_vD_7W_3se, InputTimeWindows.all7Min);
//run("SITWA_vD_8W_3se", InputBuffer.SITWA_vD_8W_3se, InputTimeWindows.all8Min);
//run("SITWA_vD_9W_3se", InputBuffer.SITWA_vD_9W_3se, InputTimeWindows.all9Min);
//run("SITWA_vD_10W_3se", InputBuffer.SITWA_vD_10W_3se, InputTimeWindows.all10Min);
//
//run("SITWA_vD_1W_4se", InputBuffer.SITWA_vD_1W_4se, InputTimeWindows.all1Min);
//run("SITWA_vD_2W_4se", InputBuffer.SITWA_vD_2W_4se, InputTimeWindows.all2Min);
//run("SITWA_vD_3W_4se", InputBuffer.SITWA_vD_3W_4se, InputTimeWindows.all3Min);
//run("SITWA_vD_4W_4se", InputBuffer.SITWA_vD_4W_4se, InputTimeWindows.all4Min);
//run("SITWA_vD_5W_4se", InputBuffer.SITWA_vD_5W_4se, InputTimeWindows.all5Min);
//run("SITWA_vD_6W_4se", InputBuffer.SITWA_vD_6W_4se, InputTimeWindows.all6Min);
//run("SITWA_vD_7W_4se", InputBuffer.SITWA_vD_7W_4se, InputTimeWindows.all7Min);
//run("SITWA_vD_8W_4se", InputBuffer.SITWA_vD_8W_4se, InputTimeWindows.all8Min);
//run("SITWA_vD_9W_4se", InputBuffer.SITWA_vD_9W_4se, InputTimeWindows.all9Min);
//run("SITWA_vD_10W_4se", InputBuffer.SITWA_vD_10W_4se, InputTimeWindows.all10Min);
//
//run("SITWA_vD_1W_5se", InputBuffer.SITWA_vD_1W_5se, InputTimeWindows.all1Min);
//run("SITWA_vD_2W_5se", InputBuffer.SITWA_vD_2W_5se, InputTimeWindows.all2Min);
//run("SITWA_vD_3W_5se", InputBuffer.SITWA_vD_3W_5se, InputTimeWindows.all3Min);
//run("SITWA_vD_4W_5se", InputBuffer.SITWA_vD_4W_5se, InputTimeWindows.all4Min);
//run("SITWA_vD_5W_5se", InputBuffer.SITWA_vD_5W_5se, InputTimeWindows.all5Min);
//run("SITWA_vD_6W_5se", InputBuffer.SITWA_vD_6W_5se, InputTimeWindows.all6Min);
//run("SITWA_vD_7W_5se", InputBuffer.SITWA_vD_7W_5se, InputTimeWindows.all7Min);
//run("SITWA_vD_8W_5se", InputBuffer.SITWA_vD_8W_5se, InputTimeWindows.all8Min);
//run("SITWA_vD_9W_5se", InputBuffer.SITWA_vD_9W_5se, InputTimeWindows.all9Min);
//run("SITWA_vD_10W_5se", InputBuffer.SITWA_vD_10W_5se, InputTimeWindows.all10Min);
//
//run("SITWA_vD_1W_6se", InputBuffer.SITWA_vD_1W_6se, InputTimeWindows.all1Min);
//run("SITWA_vD_2W_6se", InputBuffer.SITWA_vD_2W_6se, InputTimeWindows.all2Min);
//run("SITWA_vD_3W_6se", InputBuffer.SITWA_vD_3W_6se, InputTimeWindows.all3Min);
//run("SITWA_vD_4W_6se", InputBuffer.SITWA_vD_4W_6se, InputTimeWindows.all4Min);
//run("SITWA_vD_5W_6se", InputBuffer.SITWA_vD_5W_6se, InputTimeWindows.all5Min);
//run("SITWA_vD_6W_6se", InputBuffer.SITWA_vD_6W_6se, InputTimeWindows.all6Min);
//run("SITWA_vD_7W_6se", InputBuffer.SITWA_vD_7W_6se, InputTimeWindows.all7Min);
//run("SITWA_vD_8W_6se", InputBuffer.SITWA_vD_8W_6se, InputTimeWindows.all8Min);
//run("SITWA_vD_9W_6se", InputBuffer.SITWA_vD_9W_6se, InputTimeWindows.all9Min);
//run("SITWA_vD_10W_6se", InputBuffer.SITWA_vD_10W_6se, InputTimeWindows.all10Min);
//
//run("SITWA_vD_1W_7se", InputBuffer.SITWA_vD_1W_7se, InputTimeWindows.all1Min);
//run("SITWA_vD_2W_7se", InputBuffer.SITWA_vD_2W_7se, InputTimeWindows.all2Min);
//run("SITWA_vD_3W_7se", InputBuffer.SITWA_vD_3W_7se, InputTimeWindows.all3Min);
//run("SITWA_vD_4W_7se", InputBuffer.SITWA_vD_4W_7se, InputTimeWindows.all4Min);
//run("SITWA_vD_5W_7se", InputBuffer.SITWA_vD_5W_7se, InputTimeWindows.all5Min);
//run("SITWA_vD_6W_7se", InputBuffer.SITWA_vD_6W_7se, InputTimeWindows.all6Min);
//run("SITWA_vD_7W_7se", InputBuffer.SITWA_vD_7W_7se, InputTimeWindows.all7Min);
//run("SITWA_vD_8W_7se", InputBuffer.SITWA_vD_8W_7se, InputTimeWindows.all8Min);
//run("SITWA_vD_9W_7se", InputBuffer.SITWA_vD_9W_7se, InputTimeWindows.all9Min);
//run("SITWA_vD_10W_7se", InputBuffer.SITWA_vD_10W_7se, InputTimeWindows.all10Min);
//
//
//run("SITWA_vAx_1W_1se", InputBuffer.SITWA_vAx_1W_1se, InputTimeWindows.all1Min);
//run("SITWA_vAx_2W_1se", InputBuffer.SITWA_vAx_2W_1se, InputTimeWindows.all2Min);
//run("SITWA_vAx_3W_1se", InputBuffer.SITWA_vAx_3W_1se, InputTimeWindows.all3Min);
//run("SITWA_vAx_4W_1se", InputBuffer.SITWA_vAx_4W_1se, InputTimeWindows.all4Min);
//run("SITWA_vAx_5W_1se", InputBuffer.SITWA_vAx_5W_1se, InputTimeWindows.all5Min);
//run("SITWA_vAx_6W_1se", InputBuffer.SITWA_vAx_6W_1se, InputTimeWindows.all6Min);
//run("SITWA_vAx_7W_1se", InputBuffer.SITWA_vAx_7W_1se, InputTimeWindows.all7Min);
//run("SITWA_vAx_8W_1se", InputBuffer.SITWA_vAx_8W_1se, InputTimeWindows.all8Min);
//run("SITWA_vAx_9W_1se", InputBuffer.SITWA_vAx_9W_1se, InputTimeWindows.all9Min);
//run("SITWA_vAx_10W_1se", InputBuffer.SITWA_vAx_10W_1se, InputTimeWindows.all10Min);
//
//run("SITWA_vAx_1W_2se", InputBuffer.SITWA_vAx_1W_2se, InputTimeWindows.all1Min);
//run("SITWA_vAx_2W_2se", InputBuffer.SITWA_vAx_2W_2se, InputTimeWindows.all2Min);
//run("SITWA_vAx_3W_2se", InputBuffer.SITWA_vAx_3W_2se, InputTimeWindows.all3Min);
//run("SITWA_vAx_4W_2se", InputBuffer.SITWA_vAx_4W_2se, InputTimeWindows.all4Min);
//run("SITWA_vAx_5W_2se", InputBuffer.SITWA_vAx_5W_2se, InputTimeWindows.all5Min);
//run("SITWA_vAx_6W_2se", InputBuffer.SITWA_vAx_6W_2se, InputTimeWindows.all6Min);
//run("SITWA_vAx_7W_2se", InputBuffer.SITWA_vAx_7W_2se, InputTimeWindows.all7Min);
//run("SITWA_vAx_8W_2se", InputBuffer.SITWA_vAx_8W_2se, InputTimeWindows.all8Min);
//run("SITWA_vAx_9W_2se", InputBuffer.SITWA_vAx_9W_2se, InputTimeWindows.all9Min);
//run("SITWA_vAx_10W_2se", InputBuffer.SITWA_vAx_10W_2se, InputTimeWindows.all10Min);
//
//run("SITWA_vAx_1W_3se", InputBuffer.SITWA_vAx_1W_3se, InputTimeWindows.all1Min);
//run("SITWA_vAx_2W_3se", InputBuffer.SITWA_vAx_2W_3se, InputTimeWindows.all2Min);
//run("SITWA_vAx_3W_3se", InputBuffer.SITWA_vAx_3W_3se, InputTimeWindows.all3Min);
//run("SITWA_vAx_4W_3se", InputBuffer.SITWA_vAx_4W_3se, InputTimeWindows.all4Min);
//run("SITWA_vAx_5W_3se", InputBuffer.SITWA_vAx_5W_3se, InputTimeWindows.all5Min);
//run("SITWA_vAx_6W_3se", InputBuffer.SITWA_vAx_6W_3se, InputTimeWindows.all6Min);
//run("SITWA_vAx_7W_3se", InputBuffer.SITWA_vAx_7W_3se, InputTimeWindows.all7Min);
//run("SITWA_vAx_8W_3se", InputBuffer.SITWA_vAx_8W_3se, InputTimeWindows.all8Min);
//run("SITWA_vAx_9W_3se", InputBuffer.SITWA_vAx_9W_3se, InputTimeWindows.all9Min);
//run("SITWA_vAx_10W_3se", InputBuffer.SITWA_vAx_10W_3se, InputTimeWindows.all10Min);
//
//run("SITWA_vAx_1W_4se", InputBuffer.SITWA_vAx_1W_4se, InputTimeWindows.all1Min);
//run("SITWA_vAx_2W_4se", InputBuffer.SITWA_vAx_2W_4se, InputTimeWindows.all2Min);
//run("SITWA_vAx_3W_4se", InputBuffer.SITWA_vAx_3W_4se, InputTimeWindows.all3Min);
//run("SITWA_vAx_4W_4se", InputBuffer.SITWA_vAx_4W_4se, InputTimeWindows.all4Min);
//run("SITWA_vAx_5W_4se", InputBuffer.SITWA_vAx_5W_4se, InputTimeWindows.all5Min);
//run("SITWA_vAx_6W_4se", InputBuffer.SITWA_vAx_6W_4se, InputTimeWindows.all6Min);
//run("SITWA_vAx_7W_4se", InputBuffer.SITWA_vAx_7W_4se, InputTimeWindows.all7Min);
//run("SITWA_vAx_8W_4se", InputBuffer.SITWA_vAx_8W_4se, InputTimeWindows.all8Min);
//run("SITWA_vAx_9W_4se", InputBuffer.SITWA_vAx_9W_4se, InputTimeWindows.all9Min);
//run("SITWA_vAx_10W_4se", InputBuffer.SITWA_vAx_10W_4se, InputTimeWindows.all10Min);
//
//run("SITWA_vAx_1W_5se", InputBuffer.SITWA_vAx_1W_5se, InputTimeWindows.all1Min);
//run("SITWA_vAx_2W_5se", InputBuffer.SITWA_vAx_2W_5se, InputTimeWindows.all2Min);
//run("SITWA_vAx_3W_5se", InputBuffer.SITWA_vAx_3W_5se, InputTimeWindows.all3Min);
//run("SITWA_vAx_4W_5se", InputBuffer.SITWA_vAx_4W_5se, InputTimeWindows.all4Min);
//run("SITWA_vAx_5W_5se", InputBuffer.SITWA_vAx_5W_5se, InputTimeWindows.all5Min);
//run("SITWA_vAx_6W_5se", InputBuffer.SITWA_vAx_6W_5se, InputTimeWindows.all6Min);
//run("SITWA_vAx_7W_5se", InputBuffer.SITWA_vAx_7W_5se, InputTimeWindows.all7Min);
//run("SITWA_vAx_8W_5se", InputBuffer.SITWA_vAx_8W_5se, InputTimeWindows.all8Min);
//run("SITWA_vAx_9W_5se", InputBuffer.SITWA_vAx_9W_5se, InputTimeWindows.all9Min);
//run("SITWA_vAx_10W_5se", InputBuffer.SITWA_vAx_10W_5se, InputTimeWindows.all10Min);
//
//run("SITWA_vAx_1W_6se", InputBuffer.SITWA_vAx_1W_6se, InputTimeWindows.all1Min);
//run("SITWA_vAx_2W_6se", InputBuffer.SITWA_vAx_2W_6se, InputTimeWindows.all2Min);
//run("SITWA_vAx_3W_6se", InputBuffer.SITWA_vAx_3W_6se, InputTimeWindows.all3Min);
//run("SITWA_vAx_4W_6se", InputBuffer.SITWA_vAx_4W_6se, InputTimeWindows.all4Min);
//run("SITWA_vAx_5W_6se", InputBuffer.SITWA_vAx_5W_6se, InputTimeWindows.all5Min);
//run("SITWA_vAx_6W_6se", InputBuffer.SITWA_vAx_6W_6se, InputTimeWindows.all6Min);
//run("SITWA_vAx_7W_6se", InputBuffer.SITWA_vAx_7W_6se, InputTimeWindows.all7Min);
//run("SITWA_vAx_8W_6se", InputBuffer.SITWA_vAx_8W_6se, InputTimeWindows.all8Min);
//run("SITWA_vAx_9W_6se", InputBuffer.SITWA_vAx_9W_6se, InputTimeWindows.all9Min);
//run("SITWA_vAx_10W_6se", InputBuffer.SITWA_vAx_10W_6se, InputTimeWindows.all10Min);
//
//run("SITWA_vAx_1W_7se", InputBuffer.SITWA_vAx_1W_7se, InputTimeWindows.all1Min);
//run("SITWA_vAx_2W_7se", InputBuffer.SITWA_vAx_2W_7se, InputTimeWindows.all2Min);
//run("SITWA_vAx_3W_7se", InputBuffer.SITWA_vAx_3W_7se, InputTimeWindows.all3Min);
//run("SITWA_vAx_4W_7se", InputBuffer.SITWA_vAx_4W_7se, InputTimeWindows.all4Min);
//run("SITWA_vAx_5W_7se", InputBuffer.SITWA_vAx_5W_7se, InputTimeWindows.all5Min);
//run("SITWA_vAx_6W_7se", InputBuffer.SITWA_vAx_6W_7se, InputTimeWindows.all6Min);
//run("SITWA_vAx_7W_7se", InputBuffer.SITWA_vAx_7W_7se, InputTimeWindows.all7Min);
//run("SITWA_vAx_8W_7se", InputBuffer.SITWA_vAx_8W_7se, InputTimeWindows.all8Min);
//run("SITWA_vAx_9W_7se", InputBuffer.SITWA_vAx_9W_7se, InputTimeWindows.all9Min);
//run("SITWA_vAx_10W_7se", InputBuffer.SITWA_vAx_10W_7se, InputTimeWindows.all10Min);
//
//
//run("SITWA_vBx_1W_1se", InputBuffer.SITWA_vBx_1W_1se, InputTimeWindows.all1Min);
//run("SITWA_vBx_2W_1se", InputBuffer.SITWA_vBx_2W_1se, InputTimeWindows.all2Min);
//run("SITWA_vBx_3W_1se", InputBuffer.SITWA_vBx_3W_1se, InputTimeWindows.all3Min);
//run("SITWA_vBx_4W_1se", InputBuffer.SITWA_vBx_4W_1se, InputTimeWindows.all4Min);
//run("SITWA_vBx_5W_1se", InputBuffer.SITWA_vBx_5W_1se, InputTimeWindows.all5Min);
//run("SITWA_vBx_6W_1se", InputBuffer.SITWA_vBx_6W_1se, InputTimeWindows.all6Min);
//run("SITWA_vBx_7W_1se", InputBuffer.SITWA_vBx_7W_1se, InputTimeWindows.all7Min);
//run("SITWA_vBx_8W_1se", InputBuffer.SITWA_vBx_8W_1se, InputTimeWindows.all8Min);
//run("SITWA_vBx_9W_1se", InputBuffer.SITWA_vBx_9W_1se, InputTimeWindows.all9Min);
//run("SITWA_vBx_10W_1se", InputBuffer.SITWA_vBx_10W_1se, InputTimeWindows.all10Min);
//
//run("SITWA_vBx_1W_2se", InputBuffer.SITWA_vBx_1W_2se, InputTimeWindows.all1Min);
//run("SITWA_vBx_2W_2se", InputBuffer.SITWA_vBx_2W_2se, InputTimeWindows.all2Min);
//run("SITWA_vBx_3W_2se", InputBuffer.SITWA_vBx_3W_2se, InputTimeWindows.all3Min);
//run("SITWA_vBx_4W_2se", InputBuffer.SITWA_vBx_4W_2se, InputTimeWindows.all4Min);
//run("SITWA_vBx_5W_2se", InputBuffer.SITWA_vBx_5W_2se, InputTimeWindows.all5Min);
//run("SITWA_vBx_6W_2se", InputBuffer.SITWA_vBx_6W_2se, InputTimeWindows.all6Min);
//run("SITWA_vBx_7W_2se", InputBuffer.SITWA_vBx_7W_2se, InputTimeWindows.all7Min);
//run("SITWA_vBx_8W_2se", InputBuffer.SITWA_vBx_8W_2se, InputTimeWindows.all8Min);
//run("SITWA_vBx_9W_2se", InputBuffer.SITWA_vBx_9W_2se, InputTimeWindows.all9Min);
//run("SITWA_vBx_10W_2se", InputBuffer.SITWA_vBx_10W_2se, InputTimeWindows.all10Min);
//
//run("SITWA_vBx_1W_3se", InputBuffer.SITWA_vBx_1W_3se, InputTimeWindows.all1Min);
//run("SITWA_vBx_2W_3se", InputBuffer.SITWA_vBx_2W_3se, InputTimeWindows.all2Min);
//run("SITWA_vBx_3W_3se", InputBuffer.SITWA_vBx_3W_3se, InputTimeWindows.all3Min);
//run("SITWA_vBx_4W_3se", InputBuffer.SITWA_vBx_4W_3se, InputTimeWindows.all4Min);
//run("SITWA_vBx_5W_3se", InputBuffer.SITWA_vBx_5W_3se, InputTimeWindows.all5Min);
//run("SITWA_vBx_6W_3se", InputBuffer.SITWA_vBx_6W_3se, InputTimeWindows.all6Min);
//run("SITWA_vBx_7W_3se", InputBuffer.SITWA_vBx_7W_3se, InputTimeWindows.all7Min);
//run("SITWA_vBx_8W_3se", InputBuffer.SITWA_vBx_8W_3se, InputTimeWindows.all8Min);
//run("SITWA_vBx_9W_3se", InputBuffer.SITWA_vBx_9W_3se, InputTimeWindows.all9Min);
//run("SITWA_vBx_10W_3se", InputBuffer.SITWA_vBx_10W_3se, InputTimeWindows.all10Min);
//
//run("SITWA_vBx_1W_4se", InputBuffer.SITWA_vBx_1W_4se, InputTimeWindows.all1Min);
//run("SITWA_vBx_2W_4se", InputBuffer.SITWA_vBx_2W_4se, InputTimeWindows.all2Min);
//run("SITWA_vBx_3W_4se", InputBuffer.SITWA_vBx_3W_4se, InputTimeWindows.all3Min);
//run("SITWA_vBx_4W_4se", InputBuffer.SITWA_vBx_4W_4se, InputTimeWindows.all4Min);
//run("SITWA_vBx_5W_4se", InputBuffer.SITWA_vBx_5W_4se, InputTimeWindows.all5Min);
//run("SITWA_vBx_6W_4se", InputBuffer.SITWA_vBx_6W_4se, InputTimeWindows.all6Min);
//run("SITWA_vBx_7W_4se", InputBuffer.SITWA_vBx_7W_4se, InputTimeWindows.all7Min);
//run("SITWA_vBx_8W_4se", InputBuffer.SITWA_vBx_8W_4se, InputTimeWindows.all8Min);
//run("SITWA_vBx_9W_4se", InputBuffer.SITWA_vBx_9W_4se, InputTimeWindows.all9Min);
//run("SITWA_vBx_10W_4se", InputBuffer.SITWA_vBx_10W_4se, InputTimeWindows.all10Min);
//
//run("SITWA_vBx_1W_5se", InputBuffer.SITWA_vBx_1W_5se, InputTimeWindows.all1Min);
//run("SITWA_vBx_2W_5se", InputBuffer.SITWA_vBx_2W_5se, InputTimeWindows.all2Min);
//run("SITWA_vBx_3W_5se", InputBuffer.SITWA_vBx_3W_5se, InputTimeWindows.all3Min);
//run("SITWA_vBx_4W_5se", InputBuffer.SITWA_vBx_4W_5se, InputTimeWindows.all4Min);
//run("SITWA_vBx_5W_5se", InputBuffer.SITWA_vBx_5W_5se, InputTimeWindows.all5Min);
//run("SITWA_vBx_6W_5se", InputBuffer.SITWA_vBx_6W_5se, InputTimeWindows.all6Min);
//run("SITWA_vBx_7W_5se", InputBuffer.SITWA_vBx_7W_5se, InputTimeWindows.all7Min);
//run("SITWA_vBx_8W_5se", InputBuffer.SITWA_vBx_8W_5se, InputTimeWindows.all8Min);
//run("SITWA_vBx_9W_5se", InputBuffer.SITWA_vBx_9W_5se, InputTimeWindows.all9Min);
//run("SITWA_vBx_10W_5se", InputBuffer.SITWA_vBx_10W_5se, InputTimeWindows.all10Min);
//
//run("SITWA_vBx_1W_6se", InputBuffer.SITWA_vBx_1W_6se, InputTimeWindows.all1Min);
//run("SITWA_vBx_2W_6se", InputBuffer.SITWA_vBx_2W_6se, InputTimeWindows.all2Min);
//run("SITWA_vBx_3W_6se", InputBuffer.SITWA_vBx_3W_6se, InputTimeWindows.all3Min);
//run("SITWA_vBx_4W_6se", InputBuffer.SITWA_vBx_4W_6se, InputTimeWindows.all4Min);
//run("SITWA_vBx_5W_6se", InputBuffer.SITWA_vBx_5W_6se, InputTimeWindows.all5Min);
//run("SITWA_vBx_6W_6se", InputBuffer.SITWA_vBx_6W_6se, InputTimeWindows.all6Min);
//run("SITWA_vBx_7W_6se", InputBuffer.SITWA_vBx_7W_6se, InputTimeWindows.all7Min);
//run("SITWA_vBx_8W_6se", InputBuffer.SITWA_vBx_8W_6se, InputTimeWindows.all8Min);
//run("SITWA_vBx_9W_6se", InputBuffer.SITWA_vBx_9W_6se, InputTimeWindows.all9Min);
//run("SITWA_vBx_10W_6se", InputBuffer.SITWA_vBx_10W_6se, InputTimeWindows.all10Min);
//
//run("SITWA_vBx_1W_7se", InputBuffer.SITWA_vBx_1W_7se, InputTimeWindows.all1Min);
//run("SITWA_vBx_2W_7se", InputBuffer.SITWA_vBx_2W_7se, InputTimeWindows.all2Min);
//run("SITWA_vBx_3W_7se", InputBuffer.SITWA_vBx_3W_7se, InputTimeWindows.all3Min);
//run("SITWA_vBx_4W_7se", InputBuffer.SITWA_vBx_4W_7se, InputTimeWindows.all4Min);
//run("SITWA_vBx_5W_7se", InputBuffer.SITWA_vBx_5W_7se, InputTimeWindows.all5Min);
//run("SITWA_vBx_6W_7se", InputBuffer.SITWA_vBx_6W_7se, InputTimeWindows.all6Min);
//run("SITWA_vBx_7W_7se", InputBuffer.SITWA_vBx_7W_7se, InputTimeWindows.all7Min);
//run("SITWA_vBx_8W_7se", InputBuffer.SITWA_vBx_8W_7se, InputTimeWindows.all8Min);
//run("SITWA_vBx_9W_7se", InputBuffer.SITWA_vBx_9W_7se, InputTimeWindows.all9Min);
//run("SITWA_vBx_10W_7se", InputBuffer.SITWA_vBx_10W_7se, InputTimeWindows.all10Min);

//run("SITWA_vD2_1W_2se", InputBuffer.SITWA_vD2_1W_2se, InputTimeWindows.all1Min);
//run("SITWA_vD2_2W_2se", InputBuffer.SITWA_vD2_2W_2se, InputTimeWindows.all2Min);
//run("SITWA_vD2_3W_2se", InputBuffer.SITWA_vD2_3W_2se, InputTimeWindows.all3Min);
//run("SITWA_vD2_4W_2se", InputBuffer.SITWA_vD2_4W_2se, InputTimeWindows.all4Min);
//run("SITWA_vD2_5W_2se", InputBuffer.SITWA_vD2_5W_2se, InputTimeWindows.all5Min);
//run("SITWA_vD2_6W_2se", InputBuffer.SITWA_vD2_6W_2se, InputTimeWindows.all6Min);
//run("SITWA_vD2_7W_2se", InputBuffer.SITWA_vD2_7W_2se, InputTimeWindows.all7Min);
//run("SITWA_vD2_8W_2se", InputBuffer.SITWA_vD2_8W_2se, InputTimeWindows.all8Min);
//run("SITWA_vD2_9W_2se", InputBuffer.SITWA_vD2_9W_2se, InputTimeWindows.all9Min);
//run("SITWA_vD2_10W_2se", InputBuffer.SITWA_vD2_10W_2se, InputTimeWindows.all10Min);
//
//run("SITWA_vD2_1W_3se", InputBuffer.SITWA_vD2_1W_3se, InputTimeWindows.all1Min);
//run("SITWA_vD2_2W_3se", InputBuffer.SITWA_vD2_2W_3se, InputTimeWindows.all2Min);
//run("SITWA_vD2_3W_3se", InputBuffer.SITWA_vD2_3W_3se, InputTimeWindows.all3Min);
//run("SITWA_vD2_4W_3se", InputBuffer.SITWA_vD2_4W_3se, InputTimeWindows.all4Min);
//run("SITWA_vD2_5W_3se", InputBuffer.SITWA_vD2_5W_3se, InputTimeWindows.all5Min);
//run("SITWA_vD2_6W_3se", InputBuffer.SITWA_vD2_6W_3se, InputTimeWindows.all6Min);
//run("SITWA_vD2_7W_3se", InputBuffer.SITWA_vD2_7W_3se, InputTimeWindows.all7Min);
//run("SITWA_vD2_8W_3se", InputBuffer.SITWA_vD2_8W_3se, InputTimeWindows.all8Min);
//run("SITWA_vD2_9W_3se", InputBuffer.SITWA_vD2_9W_3se, InputTimeWindows.all9Min);
//run("SITWA_vD2_10W_3se", InputBuffer.SITWA_vD2_10W_3se, InputTimeWindows.all10Min);
//
//run("SITWA_vD2_1W_4se", InputBuffer.SITWA_vD2_1W_4se, InputTimeWindows.all1Min);
//run("SITWA_vD2_2W_4se", InputBuffer.SITWA_vD2_2W_4se, InputTimeWindows.all2Min);
//run("SITWA_vD2_3W_4se", InputBuffer.SITWA_vD2_3W_4se, InputTimeWindows.all3Min);
//run("SITWA_vD2_4W_4se", InputBuffer.SITWA_vD2_4W_4se, InputTimeWindows.all4Min);
//run("SITWA_vD2_5W_4se", InputBuffer.SITWA_vD2_5W_4se, InputTimeWindows.all5Min);
//run("SITWA_vD2_6W_4se", InputBuffer.SITWA_vD2_6W_4se, InputTimeWindows.all6Min);
//run("SITWA_vD2_7W_4se", InputBuffer.SITWA_vD2_7W_4se, InputTimeWindows.all7Min);
//run("SITWA_vD2_8W_4se", InputBuffer.SITWA_vD2_8W_4se, InputTimeWindows.all8Min);
//run("SITWA_vD2_9W_4se", InputBuffer.SITWA_vD2_9W_4se, InputTimeWindows.all9Min);
//run("SITWA_vD2_10W_4se", InputBuffer.SITWA_vD2_10W_4se, InputTimeWindows.all10Min);

//run("SITWA_vBx_1W_6se1W", InputBuffer.SITWA_vBx_1W_6se, InputTimeWindows.all1Min);
//run("SITWA_vBx_2W_6se1W", InputBuffer.SITWA_vBx_1W_6se, InputTimeWindows.all2Min);
//run("SITWA_vBx_3W_6se1W", InputBuffer.SITWA_vBx_1W_6se, InputTimeWindows.all3Min);
//run("SITWA_vBx_4W_6se1W", InputBuffer.SITWA_vBx_1W_6se, InputTimeWindows.all4Min);
//run("SITWA_vBx_5W_6se1W", InputBuffer.SITWA_vBx_1W_6se, InputTimeWindows.all5Min);
//run("SITWA_vBx_6W_6se1W", InputBuffer.SITWA_vBx_1W_6se, InputTimeWindows.all6Min);
//run("SITWA_vBx_7W_6se1W", InputBuffer.SITWA_vBx_1W_6se, InputTimeWindows.all7Min);
//run("SITWA_vBx_8W_6se1W", InputBuffer.SITWA_vBx_1W_6se, InputTimeWindows.all8Min);
//run("SITWA_vBx_9W_6se1W", InputBuffer.SITWA_vBx_1W_6se, InputTimeWindows.all9Min);
//run("SITWA_vBx_10W_6se1W", InputBuffer.SITWA_vBx_1W_6se, InputTimeWindows.all10Min);

//run("SITWA_vBx_1W_7se2W", InputBuffer.SITWA_vBx_2W_7se, InputTimeWindows.all1Min);
//run("SITWA_vBx_2W_7se2W", InputBuffer.SITWA_vBx_2W_7se, InputTimeWindows.all2Min);
//run("SITWA_vBx_3W_7se2W", InputBuffer.SITWA_vBx_2W_7se, InputTimeWindows.all3Min);
//run("SITWA_vBx_4W_7se2W", InputBuffer.SITWA_vBx_2W_7se, InputTimeWindows.all4Min);
//run("SITWA_vBx_5W_7se2W", InputBuffer.SITWA_vBx_2W_7se, InputTimeWindows.all5Min);
//run("SITWA_vBx_6W_7se2W", InputBuffer.SITWA_vBx_2W_7se, InputTimeWindows.all6Min);
//run("SITWA_vBx_7W_7se2W", InputBuffer.SITWA_vBx_2W_7se, InputTimeWindows.all7Min);
//run("SITWA_vBx_8W_7se2W", InputBuffer.SITWA_vBx_2W_7se, InputTimeWindows.all8Min);
//run("SITWA_vBx_9W_7se2W", InputBuffer.SITWA_vBx_2W_7se, InputTimeWindows.all9Min);
//run("SITWA_vBx_10W_7se2W", InputBuffer.SITWA_vBx_2W_7se, InputTimeWindows.all10Min);
//
//run("SITWA_vBx_1W_6se0W", InputBuffer.SITWA_vBx_0W_6se, InputTimeWindows.all1Min);
//run("SITWA_vBx_2W_6se0W", InputBuffer.SITWA_vBx_0W_6se, InputTimeWindows.all2Min);
//run("SITWA_vBx_3W_6se0W", InputBuffer.SITWA_vBx_0W_6se, InputTimeWindows.all3Min);
//run("SITWA_vBx_4W_6se0W", InputBuffer.SITWA_vBx_0W_6se, InputTimeWindows.all4Min);
//run("SITWA_vBx_5W_6se0W", InputBuffer.SITWA_vBx_0W_6se, InputTimeWindows.all5Min);
//run("SITWA_vBx_6W_6se0W", InputBuffer.SITWA_vBx_0W_6se, InputTimeWindows.all6Min);
//run("SITWA_vBx_7W_6se0W", InputBuffer.SITWA_vBx_0W_6se, InputTimeWindows.all7Min);
//run("SITWA_vBx_8W_6se0W", InputBuffer.SITWA_vBx_0W_6se, InputTimeWindows.all8Min);
//run("SITWA_vBx_9W_6se0W", InputBuffer.SITWA_vBx_0W_6se, InputTimeWindows.all9Min);
//run("SITWA_vBx_10W_6se0W", InputBuffer.SITWA_vBx_0W_6se, InputTimeWindows.all10Min);
//
//run("SITWA_vBx_1W_7se0W", InputBuffer.SITWA_vBx_0W_7se, InputTimeWindows.all1Min);
//run("SITWA_vBx_2W_7se0W", InputBuffer.SITWA_vBx_0W_7se, InputTimeWindows.all2Min);
//run("SITWA_vBx_3W_7se0W", InputBuffer.SITWA_vBx_0W_7se, InputTimeWindows.all3Min);
//run("SITWA_vBx_4W_7se0W", InputBuffer.SITWA_vBx_0W_7se, InputTimeWindows.all4Min);
//run("SITWA_vBx_5W_7se0W", InputBuffer.SITWA_vBx_0W_7se, InputTimeWindows.all5Min);
//run("SITWA_vBx_6W_7se0W", InputBuffer.SITWA_vBx_0W_7se, InputTimeWindows.all6Min);
//run("SITWA_vBx_7W_7se0W", InputBuffer.SITWA_vBx_0W_7se, InputTimeWindows.all7Min);
//run("SITWA_vBx_8W_7se0W", InputBuffer.SITWA_vBx_0W_7se, InputTimeWindows.all8Min);
//run("SITWA_vBx_9W_7se0W", InputBuffer.SITWA_vBx_0W_7se, InputTimeWindows.all9Min);
//run("SITWA_vBx_10W_7se0W", InputBuffer.SITWA_vBx_0W_7se, InputTimeWindows.all10Min);

//
//
//run("SITWA_vA_W00_se00_1W", InputBuffer.SITWA_vA_W00_se00, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se00_1W", InputBuffer.SITWA_vA_W10_se00, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se00_2W", InputBuffer.SITWA_vA_W20_se00, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se00_3W", InputBuffer.SITWA_vA_W30_se00, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se00_4W", InputBuffer.SITWA_vA_W40_se00, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se00_5W", InputBuffer.SITWA_vA_W50_se00, InputTimeWindows.all5Min);
//run("SITWA_vA_W60_se00_6W", InputBuffer.SITWA_vA_W60_se00, InputTimeWindows.all6Min);
//run("SITWA_vA_W70_se00_7W", InputBuffer.SITWA_vA_W70_se00, InputTimeWindows.all7Min);
//run("SITWA_vA_W80_se00_8W", InputBuffer.SITWA_vA_W80_se00, InputTimeWindows.all8Min);
//run("SITWA_vA_W90_se00_9W", InputBuffer.SITWA_vA_W90_se00, InputTimeWindows.all9Min);
//run("SITWA_vA_W100_se00_10W", InputBuffer.SITWA_vA_W100_se00, InputTimeWindows.all10Min);
//
//run("SITWA_x_vA_W00_se00_1W", InputBuffer2.SITWA_x_vA_W00_se00, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se00_1W", InputBuffer2.SITWA_x_vA_W10_se00, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se00_2W", InputBuffer2.SITWA_x_vA_W20_se00, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se00_3W", InputBuffer2.SITWA_x_vA_W30_se00, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se00_4W", InputBuffer2.SITWA_x_vA_W40_se00, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se00_5W", InputBuffer2.SITWA_x_vA_W50_se00, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se00_6W", InputBuffer2.SITWA_x_vA_W60_se00, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se00_7W", InputBuffer2.SITWA_x_vA_W70_se00, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se00_8W", InputBuffer2.SITWA_x_vA_W80_se00, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se00_9W", InputBuffer2.SITWA_x_vA_W90_se00, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se00_10W", InputBuffer2.SITWA_x_vA_W100_se00, InputTimeWindows.all10Min);
//
//
//run("SITWA_vA_W00_se01_1W", InputBuffer.SITWA_vA_W00_se01, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se01_1W", InputBuffer.SITWA_vA_W10_se01, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se01_2W", InputBuffer.SITWA_vA_W20_se01, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se01_3W", InputBuffer.SITWA_vA_W30_se01, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se01_4W", InputBuffer.SITWA_vA_W40_se01, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se01_5W", InputBuffer.SITWA_vA_W50_se01, InputTimeWindows.all5Min);
//run("SITWA_vA_W60_se01_6W", InputBuffer.SITWA_vA_W60_se01, InputTimeWindows.all6Min);
//run("SITWA_vA_W70_se01_7W", InputBuffer.SITWA_vA_W70_se01, InputTimeWindows.all7Min);
//run("SITWA_vA_W80_se01_8W", InputBuffer.SITWA_vA_W80_se01, InputTimeWindows.all8Min);
//run("SITWA_vA_W90_se01_9W", InputBuffer.SITWA_vA_W90_se01, InputTimeWindows.all9Min);
//run("SITWA_vA_W100_se01_10W", InputBuffer.SITWA_vA_W100_se01, InputTimeWindows.all10Min);
//
//run("SITWA_x_vA_W00_se01_1W", InputBuffer2.SITWA_x_vA_W00_se01, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se01_1W", InputBuffer2.SITWA_x_vA_W10_se01, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se01_2W", InputBuffer2.SITWA_x_vA_W20_se01, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se01_3W", InputBuffer2.SITWA_x_vA_W30_se01, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se01_4W", InputBuffer2.SITWA_x_vA_W40_se01, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se01_5W", InputBuffer2.SITWA_x_vA_W50_se01, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se01_6W", InputBuffer2.SITWA_x_vA_W60_se01, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se01_7W", InputBuffer2.SITWA_x_vA_W70_se01, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se01_8W", InputBuffer2.SITWA_x_vA_W80_se01, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se01_9W", InputBuffer2.SITWA_x_vA_W90_se01, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se01_10W", InputBuffer2.SITWA_x_vA_W100_se01, InputTimeWindows.all10Min);
//
//run("SITWA_vA_W00_se05_1W", InputBuffer.SITWA_vA_W00_se05, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se05_1W", InputBuffer.SITWA_vA_W10_se05, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se05_2W", InputBuffer.SITWA_vA_W20_se05, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se05_3W", InputBuffer.SITWA_vA_W30_se05, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se05_4W", InputBuffer.SITWA_vA_W40_se05, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se05_5W", InputBuffer.SITWA_vA_W50_se05, InputTimeWindows.all5Min);
//run("SITWA_vA_W60_se05_6W", InputBuffer.SITWA_vA_W60_se05, InputTimeWindows.all6Min);
//run("SITWA_vA_W70_se05_7W", InputBuffer.SITWA_vA_W70_se05, InputTimeWindows.all7Min);
//run("SITWA_vA_W80_se05_8W", InputBuffer.SITWA_vA_W80_se05, InputTimeWindows.all8Min);
//run("SITWA_vA_W90_se05_9W", InputBuffer.SITWA_vA_W90_se05, InputTimeWindows.all9Min);
//run("SITWA_vA_W100_se05_10W", InputBuffer.SITWA_vA_W100_se05, InputTimeWindows.all10Min);
//
//run("SITWA_x_vA_W00_se05_1W", InputBuffer2.SITWA_x_vA_W00_se05, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se05_1W", InputBuffer2.SITWA_x_vA_W10_se05, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se05_2W", InputBuffer2.SITWA_x_vA_W20_se05, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se05_3W", InputBuffer2.SITWA_x_vA_W30_se05, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se05_4W", InputBuffer2.SITWA_x_vA_W40_se05, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se05_5W", InputBuffer2.SITWA_x_vA_W50_se05, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se05_6W", InputBuffer2.SITWA_x_vA_W60_se05, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se05_7W", InputBuffer2.SITWA_x_vA_W70_se05, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se05_8W", InputBuffer2.SITWA_x_vA_W80_se05, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se05_9W", InputBuffer2.SITWA_x_vA_W90_se05, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se05_10W", InputBuffer2.SITWA_x_vA_W100_se05, InputTimeWindows.all10Min);
//
//
//run("SITWA_vA_W00_se10_1W", InputBuffer.SITWA_vA_W00_se10, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se10_1W", InputBuffer.SITWA_vA_W10_se10, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se10_2W", InputBuffer.SITWA_vA_W20_se10, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se10_3W", InputBuffer.SITWA_vA_W30_se10, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se10_4W", InputBuffer.SITWA_vA_W40_se10, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se10_5W", InputBuffer.SITWA_vA_W50_se10, InputTimeWindows.all5Min);
//run("SITWA_vA_W60_se10_6W", InputBuffer.SITWA_vA_W60_se10, InputTimeWindows.all6Min);
//run("SITWA_vA_W70_se10_7W", InputBuffer.SITWA_vA_W70_se10, InputTimeWindows.all7Min);
//run("SITWA_vA_W80_se10_8W", InputBuffer.SITWA_vA_W80_se10, InputTimeWindows.all8Min);
//run("SITWA_vA_W90_se10_9W", InputBuffer.SITWA_vA_W90_se10, InputTimeWindows.all9Min);
//run("SITWA_vA_W100_se10_10W", InputBuffer.SITWA_vA_W100_se10, InputTimeWindows.all10Min);
//
//run("SITWA_x_vA_W00_se10_1W", InputBuffer2.SITWA_x_vA_W00_se10, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se10_1W", InputBuffer2.SITWA_x_vA_W10_se10, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se10_2W", InputBuffer2.SITWA_x_vA_W20_se10, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se10_3W", InputBuffer2.SITWA_x_vA_W30_se10, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se10_4W", InputBuffer2.SITWA_x_vA_W40_se10, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se10_5W", InputBuffer2.SITWA_x_vA_W50_se10, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se10_6W", InputBuffer2.SITWA_x_vA_W60_se10, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se10_7W", InputBuffer2.SITWA_x_vA_W70_se10, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se10_8W", InputBuffer2.SITWA_x_vA_W80_se10, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se10_9W", InputBuffer2.SITWA_x_vA_W90_se10, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se10_10W", InputBuffer2.SITWA_x_vA_W100_se10, InputTimeWindows.all10Min);
//
//
//run("SITWA_vA_W00_se15_1W", InputBuffer.SITWA_vA_W00_se15, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se15_1W", InputBuffer.SITWA_vA_W10_se15, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se15_2W", InputBuffer.SITWA_vA_W20_se15, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se15_3W", InputBuffer.SITWA_vA_W30_se15, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se15_4W", InputBuffer.SITWA_vA_W40_se15, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se15_5W", InputBuffer.SITWA_vA_W50_se15, InputTimeWindows.all5Min);
//run("SITWA_vA_W60_se15_6W", InputBuffer.SITWA_vA_W60_se15, InputTimeWindows.all6Min);
//run("SITWA_vA_W70_se15_7W", InputBuffer.SITWA_vA_W70_se15, InputTimeWindows.all7Min);
//run("SITWA_vA_W80_se15_8W", InputBuffer.SITWA_vA_W80_se15, InputTimeWindows.all8Min);
//run("SITWA_vA_W90_se15_9W", InputBuffer.SITWA_vA_W90_se15, InputTimeWindows.all9Min);
//run("SITWA_vA_W100_se15_10W", InputBuffer.SITWA_vA_W100_se15, InputTimeWindows.all10Min);
//
//run("SITWA_x_vA_W00_se15_1W", InputBuffer2.SITWA_x_vA_W00_se15, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se15_1W", InputBuffer2.SITWA_x_vA_W10_se15, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se15_2W", InputBuffer2.SITWA_x_vA_W20_se15, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se15_3W", InputBuffer2.SITWA_x_vA_W30_se15, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se15_4W", InputBuffer2.SITWA_x_vA_W40_se15, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se15_5W", InputBuffer2.SITWA_x_vA_W50_se15, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se15_6W", InputBuffer2.SITWA_x_vA_W60_se15, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se15_7W", InputBuffer2.SITWA_x_vA_W70_se15, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se15_8W", InputBuffer2.SITWA_x_vA_W80_se15, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se15_9W", InputBuffer2.SITWA_x_vA_W90_se15, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se15_10W", InputBuffer2.SITWA_x_vA_W100_se15, InputTimeWindows.all10Min);
//
//
//run("SITWA_vA_W00_se20_1W", InputBuffer.SITWA_vA_W00_se20, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se20_1W", InputBuffer.SITWA_vA_W10_se20, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se20_2W", InputBuffer.SITWA_vA_W20_se20, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se20_3W", InputBuffer.SITWA_vA_W30_se20, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se20_4W", InputBuffer.SITWA_vA_W40_se20, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se20_5W", InputBuffer.SITWA_vA_W50_se20, InputTimeWindows.all5Min);
//run("SITWA_vA_W60_se20_6W", InputBuffer.SITWA_vA_W60_se20, InputTimeWindows.all6Min);
//run("SITWA_vA_W70_se20_7W", InputBuffer.SITWA_vA_W70_se20, InputTimeWindows.all7Min);
//run("SITWA_vA_W80_se20_8W", InputBuffer.SITWA_vA_W80_se20, InputTimeWindows.all8Min);
//run("SITWA_vA_W90_se20_9W", InputBuffer.SITWA_vA_W90_se20, InputTimeWindows.all9Min);
//run("SITWA_vA_W100_se20_10W", InputBuffer.SITWA_vA_W100_se20, InputTimeWindows.all10Min);
//
//run("SITWA_x_vA_W00_se20_1W", InputBuffer2.SITWA_x_vA_W00_se20, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se20_1W", InputBuffer2.SITWA_x_vA_W10_se20, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se20_2W", InputBuffer2.SITWA_x_vA_W20_se20, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se20_3W", InputBuffer2.SITWA_x_vA_W30_se20, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se20_4W", InputBuffer2.SITWA_x_vA_W40_se20, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se20_5W", InputBuffer2.SITWA_x_vA_W50_se20, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se20_6W", InputBuffer2.SITWA_x_vA_W60_se20, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se20_7W", InputBuffer2.SITWA_x_vA_W70_se20, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se20_8W", InputBuffer2.SITWA_x_vA_W80_se20, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se20_9W", InputBuffer2.SITWA_x_vA_W90_se20, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se20_10W", InputBuffer2.SITWA_x_vA_W100_se20, InputTimeWindows.all10Min);
//
//
//run("SITWA_vA_W00_se25_1W", InputBuffer.SITWA_vA_W00_se25, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se25_1W", InputBuffer.SITWA_vA_W10_se25, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se25_2W", InputBuffer.SITWA_vA_W20_se25, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se25_3W", InputBuffer.SITWA_vA_W30_se25, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se25_4W", InputBuffer.SITWA_vA_W40_se25, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se25_5W", InputBuffer.SITWA_vA_W50_se25, InputTimeWindows.all5Min);
//run("SITWA_vA_W60_se25_6W", InputBuffer.SITWA_vA_W60_se25, InputTimeWindows.all6Min);
//run("SITWA_vA_W70_se25_7W", InputBuffer.SITWA_vA_W70_se25, InputTimeWindows.all7Min);
//run("SITWA_vA_W80_se25_8W", InputBuffer.SITWA_vA_W80_se25, InputTimeWindows.all8Min);
//run("SITWA_vA_W90_se25_9W", InputBuffer.SITWA_vA_W90_se25, InputTimeWindows.all9Min);
//run("SITWA_vA_W100_se25_10W", InputBuffer.SITWA_vA_W100_se25, InputTimeWindows.all10Min);
//
//run("SITWA_x_vA_W00_se25_1W", InputBuffer2.SITWA_x_vA_W00_se25, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se25_1W", InputBuffer2.SITWA_x_vA_W10_se25, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se25_2W", InputBuffer2.SITWA_x_vA_W20_se25, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se25_3W", InputBuffer2.SITWA_x_vA_W30_se25, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se25_4W", InputBuffer2.SITWA_x_vA_W40_se25, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se25_5W", InputBuffer2.SITWA_x_vA_W50_se25, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se25_6W", InputBuffer2.SITWA_x_vA_W60_se25, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se25_7W", InputBuffer2.SITWA_x_vA_W70_se25, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se25_8W", InputBuffer2.SITWA_x_vA_W80_se25, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se25_9W", InputBuffer2.SITWA_x_vA_W90_se25, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se25_10W", InputBuffer2.SITWA_x_vA_W100_se25, InputTimeWindows.all10Min);
//
//
//run("SITWA_vA_W00_se30_1W", InputBuffer.SITWA_vA_W00_se30, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se30_1W", InputBuffer.SITWA_vA_W10_se30, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se30_2W", InputBuffer.SITWA_vA_W20_se30, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se30_3W", InputBuffer.SITWA_vA_W30_se30, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se30_4W", InputBuffer.SITWA_vA_W40_se30, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se30_5W", InputBuffer.SITWA_vA_W50_se30, InputTimeWindows.all5Min);
//run("SITWA_vA_W60_se30_6W", InputBuffer.SITWA_vA_W60_se30, InputTimeWindows.all6Min);
//run("SITWA_vA_W70_se30_7W", InputBuffer.SITWA_vA_W70_se30, InputTimeWindows.all7Min);
//run("SITWA_vA_W80_se30_8W", InputBuffer.SITWA_vA_W80_se30, InputTimeWindows.all8Min);
//run("SITWA_vA_W90_se30_9W", InputBuffer.SITWA_vA_W90_se30, InputTimeWindows.all9Min);
//run("SITWA_vA_W100_se30_10W", InputBuffer.SITWA_vA_W100_se30, InputTimeWindows.all10Min);
//
//run("SITWA_x_vA_W00_se30_1W", InputBuffer2.SITWA_x_vA_W00_se30, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se30_1W", InputBuffer2.SITWA_x_vA_W10_se30, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se30_2W", InputBuffer2.SITWA_x_vA_W20_se30, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se30_3W", InputBuffer2.SITWA_x_vA_W30_se30, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se30_4W", InputBuffer2.SITWA_x_vA_W40_se30, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se30_5W", InputBuffer2.SITWA_x_vA_W50_se30, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se30_6W", InputBuffer2.SITWA_x_vA_W60_se30, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se30_7W", InputBuffer2.SITWA_x_vA_W70_se30, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se30_8W", InputBuffer2.SITWA_x_vA_W80_se30, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se30_9W", InputBuffer2.SITWA_x_vA_W90_se30, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se30_10W", InputBuffer2.SITWA_x_vA_W100_se30, InputTimeWindows.all10Min);
//
//
//run("SITWA_vA_W00_se35_1W", InputBuffer.SITWA_vA_W00_se35, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se35_1W", InputBuffer.SITWA_vA_W10_se35, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se35_2W", InputBuffer.SITWA_vA_W20_se35, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se35_3W", InputBuffer.SITWA_vA_W30_se35, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se35_4W", InputBuffer.SITWA_vA_W40_se35, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se35_5W", InputBuffer.SITWA_vA_W50_se35, InputTimeWindows.all5Min);
//run("SITWA_vA_W60_se35_6W", InputBuffer.SITWA_vA_W60_se35, InputTimeWindows.all6Min);
//run("SITWA_vA_W70_se35_7W", InputBuffer.SITWA_vA_W70_se35, InputTimeWindows.all7Min);
//run("SITWA_vA_W80_se35_8W", InputBuffer.SITWA_vA_W80_se35, InputTimeWindows.all8Min);
//run("SITWA_vA_W90_se35_9W", InputBuffer.SITWA_vA_W90_se35, InputTimeWindows.all9Min);
//run("SITWA_vA_W100_se35_10W", InputBuffer.SITWA_vA_W100_se35, InputTimeWindows.all10Min);
//
//run("SITWA_x_vA_W00_se35_1W", InputBuffer2.SITWA_x_vA_W00_se35, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se35_1W", InputBuffer2.SITWA_x_vA_W10_se35, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se35_2W", InputBuffer2.SITWA_x_vA_W20_se35, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se35_3W", InputBuffer2.SITWA_x_vA_W30_se35, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se35_4W", InputBuffer2.SITWA_x_vA_W40_se35, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se35_5W", InputBuffer2.SITWA_x_vA_W50_se35, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se35_6W", InputBuffer2.SITWA_x_vA_W60_se35, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se35_7W", InputBuffer2.SITWA_x_vA_W70_se35, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se35_8W", InputBuffer2.SITWA_x_vA_W80_se35, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se35_9W", InputBuffer2.SITWA_x_vA_W90_se35, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se35_10W", InputBuffer2.SITWA_x_vA_W100_se35, InputTimeWindows.all10Min);
//
//
//run("SITWA_vA_W00_se40_1W", InputBuffer.SITWA_vA_W00_se40, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se40_1W", InputBuffer.SITWA_vA_W10_se40, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se40_2W", InputBuffer.SITWA_vA_W20_se40, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se40_3W", InputBuffer.SITWA_vA_W30_se40, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se40_4W", InputBuffer.SITWA_vA_W40_se40, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se40_5W", InputBuffer.SITWA_vA_W50_se40, InputTimeWindows.all5Min);
//run("SITWA_vA_W60_se40_6W", InputBuffer.SITWA_vA_W60_se40, InputTimeWindows.all6Min);
//run("SITWA_vA_W70_se40_7W", InputBuffer.SITWA_vA_W70_se40, InputTimeWindows.all7Min);
//run("SITWA_vA_W80_se40_8W", InputBuffer.SITWA_vA_W80_se40, InputTimeWindows.all8Min);
//run("SITWA_vA_W90_se40_9W", InputBuffer.SITWA_vA_W90_se40, InputTimeWindows.all9Min);
//run("SITWA_vA_W100_se40_10W", InputBuffer.SITWA_vA_W100_se40, InputTimeWindows.all10Min);
//
//run("SITWA_x_vA_W00_se40_1W", InputBuffer2.SITWA_x_vA_W00_se40, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se40_1W", InputBuffer2.SITWA_x_vA_W10_se40, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se40_2W", InputBuffer2.SITWA_x_vA_W20_se40, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se40_3W", InputBuffer2.SITWA_x_vA_W30_se40, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se40_4W", InputBuffer2.SITWA_x_vA_W40_se40, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se40_5W", InputBuffer2.SITWA_x_vA_W50_se40, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se40_6W", InputBuffer2.SITWA_x_vA_W60_se40, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se40_7W", InputBuffer2.SITWA_x_vA_W70_se40, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se40_8W", InputBuffer2.SITWA_x_vA_W80_se40, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se40_9W", InputBuffer2.SITWA_x_vA_W90_se40, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se40_10W", InputBuffer2.SITWA_x_vA_W100_se40, InputTimeWindows.all10Min);
//
//	

//run("SITWA_x_vA_W00_se1585246474_W1", InputBuffer.SITWA_x_vA_W00_se1585246474, InputTimeWindows.all1Min);
//run("SITWA_vA_W00_se1585246474_W1", InputBuffer.SITWA_vA_W00_se1585246474, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W00_se1585246474_W2", InputBuffer.SITWA_x_vA_W00_se1585246474, InputTimeWindows.all2Min);
//run("SITWA_vA_W00_se1585246474_W2", InputBuffer.SITWA_vA_W00_se1585246474, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W00_se1585246474_W3", InputBuffer.SITWA_x_vA_W00_se1585246474, InputTimeWindows.all3Min);
//run("SITWA_vA_W00_se1585246474_W3", InputBuffer.SITWA_vA_W00_se1585246474, InputTimeWindows.all3Min);
////run("SITWA_x_vA_W00_se1585246474_W4", InputBuffer.SITWA_x_vA_W00_se1585246474, InputTimeWindows.all4Min);
////run("SITWA_vA_W00_se1585246474_W4", InputBuffer.SITWA_vA_W00_se1585246474, InputTimeWindows.all4Min);
////run("SITWA_x_vA_W00_se1585246474_W5", InputBuffer.SITWA_x_vA_W00_se1585246474, InputTimeWindows.all5Min);
////run("SITWA_vA_W00_se1585246474_W5", InputBuffer.SITWA_vA_W00_se1585246474, InputTimeWindows.all5Min);
//
////run("SITWA_x_vA_W10_se1585246474_W1", InputBuffer.SITWA_x_vA_W10_se1585246474, InputTimeWindows.all1Min);
////run("SITWA_vA_W10_se1585246474_W1", InputBuffer.SITWA_vA_W10_se1585246474, InputTimeWindows.all1Min);
////run("SITWA_x_vA_W20_se1585246474_W2", InputBuffer.SITWA_x_vA_W20_se1585246474, InputTimeWindows.all2Min);
////run("SITWA_vA_W20_se1585246474_W2", InputBuffer.SITWA_vA_W20_se1585246474, InputTimeWindows.all2Min);
////run("SITWA_x_vA_W30_se1585246474_W3", InputBuffer.SITWA_x_vA_W30_se1585246474, InputTimeWindows.all3Min);
////run("SITWA_vA_W30_se1585246474_W3", InputBuffer.SITWA_vA_W30_se1585246474, InputTimeWindows.all3Min);
////run("SITWA_x_vA_W40_se1585246474_W4", InputBuffer.SITWA_x_vA_W40_se1585246474, InputTimeWindows.all4Min);
////run("SITWA_vA_W40_se1585246474_W4", InputBuffer.SITWA_vA_W40_se1585246474, InputTimeWindows.all4Min);
////run("SITWA_x_vA_W50_se1585246474_W5", InputBuffer.SITWA_x_vA_W50_se1585246474, InputTimeWindows.all5Min);
////run("SITWA_vA_W50_se1585246474_W5", InputBuffer.SITWA_vA_W50_se1585246474, InputTimeWindows.all5Min);
//
//
//settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;
//
//run("SITWA_x_vB_W00_se1585246474_W1", InputBuffer.SITWA_x_vB_W00_se1585246474, InputTimeWindows.all1Min);
//run("SITWA_vB_W00_se1585246474_W1", InputBuffer.SITWA_vB_W00_se1585246474, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W00_se1585246474_W2", InputBuffer.SITWA_x_vB_W00_se1585246474, InputTimeWindows.all2Min);
//run("SITWA_vB_W00_se1585246474_W2", InputBuffer.SITWA_vB_W00_se1585246474, InputTimeWindows.all2Min);
//run("SITWA_x_vB_W00_se1585246474_W3", InputBuffer.SITWA_x_vB_W00_se1585246474, InputTimeWindows.all3Min);
//run("SITWA_vB_W00_se1585246474_W3", InputBuffer.SITWA_vB_W00_se1585246474, InputTimeWindows.all3Min);
////run("SITWA_x_vB_W00_se1585246474_W4", InputBuffer.SITWA_x_vB_W00_se1585246474, InputTimeWindows.all4Min);
////run("SITWA_vB_W00_se1585246474_W4", InputBuffer.SITWA_vB_W00_se1585246474, InputTimeWindows.all4Min);
////run("SITWA_x_vB_W00_se1585246474_W5", InputBuffer.SITWA_x_vB_W00_se1585246474, InputTimeWindows.all5Min);
////run("SITWA_vB_W00_se1585246474_W5", InputBuffer.SITWA_vB_W00_se1585246474, InputTimeWindows.all5Min);
//
////run("SITWA_x_vB_W10_se1585246474_W1", InputBuffer.SITWA_x_vB_W10_se1585246474, InputTimeWindows.all1Min);
////run("SITWA_vB_W10_se1585246474_W1", InputBuffer.SITWA_vB_W10_se1585246474, InputTimeWindows.all1Min);
////run("SITWA_x_vB_W20_se1585246474_W2", InputBuffer.SITWA_x_vB_W20_se1585246474, InputTimeWindows.all2Min);
////run("SITWA_vB_W20_se1585246474_W2", InputBuffer.SITWA_vB_W20_se1585246474, InputTimeWindows.all2Min);
////run("SITWA_x_vB_W30_se1585246474_W3", InputBuffer.SITWA_x_vB_W30_se1585246474, InputTimeWindows.all3Min);
////run("SITWA_vB_W30_se1585246474_W3", InputBuffer.SITWA_vB_W30_se1585246474, InputTimeWindows.all3Min);
////run("SITWA_x_vB_W40_se1585246474_W4", InputBuffer.SITWA_x_vB_W40_se1585246474, InputTimeWindows.all4Min);
////run("SITWA_vB_W40_se1585246474_W4", InputBuffer.SITWA_vB_W40_se1585246474, InputTimeWindows.all4Min);
////run("SITWA_x_vB_W50_se1585246474_W5", InputBuffer.SITWA_x_vB_W50_se1585246474, InputTimeWindows.all5Min);
////run("SITWA_vB_W50_se1585246474_W5", InputBuffer.SITWA_vB_W50_se1585246474, InputTimeWindows.all5Min);
//
//
//run("SITWA_x_vC_W00_se1585246474_W1", InputBuffer.SITWA_x_vC_W00_se1585246474, InputTimeWindows.all1Min);
//run("SITWA_vC_W00_se1585246474_W1", InputBuffer.SITWA_vC_W00_se1585246474, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W00_se1585246474_W2", InputBuffer.SITWA_x_vC_W00_se1585246474, InputTimeWindows.all2Min);
//run("SITWA_vC_W00_se1585246474_W2", InputBuffer.SITWA_vC_W00_se1585246474, InputTimeWindows.all2Min);
//run("SITWA_x_vC_W00_se1585246474_W3", InputBuffer.SITWA_x_vC_W00_se1585246474, InputTimeWindows.all3Min);
//run("SITWA_vC_W00_se1585246474_W3", InputBuffer.SITWA_vC_W00_se1585246474, InputTimeWindows.all3Min);
////run("SITWA_x_vC_W00_se1585246474_W4", InputBuffer.SITWA_x_vC_W00_se1585246474, InputTimeWindows.all4Min);
////run("SITWA_vC_W00_se1585246474_W4", InputBuffer.SITWA_vC_W00_se1585246474, InputTimeWindows.all4Min);
////run("SITWA_x_vC_W00_se1585246474_W5", InputBuffer.SITWA_x_vC_W00_se1585246474, InputTimeWindows.all5Min);
////run("SITWA_vC_W00_se1585246474_W5", InputBuffer.SITWA_vC_W00_se1585246474, InputTimeWindows.all5Min);
//
////run("SITWA_x_vC_W10_se1585246474_W1", InputBuffer.SITWA_x_vC_W10_se1585246474, InputTimeWindows.all1Min);
////run("SITWA_vC_W10_se1585246474_W1", InputBuffer.SITWA_vC_W10_se1585246474, InputTimeWindows.all1Min);
////run("SITWA_x_vC_W10_se1585246474_W2", InputBuffer.SITWA_x_vC_W20_se1585246474, InputTimeWindows.all2Min);
////run("SITWA_vC_W10_se1585246474_W2", InputBuffer.SITWA_vC_W20_se1585246474, InputTimeWindows.all2Min);
////run("SITWA_x_vC_W10_se1585246474_W3", InputBuffer.SITWA_x_vC_W30_se1585246474, InputTimeWindows.all3Min);
////run("SITWA_vC_W10_se1585246474_W3", InputBuffer.SITWA_vC_W30_se1585246474, InputTimeWindows.all3Min);
////run("SITWA_x_vC_W10_se1585246474_W4", InputBuffer.SITWA_x_vC_W40_se1585246474, InputTimeWindows.all4Min);
////run("SITWA_vC_W10_se1585246474_W3", InputBuffer.SITWA_vC_W40_se1585246474, InputTimeWindows.all4Min);
////run("SITWA_x_vC_W10_se1585246474_W5", InputBuffer.SITWA_x_vC_W50_se1585246474, InputTimeWindows.all5Min);
////run("SITWA_vC_W10_se1585246474_W3", InputBuffer.SITWA_vC_W50_se1585246474, InputTimeWindows.all5Min);
//
//settings.timeWindowMethod = "AfterArrival";
//
//settings.expectedTravelTime = InputExpectedTravelTime.newMinTT;
//
//run("aaSITWA_x_vA_W00_se1585246474_W1", InputBuffer.SITWA_x_vA_W00_se1585246474, InputTimeWindows.all1Min);
//run("aaSITWA_vA_W00_se1585246474_W1", InputBuffer.SITWA_vA_W00_se1585246474, InputTimeWindows.all1Min);
//run("aaSITWA_x_vA_W00_se1585246474_W2", InputBuffer.SITWA_x_vA_W00_se1585246474, InputTimeWindows.all2Min);
//run("aaSITWA_vA_W00_se1585246474_W2", InputBuffer.SITWA_vA_W00_se1585246474, InputTimeWindows.all2Min);
//run("aaSITWA_x_vA_W00_se1585246474_W3", InputBuffer.SITWA_x_vA_W00_se1585246474, InputTimeWindows.all3Min);
//run("aaSITWA_vA_W00_se1585246474_W3", InputBuffer.SITWA_vA_W00_se1585246474, InputTimeWindows.all3Min);
////run("aaSITWA_x_vA_W00_se1585246474_W4", InputBuffer.SITWA_x_vA_W00_se1585246474, InputTimeWindows.all4Min);
////run("aaSITWA_vA_W00_se1585246474_W4", InputBuffer.SITWA_vA_W00_se1585246474, InputTimeWindows.all4Min);
////run("aaSITWA_x_vA_W00_se1585246474_W5", InputBuffer.SITWA_x_vA_W00_se1585246474, InputTimeWindows.all5Min);
////run("aaSITWA_vA_W00_se1585246474_W5", InputBuffer.SITWA_vA_W00_se1585246474, InputTimeWindows.all5Min);
//
////run("aaSITWA_x_vA_W10_se1585246474_W1", InputBuffer.SITWA_x_vA_W10_se1585246474, InputTimeWindows.all1Min);
////run("aaSITWA_vA_W10_se1585246474_W1", InputBuffer.SITWA_vA_W10_se1585246474, InputTimeWindows.all1Min);
////run("aaSITWA_x_vA_W20_se1585246474_W2", InputBuffer.SITWA_x_vA_W20_se1585246474, InputTimeWindows.all2Min);
////run("aaSITWA_vA_W20_se1585246474_W2", InputBuffer.SITWA_vA_W20_se1585246474, InputTimeWindows.all2Min);
////run("aaSITWA_x_vA_W30_se1585246474_W3", InputBuffer.SITWA_x_vA_W30_se1585246474, InputTimeWindows.all3Min);
////run("aaSITWA_vA_W30_se1585246474_W3", InputBuffer.SITWA_vA_W30_se1585246474, InputTimeWindows.all3Min);
////run("aaSITWA_x_vA_W40_se1585246474_W4", InputBuffer.SITWA_x_vA_W40_se1585246474, InputTimeWindows.all4Min);
////run("aaSITWA_vA_W40_se1585246474_W4", InputBuffer.SITWA_vA_W40_se1585246474, InputTimeWindows.all4Min);
////run("aaSITWA_x_vA_W50_se1585246474_W5", InputBuffer.SITWA_x_vA_W50_se1585246474, InputTimeWindows.all5Min);
////run("aaSITWA_vA_W50_se1585246474_W5", InputBuffer.SITWA_vA_W50_se1585246474, InputTimeWindows.all5Min);
//
//
//settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;
//
//run("aaSITWA_x_vB_W00_se1585246474_W1", InputBuffer.SITWA_x_vB_W00_se1585246474, InputTimeWindows.all1Min);
//run("aaSITWA_vB_W00_se1585246474_W1", InputBuffer.SITWA_vB_W00_se1585246474, InputTimeWindows.all1Min);
//run("aaSITWA_x_vB_W00_se1585246474_W2", InputBuffer.SITWA_x_vB_W00_se1585246474, InputTimeWindows.all2Min);
//run("aaSITWA_vB_W00_se1585246474_W2", InputBuffer.SITWA_vB_W00_se1585246474, InputTimeWindows.all2Min);
//run("aaSITWA_x_vB_W00_se1585246474_W3", InputBuffer.SITWA_x_vB_W00_se1585246474, InputTimeWindows.all3Min);
//run("aaSITWA_vB_W00_se1585246474_W3", InputBuffer.SITWA_vB_W00_se1585246474, InputTimeWindows.all3Min);
////run("aaSITWA_x_vB_W00_se1585246474_W4", InputBuffer.SITWA_x_vB_W00_se1585246474, InputTimeWindows.all4Min);
////run("aaSITWA_vB_W00_se1585246474_W4", InputBuffer.SITWA_vB_W00_se1585246474, InputTimeWindows.all4Min);
////run("aaSITWA_x_vB_W00_se1585246474_W5", InputBuffer.SITWA_x_vB_W00_se1585246474, InputTimeWindows.all5Min);
////run("aaSITWA_vB_W00_se1585246474_W5", InputBuffer.SITWA_vB_W00_se1585246474, InputTimeWindows.all5Min);
//
////run("aaSITWA_x_vB_W10_se1585246474_W1", InputBuffer.SITWA_x_vB_W10_se1585246474, InputTimeWindows.all1Min);
////run("aaSITWA_vB_W10_se1585246474_W1", InputBuffer.SITWA_vB_W10_se1585246474, InputTimeWindows.all1Min);
////run("aaSITWA_x_vB_W20_se1585246474_W2", InputBuffer.SITWA_x_vB_W20_se1585246474, InputTimeWindows.all2Min);
////run("aaSITWA_vB_W20_se1585246474_W2", InputBuffer.SITWA_vB_W20_se1585246474, InputTimeWindows.all2Min);
////run("aaSITWA_x_vB_W30_se1585246474_W3", InputBuffer.SITWA_x_vB_W30_se1585246474, InputTimeWindows.all3Min);
////run("aaSITWA_vB_W30_se1585246474_W3", InputBuffer.SITWA_vB_W30_se1585246474, InputTimeWindows.all3Min);
////run("aaSITWA_x_vB_W40_se1585246474_W4", InputBuffer.SITWA_x_vB_W40_se1585246474, InputTimeWindows.all4Min);
////run("aaSITWA_vB_W40_se1585246474_W4", InputBuffer.SITWA_vB_W40_se1585246474, InputTimeWindows.all4Min);
////run("aaSITWA_x_vB_W50_se1585246474_W5", InputBuffer.SITWA_x_vB_W50_se1585246474, InputTimeWindows.all5Min);
////run("aaSITWA_vB_W50_se1585246474_W5", InputBuffer.SITWA_vB_W50_se1585246474, InputTimeWindows.all5Min);
//
//
//run("aaSITWA_x_vC_W00_se1585246474_W1", InputBuffer.SITWA_x_vC_W00_se1585246474, InputTimeWindows.all1Min);
//run("aaSITWA_vC_W00_se1585246474_W1", InputBuffer.SITWA_vC_W00_se1585246474, InputTimeWindows.all1Min);
//run("aaSITWA_x_vC_W00_se1585246474_W2", InputBuffer.SITWA_x_vC_W00_se1585246474, InputTimeWindows.all2Min);
//run("aaSITWA_vC_W00_se1585246474_W2", InputBuffer.SITWA_vC_W00_se1585246474, InputTimeWindows.all2Min);
//run("aaSITWA_x_vC_W00_se1585246474_W3", InputBuffer.SITWA_x_vC_W00_se1585246474, InputTimeWindows.all3Min);
//run("aaSITWA_vC_W00_se1585246474_W3", InputBuffer.SITWA_vC_W00_se1585246474, InputTimeWindows.all3Min);
////run("aaSITWA_x_vC_W00_se1585246474_W4", InputBuffer.SITWA_x_vC_W00_se1585246474, InputTimeWindows.all4Min);
////run("aaSITWA_vC_W00_se1585246474_W4", InputBuffer.SITWA_vC_W00_se1585246474, InputTimeWindows.all4Min);
////run("aaSITWA_x_vC_W00_se1585246474_W5", InputBuffer.SITWA_x_vC_W00_se1585246474, InputTimeWindows.all5Min);
////run("aaSITWA_vC_W00_se1585246474_W5", InputBuffer.SITWA_vC_W00_se1585246474, InputTimeWindows.all5Min);
//
////run("aaSITWA_x_vC_W10_se1585246474_W1", InputBuffer.SITWA_x_vC_W10_se1585246474, InputTimeWindows.all1Min);
////run("aaSITWA_vC_W10_se1585246474_W1", InputBuffer.SITWA_vC_W10_se1585246474, InputTimeWindows.all1Min);
////run("aaSITWA_x_vC_W10_se1585246474_W2", InputBuffer.SITWA_x_vC_W20_se1585246474, InputTimeWindows.all2Min);
////run("aaSITWA_vC_W10_se1585246474_W2", InputBuffer.SITWA_vC_W20_se1585246474, InputTimeWindows.all2Min);
////run("aaSITWA_x_vC_W10_se1585246474_W3", InputBuffer.SITWA_x_vC_W30_se1585246474, InputTimeWindows.all3Min);
////run("aaSITWA_vC_W10_se1585246474_W3", InputBuffer.SITWA_vC_W30_se1585246474, InputTimeWindows.all3Min);
////run("aaSITWA_x_vC_W10_se1585246474_W4", InputBuffer.SITWA_x_vC_W40_se1585246474, InputTimeWindows.all4Min);
////run("aaSITWA_vC_W10_se1585246474_W3", InputBuffer.SITWA_vC_W40_se1585246474, InputTimeWindows.all4Min);
////run("aaSITWA_x_vC_W10_se1585246474_W5", InputBuffer.SITWA_x_vC_W50_se1585246474, InputTimeWindows.all5Min);
////run("aaSITWA_vC_W10_se1585246474_W3", InputBuffer.SITWA_vC_W50_se1585246474, InputTimeWindows.all5Min);

//run("SD_1W", InputBuffer.bufferNewSD, InputTimeWindows.all1Min);
//run("SITWA_vB1_W00_se1585246474_1W", InputBuffer.SITWA_vB1_W00_se1585246474, InputTimeWindows.all1Min);
//run("SITWA_vB2_W00_se1585246474_1W", InputBuffer.SITWA_vB2_W00_se1585246474, InputTimeWindows.all1Min);
//run("SITWA_x_vB1_W00_se1585246474_1W", InputBuffer.SITWA_x_vB1_W00_se1585246474, InputTimeWindows.all1Min);
//run("SITWA_x_vB2_W00_se1585246474_1W", InputBuffer.SITWA_x_vB2_W00_se1585246474, InputTimeWindows.all1Min);
//
//run("SD_2W", InputBuffer.bufferNewSD, InputTimeWindows.all2Min);
//run("SITWA_vB1_W00_se1585246474_2W", InputBuffer.SITWA_vB1_W00_se1585246474, InputTimeWindows.all2Min);
//run("SITWA_vB2_W00_se1585246474_2W", InputBuffer.SITWA_vB2_W00_se1585246474, InputTimeWindows.all2Min);
//run("SITWA_x_vB1_W00_se1585246474_2W", InputBuffer.SITWA_x_vB1_W00_se1585246474, InputTimeWindows.all2Min);
//run("SITWA_x_vB2_W00_se1585246474_2W", InputBuffer.SITWA_x_vB2_W00_se1585246474, InputTimeWindows.all2Min);
//
//run("SD_3W", InputBuffer.bufferNewSD, InputTimeWindows.all3Min);
//run("SITWA_vB1_W00_se1585246474_3W", InputBuffer.SITWA_vB1_W00_se1585246474, InputTimeWindows.all3Min);
//run("SITWA_vB2_W00_se1585246474_3W", InputBuffer.SITWA_vB2_W00_se1585246474, InputTimeWindows.all3Min);
//run("SITWA_x_vB1_W00_se1585246474_3W", InputBuffer.SITWA_x_vB1_W00_se1585246474, InputTimeWindows.all3Min);
//run("SITWA_x_vB2_W00_se1585246474_3W", InputBuffer.SITWA_x_vB2_W00_se1585246474, InputTimeWindows.all3Min);
//
//run("SD_4W", InputBuffer.bufferNewSD, InputTimeWindows.all4Min);
//run("SITWA_vB1_W00_se1585246474_4W", InputBuffer.SITWA_vB1_W00_se1585246474, InputTimeWindows.all4Min);
//run("SITWA_vB2_W00_se1585246474_4W", InputBuffer.SITWA_vB2_W00_se1585246474, InputTimeWindows.all4Min);
//run("SITWA_x_vB1_W00_se1585246474_4W", InputBuffer.SITWA_x_vB1_W00_se1585246474, InputTimeWindows.all4Min);
//run("SITWA_x_vB2_W00_se1585246474_4W", InputBuffer.SITWA_x_vB2_W00_se1585246474, InputTimeWindows.all4Min);
//
//run("SD_5W", InputBuffer.bufferNewSD, InputTimeWindows.all5Min);
//run("SITWA_vB1_W00_se1585246474_5W", InputBuffer.SITWA_vB1_W00_se1585246474, InputTimeWindows.all5Min);
//run("SITWA_vB2_W00_se1585246474_5W", InputBuffer.SITWA_vB2_W00_se1585246474, InputTimeWindows.all5Min);
//run("SITWA_x_vB1_W00_se1585246474_5W", InputBuffer.SITWA_x_vB1_W00_se1585246474, InputTimeWindows.all5Min);
//run("SITWA_x_vB2_W00_se1585246474_5W", InputBuffer.SITWA_x_vB2_W00_se1585246474, InputTimeWindows.all5Min);
//
//run("SD_6W", InputBuffer.bufferNewSD, InputTimeWindows.all6Min);
//run("SITWA_vB1_W00_se1585246474_6W", InputBuffer.SITWA_vB1_W00_se1585246474, InputTimeWindows.all6Min);
//run("SITWA_vB2_W00_se1585246474_6W", InputBuffer.SITWA_vB2_W00_se1585246474, InputTimeWindows.all6Min);
//run("SITWA_x_vB1_W00_se1585246474_6W", InputBuffer.SITWA_x_vB1_W00_se1585246474, InputTimeWindows.all6Min);
//run("SITWA_x_vB2_W00_se1585246474_6W", InputBuffer.SITWA_x_vB2_W00_se1585246474, InputTimeWindows.all6Min);
//
//run("SD_7W", InputBuffer.bufferNewSD, InputTimeWindows.all7Min);
//run("SITWA_vB1_W00_se1585246474_7W", InputBuffer.SITWA_vB1_W00_se1585246474, InputTimeWindows.all7Min);
//run("SITWA_vB2_W00_se1585246474_7W", InputBuffer.SITWA_vB2_W00_se1585246474, InputTimeWindows.all7Min);
//run("SITWA_x_vB1_W00_se1585246474_7W", InputBuffer.SITWA_x_vB1_W00_se1585246474, InputTimeWindows.all7Min);
//run("SITWA_x_vB2_W00_se1585246474_7W", InputBuffer.SITWA_x_vB2_W00_se1585246474, InputTimeWindows.all7Min);
//
//run("SD_8W", InputBuffer.bufferNewSD, InputTimeWindows.all8Min);
//run("SITWA_vB1_W00_se1585246474_8W", InputBuffer.SITWA_vB1_W00_se1585246474, InputTimeWindows.all8Min);
//run("SITWA_vB2_W00_se1585246474_8W", InputBuffer.SITWA_vB2_W00_se1585246474, InputTimeWindows.all8Min);
//run("SITWA_x_vB1_W00_se1585246474_8W", InputBuffer.SITWA_x_vB1_W00_se1585246474, InputTimeWindows.all8Min);
//run("SITWA_x_vB2_W00_se1585246474_8W", InputBuffer.SITWA_x_vB2_W00_se1585246474, InputTimeWindows.all8Min);
//
//run("SD_9W", InputBuffer.bufferNewSD, InputTimeWindows.all9Min);
//run("SITWA_vB1_W00_se1585246474_9W", InputBuffer.SITWA_vB1_W00_se1585246474, InputTimeWindows.all9Min);
//run("SITWA_vB2_W00_se1585246474_9W", InputBuffer.SITWA_vB2_W00_se1585246474, InputTimeWindows.all9Min);
//run("SITWA_x_vB1_W00_se1585246474_9W", InputBuffer.SITWA_x_vB1_W00_se1585246474, InputTimeWindows.all9Min);
//run("SITWA_x_vB2_W00_se1585246474_9W", InputBuffer.SITWA_x_vB2_W00_se1585246474, InputTimeWindows.all9Min);
//
//run("SD_10W", InputBuffer.bufferNewSD, InputTimeWindows.all10Min);
//run("SITWA_vB1_W00_se1585246474_10W", InputBuffer.SITWA_vB1_W00_se1585246474, InputTimeWindows.all10Min);
//run("SITWA_vB2_W00_se1585246474_10W", InputBuffer.SITWA_vB2_W00_se1585246474, InputTimeWindows.all10Min);
//run("SITWA_x_vB1_W00_se1585246474_10W", InputBuffer.SITWA_x_vB1_W00_se1585246474, InputTimeWindows.all10Min);
//run("SITWA_x_vB2_W00_se1585246474_10W", InputBuffer.SITWA_x_vB2_W00_se1585246474, InputTimeWindows.all10Min);

//***************************************************************************************************************************
//run("SITWA_x_vA_W00_se00_1W", InputBuffer2.SITWA_x_vA_W00_se00, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se00_1W", InputBuffer2.SITWA_x_vA_W10_se00, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se00_2W", InputBuffer2.SITWA_x_vA_W20_se00, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se00_3W", InputBuffer2.SITWA_x_vA_W30_se00, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se00_4W", InputBuffer2.SITWA_x_vA_W40_se00, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se00_5W", InputBuffer2.SITWA_x_vA_W50_se00, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se00_6W", InputBuffer2.SITWA_x_vA_W60_se00, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se00_7W", InputBuffer2.SITWA_x_vA_W70_se00, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se00_8W", InputBuffer2.SITWA_x_vA_W80_se00, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se00_9W", InputBuffer2.SITWA_x_vA_W90_se00, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se00_10W", InputBuffer2.SITWA_x_vA_W100_se00, InputTimeWindows.all10Min);
//
//run("SITWA_vA_W00_se01_1W", InputBuffer.SITWA_vA_W00_se01, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se01_1W", InputBuffer.SITWA_vA_W10_se01, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se01_2W", InputBuffer.SITWA_vA_W20_se01, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se01_3W", InputBuffer.SITWA_vA_W30_se01, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se01_4W", InputBuffer.SITWA_vA_W40_se01, InputTimeWindows.all4Min);
//
//run("SITWA_x_vA_W00_se01_1W", InputBuffer2.SITWA_x_vA_W00_se01, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se01_1W", InputBuffer2.SITWA_x_vA_W10_se01, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se01_2W", InputBuffer2.SITWA_x_vA_W20_se01, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se01_3W", InputBuffer2.SITWA_x_vA_W30_se01, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se01_4W", InputBuffer2.SITWA_x_vA_W40_se01, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se01_5W", InputBuffer2.SITWA_x_vA_W50_se01, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se01_6W", InputBuffer2.SITWA_x_vA_W60_se01, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se01_7W", InputBuffer2.SITWA_x_vA_W70_se01, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se01_8W", InputBuffer2.SITWA_x_vA_W80_se01, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se01_9W", InputBuffer2.SITWA_x_vA_W90_se01, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se01_10W", InputBuffer2.SITWA_x_vA_W100_se01, InputTimeWindows.all10Min);
//
//run("SITWA_vA_W00_se05_1W", InputBuffer.SITWA_vA_W00_se05, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se05_1W", InputBuffer.SITWA_vA_W10_se05, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se05_2W", InputBuffer.SITWA_vA_W20_se05, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se05_3W", InputBuffer.SITWA_vA_W30_se05, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se05_4W", InputBuffer.SITWA_vA_W40_se05, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se05_5W", InputBuffer.SITWA_vA_W50_se05, InputTimeWindows.all5Min);
//
//run("SITWA_x_vA_W00_se05_1W", InputBuffer2.SITWA_x_vA_W00_se05, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se05_1W", InputBuffer2.SITWA_x_vA_W10_se05, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se05_2W", InputBuffer2.SITWA_x_vA_W20_se05, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se05_3W", InputBuffer2.SITWA_x_vA_W30_se05, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se05_4W", InputBuffer2.SITWA_x_vA_W40_se05, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se05_5W", InputBuffer2.SITWA_x_vA_W50_se05, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se05_6W", InputBuffer2.SITWA_x_vA_W60_se05, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se05_7W", InputBuffer2.SITWA_x_vA_W70_se05, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se05_8W", InputBuffer2.SITWA_x_vA_W80_se05, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se05_9W", InputBuffer2.SITWA_x_vA_W90_se05, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se05_10W", InputBuffer2.SITWA_x_vA_W100_se05, InputTimeWindows.all10Min);
//
//run("SITWA_vA_W00_se10_1W", InputBuffer.SITWA_vA_W00_se10, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se10_1W", InputBuffer.SITWA_vA_W10_se10, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se10_2W", InputBuffer.SITWA_vA_W20_se10, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se10_3W", InputBuffer.SITWA_vA_W30_se10, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se10_4W", InputBuffer.SITWA_vA_W40_se10, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se10_5W", InputBuffer.SITWA_vA_W50_se10, InputTimeWindows.all5Min);
//
//run("SITWA_x_vA_W00_se10_1W", InputBuffer2.SITWA_x_vA_W00_se10, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se10_1W", InputBuffer2.SITWA_x_vA_W10_se10, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se10_2W", InputBuffer2.SITWA_x_vA_W20_se10, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se10_3W", InputBuffer2.SITWA_x_vA_W30_se10, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se10_4W", InputBuffer2.SITWA_x_vA_W40_se10, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se10_5W", InputBuffer2.SITWA_x_vA_W50_se10, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se10_6W", InputBuffer2.SITWA_x_vA_W60_se10, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se10_7W", InputBuffer2.SITWA_x_vA_W70_se10, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se10_8W", InputBuffer2.SITWA_x_vA_W80_se10, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se10_9W", InputBuffer2.SITWA_x_vA_W90_se10, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se10_10W", InputBuffer2.SITWA_x_vA_W100_se10, InputTimeWindows.all10Min);
//
//run("SITWA_vA_W00_se15_1W", InputBuffer.SITWA_vA_W00_se15, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se15_1W", InputBuffer.SITWA_vA_W10_se15, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se15_2W", InputBuffer.SITWA_vA_W20_se15, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se15_3W", InputBuffer.SITWA_vA_W30_se15, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se15_4W", InputBuffer.SITWA_vA_W40_se15, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se15_5W", InputBuffer.SITWA_vA_W50_se15, InputTimeWindows.all5Min);
//
//run("SITWA_x_vA_W00_se15_1W", InputBuffer2.SITWA_x_vA_W00_se15, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se15_1W", InputBuffer2.SITWA_x_vA_W10_se15, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se15_2W", InputBuffer2.SITWA_x_vA_W20_se15, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se15_3W", InputBuffer2.SITWA_x_vA_W30_se15, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se15_4W", InputBuffer2.SITWA_x_vA_W40_se15, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se15_5W", InputBuffer2.SITWA_x_vA_W50_se15, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se15_6W", InputBuffer2.SITWA_x_vA_W60_se15, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se15_7W", InputBuffer2.SITWA_x_vA_W70_se15, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se15_8W", InputBuffer2.SITWA_x_vA_W80_se15, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se15_9W", InputBuffer2.SITWA_x_vA_W90_se15, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se15_10W", InputBuffer2.SITWA_x_vA_W100_se15, InputTimeWindows.all10Min);
//
//run("SITWA_vA_W00_se20_1W", InputBuffer.SITWA_vA_W00_se20, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se20_1W", InputBuffer.SITWA_vA_W10_se20, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se20_2W", InputBuffer.SITWA_vA_W20_se20, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se20_3W", InputBuffer.SITWA_vA_W30_se20, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se20_4W", InputBuffer.SITWA_vA_W40_se20, InputTimeWindows.all4Min);
//
//run("SITWA_x_vA_W00_se20_1W", InputBuffer2.SITWA_x_vA_W00_se20, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se20_1W", InputBuffer2.SITWA_x_vA_W10_se20, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se20_2W", InputBuffer2.SITWA_x_vA_W20_se20, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se20_3W", InputBuffer2.SITWA_x_vA_W30_se20, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se20_4W", InputBuffer2.SITWA_x_vA_W40_se20, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se20_5W", InputBuffer2.SITWA_x_vA_W50_se20, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se20_6W", InputBuffer2.SITWA_x_vA_W60_se20, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se20_7W", InputBuffer2.SITWA_x_vA_W70_se20, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se20_8W", InputBuffer2.SITWA_x_vA_W80_se20, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se20_9W", InputBuffer2.SITWA_x_vA_W90_se20, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se20_10W", InputBuffer2.SITWA_x_vA_W100_se20, InputTimeWindows.all10Min);
//
//run("SITWA_vA_W00_se25_1W", InputBuffer.SITWA_vA_W00_se25, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se25_1W", InputBuffer.SITWA_vA_W10_se25, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se25_2W", InputBuffer.SITWA_vA_W20_se25, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se25_3W", InputBuffer.SITWA_vA_W30_se25, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se25_4W", InputBuffer.SITWA_vA_W40_se25, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se25_5W", InputBuffer.SITWA_vA_W50_se25, InputTimeWindows.all5Min);
//
//run("SITWA_x_vA_W00_se25_1W", InputBuffer2.SITWA_x_vA_W00_se25, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se25_1W", InputBuffer2.SITWA_x_vA_W10_se25, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se25_2W", InputBuffer2.SITWA_x_vA_W20_se25, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se25_3W", InputBuffer2.SITWA_x_vA_W30_se25, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se25_4W", InputBuffer2.SITWA_x_vA_W40_se25, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se25_5W", InputBuffer2.SITWA_x_vA_W50_se25, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se25_6W", InputBuffer2.SITWA_x_vA_W60_se25, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se25_7W", InputBuffer2.SITWA_x_vA_W70_se25, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se25_8W", InputBuffer2.SITWA_x_vA_W80_se25, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se25_9W", InputBuffer2.SITWA_x_vA_W90_se25, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se25_10W", InputBuffer2.SITWA_x_vA_W100_se25, InputTimeWindows.all10Min);
//
//run("SITWA_vA_W00_se30_1W", InputBuffer.SITWA_vA_W00_se30, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se30_1W", InputBuffer.SITWA_vA_W10_se30, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se30_2W", InputBuffer.SITWA_vA_W20_se30, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se30_3W", InputBuffer.SITWA_vA_W30_se30, InputTimeWindows.all3Min);
//
//run("SITWA_x_vA_W00_se30_1W", InputBuffer2.SITWA_x_vA_W00_se30, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se30_1W", InputBuffer2.SITWA_x_vA_W10_se30, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se30_2W", InputBuffer2.SITWA_x_vA_W20_se30, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se30_3W", InputBuffer2.SITWA_x_vA_W30_se30, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se30_4W", InputBuffer2.SITWA_x_vA_W40_se30, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se30_5W", InputBuffer2.SITWA_x_vA_W50_se30, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se30_6W", InputBuffer2.SITWA_x_vA_W60_se30, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se30_7W", InputBuffer2.SITWA_x_vA_W70_se30, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se30_8W", InputBuffer2.SITWA_x_vA_W80_se30, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se30_9W", InputBuffer2.SITWA_x_vA_W90_se30, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se30_10W", InputBuffer2.SITWA_x_vA_W100_se30, InputTimeWindows.all10Min);
//
//run("SITWA_vA_W00_se35_1W", InputBuffer.SITWA_vA_W00_se35, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se35_1W", InputBuffer.SITWA_vA_W10_se35, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se35_2W", InputBuffer.SITWA_vA_W20_se35, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se35_3W", InputBuffer.SITWA_vA_W30_se35, InputTimeWindows.all3Min);
//
//run("SITWA_x_vA_W00_se35_1W", InputBuffer2.SITWA_x_vA_W00_se35, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se35_1W", InputBuffer2.SITWA_x_vA_W10_se35, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se35_2W", InputBuffer2.SITWA_x_vA_W20_se35, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se35_3W", InputBuffer2.SITWA_x_vA_W30_se35, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se35_4W", InputBuffer2.SITWA_x_vA_W40_se35, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se35_5W", InputBuffer2.SITWA_x_vA_W50_se35, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se35_6W", InputBuffer2.SITWA_x_vA_W60_se35, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se35_7W", InputBuffer2.SITWA_x_vA_W70_se35, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se35_8W", InputBuffer2.SITWA_x_vA_W80_se35, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se35_9W", InputBuffer2.SITWA_x_vA_W90_se35, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se35_10W", InputBuffer2.SITWA_x_vA_W100_se35, InputTimeWindows.all10Min);
//
//run("SITWA_vA_W00_se40_1W", InputBuffer.SITWA_vA_W00_se40, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se40_1W", InputBuffer.SITWA_vA_W10_se40, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se40_2W", InputBuffer.SITWA_vA_W20_se40, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se40_3W", InputBuffer.SITWA_vA_W30_se40, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se40_4W", InputBuffer.SITWA_vA_W40_se40, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se40_5W", InputBuffer.SITWA_vA_W50_se40, InputTimeWindows.all5Min);
//
//run("SITWA_x_vA_W00_se40_1W", InputBuffer2.SITWA_x_vA_W00_se40, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se40_1W", InputBuffer2.SITWA_x_vA_W10_se40, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se40_2W", InputBuffer2.SITWA_x_vA_W20_se40, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se40_3W", InputBuffer2.SITWA_x_vA_W30_se40, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se40_4W", InputBuffer2.SITWA_x_vA_W40_se40, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se40_5W", InputBuffer2.SITWA_x_vA_W50_se40, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se40_6W", InputBuffer2.SITWA_x_vA_W60_se40, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se40_7W", InputBuffer2.SITWA_x_vA_W70_se40, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se40_8W", InputBuffer2.SITWA_x_vA_W80_se40, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se40_9W", InputBuffer2.SITWA_x_vA_W90_se40, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se40_10W", InputBuffer2.SITWA_x_vA_W100_se40, InputTimeWindows.all10Min);
//
//run("SITWA_vA_W00_se45_1W", InputBuffer.SITWA_vA_W00_se45, InputTimeWindows.all1Min);
//run("SITWA_vA_W10_se45_1W", InputBuffer.SITWA_vA_W10_se45, InputTimeWindows.all1Min);
//run("SITWA_vA_W20_se45_2W", InputBuffer.SITWA_vA_W20_se45, InputTimeWindows.all2Min);
//run("SITWA_vA_W30_se45_3W", InputBuffer.SITWA_vA_W30_se45, InputTimeWindows.all3Min);
//run("SITWA_vA_W40_se45_4W", InputBuffer.SITWA_vA_W40_se45, InputTimeWindows.all4Min);
//run("SITWA_vA_W50_se45_5W", InputBuffer.SITWA_vA_W50_se45, InputTimeWindows.all5Min);
//
//run("SITWA_x_vA_W00_se45_1W", InputBuffer2.SITWA_x_vA_W00_se45, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W10_se45_1W", InputBuffer2.SITWA_x_vA_W10_se45, InputTimeWindows.all1Min);
//run("SITWA_x_vA_W20_se45_2W", InputBuffer2.SITWA_x_vA_W20_se45, InputTimeWindows.all2Min);
//run("SITWA_x_vA_W30_se45_3W", InputBuffer2.SITWA_x_vA_W30_se45, InputTimeWindows.all3Min);
//run("SITWA_x_vA_W40_se45_4W", InputBuffer2.SITWA_x_vA_W40_se45, InputTimeWindows.all4Min);
//run("SITWA_x_vA_W50_se45_5W", InputBuffer2.SITWA_x_vA_W50_se45, InputTimeWindows.all5Min);
//run("SITWA_x_vA_W60_se45_6W", InputBuffer2.SITWA_x_vA_W60_se45, InputTimeWindows.all6Min);
//run("SITWA_x_vA_W70_se45_7W", InputBuffer2.SITWA_x_vA_W70_se45, InputTimeWindows.all7Min);
//run("SITWA_x_vA_W80_se45_8W", InputBuffer2.SITWA_x_vA_W80_se45, InputTimeWindows.all8Min);
//run("SITWA_x_vA_W90_se45_9W", InputBuffer2.SITWA_x_vA_W90_se45, InputTimeWindows.all9Min);
//run("SITWA_x_vA_W100_se45_10W", InputBuffer2.SITWA_x_vA_W100_se45, InputTimeWindows.all10Min);
//
//settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;
//
//run("SITWA_x_vB_W00_se00_1W", InputBuffer2.SITWA_x_vB_W00_se00, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W10_se00_1W", InputBuffer2.SITWA_x_vB_W10_se00, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W20_se00_2W", InputBuffer2.SITWA_x_vB_W20_se00, InputTimeWindows.all2Min);
//run("SITWA_x_vB_W30_se00_3W", InputBuffer2.SITWA_x_vB_W30_se00, InputTimeWindows.all3Min);
//run("SITWA_x_vB_W40_se00_4W", InputBuffer2.SITWA_x_vB_W40_se00, InputTimeWindows.all4Min);
//run("SITWA_x_vB_W50_se00_5W", InputBuffer2.SITWA_x_vB_W50_se00, InputTimeWindows.all5Min);
//run("SITWA_x_vB_W60_se00_6W", InputBuffer2.SITWA_x_vB_W60_se00, InputTimeWindows.all6Min);
//run("SITWA_x_vB_W70_se00_7W", InputBuffer2.SITWA_x_vB_W70_se00, InputTimeWindows.all7Min);
//run("SITWA_x_vB_W80_se00_8W", InputBuffer2.SITWA_x_vB_W80_se00, InputTimeWindows.all8Min);
//run("SITWA_x_vB_W90_se00_9W", InputBuffer2.SITWA_x_vB_W90_se00, InputTimeWindows.all9Min);
//run("SITWA_x_vB_W100_se00_10W", InputBuffer2.SITWA_x_vB_W100_se00, InputTimeWindows.all10Min);
//
//run("SITWA_vB_W00_se01_1W", InputBuffer.SITWA_vB_W00_se01, InputTimeWindows.all1Min);
//run("SITWA_vB_W10_se01_1W", InputBuffer.SITWA_vB_W10_se01, InputTimeWindows.all1Min);
//run("SITWA_vB_W20_se01_2W", InputBuffer.SITWA_vB_W20_se01, InputTimeWindows.all2Min);
//
//run("SITWA_x_vB_W00_se01_1W", InputBuffer2.SITWA_x_vB_W00_se01, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W10_se01_1W", InputBuffer2.SITWA_x_vB_W10_se01, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W20_se01_2W", InputBuffer2.SITWA_x_vB_W20_se01, InputTimeWindows.all2Min);
//run("SITWA_x_vB_W30_se01_3W", InputBuffer2.SITWA_x_vB_W30_se01, InputTimeWindows.all3Min);
//run("SITWA_x_vB_W40_se01_4W", InputBuffer2.SITWA_x_vB_W40_se01, InputTimeWindows.all4Min);
//run("SITWA_x_vB_W50_se01_5W", InputBuffer2.SITWA_x_vB_W50_se01, InputTimeWindows.all5Min);
//run("SITWA_x_vB_W60_se01_6W", InputBuffer2.SITWA_x_vB_W60_se01, InputTimeWindows.all6Min);
//run("SITWA_x_vB_W70_se01_7W", InputBuffer2.SITWA_x_vB_W70_se01, InputTimeWindows.all7Min);
//run("SITWA_x_vB_W80_se01_8W", InputBuffer2.SITWA_x_vB_W80_se01, InputTimeWindows.all8Min);
//run("SITWA_x_vB_W90_se01_9W", InputBuffer2.SITWA_x_vB_W90_se01, InputTimeWindows.all9Min);
//
//run("SITWA_vB_W00_se05_1W", InputBuffer.SITWA_vB_W00_se05, InputTimeWindows.all1Min);
//run("SITWA_vB_W10_se05_1W", InputBuffer.SITWA_vB_W10_se05, InputTimeWindows.all1Min);
//run("SITWA_vB_W20_se05_2W", InputBuffer.SITWA_vB_W20_se05, InputTimeWindows.all2Min);
//
//run("SITWA_x_vB_W00_se05_1W", InputBuffer2.SITWA_x_vB_W00_se05, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W10_se05_1W", InputBuffer2.SITWA_x_vB_W10_se05, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W20_se05_2W", InputBuffer2.SITWA_x_vB_W20_se05, InputTimeWindows.all2Min);
//run("SITWA_x_vB_W30_se05_3W", InputBuffer2.SITWA_x_vB_W30_se05, InputTimeWindows.all3Min);
//run("SITWA_x_vB_W40_se05_4W", InputBuffer2.SITWA_x_vB_W40_se05, InputTimeWindows.all4Min);
//run("SITWA_x_vB_W50_se05_5W", InputBuffer2.SITWA_x_vB_W50_se05, InputTimeWindows.all5Min);
//run("SITWA_x_vB_W60_se05_6W", InputBuffer2.SITWA_x_vB_W60_se05, InputTimeWindows.all6Min);
//run("SITWA_x_vB_W70_se05_7W", InputBuffer2.SITWA_x_vB_W70_se05, InputTimeWindows.all7Min);
//run("SITWA_x_vB_W80_se05_8W", InputBuffer2.SITWA_x_vB_W80_se05, InputTimeWindows.all8Min);
//
//run("SITWA_vB_W00_se10_1W", InputBuffer.SITWA_vB_W00_se10, InputTimeWindows.all1Min);
//run("SITWA_vB_W10_se10_1W", InputBuffer.SITWA_vB_W10_se10, InputTimeWindows.all1Min);
//run("SITWA_vB_W20_se10_2W", InputBuffer.SITWA_vB_W20_se10, InputTimeWindows.all2Min);
//
//run("SITWA_x_vB_W00_se10_1W", InputBuffer2.SITWA_x_vB_W00_se10, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W10_se10_1W", InputBuffer2.SITWA_x_vB_W10_se10, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W20_se10_2W", InputBuffer2.SITWA_x_vB_W20_se10, InputTimeWindows.all2Min);
//run("SITWA_x_vB_W30_se10_3W", InputBuffer2.SITWA_x_vB_W30_se10, InputTimeWindows.all3Min);
//run("SITWA_x_vB_W40_se10_4W", InputBuffer2.SITWA_x_vB_W40_se10, InputTimeWindows.all4Min);
//run("SITWA_x_vB_W50_se10_5W", InputBuffer2.SITWA_x_vB_W50_se10, InputTimeWindows.all5Min);
//run("SITWA_x_vB_W60_se10_6W", InputBuffer2.SITWA_x_vB_W60_se10, InputTimeWindows.all6Min);
//run("SITWA_x_vB_W70_se10_7W", InputBuffer2.SITWA_x_vB_W70_se10, InputTimeWindows.all7Min);
//run("SITWA_x_vB_W80_se10_8W", InputBuffer2.SITWA_x_vB_W80_se10, InputTimeWindows.all8Min);
//run("SITWA_x_vB_W90_se10_9W", InputBuffer2.SITWA_x_vB_W90_se10, InputTimeWindows.all9Min);
//run("SITWA_x_vB_W100_se10_10W", InputBuffer2.SITWA_x_vB_W100_se10, InputTimeWindows.all10Min);
//
//run("SITWA_vB_W00_se15_1W", InputBuffer.SITWA_vB_W00_se15, InputTimeWindows.all1Min);
//run("SITWA_vB_W10_se15_1W", InputBuffer.SITWA_vB_W10_se15, InputTimeWindows.all1Min);
//run("SITWA_vB_W20_se15_2W", InputBuffer.SITWA_vB_W20_se15, InputTimeWindows.all2Min);
//
//run("SITWA_x_vB_W00_se15_1W", InputBuffer2.SITWA_x_vB_W00_se15, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W10_se15_1W", InputBuffer2.SITWA_x_vB_W10_se15, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W20_se15_2W", InputBuffer2.SITWA_x_vB_W20_se15, InputTimeWindows.all2Min);
//run("SITWA_x_vB_W30_se15_3W", InputBuffer2.SITWA_x_vB_W30_se15, InputTimeWindows.all3Min);
//run("SITWA_x_vB_W40_se15_4W", InputBuffer2.SITWA_x_vB_W40_se15, InputTimeWindows.all4Min);
//run("SITWA_x_vB_W50_se15_5W", InputBuffer2.SITWA_x_vB_W50_se15, InputTimeWindows.all5Min);
//run("SITWA_x_vB_W60_se15_6W", InputBuffer2.SITWA_x_vB_W60_se15, InputTimeWindows.all6Min);
//run("SITWA_x_vB_W70_se15_7W", InputBuffer2.SITWA_x_vB_W70_se15, InputTimeWindows.all7Min);
//run("SITWA_x_vB_W80_se15_8W", InputBuffer2.SITWA_x_vB_W80_se15, InputTimeWindows.all8Min);
//run("SITWA_x_vB_W90_se15_9W", InputBuffer2.SITWA_x_vB_W90_se15, InputTimeWindows.all9Min);
//run("SITWA_x_vB_W100_se15_10W", InputBuffer2.SITWA_x_vB_W100_se15, InputTimeWindows.all10Min);
//
//run("SITWA_vB_W00_se20_1W", InputBuffer.SITWA_vB_W00_se20, InputTimeWindows.all1Min);
//run("SITWA_vB_W10_se20_1W", InputBuffer.SITWA_vB_W10_se20, InputTimeWindows.all1Min);
//run("SITWA_vB_W20_se20_2W", InputBuffer.SITWA_vB_W20_se20, InputTimeWindows.all2Min);
//
//run("SITWA_x_vB_W00_se20_1W", InputBuffer2.SITWA_x_vB_W00_se20, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W10_se20_1W", InputBuffer2.SITWA_x_vB_W10_se20, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W20_se20_2W", InputBuffer2.SITWA_x_vB_W20_se20, InputTimeWindows.all2Min);
//run("SITWA_x_vB_W30_se20_3W", InputBuffer2.SITWA_x_vB_W30_se20, InputTimeWindows.all3Min);
//run("SITWA_x_vB_W40_se20_4W", InputBuffer2.SITWA_x_vB_W40_se20, InputTimeWindows.all4Min);
//run("SITWA_x_vB_W50_se20_5W", InputBuffer2.SITWA_x_vB_W50_se20, InputTimeWindows.all5Min);
//run("SITWA_x_vB_W60_se20_6W", InputBuffer2.SITWA_x_vB_W60_se20, InputTimeWindows.all6Min);
//run("SITWA_x_vB_W70_se20_7W", InputBuffer2.SITWA_x_vB_W70_se20, InputTimeWindows.all7Min);
//run("SITWA_x_vB_W80_se20_8W", InputBuffer2.SITWA_x_vB_W80_se20, InputTimeWindows.all8Min);
//run("SITWA_x_vB_W90_se20_9W", InputBuffer2.SITWA_x_vB_W90_se20, InputTimeWindows.all9Min);
//run("SITWA_x_vB_W100_se20_10W", InputBuffer2.SITWA_x_vB_W100_se20, InputTimeWindows.all10Min);
//
//run("SITWA_vB_W00_se25_1W", InputBuffer.SITWA_vB_W00_se25, InputTimeWindows.all1Min);
//run("SITWA_vB_W10_se25_1W", InputBuffer.SITWA_vB_W10_se25, InputTimeWindows.all1Min);
//run("SITWA_vB_W20_se25_2W", InputBuffer.SITWA_vB_W20_se25, InputTimeWindows.all2Min);
//
//run("SITWA_x_vB_W00_se25_1W", InputBuffer2.SITWA_x_vB_W00_se25, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W10_se25_1W", InputBuffer2.SITWA_x_vB_W10_se25, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W20_se25_2W", InputBuffer2.SITWA_x_vB_W20_se25, InputTimeWindows.all2Min);
//run("SITWA_x_vB_W30_se25_3W", InputBuffer2.SITWA_x_vB_W30_se25, InputTimeWindows.all3Min);
//run("SITWA_x_vB_W40_se25_4W", InputBuffer2.SITWA_x_vB_W40_se25, InputTimeWindows.all4Min);
//run("SITWA_x_vB_W50_se25_5W", InputBuffer2.SITWA_x_vB_W50_se25, InputTimeWindows.all5Min);
//run("SITWA_x_vB_W60_se25_6W", InputBuffer2.SITWA_x_vB_W60_se25, InputTimeWindows.all6Min);
//run("SITWA_x_vB_W70_se25_7W", InputBuffer2.SITWA_x_vB_W70_se25, InputTimeWindows.all7Min);
//
//run("SITWA_vB_W00_se30_1W", InputBuffer.SITWA_vB_W00_se30, InputTimeWindows.all1Min);
//run("SITWA_vB_W10_se30_1W", InputBuffer.SITWA_vB_W10_se30, InputTimeWindows.all1Min);
//
//run("SITWA_x_vB_W00_se30_1W", InputBuffer2.SITWA_x_vB_W00_se30, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W10_se30_1W", InputBuffer2.SITWA_x_vB_W10_se30, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W20_se30_2W", InputBuffer2.SITWA_x_vB_W20_se30, InputTimeWindows.all2Min);
//run("SITWA_x_vB_W30_se30_3W", InputBuffer2.SITWA_x_vB_W30_se30, InputTimeWindows.all3Min);
//run("SITWA_x_vB_W40_se30_4W", InputBuffer2.SITWA_x_vB_W40_se30, InputTimeWindows.all4Min);
//run("SITWA_x_vB_W50_se30_5W", InputBuffer2.SITWA_x_vB_W50_se30, InputTimeWindows.all5Min);
//run("SITWA_x_vB_W60_se30_6W", InputBuffer2.SITWA_x_vB_W60_se30, InputTimeWindows.all6Min);
//run("SITWA_x_vB_W70_se30_7W", InputBuffer2.SITWA_x_vB_W70_se30, InputTimeWindows.all7Min);
//
//run("SITWA_x_vB_W00_se35_1W", InputBuffer2.SITWA_x_vB_W00_se35, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W10_se35_1W", InputBuffer2.SITWA_x_vB_W10_se35, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W20_se35_2W", InputBuffer2.SITWA_x_vB_W20_se35, InputTimeWindows.all2Min);
//run("SITWA_x_vB_W30_se35_3W", InputBuffer2.SITWA_x_vB_W30_se35, InputTimeWindows.all3Min);
//run("SITWA_x_vB_W40_se35_4W", InputBuffer2.SITWA_x_vB_W40_se35, InputTimeWindows.all4Min);
//run("SITWA_x_vB_W50_se35_5W", InputBuffer2.SITWA_x_vB_W50_se35, InputTimeWindows.all5Min);
//run("SITWA_x_vB_W60_se35_6W", InputBuffer2.SITWA_x_vB_W60_se35, InputTimeWindows.all6Min);
//run("SITWA_x_vB_W70_se35_7W", InputBuffer2.SITWA_x_vB_W70_se35, InputTimeWindows.all7Min);
//
//run("SITWA_vB_W00_se40_1W", InputBuffer.SITWA_vB_W00_se40, InputTimeWindows.all1Min);
//run("SITWA_vB_W10_se40_1W", InputBuffer.SITWA_vB_W10_se40, InputTimeWindows.all1Min);
//run("SITWA_vB_W20_se40_2W", InputBuffer.SITWA_vB_W20_se40, InputTimeWindows.all2Min);
//run("SITWA_vB_W30_se40_3W", InputBuffer.SITWA_vB_W30_se40, InputTimeWindows.all3Min);
//
//run("SITWA_x_vB_W00_se40_1W", InputBuffer2.SITWA_x_vB_W00_se40, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W10_se40_1W", InputBuffer2.SITWA_x_vB_W10_se40, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W20_se40_2W", InputBuffer2.SITWA_x_vB_W20_se40, InputTimeWindows.all2Min);
//run("SITWA_x_vB_W30_se40_3W", InputBuffer2.SITWA_x_vB_W30_se40, InputTimeWindows.all3Min);
//run("SITWA_x_vB_W40_se40_4W", InputBuffer2.SITWA_x_vB_W40_se40, InputTimeWindows.all4Min);
//run("SITWA_x_vB_W50_se40_5W", InputBuffer2.SITWA_x_vB_W50_se40, InputTimeWindows.all5Min);
//run("SITWA_x_vB_W60_se40_6W", InputBuffer2.SITWA_x_vB_W60_se40, InputTimeWindows.all6Min);
//run("SITWA_x_vB_W70_se40_7W", InputBuffer2.SITWA_x_vB_W70_se40, InputTimeWindows.all7Min);
//run("SITWA_x_vB_W80_se40_8W", InputBuffer2.SITWA_x_vB_W80_se40, InputTimeWindows.all8Min);
//run("SITWA_x_vB_W90_se40_9W", InputBuffer2.SITWA_x_vB_W90_se40, InputTimeWindows.all9Min);
//run("SITWA_x_vB_W100_se40_10W", InputBuffer2.SITWA_x_vB_W100_se40, InputTimeWindows.all10Min);
//
//run("SITWA_vB_W00_se45_1W", InputBuffer.SITWA_vB_W00_se45, InputTimeWindows.all1Min);
//run("SITWA_vB_W10_se45_1W", InputBuffer.SITWA_vB_W10_se45, InputTimeWindows.all1Min);
//run("SITWA_vB_W20_se45_2W", InputBuffer.SITWA_vB_W20_se45, InputTimeWindows.all2Min);
//run("SITWA_vB_W30_se45_3W", InputBuffer.SITWA_vB_W30_se45, InputTimeWindows.all3Min);
//
//run("SITWA_x_vB_W00_se45_1W", InputBuffer2.SITWA_x_vB_W00_se45, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W10_se45_1W", InputBuffer2.SITWA_x_vB_W10_se45, InputTimeWindows.all1Min);
//run("SITWA_x_vB_W20_se45_2W", InputBuffer2.SITWA_x_vB_W20_se45, InputTimeWindows.all2Min);
//run("SITWA_x_vB_W30_se45_3W", InputBuffer2.SITWA_x_vB_W30_se45, InputTimeWindows.all3Min);
//run("SITWA_x_vB_W40_se45_4W", InputBuffer2.SITWA_x_vB_W40_se45, InputTimeWindows.all4Min);
//run("SITWA_x_vB_W50_se45_5W", InputBuffer2.SITWA_x_vB_W50_se45, InputTimeWindows.all5Min);
//run("SITWA_x_vB_W60_se45_6W", InputBuffer2.SITWA_x_vB_W60_se45, InputTimeWindows.all6Min);
//run("SITWA_x_vB_W70_se45_7W", InputBuffer2.SITWA_x_vB_W70_se45, InputTimeWindows.all7Min);
//run("SITWA_x_vB_W80_se45_8W", InputBuffer2.SITWA_x_vB_W80_se45, InputTimeWindows.all8Min);
//run("SITWA_x_vB_W90_se45_9W", InputBuffer2.SITWA_x_vB_W90_se45, InputTimeWindows.all9Min);
//run("SITWA_x_vB_W100_se45_10W", InputBuffer2.SITWA_x_vB_W100_se45, InputTimeWindows.all10Min);
//
//run("SITWA_x_vC_W00_se00_1W", InputBuffer2.SITWA_x_vC_W00_se00, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W10_se00_1W", InputBuffer2.SITWA_x_vC_W10_se00, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W20_se00_2W", InputBuffer2.SITWA_x_vC_W20_se00, InputTimeWindows.all2Min);
//run("SITWA_x_vC_W30_se00_3W", InputBuffer2.SITWA_x_vC_W30_se00, InputTimeWindows.all3Min);
//run("SITWA_x_vC_W40_se00_4W", InputBuffer2.SITWA_x_vC_W40_se00, InputTimeWindows.all4Min);
//run("SITWA_x_vC_W50_se00_5W", InputBuffer2.SITWA_x_vC_W50_se00, InputTimeWindows.all5Min);
//run("SITWA_x_vC_W60_se00_6W", InputBuffer2.SITWA_x_vC_W60_se00, InputTimeWindows.all6Min);
//run("SITWA_x_vC_W70_se00_7W", InputBuffer2.SITWA_x_vC_W70_se00, InputTimeWindows.all7Min);
//run("SITWA_x_vC_W80_se00_8W", InputBuffer2.SITWA_x_vC_W80_se00, InputTimeWindows.all8Min);
//run("SITWA_x_vC_W90_se00_9W", InputBuffer2.SITWA_x_vC_W90_se00, InputTimeWindows.all9Min);
//run("SITWA_x_vC_W100_se00_10W", InputBuffer2.SITWA_x_vC_W100_se00, InputTimeWindows.all10Min);
//
//run("SITWA_vC_W00_se01_1W", InputBuffer.SITWA_vC_W00_se01, InputTimeWindows.all1Min);
//run("SITWA_vC_W10_se01_1W", InputBuffer.SITWA_vC_W10_se01, InputTimeWindows.all1Min);
//run("SITWA_vC_W20_se01_2W", InputBuffer.SITWA_vC_W20_se01, InputTimeWindows.all2Min);
//run("SITWA_vC_W30_se01_3W", InputBuffer.SITWA_vC_W30_se01, InputTimeWindows.all3Min);
//
//run("SITWA_x_vC_W00_se01_1W", InputBuffer2.SITWA_x_vC_W00_se01, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W10_se01_1W", InputBuffer2.SITWA_x_vC_W10_se01, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W20_se01_2W", InputBuffer2.SITWA_x_vC_W20_se01, InputTimeWindows.all2Min);
//run("SITWA_x_vC_W30_se01_3W", InputBuffer2.SITWA_x_vC_W30_se01, InputTimeWindows.all3Min);
//run("SITWA_x_vC_W40_se01_4W", InputBuffer2.SITWA_x_vC_W40_se01, InputTimeWindows.all4Min);
//run("SITWA_x_vC_W50_se01_5W", InputBuffer2.SITWA_x_vC_W50_se01, InputTimeWindows.all5Min);
//run("SITWA_x_vC_W60_se01_6W", InputBuffer2.SITWA_x_vC_W60_se01, InputTimeWindows.all6Min);
//run("SITWA_x_vC_W70_se01_7W", InputBuffer2.SITWA_x_vC_W70_se01, InputTimeWindows.all7Min);
//run("SITWA_x_vC_W80_se01_8W", InputBuffer2.SITWA_x_vC_W80_se01, InputTimeWindows.all8Min);
//run("SITWA_x_vC_W90_se01_9W", InputBuffer2.SITWA_x_vC_W90_se01, InputTimeWindows.all9Min);
//run("SITWA_x_vC_W100_se01_10W", InputBuffer2.SITWA_x_vC_W100_se01, InputTimeWindows.all10Min);
//
//run("SITWA_vC_W00_se05_1W", InputBuffer.SITWA_vC_W00_se05, InputTimeWindows.all1Min);
//run("SITWA_vC_W10_se05_1W", InputBuffer.SITWA_vC_W10_se05, InputTimeWindows.all1Min);
//run("SITWA_vC_W20_se05_2W", InputBuffer.SITWA_vC_W20_se05, InputTimeWindows.all2Min);
//run("SITWA_vC_W30_se05_3W", InputBuffer.SITWA_vC_W30_se05, InputTimeWindows.all3Min);
//
//run("SITWA_x_vC_W00_se05_1W", InputBuffer2.SITWA_x_vC_W00_se05, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W10_se05_1W", InputBuffer2.SITWA_x_vC_W10_se05, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W20_se05_2W", InputBuffer2.SITWA_x_vC_W20_se05, InputTimeWindows.all2Min);
//run("SITWA_x_vC_W30_se05_3W", InputBuffer2.SITWA_x_vC_W30_se05, InputTimeWindows.all3Min);
//run("SITWA_x_vC_W40_se05_4W", InputBuffer2.SITWA_x_vC_W40_se05, InputTimeWindows.all4Min);
//run("SITWA_x_vC_W50_se05_5W", InputBuffer2.SITWA_x_vC_W50_se05, InputTimeWindows.all5Min);
//run("SITWA_x_vC_W60_se05_6W", InputBuffer2.SITWA_x_vC_W60_se05, InputTimeWindows.all6Min);
//run("SITWA_x_vC_W70_se05_7W", InputBuffer2.SITWA_x_vC_W70_se05, InputTimeWindows.all7Min);
//run("SITWA_x_vC_W80_se05_8W", InputBuffer2.SITWA_x_vC_W80_se05, InputTimeWindows.all8Min);
//run("SITWA_x_vC_W90_se05_9W", InputBuffer2.SITWA_x_vC_W90_se05, InputTimeWindows.all9Min);
//run("SITWA_x_vC_W100_se05_10W", InputBuffer2.SITWA_x_vC_W100_se05, InputTimeWindows.all10Min);
//
//run("SITWA_vC_W00_se10_1W", InputBuffer.SITWA_vC_W00_se10, InputTimeWindows.all1Min);
//run("SITWA_vC_W10_se10_1W", InputBuffer.SITWA_vC_W10_se10, InputTimeWindows.all1Min);
//run("SITWA_vC_W20_se10_2W", InputBuffer.SITWA_vC_W20_se10, InputTimeWindows.all2Min);
//run("SITWA_vC_W30_se10_3W", InputBuffer.SITWA_vC_W30_se10, InputTimeWindows.all3Min);
//
//run("SITWA_x_vC_W00_se10_1W", InputBuffer2.SITWA_x_vC_W00_se10, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W10_se10_1W", InputBuffer2.SITWA_x_vC_W10_se10, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W20_se10_2W", InputBuffer2.SITWA_x_vC_W20_se10, InputTimeWindows.all2Min);
//run("SITWA_x_vC_W30_se10_3W", InputBuffer2.SITWA_x_vC_W30_se10, InputTimeWindows.all3Min);
//run("SITWA_x_vC_W40_se10_4W", InputBuffer2.SITWA_x_vC_W40_se10, InputTimeWindows.all4Min);
//run("SITWA_x_vC_W50_se10_5W", InputBuffer2.SITWA_x_vC_W50_se10, InputTimeWindows.all5Min);
//run("SITWA_x_vC_W60_se10_6W", InputBuffer2.SITWA_x_vC_W60_se10, InputTimeWindows.all6Min);
//run("SITWA_x_vC_W70_se10_7W", InputBuffer2.SITWA_x_vC_W70_se10, InputTimeWindows.all7Min);
//run("SITWA_x_vC_W80_se10_8W", InputBuffer2.SITWA_x_vC_W80_se10, InputTimeWindows.all8Min);
//run("SITWA_x_vC_W90_se10_9W", InputBuffer2.SITWA_x_vC_W90_se10, InputTimeWindows.all9Min);
//run("SITWA_x_vC_W100_se10_10W", InputBuffer2.SITWA_x_vC_W100_se10, InputTimeWindows.all10Min);
//
//run("SITWA_vC_W00_se15_1W", InputBuffer.SITWA_vC_W00_se15, InputTimeWindows.all1Min);
//run("SITWA_vC_W10_se15_1W", InputBuffer.SITWA_vC_W10_se15, InputTimeWindows.all1Min);
//run("SITWA_vC_W20_se15_2W", InputBuffer.SITWA_vC_W20_se15, InputTimeWindows.all2Min);
//run("SITWA_vC_W30_se15_3W", InputBuffer.SITWA_vC_W30_se15, InputTimeWindows.all3Min);
//
//run("SITWA_x_vC_W00_se15_1W", InputBuffer2.SITWA_x_vC_W00_se15, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W10_se15_1W", InputBuffer2.SITWA_x_vC_W10_se15, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W20_se15_2W", InputBuffer2.SITWA_x_vC_W20_se15, InputTimeWindows.all2Min);
//run("SITWA_x_vC_W30_se15_3W", InputBuffer2.SITWA_x_vC_W30_se15, InputTimeWindows.all3Min);
//run("SITWA_x_vC_W40_se15_4W", InputBuffer2.SITWA_x_vC_W40_se15, InputTimeWindows.all4Min);
//run("SITWA_x_vC_W50_se15_5W", InputBuffer2.SITWA_x_vC_W50_se15, InputTimeWindows.all5Min);
//run("SITWA_x_vC_W60_se15_6W", InputBuffer2.SITWA_x_vC_W60_se15, InputTimeWindows.all6Min);
//run("SITWA_x_vC_W70_se15_7W", InputBuffer2.SITWA_x_vC_W70_se15, InputTimeWindows.all7Min);
//run("SITWA_x_vC_W80_se15_8W", InputBuffer2.SITWA_x_vC_W80_se15, InputTimeWindows.all8Min);
//run("SITWA_x_vC_W90_se15_9W", InputBuffer2.SITWA_x_vC_W90_se15, InputTimeWindows.all9Min);
//run("SITWA_x_vC_W100_se15_10W", InputBuffer2.SITWA_x_vC_W100_se15, InputTimeWindows.all10Min);
//
//run("SITWA_vC_W00_se20_1W", InputBuffer.SITWA_vC_W00_se20, InputTimeWindows.all1Min);
//run("SITWA_vC_W10_se20_1W", InputBuffer.SITWA_vC_W10_se20, InputTimeWindows.all1Min);
//run("SITWA_vC_W20_se20_2W", InputBuffer.SITWA_vC_W20_se20, InputTimeWindows.all2Min);
//run("SITWA_vC_W30_se20_3W", InputBuffer.SITWA_vC_W30_se20, InputTimeWindows.all3Min);
//
//run("SITWA_x_vC_W00_se20_1W", InputBuffer2.SITWA_x_vC_W00_se20, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W10_se20_1W", InputBuffer2.SITWA_x_vC_W10_se20, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W20_se20_2W", InputBuffer2.SITWA_x_vC_W20_se20, InputTimeWindows.all2Min);
//run("SITWA_x_vC_W30_se20_3W", InputBuffer2.SITWA_x_vC_W30_se20, InputTimeWindows.all3Min);
//run("SITWA_x_vC_W40_se20_4W", InputBuffer2.SITWA_x_vC_W40_se20, InputTimeWindows.all4Min);
//run("SITWA_x_vC_W50_se20_5W", InputBuffer2.SITWA_x_vC_W50_se20, InputTimeWindows.all5Min);
//run("SITWA_x_vC_W60_se20_6W", InputBuffer2.SITWA_x_vC_W60_se20, InputTimeWindows.all6Min);
//run("SITWA_x_vC_W70_se20_7W", InputBuffer2.SITWA_x_vC_W70_se20, InputTimeWindows.all7Min);
//run("SITWA_x_vC_W80_se20_8W", InputBuffer2.SITWA_x_vC_W80_se20, InputTimeWindows.all8Min);
//run("SITWA_x_vC_W90_se20_9W", InputBuffer2.SITWA_x_vC_W90_se20, InputTimeWindows.all9Min);
//run("SITWA_x_vC_W100_se20_10W", InputBuffer2.SITWA_x_vC_W100_se20, InputTimeWindows.all10Min);
//
//run("SITWA_vC_W00_se25_1W", InputBuffer.SITWA_vC_W00_se25, InputTimeWindows.all1Min);
//run("SITWA_vC_W10_se25_1W", InputBuffer.SITWA_vC_W10_se25, InputTimeWindows.all1Min);
//run("SITWA_vC_W20_se25_2W", InputBuffer.SITWA_vC_W20_se25, InputTimeWindows.all2Min);
//run("SITWA_vC_W30_se25_3W", InputBuffer.SITWA_vC_W30_se25, InputTimeWindows.all3Min);
//
//run("SITWA_x_vC_W00_se25_1W", InputBuffer2.SITWA_x_vC_W00_se25, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W10_se25_1W", InputBuffer2.SITWA_x_vC_W10_se25, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W20_se25_2W", InputBuffer2.SITWA_x_vC_W20_se25, InputTimeWindows.all2Min);
//run("SITWA_x_vC_W30_se25_3W", InputBuffer2.SITWA_x_vC_W30_se25, InputTimeWindows.all3Min);
//run("SITWA_x_vC_W40_se25_4W", InputBuffer2.SITWA_x_vC_W40_se25, InputTimeWindows.all4Min);
//run("SITWA_x_vC_W50_se25_5W", InputBuffer2.SITWA_x_vC_W50_se25, InputTimeWindows.all5Min);
//run("SITWA_x_vC_W60_se25_6W", InputBuffer2.SITWA_x_vC_W60_se25, InputTimeWindows.all6Min);
//run("SITWA_x_vC_W70_se25_7W", InputBuffer2.SITWA_x_vC_W70_se25, InputTimeWindows.all7Min);
//run("SITWA_x_vC_W80_se25_8W", InputBuffer2.SITWA_x_vC_W80_se25, InputTimeWindows.all8Min);
//run("SITWA_x_vC_W90_se25_9W", InputBuffer2.SITWA_x_vC_W90_se25, InputTimeWindows.all9Min);
//run("SITWA_x_vC_W100_se25_10W", InputBuffer2.SITWA_x_vC_W100_se25, InputTimeWindows.all10Min);
//
//run("SITWA_vC_W00_se30_1W", InputBuffer.SITWA_vC_W00_se30, InputTimeWindows.all1Min);
//run("SITWA_vC_W10_se30_1W", InputBuffer.SITWA_vC_W10_se30, InputTimeWindows.all1Min);
//run("SITWA_vC_W20_se30_2W", InputBuffer.SITWA_vC_W20_se30, InputTimeWindows.all2Min);
//run("SITWA_vC_W30_se30_3W", InputBuffer.SITWA_vC_W30_se30, InputTimeWindows.all3Min);
//
//run("SITWA_x_vC_W00_se30_1W", InputBuffer2.SITWA_x_vC_W00_se30, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W10_se30_1W", InputBuffer2.SITWA_x_vC_W10_se30, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W20_se30_2W", InputBuffer2.SITWA_x_vC_W20_se30, InputTimeWindows.all2Min);
//run("SITWA_x_vC_W30_se30_3W", InputBuffer2.SITWA_x_vC_W30_se30, InputTimeWindows.all3Min);
//run("SITWA_x_vC_W40_se30_4W", InputBuffer2.SITWA_x_vC_W40_se30, InputTimeWindows.all4Min);
//run("SITWA_x_vC_W50_se30_5W", InputBuffer2.SITWA_x_vC_W50_se30, InputTimeWindows.all5Min);
//run("SITWA_x_vC_W60_se30_6W", InputBuffer2.SITWA_x_vC_W60_se30, InputTimeWindows.all6Min);
//run("SITWA_x_vC_W70_se30_7W", InputBuffer2.SITWA_x_vC_W70_se30, InputTimeWindows.all7Min);
//run("SITWA_x_vC_W80_se30_8W", InputBuffer2.SITWA_x_vC_W80_se30, InputTimeWindows.all8Min);
//run("SITWA_x_vC_W90_se30_9W", InputBuffer2.SITWA_x_vC_W90_se30, InputTimeWindows.all9Min);
//
//run("SITWA_vC_W00_se35_1W", InputBuffer.SITWA_vC_W00_se35, InputTimeWindows.all1Min);
//run("SITWA_vC_W10_se35_1W", InputBuffer.SITWA_vC_W10_se35, InputTimeWindows.all1Min);
//run("SITWA_vC_W20_se35_2W", InputBuffer.SITWA_vC_W20_se35, InputTimeWindows.all2Min);
//
//run("SITWA_x_vC_W00_se35_1W", InputBuffer2.SITWA_x_vC_W00_se35, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W10_se35_1W", InputBuffer2.SITWA_x_vC_W10_se35, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W20_se35_2W", InputBuffer2.SITWA_x_vC_W20_se35, InputTimeWindows.all2Min);
//run("SITWA_x_vC_W30_se35_3W", InputBuffer2.SITWA_x_vC_W30_se35, InputTimeWindows.all3Min);
//run("SITWA_x_vC_W40_se35_4W", InputBuffer2.SITWA_x_vC_W40_se35, InputTimeWindows.all4Min);
//run("SITWA_x_vC_W50_se35_5W", InputBuffer2.SITWA_x_vC_W50_se35, InputTimeWindows.all5Min);
//run("SITWA_x_vC_W60_se35_6W", InputBuffer2.SITWA_x_vC_W60_se35, InputTimeWindows.all6Min);
//
//run("SITWA_vC_W00_se40_1W", InputBuffer.SITWA_vC_W00_se40, InputTimeWindows.all1Min);
//run("SITWA_vC_W10_se40_1W", InputBuffer.SITWA_vC_W10_se40, InputTimeWindows.all1Min);
//run("SITWA_vC_W20_se40_2W", InputBuffer.SITWA_vC_W20_se40, InputTimeWindows.all2Min);
//run("SITWA_vC_W30_se40_3W", InputBuffer.SITWA_vC_W30_se40, InputTimeWindows.all3Min);
//
//run("SITWA_x_vC_W00_se40_1W", InputBuffer2.SITWA_x_vC_W00_se40, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W10_se40_1W", InputBuffer2.SITWA_x_vC_W10_se40, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W20_se40_2W", InputBuffer2.SITWA_x_vC_W20_se40, InputTimeWindows.all2Min);
//run("SITWA_x_vC_W30_se40_3W", InputBuffer2.SITWA_x_vC_W30_se40, InputTimeWindows.all3Min);
//run("SITWA_x_vC_W40_se40_4W", InputBuffer2.SITWA_x_vC_W40_se40, InputTimeWindows.all4Min);
//run("SITWA_x_vC_W50_se40_5W", InputBuffer2.SITWA_x_vC_W50_se40, InputTimeWindows.all5Min);
//run("SITWA_x_vC_W60_se40_6W", InputBuffer2.SITWA_x_vC_W60_se40, InputTimeWindows.all6Min);
//run("SITWA_x_vC_W70_se40_7W", InputBuffer2.SITWA_x_vC_W70_se40, InputTimeWindows.all7Min);
//run("SITWA_x_vC_W80_se40_8W", InputBuffer2.SITWA_x_vC_W80_se40, InputTimeWindows.all8Min);
//run("SITWA_x_vC_W90_se40_9W", InputBuffer2.SITWA_x_vC_W90_se40, InputTimeWindows.all9Min);
//run("SITWA_x_vC_W100_se40_10W", InputBuffer2.SITWA_x_vC_W100_se40, InputTimeWindows.all10Min);
//
//run("SITWA_vC_W00_se45_1W", InputBuffer.SITWA_vC_W00_se45, InputTimeWindows.all1Min);
//run("SITWA_vC_W10_se45_1W", InputBuffer.SITWA_vC_W10_se45, InputTimeWindows.all1Min);
//run("SITWA_vC_W20_se45_2W", InputBuffer.SITWA_vC_W20_se45, InputTimeWindows.all2Min);
//run("SITWA_vC_W30_se45_3W", InputBuffer.SITWA_vC_W30_se45, InputTimeWindows.all3Min);
//
//run("SITWA_x_vC_W00_se45_1W", InputBuffer2.SITWA_x_vC_W00_se45, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W10_se45_1W", InputBuffer2.SITWA_x_vC_W10_se45, InputTimeWindows.all1Min);
//run("SITWA_x_vC_W20_se45_2W", InputBuffer2.SITWA_x_vC_W20_se45, InputTimeWindows.all2Min);
//run("SITWA_x_vC_W30_se45_3W", InputBuffer2.SITWA_x_vC_W30_se45, InputTimeWindows.all3Min);
//run("SITWA_x_vC_W40_se45_4W", InputBuffer2.SITWA_x_vC_W40_se45, InputTimeWindows.all4Min);
//run("SITWA_x_vC_W50_se45_5W", InputBuffer2.SITWA_x_vC_W50_se45, InputTimeWindows.all5Min);
//run("SITWA_x_vC_W60_se45_6W", InputBuffer2.SITWA_x_vC_W60_se45, InputTimeWindows.all6Min);
//run("SITWA_x_vC_W70_se45_7W", InputBuffer2.SITWA_x_vC_W70_se45, InputTimeWindows.all7Min);
//run("SITWA_x_vC_W80_se45_8W", InputBuffer2.SITWA_x_vC_W80_se45, InputTimeWindows.all8Min);
//run("SITWA_x_vC_W90_se45_9W", InputBuffer2.SITWA_x_vC_W90_se45, InputTimeWindows.all9Min);
//run("SITWA_x_vC_W100_se45_10W", InputBuffer2.SITWA_x_vC_W100_se45, InputTimeWindows.all10Min);
//
//run("BASE_1W", InputBuffer.bufferNone, InputTimeWindows.all1Min);
//run("BASE_2W", InputBuffer.bufferNone, InputTimeWindows.all2Min);
//run("BASE_3W", InputBuffer.bufferNone, InputTimeWindows.all3Min);
//run("BASE_4W", InputBuffer.bufferNone, InputTimeWindows.all4Min);
//run("BASE_5W", InputBuffer.bufferNone, InputTimeWindows.all5Min);
//run("BASE_6W", InputBuffer.bufferNone, InputTimeWindows.all6Min);
//run("BASE_7W", InputBuffer.bufferNone, InputTimeWindows.all7Min);
//run("BASE_8W", InputBuffer.bufferNone, InputTimeWindows.all8Min);
//run("BASE_9W", InputBuffer.bufferNone, InputTimeWindows.all9Min);
//run("BASE_10W", InputBuffer.bufferNone, InputTimeWindows.all10Min);
//
//run("SD_1W", InputBuffer.bufferNewSD, InputTimeWindows.all1Min);
//run("SD_2W", InputBuffer.bufferNewSD, InputTimeWindows.all2Min);
//run("SD_3W", InputBuffer.bufferNewSD, InputTimeWindows.all3Min);
//run("SD_4W", InputBuffer.bufferNewSD, InputTimeWindows.all4Min);
//run("SD_5W", InputBuffer.bufferNewSD, InputTimeWindows.all5Min);
//run("SD_6W", InputBuffer.bufferNewSD, InputTimeWindows.all6Min);
//run("SD_7W", InputBuffer.bufferNewSD, InputTimeWindows.all7Min);
//run("SD_8W", InputBuffer.bufferNewSD, InputTimeWindows.all8Min);
//run("SD_9W", InputBuffer.bufferNewSD, InputTimeWindows.all9Min);
//run("SD_10W", InputBuffer.bufferNewSD, InputTimeWindows.all10Min);
