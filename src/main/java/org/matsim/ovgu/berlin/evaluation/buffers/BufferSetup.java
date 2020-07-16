package org.matsim.ovgu.berlin.evaluation.buffers;

import org.matsim.ovgu.berlin.evaluation.EvBufferSetup;
import org.matsim.ovgu.berlin.evaluation.EvBufferVariant;
import org.matsim.ovgu.berlin.evaluation.EvTour;

public class BufferSetup {

	public static void initialize(EvTour tour) {
		setupBuffersForVariants(tour);
	}

	public static void load(EvTour tour, boolean runModel) {
		for (EvBufferVariant variant : tour.evBufferVariants) {
			if (runModel)
				for (EvBufferSetup buffer : variant.buffers) {
					switch (variant.variantType) {
					case "LP":
						buffer.runLP();
						break;
					case "SD":
						buffer.calculateStandardDeviationBuffer(tour.avgTravelTime, tour.traveltimeMatrix, false);
						break;
					case "SD-Test":
						buffer.calculateStandardDeviationBuffer(tour.avgTravelTime, tour.traveltimeMatrix, true);
						break;
					case "BASE":
						break;
					default:
						System.out.println(
								"BufferSetup.load() - Unknown Buffer Variant Type: \"" + variant.variantType + "\"");
						break;
					}
				}
			variant.writeOrLoad(runModel);
		}
	}

	private static void setupBuffersForVariants(EvTour tour) {
		initLPmin(tour);
		initLPavg(tour);
//		initVersionC();
		initBASEavg(tour);
		initBASEmin(tour);
		initSDavg(tour);
		initSDTestavg(tour);
	}

	private static void initBASEmin(EvTour tour) {
		EvBufferVariant baseMin = new EvBufferVariant("BASE", tour.tourDirectory, tour.tourIdent + "_BASEmin",
				tour.minTravelTime, tour.linkIDs);
		setupTimeWindowBuffers(baseMin, 0, 0, false);
		tour.evBufferVariants.add(baseMin);
	}

	private static void initBASEavg(EvTour tour) {
		EvBufferVariant baseAvg = new EvBufferVariant("BASE", tour.tourDirectory, tour.tourIdent + "_BASEavg",
				tour.avgTravelTime, tour.linkIDs);
		setupTimeWindowBuffers(baseAvg, 0, 0, false);
		tour.evBufferVariants.add(baseAvg);
	}

	private static void initSDavg(EvTour tour) {
		EvBufferVariant sdAvg = new EvBufferVariant("SD", tour.tourDirectory, tour.tourIdent + "_SDavg",
				tour.avgTravelTime, tour.linkIDs);
		setupTimeWindowBuffers(sdAvg, 0, 0, false);
		tour.evBufferVariants.add(sdAvg);
	}

	private static void initSDTestavg(EvTour tour) {
		EvBufferVariant sdAvgTest = new EvBufferVariant("SD-Test", tour.tourDirectory, tour.tourIdent + "_SDavgTEST",
				tour.avgTravelTime, tour.linkIDs);
		setupTimeWindowBuffers(sdAvgTest, 0, 0, true);
		tour.evBufferVariants.add(sdAvgTest);
	}

	private static void initLPmin(EvTour tour) {
		EvBufferVariant lpMin = new EvBufferVariant("LP", tour.tourDirectory, tour.tourIdent + "_LPmin",
				tour.minTravelTime, tour.linkIDs);
		lpMin.calcDelayScenarios(tour.traveltimeMatrix);
		setupLPBuffers(lpMin);
		tour.evBufferVariants.add(lpMin);
	}

	private static void initLPavg(EvTour tour) {
		EvBufferVariant lpAvg = new EvBufferVariant("LP", tour.tourDirectory, tour.tourIdent + "_LPavg",
				tour.avgTravelTime, tour.linkIDs);
		lpAvg.calcDelayScenarios(tour.traveltimeMatrix);
		lpAvg.removeNegativScenarioValues();
		setupLPBuffers(lpAvg);
		tour.evBufferVariants.add(lpAvg);
	}

