/**
 */
package ovgu.pave.model.config;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>LNS</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.config.LNS#getPenaltyTerm <em>Penalty Term</em>}</li>
 *   <li>{@link ovgu.pave.model.config.LNS#getTemperatureControlParameter <em>Temperature Control Parameter</em>}</li>
 *   <li>{@link ovgu.pave.model.config.LNS#getCoolingRate <em>Cooling Rate</em>}</li>
 *   <li>{@link ovgu.pave.model.config.LNS#getMinSmallRequestSet <em>Min Small Request Set</em>}</li>
 *   <li>{@link ovgu.pave.model.config.LNS#getMaxLargeRequestSet <em>Max Large Request Set</em>}</li>
 *   <li>{@link ovgu.pave.model.config.LNS#getMaxSmallRequestSet <em>Max Small Request Set</em>}</li>
 *   <li>{@link ovgu.pave.model.config.LNS#getMinLargeRequestSet <em>Min Large Request Set</em>}</li>
 *   <li>{@link ovgu.pave.model.config.LNS#getMaxIterations <em>Max Iterations</em>}</li>
 *   <li>{@link ovgu.pave.model.config.LNS#getMaxCalculationTime <em>Max Calculation Time</em>}</li>
 *   <li>{@link ovgu.pave.model.config.LNS#getInsertionHeuristics <em>Insertion Heuristics</em>}</li>
 *   <li>{@link ovgu.pave.model.config.LNS#getRemovalHeuristics <em>Removal Heuristics</em>}</li>
 *   <li>{@link ovgu.pave.model.config.LNS#getRws <em>Rws</em>}</li>
 *   <li>{@link ovgu.pave.model.config.LNS#getClusterPercentagePerRoute <em>Cluster Percentage Per Route</em>}</li>
 *   <li>{@link ovgu.pave.model.config.LNS#getShawDistanceWeight <em>Shaw Distance Weight</em>}</li>
 *   <li>{@link ovgu.pave.model.config.LNS#getShawBeginWeight <em>Shaw Begin Weight</em>}</li>
 *   <li>{@link ovgu.pave.model.config.LNS#getMaxWithoutImprovement <em>Max Without Improvement</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.config.ConfigPackage#getLNS()
 * @model
 * @generated
 */
public interface LNS extends EObject {
	/**
	 * Returns the value of the '<em><b>Penalty Term</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Penalty Term</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Penalty Term</em>' attribute.
	 * @see #setPenaltyTerm(int)
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_PenaltyTerm()
	 * @model
	 * @generated
	 */
	int getPenaltyTerm();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.LNS#getPenaltyTerm <em>Penalty Term</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Penalty Term</em>' attribute.
	 * @see #getPenaltyTerm()
	 * @generated
	 */
	void setPenaltyTerm(int value);

	/**
	 * Returns the value of the '<em><b>Temperature Control Parameter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Temperature Control Parameter</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Temperature Control Parameter</em>' attribute.
	 * @see #setTemperatureControlParameter(double)
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_TemperatureControlParameter()
	 * @model
	 * @generated
	 */
	double getTemperatureControlParameter();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.LNS#getTemperatureControlParameter <em>Temperature Control Parameter</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Temperature Control Parameter</em>' attribute.
	 * @see #getTemperatureControlParameter()
	 * @generated
	 */
	void setTemperatureControlParameter(double value);

	/**
	 * Returns the value of the '<em><b>Cooling Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cooling Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cooling Rate</em>' attribute.
	 * @see #setCoolingRate(double)
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_CoolingRate()
	 * @model
	 * @generated
	 */
	double getCoolingRate();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.LNS#getCoolingRate <em>Cooling Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cooling Rate</em>' attribute.
	 * @see #getCoolingRate()
	 * @generated
	 */
	void setCoolingRate(double value);

	/**
	 * Returns the value of the '<em><b>Min Small Request Set</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Small Request Set</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Small Request Set</em>' attribute.
	 * @see #setMinSmallRequestSet(double)
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_MinSmallRequestSet()
	 * @model
	 * @generated
	 */
	double getMinSmallRequestSet();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.LNS#getMinSmallRequestSet <em>Min Small Request Set</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Small Request Set</em>' attribute.
	 * @see #getMinSmallRequestSet()
	 * @generated
	 */
	void setMinSmallRequestSet(double value);

	/**
	 * Returns the value of the '<em><b>Max Large Request Set</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Large Request Set</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Large Request Set</em>' attribute.
	 * @see #setMaxLargeRequestSet(int)
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_MaxLargeRequestSet()
	 * @model
	 * @generated
	 */
	int getMaxLargeRequestSet();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.LNS#getMaxLargeRequestSet <em>Max Large Request Set</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Large Request Set</em>' attribute.
	 * @see #getMaxLargeRequestSet()
	 * @generated
	 */
	void setMaxLargeRequestSet(int value);

	/**
	 * Returns the value of the '<em><b>Max Small Request Set</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Small Request Set</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Small Request Set</em>' attribute.
	 * @see #setMaxSmallRequestSet(double)
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_MaxSmallRequestSet()
	 * @model
	 * @generated
	 */
	double getMaxSmallRequestSet();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.LNS#getMaxSmallRequestSet <em>Max Small Request Set</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Small Request Set</em>' attribute.
	 * @see #getMaxSmallRequestSet()
	 * @generated
	 */
	void setMaxSmallRequestSet(double value);

	/**
	 * Returns the value of the '<em><b>Min Large Request Set</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Large Request Set</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Large Request Set</em>' attribute.
	 * @see #setMinLargeRequestSet(int)
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_MinLargeRequestSet()
	 * @model
	 * @generated
	 */
	int getMinLargeRequestSet();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.LNS#getMinLargeRequestSet <em>Min Large Request Set</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Large Request Set</em>' attribute.
	 * @see #getMinLargeRequestSet()
	 * @generated
	 */
	void setMinLargeRequestSet(int value);

	/**
	 * Returns the value of the '<em><b>Max Iterations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Iterations</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Iterations</em>' attribute.
	 * @see #setMaxIterations(int)
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_MaxIterations()
	 * @model
	 * @generated
	 */
	int getMaxIterations();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.LNS#getMaxIterations <em>Max Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Iterations</em>' attribute.
	 * @see #getMaxIterations()
	 * @generated
	 */
	void setMaxIterations(int value);

	/**
	 * Returns the value of the '<em><b>Max Calculation Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Calculation Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Calculation Time</em>' attribute.
	 * @see #setMaxCalculationTime(long)
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_MaxCalculationTime()
	 * @model
	 * @generated
	 */
	long getMaxCalculationTime();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.LNS#getMaxCalculationTime <em>Max Calculation Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Calculation Time</em>' attribute.
	 * @see #getMaxCalculationTime()
	 * @generated
	 */
	void setMaxCalculationTime(long value);

	/**
	 * Returns the value of the '<em><b>Insertion Heuristics</b></em>' containment reference list.
	 * The list contents are of type {@link ovgu.pave.model.config.Heuristic}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Insertion Heuristics</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Insertion Heuristics</em>' containment reference list.
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_InsertionHeuristics()
	 * @model containment="true"
	 * @generated
	 */
	EList<Heuristic> getInsertionHeuristics();

	/**
	 * Returns the value of the '<em><b>Removal Heuristics</b></em>' containment reference list.
	 * The list contents are of type {@link ovgu.pave.model.config.Heuristic}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Removal Heuristics</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Removal Heuristics</em>' containment reference list.
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_RemovalHeuristics()
	 * @model containment="true"
	 * @generated
	 */
	EList<Heuristic> getRemovalHeuristics();

	/**
	 * Returns the value of the '<em><b>Rws</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rws</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rws</em>' containment reference.
	 * @see #setRws(RWS)
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_Rws()
	 * @model containment="true" required="true"
	 * @generated
	 */
	RWS getRws();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.LNS#getRws <em>Rws</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rws</em>' containment reference.
	 * @see #getRws()
	 * @generated
	 */
	void setRws(RWS value);

	/**
	 * Returns the value of the '<em><b>Cluster Percentage Per Route</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cluster Percentage Per Route</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cluster Percentage Per Route</em>' attribute.
	 * @see #setClusterPercentagePerRoute(double)
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_ClusterPercentagePerRoute()
	 * @model
	 * @generated
	 */
	double getClusterPercentagePerRoute();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.LNS#getClusterPercentagePerRoute <em>Cluster Percentage Per Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cluster Percentage Per Route</em>' attribute.
	 * @see #getClusterPercentagePerRoute()
	 * @generated
	 */
	void setClusterPercentagePerRoute(double value);

	/**
	 * Returns the value of the '<em><b>Shaw Distance Weight</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shaw Distance Weight</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shaw Distance Weight</em>' attribute.
	 * @see #setShawDistanceWeight(double)
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_ShawDistanceWeight()
	 * @model default="1"
	 * @generated
	 */
	double getShawDistanceWeight();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.LNS#getShawDistanceWeight <em>Shaw Distance Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shaw Distance Weight</em>' attribute.
	 * @see #getShawDistanceWeight()
	 * @generated
	 */
	void setShawDistanceWeight(double value);

	/**
	 * Returns the value of the '<em><b>Shaw Begin Weight</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shaw Begin Weight</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shaw Begin Weight</em>' attribute.
	 * @see #setShawBeginWeight(double)
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_ShawBeginWeight()
	 * @model default="1"
	 * @generated
	 */
	double getShawBeginWeight();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.LNS#getShawBeginWeight <em>Shaw Begin Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shaw Begin Weight</em>' attribute.
	 * @see #getShawBeginWeight()
	 * @generated
	 */
	void setShawBeginWeight(double value);

	/**
	 * Returns the value of the '<em><b>Max Without Improvement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Without Improvement</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Without Improvement</em>' attribute.
	 * @see #setMaxWithoutImprovement(int)
	 * @see ovgu.pave.model.config.ConfigPackage#getLNS_MaxWithoutImprovement()
	 * @model
	 * @generated
	 */
	int getMaxWithoutImprovement();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.LNS#getMaxWithoutImprovement <em>Max Without Improvement</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Without Improvement</em>' attribute.
	 * @see #getMaxWithoutImprovement()
	 * @generated
	 */
	void setMaxWithoutImprovement(int value);

} // LNS
