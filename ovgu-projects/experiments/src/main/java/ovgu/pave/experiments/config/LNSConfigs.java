package ovgu.pave.experiments.config;

import ovgu.pave.handler.Handler;
import ovgu.pave.handler.modelHandler.InputHandler;

public class LNSConfigs {
	public static void setupAlgorithmALNS() {

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
	
	public static void ALNS() {

Handler.getInput().getConfig().getAlgorithm().setAlgorithm("INSERTION");
		

Handler.getInput().getConfig().getAlgorithm().getLns().getInsertionHeuristics()
		.add(InputHandler.createHeuristic("REGRET1", 0));
Handler.getInput().getConfig().getAlgorithm().getLns().getInsertionHeuristics()
		.add(InputHandler.createHeuristic("REGRET2", 0));
Handler.getInput().getConfig().getAlgorithm().getLns().getInsertionHeuristics()
		.add(InputHandler.createHeuristic("REGRET2NOISE", 4));
Handler.getInput().getConfig().getAlgorithm().getLns().getRemovalHeuristics()
		.add(InputHandler.createHeuristic("RANDOMREMOVAL", 0));
Handler.getInput().getConfig().getAlgorithm().getLns().getRemovalHeuristics()
		.add(InputHandler.createHeuristic("WORSTREMOVAL", 4));
Handler.getInput().getConfig().getAlgorithm().getLns().getRemovalHeuristics()
		.add(InputHandler.createHeuristic("SHAWREMOVAL", 0));
Handler.getInput().getConfig().getAlgorithm().getLns().setShawDistanceWeight(1);
Handler.getInput().getConfig().getAlgorithm().getLns().setShawBeginWeight(6);	

Handler.getInput().getConfig().getAlgorithm().getLns().setMaxCalculationTime(Long.MAX_VALUE);
Handler.getInput().getConfig().getAlgorithm().getLns().setMaxIterations(25000);
Handler.getInput().getConfig().getAlgorithm().getLns().setMaxWithoutImprovement(5000);
Handler.getInput().getConfig().getAlgorithm().getLns().setPenaltyTerm(100*60*1000);
Handler.getInput().getConfig().getAlgorithm().getLns().setTemperatureControlParameter(0.05);
Handler.getInput().getConfig().getAlgorithm().getLns().setCoolingRate(0.99975);
Handler.getInput().getConfig().getAlgorithm().getLns().setMinLargeRequestSet(30);
Handler.getInput().getConfig().getAlgorithm().getLns().setMaxLargeRequestSet(60);
Handler.getInput().getConfig().getAlgorithm().getLns().setMinSmallRequestSet(0.2);
Handler.getInput().getConfig().getAlgorithm().getLns().setMaxSmallRequestSet(0.4);
Handler.getInput().getConfig().getAlgorithm().getLns().getRws().setLastScoreInfluence(0.9);
Handler.getInput().getConfig().getAlgorithm().getLns().getRws().setCase1(33);
Handler.getInput().getConfig().getAlgorithm().getLns().getRws().setCase2(9);
Handler.getInput().getConfig().getAlgorithm().getLns().getRws().setCase3(13);
	}

	public static void setupAlgorithmLMNS() {

		Handler.getInput().getConfig().getAlgorithm().setAlgorithm("LMNS");
		
		Handler.getInput().getConfig().getAlgorithm().getLns().getInsertionHeuristics()
				.add(InputHandler.createHeuristic("REGRET1", 0));
		Handler.getInput().getConfig().getAlgorithm().getLns().getInsertionHeuristics()
				.add(InputHandler.createHeuristic("REGRET2", 0));
		Handler.getInput().getConfig().getAlgorithm().getLns().getInsertionHeuristics()
				.add(InputHandler.createHeuristic("REGRET2", 7));
		Handler.getInput().getConfig().getAlgorithm().getLns().getRemovalHeuristics()
				.add(InputHandler.createHeuristic("RANDOMREMOVAL", 0));
		Handler.getInput().getConfig().getAlgorithm().getLns().getRemovalHeuristics()
				.add(InputHandler.createHeuristic("TIMEWINDOWREMOVAL", 0));
		Handler.getInput().getConfig().getAlgorithm().getLns().getRemovalHeuristics()
				.add(InputHandler.createHeuristic("WORSTREMOVAL", 7));
		Handler.getInput().getConfig().getAlgorithm().getLns().getRemovalHeuristics()
				.add(InputHandler.createHeuristic("HISTORICALREMOVAL", 7));
		Handler.getInput().getConfig().getAlgorithm().getLns().getRemovalHeuristics()
				.add(InputHandler.createHeuristic("SHAWREMOVAL", 0));
		Handler.getInput().getConfig().getAlgorithm().getLns().getRemovalHeuristics()
				.add(InputHandler.createHeuristic("CLUSTERREMOVAL", 0));
		Handler.getInput().getConfig().getAlgorithm().getLns().setMaxCalculationTime(20000000);
		Handler.getInput().getConfig().getAlgorithm().getLns().setMaxIterations(Integer.MAX_VALUE);
		Handler.getInput().getConfig().getAlgorithm().getLns().setMinLargeRequestSet(30);
		Handler.getInput().getConfig().getAlgorithm().getLns().setMaxLargeRequestSet(60);
		Handler.getInput().getConfig().getAlgorithm().getLns().setMinSmallRequestSet(0.2);
		Handler.getInput().getConfig().getAlgorithm().getLns().setMaxSmallRequestSet(0.4);
	}
}
