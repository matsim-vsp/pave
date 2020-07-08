package org.matsim.ovgu.berlin.eventHandling;

import java.util.HashMap;
import java.util.List;

public class Summary {

	public HashMap<Double, Integer> timewindow = new HashMap<Double, Integer>();

	private double[] count_customers_tmp;
	private double[] count_oBeforeTW_tmp;
	private double[] count_oInTW_tmp;
	private double[] count_oAfterTW_tmp;
	private double[] count_dBeforeTW_tmp;
	private double[] count_dInTW_tmp;
	private double[] count_dAfterTW_tmp;

	private double[] sum_oEarlyTW_tmp;
	private double[] sum_oLateTW_tmp;
	private double[] sum_dEarlyTW_tmp;
	private double[] sum_dLateTW_tmp;

	public double[] percent_oBeforeTW_groups;
	public double[] percent_oInTW_groups;
	public double[] percent_oAfterTW_groups;
	public double[] percent_dBeforeTW_groups;
	public double[] percent_dInTW_groups;
	public double[] percent_dAfterTW_groups;

	public double[] avg_oEarlyTW_groups;
	public double[] avg_oLateTW_groups;
	public double[] avg_dEarlyTW_groups;
	public double[] avg_dLateTW_groups;

	public double[] max_oEarlyTW_groups;
	public double[] max_oLateTW_groups;
	public double[] max_dEarlyTW_groups;
	public double[] max_dLateTW_groups;

	public double percent_oBeforeTW_all;
	public double percent_oInTW_all;
	public double percent_oAfterTW_all;
	public double percent_dBeforeTW_all;
	public double percent_dInTW_all;
	public double percent_dAfterTW_all;

	public double avg_oEarlyTW_all;
	public double avg_oLateTW_all;
	public double avg_dEarlyTW_all;
	public double avg_dLateTW_all;

	public double max_oEarlyTW_all;
	public double max_oLateTW_all;
	public double max_dEarlyTW_all;
	public double max_dLateTW_all;

	public double avg_tourDuration_all;

	public Summary(List<CarrierTour> plans) {

		initialzeArrays(plans);

		int count_tours = 0;
		double sum_tourDuration = 0;

		for (CarrierTour tour : plans) {
			count_tours++;

			int hour = tour.carrierID;
			double tourDuration = tour.customers.get(tour.customers.size() - 1).destinationDeparture;
			sum_tourDuration += tourDuration - hour * 60 * 60;

			for (Customer customer : tour.customers) {
				int i = timewindow.get(customer.timewindow);

				count_customers_tmp[i]++;
				sum_oEarlyTW_tmp[i] += customer.oEarlyTW;
				sum_oLateTW_tmp[i] += customer.oLateTW;
				sum_dEarlyTW_tmp[i] += customer.dEarlyTW;
				sum_dLateTW_tmp[i] += customer.dLateTW;

				if (max_oEarlyTW_groups[i] < customer.oEarlyTW)
					max_oEarlyTW_groups[i] = customer.oEarlyTW;
				if (max_oLateTW_groups[i] < customer.oLateTW)
					max_oLateTW_groups[i] = customer.oLateTW;
				if (max_dEarlyTW_groups[i] < customer.dEarlyTW)
					max_dEarlyTW_groups[i] = customer.dEarlyTW;
				if (max_dLateTW_groups[i] < customer.dLateTW)
					max_dLateTW_groups[i] = customer.dLateTW;

				if (customer.oBeforeTW)
					count_oBeforeTW_tmp[i]++;
				if (customer.oInTW)
					count_oInTW_tmp[i]++;
				if (customer.oAfterTW)
					count_oAfterTW_tmp[i]++;

				if (customer.dBeforeTW)
					count_dBeforeTW_tmp[i]++;
				if (customer.dInTW)
					count_dInTW_tmp[i]++;
				if (customer.dAfterTW)
					count_dAfterTW_tmp[i]++;
			}
		}

		for (int i = 0; i < timewindow.size(); i++) {
			percent_oBeforeTW_groups[i] = count_oBeforeTW_tmp[i] / count_customers_tmp[i];
			percent_oInTW_groups[i] = count_oInTW_tmp[i] / count_customers_tmp[i];
			percent_oAfterTW_groups[i] = count_oAfterTW_tmp[i] / count_customers_tmp[i];
			percent_dBeforeTW_groups[i] = count_dBeforeTW_tmp[i] / count_customers_tmp[i];
			percent_dInTW_groups[i] = count_dInTW_tmp[i] / count_customers_tmp[i];
			percent_dAfterTW_groups[i] = count_dAfterTW_tmp[i] / count_customers_tmp[i];

			avg_oEarlyTW_groups[i] = sum_oEarlyTW_tmp[i] / count_oBeforeTW_tmp[i];
			avg_oLateTW_groups[i] = sum_oLateTW_tmp[i] / count_oAfterTW_tmp[i];
			avg_dEarlyTW_groups[i] = sum_dEarlyTW_tmp[i] / count_dBeforeTW_tmp[i];
			avg_dLateTW_groups[i] = sum_dLateTW_tmp[i] / count_dAfterTW_tmp[i];
		}

		calculateSummaryAll();

		avg_tourDuration_all = sum_tourDuration / count_tours;
	}

