/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package freight.manager;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import freight.FreightTourCalculation;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.schedule.*;
import org.matsim.contrib.dvrp.trafficmonitoring.DvrpTravelTimeModule;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.taxi.schedule.TaxiEmptyDriveTask;
import org.matsim.contrib.util.StraightLineKnnFinder;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.events.IterationStartsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.controler.listener.IterationStartsListener;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.router.util.LeastCostPathCalculator;
import org.matsim.core.router.util.TravelTime;
import privateAV.PFAVUtils;
import privateAV.schedule.PFAVServiceDriveTask;
import privateAV.schedule.PFAVStartTask;
import privateAV.vehicle.PFAVSpecification;
import privateAV.vehicle.PFAVehicle;

import java.util.*;

/**
 * @author tschlenther
 *
 */
public class FreightTourManagerImpl implements FreightTourManager, IterationStartsListener, IterationEndsListener {

	private final static Logger log = Logger.getLogger(FreightTourManagerImpl.class);
	
    /**
     * the freight contrib will be run before every iteration where iterationNumber % FREIGHTTOUR_PLANNING_INTERVAL == 0- \n
	 * if FREIGHTTOUR_PLANNING_INTERVAL is set to 0 or any negative integer, the freight contrib will run only before iteration 0.
     */
    private final int FREIGHTTOUR_PLANNING_INTERVAL = 1;

	private Map<Link, List<List<StayTask>>> startLinkToFreightTour = new HashMap<>();

	private List<List<StayTask>> freightTours = new ArrayList<>();
	@Inject
	@Named(DvrpRoutingNetworkProvider.DVRP_ROUTING)
	private Network network;


	/* TODO:
	 * actually, i would need two travelTime objects here.
	 * 	- one that i can hand over (or inject into) the freight calculator before the mobsim
	 * 	- one that is up to date within the mobsim (the DVRP_ESTIMATED !?) that helps rerouting the legs contained in the freight tour
	 */
	@Inject
	@Named(DvrpTravelTimeModule.DVRP_ESTIMATED)
	private TravelTime travelTime;

    private Carriers carriers;
    private CarrierVehicleTypes vehicleTypes;

	/**
	 *
	 */
	public FreightTourManagerImpl(String pathToCarriersFile, String pathToVehTypesFile) {

		this.carriers = readCarriers(pathToCarriersFile);
        this.vehicleTypes = readVehicleTypes(pathToVehTypesFile);
		log.info("loading carrier vehicle types..");
		new CarrierVehicleTypeLoader(carriers).loadVehicleTypes(vehicleTypes);
	}

	private CarrierVehicleTypes readVehicleTypes(String input) {
		CarrierVehicleTypes vTypes = new CarrierVehicleTypes();
		CarrierVehicleTypeReader reader = new CarrierVehicleTypeReader(vTypes);
		reader.readFile(input);
		return vTypes;
	}

	private Carriers readCarriers(String file){
		Carriers carriers = new Carriers();
		CarrierPlanXmlReaderV2 reader = new CarrierPlanXmlReaderV2(carriers);
		reader.readFile(file);
		return carriers;
	}

	private List<List<StayTask>> convertCarrierPlansToTaskList(Carriers carriers) {
		
		log.info("start converting carrier plans to taxi tasks..");
		
		List<List<StayTask>> freightTours = new ArrayList<>();
		
		for(Carrier carrier : carriers.getCarriers().values()) {
			CarrierPlan plan = carrier.getSelectedPlan();
			for (ScheduledTour freightTour : plan.getScheduledTours()) {
				freightTours.add(ConvertFreightTourForDvrp.convertToList(freightTour, this.network));
			}
		}
		return freightTours;
	}

	@Deprecated
	public List<StayTask> getRandomPFAVTour() {
		if (this.freightTours.size() == 0) return null;
		return( this.freightTours.remove(( MatsimRandom.getRandom().nextInt(this.freightTours.size()) )) );
	}

	/**
	 *
	 */
	@Override
	public Schedule getBestPFAVTourForVehicle(PFAVehicle vehicle, LeastCostPathCalculator router) {
		return getDispatchedTourForVehicle(vehicle, router);
	}

