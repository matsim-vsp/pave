package ovgu.pave.experiments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ovgu.pave.core.Core;
import ovgu.pave.experiments.project.JarmoEWGT;
import ovgu.pave.experiments.utils.RoutesConsoleOutput;
import ovgu.pave.experiments.utils.WriteCSV;
import ovgu.pave.handler.Handler;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.solution.Solution;

public class Lab {

	/*
	 * 
	 * Setup:
	 * 
	 */

	String initialConfigPath = "../config.xml";
	List<Request> requests = new ArrayList<Request>();
	int howManyOutside;
	int howManyInside;
	int  time = 0;
	List<HashMap<String, Double>> results = new ArrayList<HashMap<String, Double>>();

	private void initProject() {
	
		JarmoEWGT.initialize();		
		requests.addAll(Handler.getInput().getNewRequests());
		Handler.getInput().getNewRequests().clear();	
	}
	
	private void setValuesOutside(int outsideCounter) {
		Handler.getInput().getConfig().getExperiment().setNumberOfVehicles(60);
		Handler.getInput().getConfig().getExperiment().setMaxDelayValue(20);
	}
	
	private void setValuesInside(int insideCounter) {
		JarmoEWGT.updateSequence(requests, Handler.getInput().getConfig().getExperiment().getStartSequence() + insideCounter);		
		Handler.getInput().getAcceptedRequests().clear();
		Handler.getInput().getRejectedRequests().clear();
	}
	
	private void aggregateSolution(Solution solution, int exp, int instance) {
		results = JarmoEWGT.aggregateSolution(results, solution, exp, instance);		
	}
	
	private void createOutput() {
		WriteCSV.write(JarmoEWGT.getOutput(results));
	}
	
	/*
	 * 
	 * Code:
	 * 
	 */
	Core core = new Core();
	List<List<Solution>> solutions = new ArrayList<List<Solution>>();

	public static void main(String[] args) {

		new Lab().run();
		System.out.println("done");
	}

	private void run() {

		core.initConfig(initialConfigPath);
		core.initInput();
		core.initNetwork();
		initProject();

		howManyOutside = Handler.getInput().getConfig().getExperiment().getNumberOfOutsideIterations();
		howManyInside =  Handler.getInput().getConfig().getExperiment().getNumberOfInsideIterations();
		
		for (int o = 0; o < howManyOutside; o++) {
			List<Solution> solutionsInside = new ArrayList<Solution>();
			for (int i = 0; i < howManyInside; i++) {
				setValuesInside(i);
				core.reset();
				core.run();
				RoutesConsoleOutput.validateServiceBegin(core.getSolution().getRoutes(), Handler.getNetwork());
				aggregateSolution(core.getSolution(), o, i);  
				solutionsInside.add(core.getSolution());
				System.out.println("finished inside " + (o + 1) + "." + (i + 1));
			}	
			solutions.add(solutionsInside);
			setValuesOutside(o);				
			System.out.println(">>>>> finished outside " + (o + 1) + "  " + time);
		}
		createOutput();
	}
}
