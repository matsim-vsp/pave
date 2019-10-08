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

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.dvrp.schedule.Tasks;
import org.matsim.contrib.dvrp.trafficmonitoring.DvrpTravelTimeModule;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.taxi.schedule.TaxiEmptyDriveTask;
import org.matsim.contrib.taxi.schedule.TaxiStayTask;
import org.matsim.contrib.util.StraightLineKnnFinder;
import org.matsim.contrib.util.distance.DistanceUtils;
import org.matsim.core.config.Config;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.events.IterationStartsEvent;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelTime;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author tschlenther
 */
class FreightTourManagerListBasedImpl implements FreightTourManagerListBased {

    private final static Logger log = Logger.getLogger(FreightTourManagerListBasedImpl.class);
    private final FreightAVConfigGroup pfavConfigGroup;

    private Map<Link, LinkedList<FreightTourDataPlanned>> depotToFreightTour = new HashMap<>();

    private List<FreightTourDataPlanned> freightTours = new ArrayList<>();
    @Inject
    @Named(DvrpRoutingNetworkProvider.DVRP_ROUTING)
    private Network network;
    @Inject
    @Named(DvrpTravelTimeModule.DVRP_ESTIMATED)
    private TravelTime travelTime;

    @Inject
    @Named(FreightAVConfigGroup.GROUP_NAME)
    private Carriers carriers;

    private Carriers carriersWithOnlyUsedTours;

    @Inject
    @Named(FreightAVConfigGroup.GROUP_NAME)
    private CarrierVehicleTypes vehicleTypes;

    @Inject
    FreightTourManagerListBasedImpl(Config config) {
        this.pfavConfigGroup = FreightAVConfigGroup.get(config);
    }

    private List<FreightTourDataPlanned> convertCarrierPlansToTaskList(Carriers carriers) {
        if (pfavConfigGroup.getFreightDemandSampleSize() < 0 || pfavConfigGroup.getFreightDemandSampleSize() > 1)
            throw new IllegalStateException("the sample size for the freight demand must be in between 0 and 1");

        //here we create a carrier copy which only contains the freight tours used in the current iteration in order to write those out in form of a carriers.xml file

        log.info("start converting carrier plans to taxi tasks..");

        List<FreightTourDataPlanned> freightTours = new ArrayList<>();
        carriersWithOnlyUsedTours = new Carriers();
        log.info(("number of carriers = " + carriers.getCarriers().size()));
        for (Carrier carrier : carriers.getCarriers().values()) {
            Collection<ScheduledTour> onlyUsedTours = new ArrayList<>();
            Carrier copy = CarrierImpl.newInstance(carrier.getId());
            CarrierPlan plan = carrier.getSelectedPlan();
            for (ScheduledTour freightTour : plan.getScheduledTours()) {
                //we need to cut down here and not beforehand in the services, since the tour planning would otherwise result in longer and inaccurate tours
                if (MatsimRandom.getRandom().nextDouble() <= pfavConfigGroup.getFreightDemandSampleSize()) {
                    freightTours.add(FreightTourPlanning.convertToPFAVTourData(freightTour, this.network, this.travelTime, pfavConfigGroup));
                    onlyUsedTours.add(freightTour);
                }
            }
            CarrierPlan planWithOnlyUsedTours = new CarrierPlan(copy, onlyUsedTours);
            copy.setSelectedPlan(planWithOnlyUsedTours);
            copy.setCarrierCapabilities(carrier.getCarrierCapabilities());
            carriersWithOnlyUsedTours.addCarrier(copy);
        }
        log.info("number of carriers with only used tours = " + carriersWithOnlyUsedTours.getCarriers().size());
        return freightTours;
    }

    /* (non-Javadoc)
     * @see privateAV.FreightTourManagerListBased#getAVFreightTours()
     */
    @Override
    public List<FreightTourDataPlanned> getPFAVTours() {
        return this.freightTours;
    }

