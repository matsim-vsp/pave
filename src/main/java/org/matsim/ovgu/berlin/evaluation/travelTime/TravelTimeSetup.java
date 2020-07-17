package org.matsim.ovgu.berlin.evaluation.travelTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.matsim.ovgu.berlin.createTravelTimeMatrix.Version3_RunSimReadEvents;
import org.matsim.ovgu.berlin.evaluation.model.EvTour;

public class TravelTimeSetup {

	public static void runTravelTimeSimulations(EvTour tour, boolean readEventsOnly) {
		tour.traveltimeMatrix = Version3_RunSimReadEvents.run(tour.linkIDs, tour.tourDirectory + "/matrixData",
				!readEventsOnly, false, true);
		calcAggTravelTimes(tour);
		writeTravelTimesCSV(tour);
	}

	public static void readTravelTimes(EvTour tour) {
		int arcs = tour.linkIDs.length - 1;
		tour.traveltimeMatrix = new double[arcs][24];
		tour.minTravelTime = new double[arcs];
		tour.avgTravelTime = new double[arcs];
		tour.maxTravelTime = new double[arcs];
		readTravelTimesCSV(tour);
	}

	private static void readTravelTimesCSV(EvTour tour) {
		try {
			BufferedReader csvReader = new BufferedReader(
					new FileReader(tour.tourDirectory + "/" + tour.tourIdent + "_traveltimes.csv"));
			String firstRow = csvReader.readLine();
			String row;
			int rowCounter = 0;
			while ((row = csvReader.readLine()) != null) {
				String[] data = row.replace(",", ".").split(";");
				for (int i = 0; i < 24; i++)
					tour.traveltimeMatrix[rowCounter][i] = Double.parseDouble(data[i + 3]);

				tour.minTravelTime[rowCounter] = Double.parseDouble(data[28]);
				tour.avgTravelTime[rowCounter] = Double.parseDouble(data[29]);
				tour.maxTravelTime[rowCounter] = Double.parseDouble(data[30]);
				rowCounter++;
			}
			csvReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

//	public TravelTimeSetup(EvTour tour, boolean runSimulation) {
//		tour.traveltimeMatrix = Version3_RunSimReadEvents.run(tour.linkIDs, tour.tourDirectory + "/matrixData",
//				runSimulation, false, true);
//		calcAggTravelTimes(tour);
//		writeTravelTimesCSV(tour);
//	}

	private static void writeTravelTimesCSV(EvTour tour) {
		try {
			File csvFile = new File(tour.tourDirectory + "/" + tour.tourIdent + "_traveltimes.csv");
			csvFile.getParentFile().mkdirs();
			FileWriter csvWriter = new FileWriter(csvFile);
			csvWriter.append("from;to;;0;1;2;3;4;5;6;7;8;9;10;11;12;13;14;15;16;17;18;19;20;21;22;23;;MIN;AVG;MAX\n");

			for (int i = 0; i < tour.linkIDs.length - 1; i++) {
				String str = tour.linkIDs[i] + ";" + tour.linkIDs[i + 1] + ";;";
				for (double traveltime : tour.traveltimeMatrix[i])
					str += traveltime + ";";
				str += ";" + tour.minTravelTime[i] + ";" + tour.avgTravelTime[i] + ";" + tour.maxTravelTime[i];
				csvWriter.append(str.replace(".", ",") + "\n");
			}

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void calcAggTravelTimes(EvTour tour) {
		tour.minTravelTime = new double[tour.traveltimeMatrix.length];
		tour.avgTravelTime = new double[tour.traveltimeMatrix.length];
		tour.maxTravelTime = new double[tour.traveltimeMatrix.length];

		for (int l = 0; l < tour.traveltimeMatrix.length; l++) {
			double min = Double.MAX_VALUE;
			double max = 0;
			double sum = 0;
			for (int h = 0; h < tour.traveltimeMatrix[l].length; h++) {
				if (tour.traveltimeMatrix[l][h] < min)
					min = tour.traveltimeMatrix[l][h];
				if (tour.traveltimeMatrix[l][h] > max)
					max = tour.traveltimeMatrix[l][h];
				sum += tour.traveltimeMatrix[l][h];
			}
			double avg = sum / tour.traveltimeMatrix[l].length;
			tour.minTravelTime[l] = min;
			tour.avgTravelTime[l] = avg;
			tour.maxTravelTime[l] = max;
		}
	}
}
