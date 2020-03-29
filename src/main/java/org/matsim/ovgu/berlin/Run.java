package org.matsim.ovgu.berlin;

import org.matsim.ovgu.berlin.createNetworkChangeEvents.CreateNetworkChangeEventsFile;
import org.matsim.ovgu.berlin.createTravelTimeMatrix.Version1_ReadChangeEvents;
import org.matsim.ovgu.berlin.createTravelTimeMatrix.Version2_RunSimReadSim;
import org.matsim.ovgu.berlin.createTravelTimeMatrix.Version3_RunSimReadEvents;
import org.matsim.ovgu.berlin.evaluation.Version1_BufferEvaluation;

public class Run {

    public static void main(String[] args) {
    	
// create network change events
    	// setup values in class "CreateNetworkChangeEventsFile"
    	CreateNetworkChangeEventsFile.run();
    	
// create travel time matrix
    	// setup values in class "Input"
    	Version1_ReadChangeEvents.run();
    	Version2_RunSimReadSim.run();
    	Version3_RunSimReadEvents.run();
    	
// run evaluation
    	// setup values in class "Input"
        Version1_BufferEvaluation.run();
    }
}
