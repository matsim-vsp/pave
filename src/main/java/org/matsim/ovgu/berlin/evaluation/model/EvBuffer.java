package org.matsim.ovgu.berlin.evaluation.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.LinearProgram.GenLpSolver;
import org.matsim.ovgu.berlin.LinearProgram.LpTrip;

public class EvBuffer {

	public EvBuffer(String versionDirectory, String bufferIdent, double se, double t, double b, double[] w, double ss,
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
	public String[] linkIDs;
	public double se;
	public double t;
	public double b;
	public double[] w;
	public double ss;
	public double u;
	public boolean myMethod;
	public double[] expTT;
	public LpTrip[] trips;
	public int scenarioCount;
	public double[] scenarioProb;
	public double[] bufferValues;
	public double objectiveValue;
	public double tourDuration;
	public Settings runSettings;
	
	public void load() {
		loadRunSettings();
	}
	
	protected void write() {
		writeParameters();
		generateRunSettings();
		writeRunSettingsCSV();		
	}

	private void writeParameters() {
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

	private void writeRunSettingsCSV() {
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

	private void generateRunSettings() {
		runSettings = new Settings();
		runSettings.directory = bufferDirectory + "/matsimData/";
		runSettings.tour = linkIDs;
		runSettings.serviceTime = u;
		runSettings.timeWindow = w;
		runSettings.expectedTravelTime = expTT;
		runSettings.buffer = bufferValues;
		addPseudoDepot(runSettings, linkIDs[0], 0, 0);
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

	private void loadRunSettings() {
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
