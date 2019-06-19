package privateAV.modules;

import freight.manager.ListBasedFreightTourManager;
import freight.manager.ListBasedFreightTourManagerImpl;
import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.dvrp.router.TimeAsTravelDisutility;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeModule;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeQSimModule;
import org.matsim.contrib.dynagent.run.DynRoutingModule;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.core.router.costcalculators.TravelDisutilityFactory;

public class PFAVModeModule extends AbstractDvrpModeModule {
    private final String CARRIERS_FILE;
    private final String VEHTYPES_FILE;
    private final TaxiConfigGroup taxiCfg;
    private Scenario scenario;

    public PFAVModeModule(TaxiConfigGroup taxiCfg, Scenario scenario, String carriersFile, String vehTypesFile) {
        super(taxiCfg.getMode());
        this.taxiCfg = taxiCfg;
        this.scenario = scenario;
        this.CARRIERS_FILE = carriersFile;
        this.VEHTYPES_FILE = vehTypesFile;
    }

    @Override
    public void install() {
        bindModal(TravelDisutilityFactory.class).toInstance(TimeAsTravelDisutility::new);

        addRoutingModuleBinding(getMode()).toInstance(new DynRoutingModule(getMode()));

        //we need our own FleetModule here
        install(new PFAVFleetModule(getMode(), scenario));
        install(new PFAVAnalysisModule(getMode(), scenario.getNetwork()));

        //TODO: get those files from some kind of config group or pass it to the module as parameters
        ListBasedFreightTourManagerImpl tourManager = new ListBasedFreightTourManagerImpl(this.CARRIERS_FILE, this.VEHTYPES_FILE);

        installQSimModule(new AbstractDvrpModeQSimModule(getMode()){
            @Override
            protected void configureQSim() {
                bind(ListBasedFreightTourManager.class).toInstance(tourManager);
            }
        });
        addControlerListenerBinding().toInstance(tourManager);

        installQSimModule( new PFAVQSimModule( taxiCfg ) );
    }

}
