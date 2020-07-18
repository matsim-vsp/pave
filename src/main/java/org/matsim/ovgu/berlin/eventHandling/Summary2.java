package org.matsim.ovgu.berlin.eventHandling;

import java.util.List;

public class Summary2 {

	private double count_customers_tmp;
	private double count_oBeforeTW_tmp;
	private double count_oInTW_tmp;
	private double count_oAfterTW_tmp;
	private double count_dBeforeTW_tmp;
	private double count_dInTW_tmp;
	private double count_dAfterTW_tmp;

	private double sum_oEarlyTW_tmp;
	private double sum_oLateTW_tmp;
	private double sum_dEarlyTW_tmp;
	private double sum_dLateTW_tmp;

	public double percent_oBeforeTW;
	public double percent_oInTW;
	public double percent_oAfterTW;
	public double percent_dBeforeTW;
	public double percent_dInTW;
	public double percent_dAfterTW;

	public double avg_oEarlyTW;
	public double avg_oLateTW;
	public double avg_dEarlyTW;
	public double avg_dLateTW;

	public double max_oEarlyTW;
	public double max_oLateTW;
	public double max_dEarlyTW;
	public double max_dLateTW;

	public double avg_tourDuration_all;

	public Summary2(List<Customer> customers) {
		initialzeVariables();
		for (Customer customer : customers) {

			count_customers_tmp++;
			sum_oEarlyTW_tmp += customer.oEarlyTW;
			sum_oLateTW_tmp += customer.oLateTW;
			sum_dEarlyTW_tmp += customer.dEarlyTW;
			sum_dLateTW_tmp += customer.dLateTW;

			if (max_oEarlyTW < customer.oEarlyTW)
				max_oEarlyTW = customer.oEarlyTW;
			if (max_oLateTW < customer.oLateTW)
				max_oLateTW = customer.oLateTW;
			if (max_dEarlyTW < customer.dEarlyTW)
				max_dEarlyTW = customer.dEarlyTW;
			if (max_dLateTW < customer.dLateTW)
				max_dLateTW = customer.dLateTW;

			if (customer.oBeforeTW)
				count_oBeforeTW_tmp++;
			if (customer.oInTW)
				count_oInTW_tmp++;
			if (customer.oAfterTW)
				count_oAfterTW_tmp++;

			if (customer.dBeforeTW)
				count_dBeforeTW_tmp++;
			if (customer.dInTW)
				count_dInTW_tmp++;
			if (customer.dAfterTW)
				count_dAfterTW_tmp++;
		}

		percent_oBeforeTW = count_oBeforeTW_tmp / count_customers_tmp;
		percent_oInTW = count_oInTW_tmp / count_customers_tmp;
		percent_oAfterTW = count_oAfterTW_tmp / count_customers_tmp;
		percent_dBeforeTW = count_dBeforeTW_tmp / count_customers_tmp;
		percent_dInTW = count_dInTW_tmp / count_customers_tmp;
		percent_dAfterTW = count_dAfterTW_tmp / count_customers_tmp;

		avg_oEarlyTW = sum_oEarlyTW_tmp / count_oBeforeTW_tmp;
		avg_oLateTW = sum_oLateTW_tmp / count_oAfterTW_tmp;
		avg_dEarlyTW = sum_dEarlyTW_tmp / count_dBeforeTW_tmp;
		avg_dLateTW = sum_dLateTW_tmp / count_dAfterTW_tmp;
	}

	private void initialzeVariables() {

		count_customers_tmp = 0;
		count_oBeforeTW_tmp = 0;
		count_oInTW_tmp = 0;
		count_oAfterTW_tmp = 0;
		count_dBeforeTW_tmp = 0;
		count_dInTW_tmp = 0;
		count_dAfterTW_tmp = 0;

		sum_oEarlyTW_tmp = 0;
		sum_oLateTW_tmp = 0;
		sum_dEarlyTW_tmp = 0;
		sum_dLateTW_tmp = 0;

		percent_oBeforeTW = 0;
		percent_oInTW = 0;
		percent_oAfterTW = 0;
		percent_dBeforeTW = 0;
		percent_dInTW = 0;
		percent_dAfterTW = 0;

		avg_oEarlyTW = 0;
		avg_oLateTW = 0;
		avg_dEarlyTW = 0;
		avg_dLateTW = 0;

		max_oEarlyTW = 0;
		max_oLateTW = 0;
		max_dEarlyTW = 0;
		max_dLateTW = 0;
	}
}
