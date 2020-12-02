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
import java.util.List;

public class CarrierServiceLocationAnalysis {

    private static final String INPUT_CONFIG = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/1pct/noIncDRT.output_config.xml";
    private static final String INPUT_PLANS = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/1pct/noIncDRT.output_plans.xml.gz";
    private static final String INPUT_CARRIERS = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/carriers_services_openBerlinNet_LichtenbergNord.xml";
    private static final String INPUT_CARRIER_VEHICLE_TYPES = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/carrier_vehicleTypes.xml";
    private static final String OUTPUT_FILE = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/carrierServiceStats.csv";

    private List<CarrierServiceData> services = new ArrayList<>();

    public static void main(String[] args) { run(); }

    private static void run() {
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
        analysis.writeStats(OUTPUT_FILE);
        System.out.println("Writing of carrier service stats to " + OUTPUT_FILE + " was successful!");


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

    public void writeStats(String file) {
        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING!");
            int i =1;
            writer.write("no;serviceId;xCoord;yCoord;linkId;carrierId;earliestStart [s];lastStart [s];duration [s];numberOfPackages");
            writer.newLine();

            for (CarrierServiceData data  : this.services) {
                writer.write(i + ";" + data.serviceId + ";" + data.xCoord + ";" + data.yCoord + ";"
                        + data.linkId + ";" + data.carrierId + ";" + data.earliestStartTime + ";" + data.lastStartTime + ";"
                        + data.serviceDuration + ";" + data.packages);
                writer.newLine();
                i++;
            }
            writer.close();
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
