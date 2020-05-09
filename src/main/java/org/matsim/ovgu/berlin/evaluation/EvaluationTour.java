package org.matsim.ovgu.berlin.evaluation;

import java.util.ArrayList;
import java.util.List;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.createTravelTimeMatrix.Version3_RunSimReadEvents;
import org.matsim.ovgu.berlin.eventHandling.TourEventsHandler;
import org.matsim.ovgu.berlin.input.InputExpectedTravelTime;
import org.matsim.ovgu.berlin.input.InputTimeWindows;
import org.matsim.ovgu.berlin.input.InputTour;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class EvaluationTour {

	public EvaluationTour(String[] linkIDs, String tourID) {
		this.linkIDs = linkIDs;
		this.tourID = tourID;
	}

	private String[] linkIDs;
	private String tourID;

	private List<EvInputVersion> evInputVersions = new ArrayList<EvInputVersion>();

	private double[] minTravelTime;
	private double[] avgTravelTime;
	private double[] maxTravelTime;

	// linkID / hour --> travelTime
	private double[][] traveltimeMatrix;

	public void setup24hTravelTimes() {
//		traveltimeMatrix = Version3_RunSimReadEvents.run(linkIDs, tourID, true, false, true);
		traveltimeMatrix = Version3_RunSimReadEvents.run(linkIDs, tourID, false, false, true);
	}

	public void setupInputVersionsWithBuffers() {
		calcAggTravelTimes();
		initVersionA();
//		initVersionB();
//		initVersionC();
//		initVersionSD();
	}

	private void initVersionSD() {
		EvInputVersion eiv = new EvInputVersion("SD", avgTravelTime);
		eiv.setupTimeWindowBuffers_SD();
		eiv.calculateSD_ForAllBuffers(avgTravelTime, traveltimeMatrix);
		evInputVersions.add(eiv);
	}

	private void initVersionA() {
		EvInputVersion eiv = new EvInputVersion("A", minTravelTime);
		eiv.calcDelayScenarios(traveltimeMatrix);
		runBufferModel(eiv);
	}

	private void initVersionB() {
		EvInputVersion eiv = new EvInputVersion("B", avgTravelTime);
		eiv.calcDelayScenarios(traveltimeMatrix);
		eiv.removeNegativScenarioValues();
		runBufferModel(eiv);
	}

	private void initVersionC() {
		EvInputVersion eiv = new EvInputVersion("C", avgTravelTime);
		eiv.calcDelayScenarios(traveltimeMatrix);
		runBufferModel(eiv);
	}

	private void runBufferModel(EvInputVersion eiv) {
		double se = eiv.getBestCaseDuration(2 * 60);
		double t = 500;
		// TODO: SETUP PARAMETERS FOR BUFFERS TO BE CHECKED
		eiv.setupTimeWindowBuffers(se, t, true);
//		eiv.setupTimeWindowBuffers(se, t, false);
		eiv.runLp_ForAllBuffers();
		evInputVersions.add(eiv);
	}

	private double[] calcAggTravelTimes() {
		minTravelTime = new double[traveltimeMatrix.length];
		avgTravelTime = new double[traveltimeMatrix.length];
		maxTravelTime = new double[traveltimeMatrix.length];

		for (int l = 0; l < traveltimeMatrix.length; l++) {
			double min = Double.MAX_VALUE;
			double max = 0;
			double sum = 0;
			for (int h = 0; h < traveltimeMatrix[l].length; h++) {
				if (traveltimeMatrix[l][h] < min)
					min = traveltimeMatrix[l][h];
				if (traveltimeMatrix[l][h] > max)
					max = traveltimeMatrix[l][h];
				sum += traveltimeMatrix[l][h];
			}
			double avg = sum / traveltimeMatrix[l].length;
			minTravelTime[l] = min;
			avgTravelTime[l] = avg;
			maxTravelTime[l] = max;
		}
		return avgTravelTime;
	}

	public void runEvaluations() {
		for (EvInputVersion evi : evInputVersions) {
			for (EvBufferVersion buffer : evi.buffers) {
				Settings settings = initSettings(evi, buffer);
				new FreightOnlyMatsim(settings);
			}
		}
//		Settings settings = new Settings();
		// directory incl. name
		// usedBUffer
		// timeWindows
//		settings.directory = baseDirectory + name + "/";
//		settings.buffer = usedBuffer;
//		settings.timeWindow = timeWindow;
//		settings.expectedTravelTime = InputExpectedTravelTime.newAvgTT;
//		settings.timeWindowMethod = "AfterArrival";
//		settings.expectedTravelTime = InputExpectedTravelTime.newMinTT;
//		settings.timeWindowMethod = "PlusMinusArrival";
//		settings.pathChangeEvents = "input/networkChangeEventsFromKai.xml.gz";

//		new FreightOnlyMatsim(settings);
//		Version2_SITWA_Evaluation.run(settings, baseDirectory, name, usedBuffer, timeWindow, runMATSim,
//				generateResultsCSV);
//		Version2_SITWA_Evaluation.run(name + "_W10_1W", buffers[0], InputTimeWindows.all1Min);
//		run(name + "_W20_2W", buffers[1], InputTimeWindows.all2Min);
//		run(name + "_W30_3W", buffers[2], InputTimeWindows.all3Min);
//		run(name + "_W40_4W", buffers[3], InputTimeWindows.all4Min);
//		run(name + "_W50_5W", buffers[4], InputTimeWindows.all5Min);
//		run(name + "_W60_6W", buffers[5], InputTimeWindows.all6Min);
//		run(name + "_W70_7W", buffers[6], InputTimeWindows.all7Min);
//		run(name + "_W80_8W", buffers[7], InputTimeWindows.all8Min);
//		run(name + "_W90_9W", buffers[8], InputTimeWindows.all9Min);
//		run(name + "_W100_10W", buffers[9], InputTimeWindows.all10Min);
	}

	public void readEvents() {
		for (EvInputVersion evi : evInputVersions) {
			for (EvBufferVersion buffer : evi.buffers) {

				Settings settings = initSettings(evi, buffer);
				TourEventsHandler handler = new TourEventsHandler();
				EventsManager manager = EventsUtils.createEventsManager();
				manager.addHandler(handler);
				MatsimEventsReader reader = new MatsimEventsReader(manager);

				reader.readFile(settings.directory + "/output_events.xml.gz");
				System.out.println("Events file read!");

				handler.compareExpectedArrivals(settings);
				System.out.println("post calculations finished!");

				handler.printCSV(settings.directory + "_result.csv");
				System.out.println("CSV finished!");

//				appendSummary(handler.getSummary(), name, settings.buffer);

			}
		}

	}

	private Settings initSettings(EvInputVersion evi, EvBufferVersion buffer) {
		Settings settings = new Settings();
		settings.tour = linkIDs;
		settings.depot = linkIDs[0];
		settings.directory += "/evaluationX2/" + evi.versionName + "/" + buffer.bufferName + "/";
		settings.buffer = buffer.bufferValues;
		settings.serviceTime = buffer.u;
		settings.expectedTravelTime = getExpextedTTwithPseudoDepot(buffer.expTT);
		settings.timeWindow = generateEqualWindows(buffer.w, buffer.expTT.length);
		return settings;
		//set servicetime
//		settings.timeWindowMethod = "AfterArrival";
//		settings.timeWindowMethod = "PlusMinusArrival";
	}

	private double[] getExpextedTTwithPseudoDepot(double[] expTT) {
		double[] newExpTT = new double[expTT.length + 1];
		for (int i = 0; i < expTT.length; i++)
			newExpTT[i + 1] = expTT[i];
		return newExpTT;
	}

	private double[] generateEqualWindows(double window, int length) {
		double[] windows = new double[length];
		for (int i = 0; i < windows.length; i++)
			windows[i] = window;
		return windows;
	}

}
