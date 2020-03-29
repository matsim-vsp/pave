/*
 * *********************************************************************** *
 * project: org.matsim.*
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2019 by the members listed in the COPYING,        *
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
 * *********************************************************************** *
 */

package org.matsim.ovgu.berlin.createNetworkChangeEvents;

import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.NetworkChangeEvent;
import org.matsim.core.network.NetworkChangeEvent.ChangeType;
import org.matsim.core.network.NetworkChangeEvent.ChangeValue;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.network.io.NetworkChangeEventsWriter;
import org.matsim.core.trafficmonitoring.TravelTimeCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is based on matsim-code-examples: RunCreateNetworkChangeEventsFromExistingSimulationExample
 */
public class CreateNetworkChangeEventsFile {

    private static final int ENDTIME = 36 * 3600;
    private static final int TIMESTEP = 15 * 60;
    private static final String NETWORKFILE = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.4-10pct/input/berlin-v5-network.xml.gz";
    private static final String SIMULATION_EVENTS_FILE = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.4-10pct/output-berlin-v5.4-10pct/berlin-v5.4-10pct.output_events.xml.gz";
    private static final String CHANGE_EVENTS_FILE = "../networkChangeEvents.xml.gz"; //TODO: Define Output file for ChangeEvents.-
    private static final double MINIMUMFREESPEED = 1.39; //5 km/h

    public static void run() {
        Network network = NetworkUtils.createNetwork() ;
        new MatsimNetworkReader(network).readFile(NETWORKFILE);
        TravelTimeCalculator tcc = readEventsIntoTravelTimeCalculator( network );
        List<NetworkChangeEvent> networkChangeEvents = createNetworkChangeEvents( network, tcc );
        new NetworkChangeEventsWriter().write(CHANGE_EVENTS_FILE, networkChangeEvents);
    }

    private static List<NetworkChangeEvent> createNetworkChangeEvents(Network network, TravelTimeCalculator tcc) {
        List<NetworkChangeEvent> networkChangeEvents = new ArrayList<>() ;

        for (Link l : network.getLinks().values()) {
            
            double length = l.getLength();
            double previousTravelTime = l.getLength() / l.getFreespeed();

            for (double time = 0; time < ENDTIME; time = time + TIMESTEP) {

                double newTravelTime = tcc.getLinkTravelTimes().getLinkTravelTime(l, time, null, null);
                if (newTravelTime != previousTravelTime) {

                    NetworkChangeEvent nce = new NetworkChangeEvent(time);
                    nce.addLink(l);
                    double newFreespeed = length / newTravelTime;
                    if (newFreespeed < MINIMUMFREESPEED) newFreespeed = MINIMUMFREESPEED;
                    ChangeValue freespeedChange = new ChangeValue(ChangeType.ABSOLUTE_IN_SI_UNITS, newFreespeed);
                    nce.setFreespeedChange(freespeedChange);

                    networkChangeEvents.add(nce);
                    previousTravelTime = newTravelTime;
                }
            }
        }
        return networkChangeEvents ;
    }

    private static TravelTimeCalculator readEventsIntoTravelTimeCalculator(Network network) {
        EventsManager manager = EventsUtils.createEventsManager();
        TravelTimeCalculator.Builder builder = new TravelTimeCalculator.Builder( network );
        TravelTimeCalculator tcc = builder.build();
        manager.addHandler(tcc);
        new MatsimEventsReader(manager).readFile(SIMULATION_EVENTS_FILE);
        return tcc ;
    }

}
