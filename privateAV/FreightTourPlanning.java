/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
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
package privateAV;

import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.SchrimpfFactory;
import com.graphhopper.jsprit.core.algorithm.termination.VariationCoefficientTermination;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.util.Solutions;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelDataImpl;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.carrier.Tour.ServiceActivity;
import org.matsim.contrib.freight.carrier.Tour.TourElement;
import org.matsim.contrib.freight.jsprit.MatsimJspritFactory;
import org.matsim.contrib.freight.jsprit.NetworkBasedTransportCosts;
import org.matsim.contrib.freight.jsprit.NetworkRouter;
import org.matsim.contrib.taxi.schedule.TaxiEmptyDriveTask;
import org.matsim.contrib.taxi.schedule.TaxiTask;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.router.util.TravelTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author tschlenther
 *
 */
public final class FreightTourPlanning {

    private static final Logger log = Logger.getLogger(FreightTourPlanning.class);

    /**
	 */
    static FreightTourDataPlanned convertToPFAVTourData(ScheduledTour freightTour, Network network, TravelTime travelTime) {
        // we only need duration for the service tasks - id we wanted exact planned time points of daytime, we would need to derive them out of the legs (like we do for start and end activity)
        //the Start and End activities are not part of ScheduledTour.getTour.getTourElements();
        List<TaxiTask> taskList = new ArrayList<>();

        //we could think about setting the start time of the first retool activity according to global time window (currently PFAVUtils.FREIGHTTOUR_EARLIEST_START
        //but this implicitly happens when calculating oath to depot and wait time at depot in the FreightTourManagerListBasedImpl
        double tEnd = ((Tour.Leg) freightTour.getTour().getTourElements().get(0)).getExpectedDepartureTime();
        double tBegin = tEnd - PFAVUtils.PFAV_RETOOL_TIME;

        Link depotLink = network.getLinks().get(freightTour.getTour().getStart().getLocation());
        Link location = depotLink;
        taskList.add(new PFAVRetoolTask(tBegin, tEnd, location));

        int totalCapacityDemand = 0;

        int size = freightTour.getTour().getTourElements().size();
        for (int i = 0; i < size; i++) {
            TourElement currentElement = freightTour.getTour().getTourElements().get(i);
            if (currentElement instanceof ServiceActivity) {
                //currently we need to add PFAVUtils.PFAV_RETOOL_TIME, since we added this already to the start act duration
                ServiceActivity serviceAct = (ServiceActivity) currentElement;

                tBegin = serviceAct.getExpectedArrival() + PFAVUtils.PFAV_RETOOL_TIME;
                tEnd = tBegin + serviceAct.getDuration();
                location = network.getLinks().get(serviceAct.getLocation());
                totalCapacityDemand += serviceAct.getService().getCapacityDemand();
                taskList.add(new PFAVServiceTask(tBegin, tEnd, location, serviceAct.getService()));
            } else if (currentElement instanceof Tour.Leg) {
                NetworkRoute route = (NetworkRoute) ((Tour.Leg) currentElement).getRoute();
                VrpPathWithTravelData path;
                if (route.getStartLinkId().equals(route.getEndLinkId()))
                    path = VrpPaths.createZeroLengthPath(network.getLinks().get(route.getStartLinkId()), tEnd);
                else {
                    path = createVrpPath(route, tEnd, network, travelTime);
                }
                if (i == size - 1) {
                    taskList.add(new TaxiEmptyDriveTask(path));
                } else {
                    taskList.add(new PFAVServiceDriveTask(path));
                }
            }
        }
        double travelTimeToLastService = taskList.get(taskList.size() - 2).getBeginTime();

        tBegin = taskList.get(taskList.size() - 1).getEndTime();
        tEnd = tBegin + PFAVUtils.PFAV_RETOOL_TIME;
        location = network.getLinks().get(freightTour.getTour().getEnd().getLocation());
        taskList.add(new PFAVRetoolTask(tBegin, tEnd, location));

        double plannedTourDuration = tEnd - taskList.get(0).getBeginTime();
        return new FreightTourDataPlanned(taskList, depotLink, plannedTourDuration, travelTimeToLastService, totalCapacityDemand);
	}


    private static VrpPathWithTravelDataImpl createVrpPath(NetworkRoute networkRoute, double departureTime, Network network, TravelTime travelTime) {
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

    public static void runTourPlanningForCarriers(Carriers carriers, CarrierVehicleTypes vehicleTypes, Network network, TravelTime travelTime, int timeSlice) {

        NetworkBasedTransportCosts.Builder netBuilder = NetworkBasedTransportCosts.Builder.newInstance(network, vehicleTypes.getVehicleTypes().values());
        netBuilder.setTimeSliceWidth(timeSlice); // !!!! otherwise it will not do anything.
        netBuilder.setTravelTime(travelTime);
        final NetworkBasedTransportCosts netBasedCosts = netBuilder.build();

        for (Carrier carrier : carriers.getCarriers().values()) {
            //Build VRP
            VehicleRoutingProblem.Builder vrpBuilder = MatsimJspritFactory.createRoutingProblemBuilder(carrier, network);
            vrpBuilder.setRoutingCost(netBasedCosts);
            VehicleRoutingProblem problem = vrpBuilder.build();

            //get the algorithm out-of-the-box, search solution and get the best one.
            VehicleRoutingAlgorithm algorithm = new SchrimpfFactory().createAlgorithm(problem);

            algorithm.setMaxIterations(PFAVUtils.NR_OF_JSPRIT_ITERATIONS);

            // variationCoefficient = stdDeviation/mean. so i set the threshold rather soft
            algorithm.addTerminationCriterion(new VariationCoefficientTermination(50, 0.01));

            //TODO: add Initial solution - the one from last run => i tried this but this lead to the FreightReactionToTravelTimesTest failing...
//			algorithm.addInitialSolution();

            Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();
            VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

            //get the CarrierPlan
            CarrierPlan carrierPlan = MatsimJspritFactory.createPlan(carrier, bestSolution);

            /* calculate the route - we need this because otherwise we only have the duration of the service task and do not have a clue about tour duration
             */
            //if we use this default method, FastDijkstraFactory.createLeastCostPathCalculator(network, travelDisutility, travelTime) is called;
            NetworkRouter.routePlan(carrierPlan, netBasedCosts);

            carrier.setSelectedPlan(carrierPlan);
        }
    }

}
