package org.matsim.ovgu.berlin.evaluation;

import java.util.ArrayList;
import java.util.List;



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

	public double getNoDelayDuration(double serviceTime) {
		double sum = 0;
		for (int i = 0; i < minTravelTime.length; i++)
			sum += minTravelTime[i] + serviceTime;
		return sum;
	}
}
