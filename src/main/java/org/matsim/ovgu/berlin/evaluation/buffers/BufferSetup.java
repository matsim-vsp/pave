package org.matsim.ovgu.berlin.evaluation.buffers;

import org.matsim.ovgu.berlin.evaluation.model.EvBuffer;
import org.matsim.ovgu.berlin.evaluation.model.EvTour;
import org.matsim.ovgu.berlin.evaluation.model.EvVariant;

public class BufferSetup {

	public static void initialize(EvTour tour) {
		setupBuffersForVariants(tour);
	}

	private static void setupBuffersForVariants(EvTour tour) {
		setupTimeWindowBuffers(BufferBASE.init("min", tour.minTravelTime, tour), 0, 0, false);
		setupTimeWindowBuffers(BufferBASE.init("avg", tour.avgTravelTime, tour), 0, 0, false);

		setupTimeWindowBuffers(BufferSD.init(tour, false), 0, 0, false);
		setupTimeWindowBuffers(BufferSD.init(tour, true), 0, 0, true);

		double t = 500;
		double se = getBestCaseDuration(2 * 60, tour.minTravelTime);
		EvVariant min = BufferLP.init("min", tour.minTravelTime, tour, false);
		setupTimeWindowBuffers(min, se, t, true);
		setupTimeWindowBuffers(min, se, t, false);

		EvVariant minHalf = BufferLP.init("min", tour.minTravelTime, tour, true);
		setupTimeWindowBuffers(minHalf, se, t, true);
		setupTimeWindowBuffers(minHalf, se, t, false);

		se = getBestCaseDuration(2 * 60, tour.avgTravelTime);
		EvVariant avg = BufferLP.init("avg", tour.avgTravelTime, tour, false);
		setupTimeWindowBuffers(avg, se, t, true);
		setupTimeWindowBuffers(avg, se, t, false);
		EvVariant avgHalf = BufferLP.init("avg", tour.avgTravelTime, tour, true);
		setupTimeWindowBuffers(avgHalf, se, t, true);
		setupTimeWindowBuffers(avgHalf, se, t, false);

	}

// without delay
	private static double getBestCaseDuration(double serviceTime, double[] expTT) {
		double expDuration = 0;
		for (double tt : expTT)
			expDuration += tt + serviceTime;
		return expDuration;
	}

	private static void setupTimeWindowBuffers(EvVariant variant, double se, double t, boolean myMethod) {
//		setupEqualTimeWindowBuffers(variant, se, t, myMethod);
		setupMixedTimeWindowBuffers(variant, se, t, myMethod);
	}

