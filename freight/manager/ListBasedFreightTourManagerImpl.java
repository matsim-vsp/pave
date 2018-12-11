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
import java.util.List;

import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.data.Vehicle;
import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.dvrp.schedule.StayTask;
import org.matsim.contrib.dvrp.schedule.StayTaskImpl;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierPlan;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlReaderV2;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.carrier.ScheduledTour;
import org.matsim.contrib.taxi.schedule.TaxiTask;
import org.matsim.core.gbl.MatsimRandom;

/**
 * @author tschlenther
 *
 */
public class ListBasedFreightTourManagerImpl implements ListBasedFreightTourManager {

	private List<List<StayTask>> freightActivities = new ArrayList<List<StayTask>>();
	private static final String DEFAULT_CARRIER_FILE = "C:/TU Berlin/MasterArbeit/input/Scenarios/mielec/freight/carrierPlans_routed.xml";

	private Network network;
	
	/**
	 * 
	 */
	public ListBasedFreightTourManagerImpl(Network network, String pathToCarriersFile) {
		this.network = network;
		pathToCarriersFile = (pathToCarriersFile == null) ?  DEFAULT_CARRIER_FILE : pathToCarriersFile;
		initWithCarrierPlansFile(pathToCarriersFile);
	}
	
	private Carriers readCarriersFile(String pathToCarrierFile) {
		Carriers carriers = new Carriers();
		CarrierPlanXmlReaderV2 reader = new CarrierPlanXmlReaderV2(carriers);
		reader.readFile(pathToCarrierFile);
		return carriers;
	}
	
	public void initWithCarrierPlansFile(String filePath) {
		Carriers carriers = readCarriersFile(filePath);
		this.freightActivities = convertCarrierPlansToTaskList(carriers);
	}
	
	private List<List<StayTask>> convertCarrierPlansToTaskList(Carriers carriers) {
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
	public List<List<StayTask>> getAVFreightTours() {
		return this.freightActivities;
	}

	/* (non-Javadoc)
	 * @see freight.manager.ListBasedFreightTourManager#getRandomAVFreightTour()
	 */
	@Override
	public List<StayTask> getRandomAVFreightTour() {
		if (this.freightActivities.size() == 0) return null;
		return( this.freightActivities.remove(( MatsimRandom.getRandom().nextInt(this.freightActivities.size()) )) );
	}

	/**
	 * at the moment, a random tour is returned
	 */
	@Override
	public List<StayTask> getBestAVFreightTourForVehicle(Vehicle vehicle) {
		// TODO Auto-generated method stub
		return getRandomAVFreightTour();
	}

}
