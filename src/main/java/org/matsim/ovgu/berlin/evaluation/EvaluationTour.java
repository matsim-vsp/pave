package org.matsim.ovgu.berlin.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.createTravelTimeMatrix.Version3_RunSimReadEvents;
import org.matsim.ovgu.berlin.eventHandling.Summary;
import org.matsim.ovgu.berlin.eventHandling.TourEventsHandler;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class EvaluationTour {

	public EvaluationTour(String evaluationDirectory, String tourIdent, String[] linkIDs) {
		this.tourIdent = tourIdent;
		this.linkIDs = linkIDs;
		this.tourDirectory = evaluationDirectory + "/" + tourIdent + "/";
	}

	protected String tourDirectory;
	protected String tourIdent;
	protected String[] linkIDs;

	private List<EvInputVersion> evInputVersions = new ArrayList<EvInputVersion>();

	private double[] minTravelTime;
	private double[] avgTravelTime;
	private double[] maxTravelTime;

	// linkID / hour --> travelTime
	private double[][] traveltimeMatrix;

	public void setup24hTravelTimes(boolean runSimulation) {
		traveltimeMatrix = Version3_RunSimReadEvents.run(linkIDs, tourDirectory + "/matrixData", runSimulation, false,
				true);
		calcAggTravelTimes();
		writeTravelTimesCSV();
	}

	private void writeTravelTimesCSV() {
		try {
			File csvFile = new File(tourDirectory + "/" + tourIdent + "_traveltimes.csv");
			csvFile.getParentFile().mkdirs();
			FileWriter csvWriter = new FileWriter(csvFile);
			csvWriter.append("from;to;;0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;;MIN;AVG;MAX\n");

			for (int i = 0; i < linkIDs.length - 1; i++) {
				String str = linkIDs[i] + ";" + linkIDs[i + 1] + ";;";
				for (double traveltime : traveltimeMatrix[i])
					str += traveltime + ";";
				str += ";" + minTravelTime[i] + ";" + avgTravelTime[i] + ";" + maxTravelTime[i];
				csvWriter.append(str.replace(".", ",") + "\n");
			}

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setupInputVersionsWithBuffers(boolean runModel) {
		initVersionA(runModel);
		initVersionB(runModel);
//		initVersionC(runModel);
		initVersionBASEmin();
		initVersionBASEavg();
		initVersionSD();
	}

	private void initVersionBASEmin() {
		EvInputVersion eiv = new EvInputVersion(tourDirectory, tourIdent + "_versionBASEmin", minTravelTime, linkIDs);
		eiv.setupTimeWindowBuffers(0, 0, false);
		eiv.writeCSVs();
		evInputVersions.add(eiv);

	}

	private void initVersionBASEavg() {
		EvInputVersion eiv = new EvInputVersion(tourDirectory, tourIdent + "_versionBASEavg", avgTravelTime, linkIDs);
		eiv.setupTimeWindowBuffers(0, 0, false);
		eiv.writeCSVs();
		evInputVersions.add(eiv);

	}

	private void initVersionSD() {
		EvInputVersion eiv = new EvInputVersion(tourDirectory, tourIdent + "_versionSD", avgTravelTime, linkIDs);
//		eiv.setupTimeWindowBuffers_SD();
		eiv.setupTimeWindowBuffers(0, 0, false);
		eiv.calculateSD_ForAllBuffers(avgTravelTime, traveltimeMatrix);
		eiv.writeCSVs();
		evInputVersions.add(eiv);
	}

	private void initVersionA(boolean runModel) {
		EvInputVersion eiv = new EvInputVersion(tourDirectory, tourIdent + "_versionA", minTravelTime, linkIDs);
		eiv.calcDelayScenarios(traveltimeMatrix);
		eiv.setupBuffers(runModel);
		eiv.writeCSVs();
		evInputVersions.add(eiv);
	}

	private void initVersionB(boolean runModel) {
		EvInputVersion eiv = new EvInputVersion(tourDirectory, tourIdent + "_versionB", avgTravelTime, linkIDs);
		eiv.calcDelayScenarios(traveltimeMatrix);
		eiv.removeNegativScenarioValues();
		eiv.setupBuffers(runModel);
		eiv.writeCSVs();
		evInputVersions.add(eiv);
	}

	private void initVersionC(boolean runModel) {
		EvInputVersion eiv = new EvInputVersion(tourDirectory, tourIdent + "_versionC", avgTravelTime, linkIDs);
		eiv.calcDelayScenarios(traveltimeMatrix);
		eiv.setupBuffers(runModel);
		eiv.writeCSVs();
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

	public void evaluate(String timeWindowMethod, boolean runSimulation) {

		for (EvInputVersion evi : evInputVersions) {
			for (EvBufferVersion buffer : evi.buffers) {
				buffer.readRunSettings();
				Settings settings = buffer.runSettings;
				settings.directory += timeWindowMethod + "/";
				settings.timeWindowMethod = timeWindowMethod;
				if (runSimulation)
					if (Files.notExists(Path.of(settings.directory)))
						new FreightOnlyMatsim(settings);
				readEvents(settings, buffer.bufferIdent + "_" + timeWindowMethod + "_result");
			}
		}

		generateTourAndVersionSummaries(timeWindowMethod);
	}

	private void readEvents(Settings settings, String fileName) {
		TourEventsHandler handler = new TourEventsHandler();
		EventsManager manager = EventsUtils.createEventsManager();
		manager.addHandler(handler);
		MatsimEventsReader reader = new MatsimEventsReader(manager);

		reader.readFile(settings.directory + "/output_events.xml.gz");
		System.out.println("Events file read!");

		handler.compareExpectedArrivals(settings);
		System.out.println("post calculations finished!");

		handler.writeCSV(settings.directory + "/../../" + fileName + ".csv");
		System.out.println("CSV finished!");

		writeBufferSummaryCSV(handler.getSummary(), settings.directory + "/../../" + fileName + "_summary.csv",
				settings.buffer);
	}

	private void writeBufferSummaryCSV(Summary summary, String summaryFilePath, double[] usedBuffer) {

		try {
			File csvFile = new File(summaryFilePath);
			csvFile.getParentFile().mkdirs();
			FileWriter csvWriter = new FileWriter(csvFile);
			csvWriter.append(getSummaryHeadline(usedBuffer.length).replace("bufferIdent;", ""));

			String str = summary.percent_oBeforeTW + ";" + summary.percent_oInTW + ";" + summary.percent_oAfterTW + ";"
					+ summary.avg_oEarlyTW / 60 + ";" + summary.max_oEarlyTW / 60 + ";" + summary.avg_oLateTW / 60 + ";"
					+ summary.max_oLateTW / 60 + ";" + summary.percent_dBeforeTW + ";" + summary.percent_dInTW + ";"
					+ summary.percent_dAfterTW + ";" + summary.avg_dEarlyTW / 60 + ";" + summary.max_dEarlyTW / 60 + ";"
					+ summary.avg_dLateTW / 60 + ";" + summary.max_dLateTW / 60 + ";"
					+ summary.avg_tourDuration / 60 / 1440 + ";";

			for (double value : usedBuffer)
				str += value + ";";

			str = str.replace(".", ",");

			csvWriter.append(str);

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected String getSummaryHeadline(int bufferCount) {
		String str = "bufferIdent;oBeforeTW;oInTW;oAfterTW;oEarlyTW;oMaxEarlyTW;oLateTW;oMaxLateTW;dBeforeTW;dInTW;dAfterTW;dEarlyTW;dMaxEarlyTW;dLateTW;dMaxTW;tourDuration;";

		for (int i = 0; i < bufferCount; i++)
			str += "buf" + i + ";";

		return str + "\n";
	}

	private void generateTourAndVersionSummaries(String timeWindowMethod) {

		try {

			File csvFileTour = new File(tourDirectory + "/" + tourIdent + "_" + timeWindowMethod + "_summary.csv");
			csvFileTour.getParentFile().mkdirs();
			FileWriter csvWriterTour = new FileWriter(csvFileTour);
			csvWriterTour.append(getSummaryHeadline(linkIDs.length));
			for (EvInputVersion evi : evInputVersions) {
				File csvFileVersion = new File(
						evi.versionDirectory + "/" + evi.versionIdent + "_" + timeWindowMethod + "_summary.csv");
				csvFileVersion.getParentFile().mkdirs();
				FileWriter csvWriterVersion = new FileWriter(csvFileVersion);
				csvWriterVersion.append(getSummaryHeadline(linkIDs.length));
				for (EvBufferVersion buffer : evi.buffers) {
					String bufferSummary = readBufferSummaryString(buffer.bufferDirectory + "/" + buffer.bufferIdent
							+ "_" + timeWindowMethod + "_result_summary.csv");

					csvWriterTour.append(buffer.bufferIdent + ";" + bufferSummary + "\n");
					csvWriterVersion.append(buffer.bufferIdent + ";" + bufferSummary + "\n");
				}

				csvWriterVersion.flush();
				csvWriterVersion.close();
			}

			csvWriterTour.flush();
			csvWriterTour.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private String readBufferSummaryString(String file) throws IOException {

		BufferedReader csvReader = new BufferedReader(new FileReader(file));

		String firstRow = csvReader.readLine();
		String secondRow = csvReader.readLine();

		csvReader.close();
		return secondRow;
	}

}
