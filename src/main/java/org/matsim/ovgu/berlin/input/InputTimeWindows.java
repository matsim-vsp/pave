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

}
