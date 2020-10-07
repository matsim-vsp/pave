package org.matsim.drtBlockings.analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
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
import org.matsim.drtBlockings.tasks.FreightRetoolTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicTourStatsAnalysis implements DrtBlockingRequestScheduledEventHandler, TaskStartedEventHandler,
TaskEndedEventHandler, DrtBlockingEndedEventHandler, LinkEnterEventHandler {

    private Network network;
    private Map<Id<DvrpVehicle>, DrtBlockingTourData> currentTours = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToDistance = new HashMap<>();
//    private Map<Id<DvrpVehicle>, Double> vehToAccess = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToDeparture = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToArrival = new HashMap<>();
//    private Map<Id<DvrpVehicle>, Double> vehToDuration = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToAccessDistance = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToAccessDuration = new HashMap<>();

    private List<DrtBlockingTourData> finishedTours = new ArrayList<>();

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
            writer.write("no;vehId;totalDistance;accessLegDistance;departureTime;arrivalTime;tourDuration;accessLegDuration");
            writer.newLine();

            for (DrtBlockingTourData data  : this.finishedTours) {
                writer.write(i + ";" + data.veh + ";" + data.tourDistance + ";" + data.accessDistance + ";"
                + data.departure + ";" + data.arrival + ";" + data.tourDuration + data.accessDuration);
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

        //could be that the vehicle will not be recognized because its VehicleId and not vId
        //maybe its necessary to add .toString after getVehicleId() but should work like this too
        //maybe like that
        Id<DvrpVehicle> dvrpVehicleId = Id.create(event.getVehicleId(), DvrpVehicle.class);
        if (this.currentTours.containsKey(dvrpVehicleId)) {
            //add up linkLength to distance travelled so far
            Double distanceSoFar = this.vehToDistance.get(dvrpVehicleId);
            if (distanceSoFar == 0) {
                this.vehToDistance.put(dvrpVehicleId, network.getLinks().get(event.getLinkId()).getLength());
                this.vehToDeparture.putIfAbsent(dvrpVehicleId, event.getTime());
            } else {
                this.vehToDistance.replace(dvrpVehicleId,
                        distanceSoFar + network.getLinks().get(event.getLinkId()).getLength());
            }
        }
    }

    @Override
    public void handleEvent(TaskEndedEvent event) {
        //not sure if this eventType is even needed
        //so far we just need DrtBlockingEndedEvent to register end of tour!
    }

    @Override
    public void handleEvent(TaskStartedEvent event) {
        //here we register the access leg
        //check if veh has a running tour
        Id<DvrpVehicle> dvrpVehicleId = event.getDvrpVehicleId();
        if (this.currentTours.containsKey(dvrpVehicleId)) {
            if (event.getTaskType().equals(FreightRetoolTask.RETOOL_TASK_TYPE)) {
                this.vehToAccessDistance.putIfAbsent(dvrpVehicleId, this.vehToDistance.get(dvrpVehicleId));
                Double accessDuration = event.getTime() - this.vehToDeparture.get(dvrpVehicleId);
                if (accessDuration >= 0) {
                    this.vehToAccessDuration.putIfAbsent(dvrpVehicleId, accessDuration);
                } else {
                    System.out.println("Access leg duration for vehicle " + dvrpVehicleId + " is " + accessDuration + " (< 0!");
                }
            }
        }
    }

    @Override
    public void handleEvent(DrtBlockingEndedEvent event) {
        if (this.currentTours.containsKey(event.getVehicleId())) {
            //add up linkLength to distance travelled so far
            Double distanceSoFar = this.vehToDistance.get(event.getVehicleId());
            DrtBlockingTourData data = this.currentTours.remove(event.getVehicleId());
            data.tourDistance = distanceSoFar + network.getLinks().get(event.getLinkId()).getLength();
            data.accessDistance = this.vehToAccessDistance.remove(event.getVehicleId());
//            this.vehToDistance.replace(event.getVehicleId(),
//                    distanceSoFar + network.getLinks().get(event.getLinkId()).getLength());

            //get eventTime and calculate tourDuration
            //The following should be the case for every tour!
            if (event.getTime() > this.vehToDeparture.get(event.getVehicleId())) {
                this.vehToArrival.put(event.getVehicleId(), event.getTime());
                Double tourDuration = event.getTime() - vehToDeparture.get(event.getVehicleId());
                data.tourDuration = tourDuration;
                data.accessDuration = this.vehToAccessDuration.remove(event.getVehicleId());
                data.departure = this.vehToDeparture.remove(event.getVehicleId());
                data.arrival = this.vehToArrival.remove(event.getVehicleId());

//                this.vehToDuration.put(event.getVehicleId(), tourDuration);
            } else {
                System.out.println("The tours of vehicle " + event.getVehicleId() + " are not correctly handled!");
            }
            //remove  veh from currentTours and out it onto finishedTours
//            this.finishedTours.add(this.currentTours.remove(event.getVehicleId()));
            this.finishedTours.add(data);
        }
    }

    @Override
    public void handleEvent(DrtBlockingRequestScheduledEvent event) {
        //put veh into map of current tours when Request is requested
//        currentTours.put(event.getVehicleId(), event.getVehicleId());
        DrtBlockingTourData data = new DrtBlockingTourData(event.getVehicleId(), 0.);
        this.currentTours.put(event.getVehicleId(), data);
    }

    private class DrtBlockingTourData {
        //TODO: switch from data in maps to data in here!
        private final Id<DvrpVehicle> veh;
        private double departure;
        private double arrival;
        private double tourDuration;
        private double accessDuration;
        private double tourDistance = 0.;
        private double accessDistance = 0.;

        private DrtBlockingTourData(Id<DvrpVehicle> veh, double departure) {
            this.veh = veh;
            this.departure = departure;
        }
    }
}
