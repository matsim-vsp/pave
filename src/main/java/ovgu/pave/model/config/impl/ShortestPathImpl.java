/**
 */
package ovgu.pave.model.config.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import ovgu.pave.model.config.ConfigPackage;
import ovgu.pave.model.config.GraphHopper;
import ovgu.pave.model.config.ShortestPath;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Shortest Path</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.config.impl.ShortestPathImpl#getUse <em>Use</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ShortestPathImpl#getGraphhopper <em>Graphhopper</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ShortestPathImpl extends MinimalEObjectImpl.Container implements ShortestPath {
	/**
	 * The default value of the '{@link #getUse() <em>Use</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUse()
	 * @generated
	 * @ordered
	 */
	protected static final String USE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUse() <em>Use</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUse()
	 * @generated
	 * @ordered
	 */
	protected String use = USE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getGraphhopper() <em>Graphhopper</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGraphhopper()
	 * @generated
	 * @ordered
	 */
	protected GraphHopper graphhopper;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ShortestPathImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigPackage.Literals.SHORTEST_PATH;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getUse() {
		return use;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUse(String newUse) {
		String oldUse = use;
		use = newUse;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.SHORTEST_PATH__USE, oldUse, use));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GraphHopper getGraphhopper() {
		return graphhopper;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGraphhopper(GraphHopper newGraphhopper, NotificationChain msgs) {
		GraphHopper oldGraphhopper = graphhopper;
		graphhopper = newGraphhopper;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigPackage.SHORTEST_PATH__GRAPHHOPPER, oldGraphhopper, newGraphhopper);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGraphhopper(GraphHopper newGraphhopper) {
		if (newGraphhopper != graphhopper) {
			NotificationChain msgs = null;
			if (graphhopper != null)
				msgs = ((InternalEObject)graphhopper).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigPackage.SHORTEST_PATH__GRAPHHOPPER, null, msgs);
			if (newGraphhopper != null)
				msgs = ((InternalEObject)newGraphhopper).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigPackage.SHORTEST_PATH__GRAPHHOPPER, null, msgs);
			msgs = basicSetGraphhopper(newGraphhopper, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.SHORTEST_PATH__GRAPHHOPPER, newGraphhopper, newGraphhopper));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigPackage.SHORTEST_PATH__GRAPHHOPPER:
				return basicSetGraphhopper(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConfigPackage.SHORTEST_PATH__USE:
				return getUse();
			case ConfigPackage.SHORTEST_PATH__GRAPHHOPPER:
				return getGraphhopper();
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
			case ConfigPackage.SHORTEST_PATH__USE:
				setUse((String)newValue);
				return;
			case ConfigPackage.SHORTEST_PATH__GRAPHHOPPER:
				setGraphhopper((GraphHopper)newValue);
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
			case ConfigPackage.SHORTEST_PATH__USE:
				setUse(USE_EDEFAULT);
				return;
			case ConfigPackage.SHORTEST_PATH__GRAPHHOPPER:
				setGraphhopper((GraphHopper)null);
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
			case ConfigPackage.SHORTEST_PATH__USE:
				return USE_EDEFAULT == null ? use != null : !USE_EDEFAULT.equals(use);
			case ConfigPackage.SHORTEST_PATH__GRAPHHOPPER:
				return graphhopper != null;
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
		result.append(" (use: ");
		result.append(use);
		result.append(')');
		return result.toString();
	}

} //ShortestPathImpl
