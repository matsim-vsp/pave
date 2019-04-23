package ovgu.pave.experiments.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ovgu.pave.experiments.utils.ReadCSV;
import ovgu.pave.handler.Handler;
import ovgu.pave.handler.modelHandler.InputHandler;
import ovgu.pave.handler.modelHandler.SolutionHandler;
import ovgu.pave.model.input.DestinationRequestActivity;
import ovgu.pave.model.input.Location;
import ovgu.pave.model.input.OriginRequestActivity;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.RouteElement;
import ovgu.pave.model.solution.Solution;

public class JarmoEWGT {

	private static String folderPath;
	private static List<List<Integer>> vehicleStartLocations;
	private static List<List<Integer>> recievingSequence;
	private static List<List<Integer>> recievingTimes;
	private static List<Integer> numAcceptedRequests;

	public static void initialize() {

		folderPath = (Handler.getInput().getConfig().getInputFolder() + "\\DARP\\");
		vehicleStartLocations = readSequence("EWGT_VehicleStartLocations.csv");
		recievingSequence = readSequence("EWGT_RequestSequence.csv");
		recievingTimes = readSequence("EWGT_RequestTimes.csv");
		Handler.getInput().getVehicleTypes().add(
				InputHandler.createVehicleType(0, Handler.getInput().getConfig().getExperiment().getVehicleCapacity()));
	}

	public static void updateSequence(List<Request> requests, int sequence) {
		createVehicles(sequence);
		setRecieving(requests, sequence);
	}

