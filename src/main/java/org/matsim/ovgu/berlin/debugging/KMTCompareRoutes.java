package org.matsim.ovgu.berlin.debugging;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.population.Route;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.events.handler.EventHandler;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.vehicles.Vehicle;

/**
 * Vergleiche die Routen, die laut Carrier-File geplant sind, mit denen aus den Events.
 * Es kommt nur auf die Reihenfolge der genutzten Links an. Die Fahrzeiten können abweichen.
 *
 */
class KMTCompareRoutes {

	private static String WORKINGDIR =  "/Users/kturner/Desktop/OVGU/Tour2oLP/Output/1withTours";

	static Logger log = Logger.getLogger(KMTCompareRoutes.class);

	public static void main(String[] args){

		Carriers carriers = new Carriers();
		CarrierPlanXmlReader carrierPlanXmlReader = new CarrierPlanXmlReader(carriers);
		carrierPlanXmlReader.readFile(WORKINGDIR + "/output_carriers.xml");

		EventsManager eventsManager = EventsUtils.createEventsManager();
		SimpleEventshandler eventsHandler = new SimpleEventshandler();
		eventsManager.addHandler(eventsHandler);
		MatsimEventsReader eventsReader = new MatsimEventsReader(eventsManager);
		eventsReader.readFile(WORKINGDIR + "/output_events.xml.gz");


		log.info("# of carriers in Carrier File: " + carriers.getCarriers().size());
		log.info("# of LinkEnterEvents: " + eventsHandler.getNumberOfLinkEnterEvents());

		LinkedHashMap<Id<Carrier>, List> routesFromCarrierFile = getRouteFromCarrierFile(carriers);
		LinkedHashMap<Id<Carrier>, List> routesFromEventsFile = eventsHandler.getRoutesFromEventsFile();

		for (Id<Carrier> carrierId : routesFromCarrierFile.keySet()) {
			log.info("Carrier has #links in carriers Route:" + carrierId + " : " + routesFromCarrierFile.get(carrierId).size());
			log.info("Route: " + routesFromCarrierFile.get(carrierId).toString());
		}
		
		for (Id<Carrier> carrierId : routesFromEventsFile.keySet()) {
			log.info("Carrier has #links in events Route:" + carrierId + " : " + routesFromEventsFile.get(carrierId).size());
			log.info("Route: " + routesFromEventsFile.get(carrierId).toString());
		}
		
		log.info("Beide Listen haben die gleichen Carrier: " + routesFromCarrierFile.keySet().equals(routesFromEventsFile.keySet()));
		
		for (Id<Carrier> carrierId : routesFromCarrierFile.keySet()) {
			log.info("Beide Routen für Carrier sind identisch " + carrierId + "_: " 
						+ (routesFromCarrierFile.get(carrierId)).equals(routesFromEventsFile.get(carrierId)));
		}
		
		log.info("### Fertig ###");

	}

	private static LinkedHashMap<Id<Carrier>, List> getRouteFromCarrierFile(Carriers carriers) {
		LinkedHashMap<Id<Carrier>, List> routesFromCarrierFile = new LinkedHashMap();
		for (Carrier carrier : carriers.getCarriers().values()) {
			routesFromCarrierFile.put(carrier.getId(), new ArrayList());

			for (ScheduledTour tour : carrier.getSelectedPlan().getScheduledTours()) {
				for (Tour.TourElement tourElement : tour.getTour().getTourElements()) {
					if (tourElement instanceof Tour.Leg){
						Route route = ((Tour.Leg) tourElement).getRoute();
						List tempList = routesFromCarrierFile.get(carrier.getId());
						tempList.addAll(((NetworkRoute) route).getLinkIds());
						if ( ! ((NetworkRoute) route).getLinkIds().isEmpty()) {
							tempList.add(route.getEndLinkId()); // Der letzte Link ist in den CarrierPlänen nicht mit ausgewiesen. 
							//(Aber nur, wenn Route nicht leer ist. Begr.: Bei leerer Route gibt es keine LinkEnterEvents)
						}
						routesFromCarrierFile.put(carrier.getId(), (tempList));
					}
				}
			}
		}
		return routesFromCarrierFile;
	}

	static class SimpleEventshandler implements LinkEnterEventHandler, EventHandler {


		@Override
		public void reset(int iteration) {
		}

		private int linkEnterEventsCounter = 0;
		LinkedHashMap<Id<Carrier>, List> routesFromEventsFile = new LinkedHashMap();
		
		@Override
		public void handleEvent(LinkEnterEvent event) {
			linkEnterEventsCounter++;
			
			Id<Carrier> carrierId = getCarrierofEvent(event);
			if ((routesFromEventsFile.get(carrierId) == null )) {
				List tempList = new ArrayList();
				tempList.add(event.getLinkId());
				routesFromEventsFile.put(carrierId, tempList);
			} else {
				List tempList = routesFromEventsFile.get(carrierId);
				tempList.add(event.getLinkId());
				routesFromEventsFile.put(carrierId, tempList);
			}
		}

		private Id<Carrier> getCarrierofEvent(LinkEnterEvent event) {
			var vehicleId = event.getVehicleId();
			var carrierId = Id.create((vehicleId.toString().split("_", 3)[1]), Carrier.class);
//			log.info("CarrierId of vehicle is:" + vehicleId + " --> " + carrierId.toString());
			return carrierId;
		}

		int getNumberOfLinkEnterEvents(){
			return linkEnterEventsCounter;
		}

		LinkedHashMap<Id<Carrier>, List> getRoutesFromEventsFile() {
			return routesFromEventsFile;
		}
		
		
	}
}
