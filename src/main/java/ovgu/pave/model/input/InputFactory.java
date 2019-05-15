/**
 */
package ovgu.pave.model.input;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see ovgu.pave.model.input.InputPackage
 * @generated
 */
public interface InputFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	InputFactory eINSTANCE = ovgu.pave.model.input.impl.InputFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Input</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Input</em>'.
	 * @generated
	 */
	Input createInput();

	/**
	 * Returns a new object of class '<em>Request</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Request</em>'.
	 * @generated
	 */
	Request createRequest();

	/**
	 * Returns a new object of class '<em>Location</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Location</em>'.
	 * @generated
	 */
	Location createLocation();

	/**
	 * Returns a new object of class '<em>Vehicle Type</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vehicle Type</em>'.
	 * @generated
	 */
	VehicleType createVehicleType();

	/**
	 * Returns a new object of class '<em>Vehicle</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vehicle</em>'.
	 * @generated
	 */
	Vehicle createVehicle();

	/**
	 * Returns a new object of class '<em>Origin Request Activity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Origin Request Activity</em>'.
	 * @generated
	 */
	OriginRequestActivity createOriginRequestActivity();

	/**
	 * Returns a new object of class '<em>Request Activity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Request Activity</em>'.
	 * @generated
	 */
	RequestActivity createRequestActivity();

	/**
	 * Returns a new object of class '<em>Destination Request Activity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Destination Request Activity</em>'.
	 * @generated
	 */
	DestinationRequestActivity createDestinationRequestActivity();

	/**
	 * Returns a new object of class '<em>Edge</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Edge</em>'.
	 * @generated
	 */
	Edge createEdge();

	/**
	 * Returns a new object of class '<em>Way Point</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Way Point</em>'.
	 * @generated
	 */
	WayPoint createWayPoint();

	/**
	 * Returns a new object of class '<em>Requests</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Requests</em>'.
	 * @generated
	 */
	Requests createRequests();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	InputPackage getInputPackage();

} //InputFactory
