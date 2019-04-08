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

package analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.PersonArrivalEvent;
import org.matsim.api.core.v01.events.PersonDepartureEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.events.handler.PersonArrivalEventHandler;
import org.matsim.api.core.v01.events.handler.PersonDepartureEventHandler;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.utils.io.IOUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OverallTravelTimeAndDistanceListener implements PersonDepartureEventHandler, PersonArrivalEventHandler, LinkEnterEventHandler, IterationEndsListener {

    double overallTravelTime = 0.;
    double overallTravelDistance = 0.;
    private Map<Id<Person>, Double> departureTimes = new HashMap<>();
    private Network network;

    public OverallTravelTimeAndDistanceListener(Network network) {
        this.network = network;
    }

    @Override
    public void handleEvent(PersonDepartureEvent event) {
        this.departureTimes.put(event.getPersonId(), event.getTime());
    }

    @Override
    public void handleEvent(PersonArrivalEvent event) {
        if (!this.departureTimes.containsKey(event.getPersonId()))
            throw new IllegalStateException("arrival without departure of agent " + event.getPersonId());
        this.overallTravelTime += event.getTime() - this.departureTimes.remove(event.getPersonId());
    }

    /**
     * Gives the event handler the possibility to clean up its internal state.
     * Within a Controler-Simulation, this is called before the mobsim starts.
     *
     * @param iteration the up-coming iteration from which up-coming events will be from.
     */
    @Override
    public void reset(int iteration) {
        this.overallTravelDistance = 0.;
        this.overallTravelTime = 0.;
    }

    @Override
    public void handleEvent(LinkEnterEvent event) {
        this.overallTravelDistance += network.getLinks().get(event.getLinkId()).getLength();
    }


    /**
     * Notifies all observers of the Controler that a iteration is finished
     *
     * @param event
     */
    @Override
    public void notifyIterationEnds(IterationEndsEvent event) {
        String dir = event.getServices().getConfig().controler().getOutputDirectory() + "/ITERS/it." + event.getIteration() + "/";
        BufferedWriter writer = IOUtils.getBufferedWriter(dir + "generaltStatsIt" + event.getIteration() + ".txt");
        try {
            writer.write("overallTravelTime;overallTravelDistance");
            writer.newLine();
            writer.write("" + overallTravelTime + ";" + overallTravelDistance);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
