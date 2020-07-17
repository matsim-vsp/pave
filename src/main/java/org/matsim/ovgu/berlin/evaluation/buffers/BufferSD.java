package org.matsim.ovgu.berlin.evaluation.buffers;

import org.matsim.ovgu.berlin.evaluation.model.EvBuffer;
import org.matsim.ovgu.berlin.evaluation.model.EvTour;
import org.matsim.ovgu.berlin.evaluation.model.EvVariant;

public class BufferSD {

	protected static EvVariant init(EvTour tour, boolean test) {
		EvVariant sd = new EvVariant("SD", tour.tourDirectory, tour.tourIdent + "_SDavg", tour.avgTravelTime,
				tour.linkIDs);
		if (test)
			sd = new EvVariant("SD-Test", tour.tourDirectory, tour.tourIdent + "_SDavgTEST", tour.avgTravelTime,
					tour.linkIDs);
		tour.evBufferVariants.add(sd);
		return sd;
	}

	public static void calculate(EvBuffer buffer, EvTour tour, boolean test) {
		int linksCount = tour.traveltimeMatrix.length;
		int szenariosCount = tour.traveltimeMatrix[0].length;

		buffer.bufferValues = new double[linksCount];

		for (int l = 0; l < linksCount; l++) {
			double[] ttArray = new double[szenariosCount];
			for (int s = 0; s < szenariosCount; s++)
				ttArray[s] = tour.traveltimeMatrix[l][s];
			double sd = calculateStandardDeviation(tour.avgTravelTime[l], ttArray);

			if (test && buffer.w[l] == 1 * 60)
				buffer.bufferValues[l] = sd * 1.3;
			else
				buffer.bufferValues[l] = sd * 1;
			// bei 1 Min -> 1.3
			// bei 10 Min -> 0.65
		}

	}

	private static double calculateStandardDeviation(double avgTT, double[] ttArray) {
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

}
