package privateAV;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.optimizer.VrpOptimizer;
import org.matsim.contrib.dvrp.passenger.*;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeQSimModule;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.ModalProviders;
import org.matsim.contrib.dvrp.trafficmonitoring.DvrpTravelTimeModule;
import org.matsim.contrib.dvrp.vrpagent.VrpAgentLogic;
import org.matsim.contrib.dvrp.vrpagent.VrpAgentSourceQSimModule;
import org.matsim.contrib.taxi.optimizer.TaxiOptimizer;
import org.matsim.contrib.taxi.passenger.SubmittedTaxiRequestsCollector;
import org.matsim.contrib.taxi.passenger.TaxiRequestCreator;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.contrib.taxi.scheduler.TaxiScheduleInquiry;
import org.matsim.contrib.taxi.util.TaxiSimulationConsistencyChecker;
import org.matsim.contrib.taxi.util.stats.TaxiStatusTimeProfileCollectorProvider;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.controler.MatsimServices;
import org.matsim.core.mobsim.framework.MobsimTimer;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;

final class PFAVModuleQSim extends AbstractDvrpModeQSimModule {

    private final TaxiConfigGroup taxiCfg;
    private final FreightAVConfigGroup pfavConfigGroup;

    PFAVModuleQSim(TaxiConfigGroup taxiCfg, FreightAVConfigGroup pfavConfigGroup) {
        super(taxiCfg.getMode());
        this.taxiCfg = taxiCfg;
        this.pfavConfigGroup = pfavConfigGroup;
    }

    @Override
    protected void configureQSim() {
        install(new VrpAgentSourceQSimModule(getMode()));
        install(new PassengerEngineQSimModule(getMode()));

        addModalComponent(TaxiOptimizer.class, new ModalProviders.AbstractProvider<TaxiOptimizer>(taxiCfg.getMode()) {
            @Inject
            private MobsimTimer timer;

            @Inject
            private EventsManager events;

            @Override
            public TaxiOptimizer get() {
                Fleet fleet = getModalInstance(Fleet.class);
                TaxiScheduleInquiry taxiScheduler = getModalInstance(TaxiScheduleInquiry.class);
                return new PFAVProvider(taxiCfg, fleet, taxiScheduler, timer, events).get();
            }
        });

        bindModal(TaxiScheduleInquiry.class).toProvider(
                new ModalProviders.AbstractProvider<TaxiScheduleInquiry>(taxiCfg.getMode()) {
                    @Inject
                    private MobsimTimer timer;

                    @Inject
                    @Named(DvrpTravelTimeModule.DVRP_ESTIMATED)
                    private TravelTime travelTime;

                    @Inject
                    FreightTourManagerListBased tourManager;

                    @Inject
                    private EventsManager events;

                    @Override
                    public TaxiScheduleInquiry get() {
                        Fleet fleet = getModalInstance(Fleet.class);
                        Network network = getModalInstance(Network.class);
//                        TravelDisutility travelDisutility = getModalInstance(
//                                TravelDisutilityFactory.class).createTravelDisutility(travelTime);
                        TravelDisutility travelDisutility = getModalInstance(TravelDisutility.class);
                        return new PFAVScheduler(taxiCfg, fleet, network, timer, travelTime, travelDisutility, events, tourManager, pfavConfigGroup);
                    }
                }).asEagerSingleton();

        bindModal(VrpAgentLogic.DynActionCreator.class).toProvider(
                new ModalProviders.AbstractProvider<PFAVActionCreator>(taxiCfg.getMode()) {
                    @Inject
                    private MobsimTimer timer;

                    @Inject
                    private DvrpConfigGroup dvrpCfg;

                    @Override
                    public PFAVActionCreator get() {
                        PassengerEngine passengerEngine = getModalInstance(PassengerEngine.class);
                        TaxiOptimizer optimizer = getModalInstance(TaxiOptimizer.class);
                        return new PFAVActionCreator(passengerEngine, taxiCfg, optimizer, timer, dvrpCfg);
                    }
                }).asEagerSingleton();

        bindModal(PassengerRequestCreator.class).toProvider(modalProvider(
                getter -> new TaxiRequestCreator(getMode(), getter.getModal(SubmittedTaxiRequestsCollector.class))))
                .asEagerSingleton();

        bindModal(PassengerRequestValidator.class).to(DefaultPassengerRequestValidator.class).asEagerSingleton();

        bindModal(SubmittedTaxiRequestsCollector.class).to(SubmittedTaxiRequestsCollector.class).asEagerSingleton();

        addModalQSimComponentBinding().toProvider(modalProvider(
                getter -> new TaxiSimulationConsistencyChecker(getter.getModal(SubmittedTaxiRequestsCollector.class),
                        taxiCfg)));

        if (taxiCfg.getTimeProfiles()) {
            addModalQSimComponentBinding().toProvider(modalProvider(
                    getter -> new TaxiStatusTimeProfileCollectorProvider(getter.getModal(Fleet.class),
                            getter.get(MatsimServices.class), getter.getModal(SubmittedTaxiRequestsCollector.class),
                            taxiCfg).get()));
        }

        bindModal(VrpOptimizer.class).to(modalKey(TaxiOptimizer.class));
    }
}
