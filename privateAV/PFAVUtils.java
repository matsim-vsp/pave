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

/**
 * @author tschlenther
 *
 * //TODO transfer (almost) all of this to config group
 */
public final class PFAVUtils {

	public static final String DEFAULT_CARRIERS_FILE = "input/mielecScenario/freight/upToDate/2carriers_a_5vehicles_INFINITE.xml";
	public static final String DEFAULT_VEHTYPES_FILE =  "input/mielecScenario/freight/upToDate/PFAVvehicleTypes.xml";

	/**
	 *
	 */
	public static final int DEFAULT_PFAV_CAPACITY = 4;

	/**
	 * the amount of time that is needed to rebuild/retool the vehicle. that means how much time is needed to change the module on top of the car body
	 */
    public static final double RETOOL_TIME_FOR_PFAVEHICLES = 15 * 60;

	/**
	 * the amount of time that the vehicle plans to arrive before it's owner ends the activity
	 */
    public static final double TIME_BUFFER = 5 * 60;


	/**
	 *
	 */
	public static final double FREIGHT_DEMAND_SAMPLE_SIZE = 1.;

	/**
	 * defines whether a TaxiEmptyDriveTask from the depot back to the owner is inserted when the freight tour gets scheduled.
	 * when true, the vehicle will immediately return to it's owner's activity location and wait there.
	 * if false, it might perform another freight tour after having performed the last PFAVServiceTask.
	 * TODO create JUnit test...
	 */
	public static boolean IMMEDIATE_RETURN_AFTER_FREIGHT_TOUR = false;
}
