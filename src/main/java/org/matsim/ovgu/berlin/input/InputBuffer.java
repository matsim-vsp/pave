package org.matsim.ovgu.berlin.input;

public class InputBuffer {

	public static final double[] bufferNone = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
			0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
	public static final double[] buffer05M = new double[] { 0.0, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30,
			30, 30, 30, 30, 30 };
	public static final double[] buffer5P = new double[] { 0.0, 71.13958333, 24.325, 34.52708333, 29.22291667,
			22.79166667, 33.59375, 32.53958333, 27.5, 41.79791667, 40.01458333, 22.21458333, 28.20416667, 29.03333333,
			32.11458333, 39.04375, 20.32291667, 40.35625, 30.7625, 43.7125 };
	public static final double[] bufferSD = new double[] { 0.0, 80.0036349, 47.0496192, 36.41425907, 35.90378249,
			15.29342633, 25.18318305, 37.42657163, 39.97916124, 46.68465055, 46.93388893, 15.66439478, 17.46643607,
			35.67756843, 47.81603564, 45.54422073, 40.90026402, 58.57922876, 68.71817445, 97.82222396 };
	public static final double[] bufferSDcum = new double[] { 0.0, 80.0036349, 92.81297465, 99.70078499, 105.9685242,
			107.066414, 109.9882254, 116.1815734, 122.8677798, 131.4380003, 139.566249, 140.4425545, 141.524512,
			145.9523086, 153.5853172, 160.1958979, 165.3346827, 175.4054826, 188.3859624, 212.2697772 };
	public static final double[] bufferSDcumStep = new double[] { 0.0, 80.0036349, 12.80933975, 6.887810338,
			6.267739227, 1.097889815, 2.921811347, 6.19334797, 6.686206476, 8.57022045, 8.128248678, 0.876305534,
			1.081957523, 4.4277966, 7.633008567, 6.610580692, 5.138784794, 10.07079996, 12.98047979, 23.88381484 };
	public static final double[] buffer5P_05M = new double[] { 0.0, 63.04184049, 21.55611121, 30.59690229, 25.89650325,
			20.19731558, 29.76980929, 28.83563729, 24.36970435, 37.04010442, 35.45976602, 19.68592102, 24.99371648,
			25.72849999, 28.45901459, 34.59944161, 18.00958075, 35.76254113, 27.26083746, 38.73675278 };
	public static final double[] bufferSD_05M = new double[] { 0.0, 54.34895303, 31.96226705, 24.73733673, 24.39055414,
			10.38929932, 17.1077181, 25.42503208, 27.15908545, 31.71433252, 31.88364789, 10.6413097, 11.86549229,
			24.2368799, 32.48291754, 30.93960314, 27.78481917, 39.79468878, 46.68238938, 66.45367379 };
	public static final double[] bufferSDcum_05M = new double[] { 0.0, 17.61588307, 20.43635281, 21.95296967,
			23.33305398, 23.57479673, 24.21814608, 25.58185029, 27.05407629, 28.94114057, 30.73088773, 30.92384016,
			31.1620748, 32.13702484, 33.81772581, 35.27329988, 36.40480137, 38.62227604, 41.48042886, 46.73937103 };
	public static final double[] bufferSDcumStep_05M = new double[] { 0.0, 214.8307332, 34.3964353, 18.49557645,
			16.83052296, 2.948121972, 7.845829441, 16.63076293, 17.95421723, 23.01328866, 21.82647858, 2.353110087,
			2.905339593, 11.88979465, 20.49662905, 17.75114217, 13.79898434, 27.04273802, 34.85599116, 64.13430416 };

