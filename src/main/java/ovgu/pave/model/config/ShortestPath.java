/**
 */
package ovgu.pave.model.config;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Shortest Path</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.config.ShortestPath#getUse <em>Use</em>}</li>
 *   <li>{@link ovgu.pave.model.config.ShortestPath#getGraphhopper <em>Graphhopper</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.config.ConfigPackage#getShortestPath()
 * @model
 * @generated
 */
public interface ShortestPath extends EObject {
	/**
	 * Returns the value of the '<em><b>Use</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Use</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Use</em>' attribute.
	 * @see #setUse(String)
	 * @see ovgu.pave.model.config.ConfigPackage#getShortestPath_Use()
	 * @model
	 * @generated
	 */
	String getUse();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.ShortestPath#getUse <em>Use</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Use</em>' attribute.
	 * @see #getUse()
	 * @generated
	 */
	void setUse(String value);

	/**
	 * Returns the value of the '<em><b>Graphhopper</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Graphhopper</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Graphhopper</em>' containment reference.
	 * @see #setGraphhopper(GraphHopper)
	 * @see ovgu.pave.model.config.ConfigPackage#getShortestPath_Graphhopper()
	 * @model containment="true"
	 * @generated
	 */
	GraphHopper getGraphhopper();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.ShortestPath#getGraphhopper <em>Graphhopper</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Graphhopper</em>' containment reference.
	 * @see #getGraphhopper()
	 * @generated
	 */
	void setGraphhopper(GraphHopper value);

} // ShortestPath
