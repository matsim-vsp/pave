package ovgu.pave.core;

import ovgu.pave.core.algorithm.AlgorithmsOfflineHandler;
import ovgu.pave.core.algorithm.AlgorithmsOnlineHandler;
import ovgu.pave.core.algorithm.lns.utilities.SolutionEvaluator;
import ovgu.pave.handler.Handler;
import ovgu.pave.model.config.Config;
import ovgu.pave.model.input.Input;
import ovgu.pave.model.network.Network;
import ovgu.pave.model.solution.Solution;

public class Core {

	
	/*
	 * 
	 * initialize what's needed - Config parameters - Input (Requests with
	 * Activities with Locations, Vehicles) - Network must be build
	 * 
	 */

	private static final String _DefaultConfigPath = "../config.xml";

	public void initConfig(String path) {
		Handler.getInput().loadConfig(path);
	}

	public void initConfig(Config config) {
		if (config != null)
			Handler.getInput().setConfig(config);
		else
			initConfig(_DefaultConfigPath);
	}

	public void initInput() {
		Handler.getInput().loadInput();
	}

	public void initInput(Input input) {
		if (input != null)
			Handler.getInput().setInput(input);
		else
			initInput();
	}

	public void initNetwork() {
		Handler.getNetwork().setupNetwork(Handler.getInput());
	}

	public void initNetwork(Network network) {
		if (network != null)
			Handler.getNetwork().setNetwork(network);
		else
			initNetwork();
	}

	public Config getConfig() {
		return Handler.getInput().getConfig();
	}

	/*
	 * 
	 * if you run it a second time (or more then ones), reset it - solution should
	 * be empty - if you want to compare Random should be reseted too
	 * 
	 */

	public void resetSolution() {
		Handler.getSolution().resetSolution();
	}

	public void resetRandom() {
		Handler.resetRandom();
	}

	public void reset() {
		resetSolution();
		resetRandom();
	}

	/*
	 * 
	 * run
	 * 
	 */

	// SEMIONLINE: Request acceptance without moving vehicles
	public enum Problem {
		OFFLINE, SEMIONLINE, ONLINE
	}
	
	public void run() {	
		String algorithm = Handler.getInput().getConfig().getAlgorithm().getAlgorithm();
		switch (Problem.valueOf(Handler.getInput().getConfig().getExperiment().getProblem())) {
		case OFFLINE:
			AlgorithmsOfflineHandler.run(algorithm);
			break;
		case SEMIONLINE:
			AlgorithmsOnlineHandler.run(algorithm, false);
			break;
		case ONLINE:
			AlgorithmsOnlineHandler.run(algorithm, true);
			break;
		}
	}

	/*
	 * 
	 * calculate important/needed values
	 * 
	 */

	public void calculateScore() {
		Handler.getSolution().setScore(SolutionEvaluator.getTotalTravelTime());
	}

	/*
	 * 
	 * get a copy of the solution - important values - generated routes (Vehicle +
	 * RouteElements/Activities)
	 * 
	 */

	public Solution getSolution() {
		return Handler.getSolution().copySolution();
	}
}
