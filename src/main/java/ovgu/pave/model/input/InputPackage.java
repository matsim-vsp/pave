/**
 */
package ovgu.pave.model.input;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * @see ovgu.pave.model.input.InputFactory
 * @model kind="package"
 * @generated
 */
public interface InputPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "input";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "ovgu.pave.input";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "in";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	InputPackage eINSTANCE = ovgu.pave.model.input.impl.InputPackageImpl.init();

	/**
	 * The meta object id for the '{@link ovgu.pave.model.input.impl.InputImpl <em>Input</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.impl.InputImpl
	 * @see ovgu.pave.model.input.impl.InputPackageImpl#getInput()
	 * @generated
	 */
	int INPUT = 0;

	/**
	 * The feature id for the '<em><b>Requests</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT__REQUESTS = 0;

	/**
	 * The feature id for the '<em><b>Vehicles</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT__VEHICLES = 1;

	/**
	 * The feature id for the '<em><b>Vehicle Types</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT__VEHICLE_TYPES = 2;

	/**
	 * The feature id for the '<em><b>Locations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT__LOCATIONS = 3;

	/**
	 * The feature id for the '<em><b>Edges</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT__EDGES = 4;

	/**
	 * The feature id for the '<em><b>Solution</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT__SOLUTION = 5;

	/**
	 * The number of structural features of the '<em>Input</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Input</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INPUT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.input.impl.RequestImpl <em>Request</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.impl.RequestImpl
	 * @see ovgu.pave.model.input.impl.InputPackageImpl#getRequest()
	 * @generated
	 */
	int REQUEST = 1;

	/**
	 * The feature id for the '<em><b>Origin Activity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST__ORIGIN_ACTIVITY = 0;

	/**
	 * The feature id for the '<em><b>Destination Activity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST__DESTINATION_ACTIVITY = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST__ID = 2;

	/**
	 * The feature id for the '<em><b>Receiving Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST__RECEIVING_TIME = 3;

	/**
	 * The feature id for the '<em><b>Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST__QUANTITY = 4;

	/**
	 * The number of structural features of the '<em>Request</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Request</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.input.impl.LocationImpl <em>Location</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.impl.LocationImpl
	 * @see ovgu.pave.model.input.impl.InputPackageImpl#getLocation()
	 * @generated
	 */
	int LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Lat</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__LAT = 0;

	/**
	 * The feature id for the '<em><b>Lon</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__LON = 1;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION__ID = 2;

	/**
	 * The number of structural features of the '<em>Location</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Location</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOCATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.input.impl.VehicleTypeImpl <em>Vehicle Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.impl.VehicleTypeImpl
	 * @see ovgu.pave.model.input.impl.InputPackageImpl#getVehicleType()
	 * @generated
	 */
	int VEHICLE_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VEHICLE_TYPE__ID = 0;

	/**
	 * The feature id for the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VEHICLE_TYPE__CAPACITY = 1;

	/**
	 * The number of structural features of the '<em>Vehicle Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VEHICLE_TYPE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Vehicle Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VEHICLE_TYPE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.input.impl.VehicleImpl <em>Vehicle</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.impl.VehicleImpl
	 * @see ovgu.pave.model.input.impl.InputPackageImpl#getVehicle()
	 * @generated
	 */
	int VEHICLE = 4;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VEHICLE__TYPE = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VEHICLE__ID = 1;

	/**
	 * The feature id for the '<em><b>Start Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VEHICLE__START_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>End Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VEHICLE__END_LOCATION = 3;

	/**
	 * The number of structural features of the '<em>Vehicle</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VEHICLE_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Vehicle</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VEHICLE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.input.impl.RequestActivityImpl <em>Request Activity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.impl.RequestActivityImpl
	 * @see ovgu.pave.model.input.impl.InputPackageImpl#getRequestActivity()
	 * @generated
	 */
	int REQUEST_ACTIVITY = 6;

	/**
	 * The feature id for the '<em><b>Request</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_ACTIVITY__REQUEST = 0;

	/**
	 * The feature id for the '<em><b>Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_ACTIVITY__LOCATION = 1;

	/**
	 * The feature id for the '<em><b>Service Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_ACTIVITY__SERVICE_DURATION = 2;

	/**
	 * The feature id for the '<em><b>Earliest Arrival</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_ACTIVITY__EARLIEST_ARRIVAL = 3;

	/**
	 * The feature id for the '<em><b>Latest Arrival</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_ACTIVITY__LATEST_ARRIVAL = 4;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_ACTIVITY__ID = 5;

	/**
	 * The number of structural features of the '<em>Request Activity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_ACTIVITY_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Request Activity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUEST_ACTIVITY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.input.impl.OriginRequestActivityImpl <em>Origin Request Activity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.impl.OriginRequestActivityImpl
	 * @see ovgu.pave.model.input.impl.InputPackageImpl#getOriginRequestActivity()
	 * @generated
	 */
	int ORIGIN_REQUEST_ACTIVITY = 5;

	/**
	 * The feature id for the '<em><b>Request</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_REQUEST_ACTIVITY__REQUEST = REQUEST_ACTIVITY__REQUEST;

	/**
	 * The feature id for the '<em><b>Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_REQUEST_ACTIVITY__LOCATION = REQUEST_ACTIVITY__LOCATION;

	/**
	 * The feature id for the '<em><b>Service Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_REQUEST_ACTIVITY__SERVICE_DURATION = REQUEST_ACTIVITY__SERVICE_DURATION;

	/**
	 * The feature id for the '<em><b>Earliest Arrival</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_REQUEST_ACTIVITY__EARLIEST_ARRIVAL = REQUEST_ACTIVITY__EARLIEST_ARRIVAL;

	/**
	 * The feature id for the '<em><b>Latest Arrival</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_REQUEST_ACTIVITY__LATEST_ARRIVAL = REQUEST_ACTIVITY__LATEST_ARRIVAL;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_REQUEST_ACTIVITY__ID = REQUEST_ACTIVITY__ID;

	/**
	 * The number of structural features of the '<em>Origin Request Activity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_REQUEST_ACTIVITY_FEATURE_COUNT = REQUEST_ACTIVITY_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Origin Request Activity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORIGIN_REQUEST_ACTIVITY_OPERATION_COUNT = REQUEST_ACTIVITY_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.input.impl.DestinationRequestActivityImpl <em>Destination Request Activity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.impl.DestinationRequestActivityImpl
	 * @see ovgu.pave.model.input.impl.InputPackageImpl#getDestinationRequestActivity()
	 * @generated
	 */
	int DESTINATION_REQUEST_ACTIVITY = 7;

	/**
	 * The feature id for the '<em><b>Request</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESTINATION_REQUEST_ACTIVITY__REQUEST = REQUEST_ACTIVITY__REQUEST;

	/**
	 * The feature id for the '<em><b>Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESTINATION_REQUEST_ACTIVITY__LOCATION = REQUEST_ACTIVITY__LOCATION;

	/**
	 * The feature id for the '<em><b>Service Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESTINATION_REQUEST_ACTIVITY__SERVICE_DURATION = REQUEST_ACTIVITY__SERVICE_DURATION;

	/**
	 * The feature id for the '<em><b>Earliest Arrival</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESTINATION_REQUEST_ACTIVITY__EARLIEST_ARRIVAL = REQUEST_ACTIVITY__EARLIEST_ARRIVAL;

	/**
	 * The feature id for the '<em><b>Latest Arrival</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESTINATION_REQUEST_ACTIVITY__LATEST_ARRIVAL = REQUEST_ACTIVITY__LATEST_ARRIVAL;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESTINATION_REQUEST_ACTIVITY__ID = REQUEST_ACTIVITY__ID;

	/**
	 * The number of structural features of the '<em>Destination Request Activity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESTINATION_REQUEST_ACTIVITY_FEATURE_COUNT = REQUEST_ACTIVITY_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Destination Request Activity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DESTINATION_REQUEST_ACTIVITY_OPERATION_COUNT = REQUEST_ACTIVITY_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.input.impl.EdgeImpl <em>Edge</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.impl.EdgeImpl
	 * @see ovgu.pave.model.input.impl.InputPackageImpl#getEdge()
	 * @generated
	 */
	int EDGE = 8;

	/**
	 * The feature id for the '<em><b>Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE__COST = 0;

	/**
	 * The feature id for the '<em><b>Start Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE__START_LOCATION = 1;

	/**
	 * The feature id for the '<em><b>End Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE__END_LOCATION = 2;

	/**
	 * The feature id for the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE__DISTANCE = 3;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE__DURATION = 4;

	/**
	 * The feature id for the '<em><b>Waypoints</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE__WAYPOINTS = 5;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE__ID = 6;

	/**
	 * The number of structural features of the '<em>Edge</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_FEATURE_COUNT = 7;

	/**
	 * The number of operations of the '<em>Edge</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EDGE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.input.impl.WayPointImpl <em>Way Point</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.impl.WayPointImpl
	 * @see ovgu.pave.model.input.impl.InputPackageImpl#getWayPoint()
	 * @generated
	 */
	int WAY_POINT = 9;

	/**
	 * The feature id for the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAY_POINT__DISTANCE = 0;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAY_POINT__DURATION = 1;

	/**
	 * The feature id for the '<em><b>Lat</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAY_POINT__LAT = 2;

	/**
	 * The feature id for the '<em><b>Lon</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAY_POINT__LON = 3;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAY_POINT__ID = 4;

	/**
	 * The number of structural features of the '<em>Way Point</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAY_POINT_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Way Point</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WAY_POINT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link ovgu.pave.model.input.impl.RequestsImpl <em>Requests</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.impl.RequestsImpl
	 * @see ovgu.pave.model.input.impl.InputPackageImpl#getRequests()
	 * @generated
	 */
	int REQUESTS = 10;

	/**
	 * The feature id for the '<em><b>Accepted</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUESTS__ACCEPTED = 0;

	/**
	 * The feature id for the '<em><b>Rejected</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUESTS__REJECTED = 1;

	/**
	 * The feature id for the '<em><b>New</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUESTS__NEW = 2;

	/**
	 * The number of structural features of the '<em>Requests</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUESTS_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Requests</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REQUESTS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '<em>Solution</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.solution.Solution
	 * @see ovgu.pave.model.input.impl.InputPackageImpl#getSolution()
	 * @generated
	 */
	int SOLUTION = 11;


	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.input.Input <em>Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Input</em>'.
	 * @see ovgu.pave.model.input.Input
	 * @generated
	 */
	EClass getInput();

	/**
	 * Returns the meta object for the containment reference '{@link ovgu.pave.model.input.Input#getRequests <em>Requests</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Requests</em>'.
	 * @see ovgu.pave.model.input.Input#getRequests()
	 * @see #getInput()
	 * @generated
	 */
	EReference getInput_Requests();

	/**
	 * Returns the meta object for the containment reference list '{@link ovgu.pave.model.input.Input#getVehicles <em>Vehicles</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Vehicles</em>'.
	 * @see ovgu.pave.model.input.Input#getVehicles()
	 * @see #getInput()
	 * @generated
	 */
	EReference getInput_Vehicles();

	/**
	 * Returns the meta object for the containment reference list '{@link ovgu.pave.model.input.Input#getVehicleTypes <em>Vehicle Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Vehicle Types</em>'.
	 * @see ovgu.pave.model.input.Input#getVehicleTypes()
	 * @see #getInput()
	 * @generated
	 */
	EReference getInput_VehicleTypes();

	/**
	 * Returns the meta object for the containment reference list '{@link ovgu.pave.model.input.Input#getLocations <em>Locations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Locations</em>'.
	 * @see ovgu.pave.model.input.Input#getLocations()
	 * @see #getInput()
	 * @generated
	 */
	EReference getInput_Locations();

	/**
	 * Returns the meta object for the containment reference list '{@link ovgu.pave.model.input.Input#getEdges <em>Edges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Edges</em>'.
	 * @see ovgu.pave.model.input.Input#getEdges()
	 * @see #getInput()
	 * @generated
	 */
	EReference getInput_Edges();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.Input#getSolution <em>Solution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Solution</em>'.
	 * @see ovgu.pave.model.input.Input#getSolution()
	 * @see #getInput()
	 * @generated
	 */
	EAttribute getInput_Solution();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.input.Request <em>Request</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Request</em>'.
	 * @see ovgu.pave.model.input.Request
	 * @generated
	 */
	EClass getRequest();

	/**
	 * Returns the meta object for the containment reference '{@link ovgu.pave.model.input.Request#getOriginActivity <em>Origin Activity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Origin Activity</em>'.
	 * @see ovgu.pave.model.input.Request#getOriginActivity()
	 * @see #getRequest()
	 * @generated
	 */
	EReference getRequest_OriginActivity();

	/**
	 * Returns the meta object for the containment reference '{@link ovgu.pave.model.input.Request#getDestinationActivity <em>Destination Activity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Destination Activity</em>'.
	 * @see ovgu.pave.model.input.Request#getDestinationActivity()
	 * @see #getRequest()
	 * @generated
	 */
	EReference getRequest_DestinationActivity();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.Request#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see ovgu.pave.model.input.Request#getId()
	 * @see #getRequest()
	 * @generated
	 */
	EAttribute getRequest_Id();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.Request#getReceivingTime <em>Receiving Time</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Receiving Time</em>'.
	 * @see ovgu.pave.model.input.Request#getReceivingTime()
	 * @see #getRequest()
	 * @generated
	 */
	EAttribute getRequest_ReceivingTime();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.Request#getQuantity <em>Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Quantity</em>'.
	 * @see ovgu.pave.model.input.Request#getQuantity()
	 * @see #getRequest()
	 * @generated
	 */
	EAttribute getRequest_Quantity();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.input.Location <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Location</em>'.
	 * @see ovgu.pave.model.input.Location
	 * @generated
	 */
	EClass getLocation();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.Location#getLat <em>Lat</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lat</em>'.
	 * @see ovgu.pave.model.input.Location#getLat()
	 * @see #getLocation()
	 * @generated
	 */
	EAttribute getLocation_Lat();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.Location#getLon <em>Lon</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lon</em>'.
	 * @see ovgu.pave.model.input.Location#getLon()
	 * @see #getLocation()
	 * @generated
	 */
	EAttribute getLocation_Lon();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.Location#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see ovgu.pave.model.input.Location#getId()
	 * @see #getLocation()
	 * @generated
	 */
	EAttribute getLocation_Id();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.input.VehicleType <em>Vehicle Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vehicle Type</em>'.
	 * @see ovgu.pave.model.input.VehicleType
	 * @generated
	 */
	EClass getVehicleType();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.VehicleType#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see ovgu.pave.model.input.VehicleType#getId()
	 * @see #getVehicleType()
	 * @generated
	 */
	EAttribute getVehicleType_Id();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.VehicleType#getCapacity <em>Capacity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Capacity</em>'.
	 * @see ovgu.pave.model.input.VehicleType#getCapacity()
	 * @see #getVehicleType()
	 * @generated
	 */
	EAttribute getVehicleType_Capacity();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.input.Vehicle <em>Vehicle</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vehicle</em>'.
	 * @see ovgu.pave.model.input.Vehicle
	 * @generated
	 */
	EClass getVehicle();

	/**
	 * Returns the meta object for the reference '{@link ovgu.pave.model.input.Vehicle#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see ovgu.pave.model.input.Vehicle#getType()
	 * @see #getVehicle()
	 * @generated
	 */
	EReference getVehicle_Type();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.Vehicle#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see ovgu.pave.model.input.Vehicle#getId()
	 * @see #getVehicle()
	 * @generated
	 */
	EAttribute getVehicle_Id();

	/**
	 * Returns the meta object for the reference '{@link ovgu.pave.model.input.Vehicle#getStartLocation <em>Start Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Start Location</em>'.
	 * @see ovgu.pave.model.input.Vehicle#getStartLocation()
	 * @see #getVehicle()
	 * @generated
	 */
	EReference getVehicle_StartLocation();

	/**
	 * Returns the meta object for the reference '{@link ovgu.pave.model.input.Vehicle#getEndLocation <em>End Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>End Location</em>'.
	 * @see ovgu.pave.model.input.Vehicle#getEndLocation()
	 * @see #getVehicle()
	 * @generated
	 */
	EReference getVehicle_EndLocation();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.input.OriginRequestActivity <em>Origin Request Activity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Origin Request Activity</em>'.
	 * @see ovgu.pave.model.input.OriginRequestActivity
	 * @generated
	 */
	EClass getOriginRequestActivity();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.input.RequestActivity <em>Request Activity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Request Activity</em>'.
	 * @see ovgu.pave.model.input.RequestActivity
	 * @generated
	 */
	EClass getRequestActivity();

	/**
	 * Returns the meta object for the reference '{@link ovgu.pave.model.input.RequestActivity#getRequest <em>Request</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Request</em>'.
	 * @see ovgu.pave.model.input.RequestActivity#getRequest()
	 * @see #getRequestActivity()
	 * @generated
	 */
	EReference getRequestActivity_Request();

	/**
	 * Returns the meta object for the reference '{@link ovgu.pave.model.input.RequestActivity#getLocation <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Location</em>'.
	 * @see ovgu.pave.model.input.RequestActivity#getLocation()
	 * @see #getRequestActivity()
	 * @generated
	 */
	EReference getRequestActivity_Location();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.RequestActivity#getServiceDuration <em>Service Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Service Duration</em>'.
	 * @see ovgu.pave.model.input.RequestActivity#getServiceDuration()
	 * @see #getRequestActivity()
	 * @generated
	 */
	EAttribute getRequestActivity_ServiceDuration();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.RequestActivity#getEarliestArrival <em>Earliest Arrival</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Earliest Arrival</em>'.
	 * @see ovgu.pave.model.input.RequestActivity#getEarliestArrival()
	 * @see #getRequestActivity()
	 * @generated
	 */
	EAttribute getRequestActivity_EarliestArrival();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.RequestActivity#getLatestArrival <em>Latest Arrival</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Latest Arrival</em>'.
	 * @see ovgu.pave.model.input.RequestActivity#getLatestArrival()
	 * @see #getRequestActivity()
	 * @generated
	 */
	EAttribute getRequestActivity_LatestArrival();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.RequestActivity#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see ovgu.pave.model.input.RequestActivity#getId()
	 * @see #getRequestActivity()
	 * @generated
	 */
	EAttribute getRequestActivity_Id();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.input.DestinationRequestActivity <em>Destination Request Activity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Destination Request Activity</em>'.
	 * @see ovgu.pave.model.input.DestinationRequestActivity
	 * @generated
	 */
	EClass getDestinationRequestActivity();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.input.Edge <em>Edge</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Edge</em>'.
	 * @see ovgu.pave.model.input.Edge
	 * @generated
	 */
	EClass getEdge();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.Edge#getCost <em>Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Cost</em>'.
	 * @see ovgu.pave.model.input.Edge#getCost()
	 * @see #getEdge()
	 * @generated
	 */
	EAttribute getEdge_Cost();

	/**
	 * Returns the meta object for the reference '{@link ovgu.pave.model.input.Edge#getStartLocation <em>Start Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Start Location</em>'.
	 * @see ovgu.pave.model.input.Edge#getStartLocation()
	 * @see #getEdge()
	 * @generated
	 */
	EReference getEdge_StartLocation();

	/**
	 * Returns the meta object for the reference '{@link ovgu.pave.model.input.Edge#getEndLocation <em>End Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>End Location</em>'.
	 * @see ovgu.pave.model.input.Edge#getEndLocation()
	 * @see #getEdge()
	 * @generated
	 */
	EReference getEdge_EndLocation();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.Edge#getDistance <em>Distance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Distance</em>'.
	 * @see ovgu.pave.model.input.Edge#getDistance()
	 * @see #getEdge()
	 * @generated
	 */
	EAttribute getEdge_Distance();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.Edge#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see ovgu.pave.model.input.Edge#getDuration()
	 * @see #getEdge()
	 * @generated
	 */
	EAttribute getEdge_Duration();

	/**
	 * Returns the meta object for the containment reference list '{@link ovgu.pave.model.input.Edge#getWaypoints <em>Waypoints</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Waypoints</em>'.
	 * @see ovgu.pave.model.input.Edge#getWaypoints()
	 * @see #getEdge()
	 * @generated
	 */
	EReference getEdge_Waypoints();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.Edge#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see ovgu.pave.model.input.Edge#getId()
	 * @see #getEdge()
	 * @generated
	 */
	EAttribute getEdge_Id();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.input.WayPoint <em>Way Point</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Way Point</em>'.
	 * @see ovgu.pave.model.input.WayPoint
	 * @generated
	 */
	EClass getWayPoint();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.WayPoint#getDistance <em>Distance</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Distance</em>'.
	 * @see ovgu.pave.model.input.WayPoint#getDistance()
	 * @see #getWayPoint()
	 * @generated
	 */
	EAttribute getWayPoint_Distance();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.WayPoint#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see ovgu.pave.model.input.WayPoint#getDuration()
	 * @see #getWayPoint()
	 * @generated
	 */
	EAttribute getWayPoint_Duration();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.WayPoint#getLat <em>Lat</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lat</em>'.
	 * @see ovgu.pave.model.input.WayPoint#getLat()
	 * @see #getWayPoint()
	 * @generated
	 */
	EAttribute getWayPoint_Lat();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.WayPoint#getLon <em>Lon</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Lon</em>'.
	 * @see ovgu.pave.model.input.WayPoint#getLon()
	 * @see #getWayPoint()
	 * @generated
	 */
	EAttribute getWayPoint_Lon();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.input.WayPoint#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see ovgu.pave.model.input.WayPoint#getId()
	 * @see #getWayPoint()
	 * @generated
	 */
	EAttribute getWayPoint_Id();

	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.input.Requests <em>Requests</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Requests</em>'.
	 * @see ovgu.pave.model.input.Requests
	 * @generated
	 */
	EClass getRequests();

	/**
	 * Returns the meta object for the containment reference list '{@link ovgu.pave.model.input.Requests#getAccepted <em>Accepted</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Accepted</em>'.
	 * @see ovgu.pave.model.input.Requests#getAccepted()
	 * @see #getRequests()
	 * @generated
	 */
	EReference getRequests_Accepted();

	/**
	 * Returns the meta object for the containment reference list '{@link ovgu.pave.model.input.Requests#getRejected <em>Rejected</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Rejected</em>'.
	 * @see ovgu.pave.model.input.Requests#getRejected()
	 * @see #getRequests()
	 * @generated
	 */
	EReference getRequests_Rejected();

	/**
	 * Returns the meta object for the containment reference list '{@link ovgu.pave.model.input.Requests#getNew <em>New</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>New</em>'.
	 * @see ovgu.pave.model.input.Requests#getNew()
	 * @see #getRequests()
	 * @generated
	 */
	EReference getRequests_New();

	/**
	 * Returns the meta object for data type '{@link ovgu.pave.model.solution.Solution <em>Solution</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Solution</em>'.
	 * @see ovgu.pave.model.solution.Solution
	 * @model instanceClass="ovgu.pave.model.solution.Solution"
	 * @generated
	 */
	EDataType getSolution();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	InputFactory getInputFactory();

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
		 * The meta object literal for the '{@link ovgu.pave.model.input.impl.InputImpl <em>Input</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.impl.InputImpl
		 * @see ovgu.pave.model.input.impl.InputPackageImpl#getInput()
		 * @generated
		 */
		EClass INPUT = eINSTANCE.getInput();

		/**
		 * The meta object literal for the '<em><b>Requests</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT__REQUESTS = eINSTANCE.getInput_Requests();

		/**
		 * The meta object literal for the '<em><b>Vehicles</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT__VEHICLES = eINSTANCE.getInput_Vehicles();

		/**
		 * The meta object literal for the '<em><b>Vehicle Types</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT__VEHICLE_TYPES = eINSTANCE.getInput_VehicleTypes();

		/**
		 * The meta object literal for the '<em><b>Locations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT__LOCATIONS = eINSTANCE.getInput_Locations();

		/**
		 * The meta object literal for the '<em><b>Edges</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INPUT__EDGES = eINSTANCE.getInput_Edges();

		/**
		 * The meta object literal for the '<em><b>Solution</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INPUT__SOLUTION = eINSTANCE.getInput_Solution();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.input.impl.RequestImpl <em>Request</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.impl.RequestImpl
		 * @see ovgu.pave.model.input.impl.InputPackageImpl#getRequest()
		 * @generated
		 */
		EClass REQUEST = eINSTANCE.getRequest();

		/**
		 * The meta object literal for the '<em><b>Origin Activity</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REQUEST__ORIGIN_ACTIVITY = eINSTANCE.getRequest_OriginActivity();

		/**
		 * The meta object literal for the '<em><b>Destination Activity</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REQUEST__DESTINATION_ACTIVITY = eINSTANCE.getRequest_DestinationActivity();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REQUEST__ID = eINSTANCE.getRequest_Id();

		/**
		 * The meta object literal for the '<em><b>Receiving Time</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REQUEST__RECEIVING_TIME = eINSTANCE.getRequest_ReceivingTime();

		/**
		 * The meta object literal for the '<em><b>Quantity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REQUEST__QUANTITY = eINSTANCE.getRequest_Quantity();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.input.impl.LocationImpl <em>Location</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.impl.LocationImpl
		 * @see ovgu.pave.model.input.impl.InputPackageImpl#getLocation()
		 * @generated
		 */
		EClass LOCATION = eINSTANCE.getLocation();

		/**
		 * The meta object literal for the '<em><b>Lat</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATION__LAT = eINSTANCE.getLocation_Lat();

		/**
		 * The meta object literal for the '<em><b>Lon</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATION__LON = eINSTANCE.getLocation_Lon();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LOCATION__ID = eINSTANCE.getLocation_Id();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.input.impl.VehicleTypeImpl <em>Vehicle Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.impl.VehicleTypeImpl
		 * @see ovgu.pave.model.input.impl.InputPackageImpl#getVehicleType()
		 * @generated
		 */
		EClass VEHICLE_TYPE = eINSTANCE.getVehicleType();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VEHICLE_TYPE__ID = eINSTANCE.getVehicleType_Id();

		/**
		 * The meta object literal for the '<em><b>Capacity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VEHICLE_TYPE__CAPACITY = eINSTANCE.getVehicleType_Capacity();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.input.impl.VehicleImpl <em>Vehicle</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.impl.VehicleImpl
		 * @see ovgu.pave.model.input.impl.InputPackageImpl#getVehicle()
		 * @generated
		 */
		EClass VEHICLE = eINSTANCE.getVehicle();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VEHICLE__TYPE = eINSTANCE.getVehicle_Type();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VEHICLE__ID = eINSTANCE.getVehicle_Id();

		/**
		 * The meta object literal for the '<em><b>Start Location</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VEHICLE__START_LOCATION = eINSTANCE.getVehicle_StartLocation();

		/**
		 * The meta object literal for the '<em><b>End Location</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VEHICLE__END_LOCATION = eINSTANCE.getVehicle_EndLocation();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.input.impl.OriginRequestActivityImpl <em>Origin Request Activity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.impl.OriginRequestActivityImpl
		 * @see ovgu.pave.model.input.impl.InputPackageImpl#getOriginRequestActivity()
		 * @generated
		 */
		EClass ORIGIN_REQUEST_ACTIVITY = eINSTANCE.getOriginRequestActivity();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.input.impl.RequestActivityImpl <em>Request Activity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.impl.RequestActivityImpl
		 * @see ovgu.pave.model.input.impl.InputPackageImpl#getRequestActivity()
		 * @generated
		 */
		EClass REQUEST_ACTIVITY = eINSTANCE.getRequestActivity();

		/**
		 * The meta object literal for the '<em><b>Request</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REQUEST_ACTIVITY__REQUEST = eINSTANCE.getRequestActivity_Request();

		/**
		 * The meta object literal for the '<em><b>Location</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REQUEST_ACTIVITY__LOCATION = eINSTANCE.getRequestActivity_Location();

		/**
		 * The meta object literal for the '<em><b>Service Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REQUEST_ACTIVITY__SERVICE_DURATION = eINSTANCE.getRequestActivity_ServiceDuration();

		/**
		 * The meta object literal for the '<em><b>Earliest Arrival</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REQUEST_ACTIVITY__EARLIEST_ARRIVAL = eINSTANCE.getRequestActivity_EarliestArrival();

		/**
		 * The meta object literal for the '<em><b>Latest Arrival</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REQUEST_ACTIVITY__LATEST_ARRIVAL = eINSTANCE.getRequestActivity_LatestArrival();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REQUEST_ACTIVITY__ID = eINSTANCE.getRequestActivity_Id();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.input.impl.DestinationRequestActivityImpl <em>Destination Request Activity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.impl.DestinationRequestActivityImpl
		 * @see ovgu.pave.model.input.impl.InputPackageImpl#getDestinationRequestActivity()
		 * @generated
		 */
		EClass DESTINATION_REQUEST_ACTIVITY = eINSTANCE.getDestinationRequestActivity();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.input.impl.EdgeImpl <em>Edge</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.impl.EdgeImpl
		 * @see ovgu.pave.model.input.impl.InputPackageImpl#getEdge()
		 * @generated
		 */
		EClass EDGE = eINSTANCE.getEdge();

		/**
		 * The meta object literal for the '<em><b>Cost</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EDGE__COST = eINSTANCE.getEdge_Cost();

		/**
		 * The meta object literal for the '<em><b>Start Location</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EDGE__START_LOCATION = eINSTANCE.getEdge_StartLocation();

		/**
		 * The meta object literal for the '<em><b>End Location</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EDGE__END_LOCATION = eINSTANCE.getEdge_EndLocation();

		/**
		 * The meta object literal for the '<em><b>Distance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EDGE__DISTANCE = eINSTANCE.getEdge_Distance();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EDGE__DURATION = eINSTANCE.getEdge_Duration();

		/**
		 * The meta object literal for the '<em><b>Waypoints</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EDGE__WAYPOINTS = eINSTANCE.getEdge_Waypoints();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EDGE__ID = eINSTANCE.getEdge_Id();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.input.impl.WayPointImpl <em>Way Point</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.impl.WayPointImpl
		 * @see ovgu.pave.model.input.impl.InputPackageImpl#getWayPoint()
		 * @generated
		 */
		EClass WAY_POINT = eINSTANCE.getWayPoint();

		/**
		 * The meta object literal for the '<em><b>Distance</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WAY_POINT__DISTANCE = eINSTANCE.getWayPoint_Distance();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WAY_POINT__DURATION = eINSTANCE.getWayPoint_Duration();

		/**
		 * The meta object literal for the '<em><b>Lat</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WAY_POINT__LAT = eINSTANCE.getWayPoint_Lat();

		/**
		 * The meta object literal for the '<em><b>Lon</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WAY_POINT__LON = eINSTANCE.getWayPoint_Lon();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute WAY_POINT__ID = eINSTANCE.getWayPoint_Id();

		/**
		 * The meta object literal for the '{@link ovgu.pave.model.input.impl.RequestsImpl <em>Requests</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.impl.RequestsImpl
		 * @see ovgu.pave.model.input.impl.InputPackageImpl#getRequests()
		 * @generated
		 */
		EClass REQUESTS = eINSTANCE.getRequests();

		/**
		 * The meta object literal for the '<em><b>Accepted</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REQUESTS__ACCEPTED = eINSTANCE.getRequests_Accepted();

		/**
		 * The meta object literal for the '<em><b>Rejected</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REQUESTS__REJECTED = eINSTANCE.getRequests_Rejected();

		/**
		 * The meta object literal for the '<em><b>New</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference REQUESTS__NEW = eINSTANCE.getRequests_New();

		/**
		 * The meta object literal for the '<em>Solution</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.solution.Solution
		 * @see ovgu.pave.model.input.impl.InputPackageImpl#getSolution()
		 * @generated
		 */
		EDataType SOLUTION = eINSTANCE.getSolution();

	}

} //InputPackage
