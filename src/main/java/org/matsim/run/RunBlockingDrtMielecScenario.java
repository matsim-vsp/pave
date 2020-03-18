package org.matsim.run;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.contrib.drt.run.*;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.contrib.otfvis.OTFVisLiveModule;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.drt.DrtBlockingModule;
import org.matsim.drt.DrtBlockingQSimModule;
import org.matsim.examples.ExamplesUtils;
import org.matsim.vis.otfvis.OTFVisConfigGroup;

import java.net.URL;

public class RunBlockingDrtMielecScenario {

    private static boolean otfvis = false;


    public static void main( String[] args) {
        URL configUrl = IOUtils.extendUrl(ExamplesUtils.getTestScenarioURL("mielec"), "mielec_drt_config.xml");
        Config config = ConfigUtils.loadConfig(configUrl);

        int threads = 8;

        ConfigUtils.addOrGetModule(config, DvrpConfigGroup.class);
        config.global().setNumberOfThreads(threads);

        MultiModeDrtConfigGroup multiModeDrtConfigGroup = ConfigUtils.addOrGetModule(config, MultiModeDrtConfigGroup.class);
        OTFVisConfigGroup otfvisCfg = ConfigUtils.addOrGetModule(config, OTFVisConfigGroup.class);

        DrtConfigGroup drtCfg = DrtConfigGroup.getSingleModeDrtConfig(config);
        drtCfg.setMode(TransportMode.drt);
        drtCfg.setMaxWaitTime(2 * 3600);
        drtCfg.setMaxTravelTimeAlpha(5);
        drtCfg.setMaxTravelTimeBeta(15 * 60);
        drtCfg.setStopDuration(60);
        drtCfg.setVehiclesFile("vehicles-10-cap-4.xml");
        drtCfg.setNumberOfThreads(threads);
        config.qsim().setSimStarttimeInterpretation(QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime);

        Scenario scenario = DrtControlerCreator.createScenarioWithDrtRouteFactory(config);
        ScenarioUtils.loadScenario(scenario);


        Carriers carriers = FreightUtils.getOrCreateCarriers(scenario);
        new CarrierPlanXmlReader(carriers).readFile("scenarios/mielec/mielec-carriers.xml");
        carriers.getCarriers().values().forEach(
                carrier -> CarrierUtils.setCarrierMode(carrier, TransportMode.drt));
        CarrierVehicleTypes vTypes = FreightUtils.getCarrierVehicleTypes(scenario);
        new CarrierVehicleTypeReader(vTypes).readFile("scenarios/mielec/PFAVvehicleTypes.xml");
        new CarrierVehicleTypeLoader(carriers).loadVehicleTypes(vTypes);


        Controler controler = new Controler(scenario);

        controler.addOverridingModule( new DvrpModule() ) ;
        controler.addOverridingModule( new DrtModeModule(drtCfg) ) ;
        controler.addOverridingModule( new DrtBlockingModule(drtCfg));

        if(otfvis){
            controler.addOverridingModule(new OTFVisLiveModule());
        }
        controler.configureQSimComponents( DvrpQSimComponents.activateModes( TransportMode.drt ) ) ;

        controler.run();
    }

}
