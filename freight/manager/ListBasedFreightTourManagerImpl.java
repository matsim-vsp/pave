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
package freight.manager;

import analysis.PFAVUnfinishedToursDumper;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import freight.calculator.FreightTourCalculatorImpl;
import freight.tour.PFAVTourData;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.dvrp.schedule.Tasks;
import org.matsim.contrib.dvrp.trafficmonitoring.DvrpTravelTimeModule;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.taxi.schedule.TaxiStayTask;
import org.matsim.contrib.util.StraightLineKnnFinder;
import org.matsim.contrib.util.distance.DistanceUtils;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.events.IterationStartsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.controler.listener.IterationStartsListener;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelTime;
import privateAV.PFAVUtils;
import privateAV.vehicle.MustReturnLinkTimePair;
import privateAV.vehicle.PFAVehicle;

import java.util.*;

/**
 *
 * @author tschlenther
 *
 */
public class ListBasedFreightTourManagerImpl implements ListBasedFreightTourManager, IterationStartsListener, IterationEndsListener {

    private final static Logger log = Logger.getLogger(ListBasedFreightTourManagerImpl.class);


	private Map<Link, LinkedList<PFAVTourData>> depotToFreightTour = new HashMap<>();

	private List<PFAVTourData> freightTours = new ArrayList<>();
	@Inject
	@Named(DvrpRoutingNetworkProvider.DVRP_ROUTING)
	private Network network;
    @Inject
    @Named(DvrpTravelTimeModule.DVRP_ESTIMATED)
    private TravelTime travelTime;

    private Carriers carriers;
    private Carriers carriersWithOnlyUsedTours;
    private CarrierVehicleTypes vehicleTypes;

	/**
	 *
	 */
    public ListBasedFreightTourManagerImpl(String pathToCarriersFile, String pathToVehTypesFile) {

		this.carriers = readCarriers(pathToCarriersFile);
        this.vehicleTypes = readVehicleTypes(pathToVehTypesFile);
		log.info("loading carrier vehicle types..");
		new CarrierVehicleTypeLoader(carriers).loadVehicleTypes(vehicleTypes);
	}

	private CarrierVehicleTypes readVehicleTypes(String input) {
		CarrierVehicleTypes vTypes = new CarrierVehicleTypes();
		CarrierVehicleTypeReader reader = new CarrierVehicleTypeReader(vTypes);
		reader.readFile(input);
		return vTypes;
	}

	private Carriers readCarriers(String file){
		Carriers carriers = new Carriers();
		CarrierPlanXmlReaderV2 reader = new CarrierPlanXmlReaderV2(carriers);
		reader.readFile(file);
		return carriers;
	}

