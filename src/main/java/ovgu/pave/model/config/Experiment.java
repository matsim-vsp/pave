/**
 */
package ovgu.pave.model.config;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Experiment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.config.Experiment#getProblem <em>Problem</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Experiment#getNumberOfRequests <em>Number Of Requests</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Experiment#getNumberOfVehicles <em>Number Of Vehicles</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Experiment#getServiceDuration <em>Service Duration</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Experiment#getTimeWindowLength <em>Time Window Length</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Experiment#getMaxDelayValue <em>Max Delay Value</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Experiment#getStartSequence <em>Start Sequence</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Experiment#getNumberOfInsideIterations <em>Number Of Inside Iterations</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Experiment#getOutputFolder <em>Output Folder</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Experiment#getNumberOfOutsideIterations <em>Number Of Outside Iterations</em>}</li>
 *   <li>{@link ovgu.pave.model.config.Experiment#getVehicleCapacity <em>Vehicle Capacity</em>}</li>
 * </ul>
 *
 * @see ovgu.pave.model.config.ConfigPackage#getExperiment()
 * @model
 * @generated
 */
public interface Experiment extends EObject {
	/**
	 * Returns the value of the '<em><b>Problem</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Problem</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Problem</em>' attribute.
	 * @see #setProblem(String)
	 * @see ovgu.pave.model.config.ConfigPackage#getExperiment_Problem()
	 * @model
	 * @generated
	 */
	String getProblem();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Experiment#getProblem <em>Problem</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Problem</em>' attribute.
	 * @see #getProblem()
	 * @generated
	 */
	void setProblem(String value);

	/**
	 * Returns the value of the '<em><b>Number Of Requests</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Number Of Requests</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Number Of Requests</em>' attribute.
	 * @see #setNumberOfRequests(int)
	 * @see ovgu.pave.model.config.ConfigPackage#getExperiment_NumberOfRequests()
	 * @model
	 * @generated
	 */
	int getNumberOfRequests();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Experiment#getNumberOfRequests <em>Number Of Requests</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Number Of Requests</em>' attribute.
	 * @see #getNumberOfRequests()
	 * @generated
	 */
	void setNumberOfRequests(int value);

	/**
	 * Returns the value of the '<em><b>Number Of Vehicles</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Number Of Vehicles</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Number Of Vehicles</em>' attribute.
	 * @see #setNumberOfVehicles(int)
	 * @see ovgu.pave.model.config.ConfigPackage#getExperiment_NumberOfVehicles()
	 * @model
	 * @generated
	 */
	int getNumberOfVehicles();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Experiment#getNumberOfVehicles <em>Number Of Vehicles</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Number Of Vehicles</em>' attribute.
	 * @see #getNumberOfVehicles()
	 * @generated
	 */
	void setNumberOfVehicles(int value);

	/**
	 * Returns the value of the '<em><b>Service Duration</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Service Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Service Duration</em>' attribute.
	 * @see #setServiceDuration(int)
	 * @see ovgu.pave.model.config.ConfigPackage#getExperiment_ServiceDuration()
	 * @model default="0"
	 * @generated
	 */
	int getServiceDuration();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Experiment#getServiceDuration <em>Service Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Service Duration</em>' attribute.
	 * @see #getServiceDuration()
	 * @generated
	 */
	void setServiceDuration(int value);

	/**
	 * Returns the value of the '<em><b>Time Window Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Time Window Length</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Time Window Length</em>' attribute.
	 * @see #setTimeWindowLength(int)
	 * @see ovgu.pave.model.config.ConfigPackage#getExperiment_TimeWindowLength()
	 * @model
	 * @generated
	 */
	int getTimeWindowLength();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Experiment#getTimeWindowLength <em>Time Window Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time Window Length</em>' attribute.
	 * @see #getTimeWindowLength()
	 * @generated
	 */
	void setTimeWindowLength(int value);

	/**
	 * Returns the value of the '<em><b>Max Delay Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Delay Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Delay Value</em>' attribute.
	 * @see #setMaxDelayValue(long)
	 * @see ovgu.pave.model.config.ConfigPackage#getExperiment_MaxDelayValue()
	 * @model
	 * @generated
	 */
	long getMaxDelayValue();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Experiment#getMaxDelayValue <em>Max Delay Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Delay Value</em>' attribute.
	 * @see #getMaxDelayValue()
	 * @generated
	 */
	void setMaxDelayValue(long value);

	/**
	 * Returns the value of the '<em><b>Start Sequence</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Start Sequence</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Sequence</em>' attribute.
	 * @see #setStartSequence(int)
	 * @see ovgu.pave.model.config.ConfigPackage#getExperiment_StartSequence()
	 * @model default="0"
	 * @generated
	 */
	int getStartSequence();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Experiment#getStartSequence <em>Start Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Sequence</em>' attribute.
	 * @see #getStartSequence()
	 * @generated
	 */
	void setStartSequence(int value);

	/**
	 * Returns the value of the '<em><b>Number Of Inside Iterations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Number Of Inside Iterations</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Number Of Inside Iterations</em>' attribute.
	 * @see #setNumberOfInsideIterations(int)
	 * @see ovgu.pave.model.config.ConfigPackage#getExperiment_NumberOfInsideIterations()
	 * @model
	 * @generated
	 */
	int getNumberOfInsideIterations();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Experiment#getNumberOfInsideIterations <em>Number Of Inside Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Number Of Inside Iterations</em>' attribute.
	 * @see #getNumberOfInsideIterations()
	 * @generated
	 */
	void setNumberOfInsideIterations(int value);

	/**
	 * Returns the value of the '<em><b>Output Folder</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Output Folder</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output Folder</em>' attribute.
	 * @see #setOutputFolder(String)
	 * @see ovgu.pave.model.config.ConfigPackage#getExperiment_OutputFolder()
	 * @model
	 * @generated
	 */
	String getOutputFolder();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Experiment#getOutputFolder <em>Output Folder</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Output Folder</em>' attribute.
	 * @see #getOutputFolder()
	 * @generated
	 */
	void setOutputFolder(String value);

	/**
	 * Returns the value of the '<em><b>Number Of Outside Iterations</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Number Of Outside Iterations</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Number Of Outside Iterations</em>' attribute.
	 * @see #setNumberOfOutsideIterations(int)
	 * @see ovgu.pave.model.config.ConfigPackage#getExperiment_NumberOfOutsideIterations()
	 * @model
	 * @generated
	 */
	int getNumberOfOutsideIterations();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Experiment#getNumberOfOutsideIterations <em>Number Of Outside Iterations</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Number Of Outside Iterations</em>' attribute.
	 * @see #getNumberOfOutsideIterations()
	 * @generated
	 */
	void setNumberOfOutsideIterations(int value);

	/**
	 * Returns the value of the '<em><b>Vehicle Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vehicle Capacity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vehicle Capacity</em>' attribute.
	 * @see #setVehicleCapacity(int)
	 * @see ovgu.pave.model.config.ConfigPackage#getExperiment_VehicleCapacity()
	 * @model
	 * @generated
	 */
	int getVehicleCapacity();

	/**
	 * Sets the value of the '{@link ovgu.pave.model.config.Experiment#getVehicleCapacity <em>Vehicle Capacity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vehicle Capacity</em>' attribute.
	 * @see #getVehicleCapacity()
	 * @generated
	 */
	void setVehicleCapacity(int value);

} // Experiment
