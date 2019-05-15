package ovgu.pave.handler;

import java.util.Random;

import ovgu.pave.handler.modelHandler.AlgorithmObjectsHandler;
import ovgu.pave.handler.modelHandler.InputHandler;
import ovgu.pave.handler.modelHandler.NetworkHandler;
import ovgu.pave.handler.modelHandler.SolutionHandler;;

public class Handler {

	private Handler() {
	}
	private static boolean initialized = false;
	private static Random random;
	
	public static Random getRandom() {
		return random;
	}
	
	/*
	 * 
	 * hopefully one version for each thread
	 * 
	 */
	
	private static final ThreadLocal<InputHandler> _localInputHandler = new ThreadLocal<InputHandler>() {
		protected InputHandler initialValue() {
			return new InputHandler();
		}
	};

	private static final ThreadLocal<NetworkHandler> _localNetworkHandler = new ThreadLocal<NetworkHandler>() {
		protected NetworkHandler initialValue() {
			return new NetworkHandler();
		}
	};

	private static final ThreadLocal<SolutionHandler> _localSolutionHandler = new ThreadLocal<SolutionHandler>() {
		protected SolutionHandler initialValue() {
			return new SolutionHandler();
		}
	};

	private static final ThreadLocal<AlgorithmObjectsHandler> _localAlgorithmObjectsHandler = new ThreadLocal<AlgorithmObjectsHandler>() {
		protected AlgorithmObjectsHandler initialValue() {
			return new AlgorithmObjectsHandler();
		}
	};
	
	private static void initHandlers() {
		if (!initialized) {
			_localInputHandler.get();
			_localNetworkHandler.get();
			_localSolutionHandler.get();
			_localAlgorithmObjectsHandler.get();
			initialized = true;
			random = new Random(_localInputHandler.get().getConfig().getAlgorithm().getRandomSeet());
		}
	}

	public static InputHandler getInput() {
		initHandlers();
		return _localInputHandler.get();
	}

	public static NetworkHandler getNetwork() {
		initHandlers();
		return _localNetworkHandler.get();
	}

	public static SolutionHandler getSolution() {
		initHandlers();
		return _localSolutionHandler.get();
	}

	public static AlgorithmObjectsHandler getAlgorithmObjects() {
		initHandlers();
		return _localAlgorithmObjectsHandler.get();
	}
	
	public static void resetRandom() {
		random = new Random(_localInputHandler.get().getConfig().getAlgorithm().getRandomSeet());
	}

	
//TODO Wird das noch gebraucht?
	/*
	 * 
	 * simple version
	 * 
	 */
	
//	private static InputHandler _localInputHandler = null;
//	private static NetworkHandler _localNetworkHandler = null;
//	private static SolutionHandler _localSolutionHandler = null;
//	private static AlgorithmObjectsHandler _localAlgorithmObjectsHandler = null;
//
//
//	public static InputHandler getInput() {
//		initHandlers();
//		return _localInputHandler;
//	}
//
//	public static NetworkHandler getNetwork() {
//		initHandlers();
//		return _localNetworkHandler;
//	}
//
//	public static SolutionHandler getSolution() {
//		initHandlers();
//		return _localSolutionHandler;
//	}
//
//	public static AlgorithmObjectsHandler getAlgorithmObjects() {
//		initHandlers();
//		return _localAlgorithmObjectsHandler;
//	}
//
//	private static void initHandlers() {
//		if (!initialized) {
//			_localInputHandler = new InputHandler();
//			_localNetworkHandler = new NetworkHandler();
//			_localSolutionHandler = new SolutionHandler();
//			_localAlgorithmObjectsHandler = new AlgorithmObjectsHandler();
//			initialized = true;
//			random = new Random(_localInputHandler.getConfig().getRandomSeet());
//		}
//	}
}
