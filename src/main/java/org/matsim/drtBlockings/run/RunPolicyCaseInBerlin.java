package org.matsim.drtBlockings.run;

/**
*   Run class for the DRT-Blocking policy cases in Berlin, where DRT and Freight-traffic is handled by one fleet
*   Differences to Base Case:
*       1) Because of the fact that Freight traffic is handled by the same fleet as DRT we need to include the DrtBlockingModule
*       2) We do not need some of the carrier modules
 */

import ch.sbb.matsim.config.SwissRailRaptorConfigGroup;
import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlWriterV2;
import org.matsim.contrib.freight.carrier.CarrierUtils;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.usecases.analysis.CarrierScoreStats;
import org.matsim.contrib.freight.usecases.analysis.LegHistogram;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.MatsimServices;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.drtBlockings.DrtBlockingModule;
import org.matsim.drtBlockings.analysis.BasicTourStatsAnalysis;
import org.matsim.drtBlockings.analysis.NeverStartedToursAnalysisV1;
import org.matsim.run.RunBerlinScenario;
import org.matsim.run.drt.RunDrtOpenBerlinScenario;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class RunPolicyCaseInBerlin {

    /**
     * @param args should contain the following arguments in the specified order:
     *             1) path to config
     *             2) path to carrier plans
     *             3) path to carrier vehicle types
     *             4) boolean value that determines whether tour planning should be performed before the mobsim. True = tour planning gets performed
     *             5) path to network
     *             6) path to output directory
     *             7) path to networkChangeEvents
     *             8) path to plans
     */

    //General input
    private static final String INPUT_CONFIG = "";
    private static final String INPUT_NETWORK_CHANGE_EVENTS = "";
    private static final String INPUT_DRT_PLANS = "";
    private static final String INPUT_NETWORK = "";

    //Carrier input
    private static final String CARRIERS_PLANS_PLANNED = "";
    private static final String CARRIER_VEHICLE_TYPES = "";
    private static final boolean RUN_TOURPLANNING = false;

    private static final String OUTPUT_DIR = "./output/berlin-v5.5-10pct/";

    public static void main(String[] args) {
        String configPath;
        String carrierPlans;
        String carrierVehTypes;
        boolean performTourplanning;
        String inputNetwork;
        String outputPath;
        String networkChangeEvents;
        String inputPlans;


        if(args.length > 0){
            configPath = args[0];
            carrierPlans = args[1];
            carrierVehTypes = args[2];
            performTourplanning = Boolean.valueOf(args[3]);
            inputNetwork = args[4];
            outputPath = args[5];
            networkChangeEvents = args[6];
            inputPlans = args[7];
        } else {
            configPath = INPUT_CONFIG;
            carrierPlans = CARRIERS_PLANS_PLANNED;
            carrierVehTypes = CARRIER_VEHICLE_TYPES;
            performTourplanning = RUN_TOURPLANNING;
            inputNetwork = INPUT_NETWORK;
            outputPath = OUTPUT_DIR;
            networkChangeEvents = INPUT_NETWORK_CHANGE_EVENTS;
            inputPlans = INPUT_DRT_PLANS;
        }

        Config config = prepareConfig(configPath, carrierPlans, carrierVehTypes, inputNetwork, outputPath,
                networkChangeEvents, inputPlans);

        Scenario scenario = prepareScenario(config, performTourplanning);

        Controler controler = prepareControler(scenario);

        //might need to customize this method, for now it stays as it is in PFAV
        //not sure if its needed here because we already got our carriers analyzed with the following analysis
        prepareFreightOutputDataAndStats(scenario, controler.getEvents(), controler, FreightUtils.getCarriers(scenario));

        //not sure which analysis are needed. We may need to add some analysis classes
        BasicTourStatsAnalysis basicBlockingAnalysis = new BasicTourStatsAnalysis(scenario.getNetwork());
        NeverStartedToursAnalysisV1 blockingRejectionAnalysis = new NeverStartedToursAnalysisV1();

        controler.addOverridingModule(new AbstractModule() {
            @Override
            public void install() {
                addControlerListenerBinding().toInstance(basicBlockingAnalysis);
                addEventHandlerBinding().toInstance(basicBlockingAnalysis);

                addControlerListenerBinding().toInstance(blockingRejectionAnalysis);
                addEventHandlerBinding().toInstance(blockingRejectionAnalysis);
            }
        });

        controler.run();

    }

    public static Config prepareConfig(String configPath, String carrierPlans, String carrierVehTypes, String inputNetwork,
                                       String outputPath, String networkChangeEvents, String inputPlans) {
        //get inputConfig
        Config config = RunDrtOpenBerlinScenario.prepareConfig(new String[]{configPath});

        //General settings
        config.controler().setLastIteration(0);
        config.controler().setRunId(config.controler().getRunId() + "DRTBlockingPolicyCase");
        config.controler().setOutputDirectory(outputPath);
        config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
        config.network().setInputFile(inputNetwork);
        config.network().setChangeEventsInputFile(networkChangeEvents);
        config.network().setTimeVariantNetwork(true);
        config.plans().setInputFile(inputPlans);
        config.qsim().setFlowCapFactor(100.);

        //Freight settings
        FreightConfigGroup freightConfig = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
        freightConfig.setCarriersFile(carrierPlans);
        freightConfig.setCarriersVehicleTypesFile(carrierVehTypes);
        freightConfig.setTimeWindowHandling(FreightConfigGroup.TimeWindowHandling.enforceBeginnings);

        SwissRailRaptorConfigGroup srrConfig = ConfigUtils.addOrGetModule(config, SwissRailRaptorConfigGroup.class);
        srrConfig.setUseIntermodalAccessEgress(false);

        return config;
    }

    public static Scenario prepareScenario(Config config, boolean performTourplanning) {

        DrtConfigGroup drtCfg = DrtConfigGroup.getSingleModeDrtConfig(config);
        FreightConfigGroup freightCfg = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);

        Scenario scenario = RunDrtOpenBerlinScenario.prepareScenario(config);
        FreightUtils.loadCarriersAccordingToFreightConfig(scenario);

        //Run Tourplanning if the carriers plans were not already planned before running the sim
        if(performTourplanning){
            try {
                FreightUtils.getCarriers(scenario).getCarriers().values().forEach(carrier -> {
                    CarrierUtils.setCarrierMode(carrier, drtCfg.getMode());
                    CarrierUtils.setJspritIterations(carrier, 20);
                });
                FreightUtils.runJsprit(scenario, freightCfg);
                new File(config.controler().getOutputDirectory()).mkdirs();
                new CarrierPlanXmlWriterV2(FreightUtils.getCarriers(scenario)).write(config.controler().getOutputDirectory() + "carriers_planned.xml");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return scenario;
    }

    public static Controler prepareControler(Scenario scenario) {

        Controler controler = RunBerlinScenario.prepareControler(scenario);
        configureDRTIncludingDRTBlocking(scenario, controler);

        //this was copied from PFAV RunNormalFreightInBerlin class, not sure if its necessary
//        controler.addOverridingModule(new CarrierModule());
//        controler.addOverridingModule(new AbstractModule() {
//            @Override
//            public void install() {
//                bind(CarrierPlanStrategyManagerFactory.class).toInstance(() -> null);
//                bind(CarrierScoringFunctionFactory.class).to(CarrierScoringFunctionFactoryImpl.class);
//            }
//        });

        return controler;
    }

    private static void configureDRTIncludingDRTBlocking(Scenario scenario, Controler controler) {

        //this line is only needed if DrtBlocking is wished to be activated (=only in the policy cases)
        DrtConfigGroup drtCfg = DrtConfigGroup.getSingleModeDrtConfig(scenario.getConfig());

        //Here the main difference between base case and policy cases is set:
        //Base case does not need to add DrtBlockingModule
        //Policy cases do need the module
        controler.addOverridingModule(new AbstractModule() {
            @Override
            public void install() {
                install(new DvrpModule());
                controler.addOverridingModule( new DvrpModule()) ;
                controler.addOverridingModule( new DrtBlockingModule(drtCfg));
            }
        });
        controler.configureQSimComponents(DvrpQSimComponents.activateAllModes(MultiModeDrtConfigGroup.get(controler.getConfig())));
    }

    private static void prepareFreightOutputDataAndStats(Scenario scenario, EventsManager eventsManager,
                                                         MatsimServices controler, final Carriers carriers) {
        //freight only histogram
        //15 min bins = 900s; Isnt this too detailed?
        final LegHistogram freightOnly = new LegHistogram(900);
        freightOnly.setPopulation(scenario.getPopulation());
        freightOnly.setInclPop(false);

        //everything besides freight histogram
        final LegHistogram withoutFreight = new LegHistogram(900);
        withoutFreight.setPopulation(scenario.getPopulation());

        CarrierScoreStats carrierScoreStats = new CarrierScoreStats(carriers, "output/carrier_scores", true);

        eventsManager.addHandler(freightOnly);
        eventsManager.addHandler(withoutFreight);
        controler.addControlerListener(carrierScoreStats);
        controler.addControlerListener((IterationEndsListener) event -> {
            String dir = event.getServices().getControlerIO().getOutputPath() + "/";

            //write stats
            freightOnly.writeGraphic(dir + "legHistogram_freight.png");
            freightOnly.reset(event.getIteration());

            withoutFreight.writeGraphic(dir + "legHistogram_withoutFreight.png");
            withoutFreight.reset(event.getIteration());
        });
    }

}