package scenarioCreation;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.carrier.CarrierCapabilities.FleetSize;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleUtils;
import privateAV.PFAVUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
                Id<CarrierService> serviceId = Id.create(z, CarrierService.class);
                carrier.getServices().put(serviceId, createMatsimService(serviceId, getRandomLinkId(network), 1));
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


    private static CarrierService createMatsimService(Id<CarrierService> id, Id<Link> linkId, int size) {
        return CarrierService.Builder.newInstance(id, linkId)
                .setCapacityDemand(size)
                .setServiceDuration(31.0)
                .setServiceStartTimeWindow(TimeWindow.newInstance(0.0, 24 * 3600.0))
                .build();
    }

    public static VehicleType createPrivateFreightAVVehicleType() {
        VehicleType vehicleType = VehicleUtils.createVehicleType(Id.create("PFAV", VehicleType.class));
        vehicleType.getCapacity().setOther(PFAVUtils.DEFAULT_PFAV_CAPACITY);
        vehicleType.setMaximumVelocity(13.3);
        vehicleType.getCostInformation().setCostsPerMeter(0.0001);
        vehicleType.getCostInformation().setCostsPerSecond(0.001);
        vehicleType.getCostInformation().setFixedCost(130.);
//                .setEngineInformation(new EngineInformationImpl(FuelType.diesel, 0.015))
//        vehicleType.getEngineInformation().getAttributes()
        return vehicleType;
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
