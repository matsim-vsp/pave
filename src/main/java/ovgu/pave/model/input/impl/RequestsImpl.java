/**
 */
package ovgu.pave.model.input.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import ovgu.pave.model.input.InputPackage;
import ovgu.pave.model.input.Request;
import ovgu.pave.model.input.Requests;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Requests</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.input.impl.RequestsImpl#getAccepted <em>Accepted</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.RequestsImpl#getRejected <em>Rejected</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.RequestsImpl#getNew <em>New</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RequestsImpl extends MinimalEObjectImpl.Container implements Requests {
	/**
	 * The cached value of the '{@link #getAccepted() <em>Accepted</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAccepted()
	 * @generated
	 * @ordered
	 */
	protected EList<Request> accepted;

	/**
	 * The cached value of the '{@link #getRejected() <em>Rejected</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRejected()
	 * @generated
	 * @ordered
	 */
	protected EList<Request> rejected;

	/**
	 * The cached value of the '{@link #getNew() <em>New</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNew()
	 * @generated
	 * @ordered
	 */
	protected EList<Request> new_;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RequestsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return InputPackage.Literals.REQUESTS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Request> getAccepted() {
		if (accepted == null) {
			accepted = new EObjectContainmentEList<Request>(Request.class, this, InputPackage.REQUESTS__ACCEPTED);
		}
		return accepted;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Request> getRejected() {
		if (rejected == null) {
			rejected = new EObjectContainmentEList<Request>(Request.class, this, InputPackage.REQUESTS__REJECTED);
		}
		return rejected;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Request> getNew() {
		if (new_ == null) {
			new_ = new EObjectContainmentEList<Request>(Request.class, this, InputPackage.REQUESTS__NEW);
		}
		return new_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case InputPackage.REQUESTS__ACCEPTED:
				return ((InternalEList<?>)getAccepted()).basicRemove(otherEnd, msgs);
			case InputPackage.REQUESTS__REJECTED:
				return ((InternalEList<?>)getRejected()).basicRemove(otherEnd, msgs);
			case InputPackage.REQUESTS__NEW:
				return ((InternalEList<?>)getNew()).basicRemove(otherEnd, msgs);
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
			case InputPackage.REQUESTS__ACCEPTED:
				return getAccepted();
			case InputPackage.REQUESTS__REJECTED:
				return getRejected();
			case InputPackage.REQUESTS__NEW:
				return getNew();
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
			case InputPackage.REQUESTS__ACCEPTED:
				getAccepted().clear();
				getAccepted().addAll((Collection<? extends Request>)newValue);
				return;
			case InputPackage.REQUESTS__REJECTED:
				getRejected().clear();
				getRejected().addAll((Collection<? extends Request>)newValue);
				return;
			case InputPackage.REQUESTS__NEW:
				getNew().clear();
				getNew().addAll((Collection<? extends Request>)newValue);
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
			case InputPackage.REQUESTS__ACCEPTED:
				getAccepted().clear();
				return;
			case InputPackage.REQUESTS__REJECTED:
				getRejected().clear();
				return;
			case InputPackage.REQUESTS__NEW:
				getNew().clear();
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
			case InputPackage.REQUESTS__ACCEPTED:
				return accepted != null && !accepted.isEmpty();
			case InputPackage.REQUESTS__REJECTED:
				return rejected != null && !rejected.isEmpty();
			case InputPackage.REQUESTS__NEW:
				return new_ != null && !new_.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //RequestsImpl
