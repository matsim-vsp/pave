/**
 */
package ovgu.pave.model.solution;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import ovgu.pave.model.input.VehicleType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Use Vehicle Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.solution.UseVehicleType#isUseVehicleTypeInsteadVehicle <em>Use Vehicle Type Instead Vehicle</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.UseVehicleType#getVehicleTypes <em>Vehicle Types</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.UseVehicleType#getVehicleTypeIDs <em>Vehicle Type IDs</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.UseVehicleType#getQuantities <em>Quantities</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.solution.SolutionPackage#getUseVehicleType()
 * @model
 * @generated
 */
public interface UseVehicleType extends EObject {
	/**
	 * Returns the value of the '<em><b>Use Vehicle Type Instead Vehicle</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Use Vehicle Type Instead Vehicle</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Use Vehicle Type Instead Vehicle</em>' attribute.
	 * @see #setUseVehicleTypeInsteadVehicle(boolean)
	 * @see ovgu.pave.model.solution.SolutionPackage#getUseVehicleType_UseVehicleTypeInsteadVehicle()
	 * @model default="false"
	 * @generated
	 */
	boolean isUseVehicleTypeInsteadVehicle();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.solution.UseVehicleType#isUseVehicleTypeInsteadVehicle <em>Use Vehicle Type Instead Vehicle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Use Vehicle Type Instead Vehicle</em>' attribute.
	 * @see #isUseVehicleTypeInsteadVehicle()
	 * @generated
	 */
	void setUseVehicleTypeInsteadVehicle(boolean value);

	/**
	 * Returns the value of the '<em><b>Vehicle Types</b></em>' attribute list.
	 * The list contents are of type {@link ovgu.pave.model.input.VehicleType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vehicle Types</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vehicle Types</em>' attribute list.
	 * @see ovgu.pave.model.solution.SolutionPackage#getUseVehicleType_VehicleTypes()
	 * @model dataType="ovgu.pave.model.solution.VehicleType" transient="true"
	 * @generated
	 */
	EList<VehicleType> getVehicleTypes();

	/**
	 * Returns the value of the '<em><b>Vehicle Type IDs</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Integer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vehicle Type IDs</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vehicle Type IDs</em>' attribute list.
	 * @see ovgu.pave.model.solution.SolutionPackage#getUseVehicleType_VehicleTypeIDs()
	 * @model
	 * @generated
	 */
	EList<Integer> getVehicleTypeIDs();

	/**
	 * Returns the value of the '<em><b>Quantities</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Integer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Quantities</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Quantities</em>' attribute list.
	 * @see ovgu.pave.model.solution.SolutionPackage#getUseVehicleType_Quantities()
	 * @model unique="false"
	 * @generated
	 */
	EList<Integer> getQuantities();

} // UseVehicleType
