/**
 */
package ovgu.pave.model.config;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Algorithm</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.config.Algorithm#getAlgorithm <em>Algorithm</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Algorithm#getComputationStarted <em>Computation Started</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Algorithm#getComputationFinished <em>Computation Finished</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Algorithm#getLns <em>Lns</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Algorithm#getRandomSeet <em>Random Seet</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.config.ConfigPackage#getAlgorithm()
 * @model
 * @generated
 */
public interface Algorithm extends EObject {
	/**
	 * Returns the value of the '<em><b>Algorithm</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Algorithm</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Algorithm</em>' attribute.
	 * @see #setAlgorithm(String)
	 * @see ovgu.pave.model.config.ConfigPackage#getAlgorithm_Algorithm()
	 * @model
	 * @generated
	 */
	String getAlgorithm();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Algorithm#getAlgorithm <em>Algorithm</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Algorithm</em>' attribute.
	 * @see #getAlgorithm()
	 * @generated
	 */
	void setAlgorithm(String value);

	/**
	 * Returns the value of the '<em><b>Computation Started</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Computation Started</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Computation Started</em>' attribute.
	 * @see #setComputationStarted(long)
	 * @see ovgu.pave.model.config.ConfigPackage#getAlgorithm_ComputationStarted()
	 * @model
	 * @generated
	 */
	long getComputationStarted();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Algorithm#getComputationStarted <em>Computation Started</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Computation Started</em>' attribute.
	 * @see #getComputationStarted()
	 * @generated
	 */
	void setComputationStarted(long value);

	/**
	 * Returns the value of the '<em><b>Computation Finished</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Computation Finished</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Computation Finished</em>' attribute.
	 * @see #setComputationFinished(long)
	 * @see ovgu.pave.model.config.ConfigPackage#getAlgorithm_ComputationFinished()
	 * @model
	 * @generated
	 */
	long getComputationFinished();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Algorithm#getComputationFinished <em>Computation Finished</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Computation Finished</em>' attribute.
	 * @see #getComputationFinished()
	 * @generated
	 */
	void setComputationFinished(long value);

	/**
	 * Returns the value of the '<em><b>Lns</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lns</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lns</em>' containment reference.
	 * @see #setLns(LNS)
	 * @see ovgu.pave.model.config.ConfigPackage#getAlgorithm_Lns()
	 * @model containment="true"
	 * @generated
	 */
	LNS getLns();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Algorithm#getLns <em>Lns</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lns</em>' containment reference.
	 * @see #getLns()
	 * @generated
	 */
	void setLns(LNS value);

	/**
	 * Returns the value of the '<em><b>Random Seet</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Random Seet</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Random Seet</em>' attribute.
	 * @see #setRandomSeet(int)
	 * @see ovgu.pave.model.config.ConfigPackage#getAlgorithm_RandomSeet()
	 * @model
	 * @generated
	 */
	int getRandomSeet();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Algorithm#getRandomSeet <em>Random Seet</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Random Seet</em>' attribute.
	 * @see #getRandomSeet()
	 * @generated
	 */
	void setRandomSeet(int value);

} // Algorithm
