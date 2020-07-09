package org.matsim.accessEgress2CarByDRT;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.matsim.analysis.LegHistogram;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.contrib.drt.run.*;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpModule;
import org.matsim.contrib.dvrp.run.DvrpQSimComponents;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;
import org.matsim.testcases.MatsimTestUtils;
import org.matsim.vehicles.Vehicle;
import org.matsim.vehicles.VehicleUtils;
import org.matsim.vis.otfvis.OTFVisConfigGroup;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class WalkAndDRTAccessEgress2CarTest {

	@Rule public MatsimTestUtils utils = new MatsimTestUtils() ;

	@Test
	public void testNumberOfModeDepartures() {

		Controler controler = setupControler();
		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				addControlerListenerBinding().to(WalkAndDRTAccessEgress2CarTestHandler.class).asEagerSingleton();
			}
		});
		controler.run();
	}

	private Controler setupControler() {
		URL configUrl = IOUtils.extendUrl(ExamplesUtils.getTestScenarioURL("gridCarRestrictedInCenter"), "gridCarRestrictedInCenter_config.xml");
		Config config = ConfigUtils.loadConfig(configUrl, new MultiModeDrtConfigGroup(), new DvrpConfigGroup(),
				new OTFVisConfigGroup());


		DrtConfigGroup drtCfg = DrtConfigGroup.getSingleModeDrtConfig(config);
		DvrpConfigGroup dvrpConfigGroup = DvrpConfigGroup.get(config);

		dvrpConfigGroup.setNetworkModes(ImmutableSet.<String>builder()
				.add(TransportMode.car)
				.add(drtCfg.getMode())
				.build());
		drtCfg.setUseModeFilteredSubnetwork(true);

		config.plans().setInputFile("gridCarRestrictedInCenter_population_intermodal.xml");

		config.plansCalcRoute().setAccessEgressType(PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLink);
		config.controler().setLastIteration(0);
		config.qsim().setStartTime(0.);
		config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
		config.controler().setOutputDirectory(utils.getOutputDirectory());

		{ //add scoring parameters for new modes
			config.planCalcScore().addModeParams(new PlanCalcScoreConfigGroup.ModeParams("walkCarDrt"));
			config.planCalcScore().addModeParams(new PlanCalcScoreConfigGroup.ModeParams("drtCarWalk"));
			config.planCalcScore().addActivityParams(new PlanCalcScoreConfigGroup.ActivityParams(PlanCalcScoreConfigGroup.createStageActivityType("walkCarDrt"))
					.setScoringThisActivityAtAll(false));
			config.planCalcScore().addActivityParams(new PlanCalcScoreConfigGroup.ActivityParams(PlanCalcScoreConfigGroup.createStageActivityType("drtCarWalk"))
					.setScoringThisActivityAtAll(false));
		}

		MultiModeDrtConfigGroup multiModeDrtConfig = MultiModeDrtConfigGroup.get(config);
		DrtConfigs.adjustMultiModeDrtConfig(multiModeDrtConfig, config.planCalcScore(), config.plansCalcRoute());

		Scenario scenario = DrtControlerCreator.createScenarioWithDrtRouteFactory(config);
		{ //add mode vehicle id's
			scenario.getPopulation().getPersons().values().parallelStream().forEach(person -> {
				Map<String, Id<Vehicle>> vehicleIdMap = new HashMap<>();
				Id<Vehicle> vehicleId = Id.createVehicleId(person.getId().toString());
				vehicleIdMap.put("walkCarDrt", vehicleId);
				vehicleIdMap.put("drtCarWalk", vehicleId);
				VehicleUtils.insertVehicleIdsIntoAttributes(person, vehicleIdMap);
			});
		}
		ScenarioUtils.loadScenario(scenario);

		Controler controler = new Controler(scenario);
		controler.addOverridingModule(new DvrpModule());
		controler.addOverridingModule(new MultiModeDrtModule());
		controler.configureQSimComponents(DvrpQSimComponents.activateAllModes(multiModeDrtConfig));

		controler.addOverridingModule(new WalkAccessDRTEgress2CarModule("walkCarDrt", drtCfg));
		controler.addOverridingModule(new DRTAccessWalkEgress2CarModule("drtCarWalk", drtCfg));
		return controler;
	}
}

class WalkAndDRTAccessEgress2CarTestHandler implements IterationEndsListener{

	@Inject
	LegHistogram histogram;

	@Override
	public void notifyIterationEnds(IterationEndsEvent event) {
/**
 * 3 agents performing walk = 3 departures
 * 3 agents performing walk->car->walk = 9 departures
 * 3 agents that want perform walk->drt->walk, but 2 of them do only walk (because drt not available at origin) = 5 departures
 * 3 agents that want to perform walk->drt->walk->drtCarWalk->walk, but 2 of them do only walk (because drt not available at origin) = 7 departures
 * 3 agents that want to perform walk->drtCarWalk->walk->drt->walk, but 2 of them do only walk (because drt not available at destination) = 7 departures
 *
 * that sums up to 3 + 9 + 5 + 7 + 7 = 31 departures
 *
 *
 */
		Assert.assertEquals("the number of all departures is expected to be 31", 31, getSumOfArrayEntries(histogram.getDepartures()));
		Assert.assertEquals("there should be 5 modes in the legHistogram ", 5, histogram.getLegModes().size());

		// if you go through the comment above:
		// 3 + 3*2 + 2 + 2*1 + 3 + 2*1 + 3 + 2*1 = 23
		Assert.assertEquals("the should be 23 walk legs", 23, getSumOfArrayEntries(histogram.getDepartures(TransportMode.walk)));

		// if you go through the comment above:
		// 0 + 3 + 0 + 0 + 0 = 3
		Assert.assertEquals("the should be 3 car legs", 3, getSumOfArrayEntries(histogram.getDepartures(TransportMode.car)));

		// if you go through the comment above:
		// 0 + 0 + 1 + 1 + 1 = 3
		Assert.assertEquals("the should be 3 drt legs", 3, getSumOfArrayEntries(histogram.getDepartures(TransportMode.drt)));

		// if you go through the comment above:
		// 0 + 0 + 0 + 1 + 0 = 1
		Assert.assertEquals("the should be 1 walkCarDrt leg", 1, getSumOfArrayEntries(histogram.getDepartures("walkCarDrt")));

		// if you go through the comment above:
		// 0 + 0 + 0 + 0 + 1 = 1
		Assert.assertEquals("the should be 1 drtCarWalk leg", 1, getSumOfArrayEntries(histogram.getDepartures("drtCarWalk")));

	}

	private int getSumOfArrayEntries(int[] array){
		int sum = 0;
		for (int i : array) {
			sum += i;
		}
		return sum;
	}
}