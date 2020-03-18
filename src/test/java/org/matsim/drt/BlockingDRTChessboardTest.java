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

package org.matsim.drt;

import org.junit.Test;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.contrib.drt.routing.DrtRoute;
import org.matsim.contrib.drt.routing.DrtRouteFactory;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.run.DrtModeModule;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlWriterV2;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.scenario.ScenarioUtils;

import javax.management.InvalidAttributeValueException;

public class BlockingDRTChessboardTest {


    @Test
    public final void testDefaultBlockingOptimizer() throws InvalidAttributeValueException {

        String input = "chessboard/";
        Config config = ConfigUtils.loadConfig(input + "chessboard_drtBlocking_config.xml");
        config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.overwriteExistingFiles);

        DrtConfigGroup drtCfg = prepareDrtConfig(config);

        FreightConfigGroup freightCfg = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
        freightCfg.setCarriersFile("chessboard_carriers_drtBlocking.xml");
        freightCfg.setCarriersVehicleTypesFile("chessboard_vehicleTypes.xml");

        Scenario scenario = ScenarioUtils.loadScenario(config);

        FreightUtils.loadCarriersAccordingToFreightConfig(scenario);

        FreightUtils.runJsprit(scenario, freightCfg);
        new CarrierPlanXmlWriterV2(FreightUtils.getCarriers(scenario)).write(config.controler().getOutputDirectory() + "carriers_planned.xml");

        setupPopulation(scenario);

        Controler controler = new Controler(scenario);
        configureControler(drtCfg, controler);


        controler.run();

    }

    private void configureControler(DrtConfigGroup drtCfg, Controler controler) {
        controler.addOverridingModule( new DvrpModule() ) ;
        controler.addOverridingModule( new DrtModeModule(drtCfg) ) ;
        controler.addOverridingModule( new DrtBlockingModule(drtCfg));

        controler.configureQSimComponents( DvrpQSimComponents.activateModes( TransportMode.drt ) ) ;
    }

    private void setupPopulation(Scenario scenario) {
        scenario.getPopulation()
                .getFactory()
                .getRouteFactories()
                .setRouteFactory(DrtRoute.class, new DrtRouteFactory());

        //delete the old population
        scenario.getPopulation().getPersons().clear();

        Id<Link> leftLink = Id.createLinkId(1);
        Id<Link> rightLink = Id.createLinkId(5);

        PopulationFactory popFactory = scenario.getPopulation().getFactory();

        for (int i = 1; i <= 200; i++) {
            Person person = popFactory.createPerson(Id.createPersonId("person_" + i));
            Plan plan = popFactory.createPlan();
            Activity act1;
            Activity act2;
            if(i % 2 == 0){
                 act1 = popFactory.createActivityFromLinkId("home", leftLink);
            } else {
                act1 = popFactory.createActivityFromLinkId("home", rightLink);
            }
            act1.setEndTime(i * 5*60);
            plan.addActivity(act1);

            plan.addLeg(popFactory.createLeg("drt"));

            if(i % 2 == 0){
                act2 = popFactory.createActivityFromLinkId("work", rightLink);
            } else {
                act2 = popFactory.createActivityFromLinkId("work", leftLink);
            }

            plan.addActivity(act2);

            person.addPlan(plan);
            scenario.getPopulation().addPerson(person);
        }
    }

    private static DrtConfigGroup prepareDrtConfig(Config config) {
        ConfigUtils.addOrGetModule(config, DvrpConfigGroup.class);

        MultiModeDrtConfigGroup multiModeDrtConfigGroup = ConfigUtils.addOrGetModule(config, MultiModeDrtConfigGroup.class);

        DrtConfigGroup drtCfg = new DrtConfigGroup();//DrtConfigGroup.getSingleModeDrtConfig(config);
        drtCfg.setMode(TransportMode.drt);
        drtCfg.setMaxWaitTime(15 * 60);
        drtCfg.setMaxTravelTimeAlpha(1);
        drtCfg.setMaxTravelTimeBeta(15 * 60);
        drtCfg.setStopDuration(60);
        drtCfg.setEstimatedDrtSpeed(10);
        drtCfg.setVehiclesFile("drtBlockingVehicles.xml");

        config.qsim().setSimStarttimeInterpretation(QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime);

        multiModeDrtConfigGroup.addParameterSet(drtCfg);

        return drtCfg;
    }

}
