package org.matsim.ovgu.berlin.evaluation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EvBufferVariant {
	public EvBufferVariant(String variantType, String tourDirectory, String versionIdent, double[] expTravelTimes,
			String[] linkIDs) {
		this.variantType = variantType;
		this.versionIdent = versionIdent;
		this.versionDirectory = tourDirectory + "/" + versionIdent + "/";
		this.expTT = expTravelTimes;
		this.linkIDs = linkIDs;
	}

	public String variantType;
	public String versionIdent;
	public String versionDirectory;
	public String[] linkIDs;
	public double[] expTT;

	// scenario / link --> delay
	public double[][] delayScenarios;
	public List<EvBufferSetup> buffers = new ArrayList<EvBufferSetup>();

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
