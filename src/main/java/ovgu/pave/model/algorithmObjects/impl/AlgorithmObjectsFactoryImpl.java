/**
 */
package ovgu.pave.model.algorithmObjects.impl;

import java.util.HashMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import ovgu.pave.model.algorithmObjects.*;

import ovgu.pave.model.input.Request;

import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.RouteElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AlgorithmObjectsFactoryImpl extends EFactoryImpl implements AlgorithmObjectsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AlgorithmObjectsFactory init() {
		try {
			AlgorithmObjectsFactory theAlgorithmObjectsFactory = (AlgorithmObjectsFactory)EPackage.Registry.INSTANCE.getEFactory(AlgorithmObjectsPackage.eNS_URI);
			if (theAlgorithmObjectsFactory != null) {
				return theAlgorithmObjectsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AlgorithmObjectsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AlgorithmObjectsFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case AlgorithmObjectsPackage.LNS: return createLNS();
			case AlgorithmObjectsPackage.INSERTION_INFORMATION: return createInsertionInformation();
			case AlgorithmObjectsPackage.INSERTION_OPTION: return createInsertionOption();
			case AlgorithmObjectsPackage.REMOVAL_OPTION: return createRemovalOption();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case AlgorithmObjectsPackage.ROUTE:
				return createRouteFromString(eDataType, initialValue);
			case AlgorithmObjectsPackage.ROUTE_ELEMENT:
				return createRouteElementFromString(eDataType, initialValue);
			case AlgorithmObjectsPackage.LOWEST_COSTS_HASH_MAP:
				return createLowestCostsHashMapFromString(eDataType, initialValue);
			case AlgorithmObjectsPackage.REQUESTS:
				return createRequestsFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case AlgorithmObjectsPackage.ROUTE:
				return convertRouteToString(eDataType, instanceValue);
			case AlgorithmObjectsPackage.ROUTE_ELEMENT:
				return convertRouteElementToString(eDataType, instanceValue);
			case AlgorithmObjectsPackage.LOWEST_COSTS_HASH_MAP:
				return convertLowestCostsHashMapToString(eDataType, instanceValue);
			case AlgorithmObjectsPackage.REQUESTS:
				return convertRequestsToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNS createLNS() {
		LNSImpl lns = new LNSImpl();
		return lns;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InsertionInformation createInsertionInformation() {
		InsertionInformationImpl insertionInformation = new InsertionInformationImpl();
		return insertionInformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InsertionOption createInsertionOption() {
		InsertionOptionImpl insertionOption = new InsertionOptionImpl();
		return insertionOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RemovalOption createRemovalOption() {
		RemovalOptionImpl removalOption = new RemovalOptionImpl();
		return removalOption;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Route createRouteFromString(EDataType eDataType, String initialValue) {
		return (Route)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRouteToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RouteElement createRouteElementFromString(EDataType eDataType, String initialValue) {
		return (RouteElement)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRouteElementToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public HashMap<Request, Long> createLowestCostsHashMapFromString(EDataType eDataType, String initialValue) {
		return (HashMap<Request, Long>)super.createFromString(initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertLowestCostsHashMapToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Request createRequestsFromString(EDataType eDataType, String initialValue) {
		return (Request)super.createFromString(eDataType, initialValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertRequestsToString(EDataType eDataType, Object instanceValue) {
		return super.convertToString(eDataType, instanceValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AlgorithmObjectsPackage getAlgorithmObjectsPackage() {
		return (AlgorithmObjectsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static AlgorithmObjectsPackage getPackage() {
		return AlgorithmObjectsPackage.eINSTANCE;
	}

} //AlgorithmObjectsFactoryImpl
