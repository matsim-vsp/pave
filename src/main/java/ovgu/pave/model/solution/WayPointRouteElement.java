/**
 */
package ovgu.pave.model.solution;

import ovgu.pave.model.input.WayPoint;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Way Point Route Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.solution.WayPointRouteElement#getWayPoint <em>Way Point</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.WayPointRouteElement#getWayPointID <em>Way Point ID</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.solution.SolutionPackage#getWayPointRouteElement()
 * @model
 * @generated
 */
public interface WayPointRouteElement extends RouteElement {
	/**
	 * Returns the value of the '<em><b>Way Point</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Way Point</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Way Point</em>' attribute.
	 * @see #setWayPoint(WayPoint)
	 * @see ovgu.pave.model.solution.SolutionPackage#getWayPointRouteElement_WayPoint()
	 * @model dataType="ovgu.pave.model.solution.WayPoint" required="true" transient="true"
	 * @generated
	 */
	WayPoint getWayPoint();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.solution.WayPointRouteElement#getWayPoint <em>Way Point</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Way Point</em>' attribute.
	 * @see #getWayPoint()
	 * @generated
	 */
	void setWayPoint(WayPoint value);

	/**
	 * Returns the value of the '<em><b>Way Point ID</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Way Point ID</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Way Point ID</em>' attribute.
	 * @see #setWayPointID(String)
	 * @see ovgu.pave.model.solution.SolutionPackage#getWayPointRouteElement_WayPointID()
	 * @model default=""
	 * @generated
	 */
	String getWayPointID();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.solution.WayPointRouteElement#getWayPointID <em>Way Point ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Way Point ID</em>' attribute.
	 * @see #getWayPointID()
	 * @generated
	 */
	void setWayPointID(String value);

} // WayPointRouteElement
