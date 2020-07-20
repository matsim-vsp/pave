package org.matsim.ovgu.berlin.evaluation.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.matsim.ovgu.berlin.evaluation.model.EvBuffer;
import org.matsim.ovgu.berlin.evaluation.model.EvTour;
import org.matsim.ovgu.berlin.evaluation.model.EvVariant;

public class SumAnalysis {
	private List<EvTour> tours = new ArrayList<EvTour>();
	private String evaluationIdent;
	private String evaluationDirectory;
	private int from;
	private int to;

	public SumAnalysis(List<EvTour> tours, String evaluationDirectory, String evaluationIdent, int from, int to,
			String windowMethod) {

		this.tours = tours;
		this.evaluationIdent = evaluationIdent;
		this.evaluationDirectory = evaluationDirectory;
		this.from = from;
		this.to = to;

		for (int i = from - 1; i < to; i++) {

			for (EvVariant variant : tours.get(i).evBufferVariants) {
				sumBuffers(variant, windowMethod);
			}
			sumVariants(tours.get(i), windowMethod);
		}
		sumTours(windowMethod);
	}

	private void sumBuffers(EvVariant variant, String windowMethod) {
		try {
			summariesBuffers(variant, windowMethod, "all");
			summariesBuffers(variant, windowMethod, "tw");
			summariesBuffers(variant, windowMethod, "cp");
			summariesBuffers(variant, windowMethod, "tw_cp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sumVariants(EvTour tour, String windowMethod) {
		try {
			summariesVariants(tour, windowMethod, "sum_all");
			summariesVariants(tour, windowMethod, "avg_all");
			summariesVariants(tour, windowMethod, "sum_tw");
			summariesVariants(tour, windowMethod, "avg_tw");
			summariesVariants(tour, windowMethod, "sum_cp");
			summariesVariants(tour, windowMethod, "avg_cp");
			summariesVariants(tour, windowMethod, "sum_tw_cp");
			summariesVariants(tour, windowMethod, "avg_tw_cp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sumTours(String windowMethod) {
		try {
			summariesTours(windowMethod, "sum_sum_all");
			summariesTours(windowMethod, "sum_avg_all");
			summariesTours(windowMethod, "avg_sum_all");
			summariesTours(windowMethod, "avg_avg_all");
			summariesTours(windowMethod, "sum_sum_tw");
			summariesTours(windowMethod, "sum_avg_tw");
			summariesTours(windowMethod, "avg_sum_tw");
			summariesTours(windowMethod, "avg_avg_tw");
			summariesTours(windowMethod, "sum_sum_cp");
			summariesTours(windowMethod, "sum_avg_cp");
			summariesTours(windowMethod, "avg_sum_cp");
			summariesTours(windowMethod, "avg_avg_cp");
			summariesTours(windowMethod, "sum_sum_tw_cp");
			summariesTours(windowMethod, "sum_avg_tw_cp");
			summariesTours(windowMethod, "avg_sum_tw_cp");
			summariesTours(windowMethod, "avg_avg_tw_cp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void summariesBuffers(EvVariant variant, String windowMethod, String group) throws IOException {

		String fileString = variant.versionDirectory + "/" + variant.versionIdent + "_" + windowMethod + "_summary_";
		String fileStringSum = fileString + "sum_";
		String fileStringAvg = fileString + "avg_";

		FileWriter csvWriterSum = getFileWriter(fileStringSum + group + ".csv");
		FileWriter csvWriterAvg = getFileWriter(fileStringAvg + group + ".csv");

		List<String[]> tmpData = new ArrayList<String[]>();
		boolean headline = true;

		for (EvBuffer buffer : variant.buffers) {
			String file = buffer.bufferDirectory + "/" + buffer.bufferIdent + "_" + windowMethod + "_result_summary_";

			if (headline) {
				String hl = readFileHeadline(file + group + ".csv");
				csvWriterSum.append(hl);
				csvWriterAvg.append(hl);
				headline = false;
			}
			String content = readFileContent(file + group + ".csv");
			csvWriterSum.append(content);
			tmpData.add(content.split("\n"));
		}
		csvWriterSum.flush();
		csvWriterSum.close();
		csvWriterAvg.append(calculateAvg(tmpData));
		csvWriterAvg.flush();
		csvWriterAvg.close();
	}

	private void summariesVariants(EvTour tour, String windowMethod, String group) throws IOException {

		String fileString = tour.tourDirectory + "/" + tour.tourIdent + "_" + windowMethod + "_summary_";
		String fileStringSum = fileString + "sum_";
		String fileStringAvg = fileString + "avg_";

		FileWriter csvWriterSum = getFileWriter(fileStringSum + group + ".csv");
		FileWriter csvWriterAvg = getFileWriter(fileStringAvg + group + ".csv");

		List<String[]> tmpData = new ArrayList<String[]>();
		boolean headline = true;

		for (EvVariant variant : tour.evBufferVariants) {
			String file = variant.versionDirectory + "/" + variant.versionIdent + "_" + windowMethod + "_summary_";
			if (headline) {
				String hl = readFileHeadline(file + group + ".csv");
				csvWriterSum.append(hl);
				csvWriterAvg.append(hl);
				headline = false;
			}
			String content = readFileContent(file + group + ".csv");
			csvWriterSum.append(content);
			tmpData.add(content.split("\n"));
		}
		csvWriterSum.flush();
		csvWriterSum.close();
		csvWriterAvg.append(calculateAvg(tmpData));
		csvWriterAvg.flush();
		csvWriterAvg.close();
	}

	private void summariesTours(String windowMethod, String group) throws IOException {

		String fileString = evaluationDirectory + "/" + evaluationIdent + "_" + windowMethod + "_result_";
		String fileStringSum = fileString + "sum_";
		String fileStringAvg = fileString + "avg_";

		FileWriter csvWriterSum = getFileWriter(fileStringSum + group + ".csv");
		FileWriter csvWriterAvg = getFileWriter(fileStringAvg + group + ".csv");

		List<String[]> tmpData = new ArrayList<String[]>();
		boolean headline = true;

		for (int i = from - 1; i < to; i++) {

			String file = tours.get(i).tourDirectory + "/" + tours.get(i).tourIdent + "_" + windowMethod + "_summary_";

			if (headline) {
				String hl = readFileHeadline(file + group + ".csv");
				csvWriterSum.append(hl);
				csvWriterAvg.append(hl);
				headline = false;
			}
			String content = readFileContent(file + group + ".csv");
			csvWriterSum.append(content);
			tmpData.add(content.split("\n"));
		}
		csvWriterSum.flush();
		csvWriterSum.close();
		csvWriterAvg.append(calculateAvg(tmpData));
		csvWriterAvg.flush();
		csvWriterAvg.close();
	}

	private String calculateAvg(List<String[]> tmpData) throws IOException {

		String result = "";

//		double optimalTourDuration = getAvgNoDelayDuration();

		double toursCount = tmpData.size();
		int linesCount = tmpData.get(0).length;
		int valuesPerLine = tmpData.get(0)[0].split(";").length;

		for (int l = 0; l < linesCount; l++) {
			String[] sumElements = new String[valuesPerLine];
			for (int t = 0; t < toursCount; t++) {
				String[] elements = tmpData.get(t)[l].replace(",", ".").split(";");
				sumElements = addDoubles(sumElements, elements);
			}
			String[] avgElements = getAvgDoubles(sumElements, toursCount);
//			double difOptTourDuration = (avgValues[avgValues.length - 3] / optimalTourDuration) - 1;
			String avgString = "";
			for (String element : avgElements)
				avgString += element + ";";
//			avgString += difOptTourDuration;
			result += avgString.replace(".", ",") + "\n";
		}
//		result += ("optimalTourDuration;" + optimalTourDuration).replace(".", ",");
		return result;
	}

	private String[] addDoubles(String[] a, String[] b) {
		String[] result = new String[b.length];
		for (int i = 0; i < a.length; i++)
			try {
				double aValue = getDouble(a[i]);
				double bValue = getDouble(b[i]);
				result[i] = "" + (aValue + bValue);
			} catch (NumberFormatException e) {
				result[i] = a[i];
			} catch (NullPointerException e) {
				result[i] = b[i];
			}
		return result;
	}

	private String[] getAvgDoubles(String[] sumElements, double tourCount) {
		String[] avgElements = new String[sumElements.length];
		for (int i = 0; i < avgElements.length; i++)
			try {
				double value = getDouble(sumElements[i]);
				avgElements[i] = "" + (value / tourCount);
			} catch (NumberFormatException e) {
				avgElements[i] = sumElements[i];
			}
		return avgElements;
	}
	
	private double getDouble(String str) {
		double value = Double.parseDouble(str);
		if (Double.isNaN(value))
			value = 0;
		return value;
	}

	private FileWriter getFileWriter(String file) throws IOException {
		File csvFile = new File(file);
		csvFile.getParentFile().mkdirs();
		return new FileWriter(csvFile);
	}

	private static String readFileContent(String file) throws IOException {

		BufferedReader csvReader = new BufferedReader(new FileReader(file));

		String firstRow = csvReader.readLine();
		String content = "";
		String line;

		while ((line = csvReader.readLine()) != null) {
			content += line + "\n";
		}

		csvReader.close();
		return content;
	}

	private static String readFileHeadline(String file) throws IOException {

		BufferedReader csvReader = new BufferedReader(new FileReader(file));

		String firstRow = csvReader.readLine();
		String headline = firstRow + "\n";
		csvReader.close();

		return headline;
	}

	private Double getAvgNoDelayDuration() {
		double sum = 0;
		for (int i = from - 1; i < to; i++) {
			sum += tours.get(i).getNoDelayDuration(2 * 60);
		}
		return sum / (double) (1 + to - from) / 60.0 / 1440.0;
	}
}
