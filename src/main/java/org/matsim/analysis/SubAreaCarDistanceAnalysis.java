/* *********************************************************************** *
 * project: org.matsim.*
 * Controler.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.analysis;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.events.*;
import org.matsim.api.core.v01.events.handler.*;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.network.NetworkUtils;
import org.matsim.vehicles.Vehicle;

import java.io.*;
import java.util.*;

public class SubAreaCarDistanceAnalysis {

	final static String networkFile = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.5-10pct/input/berlin-v5.5-network.xml.gz";

	private static final Logger log = Logger.getLogger(SubAreaCarDistanceAnalysis.class);

	public static void main(String[] args) {

		String root = "D:/pave_runs/";

		String scenario1 = "S1";

		Set<String> runIDs = new HashSet<>();
//		runIDs.add("p1-1");
//		runIDs.add("p1-2");
//		runIDs.add("p1-3");
//		runIDs.add("p1-4");
//		runIDs.add("p1-5");
//		runIDs.add("p1-6");
//		runIDs.add("p1-7");
//		runIDs.add("p1-8");
//		runIDs.add("p1-9");
//		runIDs.add("p1-10");
//		runIDs.add("p1-11");
//		runIDs.add("p1-12");
//
//		runIDs.forEach(runID -> processRun(root, scenario1, runID));

		String scenario2 = "S2";

		runIDs = new HashSet<>();
		runIDs.add("p2-1");
		runIDs.add("p2-5");
//		runIDs.add("p2-22");
//		runIDs.add("p2-23");
//		runIDs.add("p2-24");
//		runIDs.add("p2-25");
//		runIDs.add("p2-26");
//		runIDs.add("p2-28");
//		runIDs.add("p2-29");
//		runIDs.add("p2-30");
//		runIDs.add("p2-31");
//		runIDs.add("p2-32");

		runIDs.forEach(runID -> processRun(root, scenario2, runID));
	}

	private static void processRun(String root, String scenario, String runID) {
		String eventsFile = root + scenario + "/output-" + runID + "/" + runID + ".output_events.xml.gz";
		String outputFileName = root + scenario + "/output-" + runID + "/" + runID + ".subAreaCarKm.txt";

//		eventsFile =  "D:/pave_runs/output-p-baseDRT100/p-baseDRT100.output_events.xml.gz";
//		outputFileName = "D:/pave_runs/output-p-baseDRT100/p-baseDRT100.subAreaCarKm.txt";

		log.info("load links-txt-files");
		Set<Id<Link>> berlinLinks = LinksInArea.loadLinksInBerlin();
		Set<Id<Link>> hundekopfLinks = LinksInArea.loadLinksInHundekopf();

		Set<?>[] areas =  new Set<?>[2];
		areas[0] = berlinLinks;
		areas[1] = hundekopfLinks;

		SubAreaCarDistanceHandler handler = new SubAreaCarDistanceHandler(NetworkUtils.readNetwork(networkFile), (Set<Id<Link>>[]) areas);
		EventsManager eventsManager = EventsUtils.createEventsManager();
		eventsManager.addHandler(handler);


		log.info("start events reading");
		eventsManager.initProcessing();
		EventsUtils.readEvents(eventsManager, eventsFile);


		eventsManager.finishProcessing();

		log.info("dump output to " + outputFileName);
		try {
			BufferedWriter writer =  new BufferedWriter(new FileWriter(outputFileName));
			writer.write("berlinCarKm;" + (handler.carKm[0] / 1000));
			writer.newLine();
			writer.write("hundeKopfCarKm;" + (handler.carKm[1] / 1000));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static Set<Id<Link>> loadLinksFile(String fileName){
		Set<Id<Link>> links = new HashSet<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String header = reader.readLine();
			String line = reader.readLine();
			while(line != null){
				links.add(Id.createLinkId(line));
				line = reader.readLine();
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException("could not load file " + fileName + ".\n you should run writeLinksInAreasFiles() first");
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return links;
	}

}


class SubAreaCarDistanceHandler implements PersonDepartureEventHandler, LinkEnterEventHandler, PersonLeavesVehicleEventHandler, PersonEntersVehicleEventHandler {

	private Set<Id<Vehicle>> cars = new HashSet<>();
	private Set<Id<Person>> carDrivers = new HashSet<>();

	Set<Id<Link>>[] subAreas;
	double[] carKm;

	private Network network;

	public SubAreaCarDistanceHandler(Network network, Set<Id<Link>>[] subAreaLinkSets) {
		this.subAreas = subAreaLinkSets;
		this.carKm = new double[subAreas.length];
		Arrays.fill(carKm, 0.0);
		this.network = network;
	}

	@Override
	public void handleEvent(LinkEnterEvent event) {
		if(cars.contains(event.getVehicleId())){
			for (int i = 0; i < subAreas.length; i++) {
				Set<Id<Link>> subArea = subAreas[i];
				if(subArea.contains(event.getLinkId())) carKm[i] += network.getLinks().get(event.getLinkId()).getLength();
			}
		}
	}

	@Override
	public void handleEvent(PersonDepartureEvent event) {
		if(event.getLegMode().equals(TransportMode.car)){
			this.carDrivers.add(event.getPersonId());
		}
	}

	@Override
	public void reset(int iteration) {
		this.cars.clear();
		this.carDrivers.clear();
		Arrays.fill(carKm, 0.);
	}

	@Override
	public void handleEvent(PersonLeavesVehicleEvent event) {
		this.cars.remove(event.getVehicleId());
		this.carDrivers.remove(event.getPersonId());
	}

	@Override
	public void handleEvent(PersonEntersVehicleEvent event) {
		if(carDrivers.contains(event.getPersonId())) this.cars.add(event.getVehicleId());
	}




}
