package org.matsim.ovgu.berlin.evaluation.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.matsim.ovgu.berlin.evaluation.model.EvTour;

public class Summary {
	private List<EvTour> tours = new ArrayList<EvTour>();
	private String evaluationIdent;
	private String evaluationDirectory;
	private int from;
	private int to;

	public Summary(List<EvTour> tours, String evaluationIdent, String evaluationDirectory, int from, int to,
			String windowMethod) {

		this.tours = tours;
		this.evaluationIdent = evaluationIdent;
		this.evaluationDirectory = evaluationDirectory;
		this.from = from;
		this.to = to;
		
		generateOverallSummary(windowMethod, "");
		generateOverallSummary(windowMethod, "Groups");
	}

	private void generateOverallSummary(String timeWindowMethod, String groups) {

		List<String[]> tmpSummary = new ArrayList<String[]>();

		try {
			File csvFileResults = new File(evaluationDirectory + "/" + evaluationIdent + "_" + timeWindowMethod + "_f"
					+ from + "t" + to + "_results" + groups + ".csv");
			csvFileResults.getParentFile().mkdirs();
			FileWriter csvWriterResults = new FileWriter(csvFileResults);
			csvWriterResults.append(getHeadline(groups, false));

			for (int i = from - 1; i < to; i++) {

				String results = readBufferSummariesWithoutHeadline(tours.get(i).tourDirectory + "/"
						+ tours.get(i).tourIdent + "_" + timeWindowMethod + "_summary" + groups + ".csv");

				csvWriterResults.append(results + "\n");
				tmpSummary.add(results.split("\n"));

			}
			csvWriterResults.flush();
			csvWriterResults.close();

			writeSummary(tmpSummary, evaluationDirectory + "/" + evaluationIdent + "_" + timeWindowMethod + "_f" + from
					+ "t" + to + "_results_summary" + groups + ".csv", groups);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeSummary(List<String[]> tmpSummary, String file, String groups) throws IOException {

		double optimalTourDuration = getAvgNoDelayDuration();

		File csvFileSummary = new File(file);
		csvFileSummary.getParentFile().mkdirs();
		FileWriter csvWriterSummary = new FileWriter(csvFileSummary);
		csvWriterSummary.append(getHeadline(groups, true));

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

	private CharSequence getHeadline(String groups, boolean withoutTour) {

		String str = Analysis.getSummaryHeadline();

		if ("Groups".equals(groups))
			str = str.replace("myMethod", "myMethod;windowGroups");

		str += ";difTourDurationToBASEmin;difTourDurationToBASEavg";

		if (withoutTour) {
			str = str.replace("tour;", "");
			str += ";difTourDurationToOpt";
		}
		str += "\n";

		return str;
	}

	private Double getAvgNoDelayDuration() {
		double sum = 0;
		for (int i = from - 1; i < to; i++) {
			sum += tours.get(i).getNoDelayDuration(2 * 60);
		}
		return sum / (double) (1 + to - from) / 60.0 / 1440.0;
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

}
