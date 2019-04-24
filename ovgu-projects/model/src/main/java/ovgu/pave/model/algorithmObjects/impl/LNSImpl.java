/**
 */
package ovgu.pave.model.algorithmObjects.impl;

import java.util.HashMap;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage;
import ovgu.pave.model.algorithmObjects.LNS;

import ovgu.pave.model.input.Request;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>LNS</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.LNSImpl#getLowestCostsPerRequest <em>Lowest Costs Per Request</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.LNSImpl#getMaxIterations <em>Max Iterations</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.LNSImpl#getRejections <em>Rejections</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.LNSImpl#getIteration <em>Iteration</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.LNSImpl#getExchangeableRequests <em>Exchangeable Requests</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.LNSImpl#getMainIteration <em>Main Iteration</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.LNSImpl#getNewSolution <em>New Solution</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.LNSImpl#getAcceptanceIteration <em>Acceptance Iteration</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.impl.LNSImpl#getAcceptances <em>Acceptances</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LNSImpl extends MinimalEObjectImpl.Container implements LNS {
	/**
	 * The default value of the '{@link #getLowestCostsPerRequest() <em>Lowest Costs Per Request</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLowestCostsPerRequest()
	 * @generated
	 * @ordered
	 */
	protected static final HashMap<Request, Long> LOWEST_COSTS_PER_REQUEST_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLowestCostsPerRequest() <em>Lowest Costs Per Request</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLowestCostsPerRequest()
	 * @generated
	 * @ordered
	 */
	protected HashMap<Request, Long> lowestCostsPerRequest = LOWEST_COSTS_PER_REQUEST_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxIterations() <em>Max Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxIterations()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_ITERATIONS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxIterations() <em>Max Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxIterations()
	 * @generated
	 * @ordered
	 */
	protected int maxIterations = MAX_ITERATIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #getRejections() <em>Rejections</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRejections()
	 * @generated
	 * @ordered
	 */
	protected static final int REJECTIONS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getRejections() <em>Rejections</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRejections()
	 * @generated
	 * @ordered
	 */
	protected int rejections = REJECTIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #getIteration() <em>Iteration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIteration()
	 * @generated
	 * @ordered
	 */
	protected static final long ITERATION_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getIteration() <em>Iteration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIteration()
	 * @generated
	 * @ordered
	 */
	protected long iteration = ITERATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getExchangeableRequests() <em>Exchangeable Requests</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExchangeableRequests()
	 * @generated
	 * @ordered
	 */
	protected static final long EXCHANGEABLE_REQUESTS_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getExchangeableRequests() <em>Exchangeable Requests</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExchangeableRequests()
	 * @generated
	 * @ordered
	 */
	protected long exchangeableRequests = EXCHANGEABLE_REQUESTS_EDEFAULT;

	/**
	 * The default value of the '{@link #getMainIteration() <em>Main Iteration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMainIteration()
	 * @generated
	 * @ordered
	 */
	protected static final long MAIN_ITERATION_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getMainIteration() <em>Main Iteration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMainIteration()
	 * @generated
	 * @ordered
	 */
	protected long mainIteration = MAIN_ITERATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getNewSolution() <em>New Solution</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewSolution()
	 * @generated
	 * @ordered
	 */
	protected static final long NEW_SOLUTION_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getNewSolution() <em>New Solution</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewSolution()
	 * @generated
	 * @ordered
	 */
	protected long newSolution = NEW_SOLUTION_EDEFAULT;

	/**
	 * The default value of the '{@link #getAcceptanceIteration() <em>Acceptance Iteration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAcceptanceIteration()
	 * @generated
	 * @ordered
	 */
	protected static final int ACCEPTANCE_ITERATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getAcceptanceIteration() <em>Acceptance Iteration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAcceptanceIteration()
	 * @generated
	 * @ordered
	 */
	protected int acceptanceIteration = ACCEPTANCE_ITERATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getAcceptances() <em>Acceptances</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAcceptances()
	 * @generated
	 * @ordered
	 */
	protected static final int ACCEPTANCES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getAcceptances() <em>Acceptances</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAcceptances()
	 * @generated
	 * @ordered
	 */
	protected int acceptances = ACCEPTANCES_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LNSImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AlgorithmObjectsPackage.Literals.LNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HashMap<Request, Long> getLowestCostsPerRequest() {
		return lowestCostsPerRequest;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLowestCostsPerRequest(HashMap<Request, Long> newLowestCostsPerRequest) {
		HashMap<Request, Long> oldLowestCostsPerRequest = lowestCostsPerRequest;
		lowestCostsPerRequest = newLowestCostsPerRequest;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.LNS__LOWEST_COSTS_PER_REQUEST, oldLowestCostsPerRequest, lowestCostsPerRequest));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaxIterations() {
		return maxIterations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxIterations(int newMaxIterations) {
		int oldMaxIterations = maxIterations;
		maxIterations = newMaxIterations;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.LNS__MAX_ITERATIONS, oldMaxIterations, maxIterations));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getRejections() {
		return rejections;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRejections(int newRejections) {
		int oldRejections = rejections;
		rejections = newRejections;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.LNS__REJECTIONS, oldRejections, rejections));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getIteration() {
		return iteration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIteration(long newIteration) {
		long oldIteration = iteration;
		iteration = newIteration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.LNS__ITERATION, oldIteration, iteration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getExchangeableRequests() {
		return exchangeableRequests;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExchangeableRequests(long newExchangeableRequests) {
		long oldExchangeableRequests = exchangeableRequests;
		exchangeableRequests = newExchangeableRequests;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.LNS__EXCHANGEABLE_REQUESTS, oldExchangeableRequests, exchangeableRequests));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getMainIteration() {
		return mainIteration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMainIteration(long newMainIteration) {
		long oldMainIteration = mainIteration;
		mainIteration = newMainIteration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.LNS__MAIN_ITERATION, oldMainIteration, mainIteration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getNewSolution() {
		return newSolution;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewSolution(long newNewSolution) {
		long oldNewSolution = newSolution;
		newSolution = newNewSolution;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.LNS__NEW_SOLUTION, oldNewSolution, newSolution));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getAcceptanceIteration() {
		return acceptanceIteration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAcceptanceIteration(int newAcceptanceIteration) {
		int oldAcceptanceIteration = acceptanceIteration;
		acceptanceIteration = newAcceptanceIteration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.LNS__ACCEPTANCE_ITERATION, oldAcceptanceIteration, acceptanceIteration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getAcceptances() {
		return acceptances;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAcceptances(int newAcceptances) {
		int oldAcceptances = acceptances;
		acceptances = newAcceptances;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AlgorithmObjectsPackage.LNS__ACCEPTANCES, oldAcceptances, acceptances));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AlgorithmObjectsPackage.LNS__LOWEST_COSTS_PER_REQUEST:
				return getLowestCostsPerRequest();
			case AlgorithmObjectsPackage.LNS__MAX_ITERATIONS:
				return getMaxIterations();
			case AlgorithmObjectsPackage.LNS__REJECTIONS:
				return getRejections();
			case AlgorithmObjectsPackage.LNS__ITERATION:
				return getIteration();
			case AlgorithmObjectsPackage.LNS__EXCHANGEABLE_REQUESTS:
				return getExchangeableRequests();
			case AlgorithmObjectsPackage.LNS__MAIN_ITERATION:
				return getMainIteration();
			case AlgorithmObjectsPackage.LNS__NEW_SOLUTION:
				return getNewSolution();
			case AlgorithmObjectsPackage.LNS__ACCEPTANCE_ITERATION:
				return getAcceptanceIteration();
			case AlgorithmObjectsPackage.LNS__ACCEPTANCES:
				return getAcceptances();
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
			case AlgorithmObjectsPackage.LNS__LOWEST_COSTS_PER_REQUEST:
				setLowestCostsPerRequest((HashMap<Request, Long>)newValue);
				return;
			case AlgorithmObjectsPackage.LNS__MAX_ITERATIONS:
				setMaxIterations((Integer)newValue);
				return;
			case AlgorithmObjectsPackage.LNS__REJECTIONS:
				setRejections((Integer)newValue);
				return;
			case AlgorithmObjectsPackage.LNS__ITERATION:
				setIteration((Long)newValue);
				return;
			case AlgorithmObjectsPackage.LNS__EXCHANGEABLE_REQUESTS:
				setExchangeableRequests((Long)newValue);
				return;
			case AlgorithmObjectsPackage.LNS__MAIN_ITERATION:
				setMainIteration((Long)newValue);
				return;
			case AlgorithmObjectsPackage.LNS__NEW_SOLUTION:
				setNewSolution((Long)newValue);
				return;
			case AlgorithmObjectsPackage.LNS__ACCEPTANCE_ITERATION:
				setAcceptanceIteration((Integer)newValue);
				return;
			case AlgorithmObjectsPackage.LNS__ACCEPTANCES:
				setAcceptances((Integer)newValue);
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
			case AlgorithmObjectsPackage.LNS__LOWEST_COSTS_PER_REQUEST:
				setLowestCostsPerRequest(LOWEST_COSTS_PER_REQUEST_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.LNS__MAX_ITERATIONS:
				setMaxIterations(MAX_ITERATIONS_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.LNS__REJECTIONS:
				setRejections(REJECTIONS_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.LNS__ITERATION:
				setIteration(ITERATION_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.LNS__EXCHANGEABLE_REQUESTS:
				setExchangeableRequests(EXCHANGEABLE_REQUESTS_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.LNS__MAIN_ITERATION:
				setMainIteration(MAIN_ITERATION_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.LNS__NEW_SOLUTION:
				setNewSolution(NEW_SOLUTION_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.LNS__ACCEPTANCE_ITERATION:
				setAcceptanceIteration(ACCEPTANCE_ITERATION_EDEFAULT);
				return;
			case AlgorithmObjectsPackage.LNS__ACCEPTANCES:
				setAcceptances(ACCEPTANCES_EDEFAULT);
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
			case AlgorithmObjectsPackage.LNS__LOWEST_COSTS_PER_REQUEST:
				return LOWEST_COSTS_PER_REQUEST_EDEFAULT == null ? lowestCostsPerRequest != null : !LOWEST_COSTS_PER_REQUEST_EDEFAULT.equals(lowestCostsPerRequest);
			case AlgorithmObjectsPackage.LNS__MAX_ITERATIONS:
				return maxIterations != MAX_ITERATIONS_EDEFAULT;
			case AlgorithmObjectsPackage.LNS__REJECTIONS:
				return rejections != REJECTIONS_EDEFAULT;
			case AlgorithmObjectsPackage.LNS__ITERATION:
				return iteration != ITERATION_EDEFAULT;
			case AlgorithmObjectsPackage.LNS__EXCHANGEABLE_REQUESTS:
				return exchangeableRequests != EXCHANGEABLE_REQUESTS_EDEFAULT;
			case AlgorithmObjectsPackage.LNS__MAIN_ITERATION:
				return mainIteration != MAIN_ITERATION_EDEFAULT;
			case AlgorithmObjectsPackage.LNS__NEW_SOLUTION:
				return newSolution != NEW_SOLUTION_EDEFAULT;
			case AlgorithmObjectsPackage.LNS__ACCEPTANCE_ITERATION:
				return acceptanceIteration != ACCEPTANCE_ITERATION_EDEFAULT;
			case AlgorithmObjectsPackage.LNS__ACCEPTANCES:
				return acceptances != ACCEPTANCES_EDEFAULT;
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
		result.append(" (lowestCostsPerRequest: ");
		result.append(lowestCostsPerRequest);
		result.append(", maxIterations: ");
		result.append(maxIterations);
		result.append(", rejections: ");
		result.append(rejections);
		result.append(", iteration: ");
		result.append(iteration);
		result.append(", exchangeableRequests: ");
		result.append(exchangeableRequests);
		result.append(", mainIteration: ");
		result.append(mainIteration);
		result.append(", newSolution: ");
		result.append(newSolution);
		result.append(", acceptanceIteration: ");
		result.append(acceptanceIteration);
		result.append(", acceptances: ");
		result.append(acceptances);
		result.append(')');
		return result.toString();
	}

} //LNSImpl
