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

import org.junit.Assert;
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
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.passenger.PassengerRequestRejectedEvent;
import org.matsim.contrib.dvrp.passenger.PassengerRequestRejectedEventHandler;
import org.matsim.contrib.dvrp.passenger.PassengerRequestScheduledEvent;
import org.matsim.contrib.dvrp.passenger.PassengerRequestScheduledEventHandler;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlWriterV2;
import org.matsim.contrib.freight.carrier.TimeWindow;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.events.handler.EventHandler;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.drt.events.*;

import javax.management.InvalidAttributeValueException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class DrtBlockingChessboardTest {

    static Logger logger = Logger.getLogger(DrtBlockingChessboardTest.class.getName());

    @Test
    public final void testDefaultBlockingOptimizer() throws InvalidAttributeValueException {

        String input = "chessboard/";
        Config config = ConfigUtils.loadConfig(input + "chessboard_drtBlocking_config.xml");
        config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.overwriteExistingFiles);

        FreightConfigGroup freightCfg = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
        freightCfg.setCarriersFile("chessboard_carriers_drtBlocking.xml");
        freightCfg.setCarriersVehicleTypesFile("chessboard_vehicleTypes.xml");

        DrtConfigGroup drtCfg = prepareDrtConfig(config);

        Scenario scenario = ScenarioUtils.loadScenario(config);

        FreightUtils.loadCarriersAccordingToFreightConfig(scenario);

        FreightUtils.runJsprit(scenario, freightCfg);
        new CarrierPlanXmlWriterV2(FreightUtils.getCarriers(scenario)).write(config.controler().getOutputDirectory() + "carriers_planned.xml");

        setupPopulation(scenario);

        Controler controler = new Controler(scenario);

        DrtBlockingDispatchEventHandler handler = new DrtBlockingDispatchEventHandler();
        configureControler(drtCfg, controler, handler);
        controler.run();

        for(String s: handler.errors){
            logger.severe(s);
        }
        Assert.assertTrue("something went wrong while DrtBlocking dispatch. See messages above.", handler.errors.isEmpty());

    }

    private void configureControler(DrtConfigGroup drtCfg, Controler controler, EventHandler handler) {
        controler.addOverridingModule( new DvrpModule() ) ;
        controler.addOverridingModule( new DrtModeModule(drtCfg) ) ;
        controler.addOverridingModule( new DrtBlockingModule(drtCfg));
        controler.addOverridingModule(new AbstractModule() {
            @Override
            public void install() {
                addEventHandlerBinding().toInstance(handler);
            }
        });
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
        Id<Link> rightLink = Id.createLinkId(9);

        PopulationFactory popFactory = scenario.getPopulation().getFactory();

        for (int i = 1; i <= 86; i++) {
            Person person = createPersons(leftLink, rightLink, popFactory, "horizontalTraveller_", i);
            scenario.getPopulation().addPerson(person);
        }


        //verticalTravellers will only travel from leftLink to rightLink
        leftLink = Id.createLinkId(12);
        rightLink = Id.createLinkId(9);

        for (int i = 18; i <= 72; i+=18) { //18,36,54,72  => 7.00h ; 10.00h, 13.00h, 16.00h
            Person person = createPersons(leftLink, rightLink, popFactory, "verticalTraveller_", i);
            scenario.getPopulation().addPerson(person);
        }

    }

    private Person createPersons(Id<Link> leftLink, Id<Link> rightLink, PopulationFactory popFactory, String idString, int i) {
        Person person = popFactory.createPerson(Id.createPersonId(idString + i));
        Plan plan = popFactory.createPlan();
        Activity act1;
        Activity act2;
        if (i % 2 == 0) {
            act1 = popFactory.createActivityFromLinkId("home", leftLink);
        } else {
            act1 = popFactory.createActivityFromLinkId("home", rightLink);
        }
        act1.setEndTime(4 * 3600 + i * 10 * 60);
        plan.addActivity(act1);

        plan.addLeg(popFactory.createLeg("drt"));

        if (i % 2 == 0) {
            act2 = popFactory.createActivityFromLinkId("work", rightLink);
        } else {
            act2 = popFactory.createActivityFromLinkId("work", leftLink);
        }

        plan.addActivity(act2);

        person.addPlan(plan);
        return person;
    }

    private static DrtConfigGroup prepareDrtConfig(Config config) {
        ConfigUtils.addOrGetModule(config, DvrpConfigGroup.class);

        MultiModeDrtConfigGroup multiModeDrtConfigGroup = ConfigUtils.addOrGetModule(config, MultiModeDrtConfigGroup.class);

        DrtConfigGroup drtCfg = new DrtConfigGroup();//DrtConfigGroup.getSingleModeDrtConfig(config);
        drtCfg.setMode(TransportMode.drt);
        drtCfg.setMaxWaitTime(30 * 60);
        drtCfg.setMaxTravelTimeAlpha(1);
        drtCfg.setMaxTravelTimeBeta(15 * 60);
        drtCfg.setStopDuration(60);
        drtCfg.setEstimatedDrtSpeed(10);
        drtCfg.setVehiclesFile("drtBlockingVehicles.xml");

        config.qsim().setSimStarttimeInterpretation(QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime);

        multiModeDrtConfigGroup.addParameterSet(drtCfg);

        return drtCfg;
    }


    class DrtBlockingDispatchEventHandler implements DrtBlockingRequestRejectedEventHandler, DrtBlockingRequestScheduledEventHandler, DrtBlockingEndedEventHandler,
            PassengerRequestRejectedEventHandler, PassengerRequestScheduledEventHandler {


        private Set<Id<DvrpVehicle>> startedBlockings = new HashSet<>();
        private List<String> errors = new ArrayList<>();


        @Override
        public void handleEvent(DrtBlockingEndedEvent event) {
            if(! (this.startedBlockings.remove(event.getVehicleId()))){
                this.errors.add("the DrtBlocking of vehicle " + event.getVehicleId() + " just ended - but has never begun.");
            }
        }

        @Override
        public void handleEvent(DrtBlockingRequestRejectedEvent event) {
            this.errors.add("there should be no rejection of drt blockings in this test. \n Event: " + event);
        }

        @Override
        public void handleEvent(DrtBlockingRequestScheduledEvent event) {
            this.startedBlockings.add(event.getVehicleId());
        }

        @Override
        public void handleEvent(PassengerRequestRejectedEvent event) {
            this.errors.add("there should be no rejection of passenger requests in this test. \n Event: " + event);
        }

        @Override
        public void handleEvent(PassengerRequestScheduledEvent event) {
            if(this.startedBlockings.contains(event.getVehicleId())){
                this.errors.add("vehicle " + event.getVehicleId() + " just got assigned to a passenger but it's DrtBlocking has not ended yet.");
            }
        }

    }
}
