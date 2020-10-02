package org.matsim.drtBlockings.analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.dvrp.util.DvrpEventsReaders;
import org.matsim.contrib.dvrp.vrpagent.TaskEndedEvent;
import org.matsim.contrib.dvrp.vrpagent.TaskEndedEventHandler;
import org.matsim.contrib.dvrp.vrpagent.TaskStartedEvent;
import org.matsim.contrib.dvrp.vrpagent.TaskStartedEventHandler;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlReader;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.drtBlockings.events.*;
import org.matsim.pfav.analysis.BaseCaseFreightTourStatsListener;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class BasicTourStatsAnalysis implements DrtBlockingRequestScheduledEventHandler, TaskStartedEventHandler,
TaskEndedEventHandler, DrtBlockingEndedEventHandler, LinkEnterEventHandler {

    private Network network;
    private Map<Id<DvrpVehicle>, Id<DvrpVehicle>> currentTours = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToDistance = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToAccess = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToDeparture = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToArrival = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToDuration = new HashMap<>();

    private List<Id<DvrpVehicle>> finishedTours = new ArrayList<>();

    public BasicTourStatsAnalysis(Network network) {
        this.network = network;
    }

    public static void main(String[] args) {
        String dir = "";
        String eventsFile = dir + "";
        String carriersFile = dir + "";
        String inputNetwork = dir + "";
        String outputFile = dir + "";
        final Carriers carriers = new Carriers();
        new CarrierPlanXmlReader(carriers).readFile(carriersFile);

        EventsManager manager = EventsUtils.createEventsManager();
        Network network = NetworkUtils.createNetwork();
        new MatsimNetworkReader(network).readFile(inputNetwork);

        BasicTourStatsAnalysis handler = new BasicTourStatsAnalysis(network);
        manager.addHandler(handler);
        MatsimEventsReader reader = DrtBlockingEventsReaders.createEventsReader(manager);
        reader.readFile(eventsFile);
        handler.writeStats(outputFile);



    }

    private void writeStats(String file) {
        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            int i =1;
            writer.write("no;vehId;totalDistance;accessLegDistance;departureTime;arrivalTime;tourDuration");
            writer.newLine();

            for (Id<DvrpVehicle> vehId : this.finishedTours) {
                writer.write(i + ";" + vehId + ";" + vehToDistance.get(vehId) + ";" + vehToAccess.get(vehId) + ";"
                + vehToDeparture.get(vehId) + ";" + vehToArrival.get(vehId) + ";" + vehToDuration.get(vehId));
                writer.newLine();
                i++;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void handleEvent(LinkEnterEvent event) {
        //check if veh has a running tour

        //could be that the vehicle will not be recognized because its VehicleId and not DvrpVehicleId
        //maybe like that
        if (this.currentTours.containsKey(Id.create(event.getVehicleId(), DvrpVehicle.class))) {
            //add up linkLength to distane travelled so far
            Double distanceSoFar = this.vehToDistance.get(Id.create(event.getVehicleId(), DvrpVehicle.class));
            this.vehToDistance.replace(Id.create(event.getVehicleId(), DvrpVehicle.class),
                    distanceSoFar + network.getLinks().get(event.getLinkId()).getLength());
        }

    }

    @Override
    public void handleEvent(TaskEndedEvent event) {
        //not sure if this eventType is even needed
        //so far we just need DrtBlockingEndedEvent to register end of tour!
    }

    @Override
    public void handleEvent(TaskStartedEvent event) {
        //check if veh has a running tour
        if (this.currentTours.containsKey(event.getDvrpVehicleId())) {
            //register distance travelled so far
            this.vehToDistance.put(event.getDvrpVehicleId(), network.getLinks().get(event.getLinkId()).getLength());
            //register time = tourDepartureTime
            this.vehToDeparture.put((event.getDvrpVehicleId()), event.getTime());
        }
    }

    @Override
    public void handleEvent(DrtBlockingEndedEvent event) {
        //TODO: add up distance, get travelDuration, remove from currentTours, put onto finishedTours, get arrival time
    }

    @Override
    public void handleEvent(DrtBlockingRequestScheduledEvent event) {
        //put veh into map of current tours when Request is requested
        currentTours.put(event.getVehicleId(), event.getVehicleId());
        //TODO: how to get access leg length? With next link enter event after drt blocking request??
    }
}
