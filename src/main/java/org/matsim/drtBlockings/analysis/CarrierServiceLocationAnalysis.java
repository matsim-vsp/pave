package org.matsim.drtBlockings.analysis;

import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierService;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarrierServiceLocationAnalysis {

    private static final String INPUT_CONFIG = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/Berlin_Carriers/p2-23.output_config.xml";
    private static final String INPUT_PLANS = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/Berlin_Carriers/p2-23.output_plans_200Persons.xml.gz";
    private static final String INPUT_CARRIERS = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/Berlin_Carriers/carriers_4hTimeWindows_openBerlinNet_8-24_PLANNED.xml";
    private static final String INPUT_CARRIER_VEHICLE_TYPES = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/Berlin_Carriers/carrier_vehicleTypes_woTimeCost.xml";
    private static final String OUTPUT_STATS_FILE = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/CarrierCreationInput/readyForTourPlanning/carrierServiceStats.csv";
    private static final String OUTPUT_COUNTS_FILE = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/CarrierCreationInput/readyForTourPlanning/carrierServicePackageCount.csv";
    private static final String OUTPUT_CARRIERLOC_FILE = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/CarrierCreationInput/carrierLocations.csv";

    private List<CarrierServiceData> services = new ArrayList<>();
    private Map<Id<Carrier>, CarrierServiceData> carriersData = new HashMap<>();
    private Map<Id<Link>, Integer> packagesOnLinks = new HashMap<>();

    public static void main(String[] args) {
        CarrierServiceLocationAnalysis analysis = new CarrierServiceLocationAnalysis();
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

        Network network = scenario.getNetwork();

        Carriers carriers = FreightUtils.getCarriers(scenario);
        CarrierServiceLocationAnalysis analysis = new CarrierServiceLocationAnalysis();
        analysis.analyzeCarrierServices(carriers, network);
//        analysis.countPackagesOnLinks();
//        analysis.writeStats(OUTPUT_STATS_FILE, OUTPUT_COUNTS_FILE);
        analysis.writeCarrierLocations(OUTPUT_CARRIERLOC_FILE);
//        System.out.println("Writing of carrier service stats to " + OUTPUT_STATS_FILE + " was successful!");
//        System.out.println("Writing of carrier service package count to " + OUTPUT_COUNTS_FILE + " was successful!");
        System.out.println("Writing of carrier locations to " + OUTPUT_CARRIERLOC_FILE + " was successful!");
    }

    public void analyzeCarrierServices(Carriers carriers, Network network) {
        for(Carrier carrier : carriers.getCarriers().values()) {

            Id<Link> depotLinkId = carrier.getCarrierCapabilities().getCarrierVehicles().values().stream().findFirst().get().getLocation();
            Coord coordDepot = network.getLinks().get(depotLinkId).getCoord();

            for(CarrierService service : carrier.getServices().values()) {
                Id<Link> serviceLinkId = service.getLocationLinkId();
                Coord coordService = network.getLinks().get(serviceLinkId).getCoord();

                CarrierServiceData data = new CarrierServiceData(service.getId(), coordService);
                data.serviceLinkId = serviceLinkId;
                data.carrierId = carrier.getId();
                data.coordDepot = coordDepot;
                data.depotLinkId = depotLinkId;
                data.earliestStartTime = service.getServiceStartTimeWindow().getStart();
                data.lastStartTime = service.getServiceStartTimeWindow().getEnd();
                data.serviceDuration = service.getServiceDuration();
                data.packages = service.getCapacityDemand();

                services.add(data);
                carriersData.putIfAbsent(data.carrierId, data);
            }
        }
    }

    public void countPackagesOnLinks() {

        for(CarrierServiceData data : this.services) {
            Id<Link> linkId = data.serviceLinkId;
            int packages = data.packages;
            if(!this.packagesOnLinks.containsKey(linkId)) {
                this.packagesOnLinks.put(linkId, packages);
            } else {
                this.packagesOnLinks.replace(linkId, this.packagesOnLinks.get(linkId),
                        this.packagesOnLinks.get(linkId) + packages);
            }
        }
    }

    public void writeCarrierLocations(String file) {
        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING CARRIER LOCATIONS!");
            int i = 1;
            writer.write("no;carrierId;depotLinkId;xCoord;yCoord");

            for(CarrierServiceData data : this.carriersData.values()) {
                writer.newLine();
                writer.write(i + ";" + data.carrierId + ";" + data.depotLinkId + ";" + data.coordDepot.getX() + ";" +
                        data.coordDepot.getY());
                i++;
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeStats(String file, String file2) {
        BufferedWriter statsWriter = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING STATS!");
            int i =1;
            statsWriter.write("no;serviceId;xCoord;yCoord;serviceLinkId;carrierId;earliestStart [s];lastStart [s];duration [s];numberOfPackages");
            statsWriter.newLine();

            for (CarrierServiceData data  : this.services) {
                statsWriter.write(i + ";" + data.serviceId + ";" + data.coordService.getX() + ";" + data.coordService.getY() + ";"
                        + data.serviceLinkId + ";" + data.carrierId + ";" + data.earliestStartTime + ";" + data.lastStartTime + ";"
                        + data.serviceDuration + ";" + data.packages);
                statsWriter.newLine();
                i++;
            }
            statsWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedWriter countWriter = IOUtils.getBufferedWriter(file2);
        try {
            System.out.println("WRITING COUNT!");
            int j =1;
            countWriter.write("no;serviceLinkId;numberOfPackages");
            countWriter.newLine();

            for (Id<Link> linkId  : this.packagesOnLinks.keySet()) {
                countWriter.write(j + ";" + linkId + ";" + this.packagesOnLinks.get(linkId));
                countWriter.newLine();
                j++;
            }
            countWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class CarrierServiceData {
        private Id<CarrierService> serviceId;
        private Coord coordService;
        private Coord coordDepot;
        private Id<Link> serviceLinkId;
        private Id<Link> depotLinkId;
        private Id<Carrier> carrierId;
        private double earliestStartTime;
        private double lastStartTime;
        private double serviceDuration;
        private int packages;

        private CarrierServiceData(Id<CarrierService> serviceId, Coord coordService) {
            this.serviceId = serviceId;
            this.coordService = coordService;
        }
    }
}
