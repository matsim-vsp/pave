package scenarioCreation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierCapabilities;
import org.matsim.contrib.freight.carrier.CarrierCapabilities.FleetSize;
import org.matsim.contrib.freight.carrier.CarrierImpl;
import org.matsim.contrib.freight.carrier.CarrierService;
import org.matsim.contrib.freight.carrier.CarrierVehicle;
import org.matsim.contrib.freight.carrier.CarrierVehicleType;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.carrier.TimeWindow;
import org.matsim.vehicles.VehicleType;

import privateAV.PFAVUtils;

public class FreightSetUp {

    public static Carriers createCarriersWithRandomDepotAndServices(Collection<VehicleType> vehicleTypes, FleetSize fleetSize, Network network, int numberOfCarriers, int nrOfVehPerCarrierPerVehType, int nrOfServicesPerCarrier) {
        Carriers carriers = new Carriers();

        for (int i = 1; i <= numberOfCarriers; i++) {

            List<CarrierVehicle> vehList = new ArrayList<>();
            for (VehicleType type : vehicleTypes) {
                for (int y = 1; y <= nrOfVehPerCarrierPerVehType; y++) {
                    vehList.add(createCarrierVehicleWithRandomDepotLink(type, network, type.getId().toString() + "_" + y));
                }
            }

            Carrier carrier = CarrierImpl.newInstance(Id.create("carrier" + i, Carrier.class));
            for (int z = 0; z < nrOfServicesPerCarrier; z++) {
            	CarrierService service = createMatsimService("" + z, getRandomLinkId(network), 1);
                carrier.getServices().put(service.getId(), service);
            }

            carrier.setCarrierCapabilities(buildCarrierCapabilities(vehicleTypes, vehList, fleetSize));

            carriers.addCarrier(carrier);
        }

        return carriers;

    }

    public static Carriers createPrivateFreightAVCarriersWithRandomDepotAnd10RandomServices(FleetSize fleetSize, Network network, int numberOfCarriers, int nrOfVehiclesPerCarrierPerVehType) {
        List<VehicleType> vTypes = new ArrayList<>();
        VehicleType type = createPrivateFreightAVVehicleType();
        vTypes.add(type);

        return createCarriersWithRandomDepotAndServices(vTypes, fleetSize, network, numberOfCarriers, nrOfVehiclesPerCarrierPerVehType, 10);
    }


    private static CarrierService createMatsimService(String id, Id<Link> linkId, int size) {
        return CarrierService.Builder.newInstance(Id.create(id, CarrierService.class), linkId)
                .setCapacityDemand(size)
                .setServiceDuration(31.0)
                .setServiceStartTimeWindow(TimeWindow.newInstance(0.0, 24 * 3600.0))
                .build();
    }

    public static VehicleType createPrivateFreightAVVehicleType() {
        return CarrierVehicleType.Builder.newInstance(Id.create("PFAV", VehicleType.class))
                .setCapacity(PFAVUtils.DEFAULT_PFAV_CAPACITY)
                .setMaxVelocity(13.3)
                .setCostPerDistanceUnit(0.0001)
                .setCostPerTimeUnit(0.001)
                .setFixCost(130)
//                .setEngineInformation(new EngineInformationImpl(FuelType.diesel, 0.015))
                .build();
    }

    public static CarrierVehicle createCarrierVehicleWithRandomDepotLink(VehicleType vehType, Network network, String id) {
        return CarrierVehicle.Builder.newInstance(Id.create(id, org.matsim.vehicles.Vehicle.class), getRandomLinkId(network))
                .setEarliestStart(0.0)
                .setLatestEnd(36000.0)
                .setTypeId(vehType.getId())
                .setType(vehType)
                .build();
    }

    public static CarrierCapabilities buildCarrierCapabilities(Collection<VehicleType> vehTypes, List<CarrierVehicle> vehicles, FleetSize fleetSize) {
        CarrierCapabilities.Builder ccBuilder = CarrierCapabilities.Builder.newInstance();

        for (VehicleType type : vehTypes) {
            ccBuilder.addType(type);
        }

        for (CarrierVehicle vehicle : vehicles) {
            ccBuilder.addVehicle(vehicle);
        }

        ccBuilder.setFleetSize(fleetSize);

        return ccBuilder.build();
    }

    private static Id<Link> getRandomLinkId(Network network) {
        //this feels very dirty
        int randomIndex = (int) (Math.round(Math.random() * network.getLinks().size()));
        return (Id<Link>) network.getLinks().keySet().toArray()[randomIndex];
    }
}