	private static void setupLPBuffers(EvBufferVariant variant) {
		double se = getBestCaseDuration(2 * 60, variant.expTT);
		double t = 500;
		// TODO: SETUP PARAMETERS FOR BUFFERS TO BE CHECKED
		setupTimeWindowBuffers(variant, se, t, true);
		setupTimeWindowBuffers(variant, se, t, false);
	}

	// without delay
	private static double getBestCaseDuration(double serviceTime, double[] expTT) {
		double expDuration = 0;
		for (double tt : expTT)
			expDuration += tt + serviceTime;
		return expDuration;
	}

	private static void setupTimeWindowBuffers(EvBufferVariant variant, double se, double t, boolean myMethod) {
		setupEqualTimeWindowBuffers(variant, se, t, myMethod);
		setupMixedTimeWindowBuffers(variant, se, t, myMethod);
	}

	private static void setupEqualTimeWindowBuffers(EvBufferVariant variant, double se, double t, boolean myMethod) {
//		double[] windows = new double[] { 10 };
		double[] windows = new double[] { 10, 1 };
//		double[] windows = new double[] { 1, 2, 3, /* 4, */ 5, /* 6, 7, 8, 9, */ 10 };
		double factor = 60;
		double b = 1;
		double ss = 0;
		double u = 2 * factor;

		for (double window : windows) {
			double w = window * factor;

			double[] wArray = generateEqualWindows(w, variant.expTT.length);
			variant.buffers.add(createBuffer(wArray, "_bufferW" + w, variant, myMethod, se, t, b, ss, u));
		}
	}

	private static double[] generateEqualWindows(double window, int length) {
		double[] windows = new double[length];
		for (int i = 0; i < windows.length; i++)
			windows[i] = window;
		return windows;
	}

	private static void setupMixedTimeWindowBuffers(EvBufferVariant variant, double se, double t, boolean myMethod) {
		double[] mix1 = new double[] { 60, 60, 60, 60, 60, 60, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600,
				600, 600, 600 };
		double[] mix2 = new double[] { 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 60, 60, 60,
				60, 60, 60 };
		double[] mix3 = new double[] { 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 600, 600, 600, 600, 600, 600, 600, 600,
				600, 600 };
		double[] mix4 = new double[] { 60, 60, 600, 600, 60, 60, 600, 600, 60, 60, 600, 600, 60, 60, 600, 600, 60, 60,
				600, 600 };
		double[] mix1r = new double[] { 600, 600, 600, 600, 600, 600, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60,
				60, 60 };
		double[] mix2r = new double[] { 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 600, 600, 600, 600, 600,
				600 };
		double[] mix3r = new double[] { 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 60, 60, 60, 60, 60, 60, 60,
				60, 60, 60 };
		double[] mix4r = new double[] { 600, 600, 60, 60, 600, 600, 60, 60, 600, 600, 60, 60, 600, 600, 60, 60, 600,
				600, 60, 60 };

		double factor = 60;
		double b = 1;
		double ss = 0;
		double u = 2 * factor;

		variant.buffers.add(createBuffer(mix1, "_bufferWmix1", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix1r, "_bufferWmix1r", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix2, "_bufferWmix2", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix2r, "_bufferWmix2r", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix3, "_bufferWmix3", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix3r, "_bufferWmix3r", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix4, "_bufferWmix4", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix4r, "_bufferWmix4r", variant, myMethod, se, t, b, ss, u));

	}

	private static EvBufferSetup createBuffer(double[] w, String bufferIdent, EvBufferVariant variant, boolean myMethod,
			double se, double t, double b, double ss, double u) {

		return new EvBufferSetup(variant.versionDirectory, variant.versionIdent + bufferIdent + "_myM-" + myMethod, se,
				t, b, w, ss, u, myMethod, variant.expTT, variant.delayScenarios, variant.linkIDs);

	}
}
