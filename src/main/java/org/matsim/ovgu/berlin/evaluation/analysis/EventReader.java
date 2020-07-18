package org.matsim.ovgu.berlin.evaluation.analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.eventHandling.FilteredSummaries;
import org.matsim.ovgu.berlin.eventHandling.Summary;
import org.matsim.ovgu.berlin.eventHandling.Summary2;
import org.matsim.ovgu.berlin.eventHandling.TourEventsHandler;

public class EventReader {

	protected static void read(Settings settings, String fileName) {
		TourEventsHandler handler = new TourEventsHandler();
		EventsManager manager = EventsUtils.createEventsManager();
		manager.addHandler(handler);
		MatsimEventsReader reader = new MatsimEventsReader(manager);

		reader.readFile(settings.directory + "/output_events.xml.gz");
		System.out.println("Events file read!");

		handler.compareExpectedArrivals(settings);
		System.out.println("post calculations finished!");

		String file = settings.directory + "/../../" + fileName;

		handler.writeCSV(file + ".csv");
		writeBufferSummaryCSV(handler.getSummary(), file + "_summary.csv");
		writeBufferSummaryTWGroupsCSV(handler.getSummary(), file + "_summaryGroups.csv");
		writeSummaries(handler.getFilteredSummaries(), file);

		System.out.println("CSV finished!");
	}

	private static void writeSummaries(FilteredSummaries summaries, String file) {
		try {
			File csvFileSum = new File(file + "_summary_all.csv");
			File csvFileSumTW = new File(file + "_summary_tw.csv");
			File csvFileSumCP = new File(file + "_summary_cp.csv");
			File csvFileSumTWCP = new File(file + "_summary_tw_cp.csv");
			csvFileSum.getParentFile().mkdirs();
			csvFileSumTW.getParentFile().mkdirs();
			csvFileSumCP.getParentFile().mkdirs();
			csvFileSumTWCP.getParentFile().mkdirs();
			FileWriter csvWriterSum = new FileWriter(csvFileSum);
			FileWriter csvWriterSumTW = new FileWriter(csvFileSumTW);
			FileWriter csvWriterSumCP = new FileWriter(csvFileSumCP);
			FileWriter csvWriterSumTWCP = new FileWriter(csvFileSumTWCP);

			csvWriterSum.append(
					Analysis.getSummaryHeadline().replace("evaluation;tour;version;window;myMethod;", "") + "\n");
			csvWriterSumTW.append("GroupTW;"
					+ Analysis.getSummaryHeadline().replace("evaluation;tour;version;window;myMethod;", "") + "\n");
			csvWriterSumCP.append("GroupCP;"
					+ Analysis.getSummaryHeadline().replace("evaluation;tour;version;window;myMethod;", "") + "\n");
			csvWriterSumTWCP.append("GroupTW;GroupCP;"
					+ Analysis.getSummaryHeadline().replace("evaluation;tour;version;window;myMethod;", "") + "\n");

			csvWriterSum.append(getSummaryStr(summaries.summary_all));

			Iterator<Entry<Double, Integer>> iterTW = summaries.timeWindows.entrySet().iterator();
			while (iterTW.hasNext()) {
				Entry<Double, Integer> entry = iterTW.next();
				csvWriterSumTW.append(entry.getKey() + ";" + getSummaryStr(summaries.summary_tw[entry.getValue()]));
			}

			Iterator<Entry<Integer, Integer>> iterCP = summaries.customerPositions.entrySet().iterator();
			while (iterCP.hasNext()) {
				Entry<Integer, Integer> entry = iterCP.next();
				csvWriterSumCP.append(entry.getKey() + ";" + getSummaryStr(summaries.summary_cp[entry.getValue()]));
			}

			iterTW = summaries.timeWindows.entrySet().iterator();
			iterCP = summaries.customerPositions.entrySet().iterator();
			while (iterTW.hasNext()) {
				Entry<Double, Integer> entryTW = iterTW.next();
				while (iterCP.hasNext()) {
					Entry<Integer, Integer> entryCP = iterCP.next();
					csvWriterSumTWCP.append(entryTW.getKey() + ";" + entryCP.getKey() + ";"
							+ getSummaryStr(summaries.summary_tw_cp[entryTW.getValue()][entryCP.getValue()]));
				}
			}

			csvWriterSum.flush();
			csvWriterSumTW.flush();
			csvWriterSumCP.flush();
			csvWriterSumTWCP.flush();
			csvWriterSum.close();
			csvWriterSumTW.close();
			csvWriterSumCP.close();
			csvWriterSumTWCP.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String getSummaryStr(Summary2 summary) {
		String str = summary.percent_oBeforeTW + ";" + summary.percent_oInTW + ";" + summary.percent_oAfterTW + ";"
				+ summary.avg_oEarlyTW / 60 + ";" + summary.max_oEarlyTW / 60 + ";" + summary.avg_oLateTW / 60 + ";"
				+ summary.max_oLateTW / 60 + ";" + summary.percent_dBeforeTW + ";" + summary.percent_dInTW + ";"
				+ summary.percent_dAfterTW + ";" + summary.avg_dEarlyTW / 60 + ";" + summary.max_dEarlyTW / 60 + ";"
				+ summary.avg_dLateTW / 60 + ";" + summary.max_dLateTW / 60 + ";"
				+ summary.avg_tourDuration_all / 60 / 1440 + ";\n";

		str = str.replace(".", ",");
		return str;

	}

	private static void writeBufferSummaryCSV(Summary summary, String summaryFilePath) {

		try {
			File csvFile = new File(summaryFilePath);
			csvFile.getParentFile().mkdirs();
			FileWriter csvWriter = new FileWriter(csvFile);
			csvWriter.append(
					Analysis.getSummaryHeadline().replace("evaluation;tour;version;window;myMethod;", "") + "\n");

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

	private static void writeBufferSummaryTWGroupsCSV(Summary summary, String summaryFilePath) {

		try {
			File csvFile = new File(summaryFilePath);
			csvFile.getParentFile().mkdirs();
			FileWriter csvWriter = new FileWriter(csvFile);
			csvWriter.append("windowGroups;"
					+ Analysis.getSummaryHeadline().replace("evaluation;tour;version;window;myMethod;", "") + "\n");

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

}
