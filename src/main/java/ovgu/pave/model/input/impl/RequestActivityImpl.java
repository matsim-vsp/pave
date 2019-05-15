/**
 */
package ovgu.pave.model.input.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import ovgu.pave.model.input.InputPackage;
import ovgu.pave.model.input.Location;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.input.RequestActivity;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Request Activity</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.input.impl.RequestActivityImpl#getRequest <em>Request</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.RequestActivityImpl#getLocation <em>Location</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.RequestActivityImpl#getServiceDuration <em>Service Duration</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.RequestActivityImpl#getEarliestArrival <em>Earliest Arrival</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.RequestActivityImpl#getLatestArrival <em>Latest Arrival</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.RequestActivityImpl#getId <em>Id</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RequestActivityImpl extends MinimalEObjectImpl.Container implements RequestActivity {
	/**
	 * The cached value of the '{@link #getRequest() <em>Request</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequest()
	 * @generated
	 * @ordered
	 */
	protected Request request;

	/**
	 * The cached value of the '{@link #getLocation() <em>Location</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocation()
	 * @generated
	 * @ordered
	 */
	protected Location location;

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
	 * The default value of the '{@link #getEarliestArrival() <em>Earliest Arrival</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEarliestArrival()
	 * @generated
	 * @ordered
	 */
	protected static final long EARLIEST_ARRIVAL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getEarliestArrival() <em>Earliest Arrival</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEarliestArrival()
	 * @generated
	 * @ordered
	 */
	protected long earliestArrival = EARLIEST_ARRIVAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getLatestArrival() <em>Latest Arrival</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestArrival()
	 * @generated
	 * @ordered
	 */
	protected static final long LATEST_ARRIVAL_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getLatestArrival() <em>Latest Arrival</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLatestArrival()
	 * @generated
	 * @ordered
	 */
	protected long latestArrival = LATEST_ARRIVAL_EDEFAULT;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RequestActivityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return InputPackage.Literals.REQUEST_ACTIVITY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Request getRequest() {
		if (request != null && request.eIsProxy()) {
			InternalEObject oldRequest = (InternalEObject)request;
			request = (Request)eResolveProxy(oldRequest);
			if (request != oldRequest) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, InputPackage.REQUEST_ACTIVITY__REQUEST, oldRequest, request));
			}
		}
		return request;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Request basicGetRequest() {
		return request;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRequest(Request newRequest) {
		Request oldRequest = request;
		request = newRequest;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.REQUEST_ACTIVITY__REQUEST, oldRequest, request));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Location getLocation() {
		if (location != null && location.eIsProxy()) {
			InternalEObject oldLocation = (InternalEObject)location;
			location = (Location)eResolveProxy(oldLocation);
			if (location != oldLocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, InputPackage.REQUEST_ACTIVITY__LOCATION, oldLocation, location));
			}
		}
		return location;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Location basicGetLocation() {
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
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.REQUEST_ACTIVITY__LOCATION, oldLocation, location));
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
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.REQUEST_ACTIVITY__SERVICE_DURATION, oldServiceDuration, serviceDuration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getEarliestArrival() {
		return earliestArrival;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEarliestArrival(long newEarliestArrival) {
		long oldEarliestArrival = earliestArrival;
		earliestArrival = newEarliestArrival;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.REQUEST_ACTIVITY__EARLIEST_ARRIVAL, oldEarliestArrival, earliestArrival));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getLatestArrival() {
		return latestArrival;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLatestArrival(long newLatestArrival) {
		long oldLatestArrival = latestArrival;
		latestArrival = newLatestArrival;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.REQUEST_ACTIVITY__LATEST_ARRIVAL, oldLatestArrival, latestArrival));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.REQUEST_ACTIVITY__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case InputPackage.REQUEST_ACTIVITY__REQUEST:
				if (resolve) return getRequest();
				return basicGetRequest();
			case InputPackage.REQUEST_ACTIVITY__LOCATION:
				if (resolve) return getLocation();
				return basicGetLocation();
			case InputPackage.REQUEST_ACTIVITY__SERVICE_DURATION:
				return getServiceDuration();
			case InputPackage.REQUEST_ACTIVITY__EARLIEST_ARRIVAL:
				return getEarliestArrival();
			case InputPackage.REQUEST_ACTIVITY__LATEST_ARRIVAL:
				return getLatestArrival();
			case InputPackage.REQUEST_ACTIVITY__ID:
				return getId();
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
			case InputPackage.REQUEST_ACTIVITY__REQUEST:
				setRequest((Request)newValue);
				return;
			case InputPackage.REQUEST_ACTIVITY__LOCATION:
				setLocation((Location)newValue);
				return;
			case InputPackage.REQUEST_ACTIVITY__SERVICE_DURATION:
				setServiceDuration((Long)newValue);
				return;
			case InputPackage.REQUEST_ACTIVITY__EARLIEST_ARRIVAL:
				setEarliestArrival((Long)newValue);
				return;
			case InputPackage.REQUEST_ACTIVITY__LATEST_ARRIVAL:
				setLatestArrival((Long)newValue);
				return;
			case InputPackage.REQUEST_ACTIVITY__ID:
				setId((String)newValue);
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
			case InputPackage.REQUEST_ACTIVITY__REQUEST:
				setRequest((Request)null);
				return;
			case InputPackage.REQUEST_ACTIVITY__LOCATION:
				setLocation((Location)null);
				return;
			case InputPackage.REQUEST_ACTIVITY__SERVICE_DURATION:
				setServiceDuration(SERVICE_DURATION_EDEFAULT);
				return;
			case InputPackage.REQUEST_ACTIVITY__EARLIEST_ARRIVAL:
				setEarliestArrival(EARLIEST_ARRIVAL_EDEFAULT);
				return;
			case InputPackage.REQUEST_ACTIVITY__LATEST_ARRIVAL:
				setLatestArrival(LATEST_ARRIVAL_EDEFAULT);
				return;
			case InputPackage.REQUEST_ACTIVITY__ID:
				setId(ID_EDEFAULT);
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
			case InputPackage.REQUEST_ACTIVITY__REQUEST:
				return request != null;
			case InputPackage.REQUEST_ACTIVITY__LOCATION:
				return location != null;
			case InputPackage.REQUEST_ACTIVITY__SERVICE_DURATION:
				return serviceDuration != SERVICE_DURATION_EDEFAULT;
			case InputPackage.REQUEST_ACTIVITY__EARLIEST_ARRIVAL:
				return earliestArrival != EARLIEST_ARRIVAL_EDEFAULT;
			case InputPackage.REQUEST_ACTIVITY__LATEST_ARRIVAL:
				return latestArrival != LATEST_ARRIVAL_EDEFAULT;
			case InputPackage.REQUEST_ACTIVITY__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
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
		result.append(" (serviceDuration: ");
		result.append(serviceDuration);
		result.append(", earliestArrival: ");
		result.append(earliestArrival);
		result.append(", latestArrival: ");
		result.append(latestArrival);
		result.append(", id: ");
		result.append(id);
		result.append(')');
		return result.toString();
	}

} //RequestActivityImpl
