package org.matsim.ovgu.berlin.createTravelTimeMatrix;

import java.io.FileWriter;
import java.io.IOException;

import org.matsim.api.core.v01.network.Node;
import org.matsim.core.router.util.LeastCostPathCalculator.Path;
import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class Version2_RunSimReadSim {

	public static void run(String[] tour) {
		Settings settings = new Settings();
		settings.directory += "/Version2_RunSimReadSim/";
		
		// Run Simulation empty
		FreightOnlyMatsim sim = new FreightOnlyMatsim(settings);
		
		// Read Simulation
		settings.tour = tour;
		settings.depot = tour[0];
		printMatrix(sim, settings);
	}

	private static void printMatrix(FreightOnlyMatsim sim, Settings settings) {
		try {
			System.out.println("Version2_RunSimReadSim.printMatrix() start matrix calculation");
			FileWriter csvWriter = new FileWriter(settings.directory + "matrix.csv");
			csvWriter.append("from; to; hour; time; distance; path\n");
			// do it
			for (int x = 0; x < settings.tour.length; x++) {
				for (int y = 0; y < settings.tour.length; y++) {
					String idFrom = settings.tour[x];
					String idTo = settings.tour[y];
					print(sim, idFrom, idTo, csvWriter);
				}
			}

			// finish
			csvWriter.flush();
			csvWriter.close();
			System.out.println("Version2_RunSimReadSim.printMatrix() finished matrix calculation");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void print(FreightOnlyMatsim calc, String fromLink, String toLink, FileWriter csvWriter)
			throws IOException {
		printPerHour(calc, fromLink, toLink, 0, csvWriter);
		printPerHour(calc, fromLink, toLink, 1, csvWriter);
		printPerHour(calc, fromLink, toLink, 2, csvWriter);
		printPerHour(calc, fromLink, toLink, 3, csvWriter);
		printPerHour(calc, fromLink, toLink, 4, csvWriter);
		printPerHour(calc, fromLink, toLink, 5, csvWriter);
		printPerHour(calc, fromLink, toLink, 6, csvWriter);
		printPerHour(calc, fromLink, toLink, 7, csvWriter);
		printPerHour(calc, fromLink, toLink, 8, csvWriter);
		printPerHour(calc, fromLink, toLink, 9, csvWriter);
		printPerHour(calc, fromLink, toLink, 10, csvWriter);
		printPerHour(calc, fromLink, toLink, 11, csvWriter);
		printPerHour(calc, fromLink, toLink, 12, csvWriter);
		printPerHour(calc, fromLink, toLink, 13, csvWriter);
		printPerHour(calc, fromLink, toLink, 14, csvWriter);
		printPerHour(calc, fromLink, toLink, 15, csvWriter);
		printPerHour(calc, fromLink, toLink, 16, csvWriter);
		printPerHour(calc, fromLink, toLink, 17, csvWriter);
		printPerHour(calc, fromLink, toLink, 18, csvWriter);
		printPerHour(calc, fromLink, toLink, 19, csvWriter);
		printPerHour(calc, fromLink, toLink, 20, csvWriter);
		printPerHour(calc, fromLink, toLink, 21, csvWriter);
		printPerHour(calc, fromLink, toLink, 22, csvWriter);
		printPerHour(calc, fromLink, toLink, 23, csvWriter);
	}

	private static void printPerHour(FreightOnlyMatsim sim, String fromLink, String toLink, int hour,
			FileWriter csvWriter) throws IOException {
		double starttime = hour * 3600;
		Path path = sim.getTravelPathWithFromNodes(fromLink, toLink, starttime);
		String str = fromLink + "; " + toLink + "; " + hour + "; " + path.travelTime + "; " + path.travelCost + ";  ";
		for (Node node : path.nodes) {
			str = str + node.getId() + ",";
		}
		str = str.substring(0, str.length() - 1) + "\n";
		csvWriter.append(str.replace(".", ","));
	}

}
