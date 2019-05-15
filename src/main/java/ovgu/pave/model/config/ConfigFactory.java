/**
 */
package ovgu.pave.model.config;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see ovgu.pave.model.config.ConfigPackage
 * @generated
 */
public interface ConfigFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ConfigFactory eINSTANCE = ovgu.pave.model.config.impl.ConfigFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Algorithm</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Algorithm</em>'.
	 * @generated
	 */
	Algorithm createAlgorithm();

	/**
	 * Returns a new object of class '<em>Config</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Config</em>'.
	 * @generated
	 */
	Config createConfig();

	/**
	 * Returns a new object of class '<em>Shortest Path</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Shortest Path</em>'.
	 * @generated
	 */
	ShortestPath createShortestPath();

	/**
	 * Returns a new object of class '<em>Experiment</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Experiment</em>'.
	 * @generated
	 */
	Experiment createExperiment();

	/**
	 * Returns a new object of class '<em>Graph Hopper</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Graph Hopper</em>'.
	 * @generated
	 */
	GraphHopper createGraphHopper();

	/**
	 * Returns a new object of class '<em>LNS</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>LNS</em>'.
	 * @generated
	 */
	LNS createLNS();

	/**
	 * Returns a new object of class '<em>Heuristic</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Heuristic</em>'.
	 * @generated
	 */
	Heuristic createHeuristic();

	/**
	 * Returns a new object of class '<em>RWS</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>RWS</em>'.
	 * @generated
	 */
	RWS createRWS();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ConfigPackage getConfigPackage();

} //ConfigFactory
