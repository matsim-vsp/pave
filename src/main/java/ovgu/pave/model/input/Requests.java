/**
 */
package ovgu.pave.model.input;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Requests</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.input.Requests#getAccepted <em>Accepted</em>}</li>
 *   <li>{@link ovgu.pave.model.input.Requests#getRejected <em>Rejected</em>}</li>
 *   <li>{@link ovgu.pave.model.input.Requests#getNew <em>New</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.input.InputPackage#getRequests()
 * @model
 * @generated
 */
public interface Requests extends EObject {
	/**
	 * Returns the value of the '<em><b>Accepted</b></em>' containment reference list.
	 * The list contents are of type {@link ovgu.pave.model.input.Request}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Accepted</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Accepted</em>' containment reference list.
	 * @see ovgu.pave.model.input.InputPackage#getRequests_Accepted()
	 * @model containment="true"
	 * @generated
	 */
	EList<Request> getAccepted();

	/**
	 * Returns the value of the '<em><b>Rejected</b></em>' containment reference list.
	 * The list contents are of type {@link ovgu.pave.model.input.Request}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rejected</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rejected</em>' containment reference list.
	 * @see ovgu.pave.model.input.InputPackage#getRequests_Rejected()
	 * @model containment="true"
	 * @generated
	 */
	EList<Request> getRejected();

	/**
	 * Returns the value of the '<em><b>New</b></em>' containment reference list.
	 * The list contents are of type {@link ovgu.pave.model.input.Request}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New</em>' containment reference list.
	 * @see ovgu.pave.model.input.InputPackage#getRequests_New()
	 * @model containment="true"
	 * @generated
	 */
	EList<Request> getNew();

} // Requests
