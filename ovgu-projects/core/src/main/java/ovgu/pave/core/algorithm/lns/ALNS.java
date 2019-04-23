package ovgu.pave.core.algorithm.lns;

import ovgu.pave.core.algorithm.lns.insertion.InsertionHeuristics;
import ovgu.pave.core.algorithm.lns.removal.RemovalHeuristics;
import ovgu.pave.core.algorithm.lns.utilities.RouletteWheelSelection;
import ovgu.pave.core.algorithm.lns.utilities.SolutionEvaluator;
import ovgu.pave.handler.Handler;
import ovgu.pave.model.solution.Solution;

public class ALNS {

	public static Solution performALNS() {

		ovgu.pave.model.config.LNS configLNS = Handler.getInput().getConfig().getAlgorithm().getLns();
		
		RouletteWheelSelection insertionRWS = new RouletteWheelSelection(configLNS.getInsertionHeuristics().size()); 
		RouletteWheelSelection removalRWS = new RouletteWheelSelection(configLNS.getInsertionHeuristics().size()); 
		
		InsertionHeuristics.regretInsertion(1, 0);
		
		Handler.getSolution().setScore(SolutionEvaluator.getTotalTravelTime()
				+ Handler.getSolution().getUnintegratedRequests().size() * configLNS.getPenaltyTerm());
		Solution backupSolution = Handler.getSolution().copySolution();
		Solution bestSolution = Handler.getSolution().copySolution();

		double temperature = ((double) Handler.getSolution().getScore() * configLNS.getTemperatureControlParameter()) / Math.log(2);
		int iteration = 0;
		int maxWithoutImprovement = 0;
		long startTime = System.nanoTime();
		while (System.nanoTime() - startTime <= configLNS.getMaxCalculationTime()
				&& iteration < configLNS.getMaxIterations()) {
			iteration++;
			maxWithoutImprovement++;
			int reasonForAcceptance = 0;
			
			RemovalHeuristics.removeRequests(configLNS.getRemovalHeuristics().get(removalRWS.selectHeuristic()));
			InsertionHeuristics.insertRequests(configLNS.getInsertionHeuristics().get(insertionRWS.selectHeuristic()));

			Handler.getSolution().setScore(SolutionEvaluator.getTotalTravelTime()
					+ Handler.getSolution().getUnintegratedRequests().size() * configLNS.getPenaltyTerm());
			
			if (Handler.getSolution().getScore() < bestSolution.getScore())
				reasonForAcceptance = 1;
			else if (Handler.getSolution().getScore() < backupSolution.getScore())
				reasonForAcceptance = 2;
			else if (Math.exp(-(double) (Handler.getSolution().getScore() - backupSolution.getScore()) / temperature) > Handler.getRandom().nextDouble())
				reasonForAcceptance = 3;

			// update after Iteration
			if (reasonForAcceptance > 0) {
				if (reasonForAcceptance == 1) {
					bestSolution = Handler.getSolution().copySolution();
					maxWithoutImprovement = 0;
				}
				backupSolution = Handler.getSolution().copySolution();
				insertionRWS.updateScore(reasonForAcceptance);
				removalRWS.updateScore(reasonForAcceptance);
			} else {
				Handler.getSolution().setSolution(backupSolution);
				backupSolution = Handler.getSolution().copySolution();
			}
			
			if (maxWithoutImprovement >= configLNS.getMaxWithoutImprovement()) break;			
			if (iteration % 100 == 0) {
				System.out.println("Iteration: " + iteration);
				insertionRWS.updateWheel();
				removalRWS.updateWheel();
			}
			temperature = temperature * configLNS.getCoolingRate();
		}	
		return bestSolution;
	}
}
