/**
 */
package ovgu.pave.model.network;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import ovgu.pave.model.input.Edge;
import ovgu.pave.model.input.Location;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Network</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.network.Network#getEdges <em>Edges</em>}</li>
 *   <li>{@link ovgu.pave.model.network.Network#getLocations <em>Locations</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.network.NetworkPackage#getNetwork()
 * @model
 * @generated
 */
public interface Network extends EObject {
	/**
	 * Returns the value of the '<em><b>Edges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Edges</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Edges</em>' attribute.
	 * @see #setEdges(Edge[][])
	 * @see ovgu.pave.model.network.NetworkPackage#getNetwork_Edges()
	 * @model dataType="ovgu.pave.model.network.EdgeMatrix" required="true"
	 * @generated
	 */
	Edge[][] getEdges();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.network.Network#getEdges <em>Edges</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Edges</em>' attribute.
	 * @see #getEdges()
	 * @generated
	 */
	void setEdges(Edge[][] value);

	/**
	 * Returns the value of the '<em><b>Locations</b></em>' attribute list.
	 * The list contents are of type {@link ovgu.pave.model.input.Location}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Locations</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Locations</em>' attribute list.
	 * @see ovgu.pave.model.network.NetworkPackage#getNetwork_Locations()
	 * @model dataType="ovgu.pave.model.network.Location"
	 * @generated
	 */
	EList<Location> getLocations();

} // Network
