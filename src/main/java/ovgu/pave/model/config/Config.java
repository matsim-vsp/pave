/**
 */
package ovgu.pave.model.config;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Config</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.config.Config#getInputFolder <em>Input Folder</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Config#getShortestPath <em>Shortest Path</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Config#getExperiment <em>Experiment</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Config#getInputFilename <em>Input Filename</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Config#getAlgorithm <em>Algorithm</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Config#getConfigPath <em>Config Path</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.config.ConfigPackage#getConfig()
 * @model
 * @generated
 */
public interface Config extends EObject {
	/**
	 * Returns the value of the '<em><b>Input Folder</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input Folder</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input Folder</em>' attribute.
	 * @see #setInputFolder(String)
	 * @see ovgu.pave.model.config.ConfigPackage#getConfig_InputFolder()
	 * @model
	 * @generated
	 */
	String getInputFolder();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Config#getInputFolder <em>Input Folder</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Input Folder</em>' attribute.
	 * @see #getInputFolder()
	 * @generated
	 */
	void setInputFolder(String value);

	/**
	 * Returns the value of the '<em><b>Shortest Path</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Shortest Path</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Shortest Path</em>' containment reference.
	 * @see #setShortestPath(ShortestPath)
	 * @see ovgu.pave.model.config.ConfigPackage#getConfig_ShortestPath()
	 * @model containment="true" required="true"
	 * @generated
	 */
	ShortestPath getShortestPath();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Config#getShortestPath <em>Shortest Path</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Shortest Path</em>' containment reference.
	 * @see #getShortestPath()
	 * @generated
	 */
	void setShortestPath(ShortestPath value);

	/**
	 * Returns the value of the '<em><b>Experiment</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Experiment</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Experiment</em>' containment reference.
	 * @see #setExperiment(Experiment)
	 * @see ovgu.pave.model.config.ConfigPackage#getConfig_Experiment()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Experiment getExperiment();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Config#getExperiment <em>Experiment</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Experiment</em>' containment reference.
	 * @see #getExperiment()
	 * @generated
	 */
	void setExperiment(Experiment value);

	/**
	 * Returns the value of the '<em><b>Input Filename</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input Filename</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input Filename</em>' attribute.
	 * @see #setInputFilename(String)
	 * @see ovgu.pave.model.config.ConfigPackage#getConfig_InputFilename()
	 * @model
	 * @generated
	 */
	String getInputFilename();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Config#getInputFilename <em>Input Filename</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Input Filename</em>' attribute.
	 * @see #getInputFilename()
	 * @generated
	 */
	void setInputFilename(String value);

	/**
	 * Returns the value of the '<em><b>Algorithm</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Algorithm</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Algorithm</em>' containment reference.
	 * @see #setAlgorithm(Algorithm)
	 * @see ovgu.pave.model.config.ConfigPackage#getConfig_Algorithm()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Algorithm getAlgorithm();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Config#getAlgorithm <em>Algorithm</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Algorithm</em>' containment reference.
	 * @see #getAlgorithm()
	 * @generated
	 */
	void setAlgorithm(Algorithm value);

	/**
	 * Returns the value of the '<em><b>Config Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Config Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Config Path</em>' attribute.
	 * @see #setConfigPath(String)
	 * @see ovgu.pave.model.config.ConfigPackage#getConfig_ConfigPath()
	 * @model
	 * @generated
	 */
	String getConfigPath();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Config#getConfigPath <em>Config Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Config Path</em>' attribute.
	 * @see #getConfigPath()
	 * @generated
	 */
	void setConfigPath(String value);

} // Config
