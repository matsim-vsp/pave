/**
 */
package ovgu.pave.model.input.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import ovgu.pave.model.input.Edge;
import ovgu.pave.model.input.Input;
import ovgu.pave.model.input.InputPackage;
import ovgu.pave.model.input.Location;
import ovgu.pave.model.input.Requests;
import ovgu.pave.model.input.Vehicle;
import ovgu.pave.model.input.VehicleType;

import ovgu.pave.model.solution.Solution;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Input</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link ovgu.pave.model.input.impl.InputImpl#getRequests <em>Requests</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.InputImpl#getVehicles <em>Vehicles</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.InputImpl#getVehicleTypes <em>Vehicle Types</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.InputImpl#getLocations <em>Locations</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.InputImpl#getEdges <em>Edges</em>}</li>
 *   <li>{@link ovgu.pave.model.input.impl.InputImpl#getSolution <em>Solution</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InputImpl extends MinimalEObjectImpl.Container implements Input {
	/**
	 * The cached value of the '{@link #getRequests() <em>Requests</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequests()
	 * @generated
	 * @ordered
	 */
	protected Requests requests;

	/**
	 * The cached value of the '{@link #getVehicles() <em>Vehicles</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicles()
	 * @generated
	 * @ordered
	 */
	protected EList<Vehicle> vehicles;

	/**
	 * The cached value of the '{@link #getVehicleTypes() <em>Vehicle Types</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVehicleTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<VehicleType> vehicleTypes;

	/**
	 * The cached value of the '{@link #getLocations() <em>Locations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLocations()
	 * @generated
	 * @ordered
	 */
	protected EList<Location> locations;

	/**
	 * The cached value of the '{@link #getEdges() <em>Edges</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEdges()
	 * @generated
	 * @ordered
	 */
	protected EList<Edge> edges;

	/**
	 * The default value of the '{@link #getSolution() <em>Solution</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSolution()
	 * @generated
	 * @ordered
	 */
	protected static final Solution SOLUTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSolution() <em>Solution</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSolution()
	 * @generated
	 * @ordered
	 */
	protected Solution solution = SOLUTION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InputImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return InputPackage.Literals.INPUT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Requests getRequests() {
		return requests;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRequests(Requests newRequests, NotificationChain msgs) {
		Requests oldRequests = requests;
		requests = newRequests;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, InputPackage.INPUT__REQUESTS, oldRequests, newRequests);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRequests(Requests newRequests) {
		if (newRequests != requests) {
			NotificationChain msgs = null;
			if (requests != null)
				msgs = ((InternalEObject)requests).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - InputPackage.INPUT__REQUESTS, null, msgs);
			if (newRequests != null)
				msgs = ((InternalEObject)newRequests).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - InputPackage.INPUT__REQUESTS, null, msgs);
			msgs = basicSetRequests(newRequests, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.INPUT__REQUESTS, newRequests, newRequests));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Vehicle> getVehicles() {
		if (vehicles == null) {
			vehicles = new EObjectContainmentEList<Vehicle>(Vehicle.class, this, InputPackage.INPUT__VEHICLES);
		}
		return vehicles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VehicleType> getVehicleTypes() {
		if (vehicleTypes == null) {
			vehicleTypes = new EObjectContainmentEList<VehicleType>(VehicleType.class, this, InputPackage.INPUT__VEHICLE_TYPES);
		}
		return vehicleTypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Location> getLocations() {
		if (locations == null) {
			locations = new EObjectContainmentEList<Location>(Location.class, this, InputPackage.INPUT__LOCATIONS);
		}
		return locations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Edge> getEdges() {
		if (edges == null) {
			edges = new EObjectContainmentEList<Edge>(Edge.class, this, InputPackage.INPUT__EDGES);
		}
		return edges;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Solution getSolution() {
		return solution;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSolution(Solution newSolution) {
		Solution oldSolution = solution;
		solution = newSolution;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.INPUT__SOLUTION, oldSolution, solution));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case InputPackage.INPUT__REQUESTS:
				return basicSetRequests(null, msgs);
			case InputPackage.INPUT__VEHICLES:
				return ((InternalEList<?>)getVehicles()).basicRemove(otherEnd, msgs);
			case InputPackage.INPUT__VEHICLE_TYPES:
				return ((InternalEList<?>)getVehicleTypes()).basicRemove(otherEnd, msgs);
			case InputPackage.INPUT__LOCATIONS:
				return ((InternalEList<?>)getLocations()).basicRemove(otherEnd, msgs);
			case InputPackage.INPUT__EDGES:
				return ((InternalEList<?>)getEdges()).basicRemove(otherEnd, msgs);
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
			case InputPackage.INPUT__REQUESTS:
				return getRequests();
			case InputPackage.INPUT__VEHICLES:
				return getVehicles();
			case InputPackage.INPUT__VEHICLE_TYPES:
				return getVehicleTypes();
			case InputPackage.INPUT__LOCATIONS:
				return getLocations();
			case InputPackage.INPUT__EDGES:
				return getEdges();
			case InputPackage.INPUT__SOLUTION:
				return getSolution();
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
			case InputPackage.INPUT__REQUESTS:
				setRequests((Requests)newValue);
				return;
			case InputPackage.INPUT__VEHICLES:
				getVehicles().clear();
				getVehicles().addAll((Collection<? extends Vehicle>)newValue);
				return;
			case InputPackage.INPUT__VEHICLE_TYPES:
				getVehicleTypes().clear();
				getVehicleTypes().addAll((Collection<? extends VehicleType>)newValue);
				return;
			case InputPackage.INPUT__LOCATIONS:
				getLocations().clear();
				getLocations().addAll((Collection<? extends Location>)newValue);
				return;
			case InputPackage.INPUT__EDGES:
				getEdges().clear();
				getEdges().addAll((Collection<? extends Edge>)newValue);
				return;
			case InputPackage.INPUT__SOLUTION:
				setSolution((Solution)newValue);
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
			case InputPackage.INPUT__REQUESTS:
				setRequests((Requests)null);
				return;
			case InputPackage.INPUT__VEHICLES:
				getVehicles().clear();
				return;
			case InputPackage.INPUT__VEHICLE_TYPES:
				getVehicleTypes().clear();
				return;
			case InputPackage.INPUT__LOCATIONS:
				getLocations().clear();
				return;
			case InputPackage.INPUT__EDGES:
				getEdges().clear();
				return;
			case InputPackage.INPUT__SOLUTION:
				setSolution(SOLUTION_EDEFAULT);
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
			case InputPackage.INPUT__REQUESTS:
				return requests != null;
			case InputPackage.INPUT__VEHICLES:
				return vehicles != null && !vehicles.isEmpty();
			case InputPackage.INPUT__VEHICLE_TYPES:
				return vehicleTypes != null && !vehicleTypes.isEmpty();
			case InputPackage.INPUT__LOCATIONS:
				return locations != null && !locations.isEmpty();
			case InputPackage.INPUT__EDGES:
				return edges != null && !edges.isEmpty();
			case InputPackage.INPUT__SOLUTION:
				return SOLUTION_EDEFAULT == null ? solution != null : !SOLUTION_EDEFAULT.equals(solution);
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
		result.append(" (solution: ");
		result.append(solution);
		result.append(')');
		return result.toString();
	}

} //InputImpl
