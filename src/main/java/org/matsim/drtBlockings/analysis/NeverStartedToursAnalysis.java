package org.matsim.drtBlockings.analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.contrib.drt.passenger.events.DrtRequestSubmittedEvent;
import org.matsim.contrib.drt.passenger.events.DrtRequestSubmittedEventHandler;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.drtBlockings.events.DrtBlockingEventsReader;
import org.matsim.drtBlockings.events.DrtBlockingRequestScheduledEvent;
import org.matsim.drtBlockings.events.DrtBlockingRequestScheduledEventHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NeverStartedToursAnalysis implements DrtRequestSubmittedEventHandler, DrtBlockingRequestScheduledEventHandler {

    private Map<Id<Request>, Double> requestToTime = new HashMap<>();
    private Map<Id<Request>, Id<DvrpVehicle>> requestToVehicleId = new HashMap<>();
    private List<Id<Request>> neverScheduledTours = new ArrayList<>();

    public static void main(String[] args) {
        String dir = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/chessboard/drtBlocking/Analysis_test/";
        String eventsFile = dir + "output_events.xml.gz";
        String outputFile = dir + "neverStartedTourStats.csv";

        EventsManager manager = EventsUtils.createEventsManager();
        NeverStartedToursAnalysis handler = new NeverStartedToursAnalysis();
        manager.addHandler(handler);
        manager.initProcessing();
        MatsimEventsReader reader = DrtBlockingEventsReader.create(manager);
        reader.readFile(eventsFile);
        manager.finishProcessing();
        handler.writeStats(outputFile);
    }

    private void writeStats(String file) {
        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            int i = 1;
            writer.write("No;requestId;requestTime;vehicleId");
            writer.newLine();

//            for (i=0; i <= this.neverScheduledTours.size(); i++) {
            for (Id<Request> requestId : this.neverScheduledTours) {
//                Id<Request> requestId = this.neverScheduledTours.get(i);
                Double requestTime = this.requestToTime.get(requestId);
                Id<DvrpVehicle> vehicleId = this.requestToVehicleId.get(requestId);
                writer.write(i + ";" + requestId + ";" + requestTime + ";" + vehicleId);
                writer.newLine();
                i++;
            }
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleEvent(DrtRequestSubmittedEvent event) {
        this.neverScheduledTours.add(event.getRequestId());
        this.requestToTime.putIfAbsent(event.getRequestId(), event.getTime());
        System.out.println("test");
    }

    @Override
    public void handleEvent(DrtBlockingRequestScheduledEvent event) {
//        this.neverScheduledTours.remove(event.getRequestId());
        this.requestToVehicleId.putIfAbsent(event.getRequestId(), event.getVehicleId());
        System.out.println(this.neverScheduledTours.remove(event.getRequestId()));
    }
}
