package org.matsim.ovgu.berlin.evaluation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EvBufferVariant {
	public EvBufferVariant(String tourDirectory, String versionIdent, double[] expTravelTimes, String[] linkIDs) {
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
	public List<EvBufferSetup> buffers = new ArrayList<EvBufferSetup>();

	// without delay
	public double getBestCaseDuration(double serviceTime) {
		double expDuration = 0;
		for (double tt : expTT)
			expDuration += tt + serviceTime;
		return expDuration;
	}
	

	private void setupTimeWindowBuffers(double se, double t, boolean myMethod) {
//		setupEqualTimeWindowBuffers(se, t, myMethod);
		setupMixedTimeWindowBuffers(se, t, myMethod);
	}

	private void setupEqualTimeWindowBuffers(double se, double t, boolean myMethod) {
		double[] windows = new double[] { 1, 2, 3, /* 4, */ 5, /* 6, 7, 8, 9, */ 10 };
		double factor = 60;
		double b = 1;
		double ss = 0;
		double u = 2 * factor;

		for (double window : windows) {
			double w = window * factor;

			buffers.add(new EvBufferSetup(versionDirectory, versionIdent + "_bufferW" + w + "_myM-" + myMethod, se, t,
					b, generateEqualWindows(w, expTT.length), ss, u, myMethod, expTT, delayScenarios, linkIDs));
		}
	}
	
	private double[] generateEqualWindows(double window, int length) {
		double[] windows = new double[length];
		for (int i = 0; i < windows.length; i++)
			windows[i] = window;
		return windows;
	}

	private void setupMixedTimeWindowBuffers(double se, double t, boolean myMethod) {
		// TODO: check number
		double[] mix1 = new double[] { 60, 60, 60, 60, 60, 60, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600,
				600, 600, 600 };
		double[] mix2 = new double[] { 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 60, 60, 60,
				60, 60, 60 };
		double[] mix3 = new double[] { 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 600, 600, 600, 600, 600, 600, 600, 600,
				600, 600 };
		double[] mix4 = new double[] { 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 60, 60, 60, 60, 60, 60, 60, 60,
				60, 60 };
		double[] mix5 = new double[] { 60, 60, 600, 600, 60, 60, 600, 600, 60, 60, 600, 600, 60, 60, 600, 600, 60, 60,
				600, 600 };

		double factor = 60;
		double b = 1;
		double ss = 0;
		double u = 2 * factor;

//		buffers.add(new EvBufferSetup(versionDirectory, versionIdent + "_bufferWmix1_myM-" + myMethod, se, t, b, mix1,
//				ss, u, myMethod, expTT, delayScenarios, linkIDs));
//		buffers.add(new EvBufferSetup(versionDirectory, versionIdent + "_bufferWmix2_myM-" + myMethod, se, t, b, mix2,
//				ss, u, myMethod, expTT, delayScenarios, linkIDs));
//		buffers.add(new EvBufferSetup(versionDirectory, versionIdent + "_bufferWmix3_myM-" + myMethod, se, t, b, mix3,
//				ss, u, myMethod, expTT, delayScenarios, linkIDs));
//		buffers.add(new EvBufferSetup(versionDirectory, versionIdent + "_bufferWmix4_myM-" + myMethod, se, t, b, mix4,
//				ss, u, myMethod, expTT, delayScenarios, linkIDs));
		buffers.add(new EvBufferSetup(versionDirectory, versionIdent + "_bufferWmix5_myM-" + myMethod, se, t, b, mix5,
				ss, u, myMethod, expTT, delayScenarios, linkIDs));
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

	public void calcDelayScenarios(double[][] traveltimeMatrix) {
		int linksCount = traveltimeMatrix.length;
		int szenariosCount = traveltimeMatrix[0].length;
		delayScenarios = new double[szenariosCount][linksCount];

		for (int s = 0; s < szenariosCount; s++)
			for (int l = 0; l < linksCount; l++)
				delayScenarios[s][l] = traveltimeMatrix[l][s] - expTT[l];

		writeScenariosCSV();
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

	public void setupBASEBuffers(boolean runModel) {
		setupTimeWindowBuffers(0, 0, false);
		writeOrLoad(runModel);
	}

	public void setupSDBuffers(double[] avgTT, double[][] traveltimeMatrix, boolean runModel) {
		setupTimeWindowBuffers(0, 0, false);
		if (runModel)
			for (EvBufferSetup buffer : buffers)
				buffer.calculateStandardDeviationBuffer(avgTT, traveltimeMatrix);
		writeOrLoad(runModel);
	}

	public void setupLPBuffers(boolean runModel) {
		double se = getBestCaseDuration(2 * 60);
		double t = 500;
		// TODO: SETUP PARAMETERS FOR BUFFERS TO BE CHECKED
		setupTimeWindowBuffers(se, t, true);
//		setupTimeWindowBuffers(se, t, false);

		if (runModel)
			for (EvBufferSetup buffer : buffers)
				buffer.runLP();
		writeOrLoad(runModel);
	}

	public void writeOrLoad(boolean write) {
		if (write) {
			for (EvBufferSetup buffer : buffers) {
				buffer.writeParameters();
				buffer.generateRunSettings();
			}
			writeScenariosCSV();
		} else
			loadRunSettings();
	}

	public void loadRunSettings() {
		for (EvBufferSetup buffer : buffers) {
			buffer.readRunSettings();
//			buffer.runSettings.directory = buffer.runSettings.directory.replace("C:\\Users\\koetscha\\Desktop\\develop\\matsim.pave\\pave", "D:\\Rico\\ExperimentsMay2020");
//			buffer.writeRunSettingsCSV();
		}
	}

}