	public static final double[] buffer1P = new double[] { 0.0, 14.22791667, 4.865, 6.905416667, 5.844583333,
			4.558333333, 6.71875, 6.507916667, 5.5, 8.359583333, 8.002916667, 4.442916667, 5.640833333, 5.806666667,
			6.422916667, 7.80875, 4.064583333, 8.07125, 6.1525, 8.7425 };
	public static final double[] buffer2P = new double[] { 0.0, 28.45583333, 9.73, 13.81083333, 11.68916667,
			9.116666667, 13.4375, 13.01583333, 11, 16.71916667, 16.00583333, 8.885833333, 11.28166667, 11.61333333,
			12.84583333, 15.6175, 8.129166667, 16.1425, 12.305, 17.485 };
	public static final double[] buffer3P = new double[] { 0.0, 42.68375, 14.595, 20.71625, 17.53375, 13.675, 20.15625,
			19.52375, 16.5, 25.07875, 24.00875, 13.32875, 16.9225, 17.42, 19.26875, 23.42625, 12.19375, 24.21375,
			18.4575, 26.2275 };
	public static final double[] buffer4P = new double[] { 0.0, 56.91166667, 19.46, 27.62166667, 23.37833333,
			18.23333333, 26.875, 26.03166667, 22, 33.43833333, 32.01166667, 17.77166667, 22.56333333, 23.22666667,
			25.69166667, 31.235, 16.25833333, 32.285, 24.61, 34.97 };

	public static final double[] bufferMin_3P = new double[] { 0.0, 20.31210526, 20.31210526, 20.31210526, 20.31210526,
			20.31210526, 20.31210526, 20.31210526, 20.31210526, 20.31210526, 20.31210526, 20.31210526, 20.31210526,
			20.31210526, 20.31210526, 20.31210526, 20.31210526, 20.31210526, 20.31210526, 20.31210526 };
	public static final double[] bufferSD_3P = new double[] { 0.0, 36.79805517, 21.64069776, 16.74891292, 16.51411677,
			7.03428471, 11.58312569, 17.21453093, 18.38860675, 21.47282869, 21.58746707, 7.204913427, 8.03377095,
			16.41006853, 21.99321467, 20.94828252, 18.81227239, 26.94379692, 31.60725356, 44.99380057 };
	public static final double[] bufferSDcum_3P = new double[] { 0.0, 11.92718904, 13.83684498, 14.86370102,
			15.79811495, 15.96179176, 16.39738442, 17.32070787, 18.31750818, 19.59518312, 20.80696755, 20.93760988,
			21.09891145, 21.75902105, 22.89697354, 23.88249934, 24.64860525, 26.14999122, 28.08516124, 31.64583414 };
	public static final double[] bufferSDcumStep_3P = new double[] { 0.0, 145.4554823, 23.28880048, 12.52280319,
			11.39544513, 1.996085461, 5.312177116, 11.26019358, 12.15626501, 15.58161139, 14.77805768, 1.59322066,
			1.967118788, 8.05022535, 13.87765623, 12.01876894, 9.342880749, 18.30983137, 23.59995205, 43.42342457 };

