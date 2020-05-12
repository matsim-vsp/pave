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
import org.matsim.ovgu.berlin.input.InputTour;

public class Version3_EvalutionFromLinks {

	private List<EvaluationTour> tours = new ArrayList<EvaluationTour>();
	private final String evaluationIdent = "myEvaluation200x2";
	private String evaluationDirectory;

	// 200 linkIDs expected
	public void run() {
		setEvaluationDirectory();

		String[] linkIDs = getMySample();
		writeLinksCSV(linkIDs);
		generateRandomTours(linkIDs, 20);
//		setupMyTour();

		writeToursCSV();

		int from = 1;
		int to = tours.size();

		for (int i = from - 1; i < to; i++)
			tours.get(i).setup24hTravelTimes(false); // Simulation needed ?

		for (int i = from - 1; i < to; i++)
			tours.get(i).setupInputVersionsWithBuffers(false);// LP Model needed ?
		
		for (int i = from -1; i < to; i++)
			tours.get(i).evaluate("PlusMinusArrival", false);// Simulation needed ?
		
		for (int i = from -1; i < to; i++)
			tours.get(i).evaluate("AfterArrival", false);// Simulation needed ?

		generateOverallSummary("PlusMinusArrival");

		generateOverallSummary("AfterArrival");

		System.out.println("Version3_EvalutionFromLinks.run() Finished !");
	}

	private void generateOverallSummary(String timeWindowMethod) {

		List<String[]> tmpSummary = new ArrayList<String[]>();

		try {
			File csvFileResults = new File(
					evaluationDirectory + "/" + evaluationIdent + "_" + timeWindowMethod + "_results.csv");
			csvFileResults.getParentFile().mkdirs();
			FileWriter csvWriterResults = new FileWriter(csvFileResults);
			csvWriterResults.append(tours.get(0).getSummaryHeadline(tours.get(0).linkIDs.length));
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

		File csvFileSummary = new File(file);
		csvFileSummary.getParentFile().mkdirs();
		FileWriter csvWriterSummary = new FileWriter(csvFileSummary);
		csvWriterSummary.append(tours.get(0).getSummaryHeadline(tours.get(0).linkIDs.length));

		double tourCount = tmpSummary.size();
		int bufferCount = tmpSummary.get(0).length;
		int valuesPerBufferCount = tmpSummary.get(0)[0].split(";").length - 1;

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
			String avgString = bufIdentifier + ";";
			for (double value : avgValues)
				avgString += value + ";";
			csvWriterSummary.append(avgString.replace(".", ",") + "\n");
		}
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
		return strings[0].replace("_tour1", "");
	}