    /* (non-Javadoc)
     * @see privateAV.FreightTourManagerListBased#getRandomAVFreightTour()
     */
    @Override
    public FreightTourDataPlanned getRandomPFAVTour() {
        if (this.depotToFreightTour.size() == 0) return null;
        Link depot = this.depotToFreightTour.keySet().toArray(new Link[depotToFreightTour.size()])[MatsimRandom.getRandom().nextInt(this.depotToFreightTour.size())];


        FreightTourDataPlanned tour = (FreightTourDataPlanned) this.depotToFreightTour.get(depot).toArray()[MatsimRandom.getRandom().nextInt(this.depotToFreightTour.get(depot).size())];

        if (pfavConfigGroup.isAllowEmptyTourListsForDepots()) {
            if (tour == null) {
                return getRandomPFAVTour();
            }
        } else {
            //if no tour at the depot is left, delete depot
            if (this.depotToFreightTour.get(depot).isEmpty()) {
                this.depotToFreightTour.remove(depot);
            }
        }
        return tour;
    }

    /**
     * returns the list of all freight activities converted to taxi task that represent the best matching freight tour for the given vehicle.
     * at the moment, the dispatch logic does the following:
     * look for the closest three depots. ask either one, if a tour is available that the vehicle can handle before it needs to be back at it's owner's place.
     * as soon as a fitting tour is found, return it and delete it from the manager's to do list for the iteration.
     *
     * @param vehicle that requests a freight tozr.
     */
    @Override
    public FreightTourDataPlanned vehicleRequestedFreightTour(PFAVehicle vehicle, LeastCostPathCalculator router) {
        Link requestLink = Tasks.getEndLink(vehicle.getSchedule().getCurrentTask());
        List<Link> nearestDepots = findNearestDepots(requestLink, depotToFreightTour.keySet().stream());
        return searchForTourAtSeveralDepots(vehicle, router, requestLink, nearestDepots);
    }

    @Override
    public FreightTourDataPlanned vehicleRequestedFreightTourAtDepot(PFAVehicle vehicle, Link depotLink, LeastCostPathCalculator router) {
        Link requestLink = Tasks.getEndLink(vehicle.getSchedule().getCurrentTask());
        return searchForTourAtDepot(depotLink, requestLink, vehicle, router);
    }

    @Override
    public FreightTourDataPlanned vehicleRequestedFreightTourExcludingDepot(PFAVehicle vehicle, Link depotLink, LeastCostPathCalculator router) {
        Link requestLink = Tasks.getEndLink(vehicle.getSchedule().getCurrentTask());
        Set<Link> depots = new HashSet<>(depotToFreightTour.keySet());
        depots.remove(depotLink);
        List<Link> nearestDepots = findNearestDepots(requestLink, depots.stream());
        return searchForTourAtSeveralDepots(vehicle, router, requestLink, nearestDepots);
    }

    private List<Link> findNearestDepots(Link requestLink, Stream<Link> depotLinks) {
        StraightLineKnnFinder<Link, Link> finder = new StraightLineKnnFinder<>(pfavConfigGroup.getAmountOfDepotsToConsider(), link1 -> link1, link2 -> link2);
        return finder.findNearest(requestLink, depotLinks);
    }

    private FreightTourDataPlanned searchForTourAtSeveralDepots(PFAVehicle vehicle, LeastCostPathCalculator router, Link requestLink, List<Link> nearestDepots) {
        FreightTourDataPlanned matchingFreightTour = null;
        for (Link depot : nearestDepots) {
            matchingFreightTour = searchForTourAtDepot(depot, requestLink, vehicle, router);
            if (matchingFreightTour != null) break;
        }
        if (matchingFreightTour == null) {
            log.info("request will be rejected. must return log of vehicle:" + vehicle.getMustReturnToOwnerLinkTimePairs().peek());
            //we could throw the FreightTourRequestRejectedEvent here and hand to it the depot list on which we had a look on as well as the amount of
            //freight tours we looked at etc.
            //BUT: we would need the eventsManager AND the MobsimTimer (for latter, we could normally also use currentTask.getEndTime() but that would be dirty somehow)
//			events.processEvent(new FreightTourRequestRejectedEvent(vehicle, requestLink.getId(), time));
        }
        return matchingFreightTour;
    }

