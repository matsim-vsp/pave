/**
 */
package ovgu.pave.model.solution.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import ovgu.pave.model.input.Request;

import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.Solution;
import ovgu.pave.model.solution.SolutionPackage;
import ovgu.pave.model.solution.UseVehicleType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Solution</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.solution.impl.SolutionImpl#getRoutes <em>Routes</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.SolutionImpl#getScore <em>Score</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.SolutionImpl#getUseVehicleType <em>Use Vehicle Type</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.SolutionImpl#getUnintegratedRequests <em>Unintegrated Requests</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.SolutionImpl#getIntegratedRequests <em>Integrated Requests</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SolutionImpl extends MinimalEObjectImpl.Container implements Solution {
	/**
	 * The cached value of the '{@link #getRoutes() <em>Routes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoutes()
	 * @generated
	 * @ordered
	 */
	protected EList<Route> routes;

	/**
	 * The default value of the '{@link #getScore() <em>Score</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScore()
	 * @generated
	 * @ordered
	 */
	protected static final long SCORE_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getScore() <em>Score</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScore()
	 * @generated
	 * @ordered
	 */
	protected long score = SCORE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getUseVehicleType() <em>Use Vehicle Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUseVehicleType()
	 * @generated
	 * @ordered
	 */
	protected UseVehicleType useVehicleType;

	/**
	 * The cached value of the '{@link #getUnintegratedRequests() <em>Unintegrated Requests</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnintegratedRequests()
	 * @generated
	 * @ordered
	 */
	protected EList<Request> unintegratedRequests;

	/**
	 * The cached value of the '{@link #getIntegratedRequests() <em>Integrated Requests</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIntegratedRequests()
	 * @generated
	 * @ordered
	 */
	protected EList<Request> integratedRequests;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SolutionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SolutionPackage.Literals.SOLUTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Route> getRoutes() {
		if (routes == null) {
			routes = new EObjectContainmentEList<Route>(Route.class, this, SolutionPackage.SOLUTION__ROUTES);
		}
		return routes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getScore() {
		return score;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScore(long newScore) {
		long oldScore = score;
		score = newScore;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.SOLUTION__SCORE, oldScore, score));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UseVehicleType getUseVehicleType() {
		return useVehicleType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetUseVehicleType(UseVehicleType newUseVehicleType, NotificationChain msgs) {
		UseVehicleType oldUseVehicleType = useVehicleType;
		useVehicleType = newUseVehicleType;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SolutionPackage.SOLUTION__USE_VEHICLE_TYPE, oldUseVehicleType, newUseVehicleType);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUseVehicleType(UseVehicleType newUseVehicleType) {
		if (newUseVehicleType != useVehicleType) {
			NotificationChain msgs = null;
			if (useVehicleType != null)
				msgs = ((InternalEObject)useVehicleType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SolutionPackage.SOLUTION__USE_VEHICLE_TYPE, null, msgs);
			if (newUseVehicleType != null)
				msgs = ((InternalEObject)newUseVehicleType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SolutionPackage.SOLUTION__USE_VEHICLE_TYPE, null, msgs);
			msgs = basicSetUseVehicleType(newUseVehicleType, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.SOLUTION__USE_VEHICLE_TYPE, newUseVehicleType, newUseVehicleType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Request> getUnintegratedRequests() {
		if (unintegratedRequests == null) {
			unintegratedRequests = new EDataTypeUniqueEList<Request>(Request.class, this, SolutionPackage.SOLUTION__UNINTEGRATED_REQUESTS);
		}
		return unintegratedRequests;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Request> getIntegratedRequests() {
		if (integratedRequests == null) {
			integratedRequests = new EDataTypeUniqueEList<Request>(Request.class, this, SolutionPackage.SOLUTION__INTEGRATED_REQUESTS);
		}
		return integratedRequests;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SolutionPackage.SOLUTION__ROUTES:
				return ((InternalEList<?>)getRoutes()).basicRemove(otherEnd, msgs);
			case SolutionPackage.SOLUTION__USE_VEHICLE_TYPE:
				return basicSetUseVehicleType(null, msgs);
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
			case SolutionPackage.SOLUTION__ROUTES:
				return getRoutes();
			case SolutionPackage.SOLUTION__SCORE:
				return getScore();
			case SolutionPackage.SOLUTION__USE_VEHICLE_TYPE:
				return getUseVehicleType();
			case SolutionPackage.SOLUTION__UNINTEGRATED_REQUESTS:
				return getUnintegratedRequests();
			case SolutionPackage.SOLUTION__INTEGRATED_REQUESTS:
				return getIntegratedRequests();
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
			case SolutionPackage.SOLUTION__ROUTES:
				getRoutes().clear();
				getRoutes().addAll((Collection<? extends Route>)newValue);
				return;
			case SolutionPackage.SOLUTION__SCORE:
				setScore((Long)newValue);
				return;
			case SolutionPackage.SOLUTION__USE_VEHICLE_TYPE:
				setUseVehicleType((UseVehicleType)newValue);
				return;
			case SolutionPackage.SOLUTION__UNINTEGRATED_REQUESTS:
				getUnintegratedRequests().clear();
				getUnintegratedRequests().addAll((Collection<? extends Request>)newValue);
				return;
			case SolutionPackage.SOLUTION__INTEGRATED_REQUESTS:
				getIntegratedRequests().clear();
				getIntegratedRequests().addAll((Collection<? extends Request>)newValue);
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
			case SolutionPackage.SOLUTION__ROUTES:
				getRoutes().clear();
				return;
			case SolutionPackage.SOLUTION__SCORE:
				setScore(SCORE_EDEFAULT);
				return;
			case SolutionPackage.SOLUTION__USE_VEHICLE_TYPE:
				setUseVehicleType((UseVehicleType)null);
				return;
			case SolutionPackage.SOLUTION__UNINTEGRATED_REQUESTS:
				getUnintegratedRequests().clear();
				return;
			case SolutionPackage.SOLUTION__INTEGRATED_REQUESTS:
				getIntegratedRequests().clear();
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
			case SolutionPackage.SOLUTION__ROUTES:
				return routes != null && !routes.isEmpty();
			case SolutionPackage.SOLUTION__SCORE:
				return score != SCORE_EDEFAULT;
			case SolutionPackage.SOLUTION__USE_VEHICLE_TYPE:
				return useVehicleType != null;
			case SolutionPackage.SOLUTION__UNINTEGRATED_REQUESTS:
				return unintegratedRequests != null && !unintegratedRequests.isEmpty();
			case SolutionPackage.SOLUTION__INTEGRATED_REQUESTS:
				return integratedRequests != null && !integratedRequests.isEmpty();
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
		result.append(" (Score: ");
		result.append(score);
		result.append(", unintegratedRequests: ");
		result.append(unintegratedRequests);
		result.append(", integratedRequests: ");
		result.append(integratedRequests);
		result.append(')');
		return result.toString();
	}

} //SolutionImpl
