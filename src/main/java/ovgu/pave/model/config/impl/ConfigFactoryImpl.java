/**
 */
package ovgu.pave.model.config.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import ovgu.pave.model.config.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ConfigFactoryImpl extends EFactoryImpl implements ConfigFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ConfigFactory init() {
		try {
			ConfigFactory theConfigFactory = (ConfigFactory)EPackage.Registry.INSTANCE.getEFactory(ConfigPackage.eNS_URI);
			if (theConfigFactory != null) {
				return theConfigFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ConfigFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConfigFactoryImpl() {
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
			case ConfigPackage.ALGORITHM: return createAlgorithm();
			case ConfigPackage.CONFIG: return createConfig();
			case ConfigPackage.SHORTEST_PATH: return createShortestPath();
			case ConfigPackage.EXPERIMENT: return createExperiment();
			case ConfigPackage.GRAPH_HOPPER: return createGraphHopper();
			case ConfigPackage.LNS: return createLNS();
			case ConfigPackage.HEURISTIC: return createHeuristic();
			case ConfigPackage.RWS: return createRWS();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Algorithm createAlgorithm() {
		AlgorithmImpl algorithm = new AlgorithmImpl();
		return algorithm;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Config createConfig() {
		ConfigImpl config = new ConfigImpl();
		return config;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ShortestPath createShortestPath() {
		ShortestPathImpl shortestPath = new ShortestPathImpl();
		return shortestPath;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Experiment createExperiment() {
		ExperimentImpl experiment = new ExperimentImpl();
		return experiment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GraphHopper createGraphHopper() {
		GraphHopperImpl graphHopper = new GraphHopperImpl();
		return graphHopper;
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
	public Heuristic createHeuristic() {
		HeuristicImpl heuristic = new HeuristicImpl();
		return heuristic;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RWS createRWS() {
		RWSImpl rws = new RWSImpl();
		return rws;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConfigPackage getConfigPackage() {
		return (ConfigPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ConfigPackage getPackage() {
		return ConfigPackage.eINSTANCE;
	}

} //ConfigFactoryImpl