	public static List<HashMap<String, Double>> aggregateSolution(List<HashMap<String, Double>> results,
			Solution solution, int exp, int instance) {

		double zero = 0;
		if (instance == 0) {

			Handler.getAlgorithmObjects().setAvgAcceptanceIterations(0); 
			Handler.getAlgorithmObjects().setAvgMaxIterations(0);
			Handler.getAlgorithmObjects().setAvgExchangeableRequests(0);
			Handler.getAlgorithmObjects().setAvgNewSolution(0);
			numAcceptedRequests = new ArrayList<Integer>();
			results.add(new HashMap<String, Double>());
			results.get(exp).put("NumAcceptedRequests", zero);
			results.get(exp).put("Position", zero);
			results.get(exp).put("ActivTime", zero);
			results.get(exp).put("IdleTime", zero);
			results.get(exp).put("TravelTime", zero);
			results.get(exp).put("ServiceTime", zero);
			results.get(exp).put("Detours", zero);
			results.get(exp).put("Pooling", zero);
			results.get(exp).put("PoolingDetours", zero);
			results.get(exp).put("WaitingTime", zero);
			results.get(exp).put("AcceptanceIterations", zero);
			results.get(exp).put("MaxIterations", zero);
			results.get(exp).put("ExchangeableRequests", zero);
			results.get(exp).put("NewSolution", zero);
			return results;
		}

		results.get(exp).put("MaxIterations", Handler.getAlgorithmObjects().getAvgMaxIterations());
		results.get(exp).put("ExchangeableRequests", Handler.getAlgorithmObjects().getAvgExchangeableRequests());
		results.get(exp).put("AcceptanceIterations", Handler.getAlgorithmObjects().getAvgAcceptanceIterations());
		results.get(exp).put("NewSolution", Handler.getAlgorithmObjects().getAvgNewSolution());
		
		int acceptedRequests = 0;
		for (Route route : solution.getRoutes()) {

			results.get(exp).put("ActivTime",
					results.get(exp).get("ActivTime")
							+ (SolutionHandler.getServiceEnd(route, route.getRouteElements().size() - 2)
									- (route.getRouteElement(1).getServiceBegin() - Handler.getNetwork()
											.getTravelDuration(route.getRouteElement(0), route.getRouteElement(1)))));

			int utilization = 0;
			for (int j = 0; j < route.getRouteElements().size() - 2; j++) {

				RouteElement currentRouteElement = route.getRouteElement(j);
				RouteElement nextRouteElement = route.getRouteElement(j + 1);

				if (j > 0 && SolutionHandler.getServiceEnd(route, j) < SolutionHandler.getRequest(nextRouteElement)
						.getReceivingTime()) {
					results.get(exp).put("IdleTime",
							results.get(exp).get("IdleTime")
									+ (SolutionHandler.getRequest(nextRouteElement).getReceivingTime()
											- SolutionHandler.getServiceEnd(route, j)));
				}

				results.get(exp).put("TravelTime", results.get(exp).get("TravelTime")
						+ Handler.getNetwork().getTravelDuration(currentRouteElement, nextRouteElement));
				results.get(exp).put("ServiceTime", results.get(exp).get("ServiceTime")
						+ SolutionHandler.getRequestActivity(nextRouteElement).getServiceDuration());

				if (InputHandler.isOriginRequestActivity(SolutionHandler.getRequestActivity(nextRouteElement))) {
					acceptedRequests++;
					results.get(exp).put("NumAcceptedRequests", results.get(exp).get("NumAcceptedRequests") + 1);
					results.get(exp).put("WaitingTime",
							results.get(exp).get("WaitingTime") + (nextRouteElement.getServiceBegin()
									- SolutionHandler.getRequestActivity(nextRouteElement).getEarliestArrival()));

					int checkElement = j + 2;
					while (SolutionHandler.getRequest(nextRouteElement) != SolutionHandler
							.getRequest(route.getRouteElement(checkElement)))
						checkElement++;

					results.get(exp).put("Detours",
							results.get(exp).get("Detours") + (route.getRouteElement(checkElement).getServiceBegin()
									- SolutionHandler.getServiceEnd(route, j + 1) - Handler.getNetwork()
											.getTravelDuration(nextRouteElement, route.getRouteElement(checkElement))));

					if (checkElement != j + 2 || utilization > 0) {
						results.get(exp).put("Pooling", results.get(exp).get("Pooling") + 1);
						results.get(exp).put("PoolingDetours",
								results.get(exp).get("PoolingDetours")
										+ (route.getRouteElement(checkElement).getServiceBegin()
												- SolutionHandler.getServiceEnd(route, j + 1)
												- Handler.getNetwork().getTravelDuration(nextRouteElement,
														route.getRouteElement(checkElement))));
					}

					utilization += SolutionHandler.getChangeInQuantity(nextRouteElement);
				}
			}
		}

		numAcceptedRequests.add(acceptedRequests);
		results.get(exp).put("Variance", calculateVariance(results.get(exp).get("NumAcceptedRequests")));
		return results;
	}

	private static double calculateVariance(double numberOfAcceptence) {
		double mean = (double) numberOfAcceptence / numAcceptedRequests.size();
		double sum = 0;
		for (int i = 0; i < numAcceptedRequests.size(); i++) {
			sum = sum + (numAcceptedRequests.get(i) - mean) * (numAcceptedRequests.get(i) - mean);
		}
		return (double) sum / (numAcceptedRequests.size() - 1);
	}

