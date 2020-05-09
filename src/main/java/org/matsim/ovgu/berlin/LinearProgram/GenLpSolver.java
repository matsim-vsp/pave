package org.matsim.ovgu.berlin.LinearProgram;

import java.util.Map.Entry;

import scpsolver.problems.LPSolution;
import scpsolver.problems.LPWizard;

public class GenLpSolver {

	boolean myMethod = false;
	boolean myMethod2 = false;
	private LpTrip[] trips;

	private int customerCount; // nr
	private int tripsCount;
	private int scenariosCount;
	private double shiftStart; // ss
	private double shiftEnd; // se
	private double penaltyOvertime; // b
	private double[] penaltyTardiness; // t -> i
	private double[] distance; // d -> i
	private double[] window; // W -> i
	private double[] serviceTime; // u -> i
	private double[] delayProbability; // p -> i
	private double sumProbability;
	private double[] scenarioProbability; // g -> k
	private double[][] delay; // l -> i/k

	// i element of trips
	// k element of scenarios
	// define tour: depot -> customer 0 -> customer N -> depot
	public GenLpSolver(double shiftStart, double shiftEnd, double penaltyOvertime, LpTrip[] trips,
			double[] scenarioProbability, boolean myMethod) {

		this.myMethod = myMethod;
		this.trips = trips;

		this.tripsCount = trips.length;
		this.scenariosCount = scenarioProbability.length;
		this.customerCount = trips.length - 2;
		this.shiftStart = shiftStart;
		this.shiftEnd = shiftEnd;
		this.penaltyOvertime = penaltyOvertime;
		this.scenarioProbability = scenarioProbability;

		this.distance = new double[trips.length];
		this.window = new double[trips.length];
		this.serviceTime = new double[trips.length];
		this.penaltyTardiness = new double[trips.length];
		this.delayProbability = new double[trips.length];
		this.delay = new double[trips.length - 1][scenarioProbability.length];

		for (int i = 0; i < trips.length; i++) {

			this.distance[i] = trips[i].distance;
			this.window[i] = trips[i].window;
			this.serviceTime[i] = trips[i].serviceTime;
			this.penaltyTardiness[i] = trips[i].penaltyTardiness;
			this.delayProbability[i] = trips[i].delayProbability;
			this.sumProbability += trips[i].delayProbability;

			if (i < trips.length - 1)
				for (int k = 0; k < delay[i].length; k++)
					this.delay[i][k] = trips[i].delay[k];
		}

		if (sumProbability < 1)
			System.out.println(
					"GenLpSolver.init() Info cumulative delay probabiliy must be < 1 (" + sumProbability + ")");

		if (sumProbability > 1)
			System.out.println("GenLpSolver.init() Error cumulative delay probabiliy must be <= 1");
	}

	public LpTrip[] solve() {
		calculateDepartureConstraintValues();
		calculateZetaConstraintValues();
		calculateLambdaConstraintValues();
		calculateDeltaConstraintValues();
		setupLP();
		LPSolution sol = solveLP();

		for (int i = 0; i < trips.length; i++) {
			trips[i].departure = sol.getDouble("Si" + i);
			trips[i].objectiveValue = sol.getObjectiveValue();
		}

		return trips;
	}

	// departure constraint values s
	// ss <= s(0)
	// d(i-1) + u(i) <= s(i) - s(i-1)
	private double departure_cv[];

	private void calculateDepartureConstraintValues() {
		departure_cv = new double[tripsCount];
		departure_cv[0] = shiftStart;
		for (int i = 1; i < tripsCount; i++)
			departure_cv[i] = distance[i - 1] + serviceTime[i];
	}

	// lambda constraint values
	// -se <= lambda(ik) - s(nr+1) - delta(inr+1k)
	private double lambda_cv;

	private void calculateLambdaConstraintValues() {
		lambda_cv = shiftEnd * -1;
	}

	// zeta constraint values
	// se >= s(nr+1) - zeta
	private double zeta_cv;

	private void calculateZetaConstraintValues() {
		zeta_cv = shiftEnd;
	}

	// delta constraint values
	// d(i) + l(ik) + sum(ij) + u(j) - W(j) <= s(j) + delta(ijk) - s(i)
	// sum(ij)
	// delta(ijk)
	private double sum[][];
	private double sumWithK[][][];
	private double delta_cv[][][];

