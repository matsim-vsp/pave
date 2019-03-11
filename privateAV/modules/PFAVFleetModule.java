package privateAV.modules;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.fleet.FleetImpl;
import org.matsim.contrib.dvrp.fleet.FleetSpecification;
import org.matsim.contrib.dvrp.fleet.FleetSpecificationImpl;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeModule;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeQSimModule;
import org.matsim.contrib.dvrp.run.ModalProviders;
import org.matsim.contrib.dvrp.run.QSimScopeObjectListenerModule;
import privateAV.vehicle.PFAVehicle;

public class PFAVFleetModule extends AbstractDvrpModeModule {

    private boolean updateVehicleStartLinkToLastLink;
    private Scenario scenario;

    public PFAVFleetModule(String mode, Scenario scenario){
        super(mode);
        this.scenario = scenario;
    }

    @Override
    public void install() {
        FleetSpecificationImpl fleetSpecification = new FleetSpecificationImpl();
        bindModal(FleetSpecification.class).toProvider(() -> {
            return fleetSpecification;
        }).asEagerSingleton();

        installQSimModule(new AbstractDvrpModeQSimModule(getMode()) {
            @Override
            protected void configureQSim() {
                bindModal(Fleet.class).toProvider(new ModalProviders.AbstractProvider<Fleet>(getMode()) {
                    @Inject
                    @Named(DvrpRoutingNetworkProvider.DVRP_ROUTING)
                    private Network network;

                    @Override
                    public Fleet get() {
                        FleetSpecification fleetSpecification = getModalInstance(FleetSpecification.class);
//                        return FleetImpl.create(fleetSpecification, network.getLinks()::get);
                        FleetImpl fleet = new FleetImpl();
                        fleetSpecification.getVehicleSpecifications()
                                .values().stream().map(s -> PFAVehicle.createWithLinkProvider(s, network.getLinks()::get))
                                .forEach(fleet::addVehicle);
                        return fleet;
                    }
                }).asEagerSingleton();
            }
        });

        bindModal(PFAVFleetStatsCalculator.class).toProvider(modalProvider(getter ->
                new PFAVFleetStatsCalculator(fleetSpecification, scenario, getMode()))).asEagerSingleton();
        addControlerListenerBinding().to(modalKey(PFAVFleetStatsCalculator.class));

        installQSimModule(QSimScopeObjectListenerModule.createSimplifiedModule(getMode(), Fleet.class, PFAVFleetStatsCalculator.class));

//        installQSimModule(new AbstractDvrpModeQSimModule(getMode()) {
//            @Override
//            protected void configureQSim() {
////                addModalQSimComponentBinding().to(modalKey(PFAVFleetStatsCalculator.class));
//                addModalQSimComponentBinding().toProvider(modalProvider(
//                        getter -> (MobsimInitializedListener) e -> getter.getModal(PFAVFleetStatsCalculator.class).objectCreated(getter.getModal(Fleet.class)))  );
//            }
//        });


        //as PFAVFleetStatsCalculator is no MobsimListener, we cannot do the following
//        install(QSimScopeObjectListenerModule.builder(PFAVFleetStatsCalculator.class)
//                .mode(getMode())
//                .objectClass(Fleet.class)
//                .listenerCreator(
//                        getter -> new PFAVFleetStatsCalculator(getter.getModal(FleetSpecification.class), scenario, getMode() ) )
//                .build());

    }
}
