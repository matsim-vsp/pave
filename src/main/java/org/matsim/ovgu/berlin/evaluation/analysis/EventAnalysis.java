package org.matsim.ovgu.berlin.evaluation.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.evaluation.EvBufferSetup;
import org.matsim.ovgu.berlin.evaluation.EvBufferVariant;
import org.matsim.ovgu.berlin.evaluation.EvTour;
import org.matsim.ovgu.berlin.eventHandling.Summary;
import org.matsim.ovgu.berlin.eventHandling.TourEventsHandler;

public class EventAnalysis {

	public static void run(EvTour tour, String timeWindowMethod) {
		evaluate(tour, timeWindowMethod);

	}

	private static void evaluate(EvTour tour, String timeWindowMethod) {

		for (EvBufferVariant variant : tour.evBufferVariants) {
			for (EvBufferSetup buffer : variant.buffers) {
				buffer.readRunSettings();
				Settings settings = buffer.runSettings;
				settings.directory += timeWindowMethod + "/";
				settings.timeWindowMethod = timeWindowMethod;
				readEvents(settings, buffer.bufferIdent + "_" + timeWindowMethod + "_result");
			}
		}
		generateTourAndVersionSummaries(tour, timeWindowMethod);
		generateTourAndVersionSummaries_Groups(tour, timeWindowMethod);
	}

	private static void readEvents(Settings settings, String fileName) {
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

	private static void writeBufferSummaryCSV(Summary summary, String summaryFilePath, double[] usedBuffer) {

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

	private static void writeBufferSummaryTWGroupsCSV(Summary summary, String summaryFilePath, double[] usedBuffer) {

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

	public static String getSummaryHeadline() {
		String str = "evaluation;tour;version;window;myMethod;" + "oBeforeTW;oInTW;oAfterTW;"
				+ "oAvgEarlyTW;oMaxEarlyTW;oAvgLateTW;oMaxLateTW;" + "dBeforeTW;dInTW;dAfterTW;"
				+ "dAvgEarlyTW;dMaxEarlyTW;dAvgLateTW;dMaxLateTW;tourDuration";

		return str;
	}

	private static void generateTourAndVersionSummaries_Groups(EvTour tour, String timeWindowMethod) {

		List<String> summaryStrings = new ArrayList<String>();

		try {

			File csvFileTour = new File(
					tour.tourDirectory + "/" + tour.tourIdent + "_" + timeWindowMethod + "_summaryGroups.csv");
			csvFileTour.getParentFile().mkdirs();
			FileWriter csvWriterTour = new FileWriter(csvFileTour);
			csvWriterTour.append(getSummaryHeadline().replace("myMethod", "myMethod;windowGroups")
					+ ";difTourDurationToBASEmin;difTourDurationToBASEavg\n");
			for (EvBufferVariant variant : tour.evBufferVariants) {
				File csvFileVersion = new File(variant.versionDirectory + "/" + variant.versionIdent + "_"
						+ timeWindowMethod + "_summaryGroups.csv");
				csvFileVersion.getParentFile().mkdirs();
				FileWriter csvWriterVersion = new FileWriter(csvFileVersion);
				csvWriterVersion.append(getSummaryHeadline().replace("myMethod", "myMethod;windowGroups") + "\n");
				for (EvBufferSetup buffer : variant.buffers) {
					String bufferSummaryGroups = readBufferSummaryString_Groups(
							buffer.bufferIdent.replace("_", ";") + ";", buffer.bufferDirectory + "/"
									+ buffer.bufferIdent + "_" + timeWindowMethod + "_result_summaryGroups.csv");

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

	private static void generateTourAndVersionSummaries(EvTour tour, String timeWindowMethod) {

		List<String> summaryStrings = new ArrayList<String>();

		try {

			File csvFileTour = new File(
					tour.tourDirectory + "/" + tour.tourIdent + "_" + timeWindowMethod + "_summary.csv");
			csvFileTour.getParentFile().mkdirs();
			FileWriter csvWriterTour = new FileWriter(csvFileTour);
			csvWriterTour.append(getSummaryHeadline() + ";difTourDurationToBASEmin;difTourDurationToBASEavg\n");
			for (EvBufferVariant variant : tour.evBufferVariants) {
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

	private static String readBufferSummaryString(String file) throws IOException {

		BufferedReader csvReader = new BufferedReader(new FileReader(file));

		String firstRow = csvReader.readLine();
		String secondRow = csvReader.readLine();

		csvReader.close();
		return secondRow;
	}

	private static String readBufferSummaryString_Groups(String linePrefix, String file) throws IOException {

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

	private static List<String> extendRelativeTourDurations(List<String> summaryStrings) {
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
				extendedSummaryStrings.add(str.replace("\n", "").replace(".", ",") + "\n");
			}
		}
		return extendedSummaryStrings;
	}

	private static double findTourDuration(String contains, List<String> buffers) {
		for (String str : buffers)
			if (str.contains(contains)) {
				String[] split = str.split(";");
				return Double.parseDouble(split[split.length - 2].replace(",", "."));
			}
		return -1;
	}

}
