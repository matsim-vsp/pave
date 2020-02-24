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

package org.matsim.pfav.privateAV;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.matsim.api.core.v01.Id;
import org.matsim.contrib.freight.carrier.*;

/**
 * this class should test whether the tour planning reacts to travel times. the chessboard scenario is used.
 * one PFAV is inserted that is supposed to perform a freight tour with three service activities. the carrier has four service activities.
 * since tour planning is run before the mobsim,in the 0th iteration, the PFAV should get into congestion while travelling towards
 * it's last service activity. in the next generation, the tour planning is supposed to react and change the last service activity in the tour.
 */
public class FreightReactionToTravelTimesTest {

	private static String OUTPUT;


	@BeforeClass
	public static void runChessboardScenario() {
		RunChessboardScenarioForTest testRunner =
				new RunChessboardScenarioForTest(FreightReactionToTravelTimesTest.class, 2, CarrierCapabilities.FleetSize.FINITE);
		OUTPUT = testRunner.getOutputDir();
		testRunner.run();
	}

	@Test
	public final void testFreightReactionToTravelTimes() {
		Carriers carriers = new Carriers();
		CarrierPlanXmlReader reader = new CarrierPlanXmlReader(carriers);
		reader.readFile(OUTPUT + "ITERS/it.1/carriers_it1.xml");

		Assert.assertEquals("there should be only one carrier", 1, carriers.getCarriers().size());

		for (Carrier carrier : carriers.getCarriers().values()) {
			Assert.assertEquals("the carrier should have 2 plans after iteration 1 ", 2, carrier.getPlans().size());

			CarrierPlan selectedPlan = carrier.getSelectedPlan();
			Assert.assertEquals("there should be only one tour in the selected carrier plan", 1,
					selectedPlan.getScheduledTours().size());

			ScheduledTour tour = (ScheduledTour) selectedPlan.getScheduledTours().toArray()[0];
			Tour.ServiceActivity act = (Tour.ServiceActivity) tour.getTour()
					.getTourElements()
					.get(tour.getTour().getTourElements().size() - 2);

			Id<CarrierService> expectedID = Id.create(3, CarrierService.class);

			Assert.assertEquals(" after iteration 1, the last service served by the PFAV should have id=3", expectedID,
					act.getService().getId());

			CarrierPlan firstPlan = carrier.getPlans().get(0);
			CarrierPlan secondPlan = carrier.getPlans().get(1);

			ScheduledTour firstPlanTour = (ScheduledTour) firstPlan.getScheduledTours().toArray()[0];
			Tour.ServiceActivity firstPlanAct = (Tour.ServiceActivity) firstPlanTour.getTour()
					.getTourElements()
					.get(firstPlanTour.getTour().getTourElements().size() - 2);

			ScheduledTour secondPlanTour = (ScheduledTour) secondPlan.getScheduledTours().toArray()[0];
			Tour.ServiceActivity secondPlanAct = (Tour.ServiceActivity) secondPlanTour.getTour()
					.getTourElements()
					.get(secondPlanTour.getTour().getTourElements().size() - 2);

			//basically, we test whether the order in which the tours are dispatched changes..
			Assert.assertNotEquals("the last services of the carrier plans for iteration 0 and 1 should be different",
					secondPlanAct.getService().getId(), firstPlanAct.getService().getId());
		}

	}

}