    private FreightTourDataPlanned searchForTourAtDepot(Link depot, Link requestLink, PFAVehicle vehicle, LeastCostPathCalculator router) {
        FreightTourDataPlanned matchingFreightTour = null;
        log.info("size of depot todo list: " + this.depotToFreightTour.get(depot).size());
        if (this.depotToFreightTour.get(depot).isEmpty())
            return null;//only go on if there is a tour left at depot
        Iterator<FreightTourDataPlanned> freightTourIterator = this.depotToFreightTour.get(depot).iterator();
        VrpPathWithTravelData pathFromCurrTaskToDepot = calcPathToDepot(vehicle, depot, router);
        if (DistanceUtils.calculateDistance(depot.getCoord(), requestLink.getCoord()) <= pfavConfigGroup.getMaxBeelineDistanceToDepot()    // MAX BEELINE DISTANCE TO DEPOT
                && pathFromCurrTaskToDepot.getTravelTime() <= pfavConfigGroup.getMaxTravelTimeToDepot()                                    // MAX TRAVEL TIME TO DEPOT
                && pathFromCurrTaskToDepot.getArrivalTime() < pfavConfigGroup.getFreightTourLatestStart()) {                                  // ARRIVAL BEFORE LATEST START

            log.info("computed arrival time at depot = " + pathFromCurrTaskToDepot.getArrivalTime());
            log.info("latest start is = " + pfavConfigGroup.getFreightTourLatestStart());
            double waitTimeAtDepot = computeWaitTimeAtDepot(pathFromCurrTaskToDepot);

            while (freightTourIterator.hasNext()) {
                FreightTourDataPlanned tourData = freightTourIterator.next();
                if (isEnoughTimeLeftToPerformFreightTour(vehicle, pathFromCurrTaskToDepot, waitTimeAtDepot, tourData, router)) {
                    freightTourIterator.remove();
                    matchingFreightTour = tourData;
                    break;
                } else {
                    tourData.incrementAmountOfRejections();
                }
            }
            if (matchingFreightTour != null) {
                //if no tour at the depot is left, delete depot
                if (depotToFreightTour.get(depot).isEmpty() && !pfavConfigGroup.isAllowEmptyTourListsForDepots())
                    depotToFreightTour.remove(depot);
                log.info("size of depot to do list after removal: " + this.depotToFreightTour.get(depot).size());
            }
        }
        return matchingFreightTour;
    }


    private VrpPathWithTravelData calcPathToDepot(PFAVehicle vehicle, Link depotLink, LeastCostPathCalculator router) {
        Task currentTask = vehicle.getSchedule().getCurrentTask();
        return VrpPaths.calcAndCreatePath(Tasks.getEndLink(currentTask), depotLink, currentTask.getEndTime(), router, travelTime);
    }


