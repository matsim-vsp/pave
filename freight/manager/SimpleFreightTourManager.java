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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierPlan;
import org.matsim.contrib.freight.carrier.CarrierPlanReader;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlReaderV2;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.carrier.ScheduledTour;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelTime;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * @author tschlenther
 *
 */
public class SimpleFreightTourManager implements PrivateAVFreightTourManager {

	private static final String DEFAULT_CARRIER_FILE = "C:/TU Berlin/MasterArbeit/input/Scenarios/mielec/freight/carrierPlans_routed.xml";
	
	@Inject
	@Named(DvrpRoutingNetworkProvider.DVRP_ROUTING) 
	private Network network;
	
	private LeastCostPathCalculator router;
	private TravelTime travelTime;
	
	private List<Schedule> freightTours = new ArrayList<Schedule>();
	

	public SimpleFreightTourManager(Network network, LeastCostPathCalculator router, TravelTime travelTime, String pathToCarriersFile) {
		this.network = network;
		this.router = router;
		this.travelTime = travelTime;
		
		pathToCarriersFile = (pathToCarriersFile == null) ?  DEFAULT_CARRIER_FILE : pathToCarriersFile;
		Carriers carriers =  readCarriersFile(pathToCarriersFile);
		this.freightTours = convertCarrierPlansToTaxiSchedules(carriers);
	}

	public void initWithCarrierPlansFile(String filePath) {
		Carriers carriers = readCarriersFile(filePath);
		this.freightTours = convertCarrierPlansToTaxiSchedules(carriers);
	}
	
	private List<Schedule> convertCarrierPlansToTaxiSchedules(Carriers carriers) {
		List<Schedule> freightTours = new ArrayList<Schedule>();
		
		for(Carrier carrier : carriers.getCarriers().values()) {
			CarrierPlan plan = carrier.getSelectedPlan();
			for (ScheduledTour freightSchedule : plan.getScheduledTours()) {
				freightTours.add(ConvertFreightTourForDvrp.convert2(freightSchedule, this.network, this.router, this.travelTime));
			}
		}
		
		return freightTours;
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
		return this.freightTours.remove(new Random().nextInt(this.freightTours.size()));
	}

	/**
	 * at the moment, a random schedule is returned
	 */
	@Override
	public Schedule getBestAVFreightTourForVehicle(DvrpVehicle vehicle) {
		return getRandomAVFreightTour();
	}

}
