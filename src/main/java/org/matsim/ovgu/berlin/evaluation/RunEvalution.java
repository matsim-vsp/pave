package org.matsim.ovgu.berlin.evaluation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.evaluation.analysis.Analysis;
import org.matsim.ovgu.berlin.evaluation.buffers.BufferSetup;
import org.matsim.ovgu.berlin.evaluation.model.EvTour;
import org.matsim.ovgu.berlin.evaluation.simulation.Simulation;
import org.matsim.ovgu.berlin.evaluation.travelTime.TravelTimeSetup;

public class RunEvalution {

	private List<EvTour> tours = new ArrayList<EvTour>();
	private String evaluationIdent;
	private String evaluationDirectory;

	public void runDefault(String[] linkIDs) {
		String name = "NextGen2";
		int from = 1;
		int to = from;
		boolean ttSimulation = false;
		boolean ttReadEventsOnly = false;
		boolean runBufferModel = false;
		String windowMethod = "PlusMinusArrival";
		boolean runSimulation = false;
		boolean runAnalysis = true;
		boolean runSummary = false;

		run(name, linkIDs, from, to, ttSimulation, ttReadEventsOnly, runBufferModel, windowMethod, runSimulation, runAnalysis,
				runSummary);
	}

	public void run(String evaluationIdent, String[] linkIDs, int from, int to, boolean ttSimulation,
			boolean ttReadEventsOnly, boolean runBufferModel, String windowMethod, boolean runSimulation,
			boolean runAnalysis, boolean runSummary) {

		this.evaluationIdent = evaluationIdent;
		setEvaluationDirectory();

		writeLinksCSV(linkIDs);
		generateRandomTours(linkIDs, 20);
		writeToursCSV();

		for (int i = from - 1; i < to; i++) {
			// simulate 24 hour travel time values
			if (ttSimulation || ttReadEventsOnly)
				TravelTimeSetup.runTravelTimeSimulations(tours.get(i), ttReadEventsOnly);

			// read 24 hour travel time values
			TravelTimeSetup.readTravelTimes(tours.get(i));

			// initialize buffer variants
			BufferSetup.initialize(tours.get(i));

			// calculate or read buffers
			BufferSetup.load(tours.get(i), runBufferModel);

			// simulate with buffer variants
			if (runSimulation)
				Simulation.run(tours.get(i), windowMethod);

			// read and analyse events
			if (runAnalysis)
				Analysis.run(tours.get(i), windowMethod);
		}

		if (runSummary) {

		}

		System.out.println("RunEvalution.run() Finished !");
	}

	private void setEvaluationDirectory() {
		evaluationDirectory = new Settings().directory + "/" + evaluationIdent + "/";

	}

	private void writeLinksCSV(String[] linkIDs) {
		try {
			File csvFile = new File(evaluationDirectory + "/" + evaluationIdent + "_links.csv");
			csvFile.getParentFile().mkdirs();
			FileWriter csvWriter = new FileWriter(csvFile);

			for (String linkID : linkIDs)
				csvWriter.append(linkID + "\n");

			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void writeToursCSV() {
		try {
			File csvFile = new File(evaluationDirectory + "/" + evaluationIdent + "_tours.csv");
			csvFile.getParentFile().mkdirs();
			FileWriter csvWriter = new FileWriter(csvFile);

			for (EvTour tour : tours) {
				String str = tour.tourIdent + ";;";
				for (String linkID : tour.linkIDs)
					str += linkID + ";";
				csvWriter.append(str + "\n");
			}
			csvWriter.flush();
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void generateRandomTours(String[] linkIDs, int linksPerTour) {

		// shuffle linkIDs
		shuffleLinkIDs(linkIDs);

		// a tour consist of linksPerTour links
		String[] ids = new String[linksPerTour];
		int counter = 0;
		for (int i = 0; i < linkIDs.length; i++) {
			ids[counter++] = linkIDs[i];
			// a tour consist of linksPerTour links
			if (counter == linksPerTour) {
				tours.add(new EvTour(evaluationDirectory, evaluationIdent + "_tour" + ((i + 1) / counter), ids));
				ids = new String[linksPerTour];
				counter = 0;
			}
		}
	}

	private String[] shuffleLinkIDs(String[] linkIDs) {
		// Implementing Fisher–Yates shuffle
		Random rnd = new Random(99999);
		for (int i = linkIDs.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			// Simple swap
			String id = linkIDs[index];
			linkIDs[index] = linkIDs[i];
			linkIDs[i] = id;
		}
		return linkIDs;

	}

}
