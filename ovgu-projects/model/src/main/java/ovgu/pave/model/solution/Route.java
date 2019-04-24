/**
 */
package ovgu.pave.model.solution;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import ovgu.pave.model.input.Vehicle;
import ovgu.pave.model.input.VehicleType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Route</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.solution.Route#getRouteElements <em>Route Elements</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.Route#getVehicle <em>Vehicle</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.Route#getVehicleType <em>Vehicle Type</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.Route#getVehicleID <em>Vehicle ID</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.Route#getVehicleTypeID <em>Vehicle Type ID</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.Route#getUtilisation <em>Utilisation</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.solution.SolutionPackage#getRoute()
 * @model
 * @generated
 */
public interface Route extends EObject {
	/**
	 * Returns the value of the '<em><b>Route Elements</b></em>' containment reference list.
	 * The list contents are of type {@link ovgu.pave.model.solution.RouteElement}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route Elements</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route Elements</em>' containment reference list.
	 * @see ovgu.pave.model.solution.SolutionPackage#getRoute_RouteElements()
	 * @model containment="true"
	 * @generated
	 */
	EList<RouteElement> getRouteElements();

	/**
	 * Returns the value of the '<em><b>Vehicle</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vehicle</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vehicle</em>' attribute.
	 * @see #setVehicle(Vehicle)
	 * @see ovgu.pave.model.solution.SolutionPackage#getRoute_Vehicle()
	 * @model dataType="ovgu.pave.model.solution.Vehicle" transient="true"
	 * @generated
	 */
	Vehicle getVehicle();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.solution.Route#getVehicle <em>Vehicle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vehicle</em>' attribute.
	 * @see #getVehicle()
	 * @generated
	 */
	void setVehicle(Vehicle value);

	/**
	 * Returns the value of the '<em><b>Vehicle Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vehicle Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vehicle Type</em>' attribute.
	 * @see #setVehicleType(VehicleType)
	 * @see ovgu.pave.model.solution.SolutionPackage#getRoute_VehicleType()
	 * @model dataType="ovgu.pave.model.solution.VehicleType" transient="true"
	 * @generated
	 */
	VehicleType getVehicleType();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.solution.Route#getVehicleType <em>Vehicle Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vehicle Type</em>' attribute.
	 * @see #getVehicleType()
	 * @generated
	 */
	void setVehicleType(VehicleType value);

	/**
	 * Returns the value of the '<em><b>Vehicle ID</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vehicle ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vehicle ID</em>' attribute.
	 * @see #setVehicleID(int)
	 * @see ovgu.pave.model.solution.SolutionPackage#getRoute_VehicleID()
	 * @model default="-1"
	 * @generated
	 */
	int getVehicleID();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.solution.Route#getVehicleID <em>Vehicle ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vehicle ID</em>' attribute.
	 * @see #getVehicleID()
	 * @generated
	 */
	void setVehicleID(int value);

	/**
	 * Returns the value of the '<em><b>Vehicle Type ID</b></em>' attribute.
	 * The default value is <code>"-1"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vehicle Type ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vehicle Type ID</em>' attribute.
	 * @see #setVehicleTypeID(int)
	 * @see ovgu.pave.model.solution.SolutionPackage#getRoute_VehicleTypeID()
	 * @model default="-1"
	 * @generated
	 */
	int getVehicleTypeID();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.solution.Route#getVehicleTypeID <em>Vehicle Type ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vehicle Type ID</em>' attribute.
	 * @see #getVehicleTypeID()
	 * @generated
	 */
	void setVehicleTypeID(int value);

	/**
	 * Returns the value of the '<em><b>Utilisation</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Utilisation</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Utilisation</em>' attribute.
	 * @see #setUtilisation(long)
	 * @see ovgu.pave.model.solution.SolutionPackage#getRoute_Utilisation()
	 * @model default="0"
	 * @generated
	 */
	long getUtilisation();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.solution.Route#getUtilisation <em>Utilisation</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Utilisation</em>' attribute.
	 * @see #getUtilisation()
	 * @generated
	 */
	void setUtilisation(long value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	RouteElement getRouteElement(int index);

} // Route
