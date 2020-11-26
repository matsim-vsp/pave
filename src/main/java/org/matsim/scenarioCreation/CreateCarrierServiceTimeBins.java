package org.matsim.scenarioCreation;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.utils.objectattributes.attributable.Attributes;

import java.util.*;

public class CreateCarrierServiceTimeBins {
    //We want to create new, realistic service time-bins based on an analysis when the home-activities start
    private static final String INPUT_CONFIG = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/1pct/noIncDRT.output_config.xml";
    private static final String INPUT_PLANS = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/1pct/noIncDRT.output_plans.xml.gz";
    private static final String INPUT_CARRIERS = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/carriers_services_openBerlinNet_LichtenbergNord.xml";
    private static final String INPUT_CARRIER_VEHICLE_TYPES = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/carrier_vehicleTypes.xml";
    private static final String OUTPUT_CARRIERS = "C:/Users/simon/Desktop/carriers_newServices_openBerlinNet_LichtenbergNord.xml";

    public static void main(String[] args) { run(); }

    private static void run() {
        Config config = ConfigUtils.loadConfig(INPUT_CONFIG);
        config.plans().setInputFile(INPUT_PLANS);
        FreightConfigGroup freightCfg = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
        freightCfg.setCarriersFile(INPUT_CARRIERS);
        freightCfg.setCarriersVehicleTypesFile(INPUT_CARRIER_VEHICLE_TYPES);

        Scenario scenario = ScenarioUtils.loadScenario(config);
        FreightUtils.loadCarriersAccordingToFreightConfig(scenario);

        List<Carrier> newCarriers = createServiceTimeBinsBasedOnHomeActivities(scenario);
        new CarrierPlanWriter(newCarriers).write(OUTPUT_CARRIERS);
        System.out.println("New Carrier services were written to " + OUTPUT_CARRIERS);
    }

    public static List<Carrier> createServiceTimeBinsBasedOnHomeActivities(Scenario scenario) {
        //Base: all home_activity starts after 8AM
        //Time bins [h]: 8:00-11:59, 12:00-15:59, 16:00-19:59, 20:00-24:00+*
        //Absolute values [number of activity starts]: 10318, 16402, 23746, 9237; sum: 59703
        //relative values [%]: 0.17, 0.27, 0.40, 0.16
        //*: Because the base run runs for 36 hours we are treating every hour after 24:00 as if it was 24:00

        final Random rnd = MatsimRandom.getLocalInstance();

        Carriers carriers = FreightUtils.getCarriers(scenario);
        List<Carrier> newCarriers = new ArrayList<>();

        for (Carrier carrier : carriers.getCarriers().values()) {

            Carrier carrierCopy = createCarrierCopy(carrier);

            for (CarrierService service : carrier.getServices().values()) {
                System.out.println(service.getServiceStartTimeWindow());
                //TODO Service time window setter is inside of Builder method, is not reachable
                //Waiting for an answer from KMT, until then we have to work with building a copy
                double start = 0.;
                double end = 0.;

                double rndDouble = rnd.nextDouble();

                if(rndDouble <= 0.16) {
                    start = 72000.;
                    end = scenario.getConfig().qsim().getEndTime().seconds();
                } else if(rndDouble > 0.16 && rndDouble <= 0.17) {
                    start = 28800.;
                    end = 43199.;
                } else if(rndDouble > 0.17 && rndDouble <= 0.27) {
                    start = 43200.;
                    end = 57599.;
                } else if(rndDouble > 0.27) {
                    start = 57600.;
                    end = 71999.;
                }
                TimeWindow timeWindow = TimeWindow.newInstance(start, end);

                CarrierService serviceDummy = createServiceCopyWithNewTimeWindow(service, timeWindow);
                CarrierUtils.addService(carrierCopy, serviceDummy);

            }
            newCarriers.add(carrierCopy);
        }

        return newCarriers;
    }

    private static Carrier createCarrierCopy(Carrier carrier) {
        CarrierCapabilities cap = carrier.getCarrierCapabilities();

        Carrier carrierCopy = CarrierUtils.createCarrier(carrier.getId());
        carrierCopy.setCarrierCapabilities(cap);


        return carrierCopy;
    }

    private static CarrierService createServiceCopyWithNewTimeWindow(CarrierService service, TimeWindow timeWindow) {
        Id<CarrierService> serviceId = service.getId();
        Id<Link> locationLinkId = service.getLocationLinkId();
        int capacityDemand = service.getCapacityDemand();
        double serviceDuration = service.getServiceDuration();

        CarrierService.Builder builder = CarrierService.Builder.newInstance(serviceId, locationLinkId);
        builder.setCapacityDemand(capacityDemand);
        builder.setServiceDuration(serviceDuration);
        builder.setServiceStartTimeWindow(timeWindow);

        CarrierService carrierService = builder.build();

        return carrierService;
    }
}
