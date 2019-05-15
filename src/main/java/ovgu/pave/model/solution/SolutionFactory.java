/**
 */
package ovgu.pave.model.solution;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see ovgu.pave.model.solution.SolutionPackage
 * @generated
 */
public interface SolutionFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SolutionFactory eINSTANCE = ovgu.pave.model.solution.impl.SolutionFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Route</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Route</em>'.
	 * @generated
	 */
	Route createRoute();

	/**
	 * Returns a new object of class '<em>Route Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Route Element</em>'.
	 * @generated
	 */
	RouteElement createRouteElement();

	/**
	 * Returns a new object of class '<em>Solution</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Solution</em>'.
	 * @generated
	 */
	Solution createSolution();

	/**
	 * Returns a new object of class '<em>Support Route Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Support Route Element</em>'.
	 * @generated
	 */
	SupportRouteElement createSupportRouteElement();

	/**
	 * Returns a new object of class '<em>Request Activity Route Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Request Activity Route Element</em>'.
	 * @generated
	 */
	RequestActivityRouteElement createRequestActivityRouteElement();

	/**
	 * Returns a new object of class '<em>Way Point Route Element</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Way Point Route Element</em>'.
	 * @generated
	 */
	WayPointRouteElement createWayPointRouteElement();

	/**
	 * Returns a new object of class '<em>Use Vehicle Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Use Vehicle Type</em>'.
	 * @generated
	 */
	UseVehicleType createUseVehicleType();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	SolutionPackage getSolutionPackage();

} //SolutionFactory
