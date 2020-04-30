package org.matsim.ovgu.berlin;

public class Settings {

	// network change events file
	public String pathChangeEvents = "input/scenario-A.15.networkChangeEvents.xml.gz";

	// output directory
	private String dir = System.getProperty("user.dir");
	public String directory = dir + "/output/OVGU/";

	// depot link id
	public String depot;

	// link id's for example tour
	public String[] tour;

	// serviceTime for every location
	public double serviceTime = 2 * 60;

	// use buffer data set
	public double[] buffer;

	// use buffer data set
	public double[] timeWindow;
	public String timeWindowMethod = "PlusMinus";
	// time window methods -> PlusMinusArrival or AfterArrival

	// use buffer data set
	public double[] expectedTravelTime;

	// use buffer data set
	public boolean subtractBuffer = false;

	// buffer calculation
	public double[] getExpectedArrivalTimes(int hour) {
		if (expectedTravelTime == null)
			return null;
		else {

			double tourStart = hour * 3600.;
			// calculate expected arrival times from expected travel times
			double[] expectedArrival = new double[expectedTravelTime.length];
			expectedArrival[0] = tourStart + expectedTravelTime[0];
			for (int x = 1; x < expectedArrival.length; x++)
				if (subtractBuffer)
					expectedArrival[x] = expectedArrival[x - 1] + serviceTime + expectedTravelTime[x] - buffer[x];
				else
					expectedArrival[x] = expectedArrival[x - 1] + serviceTime + expectedTravelTime[x] + buffer[x];

			return expectedArrival;
		}
	}
}