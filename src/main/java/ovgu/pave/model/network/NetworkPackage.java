/**
 */
package ovgu.pave.model.network;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see ovgu.pave.model.network.NetworkFactory
 * @model kind="package"
 * @generated
 */
public interface NetworkPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "network";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "ovgu.pave.solution";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "net";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	NetworkPackage eINSTANCE = ovgu.pave.model.network.impl.NetworkPackageImpl.init();

	/**
	 * The meta object id for the '{@link ovgu.pave.model.network.impl.NetworkImpl <em>Network</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.network.impl.NetworkImpl
	 * @see ovgu.pave.model.network.impl.NetworkPackageImpl#getNetwork()
	 * @generated
	 */
	int NETWORK = 0;

	/**
	 * The feature id for the '<em><b>Edges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETWORK__EDGES = 0;

	/**
	 * The feature id for the '<em><b>Locations</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETWORK__LOCATIONS = 1;

	/**
	 * The number of structural features of the '<em>Network</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETWORK_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Network</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NETWORK_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '<em>Edge Matrix</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.network.impl.NetworkPackageImpl#getEdgeMatrix()
	 * @generated
	 */
	int EDGE_MATRIX = 1;

	/**
	 * The meta object id for the '<em>Location</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see ovgu.pave.model.input.Location
	 * @see ovgu.pave.model.network.impl.NetworkPackageImpl#getLocation()
	 * @generated
	 */
	int LOCATION = 2;


	/**
	 * Returns the meta object for class '{@link ovgu.pave.model.network.Network <em>Network</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Network</em>'.
	 * @see ovgu.pave.model.network.Network
	 * @generated
	 */
	EClass getNetwork();

	/**
	 * Returns the meta object for the attribute '{@link ovgu.pave.model.network.Network#getEdges <em>Edges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Edges</em>'.
	 * @see ovgu.pave.model.network.Network#getEdges()
	 * @see #getNetwork()
	 * @generated
	 */
	EAttribute getNetwork_Edges();

	/**
	 * Returns the meta object for the attribute list '{@link ovgu.pave.model.network.Network#getLocations <em>Locations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Locations</em>'.
	 * @see ovgu.pave.model.network.Network#getLocations()
	 * @see #getNetwork()
	 * @generated
	 */
	EAttribute getNetwork_Locations();

	/**
	 * Returns the meta object for data type '<em>Edge Matrix</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Edge Matrix</em>'.
	 * @model instanceClass="ovgu.pave.model.input.Edge[][]"
	 * @generated
	 */
	EDataType getEdgeMatrix();

	/**
	 * Returns the meta object for data type '{@link ovgu.pave.model.input.Location <em>Location</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Location</em>'.
	 * @see ovgu.pave.model.input.Location
	 * @model instanceClass="ovgu.pave.model.input.Location"
	 * @generated
	 */
	EDataType getLocation();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	NetworkFactory getNetworkFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link ovgu.pave.model.network.impl.NetworkImpl <em>Network</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.network.impl.NetworkImpl
		 * @see ovgu.pave.model.network.impl.NetworkPackageImpl#getNetwork()
		 * @generated
		 */
		EClass NETWORK = eINSTANCE.getNetwork();

		/**
		 * The meta object literal for the '<em><b>Edges</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NETWORK__EDGES = eINSTANCE.getNetwork_Edges();

		/**
		 * The meta object literal for the '<em><b>Locations</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute NETWORK__LOCATIONS = eINSTANCE.getNetwork_Locations();

		/**
		 * The meta object literal for the '<em>Edge Matrix</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.network.impl.NetworkPackageImpl#getEdgeMatrix()
		 * @generated
		 */
		EDataType EDGE_MATRIX = eINSTANCE.getEdgeMatrix();

		/**
		 * The meta object literal for the '<em>Location</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see ovgu.pave.model.input.Location
		 * @see ovgu.pave.model.network.impl.NetworkPackageImpl#getLocation()
		 * @generated
		 */
		EDataType LOCATION = eINSTANCE.getLocation();

	}

} //NetworkPackage
