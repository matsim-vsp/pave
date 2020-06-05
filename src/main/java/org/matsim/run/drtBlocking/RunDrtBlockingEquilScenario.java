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

package org.matsim.run.drtBlocking;

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
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.controler.Controler;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.drtBlockings.DrtBlockingModule;

import javax.management.InvalidAttributeValueException;
import java.io.File;

public class RunDrtBlockingEquilScenario {

    public static void main(String[] args) {

        Config config = ConfigUtils.loadConfig("scenarios/equil/config.xml");
        config.controler().setOutputDirectory("./output/equil/drtBlocking/");
        config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.overwriteExistingFiles);
        config.controler().setLastIteration(0);

        config.qsim().setEndTime(30*3600);


        DrtConfigGroup drtCfg = prepareDrtConfig(config);

        FreightConfigGroup freightCfg = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
        freightCfg.setCarriersFile("equil_carriers_empty.xml");
        freightCfg.setCarriersVehicleTypesFile("equil_carrierVehicleTypes.xml");

        Scenario scenario = ScenarioUtils.loadScenario(config);
        FreightUtils.loadCarriersAccordingToFreightConfig(scenario);


        try {
            FreightUtils.runJsprit(scenario, freightCfg);
            new File(config.controler().getOutputDirectory()).mkdirs();
            new CarrierPlanXmlWriterV2(FreightUtils.getCarriers(scenario)).write(config.controler().getOutputDirectory() + "carriers_planned.xml");
        } catch (/*InvalidAttributeValueException*/ Exception e) {
            e.printStackTrace();
        }

        setupPopulation(scenario);

        Controler controler = new Controler(scenario);
        configureControler(drtCfg, controler);


        controler.run();
    }

    private static DrtConfigGroup prepareDrtConfig(Config config) {
        ConfigUtils.addOrGetModule(config, DvrpConfigGroup.class);

        MultiModeDrtConfigGroup multiModeDrtConfigGroup = ConfigUtils.addOrGetModule(config, MultiModeDrtConfigGroup.class);

        DrtConfigGroup drtCfg = new DrtConfigGroup();//DrtConfigGroup.getSingleModeDrtConfig(config);
        drtCfg.setMode(TransportMode.drt);
        drtCfg.setMaxWaitTime(2 * 3600);
        drtCfg.setMaxTravelTimeAlpha(1);
        drtCfg.setMaxTravelTimeBeta(15 * 60);
        drtCfg.setStopDuration(60);
        drtCfg.setEstimatedDrtSpeed(27.78);
        drtCfg.setVehiclesFile("drtVehicles.xml");

        drtCfg.setRejectRequestIfMaxWaitOrTravelTimeViolated(false);

        config.qsim().setSimStarttimeInterpretation(QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime);

        multiModeDrtConfigGroup.addParameterSet(drtCfg);

        return drtCfg;
    }

    private static void configureControler(DrtConfigGroup drtCfg, Controler controler) {
        controler.addOverridingModule( new DvrpModule() ) ;
        controler.addOverridingModule( new DrtModeModule(drtCfg) ) ;
        controler.addOverridingModule( new DrtBlockingModule(drtCfg));
        controler.configureQSimComponents( DvrpQSimComponents.activateModes( TransportMode.drt ) ) ;
    }


    private static void setupPopulation(Scenario scenario){
        scenario.getPopulation()
                .getFactory()
                .getRouteFactories()
                .setRouteFactory(DrtRoute.class, new DrtRouteFactory());
        
        //delete the old population
        scenario.getPopulation().getPersons().clear();

        Id<Link> homeLink = Id.createLinkId(1);
        Id<Link> workLink = Id.createLinkId(22);

        PopulationFactory popFactory = scenario.getPopulation().getFactory();

        for (int i = 1; i <= 48; i++) {
            Person person = popFactory.createPerson(Id.createPersonId("person_" + i));
            Plan plan = popFactory.createPlan();

            Activity home = popFactory.createActivityFromLinkId("h", homeLink);
            home.setEndTime(i * 1800);
            plan.addActivity(home);

            plan.addLeg(popFactory.createLeg("drt"));

            Activity work = popFactory.createActivityFromLinkId("w", workLink);
            plan.addActivity(work);

            person.addPlan(plan);
            scenario.getPopulation().addPerson(person);
        }

    }

}
