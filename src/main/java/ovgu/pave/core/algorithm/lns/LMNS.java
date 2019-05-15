package ovgu.pave.core.algorithm.lns;

import ovgu.pave.core.algorithm.lns.insertion.InsertionHeuristics;
import ovgu.pave.core.algorithm.lns.removal.RemovalHeuristics;
import ovgu.pave.core.algorithm.lns.utilities.SolutionEvaluator;
import ovgu.pave.handler.Handler;
import ovgu.pave.model.solution.Solution;

public class LMNS {

	public static Solution performLMNS() {

		ovgu.pave.model.config.LNS lnsConfig = Handler.getInput().getConfig().getAlgorithm().getLns();

		Solution backupSolution = Handler.getSolution().copySolution();
		Solution bestSolution = null;

		int iteration = 0;
		int maxUnintegratedRequests = 1;

		long startTime = System.nanoTime();
		while (System.nanoTime() - startTime <= lnsConfig.getMaxCalculationTime()
				&& iteration < lnsConfig.getMaxIterations()) {
			iteration++;
			InsertionHeuristics.insertRequests(lnsConfig.getInsertionHeuristics()
					.get(Handler.getRandom().nextInt(lnsConfig.getInsertionHeuristics().size())));

			if (Handler.getSolution().getUnintegratedRequests().isEmpty()) {
				Handler.getSolution().setScore(SolutionEvaluator.getTotalTravelTime());
				backupSolution = Handler.getSolution().copySolution();
				if (bestSolution == null || Handler.getSolution().getScore() < bestSolution.getScore()) {
					bestSolution = Handler.getSolution().copySolution();
					maxUnintegratedRequests = 0;
				}
			} else if (maxUnintegratedRequests == Handler.getSolution().getUnintegratedRequests().size()) {
				backupSolution = Handler.getSolution().copySolution();
			} else {
				Handler.getSolution().setSolution(backupSolution);
				backupSolution = Handler.getSolution().copySolution();
			}
			RemovalHeuristics.removeRequests(lnsConfig.getRemovalHeuristics()
					.get(Handler.getRandom().nextInt(lnsConfig.getRemovalHeuristics().size())));
		}
		return bestSolution;
	}
}