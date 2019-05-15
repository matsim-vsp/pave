/**
 */
package ovgu.pave.model.input;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Request Activity</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.input.RequestActivity#getRequest <em>Request</em>}</li>
 *   <li>{@link ovgu.pave.model.input.RequestActivity#getLocation <em>Location</em>}</li>
 *   <li>{@link ovgu.pave.model.input.RequestActivity#getServiceDuration <em>Service Duration</em>}</li>
 *   <li>{@link ovgu.pave.model.input.RequestActivity#getEarliestArrival <em>Earliest Arrival</em>}</li>
 *   <li>{@link ovgu.pave.model.input.RequestActivity#getLatestArrival <em>Latest Arrival</em>}</li>
 *   <li>{@link ovgu.pave.model.input.RequestActivity#getId <em>Id</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.input.InputPackage#getRequestActivity()
 * @model
 * @generated
 */
public interface RequestActivity extends EObject {
	/**
	 * Returns the value of the '<em><b>Request</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Request</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Request</em>' reference.
	 * @see #setRequest(Request)
	 * @see ovgu.pave.model.input.InputPackage#getRequestActivity_Request()
	 * @model required="true"
	 * @generated
	 */
	Request getRequest();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.RequestActivity#getRequest <em>Request</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Request</em>' reference.
	 * @see #getRequest()
	 * @generated
	 */
	void setRequest(Request value);

	/**
	 * Returns the value of the '<em><b>Location</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Location</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Location</em>' reference.
	 * @see #setLocation(Location)
	 * @see ovgu.pave.model.input.InputPackage#getRequestActivity_Location()
	 * @model required="true"
	 * @generated
	 */
	Location getLocation();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.RequestActivity#getLocation <em>Location</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Location</em>' reference.
	 * @see #getLocation()
	 * @generated
	 */
	void setLocation(Location value);

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
	 * @see ovgu.pave.model.input.InputPackage#getRequestActivity_ServiceDuration()
	 * @model
	 * @generated
	 */
	long getServiceDuration();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.RequestActivity#getServiceDuration <em>Service Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service Duration</em>' attribute.
	 * @see #getServiceDuration()
	 * @generated
	 */
	void setServiceDuration(long value);

	/**
	 * Returns the value of the '<em><b>Earliest Arrival</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Earliest Arrival</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Earliest Arrival</em>' attribute.
	 * @see #setEarliestArrival(long)
	 * @see ovgu.pave.model.input.InputPackage#getRequestActivity_EarliestArrival()
	 * @model
	 * @generated
	 */
	long getEarliestArrival();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.RequestActivity#getEarliestArrival <em>Earliest Arrival</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Earliest Arrival</em>' attribute.
	 * @see #getEarliestArrival()
	 * @generated
	 */
	void setEarliestArrival(long value);

	/**
	 * Returns the value of the '<em><b>Latest Arrival</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Latest Arrival</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Latest Arrival</em>' attribute.
	 * @see #setLatestArrival(long)
	 * @see ovgu.pave.model.input.InputPackage#getRequestActivity_LatestArrival()
	 * @model
	 * @generated
	 */
	long getLatestArrival();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.RequestActivity#getLatestArrival <em>Latest Arrival</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Latest Arrival</em>' attribute.
	 * @see #getLatestArrival()
	 * @generated
	 */
	void setLatestArrival(long value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see ovgu.pave.model.input.InputPackage#getRequestActivity_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.RequestActivity#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

} // RequestActivity
