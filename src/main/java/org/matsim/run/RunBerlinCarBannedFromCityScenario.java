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
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.matsim.accessEgress2CarByDRT.DRTAccessWalkEgress2CarModule;
import org.matsim.accessEgress2CarByDRT.WalkAccessDRTEgress2CarModule;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.router.AnalysisMainModeIdentifier;
import org.matsim.core.utils.collections.Tuple;
import org.matsim.optDRT.MultiModeOptDrtConfigGroup;
import org.matsim.optDRT.OptDrt;
import org.matsim.run.drt.RunDrtOpenBerlinScenario;
import org.matsim.vehicles.Vehicle;
import org.matsim.vehicles.VehicleUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.matsim.utils.gis.shp2matsim.ShpGeometryUtils.loadPreparedGeometries;

public class RunBerlinCarBannedFromCityScenario {


    private static final Logger log = Logger.getLogger(RunBerlinCarBannedFromCityScenario.class );

    private static final String BERLIN_V5_5_CONFIG = "scenarios/berlin-v5.5-1pct/input/drt/berlin-drt-v5.5-1pct.config.xml";
//    private static final String BERLIN_V5_5_CONFIG = "scenarios/berlin-v5.5-1pct/input/drt/berlin-drt-v5.5-1pct.config.speedUp.xml";

//    private static final String BERLIN_V5_5_CONFIG = "D:/bannedCarStudy/berlin-drt-v5.5-10pct.config_pave508.xml";

//    private static final String BERLIN_V5_5_CONFIG = "scenarios/berlin-v5.5-1pct/input/drt/berlin-drt-v5.5-1pct.config_banCarTest.xml";
//    private static final String BERLIN_V5_5_CONFIG = "scenarios/berlin-v5.5-1pct/input/drt/berlin-drt-v5.5-1pct.config_banCarTest_speedUp.xml";

    static final String BERLIN_SHP_MINUS_500m_BUFFER = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/shp-files/S5/berlin-minus-500m-buffer.shp";
    static final String BERLIN_HUNDEKOPF_SHP_MINUS_100m_BUFFER = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/shp-files/S5/berlin-hundekopf-minus-100m.shp";


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
        String carFreeZoneShape;
        double buffer;
        if ( args.length==0 ) {

            //careful if you change this: further down, we set service area to hundekopf
            carFreeZoneShape = BERLIN_HUNDEKOPF_SHP_MINUS_100m_BUFFER;
            buffer = 100;
            configArgs = new String[]{BERLIN_V5_5_CONFIG ,"--config:controler.outputDirectory", "output/berlin5.5_1pct/bannedCarFromCity/pave508-trial"};
        } else {
            carFreeZoneShape = args[0];
            buffer = Double.parseDouble(args[1]);
            configArgs = new String[args.length-2];
            for(int i = 1; i < args.length; i++){
                configArgs[i-1] = args[i];
            }
        }

        //prepare config
        Config config = RunDrtOpenBerlinScenario.prepareConfig(configArgs);

//        config.plans().setInputFile("pave509.output_plans.xml.gz");
//        config.controler().setOutputDirectory("D:/bannedCarStudy/output/test-speedUp-replacementRideOnlyServiceArea");
//        config.controler().setLastIteration(1);
//        config.controler().setWriteTripsInterval(1);
//        config.controler().setWriteEventsInterval(1);

//        config.plans().setInputFile("D:/bannedCarStudy/berlin-v5.5-0.1pct-woRoutes.xml.gz");
//        config.transit().setUseTransit(true);
//        config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.overwriteExistingFiles);

//        MultiModeDrtSpeedUpModule.addTeleportedDrtMode(config);

        //this will throw an exception if more than one drt mode is configured... multiple drt operators are currently not supported with car banned scenario..
        DrtConfigGroup drtConfigGroup = DrtConfigGroup.getSingleModeDrtConfig(config);
        drtConfigGroup.setDrtServiceAreaShapeFile("https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/projects/pave/shp-files/berlin-planungsraum-hundekopf/berlin-hundekopf-based-on-planungsraum.shp");

        //prepare car banned specific parameter settings
        CarBannedScenarioPreparation.prepareConfig(config,drtConfigGroup, new Tuple<>(WALK_ACCESS_DRT_EGRESS_MODE, DRT_ACCESS_DRT_WALK_MODE));

        { //this is for open berlin scenario in pave context only!
            //we need a transfer zone because otherwise we might get rejections. we do this by allowing DRT in an area  slightly larger than its service area and then removing car and ride from links within carFreeZoneShape
            BerlinExperimentalConfigGroup berlinCfg = ConfigUtils.addOrGetModule(config, BerlinExperimentalConfigGroup.class);
            berlinCfg.setTagDrtLinksBufferAroundServiceAreaShp(buffer);
        }

        //prepare scenario
        Scenario scenario = RunDrtOpenBerlinScenario.prepareScenario(config);

//        ban car from drt service area -- be aware that this will not create a transfer zone (where both car and drt are allowed)
//        replace ride trips inside service area with single-leg car trips (which will then be routed with fallback mode which triggers mode choice)
//        CarBannedScenarioPreparation.banCarFromDRTServiceArea(scenario, drtConfigGroup, TransportMode.car);

        { //this is for open berlin scenario in pave context only!
            CarBannedScenarioPreparation.banCarAndRideFromArea(scenario.getNetwork(), carFreeZoneShape);
//        replace ride trips inside service area with single-leg car trips (which will then be routed with fallback mode which triggers mode choice)
            List<PreparedGeometry> serviceAreaGeoms = loadPreparedGeometries(drtConfigGroup.getDrtServiceAreaShapeFileURL(scenario.getConfig().getContext()));
            CarBannedScenarioPreparation.replaceRideTripsWithinGeomsWithSingleLegTripsOfMode(scenario.getPopulation(), TransportMode.car, serviceAreaGeoms); //TODO what is the best replacement mode for ride?
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
//                bind(MainModeIdentifier.class).to(OpenBerlinIntermodalMainModeIdentifier.class);
                bind(AnalysisMainModeIdentifier.class).to(OpenBerlinIntermodalMainModeIdentifier.class);
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
