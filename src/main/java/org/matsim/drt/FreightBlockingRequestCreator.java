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

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelDataImpl;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.freight.carrier.CarrierUtils;
import org.matsim.contrib.freight.carrier.ScheduledTour;
import org.matsim.contrib.freight.carrier.Tour;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.contrib.taxi.schedule.TaxiEmptyDriveTask;
import org.matsim.core.config.Config;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.router.util.TravelTime;
import org.matsim.drt.tasks.FreightDeliveryTask;
import org.matsim.drt.tasks.FreightDriveTask;
import org.matsim.drt.tasks.FreightPickupTask;
import org.matsim.drt.tasks.FreightRetoolTask;
import org.matsim.pfav.privateAV.*;

import java.util.*;

class FreightBlockingRequestCreator implements BlockingRequestCreator {

    //TODO make this configurable
    private static double RETOOL_DURATION = 5*60;
    private static final double SUBMISSION_LOOK_AHEAD = 15*60;

    private final String mode;
    private final Network network;
    private final TravelTime travelTime;
    private final double qSimStartTime;
    private final Config config;

    public FreightBlockingRequestCreator(Network network, TravelTime travelTime, Config config) {
        this.network = network;
        this.travelTime = travelTime;
        this.mode = DrtConfigGroup.getSingleModeDrtConfig(config).getMode();
        this.qSimStartTime = Math.max(0, config.qsim().getStartTime());
        this.config = config;
    }
    
    @Override
    public Set<DrtBlockingRequest> createRequestsForIteration(Scenario scenario) {
        Set<DrtBlockingRequest> requests = new HashSet<>();

        FreightUtils.getCarriers(scenario).getCarriers().values().forEach(carrier -> {
            if(CarrierUtils.getCarrierMode(carrier).equals(mode)){
                carrier.getSelectedPlan().getScheduledTours().forEach(tour -> requests.add(createRequest(tour)));
            }
        });
        return requests;
    }

    private DrtBlockingRequest createRequest(ScheduledTour scheduledTour) {
        //TODO Id
        Id<Request> id = Id.create("blockingRequest_customer_" + scheduledTour.getVehicle().getId().toString(), Request.class);
        double blockingStart = determineStartOfBlocking(scheduledTour);
        List<Task> tourTasks = convertScheduledTour2DvrpTasks(scheduledTour, blockingStart);
        double blockingEnd = tourTasks.get(tourTasks.size() - 1).getEndTime();
        return new DrtBlockingRequest(id, Math.max(qSimStartTime, blockingStart - SUBMISSION_LOOK_AHEAD), blockingStart, blockingEnd, tourTasks);
    }


    /**
     * a problem is that JSprit calculates the departure for a ScheduledTour in a way that vehicles start as early as possible and
     * then wait at the location of the first delivery. We account for this by setting the earliest departure to 1.5*travelTime between depot
     * and first delivery.
     *
     * @param scheduledTour
     * @return
     */
    private double determineStartOfBlocking(ScheduledTour scheduledTour) {
        double vehicleEarliestStart = scheduledTour.getVehicle().getEarliestStartTime();
        double ttDepot2FirstDelivery = ((Tour.Leg) scheduledTour.getTour().getTourElements().get(0)).getExpectedTransportTime();
        double firstDeliveryEarliestStart =((Tour.TourActivity) scheduledTour.getTour().getTourElements().get(1)).getTimeWindow().getStart();

        double calculatedStart = firstDeliveryEarliestStart - ttDepot2FirstDelivery * 1.5 - RETOOL_DURATION;
//        double bufferFactor = config.plansCalcRoute().getTeleportedModeFreespeedFactors().get(TransportMode.ride);

        return Math.max(qSimStartTime, Math.max(vehicleEarliestStart, calculatedStart));
    }

    private List<Task> convertScheduledTour2DvrpTasks(ScheduledTour scheduledTour, double blockingStart) {
        List<Task> tourTasks = new ArrayList<>();
        double previousTaskEndTime = blockingStart + RETOOL_DURATION;
        tourTasks.add(new FreightRetoolTask(blockingStart, previousTaskEndTime, network.getLinks().get(scheduledTour.getTour().getStartLinkId())));

        List<Tour.TourElement> tourElements = scheduledTour.getTour().getTourElements();
        for (Tour.TourElement tourElement : tourElements) {
            if(tourElement instanceof Tour.Leg){
                NetworkRoute route = (NetworkRoute) ((Tour.Leg) tourElement).getRoute();
                VrpPathWithTravelData path;
                if (route.getStartLinkId().equals(route.getEndLinkId()))
                    path = VrpPaths.createZeroLengthPath(network.getLinks().get(route.getStartLinkId()), previousTaskEndTime);
                else {
                    path = createVrpPath(route, previousTaskEndTime);
                }
                tourTasks.add(new FreightDriveTask(path));
                previousTaskEndTime = path.getArrivalTime();
            } else {
                double currentTaskEndTime = previousTaskEndTime + ((Tour.TourActivity) tourElement).getDuration();
                if (tourElement instanceof Tour.ServiceActivity){
                    Tour.ServiceActivity serviceAct = (Tour.ServiceActivity) tourElement;
                    tourTasks.add(new FreightDeliveryTask(serviceAct, previousTaskEndTime, currentTaskEndTime,  network.getLinks().get(serviceAct.getLocation())));
                } else if(tourElement instanceof Tour.Pickup){
                    Tour.Pickup pickup = (Tour.Pickup) tourElement;
                    tourTasks.add(new FreightPickupTask(pickup, previousTaskEndTime, currentTaskEndTime, network.getLinks().get(pickup.getLocation())));
                } else if (tourElement instanceof Tour.Delivery){
                    Tour.Delivery delivery = (Tour.Delivery) tourElement;
                    tourTasks.add(new FreightDeliveryTask(delivery, previousTaskEndTime, currentTaskEndTime, network.getLinks().get(delivery.getLocation())));
                } else {
                    throw new RuntimeException();
                }
                previousTaskEndTime = currentTaskEndTime;
            }
        }
        tourTasks.add(new FreightRetoolTask(previousTaskEndTime, previousTaskEndTime + RETOOL_DURATION , network.getLinks().get(scheduledTour.getTour().getEndLinkId())));

        return tourTasks;
    }


    private VrpPathWithTravelDataImpl createVrpPath(NetworkRoute networkRoute, double departureTime) {
        int count = networkRoute.getLinkIds().size();

        Link[] links = new Link[count + 2];
        double[] linkTTs = new double[count + 2];
        links[0] = network.getLinks().get(networkRoute.getStartLinkId());
        double linkTT = 1.0D;
        linkTTs[0] = linkTT;
        double currentTime = departureTime + linkTT;

        for (int i = 0; i < count; ++i) {
            Link link = network.getLinks().get(networkRoute.getLinkIds().get(i));
            links[i + 1] = link;
            linkTT = travelTime.getLinkTravelTime(link, currentTime, null, null);
            linkTTs[i + 1] = linkTT;
            currentTime += linkTT;
        }
        links[count + 1] = network.getLinks().get(networkRoute.getEndLinkId());
        Link lastLink = network.getLinks().get(networkRoute.getEndLinkId());
        linkTT = Math.floor(lastLink.getLength() / lastLink.getFreespeed(currentTime));
        linkTTs[count + 1] = linkTT;
//        double totalTT = 1.0D + networkRoute.getTravelTime() + linkTT;
        double totalTT = (currentTime + linkTT) - departureTime;
        return new VrpPathWithTravelDataImpl(departureTime, totalTT, links, linkTTs);
    }

}
