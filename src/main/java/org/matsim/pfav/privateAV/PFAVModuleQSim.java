package org.matsim.pfav.privateAV;

import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.optimizer.VrpOptimizer;
import org.matsim.contrib.dvrp.passenger.DefaultPassengerRequestValidator;
import org.matsim.contrib.dvrp.passenger.PassengerEngineQSimModule;
import org.matsim.contrib.dvrp.passenger.PassengerHandler;
import org.matsim.contrib.dvrp.passenger.PassengerRequestCreator;
import org.matsim.contrib.dvrp.passenger.PassengerRequestValidator;
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
import org.matsim.contrib.taxi.util.TaxiSimulationConsistencyChecker;
import org.matsim.contrib.taxi.util.stats.TaxiStatusTimeProfileCollectorProvider;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.controler.MatsimServices;
import org.matsim.core.mobsim.framework.MobsimTimer;
import org.matsim.core.router.FastAStarLandmarksFactory;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;

import com.google.inject.Inject;
import com.google.inject.name.Named;

final class PFAVModuleQSim extends AbstractDvrpModeQSimModule {


    PFAVModuleQSim(String mode) {
        super(mode);
    }

    
    @Override
    protected void configureQSim() {
        TaxiConfigGroup taxiCfg = TaxiConfigGroup.getSingleModeTaxiConfig(getConfig());
        FreightAVConfigGroup pfavConfigGroup = FreightAVConfigGroup.get(getConfig());

        install(new VrpAgentSourceQSimModule(getMode()));
        install(new PassengerEngineQSimModule(getMode()));


//        bind(FreightTourManagerListBased.class).to(FreightTourManagerListBasedImpl.class).in(Singleton.class);


        addModalComponent(TaxiOptimizer.class, new ModalProviders.AbstractProvider<>(taxiCfg.getMode()) {
            @Inject
            private MobsimTimer timer;

            @Inject
			private EventsManager events;

			@Override
			public TaxiOptimizer get() {
				Fleet fleet = getModalInstance(Fleet.class);
				PFAVScheduler scheduler = getModalInstance(PFAVScheduler.class);
				return new PFAVOptimizer(taxiCfg, fleet, scheduler, events, timer);
			}
		});

		bindModal(PFAVScheduler.class).toProvider(new ModalProviders.AbstractProvider<>(taxiCfg.getMode()) {
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
			public PFAVScheduler get() {
				Fleet fleet = getModalInstance(Fleet.class);
				Network network = getModalInstance(Network.class);
				TravelDisutility travelDisutility = getModalInstance(TravelDisutility.class);
				LeastCostPathCalculator router = new FastAStarLandmarksFactory(
						getConfig().global()).createPathCalculator(network, travelDisutility, travelTime);
				return new PFAVScheduler(taxiCfg, fleet, network, timer, travelTime, router, events, tourManager,
						pfavConfigGroup);
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
						PassengerHandler passengerHandler = getModalInstance(PassengerHandler.class);
						TaxiOptimizer optimizer = getModalInstance(TaxiOptimizer.class);
						return new PFAVActionCreator(passengerHandler, taxiCfg, optimizer, timer, dvrpCfg);
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
