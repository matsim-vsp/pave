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

package org.matsim.drtBlockings.analysis;

import org.matsim.contrib.dvrp.passenger.PassengerRequestRejectedEvent;
import org.matsim.contrib.dvrp.passenger.PassengerRequestRejectedEventHandler;
import org.matsim.contrib.util.CSVLineBuilder;
import org.matsim.contrib.util.CompactCSVWriter;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.utils.io.IOUtils;

import java.util.Comparator;
import java.util.TreeMap;

public class PassengerRequestRejectionListener implements PassengerRequestRejectedEventHandler, IterationEndsListener {

    private TreeMap<PassengerRequestRejectedEvent, CSVLineBuilder> event2Output = new TreeMap<>(Comparator.comparingDouble(PassengerRequestRejectedEvent::getTime));

    @Override
    public void handleEvent(PassengerRequestRejectedEvent event) {
        String string = "%s";
        String dbl = "%.1f";

        String attributes = "attributes: ";
        for(String attribute : event.getAttributes().keySet()){
            attributes += attribute + "=" + event.getAttributes() + " # ";
        }

        CSVLineBuilder eventOutput = new CSVLineBuilder()
                .addf(dbl, event.getTime())
                .addf(string, event.getPersonId())
                .addf(string, event.getRequestId())
                .addf(string, event.getMode())
                .addf(string, event.getCause())
                .addf(string, attributes);

        this.event2Output.put(event, eventOutput);
    }


    @Override
    public void reset(int iteration) {
        this.event2Output.clear();
    }

    @Override
    public void notifyIterationEnds(IterationEndsEvent event) {
        String fileName = event.getServices().getControlerIO().getIterationFilename(event.getIteration(), "passengerRejections.csv");
        writeOutput(fileName);
    }

    public void writeOutput(String fileName){
        try (CompactCSVWriter writer = new CompactCSVWriter(IOUtils.getBufferedWriter(fileName), ';')) {
            writeHeader(writer);
            while(! this.event2Output.isEmpty()){
                writer.writeNext(this.event2Output.pollFirstEntry().getValue());
            }
        }
    }

    private void writeHeader(CompactCSVWriter writer) {
        CSVLineBuilder lineBuilder = new CSVLineBuilder()
                .add("Time")
                .add("Person")
                .add("Request")
                .add("Mode")
                .add("RejectionCause")
                .add("EventAttributes");
        writer.writeNext(lineBuilder);
    }
}
