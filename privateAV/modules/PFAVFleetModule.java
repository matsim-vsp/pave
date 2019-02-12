package privateAV.modules;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.checkerframework.checker.units.UnitsTools;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.fleet.*;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeModule;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeQSimModule;
import org.matsim.contrib.dvrp.run.ModalProviders;
import org.matsim.core.config.ConfigGroup;

public class PFAVFleetModule extends AbstractDvrpModeModule {

    private boolean updateVehicleStartLinkToLastLink;
    private Scenario scenario;

    public PFAVFleetModule(String mode, Scenario scenario){
        super(mode);
        this.scenario = scenario;
    }

    @Override
    public void install() {
        bindModal(FleetSpecification.class).toProvider(() -> {
            return new FleetSpecificationImpl();
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
                        return FleetImpl.create(fleetSpecification, network.getLinks()::get);
                    }
                }).asEagerSingleton();
            }
        });

        install(FleetStatsCalculatorModule.createModule(getMode(), PFAVFleetStatsCalculator.class,
            getter -> new PFAVFleetStatsCalculator(getter.getModal(FleetSpecification.class), scenario, getMode())));
    }
}
