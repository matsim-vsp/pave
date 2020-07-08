package org.matsim.ovgu.berlin.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.createTravelTimeMatrix.Version3_RunSimReadEvents;
import org.matsim.ovgu.berlin.eventHandling.Summary;
import org.matsim.ovgu.berlin.eventHandling.TourEventsHandler;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class EvTour {

	public EvTour(String evaluationDirectory, String tourIdent, String[] linkIDs) {
		this.tourIdent = tourIdent;
		this.linkIDs = linkIDs;
		this.tourDirectory = evaluationDirectory + "/" + tourIdent + "/";
	}

	protected String tourDirectory;
	protected String tourIdent;
	protected String[] linkIDs;

	private List<EvBufferVariant> evBufferVariants = new ArrayList<EvBufferVariant>();

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

	public void setupBuffersForVariants(boolean runModel) {
		initLPmin(runModel);
//		initLPavg(runModel);
////		initVersionC(runModel);
//		initBASEavg(runModel);
//		initBASEmin(runModel);
//		initSDavg(runModel);
	}

	private void initBASEmin(boolean runModel) {
		EvBufferVariant baseMin = new EvBufferVariant(tourDirectory, tourIdent + "_BASEmin", minTravelTime, linkIDs);
		baseMin.setupBASEBuffers(runModel);
		evBufferVariants.add(baseMin);
	}

	private void initBASEavg(boolean runModel) {
		EvBufferVariant baseAvg = new EvBufferVariant(tourDirectory, tourIdent + "_BASEavg", avgTravelTime, linkIDs);
		baseAvg.setupBASEBuffers(runModel);
		evBufferVariants.add(baseAvg);
	}

	private void initSDavg(boolean runModel) {
		EvBufferVariant sdAvg = new EvBufferVariant(tourDirectory, tourIdent + "_SDavg", avgTravelTime, linkIDs);
		sdAvg.setupSDBuffers(avgTravelTime, traveltimeMatrix, runModel);
		evBufferVariants.add(sdAvg);
	}

	private void initLPmin(boolean runModel) {
		EvBufferVariant lpMin = new EvBufferVariant(tourDirectory, tourIdent + "_LPmin", minTravelTime, linkIDs);
		lpMin.calcDelayScenarios(traveltimeMatrix);
		lpMin.setupLPBuffers(runModel);
		evBufferVariants.add(lpMin);
	}

	private void initLPavg(boolean runModel) {
		EvBufferVariant lpAvg = new EvBufferVariant(tourDirectory, tourIdent + "_LPavg", avgTravelTime, linkIDs);
		lpAvg.calcDelayScenarios(traveltimeMatrix);
		lpAvg.removeNegativScenarioValues();
		lpAvg.setupLPBuffers(runModel);
		evBufferVariants.add(lpAvg);
	}

//	private void initVersionC(boolean runModel) {
//		EvBufferVariant vC = new EvBufferVariant(tourDirectory, tourIdent + "_versionC", avgTravelTime, linkIDs);
//		vC.calcDelayScenarios(traveltimeMatrix);
//		vC.setupSITWABuffers(runModel);
//		evBufferVariants.add(vC);
//	}

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

		for (EvBufferVariant variant : evBufferVariants) {
			for (EvBufferSetup buffer : variant.buffers) {
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
		generateTourAndVersionSummaries_Groups(timeWindowMethod);
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

		writeBufferSummaryTWGroupsCSV(handler.getSummary(),
				settings.directory + "/../../" + fileName + "_summaryGroups.csv", settings.buffer);
	}

	private void writeBufferSummaryCSV(Summary summary, String summaryFilePath, double[] usedBuffer) {

		try {
			File csvFile = new File(summaryFilePath);
			csvFile.getParentFile().mkdirs();
			FileWriter csvWriter = new FileWriter(csvFile);
			csvWriter.append(getSummaryHeadline().replace("evaluation;tour;version;window;myMethod;", "") + "\n");

			String str = summary.percent_oBeforeTW_all + ";" + summary.percent_oInTW_all + ";"
					+ summary.percent_oAfterTW_all + ";" + summary.avg_oEarlyTW_all / 60 + ";"
					+ summary.max_oEarlyTW_all / 60 + ";" + summary.avg_oLateTW_all / 60 + ";"
					+ summary.max_oLateTW_all / 60 + ";" + summary.percent_dBeforeTW_all + ";"
					+ summary.percent_dInTW_all + ";" + summary.percent_dAfterTW_all + ";"
					+ summary.avg_dEarlyTW_all / 60 + ";" + summary.max_dEarlyTW_all / 60 + ";"
					+ summary.avg_dLateTW_all / 60 + ";" + summary.max_dLateTW_all / 60 + ";"
					+ summary.avg_tourDuration_all / 60 / 1440 + ";\n";

			str = str.replace(".", ",");

			csvWriter.append(str);

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeBufferSummaryTWGroupsCSV(Summary summary, String summaryFilePath, double[] usedBuffer) {

		try {
			File csvFile = new File(summaryFilePath);
			csvFile.getParentFile().mkdirs();
			FileWriter csvWriter = new FileWriter(csvFile);
			csvWriter.append("windowGroups;"
					+ getSummaryHeadline().replace("evaluation;tour;version;window;myMethod;", "") + "\n");

			String str = "";

			Iterator<Entry<Double, Integer>> iter = summary.timewindow.entrySet().iterator();

			while (iter.hasNext()) {
				Entry<Double, Integer> entry = iter.next();
				int i = entry.getValue();

				str += entry.getKey() + ";" + summary.percent_oBeforeTW_groups[i] + ";"
						+ summary.percent_oInTW_groups[i] + ";" + summary.percent_oAfterTW_groups[i] + ";"
						+ summary.avg_oEarlyTW_groups[i] / 60 + ";" + summary.max_oEarlyTW_groups[i] / 60 + ";"
						+ summary.avg_oLateTW_groups[i] / 60 + ";" + summary.max_oLateTW_groups[i] / 60 + ";"
						+ summary.percent_dBeforeTW_groups[i] + ";" + summary.percent_dInTW_groups[i] + ";"
						+ summary.percent_dAfterTW_groups[i] + ";" + summary.avg_dEarlyTW_groups[i] / 60 + ";"
						+ summary.max_dEarlyTW_groups[i] / 60 + ";" + summary.avg_dLateTW_groups[i] / 60 + ";"
						+ summary.max_dLateTW_groups[i] / 60 + ";" + summary.avg_tourDuration_all / 60 / 1440 + ";\n";
			}

			str = str.replace(".", ",");

			csvWriter.append(str);

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected String getSummaryHeadline() {
		String str = "evaluation;tour;version;window;myMethod;" + "oBeforeTW;oInTW;oAfterTW;"
				+ "oAvgEarlyTW;oMaxEarlyTW;oAvgLateTW;oMaxLateTW;" + "dBeforeTW;dInTW;dAfterTW;"
				+ "dAvgEarlyTW;dMaxEarlyTW;dAvgLateTW;dMaxLateTW;tourDuration";

		return str;
	}

	private void generateTourAndVersionSummaries_Groups(String timeWindowMethod) {

		List<String> summaryStrings = new ArrayList<String>();

		try {

			File csvFileTour = new File(
					tourDirectory + "/" + tourIdent + "_" + timeWindowMethod + "_summaryGroups.csv");
			csvFileTour.getParentFile().mkdirs();
			FileWriter csvWriterTour = new FileWriter(csvFileTour);
			csvWriterTour.append(getSummaryHeadline().replace("myMethod", "myMethod;windowGroups") + ";difTourDurationToBASEmin;difTourDurationToBASEavg\n");
			for (EvBufferVariant variant : evBufferVariants) {
				File csvFileVersion = new File(variant.versionDirectory + "/" + variant.versionIdent + "_"
						+ timeWindowMethod + "_summaryGroups.csv");
				csvFileVersion.getParentFile().mkdirs();
				FileWriter csvWriterVersion = new FileWriter(csvFileVersion);
				csvWriterVersion.append(getSummaryHeadline().replace("myMethod", "myMethod;windowGroups") + "\n");
				for (EvBufferSetup buffer : variant.buffers) {
					String bufferSummaryGroups = readBufferSummaryString_Groups(buffer.bufferIdent.replace("_", ";") + ";",
							buffer.bufferDirectory + "/" + buffer.bufferIdent + "_" + timeWindowMethod
									+ "_result_summaryGroups.csv");

					summaryStrings.add(bufferSummaryGroups);
					csvWriterVersion.append(bufferSummaryGroups);
				}

				csvWriterVersion.flush();
				csvWriterVersion.close();
			}

			for (String str : summaryStrings)
				csvWriterTour.append(str);

			csvWriterTour.flush();
			csvWriterTour.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void generateTourAndVersionSummaries(String timeWindowMethod) {

		List<String> summaryStrings = new ArrayList<String>();

		try {

			File csvFileTour = new File(tourDirectory + "/" + tourIdent + "_" + timeWindowMethod + "_summary.csv");
			csvFileTour.getParentFile().mkdirs();
			FileWriter csvWriterTour = new FileWriter(csvFileTour);
			csvWriterTour.append(getSummaryHeadline() + ";difTourDurationToBASEmin;difTourDurationToBASEavg\n");
			for (EvBufferVariant variant : evBufferVariants) {
				File csvFileVersion = new File(variant.versionDirectory + "/" + variant.versionIdent + "_"
						+ timeWindowMethod + "_summary.csv");
				csvFileVersion.getParentFile().mkdirs();
				FileWriter csvWriterVersion = new FileWriter(csvFileVersion);
				csvWriterVersion.append(getSummaryHeadline() + "\n");
				for (EvBufferSetup buffer : variant.buffers) {
					String bufferSummary = readBufferSummaryString(buffer.bufferDirectory + "/" + buffer.bufferIdent
							+ "_" + timeWindowMethod + "_result_summary.csv");

					summaryStrings.add(buffer.bufferIdent.replace("_", ";") + ";" + bufferSummary + "\n");
					csvWriterVersion.append(buffer.bufferIdent.replace("_", ";") + ";" + bufferSummary + "\n");
				}

				csvWriterVersion.flush();
				csvWriterVersion.close();
			}

			List<String> extendedSummaryStrings = extendRelativeTourDurations(summaryStrings);

			for (String str : extendedSummaryStrings)
				csvWriterTour.append(str);

			csvWriterTour.flush();
			csvWriterTour.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<String> extendRelativeTourDurations(List<String> summaryStrings) {
		List<String> extendedSummaryStrings = new ArrayList<String>();

		// filter windows
		List<List<String>> listEachWindow = new ArrayList<List<String>>();
		HashMap<String, Integer> myMap = new HashMap<String, Integer>();

		for (int i = 0; i < summaryStrings.size(); i++) {
			String window = summaryStrings.get(i).split(";")[3];
			if (!myMap.containsKey(window)) {
				myMap.put(window, listEachWindow.size());
				listEachWindow.add(new ArrayList<String>());
			}
			listEachWindow.get(myMap.get(window)).add(summaryStrings.get(i));
		}

		for (int tw = 0; tw < listEachWindow.size(); tw++) {
			double minBASEduration = findTourDuration("BASEmin", listEachWindow.get(tw));
			double avgBASEduration = findTourDuration("BASEavg", listEachWindow.get(tw));

			for (int b = 0; b < listEachWindow.get(tw).size(); b++) {
				String str = listEachWindow.get(tw).get(b);
				String[] split = str.split(";");
				double tourDuration = Double.parseDouble(split[split.length - 2].replace(",", "."));
				str += ((tourDuration / minBASEduration) - 1);
				str += ";" + ((tourDuration / avgBASEduration) - 1);
				extendedSummaryStrings.add(str.replace("\n", "") + "\n");
			}
		}
		return extendedSummaryStrings;
	}

	private double findTourDuration(String contains, List<String> buffers) {
		for (String str : buffers)
			if (str.contains(contains)) {
				String[] split = str.split(";");
				return Double.parseDouble(split[split.length - 2].replace(",", "."));
			}
		return -1;
	}

	private String readBufferSummaryString(String file) throws IOException {

		BufferedReader csvReader = new BufferedReader(new FileReader(file));

		String firstRow = csvReader.readLine();
		String secondRow = csvReader.readLine();

		csvReader.close();
		return secondRow;
	}

	private String readBufferSummaryString_Groups(String linePrefix, String file) throws IOException {

		BufferedReader csvReader = new BufferedReader(new FileReader(file));

		String firstRow = csvReader.readLine();

		String content = "";
		String line;

		while ((line = csvReader.readLine()) != null) {
			content += linePrefix + line + "\n";
		}

		csvReader.close();
		return content;
	}

	public double getNoDelayDuration(double serviceTime) {
		double sum = 0;
		for (int i = 0; i < minTravelTime.length; i++)
			sum += minTravelTime[i] + serviceTime;
		return sum;
	}
}
