/**
 */
package ovgu.pave.model.algorithmObjects.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage;
import ovgu.pave.model.algorithmObjects.RemovalOption;

import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.RouteElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Removal Option</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.RemovalOptionImpl#getCosts <em>Costs</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.RemovalOptionImpl#getRoute <em>Route</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.RemovalOptionImpl#getOriginRouteElement <em>Origin Route Element</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.RemovalOptionImpl#getDestinationRouteElement <em>Destination Route Element</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.RemovalOptionImpl#getDistance <em>Distance</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.RemovalOptionImpl#getAbsServiceBegin <em>Abs Service Begin</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RemovalOptionImpl extends MinimalEObjectImpl.Container implements RemovalOption {
	/**
	 * The default value of the '{@link #getCosts() <em>Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCosts()
	 * @generated
	 * @ordered
	 */
	protected static final long COSTS_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getCosts() <em>Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCosts()
	 * @generated
	 * @ordered
	 */
	protected long costs = COSTS_EDEFAULT;

	/**
	 * The default value of the '{@link #getRoute() <em>Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoute()
	 * @generated
	 * @ordered
	 */
	protected static final Route ROUTE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRoute() <em>Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoute()
	 * @generated
	 * @ordered
	 */
	protected Route route = ROUTE_EDEFAULT;

	/**
	 * The default value of the '{@link #getOriginRouteElement() <em>Origin Route Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginRouteElement()
	 * @generated
	 * @ordered
	 */
	protected static final RouteElement ORIGIN_ROUTE_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOriginRouteElement() <em>Origin Route Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginRouteElement()
	 * @generated
	 * @ordered
	 */
	protected RouteElement originRouteElement = ORIGIN_ROUTE_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getDestinationRouteElement() <em>Destination Route Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestinationRouteElement()
	 * @generated
	 * @ordered
	 */
	protected static final RouteElement DESTINATION_ROUTE_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDestinationRouteElement() <em>Destination Route Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestinationRouteElement()
	 * @generated
	 * @ordered
	 */
	protected RouteElement destinationRouteElement = DESTINATION_ROUTE_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getDistance() <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistance()
	 * @generated
	 * @ordered
	 */
	protected static final long DISTANCE_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getDistance() <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistance()
	 * @generated
	 * @ordered
	 */
	protected long distance = DISTANCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getAbsServiceBegin() <em>Abs Service Begin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAbsServiceBegin()
	 * @generated
	 * @ordered
	 */
	protected static final long ABS_SERVICE_BEGIN_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getAbsServiceBegin() <em>Abs Service Begin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAbsServiceBegin()
	 * @generated
	 * @ordered
	 */
	protected long absServiceBegin = ABS_SERVICE_BEGIN_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RemovalOptionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AlgorithmObjectsPackage.Literals.REMOVAL_OPTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getCosts() {
		return costs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCosts(long newCosts) {
		long oldCosts = costs;
		costs = newCosts;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.REMOVAL_OPTION__COSTS, oldCosts, costs));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Route getRoute() {
		return route;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRoute(Route newRoute) {
		Route oldRoute = route;
		route = newRoute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.REMOVAL_OPTION__ROUTE, oldRoute, route));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RouteElement getOriginRouteElement() {
		return originRouteElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginRouteElement(RouteElement newOriginRouteElement) {
		RouteElement oldOriginRouteElement = originRouteElement;
		originRouteElement = newOriginRouteElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.REMOVAL_OPTION__ORIGIN_ROUTE_ELEMENT, oldOriginRouteElement, originRouteElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RouteElement getDestinationRouteElement() {
		return destinationRouteElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDestinationRouteElement(RouteElement newDestinationRouteElement) {
		RouteElement oldDestinationRouteElement = destinationRouteElement;
		destinationRouteElement = newDestinationRouteElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.REMOVAL_OPTION__DESTINATION_ROUTE_ELEMENT, oldDestinationRouteElement, destinationRouteElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getDistance() {
		return distance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDistance(long newDistance) {
		long oldDistance = distance;
		distance = newDistance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.REMOVAL_OPTION__DISTANCE, oldDistance, distance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getAbsServiceBegin() {
		return absServiceBegin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAbsServiceBegin(long newAbsServiceBegin) {
		long oldAbsServiceBegin = absServiceBegin;
		absServiceBegin = newAbsServiceBegin;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.REMOVAL_OPTION__ABS_SERVICE_BEGIN, oldAbsServiceBegin, absServiceBegin));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AlgorithmObjectsPackage.REMOVAL_OPTION__COSTS:
				return getCosts();
			case AlgorithmObjectsPackage.REMOVAL_OPTION__ROUTE:
				return getRoute();
			case AlgorithmObjectsPackage.REMOVAL_OPTION__ORIGIN_ROUTE_ELEMENT:
				return getOriginRouteElement();
			case AlgorithmObjectsPackage.REMOVAL_OPTION__DESTINATION_ROUTE_ELEMENT:
				return getDestinationRouteElement();
			case AlgorithmObjectsPackage.REMOVAL_OPTION__DISTANCE:
				return getDistance();
			case AlgorithmObjectsPackage.REMOVAL_OPTION__ABS_SERVICE_BEGIN:
				return getAbsServiceBegin();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case AlgorithmObjectsPackage.REMOVAL_OPTION__COSTS:
				setCosts((Long)newValue);
				return;
			case AlgorithmObjectsPackage.REMOVAL_OPTION__ROUTE:
				setRoute((Route)newValue);
				return;
			case AlgorithmObjectsPackage.REMOVAL_OPTION__ORIGIN_ROUTE_ELEMENT:
				setOriginRouteElement((RouteElement)newValue);
				return;
			case AlgorithmObjectsPackage.REMOVAL_OPTION__DESTINATION_ROUTE_ELEMENT:
				setDestinationRouteElement((RouteElement)newValue);
				return;
			case AlgorithmObjectsPackage.REMOVAL_OPTION__DISTANCE:
				setDistance((Long)newValue);
				return;
			case AlgorithmObjectsPackage.REMOVAL_OPTION__ABS_SERVICE_BEGIN:
				setAbsServiceBegin((Long)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case AlgorithmObjectsPackage.REMOVAL_OPTION__COSTS:
				setCosts(COSTS_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.REMOVAL_OPTION__ROUTE:
				setRoute(ROUTE_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.REMOVAL_OPTION__ORIGIN_ROUTE_ELEMENT:
				setOriginRouteElement(ORIGIN_ROUTE_ELEMENT_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.REMOVAL_OPTION__DESTINATION_ROUTE_ELEMENT:
				setDestinationRouteElement(DESTINATION_ROUTE_ELEMENT_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.REMOVAL_OPTION__DISTANCE:
				setDistance(DISTANCE_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.REMOVAL_OPTION__ABS_SERVICE_BEGIN:
				setAbsServiceBegin(ABS_SERVICE_BEGIN_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case AlgorithmObjectsPackage.REMOVAL_OPTION__COSTS:
				return costs != COSTS_EDEFAULT;
			case AlgorithmObjectsPackage.REMOVAL_OPTION__ROUTE:
				return ROUTE_EDEFAULT == null ? route != null : !ROUTE_EDEFAULT.equals(route);
			case AlgorithmObjectsPackage.REMOVAL_OPTION__ORIGIN_ROUTE_ELEMENT:
				return ORIGIN_ROUTE_ELEMENT_EDEFAULT == null ? originRouteElement != null : !ORIGIN_ROUTE_ELEMENT_EDEFAULT.equals(originRouteElement);
			case AlgorithmObjectsPackage.REMOVAL_OPTION__DESTINATION_ROUTE_ELEMENT:
				return DESTINATION_ROUTE_ELEMENT_EDEFAULT == null ? destinationRouteElement != null : !DESTINATION_ROUTE_ELEMENT_EDEFAULT.equals(destinationRouteElement);
			case AlgorithmObjectsPackage.REMOVAL_OPTION__DISTANCE:
				return distance != DISTANCE_EDEFAULT;
			case AlgorithmObjectsPackage.REMOVAL_OPTION__ABS_SERVICE_BEGIN:
				return absServiceBegin != ABS_SERVICE_BEGIN_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (costs: ");
		result.append(costs);
		result.append(", route: ");
		result.append(route);
		result.append(", originRouteElement: ");
		result.append(originRouteElement);
		result.append(", destinationRouteElement: ");
		result.append(destinationRouteElement);
		result.append(", distance: ");
		result.append(distance);
		result.append(", absServiceBegin: ");
		result.append(absServiceBegin);
		result.append(')');
		return result.toString();
	}

} //RemovalOptionImpl
