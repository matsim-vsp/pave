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
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.dvrp.trafficmonitoring.DvrpTravelTimeModule;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.events.IterationStartsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.controler.listener.IterationStartsListener;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.router.util.TravelTime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tschlenther
 *
 */
public class ListBasedFreightTourManagerImpl implements ListBasedFreightTourManager, IterationStartsListener, IterationEndsListener {

	private final static Logger log = Logger.getLogger(ListBasedFreightTourManagerImpl.class);
	
    /**
     * the freight contrib will be run before every iteration where iterationNumber % FREIGHTTOUR_PLANNING_INTERVAL == 0- \n
	 * if FREIGHTTOUR_PLANNING_INTERVAL is set to 0 or any negative integer, the freight contrib will run only before iteration 0.
     */
    private final int FREIGHTTOUR_PLANNING_INTERVAL = 1;
	private List<List<StayTask>> freightTours = new ArrayList<>();
	@Inject
	@Named(DvrpRoutingNetworkProvider.DVRP_ROUTING)
	private Network network;
	@Inject
	@Named(DvrpTravelTimeModule.DVRP_ESTIMATED)
	private TravelTime travelTime;
    private ArrayList<Link> depotLinks;
    private Carriers carriers;
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
		
		log.info("start converting carrier plans to taxi tasks..");
		
		List<List<StayTask>> freightTours = new ArrayList<>();
		
		for(Carrier carrier : carriers.getCarriers().values()) {
			CarrierPlan plan = carrier.getSelectedPlan();
			for (ScheduledTour freightTour : plan.getScheduledTours()) {
				freightTours.add(ConvertFreightTourForDvrp.convertToList(freightTour, this.network));
			}
		}
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
		if (this.freightTours.size() == 0) return null;
		return( this.freightTours.remove(( MatsimRandom.getRandom().nextInt(this.freightTours.size()) )) );
	}

	/**
	 * at the moment, a random tour is returned
	 */
	@Override
	public List<StayTask> getBestPFAVTourForVehicle(DvrpVehicle vehicle) {
		// TODO: dispatch tours
		return getRandomPFAVTour();
	}

//	private List<StayTask> getDispatchedTourForVehicle(Vehicle vehicle){
//		
//		List<Id<Link>> list = new ArrayList<Id<Link>>();
//		
//		StraightLineKnnFinder<Link, Charger> straightLineKnnFinder = new StraightLineKnnFinder(2, l -> (Link) l, CHARGER_TO_LINK);
////		List<Charger> nearestChargers = straightLineKnnFinder.findNearest(stopLocation, chargingInfrastructure.getChargers().values().stream().filter(charger -> ev.getChargingTypes().contains(charger.getChargerType())));
//		
//		
//		Coord objectCoord = Schedules.getLastLinkInSchedule(vehicle).getCoord();
//		Stream<Id<Link>> neighbours = list.stream();
//		
//		PartialSort<Id<Link>> nearestRequestSort = new PartialSort<Id<Link>>(3);
//
//		for(Link l : this.depotLinks) {
//			nearestRequestSort.add(l.getId(), DistanceUtils.calculateDistance(objectCoord, l.getCoord()));
//		}
//		
//		List<>nearestRequestSort.kSmallestElements();
//		
////		PartialSort.kSmallestElements(3, neighbours,
////				n -> DistanceUtils.calculateSquaredDistance(objectCoord, neighbourToLink.apply(n).getCoord()));
//	}
	
	private void runTourPlanning() {

	    FreightTourCalculatorImpl tourCalculator = new FreightTourCalculatorImpl();

        //TODO: I'm not sure whether the travelTime object represents the current travel times of current iteration as it is injected...
        this.carriers = tourCalculator.runTourPlanningForCarriers(this.carriers, this.vehicleTypes, this.network, this.travelTime);

		log.info("overriding list of PFAV schedules...");
		this.freightTours = convertCarrierPlansToTaskList(carriers);
		
		this.depotLinks = new ArrayList<Link>();
		for(Carrier carrier : carriers.getCarriers().values()) {
			for(CarrierVehicle veh : carrier.getCarrierCapabilities().getCarrierVehicles()) {
				if(! this.depotLinks.contains(veh.getLocation())){
					this.depotLinks.add(network.getLinks().get(veh.getLocation()));			// this is the depot link id .... TODO: check if that is correct!
				} 
			}
		}
	}

    @Override
    public void notifyIterationStarts(IterationStartsEvent event){
		if(FREIGHTTOUR_PLANNING_INTERVAL < 1){
			if(event.getIteration() < 1){
				runTourPlanning();
				return;
			}
		} else if(event.getIteration() % FREIGHTTOUR_PLANNING_INTERVAL == 0){
			log.info("RUNNING FREIGHT CONTRIB TO CALCULATE FREIGHT TOURS BASED ON CURRENT TRAVEL TIMES");
			runTourPlanning();
		}
    }

	void writeCarriers(String outputDir) {
		CarrierPlanXmlWriterV2 planWriter = new CarrierPlanXmlWriterV2(carriers);
		planWriter.write(outputDir);
	}

	/**
	 * Notifies all observers of the Controler that a iteration is finished
	 *
	 * @param event
	 */
	@Override
	public void notifyIterationEnds(IterationEndsEvent event) {
		String dir = event.getServices().getConfig().controler().getOutputDirectory() + "carriers_it" + event.getIteration() + ".xml";
		log.info("writing carrier file of iteration " + event.getIteration() + " to " + dir);
		writeCarriers(dir);
	}

	//folgendes kommt aus dem RuleBasedOptimizer
	//Fragen/AUfgaben
	//TODO
	/*
	 * 1) kann/will ich auch sone registry benutzen?
	 * 2) baue eine Map ein: Carrier zu ihren Depots bzw. führe Liste über die location von depots
	 * 3) finde die nächsten 3?! Depots zum Fahrzeug
	 * 4) iteriere drüber und frage ob es eine Tour gibt, die das Fahrzeug schafft bis Herrchen wieder da ist / gegebene Rückkehrzeit 
	 */
	
//	
//	// vehicle-initiated scheduling
//		private void scheduleIdleVehiclesImpl(Collection<TaxiRequest> unplannedRequests) {
//			Iterator<Vehicle> vehIter = idleTaxiRegistry.vehicles().iterator();
//			while (vehIter.hasNext() && !unplannedRequests.isEmpty()) {
//				Vehicle veh = vehIter.next();
//				Link link = ((TaxiStayTask)veh.getSchedule().getCurrentTask()).getLink();
//
//				Stream<TaxiRequest> selectedReqs = unplannedRequests.size() > params.nearestRequestsLimit
//						? unplannedRequestRegistry.findNearestRequests(link.getToNode(), params.nearestRequestsLimit)
//						: unplannedRequests.stream();
//
//				BestDispatchFinder.Dispatch<TaxiRequest> best = dispatchFinder.findBestRequestForVehicle(veh, selectedReqs);
//
//				scheduler.scheduleRequest(best.vehicle, best.destination, best.path);
//
//				unplannedRequests.remove(best.destination);
//				unplannedRequestRegistry.removeRequest(best.destination);
//			}
//		}

}
