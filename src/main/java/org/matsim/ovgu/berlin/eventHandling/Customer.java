package org.matsim.ovgu.berlin.eventHandling;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;

public class Customer {

	// figures from EventFile
	public String customerID;
	public Id<Link> originArrivalLinkID;
	public double originArrival;
	public Id<Link> originDepartureLinkID;
	public double originDeparture;
	public Id<Link> destinationArrivalLinkID;
	public double destinationArrival;
	public Id<Link> destinationDepartureLinkID;
	public double destinationDeparture;

	// figures calculated from Events
	public double originServiceTime;
	public double destinationServiceTime;
	public double odTravelTime;
	public double toOriginTravelTime;

	public void calculation(double departureBefore) {
		originServiceTime = originDeparture - originArrival;
		destinationServiceTime = destinationDeparture - destinationArrival;
		odTravelTime = destinationArrival - originDeparture;
		toOriginTravelTime = originArrival - departureBefore;
	}

	// figures calculated with expected travel times and timewindow
	public double oExpectedArrival;
	public double oTwStart;
	public double oTwEnd;
	public boolean oBeforeTW = false;
	public boolean oInTW = false;
	public boolean oAfterTW = false;
	public double oEarlyTW;
	public double oLateTW;
	public double oEarly;
	public double oLate;
	public double dExpectedArrival;
	public double dTwStart;
	public double dTwEnd;
	public boolean dBeforeTW = false;
	public boolean dInTW = false;
	public boolean dAfterTW = false;
	public double dEarlyTW;
	public double dLateTW;
	public double dEarly;
	public double dLate;
	public double timewindow;

	public void analysis(double oExpectedArrival, double dExpectedArrival, double timewindow) {
		this.timewindow = timewindow;
		this.oExpectedArrival = oExpectedArrival;
		oTwStart = oExpectedArrival - timewindow / 2;
		oTwEnd = oExpectedArrival + timewindow / 2;

		if (originArrival < oTwStart) {
			oBeforeTW = true;
			oEarlyTW = oTwStart - originArrival;
			oEarly = oExpectedArrival - originArrival;
		} else if (originArrival > oTwEnd) {
			oAfterTW = true;
			oLateTW = originArrival - oTwEnd;
			oLate = originArrival - oExpectedArrival;
		} else {
			oInTW = true;
			if (originArrival < oExpectedArrival)
				oEarly = oExpectedArrival - originArrival;
			else
				oLate = originArrival - oExpectedArrival;
		}

		this.dExpectedArrival = dExpectedArrival;
		dTwStart = dExpectedArrival - timewindow / 2;
		dTwEnd = dExpectedArrival + timewindow / 2;

		if (destinationArrival < dTwStart) {
			dBeforeTW = true;
			dEarlyTW = dTwStart - destinationArrival;
			dEarly = dExpectedArrival - destinationArrival;
		} else if (destinationArrival > dTwEnd) {
			dAfterTW = true;
			dLateTW = destinationArrival - dTwEnd;
			dLate = destinationArrival - dExpectedArrival;
		} else {
			dInTW = true;
			if (destinationArrival < dExpectedArrival)
				dEarly = dExpectedArrival - destinationArrival;
			else
				dLate = destinationArrival - dExpectedArrival;
		}
	}

}
