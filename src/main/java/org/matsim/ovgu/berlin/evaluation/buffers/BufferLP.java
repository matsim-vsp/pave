package org.matsim.ovgu.berlin.evaluation.buffers;

import org.matsim.ovgu.berlin.LinearProgram.GenLpSolver;
import org.matsim.ovgu.berlin.LinearProgram.LpTrip;
import org.matsim.ovgu.berlin.evaluation.model.EvBuffer;
import org.matsim.ovgu.berlin.evaluation.model.EvTour;
import org.matsim.ovgu.berlin.evaluation.model.EvVariant;

public class BufferLP {

	protected static EvVariant init(String name, double[] expTT, EvTour tour) {
		EvVariant lp = new EvVariant("LP", tour.tourDirectory, tour.tourIdent + "_LP"+name,
				expTT, tour.linkIDs);
		
		lp.delayScenarios = calcDelayScenarios(tour.traveltimeMatrix, lp.expTT);
		
		if ("avg".equals(name))
			lp.delayScenarios = removeNegativScenarioValues(lp.delayScenarios);
		
		lp.writeScenariosCSV();
		tour.evBufferVariants.add(lp);
		return lp;
	}

	private static double[][] removeNegativScenarioValues(double[][] delayScenarios) {
		int szenariosCount = delayScenarios.length;
		int linksCount = delayScenarios[0].length;

		for (int s = 0; s < szenariosCount; s++)
			for (int l = 0; l < linksCount; l++)
				if (delayScenarios[s][l] < 0)
					delayScenarios[s][l] = 0;
		return delayScenarios;
	}

	private static double[][] calcDelayScenarios(double[][] traveltimeMatrix, double[] expTT) {
		int linksCount = traveltimeMatrix.length;
		int szenariosCount = traveltimeMatrix[0].length;
		double[][] delayScenarios = new double[szenariosCount][linksCount];

		for (int s = 0; s < szenariosCount; s++)
			for (int l = 0; l < linksCount; l++)
				delayScenarios[s][l] = traveltimeMatrix[l][s] - expTT[l];
		return delayScenarios;
	}

	protected static void runLP(EvBuffer buffer, double[][] delayScenarios) {

		buffer.trips = createLpTrips(buffer, delayScenarios);

//		buffer.scenarioProb = getEqualScenarioProb(trips[0].delay.length);
		buffer.scenarioProb = getEqualScenarioProb(buffer.scenarioCount);

		buffer.trips = new GenLpSolver(buffer.ss, buffer.se, buffer.b, buffer.trips, buffer.scenarioProb, buffer.myMethod).solve();
		buffer.objectiveValue = buffer.trips[0].objectiveValue;
		buffer.tourDuration = buffer.trips[buffer.trips.length - 1].departure;

		buffer.bufferValues = new double[buffer.trips.length - 2];
		for (int i = 0; i < buffer.bufferValues.length; i++)
			buffer.bufferValues[i] = Math.round(
					buffer.trips[i + 2].departure - buffer.trips[i + 2].serviceTime - buffer.trips[i + 1].distance - buffer.trips[i + 1].departure);

		for (int i = 0; i < buffer.trips.length; i++)
			System.out.println("LoadAndSolveTest.runLP() s" + i + ":\t" + buffer.trips[i].departure);
		System.out.println("LoadAndSolveTest.runLP() obj:\t" + buffer.trips[0].objectiveValue);
	}

	private static LpTrip[] createLpTrips(EvBuffer buffer, double[][] delayScenarios) {

		int tripsFromCustomer = buffer.expTT.length;
		LpTrip[] trips = new LpTrip[tripsFromCustomer + 2]; // customerTrips + FromDepot + ToDepot

		// setup Depot start trip
		double[] noDelays = new double[buffer.scenarioCount];
		trips[0] = new LpTrip(0, 0, 0, 0, 0, noDelays); // Instant from Depot to first Location, without any delays

		// setup Customer trips
		double delayProbability = 1 / ((double) tripsFromCustomer); // p
		for (int i = 0; i < tripsFromCustomer; i++) {
			double[] delay = new double[buffer.scenarioCount];
			for (int s = 0; s < buffer.scenarioCount; s++)
				delay[s] = delayScenarios[s][i];

//TODO: Check that the right time window is taken
			trips[i + 1] = new LpTrip(buffer.expTT[i], buffer.w[i], buffer.u, buffer.t, delayProbability, delay);
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

}
