package ovgu.utilities;

import java.io.IOException;
import java.util.ArrayList;

import ovgu.data.entity.RouteElement;
import ovgu.vrptw.vrpSolver;

public class Main {

	public Main() {
	}

	public static void main(String[] args) throws IOException {
		
		DistanceMatrix matrix = new DistanceMatrix();
		Instances instances = new Instances();
		ArrayList<ArrayList<RouteElement>> finalRoutes = null;
		
		for (int currentExp = 0; currentExp < Settings.expSize; currentExp++) {

			matrix.readDistanceMatrix();
			instances.createRequests();
			ArrayList<RouteElement> currentRequests;
			vrpSolver vrpSolver = new vrpSolver(matrix);
			
			System.out.println("Exp: " + currentExp);
			for (int i = 0; i < Settings.numberOfInstances; i++) {

				System.out.println("Instance: " + i);
				currentRequests = instances.currentRequests(i);

				switch (Settings.algorithm) {
				case 0:
					finalRoutes = vrpSolver.startInsertion(currentRequests);
					break;
				case 1:
					finalRoutes = vrpSolver.startLMNS(currentRequests);
					break;
				case 2:
					finalRoutes = vrpSolver.startALNS(currentRequests);
					break;
				}
			}
			RouteHandler.printRoute(finalRoutes);
		}
	}
}
