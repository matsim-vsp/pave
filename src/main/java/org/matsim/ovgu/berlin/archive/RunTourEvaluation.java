package org.matsim.ovgu.berlin.archive;

import static org.matsim.core.config.groups.ControlerConfigGroup.RoutingAlgorithmType.FastAStarLandmarks;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.Node;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlansConfigGroup.HandlingOfPlansWithoutRoutingMode;
import org.matsim.core.config.groups.PlansConfigGroup;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryLogging;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.router.costcalculators.TravelDisutilityFactory;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.LeastCostPathCalculator.Path;
import org.matsim.core.router.util.LeastCostPathCalculatorFactory;
import org.matsim.core.router.util.TravelDisutility;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.ovgu.berlin.input.Input;

public class RunTourEvaluation {

	private static final Logger log = Logger.getLogger(RunTourEvaluation.class);

	private static final String inputDirectory = "../tubCloud/kturner/Arbeit/40 Projekte/40.10 PAVE/PAVE-Cloud/PAVE-Projekte/Daten/Repo/input/example";
	private static final String configFile = "finalA.config.xml";
//	private static final String changeEventsFile = "scenario-A.15.networkChangeEvents.xml.gz";
	private static final String networkFile = "scenario-A.output_network.xml.gz";
	private static final String outputDirectory = "tourEvaluation";

	private static Network network;
	private static LeastCostPathCalculator calc;

	public static void main(String[] args) {

		for (String arg : args) {
			log.info(arg);
		}

		if (args.length == 0) {
			args = new String[] { inputDirectory + "/" + configFile };
		}

		Config config = prepareConfig(args);

		// use changeEvents -->
//		config.network().setTimeVariantNetwork(true);
//		config.network().setChangeEventsInputFile(changeEventsFile);
		config.plans().setActivityDurationInterpretation(
				PlansConfigGroup.ActivityDurationInterpretation.tryEndTimeThenDuration);
//		config.network().setInputFile("https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.4-10pct/input/berlin-v5-network.xml.gz");
		config.network().setInputFile(networkFile);
		// <-- use changeEvents

		config.plans().setHandlingOfPlansWithoutRoutingMode(HandlingOfPlansWithoutRoutingMode.useMainModeIdentifier);

		Scenario scenario = prepareScenario(config);
		Controler controler = prepareControler(scenario);

		config.controler().setLastIteration(0);
		controler.run();

		controler.getTripRouterProvider();
		LeastCostPathCalculatorFactory calcFac = controler.getLeastCostPathCalculatorFactory();
		TravelDisutilityFactory disuFac = controler.getTravelDisutilityFactory();
		TravelTime travelTimes = controler.getLinkTravelTimes();
		TravelTime timeCalculator = controler.getLinkTravelTimes();

		TravelDisutility travelCosts = disuFac.createTravelDisutility(timeCalculator);

		network = controler.getScenario().getNetwork();
		calc = calcFac.createPathCalculator(network, travelCosts, travelTimes);

		runEvaluation(controler);

	}

	private static void runEvaluation(Controler controler) {

		runCalculations(Input.tourNodes, true, Input.avgTT, Input.standardTW, "avgStandard");
		runCalculations(Input.tourNodes, true, Input.avgTT, Input.premiumTW, "avgPremium");

		runCalculations(Input.tourNodes, true, Input.minTT, Input.standardTW, "minStandard");
		runCalculations(Input.tourNodes, true, Input.minTT, Input.premiumTW, "minPremium");

		runCalculations(Input.tourNodes, true, Input.maxTT, Input.standardTW, "maxStandard");
		runCalculations(Input.tourNodes, true, Input.maxTT, Input.premiumTW, "maxPremium");

		runCalculations(Input.lTourNodes, true, Input.lAvgTT, Input.standardTW, "avgStandardXL");
		runCalculations(Input.lTourNodes, true, Input.lAvgTT, Input.premiumTW, "avgPremiumXL");

		runCalculations(Input.lTourNodes, true, Input.lMinTT, Input.standardTW, "minStandardXL");
		runCalculations(Input.lTourNodes, true, Input.lMinTT, Input.premiumTW, "minPremiumXL");

		runCalculations(Input.lTourNodes, true, Input.lMaxTT, Input.standardTW, "maxStandardXL");
		runCalculations(Input.lTourNodes, true, Input.lMaxTT, Input.premiumTW, "maxPremiumXL");

	}

