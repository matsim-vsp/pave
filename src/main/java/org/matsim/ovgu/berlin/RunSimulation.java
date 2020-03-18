package org.matsim.ovgu.berlin;

import org.matsim.ovgu.berlin.input.Input;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class RunSimulation {

	private static final int percent = 10;
	private static final String pathChangeEvents = "input/" + percent + "pc/scenario-A.15.networkChangeEvents.xml.gz";
	private static final String directory = "OutputKMT/OVGU/" + percent + "pc/";

	public static void main(String[] args) {

		run("05M", Input.buffer05M);
		run("5P", Input.buffer5P);
		run("SD", Input.bufferSD);
		run("SDcum", Input.bufferSDcum);
		run("SDcumStep", Input.bufferSDcumStep);
		
		run("1P", Input.buffer1P);
		run("2P", Input.buffer2P);
		run("3P", Input.buffer3P);
		run("4P", Input.buffer4P);
		
		run("Min_3P", Input.bufferMin_3P);
		run("SD_3P", Input.bufferSD_3P);
		run("SDcum_3P", Input.bufferSDcum_3P);
		run("SDcumStep_3P", Input.bufferSDcumStep_3P);

		run("5P_05M", Input.buffer5P_05M);
		run("SD_05M", Input.bufferSD_05M);
		run("SDcum_05M", Input.bufferSDcum_05M);
		run("SDcumStep_05M", Input.bufferSDcumStep_05M);

//		runMatrixTour(pathChangeEvents, pathOutput);
	}

	private static void run(String bufferName, double[] usedBuffer) {
		Input.usedBuffer = usedBuffer;
		
		Input.subtractBuffer = false;
		runStandardPremiumEvaluation(bufferName, Input.tour, Input.minTT, "min");
		runStandardPremiumEvaluation(bufferName, Input.tour, Input.avgTT, "avg");

		Input.subtractBuffer = true;
		runStandardPremiumEvaluation(bufferName, Input.tour, Input.maxTT, "max");
		
		
		// want work because arrays are not compatible
//		runStandardPremiumEvaluation(bufferName, Input.lTour, Input.lMinTT, "min");
//		runStandardPremiumEvaluation(bufferName, Input.lTour, Input.lAvgTT, "avg");
//		runStandardPremiumEvaluation(bufferName, Input.lTour, Input.lMaxTT, "max");
	}

	private static void runStandardPremiumEvaluation(String bufferName, String[] tour, double[] travelTime,
			String prefix) {
		runSimEvaluation(prefix + "Standard", bufferName, tour, travelTime, Input.standardTWs);
		runSimEvaluation(prefix + "Premium", bufferName, tour, travelTime, Input.premiumTWs);
	}

	private static void runSimEvaluation(String mode, String bufferName, String[] tour, double[] travelTime,
			double[] timewindows) {
		String simFolder = directory + "/" + bufferName + "/" + mode + bufferName;
		new FreightOnlyMatsim(pathChangeEvents, simFolder, tour, travelTime, timewindows);
		RunEventHandler.generateCSV(simFolder, mode + bufferName, travelTime, timewindows);
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
