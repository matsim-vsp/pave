package ovgu.pave.core.algorithm.lns.utilities;

import java.util.Arrays;

import ovgu.pave.handler.Handler;

public class RouletteWheelSelection {

	ovgu.pave.model.config.RWS rwsConfig = Handler.getInput().getConfig().getAlgorithm().getLns().getRws();
	int[] rouletteWheel;
	int[] currentScores;
	int[] overallScores;
	int[] heuristicsFrequency;
	int wheelSize;

	private int selcetedHeuristic;

	public RouletteWheelSelection(int numberOfHeuristics) {

		heuristicsFrequency = new int[numberOfHeuristics];
		currentScores = new int[numberOfHeuristics];
		overallScores = new int[numberOfHeuristics];
		rouletteWheel = new int[numberOfHeuristics];
		wheelSize = numberOfHeuristics;
		Arrays.fill(heuristicsFrequency, 1);
		Arrays.fill(overallScores, 1);

		for (int i = 0; i < rouletteWheel.length; i++)
			rouletteWheel[i] = i;
	}

	public void updateScore(int scoreCase) {
		switch (scoreCase) {
		case (1):
			currentScores[selcetedHeuristic] = currentScores[selcetedHeuristic] + rwsConfig.getCase1();
			break;
		case (2):
			currentScores[selcetedHeuristic] = currentScores[selcetedHeuristic] + rwsConfig.getCase2();
			break;
		case (3):
			currentScores[selcetedHeuristic] = currentScores[selcetedHeuristic] + rwsConfig.getCase3();
			break;
		}
	}

	public void updateWheel() {

		overallScores[0] = (int) Math.ceil((overallScores[0] * rwsConfig.getLastScoreInfluence()) + (currentScores[0] / heuristicsFrequency[0]));
		rouletteWheel[0] = overallScores[0];

		for (int i = 1; i < overallScores.length; i++) {
			overallScores[i] = (int) Math.ceil((overallScores[i] *  rwsConfig.getLastScoreInfluence()) + (currentScores[i] / heuristicsFrequency[i]));
			rouletteWheel[i] = rouletteWheel[i - 1] + overallScores[i];
		}

		wheelSize = rouletteWheel[rouletteWheel.length - 1];
		currentScores = new int[6];
		Arrays.fill(heuristicsFrequency, 1);
	}

	public int selectHeuristic() {
		int rouletteSelection;
		rouletteSelection = Handler.getRandom().nextInt(wheelSize);

		for (int i = 0; i < rouletteWheel.length; i++) {
			if (rouletteWheel[i] >= rouletteSelection) {
				selcetedHeuristic = i;
				break;
			}
		}

		heuristicsFrequency[selcetedHeuristic] = heuristicsFrequency[selcetedHeuristic] + 1;
		return selcetedHeuristic;
	}
}