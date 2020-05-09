package org.matsim.ovgu.berlin.evaluation;

import java.util.ArrayList;
import java.util.List;

public class EvInputVersion {
	public EvInputVersion(String versionName, double[] expTravelTimes) {
		this.versionName = versionName;
		this.expTT = expTravelTimes;
	}

	public String versionName;
	private double[] expTT;
	private double[][] delayScenarios;
	public List<EvBufferVersion> buffers = new ArrayList<EvBufferVersion>();

	// without delay
	public double getBestCaseDuration(double serviceTime) {
		double expDuration = 0;
		for (double tt : expTT)
			expDuration += tt + serviceTime;
		return expDuration;
	}

	public void setupTimeWindowBuffers(double se, double t, boolean myMethod) {
		double[] windows = new double[] { 1, 2/*, 3, 4, 5, 6, 7, 8, 9, 10 */};
		double factor = 60;
		double b = 1;
		double ss = 0;
		double u = 2 * factor;

		for (double window : windows) {
			double w = window * factor;
			buffers.add(new EvBufferVersion(versionName + "_W" + w, se, t, b, w, ss, u, myMethod, expTT, delayScenarios));
		}
	}

	public void setupTimeWindowBuffers_SD() {
		double[] windows = new double[] { 1, 2/* , 3, 4, 5, 6, 7, 8, 9, 10 */ };
		double factor = 60;
		double u = 2 * factor;

		for (double window : windows) {
			double w = window * factor;
			buffers.add(new EvBufferVersion(versionName + "_W" + w, w, u, expTT));
		}
	}

	public void calculateSD_ForAllBuffers(double[] avgTT, double[][] traveltimeMatrix) {
		for (EvBufferVersion buffer : buffers)
			buffer.calculateStandardDeviationBuffer(avgTT, traveltimeMatrix);
	}

	public void runLp_ForAllBuffers() {
		for (EvBufferVersion buffer : buffers)
			buffer.runLP();
	}

	public void calcDelayScenarios(double[][] traveltimeMatrix) {
		int linksCount = traveltimeMatrix.length;
		int szenariosCount = traveltimeMatrix[0].length;
		delayScenarios = new double[szenariosCount][linksCount];

		for (int s = 0; s < szenariosCount; s++)
			for (int l = 0; l < linksCount; l++)
				delayScenarios[s][l] = traveltimeMatrix[l][s] - expTT[l];
	}

	public void removeNegativScenarioValues() {
		int szenariosCount = delayScenarios.length;
		int linksCount = delayScenarios[0].length;

		for (int s = 0; s < szenariosCount; s++)
			for (int l = 0; l < linksCount; l++)
				if (delayScenarios[s][l] < 0)
					delayScenarios[s][l] = 0;
	}

}