	private Schedule getDispatchedTourForVehicle(PFAVehicle vehicle, LeastCostPathCalculator router) {
		StraightLineKnnFinder<Link,Link> finder = new StraightLineKnnFinder<>(3,link1-> link1 , link2 -> link2 );
		// at the moment, we need to assume that on one link there can only be one depot/carrier! this is because we need a mapping in this direction for the following piece of code
		List<Link> nearestDepots = finder.findNearest(Tasks.getEndLink(vehicle.getSchedule().getCurrentTask()),startLinkToFreightTour.keySet().stream());

		//TODO: probably create a representation of the freightTour - something like FreightTourData - that contains the List<StayTask> and the duration, maybe the start and end link explicitly
		//TODO: test this spatial dispatch algorithm

		for (Link l : nearestDepots){
			Iterator<List<StayTask>> freightTourIterator = this.startLinkToFreightTour.get(l).iterator();
			while(freightTourIterator.hasNext()){
				List<StayTask> freightTour = freightTourIterator.next();
				VrpPathWithTravelData[] depotPaths = getDepotPathsIfThereIsEnoughTimeToPerformFreightTour(vehicle, freightTour, router);
				if (depotPaths != null) {
					freightTourIterator.remove();
					return computeResultingSchedule(vehicle, freightTour, depotPaths, router);
				}
			}
		}
		return null;
	}

	/**
	 * to know the reason why i don't manipulate the vehicle's schedule directly, please see scheduleFreightTour() in {@link privateAV.PFAVScheduler}
	 *
	 * @param vehicle
	 * @param freightTour
	 * @param depotPaths
	 * @param router
	 * @return
	 * @see privateAV.PFAVScheduler
	 */
	private Schedule computeResultingSchedule(PFAVehicle vehicle, List<StayTask> freightTour, VrpPathWithTravelData[] depotPaths, LeastCostPathCalculator router) {

		//we need to build the vehicle specification for the schedule. later, this schedule (and dummy specification) will be dumped anyways
		PFAVSpecification sp = PFAVSpecification.newBuilder().
				id(vehicle.getId())
				.capacity(vehicle.getCapacity())
				.serviceBeginTime(vehicle.getServiceBeginTime())
				.serviceEndTime(vehicle.getServiceEndTime())
				.actEndTimes(vehicle.getOwnerActEndTimes())
				.startLinkId(depotPaths[0].getFromLink().getId()).build();

		Schedule schedule = new ScheduleImpl(sp);

		//insert the empty drive to the depot at first position
		DriveTask precedingDriveTask = new TaxiEmptyDriveTask(depotPaths[0]);
		schedule.addTask(precedingDriveTask);

		for (int i = 0; i < freightTour.size(); i++) {
			StayTask currentTask = freightTour.get(i);

			double duration = currentTask.getEndTime() - currentTask.getBeginTime();
			currentTask.setBeginTime(precedingDriveTask.getEndTime());
			currentTask.setEndTime(precedingDriveTask.getEndTime() + duration);

			if (currentTask instanceof PFAVStartTask) {
				((PFAVStartTask) currentTask).setVehicle(vehicle.getId());
			}
			schedule.addTask(currentTask);


			if (i < freightTour.size() - 1) {
				VrpPathWithTravelData path = VrpPaths.calcAndCreatePath(currentTask.getLink(), freightTour.get(i + 1).getLink(), currentTask.getEndTime(), router, travelTime);
				precedingDriveTask = new PFAVServiceDriveTask(path);
				schedule.addTask(precedingDriveTask);
			} else {
				//insert EmptyDriveTask back to owner's activity link
				VrpPathWithTravelData path = VrpPaths.calcAndCreatePath(depotPaths[1].getFromLink(), depotPaths[1].getToLink(), currentTask.getEndTime(), router, travelTime);
				schedule.addTask(new PFAVServiceDriveTask(path));
			}
		}
		return schedule;
	}

