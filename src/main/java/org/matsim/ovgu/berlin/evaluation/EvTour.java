package org.matsim.ovgu.berlin.evaluation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.ovgu.berlin.Settings;
import org.matsim.ovgu.berlin.createTravelTimeMatrix.Version3_RunSimReadEvents;
import org.matsim.ovgu.berlin.eventHandling.Summary;
import org.matsim.ovgu.berlin.eventHandling.TourEventsHandler;
import org.matsim.ovgu.berlin.simulation.FreightOnlyMatsim;

public class EvTour {

	public EvTour(String evaluationDirectory, String tourIdent, String[] linkIDs) {
		this.tourIdent = tourIdent;
		this.linkIDs = linkIDs;
		this.tourDirectory = evaluationDirectory + "/" + tourIdent + "/";
	}
	
	public double[] minTravelTime;
	public double[] avgTravelTime;
	public double[] maxTravelTime;
	
	// linkID / hour --> travelTime
	public double[][] traveltimeMatrix;

	
	public String tourDirectory;
	public String tourIdent;
	public String[] linkIDs;

	public List<EvBufferVariant> evBufferVariants = new ArrayList<EvBufferVariant>();

//	private void initVersionC(boolean runModel) {
//		EvBufferVariant vC = new EvBufferVariant(tourDirectory, tourIdent + "_versionC", avgTravelTime, linkIDs);
//		vC.calcDelayScenarios(traveltimeMatrix);
//		vC.setupSITWABuffers(runModel);
//		evBufferVariants.add(vC);
//	}

	public double getNoDelayDuration(double serviceTime) {
		double sum = 0;
		for (int i = 0; i < minTravelTime.length; i++)
			sum += minTravelTime[i] + serviceTime;
		return sum;
	}
}
