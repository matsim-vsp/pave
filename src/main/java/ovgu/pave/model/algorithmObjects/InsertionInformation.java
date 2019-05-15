/**
 */
package ovgu.pave.model.algorithmObjects;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import ovgu.pave.model.input.Request;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Insertion Information</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.algorithmObjects.InsertionInformation#getInsertionOptions <em>Insertion Options</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.InsertionInformation#getRequest <em>Request</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.InsertionInformation#getScore <em>Score</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getInsertionInformation()
 * @model
 * @generated
 */
public interface InsertionInformation extends EObject {
	/**
	 * Returns the value of the '<em><b>Insertion Options</b></em>' reference list.
	 * The list contents are of type {@link ovgu.pave.model.algorithmObjects.InsertionOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Insertion Options</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Insertion Options</em>' reference list.
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getInsertionInformation_InsertionOptions()
	 * @model
	 * @generated
	 */
	EList<InsertionOption> getInsertionOptions();

	/**
	 * Returns the value of the '<em><b>Request</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Request</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Request</em>' attribute.
	 * @see #setRequest(Request)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getInsertionInformation_Request()
	 * @model dataType="ovgu.pave.model.algorithmObjects.Requests" required="true"
	 * @generated
	 */
	Request getRequest();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.InsertionInformation#getRequest <em>Request</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Request</em>' attribute.
	 * @see #getRequest()
	 * @generated
	 */
	void setRequest(Request value);

	/**
	 * Returns the value of the '<em><b>Score</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Score</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Score</em>' attribute.
	 * @see #setScore(long)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getInsertionInformation_Score()
	 * @model
	 * @generated
	 */
	long getScore();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.InsertionInformation#getScore <em>Score</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Score</em>' attribute.
	 * @see #getScore()
	 * @generated
	 */
	void setScore(long value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setScoreForRegretHeuristic(int regretValue);

} // InsertionInformation