	/**
	 * if there is enough time left to perform the freight tour
	 * returns an Array containing the path from current task link to depot, the path from last service task to depot, and the path from depot to activity linkk of owner
	 * else returns null#
	 * <p>
	 * we return the paths here instead of a boolean for efficiency purpose:
	 * we would compute these paths in the scheduler again if there is enough time
	 */
	private VrpPathWithTravelData[] getDepotPathsIfThereIsEnoughTimeToPerformFreightTour(PFAVehicle vehicle, List<StayTask> freightTour, LeastCostPathCalculator router) {

	    StayTask currentTask = (StayTask) vehicle.getSchedule().getCurrentTask();
	    StayTask start = freightTour.get(0);


		//TODO delete log.info's
		VrpPathWithTravelData pathFromCurrTaskToDepot = VrpPaths.calcAndCreatePath(currentTask.getLink(), start.getLink(), currentTask.getEndTime(), router, travelTime);
		VrpPathWithTravelData path = pathFromCurrTaskToDepot;
		System.out.println("path to depot: " + path.getDepartureTime() + ";" + path.getArrivalTime() + ";" + path.getTravelTime());

		double tourDuration = freightTour.get(freightTour.size() - 1).getEndTime() - start.getBeginTime();
		System.out.println("tourDuration: " + tourDuration);

		double departureTimeFromDepot = pathFromCurrTaskToDepot.getArrivalTime() + tourDuration;
		System.out.println("departureTimeFromDepot " + departureTimeFromDepot);

		VrpPathWithTravelData pathFromDepotBackToOwner = VrpPaths.calcAndCreatePath(start.getLink(), currentTask.getLink(), departureTimeFromDepot, router, travelTime);
		path = pathFromDepotBackToOwner;
		System.out.println("path from depot: " + path.getDepartureTime() + ";" + path.getArrivalTime() + ";" + path.getTravelTime());

		double totalTimeNeededToPerformFreightTour = pathFromCurrTaskToDepot.getTravelTime() +
				tourDuration +
				pathFromDepotBackToOwner.getTravelTime();
		System.out.println("totalTimeNeededToPerformFreightTour " + totalTimeNeededToPerformFreightTour);
		// is enough time to perform the freight tour?
		//if endTime is infinite, the next activity of the vehicle owner is the last for the day, so we can always perform the freight tour
		//TODO: implement a global end time point for freight tours ? so somehting like: after 8 p.m. no one should deliver anything anymore ??
		Double actEndTime = vehicle.getOwnerActEndTimes().peek();
		if (actEndTime == null) {
			throw new IllegalStateException("there should be an activity in the agent's plan after the taxi leg !");
		} else if (Double.isInfinite(actEndTime) || actEndTime >= currentTask.getEndTime() + totalTimeNeededToPerformFreightTour + PFAVUtils.TIME_BUFFER) {
			vehicle.getOwnerActEndTimes().remove();
			return new VrpPathWithTravelData[]{pathFromCurrTaskToDepot, pathFromDepotBackToOwner};
		}
		return null;
	}


	//dirty zwischenloesung
	@Override
	public void routeCarrierPlans(LeastCostPathCalculator router) {
		log.info("route the carriers");
		FreightTourCalculation.routeCarriersSelectedPlans(this.carriers, router, this.network, this.travelTime);
		log.info("done.");
		initializeAndFillMapFreightToursToStartLinks();
	}

	@Override
	public void runTourPlanning() {

		//TODO: I'm not sure whether the travelTime object represents the current travel times of current iteration as it is injected...
		// no it does not ! tschlenther, 16.feb'
		this.carriers = FreightTourCalculation.runTourPlanningForCarriers(this.carriers, this.vehicleTypes, this.network, this.travelTime);
		initializeAndFillMapFreightToursToStartLinks();
	}

	private void initializeAndFillMapFreightToursToStartLinks() {
		log.info("overriding list of PFAV schedules...");
		this.startLinkToFreightTour = new HashMap<>();
		List<List<StayTask>> allFreightTours = convertCarrierPlansToTaskList(carriers);

		for(List<StayTask> freightTour : allFreightTours){
			Link start = freightTour.get(0).getLink();
			if(this.startLinkToFreightTour.containsKey(start)){
				this.startLinkToFreightTour.get(start).add(freightTour);
			} else{
				List<List<StayTask>> allDepotTours = new ArrayList<>();
				allDepotTours.add(freightTour);
				this.startLinkToFreightTour.put(start,allDepotTours);
			}
		}
	}

	@Override
    public void notifyIterationStarts(IterationStartsEvent event){
		if( (FREIGHTTOUR_PLANNING_INTERVAL < 1 && event.getIteration() < 1) ||  (event.getIteration() % FREIGHTTOUR_PLANNING_INTERVAL == 0) ){
			log.info("RUNNING FREIGHT CONTRIB TO CALCULATE FREIGHT TOURS BASED ON CURRENT TRAVEL TIMES");
			runTourPlanning();
		}
    }

	/**
	 * Notifies all observers of the Controler that a iteration is finished
	 *
	 * @param event
	 */
	@Override
	public void notifyIterationEnds(IterationEndsEvent event) {
		if ((FREIGHTTOUR_PLANNING_INTERVAL < 1 && event.getIteration() < 1) || (event.getIteration() % FREIGHTTOUR_PLANNING_INTERVAL == 0)) {
			String dir = event.getServices().getConfig().controler().getOutputDirectory() + "/ITERS/it." + event.getIteration() + "/carriers_it" + event.getIteration() + ".xml";
			log.info("writing carrier file of iteration " + event.getIteration() + " to " + dir);
			writeCarriers(dir);
		}
	}

	private void writeCarriers(String outputDir) {
		CarrierPlanXmlWriterV2 planWriter = new CarrierPlanXmlWriterV2(carriers);
		planWriter.write(outputDir);
	}
}