	private static void setupEqualTimeWindowBuffers(EvVariant variant, double se, double t, boolean myMethod) {
//		double[] windows = new double[] { 10 };
//		double[] windows = new double[] { 10, 1 };
		double[] windows = new double[] { 1, 2, 3, /* 4, */ 5, /* 6, 7, 8, 9, */ 10 };
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
		double factor = 60;
		double b = 1;
		double ss = 0;
		double u = 2 * factor;

//		******3er******

		double[] mix3P2 = getWindows( 600, 60, 60, 60, 600, 600, 600, 600, 600, 600);
		double[] mix3P3 = getWindows( 600, 600, 60, 60, 60, 600, 600, 600, 600, 600);
		double[] mix3P4 = getWindows( 600, 600, 600, 60, 60, 60, 600, 600, 600, 600);
		double[] mix3P5 = getWindows( 600, 600, 600, 600, 60, 60, 60, 600, 600, 600);
		double[] mix3P6 = getWindows( 600, 600, 600, 600, 600, 60, 60, 60, 600, 600);
		double[] mix3P7 = getWindows( 600, 600, 600, 600, 600, 600, 60, 60, 60, 600);

		double[] mix3S2 = getWindows( 60, 600, 600, 600, 60, 60, 60, 60, 60, 60);
		double[] mix3S3 = getWindows( 60, 60, 600, 600, 600, 60, 60, 60, 60, 60);
		double[] mix3S4 = getWindows( 60, 60, 60, 600, 600, 600, 60, 60, 60, 60);
		double[] mix3S5 = getWindows( 60, 60, 60, 60, 600, 600, 600, 60, 60, 60);
		double[] mix3S6 = getWindows( 60, 60, 60, 60, 60, 600, 600, 600, 60, 60);
		double[] mix3S7 = getWindows( 60, 60, 60, 60, 60, 60, 600, 600, 600, 60);

		variant.buffers.add(createBuffer(mix3P2, "_bufferWmix3P2", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix3P3, "_bufferWmix3P3", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix3P4, "_bufferWmix3P4", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix3P5, "_bufferWmix3P5", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix3P6, "_bufferWmix3P6", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix3P7, "_bufferWmix3P7", variant, myMethod, se, t, b, ss, u));

		variant.buffers.add(createBuffer(mix3S2, "_bufferWmix3S2", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix3S3, "_bufferWmix3S3", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix3S4, "_bufferWmix3S4", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix3S5, "_bufferWmix3S5", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix3S6, "_bufferWmix3S6", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix3S7, "_bufferWmix3S7", variant, myMethod, se, t, b, ss, u));
		
		
//		******5er******

		double[] mix5P2 = getWindows( 600, 60, 60, 60, 60, 60, 600, 600, 600, 600);
		double[] mix5P3 = getWindows( 600, 600, 60, 60, 60, 60, 60, 600, 600, 690);
		double[] mix5P4 = getWindows( 600, 600, 600, 60, 60, 60, 60, 60, 600, 600);
		double[] mix5P5 = getWindows( 600, 600, 600, 600, 60, 60, 60, 60, 60, 600);

		double[] mix5S2 = getWindows( 60, 600, 600, 600, 600, 600, 60, 60, 60, 60);
		double[] mix5S3 = getWindows( 60, 60, 600, 600, 600, 600, 600, 60, 60, 60);
		double[] mix5S4 = getWindows( 60, 60, 60, 600, 600, 600, 600, 600, 60, 60);
		double[] mix5S5 = getWindows( 60, 60, 60, 60, 600, 600, 600, 600, 600, 60);


		variant.buffers.add(createBuffer(mix5P2, "_bufferWmix5P2", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix5P3, "_bufferWmix5P3", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix5P4, "_bufferWmix5P4", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix5P5, "_bufferWmix5P5", variant, myMethod, se, t, b, ss, u));

		variant.buffers.add(createBuffer(mix5S2, "_bufferWmix5S2", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix5S3, "_bufferWmix5S3", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix5S4, "_bufferWmix5S4", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix5S5, "_bufferWmix5S5", variant, myMethod, se, t, b, ss, u));

		

//		******7er******

		double[] mix7P2 = getWindows( 600, 60, 60, 60, 60, 60, 60, 60, 600, 600);
		double[] mix7P3 = getWindows( 600, 600, 60, 60, 60, 60, 60, 60, 60, 600);
		double[] mix7S2 = getWindows( 60, 600, 600, 600, 600, 600, 600, 600, 60, 60);
		double[] mix7S3 = getWindows( 60, 60, 600, 600, 600, 600, 600, 600, 600, 60);
		

		variant.buffers.add(createBuffer(mix7P2, "_bufferWmix7P2", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix7P3, "_bufferWmix7P3", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix7S2, "_bufferWmix7S2", variant, myMethod, se, t, b, ss, u));
		variant.buffers.add(createBuffer(mix7S3, "_bufferWmix7S3", variant, myMethod, se, t, b, ss, u));
		
		
		
//		******FIRST******
		
//		double[] mix1 = new double[] { 60, 60, 60, 60, 60, 60, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600,
//				600, 600, 600 };
//		double[] mix2 = new double[] { 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 60, 60, 60,
//				60, 60, 60 };
//		double[] mix3 = new double[] { 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 600, 600, 600, 600, 600, 600, 600, 600,
//				600, 600 };
//		double[] mix4 = new double[] { 60, 60, 600, 600, 60, 60, 600, 600, 60, 60, 600, 600, 60, 60, 600, 600, 60, 60,
//				600, 600 };
//		double[] mix1r = new double[] { 600, 600, 600, 600, 600, 600, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60,
//				60, 60 };
//		double[] mix2r = new double[] { 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 600, 600, 600, 600, 600,
//				600 };
//		double[] mix3r = new double[] { 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 60, 60, 60, 60, 60, 60, 60,
//				60, 60, 60 };
//		double[] mix4r = new double[] { 600, 600, 60, 60, 600, 600, 60, 60, 600, 600, 60, 60, 600, 600, 60, 60, 600,
//				600, 60, 60 };
//
//
//		variant.buffers.add(createBuffer(mix1, "_bufferWmix1", variant, myMethod, se, t, b, ss, u));
//		variant.buffers.add(createBuffer(mix1r, "_bufferWmix1r", variant, myMethod, se, t, b, ss, u));
//		variant.buffers.add(createBuffer(mix2, "_bufferWmix2", variant, myMethod, se, t, b, ss, u));
//		variant.buffers.add(createBuffer(mix2r, "_bufferWmix2r", variant, myMethod, se, t, b, ss, u));
//		variant.buffers.add(createBuffer(mix3, "_bufferWmix3", variant, myMethod, se, t, b, ss, u));
//		variant.buffers.add(createBuffer(mix3r, "_bufferWmix3r", variant, myMethod, se, t, b, ss, u));
//		variant.buffers.add(createBuffer(mix4, "_bufferWmix4", variant, myMethod, se, t, b, ss, u));
//		variant.buffers.add(createBuffer(mix4r, "_bufferWmix4r", variant, myMethod, se, t, b, ss, u));
	}

	private static double[] getWindows(double c1, double c2, double c3, double c4, double c5, double c6, double c7, double c8,
			double c9, double c10) {
		return new double[] { c1, c1, c2, c2, c3, c3, c4, c4, c5, c5, c6, c6, c7, c7, c8, c8, c9, c9, c10, c10 };
	}

	public static void load(EvTour tour, boolean runModel) {
		for (EvVariant variant : tour.evBufferVariants) {
			if (runModel)
				for (EvBuffer buffer : variant.buffers) {
					switch (variant.variantType) {
					case "LP":
						BufferLP.runLP(buffer, variant.delayScenarios, false);
						break;
					case "LP-halfTW":
						BufferLP.runLP(buffer, variant.delayScenarios, true);
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
