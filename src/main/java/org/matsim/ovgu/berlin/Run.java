package org.matsim.ovgu.berlin;

import org.matsim.ovgu.berlin.createNetworkChangeEvents.CreateNetworkChangeEventsFile;
import org.matsim.ovgu.berlin.createTravelTimeMatrix.Version1_ReadChangeEvents;
import org.matsim.ovgu.berlin.createTravelTimeMatrix.Version2_RunSimReadSim;
import org.matsim.ovgu.berlin.createTravelTimeMatrix.Version3_RunSimReadEvents;
import org.matsim.ovgu.berlin.evaluation.Version1_BufferEvaluation;
import org.matsim.ovgu.berlin.evaluation.Version2_SITWA_Evaluation;
import org.matsim.ovgu.berlin.evaluation.Version3_EvalutionFromLinks;
import org.matsim.ovgu.berlin.input.InputTour;

public class Run {

	public static void main(String[] args) {

// create network change events
		// setup values in class "CreateNetworkChangeEventsFile"
//    	CreateNetworkChangeEventsFile.run(); // not checked

// create travel time matrix for tour
		// setup values in class "Input"
//    	Version1_ReadChangeEvents.run(InputTour.tour); // not implemented
//    	Version2_RunSimReadSim.run(InputTour.tour);
//    	Version3_RunSimReadEvents.run(InputTour.tour);

// run evaluation
		// setup values in class "Input"
//      Version1_BufferEvaluation.run();
//        Version2_SITWA_Evaluation.run();
        Version3_EvalutionFromLinks v3 = new Version3_EvalutionFromLinks();
        v3.run(null);
	}
}
