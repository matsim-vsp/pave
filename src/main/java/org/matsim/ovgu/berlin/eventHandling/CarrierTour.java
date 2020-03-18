package org.matsim.ovgu.berlin.eventHandling;

import java.util.ArrayList;
import java.util.List;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;

public class CarrierTour {

	public int carrierID; // carrierID == hour
	public Id<Link> linkID;
	public double depature;
	public List<Customer> customers = new ArrayList<Customer>();
	private int flag = 0;
	private Customer customer;
	private double tmpDepatureBeforeCustomer;

	public CarrierTour(int carrierID, Id<Link> linkID, double depature) {
		this.carrierID = carrierID;
		this.linkID = linkID;
		this.depature = depature;
		this.tmpDepatureBeforeCustomer = depature;
	}

	public void addValues(double time, Id<Link> linkID) {
		switch (flag) {
		case 0:
			customer = new Customer();
			customer.customerID = "c" + customers.size();
			customer.originArrival = time;
			customer.originArrivalLinkID = linkID;
			flag++;
			break;
		case 1:
			customer.originDeparture = time;
			customer.originDepartureLinkID = linkID;
			flag++;

			break;
		case 2:
			customer.destinationArrival = time;
			customer.destinationArrivalLinkID = linkID;
			flag++;

			break;
		case 3:
			customer.destinationDeparture = time;
			customer.destinationDepartureLinkID = linkID;
			customer.calculation(tmpDepatureBeforeCustomer);
			tmpDepatureBeforeCustomer = time;
			customers.add(customer);
			flag = 0;
			break;
		}
	}
}
