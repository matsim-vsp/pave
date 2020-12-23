package org.matsim.drtBlockings.analysis;

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

    private static final String INPUT_CONFIG = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/1pct/noIncDRT.output_config.xml";
    private static final String INPUT_PLANS = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/1pct/noIncDRT.output_plans.xml.gz";
    private static final String INPUT_CARRIERS = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/carriers_services_openBerlinNet_LichtenbergNord.xml";
    private static final String INPUT_CARRIER_VEHICLE_TYPES = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/carrier_vehicleTypes.xml";
    private static final String OUTPUT_STATS_FILE = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/carrierServiceStats.csv";
    private static final String OUTPUT_COUNTS_FILE = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/carrierServicePackageCount.csv";

    private List<CarrierServiceData> services = new ArrayList<>();
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
        analysis.countPackagesOnLinks();
        analysis.writeStats(OUTPUT_STATS_FILE, OUTPUT_COUNTS_FILE);
        System.out.println("Writing of carrier service stats to " + OUTPUT_STATS_FILE + " was successful!");
        System.out.println("Writing of carrier service package count to " + OUTPUT_COUNTS_FILE + " was successful!");
    }

    public void analyzeCarrierServices(Carriers carriers, Network network) {
        for(Carrier carrier : carriers.getCarriers().values()) {
            for(CarrierService service : carrier.getServices().values()) {
                Id<Link> linkId = service.getLocationLinkId();
                double xCoord = network.getLinks().get(linkId).getCoord().getX();
                double yCoord = network.getLinks().get(linkId).getCoord().getY();

                CarrierServiceData data = new CarrierServiceData(service.getId(), xCoord, yCoord);
                data.linkId = linkId;
                data.carrierId = carrier.getId();
                data.earliestStartTime = service.getServiceStartTimeWindow().getStart();
                data.lastStartTime = service.getServiceStartTimeWindow().getEnd();
                data.serviceDuration = service.getServiceDuration();
                data.packages = service.getCapacityDemand();

                services.add(data);
            }
        }
    }

    public void countPackagesOnLinks() {

        for(CarrierServiceData data : this.services) {
            Id<Link> linkId = data.linkId;
            int packages = data.packages;
            if(!this.packagesOnLinks.containsKey(linkId)) {
                this.packagesOnLinks.put(linkId, packages);
            } else {
                this.packagesOnLinks.replace(linkId, this.packagesOnLinks.get(linkId),
                        this.packagesOnLinks.get(linkId) + packages);
            }
        }
    }

    public void writeStats(String file, String file2) {
        BufferedWriter statsWriter = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING STATS!");
            int i =1;
            statsWriter.write("no;serviceId;xCoord;yCoord;linkId;carrierId;earliestStart [s];lastStart [s];duration [s];numberOfPackages");
            statsWriter.newLine();

            for (CarrierServiceData data  : this.services) {
                statsWriter.write(i + ";" + data.serviceId + ";" + data.xCoord + ";" + data.yCoord + ";"
                        + data.linkId + ";" + data.carrierId + ";" + data.earliestStartTime + ";" + data.lastStartTime + ";"
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
            countWriter.write("no;linkId;numberOfPackages");
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
        private double xCoord;
        private double yCoord;
        private Id<Link> linkId;
        private Id<Carrier> carrierId;
        private double earliestStartTime;
        private double lastStartTime;
        private double serviceDuration;
        private int packages;

        private CarrierServiceData(Id<CarrierService> serviceId, double xCoord, double yCoord) {
            this.serviceId = serviceId;
            this.xCoord = xCoord;
            this.yCoord = yCoord;
        }
    }
}
