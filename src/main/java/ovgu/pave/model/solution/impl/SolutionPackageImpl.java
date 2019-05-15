/**
 */
package ovgu.pave.model.solution.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import ovgu.pave.model.input.Location;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.input.RequestActivity;
import ovgu.pave.model.input.Vehicle;
import ovgu.pave.model.input.VehicleType;
import ovgu.pave.model.input.WayPoint;

import ovgu.pave.model.solution.RequestActivityRouteElement;
import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.RouteElement;
import ovgu.pave.model.solution.Solution;
import ovgu.pave.model.solution.SolutionFactory;
import ovgu.pave.model.solution.SolutionPackage;
import ovgu.pave.model.solution.SupportRouteElement;
import ovgu.pave.model.solution.UseVehicleType;
import ovgu.pave.model.solution.WayPointRouteElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SolutionPackageImpl extends EPackageImpl implements SolutionPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass routeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass routeElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass solutionEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass supportRouteElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass requestActivityRouteElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass wayPointRouteElementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass useVehicleTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType vehicleEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType requestActivityEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType requestsEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType locationEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType vehicleTypeEDataType = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EDataType wayPointEDataType = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see ovgu.pave.model.solution.SolutionPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private SolutionPackageImpl() {
		super(eNS_URI, SolutionFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link SolutionPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static SolutionPackage init() {
		if (isInited) return (SolutionPackage)EPackage.Registry.INSTANCE.getEPackage(SolutionPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredSolutionPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		SolutionPackageImpl theSolutionPackage = registeredSolutionPackage instanceof SolutionPackageImpl ? (SolutionPackageImpl)registeredSolutionPackage : new SolutionPackageImpl();

		isInited = true;

		// Create package meta-data objects
		theSolutionPackage.createPackageContents();

		// Initialize created meta-data
		theSolutionPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theSolutionPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(SolutionPackage.eNS_URI, theSolutionPackage);
		return theSolutionPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRoute() {
		return routeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getRoute_RouteElements() {
		return (EReference)routeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRoute_Vehicle() {
		return (EAttribute)routeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRoute_VehicleType() {
		return (EAttribute)routeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRoute_VehicleID() {
		return (EAttribute)routeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRoute_VehicleTypeID() {
		return (EAttribute)routeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRoute_Utilisation() {
		return (EAttribute)routeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getRoute__GetRouteElement__int() {
		return routeEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRouteElement() {
		return routeElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRouteElement_ServiceBegin() {
		return (EAttribute)routeElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRouteElement_ServiceDuration() {
		return (EAttribute)routeElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSolution() {
		return solutionEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSolution_Routes() {
		return (EReference)solutionEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSolution_Score() {
		return (EAttribute)solutionEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getSolution_UseVehicleType() {
		return (EReference)solutionEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSolution_UnintegratedRequests() {
		return (EAttribute)solutionEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSolution_IntegratedRequests() {
		return (EAttribute)solutionEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getSupportRouteElement() {
		return supportRouteElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSupportRouteElement_Location() {
		return (EAttribute)supportRouteElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getSupportRouteElement_LocationID() {
		return (EAttribute)supportRouteElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getRequestActivityRouteElement() {
		return requestActivityRouteElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRequestActivityRouteElement_RequestActivity() {
		return (EAttribute)requestActivityRouteElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getRequestActivityRouteElement_RequestActivityID() {
		return (EAttribute)requestActivityRouteElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getWayPointRouteElement() {
		return wayPointRouteElementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWayPointRouteElement_WayPoint() {
		return (EAttribute)wayPointRouteElementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getWayPointRouteElement_WayPointID() {
		return (EAttribute)wayPointRouteElementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUseVehicleType() {
		return useVehicleTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUseVehicleType_UseVehicleTypeInsteadVehicle() {
		return (EAttribute)useVehicleTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUseVehicleType_VehicleTypes() {
		return (EAttribute)useVehicleTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUseVehicleType_VehicleTypeIDs() {
		return (EAttribute)useVehicleTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUseVehicleType_Quantities() {
		return (EAttribute)useVehicleTypeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getVehicle() {
		return vehicleEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getRequestActivity() {
		return requestActivityEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getRequests() {
		return requestsEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getLocation() {
		return locationEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getVehicleType() {
		return vehicleTypeEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDataType getWayPoint() {
		return wayPointEDataType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SolutionFactory getSolutionFactory() {
		return (SolutionFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		routeEClass = createEClass(ROUTE);
		createEReference(routeEClass, ROUTE__ROUTE_ELEMENTS);
		createEAttribute(routeEClass, ROUTE__VEHICLE);
		createEAttribute(routeEClass, ROUTE__VEHICLE_TYPE);
		createEAttribute(routeEClass, ROUTE__VEHICLE_ID);
		createEAttribute(routeEClass, ROUTE__VEHICLE_TYPE_ID);
		createEAttribute(routeEClass, ROUTE__UTILISATION);
		createEOperation(routeEClass, ROUTE___GET_ROUTE_ELEMENT__INT);

		routeElementEClass = createEClass(ROUTE_ELEMENT);
		createEAttribute(routeElementEClass, ROUTE_ELEMENT__SERVICE_BEGIN);
		createEAttribute(routeElementEClass, ROUTE_ELEMENT__SERVICE_DURATION);

		solutionEClass = createEClass(SOLUTION);
		createEReference(solutionEClass, SOLUTION__ROUTES);
		createEAttribute(solutionEClass, SOLUTION__SCORE);
		createEReference(solutionEClass, SOLUTION__USE_VEHICLE_TYPE);
		createEAttribute(solutionEClass, SOLUTION__UNINTEGRATED_REQUESTS);
		createEAttribute(solutionEClass, SOLUTION__INTEGRATED_REQUESTS);

		supportRouteElementEClass = createEClass(SUPPORT_ROUTE_ELEMENT);
		createEAttribute(supportRouteElementEClass, SUPPORT_ROUTE_ELEMENT__LOCATION);
		createEAttribute(supportRouteElementEClass, SUPPORT_ROUTE_ELEMENT__LOCATION_ID);

		requestActivityRouteElementEClass = createEClass(REQUEST_ACTIVITY_ROUTE_ELEMENT);
		createEAttribute(requestActivityRouteElementEClass, REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY);
		createEAttribute(requestActivityRouteElementEClass, REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY_ID);

		wayPointRouteElementEClass = createEClass(WAY_POINT_ROUTE_ELEMENT);
		createEAttribute(wayPointRouteElementEClass, WAY_POINT_ROUTE_ELEMENT__WAY_POINT);
		createEAttribute(wayPointRouteElementEClass, WAY_POINT_ROUTE_ELEMENT__WAY_POINT_ID);

		useVehicleTypeEClass = createEClass(USE_VEHICLE_TYPE);
		createEAttribute(useVehicleTypeEClass, USE_VEHICLE_TYPE__USE_VEHICLE_TYPE_INSTEAD_VEHICLE);
		createEAttribute(useVehicleTypeEClass, USE_VEHICLE_TYPE__VEHICLE_TYPES);
		createEAttribute(useVehicleTypeEClass, USE_VEHICLE_TYPE__VEHICLE_TYPE_IDS);
		createEAttribute(useVehicleTypeEClass, USE_VEHICLE_TYPE__QUANTITIES);

		// Create data types
		vehicleEDataType = createEDataType(VEHICLE);
		requestActivityEDataType = createEDataType(REQUEST_ACTIVITY);
		requestsEDataType = createEDataType(REQUESTS);
		locationEDataType = createEDataType(LOCATION);
		vehicleTypeEDataType = createEDataType(VEHICLE_TYPE);
		wayPointEDataType = createEDataType(WAY_POINT);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		supportRouteElementEClass.getESuperTypes().add(this.getRouteElement());
		requestActivityRouteElementEClass.getESuperTypes().add(this.getRouteElement());
		wayPointRouteElementEClass.getESuperTypes().add(this.getRouteElement());

		// Initialize classes, features, and operations; add parameters
		initEClass(routeEClass, Route.class, "Route", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRoute_RouteElements(), this.getRouteElement(), null, "routeElements", null, 0, -1, Route.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRoute_Vehicle(), this.getVehicle(), "vehicle", null, 0, 1, Route.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRoute_VehicleType(), this.getVehicleType(), "vehicleType", null, 0, 1, Route.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRoute_VehicleID(), ecorePackage.getEInt(), "vehicleID", "-1", 0, 1, Route.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRoute_VehicleTypeID(), ecorePackage.getEInt(), "vehicleTypeID", "-1", 0, 1, Route.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRoute_Utilisation(), ecorePackage.getELong(), "utilisation", "0", 0, 1, Route.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = initEOperation(getRoute__GetRouteElement__int(), this.getRouteElement(), "getRouteElement", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEInt(), "index", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(routeElementEClass, RouteElement.class, "RouteElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRouteElement_ServiceBegin(), ecorePackage.getELong(), "serviceBegin", null, 0, 1, RouteElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRouteElement_ServiceDuration(), ecorePackage.getELong(), "serviceDuration", null, 0, 1, RouteElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(solutionEClass, Solution.class, "Solution", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getSolution_Routes(), this.getRoute(), null, "routes", null, 0, -1, Solution.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSolution_Score(), ecorePackage.getELong(), "Score", null, 0, 1, Solution.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getSolution_UseVehicleType(), this.getUseVehicleType(), null, "useVehicleType", null, 0, 1, Solution.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSolution_UnintegratedRequests(), this.getRequests(), "unintegratedRequests", null, 0, -1, Solution.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSolution_IntegratedRequests(), this.getRequests(), "integratedRequests", null, 0, -1, Solution.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(supportRouteElementEClass, SupportRouteElement.class, "SupportRouteElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSupportRouteElement_Location(), this.getLocation(), "location", null, 1, 1, SupportRouteElement.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSupportRouteElement_LocationID(), ecorePackage.getEInt(), "locationID", "0", 0, 1, SupportRouteElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(requestActivityRouteElementEClass, RequestActivityRouteElement.class, "RequestActivityRouteElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getRequestActivityRouteElement_RequestActivity(), this.getRequestActivity(), "requestActivity", null, 1, 1, RequestActivityRouteElement.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRequestActivityRouteElement_RequestActivityID(), ecorePackage.getEString(), "requestActivityID", "", 0, 1, RequestActivityRouteElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(wayPointRouteElementEClass, WayPointRouteElement.class, "WayPointRouteElement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getWayPointRouteElement_WayPoint(), this.getWayPoint(), "wayPoint", null, 1, 1, WayPointRouteElement.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getWayPointRouteElement_WayPointID(), ecorePackage.getEString(), "wayPointID", "", 0, 1, WayPointRouteElement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(useVehicleTypeEClass, UseVehicleType.class, "UseVehicleType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUseVehicleType_UseVehicleTypeInsteadVehicle(), ecorePackage.getEBoolean(), "useVehicleTypeInsteadVehicle", "false", 0, 1, UseVehicleType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUseVehicleType_VehicleTypes(), this.getVehicleType(), "VehicleTypes", null, 0, -1, UseVehicleType.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUseVehicleType_VehicleTypeIDs(), ecorePackage.getEInt(), "vehicleTypeIDs", null, 0, -1, UseVehicleType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUseVehicleType_Quantities(), ecorePackage.getEInt(), "quantities", null, 0, -1, UseVehicleType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, !IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize data types
		initEDataType(vehicleEDataType, Vehicle.class, "Vehicle", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(requestActivityEDataType, RequestActivity.class, "RequestActivity", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(requestsEDataType, Request.class, "Requests", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(locationEDataType, Location.class, "Location", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(vehicleTypeEDataType, VehicleType.class, "VehicleType", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);
		initEDataType(wayPointEDataType, WayPoint.class, "WayPoint", IS_SERIALIZABLE, !IS_GENERATED_INSTANCE_CLASS);

		// Create resource
		createResource(eNS_URI);
	}

} //SolutionPackageImpl