    /**
     * here we need to compute the path back from the depot to the owner to estimate the total amount needed for the tour.
     * this path will again be computed by the scheduler. i tried to avoid computing this twice by moving all the schedule construction
     * from the scheduleFreightTour() in the scheduler to here. this lead actually to 2 days of work, since the logic of the manager then needs to be
     * scheduleBased and in the end, the last path back to the owner needs to be computed twice anyways. this is because here, we use the estimated arrival time
     * of the freight tour computed by the freight contrib as the departure time for this trip. this is not the same time we would use later for the construction of
     * the schedule, as travel times / routes for the freight legs might have changed in the mean time (freight contrib runs outside of mobsim and does not use VrpPaths)
     * <p>
     * tschlenther, 20.02.2019
     *
     * @param vehicle
     * @param freightTour
     * @param router
     * @return
     */
    @Override
    public boolean isEnoughTimeLeftToPerformFreightTour(PFAVehicle vehicle, VrpPathWithTravelData pathFromCurrTaskToDepot, double waitTimeAtDepot,
                                                        FreightTourDataPlanned freightTour, LeastCostPathCalculator router) {

        //does the vehicle have the chance to be at the last service in time?
        if (pfavConfigGroup.isConsiderServiceTimeWindowsForDispatch() &&
                freightTour.getLatestArrivalAtLastService() < pathFromCurrTaskToDepot.getArrivalTime() + freightTour.getTravelTimeToLastService())
            return false;

        PFAVehicle.MustReturnLinkTimePair mustReturnToOwnerLog = vehicle.getMustReturnToOwnerLinkTimePairs().peek();
        Double timeWhenOwnerNeedsVehicle = mustReturnToOwnerLog.getTime();
        Link returnLink = network.getLinks().get(mustReturnToOwnerLog.getLinkId());
        Task currentTask = vehicle.getSchedule().getCurrentTask();
        StayTask start = (StayTask) freightTour.getTourTasks().get(0);
        StayTask end = (StayTask) freightTour.getTourTasks().get(freightTour.getTourTasks().size() - 1);

        if (timeWhenOwnerNeedsVehicle == null) {
            throw new IllegalStateException("could not derive must return time of vehicle " + vehicle.getId() + " out of vehicle specification");
        } else if (timeWhenOwnerNeedsVehicle == Double.NEGATIVE_INFINITY) {
            log.warn("vehicle " + vehicle.getId() + " has a undefined must return time. this should not happen! no freight tour will be dispatched...");
            throw new RuntimeException("should not happen !?");
//            return false;
        } else if (timeWhenOwnerNeedsVehicle == Double.POSITIVE_INFINITY) {
            log.info("tour duration is irrelevant for vehicle " + vehicle.getId() + " because owner does not need the PFAV anymore for today");
            accountForWaitTaskAndAccessDrive(vehicle, pathFromCurrTaskToDepot, waitTimeAtDepot, freightTour, start);
            return true;
        }

        double tourDuration = freightTour.getPlannedTourDuration();
        if (tourDuration < 0) throw new IllegalStateException("tour duration must be positive..");

//		Link returnLink = PFAVUtils.getLastPassengerDropOff(vehicle.getSchedule()).getLink();
        //owner link will be null if no dropOff has been performed yet. return to start link instead.
        //actually this should not happen in the moment, as the first call of the requestFreightTour() method in the scheduler always is triggered after a passenger dropoff
//		if (returnLink == null) returnLink = vehicle.getStartLink();

        VrpPathWithTravelData pathFromDepotToOwner = VrpPaths.calcAndCreatePath(freightTour.getDepotLink(), returnLink, end.getEndTime(), router, travelTime);

        double totalTimeNeededToPerformFreightTour = pathFromCurrTaskToDepot.getTravelTime() +
                waitTimeAtDepot +
                tourDuration +
                pathFromDepotToOwner.getTravelTime();

        if (totalTimeNeededToPerformFreightTour < 0) throw new IllegalStateException("total time needed for tour must be positive. \n " +
                "access drive duration = " + pathFromCurrTaskToDepot.getTravelTime() +
                "\n wait time at depot = " + waitTimeAtDepot +
                "\n tourDuration = " + tourDuration +
                "\n pathFromDepotToOwner = " + pathFromDepotToOwner);

        if (timeWhenOwnerNeedsVehicle >= currentTask.getEndTime() + totalTimeNeededToPerformFreightTour + pfavConfigGroup.getTimeBuffer()) {
            log.info("tour duration = " + totalTimeNeededToPerformFreightTour + " seems to be okay for vehicle " + vehicle.getId() + " starting at time " + currentTask.getEndTime());
            log.warn("the owner wants the vehicle back at time " + timeWhenOwnerNeedsVehicle);

            accountForWaitTaskAndAccessDrive(vehicle, pathFromCurrTaskToDepot, waitTimeAtDepot, freightTour, start);
            return true;
        }
        return false;
    }

    private void accountForWaitTaskAndAccessDrive(PFAVehicle vehicle, VrpPathWithTravelData pathFromCurrTaskToDepot, double waitTimeAtDepot, FreightTourDataPlanned freightTour, StayTask start) {
        if (waitTimeAtDepot > 0) {
            log.info("inserting wait task with duration= " + waitTimeAtDepot + " at the depot link " + freightTour.getDepotLink().getId() + " for vehicle " + vehicle.getId()
                    + " in order to be consistent with earliest start time set to " + pfavConfigGroup.getFreightTourEarliestStart());
            freightTour.getTourTasks().add(0, new TaxiStayTask(start.getBeginTime() - waitTimeAtDepot, start.getBeginTime(), start.getLink()));
        }
        freightTour.setAccessDriveTask(new TaxiEmptyDriveTask(pathFromCurrTaskToDepot));
    }

    private double computeWaitTimeAtDepot(VrpPathWithTravelData pathFromCurrTaskToDepot) {
//		check if vehicle arrives before earliest start. calculate the waiting time in case.
        double waitTimeAtDepot = 0;
        if (pathFromCurrTaskToDepot.getArrivalTime() < pfavConfigGroup.getFreightTourEarliestStart()) {
            waitTimeAtDepot = pfavConfigGroup.getFreightTourEarliestStart() - pathFromCurrTaskToDepot.getArrivalTime();
        }
        log.info("wait time at depot is = " + waitTimeAtDepot);
        return waitTimeAtDepot;
    }

