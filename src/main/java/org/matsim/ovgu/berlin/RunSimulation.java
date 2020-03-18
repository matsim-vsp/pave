package org.matsim.ovgu.berlin;

import org.matsim.ovgu.berlin.input.Input;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class RunSimulation {

	public static void main(String[] args) {

		String pathChangeEvents = "input/10pc/scenario-A.15.networkChangeEvents.xml.gz";
		String pathOutput = "OutputKMT/OVGU/10pc/";

		runTour(pathChangeEvents, pathOutput);
		runTourXL(pathChangeEvents, pathOutput);
	}

	private static void runTour(String pathChangeEvents, String pathOutput) {

		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/minStandard/", Input.tour, Input.minTT,
				Input.serviceTime, Input.standardTW, Input.noStaticBuffer, Input.noAdaptiveBuffer);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/minPremium/", Input.tour, Input.minTT, Input.serviceTime,
				Input.premiumTW, Input.noStaticBuffer, Input.noAdaptiveBuffer);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/avgStandard/", Input.tour, Input.avgTT,
				Input.serviceTime, Input.standardTW, Input.noStaticBuffer, Input.noAdaptiveBuffer);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/avgPremium/", Input.tour, Input.avgTT, Input.serviceTime,
				Input.premiumTW, Input.noStaticBuffer, Input.noAdaptiveBuffer);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/maxStandard/", Input.tour, Input.maxTT,
				Input.serviceTime, Input.standardTW, Input.noStaticBuffer, Input.noAdaptiveBuffer);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/maxPremium/", Input.tour, Input.maxTT, Input.serviceTime,
				Input.premiumTW, Input.noStaticBuffer, Input.noAdaptiveBuffer);
	}

	private static void runTourXL(String pathChangeEvents, String pathOutput) {

		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/xl/minStandard/", Input.lTour, Input.lMinTT,
				Input.serviceTime, Input.standardTW, Input.noStaticBuffer, Input.noAdaptiveBuffer);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/xl/minPremium/", Input.lTour, Input.lMinTT,
				Input.serviceTime, Input.premiumTW, Input.noStaticBuffer, Input.noAdaptiveBuffer);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/xl/avgStandard/", Input.lTour, Input.lAvgTT,
				Input.serviceTime, Input.standardTW, Input.noStaticBuffer, Input.noAdaptiveBuffer);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/xl/avgPremium/", Input.lTour, Input.lAvgTT,
				Input.serviceTime, Input.premiumTW, Input.noStaticBuffer, Input.noAdaptiveBuffer);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/xl/maxStandard/", Input.lTour, Input.lMaxTT,
				Input.serviceTime, Input.standardTW, Input.noStaticBuffer, Input.noAdaptiveBuffer);
		new FreightOnlyMatsim(pathChangeEvents, pathOutput + "/xl/maxPremium/", Input.lTour, Input.lMaxTT,
				Input.serviceTime, Input.premiumTW, Input.noStaticBuffer, Input.noAdaptiveBuffer);
	}

}
