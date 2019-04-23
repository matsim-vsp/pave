package ovgu.pave.core.algorithm.lns.insertion;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ovgu.pave.handler.Handler;
import ovgu.pave.model.algorithmObjects.InsertionInformation;
import ovgu.pave.model.config.Heuristic;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.solution.Route;

public class InsertionHeuristics {
	
	public enum Heuristics {
		REGRET1, REGRET1NOISE, REGRET2, REGRET2NOISE, REGRET3, REGRET3NOISE, REGRET4, REGRET4NOISE
	}

	public static void insertRequests(Heuristic insertionHeurstic) {
		switch (Heuristics.valueOf(insertionHeurstic.getHeuristicName())) {
		case REGRET1:
			regretInsertion(-1, 0);
			break;
		case REGRET1NOISE:
			regretInsertion(0, insertionHeurstic.getNoise());
			break;
		case REGRET2:
			regretInsertion(1, 0);
			break;
		case REGRET2NOISE:
			regretInsertion(1, insertionHeurstic.getNoise());
			break;
		case REGRET3:
			regretInsertion(2, 0);
			break;
		case REGRET3NOISE:
			regretInsertion(2, insertionHeurstic.getNoise());
			break;
		case REGRET4:
			regretInsertion(3, 0);
			break;
		case REGRET4NOISE:
			regretInsertion(3, insertionHeurstic.getNoise());
			break;
		}
	}

	public static void regretInsertion(int regretValue, int noise) {

		Comparator<InsertionInformation> rankingValueComparator = Comparator.comparing(InsertionInformation::getScore);
		Comparator<InsertionInformation> rankingComparatorReversed = rankingValueComparator.reversed();

		List<Request> requests = Handler.getSolution().getUnintegratedRequests();
		ArrayList<InsertionInformation> informationList = new ArrayList<InsertionInformation>();

		// compute insertion costs for all requests
		for (Request request : requests) {
			InsertionInformation insertionInformation = InsertionMethods.computeInsertionInformation(request);
			if (insertionInformation != null) {
				insertionInformation.setScoreForRegretHeuristic(regretValue);
				informationList.add(insertionInformation);
			}
		}
		
		informationList.sort(rankingComparatorReversed);

		// insert requests and update positions
		while (!informationList.isEmpty()) {

			int index = 0;
			if (noise > 0) {
				index = (int) (Math.pow(Handler.getRandom().nextDouble(), (10 - noise)) * informationList.size());
			}
			InsertionMethods.insertRequest(informationList.get(index));
			Route updatedRoute = informationList.get(index).getInsertionOptions().get(0).getRoute();
			informationList.remove(informationList.get(index));

			// Update insertion positions
			for (int i = 0; i < informationList.size(); i++) {
				InsertionMethods.updateInsertionInformation(updatedRoute, informationList.get(i));
				if (informationList.get(i).getInsertionOptions().isEmpty()) {
					informationList.remove(i);
					i--;
				} else
					informationList.get(i).setScoreForRegretHeuristic(regretValue);
			}
			informationList.sort(rankingComparatorReversed);
		}
	}
	
	public static void greedyInsertion() {

		List<Request> requests = Handler.getSolution().getUnintegratedRequests();

		int index;
		for (int i = 0; i < requests.size(); i++) {

			if (requests.size() == 1)
				index = 0;
			else
				index = Handler.getRandom().nextInt(requests.size());

			Request selectedRequest = requests.get(index);
			requests.remove(index);

			InsertionInformation insertionInformation = InsertionMethods.computeInsertionInformation(selectedRequest);
			if (insertionInformation != null) {
				InsertionMethods.insertRequest(insertionInformation);
			}
		}
	}
}