	private List<PFAVTourData> convertCarrierPlansToTaskList(Carriers carriers) {
        if (PFAVUtils.FREIGHT_DEMAND_SAMPLE_SIZE < 0 || PFAVUtils.FREIGHT_DEMAND_SAMPLE_SIZE > 1)
			throw new IllegalStateException("the sample size for the freight demand must be in between 0 and 1");

        //here we create a carrier copy which only contains the freight tours used in the current iteration in order to write those out in form of a carriers.xml file

		log.info("start converting carrier plans to taxi tasks..");

		List<PFAVTourData> freightTours = new ArrayList<>();
        carriersWithOnlyUsedTours = new Carriers();
        log.info(("number of carriers = " + carriers.getCarriers().size()));
		for(Carrier carrier : carriers.getCarriers().values()) {
            Collection<ScheduledTour> onlyUsedTours = new ArrayList<>();
            Carrier copy = CarrierImpl.newInstance(carrier.getId());
			CarrierPlan plan = carrier.getSelectedPlan();
			for (ScheduledTour freightTour : plan.getScheduledTours()) {
                //we need to cut down here and not beforehand in the services, since the tour planning would otherwise result in longer and inaccurate tours
                if (MatsimRandom.getRandom().nextDouble() <= PFAVUtils.FREIGHT_DEMAND_SAMPLE_SIZE) {
					freightTours.add(ConvertFreightTourForDvrp.convertToPFAVTourData(freightTour, this.network));
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
     * @see freight.manager.ListBasedFreightTourManager#getAVFreightTours()
     */
    @Override
	public List<PFAVTourData> getPFAVTours() {
        return this.freightTours;
    }

    /* (non-Javadoc)
     * @see freight.manager.ListBasedFreightTourManager#getRandomAVFreightTour()
     */
    @Override
	public PFAVTourData getRandomPFAVTour() {
		if (this.depotToFreightTour.size() == 0) return null;
		Link depot = this.depotToFreightTour.keySet().toArray(new Link[depotToFreightTour.size()])[MatsimRandom.getRandom().nextInt(this.depotToFreightTour.size())];


		PFAVTourData tour = (PFAVTourData) this.depotToFreightTour.get(depot).toArray()[MatsimRandom.getRandom().nextInt(this.depotToFreightTour.get(depot).size())];

        if (PFAVUtils.ALLOW_EMPTY_TOUR_LISTS_FOR_DEPOTS) {
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
     *returns the list of all freight activities converted to taxi task that represent the best matching freight tour for the given vehicle.
     * at the moment, the dispatch logic does the following:
     * look for the closest three depots. ask either one, if a tour is available that the vehicle can handle before it needs to be back at it's owner's place.
	 * as soon as a fitting tour is found, return it and delete it from the manager's to do list for the iteration.
     * @param vehicle that requests a freight tozr.
	 */
	@Override
	public PFAVTourData getBestPFAVTourForVehicle(PFAVehicle vehicle, LeastCostPathCalculator router) {
		return getDispatchedTourForVehicle(vehicle, router);
	}

	private PFAVTourData getDispatchedTourForVehicle(PFAVehicle vehicle, LeastCostPathCalculator router) {
		StraightLineKnnFinder<Link, Link> finder = new StraightLineKnnFinder<>(PFAVUtils.AMOUNT_OF_DEPOTS_TO_CONSIDER, link1 -> link1, link2 -> link2);
		Link requestLink = Tasks.getEndLink(vehicle.getSchedule().getCurrentTask());
		List<Link> nearestDepots = finder.findNearest(requestLink, depotToFreightTour.keySet().stream());

        //TODO: test this spatial dispatch algorithm, especially: is the nearestDepots list sorted??

		PFAVTourData matchingFreightTour = null;
        for (Link depot : nearestDepots) {
			Iterator<PFAVTourData> freightTourIterator = this.depotToFreightTour.get(depot).iterator();
			VrpPathWithTravelData pathFromCurrTaskToDepot = calcPathToDepot(vehicle, depot, router);
            if (DistanceUtils.calculateDistance(depot.getCoord(), requestLink.getCoord()) <= PFAVUtils.MAX_DISTANCE_TO_DEPOT
                    && pathFromCurrTaskToDepot.getTravelTime() <= PFAVUtils.MAX_TRAVELTIME_TO_DEPOT) {

				while (freightTourIterator.hasNext()) {
					PFAVTourData tourData = freightTourIterator.next();
					if (isEnoughTimeLeftToPerformFreightTour(vehicle, pathFromCurrTaskToDepot, tourData, router)) {
						freightTourIterator.remove();
						matchingFreightTour = tourData;
						break;
					} else {
						tourData.incrementAmountOfRejections();
					}
				}
				if (matchingFreightTour != null) {
					//if no tour at the depot is left, delete depot
					if (depotToFreightTour.get(depot).isEmpty() && !PFAVUtils.ALLOW_EMPTY_TOUR_LISTS_FOR_DEPOTS)
						depotToFreightTour.remove(depot);
					break;
				}
			}
        }
		if (matchingFreightTour == null) {
			//we could throw the FreightTourRequestDeniedEvent here and hand to it the depot list on which we had a look on as well as the amount of
			//freight tours we looked at etc.
			//BUT: we would need the eventsManager AND the MobsimTimer (for latter, we could normally also use currentTask.getEndTime() but that would be dirty somehow)
//			events.processEvent(new FreightTourRequestDeniedEvent(vehicle, requestLink.getId(), time));
		}
        return matchingFreightTour;
	}

	private VrpPathWithTravelData calcPathToDepot(PFAVehicle vehicle, Link depotLink, LeastCostPathCalculator router) {
		StayTask currentTask = (StayTask) vehicle.getSchedule().getCurrentTask();
		return VrpPaths.calcAndCreatePath(currentTask.getLink(), depotLink, currentTask.getEndTime(), router, travelTime);
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
	public boolean isEnoughTimeLeftToPerformFreightTour(PFAVehicle vehicle, VrpPathWithTravelData pathFromCurrTaskToDepot,
														 PFAVTourData freightTour, LeastCostPathCalculator router) {

        //do we have the chance to be at the last service in time?
        if (PFAVUtils.CONSIDER_SERVICE_TIME_WINDOWS_FOR_DISPATCH &&
                freightTour.getLatestArrivalAtLastService() < pathFromCurrTaskToDepot.getArrivalTime() + freightTour.getTravelTimeToLastService())
            return false;

		MustReturnLinkTimePair mustReturnToOwnerLog = vehicle.getMustReturnToOwnerLinkTimePairs().peek();
//		Double timeWhenOwnerNeedsVehicle = vehicle.getOwnerActEndTimes().peek();
		Double timeWhenOwnerNeedsVehicle = mustReturnToOwnerLog.getTime();
		Link returnLink = network.getLinks().get(mustReturnToOwnerLog.getLinkId());
		if (timeWhenOwnerNeedsVehicle == null) {
			throw new IllegalStateException("could not derive must return time of vehicle " + vehicle.getId() + " out of vehicle specification");
        } else if (timeWhenOwnerNeedsVehicle == Double.NEGATIVE_INFINITY) {
            log.warn("vehicle " + vehicle.getId() + " has a undefined must return time. this should not happen! no freight tour will be dispatched...");
            throw new RuntimeException("should not happen !?");
//            return false;
        } else if (timeWhenOwnerNeedsVehicle == Double.POSITIVE_INFINITY) {
            log.info("tour duration is irrelevant for vehicle " + vehicle.getId() + " because owner does not need the PFAV anymore for today");
			return true;
		}

		StayTask currentTask = (StayTask) vehicle.getSchedule().getCurrentTask();
		StayTask start = freightTour.getTourTasks().get(0);
		StayTask end = freightTour.getTourTasks().get(freightTour.getTourTasks().size() - 1);

		double tourDuration = freightTour.getPlannedTourDuration();

//		check if there is enough time before latest start
		if (pathFromCurrTaskToDepot.getArrivalTime() > PFAVUtils.FREIGHTTOUR_LATEST_START) return false;
//		check if vehicle arrives before earliest start. calculate the waiting time in case.
		double waitTimeAtDepot = 0;
		if (pathFromCurrTaskToDepot.getArrivalTime() < PFAVUtils.FREIGHTTOUR_EARLIEST_START) {
			waitTimeAtDepot = PFAVUtils.FREIGHTTOUR_EARLIEST_START - pathFromCurrTaskToDepot.getArrivalTime();
		}

//		Link returnLink = PFAVUtils.getLastPassengerDropOff(vehicle.getSchedule()).getLink();

		//owner link will be null if no dropOff has been performed yet. return to start link instead.
		//actually this should not happen in the moment, as the first call of the requestFreightTour() method in the scheduler always is triggered after a passenger dropoff
//		if (returnLink == null) returnLink = vehicle.getStartLink();

		VrpPathWithTravelData pathFromDepotToOwner = VrpPaths.calcAndCreatePath(freightTour.getDepotLink(), returnLink, end.getEndTime(), router, travelTime);

		double totalTimeNeededToPerformFreightTour = pathFromCurrTaskToDepot.getTravelTime() +
				waitTimeAtDepot +
				tourDuration +
				pathFromDepotToOwner.getTravelTime();

		if (timeWhenOwnerNeedsVehicle >= currentTask.getEndTime() + totalTimeNeededToPerformFreightTour + PFAVUtils.TIME_BUFFER) {
			log.info("tour duration = " + totalTimeNeededToPerformFreightTour + " seems to be okay for vehicle " + vehicle.getId() + " starting at time " + currentTask.getEndTime());
			log.warn("the owner wants the vehicle back at time " + timeWhenOwnerNeedsVehicle);

			if (waitTimeAtDepot > 0) {
				log.info("inserting wait task with duration= " + waitTimeAtDepot + " at the depot link " + freightTour.getDepotLink().getId() + " for vehicle " + vehicle.getId()
						+ " in order to be consistent with earliest start time set to " + PFAVUtils.FREIGHTTOUR_EARLIEST_START);
				freightTour.getTourTasks().add(0, new TaxiStayTask(start.getBeginTime() - waitTimeAtDepot, start.getBeginTime(), start.getLink()));
			}
			return true;
		}
		return false;
	}

	private void runTourPlanning() {
		FreightTourCalculatorImpl tourCalculator = new FreightTourCalculatorImpl();

		//the travel times we hand over contain the travel times of last mobsim iteration as long as we use the OfflineEstimator (set via TaxiConfigGroup)
		this.carriers = tourCalculator.runTourPlanningForCarriers(this.carriers, this.vehicleTypes, this.network, this.travelTime);

		log.info("overriding list of freight tours...");
		this.freightTours = convertCarrierPlansToTaskList(carriers);

        log.info("initialising mapping of freight tours to link id's");
		this.depotToFreightTour = new HashMap<>();

//		if we allow empty depots, we need to initialise the manager's map of depot links to tours with empty lists.
        if (PFAVUtils.ALLOW_EMPTY_TOUR_LISTS_FOR_DEPOTS) mapDepotLinksToEmptyTourList();

        //now fill the map with the freightTours that came out of the calculator
		mapStartLinkOfToursToTour();
		sortDepotLists();
    }

    private void mapDepotLinksToEmptyTourList() {
        for (Carrier carrier : this.carriers.getCarriers().values()) {
            for (CarrierVehicle v : carrier.getCarrierCapabilities().getCarrierVehicles()) {
				this.depotToFreightTour.put(network.getLinks().get(v.getLocation()), new LinkedList<>());
            }
        }
    }

	private void sortDepotLists() {
		Comparator<PFAVTourData> comparator = new Comparator<PFAVTourData>() {
			@Override
			public int compare(PFAVTourData tour1, PFAVTourData tour2) {
				return Double.compare(tour1.getLatestArrivalAtLastService(), tour2.getLatestArrivalAtLastService());
			}
		};
		for (LinkedList<PFAVTourData> q : this.depotToFreightTour.values()) {
			Collections.sort(q, comparator);
		}
	}
    
    
    private void mapStartLinkOfToursToTour() {
        for (PFAVTourData freightTour : this.freightTours) {
			Link start = freightTour.getDepotLink();
			if (this.depotToFreightTour.containsKey(start)) {
				this.depotToFreightTour.get(start).add(freightTour);
			} else{
				LinkedList<PFAVTourData> allDepotTours = new LinkedList<>();
				allDepotTours.add(freightTour);
				this.depotToFreightTour.put(start, allDepotTours);
			}
		}
	}

	@Override
    public void notifyIterationStarts(IterationStartsEvent event){
		if ((PFAVUtils.FREIGHTTOUR_PLANNING_INTERVAL < 1 && event.getIteration() < 1 && PFAVUtils.RUN_TOUR_PLANNING_BEFORE_FIRST_ITERATION)
				|| (event.getIteration() % PFAVUtils.FREIGHTTOUR_PLANNING_INTERVAL == 0)) {
            log.info("RUNNING FREIGHT CONTRIB TO CALCULATE FREIGHT TOURS BASED ON CURRENT TRAVEL TIMES");
            runTourPlanning();
        } else {
            mapStartLinkOfToursToTour();
			sortDepotLists();
        }
    }

	/**
	 * Notifies all observers of the Controler that a iteration is finished
	 *
	 * @param event
	 */
	@Override
	public void notifyIterationEnds(IterationEndsEvent event) {
		if ((PFAVUtils.FREIGHTTOUR_PLANNING_INTERVAL < 1 && event.getIteration() < 1) || (event.getIteration() % PFAVUtils.FREIGHTTOUR_PLANNING_INTERVAL == 0)) {
            String dir = event.getServices().getConfig().controler().getOutputDirectory() + "/ITERS/it." + event.getIteration() + "/";
			log.info("writing carrier file of iteration " + event.getIteration() + " to " + dir);
            writeCarriers(this.carriers, dir + "carriers_it" + event.getIteration() + ".xml");
            writeCarriers(this.carriersWithOnlyUsedTours, dir + "carriersOnlyUsedTours_it" + event.getIteration() + ".xml");
			List<PFAVTourData> unfinishedTours = new ArrayList<>();
			this.depotToFreightTour.forEach((link, tours) -> unfinishedTours.addAll(tours));
			new PFAVUnfinishedToursDumper(unfinishedTours).writeStats(dir + "notDispatchedTours_it" + event.getIteration() + ".csv");
		}
	}

    private void writeCarriers(Carriers carriers, String outputDir) {
		CarrierPlanXmlWriterV2 planWriter = new CarrierPlanXmlWriterV2(carriers);
		planWriter.write(outputDir);
	}
}
