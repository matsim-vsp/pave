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

package org.matsim.pfav.privateAV;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierCapabilities;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlReader;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsManagerImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreightTourDispatchTest {

    private static String OUTPUT;

    @BeforeClass
    public static void runChessboardScenario() {
        RunChessboardScenarioForTest testRunner =
                new RunChessboardScenarioForTest(FreightTourDispatchTest.class, 2, CarrierCapabilities.FleetSize.INFINITE);
        OUTPUT = testRunner.getOutputDir();
        testRunner.run();
    }
    
    @Test
    public final void testFreightTourDispatch() {
        String eventsFile = OUTPUT + "output_events.xml.gz";
        String carriersFile = OUTPUT + "ITERS/it.1/carriers_it1.xml";
        EventsManager events = new EventsManagerImpl();

        PFAVRequestHandler eventHandler = new PFAVRequestHandler();
        events.addHandler(eventHandler);
        PFAVEventsReader reader = new PFAVEventsReader(events);
        reader.readFile(eventsFile);

        //test count of events
        Assert.assertEquals("there should be three requestDeniedEvents: one at the end of tour 1 (at depot A), one at the end of tour 2 (at depot B) and one after retooling after tour 2 (at all depots) BUT none after last passenger dropoff, because this is after 6.p.m.", 3, eventHandler.deniedEvents.size());
        Assert.assertEquals("there should be two FreightTourScheduledEvents", 2, eventHandler.scheduledEvents.size());
        Assert.assertEquals("there should be two FreightToursCompletedEvents", 2, eventHandler.completedEvents.size());

        Carriers carriers = new Carriers();
        CarrierPlanXmlReader carrierReader = new CarrierPlanXmlReader(carriers);
        carrierReader.readFile(carriersFile);

        int tourCount = 0;
        for (Carrier carrier : carriers.getCarriers().values()) {
            tourCount += carrier.getSelectedPlan().getScheduledTours().size();
        }
        Assert.assertTrue("the number of tours to be driven should be greater or equal to the number of tours dispatched", tourCount >= eventHandler.scheduledEvents.size());
        Assert.assertTrue("the number of tours to be driven should be greater or equal to the number of tours completed", tourCount >= eventHandler.completedEvents.size());

        for (EventFreightTourScheduled scheduledEvent : eventHandler.scheduledEvents) {
            String vehId = scheduledEvent.getAttributes().get(EventFreightTourScheduled.ATTRIBUTE_VEHICLE);
            if (!eventHandler.veh2dropOffs.containsKey(vehId)) {
                Assert.fail("vehicle " + vehId + " got assigned a freight tour but did not drop off a passenger at all");
            }
            Assert.assertTrue("no passenger dropOff found before freight tour assignment at time= " + scheduledEvent.getTime() + " for vehicle " + vehId,
                    hasHadDropOffBefore(scheduledEvent.getTime(), eventHandler.veh2dropOffs.get(vehId)));
        }

        int dropOffCount = 0;
        for (String vehicle : eventHandler.veh2dropOffs.keySet()) {
            dropOffCount += eventHandler.veh2dropOffs.get(vehicle).size();
        }


    }


    private boolean hasHadDropOffBefore(double freightTourScheduledEventTime, List<ActivityStartEvent> activityStartEvents) {
        for (ActivityStartEvent event : activityStartEvents) {
            if (event.getTime() <= freightTourScheduledEventTime) return true;
        }
        return false;
    }

    /**
     * event handler for FreightTourDispatchTest.class
     */
    class PFAVRequestHandler implements FreightTourRequestEventHandler, ActivityStartEventHandler {
        List<EventFreightTourScheduled> scheduledEvents = new ArrayList<>();
        List<EventFreightTourCompleted> completedEvents = new ArrayList<>();
        List<EventFreightTourRequestRejected> deniedEvents = new ArrayList<>();
        Map<String, List<ActivityStartEvent>> veh2dropOffs = new HashMap<>();

        @Override
        public void handleEvent(EventFreightTourScheduled event) {
            this.scheduledEvents.add(event);
        }

        @Override
        public void handleEvent(EventFreightTourRequestRejected event) {
            this.deniedEvents.add(event);
        }

        @Override
        public void handleEvent(EventFreightTourCompleted event) {
            this.completedEvents.add(event);
        }

        @Override
        public void handleEvent(ActivityStartEvent activityStartEvent) {
            switch (activityStartEvent.getActType()) {
                case PFAVActionCreator.DROPOFF_ACTIVITY_TYPE:
                    if (this.veh2dropOffs.containsKey(activityStartEvent.getPersonId().toString())) {
                        this.veh2dropOffs.get(activityStartEvent.getPersonId().toString()).add(activityStartEvent);
                    } else {
                        List<ActivityStartEvent> list = new ArrayList<>();
                        list.add(activityStartEvent);
                        this.veh2dropOffs.put(activityStartEvent.getPersonId().toString(), list);
                        break;
                    }
                default:
                    break;
            }
        }
    }
}
