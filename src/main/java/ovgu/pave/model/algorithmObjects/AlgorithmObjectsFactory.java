/**
 */
package ovgu.pave.model.algorithmObjects;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage
 * @generated
 */
public interface AlgorithmObjectsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AlgorithmObjectsFactory eINSTANCE = ovgu.pave.model.algorithmObjects.impl.AlgorithmObjectsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>LNS</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>LNS</em>'.
	 * @generated
	 */
	LNS createLNS();

	/**
	 * Returns a new object of class '<em>Insertion Information</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Insertion Information</em>'.
	 * @generated
	 */
	InsertionInformation createInsertionInformation();

	/**
	 * Returns a new object of class '<em>Insertion Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Insertion Option</em>'.
	 * @generated
	 */
	InsertionOption createInsertionOption();

	/**
	 * Returns a new object of class '<em>Removal Option</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Removal Option</em>'.
	 * @generated
	 */
	RemovalOption createRemovalOption();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	AlgorithmObjectsPackage getAlgorithmObjectsPackage();

} //AlgorithmObjectsFactory
