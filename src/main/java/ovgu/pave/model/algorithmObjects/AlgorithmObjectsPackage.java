/**
 */
package ovgu.pave.model.algorithmObjects;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
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
 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsFactory
 * @model kind="package"
 * @generated
 */
public interface AlgorithmObjectsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "algorithmObjects";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "ovgu.pave.algorithmObjects";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "alg";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AlgorithmObjectsPackage eINSTANCE = ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl.init();

	/**
	 * The meta object id for the '{@link ovgu.pave.model.algorithmObjects.impl.LNSImpl <em>LNS</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.algorithmObjects.impl.LNSImpl
	 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getLNS()
	 * @generated
	 */
	int LNS = 0;

	/**
	 * The feature id for the '<em><b>Lowest Costs Per Request</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__LOWEST_COSTS_PER_REQUEST = 0;

	/**
	 * The feature id for the '<em><b>Max Iterations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__MAX_ITERATIONS = 1;

	/**
	 * The feature id for the '<em><b>Rejections</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__REJECTIONS = 2;

	/**
	 * The feature id for the '<em><b>Iteration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__ITERATION = 3;

	/**
	 * The feature id for the '<em><b>Exchangeable Requests</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__EXCHANGEABLE_REQUESTS = 4;

	/**
	 * The feature id for the '<em><b>Main Iteration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__MAIN_ITERATION = 5;

	/**
	 * The feature id for the '<em><b>New Solution</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__NEW_SOLUTION = 6;

	/**
	 * The feature id for the '<em><b>Acceptance Iteration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__ACCEPTANCE_ITERATION = 7;

	/**
	 * The feature id for the '<em><b>Acceptances</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS__ACCEPTANCES = 8;

	/**
	 * The number of structural features of the '<em>LNS</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS_FEATURE_COUNT = 9;

	/**
	 * The number of operations of the '<em>LNS</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LNS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.algorithmObjects.impl.InsertionInformationImpl <em>Insertion Information</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.algorithmObjects.impl.InsertionInformationImpl
	 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getInsertionInformation()
	 * @generated
	 */
	int INSERTION_INFORMATION = 1;

	/**
	 * The feature id for the '<em><b>Insertion Options</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTION_INFORMATION__INSERTION_OPTIONS = 0;

	/**
	 * The feature id for the '<em><b>Request</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTION_INFORMATION__REQUEST = 1;

	/**
	 * The feature id for the '<em><b>Score</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTION_INFORMATION__SCORE = 2;

	/**
	 * The number of structural features of the '<em>Insertion Information</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTION_INFORMATION_FEATURE_COUNT = 3;

	/**
	 * The operation id for the '<em>Set Score For Regret Heuristic</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTION_INFORMATION___SET_SCORE_FOR_REGRET_HEURISTIC__INT = 0;

	/**
	 * The number of operations of the '<em>Insertion Information</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTION_INFORMATION_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.algorithmObjects.impl.InsertionOptionImpl <em>Insertion Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.algorithmObjects.impl.InsertionOptionImpl
	 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getInsertionOption()
	 * @generated
	 */
	int INSERTION_OPTION = 2;

	/**
	 * The feature id for the '<em><b>Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTION_OPTION__COSTS = 0;

	/**
	 * The feature id for the '<em><b>Origin Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTION_OPTION__ORIGIN_INDEX = 1;

	/**
	 * The feature id for the '<em><b>Destination Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTION_OPTION__DESTINATION_INDEX = 2;

	/**
	 * The feature id for the '<em><b>Route</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTION_OPTION__ROUTE = 3;

	/**
	 * The number of structural features of the '<em>Insertion Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTION_OPTION_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Insertion Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSERTION_OPTION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.algorithmObjects.impl.RemovalOptionImpl <em>Removal Option</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.algorithmObjects.impl.RemovalOptionImpl
	 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getRemovalOption()
	 * @generated
	 */
	int REMOVAL_OPTION = 3;

	/**
	 * The feature id for the '<em><b>Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVAL_OPTION__COSTS = 0;

	/**
	 * The feature id for the '<em><b>Route</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVAL_OPTION__ROUTE = 1;

	/**
	 * The feature id for the '<em><b>Origin Route Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVAL_OPTION__ORIGIN_ROUTE_ELEMENT = 2;

	/**
	 * The feature id for the '<em><b>Destination Route Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVAL_OPTION__DESTINATION_ROUTE_ELEMENT = 3;

	/**
	 * The feature id for the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVAL_OPTION__DISTANCE = 4;

	/**
	 * The feature id for the '<em><b>Abs Service Begin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVAL_OPTION__ABS_SERVICE_BEGIN = 5;

	/**
	 * The number of structural features of the '<em>Removal Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVAL_OPTION_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Removal Option</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REMOVAL_OPTION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '<em>Route</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.solution.Route
	 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getRoute()
	 * @generated
	 */
	int ROUTE = 4;

	/**
	 * The meta object id for the '<em>Route Element</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.solution.RouteElement
	 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getRouteElement()
	 * @generated
	 */
	int ROUTE_ELEMENT = 5;

	/**
	 * The meta object id for the '<em>Lowest Costs Hash Map</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.util.HashMap
	 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getLowestCostsHashMap()
	 * @generated
	 */
	int LOWEST_COSTS_HASH_MAP = 6;

	/**
	 * The meta object id for the '<em>Requests</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.Request
	 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getRequests()
	 * @generated
	 */
	int REQUESTS = 7;


	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.algorithmObjects.LNS <em>LNS</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>LNS</em>'.
	 * @see ovgu.pave.model.algorithmObjects.LNS
	 * @generated
	 */
	EClass getLNS();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.LNS#getLowestCostsPerRequest <em>Lowest Costs Per Request</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lowest Costs Per Request</em>'.
	 * @see ovgu.pave.model.algorithmObjects.LNS#getLowestCostsPerRequest()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_LowestCostsPerRequest();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.LNS#getMaxIterations <em>Max Iterations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Iterations</em>'.
	 * @see ovgu.pave.model.algorithmObjects.LNS#getMaxIterations()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_MaxIterations();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.LNS#getRejections <em>Rejections</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Rejections</em>'.
	 * @see ovgu.pave.model.algorithmObjects.LNS#getRejections()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_Rejections();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.LNS#getIteration <em>Iteration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Iteration</em>'.
	 * @see ovgu.pave.model.algorithmObjects.LNS#getIteration()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_Iteration();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.LNS#getExchangeableRequests <em>Exchangeable Requests</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Exchangeable Requests</em>'.
	 * @see ovgu.pave.model.algorithmObjects.LNS#getExchangeableRequests()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_ExchangeableRequests();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.LNS#getMainIteration <em>Main Iteration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Main Iteration</em>'.
	 * @see ovgu.pave.model.algorithmObjects.LNS#getMainIteration()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_MainIteration();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.LNS#getNewSolution <em>New Solution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>New Solution</em>'.
	 * @see ovgu.pave.model.algorithmObjects.LNS#getNewSolution()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_NewSolution();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.LNS#getAcceptanceIteration <em>Acceptance Iteration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Acceptance Iteration</em>'.
	 * @see ovgu.pave.model.algorithmObjects.LNS#getAcceptanceIteration()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_AcceptanceIteration();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.LNS#getAcceptances <em>Acceptances</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Acceptances</em>'.
	 * @see ovgu.pave.model.algorithmObjects.LNS#getAcceptances()
	 * @see #getLNS()
	 * @generated
	 */
	EAttribute getLNS_Acceptances();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.algorithmObjects.InsertionInformation <em>Insertion Information</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Insertion Information</em>'.
	 * @see ovgu.pave.model.algorithmObjects.InsertionInformation
	 * @generated
	 */
	EClass getInsertionInformation();

	/**
	 * Returns the meta object for the reference list '{@link ovgu.pave.model.algorithmObjects.InsertionInformation#getInsertionOptions <em>Insertion Options</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Insertion Options</em>'.
	 * @see ovgu.pave.model.algorithmObjects.InsertionInformation#getInsertionOptions()
	 * @see #getInsertionInformation()
	 * @generated
	 */
	EReference getInsertionInformation_InsertionOptions();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.InsertionInformation#getRequest <em>Request</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Request</em>'.
	 * @see ovgu.pave.model.algorithmObjects.InsertionInformation#getRequest()
	 * @see #getInsertionInformation()
	 * @generated
	 */
	EAttribute getInsertionInformation_Request();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.InsertionInformation#getScore <em>Score</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Score</em>'.
	 * @see ovgu.pave.model.algorithmObjects.InsertionInformation#getScore()
	 * @see #getInsertionInformation()
	 * @generated
	 */
	EAttribute getInsertionInformation_Score();

	/**
	 * Returns the meta object for the '{@link ovgu.pave.model.algorithmObjects.InsertionInformation#setScoreForRegretHeuristic(int) <em>Set Score For Regret Heuristic</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Set Score For Regret Heuristic</em>' operation.
	 * @see ovgu.pave.model.algorithmObjects.InsertionInformation#setScoreForRegretHeuristic(int)
	 * @generated
	 */
	EOperation getInsertionInformation__SetScoreForRegretHeuristic__int();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.algorithmObjects.InsertionOption <em>Insertion Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Insertion Option</em>'.
	 * @see ovgu.pave.model.algorithmObjects.InsertionOption
	 * @generated
	 */
	EClass getInsertionOption();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.InsertionOption#getCosts <em>Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Costs</em>'.
	 * @see ovgu.pave.model.algorithmObjects.InsertionOption#getCosts()
	 * @see #getInsertionOption()
	 * @generated
	 */
	EAttribute getInsertionOption_Costs();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.InsertionOption#getOriginIndex <em>Origin Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Origin Index</em>'.
	 * @see ovgu.pave.model.algorithmObjects.InsertionOption#getOriginIndex()
	 * @see #getInsertionOption()
	 * @generated
	 */
	EAttribute getInsertionOption_OriginIndex();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.InsertionOption#getDestinationIndex <em>Destination Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Destination Index</em>'.
	 * @see ovgu.pave.model.algorithmObjects.InsertionOption#getDestinationIndex()
	 * @see #getInsertionOption()
	 * @generated
	 */
	EAttribute getInsertionOption_DestinationIndex();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.InsertionOption#getRoute <em>Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route</em>'.
	 * @see ovgu.pave.model.algorithmObjects.InsertionOption#getRoute()
	 * @see #getInsertionOption()
	 * @generated
	 */
	EAttribute getInsertionOption_Route();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.algorithmObjects.RemovalOption <em>Removal Option</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Removal Option</em>'.
	 * @see ovgu.pave.model.algorithmObjects.RemovalOption
	 * @generated
	 */
	EClass getRemovalOption();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.RemovalOption#getCosts <em>Costs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Costs</em>'.
	 * @see ovgu.pave.model.algorithmObjects.RemovalOption#getCosts()
	 * @see #getRemovalOption()
	 * @generated
	 */
	EAttribute getRemovalOption_Costs();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.RemovalOption#getRoute <em>Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Route</em>'.
	 * @see ovgu.pave.model.algorithmObjects.RemovalOption#getRoute()
	 * @see #getRemovalOption()
	 * @generated
	 */
	EAttribute getRemovalOption_Route();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.RemovalOption#getOriginRouteElement <em>Origin Route Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Origin Route Element</em>'.
	 * @see ovgu.pave.model.algorithmObjects.RemovalOption#getOriginRouteElement()
	 * @see #getRemovalOption()
	 * @generated
	 */
	EAttribute getRemovalOption_OriginRouteElement();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.RemovalOption#getDestinationRouteElement <em>Destination Route Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Destination Route Element</em>'.
	 * @see ovgu.pave.model.algorithmObjects.RemovalOption#getDestinationRouteElement()
	 * @see #getRemovalOption()
	 * @generated
	 */
	EAttribute getRemovalOption_DestinationRouteElement();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.RemovalOption#getDistance <em>Distance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Distance</em>'.
	 * @see ovgu.pave.model.algorithmObjects.RemovalOption#getDistance()
	 * @see #getRemovalOption()
	 * @generated
	 */
	EAttribute getRemovalOption_Distance();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.algorithmObjects.RemovalOption#getAbsServiceBegin <em>Abs Service Begin</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Abs Service Begin</em>'.
	 * @see ovgu.pave.model.algorithmObjects.RemovalOption#getAbsServiceBegin()
	 * @see #getRemovalOption()
	 * @generated
	 */
	EAttribute getRemovalOption_AbsServiceBegin();

	/**
	 * Returns the meta object for data type '{@link ovgu.pave.model.solution.Route <em>Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Route</em>'.
	 * @see ovgu.pave.model.solution.Route
	 * @model instanceClass="ovgu.pave.model.solution.Route"
	 * @generated
	 */
	EDataType getRoute();

	/**
	 * Returns the meta object for data type '{@link ovgu.pave.model.solution.RouteElement <em>Route Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Route Element</em>'.
	 * @see ovgu.pave.model.solution.RouteElement
	 * @model instanceClass="ovgu.pave.model.solution.RouteElement"
	 * @generated
	 */
	EDataType getRouteElement();

	/**
	 * Returns the meta object for data type '{@link java.util.HashMap <em>Lowest Costs Hash Map</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Lowest Costs Hash Map</em>'.
	 * @see java.util.HashMap
	 * @model instanceClass="java.util.HashMap&lt;ovgu.pave.model.input.Request, java.lang.Long&gt;"
	 * @generated
	 */
	EDataType getLowestCostsHashMap();

	/**
	 * Returns the meta object for data type '{@link ovgu.pave.model.input.Request <em>Requests</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Requests</em>'.
	 * @see ovgu.pave.model.input.Request
	 * @model instanceClass="ovgu.pave.model.input.Request"
	 * @generated
	 */
	EDataType getRequests();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AlgorithmObjectsFactory getAlgorithmObjectsFactory();

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
		 * The meta object literal for the '{@link ovgu.pave.model.algorithmObjects.impl.LNSImpl <em>LNS</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.algorithmObjects.impl.LNSImpl
		 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getLNS()
		 * @generated
		 */
		EClass LNS = eINSTANCE.getLNS();

		/**
		 * The meta object literal for the '<em><b>Lowest Costs Per Request</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__LOWEST_COSTS_PER_REQUEST = eINSTANCE.getLNS_LowestCostsPerRequest();

		/**
		 * The meta object literal for the '<em><b>Max Iterations</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__MAX_ITERATIONS = eINSTANCE.getLNS_MaxIterations();

		/**
		 * The meta object literal for the '<em><b>Rejections</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__REJECTIONS = eINSTANCE.getLNS_Rejections();

		/**
		 * The meta object literal for the '<em><b>Iteration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__ITERATION = eINSTANCE.getLNS_Iteration();

		/**
		 * The meta object literal for the '<em><b>Exchangeable Requests</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__EXCHANGEABLE_REQUESTS = eINSTANCE.getLNS_ExchangeableRequests();

		/**
		 * The meta object literal for the '<em><b>Main Iteration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__MAIN_ITERATION = eINSTANCE.getLNS_MainIteration();

		/**
		 * The meta object literal for the '<em><b>New Solution</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__NEW_SOLUTION = eINSTANCE.getLNS_NewSolution();

		/**
		 * The meta object literal for the '<em><b>Acceptance Iteration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__ACCEPTANCE_ITERATION = eINSTANCE.getLNS_AcceptanceIteration();

		/**
		 * The meta object literal for the '<em><b>Acceptances</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LNS__ACCEPTANCES = eINSTANCE.getLNS_Acceptances();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.algorithmObjects.impl.InsertionInformationImpl <em>Insertion Information</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.algorithmObjects.impl.InsertionInformationImpl
		 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getInsertionInformation()
		 * @generated
		 */
		EClass INSERTION_INFORMATION = eINSTANCE.getInsertionInformation();

		/**
		 * The meta object literal for the '<em><b>Insertion Options</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INSERTION_INFORMATION__INSERTION_OPTIONS = eINSTANCE.getInsertionInformation_InsertionOptions();

		/**
		 * The meta object literal for the '<em><b>Request</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INSERTION_INFORMATION__REQUEST = eINSTANCE.getInsertionInformation_Request();

		/**
		 * The meta object literal for the '<em><b>Score</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INSERTION_INFORMATION__SCORE = eINSTANCE.getInsertionInformation_Score();

		/**
		 * The meta object literal for the '<em><b>Set Score For Regret Heuristic</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation INSERTION_INFORMATION___SET_SCORE_FOR_REGRET_HEURISTIC__INT = eINSTANCE.getInsertionInformation__SetScoreForRegretHeuristic__int();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.algorithmObjects.impl.InsertionOptionImpl <em>Insertion Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.algorithmObjects.impl.InsertionOptionImpl
		 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getInsertionOption()
		 * @generated
		 */
		EClass INSERTION_OPTION = eINSTANCE.getInsertionOption();

		/**
		 * The meta object literal for the '<em><b>Costs</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INSERTION_OPTION__COSTS = eINSTANCE.getInsertionOption_Costs();

		/**
		 * The meta object literal for the '<em><b>Origin Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INSERTION_OPTION__ORIGIN_INDEX = eINSTANCE.getInsertionOption_OriginIndex();

		/**
		 * The meta object literal for the '<em><b>Destination Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INSERTION_OPTION__DESTINATION_INDEX = eINSTANCE.getInsertionOption_DestinationIndex();

		/**
		 * The meta object literal for the '<em><b>Route</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INSERTION_OPTION__ROUTE = eINSTANCE.getInsertionOption_Route();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.algorithmObjects.impl.RemovalOptionImpl <em>Removal Option</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.algorithmObjects.impl.RemovalOptionImpl
		 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getRemovalOption()
		 * @generated
		 */
		EClass REMOVAL_OPTION = eINSTANCE.getRemovalOption();

		/**
		 * The meta object literal for the '<em><b>Costs</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REMOVAL_OPTION__COSTS = eINSTANCE.getRemovalOption_Costs();

		/**
		 * The meta object literal for the '<em><b>Route</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REMOVAL_OPTION__ROUTE = eINSTANCE.getRemovalOption_Route();

		/**
		 * The meta object literal for the '<em><b>Origin Route Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REMOVAL_OPTION__ORIGIN_ROUTE_ELEMENT = eINSTANCE.getRemovalOption_OriginRouteElement();

		/**
		 * The meta object literal for the '<em><b>Destination Route Element</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REMOVAL_OPTION__DESTINATION_ROUTE_ELEMENT = eINSTANCE.getRemovalOption_DestinationRouteElement();

		/**
		 * The meta object literal for the '<em><b>Distance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REMOVAL_OPTION__DISTANCE = eINSTANCE.getRemovalOption_Distance();

		/**
		 * The meta object literal for the '<em><b>Abs Service Begin</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REMOVAL_OPTION__ABS_SERVICE_BEGIN = eINSTANCE.getRemovalOption_AbsServiceBegin();

		/**
		 * The meta object literal for the '<em>Route</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.solution.Route
		 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getRoute()
		 * @generated
		 */
		EDataType ROUTE = eINSTANCE.getRoute();

		/**
		 * The meta object literal for the '<em>Route Element</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.solution.RouteElement
		 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getRouteElement()
		 * @generated
		 */
		EDataType ROUTE_ELEMENT = eINSTANCE.getRouteElement();

		/**
		 * The meta object literal for the '<em>Lowest Costs Hash Map</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.util.HashMap
		 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getLowestCostsHashMap()
		 * @generated
		 */
		EDataType LOWEST_COSTS_HASH_MAP = eINSTANCE.getLowestCostsHashMap();

		/**
		 * The meta object literal for the '<em>Requests</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.Request
		 * @see ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsPackageImpl#getRequests()
		 * @generated
		 */
		EDataType REQUESTS = eINSTANCE.getRequests();

	}

} //AlgorithmObjectsPackage
