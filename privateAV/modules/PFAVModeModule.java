package privateAV.modules;

import freight.manager.ListBasedFreightTourManager;
import freight.manager.ListBasedFreightTourManagerImpl;
import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.dvrp.fleet.FleetStatsCalculatorModule;
import org.matsim.contrib.dvrp.router.TimeAsTravelDisutility;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeModule;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeQSimModule;
import org.matsim.contrib.dynagent.run.DynRoutingModule;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.contrib.taxi.util.stats.TaxiStatsDumper;
import org.matsim.core.controler.IterationCounter;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.router.costcalculators.TravelDisutilityFactory;
import privateAV.PFAVUtils;

public class PFAVModeModule extends AbstractDvrpModeModule {
    private final TaxiConfigGroup taxiCfg;
    private Scenario scenario;

    public PFAVModeModule(TaxiConfigGroup taxiCfg, Scenario scenario) {
        super(taxiCfg.getMode());
        this.taxiCfg = taxiCfg;
        this.scenario = scenario;
    }

    @Override
    public void install() {
        bindModal(TravelDisutilityFactory.class).toInstance(TimeAsTravelDisutility::new);

        addRoutingModuleBinding(getMode()).toInstance(new DynRoutingModule(getMode()));

        //we need our own FleetModule here
        install(new PFAVFleetModule(getMode(), scenario));

        install(FleetStatsCalculatorModule.createModule(getMode(), TaxiStatsDumper.class,
                getter -> new TaxiStatsDumper(taxiCfg, getter.get(OutputDirectoryHierarchy.class),
                        getter.get(IterationCounter.class))));

        //TODO: get those files from some kind of config group or pass it to the module as parameter
        ListBasedFreightTourManagerImpl tourManager = new ListBasedFreightTourManagerImpl(PFAVUtils.DEFAULT_CARRIERS_FILE, PFAVUtils.DEFAULT_VEHTYPES_FILE);

        installQSimModule(new AbstractDvrpModeQSimModule(getMode()){
            @Override
            protected void configureQSim() {
                bind(ListBasedFreightTourManager.class).toInstance(tourManager);
            }
        });
        addControlerListenerBinding().toInstance(tourManager);
    }

}