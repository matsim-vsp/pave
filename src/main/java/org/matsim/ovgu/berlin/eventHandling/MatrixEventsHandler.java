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

public class MatrixEventsHandler implements LinkEnterEventHandler, LinkLeaveEventHandler, PersonArrivalEventHandler,
		PersonDepartureEventHandler {

	private List<Double> arrivals = new ArrayList<Double>();
	private List<Double> departures = new ArrayList<Double>();
	private List<Id<Link>> arrivalLinks = new ArrayList<Id<Link>>();
	private List<Id<Link>> departureLinks = new ArrayList<Id<Link>>();

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
		arrivalLinks.add(event.getLinkId());
		arrivals.add(event.getTime());
	}

	@Override
	public void handleEvent(PersonDepartureEvent event) {
		departureLinks.add(event.getLinkId());
		departures.add(event.getTime());
	}

	public String getTravelTimeString() {
		Id<Link> from = departureLinks.get(0);
		String str = from + ";" + departureLinks.get(departureLinks.size() - 1) + ";";

		for (int i = 0; i < arrivals.size(); i++) {
			if (!departureLinks.get(i).equals(arrivalLinks.get(i)) && departureLinks.get(i).equals(from)) {
				str += (arrivals.get(i) - departures.get(i)) + ";";
			}
		}
		return str;
	}

	public void printCSV(String filePath) {
		try {
			FileWriter csvWriter = new FileWriter(filePath);

			Id<Link> from = departureLinks.get(0);
			String head = "from; to; ";
			String content = from + ";" + departureLinks.get(departureLinks.size() - 1) + ";";

			// do it
			for (int i = 0; i < arrivals.size(); i++) {
				if (!departureLinks.get(i).equals(arrivalLinks.get(i)) && departureLinks.get(i).equals(from)) {
					head += (departures.get(i) / 3600) + ";";
					content += (arrivals.get(i) - departures.get(i)) + ";";
				}
			}

			head += "\n";
			content += "\n";
			csvWriter.append(head);
			csvWriter.append(content);
			// finish
			csvWriter.flush();
			csvWriter.close();

		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
