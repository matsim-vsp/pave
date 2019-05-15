/**
 */
package ovgu.pave.model.solution.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import ovgu.pave.model.input.VehicleType;

import ovgu.pave.model.solution.SolutionPackage;
import ovgu.pave.model.solution.UseVehicleType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Use Vehicle Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.solution.impl.UseVehicleTypeImpl#isUseVehicleTypeInsteadVehicle <em>Use Vehicle Type Instead Vehicle</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.UseVehicleTypeImpl#getVehicleTypes <em>Vehicle Types</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.UseVehicleTypeImpl#getVehicleTypeIDs <em>Vehicle Type IDs</em>}</li>
 *   <li>{@link ovgu.pave.model.solution.impl.UseVehicleTypeImpl#getQuantities <em>Quantities</em>}</li>
 * </ul>
 *
 * @generated
 */
public class UseVehicleTypeImpl extends MinimalEObjectImpl.Container implements UseVehicleType {
	/**
	 * The default value of the '{@link #isUseVehicleTypeInsteadVehicle() <em>Use Vehicle Type Instead Vehicle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseVehicleTypeInsteadVehicle()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USE_VEHICLE_TYPE_INSTEAD_VEHICLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUseVehicleTypeInsteadVehicle() <em>Use Vehicle Type Instead Vehicle</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUseVehicleTypeInsteadVehicle()
	 * @generated
	 * @ordered
	 */
	protected boolean useVehicleTypeInsteadVehicle = USE_VEHICLE_TYPE_INSTEAD_VEHICLE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getVehicleTypes() <em>Vehicle Types</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicleTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<VehicleType> vehicleTypes;

	/**
	 * The cached value of the '{@link #getVehicleTypeIDs() <em>Vehicle Type IDs</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicleTypeIDs()
	 * @generated
	 * @ordered
	 */
	protected EList<Integer> vehicleTypeIDs;

	/**
	 * The cached value of the '{@link #getQuantities() <em>Quantities</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQuantities()
	 * @generated
	 * @ordered
	 */
	protected EList<Integer> quantities;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UseVehicleTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SolutionPackage.Literals.USE_VEHICLE_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUseVehicleTypeInsteadVehicle() {
		return useVehicleTypeInsteadVehicle;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUseVehicleTypeInsteadVehicle(boolean newUseVehicleTypeInsteadVehicle) {
		boolean oldUseVehicleTypeInsteadVehicle = useVehicleTypeInsteadVehicle;
		useVehicleTypeInsteadVehicle = newUseVehicleTypeInsteadVehicle;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SolutionPackage.USE_VEHICLE_TYPE__USE_VEHICLE_TYPE_INSTEAD_VEHICLE, oldUseVehicleTypeInsteadVehicle, useVehicleTypeInsteadVehicle));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VehicleType> getVehicleTypes() {
		if (vehicleTypes == null) {
			vehicleTypes = new EDataTypeUniqueEList<VehicleType>(VehicleType.class, this, SolutionPackage.USE_VEHICLE_TYPE__VEHICLE_TYPES);
		}
		return vehicleTypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Integer> getVehicleTypeIDs() {
		if (vehicleTypeIDs == null) {
			vehicleTypeIDs = new EDataTypeUniqueEList<Integer>(Integer.class, this, SolutionPackage.USE_VEHICLE_TYPE__VEHICLE_TYPE_IDS);
		}
		return vehicleTypeIDs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Integer> getQuantities() {
		if (quantities == null) {
			quantities = new EDataTypeEList<Integer>(Integer.class, this, SolutionPackage.USE_VEHICLE_TYPE__QUANTITIES);
		}
		return quantities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SolutionPackage.USE_VEHICLE_TYPE__USE_VEHICLE_TYPE_INSTEAD_VEHICLE:
				return isUseVehicleTypeInsteadVehicle();
			case SolutionPackage.USE_VEHICLE_TYPE__VEHICLE_TYPES:
				return getVehicleTypes();
			case SolutionPackage.USE_VEHICLE_TYPE__VEHICLE_TYPE_IDS:
				return getVehicleTypeIDs();
			case SolutionPackage.USE_VEHICLE_TYPE__QUANTITIES:
				return getQuantities();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case SolutionPackage.USE_VEHICLE_TYPE__USE_VEHICLE_TYPE_INSTEAD_VEHICLE:
				setUseVehicleTypeInsteadVehicle((Boolean)newValue);
				return;
			case SolutionPackage.USE_VEHICLE_TYPE__VEHICLE_TYPES:
				getVehicleTypes().clear();
				getVehicleTypes().addAll((Collection<? extends VehicleType>)newValue);
				return;
			case SolutionPackage.USE_VEHICLE_TYPE__VEHICLE_TYPE_IDS:
				getVehicleTypeIDs().clear();
				getVehicleTypeIDs().addAll((Collection<? extends Integer>)newValue);
				return;
			case SolutionPackage.USE_VEHICLE_TYPE__QUANTITIES:
				getQuantities().clear();
				getQuantities().addAll((Collection<? extends Integer>)newValue);
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
			case SolutionPackage.USE_VEHICLE_TYPE__USE_VEHICLE_TYPE_INSTEAD_VEHICLE:
				setUseVehicleTypeInsteadVehicle(USE_VEHICLE_TYPE_INSTEAD_VEHICLE_EDEFAULT);
				return;
			case SolutionPackage.USE_VEHICLE_TYPE__VEHICLE_TYPES:
				getVehicleTypes().clear();
				return;
			case SolutionPackage.USE_VEHICLE_TYPE__VEHICLE_TYPE_IDS:
				getVehicleTypeIDs().clear();
				return;
			case SolutionPackage.USE_VEHICLE_TYPE__QUANTITIES:
				getQuantities().clear();
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
			case SolutionPackage.USE_VEHICLE_TYPE__USE_VEHICLE_TYPE_INSTEAD_VEHICLE:
				return useVehicleTypeInsteadVehicle != USE_VEHICLE_TYPE_INSTEAD_VEHICLE_EDEFAULT;
			case SolutionPackage.USE_VEHICLE_TYPE__VEHICLE_TYPES:
				return vehicleTypes != null && !vehicleTypes.isEmpty();
			case SolutionPackage.USE_VEHICLE_TYPE__VEHICLE_TYPE_IDS:
				return vehicleTypeIDs != null && !vehicleTypeIDs.isEmpty();
			case SolutionPackage.USE_VEHICLE_TYPE__QUANTITIES:
				return quantities != null && !quantities.isEmpty();
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
		result.append(" (useVehicleTypeInsteadVehicle: ");
		result.append(useVehicleTypeInsteadVehicle);
		result.append(", VehicleTypes: ");
		result.append(vehicleTypes);
		result.append(", vehicleTypeIDs: ");
		result.append(vehicleTypeIDs);
		result.append(", quantities: ");
		result.append(quantities);
		result.append(')');
		return result.toString();
	}

} //UseVehicleTypeImpl
