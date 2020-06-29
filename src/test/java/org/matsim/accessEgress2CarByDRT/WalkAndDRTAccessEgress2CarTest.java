package org.matsim.accessEgress2CarByDRT;

import com.google.common.collect.ImmutableSet;
import org.junit.Rule;
import org.junit.Test;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.run.DrtControlerCreator;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.PlansCalcRouteConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;
import org.matsim.testcases.MatsimTestUtils;
import org.matsim.vehicles.Vehicle;
import org.matsim.vehicles.VehicleUtils;
import org.matsim.vis.otfvis.OTFVisConfigGroup;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WalkAndDRTAccessEgress2CarTest {

	@Rule public MatsimTestUtils utils = new MatsimTestUtils() ;


	//TODO actually test something or classify it as an IT...
	@Test
	public void interModalCarDRTTest() {
//		modifyNetwork();

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

//		drtCfg.setOperationalScheme(DrtConfigGroup.OperationalScheme.stopbased);
//		drtCfg.setTransitStopFile("drtstops.xml");

		config.plans().setInputFile("gridCarRestrictedInCenter_population_car.xml");

		config.plansCalcRoute().setAccessEgressType(PlansCalcRouteConfigGroup.AccessEgressType.accessEgressModeToLink);
		config.controler().setLastIteration(1);
		config.qsim().setStartTime(0.);
		config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
		config.controler().setOutputDirectory(utils.getOutputDirectory() + "carPop");

		{ //add scoring parameters for new modes
			config.planCalcScore().addModeParams(new PlanCalcScoreConfigGroup.ModeParams("walkCarDrt"));
			config.planCalcScore().addModeParams(new PlanCalcScoreConfigGroup.ModeParams("drtCarWalk"));
			config.planCalcScore().addModeParams(new PlanCalcScoreConfigGroup.ModeParams("carDrtCar"));
			config.planCalcScore().addActivityParams(new PlanCalcScoreConfigGroup.ActivityParams("walkCarDrt interaction").setScoringThisActivityAtAll(false));
			config.planCalcScore().addActivityParams(new PlanCalcScoreConfigGroup.ActivityParams("drtCarWalk interaction").setScoringThisActivityAtAll(false));
			config.planCalcScore().addActivityParams(new PlanCalcScoreConfigGroup.ActivityParams("carDrtCar interaction").setScoringThisActivityAtAll(false));
		}

		//this is the wrong way around (create controler before manipulating scenario, but it is handy here...
		Controler controler = DrtControlerCreator.createControler(config, false);

		{ //add mode vehicle id's
			controler.getScenario().getPopulation().getPersons().values().parallelStream().forEach(person -> {
				Map<String, Id<Vehicle>> vehicleIdMap = new HashMap<>();
				Id<Vehicle> vehicleId = Id.createVehicleId(person.getId().toString());
				vehicleIdMap.put("walkCarDrt", vehicleId);
				vehicleIdMap.put("drtCarWalk", vehicleId);
				vehicleIdMap.put("carDrtcar", vehicleId);
				VehicleUtils.insertVehicleIdsIntoAttributes(person, vehicleIdMap);
			});
		}

		controler.addOverridingModule(new WalkAccessDRTEgress2CarModule("walkCarDrt", drtCfg));
		controler.addOverridingModule(new DRTAccessWalk2Egress2CarModule("drtCarWalk", drtCfg));
//		controler.addOverridingModule(new CarAccessEgress2DrtModule("carDrtCar", drtCfg));

		controler.run();
	}

	private void modifyNetwork() {
		URL networkUrl = IOUtils.extendUrl(ExamplesUtils.getTestScenarioURL("gridCarRestrictedInCenter"), "gridCarRestrictedInCenter_network.xml");


		Network network = NetworkUtils.readNetwork(networkUrl.toString());


		network.getLinks().values().forEach(link -> {

			link.setLength(300);
			link.setFreespeed(15.0);

			Set<String> modes = new HashSet<>();
			modes.addAll(link.getAllowedModes());

			if(modes.contains(TransportMode.walk) ) modes.add("drt");
			else if (modes.contains(TransportMode.car)){
				modes.add("walkCarDrt");
				modes.add("drtCarWalk");
			}
			modes.add(TransportMode.walk);
			link.setAllowedModes(modes);

		});
		NetworkUtils.writeNetwork(network, "D:/git/matsim/examples/scenarios/gridCarRestrictedInCenter/gridCarRestrictedInCenter_network_drtInZone.xml");
	}
}