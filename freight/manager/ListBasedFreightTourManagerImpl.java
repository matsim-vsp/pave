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

import com.google.inject.Inject;
import com.google.inject.name.Named;
import freight.FreightTourCalculatorImpl;
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
import org.matsim.contrib.util.StraightLineKnnFinder;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.events.IterationStartsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.controler.listener.IterationStartsListener;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelTime;
import privateAV.PFAVUtils;
import privateAV.vehicle.PFAVehicle;

import java.util.*;

/**
 *
 * @author tschlenther
 *
 */
public class ListBasedFreightTourManagerImpl implements ListBasedFreightTourManager, IterationStartsListener, IterationEndsListener {

    private final static Logger log = Logger.getLogger(ListBasedFreightTourManagerImpl.class);
	
    /**
     * the freight contrib will be run before every iteration where iterationNumber % FREIGHTTOUR_PLANNING_INTERVAL == 0- \n
	 * if FREIGHTTOUR_PLANNING_INTERVAL is set to 0 or any negative integer, the freight contrib will run only before iteration 0.
     */
    //TODO make configurable
    private final int FREIGHTTOUR_PLANNING_INTERVAL = 1;

	private Map<Link, List<List<StayTask>>> startLinkToFreightTour = new HashMap<>();

	private List<List<StayTask>> freightTours = new ArrayList<>();
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

	private List<List<StayTask>> convertCarrierPlansToTaskList(Carriers carriers) {
        if (PFAVUtils.FREIGHT_DEMAND_SAMPLE_SIZE < 0 || PFAVUtils.FREIGHT_DEMAND_SAMPLE_SIZE > 1)
			throw new IllegalStateException("the sample size for the freight demand must be in between 0 and 1");

        //here we create a carrier copy which only contains the freight tours used in the current iteration in order to write those out in form of a carriers.xml file

		log.info("start converting carrier plans to taxi tasks..");
		
		List<List<StayTask>> freightTours = new ArrayList<>();
        carriersWithOnlyUsedTours = new Carriers();
        log.info(("number of carriers = " + carriers.getCarriers().size()));
		for(Carrier carrier : carriers.getCarriers().values()) {
            Collection<ScheduledTour> onlyUsedTours = new ArrayList<>();
            Carrier copy = CarrierImpl.newInstance(carrier.getId());
			CarrierPlan plan = carrier.getSelectedPlan();
			for (ScheduledTour freightTour : plan.getScheduledTours()) {
                //we need to cut down here and not beforehand in the services, since the tour planning would otherwise result in longer and inaccurate tours
                if (MatsimRandom.getRandom().nextDouble() <= PFAVUtils.FREIGHT_DEMAND_SAMPLE_SIZE) {
                    freightTours.add(ConvertFreightTourForDvrp.convertToList(freightTour, this.network));
                    onlyUsedTours.add(freightTour);
                }
            }
            CarrierPlan planWithOnlyUsedTours = new CarrierPlan(copy, onlyUsedTours);
            copy.setSelectedPlan(planWithOnlyUsedTours);
            carriersWithOnlyUsedTours.addCarrier(copy);
		}
        log.info("number of carriers with only used tours = " + carriersWithOnlyUsedTours.getCarriers().size());
		return freightTours;
	}

    /* (non-Javadoc)
     * @see freight.manager.ListBasedFreightTourManager#getAVFreightTours()
     */
    @Override
    public List<List<StayTask>> getPFAVTours() {
        return this.freightTours;
    }

    /* (non-Javadoc)
     * @see freight.manager.ListBasedFreightTourManager#getRandomAVFreightTour()
     */
    @Override
	public List<StayTask> getRandomPFAVTour() {
        if (this.startLinkToFreightTour.size() == 0) return null;
        Link depot = this.startLinkToFreightTour.keySet().toArray(new Link[startLinkToFreightTour.size()])[MatsimRandom.getRandom().nextInt(this.startLinkToFreightTour.size())];

        List<StayTask> tour = this.startLinkToFreightTour.get(depot).remove(MatsimRandom.getRandom().nextInt(this.startLinkToFreightTour.get(depot).size()));

        //if no tour at the depot is left, delete depot
        if (this.startLinkToFreightTour.get(depot).isEmpty()) {
            this.startLinkToFreightTour.remove(depot);
        }
        return tour;
	}

	/**
     *returns the list of all freight activities converted to taxi task that represent the best matching freight tour for the given vehicle.
     * at the moment, the dispatch logic does the following:
     * look for the closest three depots. ask either one, if a tour is available that the vehicle can handle before it needs to be back at it's owner's place.
     * as soon as a fitting tour is found, return it and delete from the manager's to do list for the iteration.
     * @param vehicle that requests a freight tozr.
	 */
	@Override
    public List<StayTask> getBestPFAVTourForVehicle(PFAVehicle vehicle, LeastCostPathCalculator router) {
		return getDispatchedTourForVehicle(vehicle, router);
	}

