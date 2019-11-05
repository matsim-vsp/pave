package privateAV;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Singleton;
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
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;
import org.matsim.vehicles.VehicleType;

public final class PFAVModeModule extends AbstractDvrpModeModule {

    private Scenario scenario;

    public PFAVModeModule(String mode, Scenario scenario) {
        super(mode);
        this.scenario = scenario;
    }

    @Override
    public void install() {

        FreightAVConfigGroup pfavConfigGroup = ConfigUtils.addOrGetModule(getConfig(), FreightAVConfigGroup.class);
        TaxiConfigGroup taxiConfigGroup = TaxiConfigGroup.getSingleModeTaxiConfig(getConfig());
        if (!taxiConfigGroup.getMode().equals(this.getMode()))
            throw new RuntimeException("pfav mode must be equal to mode set in taxi config group!");

        FreightConfigGroup freightConfig = ConfigUtils.addOrGetModule(getConfig(), FreightConfigGroup.class);

        DvrpModes.registerDvrpMode(binder(), getMode());
//        bindModal(TravelDisutilityFactory.class).toInstance(TimeAsTravelDisutility::new);
        bindModal(Network.class).to(Key.get(Network.class, Names.named(DvrpRoutingNetworkProvider.DVRP_ROUTING)));

        addRoutingModuleBinding(getMode()).toInstance(new DynRoutingModule(getMode()));

        //we need our own FleetModule here
        install(new PFAVFleetModule(getMode(), scenario));
        install(new PFAVModuleAnalysis(getMode(), scenario.getNetwork()));

        //load carriers and carrierVehicleTypes into scenario
        FreightUtils.loadCarriersAccordingToFreightConfig(scenario);

        //bind carriers and carrierVehicleTypes
        CarrierVehicleTypes vTypes = FreightUtils.getCarrierVehicleTypes(scenario);
        bind(CarrierVehicleTypes.class).annotatedWith(Names.named(FreightAVConfigGroup.GROUP_NAME)).toInstance(vTypes);
        bind(Carriers.class).annotatedWith(Names.named(FreightAVConfigGroup.GROUP_NAME)).toInstance(FreightUtils.getCarriers(scenario));

        bindModal(TravelDisutility.class).toProvider(
                new ModalProviders.AbstractProvider<TravelDisutility>(getMode()) {
                    @Inject
                    @Named(DvrpTravelTimeModule.DVRP_ESTIMATED)
                    private TravelTime travelTime;

                    @Override
                    public TravelDisutility get() {
                        return new VehTypeVariableTravelDisutility(travelTime, retrievePFAVType(vTypes, pfavConfigGroup.getPfavType()).getCostInformation());
                    }
                }
        );
        bind(FreightTourManagerListBased.class).to(FreightTourManagerListBasedImpl.class).in(Singleton.class);
        addControlerListenerBinding().to(FreightTourManagerListBased.class);
        installQSimModule(new PFAVModuleQSim(taxiConfigGroup.getMode()));
    }

    private VehicleType retrievePFAVType(CarrierVehicleTypes vehicleTypes, String pfavType) {
        //        return vehicleTypes.getVehicleTypes().values().stream().filter(t -> t.getId().toString().equals(PFAVUtils.PFAV_TYPE)).findFirst();
        for (VehicleType type : vehicleTypes.getVehicleTypes().values()) {
            if (type.getId().toString().equals(pfavType)) return type;
        }
        throw new IllegalArgumentException("no cost parameters for vehicle type " + pfavType + " could be found in carrierVehicleTypes." +
                "please make sure that PFAV_TYPE (currently set in config group to " + pfavType + ") is included in carrierVehicleTypes." +
                "Currently, no default cost parameters for pfav are implemented..");
    }

}
