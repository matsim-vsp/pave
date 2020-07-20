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
import org.matsim.ovgu.berlin.evaluation.model.EvBuffer;
import org.matsim.ovgu.berlin.evaluation.model.EvTour;
import org.matsim.ovgu.berlin.evaluation.model.EvVariant;
import org.matsim.ovgu.berlin.eventHandling.FilteredSummaries;
import org.matsim.ovgu.berlin.eventHandling.Summary;
import org.matsim.ovgu.berlin.eventHandling.TourEventsHandler;

public class EventAnalysis {

	public static String getSummaryHeadline() {
		String str = "evaluation;tour;variant;buffer;myMethod;"
				+ "oBeforeTW;oInTW;oAfterTW;oAvgEarlyTW;oMaxEarlyTW;oAvgLateTW;oMaxLateTW;"
				+ "dBeforeTW;dInTW;dAfterTW;dAvgEarlyTW;dMaxEarlyTW;dAvgLateTW;dMaxLateTW;" + "tourDuration";

		return str;
	}

	public static void run(String evaluationIdent, EvTour tour, String timeWindowMethod) {

		for (EvVariant variant : tour.evBufferVariants) {
			for (EvBuffer buffer : variant.buffers) {
				buffer.load();
				Settings settings = buffer.runSettings;
				settings.directory += timeWindowMethod + "/";
				settings.timeWindowMethod = timeWindowMethod;
				String analysisIdent = buffer.bufferIdent.replace("_", ";");
				EventAnalysis.readEvents(settings, buffer.bufferIdent + "_" + timeWindowMethod + "_result",
						analysisIdent);
			}
		}
	}

	private static void readEvents(Settings settings, String fileName, String analysisIdent) {
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

		writeSummaries(handler.getFilteredSummaries(), file, analysisIdent);

		System.out.println("CSV finished!");
	}

	private static void writeSummaries(FilteredSummaries summaries, String file, String analysisIdent) {
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

			csvWriterSum.append(getSummaryHeadline() + "\n");
			csvWriterSumTW.append("GroupTW;" + getSummaryHeadline() + "\n");
			csvWriterSumCP.append("GroupCP;" + getSummaryHeadline() + "\n");
			csvWriterSumTWCP.append("GroupTW;GroupCP;" + getSummaryHeadline() + "\n");

			csvWriterSum.append(getSummaryStr(analysisIdent, summaries.summary_all));

			Iterator<Entry<Double, Integer>> iterTW = summaries.timeWindows.entrySet().iterator();
			while (iterTW.hasNext()) {
				Entry<Double, Integer> entry = iterTW.next();
				csvWriterSumTW.append(
						entry.getKey() + ";" + getSummaryStr(analysisIdent, summaries.summary_tw[entry.getValue()]));
			}

			Iterator<Entry<Integer, Integer>> iterCP = summaries.customerPositions.entrySet().iterator();
			while (iterCP.hasNext()) {
				Entry<Integer, Integer> entry = iterCP.next();
				csvWriterSumCP.append(
						entry.getKey() + ";" + getSummaryStr(analysisIdent, summaries.summary_cp[entry.getValue()]));
			}

			iterTW = summaries.timeWindows.entrySet().iterator();
			while (iterTW.hasNext()) {
				Entry<Double, Integer> entryTW = iterTW.next();
				iterCP = summaries.customerPositions.entrySet().iterator();
				while (iterCP.hasNext()) {
					Entry<Integer, Integer> entryCP = iterCP.next();
					csvWriterSumTWCP
							.append(entryTW.getKey() + ";" + entryCP.getKey() + ";" + getSummaryStr(analysisIdent,
									summaries.summary_tw_cp[entryTW.getValue()][entryCP.getValue()]));
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

	private static String getSummaryStr(String analysisIdent, Summary summary) {
		String str = analysisIdent + ";" + summary.percent_oBeforeTW + ";" + summary.percent_oInTW + ";"
				+ summary.percent_oAfterTW + ";" + summary.avg_oEarlyTW / 60 + ";" + summary.max_oEarlyTW / 60 + ";"
				+ summary.avg_oLateTW / 60 + ";" + summary.max_oLateTW / 60 + ";" + summary.percent_dBeforeTW + ";"
				+ summary.percent_dInTW + ";" + summary.percent_dAfterTW + ";" + summary.avg_dEarlyTW / 60 + ";"
				+ summary.max_dEarlyTW / 60 + ";" + summary.avg_dLateTW / 60 + ";" + summary.max_dLateTW / 60 + ";"
				+ summary.avg_tourDuration_all / 60 / 1440 + ";\n";

		str = str.replace(".", ",");
		str = str.replace("NaN", "0");
		return str;
	}
}
