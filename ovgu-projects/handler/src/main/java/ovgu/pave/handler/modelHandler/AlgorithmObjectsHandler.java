package ovgu.pave.handler.modelHandler;

import java.util.HashMap;

import ovgu.pave.model.algorithmObjects.AlgorithmObjectsFactory;
import ovgu.pave.model.algorithmObjects.InsertionInformation;
import ovgu.pave.model.algorithmObjects.InsertionOption;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.algorithmObjects.LNS;
import ovgu.pave.model.algorithmObjects.RemovalOption;

public class AlgorithmObjectsHandler {

	LNS algorithmObjectsLNS = AlgorithmObjectsFactory.eINSTANCE.createLNS();

	public void updateAcceptanceIterations(int iteration) {
		algorithmObjectsLNS.setAcceptances(algorithmObjectsLNS.getAcceptances()+1);
		algorithmObjectsLNS.setAcceptanceIteration(algorithmObjectsLNS.getAcceptanceIteration() + iteration );
	}
	
	public double getAvgAcceptanceIterations() {
		if(algorithmObjectsLNS.getAcceptances() == 0) return -1;
		return (double) algorithmObjectsLNS.getAcceptanceIteration()/algorithmObjectsLNS.getAcceptances();
	}
	
	public void setAvgAcceptanceIterations(int value) {
		algorithmObjectsLNS.setAcceptances(value);
		algorithmObjectsLNS.setAcceptanceIteration(value);
	}
	
	
	public void updateMaxIterations(int iteration) {
		algorithmObjectsLNS.setRejections(algorithmObjectsLNS.getRejections()+1);
		algorithmObjectsLNS.setMaxIterations(algorithmObjectsLNS.getMaxIterations() + iteration );
	}
	
	public double getAvgMaxIterations() {
		if(algorithmObjectsLNS.getRejections() == 0) return -1;
		return (double) algorithmObjectsLNS.getMaxIterations()/algorithmObjectsLNS.getRejections();
	}
	
	public void setAvgMaxIterations(int value) {
		algorithmObjectsLNS.setRejections(value);
		algorithmObjectsLNS.setMaxIterations(value);
	}
	
	public void updateExchangeableRequests(int value) {
		algorithmObjectsLNS.setIteration(algorithmObjectsLNS.getIteration()+1);
		algorithmObjectsLNS.setExchangeableRequests(algorithmObjectsLNS.getExchangeableRequests() + value);
	}
	
	public double getAvgExchangeableRequests() {
		if(algorithmObjectsLNS.getIteration() == 0) return -1;
		return (double) algorithmObjectsLNS.getExchangeableRequests()/algorithmObjectsLNS.getIteration();
	}
	
	public void setAvgExchangeableRequests(int value) {
		algorithmObjectsLNS.setIteration(value);
		algorithmObjectsLNS.setExchangeableRequests(value);
	}
	
	public void updateNewSolutionCounter(int counter) {
		algorithmObjectsLNS.setMainIteration(algorithmObjectsLNS.getMainIteration() + 1);
		algorithmObjectsLNS.setNewSolution(algorithmObjectsLNS.getNewSolution() + counter);
	}
	
	public double getAvgNewSolution() {
		if(algorithmObjectsLNS.getMainIteration() == 0) return -1;
		return (double) algorithmObjectsLNS.getNewSolution()/algorithmObjectsLNS.getMainIteration();
	}
	
	public void setAvgNewSolution(int value) {
		algorithmObjectsLNS.setMainIteration(value);
		algorithmObjectsLNS.setNewSolution(value);
	}
	

	//TODO Historische Daten lagern  
	public HashMap<Request, Long> getLowestCostsPerRequest() {
		if (algorithmObjectsLNS.getLowestCostsPerRequest() == null) {
			algorithmObjectsLNS.setLowestCostsPerRequest(new HashMap<Request, Long>());
		}
		return algorithmObjectsLNS.getLowestCostsPerRequest();
	}

	public static InsertionInformation createInsertionInformation(Request request) {
		InsertionInformation insertionInformation = AlgorithmObjectsFactory.eINSTANCE.createInsertionInformation();
		insertionInformation.setRequest(request);
		return insertionInformation;
	}

	public static InsertionOption createInsertionOption() {
		return AlgorithmObjectsFactory.eINSTANCE.createInsertionOption();
	}
	
	public static RemovalOption createRemovalOption() {
		return AlgorithmObjectsFactory.eINSTANCE.createRemovalOption();
	}
}
