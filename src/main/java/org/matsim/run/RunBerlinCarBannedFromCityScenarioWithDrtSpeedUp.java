/* *********************************************************************** *
 * project: org.matsim.*
 * Controler.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.run;

import org.apache.log4j.Logger;
import org.matsim.accessEgress2CarByDRT.DRTAccessWalkEgress2CarModule;
import org.matsim.accessEgress2CarByDRT.WalkAccessDRTEgress2CarModule;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.router.AnalysisMainModeIdentifier;
import org.matsim.core.utils.collections.Tuple;
import org.matsim.optDRT.MultiModeOptDrtConfigGroup;
import org.matsim.optDRT.OptDrt;
import org.matsim.run.drt.RunDrtOpenBerlinScenario;
import org.matsim.vehicles.Vehicle;
import org.matsim.vehicles.VehicleUtils;

import java.util.HashMap;
import java.util.Map;

public class RunBerlinCarBannedFromCityScenarioWithDrtSpeedUp {


    private static final Logger log = Logger.getLogger(RunBerlinCarBannedFromCityScenarioWithDrtSpeedUp.class );

//    private static final String BERLIN_V5_5_CONFIG = "scenarios/berlin-v5.5-1pct/input/drt/berlin-drt-v5.5-1pct.config.xml";
    private static final String BERLIN_V5_5_CONFIG = "scenarios/berlin-v5.5-1pct/input/drt/berlin-drt-v5.5-1pct.config_banCarTest.xml";


    static final String WALK_ACCESS_DRT_EGRESS_MODE = "walkCarDrt";
    static final String DRT_ACCESS_DRT_WALK_MODE = "drtCarWalk";

    /**
     * @param args array containing program arguments in the following order: global sensitivity factor for mobility types (as numeric argument), path to config, custom modules to load
     */
    public static void main(String[] args) {

        for (String arg : args) {
            log.info( arg );
        }

        String[] configArgs;
        if ( args.length==0 ) {
            configArgs = new String[]{BERLIN_V5_5_CONFIG ,"--config:controler.outputDirectory", "output/berlin5.5_1pct/bannedCarFromCity/testNewNet-sevIters"};
        } else {
            configArgs = args;
        }

        //prepare config
        Config config = RunDrtOpenBerlinScenario.prepareConfig(configArgs);

//        config.plans().setInputFile("pave509.output_plans.xml.gz");
        config.controler().setLastIteration(1);
//        config.controler().setOutputDirectory("D:/bannedCarStudy/output/pave509-outputAsInput-Test-Debug-deletedRoutes");

//        MultiModeDrtSpeedUpModule.addTeleportedDrtMode(config);

        //this will throw an exception if more than one drt mode is configured... multiple drt operators are currently not supported with car banned scenario..
        DrtConfigGroup drtConfigGroup = DrtConfigGroup.getSingleModeDrtConfig(config);

        //prepare car banned specific parameter settings
        CarBannedScenarioPreparation.prepareConfig(config,drtConfigGroup, new Tuple<>(WALK_ACCESS_DRT_EGRESS_MODE, DRT_ACCESS_DRT_WALK_MODE));

        //prepare scenario
        Scenario scenario = RunDrtOpenBerlinScenario.prepareScenario(config);

//        PopulationUtils.sampleDown(scenario.getPopulation(), 0.1);

//        scenario.getPopulation().getPersons().values().stream()
//                .flatMap(person -> person.getPlans().stream())
//                .flatMap(plan -> TripStructureUtils.getLegs(plan).stream())
//                .forEach(leg -> leg.setRoute(null));

//        ban car from drt service area -- be aware that this will not create a transfer zone (where both car and drt are allowed)
//        CarBannedScenarioPreparation.banCarFromDRTServiceArea(scenario, drtConfigGroup);

        { //this is for open berlin scenario in pave context only! //TODO
            RunDrtOpenBerlinScenario.addDRTmode(scenario, drtConfigGroup.getMode(), drtConfigGroup.getDrtServiceAreaShapeFile());
            CarBannedScenarioPreparation.banCarFromLinkInsideBerlin(scenario.getNetwork());
        }


        //insert vehicles for new modes
        configureVehicleIdsForNewModes(scenario);

        //prepare controler
        Controler controler = RunDrtOpenBerlinScenario.prepareControler(scenario);
//        controler.addOverridingModule(new MultiModeDrtSpeedUpModule());
        OptDrt.addAsOverridingModule(controler, ConfigUtils.addOrGetModule(scenario.getConfig(), MultiModeOptDrtConfigGroup.class));

        //configure intermodal routing modules for new modes
        //and bind custom main mode indentifier that can handle new modes
        controler.addOverridingModule(new AbstractModule() {
            @Override
            public void install() {
                install(new WalkAccessDRTEgress2CarModule(WALK_ACCESS_DRT_EGRESS_MODE, drtConfigGroup));
                install(new DRTAccessWalkEgress2CarModule(DRT_ACCESS_DRT_WALK_MODE, drtConfigGroup));
//                bind(MainModeIdentifier.class).to(OpenBerlinCarBannedFromCityAnalysisMainModeIdentifier.class);
                bind(AnalysisMainModeIdentifier.class).to(OpenBerlinCarBannedFromCityAnalysisMainModeIdentifier.class);
            }
        });

        //run simulation
        controler.run();

        //run analysis
//        RunBerlinScenario.runAnalysis(controler);
    }

    private static void configureVehicleIdsForNewModes(Scenario scenario) {
        //add mode vehicle id's
        scenario.getPopulation().getPersons().values().parallelStream().forEach(person -> {
            Map<String, Id<Vehicle>> vehicleIdMap = new HashMap<>();
            Id<Vehicle> vehicleId = Id.createVehicleId(person.getId().toString());
            vehicleIdMap.put(WALK_ACCESS_DRT_EGRESS_MODE, vehicleId);
            vehicleIdMap.put(DRT_ACCESS_DRT_WALK_MODE, vehicleId);
            VehicleUtils.insertVehicleIdsIntoAttributes(person, vehicleIdMap);
        });
    }

}
