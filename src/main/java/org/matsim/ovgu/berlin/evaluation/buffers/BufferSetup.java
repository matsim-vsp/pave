package org.matsim.ovgu.berlin.evaluation.buffers;

import org.matsim.ovgu.berlin.evaluation.model.EvBuffer;
import org.matsim.ovgu.berlin.evaluation.model.EvTour;
import org.matsim.ovgu.berlin.evaluation.model.EvVariant;

public class BufferSetup {

	public static void initialize(EvTour tour) {
		setupBuffersForVariants(tour);
	}

	private static void setupBuffersForVariants(EvTour tour) {
//		setupTimeWindowBuffers(BufferBASE.init("min", tour.minTravelTime, tour), 0, 0, false);
//		setupTimeWindowBuffers(BufferBASE.init("avg", tour.avgTravelTime, tour), 0, 0, false);
//
//		setupTimeWindowBuffers(BufferSD.init(tour, false), 0, 0, false);
//		setupTimeWindowBuffers(BufferSD.init(tour, true), 0, 0, true);

		double t = 500;

		double se = getBestCaseDuration(2 * 60, tour.minTravelTime);
		setupTimeWindowBuffers(BufferLP.init("min", tour.minTravelTime, tour), se, t, true);
		setupTimeWindowBuffers(BufferLP.init("min", tour.minTravelTime, tour), se, t, false);

//		se = getBestCaseDuration(2 * 60, tour.avgTravelTime);
//		setupTimeWindowBuffers(BufferLP.init("avg", tour.avgTravelTime, tour), se, t, true);
//		setupTimeWindowBuffers(BufferLP.init("avg", tour.avgTravelTime, tour), se, t, false);

	}

// without delay
	private static double getBestCaseDuration(double serviceTime, double[] expTT) {
		double expDuration = 0;
		for (double tt : expTT)
			expDuration += tt + serviceTime;
		return expDuration;
	}

	private static void setupTimeWindowBuffers(EvVariant variant, double se, double t, boolean myMethod) {
		setupEqualTimeWindowBuffers(variant, se, t, myMethod);
		setupMixedTimeWindowBuffers(variant, se, t, myMethod);
	}

	private static void setupEqualTimeWindowBuffers(EvVariant variant, double se, double t, boolean myMethod) {
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

	private static void setupMixedTimeWindowBuffers(EvVariant variant, double se, double t, boolean myMethod) {
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

	public static void load(EvTour tour, boolean runModel) {
		for (EvVariant variant : tour.evBufferVariants) {
			if (runModel)
				for (EvBuffer buffer : variant.buffers) {
					switch (variant.variantType) {
					case "LP":
						BufferLP.runLP(buffer, variant.delayScenarios);
						break;
					case "SD":
						BufferSD.calculate(buffer, tour, false);
						break;
					case "SD-Test":
						BufferSD.calculate(buffer, tour, true);
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

	private static double[] generateEqualWindows(double window, int length) {
		double[] windows = new double[length];
		for (int i = 0; i < windows.length; i++)
			windows[i] = window;
		return windows;
	}

	private static EvBuffer createBuffer(double[] w, String bufferIdent, EvVariant variant, boolean myMethod, double se,
			double t, double b, double ss, double u) {

		return new EvBuffer(variant.versionDirectory, variant.versionIdent + bufferIdent + "_myM-" + myMethod, se, t, b,
				w, ss, u, myMethod, variant.expTT, variant.delayScenarios, variant.linkIDs);

	}
}
