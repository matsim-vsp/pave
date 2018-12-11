/* *********************************************************************** *
 * project: org.matsim.*
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2018 by the members listed in the COPYING,        *
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

/**
 * 
 */
package privateAV.modules;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.dvrp.data.Fleet;
import org.matsim.contrib.dvrp.data.file.FleetProvider;
import org.matsim.contrib.dvrp.router.TimeAsTravelDisutility;
import org.matsim.contrib.dynagent.run.DynRoutingModule;
import org.matsim.contrib.taxi.data.validator.DefaultTaxiRequestValidator;
import org.matsim.contrib.taxi.data.validator.TaxiRequestValidator;
import org.matsim.contrib.taxi.optimizer.DefaultTaxiOptimizer;
import org.matsim.contrib.taxi.optimizer.DefaultTaxiOptimizerProvider;
import org.matsim.contrib.taxi.passenger.SubmittedTaxiRequestsCollector;
import org.matsim.contrib.taxi.run.Taxi;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.contrib.taxi.util.TaxiSimulationConsistencyChecker;
import org.matsim.contrib.taxi.util.stats.TaxiStatsDumper;
import org.matsim.contrib.taxi.util.stats.TaxiStatusTimeProfileCollectorProvider;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.router.costcalculators.TravelDisutilityFactory;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.name.Names;

import freight.manager.PrivateAVFreightTourManager;
import freight.manager.SimpleFreightTourManager;
import privateAV.run.PFAVFleetGenerator;

/**
 * @author tschlenther
 *
 */
public final class PFAVAVModule extends AbstractModule{

	@Inject
	private TaxiConfigGroup taxiCfg;
	
	private Scenario scenario;

	public PFAVAVModule(Scenario scenario) {
		this.scenario = scenario;
	}
	
	@Override
	public void install() {
		String mode = taxiCfg.getMode();
//		install(FleetProvider.createModule(mode, taxiCfg.getTaxisFileUrl(getConfig().getContext())));
		
		//for some reason (annotations!?) fleet must be bound three times
		bind(Fleet.class).annotatedWith(Names.named(taxiCfg.getMode())).to(PFAVFleetGenerator.class);
		bind(Fleet.class).annotatedWith(Taxi.class).to(PFAVFleetGenerator.class);
		bind(Fleet.class).to(PFAVFleetGenerator.class);
		
		
		bind(TravelDisutilityFactory.class).annotatedWith(Names.named(DefaultTaxiOptimizerProvider.TAXI_OPTIMIZER))
				.toInstance(travelTime -> new TimeAsTravelDisutility(travelTime));

		bind(SubmittedTaxiRequestsCollector.class).asEagerSingleton();
		addControlerListenerBinding().to(SubmittedTaxiRequestsCollector.class);

		addControlerListenerBinding().to(TaxiSimulationConsistencyChecker.class);
		addControlerListenerBinding().to(TaxiStatsDumper.class);

		addRoutingModuleBinding(mode).toInstance(new DynRoutingModule(mode));

		if (taxiCfg.getTimeProfiles()) {
			addMobsimListenerBinding().toProvider(TaxiStatusTimeProfileCollectorProvider.class);
			// add more time profiles if necessary
		}

		bind(TaxiRequestValidator.class).to(DefaultTaxiRequestValidator.class);		
		
		
		addControlerListenerBinding().to(PFAVFleetGenerator.class);
//		bind(PrivateAVFreightTourManager.class).to(SimpleFreightTourManager.class).asEagerSingleton();
	}

}
