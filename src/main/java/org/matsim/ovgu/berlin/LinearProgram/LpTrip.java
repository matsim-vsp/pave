package org.matsim.ovgu.berlin.LinearProgram;

public class LpTrip {

	public double distance; // d
	public double window; // W
	public double serviceTime; // u
	public double penaltyTardiness; // t
	public double delayProbability; // p
	public double[] delay; // l for each k (scenario)

	public LpTrip(double distance, double window, double serviceTime, double penaltyTardiness, double delayProbability,
			double[] delay) {

		this.distance = distance;
		this.window = window;
		this.serviceTime = serviceTime;
		this.penaltyTardiness = penaltyTardiness;
		this.delayProbability = delayProbability;
		this.delay = delay;
	}

	public double departure; // s
	public double objectiveValue;
}
