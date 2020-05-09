package org.matsim.ovgu.berlin.evaluation;

import org.matsim.ovgu.berlin.LinearProgram.GenLpSolver;
import org.matsim.ovgu.berlin.LinearProgram.LpTrip;

public class EvBufferVersion {

	EvBufferVersion(String bufferName, double se, double t, double b, double w, double ss, double u, boolean myMethod,
			double[] expTT, double[][] delayScenarios) {
		this.bufferName = bufferName;
		this.se = se;
		this.t = t;
		this.b = b;
		this.w = w;
		this.ss = ss;
		this.u = u;
		this.myMethod = myMethod;
		this.expTT = expTT;
		this.trips = createLpTrips(expTT, delayScenarios);
	}

	EvBufferVersion(String bufferName, double w, double u, double[] expTT) {
		this.bufferName = bufferName;
		this.w = w;
		this.u = u;
		this.expTT = expTT;
	}

	public String bufferName;
	private double se;
	private double t;
	private double b;
	public double w;
	private double ss;
	public double u;
	private boolean myMethod;
	public double[] expTT;
	private LpTrip[] trips;

	public double[] bufferValues;
	private double objectiveValue;
	private double tourDuration;

	public void calculateStandardDeviationBuffer(double[] avgTT, double[][] traveltimeMatrix) {
		int linksCount = traveltimeMatrix.length;
		int szenariosCount = traveltimeMatrix[0].length;


		// add one buffer extra at the pseudo depot; buffer = 0
		bufferValues = new double[linksCount+1];

		for (int l = 0; l < linksCount; l++) {
			double[] ttArray = new double[szenariosCount];
			for (int s = 0; s < szenariosCount; s++)
				ttArray[s] = traveltimeMatrix[l][s];
			bufferValues[l+1] = calculateStandardDeviation(avgTT[l], ttArray);
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

		double[] scenarioProb = getEqualScenarioProb(trips[0].delay.length);

		trips = new GenLpSolver(ss, se, b, trips, scenarioProb, myMethod).solve();
		bufferValues = new double[trips.length-1];
		objectiveValue = trips[0].objectiveValue;
		tourDuration = trips[trips.length - 1].departure;

		for (int i = trips.length - 1; i > 1 ; i--)
			bufferValues[i-1] = Math
					.round(trips[i].departure - trips[i - 1].departure - trips[i].serviceTime - trips[i - 1].distance);

		for (int i = 0; i < trips.length; i++)
			System.out.println("LoadAndSolveTest.runLP() s" + i + ":\t" + trips[i].departure);
		System.out.println("LoadAndSolveTest.runLP() obj:\t" + trips[0].objectiveValue);
	}

	private LpTrip[] createLpTrips(double[] expTT, double[][] delayScenarios) {

		int tripsFromCustomer = expTT.length;
		int scenariosCount = delayScenarios.length;
		LpTrip[] trips = new LpTrip[tripsFromCustomer + 2]; // customerTrips + FromDepot + ToDepot

		// setup Depot start trip
		double[] noDelays = new double[scenariosCount];
		trips[0] = new LpTrip(0, 0, 0, 0, 0, noDelays); // Instant from Depot to first Location, without any delays

		// setup Customer trips
		double delayProbability = 1 / ((double) tripsFromCustomer); // p
		for (int i = 0; i < tripsFromCustomer; i++) {
			double[] delay = new double[scenariosCount];
			for (int s = 0; s < scenariosCount; s++)
				delay[s] = delayScenarios[s][i];

			trips[i + 1] = new LpTrip(expTT[i], w, u, t, delayProbability, delay);
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
