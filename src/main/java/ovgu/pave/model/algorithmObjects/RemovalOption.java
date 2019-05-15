/**
 */
package ovgu.pave.model.algorithmObjects;

import org.eclipse.emf.ecore.EObject;

import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.RouteElement;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Removal Option</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.algorithmObjects.RemovalOption#getCosts <em>Costs</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.RemovalOption#getRoute <em>Route</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.RemovalOption#getOriginRouteElement <em>Origin Route Element</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.RemovalOption#getDestinationRouteElement <em>Destination Route Element</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.RemovalOption#getDistance <em>Distance</em>}</li>
 *   <li>{@link ovgu.pave.model.algorithmObjects.RemovalOption#getAbsServiceBegin <em>Abs Service Begin</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getRemovalOption()
 * @model
 * @generated
 */
public interface RemovalOption extends EObject {
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
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getRemovalOption_Costs()
	 * @model
	 * @generated
	 */
	long getCosts();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.RemovalOption#getCosts <em>Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Costs</em>' attribute.
	 * @see #getCosts()
	 * @generated
	 */
	void setCosts(long value);

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
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getRemovalOption_Route()
	 * @model dataType="ovgu.pave.model.algorithmObjects.Route"
	 * @generated
	 */
	Route getRoute();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.RemovalOption#getRoute <em>Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Route</em>' attribute.
	 * @see #getRoute()
	 * @generated
	 */
	void setRoute(Route value);

	/**
	 * Returns the value of the '<em><b>Origin Route Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Origin Route Element</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Origin Route Element</em>' attribute.
	 * @see #setOriginRouteElement(RouteElement)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getRemovalOption_OriginRouteElement()
	 * @model dataType="ovgu.pave.model.algorithmObjects.RouteElement"
	 * @generated
	 */
	RouteElement getOriginRouteElement();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.RemovalOption#getOriginRouteElement <em>Origin Route Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin Route Element</em>' attribute.
	 * @see #getOriginRouteElement()
	 * @generated
	 */
	void setOriginRouteElement(RouteElement value);

	/**
	 * Returns the value of the '<em><b>Destination Route Element</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Destination Route Element</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Destination Route Element</em>' attribute.
	 * @see #setDestinationRouteElement(RouteElement)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getRemovalOption_DestinationRouteElement()
	 * @model dataType="ovgu.pave.model.algorithmObjects.RouteElement"
	 * @generated
	 */
	RouteElement getDestinationRouteElement();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.RemovalOption#getDestinationRouteElement <em>Destination Route Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Destination Route Element</em>' attribute.
	 * @see #getDestinationRouteElement()
	 * @generated
	 */
	void setDestinationRouteElement(RouteElement value);

	/**
	 * Returns the value of the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distance</em>' attribute.
	 * @see #setDistance(long)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getRemovalOption_Distance()
	 * @model
	 * @generated
	 */
	long getDistance();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.RemovalOption#getDistance <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distance</em>' attribute.
	 * @see #getDistance()
	 * @generated
	 */
	void setDistance(long value);

	/**
	 * Returns the value of the '<em><b>Abs Service Begin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Abs Service Begin</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Abs Service Begin</em>' attribute.
	 * @see #setAbsServiceBegin(long)
	 * @see ovgu.pave.model.algorithmObjects.AlgorithmObjectsPackage#getRemovalOption_AbsServiceBegin()
	 * @model
	 * @generated
	 */
	long getAbsServiceBegin();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.algorithmObjects.RemovalOption#getAbsServiceBegin <em>Abs Service Begin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Abs Service Begin</em>' attribute.
	 * @see #getAbsServiceBegin()
	 * @generated
	 */
	void setAbsServiceBegin(long value);

} // RemovalOption
