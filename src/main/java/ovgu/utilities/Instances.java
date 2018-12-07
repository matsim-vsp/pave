package ovgu.utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

import ovgu.data.entity.RouteElement;
import ovgu.timewindows.TimeWindow;
import ovgu.timewindows.TimeWindowList;

public class Instances {
	
	int[][] sequenceInstances; 
	ArrayList<RouteElement> customerRequests = new ArrayList<RouteElement>();
	
	public void readSequences() throws FileNotFoundException {
		sequenceInstances = new int[1000][400];
		Scanner scanIn = null;
		int Rowc = 0;
		String InputLine = "";
		scanIn = new Scanner (new BufferedReader (new FileReader(System.getProperty("user.dir") + "/Sequence_Instances.csv")));
		
		while(scanIn.hasNextLine()){
			InputLine = scanIn.nextLine();
			String[] InArray = InputLine.split(",");
			for(int x=0; x<400 && Rowc<1000; x++ ){sequenceInstances[Rowc][x] = Integer.parseInt(InArray[x]);}
			Rowc++;
		}
		scanIn.close();
	}
	
	public int[][] readRequests() throws FileNotFoundException {
		
		int[][] requests = new int[400][3];
		Scanner scanIn = null;
		int Rowc = 0;
		String InputLine = "";
		scanIn = new Scanner (new BufferedReader (new FileReader(System.getProperty("user.dir") + "/Requests.csv")));
		
		while(scanIn.hasNextLine()){
			InputLine = scanIn.nextLine();
			String[] InArray = InputLine.split(",");
			for(int x=0; x<3 && Rowc<400; x++ ){
				requests[Rowc][x] = Integer.parseInt(InArray[x]);			
			}
			Rowc++;
		}
		scanIn.close();
		return requests;
	}
	
	public void createRequests() throws FileNotFoundException{
		
		readSequences();
		int[][] requests = readRequests();
		
		ArrayList<TimeWindow> timeWindowList = new ArrayList<TimeWindow>();
		TimeWindowList timeWindowList120 = new TimeWindowList(timeWindowList);
		timeWindowList120.addTimeWindow(timeWindowList);	
		
		for(int i = 0; i < 400; i++) {
		 	
			RouteElement currentRequest = new RouteElement(requests[i][0], requests[i][1], requests[i][2]);
	
			currentRequest.setPositionDistanceMatrix(currentRequest.getId());
			switch (Settings.timeWindowDistribution) {
        	case 0: currentRequest.setTimeWindowKey(currentRequest.getTimeWindowIDRandom());       	
        			break;
        	case 1: currentRequest.setTimeWindowKey(currentRequest.getTimeWindowIDAllyouneed());
					break;
			}
			currentRequest.setStartTimeWindow(timeWindowList.get(currentRequest.getTimeWindowKey()-1).getBegin());
			currentRequest.setEndTimeWindow(timeWindowList.get(currentRequest.getTimeWindowKey()-1).getEnd());
			customerRequests.add(currentRequest);
		}	
	}
		 
	
	public ArrayList<RouteElement> currentRequests(int instance) {
		ArrayList<RouteElement> currentRequests = new ArrayList<RouteElement>();;
		for(int j = 0; j < Settings.numberOfCustomers; j++) {
			currentRequests.add(customerRequests.get(sequenceInstances[instance][j]-1));			
		}
		return currentRequests;	 
	}
}