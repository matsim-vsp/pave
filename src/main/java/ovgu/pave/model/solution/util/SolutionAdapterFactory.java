/**
 */
package ovgu.pave.model.solution.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import ovgu.pave.model.solution.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see ovgu.pave.model.solution.SolutionPackage
 * @generated
 */
public class SolutionAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static SolutionPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SolutionAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = SolutionPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SolutionSwitch<Adapter> modelSwitch =
		new SolutionSwitch<Adapter>() {
			@Override
			public Adapter caseRoute(Route object) {
				return createRouteAdapter();
			}
			@Override
			public Adapter caseRouteElement(RouteElement object) {
				return createRouteElementAdapter();
			}
			@Override
			public Adapter caseSolution(Solution object) {
				return createSolutionAdapter();
			}
			@Override
			public Adapter caseSupportRouteElement(SupportRouteElement object) {
				return createSupportRouteElementAdapter();
			}
			@Override
			public Adapter caseRequestActivityRouteElement(RequestActivityRouteElement object) {
				return createRequestActivityRouteElementAdapter();
			}
			@Override
			public Adapter caseWayPointRouteElement(WayPointRouteElement object) {
				return createWayPointRouteElementAdapter();
			}
			@Override
			public Adapter caseUseVehicleType(UseVehicleType object) {
				return createUseVehicleTypeAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link ovgu.pave.model.solution.Route <em>Route</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ovgu.pave.model.solution.Route
	 * @generated
	 */
	public Adapter createRouteAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link ovgu.pave.model.solution.RouteElement <em>Route Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ovgu.pave.model.solution.RouteElement
	 * @generated
	 */
	public Adapter createRouteElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link ovgu.pave.model.solution.Solution <em>Solution</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ovgu.pave.model.solution.Solution
	 * @generated
	 */
	public Adapter createSolutionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link ovgu.pave.model.solution.SupportRouteElement <em>Support Route Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ovgu.pave.model.solution.SupportRouteElement
	 * @generated
	 */
	public Adapter createSupportRouteElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link ovgu.pave.model.solution.RequestActivityRouteElement <em>Request Activity Route Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ovgu.pave.model.solution.RequestActivityRouteElement
	 * @generated
	 */
	public Adapter createRequestActivityRouteElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link ovgu.pave.model.solution.WayPointRouteElement <em>Way Point Route Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ovgu.pave.model.solution.WayPointRouteElement
	 * @generated
	 */
	public Adapter createWayPointRouteElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link ovgu.pave.model.solution.UseVehicleType <em>Use Vehicle Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see ovgu.pave.model.solution.UseVehicleType
	 * @generated
	 */
	public Adapter createUseVehicleTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //SolutionAdapterFactory
