package ovgu.pave.experiments.input;

import java.util.List;

import ovgu.pave.experiments.utils.ReadCSV;
import ovgu.pave.handler.Handler;
import ovgu.pave.handler.modelHandler.InputHandler;
import ovgu.pave.model.input.Location;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.input.RequestActivity;

public class ReadJarmosHDP {

	private static String folderPath;

	public static void readData(String folderPath) {
		ReadJarmosHDP.folderPath = folderPath;

		readRequests("Requests.csv");
		readSequences("Sequences.csv");
		readMatrix("Matrix.csv");
	}

	private static void readRequests(String filename) {
		List<List<Integer>> requests = ReadCSV.read(getPath(filename), ",", int.class, true);

		for (List<Integer> array : requests) {
			Location location = InputHandler.createLocation(array.get(0), 0, 0);
			Handler.getInput().getLocations().add(location);
			Request request = InputHandler.createRequest(array.get(0), location, null);
			setTimeWindow(array.get(1), request.getOriginActivity());
			Handler.getInput().getNewRequests().add(request);
		}
	}

	private static void readSequences(String filename) {
		List<List<Integer>> sequences = ReadCSV.read(getPath(filename), ",", int.class, true);
		for (int i = 0; i < sequences.get(0).size(); i++)
			Handler.getInput().getNewRequests().get(i).setReceivingTime(sequences.get(0).get(i));
	}

	private static void readMatrix(String filename) {
		List<List<Long>> matrix;
		matrix = ReadCSV.read(getPath(filename), ",", long.class, true);

		Handler.getNetwork().initNetwork(Handler.getInput().getLocations());

		for (int x = 0; x < matrix.size(); x++) {
			for (int y = 0; y < matrix.size(); y++) {
				Handler.getNetwork().getEdges()[x][y].setDuration(matrix.get(x).get(y));
				Handler.getInput().addEdge(Handler.getNetwork().getEdges()[x][y]);
			}
		}
	}

	private static String getPath(String filename) {
		return folderPath + filename;
	}
	

	public static void setTimeWindow(int value, RequestActivity activity) {
		switch (value) {
		case 1:
			activity.setEarliestArrival(10 * 60 * 60);
			activity.setLatestArrival(12 * 60 * 60);
			break;
		case 2:
			activity.setEarliestArrival(12 * 60 * 60);
			activity.setLatestArrival(14 * 60 * 60);
			break;
		case 3:
			activity.setEarliestArrival(14 * 60 * 60);
			activity.setLatestArrival(16 * 60 * 60);
			break;
		case 4:
			activity.setEarliestArrival(16 * 60 * 60);
			activity.setLatestArrival(18 * 60 * 60);
			break;
		case 5:
			activity.setEarliestArrival(18 * 60 * 60);
			activity.setLatestArrival(20 * 60 * 60);
			break;
		case 6:
			activity.setEarliestArrival(20 * 60 * 60);
			activity.setLatestArrival(22 * 60 * 60);
			break;
		}
	}
}
