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

package org.matsim.run;

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.contrib.av.robotaxi.fares.drt.DrtFareModule;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlWriterV2;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.router.AnalysisMainModeIdentifier;
import org.matsim.core.router.MainModeIdentifier;
import org.matsim.drtBlockings.DrtBlockingModule;
import org.matsim.run.drt.OpenBerlinIntermodalPtDrtRouterAnalysisModeIdentifier;
import org.matsim.run.drt.OpenBerlinIntermodalPtDrtRouterModeIdentifier;
import org.matsim.run.drt.RunDrtOpenBerlinScenario;
import org.matsim.run.drt.intermodalTripFareCompensator.IntermodalTripFareCompensatorsModule;
import org.matsim.run.drt.ptRoutingModes.PtIntermodalRoutingModesModule;

import javax.management.InvalidAttributeValueException;
import java.io.File;

public class DrtBlockingBerlin {

	private static final String BERLIN_V5_5_1PCT_DRT_CONFIG = "scenarios/berlin/input/v5.5/berlin-drt-v5.5-1pct.config.xml";

	public static void main(String[] args) {

		Config config = RunDrtOpenBerlinScenario.prepareConfig(new String[]{BERLIN_V5_5_1PCT_DRT_CONFIG});
//		config.qsim().setSimStarttimeInterpretation(QSimConfigGroup.StarttimeInterpretation.onlyUseStarttime);
		config.controler().setLastIteration(0);
//		this is not set by RunBerlinScenario, but vsp consistency checker needs it...
//		config.planCalcScore().setFractionOfIterationsToStartScoreMSA(0.8);
		config.controler().setOutputDirectory("output/berlin5.5_1pct_pave_drtBlocking");
		config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.overwriteExistingFiles);

		DrtConfigGroup drtCfg = DrtConfigGroup.getSingleModeDrtConfig(config);

//		DrtConfigGroup freightDRTCfg = new DrtConfigGroup();
//		freightDRTCfg.setMode("freightDRT");
//		freightDRTCfg.setMaxWaitTime(2 * 3600);
//		freightDRTCfg.setMaxTravelTimeAlpha(1);
//		freightDRTCfg.setMaxTravelTimeBeta(15 * 60);
//		freightDRTCfg.setStopDuration(60);
//		freightDRTCfg.setEstimatedDrtSpeed(27.78);
//		freightDRTCfg.setVehiclesFile(standardDRTCfg.getVehiclesFile());
//
//		freightDRTCfg.setRejectRequestIfMaxWaitOrTravelTimeViolated(false);
//		multiModeDrtConfigGroup.addParameterSet(freightDRTCfg);


//		drtCfg.setVehiclesFile("D:/git/pave/scenarios/berlin/input/vehicles-10-cap-4.xml");

		FreightConfigGroup freightCfg = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
		freightCfg.setCarriersFile("berlin-carriers.xml");
		freightCfg.setCarriersVehicleTypesFile("berlin-vehicleTypes.xml");

		Scenario scenario = RunDrtOpenBerlinScenario.prepareScenario(config);

		FreightUtils.loadCarriersAccordingToFreightConfig(scenario);

		try {
			FreightUtils.runJsprit(scenario, freightCfg);
			new File(config.controler().getOutputDirectory()).mkdirs();
			new CarrierPlanXmlWriterV2(FreightUtils.getCarriers(scenario)).write(config.controler().getOutputDirectory() + "carriers_planned.xml");
		} catch (InvalidAttributeValueException e) {
			e.printStackTrace();
		}

		Controler controler = prepareControler(scenario, drtCfg);

		controler.run();

	}


	static Controler prepareControler(Scenario scenario, DrtConfigGroup drtCfg){
		Controler controler = RunBerlinScenario.prepareControler( scenario ) ;

		// drt + dvrp module
//		controler.addOverridingModule(new MultiModeDrtModule());
//		controler.addOverridingModule(new DvrpModule());
		controler.addOverridingModule( new DrtBlockingModule(drtCfg));
		controler.configureQSimComponents(DvrpQSimComponents.activateAllModes(MultiModeDrtConfigGroup.get(controler.getConfig())));

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

		// Add drt-specific fare module
		controler.addOverridingModule(new DrtFareModule());
		// yyyy there is fareSModule (with S) in config. ?!?!  kai, jul'19

		controler.addOverridingModule(new IntermodalTripFareCompensatorsModule());

		controler.addOverridingModule(new PtIntermodalRoutingModesModule());

		return controler;
	}


}
