package ovgu.alns;

import java.util.Arrays;
import java.util.Random;

public class RouletteWheelSelection {

	private Random random = new Random();
	private int[] rouletteWheel;
	private int[] currentScores;
	int[] overallScores;
	private int[] heuristicsFrequency;
	private int wheelSize;

	private int selcetedHeuristic;

	public RouletteWheelSelection(int number) {

		heuristicsFrequency = new int[number];
		currentScores = new int[number];
		overallScores = new int[number];
		rouletteWheel = new int[number];
		wheelSize = number;
		Arrays.fill(heuristicsFrequency, 1);
		Arrays.fill(overallScores, 1);

		for (int i = 0; i < rouletteWheel.length; i++)
			rouletteWheel[i] = i;
	}

	public void updateScore(int scoreCase) {
		switch (scoreCase) {
		case (1):
			currentScores[selcetedHeuristic] = currentScores[selcetedHeuristic] + 33;
			break;
		case (2):
			currentScores[selcetedHeuristic] = currentScores[selcetedHeuristic] + 9;
			break;
		case (3):
			currentScores[selcetedHeuristic] = currentScores[selcetedHeuristic] + 13;
			break;
		}
	}

	public void updateWheel() {

		overallScores[0] = (int) Math
				.ceil((overallScores[0] * 0.1) + (currentScores[0] / heuristicsFrequency[0]));
		rouletteWheel[0] = overallScores[0];

		for (int i = 1; i < overallScores.length; i++) {
			overallScores[i] = (int) Math.ceil(
					(overallScores[i] * 0.1) + (currentScores[i] / heuristicsFrequency[i]));
			rouletteWheel[i] = rouletteWheel[i - 1] + overallScores[i];
		}

		wheelSize = rouletteWheel[rouletteWheel.length - 1];
		currentScores = new int[6];
		Arrays.fill(heuristicsFrequency, 1);
	}

	public int selectHeuristic() {
		int rouletteSelection;
		rouletteSelection = random.nextInt(wheelSize);

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