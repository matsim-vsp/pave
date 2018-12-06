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
import java.util.Random;

import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.data.Vehicle;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierPlan;
import org.matsim.contrib.freight.carrier.CarrierPlanReader;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlReaderV2;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.carrier.ScheduledTour;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import privateAV.infrastructure.FreightTourToDvrpSchedule;

/**
 * @author tschlenther
 *
 */
public class SimpleFreightTourManager implements PrivateAVFreightTourManager {

	@Inject
	@Named(DvrpRoutingNetworkProvider.DVRP_ROUTING) 
	private Network network;
	
	private List<Schedule> freightTours = new ArrayList<Schedule>();
	
	/**
	 * 
	 */

	
	public SimpleFreightTourManager(String pathToCarrierFile) {
		Carriers carriers =  readCarriersFile(pathToCarrierFile);
		
		new SimpleFreightTourManager(carriers);
	}
	
	public SimpleFreightTourManager(Carriers carriers) {
		convertCarrierPlans(carriers);
	}

	private void convertCarrierPlans(Carriers carriers) {
		for(Carrier carrier : carriers.getCarriers().values()) {
			CarrierPlan plan = carrier.getSelectedPlan();
			for (ScheduledTour freightSchedule : plan.getScheduledTours()) {
				this.freightTours.add(FreightTourToDvrpSchedule.convert(freightSchedule, network));
			}
		}
	}

	private Carriers readCarriersFile(String pathToCarrierFile) {
		Carriers carriers = new Carriers();
		CarrierPlanXmlReaderV2 reader = new CarrierPlanXmlReaderV2(carriers);
		reader.readFile(pathToCarrierFile);
		return carriers;
	}

	/* (non-Javadoc)
	 * @see freight.manager.PrivateAVFreightTourManager#getAVFreightTours()
	 */
	@Override
	public List<Schedule> getAVFreightTours() {
		return this.freightTours;
	}

	/* (non-Javadoc)
	 * @see freight.manager.PrivateAVFreightTourManager#getRandomAVFreightTour()
	 */
	@Override
	public Schedule getRandomAVFreightTour() {
		return this.freightTours.get(new Random().nextInt(this.freightTours.size()));
	}

	/**
	 * at the moment, a random schedule is returned
	 */
	@Override
	public Schedule getBestAVFreightTourForVehicle(Vehicle vehicle) {
		return getRandomAVFreightTour();
	}

}
