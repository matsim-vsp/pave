package org.matsim.ovgu.berlin.eventHandling;

import java.util.List;

public class Summary {

	public double percent_oBeforeTW = 0;
	public double percent_oInTW = 0;
	public double percent_oAfterTW = 0;
	public double percent_dBeforeTW = 0;
	public double percent_dInTW = 0;
	public double percent_dAfterTW = 0;

	public double avg_oEarlyTW = 0;
	public double avg_oLateTW = 0;
	public double avg_dEarlyTW = 0;
	public double avg_dLateTW = 0;

	public double max_oEarlyTW = 0;
	public double max_oLateTW = 0;
	public double max_dEarlyTW = 0;
	public double max_dLateTW = 0;

	public double avg_tourDuration = 0;

	public Summary(List<CarrierTour> plans) {

		double count_customers = 0;
		double count_oBeforeTW = 0;
		double count_oInTW = 0;
		double count_oAfterTW = 0;
		double count_dBeforeTW = 0;
		double count_dInTW = 0;
		double count_dAfterTW = 0;
		double sum_oEarlyTW = 0;
		double sum_oLateTW = 0;
		double sum_dEarlyTW = 0;
		double sum_dLateTW = 0;
		max_oEarlyTW = 0;
		max_oLateTW = 0;
		max_dEarlyTW = 0;
		max_dLateTW = 0;

		int count_tours = 0;
		double sum_tourDuration = 0;

		for (CarrierTour tour : plans) {
			count_tours++;

			int hour = tour.carrierID;
			double tourDuration = tour.customers.get(tour.customers.size() - 1).destinationDeparture;
			sum_tourDuration += tourDuration - hour * 60 * 60;

			for (Customer customer : tour.customers) {
				count_customers++;
				sum_oEarlyTW += customer.oEarlyTW;
				sum_oLateTW += customer.oLateTW;
				sum_dEarlyTW += customer.dEarlyTW;
				sum_dLateTW += customer.dLateTW;

				if (max_oEarlyTW < customer.oEarlyTW)
					max_oEarlyTW = customer.oEarlyTW;
				if (max_oLateTW < customer.oLateTW)
					max_oLateTW = customer.oLateTW;
				if (max_dEarlyTW < customer.dEarlyTW)
					max_dEarlyTW = customer.dEarlyTW;
				if (max_dLateTW < customer.dLateTW)
					max_dLateTW = customer.dLateTW;

				if (customer.oBeforeTW)
					count_oBeforeTW++;
				if (customer.oInTW)
					count_oInTW++;
				if (customer.oAfterTW)
					count_oAfterTW++;

				if (customer.dBeforeTW)
					count_dBeforeTW++;
				if (customer.dInTW)
					count_dInTW++;
				if (customer.dAfterTW)
					count_dAfterTW++;
			}
		}

		percent_oBeforeTW = count_oBeforeTW / count_customers;
		percent_oInTW = count_oInTW / count_customers;
		percent_oAfterTW = count_oAfterTW / count_customers;
		percent_dBeforeTW = count_dBeforeTW / count_customers;
		percent_dInTW = count_dInTW / count_customers;
		percent_dAfterTW = count_dAfterTW / count_customers;

		avg_oEarlyTW = sum_oEarlyTW / count_oBeforeTW;
		avg_oLateTW = sum_oLateTW / count_oAfterTW;
		avg_dEarlyTW = sum_dEarlyTW / count_dBeforeTW;
		avg_dLateTW = sum_dLateTW / count_dAfterTW;

		avg_tourDuration = sum_tourDuration / count_tours;
	}

}
