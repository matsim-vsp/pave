/**
 */
package ovgu.pave.model.algorithmObjects.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage;
import ovgu.pave.model.algorithmObjects.InsertionOption;

import ovgu.pave.model.solution.Route;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Insertion Option</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.InsertionOptionImpl#getCosts <em>Costs</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.InsertionOptionImpl#getOriginIndex <em>Origin Index</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.InsertionOptionImpl#getDestinationIndex <em>Destination Index</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.InsertionOptionImpl#getRoute <em>Route</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InsertionOptionImpl extends MinimalEObjectImpl.Container implements InsertionOption {
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
	 * The default value of the '{@link #getOriginIndex() <em>Origin Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int ORIGIN_INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getOriginIndex() <em>Origin Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginIndex()
	 * @generated
	 * @ordered
	 */
	protected int originIndex = ORIGIN_INDEX_EDEFAULT;

	/**
	 * The default value of the '{@link #getDestinationIndex() <em>Destination Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestinationIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int DESTINATION_INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDestinationIndex() <em>Destination Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestinationIndex()
	 * @generated
	 * @ordered
	 */
	protected int destinationIndex = DESTINATION_INDEX_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InsertionOptionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AlgorithmObjectsPackage.Literals.INSERTION_OPTION;
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
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.INSERTION_OPTION__COSTS, oldCosts, costs));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getOriginIndex() {
		return originIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginIndex(int newOriginIndex) {
		int oldOriginIndex = originIndex;
		originIndex = newOriginIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.INSERTION_OPTION__ORIGIN_INDEX, oldOriginIndex, originIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDestinationIndex() {
		return destinationIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDestinationIndex(int newDestinationIndex) {
		int oldDestinationIndex = destinationIndex;
		destinationIndex = newDestinationIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.INSERTION_OPTION__DESTINATION_INDEX, oldDestinationIndex, destinationIndex));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.INSERTION_OPTION__ROUTE, oldRoute, route));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AlgorithmObjectsPackage.INSERTION_OPTION__COSTS:
				return getCosts();
			case AlgorithmObjectsPackage.INSERTION_OPTION__ORIGIN_INDEX:
				return getOriginIndex();
			case AlgorithmObjectsPackage.INSERTION_OPTION__DESTINATION_INDEX:
				return getDestinationIndex();
			case AlgorithmObjectsPackage.INSERTION_OPTION__ROUTE:
				return getRoute();
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
			case AlgorithmObjectsPackage.INSERTION_OPTION__COSTS:
				setCosts((Long)newValue);
				return;
			case AlgorithmObjectsPackage.INSERTION_OPTION__ORIGIN_INDEX:
				setOriginIndex((Integer)newValue);
				return;
			case AlgorithmObjectsPackage.INSERTION_OPTION__DESTINATION_INDEX:
				setDestinationIndex((Integer)newValue);
				return;
			case AlgorithmObjectsPackage.INSERTION_OPTION__ROUTE:
				setRoute((Route)newValue);
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
			case AlgorithmObjectsPackage.INSERTION_OPTION__COSTS:
				setCosts(COSTS_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.INSERTION_OPTION__ORIGIN_INDEX:
				setOriginIndex(ORIGIN_INDEX_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.INSERTION_OPTION__DESTINATION_INDEX:
				setDestinationIndex(DESTINATION_INDEX_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.INSERTION_OPTION__ROUTE:
				setRoute(ROUTE_EDEFAULT);
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
			case AlgorithmObjectsPackage.INSERTION_OPTION__COSTS:
				return costs != COSTS_EDEFAULT;
			case AlgorithmObjectsPackage.INSERTION_OPTION__ORIGIN_INDEX:
				return originIndex != ORIGIN_INDEX_EDEFAULT;
			case AlgorithmObjectsPackage.INSERTION_OPTION__DESTINATION_INDEX:
				return destinationIndex != DESTINATION_INDEX_EDEFAULT;
			case AlgorithmObjectsPackage.INSERTION_OPTION__ROUTE:
				return ROUTE_EDEFAULT == null ? route != null : !ROUTE_EDEFAULT.equals(route);
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
		result.append(", originIndex: ");
		result.append(originIndex);
		result.append(", destinationIndex: ");
		result.append(destinationIndex);
		result.append(", route: ");
		result.append(route);
		result.append(')');
		return result.toString();
	}

} //InsertionOptionImpl
