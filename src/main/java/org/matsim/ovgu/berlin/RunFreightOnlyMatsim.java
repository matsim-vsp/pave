/*
  * *********************************************************************** *
  * project: org.matsim.*
  * *********************************************************************** *
  *                                                                         *
  * copyright       : (C) 2020 by the members listed in the COPYING,        *
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
  * *********************************************************************** *
 */
package org.matsim.ovgu.berlin;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.Freight;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.carrier.Tour.Leg;
import org.matsim.contrib.freight.controler.CarrierModule;
import org.matsim.contrib.freight.jsprit.NetworkBasedTransportCosts;
import org.matsim.contrib.freight.jsprit.NetworkRouter;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlansConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleUtils;

import javax.management.InvalidAttributeValueException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This is a short an easy version to run MATSim freight scenarios .
 * 
 * Optional it is possible to run MATSim after tour planning.
 * 
 * @author kturner
 * 
 */
public class RunFreightOnlyMatsim {

	private static final Logger log = Logger.getLogger(RunFreightOnlyMatsim.class);

	public static void main(String[] args) throws IOException, InvalidAttributeValueException {

		for (String arg : args) {
			log.info(arg);
		}

		if (args.length == 0) {
			String inputPath = "input/1pc/";
			args = new String[] { inputPath + "scenario-A.15.networkChangeEvents.xml.gz", "OutputKMT/OVGU/DemoRun" };
		}

		Config config = prepareConfig(args);
		Scenario scenario = prepareScenario(config);
		Controler controler = prepareControler(scenario);

		controler.run();
	}

	private static Config prepareConfig(String[] args) {
		String networkChangeEventsFileLocation = args[0];
		String outputLocation = args[1];

		Config config = ConfigUtils.createConfig();
		config.controler()
				.setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
		config.global().setRandomSeed(4177);
		config.controler().setLastIteration(0);
		config.controler().setOutputDirectory(outputLocation);

		config.network().setInputFile(
				"https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.4-10pct/input/berlin-v5-network.xml.gz");

		if (networkChangeEventsFileLocation != "") {
			log.info("Setting networkChangeEventsInput file: " + networkChangeEventsFileLocation);
			config.network().setTimeVariantNetwork(true);
			config.network().setChangeEventsInputFile(networkChangeEventsFileLocation);
		}

		config.plans().setActivityDurationInterpretation(
				PlansConfigGroup.ActivityDurationInterpretation.tryEndTimeThenDuration);
		// freight configstuff
		FreightConfigGroup freightConfigGroup = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
		freightConfigGroup.setTravelTimeSliceWidth(900);
		freightConfigGroup.setTimeWindowHandling(FreightConfigGroup.TimeWindowHandling.enforceBeginnings);

		return config;
	}

	private static Scenario prepareScenario(Config config) {
		Scenario scenario = ScenarioUtils.loadScenario(config);

		Carriers carriers = FreightUtils.getOrCreateCarriers(scenario);

		createAndaddCarriers(scenario.getNetwork(), carriers);

		createAndLoadVehicleType(scenario);

		routePlans(scenario);

		return scenario;
	}

	private static void routePlans(Scenario scenario) {
		Carriers carriers = FreightUtils.getOrCreateCarriers(scenario);

		for (Carrier carrier : carriers.getCarriers().values()) {
			NetworkBasedTransportCosts.Builder tpcostsBuilder = NetworkBasedTransportCosts.Builder
					.newInstance(scenario.getNetwork(), carrier.getCarrierCapabilities().getVehicleTypes());
			tpcostsBuilder.setTimeSliceWidth(900);
			// assign netBasedCosts to RoutingProblem
			NetworkBasedTransportCosts netbasedTransportcosts = tpcostsBuilder.build();
			NetworkRouter.routePlan(carrier.getSelectedPlan(), netbasedTransportcosts);
		}
	}

	private static void createAndaddCarriers(Network network, Carriers carriers) {

		String[] linkIDs = new String[] { "9826", "10837", "37615", "122985", "34319", "97538", "82306", "113960",
				"76890", "64709", "18863", "14787", "116212", "63691", "30311", "76811", "20545", "142877", "118271",
				"29572" };

		double[] expectedAvgTT = new double[] { 0, 1386.78514, 461.0143096, 660.7601614, 482.7117253, 531.2474148,
				615.2137168, 636.927544, 524.2512817, 773.1225155, 800.9296293, 404.1527514, 525.0083581,
				599.2197634, 593.5771799, 760.3694996, 389.5929394, 753.3953592, 605.2273493, 837.7488374 };
		
		double serviceTime = 2 * 60;
		
		// create one carrier for each our
		for (int i = 0; i < 24; i++) {
			Carrier carrier = CarrierUtils.createCarrier(Id.create("carrier" + i, Carrier.class));
			createAndAddCarrierSerivces(carrier, i, linkIDs, expectedAvgTT, serviceTime);

			CarrierVehicle carrierVehicle = createCarrierVehicle("vehicle", "9826", i);
			CarrierUtils.addCarrierVehicle(carrier, carrierVehicle);

			CarrierPlan carrierPlan = createPlan(network, carrier, i);
			carrier.setSelectedPlan(carrierPlan);

			carriers.addCarrier(carrier);
		}

	}