	private static void runCalculations(long[] tour, boolean planedDeparture, double[] expectedTT, double timewindow,
			String name) {

		try {
			FileWriter csvWriter = new FileWriter(outputDirectory + "/" + name + ".csv");
			csvWriter.append(
					"hour; customer; from; to; oArrival; oServiceTime; oDeparture; odTravelTime; dArrival; dServiceTime; dDeparture; doTravelTime;;"
							+ "oExpectedArrival; oTwStart; oTwEnd; oBeforeTW; oInTW; oAfterTW; oEarlyTW; oLateTW; oEarly; oLate;;"
							+ "dExpectedArrival; dTwStart; dTwEnd; dBeforeTW; dInTW; dAfterTW; dEarlyTW; oLateTW; dEarly; dLate;;"
							+ "odPath; nextPath\n");

			// do it
			for (int i = 0; i < 24; i++)
				calculateTravelTimes(tour, i, planedDeparture, expectedTT, timewindow, csvWriter);

			// finish
			csvWriter.flush();
			csvWriter.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void calculateTravelTimes(long[] tour, int hour, boolean planedArrival, double[] expectedTT,
			double timewindow, FileWriter csvWriter) throws IOException {

		double start = hour * 3600;
		double serviceTime = 2 * 60;

		double[] expectedArrival = new double[expectedTT.length];
		if (planedArrival) {
			expectedArrival[0] = start + expectedTT[0];
			for (int x = 1; x < expectedArrival.length; x++)
				expectedArrival[x] = expectedArrival[x - 1] + serviceTime + expectedTT[x];
		}

		for (int customer = 0; customer < tour.length / 2; customer++) {
			int x = customer * 2;

			// ArrayIndexOutOfBoundsException: 134
			double oTwStart = expectedArrival[x] - timewindow / 2;
			double oTwEnd = expectedArrival[x] + timewindow / 2;
			double oArrival = start;

			boolean oBeforeTW = false;
			boolean oInTW = false;
			boolean oAfterTW = false;
			double oEarlyTW = 0;
			double oLateTW = 0;
			double oEarly = 0;
			double oLate = 0;

			// before arrival tw
			if (oArrival < oTwStart) {
				oEarlyTW = oTwStart - oArrival;
				oBeforeTW = true;
			}
			// in arrival tw
			if (oArrival >= oTwStart && oArrival <= oTwEnd)
				oInTW = true;
			// after arrival tw
			if (oArrival > oTwEnd) {
				oLateTW = oArrival - oTwEnd;
				oAfterTW = true;
			}
			// before expected arrival
			if (oArrival < expectedArrival[x])
				oEarly = expectedArrival[x] - oArrival;
			// after expected arrival
			if (oArrival > expectedArrival[x])
				oLate = oArrival - expectedArrival[x];

			if (planedArrival && oArrival < oTwStart) {
				oArrival = oTwStart;
			}

			double oServiceTime = serviceTime;
			double oDeparture = oArrival + oServiceTime;

			long originID = tour[x];
			long destID = tour[x + 1];
			Node origin = network.getNodes().get(Id.createNodeId(originID));
			Node destination = network.getNodes().get(Id.createNodeId(destID));

			Path odPath = getTravelPath(origin, destination, oDeparture);
			double odTravelTime = odPath.travelTime;

			double dArrival = oDeparture + odTravelTime;
			double dServiceTime = serviceTime;
			double dDeparture = dArrival + dServiceTime;

//			ArrayIndexOutOfBoundsException: 19
			double dTwStart = expectedArrival[x + 1] - timewindow / 2;
			double dTwEnd = expectedArrival[x + 1] + timewindow / 2;
			boolean dBeforeTW = false;
			boolean dInTW = false;
			boolean dAfterTW = false;
			double dEarlyTW = 0;
			double dLateTW = 0;
			double dEarly = 0;
			double dLate = 0;

			// before arrival tw
			if (dArrival < dTwStart) {
				dEarlyTW = dTwStart - dArrival;
				dBeforeTW = true;
			}
			// in arrival tw
			if (dArrival >= dTwStart && dArrival <= dTwEnd)
				dInTW = true;
			// after arrival tw
			if (dArrival > dTwEnd) {
				dLateTW = dArrival - dTwEnd;
				dAfterTW = true;
			}
			// before expected arrival
			if (dArrival < expectedArrival[x + 1])
				dEarly = expectedArrival[x + 1] - dArrival;
			// after expected arrival
			if (dArrival > expectedArrival[x + 1])
				dLate = dArrival - expectedArrival[x + 1];

			double nextTravelTime = 0;
			Path nextPath = null;
			if (x < tour.length - 2) {
				long idNext = tour[x + 2];
				Node nextNode = network.getNodes().get(Id.createNodeId(idNext));
				nextPath = getTravelPath(destination, nextNode, dDeparture);
				nextTravelTime = nextPath.travelTime;
			}
			start = dDeparture + nextTravelTime;

			String str = hour + ";" + (customer + 1) + ";" + originID + ";" + destID + ";" + oArrival + ";"
					+ oServiceTime + ";" + oDeparture + ";" + odTravelTime + ";" + dArrival + ";" + dServiceTime + ";"
					+ dDeparture + ";" + nextTravelTime + ";;" + expectedArrival[x] + ";" + oTwStart + ";" + oTwEnd
					+ ";" + oBeforeTW + ";" + oInTW + ";" + oAfterTW + ";" + oEarlyTW + ";" + oLateTW + ";" + oEarly
					+ ";" + oLate + ";;" + expectedArrival[x + 1] + ";" + dTwStart + ";" + dTwEnd + ";" + dBeforeTW
					+ ";" + dInTW + ";" + dAfterTW + ";" + dEarlyTW + ";" + dLateTW + ";" + dEarly + ";" + dLate + ";;";

			for (Node node : odPath.nodes) {
				str = str + node.getId() + ",";
			}

			if (nextPath != null) {
				str = str.substring(0, str.length() - 1) + "; ";
				for (Node node : nextPath.nodes) {
					str = str + node.getId() + ",";
				}
			}
			str = str.substring(0, str.length() - 1) + "\n";
			csvWriter.append(str);
		}
	}

	private static Path getTravelPath(Node from, Node to, double departure) {
		double day = 24 * 60 * 60;
		departure = departure % day;
		return calc.calcLeastCostPath(from, to, departure, null, null);
	}

	public static Controler prepareControler(Scenario scenario) {
		// note that for something like signals, and presumably drt, one needs the
		// controler object

		Gbl.assertNotNull(scenario);

		final Controler controler = new Controler(scenario);

// KMT: Wozu brauchtest du das Folgende?		

//		if (controler.getConfig().transit().isUsingTransitInMobsim()) {
//			// use the sbb pt raptor router
//			controler.addOverridingModule(new AbstractModule() {
//				@Override
//				public void install() {
//					install(new SwissRailRaptorModule());
//				}
//			});
//		} else {
//			log.warn("Public transit will be teleported and not simulated in the mobsim! "
//					+ "This will have a significant effect on pt-related parameters (travel times, modal split, and so on). "
//					+ "Should only be used for testing or car-focused studies with a fixed modal split.  ");
//		}

//		// use the (congested) car travel time for the teleported ride mode
//		controler.addOverridingModule(new AbstractModule() {
//
//			@Override
//			public void install() {
//				addTravelTimeBinding(TransportMode.ride).to(networkTravelTime());
//				addTravelDisutilityFactoryBinding(TransportMode.ride).to(carTravelDisutilityFactoryKey());
//			}
//
//		});

		return controler;
	}

	public static Scenario prepareScenario(Config config) {
		Gbl.assertNotNull(config);

		// note that the path for this is different when run from GUI (path of original
		// config) vs.
		// when run from command line/IDE (java root). :-( See comment in method. kai,
		// jul'18
		// yy Does this comment still apply? kai, jul'19

		final Scenario scenario = ScenarioUtils.loadScenario(config);

		return scenario;
	}

	public static Config prepareConfig(String[] args) {
		OutputDirectoryLogging.catchLogEntries();

		String[] typedArgs = Arrays.copyOfRange(args, 1, args.length);

		final Config config = ConfigUtils.loadConfig(args[0]); // I need this to set the context

		config.controler().setRoutingAlgorithmType(FastAStarLandmarks);

		// KMT: Wozu brauchtest du das Folgende?
		// Damit veränderst du die Config Werte auch für die bisherigen Pläne mit der
		// Folge, dass des bei potenziellem Replanning nun auf einmal andere
		// Verhaltensweisen gibt...

//		config.subtourModeChoice().setProbaForRandomSingleTripMode(0.5);
//
//		config.plansCalcRoute().setRoutingRandomness(3.);
//		config.plansCalcRoute().removeModeRoutingParams(TransportMode.ride);
//		config.plansCalcRoute().removeModeRoutingParams(TransportMode.pt);
//		config.plansCalcRoute().removeModeRoutingParams(TransportMode.bike);
//		config.plansCalcRoute().removeModeRoutingParams("undefined");
//
//		config.qsim().setInsertingWaitingVehiclesBeforeDrivingVehicles(true);
//
//		// vsp defaults
//		config.vspExperimental().setVspDefaultsCheckingLevel(VspExperimentalConfigGroup.VspDefaultsCheckingLevel.info);
//		config.plansCalcRoute().setInsertingAccessEgressWalk(true);
//		config.qsim().setUsingTravelTimeCheckInTeleportation(true);
//		config.qsim().setTrafficDynamics(TrafficDynamics.kinematicWaves);
//
//		// activities:
//		for (long ii = 600; ii <= 97200; ii += 600) {
//			config.planCalcScore().addActivityParams(new ActivityParams("home_" + ii + ".0").setTypicalDuration(ii));
//			config.planCalcScore().addActivityParams(new ActivityParams("work_" + ii + ".0").setTypicalDuration(ii)
//					.setOpeningTime(6. * 3600.).setClosingTime(20. * 3600.));
//			config.planCalcScore().addActivityParams(new ActivityParams("leisure_" + ii + ".0").setTypicalDuration(ii)
//					.setOpeningTime(9. * 3600.).setClosingTime(27. * 3600.));
//			config.planCalcScore().addActivityParams(new ActivityParams("shopping_" + ii + ".0").setTypicalDuration(ii)
//					.setOpeningTime(8. * 3600.).setClosingTime(20. * 3600.));
//			config.planCalcScore().addActivityParams(new ActivityParams("other_" + ii + ".0").setTypicalDuration(ii));
//		}
//		config.planCalcScore().addActivityParams(new ActivityParams("freight").setTypicalDuration(12. * 3600.));

		ConfigUtils.applyCommandline(config, typedArgs);

		return config;
	}

}
