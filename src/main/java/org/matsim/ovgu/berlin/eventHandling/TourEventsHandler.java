package org.matsim.ovgu.berlin.eventHandling;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.LinkLeaveEvent;
import org.matsim.api.core.v01.events.PersonArrivalEvent;
import org.matsim.api.core.v01.events.PersonDepartureEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.events.handler.LinkLeaveEventHandler;
import org.matsim.api.core.v01.events.handler.PersonArrivalEventHandler;
import org.matsim.api.core.v01.events.handler.PersonDepartureEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Person;
import org.matsim.ovgu.berlin.input.Input;

public class TourEventsHandler implements LinkEnterEventHandler, LinkLeaveEventHandler, PersonArrivalEventHandler,
		PersonDepartureEventHandler {

	private List<CarrierTour> plans = new ArrayList<CarrierTour>();

	@Override
	public void reset(int iteration) {
		System.out.println("reset...");
	}

	@Override
	public void handleEvent(LinkEnterEvent event) {
	}

	@Override
	public void handleEvent(LinkLeaveEvent event) {
	}

	@Override
	public void handleEvent(PersonArrivalEvent event) {
		int carrierID = getCarrierID(event.getPersonId());
		Id<Link> linkID = event.getLinkId();
		double time = event.getTime();
		handleTours(carrierID, linkID, time);
	}

	@Override
	public void handleEvent(PersonDepartureEvent event) {
		int carrierID = getCarrierID(event.getPersonId());
		Id<Link> linkID = event.getLinkId();
		double time = event.getTime();
		handleTours(carrierID, linkID, time);
	}

	private void handleTours(int carrierID, Id<Link> linkID, double time) {

		CarrierTour tour = getTour(carrierID);
		if (tour == null)
			plans.add(new CarrierTour(carrierID, linkID, time));
		else
			tour.addValues(time, linkID);

	}

	private CarrierTour getTour(int carrierID) {

		for (CarrierTour tour : plans)
			if (tour.carrierID == carrierID)
				return tour;

		return null;
	}

	private int getCarrierID(Id<Person> personID) {

		char number1 = personID.toString().charAt(15);
		char number2 = personID.toString().charAt(16);

		if (Character.isDigit(number2))
			return Integer.parseInt("" + number1 + number2);
		else
			return Integer.parseInt("" + number1);
	}

	public void compareExpectedArrivals(double[] expectedTravelTime, double[] timewindows) {
		for (CarrierTour tour : plans) {
			int hour = tour.carrierID; // hour == carrierID
			double[] expectedArrivalTimes = Input.getExpectedArrivalTimes(hour, expectedTravelTime);
			int x = 0;
			for (Customer customer : tour.customers) {
				double customerTW = timewindows[x];
				double oExpectedArrival = expectedArrivalTimes[x++];
				double dExpectedArrival = expectedArrivalTimes[x++];
				customer.analysis(oExpectedArrival, dExpectedArrival, customerTW);
			}
		}
	}

	public List<CarrierTour> getPlans() {
		return plans;
	}

	public void printCSV(String filePath) {
		try {
			FileWriter csvWriter = new FileWriter(filePath);
			csvWriter.append(
					"hour/ID; customer; from; to; driveToCustomerTime; oArrival; oServiceTime; oDeparture; odTravelTime; dArrival; dServiceTime; dDeparture;;"
							+ "oExpectedArrival; oTwStart; oTwEnd; oBeforeTW; oInTW; oAfterTW; oEarlyTW; oLateTW; oEarly; oLate;;"
							+ "dExpectedArrival; dTwStart; dTwEnd; dBeforeTW; dInTW; dAfterTW; dEarlyTW; oLateTW; dEarly; dLate;;timewindow\n");

			// do it
			for (CarrierTour tour : plans) {
				for (Customer customer : tour.customers) {

					int hour = tour.carrierID;
					String customerID = customer.customerID;
					Id<Link> originID = customer.originArrivalLinkID;
					Id<Link> destID = customer.destinationArrivalLinkID;
					double oArrival = customer.originArrival;
					double oServiceTime = customer.originServiceTime;
					double oDeparture = customer.originDeparture;
					double odTravelTime = customer.odTravelTime;
					double dArrival = customer.destinationArrival;
					double dServiceTime = customer.destinationServiceTime;
					double dDeparture = customer.destinationDeparture;
					double driveToCustomerTime = customer.toOriginTravelTime;
					double oExpectedArrival = customer.oExpectedArrival;
					double oTwStart = customer.oTwStart;
					double oTwEnd = customer.oTwEnd;
					boolean oBeforeTW = customer.oBeforeTW;
					boolean oInTW = customer.oInTW;
					boolean oAfterTW = customer.oAfterTW;
					double oEarlyTW = customer.oEarlyTW;
					double oLateTW = customer.oLateTW;
					double oEarly = customer.oEarly;
					double oLate = customer.oLate;
					double dExpectedArrival = customer.dExpectedArrival;
					double dTwStart = customer.dTwStart;
					double dTwEnd = customer.dTwEnd;
					boolean dBeforeTW = customer.dBeforeTW;
					boolean dInTW = customer.dInTW;
					boolean dAfterTW = customer.dAfterTW;
					double dEarlyTW = customer.dEarlyTW;
					double dLateTW = customer.dLateTW;
					double dEarly = customer.dEarly;
					double dLate = customer.dLate;
					double timewindow = customer.timewindow;

					String str = hour + ";" + customerID + ";" + originID + ";" + destID + ";" + driveToCustomerTime
							+ ";" + oArrival + ";" + oServiceTime + ";" + oDeparture + ";" + odTravelTime + ";"
							+ dArrival + ";" + dServiceTime + ";" + dDeparture + ";;" + oExpectedArrival + ";"
							+ oTwStart + ";" + oTwEnd + ";" + oBeforeTW + ";" + oInTW + ";" + oAfterTW + ";" + oEarlyTW
							+ ";" + oLateTW + ";" + oEarly + ";" + oLate + ";;" + dExpectedArrival + ";" + dTwStart
							+ ";" + dTwEnd + ";" + dBeforeTW + ";" + dInTW + ";" + dAfterTW + ";" + dEarlyTW + ";"
							+ dLateTW + ";" + dEarly + ";" + dLate + ";;" + timewindow + ";\n";
					csvWriter.append(str);
				}
			}
			// finish
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
