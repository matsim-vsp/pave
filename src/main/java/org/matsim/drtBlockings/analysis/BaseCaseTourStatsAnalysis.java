package org.matsim.drtBlockings.analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.freight.carrier.Tour;
import org.matsim.contrib.freight.events.LSPFreightLinkEnterEvent;
import org.matsim.contrib.freight.events.LSPServiceEndEvent;
import org.matsim.contrib.freight.events.LSPTourEndEvent;
import org.matsim.contrib.freight.events.LSPTourStartEvent;
import org.matsim.contrib.freight.events.eventhandler.LSPLinkEnterEventHandler;
import org.matsim.contrib.freight.events.eventhandler.LSPServiceEndEventHandler;
import org.matsim.contrib.freight.events.eventhandler.LSPTourEndEventHandler;
import org.matsim.contrib.freight.events.eventhandler.LSPTourStartEventHandler;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.drtBlockings.events.FreightEventsReader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseCaseTourStatsAnalysis implements LSPLinkEnterEventHandler, LSPTourStartEventHandler, LSPServiceEndEventHandler,
        LSPTourEndEventHandler, IterationEndsListener {

    private Network network;
    private Map<Id<Person>, FreightTourData> currentTours = new HashMap<>();
    private Map<Id<Person>, Double> driverToDistance = new HashMap<>();
    private Map<Id<Person>, Double> driverToDeparture = new HashMap<>();
    private Map<Id<Person>, Double> driverToArrival = new HashMap<>();
    private Map<Id<Person>, Double> driverToAccessDistance = new HashMap<>();
    private Map<Id<Person>, Double> driverToAccessDuration = new HashMap<>();
    private Map<Id<Person>, Id<Tour>> driverToTour = new HashMap<>();
    private Map<Id<Person>, Integer> driverToTaskNo = new HashMap<>();
    private Map<Id<Person>, Integer> driverToServiceNo = new HashMap<>();

    private List<FreightTourData> finishedTours = new ArrayList<>();

    public BaseCaseTourStatsAnalysis(Network network) { this.network = network; }

    public static void main(String[] args) {
        String dir = "";
        String eventsFile = dir + "";
        String inputNetwork = dir + "";
        String outputFile = dir + "BaseCaseTourStats.csv";

        EventsManager manager = EventsUtils.createEventsManager();
        Network network = NetworkUtils.createNetwork();
        new MatsimNetworkReader(network).readFile(inputNetwork);

        BaseCaseTourStatsAnalysis handler = new BaseCaseTourStatsAnalysis(network);
        manager.addHandler(handler);
        manager.initProcessing();
        //TODO EventsReader finden oder bauen
        // vllt geht es auch mit dem standard reader
//        MatsimEventsReader reader = new MatsimEventsReader(manager);
        FreightEventsReader reader = new FreightEventsReader(manager);
        reader.readFile(eventsFile);
        manager.finishProcessing();
        handler.writeStats(outputFile);
        System.out.println("Writing of DrtBlocking TourStats to " + outputFile + " was successful!");
    }

    public void writeStats(String file) {
        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING!");
            int i =1;
            writer.write("no;driverId;totalDistance [m];departureTime [s];arrivalTime [s];tourDuration [s];numberOfServices");
            writer.newLine();

            for (FreightTourData data : this.finishedTours) {
                writer.write(i + ";" + data.driver + ";" + data.tourDistance + ";" + data.departure + ";" + data.arrival
                        + ";" + data.tourDuration + ";" + data.serviceNo);
                writer.newLine();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleEvent(LSPFreightLinkEnterEvent event) {
        //check if driver has a running tour

        //could be that the driver will not be recognized because its driverId and not vId
        //maybe its necessary to add .toString after getdriverId() but should work like this too
        //maybe like that
        Id<Person> driverId = event.getDriverId();
        if (this.currentTours.containsKey(driverId)) {
            //add up linkLength to distance travelled so far
            Double distanceSoFar = this.driverToDistance.computeIfAbsent(driverId, v -> 0.);
            this.driverToDeparture.putIfAbsent(driverId, event.getTime());
            this.driverToDistance.replace(driverId,
                    distanceSoFar + network.getLinks().get(event.getLinkId()).getLength());
        }
    }

    @Override
    public void handleEvent(LSPServiceEndEvent event) {
        //noOfServices
        Id<Person> driverId = event.getDriverId();
        if(!this.driverToServiceNo.containsKey(driverId)) {
            this.driverToServiceNo.put(driverId, 1);
        } else {
            this.driverToServiceNo.replace(driverId, this.driverToServiceNo.get(driverId),
                    this.driverToServiceNo.get(driverId) + 1);
        }            
    }

    @Override
    public void handleEvent(LSPTourEndEvent event) {
        if (this.currentTours.containsKey(event.getDriverId())) {
            //add up linkLength to distance travelled so far
            Double distanceSoFar = this.driverToDistance.remove(event.getDriverId());
            FreightTourData data = this.currentTours.remove(event.getDriverId());

            //get eventTime and calculate tourDuration
            //The following should be the case for every tour!
            if (event.getTime() > this.driverToDeparture.get(event.getDriverId())) {
                this.driverToArrival.put(event.getDriverId(), event.getTime());
                Double tourDuration = event.getTime() - driverToDeparture.get(event.getDriverId());
                data.tourDuration = tourDuration;
                data.tourDistance = distanceSoFar + network.getLinks().get(event.getVehicle().getLocation()).getLength();
                data.departure = this.driverToDeparture.remove(event.getDriverId());
                data.arrival = this.driverToArrival.remove(event.getDriverId());
                data.serviceNo = this.driverToServiceNo.remove(event.getDriverId());

            } else {
                System.out.println("The tours of driver " + event.getDriverId() + " are not correctly handled!");
            }
            //remove  driver from currentTours and out it onto finishedTours
            this.finishedTours.add(data);
        }
    }

    @Override
    public void handleEvent(LSPTourStartEvent event) {
        //wie TaskStarted für Access Leg, was eigentlich immer 0 ist, da Fz im Depot ist
        //wie RequestScheduled um TourStart zu registrieren

        //put driver into map of current tours when tour starts
        FreightTourData data = new FreightTourData(event.getDriverId(), 0.);
        this.currentTours.put(event.getDriverId(), data);
    }

    @Override
    public void notifyIterationEnds(IterationEndsEvent event) {
        String dir = event.getServices().getConfig().controler().getOutputDirectory();
        String outputFile = dir + "/BaseCaseTourStats.csv";
        writeStats(outputFile);
    }

    @Override
    public void reset(int iteration) {

    }

    private class FreightTourData {
        private final Id<Person> driver;
        private double departure;
        private double arrival;
        private double tourDuration;
        private double tourDistance = 0.;
        private int serviceNo;

        private FreightTourData(Id<Person> driver, double departure) {
            this.driver = driver;
            this.departure = departure;
        }
    }
}
