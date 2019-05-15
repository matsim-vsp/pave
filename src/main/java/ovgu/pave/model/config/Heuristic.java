/**
 */
package ovgu.pave.model.config;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Heuristic</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.config.Heuristic#getHeuristicName <em>Heuristic Name</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Heuristic#getNoise <em>Noise</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.config.ConfigPackage#getHeuristic()
 * @model
 * @generated
 */
public interface Heuristic extends EObject {
	/**
	 * Returns the value of the '<em><b>Heuristic Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Heuristic Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Heuristic Name</em>' attribute.
	 * @see #setHeuristicName(String)
	 * @see ovgu.pave.model.config.ConfigPackage#getHeuristic_HeuristicName()
	 * @model
	 * @generated
	 */
	String getHeuristicName();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Heuristic#getHeuristicName <em>Heuristic Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Heuristic Name</em>' attribute.
	 * @see #getHeuristicName()
	 * @generated
	 */
	void setHeuristicName(String value);

	/**
	 * Returns the value of the '<em><b>Noise</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Noise</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Noise</em>' attribute.
	 * @see #setNoise(int)
	 * @see ovgu.pave.model.config.ConfigPackage#getHeuristic_Noise()
	 * @model
	 * @generated
	 */
	int getNoise();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Heuristic#getNoise <em>Noise</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Noise</em>' attribute.
	 * @see #getNoise()
	 * @generated
	 */
	void setNoise(int value);

} // Heuristic
