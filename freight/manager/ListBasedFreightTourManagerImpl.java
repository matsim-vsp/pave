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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.data.Vehicle;
import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.dvrp.schedule.StayTaskImpl;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierPlan;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlReaderV2;
import org.matsim.contrib.freight.carrier.CarrierVehicleTypes;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.carrier.ScheduledTour;
import org.matsim.contrib.taxi.data.TaxiRequest;
import org.matsim.contrib.taxi.optimizer.BestDispatchFinder;
import org.matsim.contrib.taxi.schedule.TaxiStayTask;
import org.matsim.contrib.taxi.schedule.TaxiTask;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.router.util.TravelTime;

import freight.FreightTourCalculator;

/**
 * @author tschlenther
 *
 */
public class ListBasedFreightTourManagerImpl implements ListBasedFreightTourManager {

	private final static Logger log = Logger.getLogger(ListBasedFreightTourManagerImpl.class);
	
	private List<List<StayTask>> freightActivities = new ArrayList<List<StayTask>>();
	
	private FreightTourCalculator tourCalculator;

	private Network network;
	
	/**
	 * 
	 */
	public ListBasedFreightTourManagerImpl(Network network, String pathToCarriersFile, String pathToVehTypesFile) {
		this.network = network;
		this.tourCalculator = new FreightTourCalculator(network, pathToCarriersFile, pathToVehTypesFile);
		
		//TODO:maybe switch that off here and 
		// a) call runTourPlanning() method
		// b) let this.freightActivities() be either empty or uninitiated
		this.freightActivities = convertCarrierPlansToTaskList(tourCalculator.getCarriers());
	}
	
	public ListBasedFreightTourManagerImpl(Network network, Carriers carriers, CarrierVehicleTypes vehTypes) {
		this.network = network;
		this.tourCalculator = new FreightTourCalculator(network, carriers, vehTypes);
		
		//TODO:maybe switch that off here and 
				// a) call runTourPlanning() method
				// b) let this.freightActivities() be either empty or uninitiated
		this.freightActivities = convertCarrierPlansToTaskList(tourCalculator.getCarriers());
	}
	
	private List<List<StayTask>> convertCarrierPlansToTaskList(Carriers carriers) {
		
		log.info("start converting carrier plans to taxi tasks..");
		
		List<List<StayTask>> freightTours = new ArrayList<List<StayTask>>();
		
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
		return this.freightActivities;
	}

	/* (non-Javadoc)
	 * @see freight.manager.ListBasedFreightTourManager#getRandomAVFreightTour()
	 */
	@Override
	public List<StayTask> getRandomPFAVTour() {
		if (this.freightActivities.size() == 0) return null;
		return( this.freightActivities.remove(( MatsimRandom.getRandom().nextInt(this.freightActivities.size()) )) );
	}

	/**
	 * at the moment, a random tour is returned
	 */
	@Override
	public List<StayTask> getBestPFAVTourForVehicle(Vehicle vehicle) {
		// TODO Auto-generated method stub
		return getRandomPFAVTour();
	}

	public void runTourPlanning(TravelTime travelTime) {
		this.tourCalculator.run(travelTime);
		log.info("overriding list of PFAV schedules...");
		this.freightActivities = convertCarrierPlansToTaskList(tourCalculator.getCarriers());
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
