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

import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.taxi.schedule.TaxiDropoffTask;

/**
 * @author tschlenther
 *
 * //TODO transfer (almost) all of this to a new type of config group
 */
public final class PFAVUtils {

    /**
     * represents the latest start time of a freight tour. that means, the retool task of a freight tour has to start before
     * FREIGHTTOUR_LATEST_START. in the manager, the path to the depot is calculated. if the above mentioned condition is not fulfilled
     * the freight tour will not be dispatched to the vehicle (with reason: not enough time to perform the tour)
     */
    public static final double FREIGHTTOUR_LATEST_START = 18 * 3600;

    /**
     * represents the earliest start time of a freight tour. that means, the retool task of a freight tour must not start before
     * FREIGHTTOUR_EARLIEST_START. in the manager, the path to the depot is calculated. if needed, a stay task at the depot is inserted so that the PFAV waits in case.
     */
    public static final double FREIGHTTOUR_EARLIEST_START = 10 * 3600;

	public static final String PFAV_ID_SUFFIX = "_PFAV";

	public static final String DEFAULT_VEHTYPES_FILE = "input/PFAVvehicleTypes.xml";

	/**
	 *
	 */
	public static final int DEFAULT_PFAV_CAPACITY = 1;

	/**
	 * the amount of time that is needed to rebuild/retool the vehicle. that means how much time is needed to change the module on top of the car body
	 */
    public static final double RETOOL_TIME_FOR_PFAVEHICLES = 15 * 60;

	/**
	 * the amount of time that the vehicle wants to arrive before it's owner ends the activity
	 */
    public static final double TIME_BUFFER = 5 * 60;

    /**
     * can be used to cut down the amount of freight tours on the manager's to do list for the iteration.
     * The set of freight tours remains constant until freight tour calculation is run for the next time, see FREIGHTTOUR_PLANNING_INTERVAL.
	 */
	public static final double FREIGHT_DEMAND_SAMPLE_SIZE = 1.;

    /**
     * the freight contrib will be run before every iteration where iterationNumber % FREIGHTTOUR_PLANNING_INTERVAL == 0- \n
     * if FREIGHTTOUR_PLANNING_INTERVAL is set to 0 or any negative integer, the freight contrib will run only before iteration 0.
     */
    public static final int FREIGHTTOUR_PLANNING_INTERVAL = 1;

	/**
	 * defines whether the freight tour manager triggers the JSprit run in iteration 0
	 */
	public static final boolean RUN_TOUR_PLANNING_BEFORE_FIRST_ITERATION = true;

	/**
	 * defines the number of iterations in the tour planning algorithm, see {@link freight.calculator.FreightTourCalculatorImpl}
	 */
    public static final int NR_OF_JSPRIT_ITERATIONS = 300;

    /**
     * this is a switch, which determines whether the manager is holding depots in it's map that have no tour (anymore) to serve.
     *
     * set this to true for usecases in which you look only at a subset of depots. in order to have a correct dispatch, you need to have     *
     * a) one carrier per depot
     * b) all depots in the initial carrier file
     * c) only those carriers that you want to look at containing services
     *
     * set this to false if you want to enable long dispatch distances. setting this to false means that, over day time, the manager's set of depot
     * shrinks. consequently, the spatial dispatch algorithm allows longer distance's to depot's as it considers the closest three depots that have a tour left to serve.
     * For example: the closest two depots to a vehicle that has requested a tour are already fully served /have no tour left to serve...
     *
     */
    public static final boolean ALLOW_EMPTY_TOUR_LISTS_FOR_DEPOTS = true;

	/**
	 * defines whether a TaxiEmptyDriveTask from the depot back to the owner is inserted when the freight tour gets scheduled.
	 * when true, the vehicle will immediately return to it's owner's activity location and wait there.
	 * if false, it might perform another freight tour after having performed the last PFAVServiceTask.
	 * TODO create JUnit test...
	 */
	public static boolean IMMEDIATE_RETURN_AFTER_FREIGHT_TOUR = false;

	public static TaxiDropoffTask getLastPassengerDropOff(Schedule schedule) {
		for (int i = schedule.getTasks().size() - 1; i >= 0; i--) {
			Task task = schedule.getTasks().get(i);
			if (task instanceof TaxiDropoffTask) return (TaxiDropoffTask) task;
		}
		return null;
	}

}
