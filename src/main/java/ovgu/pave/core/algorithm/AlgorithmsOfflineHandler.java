package ovgu.pave.core.algorithm;

import ovgu.pave.core.algorithm.lns.ALNS;
import ovgu.pave.core.algorithm.lns.insertion.InsertionHeuristics;
import ovgu.pave.core.algorithm.lns.utilities.InitializeRoutes;
import ovgu.pave.core.algorithm.lns.utilities.SolutionEvaluator;
import ovgu.pave.handler.Handler;

public class AlgorithmsOfflineHandler {

	public enum Algorithm {
		INSERTION, ALNS
	}

	public static void run(String algorithm) {
		runAlgorithm(algorithm);
	}

	private static void runAlgorithm(String algorthim) {

		Handler.getSolution().getUnintegratedRequests().addAll(Handler.getInput().getNewRequests());

		switch (Algorithm.valueOf(algorthim)) {
		case INSERTION:
			runRegret2();
			break;
		case ALNS:
			runALNS();
			break;
		default:
			System.out.println("The selected algorithm is not available for an offline VRP");
			break;
		}
		
		Handler.getSolution().setScore(SolutionEvaluator.getTotalTravelTime());
		Handler.getInput().getAcceptedRequests().addAll(Handler.getSolution().getIntegratedRequests());
		Handler.getInput().getRejectedRequests().addAll(Handler.getSolution().getUnintegratedRequests());
		Handler.getInput().getNewRequests().clear();
	}

	private static void runRegret2() {
		InitializeRoutes.depotStartEnd();
		InsertionHeuristics.regretInsertion(1, 0);
	}
	
	private static void runALNS() {
		InitializeRoutes.depotStartEnd();
		Handler.getSolution().setSolution(ALNS.performALNS());
	}
}
