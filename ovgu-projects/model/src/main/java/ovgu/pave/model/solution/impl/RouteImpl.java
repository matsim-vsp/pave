/**
 */
package ovgu.pave.model.solution.impl;

import java.lang.reflect.InvocationTargetException;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import ovgu.pave.model.input.Vehicle;
import ovgu.pave.model.input.VehicleType;

import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.RouteElement;
import ovgu.pave.model.solution.SolutionPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Route</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.solution.impl.RouteImpl#getRouteElements <em>Route Elements</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.RouteImpl#getVehicle <em>Vehicle</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.RouteImpl#getVehicleType <em>Vehicle Type</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.RouteImpl#getVehicleID <em>Vehicle ID</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.RouteImpl#getVehicleTypeID <em>Vehicle Type ID</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.RouteImpl#getUtilisation <em>Utilisation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RouteImpl extends MinimalEObjectImpl.Container implements Route {
	/**
	 * The cached value of the '{@link #getRouteElements() <em>Route Elements</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteElements()
	 * @generated
	 * @ordered
	 */
	protected EList<RouteElement> routeElements;

	/**
	 * The default value of the '{@link #getVehicle() <em>Vehicle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicle()
	 * @generated
	 * @ordered
	 */
	protected static final Vehicle VEHICLE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVehicle() <em>Vehicle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicle()
	 * @generated
	 * @ordered
	 */
	protected Vehicle vehicle = VEHICLE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVehicleType() <em>Vehicle Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicleType()
	 * @generated
	 * @ordered
	 */
	protected static final VehicleType VEHICLE_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVehicleType() <em>Vehicle Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicleType()
	 * @generated
	 * @ordered
	 */
	protected VehicleType vehicleType = VEHICLE_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVehicleID() <em>Vehicle ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicleID()
	 * @generated
	 * @ordered
	 */
	protected static final int VEHICLE_ID_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getVehicleID() <em>Vehicle ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicleID()
	 * @generated
	 * @ordered
	 */
	protected int vehicleID = VEHICLE_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getVehicleTypeID() <em>Vehicle Type ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicleTypeID()
	 * @generated
	 * @ordered
	 */
	protected static final int VEHICLE_TYPE_ID_EDEFAULT = -1;

	/**
	 * The cached value of the '{@link #getVehicleTypeID() <em>Vehicle Type ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicleTypeID()
	 * @generated
	 * @ordered
	 */
	protected int vehicleTypeID = VEHICLE_TYPE_ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getUtilisation() <em>Utilisation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUtilisation()
	 * @generated
	 * @ordered
	 */
	protected static final long UTILISATION_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getUtilisation() <em>Utilisation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUtilisation()
	 * @generated
	 * @ordered
	 */
	protected long utilisation = UTILISATION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RouteImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SolutionPackage.Literals.ROUTE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<RouteElement> getRouteElements() {
		if (routeElements == null) {
			routeElements = new EObjectContainmentEList<RouteElement>(RouteElement.class, this, SolutionPackage.ROUTE__ROUTE_ELEMENTS);
		}
		return routeElements;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Vehicle getVehicle() {
		return vehicle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVehicle(Vehicle newVehicle) {
		Vehicle oldVehicle = vehicle;
		vehicle = newVehicle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.ROUTE__VEHICLE, oldVehicle, vehicle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VehicleType getVehicleType() {
		return vehicleType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVehicleType(VehicleType newVehicleType) {
		VehicleType oldVehicleType = vehicleType;
		vehicleType = newVehicleType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.ROUTE__VEHICLE_TYPE, oldVehicleType, vehicleType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getVehicleID() {
		return vehicleID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVehicleID(int newVehicleID) {
		int oldVehicleID = vehicleID;
		vehicleID = newVehicleID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.ROUTE__VEHICLE_ID, oldVehicleID, vehicleID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getVehicleTypeID() {
		return vehicleTypeID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVehicleTypeID(int newVehicleTypeID) {
		int oldVehicleTypeID = vehicleTypeID;
		vehicleTypeID = newVehicleTypeID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.ROUTE__VEHICLE_TYPE_ID, oldVehicleTypeID, vehicleTypeID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getUtilisation() {
		return utilisation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUtilisation(long newUtilisation) {
		long oldUtilisation = utilisation;
		utilisation = newUtilisation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.ROUTE__UTILISATION, oldUtilisation, utilisation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RouteElement getRouteElement(final int index) {
		return routeElements.get(index);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SolutionPackage.ROUTE__ROUTE_ELEMENTS:
				return ((InternalEList<?>)getRouteElements()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SolutionPackage.ROUTE__ROUTE_ELEMENTS:
				return getRouteElements();
			case SolutionPackage.ROUTE__VEHICLE:
				return getVehicle();
			case SolutionPackage.ROUTE__VEHICLE_TYPE:
				return getVehicleType();
			case SolutionPackage.ROUTE__VEHICLE_ID:
				return getVehicleID();
			case SolutionPackage.ROUTE__VEHICLE_TYPE_ID:
				return getVehicleTypeID();
			case SolutionPackage.ROUTE__UTILISATION:
				return getUtilisation();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SolutionPackage.ROUTE__ROUTE_ELEMENTS:
				getRouteElements().clear();
				getRouteElements().addAll((Collection<? extends RouteElement>)newValue);
				return;
			case SolutionPackage.ROUTE__VEHICLE:
				setVehicle((Vehicle)newValue);
				return;
			case SolutionPackage.ROUTE__VEHICLE_TYPE:
				setVehicleType((VehicleType)newValue);
				return;
			case SolutionPackage.ROUTE__VEHICLE_ID:
				setVehicleID((Integer)newValue);
				return;
			case SolutionPackage.ROUTE__VEHICLE_TYPE_ID:
				setVehicleTypeID((Integer)newValue);
				return;
			case SolutionPackage.ROUTE__UTILISATION:
				setUtilisation((Long)newValue);
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
			case SolutionPackage.ROUTE__ROUTE_ELEMENTS:
				getRouteElements().clear();
				return;
			case SolutionPackage.ROUTE__VEHICLE:
				setVehicle(VEHICLE_EDEFAULT);
				return;
			case SolutionPackage.ROUTE__VEHICLE_TYPE:
				setVehicleType(VEHICLE_TYPE_EDEFAULT);
				return;
			case SolutionPackage.ROUTE__VEHICLE_ID:
				setVehicleID(VEHICLE_ID_EDEFAULT);
				return;
			case SolutionPackage.ROUTE__VEHICLE_TYPE_ID:
				setVehicleTypeID(VEHICLE_TYPE_ID_EDEFAULT);
				return;
			case SolutionPackage.ROUTE__UTILISATION:
				setUtilisation(UTILISATION_EDEFAULT);
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
			case SolutionPackage.ROUTE__ROUTE_ELEMENTS:
				return routeElements != null && !routeElements.isEmpty();
			case SolutionPackage.ROUTE__VEHICLE:
				return VEHICLE_EDEFAULT == null ? vehicle != null : !VEHICLE_EDEFAULT.equals(vehicle);
			case SolutionPackage.ROUTE__VEHICLE_TYPE:
				return VEHICLE_TYPE_EDEFAULT == null ? vehicleType != null : !VEHICLE_TYPE_EDEFAULT.equals(vehicleType);
			case SolutionPackage.ROUTE__VEHICLE_ID:
				return vehicleID != VEHICLE_ID_EDEFAULT;
			case SolutionPackage.ROUTE__VEHICLE_TYPE_ID:
				return vehicleTypeID != VEHICLE_TYPE_ID_EDEFAULT;
			case SolutionPackage.ROUTE__UTILISATION:
				return utilisation != UTILISATION_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SolutionPackage.ROUTE___GET_ROUTE_ELEMENT__INT:
				return getRouteElement((Integer)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (vehicle: ");
		result.append(vehicle);
		result.append(", vehicleType: ");
		result.append(vehicleType);
		result.append(", vehicleID: ");
		result.append(vehicleID);
		result.append(", vehicleTypeID: ");
		result.append(vehicleTypeID);
		result.append(", utilisation: ");
		result.append(utilisation);
		result.append(')');
		return result.toString();
	}

} //RouteImpl
