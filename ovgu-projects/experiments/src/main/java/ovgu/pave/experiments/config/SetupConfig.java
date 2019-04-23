package ovgu.pave.experiments.config;

import ovgu.pave.handler.Handler;
import ovgu.pave.handler.modelHandler.InputHandler;

public class SetupConfig {

	// config path will be _DefaultConfigPath + suffix + ".xml"
	private static final String _DefaultConfigPath = "../config";

	public static void main(String[] args) {
		System.out.println("run SetupConfig in experiments");
	
		setupBasics();
		setupExperiment();
		setupAlgorithm();
		setupShortestPath();
		
		String suffix = "";
		saveConfig(suffix);
	
		System.out.println("SetupConfig in experiments is finished");
	}
	
	private static void setupBasics() {
		Handler.getInput().getConfig().setInputFolder("../resources/input");
		Handler.getInput().getConfig().getExperiment().setOutputFolder("../resources/output");
		Handler.getInput().getConfig().getAlgorithm().setRandomSeet(99);
		Handler.getInput().getConfig().setInputFilename("inputDARP.zip");
	}
	
	public static void setupExperiment() {		
		Handler.getInput().getConfig().getExperiment().setProblem("ONLINE");
		Handler.getInput().getConfig().getExperiment().setNumberOfOutsideIterations(1);
		Handler.getInput().getConfig().getExperiment().setStartSequence(1);
		Handler.getInput().getConfig().getExperiment().setNumberOfInsideIterations(101);
		Handler.getInput().getConfig().getExperiment().setNumberOfVehicles(50);	
		Handler.getInput().getConfig().getExperiment().setVehicleCapacity(6);	
		Handler.getInput().getConfig().getExperiment().setNumberOfRequests(800);
		Handler.getInput().getConfig().getExperiment().setServiceDuration(1);
		Handler.getInput().getConfig().getExperiment().setMaxDelayValue(40);
	}
	
	public static void setupAlgorithm() {

	Handler.getInput().getConfig().getAlgorithm().setAlgorithm("LMNSINSERTION");
		
		Handler.getInput().getConfig().getAlgorithm().getLns().getInsertionHeuristics()
		.add(InputHandler.createHeuristic("REGRET2NOISE", 4));	
		Handler.getInput().getConfig().getAlgorithm().getLns().getInsertionHeuristics()
				.add(InputHandler.createHeuristic("REGRET2", 0));	
		Handler.getInput().getConfig().getAlgorithm().getLns().getRemovalHeuristics()
				.add(InputHandler.createHeuristic("RANDOMREMOVAL", 0));
		Handler.getInput().getConfig().getAlgorithm().getLns().getRemovalHeuristics()
				.add(InputHandler.createHeuristic("WORSTREMOVAL", 4));
		Handler.getInput().getConfig().getAlgorithm().getLns().getRemovalHeuristics()
				.add(InputHandler.createHeuristic("SHAWREMOVAL", 0));
		Handler.getInput().getConfig().getAlgorithm().getLns().setShawDistanceWeight(1);
		Handler.getInput().getConfig().getAlgorithm().getLns().setShawBeginWeight(6);	

		Handler.getInput().getConfig().getAlgorithm().getLns().setMaxCalculationTime(1000000);
		Handler.getInput().getConfig().getAlgorithm().getLns().setMaxIterations(Integer.MAX_VALUE);
		Handler.getInput().getConfig().getAlgorithm().getLns().setPenaltyTerm(100*60*1000);
		Handler.getInput().getConfig().getAlgorithm().getLns().setTemperatureControlParameter(0.65);
		Handler.getInput().getConfig().getAlgorithm().getLns().setMinLargeRequestSet(30);
		Handler.getInput().getConfig().getAlgorithm().getLns().setMaxLargeRequestSet(60);
		Handler.getInput().getConfig().getAlgorithm().getLns().setMinSmallRequestSet(0.4);
		Handler.getInput().getConfig().getAlgorithm().getLns().setMaxSmallRequestSet(0.6);
	}

	private static void setupShortestPath() {
		Handler.getInput().getConfig().getShortestPath().setUse("graphhopper");
		Handler.getInput().getConfig().getShortestPath().getGraphhopper().setVehicleType("car");
		Handler.getInput().getConfig().getShortestPath().getGraphhopper().setWeighting("fastest");
		Handler.getInput().getConfig().getShortestPath().getGraphhopper()
				.setGraphFolder("../resources/graphhopper/data");
		Handler.getInput().getConfig().getShortestPath().getGraphhopper()
				.setOsmFilePath("../resources/graphhopper/Berlin100320192115.osm.pbf");
	}
	
	private static void saveConfig(String suffix) {
		String path = _DefaultConfigPath + suffix + ".xml";
		Handler.getInput().getConfig().setConfigPath(path);
		Handler.getInput().saveConfig(path);
	}
}
