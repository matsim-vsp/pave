package org.matsim.ovgu.berlin.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.LinearProgram.GenLpSolver;
import org.matsim.ovgu.berlin.LinearProgram.LpTrip;

public class EvBufferSetup {

	EvBufferSetup(String versionDirectory, String bufferIdent, double se, double t, double b, double[] w, double ss,
			double u, boolean myMethod, double[] expTT, double[][] delayScenarios, String[] linkIDs) {
		this.bufferIdent = bufferIdent;
		this.bufferDirectory = versionDirectory + "/" + bufferIdent + "/";
		this.se = se;
		this.t = t;
		this.b = b;
		this.w = w;
		this.ss = ss;
		this.u = u;
		this.myMethod = myMethod;
		this.expTT = expTT;
		if (delayScenarios != null)
			this.scenarioCount = delayScenarios.length;

		this.trips = createLpTrips(expTT, delayScenarios);
		this.linkIDs = linkIDs;
		this.bufferValues = new double[linkIDs.length - 1];
	}

//	EvBufferVersion(String versionDirectory, String bufferIdent, double w, double u, double[] expTT, String[] linkIDs) {
//		this.bufferIdent = bufferIdent;
//		this.bufferDirectory = versionDirectory + "/" + bufferIdent + "/";
//		this.w = w;
//		this.u = u;
//		this.expTT = expTT;
//		this.linkIDs = linkIDs;
//	}

	public String bufferIdent;
	public String bufferDirectory;
	private String[] linkIDs;
	private double se;
	private double t;
	private double b;
	public double[] w;
	private double ss;
	public double u;
	private boolean myMethod;
	public double[] expTT;
	private LpTrip[] trips;
	private int scenarioCount;
	private double[] scenarioProb;
	public double[] bufferValues;
	private double objectiveValue;
	private double tourDuration;
	protected Settings runSettings;

	public void calculateStandardDeviationBuffer(double[] avgTT, double[][] traveltimeMatrix, boolean test) {
		int linksCount = traveltimeMatrix.length;
		int szenariosCount = traveltimeMatrix[0].length;

		bufferValues = new double[linksCount];

		for (int l = 0; l < linksCount; l++) {
			double[] ttArray = new double[szenariosCount];
			for (int s = 0; s < szenariosCount; s++)
				ttArray[s] = traveltimeMatrix[l][s];
			double sd = calculateStandardDeviation(avgTT[l], ttArray);

			if (test && w[l] == 1)
				bufferValues[l] = sd * 1.3;
			else
				bufferValues[l] = sd * 1;
			// bei 1 Min -> 1.3
			// bei 10 Min -> 0.65
		}

	}

	private double calculateStandardDeviation(double avgTT, double[] ttArray) {
		// expTT <- is expected to be the average
		double variance = 0;

//		Take each number and subtract the mean, then square the result. 
//		Finally, divide by the length of the array and add that to your sum. 
		for (int i = 0; i < ttArray.length; i++) {
			variance += (ttArray[i] - avgTT) * (ttArray[i] - avgTT) / ttArray.length;
		}

//		The sum is the variance, and the square root of that is the standard deviation: 
		double sd = Math.sqrt(variance);
		return sd;
	}

	public void runLP() {

//		scenarioProb = getEqualScenarioProb(trips[0].delay.length);
		scenarioProb = getEqualScenarioProb(scenarioCount);

		trips = new GenLpSolver(ss, se, b, trips, scenarioProb, myMethod).solve();
		objectiveValue = trips[0].objectiveValue;
		tourDuration = trips[trips.length - 1].departure;

		bufferValues = new double[trips.length - 2];
		for (int i = 0; i < bufferValues.length; i++)
			bufferValues[i] = Math.round(
					trips[i + 2].departure - trips[i + 2].serviceTime - trips[i + 1].distance - trips[i + 1].departure);

		for (int i = 0; i < trips.length; i++)
			System.out.println("LoadAndSolveTest.runLP() s" + i + ":\t" + trips[i].departure);
		System.out.println("LoadAndSolveTest.runLP() obj:\t" + trips[0].objectiveValue);
	}

	private LpTrip[] createLpTrips(double[] expTT, double[][] delayScenarios) {

		int tripsFromCustomer = expTT.length;
		LpTrip[] trips = new LpTrip[tripsFromCustomer + 2]; // customerTrips + FromDepot + ToDepot

		// setup Depot start trip
		double[] noDelays = new double[scenarioCount];
		trips[0] = new LpTrip(0, 0, 0, 0, 0, noDelays); // Instant from Depot to first Location, without any delays

		// setup Customer trips
		double delayProbability = 1 / ((double) tripsFromCustomer); // p
		for (int i = 0; i < tripsFromCustomer; i++) {
			double[] delay = new double[scenarioCount];
			for (int s = 0; s < scenarioCount; s++)
				delay[s] = delayScenarios[s][i];

//TODO: Check that the right time window is taken
			trips[i + 1] = new LpTrip(expTT[i], w[i], u, t, delayProbability, delay);
		}

		// setup end trip to Depot
		trips[trips.length - 1] = new LpTrip(0, 0, 0, 0, 0, null); // Instant from last Location to Depot;
		return trips;
	}

	private static double[] getEqualScenarioProb(int number) {
		double[] scenarioProb = new double[number];
		for (int i = 0; i < number; i++)
			scenarioProb[i] = 1 / (double) number; // g
		return scenarioProb;
	}

