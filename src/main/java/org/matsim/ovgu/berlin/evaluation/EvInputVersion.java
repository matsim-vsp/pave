package org.matsim.ovgu.berlin.evaluation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EvInputVersion {
	public EvInputVersion(String tourDirectory, String versionIdent, double[] expTravelTimes, String[] linkIDs) {
		this.versionIdent = versionIdent;
		this.versionDirectory = tourDirectory + "/" + versionIdent + "/";
		this.expTT = expTravelTimes;
		this.linkIDs = linkIDs;
	}

	protected String versionIdent;
	protected String versionDirectory;
	private String[] linkIDs;
	private double[] expTT;

	// scenario / link --> delay
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
		double[] windows = new double[] { 1, 2, 3, 4, 5, /*6, 7, 8, 9, 10 */};
		double factor = 60;
		double b = 1;
		double ss = 0;
		double u = 2 * factor;

		for (double window : windows) {
			double w = window * factor;
			buffers.add(new EvBufferVersion(versionDirectory, versionIdent + "_bufferW" + w + "_myM-" + myMethod, se, t,
					b, w, ss, u, myMethod, expTT, delayScenarios, linkIDs));
		}
	}

//	public void setupTimeWindowBuffers_SD() {
//		double[] windows = new double[] { 1, 2 , 3, 4, 5/*, 6, 7, 8, 9, 10 */ };
//		double factor = 60;
//		double u = 2 * factor;
//
//		for (double window : windows) {
//			double w = window * factor;
//			buffers.add(
//					new EvBufferVersion(versionDirectory, versionIdent + "_bufferW" + w, w, u, expTT, linkIDs));
//		}
//	}

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

	private void writeScenariosCSV() {
		try {
			File csvFile = new File(versionDirectory + "/" + versionIdent + "_scenarios.csv");
			csvFile.getParentFile().mkdirs();
			FileWriter csvWriter = new FileWriter(csvFile);

			String str = ";Scenario:";
			if (delayScenarios != null)
				for (int i = 0; i < delayScenarios.length; i++)
					str += ";" + i;
			csvWriter.append(str + "\nexpectedTravelTime\n");

			for (int i = 0; i < expTT.length; i++) {
				str = expTT[i] + ";;";
				if (delayScenarios != null)
					for (int s = 0; s < delayScenarios.length; s++)
						str += delayScenarios[s][i] + ";";
				csvWriter.append(str.replace(".", ",") + "\n");
			}

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readRunSettings_ForAllBuffers() {
		for (EvBufferVersion buffer : buffers) {
			buffer.readRunSettings();
		}
	}

	public void writeCSVs() {
		for (EvBufferVersion buffer : buffers) {
			buffer.writeParameters();
			buffer.generateRunSettings();
		}
		writeScenariosCSV();

	}

	public void setupBuffers(boolean runModel) {
		double se = getBestCaseDuration(2 * 60);
		double t = 500;
		// TODO: SETUP PARAMETERS FOR BUFFERS TO BE CHECKED
		setupTimeWindowBuffers(se, t, true);
		setupTimeWindowBuffers(se, t, false);
		if (runModel)
			runLp_ForAllBuffers();
		else
			readRunSettings_ForAllBuffers();
	}

}
