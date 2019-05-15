/**
 */
package ovgu.pave.model.solution.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import ovgu.pave.model.input.RequestActivity;

import ovgu.pave.model.solution.RequestActivityRouteElement;
import ovgu.pave.model.solution.SolutionPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Request Activity Route Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.solution.impl.RequestActivityRouteElementImpl#getRequestActivity <em>Request Activity</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.RequestActivityRouteElementImpl#getRequestActivityID <em>Request Activity ID</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RequestActivityRouteElementImpl extends RouteElementImpl implements RequestActivityRouteElement {
	/**
	 * The default value of the '{@link #getRequestActivity() <em>Request Activity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequestActivity()
	 * @generated
	 * @ordered
	 */
	protected static final RequestActivity REQUEST_ACTIVITY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRequestActivity() <em>Request Activity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequestActivity()
	 * @generated
	 * @ordered
	 */
	protected RequestActivity requestActivity = REQUEST_ACTIVITY_EDEFAULT;

	/**
	 * The default value of the '{@link #getRequestActivityID() <em>Request Activity ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequestActivityID()
	 * @generated
	 * @ordered
	 */
	protected static final String REQUEST_ACTIVITY_ID_EDEFAULT = "";

	/**
	 * The cached value of the '{@link #getRequestActivityID() <em>Request Activity ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequestActivityID()
	 * @generated
	 * @ordered
	 */
	protected String requestActivityID = REQUEST_ACTIVITY_ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RequestActivityRouteElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SolutionPackage.Literals.REQUEST_ACTIVITY_ROUTE_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RequestActivity getRequestActivity() {
		return requestActivity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRequestActivity(RequestActivity newRequestActivity) {
		RequestActivity oldRequestActivity = requestActivity;
		requestActivity = newRequestActivity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY, oldRequestActivity, requestActivity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRequestActivityID() {
		return requestActivityID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRequestActivityID(String newRequestActivityID) {
		String oldRequestActivityID = requestActivityID;
		requestActivityID = newRequestActivityID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY_ID, oldRequestActivityID, requestActivityID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SolutionPackage.REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY:
				return getRequestActivity();
			case SolutionPackage.REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY_ID:
				return getRequestActivityID();
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
			case SolutionPackage.REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY:
				setRequestActivity((RequestActivity)newValue);
				return;
			case SolutionPackage.REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY_ID:
				setRequestActivityID((String)newValue);
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
			case SolutionPackage.REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY:
				setRequestActivity(REQUEST_ACTIVITY_EDEFAULT);
				return;
			case SolutionPackage.REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY_ID:
				setRequestActivityID(REQUEST_ACTIVITY_ID_EDEFAULT);
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
			case SolutionPackage.REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY:
				return REQUEST_ACTIVITY_EDEFAULT == null ? requestActivity != null : !REQUEST_ACTIVITY_EDEFAULT.equals(requestActivity);
			case SolutionPackage.REQUEST_ACTIVITY_ROUTE_ELEMENT__REQUEST_ACTIVITY_ID:
				return REQUEST_ACTIVITY_ID_EDEFAULT == null ? requestActivityID != null : !REQUEST_ACTIVITY_ID_EDEFAULT.equals(requestActivityID);
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
		result.append(" (requestActivity: ");
		result.append(requestActivity);
		result.append(", requestActivityID: ");
		result.append(requestActivityID);
		result.append(')');
		return result.toString();
	}

} //RequestActivityRouteElementImpl
