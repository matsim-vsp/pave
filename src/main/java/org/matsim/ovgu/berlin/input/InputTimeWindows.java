package org.matsim.ovgu.berlin.input;

public class InputTimeWindows {

	private static final double standardTW = 10 * 60;
	private static final double premiumTW = 2 * 60;

	public static final double[] standardTWs = new double[] { standardTW, standardTW, standardTW, standardTW,
			standardTW, standardTW, standardTW, standardTW, standardTW, standardTW, standardTW, standardTW, standardTW,
			standardTW, standardTW, standardTW, standardTW, standardTW, standardTW, standardTW };

	public static final double[] premiumTWs = new double[] { premiumTW, premiumTW, premiumTW, premiumTW, premiumTW,
			premiumTW, premiumTW, premiumTW, premiumTW, premiumTW, premiumTW, premiumTW, premiumTW, premiumTW,
			premiumTW, premiumTW, premiumTW, premiumTW, premiumTW, premiumTW };

	public static final double[] mix1spsp = new double[] { standardTW, standardTW, premiumTW, premiumTW, standardTW,
			standardTW, premiumTW, premiumTW, standardTW, standardTW, premiumTW, premiumTW, standardTW, standardTW,
			premiumTW, premiumTW, standardTW, standardTW, premiumTW, premiumTW };

	public static final double[] mix2sp = new double[] { standardTW, standardTW, standardTW, standardTW, standardTW,
			standardTW, standardTW, standardTW, standardTW, standardTW, premiumTW, premiumTW, premiumTW, premiumTW,
			premiumTW, premiumTW, premiumTW, premiumTW, premiumTW, premiumTW };

	public static final double[] mix3ps = new double[] { premiumTW, premiumTW, premiumTW, premiumTW, premiumTW,
			premiumTW, premiumTW, premiumTW, premiumTW, premiumTW, standardTW, standardTW, standardTW, standardTW,
			standardTW, standardTW, standardTW, standardTW, standardTW, standardTW };

	public static final double[] mix4f3ps = new double[] { premiumTW, premiumTW, premiumTW, premiumTW, premiumTW,
			premiumTW, standardTW, standardTW, standardTW, standardTW, standardTW, standardTW, standardTW, standardTW,
			standardTW, standardTW, standardTW, standardTW, standardTW, standardTW };

	public static final double[] mix5m4p = new double[] { standardTW, standardTW, standardTW, standardTW, standardTW,
			standardTW, premiumTW, premiumTW, premiumTW, premiumTW, premiumTW, premiumTW, premiumTW, premiumTW,
			standardTW, standardTW, standardTW, standardTW, standardTW, standardTW };

	public static final double[] mix6l3p = new double[] { standardTW, standardTW, standardTW, standardTW, standardTW,
			standardTW, standardTW, standardTW, standardTW, standardTW, standardTW, standardTW, standardTW, standardTW,
			standardTW, standardTW, standardTW, standardTW, standardTW, standardTW };

	public static final double[] all1Min = new double[] { 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60,
			60, 60, 60, 60, 60 };
	public static final double[] all2Min = new double[] { 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120, 120,
			120, 120, 120, 120, 120 };
	public static final double[] all3Min = new double[] { 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180, 180,
			180, 180, 180, 180, 180 };
	public static final double[] all4Min = new double[] { 240, 240, 240, 240, 240, 240, 240, 240, 240, 240, 240, 240, 240, 240, 240,
			240, 240, 240, 240, 240 };
	public static final double[] all5Min = new double[] { 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300, 300,
			300, 300, 300, 300, 300 };
	public static final double[] all6Min = new double[] { 360, 360, 360, 360, 360, 360, 360, 360, 360, 360, 360, 360, 360, 360, 360,
			360, 360, 360, 360, 360 };
	public static final double[] all7Min = new double[] { 420, 420, 420, 420, 420, 420, 420, 420, 420, 420, 420, 420, 420, 420, 420,
			420, 420, 420, 420, 420 };
	public static final double[] all8Min = new double[] { 480, 480, 480, 480, 480, 480, 480, 480, 480, 480, 480, 480, 480, 480, 480,
			480, 480, 480, 480, 480 };
	public static final double[] all9Min = new double[] { 540, 540, 540, 540, 540, 540, 540, 540, 540, 540, 540, 540, 540, 540, 540,
			540, 540, 540, 540, 540 };
	public static final double[] all10Min = new double[] { 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600, 600,
			600, 600, 600, 600, 600 };

}
