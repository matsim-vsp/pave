/**
 */
package ovgu.pave.model.algorithmObjects.impl;

import java.lang.reflect.InvocationTargetException;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage;
import ovgu.pave.model.algorithmObjects.InsertionInformation;
import ovgu.pave.model.algorithmObjects.InsertionOption;

import ovgu.pave.model.input.Request;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Insertion Information</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.InsertionInformationImpl#getInsertionOptions <em>Insertion Options</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.InsertionInformationImpl#getRequest <em>Request</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.InsertionInformationImpl#getScore <em>Score</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InsertionInformationImpl extends MinimalEObjectImpl.Container implements InsertionInformation {
	/**
	 * The cached value of the '{@link #getInsertionOptions() <em>Insertion Options</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInsertionOptions()
	 * @generated
	 * @ordered
	 */
	protected EList<InsertionOption> insertionOptions;

	/**
	 * The default value of the '{@link #getRequest() <em>Request</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequest()
	 * @generated
	 * @ordered
	 */
	protected static final Request REQUEST_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRequest() <em>Request</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequest()
	 * @generated
	 * @ordered
	 */
	protected Request request = REQUEST_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InsertionInformationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AlgorithmObjectsPackage.Literals.INSERTION_INFORMATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<InsertionOption> getInsertionOptions() {
		if (insertionOptions == null) {
			insertionOptions = new EObjectResolvingEList<InsertionOption>(InsertionOption.class, this, AlgorithmObjectsPackage.INSERTION_INFORMATION__INSERTION_OPTIONS);
		}
		return insertionOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Request getRequest() {
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
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.INSERTION_INFORMATION__REQUEST, oldRequest, request));
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
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.INSERTION_INFORMATION__SCORE, oldScore, score));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScoreForRegretHeuristic(final int regretValue) {
		if (regretValue == -1) {
		  score = insertionOptions.size();
		} else if (regretValue == 0) {
			score = - insertionOptions.get(0).getCosts();
		} else if (regretValue < insertionOptions.size()) {
		    score = insertionOptions.get(regretValue).getCosts() - insertionOptions.get(0).getCosts();
		}  else {
		    score = (Long.MAX_VALUE - insertionOptions.get(0).getCosts());
		}		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AlgorithmObjectsPackage.INSERTION_INFORMATION__INSERTION_OPTIONS:
				return getInsertionOptions();
			case AlgorithmObjectsPackage.INSERTION_INFORMATION__REQUEST:
				return getRequest();
			case AlgorithmObjectsPackage.INSERTION_INFORMATION__SCORE:
				return getScore();
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
			case AlgorithmObjectsPackage.INSERTION_INFORMATION__INSERTION_OPTIONS:
				getInsertionOptions().clear();
				getInsertionOptions().addAll((Collection<? extends InsertionOption>)newValue);
				return;
			case AlgorithmObjectsPackage.INSERTION_INFORMATION__REQUEST:
				setRequest((Request)newValue);
				return;
			case AlgorithmObjectsPackage.INSERTION_INFORMATION__SCORE:
				setScore((Long)newValue);
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
			case AlgorithmObjectsPackage.INSERTION_INFORMATION__INSERTION_OPTIONS:
				getInsertionOptions().clear();
				return;
			case AlgorithmObjectsPackage.INSERTION_INFORMATION__REQUEST:
				setRequest(REQUEST_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.INSERTION_INFORMATION__SCORE:
				setScore(SCORE_EDEFAULT);
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
			case AlgorithmObjectsPackage.INSERTION_INFORMATION__INSERTION_OPTIONS:
				return insertionOptions != null && !insertionOptions.isEmpty();
			case AlgorithmObjectsPackage.INSERTION_INFORMATION__REQUEST:
				return REQUEST_EDEFAULT == null ? request != null : !REQUEST_EDEFAULT.equals(request);
			case AlgorithmObjectsPackage.INSERTION_INFORMATION__SCORE:
				return score != SCORE_EDEFAULT;
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
			case AlgorithmObjectsPackage.INSERTION_INFORMATION___SET_SCORE_FOR_REGRET_HEURISTIC__INT:
				setScoreForRegretHeuristic((Integer)arguments.get(0));
				return null;
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
		result.append(" (request: ");
		result.append(request);
		result.append(", score: ");
		result.append(score);
		result.append(')');
		return result.toString();
	}

} //InsertionInformationImpl
