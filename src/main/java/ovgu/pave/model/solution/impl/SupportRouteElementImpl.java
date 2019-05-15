/**
 */
package ovgu.pave.model.solution.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import ovgu.pave.model.input.Location;

import ovgu.pave.model.solution.SolutionPackage;
import ovgu.pave.model.solution.SupportRouteElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Support Route Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.solution.impl.SupportRouteElementImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.SupportRouteElementImpl#getLocationID <em>Location ID</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SupportRouteElementImpl extends RouteElementImpl implements SupportRouteElement {
	/**
	 * The default value of the '{@link #getLocation() <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocation()
	 * @generated
	 * @ordered
	 */
	protected static final Location LOCATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLocation() <em>Location</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocation()
	 * @generated
	 * @ordered
	 */
	protected Location location = LOCATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getLocationID() <em>Location ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocationID()
	 * @generated
	 * @ordered
	 */
	protected static final int LOCATION_ID_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getLocationID() <em>Location ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocationID()
	 * @generated
	 * @ordered
	 */
	protected int locationID = LOCATION_ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SupportRouteElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SolutionPackage.Literals.SUPPORT_ROUTE_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocation(Location newLocation) {
		Location oldLocation = location;
		location = newLocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.SUPPORT_ROUTE_ELEMENT__LOCATION, oldLocation, location));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getLocationID() {
		return locationID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocationID(int newLocationID) {
		int oldLocationID = locationID;
		locationID = newLocationID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.SUPPORT_ROUTE_ELEMENT__LOCATION_ID, oldLocationID, locationID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SolutionPackage.SUPPORT_ROUTE_ELEMENT__LOCATION:
				return getLocation();
			case SolutionPackage.SUPPORT_ROUTE_ELEMENT__LOCATION_ID:
				return getLocationID();
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
			case SolutionPackage.SUPPORT_ROUTE_ELEMENT__LOCATION:
				setLocation((Location)newValue);
				return;
			case SolutionPackage.SUPPORT_ROUTE_ELEMENT__LOCATION_ID:
				setLocationID((Integer)newValue);
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
			case SolutionPackage.SUPPORT_ROUTE_ELEMENT__LOCATION:
				setLocation(LOCATION_EDEFAULT);
				return;
			case SolutionPackage.SUPPORT_ROUTE_ELEMENT__LOCATION_ID:
				setLocationID(LOCATION_ID_EDEFAULT);
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
			case SolutionPackage.SUPPORT_ROUTE_ELEMENT__LOCATION:
				return LOCATION_EDEFAULT == null ? location != null : !LOCATION_EDEFAULT.equals(location);
			case SolutionPackage.SUPPORT_ROUTE_ELEMENT__LOCATION_ID:
				return locationID != LOCATION_ID_EDEFAULT;
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
		result.append(" (location: ");
		result.append(location);
		result.append(", locationID: ");
		result.append(locationID);
		result.append(')');
		return result.toString();
	}

} //SupportRouteElementImpl
