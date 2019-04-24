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
import org.matsim.api.core.v01.events.PersonEntersVehicleEvent;
import org.matsim.api.core.v01.events.handler.PersonEntersVehicleEventHandler;
import org.matsim.api.core.v01.population.Person;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.vehicles.Vehicle;
import privateAV.events.PFAVEventsReader;
import privateAV.events.PFAVOwnerWaitsForVehicleEvent;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PFAVWaitTimesAnalyzer implements PFAVWaitTimesListener, PersonEntersVehicleEventHandler, IterationEndsListener {

    private Map<Id<Person>, Double> waitingOwners = new HashMap<>();
    private Set<WaitTimeData> dataSet = new HashSet<>();


    public static void main(String[] args) {

        EventsManager manager = EventsUtils.createEventsManager();

        PFAVWaitTimesAnalyzer handler = new PFAVWaitTimesAnalyzer();
        manager.addHandler(handler);
        PFAVEventsReader reader = new PFAVEventsReader(manager);

//        String dir = "C:/Users/Work/tubCloud/MasterArbeit/Runs/serious/Dep3_11kPFAV_gzBln_Transporter/";
//        String input = dir + "berlin-v5.3-1pct.output_events.xml.gz";

        String dir = "C:/Users/Work/git/freightAV/output/travelTimeTest/2019-04-24_14.35/";
        String input = dir + "output_events.xml";

        String output = dir + "/ITERS/it.0/WaitTimeStats_it0.csv";
        reader.readFile(input);
        handler.writeStats(output);

    }

    @Override
    public void handleEvent(PFAVOwnerWaitsForVehicleEvent event) {
        System.out.println("holla holla");

        if (this.waitingOwners.containsKey(event.getOwner())) {
            throw new IllegalStateException("owner " + event.getOwner() + " triggers two wait events without being picked up in the meantime." +
                    " second waiting event time=" + event.getTime());
        }
        this.waitingOwners.put(event.getOwner(), event.getTime());
    }

    @Override
    public void handleEvent(PersonEntersVehicleEvent event) {
        if (this.waitingOwners.containsKey(event.getPersonId())) {
            double start = this.waitingOwners.remove(event.getPersonId());
            WaitTimeData data = new WaitTimeData(event.getPersonId(), event.getVehicleId(), start, event.getTime());
            this.dataSet.add(data);
        }
    }

    @Override
    public void notifyIterationEnds(IterationEndsEvent event) {
        String dir = event.getServices().getConfig().controler().getOutputDirectory() + "/ITERS/it." + event.getIteration() + "/";
        writeStats(dir + "WaitTimeStats_it" + event.getIteration() + ".csv");
    }

    /**
     *
     */
    @Override
    public void reset(int iteration) {
        this.dataSet = new HashSet<>();
    }


    public void writeStats(String file) {
        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            int i = 1;
            writer.write("index;person;vehicle;beginTime;endTime;waitingTime;");
            writer.newLine();

            for (WaitTimeData data : this.dataSet) {
                writer.write("" + i + ";" + data.personId + ";" + data.vehicleId + ";" + data.begin + ";" + data.end + ";" + data.getWaitingTime());
                writer.newLine();
                i++;
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class WaitTimeData {

        double begin;
        double end;
        Id<Person> personId;
        Id<Vehicle> vehicleId;

        WaitTimeData(Id<Person> personId, Id<Vehicle> vehicleId, double start, double end) {
            this.begin = start;
            this.end = end;
            this.personId = personId;
            this.vehicleId = vehicleId;
        }

        double getWaitingTime() {
            return this.end - this.begin;
        }
    }


}


