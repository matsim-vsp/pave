/**
 */
package ovgu.pave.model.config;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Graph Hopper</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.config.GraphHopper#getOsmFilePath <em>Osm File Path</em>}</li>
 *   <li>{@link ovgu.pave.model.config.GraphHopper#getGraphFolder <em>Graph Folder</em>}</li>
 *   <li>{@link ovgu.pave.model.config.GraphHopper#getWeighting <em>Weighting</em>}</li>
 *   <li>{@link ovgu.pave.model.config.GraphHopper#getVehicleType <em>Vehicle Type</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.config.ConfigPackage#getGraphHopper()
 * @model
 * @generated
 */
public interface GraphHopper extends EObject {
	/**
	 * Returns the value of the '<em><b>Osm File Path</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Osm File Path</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Osm File Path</em>' attribute.
	 * @see #setOsmFilePath(String)
	 * @see ovgu.pave.model.config.ConfigPackage#getGraphHopper_OsmFilePath()
	 * @model
	 * @generated
	 */
	String getOsmFilePath();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.GraphHopper#getOsmFilePath <em>Osm File Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Osm File Path</em>' attribute.
	 * @see #getOsmFilePath()
	 * @generated
	 */
	void setOsmFilePath(String value);

	/**
	 * Returns the value of the '<em><b>Graph Folder</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Graph Folder</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Graph Folder</em>' attribute.
	 * @see #setGraphFolder(String)
	 * @see ovgu.pave.model.config.ConfigPackage#getGraphHopper_GraphFolder()
	 * @model
	 * @generated
	 */
	String getGraphFolder();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.GraphHopper#getGraphFolder <em>Graph Folder</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Graph Folder</em>' attribute.
	 * @see #getGraphFolder()
	 * @generated
	 */
	void setGraphFolder(String value);

	/**
	 * Returns the value of the '<em><b>Weighting</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Weighting</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Weighting</em>' attribute.
	 * @see #setWeighting(String)
	 * @see ovgu.pave.model.config.ConfigPackage#getGraphHopper_Weighting()
	 * @model
	 * @generated
	 */
	String getWeighting();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.GraphHopper#getWeighting <em>Weighting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Weighting</em>' attribute.
	 * @see #getWeighting()
	 * @generated
	 */
	void setWeighting(String value);

	/**
	 * Returns the value of the '<em><b>Vehicle Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vehicle Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vehicle Type</em>' attribute.
	 * @see #setVehicleType(String)
	 * @see ovgu.pave.model.config.ConfigPackage#getGraphHopper_VehicleType()
	 * @model
	 * @generated
	 */
	String getVehicleType();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.GraphHopper#getVehicleType <em>Vehicle Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vehicle Type</em>' attribute.
	 * @see #getVehicleType()
	 * @generated
	 */
	void setVehicleType(String value);

} // GraphHopper
