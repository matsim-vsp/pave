package ovgu.pave.core.algorithm.lns;

import ovgu.pave.core.algorithm.lns.insertion.InsertionHeuristics;
import ovgu.pave.core.algorithm.lns.removal.RemovalHeuristics;
import ovgu.pave.core.algorithm.lns.utilities.SolutionEvaluator;
import ovgu.pave.handler.Handler;
import ovgu.pave.model.solution.Solution;

public class LMNSInsertion {

	public static boolean performLMNSInsertion() {

		ovgu.pave.model.config.LNS configLNS = Handler.getInput().getConfig().getAlgorithm().getLns();

		Handler.getSolution().setScore(SolutionEvaluator.getTotalTravelTime()
				+ Handler.getSolution().getUnintegratedRequests().size() * configLNS.getPenaltyTerm());
		
		Solution backupSolution = Handler.getSolution().copySolution();
		int iteration = 0;
		long startTime = System.nanoTime();
		while (System.nanoTime() - startTime <= configLNS.getMaxCalculationTime()
				&& iteration < configLNS.getMaxIterations()) {
			iteration++;
			
			RemovalHeuristics.removeRequests(configLNS.getRemovalHeuristics()
					.get(Handler.getRandom().nextInt(configLNS.getRemovalHeuristics().size())));

			InsertionHeuristics.insertRequests(configLNS.getInsertionHeuristics()
					.get(Handler.getRandom().nextInt(configLNS.getInsertionHeuristics().size())));

			if (Handler.getSolution().getUnintegratedRequests().isEmpty()) {
				Handler.getAlgorithmObjects().updateAcceptanceIterations(iteration);
				return true;
			} 
			
			Handler.getSolution().setScore(SolutionEvaluator.getTotalTravelTime()
					+ Handler.getSolution().getUnintegratedRequests().size() * configLNS.getPenaltyTerm());
			
			
			if(Handler.getSolution().getScore() == backupSolution.getScore())  Handler.getAlgorithmObjects().updateNewSolutionCounter(0);
			else Handler.getAlgorithmObjects().updateNewSolutionCounter(1);
			
			if (Handler.getSolution().getScore() < backupSolution.getScore() || Math
					.exp(-(double) (Handler.getSolution().getScore() - backupSolution.getScore()) / (backupSolution.getScore()
							* configLNS.getTemperatureControlParameter() / Math.log(2))) > Handler.getRandom().nextDouble()) {
				backupSolution = Handler.getSolution().copySolution();
			} else {
				Handler.getSolution().setSolution(backupSolution);
				backupSolution = Handler.getSolution().copySolution();
			}		
		}

		Handler.getAlgorithmObjects().updateMaxIterations(iteration);
		return false;
	}
}
