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

import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.passenger.PassengerEngine;
import org.matsim.contrib.dvrp.passenger.SinglePassengerDropoffActivity;
import org.matsim.contrib.dvrp.passenger.SinglePassengerPickupActivity;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.dvrp.tracker.OnlineTrackerListener;
import org.matsim.contrib.dvrp.vrpagent.VrpAgentLogic;
import org.matsim.contrib.dvrp.vrpagent.VrpLegFactory;
import org.matsim.contrib.dynagent.DynAction;
import org.matsim.contrib.dynagent.DynAgent;
import org.matsim.contrib.dynagent.IdleDynActivity;
import org.matsim.contrib.taxi.optimizer.TaxiOptimizer;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.contrib.taxi.schedule.TaxiDropoffTask;
import org.matsim.contrib.taxi.schedule.TaxiPickupTask;
import org.matsim.contrib.taxi.schedule.TaxiStayTask;
import org.matsim.contrib.taxi.schedule.HasTaxiTaskType;
import org.matsim.core.mobsim.framework.MobsimTimer;

/**
 * @author tschlenther
 *
 */
public final class PFAVActionCreator implements VrpAgentLogic.DynActionCreator {

	public static final String PICKUP_ACTIVITY_TYPE = "TaxiPickup";
	static final String DROPOFF_ACTIVITY_TYPE = "TaxiDropoff";
	static final String STAY_ACTIVITY_TYPE = "TaxiStay";
	
	public static final String SERVICE_ACTIVITY_TYPE = "service";
	static final String RETOOL_ACTIVITY_TYPE = "retool";

	private final PassengerEngine passengerEngine;
	private final VrpLegFactory legFactory;
	private final double pickupDuration;

	PFAVActionCreator(PassengerEngine passengerEngine, TaxiConfigGroup taxiCfg,
					  TaxiOptimizer optimizer, MobsimTimer timer, DvrpConfigGroup dvrpCfg) {
		this(passengerEngine, taxiCfg.isOnlineVehicleTracker() ?
						v -> VrpLegFactory.createWithOnlineTracker(dvrpCfg.getMobsimMode(), v, OnlineTrackerListener.NO_LISTENER, timer) :
						v -> VrpLegFactory.createWithOfflineTracker(dvrpCfg.getMobsimMode(), v, timer),
				taxiCfg.getPickupDuration());
	}

    private PFAVActionCreator(PassengerEngine passengerEngine, VrpLegFactory legFactory, double pickupDuration) {
		this.passengerEngine = passengerEngine;
		this.legFactory = legFactory;
		this.pickupDuration = pickupDuration;
	}

	@Override
	public DynAction createAction(DynAgent dynAgent, DvrpVehicle vehicle, double now) {
		Task task = vehicle.getSchedule().getCurrentTask();
		switch (((HasTaxiTaskType.TaxiTaskType)task.getTaskType())) {
		case EMPTY_DRIVE:
		case OCCUPIED_DRIVE:
			return legFactory.create(vehicle);

		case PICKUP:
			final TaxiPickupTask pst = (TaxiPickupTask)task;
			return new SinglePassengerPickupActivity(passengerEngine, dynAgent, pst, pst.getRequest(), PICKUP_ACTIVITY_TYPE);

		case DROPOFF:
			
			if(task instanceof TaxiDropoffTask) {
				final TaxiDropoffTask dst = (TaxiDropoffTask)task;
				return new SinglePassengerDropoffActivity(passengerEngine, dynAgent, dst, dst.getRequest(),
						DROPOFF_ACTIVITY_TYPE);
			} else if( task instanceof PFAVServiceTask) {
                return new PFAVServiceActivity(SERVICE_ACTIVITY_TYPE, (PFAVServiceTask) task, vehicle.getId());
			} else if(task instanceof PFAVRetoolTask) {
				return new IdleDynActivity(RETOOL_ACTIVITY_TYPE, task::getEndTime);
			} else {
				throw new IllegalStateException();
			}

		case STAY:
			if(task instanceof TaxiStayTask) {
				return new IdleDynActivity(STAY_ACTIVITY_TYPE, task::getEndTime);
			} else {
				throw new IllegalStateException();
			}
			

		default:
			throw new IllegalStateException();
		}

	}

	

}
