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
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.utils.io.IOUtils;
import privateAV.PFAVUtils;
import privateAV.events.PFAVEventsReader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OverallTravelTimeAndDistanceListener implements PersonDepartureEventHandler, PersonArrivalEventHandler, LinkEnterEventHandler, IterationEndsListener {

    private double taxiTravelTime = 0.;
    private double taxiTravelDistance = 0.;

    private double emptyPFAVTravelTime = 0.;
    private double emptyPFAVTravelDistance = 0.;

    private double freightTravelTime = 0.;
    private double freightTravelDistance = 0.;

    private double otherTravelTime = 0.;
    private double otherTravelDistance = 0.;

    private double sumPickupTime = 0;
    private double sumDropOffTime = 0;
    private List<String> vehiclesWithPassengers = new ArrayList<>();
    private List<String> vehicleThatPassengerWaitFor;
    private Map<Id<Person>, Double> taxiDepartureTimes = new HashMap<>();

    private HashMap<Id<Person>, Double> emptyPFAVDepartureTimes = new HashMap<>();
    private HashMap<Id<Person>, Double> freightDepartureTimes = new HashMap<>();

    private Network network;
    private HashMap<Id<Person>, Double> otherDepartureTimes = new HashMap<>();


    public OverallTravelTimeAndDistanceListener(Network network) {
        this.network = network;
    }

    public static void main(String[] args) {

        String dir = "C:/Users/Work/tubCloud/MasterArbeit/Runs/serious/bCs_gzBln_11k_Truck/";
        String input = dir + "berlin-v5.3-1pct.output_events.xml.gz";
        EventsManager manager = EventsUtils.createEventsManager();

        Network network = NetworkUtils.createNetwork();
        new MatsimNetworkReader(network).readFile(dir + "berlin-v5.3-1pct.output_network.xml.gz");

        OverallTravelTimeAndDistanceListener handler = new OverallTravelTimeAndDistanceListener(network);
        manager.addHandler(handler);
        PFAVEventsReader reader = new PFAVEventsReader(manager);

        String output = dir + "/ITERS/it.0/OverallStats_it0_NEU.csv";

        reader.readFile(input);
        handler.writeStats(output);
    }


    @Override
    public void handleEvent(PersonDepartureEvent event) {
        if (event.getLegMode().equals("taxi")) {
            this.taxiDepartureTimes.put(event.getPersonId(), event.getTime());
            String pfavID = PFAVUtils.generatePFAVIdFromPersonId(event.getPersonId()).toString();
            if (this.emptyPFAVDepartureTimes.containsKey(pfavID)) {
                this.vehicleThatPassengerWaitFor.add(pfavID);
            } else {
                this.vehiclesWithPassengers.add(pfavID);
            }
            this.sumPickupTime += 60;
        } else if (event.getLegMode().equals("car")) {
            String person = event.getPersonId().toString();
            if (!event.getPersonId().toString().contains("tr")) {
                if ((person.contains(PFAVUtils.PFAV_ID_SUFFIX))) {
                    if (!this.vehiclesWithPassengers.contains(person))
                        this.emptyPFAVDepartureTimes.put(event.getPersonId(), event.getTime());
                } else if (person.contains("freight")) {
                    this.freightDepartureTimes.put(event.getPersonId(), event.getTime());
                } else {
                    this.otherDepartureTimes.put(event.getPersonId(), event.getTime());
                }
            }
        }

    }

    @Override
    public void handleEvent(PersonArrivalEvent event) {
        if (this.taxiDepartureTimes.containsKey(event.getPersonId())) {
            this.taxiTravelTime += event.getTime() - this.taxiDepartureTimes.remove(event.getPersonId());
            this.sumDropOffTime += 60;
            if (!this.vehiclesWithPassengers.remove(PFAVUtils.generatePFAVIdFromPersonId(event.getPersonId()).toString())) {
                throw new IllegalStateException();
            }

        } else if (this.emptyPFAVDepartureTimes.containsKey(event.getPersonId())) {
            this.emptyPFAVTravelTime += event.getTime() - this.emptyPFAVDepartureTimes.remove(event.getPersonId());
        } else if (this.freightDepartureTimes.containsKey(event.getPersonId())) {
            this.freightTravelTime += event.getTime() - this.freightDepartureTimes.remove(event.getPersonId());
        } else if (this.otherDepartureTimes.containsKey(event.getPersonId())) {
            this.otherTravelTime += event.getTime() - this.otherDepartureTimes.remove(event.getPersonId());
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
        this.taxiTravelTime = 0.;
        this.taxiTravelDistance = 0.;

        this.emptyPFAVTravelTime = 0.;
        this.emptyPFAVTravelDistance = 0.;

        this.freightTravelTime = 0.;
        this.freightTravelDistance = 0.;

        this.otherTravelTime = 0.;
        this.otherTravelDistance = 0.;

        this.sumPickupTime = 0;
        this.sumDropOffTime = 0;
    }

    @Override
    public void handleEvent(LinkEnterEvent event) {
        String vehID = event.getVehicleId().toString();
        double distance = network.getLinks().get(event.getLinkId()).getLength();
        if (!vehID.contains("tr")) {
            if (vehID.contains(PFAVUtils.PFAV_ID_SUFFIX)) {
                if (this.vehiclesWithPassengers.contains(vehID)) {
                    taxiTravelDistance += distance;
                } else {
                    emptyPFAVTravelDistance += distance;
                }
            } else if (vehID.contains("freight")) {
                freightTravelDistance += distance;
            } else {
                otherTravelDistance += distance;

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
        writeStats(dir + "generaltStatsIt" + event.getIteration() + ".txt");
    }


    public void writeStats(String file) {
        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            writer.write("taxiTravelTime[h];taxiTravelDistance[km];" +
                    "PFAVwoPassTravelTime[h];PFAVwoPassTravelDistance[km];" +
                    "freightTravelTime[h];freightTravelDistance[km];" +
                    "otherTravelTime[h];otherTravelDistance[km];" +
                    "sumPickup[h];sumDropoff[h]");
            writer.newLine();
            writer.write("" + taxiTravelTime / 3600 + ";" + taxiTravelDistance / 1000 + ";" +
                    emptyPFAVTravelTime / 3600 + ";" + emptyPFAVTravelDistance / 1000 + ";" +
                    freightTravelTime / 3600 + ";" + freightTravelDistance / 1000 + ";" +
                    otherTravelTime / 3600 + ";" + otherTravelDistance / 1000 + ";" +
                    +sumPickupTime / 3600 + ";" + sumDropOffTime / 3600);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
