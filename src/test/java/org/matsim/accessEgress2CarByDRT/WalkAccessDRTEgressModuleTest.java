package org.matsim.accessEgress2CarByDRT;

import com.google.common.collect.ImmutableSet;
import org.junit.Rule;
import org.junit.Test;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.drt.optimizer.rebalancing.mincostflow.MinCostFlowRebalancingParams;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.drt.run.DrtControlerCreator;
import org.matsim.contrib.drt.run.MultiModeDrtConfigGroup;
import org.matsim.contrib.dvrp.run.DvrpConfigGroup;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.examples.ExamplesUtils;
import org.matsim.testcases.MatsimTestUtils;
import org.matsim.vis.otfvis.OTFVisConfigGroup;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class WalkAccessDRTEgressModuleTest  {

	@Rule public MatsimTestUtils utils = new MatsimTestUtils() ;


	@Test
	public void jjjTest() {
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

		config.plansCalcRoute().setInsertingAccessEgressWalk(true);
		config.controler().setLastIteration(1);
		config.qsim().setStartTime(0.);
		config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
		config.controler().setOutputDirectory(utils.getOutputDirectory() + "carPop");

		//this is the wrong way around (create controler before manipulating scenario...
		Controler controler = DrtControlerCreator.createControler(config, false);
//		setupPopulation(controler.getScenario().getPopulation());


		controler.addOverridingModule(new WalkAccessDRTEgressModule(TransportMode.car, drtCfg));

		controler.run();
	}

	private void modifyNetwork() {
		URL networkUrl = IOUtils.extendUrl(ExamplesUtils.getTestScenarioURL("gridCarRestrictedInCenter"), "gridCarRestrictedInCenter_network.xml");


		Network network = NetworkUtils.readNetwork(networkUrl.toString());


		network.getLinks().values().forEach(link -> {

			Set<String> modes = new HashSet<>();
			modes.addAll(link.getAllowedModes());

			if(modes.contains(TransportMode.walk) ) modes.add("drt");

			modes.add(TransportMode.walk);
			link.setAllowedModes(modes);

		});
		NetworkUtils.writeNetwork(network, "D:/git/matsim/examples/scenarios/gridCarRestrictedInCenter/gridCarRestrictedInCenter_network_drtInZone.xml");
	}
}