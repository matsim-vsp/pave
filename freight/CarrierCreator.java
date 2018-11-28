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
package freight;

import java.util.Iterator;

import org.matsim.api.core.v01.Id;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierCapabilities.FleetSize;
import org.matsim.contrib.freight.carrier.CarrierImpl;
import org.matsim.contrib.freight.carrier.CarrierPlanReader;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlReaderV2;
import org.matsim.contrib.freight.carrier.CarrierVehicleType;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.contrib.freight.carrier.ScheduledTour;
import org.matsim.contrib.freight.carrier.Tour;
import org.matsim.contrib.freight.carrier.Tour.TourElement;

import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;

/**
 * @author tschlenther
 *
 */
public class CarrierCreator {
	Carriers carriers;
	
	public CarrierCreator() {
		
	}
	
	
	public Carriers parseCarriersFile(String path) {
		new CarrierPlanXmlReaderV2(this.carriers).readFile(path);
		return this.carriers;
	}
	
	public void test() {
		Iterator<ScheduledTour> it = this.carriers.getCarriers().get(Id.create("1", Carrier.class)).getPlans().get(0).getScheduledTours().iterator();
		ScheduledTour schedTour = it.next();
		schedTour.getDeparture();
		schedTour.getVehicle();
		Tour tour = schedTour.getTour();
		tour.getStart().getTimeWindow();
		
		tour.getStartLinkId();
		Iterator<TourElement> tElementIt = tour.getTourElements().iterator();
		
		while(tElementIt.hasNext()) {
			TourElement element = tElementIt.next();
			
			if(element instanceof TourActivity) {
				
				((TourActivity) element).getOperationTime();
				((TourActivity) element).getEndTime();
				
				
			}
		}
		
//		CarrierVehicleType avCarrierVehType = new CarrierVehicleType();
//		
//		Carrier carrier = CarrierImpl.newInstance(Id.create("dummyCarrier", Carrier.class));
//		carrier.getCarrierCapabilities().setFleetSize(FleetSize.FINITE);
//		carrier.getCarrierCapabilities().getCarrierVehicles().add(e)
	}
	
}
