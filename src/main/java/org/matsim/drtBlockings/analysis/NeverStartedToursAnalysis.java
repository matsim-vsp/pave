package org.matsim.drtBlockings.analysis;

import com.google.common.base.Function;
import org.matsim.api.core.v01.Id;
import org.matsim.contrib.drt.passenger.events.DrtRequestSubmittedEvent;
import org.matsim.contrib.drt.passenger.events.DrtRequestSubmittedEventHandler;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.drtBlockings.DrtBlockingRequest;
import org.matsim.drtBlockings.events.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NeverStartedToursAnalysis implements DrtBlockingRequestSubmittedEventHandler, DrtBlockingRequestScheduledEventHandler {

//    private Map<Id<Request>, Double> requestToTime = new HashMap<>();
//    private Map<Id<DvrpVehicle>, DrtBlockingTourData> allTours = new HashMap<>();
    private List<DrtBlockingTourData> allTours = new ArrayList<>();
    private List<DrtBlockingTourData> scheduledTours = new ArrayList<>();


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
            for (DrtBlockingTourData data : this.allTours) {
                if(!this.scheduledTours.contains(data.veh)) {

                    writer.write(i + ";" + data.requestId + ";" + data.time + ";" + data.veh);
                    writer.newLine();
                }
//                if(!this.allTours.contains(data.veh)) {
//                    System.out.println("testoooooo");
//                }
                i++;
            }
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleEvent(DrtBlockingRequestSubmittedEvent event) {

//        Id<DvrpVehicle> vehId = event.getVehicleId(); TODO: fix this
        Id<DvrpVehicle> vehId = Id.create(event.getRequestId(), DvrpVehicle.class);

        DrtBlockingTourData data = new DrtBlockingTourData(vehId, event.getTime());
//        this.allTours.put(vehId, data);
        data.requestId = event.getRequestId();
        this.allTours.add(data);
//        this.neverScheduledTours.putIfAbsent(vehId, data);
//        System.out.println(event);
    }

    @Override
    public void handleEvent(DrtBlockingRequestScheduledEvent event) {

        DrtBlockingTourData data = new DrtBlockingTourData(event.getVehicleId(), 0.);

        data.requestId = Id.create(event.getRequestId(), Request.class);
        this.scheduledTours.add(data);
//       this.neverScheduledTours.remove(event.getVehicleId());
    }

    private class DrtBlockingTourData {
        private final Id<DvrpVehicle> veh;
        private double time;
        private Id<Request> requestId;

        private DrtBlockingTourData(Id<DvrpVehicle> veh, double time) {
            this.veh = veh;
            this.time = time;
        }

    }
}
