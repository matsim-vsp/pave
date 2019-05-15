/**
 */
package ovgu.pave.model.config.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import ovgu.pave.model.config.ConfigPackage;
import ovgu.pave.model.config.RWS;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>RWS</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.config.impl.RWSImpl#getCase1 <em>Case1</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.RWSImpl#getCase2 <em>Case2</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.RWSImpl#getCase3 <em>Case3</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.RWSImpl#getLastScoreInfluence <em>Last Score Influence</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RWSImpl extends MinimalEObjectImpl.Container implements RWS {
	/**
	 * The default value of the '{@link #getCase1() <em>Case1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCase1()
	 * @generated
	 * @ordered
	 */
	protected static final int CASE1_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCase1() <em>Case1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCase1()
	 * @generated
	 * @ordered
	 */
	protected int case1 = CASE1_EDEFAULT;

	/**
	 * The default value of the '{@link #getCase2() <em>Case2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCase2()
	 * @generated
	 * @ordered
	 */
	protected static final int CASE2_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCase2() <em>Case2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCase2()
	 * @generated
	 * @ordered
	 */
	protected int case2 = CASE2_EDEFAULT;

	/**
	 * The default value of the '{@link #getCase3() <em>Case3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCase3()
	 * @generated
	 * @ordered
	 */
	protected static final int CASE3_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCase3() <em>Case3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCase3()
	 * @generated
	 * @ordered
	 */
	protected int case3 = CASE3_EDEFAULT;

	/**
	 * The default value of the '{@link #getLastScoreInfluence() <em>Last Score Influence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastScoreInfluence()
	 * @generated
	 * @ordered
	 */
	protected static final double LAST_SCORE_INFLUENCE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getLastScoreInfluence() <em>Last Score Influence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLastScoreInfluence()
	 * @generated
	 * @ordered
	 */
	protected double lastScoreInfluence = LAST_SCORE_INFLUENCE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RWSImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigPackage.Literals.RWS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCase1() {
		return case1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCase1(int newCase1) {
		int oldCase1 = case1;
		case1 = newCase1;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.RWS__CASE1, oldCase1, case1));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCase2() {
		return case2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCase2(int newCase2) {
		int oldCase2 = case2;
		case2 = newCase2;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.RWS__CASE2, oldCase2, case2));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCase3() {
		return case3;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCase3(int newCase3) {
		int oldCase3 = case3;
		case3 = newCase3;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.RWS__CASE3, oldCase3, case3));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getLastScoreInfluence() {
		return lastScoreInfluence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLastScoreInfluence(double newLastScoreInfluence) {
		double oldLastScoreInfluence = lastScoreInfluence;
		lastScoreInfluence = newLastScoreInfluence;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.RWS__LAST_SCORE_INFLUENCE, oldLastScoreInfluence, lastScoreInfluence));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConfigPackage.RWS__CASE1:
				return getCase1();
			case ConfigPackage.RWS__CASE2:
				return getCase2();
			case ConfigPackage.RWS__CASE3:
				return getCase3();
			case ConfigPackage.RWS__LAST_SCORE_INFLUENCE:
				return getLastScoreInfluence();
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
			case ConfigPackage.RWS__CASE1:
				setCase1((Integer)newValue);
				return;
			case ConfigPackage.RWS__CASE2:
				setCase2((Integer)newValue);
				return;
			case ConfigPackage.RWS__CASE3:
				setCase3((Integer)newValue);
				return;
			case ConfigPackage.RWS__LAST_SCORE_INFLUENCE:
				setLastScoreInfluence((Double)newValue);
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
			case ConfigPackage.RWS__CASE1:
				setCase1(CASE1_EDEFAULT);
				return;
			case ConfigPackage.RWS__CASE2:
				setCase2(CASE2_EDEFAULT);
				return;
			case ConfigPackage.RWS__CASE3:
				setCase3(CASE3_EDEFAULT);
				return;
			case ConfigPackage.RWS__LAST_SCORE_INFLUENCE:
				setLastScoreInfluence(LAST_SCORE_INFLUENCE_EDEFAULT);
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
			case ConfigPackage.RWS__CASE1:
				return case1 != CASE1_EDEFAULT;
			case ConfigPackage.RWS__CASE2:
				return case2 != CASE2_EDEFAULT;
			case ConfigPackage.RWS__CASE3:
				return case3 != CASE3_EDEFAULT;
			case ConfigPackage.RWS__LAST_SCORE_INFLUENCE:
				return lastScoreInfluence != LAST_SCORE_INFLUENCE_EDEFAULT;
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
		result.append(" (Case1: ");
		result.append(case1);
		result.append(", Case2: ");
		result.append(case2);
		result.append(", Case3: ");
		result.append(case3);
		result.append(", lastScoreInfluence: ");
		result.append(lastScoreInfluence);
		result.append(')');
		return result.toString();
	}

} //RWSImpl
