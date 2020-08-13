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
package org.matsim.ovgu.berlin.simulation;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.contrib.freight.Freight;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.carrier.Tour;
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
import org.matsim.core.router.costcalculators.TravelDisutilityFactory;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.LeastCostPathCalculatorFactory;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.router.util.LeastCostPathCalculator.Path;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.ovgu.berlin.Settings;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleUtils;

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
public class FreightOnlyMatsim {

	private static final Logger log = Logger.getLogger(FreightOnlyMatsim.class);
	private Settings settings;

	public FreightOnlyMatsim(Settings settings) {
		this.settings = settings;

		if (settings.tour == null) {
			settings.tour = new String[] {};
		}

		Config config = prepareConfig(settings.pathChangeEvents, settings.directory);
		Config noChangeEvents = prepareConfig("", settings.directory);
		Scenario scenario = prepareScenario(config, noChangeEvents);
		Controler controler = prepareControler(scenario);

		controler.run();
		preparePathCalculator(controler);
	}

	private Network network;
	private LeastCostPathCalculator calc;

	private void preparePathCalculator(Controler controler) {
		controler.getTripRouterProvider();
		LeastCostPathCalculatorFactory calcFac = controler.getLeastCostPathCalculatorFactory();
		TravelDisutilityFactory disuFac = controler.getTravelDisutilityFactory();
		TravelTime travelTimes = controler.getLinkTravelTimes();
		TravelTime timeCalculator = controler.getLinkTravelTimes();

		TravelDisutility travelCosts = disuFac.createTravelDisutility(timeCalculator);

		network = controler.getScenario().getNetwork();
		calc = calcFac.createPathCalculator(network, travelCosts, travelTimes);
	}

	public Path getTravelPath(Node from, Node to, double departure) {
		double day = 24 * 60 * 60;
		departure = departure % day;

		return calc.calcLeastCostPath(from, to, departure, null, null);
	}

	public Path getTravelPathWithFromNodes(String from, String to, double departure) {
		return getTravelPathWithFromNodes(Id.createLinkId(from), Id.createLinkId(to), departure);
	}

	public Path getTravelPathWithFromNodes(Id<Link> from, Id<Link> to, double departure) {
		return getTravelPath(network.getLinks().get(from).getFromNode(), network.getLinks().get(to).getFromNode(),
				departure);
	}

	public Path getTravelPath(long from, long to, double departure) {
		return getTravelPath(Id.createNodeId(from), Id.createNodeId(to), departure);
	}

	public Path getTravelPath(Id<Node> from, Id<Node> to, double departure) {
		return getTravelPath(network.getNodes().get(from), network.getNodes().get(to), departure);
	}

