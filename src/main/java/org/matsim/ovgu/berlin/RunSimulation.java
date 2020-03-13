package org.matsim.ovgu.berlin;

import org.matsim.ovgu.berlin.input.Input;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class RunSimulation {

	public static void main(String[] args) {

		String pathChangeEvents = "input/10pc/scenario-A.15.networkChangeEvents.xml.gz";
		String pathOutput = "OutputKMT/OVGU/10pc/";

		String buffer = "buffer5P";

		runTour(pathChangeEvents, pathOutput + buffer);
		RunEventHandler.handleTourEvents(buffer);
//		runTourXL(pathChangeEvents, pathOutput);

//		runMatrixTour(pathChangeEvents, pathOutput);
	}

	private static void runTour(String pathChangeEvents, String pathOutput) {

		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/minStandard/", Input.tour, Input.minTT,
				Input.standardTWs);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/minPremium/", Input.tour, Input.minTT, Input.premiumTWs);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/avgStandard/", Input.tour, Input.avgTT,
				Input.standardTWs);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/avgPremium/", Input.tour, Input.avgTT, Input.premiumTWs);
//		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/maxStandard/", Input.tour, Input.maxTT,
//				Input.standardTWs);
//		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/maxPremium/", Input.tour, Input.maxTT, Input.premiumTWs);
	}

	private static void runTourXL(String pathChangeEvents, String pathOutput) {

		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/xl/minStandard/", Input.lTour, Input.lMinTT,
				Input.standardTWs);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/xl/minPremium/", Input.lTour, Input.lMinTT,
				Input.premiumTWs);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/xl/avgStandard/", Input.lTour, Input.lAvgTT,
				Input.standardTWs);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/xl/avgPremium/", Input.lTour, Input.lAvgTT,
				Input.premiumTWs);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/xl/maxStandard/", Input.lTour, Input.lMaxTT,
				Input.standardTWs);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/xl/maxPremium/", Input.lTour, Input.lMaxTT,
				Input.premiumTWs);
	}

	private static void runMatrixTour(String pathChangeEvents, String pathOutput) {

		for (int x = 1; x < Input.tour.length; x++) {
			String depot = Input.tour[x - 1];
			String[] myTour = new String[] { Input.tour[x - 1], Input.tour[x] };
			double[] myTT = new double[] { Input.avgTT[x - 1], Input.avgTT[x] };
			double[] myTW = new double[] { 0.0, 0.0 };
			double serviceTime = 0.0;
			new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/" + x + "/", myTour, myTT, myTW, serviceTime, depot);
		}
	}

}