	public static List<List<String>> getOutput(List<HashMap<String, Double>> results) {

		List<List<String>> output = new ArrayList<List<String>>();

		output.add(Arrays.asList("avgNumAcceptedRequests", "Variance", "avgPooling", "avgActivTime", "avgIdleTime",
				"avgTravelTime", "avgServiceTime", "avgWaitingTime", "avgDetours", "avgPoolingDetours",
				"avgAcceptanceIterations", "avgMaxIterations", "avgExchangeableRequests", "avgNewSolution"));

		int insideIterations = Handler.getInput().getConfig().getExperiment().getNumberOfInsideIterations() - 1;

		for (int exp = 0; exp < results.size(); exp++) {
			ArrayList<String> arrayList = new ArrayList<String>();
			arrayList.add(String.valueOf(results.get(exp).get("NumAcceptedRequests") / insideIterations));
			arrayList.add(String.valueOf(results.get(exp).get("Variance")));
			arrayList.add(String.valueOf(results.get(exp).get("Pooling") / insideIterations));
			arrayList.add(String.valueOf(results.get(exp).get("ActivTime") / insideIterations / 60 / 1000));
			arrayList.add(String.valueOf(results.get(exp).get("IdleTime") / insideIterations / 60 / 1000));
			arrayList.add(String.valueOf(results.get(exp).get("TravelTime") / insideIterations / 60 / 1000));
			arrayList.add(String.valueOf(results.get(exp).get("ServiceTime") / insideIterations / 60 / 1000));
			arrayList.add(String.valueOf(
					results.get(exp).get("WaitingTime") / results.get(exp).get("NumAcceptedRequests") / 60 / 1000));
			arrayList.add(String.valueOf(
					results.get(exp).get("Detours") / results.get(exp).get("NumAcceptedRequests") / 60 / 1000));
			arrayList.add(String
					.valueOf(results.get(exp).get("PoolingDetours") / results.get(exp).get("Pooling") / 60 / 1000));
			arrayList.add(String.valueOf(results.get(exp).get("AcceptanceIterations")));
			arrayList.add(String.valueOf(results.get(exp).get("MaxIterations")));
			arrayList.add(String.valueOf(results.get(exp).get("ExchangeableRequests")));
			arrayList.add(String.valueOf(results.get(exp).get("NewSolution")));
			output.add(arrayList);
		}
		return output;
	}
	
	private static void createVehicles(int sequence) {

		Handler.getInput().getVehicles().clear();
		Location endLocation = Handler.getInput().getLocations().get(306);

		int z= 0;
		for (int i = 0; i < Handler.getInput().getConfig().getExperiment().getNumberOfVehicles(); i++) {
			if(i == 50) {
				sequence += 200;
				z = 50;
			}
			Location startLocation = Handler.getInput().getLocations().get(vehicleStartLocations.get(sequence).get(i-z));
			Handler.getInput().getVehicles().add(InputHandler.createVehicle(i,
					Handler.getInput().getVehicleTypes().get(0), startLocation, endLocation));
		}
	}

	private static List<List<Integer>> readSequence(String recievingTimesFile) {
		return ReadCSV.read(getPath(recievingTimesFile), ",", int.class, true);
	}

	private static void setRecieving(List<Request> requests, int sequence) {

		for (int i = 0; i < Handler.getInput().getConfig().getExperiment().getNumberOfRequests(); i++) {
			int index = recievingSequence.get(sequence).get(i);
			Request request = requests.get(index);
			request.setReceivingTime((recievingTimes.get(sequence).get(i)) * 1000);
			Handler.getInput().getNewRequests().add(request);
		}

		setTimeWindows(sequence);

		if (!Handler.getInput().getConfig().getExperiment().getProblem().matches("ONLINE")) {
			for (int i = 0; i < Handler.getInput().getNewRequests().size(); i++) {
				Handler.getInput().getNewRequests().get(i).setReceivingTime(0);
			}
		}
	}

	private static void setTimeWindows(int sequence) {

		for (int i = 0; i < Handler.getInput().getNewRequests().size(); i++) {
			OriginRequestActivity originActivity = Handler.getInput().getNewRequests().get(i).getOriginActivity();
			DestinationRequestActivity destinationActivity = Handler.getInput().getNewRequests().get(i)
					.getDestinationActivity();

			long maxDelayValue = Handler.getInput().getConfig().getExperiment().getMaxDelayValue() * 60 * 1000;

			originActivity.setEarliestArrival(Handler.getInput().getNewRequests().get(i).getReceivingTime());
			originActivity.setLatestArrival(originActivity.getEarliestArrival() + maxDelayValue);

			long travelDuration = Handler.getNetwork().getTravelDuration(originActivity, destinationActivity);
			destinationActivity.setLatestArrival(originActivity.getLatestArrival() + travelDuration);
		}
	}

	private static String getPath(String filename) {
		return folderPath + filename;
	}
}
