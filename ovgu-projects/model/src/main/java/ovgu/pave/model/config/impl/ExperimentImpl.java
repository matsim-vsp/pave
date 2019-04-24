/**
 */
package ovgu.pave.model.config.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import ovgu.pave.model.config.ConfigPackage;
import ovgu.pave.model.config.Experiment;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Experiment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.config.impl.ExperimentImpl#getProblem <em>Problem</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ExperimentImpl#getNumberOfRequests <em>Number Of Requests</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ExperimentImpl#getNumberOfVehicles <em>Number Of Vehicles</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ExperimentImpl#getServiceDuration <em>Service Duration</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ExperimentImpl#getTimeWindowLength <em>Time Window Length</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ExperimentImpl#getMaxDelayValue <em>Max Delay Value</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ExperimentImpl#getStartSequence <em>Start Sequence</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ExperimentImpl#getNumberOfInsideIterations <em>Number Of Inside Iterations</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ExperimentImpl#getOutputFolder <em>Output Folder</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ExperimentImpl#getNumberOfOutsideIterations <em>Number Of Outside Iterations</em>}</li>
 *   <li>{@link ovgu.pave.model.config.impl.ExperimentImpl#getVehicleCapacity <em>Vehicle Capacity</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExperimentImpl extends MinimalEObjectImpl.Container implements Experiment {
	/**
	 * The default value of the '{@link #getProblem() <em>Problem</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProblem()
	 * @generated
	 * @ordered
	 */
	protected static final String PROBLEM_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProblem() <em>Problem</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProblem()
	 * @generated
	 * @ordered
	 */
	protected String problem = PROBLEM_EDEFAULT;

	/**
	 * The default value of the '{@link #getNumberOfRequests() <em>Number Of Requests</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfRequests()
	 * @generated
	 * @ordered
	 */
	protected static final int NUMBER_OF_REQUESTS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNumberOfRequests() <em>Number Of Requests</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfRequests()
	 * @generated
	 * @ordered
	 */
	protected int numberOfRequests = NUMBER_OF_REQUESTS_EDEFAULT;

	/**
	 * The default value of the '{@link #getNumberOfVehicles() <em>Number Of Vehicles</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfVehicles()
	 * @generated
	 * @ordered
	 */
	protected static final int NUMBER_OF_VEHICLES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNumberOfVehicles() <em>Number Of Vehicles</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfVehicles()
	 * @generated
	 * @ordered
	 */
	protected int numberOfVehicles = NUMBER_OF_VEHICLES_EDEFAULT;

	/**
	 * The default value of the '{@link #getServiceDuration() <em>Service Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int SERVICE_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getServiceDuration() <em>Service Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getServiceDuration()
	 * @generated
	 * @ordered
	 */
	protected int serviceDuration = SERVICE_DURATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getTimeWindowLength() <em>Time Window Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeWindowLength()
	 * @generated
	 * @ordered
	 */
	protected static final int TIME_WINDOW_LENGTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTimeWindowLength() <em>Time Window Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTimeWindowLength()
	 * @generated
	 * @ordered
	 */
	protected int timeWindowLength = TIME_WINDOW_LENGTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getMaxDelayValue() <em>Max Delay Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxDelayValue()
	 * @generated
	 * @ordered
	 */
	protected static final long MAX_DELAY_VALUE_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getMaxDelayValue() <em>Max Delay Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxDelayValue()
	 * @generated
	 * @ordered
	 */
	protected long maxDelayValue = MAX_DELAY_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getStartSequence() <em>Start Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartSequence()
	 * @generated
	 * @ordered
	 */
	protected static final int START_SEQUENCE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getStartSequence() <em>Start Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartSequence()
	 * @generated
	 * @ordered
	 */
	protected int startSequence = START_SEQUENCE_EDEFAULT;

	/**
	 * The default value of the '{@link #getNumberOfInsideIterations() <em>Number Of Inside Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfInsideIterations()
	 * @generated
	 * @ordered
	 */
	protected static final int NUMBER_OF_INSIDE_ITERATIONS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNumberOfInsideIterations() <em>Number Of Inside Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfInsideIterations()
	 * @generated
	 * @ordered
	 */
	protected int numberOfInsideIterations = NUMBER_OF_INSIDE_ITERATIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #getOutputFolder() <em>Output Folder</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputFolder()
	 * @generated
	 * @ordered
	 */
	protected static final String OUTPUT_FOLDER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOutputFolder() <em>Output Folder</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputFolder()
	 * @generated
	 * @ordered
	 */
	protected String outputFolder = OUTPUT_FOLDER_EDEFAULT;

	/**
	 * The default value of the '{@link #getNumberOfOutsideIterations() <em>Number Of Outside Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfOutsideIterations()
	 * @generated
	 * @ordered
	 */
	protected static final int NUMBER_OF_OUTSIDE_ITERATIONS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNumberOfOutsideIterations() <em>Number Of Outside Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfOutsideIterations()
	 * @generated
	 * @ordered
	 */
	protected int numberOfOutsideIterations = NUMBER_OF_OUTSIDE_ITERATIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #getVehicleCapacity() <em>Vehicle Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicleCapacity()
	 * @generated
	 * @ordered
	 */
	protected static final int VEHICLE_CAPACITY_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVehicleCapacity() <em>Vehicle Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicleCapacity()
	 * @generated
	 * @ordered
	 */
	protected int vehicleCapacity = VEHICLE_CAPACITY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExperimentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ConfigPackage.Literals.EXPERIMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getProblem() {
		return problem;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProblem(String newProblem) {
		String oldProblem = problem;
		problem = newProblem;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.EXPERIMENT__PROBLEM, oldProblem, problem));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNumberOfRequests() {
		return numberOfRequests;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNumberOfRequests(int newNumberOfRequests) {
		int oldNumberOfRequests = numberOfRequests;
		numberOfRequests = newNumberOfRequests;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.EXPERIMENT__NUMBER_OF_REQUESTS, oldNumberOfRequests, numberOfRequests));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNumberOfVehicles() {
		return numberOfVehicles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNumberOfVehicles(int newNumberOfVehicles) {
		int oldNumberOfVehicles = numberOfVehicles;
		numberOfVehicles = newNumberOfVehicles;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.EXPERIMENT__NUMBER_OF_VEHICLES, oldNumberOfVehicles, numberOfVehicles));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getServiceDuration() {
		return serviceDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setServiceDuration(int newServiceDuration) {
		int oldServiceDuration = serviceDuration;
		serviceDuration = newServiceDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.EXPERIMENT__SERVICE_DURATION, oldServiceDuration, serviceDuration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getTimeWindowLength() {
		return timeWindowLength;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTimeWindowLength(int newTimeWindowLength) {
		int oldTimeWindowLength = timeWindowLength;
		timeWindowLength = newTimeWindowLength;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.EXPERIMENT__TIME_WINDOW_LENGTH, oldTimeWindowLength, timeWindowLength));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getMaxDelayValue() {
		return maxDelayValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxDelayValue(long newMaxDelayValue) {
		long oldMaxDelayValue = maxDelayValue;
		maxDelayValue = newMaxDelayValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.EXPERIMENT__MAX_DELAY_VALUE, oldMaxDelayValue, maxDelayValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getStartSequence() {
		return startSequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStartSequence(int newStartSequence) {
		int oldStartSequence = startSequence;
		startSequence = newStartSequence;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.EXPERIMENT__START_SEQUENCE, oldStartSequence, startSequence));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNumberOfInsideIterations() {
		return numberOfInsideIterations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNumberOfInsideIterations(int newNumberOfInsideIterations) {
		int oldNumberOfInsideIterations = numberOfInsideIterations;
		numberOfInsideIterations = newNumberOfInsideIterations;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.EXPERIMENT__NUMBER_OF_INSIDE_ITERATIONS, oldNumberOfInsideIterations, numberOfInsideIterations));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOutputFolder() {
		return outputFolder;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOutputFolder(String newOutputFolder) {
		String oldOutputFolder = outputFolder;
		outputFolder = newOutputFolder;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.EXPERIMENT__OUTPUT_FOLDER, oldOutputFolder, outputFolder));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNumberOfOutsideIterations() {
		return numberOfOutsideIterations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNumberOfOutsideIterations(int newNumberOfOutsideIterations) {
		int oldNumberOfOutsideIterations = numberOfOutsideIterations;
		numberOfOutsideIterations = newNumberOfOutsideIterations;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.EXPERIMENT__NUMBER_OF_OUTSIDE_ITERATIONS, oldNumberOfOutsideIterations, numberOfOutsideIterations));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getVehicleCapacity() {
		return vehicleCapacity;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVehicleCapacity(int newVehicleCapacity) {
		int oldVehicleCapacity = vehicleCapacity;
		vehicleCapacity = newVehicleCapacity;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ConfigPackage.EXPERIMENT__VEHICLE_CAPACITY, oldVehicleCapacity, vehicleCapacity));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ConfigPackage.EXPERIMENT__PROBLEM:
				return getProblem();
			case ConfigPackage.EXPERIMENT__NUMBER_OF_REQUESTS:
				return getNumberOfRequests();
			case ConfigPackage.EXPERIMENT__NUMBER_OF_VEHICLES:
				return getNumberOfVehicles();
			case ConfigPackage.EXPERIMENT__SERVICE_DURATION:
				return getServiceDuration();
			case ConfigPackage.EXPERIMENT__TIME_WINDOW_LENGTH:
				return getTimeWindowLength();
			case ConfigPackage.EXPERIMENT__MAX_DELAY_VALUE:
				return getMaxDelayValue();
			case ConfigPackage.EXPERIMENT__START_SEQUENCE:
				return getStartSequence();
			case ConfigPackage.EXPERIMENT__NUMBER_OF_INSIDE_ITERATIONS:
				return getNumberOfInsideIterations();
			case ConfigPackage.EXPERIMENT__OUTPUT_FOLDER:
				return getOutputFolder();
			case ConfigPackage.EXPERIMENT__NUMBER_OF_OUTSIDE_ITERATIONS:
				return getNumberOfOutsideIterations();
			case ConfigPackage.EXPERIMENT__VEHICLE_CAPACITY:
				return getVehicleCapacity();
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
			case ConfigPackage.EXPERIMENT__PROBLEM:
				setProblem((String)newValue);
				return;
			case ConfigPackage.EXPERIMENT__NUMBER_OF_REQUESTS:
				setNumberOfRequests((Integer)newValue);
				return;
			case ConfigPackage.EXPERIMENT__NUMBER_OF_VEHICLES:
				setNumberOfVehicles((Integer)newValue);
				return;
			case ConfigPackage.EXPERIMENT__SERVICE_DURATION:
				setServiceDuration((Integer)newValue);
				return;
			case ConfigPackage.EXPERIMENT__TIME_WINDOW_LENGTH:
				setTimeWindowLength((Integer)newValue);
				return;
			case ConfigPackage.EXPERIMENT__MAX_DELAY_VALUE:
				setMaxDelayValue((Long)newValue);
				return;
			case ConfigPackage.EXPERIMENT__START_SEQUENCE:
				setStartSequence((Integer)newValue);
				return;
			case ConfigPackage.EXPERIMENT__NUMBER_OF_INSIDE_ITERATIONS:
				setNumberOfInsideIterations((Integer)newValue);
				return;
			case ConfigPackage.EXPERIMENT__OUTPUT_FOLDER:
				setOutputFolder((String)newValue);
				return;
			case ConfigPackage.EXPERIMENT__NUMBER_OF_OUTSIDE_ITERATIONS:
				setNumberOfOutsideIterations((Integer)newValue);
				return;
			case ConfigPackage.EXPERIMENT__VEHICLE_CAPACITY:
				setVehicleCapacity((Integer)newValue);
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
			case ConfigPackage.EXPERIMENT__PROBLEM:
				setProblem(PROBLEM_EDEFAULT);
				return;
			case ConfigPackage.EXPERIMENT__NUMBER_OF_REQUESTS:
				setNumberOfRequests(NUMBER_OF_REQUESTS_EDEFAULT);
				return;
			case ConfigPackage.EXPERIMENT__NUMBER_OF_VEHICLES:
				setNumberOfVehicles(NUMBER_OF_VEHICLES_EDEFAULT);
				return;
			case ConfigPackage.EXPERIMENT__SERVICE_DURATION:
				setServiceDuration(SERVICE_DURATION_EDEFAULT);
				return;
			case ConfigPackage.EXPERIMENT__TIME_WINDOW_LENGTH:
				setTimeWindowLength(TIME_WINDOW_LENGTH_EDEFAULT);
				return;
			case ConfigPackage.EXPERIMENT__MAX_DELAY_VALUE:
				setMaxDelayValue(MAX_DELAY_VALUE_EDEFAULT);
				return;
			case ConfigPackage.EXPERIMENT__START_SEQUENCE:
				setStartSequence(START_SEQUENCE_EDEFAULT);
				return;
			case ConfigPackage.EXPERIMENT__NUMBER_OF_INSIDE_ITERATIONS:
				setNumberOfInsideIterations(NUMBER_OF_INSIDE_ITERATIONS_EDEFAULT);
				return;
			case ConfigPackage.EXPERIMENT__OUTPUT_FOLDER:
				setOutputFolder(OUTPUT_FOLDER_EDEFAULT);
				return;
			case ConfigPackage.EXPERIMENT__NUMBER_OF_OUTSIDE_ITERATIONS:
				setNumberOfOutsideIterations(NUMBER_OF_OUTSIDE_ITERATIONS_EDEFAULT);
				return;
			case ConfigPackage.EXPERIMENT__VEHICLE_CAPACITY:
				setVehicleCapacity(VEHICLE_CAPACITY_EDEFAULT);
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
			case ConfigPackage.EXPERIMENT__PROBLEM:
				return PROBLEM_EDEFAULT == null ? problem != null : !PROBLEM_EDEFAULT.equals(problem);
			case ConfigPackage.EXPERIMENT__NUMBER_OF_REQUESTS:
				return numberOfRequests != NUMBER_OF_REQUESTS_EDEFAULT;
			case ConfigPackage.EXPERIMENT__NUMBER_OF_VEHICLES:
				return numberOfVehicles != NUMBER_OF_VEHICLES_EDEFAULT;
			case ConfigPackage.EXPERIMENT__SERVICE_DURATION:
				return serviceDuration != SERVICE_DURATION_EDEFAULT;
			case ConfigPackage.EXPERIMENT__TIME_WINDOW_LENGTH:
				return timeWindowLength != TIME_WINDOW_LENGTH_EDEFAULT;
			case ConfigPackage.EXPERIMENT__MAX_DELAY_VALUE:
				return maxDelayValue != MAX_DELAY_VALUE_EDEFAULT;
			case ConfigPackage.EXPERIMENT__START_SEQUENCE:
				return startSequence != START_SEQUENCE_EDEFAULT;
			case ConfigPackage.EXPERIMENT__NUMBER_OF_INSIDE_ITERATIONS:
				return numberOfInsideIterations != NUMBER_OF_INSIDE_ITERATIONS_EDEFAULT;
			case ConfigPackage.EXPERIMENT__OUTPUT_FOLDER:
				return OUTPUT_FOLDER_EDEFAULT == null ? outputFolder != null : !OUTPUT_FOLDER_EDEFAULT.equals(outputFolder);
			case ConfigPackage.EXPERIMENT__NUMBER_OF_OUTSIDE_ITERATIONS:
				return numberOfOutsideIterations != NUMBER_OF_OUTSIDE_ITERATIONS_EDEFAULT;
			case ConfigPackage.EXPERIMENT__VEHICLE_CAPACITY:
				return vehicleCapacity != VEHICLE_CAPACITY_EDEFAULT;
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
		result.append(" (problem: ");
		result.append(problem);
		result.append(", numberOfRequests: ");
		result.append(numberOfRequests);
		result.append(", numberOfVehicles: ");
		result.append(numberOfVehicles);
		result.append(", serviceDuration: ");
		result.append(serviceDuration);
		result.append(", timeWindowLength: ");
		result.append(timeWindowLength);
		result.append(", maxDelayValue: ");
		result.append(maxDelayValue);
		result.append(", startSequence: ");
		result.append(startSequence);
		result.append(", numberOfInsideIterations: ");
		result.append(numberOfInsideIterations);
		result.append(", outputFolder: ");
		result.append(outputFolder);
		result.append(", numberOfOutsideIterations: ");
		result.append(numberOfOutsideIterations);
		result.append(", vehicleCapacity: ");
		result.append(vehicleCapacity);
		result.append(')');
		return result.toString();
	}

} //ExperimentImpl