	private static void createAndAddCarrierSerivces(Carrier carrier, int hour, String[] linkIDs, double[] expectedTT,
			double serviceTime) {
//		
		double tourStartInSec = hour * 3600.;
//
		// calculate expected arrival times from expected travel times
		double[] expectedArrival = new double[expectedTT.length];
		expectedArrival[0] = tourStartInSec + expectedTT[0];
		for (int x = 1; x < expectedArrival.length; x++)
			expectedArrival[x] = expectedArrival[x - 1] + serviceTime + expectedTT[x];

		// create customer services at origin and destination
		for (int customer = 0; customer < linkIDs.length / 2; customer++) {
			int x = customer * 2;

			Id<CarrierService> customerOriginID = Id.create("c" + (customer + 1) + "-origin", CarrierService.class);
			CarrierUtils.addService(carrier, createService(customerOriginID, linkIDs[x], expectedArrival[x]));

			Id<CarrierService> customerDestID = Id.create("c" + (customer + 1) + "-dest", CarrierService.class);
//			CarrierUtils.addService(carrier, createService(customerDestID, linkIDs[x+1], expectedArrival[x+1]));
			// set possibility to start service (drop-off) at destination earlier than expected:
			CarrierUtils.addService(carrier, createService(customerDestID, linkIDs[x + 1], expectedArrival[x]));
		}
	}

	// Erstellt Service entsprechend der vorgebenen Werte. Ermittelt auch
	// automatisch die korrekte "ServiceBeginn" Zeit -> Fahrzeuge, die eher
	// ankommen, sollen warten!
	private static CarrierService createService(Id<CarrierService> id, String linkId, Double startTimeWindowBegin) {
		CarrierService service = CarrierService.Builder.newInstance(id, Id.createLinkId(linkId)).setCapacityDemand(1)
				.setServiceDuration(1) // Muss mindestens 1 Sekunde lang sein, da sonst TimeWindowEnforcement
										// (basierend auf WithinDay-Replanning) nicht geht.
				.setServiceStartTimeWindow(TimeWindow.newInstance(startTimeWindowBegin, 36. * 3600)).build();
		return service;
	}

	private static CarrierVehicle createCarrierVehicle(String vehicleId, String locationId, int i) {
		CarrierVehicle carrierVehicle = CarrierVehicle.Builder
				.newInstance(Id.createVehicleId(vehicleId), Id.createLinkId(locationId)).setEarliestStart(i * 3600)
				.setTypeId(Id.create("vehType", VehicleType.class)).build();
		return carrierVehicle;
	}

	private static void createAndLoadVehicleType(Scenario scenario) {
		VehicleType vehType = VehicleUtils.createVehicleType(Id.create("vehType", VehicleType.class));
		vehType.setMaximumVelocity(50.0); // m/s -> soll nicht limitiert sein
		vehType.getCapacity().setOther(30);
		vehType.getCostInformation().setCostsPerMeter(0.00033);
		vehType.getCostInformation().setCostsPerSecond(0.0049);
		vehType.getCostInformation().setFixedCost(42.33);

		FreightUtils.getCarrierVehicleTypes(scenario).getVehicleTypes().put(vehType.getId(), vehType);

		new CarrierVehicleTypeLoader(FreightUtils.getCarriers(scenario))
				.loadVehicleTypes(FreightUtils.getCarrierVehicleTypes(scenario));
	}

	private static CarrierPlan createPlan(Network network, Carrier carrier, int i) {

		CarrierVehicle vehicle = CarrierUtils.getCarrierVehicle(carrier, Id.createVehicleId("vehicle"));
		Id<Link> depotLocation = vehicle.getLocation();
		double depTime = i * 3600.;

		Tour.Builder tourBuilder = Tour.Builder.newInstance();

		tourBuilder.scheduleStart(depotLocation, TimeWindow.newInstance(depTime, Double.MAX_VALUE));
		
		for (int customer = 1; customer <= 10; customer++) {
			tourBuilder.addLeg(new Leg());
			tourBuilder.scheduleService(
					CarrierUtils.getService(carrier, Id.create("c" + customer + "-origin", CarrierService.class)));
			tourBuilder.addLeg(new Leg());
			tourBuilder.scheduleService(
					CarrierUtils.getService(carrier, Id.create("c" + customer + "-dest", CarrierService.class)));
		}
		
		
		tourBuilder.addLeg(new Leg());

		tourBuilder.scheduleEnd(depotLocation);

		Tour vehicleTour = tourBuilder.build();

		assert (vehicleTour.getTourElements().size() == carrier.getServices().size() + 2); // Alle Services + Start und
																							// Ende

		ScheduledTour scheduledTour = ScheduledTour.newInstance(vehicleTour, vehicle, depTime);

		Collection<ScheduledTour> scheduledTours = new ArrayList<>();
		scheduledTours.add(scheduledTour);
		CarrierPlan plan = new CarrierPlan(carrier, scheduledTours);

		return plan;
	}

	private static Controler prepareControler(Scenario scenario) {
		Controler controler = new Controler(scenario);

		Freight.configure(controler);

		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				install(new CarrierModule());
			}
		});

		return controler;
	}

}
