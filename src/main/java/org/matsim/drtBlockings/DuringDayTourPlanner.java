package org.matsim.drtBlockings;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierService;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.carrier.Tour;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.mobsim.framework.events.MobsimAfterSimStepEvent;
import org.matsim.core.mobsim.framework.listeners.MobsimAfterSimStepListener;
import org.matsim.drtBlockings.events.DrtBlockingRequestScheduledEvent;
import org.matsim.drtBlockings.events.DrtBlockingRequestScheduledEventHandler;
import org.matsim.drtBlockings.events.DrtBlockingRequestSubmittedEvent;
import org.matsim.drtBlockings.events.DrtBlockingRequestSubmittedEventHandler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

//TODO: Let DefaultBLockingOptimizer use this class!

public class DuringDayTourPlanner implements MobsimAfterSimStepListener, DrtBlockingRequestSubmittedEventHandler, DrtBlockingRequestScheduledEventHandler {
    //When do we want to replan our rejected tours?
    Double tourPlanningTime = 60.0 * 60 * 14;

    Map<Id<Request>, TourData> submittedTours;
    Map<Id<Request>, List<Task>> tasks;
    Map<Id<Request>, Id<Carrier>> carriers;
    Map<Id<Request>, Id<CarrierService>> services;
//    Map<Id<Request>, Map<Id<CarrierService>, CarrierService>> services;


    List<Id<Request>> rejectedTours;

    @Override
    public void notifyMobsimAfterSimStep(MobsimAfterSimStepEvent event) {
        if (event.getSimulationTime() == tourPlanningTime) {
            //Here we need to insert the scenario from running Mobsim TODO:HOW?
            Scenario scenario;
            Config config = scenario.getConfig();
            FreightConfigGroup freightCfg = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
            Carriers carriers = FreightUtils.getCarriers(scenario);
            Id<Carrier> test = Id.create(123, Carrier.class);
            carriers.getCarriers().get(test).getServices();

            //Hier services vergleichen?
            //Oder einfach "hart" neu planen?


            for(TourData data : this.submittedTours.values()) {
                for(Carrier carrier : FreightUtils.getCarriers(scenario).getCarriers().values()) {
                    if(data.carrierId == carrier.getId()) {
                        for(Id<CarrierService> serviceId : carrier.getServices().keySet()) {
                            if(data.serviceId == serviceId) {
                                try {
                                    FreightUtils.runJsprit(scenario, freightCfg);
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                }
            }

        }
    }

    public void blockingRequestSubmitted(DrtBlockingRequest request, Tour.ServiceActivity serviceActivity) {
        this.tasks.putIfAbsent(request.getId(), request.getTasks());
        this.carriers.putIfAbsent(request.getId(), request.getCarrierId());
        this.services.putIfAbsent(request.getId(), serviceActivity.getService().getId());

    }

    @Override
    public void handleEvent(DrtBlockingRequestScheduledEvent event) {
        Id<Request> requestId = event.getRequestId();
        this.submittedTours.remove(requestId);
    }

    @Override
    public void handleEvent(DrtBlockingRequestSubmittedEvent event) {
        Id<Request> requestId = event.getRequestId();
        TourData data = new TourData(requestId);
        data.tasks = this.tasks.get(requestId);
        data.carrierId = this.carriers.get(requestId);
        data.serviceId = this.services.get(requestId);

        this.submittedTours.putIfAbsent(requestId, data);
    }
    //TODO: This data class may be unnecessary, when I started coding the Planner I thought there will be many aspects of the carriers, requests etc we are using
    private class TourData {
        private final Id<Request> requestId;
        private List<Task> tasks;
        private Id<Carrier> carrierId;
        private Id<CarrierService> serviceId;

        private TourData(Id<Request> requestId) {
            this.requestId = requestId;
        }
    }
}
