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

package org.matsim.run.drtBlocking;

import ch.sbb.matsim.config.SwissRailRaptorConfigGroup;
import ch.sbb.matsim.routing.pt.raptor.RaptorIntermodalAccessEgress;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Population;
import org.matsim.contrib.av.robotaxi.fares.drt.DrtFareModule;
import org.matsim.contrib.drt.analysis.DrtModeAnalysisModule;
import org.matsim.contrib.drt.routing.MultiModeDrtMainModeIdentifier;
import org.matsim.contrib.drt.run.*;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlWriterV2;
import org.matsim.contrib.freight.carrier.CarrierUtils;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.router.AnalysisMainModeIdentifier;
import org.matsim.core.router.MainModeIdentifier;
import org.matsim.core.router.TripStructureUtils;
import org.matsim.drtBlockings.DrtBlockingModule;
import org.matsim.run.RunBerlinScenario;
import org.matsim.run.drt.OpenBerlinIntermodalPtDrtRouterAnalysisModeIdentifier;
import org.matsim.run.drt.OpenBerlinIntermodalPtDrtRouterModeIdentifier;
import org.matsim.run.drt.RunDrtOpenBerlinScenario;
import org.matsim.run.drt.intermodalTripFareCompensator.IntermodalTripFareCompensatorsModule;
import org.matsim.run.drt.ptRoutingModes.PtIntermodalRoutingModesModule;

import javax.management.InvalidAttributeValueException;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class RunDrtBlocking {

	/**
	 *
	 *
	 * @param args should contain the following arguments in the specified order:
	 *             1) path to config
	 *             2) path to carrier plans
	 *             3) path to carrier vehicle types
	 *             4) boolean value that determines whether tour planning should be performed before the mobsim. True = tour planning gets performed
	 */
	public static void main(String[] args) {

		if(args.length <= 4){
			throw new IllegalArgumentException("wrong number of program arguments. expected 4, but was " + args.length);
		}

		String configPath = args[0];
		String carrierPlans = args[1];
		String carrierVehTypes = args[2];
		boolean performTourplanning = Boolean.valueOf(args[3]);

		Scenario scenario = prepareScenario(configPath, carrierPlans, carrierVehTypes, performTourplanning);

		Controler controler = prepareControler(scenario);

		controler.run();
	}

	public static Scenario prepareScenario(String configPath, String carrierPlans, String carrierVehTypes, boolean performTourplanning) {
		Config config = RunDrtOpenBerlinScenario.prepareConfig(new String[]{configPath});
//		config.qsim().setSimStarttimeInterpretation(QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime);
		config.controler().setLastIteration(0);

//		this is not set by RunBerlinScenario, but vsp consistency checker needs it...
//		config.planCalcScore().setFractionOfIterationsToStartScoreMSA(0.8);

//		config.controler().setOutputDirectory("output/" + config.controler().getRunId() + "/");

		SwissRailRaptorConfigGroup srrConfig = ConfigUtils.addOrGetModule(config, SwissRailRaptorConfigGroup.class);
		//no intermodal access egress for the time being here!
		srrConfig.setUseIntermodalAccessEgress(false);

		FreightConfigGroup freightCfg = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
		freightCfg.setCarriersFile(carrierPlans);
		freightCfg.setCarriersVehicleTypesFile(carrierVehTypes);

		DrtConfigGroup drtCfg = DrtConfigGroup.getSingleModeDrtConfig(config);

		Scenario scenario = RunDrtOpenBerlinScenario.prepareScenario(config);
		FreightUtils.loadCarriersAccordingToFreightConfig(scenario);

		if(performTourplanning){
			try {
				FreightUtils.getCarriers(scenario).getCarriers().values().forEach(carrier -> {
					CarrierUtils.setCarrierMode(carrier, drtCfg.getMode());
					CarrierUtils.setJspritIterations(carrier, 20);
				});
				FreightUtils.runJsprit(scenario, freightCfg);
				new File(config.controler().getOutputDirectory()).mkdirs();
				new CarrierPlanXmlWriterV2(FreightUtils.getCarriers(scenario)).write(config.controler().getOutputDirectory() + "carriers_planned.xml");
			} catch (InvalidAttributeValueException e) {
				e.printStackTrace();
			}
		}
		return scenario;
	}

	public static Controler prepareControler(Scenario scenario){
		Controler controler = RunBerlinScenario.prepareControler( scenario ) ;

		configureDRT(scenario, controler);

		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				// use a main mode identifier which knows how to handle intermodal trips generated by the used sbb pt raptor router
				// the SwissRailRaptor already binds its IntermodalAwareRouterModeIdentifier, however drt obviuosly replaces it
				// with its own implementation
				// So we need our own main mode indentifier which replaces both :-(
				bind(MainModeIdentifier.class).to(OpenBerlinIntermodalPtDrtRouterModeIdentifier.class);
				bind(AnalysisMainModeIdentifier.class).to(OpenBerlinIntermodalPtDrtRouterAnalysisModeIdentifier.class);
			}
		});

		controler.addOverridingModule(new IntermodalTripFareCompensatorsModule());
		controler.addOverridingModule(new PtIntermodalRoutingModesModule());

		return controler;
	}

	private static void configureDRT(Scenario scenario, Controler controler) {
		MultiModeDrtConfigGroup multiModeDrtCfg = MultiModeDrtConfigGroup.get(scenario.getConfig());
		DrtConfigGroup drtCfg = DrtConfigGroup.getSingleModeDrtConfig(scenario.getConfig());

		// at the moment, we only configure our 1 drt mode!
		// if you want to use several drt modes AND drt blocking, take care that DrtBlockingModeModule does the QSimBindings for it's mode, so do not use MultiModeDrtModule!
		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				install(new DvrpModule());
				install(new DrtModeModule(drtCfg));
//				install(new DrtModeAnalysisModule(drtCfg)); TODO: we have to write a custom OccupancyProfileCalculator that can handle FreightTasks...
				install(new DrtBlockingModule(drtCfg));
				bind(MainModeIdentifier.class).toInstance(new MultiModeDrtMainModeIdentifier(multiModeDrtCfg));
			}
		});
		controler.configureQSimComponents(DvrpQSimComponents.activateAllModes(MultiModeDrtConfigGroup.get(controler.getConfig())));

		// Add drt-specific fare module
		controler.addOverridingModule(new DrtFareModule());
	}


}
