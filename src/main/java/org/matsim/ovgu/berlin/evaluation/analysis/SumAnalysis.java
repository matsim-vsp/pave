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
				sumVariant(variant, windowMethod);
			}
			sumTour(tours.get(i), windowMethod);
		}
		sumTours(windowMethod);
		avgSummaries(windowMethod);
	}

	private void avgSummaries(String windowMethod) {
		try {
			avgSummary(windowMethod, "all");
			avgSummary(windowMethod, "tw");
			avgSummary(windowMethod, "cp");
			avgSummary(windowMethod, "tw_cp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void avgSummary(String windowMethod, String group) throws IOException {
		List<String[]> tmpData = new ArrayList<String[]>();

		String fileString = evaluationDirectory + "/" + evaluationIdent + "_" + windowMethod + "_result_avg_";
		FileWriter csvWriter = getFileWriter(fileString + group + ".csv");

		boolean headline = true;

		for (int i = from - 1; i < to; i++) {

			String file = tours.get(i).tourDirectory + "/" + tours.get(i).tourIdent + "_" + windowMethod + "_summary_";

			if (headline) {
				csvWriter.append(readFileHeadline(file + group + ".csv"));
				headline = false;
			}
			tmpData.add(readFileContent(file + group + ".csv").split("\n"));

		}
		csvWriter.append(calculateAvg(tmpData));
		csvWriter.flush();
		csvWriter.close();

	}

	private String calculateAvg(List<String[]> tmpData) throws IOException {

		String result = "";

		double optimalTourDuration = getAvgNoDelayDuration();

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
		result += ("optimalTourDuration;" + optimalTourDuration).replace(".", ",");
		return result;
	}

	private String[] addDoubles(String[] a, String[] b) {
		String[] result = new String[b.length];
		for (int i = 0; i < a.length; i++)
			try {
				result[i] = "" + (Double.parseDouble(a[i]) + Double.parseDouble(b[i]));
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
				avgElements[i] = "" + (Double.parseDouble(sumElements[i]) / tourCount);
			} catch (NumberFormatException e) {
				avgElements[i] = sumElements[i];
			}
		return avgElements;
	}

	private void sumTours(String windowMethod) {
		try {
			summariesTours(windowMethod, "all");
			summariesTours(windowMethod, "tw");
			summariesTours(windowMethod, "cp");
			summariesTours(windowMethod, "tw_cp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sumTour(EvTour tour, String windowMethod) {
		try {
			summariesTour(tour, windowMethod, "all");
			summariesTour(tour, windowMethod, "tw");
			summariesTour(tour, windowMethod, "cp");
			summariesTour(tour, windowMethod, "tw_cp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sumVariant(EvVariant variant, String windowMethod) {
		try {
			summariesVariant(variant, windowMethod, "all");
			summariesVariant(variant, windowMethod, "tw");
			summariesVariant(variant, windowMethod, "cp");
			summariesVariant(variant, windowMethod, "tw_cp");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void summariesTour(EvTour tour, String windowMethod, String group) throws IOException {

		String fileString = tour.tourDirectory + "/" + tour.tourIdent + "_" + windowMethod + "_summary_";
		FileWriter csvWriter = getFileWriter(fileString + group + ".csv");
		boolean headline = true;

		for (EvVariant variant : tour.evBufferVariants) {
			String file = variant.versionDirectory + "/" + variant.versionIdent + "_" + windowMethod + "_summary_";
			if (headline) {
				csvWriter.append(readFileHeadline(file + group + ".csv"));
				headline = false;
			}
			csvWriter.append(readFileContent(file + group + ".csv"));
		}
		csvWriter.flush();
		csvWriter.close();
	}

	private void summariesVariant(EvVariant variant, String windowMethod, String group) throws IOException {
		String fileString = variant.versionDirectory + "/" + variant.versionIdent + "_" + windowMethod + "_summary_";
		FileWriter csvWriter = getFileWriter(fileString + group + ".csv");
		boolean headline = true;

		for (EvBuffer buffer : variant.buffers) {
			String file = buffer.bufferDirectory + "/" + buffer.bufferIdent + "_" + windowMethod + "_result_summary_";

			if (headline) {
				csvWriter.append(readFileHeadline(file + group + ".csv"));
				headline = false;
			}
			csvWriter.append(readFileContent(file + group + ".csv"));
		}
		csvWriter.flush();
		csvWriter.close();
	}

	private void summariesTours(String windowMethod, String group) throws IOException {

		String fileString = evaluationDirectory + "/" + evaluationIdent + "_" + windowMethod + "_result_";
		FileWriter csvWriter = getFileWriter(fileString + group + ".csv");

		boolean headline = true;

		for (int i = from - 1; i < to; i++) {

			String file = tours.get(i).tourDirectory + "/" + tours.get(i).tourIdent + "_" + windowMethod + "_summary_";

			if (headline) {
				csvWriter.append(readFileHeadline(file + group + ".csv"));
				headline = false;
			}
			csvWriter.append(readFileContent(file + group + ".csv"));
		}
		csvWriter.flush();
		csvWriter.close();
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