	public void writeParameters() {
		try {
			File csvFile = new File(bufferDirectory + "/" + bufferIdent + "_parameters.csv");
			csvFile.getParentFile().mkdirs();
			FileWriter csvWriter = new FileWriter(csvFile);

			String str = "bufferIdent;" + bufferIdent + "\n";
			str += "se;" + se + "\n";
			str += "t;" + t + "\n";
			str += "b;" + b + "\n";
			str += "w;";
			for (double window : w)
				str += window + ";";
			str += "\n";

			str += "ss;" + ss + "\n";
			str += "u;" + u + "\n";
			str += "myMethod;" + myMethod + "\n";
			str += "expTT[];";
			for (double tt : expTT)
				str += tt + ";";
			str += "\n";

			str += "bufferValues[];";
			for (double buffer : bufferValues)
				str += buffer + ";";
			str += "\n";

			str += "objectiveValue ;" + objectiveValue + "\n";
			str += "tourDuration ;" + tourDuration + "\n\n";

			str += "scenarioCount;" + scenarioCount + "\n";
			str += "trips[];planedDeparture;distance;window;serviceTime;penaltyTardiness;delayProbability;";
			if (trips != null)
				for (int i = 0; i < scenarioCount; i++)
					str += "scenario" + i + ";";
			if (trips != null)
				for (LpTrip trip : trips) {
					str += "\n;" + trip.departure + ";" + trip.distance + ";" + trip.window + ";" + trip.serviceTime
							+ ";" + trip.penaltyTardiness + ";" + trip.delayProbability;
					if (trip.delay != null)
						for (double d : trip.delay)
							str += ";" + d;
				}

			csvWriter.append(str.replace(".", ","));

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void writeRunSettingsCSV() {
		try {
			File csvFile = new File(bufferDirectory + "/" + bufferIdent + "_runSettings.csv");
			csvFile.getParentFile().mkdirs();
			FileWriter csvWriter = new FileWriter(csvFile);

			csvWriter.append("directory;" + runSettings.directory + "\n");
			csvWriter.append("timeWindowMethod;" + runSettings.timeWindowMethod + "\n");
			csvWriter.append("serviceTime;" + runSettings.serviceTime + "\n");
			csvWriter.append("depot;" + runSettings.depot + "\n");
			csvWriter.append("tour;;");
			for (String linkID : runSettings.tour)
				csvWriter.append(linkID + ";");
			csvWriter.append("\n");
			csvWriter.append("expectedTravelTime;");
			for (double expTT : runSettings.expectedTravelTime)
				csvWriter.append(expTT + ";");
			csvWriter.append("\n");
			csvWriter.append("buffer;");
			for (double buffer : runSettings.buffer)
				csvWriter.append(buffer + ";");
			csvWriter.append("\n");
			csvWriter.append("timeWindow;;");
			for (double timeWindow : runSettings.timeWindow)
				csvWriter.append(timeWindow + ";");
			csvWriter.append("\n");

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void generateRunSettings() {
		runSettings = new Settings();
		runSettings.directory = bufferDirectory + "/matsimData/";
		runSettings.tour = linkIDs;
		runSettings.serviceTime = u;
		runSettings.timeWindow = w;
		runSettings.expectedTravelTime = expTT;
		runSettings.buffer = bufferValues;
		addPseudoDepot(runSettings, linkIDs[0], 0, 0);
		writeRunSettingsCSV();
	}

	private void addPseudoDepot(Settings settings, String depotLinkID, int expectedTravelTime, int buffer) {
		settings.depot = depotLinkID;
		settings.expectedTravelTime = addFirstIndex(settings.expectedTravelTime, expectedTravelTime);
		settings.buffer = addFirstIndex(settings.buffer, buffer);
	}

	private double[] addFirstIndex(double[] array, double value) {
		double[] newArray = new double[array.length + 1];
		newArray[0] = value;
		for (int i = 0; i < array.length; i++)
			newArray[i + 1] = array[i];
		return newArray;
	}

	public void readRunSettings() {
		try {
			BufferedReader csvReader = new BufferedReader(
					new FileReader(bufferDirectory + "/" + bufferIdent + "_runSettings.csv"));
			runSettings = new Settings();
			String row;
			while ((row = csvReader.readLine()) != null) {
				String[] data = row.split(";");

				if ("directory".equals(data[0]))
					runSettings.directory = data[1];
				if ("timeWindowMethod".equals(data[0]))
					runSettings.timeWindowMethod = data[1];
				if ("serviceTime".equals(data[0]))
					runSettings.serviceTime = Double.parseDouble(data[1]);
				if ("depot".equals(data[0]))
					runSettings.depot = data[1];

				if ("tour".equals(data[0])) {
					String[] linkIDs = new String[data.length - 2];
					for (int i = 0; i < linkIDs.length; i++)
						linkIDs[i] = data[i + 2];
					runSettings.tour = linkIDs;
				}

				if ("expectedTravelTime".equals(data[0])) {
					double[] expTT = new double[data.length - 1];
					for (int i = 0; i < expTT.length; i++)
						expTT[i] = Double.parseDouble(data[i + 1]);
					runSettings.expectedTravelTime = expTT;
				}

				if ("buffer".equals(data[0])) {
					double[] buffer = new double[data.length - 1];
					for (int i = 0; i < buffer.length; i++)
						buffer[i] = Double.parseDouble(data[i + 1]);
					runSettings.buffer = buffer;
				}

				if ("timeWindow".equals(data[0])) {
					double[] timeWindow = new double[data.length - 2];
					for (int i = 0; i < timeWindow.length; i++)
						timeWindow[i] = Double.parseDouble(data[i + 2]);
					runSettings.timeWindow = timeWindow;
				}
			}
			csvReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
