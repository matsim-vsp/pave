/**
 */
package ovgu.pave.model.solution;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Route Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.solution.RouteElement#getServiceBegin <em>Service Begin</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.RouteElement#getServiceDuration <em>Service Duration</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.solution.SolutionPackage#getRouteElement()
 * @model
 * @generated
 */
public interface RouteElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Service Begin</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service Begin</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service Begin</em>' attribute.
	 * @see #setServiceBegin(long)
	 * @see ovgu.pave.model.solution.SolutionPackage#getRouteElement_ServiceBegin()
	 * @model
	 * @generated
	 */
	long getServiceBegin();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.solution.RouteElement#getServiceBegin <em>Service Begin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service Begin</em>' attribute.
	 * @see #getServiceBegin()
	 * @generated
	 */
	void setServiceBegin(long value);

	/**
	 * Returns the value of the '<em><b>Service Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service Duration</em>' attribute.
	 * @see #setServiceDuration(long)
	 * @see ovgu.pave.model.solution.SolutionPackage#getRouteElement_ServiceDuration()
	 * @model
	 * @generated
	 */
	long getServiceDuration();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.solution.RouteElement#getServiceDuration <em>Service Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service Duration</em>' attribute.
	 * @see #getServiceDuration()
	 * @generated
	 */
	void setServiceDuration(long value);

} // RouteElement
