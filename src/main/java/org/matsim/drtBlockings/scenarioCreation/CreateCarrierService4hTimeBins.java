package org.matsim.drtBlockings.scenarioCreation;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.vehicles.Vehicle;
import org.matsim.vehicles.VehicleType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateCarrierService4hTimeBins {
    //We want to create new, realistic service time-bins based on an analysis when the home-activities start
    private static final String INPUT_CONFIG = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/CarrierCreationInput/HomeTimeWindowAnalysis/berlin-v5.5-10pct.config.xml";
    private static final String INPUT_PLANS = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/1pct/noIncDRT.output_plans.xml.gz";
//    private static final String INPUT_CARRIERS = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/CarrierCreationInput/carriers_services_openBerlinNet_LichtenbergNord.xml";
    private static final String INPUT_CARRIERS = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/CarrierCreationInput/carriers_services_openBerlinNet.xml";
    private static final String INPUT_CARRIER_VEHICLE_TYPES = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/CarrierCreationInput/carrier_vehicleTypes.xml";
//    private static final String OUTPUT_CARRIERS = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/CarrierCreationInput/carriers_4hTimeWindows_openBerlinNet_LichtenbergNord.xml";
    private static final String OUTPUT_CARRIERS = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/CarrierCreationInput/carriers_4hTimeWindows_openBerlinNet.xml";
    private static final boolean FULL_DAY = true;

    public static void main(String[] args) { run(); }

    private static void run() {
        Config config = ConfigUtils.loadConfig(INPUT_CONFIG);
//        config.plans().setInputFile(INPUT_PLANS);
        FreightConfigGroup freightCfg = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
        freightCfg.setCarriersFile(INPUT_CARRIERS);
        freightCfg.setCarriersVehicleTypesFile(INPUT_CARRIER_VEHICLE_TYPES);

        Scenario scenario = ScenarioUtils.loadScenario(config);
        FreightUtils.loadCarriersAccordingToFreightConfig(scenario);

        int binCount;
        if (!FULL_DAY) {
            binCount = 4;
        } else {
            binCount = 6;
        }

//        List<Carrier> newCarriers = createServiceTimeBinsBasedOnHomeActivities(scenario);
        Carriers newCarriers = createServiceTimeBinsBasedOnHomeActivities(scenario, binCount);

        new CarrierPlanXmlWriterV2(newCarriers).write(OUTPUT_CARRIERS);
        System.out.println("New Carrier services with 4h time bins were written to " + OUTPUT_CARRIERS);
    }

    public static Carriers createServiceTimeBinsBasedOnHomeActivities(Scenario scenario, int binCount) {
        //Base: all home_activity starts after 8AM with the starts before 8AM added to the earliest time bin
        //Time bins [h]: 8:00-11:59, 12:00-15:59, 16:00-19:59, 20:00-24:00+*
        //relative values [%]: 0.56, 0.15, 0.21, 0.08
        //*: Because the base run runs for 36 hours we are treating every hour after 24:00 as if it was 24:00

        final Random rnd = MatsimRandom.getLocalInstance();

        Carriers carriers = FreightUtils.getCarriers(scenario);
        Carriers newCarriers = new Carriers();
//        List<Carrier> newCarriers = new ArrayList<>();

        for (Carrier carrier : carriers.getCarriers().values()) {

            Carrier carrierCopy = createCarrierCopy(carrier, scenario, binCount);

            for (CarrierService service : carrier.getServices().values()) {
//                System.out.println(service.getServiceStartTimeWindow());
                //Service time window setter is inside of Builder method, is not reachable
                //we have to work with building a copy
                double start;
                double end;

                double rndDouble = rnd.nextDouble();

                if (!FULL_DAY) {

                    //TimeWindow 8-24+ with 0-8 activities added to 8-12 bin
                    //0,56; 0,15; 0,21; 0,08
                    if (rndDouble <= 0.56) {
                        //08:00 to 11:59; 0.56
                        start = 28800.;
                        end = 43199.;
                    } else if (rndDouble > 0.56 && rndDouble <= 0.71) {
                        //12:00 to 15:59; 0.15
                        start = 43200.;
                        end = 57599.;
                    } else if (rndDouble > 0.71 && rndDouble <= 0.92) {
                        //16:00 to 15:59; 0.21
                        start = 57600.;
                        end = 71999.;
                    } else if (rndDouble > 0.92) {
                        //20:00 to 24:00+; 0.08
                        start = 72000.;
//                        end = scenario.getConfig().qsim().getEndTime().seconds();
                        end = 86399.;
                    } else {
                        throw new RuntimeException("Random double " + rndDouble + " does not fit into the time windows, this should not happen!");
                    }

                } else {

                    //TimeWindow 0-24+
                    //0,44; 0,02; 0,09; 0,15; 0,22; 0,08
                    if (rndDouble <= 0.44) {
                        //00:00 to 03:59; 0.44
                        start = 0.;
                        end = 14399.;
                    } else if (rndDouble > 0.44 && rndDouble <= 0.46) {
                        //04:00 to 07:59; 0.02
                        start = 14400.;
                        end = 28799.;
                    } else if (rndDouble > 0.46 && rndDouble <= 0.55) {
                        //08:00 to 11:59; 0.09
                        start = 28800.;
                        end = 43199.;
                    } else if (rndDouble > 0.55 && rndDouble <= 0.70) {
                        //12:00 to 15:59; 0.15
                        start = 43200.;
                        end = 57599.;
                    } else if (rndDouble > 0.70 && rndDouble <= 0.92) {
                        //16:00 to 19:59; 0.22
                        start = 57600.;
                        end = 71999.;
                    } else if (rndDouble > 0.92) {
                        //20:00 to 24+; 0.08
                        start = 72000.;
//                        end = scenario.getConfig().qsim().getEndTime().seconds();
                        end = 86399.;
                    } else {
                        throw new RuntimeException("Random double " + rndDouble + " does not fit into the time windows, this should not happen!");
                    }
                }
                TimeWindow timeWindow = TimeWindow.newInstance(start, end);

                CarrierService serviceDummy = createServiceCopyWithNewTimeWindow(service, timeWindow);
                CarrierUtils.addService(carrierCopy, serviceDummy);
//                System.out.println(serviceDummy.getServiceStartTimeWindow());

            }
//            newCarriers.add(carrierCopy);
            newCarriers.addCarrier(carrierCopy);
        }

        return newCarriers;
    }

    private static Carrier createCarrierCopy(Carrier carrier, Scenario scenario, int binCount) {
        CarrierCapabilities cap = carrier.getCarrierCapabilities();

        Carrier carrierCopy = CarrierUtils.createCarrier(carrier.getId());

        carrierCopy.setCarrierCapabilities(createCarrierVehicleCopyAndReplace(cap, scenario, binCount));

        return carrierCopy;
    }

    private static CarrierCapabilities createCarrierVehicleCopyAndReplace(CarrierCapabilities cap, Scenario scenario, int binCount) {
//        Collection<CarrierVehicle> carrierVehicles = cap.getCarrierVehicles().values();
        List<CarrierVehicle> carrierVehicles = new ArrayList<>(cap.getCarrierVehicles().values());

        CarrierVehicle vehicle = carrierVehicles.stream().findAny().get();

        if (carrierVehicles.size() < binCount) {
            for (int i = carrierVehicles.size(); i < binCount; i++) {
                CarrierVehicle newVeh = CarrierVehicle.newInstance(Id.create(vehicle.getId() + "_" + i, Vehicle.class), vehicle.getLocation());
                newVeh.setType(vehicle.getType());
                carrierVehicles.add(newVeh);
            }
        }

        double startTime;
        //our time bins begin at 8:00 or 00:00, so start time should be the same
        if (!FULL_DAY) {
            startTime = 8 * 3600.;
        } else {
            startTime = 0.;
        }
        double endTime = startTime + 4 * 3600. - 1.;

//        cap.getCarrierVehicles().clear();

        for(CarrierVehicle veh : carrierVehicles) {
            Id<Vehicle> vehId = veh.getId();
            Id<Link> locationId = veh.getLocation();
            VehicleType type = veh.getType();
            Id<VehicleType> typeId = veh.getVehicleTypeId();


            //not sure which value should be set here; could be scenario.getConfig().qsim().getEndTime().seconds()
            //but is it really realistic that people will be receiving their deliveries after 22:00?
            //we should rather use 22:00 or 21:00; of the aforementioned, 21h is more realistic to me, but we also
            //should consider that we probably want to provide jspirt with some flexibility
//            double endTime = scenario.getConfig().qsim().getEndTime().seconds();
//            endTime = 22 * 3600.;

            CarrierVehicle.Builder vehicleBuilder = CarrierVehicle.Builder.newInstance(vehId, locationId);
            vehicleBuilder.setType(type);
            vehicleBuilder.setTypeId(typeId);
            vehicleBuilder.setEarliestStart(startTime);
            vehicleBuilder.setLatestEnd(endTime);
            CarrierVehicle vehicleCopy = vehicleBuilder.build();
//            cap.getCarrierVehicles().replace(vehId, veh, vehicleCopy);
            cap.getCarrierVehicles().put(vehicleCopy.getId(), vehicleCopy);
            cap.getCarrierVehicles().get(vehicleCopy.getId()).setType(type);
//            CarrierUtils.addCarrierVehicle(carrierCopy, vehicleCopy);
            startTime = startTime + 4 * 3600.;
            endTime = endTime + 4 * 3600.;
        }
        return cap;
    }

    private static CarrierService createServiceCopyWithNewTimeWindow(CarrierService service, TimeWindow timeWindow) {
        Id<CarrierService> serviceId = service.getId();
        Id<Link> locationLinkId = service.getLocationLinkId();
        int capacityDemand = service.getCapacityDemand();
        double serviceDuration = service.getServiceDuration();

        CarrierService.Builder serviceBuilder = CarrierService.Builder.newInstance(serviceId, locationLinkId);
        serviceBuilder.setCapacityDemand(capacityDemand);
        serviceBuilder.setServiceDuration(serviceDuration);
        serviceBuilder.setServiceStartTimeWindow(timeWindow);

        CarrierService carrierService = serviceBuilder.build();

        return carrierService;
    }
}
