/* *********************************************************************** *
 * project: org.matsim.*
 * Controler.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
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

package org.matsim.drtBlockings.analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierService;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.drtBlockings.FreightDrtActionCreator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ServicesConductedAnalysis implements ActivityStartEventHandler, IterationEndsListener {

	private Map<Id<CarrierService>, ActivityStartEvent> serviceStartEvents = new HashMap<>();
	private Carriers carriers;

	public ServicesConductedAnalysis (Carriers carriers){
		this.carriers = carriers;
	}

	@Override
	public void handleEvent(ActivityStartEvent event) {
		if(event.getActType().startsWith(FreightDrtActionCreator.SERVICE_ACTTYPE_PREFIX)){
			Id<CarrierService> serviceId = Id.create(event.getActType().substring(event.getActType().indexOf("_") + 1), CarrierService.class);
			if(!serviceStartEvents.containsKey(serviceId))
			this.serviceStartEvents.putIfAbsent(serviceId, event);
		}
	}

	@Override
	public void reset(int iteration) {
		this.serviceStartEvents.clear();
	}

	@Override
	public void notifyIterationEnds(IterationEndsEvent event) {
		try {
			writeAnalysis(event.getServices().getControlerIO().getIterationFilename(event.getIteration(), "conductedServices.csv"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void writeAnalysis(String outputFilePath) throws IOException {
		BufferedWriter writer = IOUtils.getBufferedWriter(outputFilePath);
		writer.write("carrierId;serviceId;endOfServiceTimeWindow;startTime;delay;personId");
		for (Carrier carrier : carriers.getCarriers().values()) {
			for (Id<CarrierService> serviceId : carrier.getServices().keySet()) {
				double endOfServiceTimeWindow = carrier.getServices().get(serviceId).getServiceStartTimeWindow().getEnd();
				writer.newLine();
				writer.write(carrier.getId() + ";" + serviceId + ";" + endOfServiceTimeWindow + ";");
				if(this.serviceStartEvents.containsKey(serviceId)){
					ActivityStartEvent event = serviceStartEvents.get(serviceId);
					double delay;
					if(event.getTime() > endOfServiceTimeWindow) {
						delay = event.getTime() - endOfServiceTimeWindow;
					} else {
						delay = 0;
					}
					writer.write(event.getTime() + ";" + delay + ";" + event.getPersonId());
				} else {
					writer.write("-;-;-");
				}
			}
		}
		writer.close();
	}
}
