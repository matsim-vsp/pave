/**
 */
package ovgu.pave.model.input;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vehicle</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.input.Vehicle#getType <em>Type</em>}</li>
 *   <li>{@link ovgu.pave.model.input.Vehicle#getId <em>Id</em>}</li>
 *   <li>{@link ovgu.pave.model.input.Vehicle#getStartLocation <em>Start Location</em>}</li>
 *   <li>{@link ovgu.pave.model.input.Vehicle#getEndLocation <em>End Location</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.input.InputPackage#getVehicle()
 * @model
 * @generated
 */
public interface Vehicle extends EObject {
	/**
	 * Returns the value of the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' reference.
	 * @see #setType(VehicleType)
	 * @see ovgu.pave.model.input.InputPackage#getVehicle_Type()
	 * @model required="true"
	 * @generated
	 */
	VehicleType getType();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.Vehicle#getType <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' reference.
	 * @see #getType()
	 * @generated
	 */
	void setType(VehicleType value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(int)
	 * @see ovgu.pave.model.input.InputPackage#getVehicle_Id()
	 * @model default="0"
	 * @generated
	 */
	int getId();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.Vehicle#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

	/**
	 * Returns the value of the '<em><b>Start Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Location</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Location</em>' reference.
	 * @see #setStartLocation(Location)
	 * @see ovgu.pave.model.input.InputPackage#getVehicle_StartLocation()
	 * @model
	 * @generated
	 */
	Location getStartLocation();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.Vehicle#getStartLocation <em>Start Location</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Location</em>' reference.
	 * @see #getStartLocation()
	 * @generated
	 */
	void setStartLocation(Location value);

	/**
	 * Returns the value of the '<em><b>End Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Location</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Location</em>' reference.
	 * @see #setEndLocation(Location)
	 * @see ovgu.pave.model.input.InputPackage#getVehicle_EndLocation()
	 * @model
	 * @generated
	 */
	Location getEndLocation();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.Vehicle#getEndLocation <em>End Location</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Location</em>' reference.
	 * @see #getEndLocation()
	 * @generated
	 */
	void setEndLocation(Location value);

} // Vehicle
