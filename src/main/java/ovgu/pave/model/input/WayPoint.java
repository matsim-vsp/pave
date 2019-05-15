/**
 */
package ovgu.pave.model.input;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Way Point</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.input.WayPoint#getDistance <em>Distance</em>}</li>
 *   <li>{@link ovgu.pave.model.input.WayPoint#getDuration <em>Duration</em>}</li>
 *   <li>{@link ovgu.pave.model.input.WayPoint#getLat <em>Lat</em>}</li>
 *   <li>{@link ovgu.pave.model.input.WayPoint#getLon <em>Lon</em>}</li>
 *   <li>{@link ovgu.pave.model.input.WayPoint#getId <em>Id</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.input.InputPackage#getWayPoint()
 * @model
 * @generated
 */
public interface WayPoint extends EObject {
	/**
	 * Returns the value of the '<em><b>Distance</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distance</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distance</em>' attribute.
	 * @see #setDistance(double)
	 * @see ovgu.pave.model.input.InputPackage#getWayPoint_Distance()
	 * @model
	 * @generated
	 */
	double getDistance();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.WayPoint#getDistance <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distance</em>' attribute.
	 * @see #getDistance()
	 * @generated
	 */
	void setDistance(double value);

	/**
	 * Returns the value of the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Duration</em>' attribute.
	 * @see #setDuration(long)
	 * @see ovgu.pave.model.input.InputPackage#getWayPoint_Duration()
	 * @model
	 * @generated
	 */
	long getDuration();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.WayPoint#getDuration <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Duration</em>' attribute.
	 * @see #getDuration()
	 * @generated
	 */
	void setDuration(long value);

	/**
	 * Returns the value of the '<em><b>Lat</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lat</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lat</em>' attribute.
	 * @see #setLat(double)
	 * @see ovgu.pave.model.input.InputPackage#getWayPoint_Lat()
	 * @model
	 * @generated
	 */
	double getLat();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.WayPoint#getLat <em>Lat</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lat</em>' attribute.
	 * @see #getLat()
	 * @generated
	 */
	void setLat(double value);

	/**
	 * Returns the value of the '<em><b>Lon</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lon</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lon</em>' attribute.
	 * @see #setLon(double)
	 * @see ovgu.pave.model.input.InputPackage#getWayPoint_Lon()
	 * @model
	 * @generated
	 */
	double getLon();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.WayPoint#getLon <em>Lon</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lon</em>' attribute.
	 * @see #getLon()
	 * @generated
	 */
	void setLon(double value);

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
	 * @see ovgu.pave.model.input.InputPackage#getWayPoint_Id()
	 * @model
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.input.WayPoint#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

} // WayPoint
