/**
 */
package ovgu.pave.model.input;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import ovgu.pave.model.solution.Solution;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.input.Input#getRequests <em>Requests</em>}</li>
 *   <li>{@link ovgu.pave.model.input.Input#getVehicles <em>Vehicles</em>}</li>
 *   <li>{@link ovgu.pave.model.input.Input#getVehicleTypes <em>Vehicle Types</em>}</li>
 *   <li>{@link ovgu.pave.model.input.Input#getLocations <em>Locations</em>}</li>
 *   <li>{@link ovgu.pave.model.input.Input#getEdges <em>Edges</em>}</li>
 *   <li>{@link ovgu.pave.model.input.Input#getSolution <em>Solution</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.input.InputPackage#getInput()
 * @model
 * @generated
 */
public interface Input extends EObject {
	/**
	 * Returns the value of the '<em><b>Requests</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Requests</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Requests</em>' containment reference.
	 * @see #setRequests(Requests)
	 * @see ovgu.pave.model.input.InputPackage#getInput_Requests()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Requests getRequests();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.Input#getRequests <em>Requests</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Requests</em>' containment reference.
	 * @see #getRequests()
	 * @generated
	 */
	void setRequests(Requests value);

	/**
	 * Returns the value of the '<em><b>Vehicles</b></em>' containment reference list.
	 * The list contents are of type {@link ovgu.pave.model.input.Vehicle}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vehicles</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vehicles</em>' containment reference list.
	 * @see ovgu.pave.model.input.InputPackage#getInput_Vehicles()
	 * @model containment="true"
	 * @generated
	 */
	EList<Vehicle> getVehicles();

	/**
	 * Returns the value of the '<em><b>Vehicle Types</b></em>' containment reference list.
	 * The list contents are of type {@link ovgu.pave.model.input.VehicleType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vehicle Types</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vehicle Types</em>' containment reference list.
	 * @see ovgu.pave.model.input.InputPackage#getInput_VehicleTypes()
	 * @model containment="true"
	 * @generated
	 */
	EList<VehicleType> getVehicleTypes();

	/**
	 * Returns the value of the '<em><b>Locations</b></em>' containment reference list.
	 * The list contents are of type {@link ovgu.pave.model.input.Location}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Locations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Locations</em>' containment reference list.
	 * @see ovgu.pave.model.input.InputPackage#getInput_Locations()
	 * @model containment="true"
	 * @generated
	 */
	EList<Location> getLocations();

	/**
	 * Returns the value of the '<em><b>Edges</b></em>' containment reference list.
	 * The list contents are of type {@link ovgu.pave.model.input.Edge}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Edges</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Edges</em>' containment reference list.
	 * @see ovgu.pave.model.input.InputPackage#getInput_Edges()
	 * @model containment="true"
	 * @generated
	 */
	EList<Edge> getEdges();

	/**
	 * Returns the value of the '<em><b>Solution</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Solution</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Solution</em>' attribute.
	 * @see #setSolution(Solution)
	 * @see ovgu.pave.model.input.InputPackage#getInput_Solution()
	 * @model dataType="ovgu.pave.model.input.Solution"
	 * @generated
	 */
	Solution getSolution();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.Input#getSolution <em>Solution</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Solution</em>' attribute.
	 * @see #getSolution()
	 * @generated
	 */
	void setSolution(Solution value);

} // Input
