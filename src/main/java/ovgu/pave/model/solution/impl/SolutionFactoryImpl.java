/**
 */
package ovgu.pave.model.solution.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import ovgu.pave.model.input.Location;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.input.RequestActivity;
import ovgu.pave.model.input.Vehicle;
import ovgu.pave.model.input.VehicleType;
import ovgu.pave.model.input.WayPoint;

import ovgu.pave.model.solution.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SolutionFactoryImpl extends EFactoryImpl implements SolutionFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SolutionFactory init() {
		try {
			SolutionFactory theSolutionFactory = (SolutionFactory)EPackage.Registry.INSTANCE.getEFactory(SolutionPackage.eNS_URI);
			if (theSolutionFactory != null) {
				return theSolutionFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new SolutionFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SolutionFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case SolutionPackage.ROUTE: return createRoute();
			case SolutionPackage.ROUTE_ELEMENT: return createRouteElement();
			case SolutionPackage.SOLUTION: return createSolution();
			case SolutionPackage.SUPPORT_ROUTE_ELEMENT: return createSupportRouteElement();
			case SolutionPackage.REQUEST_ACTIVITY_ROUTE_ELEMENT: return createRequestActivityRouteElement();
			case SolutionPackage.WAY_POINT_ROUTE_ELEMENT: return createWayPointRouteElement();
			case SolutionPackage.USE_VEHICLE_TYPE: return createUseVehicleType();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case SolutionPackage.VEHICLE:
				return createVehicleFromString(eDataType, initialValue);
			case SolutionPackage.REQUEST_ACTIVITY:
				return createRequestActivityFromString(eDataType, initialValue);
			case SolutionPackage.REQUESTS:
				return createRequestsFromString(eDataType, initialValue);
			case SolutionPackage.LOCATION:
				return createLocationFromString(eDataType, initialValue);
			case SolutionPackage.VEHICLE_TYPE:
				return createVehicleTypeFromString(eDataType, initialValue);
			case SolutionPackage.WAY_POINT:
				return createWayPointFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case SolutionPackage.VEHICLE:
				return convertVehicleToString(eDataType, instanceValue);
			case SolutionPackage.REQUEST_ACTIVITY:
				return convertRequestActivityToString(eDataType, instanceValue);
			case SolutionPackage.REQUESTS:
				return convertRequestsToString(eDataType, instanceValue);
			case SolutionPackage.LOCATION:
				return convertLocationToString(eDataType, instanceValue);
			case SolutionPackage.VEHICLE_TYPE:
				return convertVehicleTypeToString(eDataType, instanceValue);
			case SolutionPackage.WAY_POINT:
				return convertWayPointToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Route createRoute() {
		RouteImpl route = new RouteImpl();
		return route;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RouteElement createRouteElement() {
		RouteElementImpl routeElement = new RouteElementImpl();
		return routeElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Solution createSolution() {
		SolutionImpl solution = new SolutionImpl();
		return solution;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SupportRouteElement createSupportRouteElement() {
		SupportRouteElementImpl supportRouteElement = new SupportRouteElementImpl();
		return supportRouteElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RequestActivityRouteElement createRequestActivityRouteElement() {
		RequestActivityRouteElementImpl requestActivityRouteElement = new RequestActivityRouteElementImpl();
		return requestActivityRouteElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WayPointRouteElement createWayPointRouteElement() {
		WayPointRouteElementImpl wayPointRouteElement = new WayPointRouteElementImpl();
		return wayPointRouteElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UseVehicleType createUseVehicleType() {
		UseVehicleTypeImpl useVehicleType = new UseVehicleTypeImpl();
		return useVehicleType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vehicle createVehicleFromString(EDataType eDataType, String initialValue) {
		return (Vehicle)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertVehicleToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RequestActivity createRequestActivityFromString(EDataType eDataType, String initialValue) {
		return (RequestActivity)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRequestActivityToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Request createRequestsFromString(EDataType eDataType, String initialValue) {
		return (Request)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRequestsToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Location createLocationFromString(EDataType eDataType, String initialValue) {
		return (Location)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLocationToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VehicleType createVehicleTypeFromString(EDataType eDataType, String initialValue) {
		return (VehicleType)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertVehicleTypeToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WayPoint createWayPointFromString(EDataType eDataType, String initialValue) {
		return (WayPoint)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertWayPointToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SolutionPackage getSolutionPackage() {
		return (SolutionPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SolutionPackage getPackage() {
		return SolutionPackage.eINSTANCE;
	}

} //SolutionFactoryImpl
