package privateAV;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.dvrp.router.TimeAsTravelDisutility;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeModule;
import org.matsim.contrib.dvrp.run.AbstractDvrpModeQSimModule;
import org.matsim.contrib.dvrp.run.DvrpModes;
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
        DvrpModes.registerDvrpMode(binder(), getMode());
        bindModal(TravelDisutilityFactory.class).toInstance(TimeAsTravelDisutility::new);

        addRoutingModuleBinding(getMode()).toInstance(new DynRoutingModule(getMode()));

        //we need our own FleetModule here
        install(new PFAVFleetModule(getMode(), scenario));
        install(new PFAVModuleAnalysis(getMode(), scenario.getNetwork()));

        //TODO: get those files from some kind of config group or pass it to the module as parameters
//        int timeSlice = pfavConfig.getTourPlannungTimeSlice(); TODO
        FreightTourManagerListBasedImpl tourManager = new FreightTourManagerListBasedImpl(this.CARRIERS_FILE, this.VEHTYPES_FILE, 1800);

        installQSimModule(new AbstractDvrpModeQSimModule(getMode()){
            @Override
            protected void configureQSim() {
                bind(FreightTourManagerListBased.class).toInstance(tourManager);
            }
        });
        addControlerListenerBinding().toInstance(tourManager);
        installQSimModule(new PFAVModuleQSim(taxiCfg));
    }

}
