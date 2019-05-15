/**
 */
package ovgu.pave.model.solution.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import ovgu.pave.model.input.WayPoint;

import ovgu.pave.model.solution.SolutionPackage;
import ovgu.pave.model.solution.WayPointRouteElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Way Point Route Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.solution.impl.WayPointRouteElementImpl#getWayPoint <em>Way Point</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.WayPointRouteElementImpl#getWayPointID <em>Way Point ID</em>}</li>
 * </ul>
 *
 * @generated
 */
public class WayPointRouteElementImpl extends RouteElementImpl implements WayPointRouteElement {
	/**
	 * The default value of the '{@link #getWayPoint() <em>Way Point</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWayPoint()
	 * @generated
	 * @ordered
	 */
	protected static final WayPoint WAY_POINT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWayPoint() <em>Way Point</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWayPoint()
	 * @generated
	 * @ordered
	 */
	protected WayPoint wayPoint = WAY_POINT_EDEFAULT;

	/**
	 * The default value of the '{@link #getWayPointID() <em>Way Point ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWayPointID()
	 * @generated
	 * @ordered
	 */
	protected static final String WAY_POINT_ID_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getWayPointID() <em>Way Point ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWayPointID()
	 * @generated
	 * @ordered
	 */
	protected String wayPointID = WAY_POINT_ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected WayPointRouteElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SolutionPackage.Literals.WAY_POINT_ROUTE_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public WayPoint getWayPoint() {
		return wayPoint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWayPoint(WayPoint newWayPoint) {
		WayPoint oldWayPoint = wayPoint;
		wayPoint = newWayPoint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.WAY_POINT_ROUTE_ELEMENT__WAY_POINT, oldWayPoint, wayPoint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getWayPointID() {
		return wayPointID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWayPointID(String newWayPointID) {
		String oldWayPointID = wayPointID;
		wayPointID = newWayPointID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.WAY_POINT_ROUTE_ELEMENT__WAY_POINT_ID, oldWayPointID, wayPointID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SolutionPackage.WAY_POINT_ROUTE_ELEMENT__WAY_POINT:
				return getWayPoint();
			case SolutionPackage.WAY_POINT_ROUTE_ELEMENT__WAY_POINT_ID:
				return getWayPointID();
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
			case SolutionPackage.WAY_POINT_ROUTE_ELEMENT__WAY_POINT:
				setWayPoint((WayPoint)newValue);
				return;
			case SolutionPackage.WAY_POINT_ROUTE_ELEMENT__WAY_POINT_ID:
				setWayPointID((String)newValue);
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
			case SolutionPackage.WAY_POINT_ROUTE_ELEMENT__WAY_POINT:
				setWayPoint(WAY_POINT_EDEFAULT);
				return;
			case SolutionPackage.WAY_POINT_ROUTE_ELEMENT__WAY_POINT_ID:
				setWayPointID(WAY_POINT_ID_EDEFAULT);
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
			case SolutionPackage.WAY_POINT_ROUTE_ELEMENT__WAY_POINT:
				return WAY_POINT_EDEFAULT == null ? wayPoint != null : !WAY_POINT_EDEFAULT.equals(wayPoint);
			case SolutionPackage.WAY_POINT_ROUTE_ELEMENT__WAY_POINT_ID:
				return WAY_POINT_ID_EDEFAULT == null ? wayPointID != null : !WAY_POINT_ID_EDEFAULT.equals(wayPointID);
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
		result.append(" (wayPoint: ");
		result.append(wayPoint);
		result.append(", wayPointID: ");
		result.append(wayPointID);
		result.append(')');
		return result.toString();
	}

} //WayPointRouteElementImpl
