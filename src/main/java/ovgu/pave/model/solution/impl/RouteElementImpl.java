/**
 */
package ovgu.pave.model.solution.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import ovgu.pave.model.solution.RouteElement;
import ovgu.pave.model.solution.SolutionPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Route Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.solution.impl.RouteElementImpl#getServiceBegin <em>Service Begin</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.RouteElementImpl#getServiceDuration <em>Service Duration</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RouteElementImpl extends MinimalEObjectImpl.Container implements RouteElement {
	/**
	 * The default value of the '{@link #getServiceBegin() <em>Service Begin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceBegin()
	 * @generated
	 * @ordered
	 */
	protected static final long SERVICE_BEGIN_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getServiceBegin() <em>Service Begin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceBegin()
	 * @generated
	 * @ordered
	 */
	protected long serviceBegin = SERVICE_BEGIN_EDEFAULT;

	/**
	 * The default value of the '{@link #getServiceDuration() <em>Service Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceDuration()
	 * @generated
	 * @ordered
	 */
	protected static final long SERVICE_DURATION_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getServiceDuration() <em>Service Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceDuration()
	 * @generated
	 * @ordered
	 */
	protected long serviceDuration = SERVICE_DURATION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RouteElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SolutionPackage.Literals.ROUTE_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getServiceBegin() {
		return serviceBegin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setServiceBegin(long newServiceBegin) {
		long oldServiceBegin = serviceBegin;
		serviceBegin = newServiceBegin;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.ROUTE_ELEMENT__SERVICE_BEGIN, oldServiceBegin, serviceBegin));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getServiceDuration() {
		return serviceDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setServiceDuration(long newServiceDuration) {
		long oldServiceDuration = serviceDuration;
		serviceDuration = newServiceDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.ROUTE_ELEMENT__SERVICE_DURATION, oldServiceDuration, serviceDuration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SolutionPackage.ROUTE_ELEMENT__SERVICE_BEGIN:
				return getServiceBegin();
			case SolutionPackage.ROUTE_ELEMENT__SERVICE_DURATION:
				return getServiceDuration();
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
			case SolutionPackage.ROUTE_ELEMENT__SERVICE_BEGIN:
				setServiceBegin((Long)newValue);
				return;
			case SolutionPackage.ROUTE_ELEMENT__SERVICE_DURATION:
				setServiceDuration((Long)newValue);
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
			case SolutionPackage.ROUTE_ELEMENT__SERVICE_BEGIN:
				setServiceBegin(SERVICE_BEGIN_EDEFAULT);
				return;
			case SolutionPackage.ROUTE_ELEMENT__SERVICE_DURATION:
				setServiceDuration(SERVICE_DURATION_EDEFAULT);
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
			case SolutionPackage.ROUTE_ELEMENT__SERVICE_BEGIN:
				return serviceBegin != SERVICE_BEGIN_EDEFAULT;
			case SolutionPackage.ROUTE_ELEMENT__SERVICE_DURATION:
				return serviceDuration != SERVICE_DURATION_EDEFAULT;
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
		result.append(" (serviceBegin: ");
		result.append(serviceBegin);
		result.append(", serviceDuration: ");
		result.append(serviceDuration);
		result.append(')');
		return result.toString();
	}

} //RouteElementImpl