	private void calculateDeltaConstraintValues() {
		delta_cv = new double[tripsCount][tripsCount][scenariosCount];

		if (myMethod) {
			sumWithK = new double[tripsCount][tripsCount][scenariosCount];
			for (int k = 0; k < scenariosCount; k++)
				for (int i = 0; i < tripsCount - 1; i++)
					for (int j = 1; j < tripsCount; j++)
						for (int m = i + 1; m < j; m++)
							sumWithK[i][j][k] += serviceTime[m] + distance[m] + delay[m][k];

			for (int k = 0; k < scenariosCount; k++)
				for (int i = 0; i < tripsCount - 1; i++)
					for (int j = i + 1; j < tripsCount; j++)
						delta_cv[i][j][k] = distance[i] + delay[i][k] + sumWithK[i][j][k] + serviceTime[j] - window[j];
		} else {
			sum = new double[tripsCount][tripsCount];
			for (int i = 0; i < tripsCount - 1; i++)
				for (int j = 1; j < tripsCount; j++)
					for (int m = i + 1; m < j; m++)
						sum[i][j] += serviceTime[m] + distance[m];

			for (int k = 0; k < scenariosCount; k++)
				for (int i = 0; i < tripsCount - 1; i++)
					for (int j = i + 1; j < tripsCount; j++)
						delta_cv[i][j][k] = distance[i] + delay[i][k] + sum[i][j] + serviceTime[j] - window[j];
		}
	}

	private LPWizard lpw;

	private void setupLP() {
		lpw = new LPWizard();

		// departure constraints
		// d(i-1) + u(i) <= s(i) - s(i-1)
		lpw.addConstraint("c_Si0nn", 0, "<=").plus("Si0");
		lpw.addConstraint("c_Si0", departure_cv[0], "<=").plus("Si0");

		for (int i = 1; i < tripsCount; i++)
			lpw.addConstraint("c_si" + i, departure_cv[i], "<=").plus("Si" + i).plus("Si" + (i - 1), -1);

		// se >= s(nr+1) - zeta
		lpw.addConstraint("c_Z", zeta_cv, ">=").plus("Si" + (customerCount + 1)).plus("Z", -1);
		lpw.addConstraint("c_Znn", 0, "<=").plus("Z");
		lpw.plus("Z", penaltyOvertime * (1 - sumProbability));

		// -se <= lambda(ik) - s(nr+1) - delta(inr+1k)
		for (int i = 0; i < tripsCount - 1; i++)
			for (int k = 0; k < scenariosCount; k++) {
				lpw.addConstraint("c_Li" + i + "k" + k, lambda_cv, "<=").plus("Li" + i + "k" + k)
						.plus("Si" + (customerCount + 1), -1).plus("Di" + i + "j" + (customerCount + 1) + "k" + k, -1);
				lpw.addConstraint("c_Li" + i + "k" + k + "nn", 0, "<=").plus("Li" + i + "k" + k);
				lpw.plus("Li" + i + "k" + k, delayProbability[i] * scenarioProbability[k] * penaltyOvertime);
			}

		// d(i) + l(ik) + SUMME + u(j) - W(j) <= s(j) + delta(ijk) - s(i)
		for (int k = 0; k < scenariosCount; k++) {
			int iTill = tripsCount - 1;
			if(myMethod2)
				iTill = 1;
			
			for (int i = 0; i < iTill; i++)
				for (int j = 1; j < tripsCount; j++) {
					if (i < j) {
						lpw.addConstraint("c_Di" + i + "j" + j + "k" + k, delta_cv[i][j][k], "<=").plus("Si" + j)
								.plus("Di" + i + "j" + j + "k" + k).plus("Si" + i, -1);
						lpw.addConstraint("c_Di" + i + "j" + j + "k" + k + "nn", 0, "<=")
								.plus("Di" + i + "j" + j + "k" + k);
						if(myMethod2)
							lpw.plus("Di" + i + "j" + j + "k" + k, scenarioProbability[k] * penaltyTardiness[i]);
						else
							lpw.plus("Di" + i + "j" + j + "k" + k,
									delayProbability[i] * scenarioProbability[k] * penaltyTardiness[i]);
					}
				}
		}
	}

	private LPSolution solveLP() {
		lpw.setMinProblem(true);
		LPSolution sol = lpw.solve();
		for (Entry<String, Integer> entry : lpw.getLP().getIndexmap().entrySet())
			if (sol.getDouble(entry.getKey()) > 0 && entry.getKey().contains("D"))
				System.out.println(entry.getKey() + "\t" + sol.getDouble(entry.getKey()));
		return sol;
	}
}
