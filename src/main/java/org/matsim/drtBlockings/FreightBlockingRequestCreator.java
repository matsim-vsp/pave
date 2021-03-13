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

package org.matsim.drtBlockings;

import com.graphhopper.jsprit.core.problem.solution.route.activity.ServiceActivity;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelDataImpl;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.core.config.Config;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.router.util.TravelTime;
import org.matsim.drtBlockings.tasks.FreightServiceTask;
import org.matsim.drtBlockings.tasks.FreightDriveTask;
import org.matsim.drtBlockings.tasks.FreightPickupTask;
import org.matsim.drtBlockings.tasks.FreightRetoolTask;
import org.matsim.vehicles.Vehicle;

import java.util.*;

class FreightBlockingRequestCreator implements BlockingRequestCreator {

    //TODO make this configurable
    //source: DLR guy, not official though, a quotable paper or so woulb be nice
    static final double RETOOL_DURATION = 1.5 * 60;
    static final double SUBMISSION_LOOK_AHEAD = 15 * 60;

    private final Network network;
    private final String mode;
    private final TravelTime travelTime;
    private final double qSimStartTime;

    public FreightBlockingRequestCreator(Config config, Network network, TravelTime travelTime) {
        this.network = network;
        this.travelTime = travelTime;
        this.mode = DrtConfigGroup.getSingleModeDrtConfig(config).getMode();
        if(config.qsim().getStartTime().isDefined()){
            this.qSimStartTime = config.qsim().getStartTime().seconds();
        } else {
            this.qSimStartTime = 0;
        }
    }

    @Override
    public Set<DrtBlockingRequest> createBlockingRequests(Carriers carriers) {
        Set<DrtBlockingRequest> requests = new HashSet<>();

        carriers.getCarriers().values().forEach(carrier -> {
            if(CarrierUtils.getCarrierMode(carrier).equals(mode)){
                System.out.println("CARRIER: " + carrier.getId());
                requests.addAll(createBlockingRequestsForCarrier(carrier));
            }
        });
        return requests;
    }

    private Set<DrtBlockingRequest> createBlockingRequestsForCarrier(Carrier carrier){
        Set<DrtBlockingRequest> requests = new HashSet<>();
        Map<Id<Vehicle>, Integer> vehicleCount = new HashMap<>();

//        int count = 0;
        int vehCount;
        for (ScheduledTour tour : carrier.getSelectedPlan().getScheduledTours()){

            if(vehicleCount.get(tour.getVehicle().getId()) == null){
                vehCount = 1;
            } else{
                vehCount = vehicleCount.get(tour.getVehicle().getId()) + 1;
                vehicleCount.replace(tour.getVehicle().getId(), vehCount);
            }

            //TODO add noOfServices to tourId!
            //or better Id of first service!
//            String tourID = carrier.getId() + "" + tour.getVehicle().getId() + "_" + vehCount; /* + "_" + count;*/
            String tourID = carrier.getId() + "" + tour.getVehicle().getId() + "_" + getFirstTourServiceOrShipmentForId(tour.getTour());
            requests.add(createRequest(carrier.getId(), Id.create(tour.getVehicle().getId(), DvrpVehicle.class), tour, tourID));
//            count++;
        }
        return requests;
    }

    private DrtBlockingRequest createRequest(Id<Carrier> carrierId, Id<DvrpVehicle> vehicleId, ScheduledTour scheduledTour, String tourID) {
        Id<Request> id = Id.create(tourID, Request.class);
        String mode = this.mode;
        double blockingStart = determineStartOfBlocking(scheduledTour);
        double submissionTime = blockingStart - SUBMISSION_LOOK_AHEAD;

        List<Task> tourTasks = convertScheduledTour2DvrpTasks(scheduledTour, blockingStart);
        double blockingEnd = tourTasks.get(tourTasks.size() - 1).getEndTime();

        DrtBlockingRequest request = DrtBlockingRequest.newBuilder()
                .id(id)
                .mode(mode)
                .carrierId(carrierId)
                .submissionTime(submissionTime)
                .duration(blockingEnd - blockingStart)
                .tasks(tourTasks)
                .startTime(blockingStart)
                .build();

        return request;
    }


    /**
     * a problem is that JSprit calculates the departure for a ScheduledTour in a way that vehicles start as early as possible and
     * then wait at the location of the first delivery (if it's time window has not started yet). We account for this by setting the earliest departure to 1.5*travelTime between depot
     * and first delivery.
     *
     * @param scheduledTour
     * @return
     */
    private double determineStartOfBlocking(ScheduledTour scheduledTour) {
        double vehicleEarliestStart = scheduledTour.getVehicle().getEarliestStartTime();
        double ttDepot2FirstDelivery = ((Tour.Leg) scheduledTour.getTour().getTourElements().get(0)).getExpectedTransportTime();
        double firstDeliveryEarliestStart =((Tour.TourActivity) scheduledTour.getTour().getTourElements().get(1)).getTimeWindow().getStart();

        double tourStart = scheduledTour.getDeparture();
        double calculatedStart;
//        double bufferFactor = config.plansCalcRoute().getTeleportedModeFreespeedFactors().get(TransportMode.ride); //TODO
        double bufferFactor = 1.25;

        //if jsprit scheduled the tour start way too early, just account for the ttDepot2FirstDelivery * 1.5 and ignore the original start time
        if(tourStart + ttDepot2FirstDelivery * bufferFactor <= firstDeliveryEarliestStart){
            calculatedStart = firstDeliveryEarliestStart - ttDepot2FirstDelivery * bufferFactor - RETOOL_DURATION;
        } else {
            calculatedStart = tourStart - RETOOL_DURATION;
        }
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
                    tourTasks.add(new FreightServiceTask(serviceAct, previousTaskEndTime, currentTaskEndTime,  network.getLinks().get(serviceAct.getLocation())));
                } else if(tourElement instanceof Tour.Pickup){
                    Tour.Pickup pickup = (Tour.Pickup) tourElement;
                    tourTasks.add(new FreightPickupTask(pickup, previousTaskEndTime, currentTaskEndTime, network.getLinks().get(pickup.getLocation())));
                } else if (tourElement instanceof Tour.Delivery){
                    throw new RuntimeException("no drt task type implemented yet for freight delivery activities..."); //TODO
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

    private Id<CarrierService> getFirstTourServiceOrShipmentForId(Tour tour) {

        Id<CarrierService> firstServiceOrShipmentId = null;
        for(Tour.TourElement e : tour.getTourElements()) {
            if(e instanceof Tour.ServiceActivity) {
                firstServiceOrShipmentId = ((Tour.ServiceActivity) e).getService().getId();
                break;
            } else if(e instanceof Tour.ShipmentBasedActivity) {
                firstServiceOrShipmentId = Id.create(((Tour.ShipmentBasedActivity) e).getShipment().getId(), CarrierService.class);   
                break;
            }
        }

        return firstServiceOrShipmentId;
    }

}
