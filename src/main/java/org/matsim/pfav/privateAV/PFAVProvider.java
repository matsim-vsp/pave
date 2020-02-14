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

import com.google.inject.Provider;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.taxi.optimizer.TaxiOptimizer;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.contrib.taxi.scheduler.TaxiScheduleInquiry;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.mobsim.framework.MobsimTimer;

/**
 * @author tschlenther
 */
class PFAVProvider implements Provider<TaxiOptimizer> {
	private TaxiConfigGroup taxiCfg;
	private Fleet fleet;
	private TaxiScheduleInquiry scheduler;
	private MobsimTimer timer;
	private EventsManager eventsManager;
	//	private TravelDisutility travelDisutility;
	//	private Network network;

	PFAVProvider(TaxiConfigGroup taxiCfg, Fleet fleet, TaxiScheduleInquiry scheduler, MobsimTimer timer,
				 EventsManager eventsManager) {
		this.taxiCfg = taxiCfg;
		this.fleet = fleet;
		this.scheduler = scheduler;
		this.timer = timer;
		this.eventsManager = eventsManager;
	}

	@Override
	public TaxiOptimizer get() {

		if (!(scheduler instanceof PFAVScheduler)) {
			throw new IllegalArgumentException(
					"this OptimizerProvider can only work with a scheduler of type " + PFAVScheduler.class);
		}

		return new PFAVOptimizer(taxiCfg, fleet, (PFAVScheduler)scheduler, eventsManager, timer);
	}

}