	private double[] getDoubleValues(String[] strings) {
		double[] values = new double[strings.length - 1];
		for (int i = 0; i < values.length; i++) {
			values[i] = Double.parseDouble(strings[i + 1].replace(",", "."));
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

			for (EvaluationTour tour : tours) {
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

	private void setupMyTour() {
		tours.add(new EvaluationTour(evaluationDirectory, evaluationIdent + "_tour1", InputTour.tour));
		tours.add(new EvaluationTour(evaluationDirectory, evaluationIdent + "_tour2", InputTour.tour));
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
						new EvaluationTour(evaluationDirectory, evaluationIdent + "_tour" + ((i + 1) / counter), ids));
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

	private String[] getMySample() {
		String[] mySample = new String[] { "77469", "56104", "72634", "70906", "11938", "152000", "37016", "60631",
				"126410", "113115", "129732", "29348", "pt_20614", "10405", "29323", "pt_18540", "152682", "122557",
				"126500", "129537", "133478", "pt_14527", "52150", "112045", "106783", "132907", "122219", "pt_17610",
				"98801", "14291", "20042", "35296", "11653", "125000", "110096", "128009", "46756", "66661", "87303",
				"34389", "131523", "pt_20166", "15447", "33541", "58423", "119371", "122271", "152447", "78002",
				"61239", "87107", "63103", "113966", "100839", "34660", "pt_19168", "12307", "108860", "54316",
				"154719", "90438", "48007", "141412", "100234", "106086", "12997", "10230", "136377", "22710", "88036",
				"110651", "pt_17595", "6485", "50978", "79014", "37621", "140792", "154259", "79242", "54069", "28233",
				"62726", "pt_20637", "39179", "69450", "19359", "114793", "89015", "41111", "46609", "11341", "127677",
				"50114", "6464", "11802", "158899", "16266", "33492", "41268", "123006", "87635", "125280", "pt_16503",
				"pt_12650", "138176", "47120", "72910", "pt_19444", "40827", "143278", "126119", "pt_19538", "pt_20722",
				"49133", "pt_98", "78869", "86129", "16943", "139858", "138178", "124864", "62370", "108149", "41390",
				"91890", "111682", "127692", "23422", "129243", "pt_17634", "139223", "149796", "130592", "29491",
				"5709", "97909", "130768", "158396", "84649", "16892", "154261", "51170", "pt_15873", "pt_16764",
				"pt_16429", "125301", "pt_18460", "45610", "56178", "506", "119508", "78084", "112129", "94460",
				"96764", "132422", "21101", "7116", "103615", "150786", "128037", "130464", "50536", "137436", "159695",
				"62516", "pt_18225", "11519", "17620", "pt_16349", "pt_9830", "pt_15844", "22946", "17159", "138001",
				"pt_19879", "121957", "pt_19139", "106946", "94408", "38323", "127715", "99837", "88638", "104198",
				"1244", "142326", "150601", "160108", "77866", "109937", "78431", "150372", "82938", "147544", "159418",
				"61216", "92999", "46779", "93020" };

		mySample = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
				"17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33",
				"34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50",
				"51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67",
				"68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84",
				"85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", "101",
				"102", "103", "104", "105", "106", "107", "108", "109", "10", "11", "12", "13", "14", "15", "16", "117",
				"118", "119", "120", "121", "122", "123", "124", "125", "126", "127", "128", "129", "130", "131", "132",
				"133", "134", "135", "136", "137", "138", "139", "140", "141", "142", "143", "144", "145", "146", "147",
				"148", "149", "150", "151", "152", "153", "154", "155", "156", "157", "158", "159", "160", "161", "162",
				"163", "164", "165", "166", "167", "168", "169", "170", "171", "172", "173", "174", "175", "176", "177",
				"178", "179", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189", "190", "191", "192",
				"193", "194", "195", "196", "197", "198", "199", "200" };

		mySample = new String[] { "50878", "55084", "89524", "90752", "11821", "5712", "23654", "23362", "84301",
				"57169", "122136", "153188", "46802", "143895", "63251", "107638", "98217", "58957", "150919", "65599",
				"103731", "20939", "1741", "51709", "138008", "147898", "154926", "27492", "40855", "38336", "44485",
				"105745", "78324", "51923", "9395", "52901", "16260", "28917", "122959", "69223", "28303", "41691",
				"69174", "141348", "88213", "119137", "4555", "105295", "62370", "67134", "93926", "8198", "104456",
				"70921", "110431", "2548", "72827", "74694", "125775", "62546", "15313", "155933", "26651", "79044",
				"151492", "125443", "135571", "16282", "27603", "111609", "122600", "69522", "61226", "30266", "53049",
				"134169", "98363", "139651", "138049", "72854", "141107", "77687", "134841", "114198", "37396", "13190",
				"118184", "108906", "95615", "156215", "27101", "30287", "87308", "149729", "112090", "2920", "31344",
				"96660", "78576", "21451", "115832", "16245", "94555", "72257", "35071", "138131", "92188", "86314",
				"35983", "26293", "29600", "39710", "120422", "59274", "141749", "110533", "136963", "48103", "41807",
				"143092", "101995", "64432", "26742", "113120", "158788", "53448", "97971", "61270", "6356", "63395",
				"66389", "109480", "83200", "72872", "27353", "133824", "15315", "18628", "67864", "146739", "36963",
				"42704", "135068", "54286", "91320", "112977", "44782", "7726", "94556", "16328", "99072", "14783",
				"99543", "113808", "137683", "60657", "154750", "64309", "113825", "122780", "128978", "111543",
				"86610", "110755", "137202", "88285", "134487", "6525", "147406", "125664", "140234", "154269", "8745",
				"77664", "18953", "138525", "156322", "25304", "33670", "18680", "12254", "141143", "74395", "31245",
				"100050", "118139", "158220", "66043", "4785", "18780", "101079", "111833", "304", "17132", "114428",
				"138457", "156557", "28199", "28803", "765" };

		return mySample;
	}

}
