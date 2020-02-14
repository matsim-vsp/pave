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

package org.matsim.pfav.analysis;

import org.matsim.api.core.v01.events.*;
import org.matsim.api.core.v01.events.handler.*;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.pfav.privateAV.PFAVEventsReader;

import java.util.HashSet;
import java.util.Set;

public class BasicAnalysis implements ActivityStartEventHandler, ActivityEndEventHandler, PersonDepartureEventHandler, PersonArrivalEventHandler, PersonEntersVehicleEventHandler {


    static int numberOfStarts = 0;
    static int numberOfEnds = 0;
    static int  numberServices = 0;

    static int nrOfFreightDepartures = 0;
    static int nrOfFreightArrivals = 0;


    static int nrOfActStartWithFreight = 0;
    static int nrOfActEndeWithFreight = 0;

    static Set<String> allFreightPersonIds = new HashSet<>();
    static Set<String> allFreightVehIds = new HashSet<>();

    public static void main(String[] args){
        String dir = "C:/Users/Work/tubCloud/MasterArbeit/Runs/serious/bCs_gzBln_11k_Truck/";
        String input = dir + "berlin-v5.3-1pct.output_events.xml.gz";
        EventsManager manager = EventsUtils.createEventsManager();

        manager.addHandler(new BasicAnalysis());
        PFAVEventsReader reader = new PFAVEventsReader(manager);

        reader.readFile(input);

        System.out.println("nrStarts" +  numberOfStarts);
        System.out.println("nrEnds" +  numberOfEnds);
        System.out.println("nrEnds" +  numberServices);

        System.out.println("nrDeps" +  nrOfFreightDepartures);
        System.out.println("nrArrs" +  nrOfFreightArrivals);

        System.out.println("nrActStarts" +  nrOfActStartWithFreight);
        System.out.println("nrActEnd" +  nrOfActEndeWithFreight);

        System.out.println("size of person set " +  allFreightPersonIds.size());
        System.out.println("size of vehicle set" +  allFreightVehIds.size());

    }


    @Override
    public void handleEvent(ActivityEndEvent event) {
        String person = event.getPersonId().toString();
        if(person.contains("freight")){
            allFreightPersonIds.add(person);
            nrOfActEndeWithFreight ++;
            if(! event.getActType().equals("start")) throw new IllegalStateException("person id = " + person + " event = " + event.toString());
        }
        if(event.getActType().equals("start")){
            numberOfStarts++;
            if(!person.contains("freight")) throw new IllegalStateException("person id = " + person + " event = " + event.toString());
        }

    }

    @Override
    public void handleEvent(ActivityStartEvent event) {
        String person = event.getPersonId().toString();
        if(person.contains("freight")){
            allFreightPersonIds.add(person);
            nrOfActStartWithFreight ++;
            if(! event.getActType().equals("end") || event.getActType().equals("service")) throw new IllegalStateException("person id = " + person + " event = " + event.toString());
        }
        if(event.getActType().equals("end")){
            numberOfEnds++;
            if(!person.contains("freight")) throw new IllegalStateException("person id = " + person + " event = " + event.toString());
        }
    }

    @Override
    public void handleEvent(PersonArrivalEvent event) {
        String person = event.getPersonId().toString();
        if(person.contains("freight")){
            allFreightPersonIds.add(person);
            nrOfFreightArrivals ++;
        }

    }

    @Override
    public void handleEvent(PersonDepartureEvent event) {
        String person = event.getPersonId().toString();
        if(person.contains("freight")){
            allFreightPersonIds.add(person);
            nrOfFreightDepartures ++;
        }
    }


    @Override
    public void handleEvent(PersonEntersVehicleEvent event) {
        if(event.getPersonId().toString().contains("freight")) allFreightPersonIds.add(event.getPersonId().toString());
        if(event.getVehicleId().toString().contains("freight")) allFreightVehIds.add(event.getVehicleId().toString());
    }
}
