/**
 */
package ovgu.pave.model.algorithmObjects;

import java.util.HashMap;

import org.eclipse.emf.ecore.EObject;

import ovgu.pave.model.input.Request;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>LNS</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.algorithmObjects.LNS#getLowestCostsPerRequest <em>Lowest Costs Per Request</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.LNS#getMaxIterations <em>Max Iterations</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.LNS#getRejections <em>Rejections</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.LNS#getIteration <em>Iteration</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.LNS#getExchangeableRequests <em>Exchangeable Requests</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.LNS#getMainIteration <em>Main Iteration</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.LNS#getNewSolution <em>New Solution</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.LNS#getAcceptanceIteration <em>Acceptance Iteration</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.LNS#getAcceptances <em>Acceptances</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getLNS()
 * @model
 * @generated
 */
public interface LNS extends EObject {
	/**
	 * Returns the value of the '<em><b>Lowest Costs Per Request</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lowest Costs Per Request</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lowest Costs Per Request</em>' attribute.
	 * @see #setLowestCostsPerRequest(HashMap)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getLNS_LowestCostsPerRequest()
	 * @model dataType="ovgu.pave.model.algorithmObjects.LowestCostsHashMap"
	 * @generated
	 */
	HashMap<Request, Long> getLowestCostsPerRequest();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.LNS#getLowestCostsPerRequest <em>Lowest Costs Per Request</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lowest Costs Per Request</em>' attribute.
	 * @see #getLowestCostsPerRequest()
	 * @generated
	 */
	void setLowestCostsPerRequest(HashMap<Request, Long> value);

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
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getLNS_MaxIterations()
	 * @model
	 * @generated
	 */
	int getMaxIterations();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.LNS#getMaxIterations <em>Max Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Iterations</em>' attribute.
	 * @see #getMaxIterations()
	 * @generated
	 */
	void setMaxIterations(int value);

	/**
	 * Returns the value of the '<em><b>Rejections</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rejections</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rejections</em>' attribute.
	 * @see #setRejections(int)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getLNS_Rejections()
	 * @model
	 * @generated
	 */
	int getRejections();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.LNS#getRejections <em>Rejections</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rejections</em>' attribute.
	 * @see #getRejections()
	 * @generated
	 */
	void setRejections(int value);

	/**
	 * Returns the value of the '<em><b>Iteration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Iteration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Iteration</em>' attribute.
	 * @see #setIteration(long)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getLNS_Iteration()
	 * @model
	 * @generated
	 */
	long getIteration();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.LNS#getIteration <em>Iteration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Iteration</em>' attribute.
	 * @see #getIteration()
	 * @generated
	 */
	void setIteration(long value);

	/**
	 * Returns the value of the '<em><b>Exchangeable Requests</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Exchangeable Requests</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exchangeable Requests</em>' attribute.
	 * @see #setExchangeableRequests(long)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getLNS_ExchangeableRequests()
	 * @model
	 * @generated
	 */
	long getExchangeableRequests();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.LNS#getExchangeableRequests <em>Exchangeable Requests</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Exchangeable Requests</em>' attribute.
	 * @see #getExchangeableRequests()
	 * @generated
	 */
	void setExchangeableRequests(long value);

	/**
	 * Returns the value of the '<em><b>Main Iteration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Main Iteration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Main Iteration</em>' attribute.
	 * @see #setMainIteration(long)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getLNS_MainIteration()
	 * @model
	 * @generated
	 */
	long getMainIteration();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.LNS#getMainIteration <em>Main Iteration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Main Iteration</em>' attribute.
	 * @see #getMainIteration()
	 * @generated
	 */
	void setMainIteration(long value);

	/**
	 * Returns the value of the '<em><b>New Solution</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Solution</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Solution</em>' attribute.
	 * @see #setNewSolution(long)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getLNS_NewSolution()
	 * @model
	 * @generated
	 */
	long getNewSolution();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.LNS#getNewSolution <em>New Solution</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Solution</em>' attribute.
	 * @see #getNewSolution()
	 * @generated
	 */
	void setNewSolution(long value);

	/**
	 * Returns the value of the '<em><b>Acceptance Iteration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Acceptance Iteration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Acceptance Iteration</em>' attribute.
	 * @see #setAcceptanceIteration(int)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getLNS_AcceptanceIteration()
	 * @model
	 * @generated
	 */
	int getAcceptanceIteration();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.LNS#getAcceptanceIteration <em>Acceptance Iteration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Acceptance Iteration</em>' attribute.
	 * @see #getAcceptanceIteration()
	 * @generated
	 */
	void setAcceptanceIteration(int value);

	/**
	 * Returns the value of the '<em><b>Acceptances</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Acceptances</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Acceptances</em>' attribute.
	 * @see #setAcceptances(int)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getLNS_Acceptances()
	 * @model
	 * @generated
	 */
	int getAcceptances();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.LNS#getAcceptances <em>Acceptances</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Acceptances</em>' attribute.
	 * @see #getAcceptances()
	 * @generated
	 */
	void setAcceptances(int value);

} // LNS
