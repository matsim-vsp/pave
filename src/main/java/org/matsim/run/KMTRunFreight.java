package org.matsim.run;

import org.matsim.api.core.v01.Scenario;
import org.matsim.contrib.freight.Freight;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierPlanWriter;
import org.matsim.contrib.freight.carrier.Carriers;
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

/**
 * Diese Klasse soll ein fertiges Carrier-File einlesen und dann nur MATSim
 * OHNE Tourenplanung ausführen.
 *
 * Dient zum Debuggen/zur Analyse für das OVGU-VSP-Paper
 */
class KMTRunFreight {

	private static String WORKINGDIR =  "/Users/kturner/Desktop/OVGU/Tour2BASE";
//	private static String WORKINGDIR =  "/Users/kturner/Desktop/OVGU/Tour2oLP";

	public static void main(String[] args){

		Config config = ConfigUtils.createConfig();

		//Lade daten.
		config.controler()
				.setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
		config.global().setRandomSeed(4177);
		config.controler().setLastIteration(0);
		config.controler().setCreateGraphs(false);
//		config.controler().setOutputDirectory(WORKINGDIR + "/output/1withRoutes");
//		config.controler().setOutputDirectory(WORKINGDIR + "/output/2emptyRoutes");		//--> kracht, weil Route keine Weg von c1 weiter liefert -> Agent StuckAndAbort in Simulation ###
//		config.controler().setOutputDirectory(WORKINGDIR + "/output/3noRoutes");		//--> kracht, weil Route fehlt -> NullPointer in CarrierAgent Z.262 ###
		// ### sofern man die Pläne vor der Simulation neu routen lässt (siehe unten: ####), kommt das gleiche raus, wie bei 1) und bei Ricos Ergebnissen.
		config.controler().setOutputDirectory(WORKINGDIR + "/output/4cnoRoutesWithNWCE");
		
		config.plansCalcRoute().setRoutingRandomness(0);

		config.network().setInputFile(WORKINGDIR + "/input/network.xml.gz");
		config.network().setTimeVariantNetwork(true);
		config.network().setChangeEventsInputFile(WORKINGDIR + "/input/change_events.xml.gz");

		config.plans().setActivityDurationInterpretation(
				PlansConfigGroup.ActivityDurationInterpretation.tryEndTimeThenDuration);
		// freight configstuff
		FreightConfigGroup freightConfigGroup = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
		freightConfigGroup.setTravelTimeSliceWidth(900);
		freightConfigGroup.setTimeWindowHandling(FreightConfigGroup.TimeWindowHandling.enforceBeginnings);

//		freightConfigGroup.setCarriersFile(WORKINGDIR + "/input/carriers.xml.gz");
		freightConfigGroup.setCarriersFile(WORKINGDIR + "/input/carriers_noRoutes.xml");
		
		freightConfigGroup.setCarriersVehicleTypesFile(WORKINGDIR + "/input/vehicleTypes.xml");

		Scenario scenario = ScenarioUtils.loadScenario(config);

		FreightUtils.loadCarriersAccordingToFreightConfig(scenario);
		Carriers carriers = FreightUtils.getCarriers(scenario);
		
		//#### Erstellen der Routen für alle Carrier -> Kann es sein, dass der NetworkRouter hier nicht das zeitabhängige Netzwerk berücksichtigt? KMT Aug'20
		for (Carrier carrier : carriers.getCarriers().values()) {
			NetworkBasedTransportCosts.Builder tpcostsBuilder = NetworkBasedTransportCosts.Builder
					.newInstance(scenario.getNetwork(), carrier.getCarrierCapabilities().getVehicleTypes());
			tpcostsBuilder.setTimeSliceWidth(900);
			// assign netBasedCosts to RoutingProblem
			NetworkBasedTransportCosts netbasedTransportcosts = tpcostsBuilder.build();
			NetworkRouter.routePlan(carrier.getSelectedPlan(), netbasedTransportcosts);
		}
		
		CarrierPlanWriter carrierPlanWriter = new CarrierPlanWriter(carriers.getCarriers().values()); 
		carrierPlanWriter.write(config.controler().getOutputDirectory() + "plannedCarriers.xml");

		Controler controler = new Controler(scenario);

		Freight.configure(controler);

		controler.addOverridingModule(new AbstractModule() {
			@Override
			public void install() {
				install(new CarrierModule());
			}
		});

		controler.run();

	}


}
