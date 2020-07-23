package org.matsim.ovgu.berlin.eventHandling;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

public class MatrixEventsHandler implements LinkEnterEventHandler, LinkLeaveEventHandler, PersonArrivalEventHandler,
		PersonDepartureEventHandler {

	private List<Double> arrivals = new ArrayList<Double>();
	private List<Double> departures = new ArrayList<Double>();
	private List<Id<Link>> arrivalLinks = new ArrayList<Id<Link>>();
	private List<Id<Link>> departureLinks = new ArrayList<Id<Link>>();
	private List<Id<Person>> arrivalPersons = new ArrayList<Id<Person>>();
	private List<Id<Person>> departurePersons = new ArrayList<Id<Person>>();

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
		arrivalPersons.add(event.getPersonId());
	}

	@Override
	public void handleEvent(PersonDepartureEvent event) {
		departureLinks.add(event.getLinkId());
		departures.add(event.getTime());
		departurePersons.add(event.getPersonId());
	}

	public String getTravelTimeString() {
		

		Id<Link> departure = departureLinks.get(0);
		Id<Link> arrival = departureLinks.get(departureLinks.size() - 1);
		String str = departure + ";" + arrival + ";";
		double[][] arrivalDepartures = getArrivalDepartures();
		double[] travelTimes = new double[arrivalDepartures.length];
		
		for (int i = 0; i < travelTimes.length; i++)
			str += (arrivalDepartures[i][1] - arrivalDepartures[i][0]) + ";";
		return str;
		
//		Id<Link> from = departureLinks.get(0);
//		String str = from + ";" + departureLinks.get(departureLinks.size() - 1) + ";";
//
//		for (int i = 0; i < arrivals.size(); i++) {
//			if (!departureLinks.get(i).equals(arrivalLinks.get(i)) && departureLinks.get(i).equals(from)) {
//				str += (arrivals.get(i) - departures.get(i)) + ";";
//			}
//		}
//		return str;
	}

	public double[] getTravelTimeArray() {
		double[][] arrivalDepartures = getArrivalDepartures();
		double[] travelTimes = new double[arrivalDepartures.length];
		
		for (int i = 0; i < travelTimes.length; i++)
			travelTimes[i] = arrivalDepartures[i][1] - arrivalDepartures[i][0];
		
		return travelTimes;
		
		
		
//		Id<Link> from = departureLinks.get(0);
//
//		// 24 hours
//		double[] travelTime = new double[24];
//		int counter = 0;
//
//		for (int i = 0; i < arrivals.size(); i++) {
//			if (!departureLinks.get(i).equals(arrivalLinks.get(i)) && departureLinks.get(i).equals(from)) {
//				travelTime[counter++] = arrivals.get(i) - departures.get(i);
//			}
//		}
//		return travelTime;
	}

	private double[][] getArrivalDepartures() {
		Id<Link> departure = departureLinks.get(0);
		Id<Link> arrival = departureLinks.get(departureLinks.size() - 1);
		HashMap<Integer, Id<Person>> persons = getUniquePersonsHashMap();
		HashMap<Id<Person>, Double> departureTimes = getHashMap(departure, departureLinks , departurePersons, departures);
		HashMap<Id<Person>, Double> arrivalTimes = getHashMap(arrival, arrivalLinks, arrivalPersons, arrivals);
		
		// 24 hours / departure and arrival
		double[][] result = new double[24][2];
		
		for (int i = 0; i< persons.size();i++) {
			result[i][0] = departureTimes.get(persons.get(i));
			result[i][1] = arrivalTimes.get(persons.get(i));
		}
		return result;		
	}

	private HashMap<Integer, Id<Person>> getUniquePersonsHashMap() {
		HashMap<Integer, Id<Person>> persons = new HashMap<Integer, Id<Person>>();
		for (Id<Person> person : departurePersons)
			if (!persons.containsValue(person))
				persons.put(persons.size(), person);
		return persons;
	}

	private HashMap<Id<Person>, Double> getHashMap(Id<Link> link, List<Id<Link>> links, List<Id<Person>> persons,
			List<Double> times) {
		HashMap<Id<Person>, Double> result = new HashMap<Id<Person>, Double>();
		for (int i = 0; i < links.size(); i++)
			if (links.get(i).equals(link))
				if (!result.containsKey(persons.get(i)))
					result.put(persons.get(i), times.get(i));
		return result;
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
