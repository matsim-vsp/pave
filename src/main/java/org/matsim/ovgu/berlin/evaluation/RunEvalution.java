package org.matsim.ovgu.berlin.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.matsim.ovgu.berlin.Settings;

public class RunEvalution {

	private List<EvTour> tours = new ArrayList<EvTour>();
	private String evaluationIdent;
	private String evaluationDirectory;

	
	public void run(String[] linkIDs, String evaluationIdent, int from, int to, boolean runBuffers, String windowMethod, boolean runEvaluation,
			boolean runSummary, boolean ttWithSim, boolean bufWithModel, boolean evaluationWithSim) {
		
		this.evaluationIdent = evaluationIdent;
		setEvaluationDirectory();

//		writeLinksCSV(linkIDs);
		generateRandomTours(linkIDs, 20);
//		writeToursCSV();

		for (int i = from - 1; i < to; i++)
			tours.get(i).setup24hTravelTimes(ttWithSim); // Simulation needed ?

		if (runBuffers)
			for (int i = from - 1; i < to; i++)
				tours.get(i).setupBuffersForVariants(bufWithModel);// LP Model needed ?

		if (runEvaluation)
			for (int i = from - 1; i < to; i++)
				tours.get(i).evaluate(windowMethod, evaluationWithSim);// Simulation needed ?

		if (runSummary)
			generateOverallSummary(windowMethod);

//
//		for (int i = from - 1; i < to; i++)
//			tours.get(i).evaluate("PlusMinusArrival", false);// Simulation needed ?
//
////		for (int i = from -1; i < to; i++)
////			tours.get(i).evaluate("AfterArrival", false);// Simulation needed ?
//
//		generateOverallSummary("PlusMinusArrival");
////		generateOverallSummary("AfterArrival");

		System.out.println("Version3_EvalutionFromLinks.run() Finished !");
	}

