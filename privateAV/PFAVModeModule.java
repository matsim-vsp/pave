package privateAV;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeModule;
import org.matsim.contrib.dvrp.run.DvrpModes;
import org.matsim.contrib.dvrp.run.ModalProviders;
import org.matsim.contrib.dvrp.trafficmonitoring.DvrpTravelTimeModule;
import org.matsim.contrib.dynagent.run.DynRoutingModule;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;

public final class PFAVModeModule extends AbstractDvrpModeModule {

    private Scenario scenario;

    public PFAVModeModule(String mode, Scenario scenario) {
        super(mode);
        this.scenario = scenario;
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
        CarrierVehicleTypes vTypes = readVehicleTypes(pfavConfigGroup);
        Carriers carriers = readCarriersAndLoadVehicleTypes(pfavConfigGroup, vTypes);

        bind(CarrierVehicleTypes.class).annotatedWith(Names.named(FreightAVConfigGroup.GROUP_NAME)).toInstance(vTypes);
        bind(Carriers.class).annotatedWith(Names.named(FreightAVConfigGroup.GROUP_NAME)).toInstance(carriers);

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

        addControlerListenerBinding().to(FreightTourManagerListBasedImpl.class);
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

    private CarrierVehicleTypes readVehicleTypes(FreightAVConfigGroup configGroup) {
        CarrierVehicleTypes vTypes = new CarrierVehicleTypes();
        CarrierVehicleTypeReader reader = new CarrierVehicleTypeReader(vTypes);
        reader.readFile(configGroup.getVehtypesFile());
        return vTypes;
    }

    private Carriers readCarriersAndLoadVehicleTypes(FreightAVConfigGroup configGroup, CarrierVehicleTypes vehicleTypes) {
        Carriers carriers = new Carriers();
        CarrierPlanXmlReaderV2 reader = new CarrierPlanXmlReaderV2(carriers);
        reader.readFile(configGroup.getCarriersFile());
        new CarrierVehicleTypeLoader(carriers).loadVehicleTypes(vehicleTypes);
        return carriers;
    }
}
