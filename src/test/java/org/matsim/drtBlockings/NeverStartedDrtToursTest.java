package org.matsim.drtBlockings;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.events.ActivityEndEvent;
import org.matsim.api.core.v01.events.handler.ActivityEndEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.contrib.drt.optimizer.insertion.ExtensiveInsertionSearchParams;
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
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.TimeWindow;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.drtBlockings.analysis.BasicTourStatsAnalysis;
import org.matsim.drtBlockings.events.*;

import javax.management.InvalidAttributeValueException;
import java.beans.EventHandler;
import java.util.*;
import java.util.logging.Logger;

public class NeverStartedDrtToursTest {

    static Logger logger = Logger.getLogger(DrtBlockingTest.class.getName());

//    private static DrtBlockingTest.DrtBlockingDispatchLogicHandler dispatchLogicHandler;
    private static DrtBlockingTimeLogicHandler timeLogicHandler;

    @BeforeClass
    public static final void runChessboardTestScenario() throws InvalidAttributeValueException {
//        dispatchLogicHandler = new DrtBlockingTest.DrtBlockingDispatchLogicHandler();
        timeLogicHandler = new DrtBlockingTimeLogicHandler();

        Controler controler = NeverStartedDrtToursTestControlerCreator.createControlerForChessboardTest(Arrays.asList(timeLogicHandler));
        BasicTourStatsAnalysis basicTourStatsAnalysis = new BasicTourStatsAnalysis(controler.getScenario().getNetwork());


        controler.addOverridingModule(new AbstractModule() {
            @Override
            public void install() {
                addEventHandlerBinding().toInstance(basicTourStatsAnalysis);
            }
        });

        controler.run();
//        basicTourStatsAnalysis.writeStats("/testBasicTourStats.csv");
    }

//    @Test
//    public final void testBlockingDispatchLogic() {
//
//        for(String s: dispatchLogicHandler.errors){
//            logger.severe(s);
//        }
//        Assert.assertTrue("something went wrong while DrtBlocking dispatch. See messages above.", dispatchLogicHandler.errors.isEmpty());
//
//    }

    @Test
    public final void testDrtBlockingTime(){
        for(String s: timeLogicHandler.errors){
            logger.severe(s);
        }
        Assert.assertTrue("Something is wrong with the drt blocking chronology. See messages above.", timeLogicHandler.errors.isEmpty());
    }



//    static class DrtBlockingDispatchLogicHandler implements DrtBlockingRequestRejectedEventHandler, DrtBlockingRequestScheduledEventHandler, DrtBlockingEndedEventHandler,
//            PassengerRequestRejectedEventHandler, PassengerRequestScheduledEventHandler {
//
//
//        private Set<Id<DvrpVehicle>> startedBlockings = new HashSet<>();
//        private List<String> errors = new ArrayList<>();
//
//
//        @Override
//        public void handleEvent(DrtBlockingEndedEvent event) {
//            if(! (this.startedBlockings.remove(event.getVehicleId()))){
//                this.errors.add("the DrtBlocking of vehicle " + event.getVehicleId() + " just ended - but has never begun.");
//            }
//        }
//
//        @Override
//        public void handleEvent(DrtBlockingRequestRejectedEvent event) {
//            this.errors.add("there should be no rejection of drt blockings in this test. \n Event: " + event);
//        }
//
//        @Override
//        public void handleEvent(DrtBlockingRequestScheduledEvent event) {
//            this.startedBlockings.add(event.getVehicleId());
//        }
//
//        @Override
//        public void handleEvent(PassengerRequestRejectedEvent event) {
//            this.errors.add("there should be no rejection of passenger requests in this test. \n Event: " + event);
//        }
//
//        @Override
//        public void handleEvent(PassengerRequestScheduledEvent event) {
//            if(this.startedBlockings.contains(event.getVehicleId())){
//                this.errors.add("vehicle " + event.getVehicleId() + " just got assigned to a passenger but it's DrtBlocking has not ended yet.");
//            }
//        }
//
//    }

    static class DrtBlockingTimeLogicHandler implements DrtBlockingEndedEventHandler, ActivityEndEventHandler {

        private List<String> errors = new ArrayList<>();
        private Map<String, Double> retoolEndTimes = new HashMap();

        //time windows from the services in the carriers file
        private Queue<TimeWindow> deliveryTimeWindows = new LinkedList<>();

        //this value refers to serviceDurations in the carriers file
        private double serviceDuration = 5*60;

        DrtBlockingTimeLogicHandler(){
            this.deliveryTimeWindows.add(TimeWindow.newInstance(6*3600,24*3600));
            this.deliveryTimeWindows.add(TimeWindow.newInstance(9*3600,24*3600));
            this.deliveryTimeWindows.add(TimeWindow.newInstance(12*3600,24*3600));
            this.deliveryTimeWindows.add(TimeWindow.newInstance(15*3600,24*3600));
        }

        @Override
        public void handleEvent(DrtBlockingEndedEvent event) {
            if(! this.retoolEndTimes.containsKey(event.getVehicleId().toString())){
                this.errors.add("the DrtBlocking of vehicle " + event.getVehicleId() + " just ended - but no corresponding retool activity was recorded..");
                return;
            }
            double retoolEndTime = this.retoolEndTimes.remove(event.getVehicleId().toString());
            if(retoolEndTime != event.getTime()){
                this.errors.add("DrtBlockingEnd time is supposed to be equal to the end time of the corresponding retool activity - but is not for vehicle " + event.getVehicleId() +
                        "\n blocking end time = " + event.getTime() +
                        "\n retool end time = " + retoolEndTime);
            }
        }

        @Override
        public void handleEvent(ActivityEndEvent event) {
            if(event.getActType().equals("FreightDrtRetooling")){
                this.retoolEndTimes.put(event.getPersonId().toString(), event.getTime());
            } else if( event.getActType().equals("FreightDrtDelivery")){
                //test TimeWindow enforcement

                TimeWindow timeWindow = this.deliveryTimeWindows.poll();
                if (event.getTime() < timeWindow.getStart() + serviceDuration){
                    this.errors.add("delivery had started before it's TimeWindow has started..  TimeWindow=" + timeWindow);
                } else if(event.getTime() > timeWindow.getEnd() + serviceDuration){
                    this.errors.add("delivery had started too late (after time window had ended)..  TimeWindow=" + timeWindow);
                }
            }
        }
    }
}