	private void generateOverallSummary(String timeWindowMethod) {

		List<String[]> tmpSummary = new ArrayList<String[]>();

		try {
			File csvFileResults = new File(
					evaluationDirectory + "/" + evaluationIdent + "_" + timeWindowMethod + "_results.csv");
			csvFileResults.getParentFile().mkdirs();
			FileWriter csvWriterResults = new FileWriter(csvFileResults);
			csvWriterResults
					.append(tours.get(0).getSummaryHeadline() + ";difTourDurationToBASEmin;difTourDurationToBASEavg\n");
			for (int i = 0; i < tours.size(); i++) {

				String results = readBufferSummariesWithoutHeadline(tours.get(i).tourDirectory + "/"
						+ tours.get(i).tourIdent + "_" + timeWindowMethod + "_summary.csv");

				csvWriterResults.append(results + "\n");
				tmpSummary.add(results.split("\n"));

			}
			csvWriterResults.flush();
			csvWriterResults.close();

			writeSummary(tmpSummary,
					evaluationDirectory + "/" + evaluationIdent + "_" + timeWindowMethod + "_results_summary.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeSummary(List<String[]> tmpSummary, String file) throws IOException {

		double optimalTourDuration = getAvgNoDelayDuration();

		File csvFileSummary = new File(file);
		csvFileSummary.getParentFile().mkdirs();
		FileWriter csvWriterSummary = new FileWriter(csvFileSummary);
		csvWriterSummary.append(tours.get(0).getSummaryHeadline().replace("tour;", "")
				+ ";difTourDurationToBASEmin;difTourDurationToBASEavg;difTourDurationToOpt\n");

		double tourCount = tmpSummary.size();
		int bufferCount = tmpSummary.get(0).length;
		int valuesPerBufferCount = tmpSummary.get(0)[0].split(";").length - 5;

		for (int b = 0; b < bufferCount; b++) {
			double[] sumValues = new double[valuesPerBufferCount];
			String bufIdentifier = "";
			for (int t = 0; t < tourCount; t++) {
				String[] stringElements = tmpSummary.get(t)[b].split(";");
				double[] values = getDoubleValues(stringElements);
				sumValues = addArrays(sumValues, values);
				if (t == 0)
					bufIdentifier = getIdentifier(stringElements);
			}
			double[] avgValues = getAvgValues(sumValues, tourCount);
			double difOptTourDuration = (avgValues[avgValues.length - 3] / optimalTourDuration) - 1;
			String avgString = bufIdentifier + ";";
			for (double value : avgValues)
				avgString += value + ";";
			avgString += difOptTourDuration;
			csvWriterSummary.append(avgString.replace(".", ",") + "\n");
		}
		csvWriterSummary.append(("optimalTourDuration;" + optimalTourDuration).replace(".", ","));
		csvWriterSummary.flush();
		csvWriterSummary.close();
	}

	private Double getAvgNoDelayDuration() {
		double sum = 0;
		for (int i = 0; i < tours.size(); i++) {
			sum += tours.get(i).getNoDelayDuration(2 * 60);
		}
		return sum / (double) tours.size() / 60.0 / 1440.0;
	}

	private double readOptimalDuration(EvTour tour) {
		try {
			BufferedReader csvReader = new BufferedReader(
					new FileReader(tour.tourDirectory + "/" + tour.tourIdent + "_traveltimes.csv"));

			double servicetime = 120;
			String firstRow = csvReader.readLine();
			double sum = 0;
			int count = 0;
			String row;
			while ((row = csvReader.readLine()) != null) {
				sum += Double.parseDouble(row.split(";")[28].replace(",", ".")) + servicetime;
				count++;
			}

			csvReader.close();
			return sum / (double) count;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}

	}

	private double[] getAvgValues(double[] sumValues, double tourCount) {
		double[] avgValues = new double[sumValues.length];
		for (int i = 0; i < avgValues.length; i++) {
			avgValues[i] = sumValues[i] / tourCount;
		}
		return avgValues;
	}

	private String getIdentifier(String[] strings) {
		return strings[0] + ";" + strings[2] + ";" + strings[3] + ";" + strings[4];
	}

	private double[] getDoubleValues(String[] strings) {
		double[] values = new double[strings.length - 5];
		for (int i = 0; i < values.length; i++) {
			values[i] = Double.parseDouble(strings[i + 5].replace(",", "."));
			if (Double.isNaN(values[i]))
				values[i] = 0;
		}
		return values;
	}

	private double[] addArrays(double[] a, double[] b) {
		double[] result = new double[b.length];
		for (int i = 0; i < a.length; i++)
			result[i] = a[i] + b[i];

		return result;
	}

	private String readBufferSummariesWithoutHeadline(String file) throws IOException {

		BufferedReader csvReader = new BufferedReader(new FileReader(file));

		String firstRow = csvReader.readLine();
		String otherRows = "";
		String row;
		while ((row = csvReader.readLine()) != null) {
			otherRows += row + "\n";
		}

		csvReader.close();
		return otherRows;
	}

	private void setEvaluationDirectory() {
		evaluationDirectory = new Settings().directory + "/" + evaluationIdent + "/";

	}

	private void writeLinksCSV(String[] linkIDs) {
		try {
			File csvFile = new File(evaluationDirectory + "/" + evaluationIdent + "_links.csv");
			csvFile.getParentFile().mkdirs();
			FileWriter csvWriter = new FileWriter(csvFile);

			for (String linkID : linkIDs)
				csvWriter.append(linkID + "\n");

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeToursCSV() {
		try {
			File csvFile = new File(evaluationDirectory + "/" + evaluationIdent + "_tours.csv");
			csvFile.getParentFile().mkdirs();
			FileWriter csvWriter = new FileWriter(csvFile);

			for (EvTour tour : tours) {
				String str = tour.tourIdent + ";;";
				for (String linkID : tour.linkIDs)
					str += linkID + ";";
				csvWriter.append(str + "\n");
			}
			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void generateRandomTours(String[] linkIDs, int linksPerTour) {

		// shuffle linkIDs
		shuffleLinkIDs(linkIDs);

		// a tour consist of linksPerTour links
		String[] ids = new String[linksPerTour];
		int counter = 0;
		for (int i = 0; i < linkIDs.length; i++) {
			ids[counter++] = linkIDs[i];
			// a tour consist of linksPerTour links
			if (counter == linksPerTour) {
				tours.add(
						new EvTour(evaluationDirectory, evaluationIdent + "_tour" + ((i + 1) / counter), ids));
				ids = new String[linksPerTour];
				counter = 0;
			}
		}
	}

	private String[] shuffleLinkIDs(String[] linkIDs) {
		// Implementing Fisher–Yates shuffle
		Random rnd = new Random(99999);
		for (int i = linkIDs.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			String id = linkIDs[index];
			linkIDs[index] = linkIDs[i];
			linkIDs[i] = id;
		}
		return linkIDs;

	}

}