	private Config prepareConfig(String networkChangeEventsFileLocation, String outputLocation) {

		Config config = ConfigUtils.createConfig();
		config.controler()
				.setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
		config.global().setRandomSeed(4177);
		config.controler().setLastIteration(0);
		config.controler().setCreateGraphs(false);
		config.controler().setOutputDirectory(outputLocation);
		config.plansCalcRoute().setRoutingRandomness(0);

		config.network().setInputFile(System.getProperty("user.dir") + "/input/berlin-v5-network.xml.gz");
		// "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.4-10pct/input/berlin-v5-network.xml.gz")
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

	private Scenario prepareScenario(Config config, Config noChangeEvents) {
		Scenario scenario = ScenarioUtils.loadScenario(config);
		Carriers carriers = FreightUtils.getOrCreateCarriers(scenario);
		createAndaddCarriers(scenario.getNetwork(), carriers);
		createAndLoadVehicleType(scenario);
		routePlans(scenario, noChangeEvents);

		return scenario;
	}

	private void routePlans(Scenario scenario, Config noChangeEvents) {
		Carriers carriers = FreightUtils.getOrCreateCarriers(scenario);

//		scenario.getConfig().network().setTimeVariantNetwork(false);  //Route im "leeren" Netzwerk -> immer die gleiche Route (hoffentlich)
		Scenario tmpScenario = ScenarioUtils.loadScenario(noChangeEvents);
		
		for (Carrier carrier : carriers.getCarriers().values()) {
			NetworkBasedTransportCosts.Builder tpcostsBuilder = NetworkBasedTransportCosts.Builder
					.newInstance(tmpScenario.getNetwork(), carrier.getCarrierCapabilities().getVehicleTypes());
			tpcostsBuilder.setTimeSliceWidth(1);
			// assign netBasedCosts to RoutingProblem
			NetworkBasedTransportCosts netbasedTransportcosts = tpcostsBuilder.build();
			NetworkRouter.routePlan(carrier.getSelectedPlan(), netbasedTransportcosts);
		}

//		scenario.getConfig().network().setTimeVariantNetwork(true); //und wieder einschalten :)
	}

	private void createAndaddCarriers(Network network, Carriers carriers) {

		// create one carrier for each our
		if (settings.tour.length > 0)
			for (int i = 0; i < 24; i++) {
				Carrier carrier = CarrierUtils.createCarrier(Id.create("carrier" + i, Carrier.class));
				createAndAddCarrierSerivces(carrier, i);

				CarrierVehicle carrierVehicle = createCarrierVehicle("vehicle", settings.depot, i);
				CarrierUtils.addCarrierVehicle(carrier, carrierVehicle);

				CarrierPlan carrierPlan = createPlan(network, carrier, i);
				carrier.setSelectedPlan(carrierPlan);

				carriers.addCarrier(carrier);
			}

	}

	private void createAndAddCarrierSerivces(Carrier carrier, int hour) {

		// calculate expected arrival times from expected travel times
		double[] expectedArrivalTimes = settings.getExpectedArrivalTimes(hour);

		// create customer services at origin and destination
		for (int customer = 0; customer < settings.tour.length / 2; customer++) {
			int x = customer * 2;

			double earliestStart = 0;
			if (expectedArrivalTimes != null)
				if ("PlusMinusArrival".equals(settings.timeWindowMethod))
					earliestStart = expectedArrivalTimes[x] - settings.timeWindow[x] / 2;
				else if ("AfterArrival".equals(settings.timeWindowMethod))
					earliestStart = expectedArrivalTimes[x];
				else {
					System.out.println("FreightOnlyMatsim.createAndAddCarrierSerivces(): time window method not found ("
							+ settings.timeWindowMethod + ")");
					log.info("FreightOnlyMatsim createAndAddCarrierSerivces(): time window method not found ("
							+ settings.timeWindowMethod + ")");
					log.error("FreightOnlyMatsim createAndAddCarrierSerivces(): time window method not found ("
							+ settings.timeWindowMethod + ")");
				}

			Id<CarrierService> customerOriginID = Id.create("c" + (customer + 1) + "-origin", CarrierService.class);
			CarrierUtils.addService(carrier,
					createService(customerOriginID, settings.tour[x], earliestStart, settings.serviceTime));

			Id<CarrierService> customerDestID = Id.create("c" + (customer + 1) + "-dest", CarrierService.class);
			// set possibility to start service (drop-off) at destination earlier than
			// expected:
			CarrierUtils.addService(carrier,
					createService(customerDestID, settings.tour[x + 1], earliestStart, settings.serviceTime));
		}
	}

	// Erstellt Service entsprechend der vorgebenen Werte. Ermittelt auch
	// automatisch die korrekte "ServiceBeginn" Zeit -> Fahrzeuge, die eher
	// ankommen, sollen warten!
	private CarrierService createService(Id<CarrierService> id, String linkId, Double startTimeWindowBegin,
			double serviceTime) {
		CarrierService service = CarrierService.Builder.newInstance(id, Id.createLinkId(linkId)).setCapacityDemand(1)
				.setServiceDuration(serviceTime) // Muss mindestens 1 Sekunde lang sein, da sonst TimeWindowEnforcement
				// (basierend auf WithinDay-Replanning) nicht geht.
				.setServiceStartTimeWindow(TimeWindow.newInstance(startTimeWindowBegin, 36. * 3600)).build();
		return service;
	}

	private CarrierVehicle createCarrierVehicle(String vehicleId, String locationId, int i) {
		CarrierVehicle carrierVehicle = CarrierVehicle.Builder
				.newInstance(Id.createVehicleId(vehicleId), Id.createLinkId(locationId)).setEarliestStart(i * 3600)
				.setTypeId(Id.create("vehType", VehicleType.class)).build();
		return carrierVehicle;
	}

	private void createAndLoadVehicleType(Scenario scenario) {
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

	private CarrierPlan createPlan(Network network, Carrier carrier, int i) {

		CarrierVehicle vehicle = CarrierUtils.getCarrierVehicle(carrier, Id.createVehicleId("vehicle"));
		Id<Link> depotLocation = vehicle.getLocation();
		double depTime = i * 3600.;

		Tour.Builder tourBuilder = Tour.Builder.newInstance();

		tourBuilder.scheduleStart(depotLocation, TimeWindow.newInstance(depTime, Double.MAX_VALUE));

		for (int customer = 1; customer <= settings.tour.length / 2; customer++) {
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

	private Controler prepareControler(Scenario scenario) {
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
