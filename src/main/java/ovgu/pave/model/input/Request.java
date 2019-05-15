/**
 */
package ovgu.pave.model.input;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Request</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.input.Request#getOriginActivity <em>Origin Activity</em>}</li>
 *   <li>{@link ovgu.pave.model.input.Request#getDestinationActivity <em>Destination Activity</em>}</li>
 *   <li>{@link ovgu.pave.model.input.Request#getId <em>Id</em>}</li>
 *   <li>{@link ovgu.pave.model.input.Request#getReceivingTime <em>Receiving Time</em>}</li>
 *   <li>{@link ovgu.pave.model.input.Request#getQuantity <em>Quantity</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.input.InputPackage#getRequest()
 * @model
 * @generated
 */
public interface Request extends EObject {
	/**
	 * Returns the value of the '<em><b>Origin Activity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Origin Activity</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Origin Activity</em>' containment reference.
	 * @see #setOriginActivity(OriginRequestActivity)
	 * @see ovgu.pave.model.input.InputPackage#getRequest_OriginActivity()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OriginRequestActivity getOriginActivity();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.Request#getOriginActivity <em>Origin Activity</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Origin Activity</em>' containment reference.
	 * @see #getOriginActivity()
	 * @generated
	 */
	void setOriginActivity(OriginRequestActivity value);

	/**
	 * Returns the value of the '<em><b>Destination Activity</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Destination Activity</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Destination Activity</em>' containment reference.
	 * @see #setDestinationActivity(DestinationRequestActivity)
	 * @see ovgu.pave.model.input.InputPackage#getRequest_DestinationActivity()
	 * @model containment="true"
	 * @generated
	 */
	DestinationRequestActivity getDestinationActivity();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.Request#getDestinationActivity <em>Destination Activity</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Destination Activity</em>' containment reference.
	 * @see #getDestinationActivity()
	 * @generated
	 */
	void setDestinationActivity(DestinationRequestActivity value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(int)
	 * @see ovgu.pave.model.input.InputPackage#getRequest_Id()
	 * @model default="0" id="true"
	 * @generated
	 */
	int getId();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.Request#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(int value);

	/**
	 * Returns the value of the '<em><b>Receiving Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Receiving Time</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Receiving Time</em>' attribute.
	 * @see #setReceivingTime(long)
	 * @see ovgu.pave.model.input.InputPackage#getRequest_ReceivingTime()
	 * @model
	 * @generated
	 */
	long getReceivingTime();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.Request#getReceivingTime <em>Receiving Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Receiving Time</em>' attribute.
	 * @see #getReceivingTime()
	 * @generated
	 */
	void setReceivingTime(long value);

	/**
	 * Returns the value of the '<em><b>Quantity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Quantity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Quantity</em>' attribute.
	 * @see #setQuantity(long)
	 * @see ovgu.pave.model.input.InputPackage#getRequest_Quantity()
	 * @model
	 * @generated
	 */
	long getQuantity();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.Request#getQuantity <em>Quantity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Quantity</em>' attribute.
	 * @see #getQuantity()
	 * @generated
	 */
	void setQuantity(long value);

} // Request
