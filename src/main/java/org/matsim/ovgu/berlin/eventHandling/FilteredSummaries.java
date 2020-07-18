package org.matsim.ovgu.berlin.eventHandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilteredSummaries {

	public double avg_tourDuration;

	public HashMap<Double, Integer> timeWindows;
	public HashMap<Integer, Integer> customerPositions;
	private List<Customer> customers_all;
	private List<Customer>[] customers_tw;
	private List<Customer>[] customers_cp;
	private List<Customer>[][] customers_tw_cp;
	public Summary2 summary_all;
	public Summary2[] summary_tw;
	public Summary2[] summary_cp;
	public Summary2[][] summary_tw_cp;

	public FilteredSummaries(List<CarrierTour> plans) {
		init(plans);

		int count_tours = 0;
		double sum_tourDuration = 0;

		for (CarrierTour tour : plans) {
			count_tours++;

			int hour = tour.carrierID;
			double tourDuration = tour.customers.get(tour.customers.size() - 1).destinationDeparture;
			sum_tourDuration += tourDuration - hour * 60 * 60;

			for (int i = 0; i < tour.customers.size(); i++) {
				Customer customer = tour.customers.get(i);
				int tw = timeWindows.get(customer.timewindow);
				int cp = customerPositions.get(i);

				customers_all.add(customer);
				customers_tw[tw].add(customer);
				customers_cp[cp].add(customer);
				customers_tw_cp[tw][cp].add(customer);
			}
		}

		avg_tourDuration = sum_tourDuration / count_tours;

		calculateSummaries();

	}

	private void calculateSummaries() {
		summary_all = new Summary2(customers_all);
		summary_all.avg_tourDuration_all = avg_tourDuration;

		for (int i = 0; i < timeWindows.size(); i++) {
			summary_tw[i] = new Summary2(customers_tw[i]);
			summary_tw[i].avg_tourDuration_all = avg_tourDuration;
		}

		for (int i = 0; i < customerPositions.size(); i++) {
			summary_cp[i] = new Summary2(customers_cp[i]);
			summary_cp[i].avg_tourDuration_all = avg_tourDuration;
		}

		for (int w = 0; w < timeWindows.size(); w++) {
			for (int p = 0; p < customerPositions.size(); p++) {
				summary_tw_cp[w][p] = new Summary2(customers_tw_cp[w][p]);
				summary_tw_cp[w][p].avg_tourDuration_all = avg_tourDuration;
			}
		}
	}

	private void init(List<CarrierTour> plans) {
		timeWindows = new HashMap<Double, Integer>();
		customerPositions = new HashMap<Integer, Integer>();

		for (CarrierTour tour : plans) {
			for (int i = 0; i < tour.customers.size(); i++) {
				Customer customer = tour.customers.get(i);

				// check if new position for hash map
				if (!customerPositions.containsKey(i)) {
					int index = customerPositions.size();
					customerPositions.put(i, index);
				}

				// check if new time window for hash map
				if (!timeWindows.containsKey(customer.timewindow)) {
					int index = timeWindows.size();
					timeWindows.put(customer.timewindow, index);
				}
			}
		}

		int tw = timeWindows.size();
		int cp = customerPositions.size();

		customers_all = new ArrayList<Customer>();

		customers_tw = new ArrayList[tw];
		customers_cp = new ArrayList[cp];
		customers_tw_cp = new ArrayList[tw][cp];

		for (int i = 0; i < tw; i++) {
			customers_tw[i] = new ArrayList<Customer>();
		}

		for (int i = 0; i < cp; i++) {
			customers_cp[i] = new ArrayList<Customer>();
		}

		for (int w = 0; w < tw; w++) {
			for (int p = 0; p < cp; p++) {
				customers_tw_cp[w][p] = new ArrayList<Customer>();
			}
		}

		summary_tw = new Summary2[tw];
		summary_cp = new Summary2[cp];
		summary_tw_cp = new Summary2[tw][cp];
	}
}
