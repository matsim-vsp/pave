package privateAV;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeModule;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeQSimModule;
import org.matsim.contrib.dvrp.run.DvrpModes;
import org.matsim.contrib.dvrp.run.ModalProviders;
import org.matsim.contrib.dvrp.trafficmonitoring.DvrpTravelTimeModule;
import org.matsim.contrib.dynagent.run.DynRoutingModule;
import org.matsim.contrib.freight.carrier.CarrierVehicleType;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypeReader;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypes;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;

public final class PFAVModeModule extends AbstractDvrpModeModule {
    private final String CARRIERS_FILE;
    private final String VEHTYPES_FILE;
    private Scenario scenario;

    public PFAVModeModule(String mode, Scenario scenario, String carriersFile, String vehTypesFile) {
        super(mode);
        this.scenario = scenario;
        this.CARRIERS_FILE = carriersFile;
        this.VEHTYPES_FILE = vehTypesFile;
    }

    
    @Override
    public void install() {
        FreightAVConfigGroup pfavConfigGroup = (FreightAVConfigGroup) getConfig().getModules().get(FreightAVConfigGroup.GROUP_NAME);
        TaxiConfigGroup taxiConfigGroup = (TaxiConfigGroup) getConfig().getModules().get(TaxiConfigGroup.GROUP_NAME);
        if (!taxiConfigGroup.getMode().equals(this.getMode()))
            throw new RuntimeException("pfav mode must be equal to mode set in taxi config group!");

        DvrpModes.registerDvrpMode(binder(), getMode());
//        bindModal(TravelDisutilityFactory.class).toInstance(TimeAsTravelDisutility::new);
        bindModal(Network.class).to(Key.get(Network.class, Names.named(DvrpRoutingNetworkProvider.DVRP_ROUTING)));

        addRoutingModuleBinding(getMode()).toInstance(new DynRoutingModule(getMode()));

        //we need our own FleetModule here
        install(new PFAVFleetModule(getMode(), scenario));
        install(new PFAVModuleAnalysis(getMode(), scenario.getNetwork()));

        //TODO: get those files from some kind of config group or pass it to the module as parameters
//        int timeSlice = pfavConfig.getTourPlannungTimeSlice(); TODO
        CarrierVehicleTypes vTypes = readVehicleTypes(this.VEHTYPES_FILE);

        FreightTourManagerListBasedImpl tourManager = new FreightTourManagerListBasedImpl(this.CARRIERS_FILE, vTypes, PFAVUtils.timeSlice(), pfavConfigGroup);
        installQSimModule(new AbstractDvrpModeQSimModule(getMode()){
            @Override
            protected void configureQSim() {
                bind(FreightTourManagerListBased.class).toInstance(tourManager);
            }
        });

        bindModal(TravelDisutility.class).toProvider(
                new ModalProviders.AbstractProvider<TravelDisutility>(getMode()) {
                    @Inject
                    @Named(DvrpTravelTimeModule.DVRP_ESTIMATED)
                    private TravelTime travelTime;

                    @Override
                    public TravelDisutility get() {
                        return new VehTypeVariableTravelDisutility(travelTime, retrievePFAVType(vTypes, pfavConfigGroup.getPfavType()).getVehicleCostInformation());
                    }
                }
        );

        addControlerListenerBinding().toInstance(tourManager);
        installQSimModule(new PFAVModuleQSim(taxiConfigGroup.getMode()));
    }

    private CarrierVehicleType retrievePFAVType(CarrierVehicleTypes vehicleTypes, String pfavType) {
        //        return vehicleTypes.getVehicleTypes().values().stream().filter(t -> t.getId().toString().equals(PFAVUtils.PFAV_TYPE)).findFirst();
        for (CarrierVehicleType type : vehicleTypes.getVehicleTypes().values()) {
            if (type.getId().toString().equals(pfavType)) return type;
        }
        throw new IllegalArgumentException("no cost parameters for vehicle type " + pfavType + " could be found in carriersVehicleTypes." +
                "please make sure that PFAV_TYPE is included in carriersVehicleTypes. Currently, no default cost parameters for pfav are implemented..");
    }

    private CarrierVehicleTypes readVehicleTypes(String input) {
        CarrierVehicleTypes vTypes = new CarrierVehicleTypes();
        CarrierVehicleTypeReader reader = new CarrierVehicleTypeReader(vTypes);
        reader.readFile(input);
        return vTypes;
    }
}