	public static final double[] bufferNew6P = new double[] { 0, 86.79029167, 29.3715, 42.12304167, 35.418125,
			27.80583333, 40.86491667, 39.69829167, 33.55, 50.256375, 48.685625, 27.10179167, 34.40908333, 35.38508333,
			39.17979167, 47.480875, 24.79395833, 49.09991667, 36.99141667, 51.171375 };
	public static final double[] bufferNewSD = new double[] { 0, 80.0036349, 40.62121777, 36.41425907, 31.64492126,
			15.29342633, 24.76374478, 37.42657163, 39.97916124, 36.06511761, 43.9851419, 15.66439478, 17.46643607,
			35.88165347, 47.81603564, 45.17633276, 40.90026402, 58.22078428, 63.34226911, 72.00770358 };
	public static final double[] bufferNewSITWA_1W = new double[] { 0, 0, 0, 0, 83, 47, 36, 85, 223, 0, 63, 206, 0, 36,
			0, 0, 0, 0, 0, 0 };
	public static final double[] bufferNewSITWA_2W = new double[] { 0, 0, 0, 0, 0, 119, 143, 21, 0, 159, 16, 250, 0, 71,
			0, 0, 0, 0, 0, 0 };
	public static final double[] bufferNewSITWA_3W = new double[] { 0, 0, 0, 0, 37, 127, 119, 0, 0, 0, 229, 143, 0, 85,
			39, 0, 0, 0, 0, 0 };
	public static final double[] bufferNewSITWA_4W = new double[] { 0, 0, 0, 0, 49, 62, 149, 0, 0, 8, 277, 0, 0, 184,
			50, 0, 0, 0, 0, 0 };
	public static final double[] bufferNewSITWA_5W = new double[] { 0, 0, 0, 0, 115, 67, 39, 32, 0, 43, 261, 0, 0, 124,
			98, 0, 0, 0, 0, 0 };
	public static final double[] bufferNewSITWA_6W = new double[] { 0, 0, 0, 20, 174, 0, 35, 69, 0, 15, 187, 72, 2, 8,
			197, 0, 0, 0, 0, 0 };
	public static final double[] bufferNewSITWA_7W = new double[] { 0, 0, 0, 116, 146, 0, 33, 54, 0, 39, 238, 11, 1, 0,
			130, 11, 0, 0, 0, 0 };
	public static final double[] bufferNewSITWA_8W = new double[] { 0, 45, 0, 112, 105, 0, 7, 126, 0, 52, 239, 0, 0, 0,
			60, 33, 0, 0, 0, 0 };
	public static final double[] bufferNewSITWA_9W = new double[] { 0, 254, 0, 34, 33, 0, 50, 80, 0, 12, 226, 0, 0, 0,
			0, 90, 0, 0, 0, 0 };
	public static final double[] bufferNewSITWA_10W = new double[] { 0, 458, 0, 50, 12, 0, 11, 0, 0, 37, 200, 0, 0, 0,
			7, 4, 0, 0, 0, 0 };
	
	
	public static final double[] bufferSITWA_1W_40 = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 289, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	public static final double[] bufferSITWA_2W_40 = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 289, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	public static final double[] bufferSITWA_3W_40 = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 246, 12, 31, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	public static final double[] bufferSITWA_4W_40 = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 97, 192, 0, 0, 0, 0, 0, 0, 0, 0};
	public static final double[] bufferSITWA_5W_40 = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 172, 117, 0, 0, 0, 0, 0, 0, 0, 0, 0};
	public static final double[] bufferSITWA_6W_40 = new double[] { 0, 0, 0, 0, 0, 0, 0, 61, 32, 0, 118, 62, 16, 0, 0, 0, 0, 0, 0, 0};
	public static final double[] bufferSITWA_7W_40 = new double[] { 0, 0, 0, 0, 19, 0, 19, 22, 0, 3, 193, 0, 0, 21, 12, 0, 0, 0, 0, 0};
	public static final double[] bufferSITWA_8W_40 = new double[] { 0, 0, 0, 0, 31, 0, 0, 23, 0, 0, 170, 0, 1, 48, 16, 0, 0, 0, 0, 0};
	public static final double[] bufferSITWA_9W_40 = new double[] { 0, 0, 0, 0, 50, 0, 0, 0, 0, 0, 212, 0, 0, 10, 17, 0, 0, 0, 0, 0};
	public static final double[] bufferSITWA_10W_40 = new double[] { 0, 0, 0, 76, 7, 0, 16, 0, 0, 7, 183, 0, 0, 0, 0, 0, 0, 0, 0, 0};


	public static final double[] bufferSITWA_1W_405 = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 366, 0, 0, 103, 0, 0, 0, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_2W_405 = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 174, 295, 0, 0, 0, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_3W_405 = new double[] { 0, 0, 0, 0, 0, 0, 0, 68, 0, 261, 59, 56, 0, 25, 0, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_4W_405 = new double[] { 0, 0, 0, 0, 0, 0, 54, 38, 0, 0, 201, 175, 0, 1, 0, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_5W_405 = new double[] { 0, 0, 0, 0, 0, 0, 18, 43, 0, 111, 182, 0, 0, 115, 0, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_6W_405 = new double[] { 0, 0, 0, 0, 12, 0, 33, 65, 0, 0, 182, 10, 21, 38, 108, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_7W_405 = new double[] { 0, 0, 0, 0, 91, 0, 28, 11, 0, 18, 198, 0, 4, 10, 109, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_8W_405 = new double[] { 0, 0, 0, 0, 86, 0, 24, 30, 0, 13, 234, 0, 0, 5, 77, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_9W_405 = new double[] { 0, 0, 0, 10, 52, 0, 29, 77, 0, 7, 262, 0, 0, 15, 9, 8, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_10W_405 = new double[] { 0, 146, 0, 47, 0, 0, 12, 24, 0, 36, 191, 0, 0, 0, 0, 13, 0, 0, 0, 0 };


	public static final double[] bufferSITWA_1W_41 = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 266, 294, 89, 0, 0, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_2W_41 = new double[] { 0, 0, 0, 0, 0, 55, 59, 113, 240, 88, 0, 37, 16, 41, 0, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_3W_41 = new double[] { 0, 0, 0, 0, 0, 0, 192, 49, 30, 0, 115, 103, 114, 46, 0, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_4W_41 = new double[] { 0, 0, 0, 0, 12, 27, 184, 0, 0, 0, 220, 25, 5, 175, 1, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_5W_41 = new double[] { 0, 0, 0, 0, 43, 81, 37, 20, 0, 39, 277, 0, 0, 124, 28, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_6W_41 = new double[] { 0, 0, 0, 0, 107, 0, 26, 72, 0, 9, 173, 60, 5, 1, 196, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_7W_41 = new double[] { 0, 0, 0, 53, 148, 7, 26, 7, 0, 37, 224, 5, 5, 0, 133, 4, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_8W_41 = new double[] { 0, 0, 0, 59, 99, 0, 13, 117, 0, 23, 256, 0, 0, 0, 58, 24, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_9W_41 = new double[] { 0, 104, 0, 103, 39, 0, 33, 62, 0, 30, 240, 0, 0, 6, 1, 31, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_10W_41 = new double[] { 0, 346, 0, 14, 4, 0, 33, 0, 0, 41, 200, 0, 0, 0, 11, 0, 0, 0, 0, 0 };


	public static final double[] bufferSITWA_1W_415 = new double[] { 0, 0, 0, 0, 0, 29, 61, 78, 0, 0, 278, 171, 26, 186, 0, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_2W_415 = new double[] { 0, 0, 0, 0, 0, 177, 63, 91, 2, 7, 185, 218, 0, 86, 0, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_3W_415 = new double[] { 0, 0, 0, 0, 56, 127, 119, 0, 0, 0, 255, 117, 0, 85, 70, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_4W_415 = new double[] { 0, 0, 0, 0, 83, 62, 149, 0, 0, 12, 273, 0, 0, 184, 56, 10, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_5W_415 = new double[] { 0, 0, 0, 0, 142, 73, 49, 26, 0, 56, 255, 0, 0, 124, 104, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_6W_415 = new double[] { 0, 0, 0, 59, 174, 0, 40, 68, 0, 18, 185, 78, 0, 6, 201, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_7W_415 = new double[] { 0, 0, 0, 116, 146, 0, 33, 69, 0, 67, 231, 25, 1, 0, 117, 24, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_8W_415 = new double[] { 0, 95, 0, 111, 99, 0, 18, 137, 0, 28, 251, 0, 0, 0, 60, 30, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_9W_415 = new double[] { 0, 374, 0, 22, 39, 0, 34, 59, 0, 20, 244, 0, 0, 5, 14, 18, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_10W_415 = new double[] { 0, 502, 0, 16, 0, 0, 53, 0, 0, 40, 191, 0, 0, 0, 0, 27, 0, 0, 0, 0 };


	public static final double[] bufferSITWA_1W_42 = new double[] { 0, 0, 0, 0, 0, 9, 144, 247, 0, 0, 224, 93, 13, 260, 19, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_2W_42 = new double[] { 0, 0, 0, 0, 11, 28, 0, 323, 0, 0, 317, 282, 0, 48, 0, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_3W_42 = new double[] { 0, 0, 0, 0, 153, 127, 119, 0, 0, 0, 338, 34, 0, 85, 153, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_4W_42 = new double[] { 0, 0, 0, 91, 114, 42, 169, 0, 0, 22, 273, 0, 8, 176, 46, 68, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_5W_42 = new double[] { 0, 0, 0, 85, 140, 119, 32, 6, 0, 109, 265, 0, 0, 91, 83, 79, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_6W_42 = new double[] { 0, 0, 0, 104, 205, 0, 2, 227, 0, 50, 178, 30, 0, 42, 171, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_7W_42 = new double[] { 0, 131, 0, 127, 146, 0, 31, 112, 0, 55, 239, 21, 6, 0, 108, 33, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_8W_42 = new double[] { 0, 153, 0, 240, 54, 0, 153, 0, 0, 31, 286, 0, 0, 0, 52, 40, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_9W_42 = new double[] { 0, 515, 0, 63, 28, 0, 26, 76, 0, 5, 226, 0, 0, 0, 0, 70, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_10W_42 = new double[] { 0, 672, 0, 21, 0, 0, 33, 0, 0, 41, 207, 0, 0, 0, 19, 16, 0, 0, 0, 0 };


	public static final double[] bufferSITWA_1W_425 = new double[] { 0, 0, 0, 0, 72, 32, 255, 110, 0, 0, 246, 0, 239, 235, 0, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_2W_425 = new double[] { 0, 0, 0, 0, 158, 141, 197, 0, 0, 2, 359, 9, 186, 117, 20, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_3W_425 = new double[] { 0, 0, 0, 9, 252, 134, 94, 26, 0, 17, 364, 0, 0, 50, 213, 30, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_4W_425 = new double[] { 0, 0, 0, 157, 164, 82, 137, 9, 5, 33, 320, 0, 0, 165, 69, 48, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_5W_425 = new double[] { 0, 0, 0, 138, 172, 62, 76, 19, 0, 98, 280, 87, 54, 0, 5, 198, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_6W_425 = new double[] { 0, 0, 0, 79, 208, 0, 46, 247, 0, 29, 380, 0, 0, 100, 99, 1, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_7W_425 = new double[] { 0, 174, 0, 142, 139, 0, 39, 197, 0, 40, 274, 25, 0, 0, 99, 60, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_8W_425 = new double[] { 0, 334, 0, 203, 114, 0, 149, 4, 0, 13, 266, 0, 0, 25, 47, 34, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_9W_425 = new double[] { 0, 622, 0, 19, 47, 0, 45, 90, 0, 25, 245, 0, 6, 0, 0, 90, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_10W_425 = new double[] { 0, 732, 0, 4, 23, 0, 81, 24, 0, 0, 242, 0, 0, 0, 0, 83, 0, 0, 0, 0 };


	public static final double[] bufferSITWA_1W_43 = new double[] { 0, 0, 0, 0, 197, 277, 123, 0, 0, 19, 389, 169, 4, 169, 22, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_2W_43 = new double[] { 0, 0, 0, 0, 306, 50, 83, 173, 28, 6, 362, 16, 0, 305, 40, 0, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_3W_43 = new double[] { 0, 0, 0, 0, 315, 43, 144, 75, 0, 20, 362, 52, 10, 135, 0, 213, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_4W_43 = new double[] { 0, 0, 0, 245, 190, 0, 160, 59, 0, 40, 341, 0, 0, 210, 10, 114, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_5W_43 = new double[] { 0, 0, 25, 165, 234, 58, 22, 88, 0, 199, 181, 123, 0, 0, 27, 247, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_6W_43 = new double[] { 0, 247, 68, 65, 219, 0, 78, 72, 0, 123, 195, 81, 0, 0, 153, 63, 5, 0, 0, 0 };
	public static final double[] bufferSITWA_7W_43 = new double[] { 0, 188, 0, 288, 170, 0, 63, 150, 0, 40, 251, 0, 48, 0, 66, 105, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_8W_43 = new double[] { 0, 405, 0, 230, 73, 0, 173, 0, 0, 19, 271, 0, 43, 0, 17, 138, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_9W_43 = new double[] { 0, 708, 0, 22, 54, 0, 93, 82, 0, 2, 278, 0, 19, 7, 26, 78, 0, 0, 0, 0 };
	public static final double[] bufferSITWA_10W_43 = new double[] { 0, 833, 0, 0, 10, 0, 47, 37, 18, 0, 256, 0, 0, 0, 0, 137, 0, 31, 0, 0 };
	
	

}
