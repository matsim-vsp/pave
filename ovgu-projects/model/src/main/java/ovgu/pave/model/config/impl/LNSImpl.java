/**
 */
package ovgu.pave.model.config.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import ovgu.pave.model.config.ConfigPackage;
import ovgu.pave.model.config.Heuristic;
import ovgu.pave.model.config.LNS;
import ovgu.pave.model.config.RWS;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>LNS</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getPenaltyTerm <em>Penalty Term</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getTemperatureControlParameter <em>Temperature Control Parameter</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getCoolingRate <em>Cooling Rate</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getMinSmallRequestSet <em>Min Small Request Set</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getMaxLargeRequestSet <em>Max Large Request Set</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getMaxSmallRequestSet <em>Max Small Request Set</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getMinLargeRequestSet <em>Min Large Request Set</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getMaxIterations <em>Max Iterations</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getMaxCalculationTime <em>Max Calculation Time</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getInsertionHeuristics <em>Insertion Heuristics</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getRemovalHeuristics <em>Removal Heuristics</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getRws <em>Rws</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getClusterPercentagePerRoute <em>Cluster Percentage Per Route</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getShawDistanceWeight <em>Shaw Distance Weight</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getShawBeginWeight <em>Shaw Begin Weight</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.LNSImpl#getMaxWithoutImprovement <em>Max Without Improvement</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LNSImpl extends MinimalEObjectImpl.Container implements LNS {
	/**
	 * The default value of the '{@link #getPenaltyTerm() <em>Penalty Term</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPenaltyTerm()
	 * @generated
	 * @ordered
	 */
	protected static final int PENALTY_TERM_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPenaltyTerm() <em>Penalty Term</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPenaltyTerm()
	 * @generated
	 * @ordered
	 */
	protected int penaltyTerm = PENALTY_TERM_EDEFAULT;

	/**
	 * The default value of the '{@link #getTemperatureControlParameter() <em>Temperature Control Parameter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemperatureControlParameter()
	 * @generated
	 * @ordered
	 */
	protected static final double TEMPERATURE_CONTROL_PARAMETER_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getTemperatureControlParameter() <em>Temperature Control Parameter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTemperatureControlParameter()
	 * @generated
	 * @ordered
	 */
	protected double temperatureControlParameter = TEMPERATURE_CONTROL_PARAMETER_EDEFAULT;

	/**
	 * The default value of the '{@link #getCoolingRate() <em>Cooling Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCoolingRate()
	 * @generated
	 * @ordered
	 */
	protected static final double COOLING_RATE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCoolingRate() <em>Cooling Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCoolingRate()
	 * @generated
	 * @ordered
	 */
	protected double coolingRate = COOLING_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinSmallRequestSet() <em>Min Small Request Set</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinSmallRequestSet()
	 * @generated
	 * @ordered
	 */
	protected static final double MIN_SMALL_REQUEST_SET_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMinSmallRequestSet() <em>Min Small Request Set</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinSmallRequestSet()
	 * @generated
	 * @ordered
	 */
	protected double minSmallRequestSet = MIN_SMALL_REQUEST_SET_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxLargeRequestSet() <em>Max Large Request Set</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxLargeRequestSet()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_LARGE_REQUEST_SET_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxLargeRequestSet() <em>Max Large Request Set</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxLargeRequestSet()
	 * @generated
	 * @ordered
	 */
	protected int maxLargeRequestSet = MAX_LARGE_REQUEST_SET_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxSmallRequestSet() <em>Max Small Request Set</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxSmallRequestSet()
	 * @generated
	 * @ordered
	 */
	protected static final double MAX_SMALL_REQUEST_SET_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getMaxSmallRequestSet() <em>Max Small Request Set</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxSmallRequestSet()
	 * @generated
	 * @ordered
	 */
	protected double maxSmallRequestSet = MAX_SMALL_REQUEST_SET_EDEFAULT;

	/**
	 * The default value of the '{@link #getMinLargeRequestSet() <em>Min Large Request Set</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinLargeRequestSet()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_LARGE_REQUEST_SET_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMinLargeRequestSet() <em>Min Large Request Set</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinLargeRequestSet()
	 * @generated
	 * @ordered
	 */
	protected int minLargeRequestSet = MIN_LARGE_REQUEST_SET_EDEFAULT;

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
	 * The default value of the '{@link #getMaxCalculationTime() <em>Max Calculation Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxCalculationTime()
	 * @generated
	 * @ordered
	 */
	protected static final long MAX_CALCULATION_TIME_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getMaxCalculationTime() <em>Max Calculation Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxCalculationTime()
	 * @generated
	 * @ordered
	 */
	protected long maxCalculationTime = MAX_CALCULATION_TIME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInsertionHeuristics() <em>Insertion Heuristics</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInsertionHeuristics()
	 * @generated
	 * @ordered
	 */
	protected EList<Heuristic> insertionHeuristics;

	/**
	 * The cached value of the '{@link #getRemovalHeuristics() <em>Removal Heuristics</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRemovalHeuristics()
	 * @generated
	 * @ordered
	 */
	protected EList<Heuristic> removalHeuristics;

	/**
	 * The cached value of the '{@link #getRws() <em>Rws</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRws()
	 * @generated
	 * @ordered
	 */
	protected RWS rws;

	/**
	 * The default value of the '{@link #getClusterPercentagePerRoute() <em>Cluster Percentage Per Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClusterPercentagePerRoute()
	 * @generated
	 * @ordered
	 */
	protected static final double CLUSTER_PERCENTAGE_PER_ROUTE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getClusterPercentagePerRoute() <em>Cluster Percentage Per Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClusterPercentagePerRoute()
	 * @generated
	 * @ordered
	 */
	protected double clusterPercentagePerRoute = CLUSTER_PERCENTAGE_PER_ROUTE_EDEFAULT;

	/**
	 * The default value of the '{@link #getShawDistanceWeight() <em>Shaw Distance Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShawDistanceWeight()
	 * @generated
	 * @ordered
	 */
	protected static final double SHAW_DISTANCE_WEIGHT_EDEFAULT = 1.0;

	/**
	 * The cached value of the '{@link #getShawDistanceWeight() <em>Shaw Distance Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShawDistanceWeight()
	 * @generated
	 * @ordered
	 */
	protected double shawDistanceWeight = SHAW_DISTANCE_WEIGHT_EDEFAULT;

	/**
	 * The default value of the '{@link #getShawBeginWeight() <em>Shaw Begin Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShawBeginWeight()
	 * @generated
	 * @ordered
	 */
	protected static final double SHAW_BEGIN_WEIGHT_EDEFAULT = 1.0;

	/**
	 * The cached value of the '{@link #getShawBeginWeight() <em>Shaw Begin Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShawBeginWeight()
	 * @generated
	 * @ordered
	 */
	protected double shawBeginWeight = SHAW_BEGIN_WEIGHT_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxWithoutImprovement() <em>Max Without Improvement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxWithoutImprovement()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_WITHOUT_IMPROVEMENT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMaxWithoutImprovement() <em>Max Without Improvement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxWithoutImprovement()
	 * @generated
	 * @ordered
	 */
	protected int maxWithoutImprovement = MAX_WITHOUT_IMPROVEMENT_EDEFAULT;

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
		return ConfigPackage.Literals.LNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getPenaltyTerm() {
		return penaltyTerm;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPenaltyTerm(int newPenaltyTerm) {
		int oldPenaltyTerm = penaltyTerm;
		penaltyTerm = newPenaltyTerm;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.LNS__PENALTY_TERM, oldPenaltyTerm, penaltyTerm));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getTemperatureControlParameter() {
		return temperatureControlParameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTemperatureControlParameter(double newTemperatureControlParameter) {
		double oldTemperatureControlParameter = temperatureControlParameter;
		temperatureControlParameter = newTemperatureControlParameter;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.LNS__TEMPERATURE_CONTROL_PARAMETER, oldTemperatureControlParameter, temperatureControlParameter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getCoolingRate() {
		return coolingRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCoolingRate(double newCoolingRate) {
		double oldCoolingRate = coolingRate;
		coolingRate = newCoolingRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.LNS__COOLING_RATE, oldCoolingRate, coolingRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMinSmallRequestSet() {
		return minSmallRequestSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinSmallRequestSet(double newMinSmallRequestSet) {
		double oldMinSmallRequestSet = minSmallRequestSet;
		minSmallRequestSet = newMinSmallRequestSet;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.LNS__MIN_SMALL_REQUEST_SET, oldMinSmallRequestSet, minSmallRequestSet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaxLargeRequestSet() {
		return maxLargeRequestSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxLargeRequestSet(int newMaxLargeRequestSet) {
		int oldMaxLargeRequestSet = maxLargeRequestSet;
		maxLargeRequestSet = newMaxLargeRequestSet;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.LNS__MAX_LARGE_REQUEST_SET, oldMaxLargeRequestSet, maxLargeRequestSet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getMaxSmallRequestSet() {
		return maxSmallRequestSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxSmallRequestSet(double newMaxSmallRequestSet) {
		double oldMaxSmallRequestSet = maxSmallRequestSet;
		maxSmallRequestSet = newMaxSmallRequestSet;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.LNS__MAX_SMALL_REQUEST_SET, oldMaxSmallRequestSet, maxSmallRequestSet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMinLargeRequestSet() {
		return minLargeRequestSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinLargeRequestSet(int newMinLargeRequestSet) {
		int oldMinLargeRequestSet = minLargeRequestSet;
		minLargeRequestSet = newMinLargeRequestSet;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.LNS__MIN_LARGE_REQUEST_SET, oldMinLargeRequestSet, minLargeRequestSet));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.LNS__MAX_ITERATIONS, oldMaxIterations, maxIterations));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getMaxCalculationTime() {
		return maxCalculationTime;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxCalculationTime(long newMaxCalculationTime) {
		long oldMaxCalculationTime = maxCalculationTime;
		maxCalculationTime = newMaxCalculationTime;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.LNS__MAX_CALCULATION_TIME, oldMaxCalculationTime, maxCalculationTime));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Heuristic> getInsertionHeuristics() {
		if (insertionHeuristics == null) {
			insertionHeuristics = new EObjectContainmentEList<Heuristic>(Heuristic.class, this, ConfigPackage.LNS__INSERTION_HEURISTICS);
		}
		return insertionHeuristics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Heuristic> getRemovalHeuristics() {
		if (removalHeuristics == null) {
			removalHeuristics = new EObjectContainmentEList<Heuristic>(Heuristic.class, this, ConfigPackage.LNS__REMOVAL_HEURISTICS);
		}
		return removalHeuristics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RWS getRws() {
		return rws;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRws(RWS newRws, NotificationChain msgs) {
		RWS oldRws = rws;
		rws = newRws;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigPackage.LNS__RWS, oldRws, newRws);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRws(RWS newRws) {
		if (newRws != rws) {
			NotificationChain msgs = null;
			if (rws != null)
				msgs = ((InternalEObject)rws).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigPackage.LNS__RWS, null, msgs);
			if (newRws != null)
				msgs = ((InternalEObject)newRws).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigPackage.LNS__RWS, null, msgs);
			msgs = basicSetRws(newRws, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.LNS__RWS, newRws, newRws));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getClusterPercentagePerRoute() {
		return clusterPercentagePerRoute;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setClusterPercentagePerRoute(double newClusterPercentagePerRoute) {
		double oldClusterPercentagePerRoute = clusterPercentagePerRoute;
		clusterPercentagePerRoute = newClusterPercentagePerRoute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.LNS__CLUSTER_PERCENTAGE_PER_ROUTE, oldClusterPercentagePerRoute, clusterPercentagePerRoute));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getShawDistanceWeight() {
		return shawDistanceWeight;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShawDistanceWeight(double newShawDistanceWeight) {
		double oldShawDistanceWeight = shawDistanceWeight;
		shawDistanceWeight = newShawDistanceWeight;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.LNS__SHAW_DISTANCE_WEIGHT, oldShawDistanceWeight, shawDistanceWeight));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getShawBeginWeight() {
		return shawBeginWeight;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShawBeginWeight(double newShawBeginWeight) {
		double oldShawBeginWeight = shawBeginWeight;
		shawBeginWeight = newShawBeginWeight;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.LNS__SHAW_BEGIN_WEIGHT, oldShawBeginWeight, shawBeginWeight));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaxWithoutImprovement() {
		return maxWithoutImprovement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxWithoutImprovement(int newMaxWithoutImprovement) {
		int oldMaxWithoutImprovement = maxWithoutImprovement;
		maxWithoutImprovement = newMaxWithoutImprovement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.LNS__MAX_WITHOUT_IMPROVEMENT, oldMaxWithoutImprovement, maxWithoutImprovement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigPackage.LNS__INSERTION_HEURISTICS:
				return ((InternalEList<?>)getInsertionHeuristics()).basicRemove(otherEnd, msgs);
			case ConfigPackage.LNS__REMOVAL_HEURISTICS:
				return ((InternalEList<?>)getRemovalHeuristics()).basicRemove(otherEnd, msgs);
			case ConfigPackage.LNS__RWS:
				return basicSetRws(null, msgs);
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
			case ConfigPackage.LNS__PENALTY_TERM:
				return getPenaltyTerm();
			case ConfigPackage.LNS__TEMPERATURE_CONTROL_PARAMETER:
				return getTemperatureControlParameter();
			case ConfigPackage.LNS__COOLING_RATE:
				return getCoolingRate();
			case ConfigPackage.LNS__MIN_SMALL_REQUEST_SET:
				return getMinSmallRequestSet();
			case ConfigPackage.LNS__MAX_LARGE_REQUEST_SET:
				return getMaxLargeRequestSet();
			case ConfigPackage.LNS__MAX_SMALL_REQUEST_SET:
				return getMaxSmallRequestSet();
			case ConfigPackage.LNS__MIN_LARGE_REQUEST_SET:
				return getMinLargeRequestSet();
			case ConfigPackage.LNS__MAX_ITERATIONS:
				return getMaxIterations();
			case ConfigPackage.LNS__MAX_CALCULATION_TIME:
				return getMaxCalculationTime();
			case ConfigPackage.LNS__INSERTION_HEURISTICS:
				return getInsertionHeuristics();
			case ConfigPackage.LNS__REMOVAL_HEURISTICS:
				return getRemovalHeuristics();
			case ConfigPackage.LNS__RWS:
				return getRws();
			case ConfigPackage.LNS__CLUSTER_PERCENTAGE_PER_ROUTE:
				return getClusterPercentagePerRoute();
			case ConfigPackage.LNS__SHAW_DISTANCE_WEIGHT:
				return getShawDistanceWeight();
			case ConfigPackage.LNS__SHAW_BEGIN_WEIGHT:
				return getShawBeginWeight();
			case ConfigPackage.LNS__MAX_WITHOUT_IMPROVEMENT:
				return getMaxWithoutImprovement();
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
			case ConfigPackage.LNS__PENALTY_TERM:
				setPenaltyTerm((Integer)newValue);
				return;
			case ConfigPackage.LNS__TEMPERATURE_CONTROL_PARAMETER:
				setTemperatureControlParameter((Double)newValue);
				return;
			case ConfigPackage.LNS__COOLING_RATE:
				setCoolingRate((Double)newValue);
				return;
			case ConfigPackage.LNS__MIN_SMALL_REQUEST_SET:
				setMinSmallRequestSet((Double)newValue);
				return;
			case ConfigPackage.LNS__MAX_LARGE_REQUEST_SET:
				setMaxLargeRequestSet((Integer)newValue);
				return;
			case ConfigPackage.LNS__MAX_SMALL_REQUEST_SET:
				setMaxSmallRequestSet((Double)newValue);
				return;
			case ConfigPackage.LNS__MIN_LARGE_REQUEST_SET:
				setMinLargeRequestSet((Integer)newValue);
				return;
			case ConfigPackage.LNS__MAX_ITERATIONS:
				setMaxIterations((Integer)newValue);
				return;
			case ConfigPackage.LNS__MAX_CALCULATION_TIME:
				setMaxCalculationTime((Long)newValue);
				return;
			case ConfigPackage.LNS__INSERTION_HEURISTICS:
				getInsertionHeuristics().clear();
				getInsertionHeuristics().addAll((Collection<? extends Heuristic>)newValue);
				return;
			case ConfigPackage.LNS__REMOVAL_HEURISTICS:
				getRemovalHeuristics().clear();
				getRemovalHeuristics().addAll((Collection<? extends Heuristic>)newValue);
				return;
			case ConfigPackage.LNS__RWS:
				setRws((RWS)newValue);
				return;
			case ConfigPackage.LNS__CLUSTER_PERCENTAGE_PER_ROUTE:
				setClusterPercentagePerRoute((Double)newValue);
				return;
			case ConfigPackage.LNS__SHAW_DISTANCE_WEIGHT:
				setShawDistanceWeight((Double)newValue);
				return;
			case ConfigPackage.LNS__SHAW_BEGIN_WEIGHT:
				setShawBeginWeight((Double)newValue);
				return;
			case ConfigPackage.LNS__MAX_WITHOUT_IMPROVEMENT:
				setMaxWithoutImprovement((Integer)newValue);
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
			case ConfigPackage.LNS__PENALTY_TERM:
				setPenaltyTerm(PENALTY_TERM_EDEFAULT);
				return;
			case ConfigPackage.LNS__TEMPERATURE_CONTROL_PARAMETER:
				setTemperatureControlParameter(TEMPERATURE_CONTROL_PARAMETER_EDEFAULT);
				return;
			case ConfigPackage.LNS__COOLING_RATE:
				setCoolingRate(COOLING_RATE_EDEFAULT);
				return;
			case ConfigPackage.LNS__MIN_SMALL_REQUEST_SET:
				setMinSmallRequestSet(MIN_SMALL_REQUEST_SET_EDEFAULT);
				return;
			case ConfigPackage.LNS__MAX_LARGE_REQUEST_SET:
				setMaxLargeRequestSet(MAX_LARGE_REQUEST_SET_EDEFAULT);
				return;
			case ConfigPackage.LNS__MAX_SMALL_REQUEST_SET:
				setMaxSmallRequestSet(MAX_SMALL_REQUEST_SET_EDEFAULT);
				return;
			case ConfigPackage.LNS__MIN_LARGE_REQUEST_SET:
				setMinLargeRequestSet(MIN_LARGE_REQUEST_SET_EDEFAULT);
				return;
			case ConfigPackage.LNS__MAX_ITERATIONS:
				setMaxIterations(MAX_ITERATIONS_EDEFAULT);
				return;
			case ConfigPackage.LNS__MAX_CALCULATION_TIME:
				setMaxCalculationTime(MAX_CALCULATION_TIME_EDEFAULT);
				return;
			case ConfigPackage.LNS__INSERTION_HEURISTICS:
				getInsertionHeuristics().clear();
				return;
			case ConfigPackage.LNS__REMOVAL_HEURISTICS:
				getRemovalHeuristics().clear();
				return;
			case ConfigPackage.LNS__RWS:
				setRws((RWS)null);
				return;
			case ConfigPackage.LNS__CLUSTER_PERCENTAGE_PER_ROUTE:
				setClusterPercentagePerRoute(CLUSTER_PERCENTAGE_PER_ROUTE_EDEFAULT);
				return;
			case ConfigPackage.LNS__SHAW_DISTANCE_WEIGHT:
				setShawDistanceWeight(SHAW_DISTANCE_WEIGHT_EDEFAULT);
				return;
			case ConfigPackage.LNS__SHAW_BEGIN_WEIGHT:
				setShawBeginWeight(SHAW_BEGIN_WEIGHT_EDEFAULT);
				return;
			case ConfigPackage.LNS__MAX_WITHOUT_IMPROVEMENT:
				setMaxWithoutImprovement(MAX_WITHOUT_IMPROVEMENT_EDEFAULT);
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
			case ConfigPackage.LNS__PENALTY_TERM:
				return penaltyTerm != PENALTY_TERM_EDEFAULT;
			case ConfigPackage.LNS__TEMPERATURE_CONTROL_PARAMETER:
				return temperatureControlParameter != TEMPERATURE_CONTROL_PARAMETER_EDEFAULT;
			case ConfigPackage.LNS__COOLING_RATE:
				return coolingRate != COOLING_RATE_EDEFAULT;
			case ConfigPackage.LNS__MIN_SMALL_REQUEST_SET:
				return minSmallRequestSet != MIN_SMALL_REQUEST_SET_EDEFAULT;
			case ConfigPackage.LNS__MAX_LARGE_REQUEST_SET:
				return maxLargeRequestSet != MAX_LARGE_REQUEST_SET_EDEFAULT;
			case ConfigPackage.LNS__MAX_SMALL_REQUEST_SET:
				return maxSmallRequestSet != MAX_SMALL_REQUEST_SET_EDEFAULT;
			case ConfigPackage.LNS__MIN_LARGE_REQUEST_SET:
				return minLargeRequestSet != MIN_LARGE_REQUEST_SET_EDEFAULT;
			case ConfigPackage.LNS__MAX_ITERATIONS:
				return maxIterations != MAX_ITERATIONS_EDEFAULT;
			case ConfigPackage.LNS__MAX_CALCULATION_TIME:
				return maxCalculationTime != MAX_CALCULATION_TIME_EDEFAULT;
			case ConfigPackage.LNS__INSERTION_HEURISTICS:
				return insertionHeuristics != null && !insertionHeuristics.isEmpty();
			case ConfigPackage.LNS__REMOVAL_HEURISTICS:
				return removalHeuristics != null && !removalHeuristics.isEmpty();
			case ConfigPackage.LNS__RWS:
				return rws != null;
			case ConfigPackage.LNS__CLUSTER_PERCENTAGE_PER_ROUTE:
				return clusterPercentagePerRoute != CLUSTER_PERCENTAGE_PER_ROUTE_EDEFAULT;
			case ConfigPackage.LNS__SHAW_DISTANCE_WEIGHT:
				return shawDistanceWeight != SHAW_DISTANCE_WEIGHT_EDEFAULT;
			case ConfigPackage.LNS__SHAW_BEGIN_WEIGHT:
				return shawBeginWeight != SHAW_BEGIN_WEIGHT_EDEFAULT;
			case ConfigPackage.LNS__MAX_WITHOUT_IMPROVEMENT:
				return maxWithoutImprovement != MAX_WITHOUT_IMPROVEMENT_EDEFAULT;
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
		result.append(" (penaltyTerm: ");
		result.append(penaltyTerm);
		result.append(", temperatureControlParameter: ");
		result.append(temperatureControlParameter);
		result.append(", coolingRate: ");
		result.append(coolingRate);
		result.append(", minSmallRequestSet: ");
		result.append(minSmallRequestSet);
		result.append(", maxLargeRequestSet: ");
		result.append(maxLargeRequestSet);
		result.append(", maxSmallRequestSet: ");
		result.append(maxSmallRequestSet);
		result.append(", minLargeRequestSet: ");
		result.append(minLargeRequestSet);
		result.append(", maxIterations: ");
		result.append(maxIterations);
		result.append(", maxCalculationTime: ");
		result.append(maxCalculationTime);
		result.append(", clusterPercentagePerRoute: ");
		result.append(clusterPercentagePerRoute);
		result.append(", shawDistanceWeight: ");
		result.append(shawDistanceWeight);
		result.append(", shawBeginWeight: ");
		result.append(shawBeginWeight);
		result.append(", maxWithoutImprovement: ");
		result.append(maxWithoutImprovement);
		result.append(')');
		return result.toString();
	}

} //LNSImpl
