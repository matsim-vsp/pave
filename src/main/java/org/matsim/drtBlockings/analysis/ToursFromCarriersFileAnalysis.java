package org.matsim.drtBlockings.analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.vehicles.Vehicle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToursFromCarriersFileAnalysis implements IterationEndsListener {

    private static final String INPUT_DIR = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/berlin-v5.5-10pct/policy_cases/";
    private static final String INPUT_CONFIG = INPUT_DIR + "p2-23.output_config.xml";
    private static final String INPUT_PLANS = INPUT_DIR + "p2-23.output_plans_200Persons.xml.gz";
    private static final String INPUT_CARRIERS = INPUT_DIR + "carriers_4hTimeWindows_openBerlinNet_8-24.xml";
    private static final String INPUT_CARRIER_VEHICLE_TYPES = INPUT_DIR + "carrier_vehicleTypes.xml";
    private static final String OUTPUT_STATS_FILE = INPUT_DIR + "ToursFromCarriersAnalysis.csv";

    private List<CarrierTourData> tours = new ArrayList<>();
//    private Map<Id<Link>, Integer> packagesOnLinks = new HashMap<>();

    public static void main(String[] args) {
        ToursFromCarriersFileAnalysis analysis = new ToursFromCarriersFileAnalysis();
        analysis.run();
    }

    private void run() {
        Config config = ConfigUtils.loadConfig(INPUT_CONFIG);
        config.plans().setInputFile(INPUT_PLANS);
        FreightConfigGroup freightCfg = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
        freightCfg.setCarriersFile(INPUT_CARRIERS);
        freightCfg.setCarriersVehicleTypesFile(INPUT_CARRIER_VEHICLE_TYPES);

        Scenario scenario = ScenarioUtils.loadScenario(config);
        FreightUtils.loadCarriersAccordingToFreightConfig(scenario);

        Carriers carriers = FreightUtils.getCarriers(scenario);
        ToursFromCarriersFileAnalysis analysis = new ToursFromCarriersFileAnalysis();
        analysis.analyzeCarrierServices(carriers);
        analysis.writeStats(OUTPUT_STATS_FILE);
        System.out.println("Writing of carrier service stats to " + OUTPUT_STATS_FILE + " was successful!");
}

    public void analyzeCarrierServices(Carriers carriers) {
        for(Carrier carrier : carriers.getCarriers().values()) {
            for(ScheduledTour tour : carrier.getSelectedPlan().getScheduledTours()) {

                Id<Vehicle> id = tour.getVehicle().getId();
                double start = tour.getTour().getStart().getTimeWindow().getStart();

                CarrierTourData data = new CarrierTourData(id, start);

                double distance = 0;
                int serviceCount = 0;
                double duration = 0;
                List<Id<CarrierService>> services = null;
                for(Tour.TourElement e : tour.getTour().getTourElements()) {
                    if(e instanceof Tour.Leg) {


                        distance = distance + ((NetworkRoute) ((Tour.Leg) e).getRoute()).getLinkIds()getDistance();
                        duration = duration + ((Tour.Leg) e).getExpectedTransportTime();
                    } else if(e instanceof Tour.ServiceActivity) {
                        serviceCount = serviceCount + 1;
                        services.add(((Tour.ServiceActivity) e).getService().getId());
                        duration = duration + ((Tour.ServiceActivity) e).getDuration();
//                    } else if(e instanceof Tour.Delivery) {
//                        serviceCount = serviceCount + 1;
//                        services.add(e.ge)
//                        duration = duration + ((Tour.Delivery) e).getDuration();
                    } else {
                        System.out.println("There should be no other TourElements than leg or service");
                    }
                }

                data.duration = duration;
                data.distance = distance;
                data.carrierId = carrier.getId();
                data.serviceCount = serviceCount;
                data.services = services;

                tours.add(data);
            }
        }
    }

    public void writeStats(String file) {
        BufferedWriter statsWriter = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING TOUR STATS FROM CARRIERS FILE!");
            int i =1;
            statsWriter.write("no;tourId;totalDistance [m];departureTime [s];arrivalTime [s]; tourDuration [s];" +
                    "carrierId;numberOfServices;services");
            statsWriter.newLine();

            for (CarrierTourData data  : this.tours) {
                statsWriter.write(i + ";" + data.carrierId + data.tourId + ";" + data.distance + ";" + data.start + ";" +
                        (data.start + data.duration) + ";" +data.duration + ";" + data.carrierId + ";" +
                        data.serviceCount + ";" + data.services);
                statsWriter.newLine();
                i++;
            }
            statsWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyIterationEnds(IterationEndsEvent event) {
        String INPUT_DIR = event.getServices().getConfig().controler().getOutputDirectory();
        String outputFile = INPUT_DIR + "/ToursFromCarriersAnalysis.csv";
        writeStats(outputFile);
    }

    private class CarrierTourData {
        private Id<Vehicle> tourId;
        private double start;
        private double duration;
        private double distance;
        private Id<Carrier> carrierId;
        private int serviceCount;
        private List<Id<CarrierService>> services;

        private CarrierTourData(Id<Vehicle> tourId, double start) {
            this.tourId = tourId;
            this.start = start;

        }
    }
}
