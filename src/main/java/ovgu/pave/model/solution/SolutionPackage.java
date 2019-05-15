/**
 */
package ovgu.pave.model.solution;

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
 * @see ovgu.pave.model.solution.SolutionFactory
 * @model kind="package"
 * @generated
 */
public interface SolutionPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "solution";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "ovgu.pave.solution";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "sol";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SolutionPackage eINSTANCE = ovgu.pave.model.solution.impl.SolutionPackageImpl.init();

	/**
	 * The meta object id for the '{@link ovgu.pave.model.solution.impl.RouteImpl <em>Route</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.solution.impl.RouteImpl
	 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getRoute()
	 * @generated
	 */
	int ROUTE = 0;

	/**
	 * The feature id for the '<em><b>Route Elements</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__ROUTE_ELEMENTS = 0;

	/**
	 * The feature id for the '<em><b>Vehicle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__VEHICLE = 1;

	/**
	 * The feature id for the '<em><b>Vehicle Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__VEHICLE_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Vehicle ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__VEHICLE_ID = 3;

	/**
	 * The feature id for the '<em><b>Vehicle Type ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__VEHICLE_TYPE_ID = 4;

	/**
	 * The feature id for the '<em><b>Utilisation</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE__UTILISATION = 5;

	/**
	 * The number of structural features of the '<em>Route</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_FEATURE_COUNT = 6;

	/**
	 * The operation id for the '<em>Get Route Element</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE___GET_ROUTE_ELEMENT__INT = 0;

	/**
	 * The number of operations of the '<em>Route</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_OPERATION_COUNT = 1;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.solution.impl.RouteElementImpl <em>Route Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.solution.impl.RouteElementImpl
	 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getRouteElement()
	 * @generated
	 */
	int ROUTE_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Service Begin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_ELEMENT__SERVICE_BEGIN = 0;

	/**
	 * The feature id for the '<em><b>Service Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_ELEMENT__SERVICE_DURATION = 1;

	/**
	 * The number of structural features of the '<em>Route Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_ELEMENT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Route Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ROUTE_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.solution.impl.SolutionImpl <em>Solution</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.solution.impl.SolutionImpl
	 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getSolution()
	 * @generated
	 */
	int SOLUTION = 2;

	/**
	 * The feature id for the '<em><b>Routes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION__ROUTES = 0;

	/**
	 * The feature id for the '<em><b>Score</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION__SCORE = 1;

	/**
	 * The feature id for the '<em><b>Use Vehicle Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION__USE_VEHICLE_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Unintegrated Requests</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION__UNINTEGRATED_REQUESTS = 3;

	/**
	 * The feature id for the '<em><b>Integrated Requests</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION__INTEGRATED_REQUESTS = 4;

	/**
	 * The number of structural features of the '<em>Solution</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Solution</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOLUTION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.solution.impl.SupportRouteElementImpl <em>Support Route Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.solution.impl.SupportRouteElementImpl
	 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getSupportRouteElement()
	 * @generated
	 */
	int SUPPORT_ROUTE_ELEMENT = 3;

	/**
	 * The feature id for the '<em><b>Service Begin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPORT_ROUTE_ELEMENT__SERVICE_BEGIN = ROUTE_ELEMENT__SERVICE_BEGIN;

	/**
	 * The feature id for the '<em><b>Service Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPORT_ROUTE_ELEMENT__SERVICE_DURATION = ROUTE_ELEMENT__SERVICE_DURATION;

	/**
	 * The feature id for the '<em><b>Location</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPORT_ROUTE_ELEMENT__LOCATION = ROUTE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Location ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPORT_ROUTE_ELEMENT__LOCATION_ID = ROUTE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Support Route Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPORT_ROUTE_ELEMENT_FEATURE_COUNT = ROUTE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Support Route Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SUPPORT_ROUTE_ELEMENT_OPERATION_COUNT = ROUTE_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.solution.impl.RequestActivityRouteElementImpl <em>Request Activity Route Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.solution.impl.RequestActivityRouteElementImpl
	 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getRequestActivityRouteElement()
	 * @generated
	 */
	int REQUEST_ACTIVITY_ROUTE_ELEMENT = 4;

	/**
	 * The feature id for the '<em><b>Service Begin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_ACTIVITY_ROUTE_ELEMENT__SERVICE_BEGIN = ROUTE_ELEMENT__SERVICE_BEGIN;

	/**
	 * The feature id for the '<em><b>Service Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_ACTIVITY_ROUTE_ELEMENT__SERVICE_DURATION = ROUTE_ELEMENT__SERVICE_DURATION;

	/**
	 * The feature id for the '<em><b>Request Activity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY = ROUTE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Request Activity ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY_ID = ROUTE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Request Activity Route Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_ACTIVITY_ROUTE_ELEMENT_FEATURE_COUNT = ROUTE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Request Activity Route Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_ACTIVITY_ROUTE_ELEMENT_OPERATION_COUNT = ROUTE_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.solution.impl.WayPointRouteElementImpl <em>Way Point Route Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.solution.impl.WayPointRouteElementImpl
	 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getWayPointRouteElement()
	 * @generated
	 */
	int WAY_POINT_ROUTE_ELEMENT = 5;

	/**
	 * The feature id for the '<em><b>Service Begin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAY_POINT_ROUTE_ELEMENT__SERVICE_BEGIN = ROUTE_ELEMENT__SERVICE_BEGIN;

	/**
	 * The feature id for the '<em><b>Service Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAY_POINT_ROUTE_ELEMENT__SERVICE_DURATION = ROUTE_ELEMENT__SERVICE_DURATION;

	/**
	 * The feature id for the '<em><b>Way Point</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAY_POINT_ROUTE_ELEMENT__WAY_POINT = ROUTE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Way Point ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAY_POINT_ROUTE_ELEMENT__WAY_POINT_ID = ROUTE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Way Point Route Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAY_POINT_ROUTE_ELEMENT_FEATURE_COUNT = ROUTE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Way Point Route Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAY_POINT_ROUTE_ELEMENT_OPERATION_COUNT = ROUTE_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.solution.impl.UseVehicleTypeImpl <em>Use Vehicle Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.solution.impl.UseVehicleTypeImpl
	 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getUseVehicleType()
	 * @generated
	 */
	int USE_VEHICLE_TYPE = 6;

	/**
	 * The feature id for the '<em><b>Use Vehicle Type Instead Vehicle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USE_VEHICLE_TYPE__USE_VEHICLE_TYPE_INSTEAD_VEHICLE = 0;

	/**
	 * The feature id for the '<em><b>Vehicle Types</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USE_VEHICLE_TYPE__VEHICLE_TYPES = 1;

	/**
	 * The feature id for the '<em><b>Vehicle Type IDs</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USE_VEHICLE_TYPE__VEHICLE_TYPE_IDS = 2;

	/**
	 * The feature id for the '<em><b>Quantities</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USE_VEHICLE_TYPE__QUANTITIES = 3;

	/**
	 * The number of structural features of the '<em>Use Vehicle Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USE_VEHICLE_TYPE_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Use Vehicle Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int USE_VEHICLE_TYPE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '<em>Vehicle</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.Vehicle
	 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getVehicle()
	 * @generated
	 */
	int VEHICLE = 7;

	/**
	 * The meta object id for the '<em>Request Activity</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.RequestActivity
	 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getRequestActivity()
	 * @generated
	 */
	int REQUEST_ACTIVITY = 8;

	/**
	 * The meta object id for the '<em>Requests</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.Request
	 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getRequests()
	 * @generated
	 */
	int REQUESTS = 9;

	/**
	 * The meta object id for the '<em>Location</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.Location
	 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getLocation()
	 * @generated
	 */
	int LOCATION = 10;

	/**
	 * The meta object id for the '<em>Vehicle Type</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.VehicleType
	 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getVehicleType()
	 * @generated
	 */
	int VEHICLE_TYPE = 11;

	/**
	 * The meta object id for the '<em>Way Point</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.WayPoint
	 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getWayPoint()
	 * @generated
	 */
	int WAY_POINT = 12;


	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.solution.Route <em>Route</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Route</em>'.
	 * @see ovgu.pave.model.solution.Route
	 * @generated
	 */
	EClass getRoute();

	/**
	 * Returns the meta object for the containment reference list '{@link ovgu.pave.model.solution.Route#getRouteElements <em>Route Elements</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Route Elements</em>'.
	 * @see ovgu.pave.model.solution.Route#getRouteElements()
	 * @see #getRoute()
	 * @generated
	 */
	EReference getRoute_RouteElements();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.solution.Route#getVehicle <em>Vehicle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vehicle</em>'.
	 * @see ovgu.pave.model.solution.Route#getVehicle()
	 * @see #getRoute()
	 * @generated
	 */
	EAttribute getRoute_Vehicle();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.solution.Route#getVehicleType <em>Vehicle Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vehicle Type</em>'.
	 * @see ovgu.pave.model.solution.Route#getVehicleType()
	 * @see #getRoute()
	 * @generated
	 */
	EAttribute getRoute_VehicleType();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.solution.Route#getVehicleID <em>Vehicle ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vehicle ID</em>'.
	 * @see ovgu.pave.model.solution.Route#getVehicleID()
	 * @see #getRoute()
	 * @generated
	 */
	EAttribute getRoute_VehicleID();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.solution.Route#getVehicleTypeID <em>Vehicle Type ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vehicle Type ID</em>'.
	 * @see ovgu.pave.model.solution.Route#getVehicleTypeID()
	 * @see #getRoute()
	 * @generated
	 */
	EAttribute getRoute_VehicleTypeID();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.solution.Route#getUtilisation <em>Utilisation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Utilisation</em>'.
	 * @see ovgu.pave.model.solution.Route#getUtilisation()
	 * @see #getRoute()
	 * @generated
	 */
	EAttribute getRoute_Utilisation();

	/**
	 * Returns the meta object for the '{@link ovgu.pave.model.solution.Route#getRouteElement(int) <em>Get Route Element</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Route Element</em>' operation.
	 * @see ovgu.pave.model.solution.Route#getRouteElement(int)
	 * @generated
	 */
	EOperation getRoute__GetRouteElement__int();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.solution.RouteElement <em>Route Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Route Element</em>'.
	 * @see ovgu.pave.model.solution.RouteElement
	 * @generated
	 */
	EClass getRouteElement();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.solution.RouteElement#getServiceBegin <em>Service Begin</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service Begin</em>'.
	 * @see ovgu.pave.model.solution.RouteElement#getServiceBegin()
	 * @see #getRouteElement()
	 * @generated
	 */
	EAttribute getRouteElement_ServiceBegin();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.solution.RouteElement#getServiceDuration <em>Service Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service Duration</em>'.
	 * @see ovgu.pave.model.solution.RouteElement#getServiceDuration()
	 * @see #getRouteElement()
	 * @generated
	 */
	EAttribute getRouteElement_ServiceDuration();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.solution.Solution <em>Solution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Solution</em>'.
	 * @see ovgu.pave.model.solution.Solution
	 * @generated
	 */
	EClass getSolution();

	/**
	 * Returns the meta object for the containment reference list '{@link ovgu.pave.model.solution.Solution#getRoutes <em>Routes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Routes</em>'.
	 * @see ovgu.pave.model.solution.Solution#getRoutes()
	 * @see #getSolution()
	 * @generated
	 */
	EReference getSolution_Routes();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.solution.Solution#getScore <em>Score</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Score</em>'.
	 * @see ovgu.pave.model.solution.Solution#getScore()
	 * @see #getSolution()
	 * @generated
	 */
	EAttribute getSolution_Score();

	/**
	 * Returns the meta object for the containment reference '{@link ovgu.pave.model.solution.Solution#getUseVehicleType <em>Use Vehicle Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Use Vehicle Type</em>'.
	 * @see ovgu.pave.model.solution.Solution#getUseVehicleType()
	 * @see #getSolution()
	 * @generated
	 */
	EReference getSolution_UseVehicleType();

	/**
	 * Returns the meta object for the attribute list '{@link ovgu.pave.model.solution.Solution#getUnintegratedRequests <em>Unintegrated Requests</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Unintegrated Requests</em>'.
	 * @see ovgu.pave.model.solution.Solution#getUnintegratedRequests()
	 * @see #getSolution()
	 * @generated
	 */
	EAttribute getSolution_UnintegratedRequests();

	/**
	 * Returns the meta object for the attribute list '{@link ovgu.pave.model.solution.Solution#getIntegratedRequests <em>Integrated Requests</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Integrated Requests</em>'.
	 * @see ovgu.pave.model.solution.Solution#getIntegratedRequests()
	 * @see #getSolution()
	 * @generated
	 */
	EAttribute getSolution_IntegratedRequests();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.solution.SupportRouteElement <em>Support Route Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Support Route Element</em>'.
	 * @see ovgu.pave.model.solution.SupportRouteElement
	 * @generated
	 */
	EClass getSupportRouteElement();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.solution.SupportRouteElement#getLocation <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Location</em>'.
	 * @see ovgu.pave.model.solution.SupportRouteElement#getLocation()
	 * @see #getSupportRouteElement()
	 * @generated
	 */
	EAttribute getSupportRouteElement_Location();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.solution.SupportRouteElement#getLocationID <em>Location ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Location ID</em>'.
	 * @see ovgu.pave.model.solution.SupportRouteElement#getLocationID()
	 * @see #getSupportRouteElement()
	 * @generated
	 */
	EAttribute getSupportRouteElement_LocationID();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.solution.RequestActivityRouteElement <em>Request Activity Route Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Request Activity Route Element</em>'.
	 * @see ovgu.pave.model.solution.RequestActivityRouteElement
	 * @generated
	 */
	EClass getRequestActivityRouteElement();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.solution.RequestActivityRouteElement#getRequestActivity <em>Request Activity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Request Activity</em>'.
	 * @see ovgu.pave.model.solution.RequestActivityRouteElement#getRequestActivity()
	 * @see #getRequestActivityRouteElement()
	 * @generated
	 */
	EAttribute getRequestActivityRouteElement_RequestActivity();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.solution.RequestActivityRouteElement#getRequestActivityID <em>Request Activity ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Request Activity ID</em>'.
	 * @see ovgu.pave.model.solution.RequestActivityRouteElement#getRequestActivityID()
	 * @see #getRequestActivityRouteElement()
	 * @generated
	 */
	EAttribute getRequestActivityRouteElement_RequestActivityID();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.solution.WayPointRouteElement <em>Way Point Route Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Way Point Route Element</em>'.
	 * @see ovgu.pave.model.solution.WayPointRouteElement
	 * @generated
	 */
	EClass getWayPointRouteElement();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.solution.WayPointRouteElement#getWayPoint <em>Way Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Way Point</em>'.
	 * @see ovgu.pave.model.solution.WayPointRouteElement#getWayPoint()
	 * @see #getWayPointRouteElement()
	 * @generated
	 */
	EAttribute getWayPointRouteElement_WayPoint();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.solution.WayPointRouteElement#getWayPointID <em>Way Point ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Way Point ID</em>'.
	 * @see ovgu.pave.model.solution.WayPointRouteElement#getWayPointID()
	 * @see #getWayPointRouteElement()
	 * @generated
	 */
	EAttribute getWayPointRouteElement_WayPointID();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.solution.UseVehicleType <em>Use Vehicle Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Use Vehicle Type</em>'.
	 * @see ovgu.pave.model.solution.UseVehicleType
	 * @generated
	 */
	EClass getUseVehicleType();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.solution.UseVehicleType#isUseVehicleTypeInsteadVehicle <em>Use Vehicle Type Instead Vehicle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Use Vehicle Type Instead Vehicle</em>'.
	 * @see ovgu.pave.model.solution.UseVehicleType#isUseVehicleTypeInsteadVehicle()
	 * @see #getUseVehicleType()
	 * @generated
	 */
	EAttribute getUseVehicleType_UseVehicleTypeInsteadVehicle();

	/**
	 * Returns the meta object for the attribute list '{@link ovgu.pave.model.solution.UseVehicleType#getVehicleTypes <em>Vehicle Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Vehicle Types</em>'.
	 * @see ovgu.pave.model.solution.UseVehicleType#getVehicleTypes()
	 * @see #getUseVehicleType()
	 * @generated
	 */
	EAttribute getUseVehicleType_VehicleTypes();

	/**
	 * Returns the meta object for the attribute list '{@link ovgu.pave.model.solution.UseVehicleType#getVehicleTypeIDs <em>Vehicle Type IDs</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Vehicle Type IDs</em>'.
	 * @see ovgu.pave.model.solution.UseVehicleType#getVehicleTypeIDs()
	 * @see #getUseVehicleType()
	 * @generated
	 */
	EAttribute getUseVehicleType_VehicleTypeIDs();

	/**
	 * Returns the meta object for the attribute list '{@link ovgu.pave.model.solution.UseVehicleType#getQuantities <em>Quantities</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Quantities</em>'.
	 * @see ovgu.pave.model.solution.UseVehicleType#getQuantities()
	 * @see #getUseVehicleType()
	 * @generated
	 */
	EAttribute getUseVehicleType_Quantities();

	/**
	 * Returns the meta object for data type '{@link ovgu.pave.model.input.Vehicle <em>Vehicle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Vehicle</em>'.
	 * @see ovgu.pave.model.input.Vehicle
	 * @model instanceClass="ovgu.pave.model.input.Vehicle"
	 * @generated
	 */
	EDataType getVehicle();

	/**
	 * Returns the meta object for data type '{@link ovgu.pave.model.input.RequestActivity <em>Request Activity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Request Activity</em>'.
	 * @see ovgu.pave.model.input.RequestActivity
	 * @model instanceClass="ovgu.pave.model.input.RequestActivity"
	 * @generated
	 */
	EDataType getRequestActivity();

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
	 * Returns the meta object for data type '{@link ovgu.pave.model.input.Location <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Location</em>'.
	 * @see ovgu.pave.model.input.Location
	 * @model instanceClass="ovgu.pave.model.input.Location"
	 * @generated
	 */
	EDataType getLocation();

	/**
	 * Returns the meta object for data type '{@link ovgu.pave.model.input.VehicleType <em>Vehicle Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Vehicle Type</em>'.
	 * @see ovgu.pave.model.input.VehicleType
	 * @model instanceClass="ovgu.pave.model.input.VehicleType"
	 * @generated
	 */
	EDataType getVehicleType();

	/**
	 * Returns the meta object for data type '{@link ovgu.pave.model.input.WayPoint <em>Way Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Way Point</em>'.
	 * @see ovgu.pave.model.input.WayPoint
	 * @model instanceClass="ovgu.pave.model.input.WayPoint"
	 * @generated
	 */
	EDataType getWayPoint();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SolutionFactory getSolutionFactory();

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
		 * The meta object literal for the '{@link ovgu.pave.model.solution.impl.RouteImpl <em>Route</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.solution.impl.RouteImpl
		 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getRoute()
		 * @generated
		 */
		EClass ROUTE = eINSTANCE.getRoute();

		/**
		 * The meta object literal for the '<em><b>Route Elements</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ROUTE__ROUTE_ELEMENTS = eINSTANCE.getRoute_RouteElements();

		/**
		 * The meta object literal for the '<em><b>Vehicle</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE__VEHICLE = eINSTANCE.getRoute_Vehicle();

		/**
		 * The meta object literal for the '<em><b>Vehicle Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE__VEHICLE_TYPE = eINSTANCE.getRoute_VehicleType();

		/**
		 * The meta object literal for the '<em><b>Vehicle ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE__VEHICLE_ID = eINSTANCE.getRoute_VehicleID();

		/**
		 * The meta object literal for the '<em><b>Vehicle Type ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE__VEHICLE_TYPE_ID = eINSTANCE.getRoute_VehicleTypeID();

		/**
		 * The meta object literal for the '<em><b>Utilisation</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE__UTILISATION = eINSTANCE.getRoute_Utilisation();

		/**
		 * The meta object literal for the '<em><b>Get Route Element</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ROUTE___GET_ROUTE_ELEMENT__INT = eINSTANCE.getRoute__GetRouteElement__int();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.solution.impl.RouteElementImpl <em>Route Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.solution.impl.RouteElementImpl
		 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getRouteElement()
		 * @generated
		 */
		EClass ROUTE_ELEMENT = eINSTANCE.getRouteElement();

		/**
		 * The meta object literal for the '<em><b>Service Begin</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE_ELEMENT__SERVICE_BEGIN = eINSTANCE.getRouteElement_ServiceBegin();

		/**
		 * The meta object literal for the '<em><b>Service Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ROUTE_ELEMENT__SERVICE_DURATION = eINSTANCE.getRouteElement_ServiceDuration();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.solution.impl.SolutionImpl <em>Solution</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.solution.impl.SolutionImpl
		 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getSolution()
		 * @generated
		 */
		EClass SOLUTION = eINSTANCE.getSolution();

		/**
		 * The meta object literal for the '<em><b>Routes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SOLUTION__ROUTES = eINSTANCE.getSolution_Routes();

		/**
		 * The meta object literal for the '<em><b>Score</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOLUTION__SCORE = eINSTANCE.getSolution_Score();

		/**
		 * The meta object literal for the '<em><b>Use Vehicle Type</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SOLUTION__USE_VEHICLE_TYPE = eINSTANCE.getSolution_UseVehicleType();

		/**
		 * The meta object literal for the '<em><b>Unintegrated Requests</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOLUTION__UNINTEGRATED_REQUESTS = eINSTANCE.getSolution_UnintegratedRequests();

		/**
		 * The meta object literal for the '<em><b>Integrated Requests</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOLUTION__INTEGRATED_REQUESTS = eINSTANCE.getSolution_IntegratedRequests();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.solution.impl.SupportRouteElementImpl <em>Support Route Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.solution.impl.SupportRouteElementImpl
		 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getSupportRouteElement()
		 * @generated
		 */
		EClass SUPPORT_ROUTE_ELEMENT = eINSTANCE.getSupportRouteElement();

		/**
		 * The meta object literal for the '<em><b>Location</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUPPORT_ROUTE_ELEMENT__LOCATION = eINSTANCE.getSupportRouteElement_Location();

		/**
		 * The meta object literal for the '<em><b>Location ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SUPPORT_ROUTE_ELEMENT__LOCATION_ID = eINSTANCE.getSupportRouteElement_LocationID();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.solution.impl.RequestActivityRouteElementImpl <em>Request Activity Route Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.solution.impl.RequestActivityRouteElementImpl
		 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getRequestActivityRouteElement()
		 * @generated
		 */
		EClass REQUEST_ACTIVITY_ROUTE_ELEMENT = eINSTANCE.getRequestActivityRouteElement();

		/**
		 * The meta object literal for the '<em><b>Request Activity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY = eINSTANCE.getRequestActivityRouteElement_RequestActivity();

		/**
		 * The meta object literal for the '<em><b>Request Activity ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY_ID = eINSTANCE.getRequestActivityRouteElement_RequestActivityID();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.solution.impl.WayPointRouteElementImpl <em>Way Point Route Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.solution.impl.WayPointRouteElementImpl
		 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getWayPointRouteElement()
		 * @generated
		 */
		EClass WAY_POINT_ROUTE_ELEMENT = eINSTANCE.getWayPointRouteElement();

		/**
		 * The meta object literal for the '<em><b>Way Point</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WAY_POINT_ROUTE_ELEMENT__WAY_POINT = eINSTANCE.getWayPointRouteElement_WayPoint();

		/**
		 * The meta object literal for the '<em><b>Way Point ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WAY_POINT_ROUTE_ELEMENT__WAY_POINT_ID = eINSTANCE.getWayPointRouteElement_WayPointID();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.solution.impl.UseVehicleTypeImpl <em>Use Vehicle Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.solution.impl.UseVehicleTypeImpl
		 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getUseVehicleType()
		 * @generated
		 */
		EClass USE_VEHICLE_TYPE = eINSTANCE.getUseVehicleType();

		/**
		 * The meta object literal for the '<em><b>Use Vehicle Type Instead Vehicle</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USE_VEHICLE_TYPE__USE_VEHICLE_TYPE_INSTEAD_VEHICLE = eINSTANCE.getUseVehicleType_UseVehicleTypeInsteadVehicle();

		/**
		 * The meta object literal for the '<em><b>Vehicle Types</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USE_VEHICLE_TYPE__VEHICLE_TYPES = eINSTANCE.getUseVehicleType_VehicleTypes();

		/**
		 * The meta object literal for the '<em><b>Vehicle Type IDs</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USE_VEHICLE_TYPE__VEHICLE_TYPE_IDS = eINSTANCE.getUseVehicleType_VehicleTypeIDs();

		/**
		 * The meta object literal for the '<em><b>Quantities</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute USE_VEHICLE_TYPE__QUANTITIES = eINSTANCE.getUseVehicleType_Quantities();

		/**
		 * The meta object literal for the '<em>Vehicle</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.Vehicle
		 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getVehicle()
		 * @generated
		 */
		EDataType VEHICLE = eINSTANCE.getVehicle();

		/**
		 * The meta object literal for the '<em>Request Activity</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.RequestActivity
		 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getRequestActivity()
		 * @generated
		 */
		EDataType REQUEST_ACTIVITY = eINSTANCE.getRequestActivity();

		/**
		 * The meta object literal for the '<em>Requests</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.Request
		 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getRequests()
		 * @generated
		 */
		EDataType REQUESTS = eINSTANCE.getRequests();

		/**
		 * The meta object literal for the '<em>Location</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.Location
		 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getLocation()
		 * @generated
		 */
		EDataType LOCATION = eINSTANCE.getLocation();

		/**
		 * The meta object literal for the '<em>Vehicle Type</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.VehicleType
		 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getVehicleType()
		 * @generated
		 */
		EDataType VEHICLE_TYPE = eINSTANCE.getVehicleType();

		/**
		 * The meta object literal for the '<em>Way Point</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.WayPoint
		 * @see ovgu.pave.model.solution.impl.SolutionPackageImpl#getWayPoint()
		 * @generated
		 */
		EDataType WAY_POINT = eINSTANCE.getWayPoint();

	}

} //SolutionPackage
