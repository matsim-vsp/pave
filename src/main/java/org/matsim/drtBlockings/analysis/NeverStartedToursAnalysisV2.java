package org.matsim.drtBlockings.analysis;

import com.google.common.base.Function;
import org.matsim.api.core.v01.Id;
import org.matsim.contrib.drt.passenger.events.DrtRequestSubmittedEvent;
import org.matsim.contrib.drt.passenger.events.DrtRequestSubmittedEventHandler;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
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

public class NeverStartedToursAnalysisV2 implements DrtBlockingRequestRejectedEventHandler, IterationEndsListener {

    private List<DrtBlockingTourData> rejectedTours = new ArrayList<>();


    public static void main(String[] args) {
//        String dir = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/chessboard/Analysis_test/";
        String dir = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/berlin-v5.5-10pct/drtBlockingTest_30Blockings_ServiceWidthIncr/";
//        String eventsFile = dir + "output_events.xml.gz";
        String eventsFile = dir + "blckBase1.output_events.xml.gz";
//        String eventsFile = dir + "output_events_3.xml";
        String outputFile = dir + "neverStartedTourStatsV2.csv";

        EventsManager manager = EventsUtils.createEventsManager();
        NeverStartedToursAnalysisV2 handler = new NeverStartedToursAnalysisV2();
        manager.addHandler(handler);
        manager.initProcessing();
        MatsimEventsReader reader = DrtBlockingEventsReader.create(manager);
        reader.readFile(eventsFile);
        manager.finishProcessing();
        handler.writeStats(outputFile);
        System.out.println("Writing of DrtBlocking TourStats to " + outputFile + " was successful!");
    }

    private void writeStats(String file) {
        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING!");
            int i = 0;
            writer.write("No;requestId;rejectionTime");
            writer.newLine();

//            for (i=0; i <= this.neverScheduledTours.size(); i++) {
            for (DrtBlockingTourData data : this.rejectedTours) {

                writer.write(i + ";" + data.requestId + ";" + data.time);
                writer.newLine();
                i++;
            }

            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void handleEvent(DrtBlockingRequestRejectedEvent event) {

        DrtBlockingTourData data = new DrtBlockingTourData(event.getRequestId(), event.getTime());
        rejectedTours.add(data);

    }

    @Override
    public void notifyIterationEnds(IterationEndsEvent event) {
        writeStats(event.getServices().getControlerIO().getIterationFilename(event.getIteration(), "neverStartedTourStatsV2.csv"));
    }

    @Override
    public void reset(int iteration) {
        this.rejectedTours.clear();
    }

    private class DrtBlockingTourData {
        private double time;
        private Id<Request> requestId;

        private DrtBlockingTourData(Id<Request> requestId, double time) {
            this.requestId = requestId;
            this.time = time;
        }

    }
}
