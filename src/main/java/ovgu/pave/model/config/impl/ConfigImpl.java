/**
 */
package ovgu.pave.model.config.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import ovgu.pave.model.config.Algorithm;
import ovgu.pave.model.config.Config;
import ovgu.pave.model.config.ConfigPackage;
import ovgu.pave.model.config.Experiment;
import ovgu.pave.model.config.ShortestPath;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Config</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.config.impl.ConfigImpl#getInputFolder <em>Input Folder</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ConfigImpl#getShortestPath <em>Shortest Path</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ConfigImpl#getExperiment <em>Experiment</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ConfigImpl#getInputFilename <em>Input Filename</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ConfigImpl#getAlgorithm <em>Algorithm</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ConfigImpl#getConfigPath <em>Config Path</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConfigImpl extends MinimalEObjectImpl.Container implements Config {
	/**
	 * The default value of the '{@link #getInputFolder() <em>Input Folder</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputFolder()
	 * @generated
	 * @ordered
	 */
	protected static final String INPUT_FOLDER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getInputFolder() <em>Input Folder</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputFolder()
	 * @generated
	 * @ordered
	 */
	protected String inputFolder = INPUT_FOLDER_EDEFAULT;

	/**
	 * The cached value of the '{@link #getShortestPath() <em>Shortest Path</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getShortestPath()
	 * @generated
	 * @ordered
	 */
	protected ShortestPath shortestPath;

	/**
	 * The cached value of the '{@link #getExperiment() <em>Experiment</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExperiment()
	 * @generated
	 * @ordered
	 */
	protected Experiment experiment;

	/**
	 * The default value of the '{@link #getInputFilename() <em>Input Filename</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputFilename()
	 * @generated
	 * @ordered
	 */
	protected static final String INPUT_FILENAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getInputFilename() <em>Input Filename</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputFilename()
	 * @generated
	 * @ordered
	 */
	protected String inputFilename = INPUT_FILENAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAlgorithm() <em>Algorithm</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAlgorithm()
	 * @generated
	 * @ordered
	 */
	protected Algorithm algorithm;

	/**
	 * The default value of the '{@link #getConfigPath() <em>Config Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConfigPath()
	 * @generated
	 * @ordered
	 */
	protected static final String CONFIG_PATH_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getConfigPath() <em>Config Path</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConfigPath()
	 * @generated
	 * @ordered
	 */
	protected String configPath = CONFIG_PATH_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConfigImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigPackage.Literals.CONFIG;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getInputFolder() {
		return inputFolder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInputFolder(String newInputFolder) {
		String oldInputFolder = inputFolder;
		inputFolder = newInputFolder;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.CONFIG__INPUT_FOLDER, oldInputFolder, inputFolder));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ShortestPath getShortestPath() {
		return shortestPath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetShortestPath(ShortestPath newShortestPath, NotificationChain msgs) {
		ShortestPath oldShortestPath = shortestPath;
		shortestPath = newShortestPath;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigPackage.CONFIG__SHORTEST_PATH, oldShortestPath, newShortestPath);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setShortestPath(ShortestPath newShortestPath) {
		if (newShortestPath != shortestPath) {
			NotificationChain msgs = null;
			if (shortestPath != null)
				msgs = ((InternalEObject)shortestPath).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigPackage.CONFIG__SHORTEST_PATH, null, msgs);
			if (newShortestPath != null)
				msgs = ((InternalEObject)newShortestPath).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigPackage.CONFIG__SHORTEST_PATH, null, msgs);
			msgs = basicSetShortestPath(newShortestPath, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.CONFIG__SHORTEST_PATH, newShortestPath, newShortestPath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Experiment getExperiment() {
		return experiment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetExperiment(Experiment newExperiment, NotificationChain msgs) {
		Experiment oldExperiment = experiment;
		experiment = newExperiment;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigPackage.CONFIG__EXPERIMENT, oldExperiment, newExperiment);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExperiment(Experiment newExperiment) {
		if (newExperiment != experiment) {
			NotificationChain msgs = null;
			if (experiment != null)
				msgs = ((InternalEObject)experiment).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigPackage.CONFIG__EXPERIMENT, null, msgs);
			if (newExperiment != null)
				msgs = ((InternalEObject)newExperiment).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigPackage.CONFIG__EXPERIMENT, null, msgs);
			msgs = basicSetExperiment(newExperiment, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.CONFIG__EXPERIMENT, newExperiment, newExperiment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getInputFilename() {
		return inputFilename;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInputFilename(String newInputFilename) {
		String oldInputFilename = inputFilename;
		inputFilename = newInputFilename;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.CONFIG__INPUT_FILENAME, oldInputFilename, inputFilename));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Algorithm getAlgorithm() {
		return algorithm;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAlgorithm(Algorithm newAlgorithm, NotificationChain msgs) {
		Algorithm oldAlgorithm = algorithm;
		algorithm = newAlgorithm;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ConfigPackage.CONFIG__ALGORITHM, oldAlgorithm, newAlgorithm);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAlgorithm(Algorithm newAlgorithm) {
		if (newAlgorithm != algorithm) {
			NotificationChain msgs = null;
			if (algorithm != null)
				msgs = ((InternalEObject)algorithm).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ConfigPackage.CONFIG__ALGORITHM, null, msgs);
			if (newAlgorithm != null)
				msgs = ((InternalEObject)newAlgorithm).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ConfigPackage.CONFIG__ALGORITHM, null, msgs);
			msgs = basicSetAlgorithm(newAlgorithm, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.CONFIG__ALGORITHM, newAlgorithm, newAlgorithm));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getConfigPath() {
		return configPath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConfigPath(String newConfigPath) {
		String oldConfigPath = configPath;
		configPath = newConfigPath;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.CONFIG__CONFIG_PATH, oldConfigPath, configPath));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ConfigPackage.CONFIG__SHORTEST_PATH:
				return basicSetShortestPath(null, msgs);
			case ConfigPackage.CONFIG__EXPERIMENT:
				return basicSetExperiment(null, msgs);
			case ConfigPackage.CONFIG__ALGORITHM:
				return basicSetAlgorithm(null, msgs);
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
			case ConfigPackage.CONFIG__INPUT_FOLDER:
				return getInputFolder();
			case ConfigPackage.CONFIG__SHORTEST_PATH:
				return getShortestPath();
			case ConfigPackage.CONFIG__EXPERIMENT:
				return getExperiment();
			case ConfigPackage.CONFIG__INPUT_FILENAME:
				return getInputFilename();
			case ConfigPackage.CONFIG__ALGORITHM:
				return getAlgorithm();
			case ConfigPackage.CONFIG__CONFIG_PATH:
				return getConfigPath();
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
			case ConfigPackage.CONFIG__INPUT_FOLDER:
				setInputFolder((String)newValue);
				return;
			case ConfigPackage.CONFIG__SHORTEST_PATH:
				setShortestPath((ShortestPath)newValue);
				return;
			case ConfigPackage.CONFIG__EXPERIMENT:
				setExperiment((Experiment)newValue);
				return;
			case ConfigPackage.CONFIG__INPUT_FILENAME:
				setInputFilename((String)newValue);
				return;
			case ConfigPackage.CONFIG__ALGORITHM:
				setAlgorithm((Algorithm)newValue);
				return;
			case ConfigPackage.CONFIG__CONFIG_PATH:
				setConfigPath((String)newValue);
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
			case ConfigPackage.CONFIG__INPUT_FOLDER:
				setInputFolder(INPUT_FOLDER_EDEFAULT);
				return;
			case ConfigPackage.CONFIG__SHORTEST_PATH:
				setShortestPath((ShortestPath)null);
				return;
			case ConfigPackage.CONFIG__EXPERIMENT:
				setExperiment((Experiment)null);
				return;
			case ConfigPackage.CONFIG__INPUT_FILENAME:
				setInputFilename(INPUT_FILENAME_EDEFAULT);
				return;
			case ConfigPackage.CONFIG__ALGORITHM:
				setAlgorithm((Algorithm)null);
				return;
			case ConfigPackage.CONFIG__CONFIG_PATH:
				setConfigPath(CONFIG_PATH_EDEFAULT);
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
			case ConfigPackage.CONFIG__INPUT_FOLDER:
				return INPUT_FOLDER_EDEFAULT == null ? inputFolder != null : !INPUT_FOLDER_EDEFAULT.equals(inputFolder);
			case ConfigPackage.CONFIG__SHORTEST_PATH:
				return shortestPath != null;
			case ConfigPackage.CONFIG__EXPERIMENT:
				return experiment != null;
			case ConfigPackage.CONFIG__INPUT_FILENAME:
				return INPUT_FILENAME_EDEFAULT == null ? inputFilename != null : !INPUT_FILENAME_EDEFAULT.equals(inputFilename);
			case ConfigPackage.CONFIG__ALGORITHM:
				return algorithm != null;
			case ConfigPackage.CONFIG__CONFIG_PATH:
				return CONFIG_PATH_EDEFAULT == null ? configPath != null : !CONFIG_PATH_EDEFAULT.equals(configPath);
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
		result.append(" (inputFolder: ");
		result.append(inputFolder);
		result.append(", inputFilename: ");
		result.append(inputFilename);
		result.append(", configPath: ");
		result.append(configPath);
		result.append(')');
		return result.toString();
	}

} //ConfigImpl
