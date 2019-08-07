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
    private final TaxiConfigGroup taxiCfg;
    private final FreightAVConfigGroup pfavConfigGroup;
    private Scenario scenario;

    public PFAVModeModule(TaxiConfigGroup taxiCfg, Scenario scenario, String carriersFile, String vehTypesFile, FreightAVConfigGroup pfavConfigGroup) {
        super(taxiCfg.getMode());
        this.taxiCfg = taxiCfg;
        this.scenario = scenario;
        this.CARRIERS_FILE = carriersFile;
        this.VEHTYPES_FILE = vehTypesFile;
        this.pfavConfigGroup = pfavConfigGroup;
    }

    @Override
    public void install() {
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
        PFAVCostParameter pfavCostParameters = getPFAVCostParameter(vTypes);
        installQSimModule(new AbstractDvrpModeQSimModule(getMode()){
            @Override
            protected void configureQSim() {
//                bind(PFAVCostParameter.class).toInstance(pfavCostParameters);
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
                        return new VehTypeVariableTravelDisutility(travelTime, pfavCostParameters);
                    }
                }
        );

        addControlerListenerBinding().toInstance(tourManager);
        installQSimModule(new PFAVModuleQSim(taxiCfg, pfavConfigGroup));
    }

    private CarrierVehicleTypes readVehicleTypes(String input) {
        CarrierVehicleTypes vTypes = new CarrierVehicleTypes();
        CarrierVehicleTypeReader reader = new CarrierVehicleTypeReader(vTypes);
        reader.readFile(input);
        return vTypes;
    }

    private CarrierVehicleType retrievePFAVType(CarrierVehicleTypes vehicleTypes) {
//        return vehicleTypes.getVehicleTypes().values().stream().filter(t -> t.getId().toString().equals(PFAVUtils.PFAV_TYPE)).findFirst();
        for (CarrierVehicleType type : vehicleTypes.getVehicleTypes().values()) {
            if (type.getId().toString().equals(pfavConfigGroup.getPfavType())) return type;
        }
        throw new IllegalArgumentException("no cost parameters for vehicle type " + pfavConfigGroup.getPfavType() + " could be found in carriersVehicleTypes." +
                "please make sure that PFAV_TYPE is included in carreirsVehicleTypes. Currently, no default cost parameters for pfav are implemented..");
    }

    private PFAVCostParameter getPFAVCostParameter(CarrierVehicleTypes vehicleTypes) {
        CarrierVehicleType.VehicleCostInformation cstInfo = retrievePFAVType(vehicleTypes).getVehicleCostInformation();
        return new PFAVCostParameter(cstInfo.getFix(), cstInfo.getPerDistanceUnit(), cstInfo.getPerTimeUnit());
    }
}