    private List<StayTask> getDispatchedTourForVehicle(PFAVehicle vehicle, LeastCostPathCalculator router) {
		StraightLineKnnFinder<Link,Link> finder = new StraightLineKnnFinder<>(3,link1-> link1 , link2 -> link2 );
		// at the moment, we need to assume that on one link there can only be one depot/carrier! this is because we need a mapping in this direction for the following piece of code
		List<Link> nearestDepots = finder.findNearest(Tasks.getEndLink(vehicle.getSchedule().getCurrentTask()),startLinkToFreightTour.keySet().stream());

		//TODO: probably create a representation of the freightTour - something like FreightTourData - that contains the List<StayTask> and the duration, maybe the start and end link explicitly
		//TODO: test this spatial dispatch algorithm

        List<StayTask> matchingFreightTour = null;
		for (Link l : nearestDepots){
			Iterator<List<StayTask>> freightTourIterator = this.startLinkToFreightTour.get(l).iterator();
			while(freightTourIterator.hasNext()){
				List<StayTask> freightTour = freightTourIterator.next();
                if (isEnoughTimeLeftToPerformFreightTour(vehicle, freightTour, router)) {
					freightTourIterator.remove();
                    matchingFreightTour = freightTour;
                    break;
				}
			}
            if (matchingFreightTour != null) {
                if (startLinkToFreightTour.get(l).isEmpty()) startLinkToFreightTour.remove(l);
                break;
            }
        }
        return matchingFreightTour;
	}

	/**
	 * here we need to compute the paths to the depot and back from the depot to the owner to estimate the total amount needed for the tour.
	 * those paths will again be computed by the scheduler. i tried to avoid computing these paths twice by moving all the schedule construction
	 * from the scheduleFreightTour() in the scheduler to here. this lead actually to 2 days of work, since the logic of the manager then needs to be
	 * scheduleBased and in the end, the last path back to the owner needs to be computed twice anyways. this iss because here, we use the estimated arrival time
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
	private boolean isEnoughTimeLeftToPerformFreightTour(PFAVehicle vehicle, List<StayTask> freightTour, LeastCostPathCalculator router) {


		//TODO: implement a global end time point for freigh tours ? so somehting like: after 8 p.m. no one should deliver anything anymore ??
		Double timeWhenOwnerNeedsVehicle = vehicle.getOwnerActEndTimes().peek();
		if (timeWhenOwnerNeedsVehicle == null) {
			throw new IllegalStateException("could not derive must return time of vehicle " + vehicle.getId() + " out of vehicle specification");
		} else if (Double.isInfinite(timeWhenOwnerNeedsVehicle)) {
			//the next activity of the vehicle owner is the last for the day, so we can always perform the freight tour
			vehicle.getOwnerActEndTimes().remove();
			return true;
		}

		StayTask currentTask = (StayTask) vehicle.getSchedule().getCurrentTask();
		StayTask start = freightTour.get(0);
		StayTask end = freightTour.get(freightTour.size() - 1);

		double tourDuration = end.getEndTime() - start.getBeginTime();


		VrpPathWithTravelData pathFromCurrTaskToDepot = VrpPaths.calcAndCreatePath(currentTask.getLink(), start.getLink(), currentTask.getEndTime(), router, travelTime);
		VrpPathWithTravelData pathFromDepot = VrpPaths.calcAndCreatePath(end.getLink(), currentTask.getLink(), end.getEndTime(), router, travelTime);


		//TODO check this again
		double totalTimeNeededToPerformFreightTour = pathFromCurrTaskToDepot.getTravelTime() +
				tourDuration +
				pathFromDepot.getTravelTime();


		if (timeWhenOwnerNeedsVehicle >= currentTask.getEndTime() + totalTimeNeededToPerformFreightTour + PFAVUtils.TIME_BUFFER) {
			vehicle.getOwnerActEndTimes().remove();
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
		mapStartLinkOfToursToTour();
    }

    private void mapStartLinkOfToursToTour() {
        log.info("initialising mapping of freight tours to link id's");
		this.startLinkToFreightTour = new HashMap<>();

        for (List<StayTask> freightTour : this.freightTours) {
			Link start = freightTour.get(0).getLink();
			if(this.startLinkToFreightTour.containsKey(start)){
				this.startLinkToFreightTour.get(start).add(freightTour);
			} else{
				List<List<StayTask>> allDepotTours = new ArrayList<>();
				allDepotTours.add(freightTour);
				this.startLinkToFreightTour.put(start,allDepotTours);
			}
		}
	}

	@Override
    public void notifyIterationStarts(IterationStartsEvent event){
		if ((FREIGHTTOUR_PLANNING_INTERVAL < 1 && event.getIteration() < 1) || (event.getIteration() % FREIGHTTOUR_PLANNING_INTERVAL == 0)) {
            log.info("RUNNING FREIGHT CONTRIB TO CALCULATE FREIGHT TOURS BASED ON CURRENT TRAVEL TIMES");
            runTourPlanning();
        } else {
            mapStartLinkOfToursToTour();
        }
    }

	/**
	 * Notifies all observers of the Controler that a iteration is finished
	 *
	 * @param event
	 */
	@Override
	public void notifyIterationEnds(IterationEndsEvent event) {
		if ((FREIGHTTOUR_PLANNING_INTERVAL < 1 && event.getIteration() < 1) || (event.getIteration() % FREIGHTTOUR_PLANNING_INTERVAL == 0)) {
            String dir = event.getServices().getConfig().controler().getOutputDirectory() + "/ITERS/it." + event.getIteration() + "/";
			log.info("writing carrier file of iteration " + event.getIteration() + " to " + dir);
            writeCarriers(this.carriers, dir + "carriers_it" + event.getIteration() + ".xml");
            writeCarriers(this.carriersWithOnlyUsedTours, dir + "carriersOnlyUsedTours_it" + event.getIteration() + ".xml");
		}
	}

    private void writeCarriers(Carriers carriers, String outputDir) {
		CarrierPlanXmlWriterV2 planWriter = new CarrierPlanXmlWriterV2(carriers);
		planWriter.write(outputDir);
	}
}
