/**
 */
package ovgu.pave.model.input.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import ovgu.pave.model.input.DestinationRequestActivity;
import ovgu.pave.model.input.InputPackage;
import ovgu.pave.model.input.OriginRequestActivity;
import ovgu.pave.model.input.Request;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Request</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.input.impl.RequestImpl#getOriginActivity <em>Origin Activity</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.RequestImpl#getDestinationActivity <em>Destination Activity</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.RequestImpl#getId <em>Id</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.RequestImpl#getReceivingTime <em>Receiving Time</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.RequestImpl#getQuantity <em>Quantity</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RequestImpl extends MinimalEObjectImpl.Container implements Request {
	/**
	 * The cached value of the '{@link #getOriginActivity() <em>Origin Activity</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOriginActivity()
	 * @generated
	 * @ordered
	 */
	protected OriginRequestActivity originActivity;

	/**
	 * The cached value of the '{@link #getDestinationActivity() <em>Destination Activity</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestinationActivity()
	 * @generated
	 * @ordered
	 */
	protected DestinationRequestActivity destinationActivity;

	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final int ID_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected int id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getReceivingTime() <em>Receiving Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReceivingTime()
	 * @generated
	 * @ordered
	 */
	protected static final long RECEIVING_TIME_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getReceivingTime() <em>Receiving Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReceivingTime()
	 * @generated
	 * @ordered
	 */
	protected long receivingTime = RECEIVING_TIME_EDEFAULT;

	/**
	 * The default value of the '{@link #getQuantity() <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuantity()
	 * @generated
	 * @ordered
	 */
	protected static final long QUANTITY_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getQuantity() <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuantity()
	 * @generated
	 * @ordered
	 */
	protected long quantity = QUANTITY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RequestImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return InputPackage.Literals.REQUEST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OriginRequestActivity getOriginActivity() {
		return originActivity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOriginActivity(OriginRequestActivity newOriginActivity, NotificationChain msgs) {
		OriginRequestActivity oldOriginActivity = originActivity;
		originActivity = newOriginActivity;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, InputPackage.REQUEST__ORIGIN_ACTIVITY, oldOriginActivity, newOriginActivity);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOriginActivity(OriginRequestActivity newOriginActivity) {
		if (newOriginActivity != originActivity) {
			NotificationChain msgs = null;
			if (originActivity != null)
				msgs = ((InternalEObject)originActivity).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - InputPackage.REQUEST__ORIGIN_ACTIVITY, null, msgs);
			if (newOriginActivity != null)
				msgs = ((InternalEObject)newOriginActivity).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - InputPackage.REQUEST__ORIGIN_ACTIVITY, null, msgs);
			msgs = basicSetOriginActivity(newOriginActivity, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.REQUEST__ORIGIN_ACTIVITY, newOriginActivity, newOriginActivity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DestinationRequestActivity getDestinationActivity() {
		return destinationActivity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDestinationActivity(DestinationRequestActivity newDestinationActivity, NotificationChain msgs) {
		DestinationRequestActivity oldDestinationActivity = destinationActivity;
		destinationActivity = newDestinationActivity;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, InputPackage.REQUEST__DESTINATION_ACTIVITY, oldDestinationActivity, newDestinationActivity);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDestinationActivity(DestinationRequestActivity newDestinationActivity) {
		if (newDestinationActivity != destinationActivity) {
			NotificationChain msgs = null;
			if (destinationActivity != null)
				msgs = ((InternalEObject)destinationActivity).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - InputPackage.REQUEST__DESTINATION_ACTIVITY, null, msgs);
			if (newDestinationActivity != null)
				msgs = ((InternalEObject)newDestinationActivity).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - InputPackage.REQUEST__DESTINATION_ACTIVITY, null, msgs);
			msgs = basicSetDestinationActivity(newDestinationActivity, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.REQUEST__DESTINATION_ACTIVITY, newDestinationActivity, newDestinationActivity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setId(int newId) {
		int oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.REQUEST__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getReceivingTime() {
		return receivingTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReceivingTime(long newReceivingTime) {
		long oldReceivingTime = receivingTime;
		receivingTime = newReceivingTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.REQUEST__RECEIVING_TIME, oldReceivingTime, receivingTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getQuantity() {
		return quantity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setQuantity(long newQuantity) {
		long oldQuantity = quantity;
		quantity = newQuantity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.REQUEST__QUANTITY, oldQuantity, quantity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case InputPackage.REQUEST__ORIGIN_ACTIVITY:
				return basicSetOriginActivity(null, msgs);
			case InputPackage.REQUEST__DESTINATION_ACTIVITY:
				return basicSetDestinationActivity(null, msgs);
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
			case InputPackage.REQUEST__ORIGIN_ACTIVITY:
				return getOriginActivity();
			case InputPackage.REQUEST__DESTINATION_ACTIVITY:
				return getDestinationActivity();
			case InputPackage.REQUEST__ID:
				return getId();
			case InputPackage.REQUEST__RECEIVING_TIME:
				return getReceivingTime();
			case InputPackage.REQUEST__QUANTITY:
				return getQuantity();
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
			case InputPackage.REQUEST__ORIGIN_ACTIVITY:
				setOriginActivity((OriginRequestActivity)newValue);
				return;
			case InputPackage.REQUEST__DESTINATION_ACTIVITY:
				setDestinationActivity((DestinationRequestActivity)newValue);
				return;
			case InputPackage.REQUEST__ID:
				setId((Integer)newValue);
				return;
			case InputPackage.REQUEST__RECEIVING_TIME:
				setReceivingTime((Long)newValue);
				return;
			case InputPackage.REQUEST__QUANTITY:
				setQuantity((Long)newValue);
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
			case InputPackage.REQUEST__ORIGIN_ACTIVITY:
				setOriginActivity((OriginRequestActivity)null);
				return;
			case InputPackage.REQUEST__DESTINATION_ACTIVITY:
				setDestinationActivity((DestinationRequestActivity)null);
				return;
			case InputPackage.REQUEST__ID:
				setId(ID_EDEFAULT);
				return;
			case InputPackage.REQUEST__RECEIVING_TIME:
				setReceivingTime(RECEIVING_TIME_EDEFAULT);
				return;
			case InputPackage.REQUEST__QUANTITY:
				setQuantity(QUANTITY_EDEFAULT);
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
			case InputPackage.REQUEST__ORIGIN_ACTIVITY:
				return originActivity != null;
			case InputPackage.REQUEST__DESTINATION_ACTIVITY:
				return destinationActivity != null;
			case InputPackage.REQUEST__ID:
				return id != ID_EDEFAULT;
			case InputPackage.REQUEST__RECEIVING_TIME:
				return receivingTime != RECEIVING_TIME_EDEFAULT;
			case InputPackage.REQUEST__QUANTITY:
				return quantity != QUANTITY_EDEFAULT;
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
		result.append(" (id: ");
		result.append(id);
		result.append(", receivingTime: ");
		result.append(receivingTime);
		result.append(", quantity: ");
		result.append(quantity);
		result.append(')');
		return result.toString();
	}

} //RequestImpl
