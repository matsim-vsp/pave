/**
 */
package ovgu.pave.model.config.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import ovgu.pave.model.config.ConfigPackage;
import ovgu.pave.model.config.GraphHopper;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Graph Hopper</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.config.impl.GraphHopperImpl#getOsmFilePath <em>Osm File Path</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.GraphHopperImpl#getGraphFolder <em>Graph Folder</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.GraphHopperImpl#getWeighting <em>Weighting</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.GraphHopperImpl#getVehicleType <em>Vehicle Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GraphHopperImpl extends MinimalEObjectImpl.Container implements GraphHopper {
	/**
	 * The default value of the '{@link #getOsmFilePath() <em>Osm File Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOsmFilePath()
	 * @generated
	 * @ordered
	 */
	protected static final String OSM_FILE_PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOsmFilePath() <em>Osm File Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOsmFilePath()
	 * @generated
	 * @ordered
	 */
	protected String osmFilePath = OSM_FILE_PATH_EDEFAULT;

	/**
	 * The default value of the '{@link #getGraphFolder() <em>Graph Folder</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGraphFolder()
	 * @generated
	 * @ordered
	 */
	protected static final String GRAPH_FOLDER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGraphFolder() <em>Graph Folder</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGraphFolder()
	 * @generated
	 * @ordered
	 */
	protected String graphFolder = GRAPH_FOLDER_EDEFAULT;

	/**
	 * The default value of the '{@link #getWeighting() <em>Weighting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeighting()
	 * @generated
	 * @ordered
	 */
	protected static final String WEIGHTING_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWeighting() <em>Weighting</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeighting()
	 * @generated
	 * @ordered
	 */
	protected String weighting = WEIGHTING_EDEFAULT;

	/**
	 * The default value of the '{@link #getVehicleType() <em>Vehicle Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicleType()
	 * @generated
	 * @ordered
	 */
	protected static final String VEHICLE_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVehicleType() <em>Vehicle Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicleType()
	 * @generated
	 * @ordered
	 */
	protected String vehicleType = VEHICLE_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GraphHopperImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigPackage.Literals.GRAPH_HOPPER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOsmFilePath() {
		return osmFilePath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOsmFilePath(String newOsmFilePath) {
		String oldOsmFilePath = osmFilePath;
		osmFilePath = newOsmFilePath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.GRAPH_HOPPER__OSM_FILE_PATH, oldOsmFilePath, osmFilePath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getGraphFolder() {
		return graphFolder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGraphFolder(String newGraphFolder) {
		String oldGraphFolder = graphFolder;
		graphFolder = newGraphFolder;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.GRAPH_HOPPER__GRAPH_FOLDER, oldGraphFolder, graphFolder));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getWeighting() {
		return weighting;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWeighting(String newWeighting) {
		String oldWeighting = weighting;
		weighting = newWeighting;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.GRAPH_HOPPER__WEIGHTING, oldWeighting, weighting));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVehicleType(String newVehicleType) {
		String oldVehicleType = vehicleType;
		vehicleType = newVehicleType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.GRAPH_HOPPER__VEHICLE_TYPE, oldVehicleType, vehicleType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConfigPackage.GRAPH_HOPPER__OSM_FILE_PATH:
				return getOsmFilePath();
			case ConfigPackage.GRAPH_HOPPER__GRAPH_FOLDER:
				return getGraphFolder();
			case ConfigPackage.GRAPH_HOPPER__WEIGHTING:
				return getWeighting();
			case ConfigPackage.GRAPH_HOPPER__VEHICLE_TYPE:
				return getVehicleType();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ConfigPackage.GRAPH_HOPPER__OSM_FILE_PATH:
				setOsmFilePath((String)newValue);
				return;
			case ConfigPackage.GRAPH_HOPPER__GRAPH_FOLDER:
				setGraphFolder((String)newValue);
				return;
			case ConfigPackage.GRAPH_HOPPER__WEIGHTING:
				setWeighting((String)newValue);
				return;
			case ConfigPackage.GRAPH_HOPPER__VEHICLE_TYPE:
				setVehicleType((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ConfigPackage.GRAPH_HOPPER__OSM_FILE_PATH:
				setOsmFilePath(OSM_FILE_PATH_EDEFAULT);
				return;
			case ConfigPackage.GRAPH_HOPPER__GRAPH_FOLDER:
				setGraphFolder(GRAPH_FOLDER_EDEFAULT);
				return;
			case ConfigPackage.GRAPH_HOPPER__WEIGHTING:
				setWeighting(WEIGHTING_EDEFAULT);
				return;
			case ConfigPackage.GRAPH_HOPPER__VEHICLE_TYPE:
				setVehicleType(VEHICLE_TYPE_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ConfigPackage.GRAPH_HOPPER__OSM_FILE_PATH:
				return OSM_FILE_PATH_EDEFAULT == null ? osmFilePath != null : !OSM_FILE_PATH_EDEFAULT.equals(osmFilePath);
			case ConfigPackage.GRAPH_HOPPER__GRAPH_FOLDER:
				return GRAPH_FOLDER_EDEFAULT == null ? graphFolder != null : !GRAPH_FOLDER_EDEFAULT.equals(graphFolder);
			case ConfigPackage.GRAPH_HOPPER__WEIGHTING:
				return WEIGHTING_EDEFAULT == null ? weighting != null : !WEIGHTING_EDEFAULT.equals(weighting);
			case ConfigPackage.GRAPH_HOPPER__VEHICLE_TYPE:
				return VEHICLE_TYPE_EDEFAULT == null ? vehicleType != null : !VEHICLE_TYPE_EDEFAULT.equals(vehicleType);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (osmFilePath: ");
		result.append(osmFilePath);
		result.append(", graphFolder: ");
		result.append(graphFolder);
		result.append(", weighting: ");
		result.append(weighting);
		result.append(", vehicleType: ");
		result.append(vehicleType);
		result.append(')');
		return result.toString();
	}

} //GraphHopperImpl
