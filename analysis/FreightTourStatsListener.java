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
import org.matsim.api.core.v01.events.*;
import org.matsim.api.core.v01.events.handler.*;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.vehicles.Vehicle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreightTourStatsListener implements ActivityEndEventHandler, ActivityStartEventHandler, VehicleEntersTrafficEventHandler, VehicleLeavesTrafficEventHandler, LinkEnterEventHandler, IterationEndsListener {

    private Map<Id<Person>, Double> departureTimes = new HashMap<>();

    private Map<Id<Person>, Id<Vehicle>> driverToVehicle = new HashMap<>();
    private Map<Id<Vehicle>, Id<Person>> vehicleToDriver = new HashMap<>();

    private Map<Id<Vehicle>, Double> lastLegLength = new HashMap<>();

    private List<FreightTourData> allTours = new ArrayList<>();

    private Map<Id<Person>, FreightTourData> currentTours = new HashMap<>();

    private Network network;

    public FreightTourStatsListener(Network network) {
        this.network = network;
    }


    @Override
    public void handleEvent(ActivityEndEvent event) {
        if (event.getActType().equals("start")) {
            if (this.departureTimes.containsKey(event.getPersonId()))
                throw new IllegalStateException("two carrier agent departures in a row of agent " + event.getPersonId());
            this.departureTimes.put(event.getPersonId(), event.getTime());
            FreightTourData data = new FreightTourData(event.getLinkId().toString(), event.getTime());
            this.currentTours.put(event.getPersonId(), data);
        } else if (event.getActType().equals("service")) {
            this.lastLegLength.put(this.driverToVehicle.get(event.getPersonId()), 0.);
        }
    }


    @Override
    public void handleEvent(VehicleEntersTrafficEvent event) {
        if (this.departureTimes.containsKey(event.getPersonId())) {
            this.lastLegLength.put(event.getVehicleId(), 0.);
            this.driverToVehicle.put(event.getPersonId(), event.getVehicleId());
            this.vehicleToDriver.put(event.getVehicleId(), event.getPersonId());
        }
    }


    @Override
    public void handleEvent(LinkEnterEvent event) {
        FreightTourData data = this.currentTours.get(this.vehicleToDriver.get(event.getVehicleId()));
        if (data != null) {
            data.distance += network.getLinks().get(event.getLinkId()).getLength();
            Double length = this.lastLegLength.get(event.getVehicleId());
            this.lastLegLength.put(event.getVehicleId(), length + network.getLinks().get(event.getLinkId()).getLength());
        }
    }

    @Override
    public void handleEvent(VehicleLeavesTrafficEvent event) {
        if (this.currentTours.containsKey(event.getPersonId())) {
            this.currentTours.get(event.getPersonId()).emptyDistance = this.lastLegLength.remove(event.getVehicleId());
        }
    }

    @Override
    public void handleEvent(ActivityStartEvent event) {
        if (event.getActType().equals("end")) {
            event.getPersonId();
            if (this.departureTimes.containsKey(event.getPersonId())) {
                FreightTourData data = this.currentTours.remove(event.getPersonId());
                data.time = event.getTime() - departureTimes.remove(event.getPersonId());

                this.allTours.add(data);
            }
        }
    }

    /**
     * Notifies all observers of the Controler that a iteration is finished
     *
     * @param event
     */
    @Override
    public void notifyIterationEnds(IterationEndsEvent event) {
        String dir = event.getServices().getConfig().controler().getOutputDirectory() + "/ITERS/it." + event.getIteration() + "/";
        writeStats(dir + "FreightTourStats_it" + event.getIteration() + ".csv");
    }

    public void writeStats(String file) {
        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            int i = 1;
            writer.write("index;departureTime;travelledDistance;emptyDistance;tourDuration;depotLink");
            writer.newLine();

            for (FreightTourData data : this.allTours) {
                writer.write("" + i + ";" + data.departureTime + ";" + data.distance + ";" + data.emptyDistance + ";" + data.time + ";" + data.depot);
                writer.newLine();
                i++;
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gives the event handler the possibility to clean up its internal state.
     * Within a Controler-Simulation, this is called before the mobsim starts.
     *
     * @param iteration the up-coming iteration from which up-coming events will be from.
     */
    @Override
    public void reset(int iteration) {
        this.allTours = new ArrayList<>();
    }

    private class FreightTourData {

        private String depot;

        private double departureTime;
        private double distance = 0.;
        private double time = 0.;
        private double emptyDistance = 0.;

        private FreightTourData(String depot, double departureTime) {
            this.depot = depot;
            this.departureTime = departureTime;
        }

    }
}
