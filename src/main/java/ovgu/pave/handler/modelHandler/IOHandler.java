package ovgu.pave.handler.modelHandler;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.XMLResource.XMLMap;
import org.eclipse.emf.ecore.xmi.impl.XMLMapImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceImpl;

import ovgu.pave.model.config.Config;
import ovgu.pave.model.config.ConfigPackage;
import ovgu.pave.model.input.Input;
import ovgu.pave.model.input.InputPackage;
import ovgu.pave.model.solution.RequestActivityRouteElement;
import ovgu.pave.model.solution.Route;
import ovgu.pave.model.solution.RouteElement;
import ovgu.pave.model.solution.Solution;
import ovgu.pave.model.solution.SolutionPackage;
import ovgu.pave.model.solution.SupportRouteElement;
import ovgu.pave.model.solution.WayPointRouteElement;

public class IOHandler {

	protected static void saveConfig(Config config, String path) {
		save(config, path, false);
	}

	protected static void saveInput(Input input, String configInputFolder, String filename) {
		save(input, getPath(configInputFolder, filename), true);
	}

	protected static void saveSolution(Solution solution, String configOutputFolder, String filename) {
		save(solution, getPath(configOutputFolder, filename), false);
	}

	protected static Config loadConfig(String path) {
		return load(Config.class, path, false);
	}

	protected static Input loadInput(String configInputFolder, String filename) {
		return load(Input.class, getPath(configInputFolder, filename), true);
	}

	protected static Solution loadSolution(String configOutputFolder, String filename, Input input) {
		Solution solution = load(Solution.class, getPath(configOutputFolder, filename), false);
		rebuildDependencies(solution, input);
		return solution;
	}

	private static String getPath(String folder, String filename) {
		return folder + "/" + filename;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void save(EObject saveObject, String path, boolean zip) {
		Resource resource = new XMLResourceImpl(URI.createURI(path));
		
		Map options = new HashMap();
		if (zip)
			options.put(XMLResource.OPTION_ZIP, true);

		options.put(XMLResource.OPTION_KEEP_DEFAULT_CONTENT, true);
		resource.getContents().add(saveObject);
		
		try {
			resource.save(options);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static <T extends EObject> T load(Class<T> clas, String path, boolean zip) {
		Resource resource = new XMLResourceImpl(URI.createURI(path));
		
		Map options = new HashMap();
		if (zip)
			options.put(XMLResource.OPTION_ZIP, true);
		
		XMLMap xmlMap = new XMLMapImpl();
		if (clas.equals(Input.class))
			xmlMap.setNoNamespacePackage(InputPackage.eINSTANCE);
		if (clas.equals(Config.class))
			xmlMap.setNoNamespacePackage(ConfigPackage.eINSTANCE);
		if (clas.equals(Solution.class))
			xmlMap.setNoNamespacePackage(SolutionPackage.eINSTANCE);
		options.put(XMLResource.OPTION_XML_MAP, xmlMap);
		
		try {
			resource.load(options);
			return (T) resource.getContents().get(0);

		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void rebuildDependencies(Solution solution, Input input) {
	
		// rebuild usedVehicleTypes vehicleTypes
		for (int id : solution.getUseVehicleType().getVehicleTypeIDs())
			solution.getUseVehicleType().getVehicleTypes().add(InputHandler.findVehicleType(input.getVehicleTypes(), id));

		for (Route route : solution.getRoutes()) {
			// rebuild Routes vehicleType/vehicle
			if (solution.getUseVehicleType().isUseVehicleTypeInsteadVehicle())
				route.setVehicleType(InputHandler.findVehicleType(input.getVehicleTypes(), route.getVehicleTypeID()));
			else
				route.setVehicle(InputHandler.findVehicle(input.getVehicles(), route.getVehicleID()));

			for (RouteElement routeElement : route.getRouteElements()) {
				// rebuild RequestActivityRouteElement activity
				if (SolutionHandler.isRequestActivityRouteElement(routeElement)) {
					RequestActivityRouteElement raRE = (RequestActivityRouteElement) routeElement;
					raRE.setRequestActivity(InputHandler.findRequestActivity(input.getRequests().getNew(), raRE.getRequestActivityID()));
				}
				// rebuild SupportRouteElement location
				if (SolutionHandler.isSupportRouteElement(routeElement)) {
					SupportRouteElement sRE = (SupportRouteElement) routeElement;
					sRE.setLocation(InputHandler.findLocation(input.getLocations(), sRE.getLocationID()));
				}
				// rebuild WayPointRouteElement wayPoint
				if (SolutionHandler.isWayPointRouteElement(routeElement)) {
					WayPointRouteElement wpRE = (WayPointRouteElement) routeElement;
					wpRE.setWayPoint(InputHandler.findWayPoint(input.getEdges(), wpRE.getWayPointID()));
				}
			}
		}
	}
}
