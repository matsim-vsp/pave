package freight;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.carrier.CarrierCapabilities.FleetSize;
import org.matsim.vehicles.EngineInformation.FuelType;
import org.matsim.vehicles.EngineInformationImpl;
import org.matsim.vehicles.VehicleType;
import privateAV.PFAVUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FreightSetUp {

	public static Carriers createCarriersWithRandomDepotAndServices(Collection<CarrierVehicleType> vehicleTypes, FleetSize fleetSize, Network network, int numberOfCarriers, int nrOfVehPerCarrierPerVehType, int nrOfServicesPerCarrier) {
		Carriers carriers = new Carriers();
		
		for(int i=1; i <=numberOfCarriers; i++) {
			
			List<CarrierVehicle> vehList = new ArrayList<>();
			for(CarrierVehicleType type : vehicleTypes) {
				for(int y = 1; y<= nrOfVehPerCarrierPerVehType; y++) {
					vehList.add(createCarrierVehicleWithRandomDepotLink(type, network, type.getId().toString() + "_" + y));
				}
			}
			
			Carrier carrier= CarrierImpl.newInstance(Id.create("carrier"+i, Carrier.class));		
			for(int z = 0; z< nrOfServicesPerCarrier; z++) {
				carrier.getServices().add(createMatsimService(""+z, getRandomLinkId(network), 1));
			}

			carrier.setCarrierCapabilities(buildCarrierCapabilities(vehicleTypes, vehList, fleetSize));
		
			carriers.addCarrier(carrier);
		}
		
		return carriers;
		
	}
	
	public static Carriers createPrivateFreightAVCarriersWithRandomDepotAnd10RandomServices(FleetSize fleetSize, Network network, int numberOfCarriers, int nrOfVehiclesPerCarrierPerVehType) {
		List<CarrierVehicleType> vTypes = new ArrayList<>();
		CarrierVehicleType type = createPrivateFreightAVVehicleType();
		vTypes.add(type);
			
		return createCarriersWithRandomDepotAndServices(vTypes, fleetSize, network, numberOfCarriers, nrOfVehiclesPerCarrierPerVehType, 10);
	}
	
	
	private static CarrierService createMatsimService(String id, Id<Link> linkId, int size) {
		return CarrierService.Builder.newInstance(Id.create(id, CarrierService.class), linkId)
				.setCapacityDemand(size)
				.setServiceDuration(31.0)
				.setServiceStartTimeWindow(TimeWindow.newInstance(0.0, 24*3600.0))
				.build();
	}
	
	public static CarrierVehicleType createPrivateFreightAVVehicleType() {
		return CarrierVehicleType.Builder.newInstance(Id.create("PFAV", VehicleType.class))
				.setCapacity(PFAVUtils.DEFAULT_PFAV_CAPACITY)
				.setMaxVelocity(13.3)
				.setCostPerDistanceUnit(0.0001)
				.setCostPerTimeUnit(0.001)
				.setFixCost(130)
				.setEngineInformation(new EngineInformationImpl(FuelType.diesel, 0.015))
				.build();
	}
	
	public static CarrierVehicle createCarrierVehicleWithRandomDepotLink(CarrierVehicleType vehType, Network network, String id) {
		return CarrierVehicle.Builder.newInstance(Id.create(id, org.matsim.vehicles.Vehicle.class), getRandomLinkId(network))
				.setEarliestStart(0.0)
				.setLatestEnd(36000.0)
				.setTypeId(vehType.getId())
				.setType(vehType)
				.build();
	}
	
	public static CarrierCapabilities buildCarrierCapabilities(Collection<CarrierVehicleType> vehTypes, List<CarrierVehicle> vehicles, FleetSize fleetSize) {
		CarrierCapabilities.Builder ccBuilder = CarrierCapabilities.Builder.newInstance();
		
		for(CarrierVehicleType type : vehTypes) {
			ccBuilder.addType(type);
		}
		
		for(CarrierVehicle vehicle : vehicles) {
			ccBuilder.addVehicle(vehicle);
		}
		
		ccBuilder.setFleetSize(fleetSize);

		return ccBuilder.build();
	}
	
	private static Id<Link> getRandomLinkId(Network network){
		//this feels very dirty
			int randomIndex = (int) ( Math.round(Math.random() * network.getLinks().size()));
			return (Id<Link>) network.getLinks().keySet().toArray()[randomIndex];
	}	
}