	private void calculateSummaryAll() {
		percent_oBeforeTW_all = getAVG(percent_oBeforeTW_groups);
		percent_oInTW_all = getAVG(percent_oInTW_groups);
		percent_oAfterTW_all = getAVG(percent_oAfterTW_groups);
		percent_dBeforeTW_all = getAVG(percent_dBeforeTW_groups);
		percent_dInTW_all = getAVG(percent_dInTW_groups);
		percent_dAfterTW_all = getAVG(percent_dAfterTW_groups);

		avg_oEarlyTW_all = getAVG(avg_oEarlyTW_groups);
		avg_oLateTW_all = getAVG(avg_oLateTW_groups);
		avg_dEarlyTW_all = getAVG(avg_dEarlyTW_groups);
		avg_dLateTW_all = getAVG(avg_dLateTW_groups);

		max_oEarlyTW_all = getMAX(max_oEarlyTW_groups);
		max_oLateTW_all = getMAX(max_oLateTW_groups);
		max_dEarlyTW_all = getMAX(max_dEarlyTW_groups);
		max_dLateTW_all = getMAX(max_dLateTW_groups);

	}

	private double getAVG(double[] values) {
		double sum = 0;
		for (int i = 0; i < values.length; i++)
			sum += values[i];
		return sum / (double) values.length;
	}

	private double getMAX(double[] values) {
		double max = 0;
		for (int i = 0; i < values.length; i++)
			if (max < values[i])
				max = values[i];
		return max;
	}

	private void initialzeArrays(List<CarrierTour> plans) {

		for (CarrierTour tour : plans) {
			for (Customer customer : tour.customers) {
				if (!timewindow.containsKey(customer.timewindow)) {
					int index = timewindow.size();
					timewindow.put(customer.timewindow, index);
				}
			}
		}
		int i = timewindow.size();

		count_customers_tmp = new double[i];
		count_oBeforeTW_tmp = new double[i];
		count_oInTW_tmp = new double[i];
		count_oAfterTW_tmp = new double[i];
		count_dBeforeTW_tmp = new double[i];
		count_dInTW_tmp = new double[i];
		count_dAfterTW_tmp = new double[i];

		sum_oEarlyTW_tmp = new double[i];
		sum_oLateTW_tmp = new double[i];
		sum_dEarlyTW_tmp = new double[i];
		sum_dLateTW_tmp = new double[i];

		percent_oBeforeTW_groups = new double[i];
		percent_oInTW_groups = new double[i];
		percent_oAfterTW_groups = new double[i];
		percent_dBeforeTW_groups = new double[i];
		percent_dInTW_groups = new double[i];
		percent_dAfterTW_groups = new double[i];

		avg_oEarlyTW_groups = new double[i];
		avg_oLateTW_groups = new double[i];
		avg_dEarlyTW_groups = new double[i];
		avg_dLateTW_groups = new double[i];

		max_oEarlyTW_groups = new double[i];
		max_oLateTW_groups = new double[i];
		max_dEarlyTW_groups = new double[i];
		max_dLateTW_groups = new double[i];
	}
}
