/**
 */
package ovgu.pave.model.algorithmObjects;

import org.eclipse.emf.ecore.EObject;

import ovgu.pave.model.solution.Route;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Insertion Option</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.algorithmObjects.InsertionOption#getCosts <em>Costs</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.InsertionOption#getOriginIndex <em>Origin Index</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.InsertionOption#getDestinationIndex <em>Destination Index</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.InsertionOption#getRoute <em>Route</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getInsertionOption()
 * @model
 * @generated
 */
public interface InsertionOption extends EObject {
	/**
	 * Returns the value of the '<em><b>Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Costs</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Costs</em>' attribute.
	 * @see #setCosts(long)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getInsertionOption_Costs()
	 * @model
	 * @generated
	 */
	long getCosts();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.InsertionOption#getCosts <em>Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Costs</em>' attribute.
	 * @see #getCosts()
	 * @generated
	 */
	void setCosts(long value);

	/**
	 * Returns the value of the '<em><b>Origin Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Origin Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Origin Index</em>' attribute.
	 * @see #setOriginIndex(int)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getInsertionOption_OriginIndex()
	 * @model
	 * @generated
	 */
	int getOriginIndex();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.InsertionOption#getOriginIndex <em>Origin Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin Index</em>' attribute.
	 * @see #getOriginIndex()
	 * @generated
	 */
	void setOriginIndex(int value);

	/**
	 * Returns the value of the '<em><b>Destination Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Destination Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Destination Index</em>' attribute.
	 * @see #setDestinationIndex(int)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getInsertionOption_DestinationIndex()
	 * @model
	 * @generated
	 */
	int getDestinationIndex();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.InsertionOption#getDestinationIndex <em>Destination Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Destination Index</em>' attribute.
	 * @see #getDestinationIndex()
	 * @generated
	 */
	void setDestinationIndex(int value);

	/**
	 * Returns the value of the '<em><b>Route</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Route</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route</em>' attribute.
	 * @see #setRoute(Route)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getInsertionOption_Route()
	 * @model dataType="ovgu.pave.model.algorithmObjects.Route"
	 * @generated
	 */
	Route getRoute();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.InsertionOption#getRoute <em>Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route</em>' attribute.
	 * @see #getRoute()
	 * @generated
	 */
	void setRoute(Route value);

} // InsertionOption
