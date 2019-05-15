/**
 */
package ovgu.pave.model.config.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import ovgu.pave.model.config.ConfigPackage;
import ovgu.pave.model.config.Heuristic;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Heuristic</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.config.impl.HeuristicImpl#getHeuristicName <em>Heuristic Name</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.HeuristicImpl#getNoise <em>Noise</em>}</li>
 * </ul>
 *
 * @generated
 */
public class HeuristicImpl extends MinimalEObjectImpl.Container implements Heuristic {
	/**
	 * The default value of the '{@link #getHeuristicName() <em>Heuristic Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeuristicName()
	 * @generated
	 * @ordered
	 */
	protected static final String HEURISTIC_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getHeuristicName() <em>Heuristic Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeuristicName()
	 * @generated
	 * @ordered
	 */
	protected String heuristicName = HEURISTIC_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getNoise() <em>Noise</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNoise()
	 * @generated
	 * @ordered
	 */
	protected static final int NOISE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNoise() <em>Noise</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNoise()
	 * @generated
	 * @ordered
	 */
	protected int noise = NOISE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HeuristicImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigPackage.Literals.HEURISTIC;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getHeuristicName() {
		return heuristicName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHeuristicName(String newHeuristicName) {
		String oldHeuristicName = heuristicName;
		heuristicName = newHeuristicName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.HEURISTIC__HEURISTIC_NAME, oldHeuristicName, heuristicName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNoise() {
		return noise;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNoise(int newNoise) {
		int oldNoise = noise;
		noise = newNoise;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.HEURISTIC__NOISE, oldNoise, noise));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConfigPackage.HEURISTIC__HEURISTIC_NAME:
				return getHeuristicName();
			case ConfigPackage.HEURISTIC__NOISE:
				return getNoise();
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
			case ConfigPackage.HEURISTIC__HEURISTIC_NAME:
				setHeuristicName((String)newValue);
				return;
			case ConfigPackage.HEURISTIC__NOISE:
				setNoise((Integer)newValue);
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
			case ConfigPackage.HEURISTIC__HEURISTIC_NAME:
				setHeuristicName(HEURISTIC_NAME_EDEFAULT);
				return;
			case ConfigPackage.HEURISTIC__NOISE:
				setNoise(NOISE_EDEFAULT);
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
			case ConfigPackage.HEURISTIC__HEURISTIC_NAME:
				return HEURISTIC_NAME_EDEFAULT == null ? heuristicName != null : !HEURISTIC_NAME_EDEFAULT.equals(heuristicName);
			case ConfigPackage.HEURISTIC__NOISE:
				return noise != NOISE_EDEFAULT;
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
		result.append(" (heuristicName: ");
		result.append(heuristicName);
		result.append(", noise: ");
		result.append(noise);
		result.append(')');
		return result.toString();
	}

} //HeuristicImpl
