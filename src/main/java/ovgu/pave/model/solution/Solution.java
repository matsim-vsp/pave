/**
 */
package ovgu.pave.model.solution;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import ovgu.pave.model.input.Request;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Solution</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.solution.Solution#getRoutes <em>Routes</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.Solution#getScore <em>Score</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.Solution#getUseVehicleType <em>Use Vehicle Type</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.Solution#getUnintegratedRequests <em>Unintegrated Requests</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.Solution#getIntegratedRequests <em>Integrated Requests</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.solution.SolutionPackage#getSolution()
 * @model
 * @generated
 */
public interface Solution extends EObject {
	/**
	 * Returns the value of the '<em><b>Routes</b></em>' containment reference list.
	 * The list contents are of type {@link ovgu.pave.model.solution.Route}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Routes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Routes</em>' containment reference list.
	 * @see ovgu.pave.model.solution.SolutionPackage#getSolution_Routes()
	 * @model containment="true"
	 * @generated
	 */
	EList<Route> getRoutes();

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
	 * @see ovgu.pave.model.solution.SolutionPackage#getSolution_Score()
	 * @model
	 * @generated
	 */
	long getScore();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.solution.Solution#getScore <em>Score</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Score</em>' attribute.
	 * @see #getScore()
	 * @generated
	 */
	void setScore(long value);

	/**
	 * Returns the value of the '<em><b>Use Vehicle Type</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Use Vehicle Type</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Use Vehicle Type</em>' containment reference.
	 * @see #setUseVehicleType(UseVehicleType)
	 * @see ovgu.pave.model.solution.SolutionPackage#getSolution_UseVehicleType()
	 * @model containment="true"
	 * @generated
	 */
	UseVehicleType getUseVehicleType();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.solution.Solution#getUseVehicleType <em>Use Vehicle Type</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Use Vehicle Type</em>' containment reference.
	 * @see #getUseVehicleType()
	 * @generated
	 */
	void setUseVehicleType(UseVehicleType value);

	/**
	 * Returns the value of the '<em><b>Unintegrated Requests</b></em>' attribute list.
	 * The list contents are of type {@link ovgu.pave.model.input.Request}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unintegrated Requests</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unintegrated Requests</em>' attribute list.
	 * @see ovgu.pave.model.solution.SolutionPackage#getSolution_UnintegratedRequests()
	 * @model dataType="ovgu.pave.model.solution.Requests" transient="true"
	 * @generated
	 */
	EList<Request> getUnintegratedRequests();

	/**
	 * Returns the value of the '<em><b>Integrated Requests</b></em>' attribute list.
	 * The list contents are of type {@link ovgu.pave.model.input.Request}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Integrated Requests</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Integrated Requests</em>' attribute list.
	 * @see ovgu.pave.model.solution.SolutionPackage#getSolution_IntegratedRequests()
	 * @model dataType="ovgu.pave.model.solution.Requests" transient="true"
	 * @generated
	 */
	EList<Request> getIntegratedRequests();

} // Solution
