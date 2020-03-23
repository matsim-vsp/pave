package org.matsim.drt;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.trafficmonitoring.FreeSpeedTravelTime;
import org.matsim.vehicles.VehicleType;

import java.util.Set;

import static org.junit.Assert.*;

public class FreightBlockingRequestCreatorTest {

    Scenario scenario;

    @Before
    public void setUp() throws Exception {
        Config config = ConfigUtils.loadConfig("chessboard/chessboard_drtBlocking_config.xml");
        MultiModeDrtConfigGroup multiModeDrtConfig = ConfigUtils.addOrGetModule(config, MultiModeDrtConfigGroup.class);
        multiModeDrtConfig.addParameterSet(new DrtConfigGroup());
        scenario = ScenarioUtils.loadScenario(config);

        CarrierVehicleTypes vTypes = FreightUtils.getCarrierVehicleTypes(scenario);
        Carriers carrierContainer = FreightUtils.getOrCreateCarriers(scenario);
        Id<VehicleType> v1 = Id.create("v1", VehicleType.class);

        VehicleType vehicleType = CarrierVehicleType.Builder.newInstance(v1)
                .setCapacity(1)
                .setCostPerDistanceUnit(1)
                .setCostPerTimeUnit(1)
                .setFixCost(1)
                .build();
        vTypes.getVehicleTypes().put(v1,vehicleType);

        Carrier carrier = CarrierUtils.createCarrier(Id.create("test", Carrier.class));
        setupCarrier(carrier, vehicleType);
        CarrierUtils.setCarrierMode(carrier, TransportMode.drt);
        carrierContainer.addCarrier(carrier);

        carrier = CarrierUtils.createCarrier(Id.create("test2", Carrier.class));
        setupCarrier(carrier, vehicleType);
        CarrierUtils.setCarrierMode(carrier, TransportMode.car);
        carrierContainer.addCarrier(carrier);

        new CarrierVehicleTypeLoader(carrierContainer).loadVehicleTypes(vTypes);

        FreightUtils.runJsprit(scenario, new FreightConfigGroup());
    }

    private void setupCarrier(Carrier carrier, VehicleType vehicleType) {
        CarrierVehicle vehicle = CarrierVehicle.Builder.newInstance(Id.createVehicleId(1), Id.createLinkId(1))
                .setEarliestStart(6*3600)
                .setLatestEnd(24*3600)
                .setType(vehicleType)
                .build();
        CarrierUtils.addCarrierVehicle(carrier, vehicle);
        CarrierUtils.setJspritIterations(carrier, 2);
        CarrierService service = CarrierService.Builder.newInstance(Id.create(1, CarrierService.class), Id.createLinkId(2))
                .setCapacityDemand(1)
                .setServiceStartTimeWindow(TimeWindow.newInstance(10*3600,12*3600))
                .setServiceDuration(5*60)
                .build();
        CarrierUtils.addService(carrier, service);
    }

    @Test
    public void createRequestsForIteration() {
        Set<DrtBlockingRequest> requests = new FreightBlockingRequestCreator(scenario.getConfig(), scenario.getNetwork(), new FreeSpeedTravelTime()).createBlockingRequests(FreightUtils.getCarriers(scenario));


//        travel time from depot to service = 100s.

        Assert.assertEquals("the number of requests should be 1. The carrier not using drt should not initiate a blocking request" ,  1 , requests.size());
        /* TODO
        test the id
        test the submission time
        test the blocking start
        test the blocking end
        ...
         */
    }
}