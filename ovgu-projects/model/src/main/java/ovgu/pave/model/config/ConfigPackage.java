/**
 */
package ovgu.pave.model.config;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see ovgu.pave.model.config.ConfigFactory
 * @model kind="package"
 * @generated
 */
public interface ConfigPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "config";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "ovgu.pave.config";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "con";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ConfigPackage eINSTANCE = ovgu.pave.model.config.impl.ConfigPackageImpl.init();

	/**
	 * The meta object id for the '{@link ovgu.pave.model.config.impl.AlgorithmImpl <em>Algorithm</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.config.impl.AlgorithmImpl
	 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getAlgorithm()
	 * @generated
	 */
	int ALGORITHM = 0;

	/**
	 * The feature id for the '<em><b>Algorithm</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALGORITHM__ALGORITHM = 0;

	/**
	 * The feature id for the '<em><b>Computation Started</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALGORITHM__COMPUTATION_STARTED = 1;

	/**
	 * The feature id for the '<em><b>Computation Finished</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALGORITHM__COMPUTATION_FINISHED = 2;

	/**
	 * The feature id for the '<em><b>Lns</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALGORITHM__LNS = 3;

	/**
	 * The feature id for the '<em><b>Random Seet</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALGORITHM__RANDOM_SEET = 4;

	/**
	 * The number of structural features of the '<em>Algorithm</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALGORITHM_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Algorithm</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALGORITHM_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.config.impl.ConfigImpl <em>Config</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.config.impl.ConfigImpl
	 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getConfig()
	 * @generated
	 */
	int CONFIG = 1;

	/**
	 * The feature id for the '<em><b>Input Folder</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIG__INPUT_FOLDER = 0;

	/**
	 * The feature id for the '<em><b>Shortest Path</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIG__SHORTEST_PATH = 1;

	/**
	 * The feature id for the '<em><b>Experiment</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIG__EXPERIMENT = 2;

	/**
	 * The feature id for the '<em><b>Input Filename</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIG__INPUT_FILENAME = 3;

	/**
	 * The feature id for the '<em><b>Algorithm</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIG__ALGORITHM = 4;

	/**
	 * The feature id for the '<em><b>Config Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIG__CONFIG_PATH = 5;

	/**
	 * The number of structural features of the '<em>Config</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIG_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Config</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONFIG_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.config.impl.ShortestPathImpl <em>Shortest Path</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.config.impl.ShortestPathImpl
	 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getShortestPath()
	 * @generated
	 */
	int SHORTEST_PATH = 2;

	/**
	 * The feature id for the '<em><b>Use</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHORTEST_PATH__USE = 0;

	/**
	 * The feature id for the '<em><b>Graphhopper</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHORTEST_PATH__GRAPHHOPPER = 1;

	/**
	 * The number of structural features of the '<em>Shortest Path</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHORTEST_PATH_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Shortest Path</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SHORTEST_PATH_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.config.impl.ExperimentImpl <em>Experiment</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.config.impl.ExperimentImpl
	 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getExperiment()
	 * @generated
	 */
	int EXPERIMENT = 3;

	/**
	 * The feature id for the '<em><b>Problem</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT__PROBLEM = 0;

	/**
	 * The feature id for the '<em><b>Number Of Requests</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT__NUMBER_OF_REQUESTS = 1;

	/**
	 * The feature id for the '<em><b>Number Of Vehicles</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT__NUMBER_OF_VEHICLES = 2;

	/**
	 * The feature id for the '<em><b>Service Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT__SERVICE_DURATION = 3;

	/**
	 * The feature id for the '<em><b>Time Window Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT__TIME_WINDOW_LENGTH = 4;

	/**
	 * The feature id for the '<em><b>Max Delay Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT__MAX_DELAY_VALUE = 5;

	/**
	 * The feature id for the '<em><b>Start Sequence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT__START_SEQUENCE = 6;

	/**
	 * The feature id for the '<em><b>Number Of Inside Iterations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT__NUMBER_OF_INSIDE_ITERATIONS = 7;

	/**
	 * The feature id for the '<em><b>Output Folder</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT__OUTPUT_FOLDER = 8;

	/**
	 * The feature id for the '<em><b>Number Of Outside Iterations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT__NUMBER_OF_OUTSIDE_ITERATIONS = 9;

	/**
	 * The feature id for the '<em><b>Vehicle Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT__VEHICLE_CAPACITY = 10;

	/**
	 * The number of structural features of the '<em>Experiment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT_FEATURE_COUNT = 11;

	/**
	 * The number of operations of the '<em>Experiment</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXPERIMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.config.impl.GraphHopperImpl <em>Graph Hopper</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.config.impl.GraphHopperImpl
	 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getGraphHopper()
	 * @generated
	 */
	int GRAPH_HOPPER = 4;

	/**
	 * The feature id for the '<em><b>Osm File Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_HOPPER__OSM_FILE_PATH = 0;

	/**
	 * The feature id for the '<em><b>Graph Folder</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_HOPPER__GRAPH_FOLDER = 1;

	/**
	 * The feature id for the '<em><b>Weighting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_HOPPER__WEIGHTING = 2;

	/**
	 * The feature id for the '<em><b>Vehicle Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_HOPPER__VEHICLE_TYPE = 3;

	/**
	 * The number of structural features of the '<em>Graph Hopper</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_HOPPER_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Graph Hopper</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int GRAPH_HOPPER_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.config.impl.LNSImpl <em>LNS</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.config.impl.LNSImpl
	 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getLNS()
	 * @generated
	 */
	int LNS = 5;

	/**
	 * The feature id for the '<em><b>Penalty Term</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__PENALTY_TERM = 0;

	/**
	 * The feature id for the '<em><b>Temperature Control Parameter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__TEMPERATURE_CONTROL_PARAMETER = 1;

	/**
	 * The feature id for the '<em><b>Cooling Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__COOLING_RATE = 2;

	/**
	 * The feature id for the '<em><b>Min Small Request Set</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__MIN_SMALL_REQUEST_SET = 3;

	/**
	 * The feature id for the '<em><b>Max Large Request Set</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__MAX_LARGE_REQUEST_SET = 4;

	/**
	 * The feature id for the '<em><b>Max Small Request Set</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__MAX_SMALL_REQUEST_SET = 5;

	/**
	 * The feature id for the '<em><b>Min Large Request Set</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__MIN_LARGE_REQUEST_SET = 6;

	/**
	 * The feature id for the '<em><b>Max Iterations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__MAX_ITERATIONS = 7;

	/**
	 * The feature id for the '<em><b>Max Calculation Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__MAX_CALCULATION_TIME = 8;

	/**
	 * The feature id for the '<em><b>Insertion Heuristics</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__INSERTION_HEURISTICS = 9;

	/**
	 * The feature id for the '<em><b>Removal Heuristics</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__REMOVAL_HEURISTICS = 10;

	/**
	 * The feature id for the '<em><b>Rws</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__RWS = 11;

	/**
	 * The feature id for the '<em><b>Cluster Percentage Per Route</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__CLUSTER_PERCENTAGE_PER_ROUTE = 12;

	/**
	 * The feature id for the '<em><b>Shaw Distance Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__SHAW_DISTANCE_WEIGHT = 13;

	/**
	 * The feature id for the '<em><b>Shaw Begin Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__SHAW_BEGIN_WEIGHT = 14;

	/**
	 * The feature id for the '<em><b>Max Without Improvement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__MAX_WITHOUT_IMPROVEMENT = 15;

	/**
	 * The number of structural features of the '<em>LNS</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS_FEATURE_COUNT = 16;

	/**
	 * The number of operations of the '<em>LNS</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.config.impl.HeuristicImpl <em>Heuristic</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.config.impl.HeuristicImpl
	 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getHeuristic()
	 * @generated
	 */
	int HEURISTIC = 6;

	/**
	 * The feature id for the '<em><b>Heuristic Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEURISTIC__HEURISTIC_NAME = 0;

	/**
	 * The feature id for the '<em><b>Noise</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEURISTIC__NOISE = 1;

	/**
	 * The number of structural features of the '<em>Heuristic</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEURISTIC_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Heuristic</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int HEURISTIC_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.config.impl.RWSImpl <em>RWS</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.config.impl.RWSImpl
	 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getRWS()
	 * @generated
	 */
	int RWS = 7;

	/**
	 * The feature id for the '<em><b>Case1</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RWS__CASE1 = 0;

	/**
	 * The feature id for the '<em><b>Case2</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RWS__CASE2 = 1;

	/**
	 * The feature id for the '<em><b>Case3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RWS__CASE3 = 2;

	/**
	 * The feature id for the '<em><b>Last Score Influence</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RWS__LAST_SCORE_INFLUENCE = 3;

	/**
	 * The number of structural features of the '<em>RWS</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RWS_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>RWS</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RWS_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.config.Algorithm <em>Algorithm</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Algorithm</em>'.
	 * @see ovgu.pave.model.config.Algorithm
	 * @generated
	 */
	EClass getAlgorithm();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Algorithm#getAlgorithm <em>Algorithm</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Algorithm</em>'.
	 * @see ovgu.pave.model.config.Algorithm#getAlgorithm()
	 * @see #getAlgorithm()
	 * @generated
	 */
	EAttribute getAlgorithm_Algorithm();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Algorithm#getComputationStarted <em>Computation Started</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Computation Started</em>'.
	 * @see ovgu.pave.model.config.Algorithm#getComputationStarted()
	 * @see #getAlgorithm()
	 * @generated
	 */
	EAttribute getAlgorithm_ComputationStarted();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Algorithm#getComputationFinished <em>Computation Finished</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Computation Finished</em>'.
	 * @see ovgu.pave.model.config.Algorithm#getComputationFinished()
	 * @see #getAlgorithm()
	 * @generated
	 */
	EAttribute getAlgorithm_ComputationFinished();

	/**
	 * Returns the meta object for the containment reference '{@link ovgu.pave.model.config.Algorithm#getLns <em>Lns</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Lns</em>'.
	 * @see ovgu.pave.model.config.Algorithm#getLns()
	 * @see #getAlgorithm()
	 * @generated
	 */
	EReference getAlgorithm_Lns();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Algorithm#getRandomSeet <em>Random Seet</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Random Seet</em>'.
	 * @see ovgu.pave.model.config.Algorithm#getRandomSeet()
	 * @see #getAlgorithm()
	 * @generated
	 */
	EAttribute getAlgorithm_RandomSeet();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.config.Config <em>Config</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Config</em>'.
	 * @see ovgu.pave.model.config.Config
	 * @generated
	 */
	EClass getConfig();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Config#getInputFolder <em>Input Folder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Input Folder</em>'.
	 * @see ovgu.pave.model.config.Config#getInputFolder()
	 * @see #getConfig()
	 * @generated
	 */
	EAttribute getConfig_InputFolder();

	/**
	 * Returns the meta object for the containment reference '{@link ovgu.pave.model.config.Config#getShortestPath <em>Shortest Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Shortest Path</em>'.
	 * @see ovgu.pave.model.config.Config#getShortestPath()
	 * @see #getConfig()
	 * @generated
	 */
	EReference getConfig_ShortestPath();

	/**
	 * Returns the meta object for the containment reference '{@link ovgu.pave.model.config.Config#getExperiment <em>Experiment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Experiment</em>'.
	 * @see ovgu.pave.model.config.Config#getExperiment()
	 * @see #getConfig()
	 * @generated
	 */
	EReference getConfig_Experiment();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Config#getInputFilename <em>Input Filename</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Input Filename</em>'.
	 * @see ovgu.pave.model.config.Config#getInputFilename()
	 * @see #getConfig()
	 * @generated
	 */
	EAttribute getConfig_InputFilename();

	/**
	 * Returns the meta object for the containment reference '{@link ovgu.pave.model.config.Config#getAlgorithm <em>Algorithm</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Algorithm</em>'.
	 * @see ovgu.pave.model.config.Config#getAlgorithm()
	 * @see #getConfig()
	 * @generated
	 */
	EReference getConfig_Algorithm();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Config#getConfigPath <em>Config Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Config Path</em>'.
	 * @see ovgu.pave.model.config.Config#getConfigPath()
	 * @see #getConfig()
	 * @generated
	 */
	EAttribute getConfig_ConfigPath();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.config.ShortestPath <em>Shortest Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Shortest Path</em>'.
	 * @see ovgu.pave.model.config.ShortestPath
	 * @generated
	 */
	EClass getShortestPath();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.ShortestPath#getUse <em>Use</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use</em>'.
	 * @see ovgu.pave.model.config.ShortestPath#getUse()
	 * @see #getShortestPath()
	 * @generated
	 */
	EAttribute getShortestPath_Use();

	/**
	 * Returns the meta object for the containment reference '{@link ovgu.pave.model.config.ShortestPath#getGraphhopper <em>Graphhopper</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Graphhopper</em>'.
	 * @see ovgu.pave.model.config.ShortestPath#getGraphhopper()
	 * @see #getShortestPath()
	 * @generated
	 */
	EReference getShortestPath_Graphhopper();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.config.Experiment <em>Experiment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Experiment</em>'.
	 * @see ovgu.pave.model.config.Experiment
	 * @generated
	 */
	EClass getExperiment();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Experiment#getProblem <em>Problem</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Problem</em>'.
	 * @see ovgu.pave.model.config.Experiment#getProblem()
	 * @see #getExperiment()
	 * @generated
	 */
	EAttribute getExperiment_Problem();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Experiment#getNumberOfRequests <em>Number Of Requests</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number Of Requests</em>'.
	 * @see ovgu.pave.model.config.Experiment#getNumberOfRequests()
	 * @see #getExperiment()
	 * @generated
	 */
	EAttribute getExperiment_NumberOfRequests();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Experiment#getNumberOfVehicles <em>Number Of Vehicles</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number Of Vehicles</em>'.
	 * @see ovgu.pave.model.config.Experiment#getNumberOfVehicles()
	 * @see #getExperiment()
	 * @generated
	 */
	EAttribute getExperiment_NumberOfVehicles();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Experiment#getServiceDuration <em>Service Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service Duration</em>'.
	 * @see ovgu.pave.model.config.Experiment#getServiceDuration()
	 * @see #getExperiment()
	 * @generated
	 */
	EAttribute getExperiment_ServiceDuration();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Experiment#getTimeWindowLength <em>Time Window Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Time Window Length</em>'.
	 * @see ovgu.pave.model.config.Experiment#getTimeWindowLength()
	 * @see #getExperiment()
	 * @generated
	 */
	EAttribute getExperiment_TimeWindowLength();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Experiment#getMaxDelayValue <em>Max Delay Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Delay Value</em>'.
	 * @see ovgu.pave.model.config.Experiment#getMaxDelayValue()
	 * @see #getExperiment()
	 * @generated
	 */
	EAttribute getExperiment_MaxDelayValue();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Experiment#getStartSequence <em>Start Sequence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start Sequence</em>'.
	 * @see ovgu.pave.model.config.Experiment#getStartSequence()
	 * @see #getExperiment()
	 * @generated
	 */
	EAttribute getExperiment_StartSequence();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Experiment#getNumberOfInsideIterations <em>Number Of Inside Iterations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number Of Inside Iterations</em>'.
	 * @see ovgu.pave.model.config.Experiment#getNumberOfInsideIterations()
	 * @see #getExperiment()
	 * @generated
	 */
	EAttribute getExperiment_NumberOfInsideIterations();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Experiment#getOutputFolder <em>Output Folder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Output Folder</em>'.
	 * @see ovgu.pave.model.config.Experiment#getOutputFolder()
	 * @see #getExperiment()
	 * @generated
	 */
	EAttribute getExperiment_OutputFolder();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Experiment#getNumberOfOutsideIterations <em>Number Of Outside Iterations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Number Of Outside Iterations</em>'.
	 * @see ovgu.pave.model.config.Experiment#getNumberOfOutsideIterations()
	 * @see #getExperiment()
	 * @generated
	 */
	EAttribute getExperiment_NumberOfOutsideIterations();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Experiment#getVehicleCapacity <em>Vehicle Capacity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vehicle Capacity</em>'.
	 * @see ovgu.pave.model.config.Experiment#getVehicleCapacity()
	 * @see #getExperiment()
	 * @generated
	 */
	EAttribute getExperiment_VehicleCapacity();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.config.GraphHopper <em>Graph Hopper</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Graph Hopper</em>'.
	 * @see ovgu.pave.model.config.GraphHopper
	 * @generated
	 */
	EClass getGraphHopper();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.GraphHopper#getOsmFilePath <em>Osm File Path</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Osm File Path</em>'.
	 * @see ovgu.pave.model.config.GraphHopper#getOsmFilePath()
	 * @see #getGraphHopper()
	 * @generated
	 */
	EAttribute getGraphHopper_OsmFilePath();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.GraphHopper#getGraphFolder <em>Graph Folder</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Graph Folder</em>'.
	 * @see ovgu.pave.model.config.GraphHopper#getGraphFolder()
	 * @see #getGraphHopper()
	 * @generated
	 */
	EAttribute getGraphHopper_GraphFolder();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.GraphHopper#getWeighting <em>Weighting</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Weighting</em>'.
	 * @see ovgu.pave.model.config.GraphHopper#getWeighting()
	 * @see #getGraphHopper()
	 * @generated
	 */
	EAttribute getGraphHopper_Weighting();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.GraphHopper#getVehicleType <em>Vehicle Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vehicle Type</em>'.
	 * @see ovgu.pave.model.config.GraphHopper#getVehicleType()
	 * @see #getGraphHopper()
	 * @generated
	 */
	EAttribute getGraphHopper_VehicleType();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.config.LNS <em>LNS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>LNS</em>'.
	 * @see ovgu.pave.model.config.LNS
	 * @generated
	 */
	EClass getLNS();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.LNS#getPenaltyTerm <em>Penalty Term</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Penalty Term</em>'.
	 * @see ovgu.pave.model.config.LNS#getPenaltyTerm()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_PenaltyTerm();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.LNS#getTemperatureControlParameter <em>Temperature Control Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Temperature Control Parameter</em>'.
	 * @see ovgu.pave.model.config.LNS#getTemperatureControlParameter()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_TemperatureControlParameter();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.LNS#getCoolingRate <em>Cooling Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cooling Rate</em>'.
	 * @see ovgu.pave.model.config.LNS#getCoolingRate()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_CoolingRate();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.LNS#getMinSmallRequestSet <em>Min Small Request Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Small Request Set</em>'.
	 * @see ovgu.pave.model.config.LNS#getMinSmallRequestSet()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_MinSmallRequestSet();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.LNS#getMaxLargeRequestSet <em>Max Large Request Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Large Request Set</em>'.
	 * @see ovgu.pave.model.config.LNS#getMaxLargeRequestSet()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_MaxLargeRequestSet();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.LNS#getMaxSmallRequestSet <em>Max Small Request Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Small Request Set</em>'.
	 * @see ovgu.pave.model.config.LNS#getMaxSmallRequestSet()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_MaxSmallRequestSet();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.LNS#getMinLargeRequestSet <em>Min Large Request Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Large Request Set</em>'.
	 * @see ovgu.pave.model.config.LNS#getMinLargeRequestSet()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_MinLargeRequestSet();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.LNS#getMaxIterations <em>Max Iterations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Iterations</em>'.
	 * @see ovgu.pave.model.config.LNS#getMaxIterations()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_MaxIterations();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.LNS#getMaxCalculationTime <em>Max Calculation Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Calculation Time</em>'.
	 * @see ovgu.pave.model.config.LNS#getMaxCalculationTime()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_MaxCalculationTime();

	/**
	 * Returns the meta object for the containment reference list '{@link ovgu.pave.model.config.LNS#getInsertionHeuristics <em>Insertion Heuristics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Insertion Heuristics</em>'.
	 * @see ovgu.pave.model.config.LNS#getInsertionHeuristics()
	 * @see #getLNS()
	 * @generated
	 */
	EReference getLNS_InsertionHeuristics();

	/**
	 * Returns the meta object for the containment reference list '{@link ovgu.pave.model.config.LNS#getRemovalHeuristics <em>Removal Heuristics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Removal Heuristics</em>'.
	 * @see ovgu.pave.model.config.LNS#getRemovalHeuristics()
	 * @see #getLNS()
	 * @generated
	 */
	EReference getLNS_RemovalHeuristics();

	/**
	 * Returns the meta object for the containment reference '{@link ovgu.pave.model.config.LNS#getRws <em>Rws</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Rws</em>'.
	 * @see ovgu.pave.model.config.LNS#getRws()
	 * @see #getLNS()
	 * @generated
	 */
	EReference getLNS_Rws();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.LNS#getClusterPercentagePerRoute <em>Cluster Percentage Per Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cluster Percentage Per Route</em>'.
	 * @see ovgu.pave.model.config.LNS#getClusterPercentagePerRoute()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_ClusterPercentagePerRoute();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.LNS#getShawDistanceWeight <em>Shaw Distance Weight</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Shaw Distance Weight</em>'.
	 * @see ovgu.pave.model.config.LNS#getShawDistanceWeight()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_ShawDistanceWeight();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.LNS#getShawBeginWeight <em>Shaw Begin Weight</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Shaw Begin Weight</em>'.
	 * @see ovgu.pave.model.config.LNS#getShawBeginWeight()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_ShawBeginWeight();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.LNS#getMaxWithoutImprovement <em>Max Without Improvement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Without Improvement</em>'.
	 * @see ovgu.pave.model.config.LNS#getMaxWithoutImprovement()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_MaxWithoutImprovement();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.config.Heuristic <em>Heuristic</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Heuristic</em>'.
	 * @see ovgu.pave.model.config.Heuristic
	 * @generated
	 */
	EClass getHeuristic();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Heuristic#getHeuristicName <em>Heuristic Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Heuristic Name</em>'.
	 * @see ovgu.pave.model.config.Heuristic#getHeuristicName()
	 * @see #getHeuristic()
	 * @generated
	 */
	EAttribute getHeuristic_HeuristicName();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.Heuristic#getNoise <em>Noise</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Noise</em>'.
	 * @see ovgu.pave.model.config.Heuristic#getNoise()
	 * @see #getHeuristic()
	 * @generated
	 */
	EAttribute getHeuristic_Noise();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.config.RWS <em>RWS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>RWS</em>'.
	 * @see ovgu.pave.model.config.RWS
	 * @generated
	 */
	EClass getRWS();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.RWS#getCase1 <em>Case1</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Case1</em>'.
	 * @see ovgu.pave.model.config.RWS#getCase1()
	 * @see #getRWS()
	 * @generated
	 */
	EAttribute getRWS_Case1();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.RWS#getCase2 <em>Case2</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Case2</em>'.
	 * @see ovgu.pave.model.config.RWS#getCase2()
	 * @see #getRWS()
	 * @generated
	 */
	EAttribute getRWS_Case2();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.RWS#getCase3 <em>Case3</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Case3</em>'.
	 * @see ovgu.pave.model.config.RWS#getCase3()
	 * @see #getRWS()
	 * @generated
	 */
	EAttribute getRWS_Case3();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.config.RWS#getLastScoreInfluence <em>Last Score Influence</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Last Score Influence</em>'.
	 * @see ovgu.pave.model.config.RWS#getLastScoreInfluence()
	 * @see #getRWS()
	 * @generated
	 */
	EAttribute getRWS_LastScoreInfluence();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ConfigFactory getConfigFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link ovgu.pave.model.config.impl.AlgorithmImpl <em>Algorithm</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.config.impl.AlgorithmImpl
		 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getAlgorithm()
		 * @generated
		 */
		EClass ALGORITHM = eINSTANCE.getAlgorithm();

		/**
		 * The meta object literal for the '<em><b>Algorithm</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ALGORITHM__ALGORITHM = eINSTANCE.getAlgorithm_Algorithm();

		/**
		 * The meta object literal for the '<em><b>Computation Started</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ALGORITHM__COMPUTATION_STARTED = eINSTANCE.getAlgorithm_ComputationStarted();

		/**
		 * The meta object literal for the '<em><b>Computation Finished</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ALGORITHM__COMPUTATION_FINISHED = eINSTANCE.getAlgorithm_ComputationFinished();

		/**
		 * The meta object literal for the '<em><b>Lns</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ALGORITHM__LNS = eINSTANCE.getAlgorithm_Lns();

		/**
		 * The meta object literal for the '<em><b>Random Seet</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ALGORITHM__RANDOM_SEET = eINSTANCE.getAlgorithm_RandomSeet();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.config.impl.ConfigImpl <em>Config</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.config.impl.ConfigImpl
		 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getConfig()
		 * @generated
		 */
		EClass CONFIG = eINSTANCE.getConfig();

		/**
		 * The meta object literal for the '<em><b>Input Folder</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONFIG__INPUT_FOLDER = eINSTANCE.getConfig_InputFolder();

		/**
		 * The meta object literal for the '<em><b>Shortest Path</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONFIG__SHORTEST_PATH = eINSTANCE.getConfig_ShortestPath();

		/**
		 * The meta object literal for the '<em><b>Experiment</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONFIG__EXPERIMENT = eINSTANCE.getConfig_Experiment();

		/**
		 * The meta object literal for the '<em><b>Input Filename</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONFIG__INPUT_FILENAME = eINSTANCE.getConfig_InputFilename();

		/**
		 * The meta object literal for the '<em><b>Algorithm</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONFIG__ALGORITHM = eINSTANCE.getConfig_Algorithm();

		/**
		 * The meta object literal for the '<em><b>Config Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONFIG__CONFIG_PATH = eINSTANCE.getConfig_ConfigPath();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.config.impl.ShortestPathImpl <em>Shortest Path</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.config.impl.ShortestPathImpl
		 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getShortestPath()
		 * @generated
		 */
		EClass SHORTEST_PATH = eINSTANCE.getShortestPath();

		/**
		 * The meta object literal for the '<em><b>Use</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SHORTEST_PATH__USE = eINSTANCE.getShortestPath_Use();

		/**
		 * The meta object literal for the '<em><b>Graphhopper</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SHORTEST_PATH__GRAPHHOPPER = eINSTANCE.getShortestPath_Graphhopper();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.config.impl.ExperimentImpl <em>Experiment</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.config.impl.ExperimentImpl
		 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getExperiment()
		 * @generated
		 */
		EClass EXPERIMENT = eINSTANCE.getExperiment();

		/**
		 * The meta object literal for the '<em><b>Problem</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPERIMENT__PROBLEM = eINSTANCE.getExperiment_Problem();

		/**
		 * The meta object literal for the '<em><b>Number Of Requests</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPERIMENT__NUMBER_OF_REQUESTS = eINSTANCE.getExperiment_NumberOfRequests();

		/**
		 * The meta object literal for the '<em><b>Number Of Vehicles</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPERIMENT__NUMBER_OF_VEHICLES = eINSTANCE.getExperiment_NumberOfVehicles();

		/**
		 * The meta object literal for the '<em><b>Service Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPERIMENT__SERVICE_DURATION = eINSTANCE.getExperiment_ServiceDuration();

		/**
		 * The meta object literal for the '<em><b>Time Window Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPERIMENT__TIME_WINDOW_LENGTH = eINSTANCE.getExperiment_TimeWindowLength();

		/**
		 * The meta object literal for the '<em><b>Max Delay Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPERIMENT__MAX_DELAY_VALUE = eINSTANCE.getExperiment_MaxDelayValue();

		/**
		 * The meta object literal for the '<em><b>Start Sequence</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPERIMENT__START_SEQUENCE = eINSTANCE.getExperiment_StartSequence();

		/**
		 * The meta object literal for the '<em><b>Number Of Inside Iterations</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPERIMENT__NUMBER_OF_INSIDE_ITERATIONS = eINSTANCE.getExperiment_NumberOfInsideIterations();

		/**
		 * The meta object literal for the '<em><b>Output Folder</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPERIMENT__OUTPUT_FOLDER = eINSTANCE.getExperiment_OutputFolder();

		/**
		 * The meta object literal for the '<em><b>Number Of Outside Iterations</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPERIMENT__NUMBER_OF_OUTSIDE_ITERATIONS = eINSTANCE.getExperiment_NumberOfOutsideIterations();

		/**
		 * The meta object literal for the '<em><b>Vehicle Capacity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EXPERIMENT__VEHICLE_CAPACITY = eINSTANCE.getExperiment_VehicleCapacity();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.config.impl.GraphHopperImpl <em>Graph Hopper</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.config.impl.GraphHopperImpl
		 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getGraphHopper()
		 * @generated
		 */
		EClass GRAPH_HOPPER = eINSTANCE.getGraphHopper();

		/**
		 * The meta object literal for the '<em><b>Osm File Path</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRAPH_HOPPER__OSM_FILE_PATH = eINSTANCE.getGraphHopper_OsmFilePath();

		/**
		 * The meta object literal for the '<em><b>Graph Folder</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRAPH_HOPPER__GRAPH_FOLDER = eINSTANCE.getGraphHopper_GraphFolder();

		/**
		 * The meta object literal for the '<em><b>Weighting</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRAPH_HOPPER__WEIGHTING = eINSTANCE.getGraphHopper_Weighting();

		/**
		 * The meta object literal for the '<em><b>Vehicle Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute GRAPH_HOPPER__VEHICLE_TYPE = eINSTANCE.getGraphHopper_VehicleType();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.config.impl.LNSImpl <em>LNS</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.config.impl.LNSImpl
		 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getLNS()
		 * @generated
		 */
		EClass LNS = eINSTANCE.getLNS();

		/**
		 * The meta object literal for the '<em><b>Penalty Term</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__PENALTY_TERM = eINSTANCE.getLNS_PenaltyTerm();

		/**
		 * The meta object literal for the '<em><b>Temperature Control Parameter</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__TEMPERATURE_CONTROL_PARAMETER = eINSTANCE.getLNS_TemperatureControlParameter();

		/**
		 * The meta object literal for the '<em><b>Cooling Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__COOLING_RATE = eINSTANCE.getLNS_CoolingRate();

		/**
		 * The meta object literal for the '<em><b>Min Small Request Set</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__MIN_SMALL_REQUEST_SET = eINSTANCE.getLNS_MinSmallRequestSet();

		/**
		 * The meta object literal for the '<em><b>Max Large Request Set</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__MAX_LARGE_REQUEST_SET = eINSTANCE.getLNS_MaxLargeRequestSet();

		/**
		 * The meta object literal for the '<em><b>Max Small Request Set</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__MAX_SMALL_REQUEST_SET = eINSTANCE.getLNS_MaxSmallRequestSet();

		/**
		 * The meta object literal for the '<em><b>Min Large Request Set</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__MIN_LARGE_REQUEST_SET = eINSTANCE.getLNS_MinLargeRequestSet();

		/**
		 * The meta object literal for the '<em><b>Max Iterations</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__MAX_ITERATIONS = eINSTANCE.getLNS_MaxIterations();

		/**
		 * The meta object literal for the '<em><b>Max Calculation Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__MAX_CALCULATION_TIME = eINSTANCE.getLNS_MaxCalculationTime();

		/**
		 * The meta object literal for the '<em><b>Insertion Heuristics</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LNS__INSERTION_HEURISTICS = eINSTANCE.getLNS_InsertionHeuristics();

		/**
		 * The meta object literal for the '<em><b>Removal Heuristics</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LNS__REMOVAL_HEURISTICS = eINSTANCE.getLNS_RemovalHeuristics();

		/**
		 * The meta object literal for the '<em><b>Rws</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LNS__RWS = eINSTANCE.getLNS_Rws();

		/**
		 * The meta object literal for the '<em><b>Cluster Percentage Per Route</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__CLUSTER_PERCENTAGE_PER_ROUTE = eINSTANCE.getLNS_ClusterPercentagePerRoute();

		/**
		 * The meta object literal for the '<em><b>Shaw Distance Weight</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__SHAW_DISTANCE_WEIGHT = eINSTANCE.getLNS_ShawDistanceWeight();

		/**
		 * The meta object literal for the '<em><b>Shaw Begin Weight</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__SHAW_BEGIN_WEIGHT = eINSTANCE.getLNS_ShawBeginWeight();

		/**
		 * The meta object literal for the '<em><b>Max Without Improvement</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__MAX_WITHOUT_IMPROVEMENT = eINSTANCE.getLNS_MaxWithoutImprovement();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.config.impl.HeuristicImpl <em>Heuristic</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.config.impl.HeuristicImpl
		 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getHeuristic()
		 * @generated
		 */
		EClass HEURISTIC = eINSTANCE.getHeuristic();

		/**
		 * The meta object literal for the '<em><b>Heuristic Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HEURISTIC__HEURISTIC_NAME = eINSTANCE.getHeuristic_HeuristicName();

		/**
		 * The meta object literal for the '<em><b>Noise</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute HEURISTIC__NOISE = eINSTANCE.getHeuristic_Noise();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.config.impl.RWSImpl <em>RWS</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.config.impl.RWSImpl
		 * @see ovgu.pave.model.config.impl.ConfigPackageImpl#getRWS()
		 * @generated
		 */
		EClass RWS = eINSTANCE.getRWS();

		/**
		 * The meta object literal for the '<em><b>Case1</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RWS__CASE1 = eINSTANCE.getRWS_Case1();

		/**
		 * The meta object literal for the '<em><b>Case2</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RWS__CASE2 = eINSTANCE.getRWS_Case2();

		/**
		 * The meta object literal for the '<em><b>Case3</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RWS__CASE3 = eINSTANCE.getRWS_Case3();

		/**
		 * The meta object literal for the '<em><b>Last Score Influence</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RWS__LAST_SCORE_INFLUENCE = eINSTANCE.getRWS_LastScoreInfluence();

	}

} //ConfigPackage