    private void runTourPlanning() {
        //the travel times we hand over contain the travel times of last mobsim iteration as long as we use the OfflineEstimator (set via TaxiConfigGroup)
        FreightTourPlanning.runTourPlanningForCarriers(this.carriers, this.vehicleTypes, this.network, this.travelTime, PFAVUtils.timeSlice(), pfavConfigGroup.getNrOfJspritIterations());
        log.info("overriding list of freight tours...");
        this.freightTours = convertCarrierPlansToTaskList(carriers);
    }

    private void mapDepotLinksToEmptyTourList() {
        for (Carrier carrier : this.carriers.getCarriers().values()) {
            for (CarrierVehicle v : carrier.getCarrierCapabilities().getCarrierVehicles().values()) {
                this.depotToFreightTour.put(network.getLinks().get(v.getLocation()), new LinkedList<>());
            }
        }
    }

    private void sortDepotLists() {
        Comparator<FreightTourDataPlanned> comparator = Comparator.comparingDouble(FreightTourDataPlanned::getLatestArrivalAtLastService);
        for (LinkedList<FreightTourDataPlanned> q : this.depotToFreightTour.values()) {
            Collections.sort(q, comparator);
        }
    }


    @Override
    public void notifyIterationStarts(IterationStartsEvent event) {
        if (event.getIteration() == 0) {
            if (pfavConfigGroup.isRunTourPlanningBeforeFirstIteration()) {
                log.info("RUNNING FREIGHT CONTRIB TO CALCULATE FREIGHT TOURS BASED ON CURRENT TRAVEL TIMES");
                runTourPlanning();
            } else {
                this.freightTours = convertCarrierPlansToTaskList(this.carriers);
            }
            //in iteration 0, always write carriers file
            writeCarriers(event);
        } else if ((event.getIteration() % pfavConfigGroup.getTourPlanningInterval() == 0)) {
            log.info("RUNNING FREIGHT CONTRIB TO CALCULATE FREIGHT TOURS BASED ON CURRENT TRAVEL TIMES");
            runTourPlanning();
            writeCarriers(event);
        }
        log.info("initialising mapping of freight tours to link id's");
        this.depotToFreightTour = new HashMap<>();

//		if we allow empty depots, we need to initialise the manager's map of depot links to tours with empty lists.
        if (pfavConfigGroup.isAllowEmptyTourListsForDepots()) mapDepotLinksToEmptyTourList();

        //now fill the map with the freightTours that came out of the calculator
        mapStartLinkOfToursToTour();
        sortDepotLists();
    }

    private void mapStartLinkOfToursToTour() {
        for (FreightTourDataPlanned freightTour : this.freightTours) {
            Link start = freightTour.getDepotLink();
            if (this.depotToFreightTour.containsKey(start)) {
                this.depotToFreightTour.get(start).add(freightTour);
            } else {
                LinkedList<FreightTourDataPlanned> allDepotTours = new LinkedList<>();
                allDepotTours.add(freightTour);
                this.depotToFreightTour.put(start, allDepotTours);
            }
        }
    }

    /**
     * Notifies all observers of the Controler that a iteration is finished
     *
     * @param event
     */
    @Override
    public void notifyIterationEnds(IterationEndsEvent event) {
        String dir = event.getServices().getConfig().controler().getOutputDirectory() + "/ITERS/it." + event.getIteration() + "/";
        List<FreightTourDataPlanned> unfinishedTours = new ArrayList<>();
        this.depotToFreightTour.forEach((link, tours) -> unfinishedTours.addAll(tours));
        new PFAVUnfinishedToursDumper(unfinishedTours).writeStats(dir + "notDispatchedTours_it" + event.getIteration() + ".csv");
    }

    private void writeCarriers(IterationStartsEvent event) {
        String dir = event.getServices().getConfig().controler().getOutputDirectory() + "/ITERS/it." + event.getIteration() + "/";
        log.info("writing carrier file of iteration " + event.getIteration() + " to " + dir);
        CarrierPlanXmlWriterV2 planWriter = new CarrierPlanXmlWriterV2(this.carriers);
        planWriter.write(dir + "carriers_it" + event.getIteration() + ".xml");
        CarrierPlanXmlWriterV2 planWriter2 = new CarrierPlanXmlWriterV2(this.carriersWithOnlyUsedTours);
        planWriter2.write(dir + "carriersOnlyUsedTours_it" + event.getIteration() + ".xml");
    }
}
