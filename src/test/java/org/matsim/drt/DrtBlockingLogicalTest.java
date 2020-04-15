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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.ActivityEndEvent;
import org.matsim.api.core.v01.events.handler.ActivityEndEventHandler;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.passenger.PassengerRequestRejectedEvent;
import org.matsim.contrib.dvrp.passenger.PassengerRequestRejectedEventHandler;
import org.matsim.contrib.dvrp.passenger.PassengerRequestScheduledEvent;
import org.matsim.contrib.dvrp.passenger.PassengerRequestScheduledEventHandler;
import org.matsim.core.controler.Controler;
import org.matsim.drt.events.*;

import javax.management.InvalidAttributeValueException;
import java.util.*;
import java.util.logging.Logger;

/**
 * TODO test more functionality. For instance: which vehicle is assigned?
 */
public class DrtBlockingLogicalTest {

    static Logger logger = Logger.getLogger(DrtBlockingLogicalTest.class.getName());

    private static DrtBlockingDispatchLogicHandler dispatchLogicHandler;
    private static DrtBlockingTimeLogicHandler timeLogicHandler;

    @BeforeClass
    public static final void runChessboardTestScenario() throws InvalidAttributeValueException {
        dispatchLogicHandler = new DrtBlockingDispatchLogicHandler();
        timeLogicHandler = new DrtBlockingTimeLogicHandler();
        Controler controler = DrtBlockingTestControlerCreator.createControlerForChessboardTest(Arrays.asList(dispatchLogicHandler, timeLogicHandler));
        controler.run();
    }

    @Test
    public final void testBlockingDispatchLogic() {

        for(String s: dispatchLogicHandler.errors){
            logger.severe(s);
        }
        Assert.assertTrue("something went wrong while DrtBlocking dispatch. See messages above.", dispatchLogicHandler.errors.isEmpty());

    }

    @Test
    public final void testDrtBlockingTime(){
        for(String s: timeLogicHandler.errors){
            logger.severe(s);
        }
        Assert.assertTrue("Something is wrong with the drt blocking chronology. See messages above.", timeLogicHandler.errors.isEmpty());
    }



    static class DrtBlockingDispatchLogicHandler implements DrtBlockingRequestRejectedEventHandler, DrtBlockingRequestScheduledEventHandler, DrtBlockingEndedEventHandler,
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

    static class DrtBlockingTimeLogicHandler implements DrtBlockingEndedEventHandler, ActivityEndEventHandler {

        private List<String> errors = new ArrayList<>();
        private Map<String, Double> retoolEndTimes = new HashMap();

        @Override
        public void handleEvent(DrtBlockingEndedEvent event) {
            if(! this.retoolEndTimes.containsKey(event.getVehicleId().toString())){
                this.errors.add("the DrtBlocking of vehicle " + event.getVehicleId() + " just ended - but no corresponding retool activity was recorded..");
            }
            double retoolEndTime = this.retoolEndTimes.remove(event.getVehicleId().toString());
            if(retoolEndTime != event.getTime()){
                this.errors.add("DrtBlockingEnd time is supposed to be equal to end the time of the corresponding retool activity - but is not for vehicle " + event.getVehicleId() +
                        "\n blocking end time = " + event.getTime() +
                        "\n retool end time = " + retoolEndTime);
            }
        }

        @Override
        public void handleEvent(ActivityEndEvent event) {
            if(event.getActType().equals("FreightDrtRetooling")){
                this.retoolEndTimes.put(event.getPersonId().toString(), event.getTime());
            }
        }
    }

}
