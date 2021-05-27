package org.matsim.drtBlockings.analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.events.ActivityEndEvent;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.handler.ActivityEndEventHandler;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Person;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.vehicles.Vehicle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseCaseTourStatsAnalysis implements LinkEnterEventHandler, ActivityEndEventHandler, ActivityStartEventHandler, IterationEndsListener {

    private Network network;
    private Carriers carriers;
    private Map<Id<Person>, FreightTourData> currentTours = new HashMap<>();
    private Map<Id<Person>, Double> driverToDistance = new HashMap<>();
    private Map<Id<Person>, Double> driverToDeparture = new HashMap<>();
    private Map<Id<Person>, Double> driverToArrival = new HashMap<>();
    private Map<Id<Person>, Integer> driverToServiceNo = new HashMap<>();
    private Map<Id<Person>, Id<Vehicle>> driverToTourId = new HashMap<>();

    private List<FreightTourData> finishedTours = new ArrayList<>();

    public BaseCaseTourStatsAnalysis(Network network, Carriers carriers) {
        this.network = network;
        this.carriers = carriers;
    }

    public static void main(String[] args) {
        String dir = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/berlin-v5.5-10pct/base_cases/carriers_4hTimeWindows_openBerlinNet_8-24_PLANNED.xml/";
        String eventsFile = dir + "p2-23DRTBlockingBaseCase.output_events.xml.gz";
        String inputNetwork = dir + "p2-23DRTBlockingBaseCase.output_network.xml.gz";
        String inputCarriers = dir + "output_carriers.xml";
        String carrierVehicleTypes = dir + "output_vehicleTypes.xml";
        String outputFile = dir + "BaseCaseTourStats.csv";

        EventsManager manager = EventsUtils.createEventsManager();
        Network network = NetworkUtils.createNetwork();
        new MatsimNetworkReader(network).readFile(inputNetwork);

        Config config = ConfigUtils.createConfig();

        FreightConfigGroup freightCfg = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
        freightCfg.setCarriersFile(inputCarriers);
        freightCfg.setCarriersVehicleTypesFile(carrierVehicleTypes);

        Scenario scenario = ScenarioUtils.createScenario(config);
        FreightUtils.loadCarriersAccordingToFreightConfig(scenario);
        Carriers carriers = FreightUtils.getCarriers(scenario);

        BaseCaseTourStatsAnalysis handler = new BaseCaseTourStatsAnalysis(network, carriers);
        manager.addHandler(handler);
        manager.initProcessing();
        MatsimEventsReader reader = new MatsimEventsReader(manager);
        reader.readFile(eventsFile);
        manager.finishProcessing();
        handler.getCorrespondingTourFromCarriersFile(carriers);
        handler.writeStats(outputFile);
        System.out.println("Writing of TourStats without using DrtBlocking to " + outputFile + " was successful!");
    }

    void getCorrespondingTourFromCarriersFile(Carriers carriers) {

        for(Carrier carrier : carriers.getCarriers().values()) {
            Id<Carrier> carrierId = carrier.getId();
            int tourCount = 0;

            for( ScheduledTour tour : carrier.getSelectedPlan().getScheduledTours()) {
                Id<Vehicle> tourVehicleId = tour.getVehicle().getId();
                Id<Person> driverId = Id.createPersonId("freight_" + carrierId + "_veh_" + tourVehicleId + "_" + tourCount);

                Id<CarrierService> firstServiceId = null;

                for(Tour.TourElement e : tour.getTour().getTourElements()) {
                    if(e instanceof Tour.ServiceActivity) {
                        firstServiceId = ((Tour.ServiceActivity) e).getService().getId();
                        break;
                    }
                }

                Id<Vehicle> tourId = Id.createVehicleId(carrierId + "" + tourVehicleId + "_" + firstServiceId);

                if(!this.driverToTourId.containsKey(driverId)) {
                    this.driverToTourId.putIfAbsent(driverId, tourId);
                } else {
                    System.out.println("Driver with Id " + driverId + " is not unique! Driver Id creation should be checked!");
                }
                tourCount = tourCount + 1;
            }
        }

    }

    public void writeStats(String file) {
        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING TOUR STATS FOR BASE CASE!");
            int i =1;
            writer.write("no;driverId;tourId;totalDistance [m];departureTime [s];arrivalTime [s];tourDuration [s];numberOfServices");
            writer.newLine();

            for (FreightTourData data : this.finishedTours) {

                Id<Vehicle> tourId = this.driverToTourId.get(data.driver);

                writer.write(i + ";" + data.driver + ";" + tourId + ";" + data.tourDistance + ";" + data.departure + ";" + data.arrival
                        + ";" + data.tourDuration + ";" + data.serviceNo);
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
        //check if driver has a running tour

        //this is very ugly but necessary if we dont want to include further events like VehicleEntersTraffic
        Id<Person> driverId = Id.createPersonId(event.getVehicleId());

        if (this.currentTours.containsKey(driverId)) {
            //add up linkLength to distance travelled so far
            Double distanceSoFar = this.driverToDistance.computeIfAbsent(driverId, v -> 0.);
            this.driverToDistance.replace(driverId,
                    distanceSoFar + network.getLinks().get(event.getLinkId()).getLength());
        }
    }

    @Override
    public void handleEvent(ActivityStartEvent event) {

        Id<Person> driverId = event.getPersonId();
        //noOfServices
        if(event.getActType().equals("service")) {

            if(!this.driverToServiceNo.containsKey(driverId)) {
                this.driverToServiceNo.put(driverId, 1);
            } else {
                this.driverToServiceNo.replace(driverId, this.driverToServiceNo.get(driverId),
                        this.driverToServiceNo.get(driverId) + 1);
            }
        } else if(event.getActType().equals("end")) {
            if (this.currentTours.containsKey(driverId)) {

                Double distanceSoFar;
                if(this.driverToDistance.containsKey(driverId)) {
                    //add up linkLength to distance travelled so far
                    distanceSoFar = this.driverToDistance.remove(driverId);
                } else {
                    distanceSoFar = 0.;
                    System.out.println("INFO: driver " + driverId + " did not enter any link! " +
                            "Therefore the distance for this tour is set to 0!" );
                }

                FreightTourData data = this.currentTours.remove(driverId);

                //get eventTime and calculate tourDuration
                //The following should be the case for every tour!
                if (event.getTime() > this.driverToDeparture.get(driverId)) {
                    this.driverToArrival.put(driverId, event.getTime());
                    Double tourDuration = event.getTime() - this.driverToDeparture.get(driverId);
                    data.tourDuration = tourDuration;

                    //Does this make sense??? I saw that services on the same link are done like:
                    // ServiceEnd-> VehicleEnter -> EnterTraffic -> LeaveTraffic -> VehicleLeave -> ServiceStart
                    //So the driver / vehicle actually does not travel! How to handle this? Ignore or fix?
                    //Possible argument: normally the services would not have the exact same location, so it would be
                    //right to assume some distance travelled = LinkLength
                    data.tourDistance = distanceSoFar + network.getLinks().get(event.getLinkId()).getLength();
                    data.departure = this.driverToDeparture.remove(driverId);
                    data.arrival = this.driverToArrival.remove(driverId);
                    data.serviceNo = this.driverToServiceNo.remove(driverId);

                } else {
                    System.out.println("The tours of driver " + driverId + " are not correctly handled!");
                }
                //remove  driver from currentTours and out it onto finishedTours
                this.finishedTours.add(data);
            }

        }
    }

    @Override
    public void handleEvent(ActivityEndEvent event) {
        //wie RequestScheduled um TourStart zu registrieren

//        System.out.println(event.getActType());

        if(event.getActType().equals("start")) {

            //put driver into map of current tours when tour starts
            FreightTourData data = new FreightTourData(event.getPersonId(), 0.);
            this.currentTours.put(event.getPersonId(), data);
            this.driverToDeparture.putIfAbsent(event.getPersonId(), event.getTime());
        }
    }

    @Override
    public void notifyIterationEnds(IterationEndsEvent event) {
        getCorrespondingTourFromCarriersFile(this.carriers);
        writeStats(event.getServices().getControlerIO().getIterationFilename(event.getIteration(), "BaseCaseTourStats.csv"));
    }

    @Override
    public void reset(int iteration) {
        this.currentTours.clear();
        this.driverToDistance.clear();
        this.driverToDeparture.clear();
        this.driverToArrival.clear();
        this.driverToServiceNo.clear();
        this.driverToTourId.clear();

        this.finishedTours.clear();

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
