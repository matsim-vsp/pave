package org.matsim.run.ovgu;
///* *********************************************************************** *
// * project: org.matsim.*
// * *********************************************************************** *
// *                                                                         *
// * copyright       : (C) 2018 by the members listed in the COPYING,        *
// *                   LICENSE and WARRANTY file.                            *
// * email           : info at matsim dot org                                *
// *                                                                         *
// * *********************************************************************** *
// *                                                                         *
// *   This program is free software; you can redistribute it and/or modify  *
// *   it under the terms of the GNU General Public License as published by  *
// *   the Free Software Foundation; either version 2 of the License, or     *
// *   (at your option) any later version.                                   *
// *   See also COPYING, LICENSE and WARRANTY file                           *
// *                                                                         *
// * *********************************************************************** */
//
//package org.matsim.run;
//
//import com.graphhopper.jsprit.core.problem.Location;
//import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
//import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem.FleetSize;
//import com.graphhopper.jsprit.core.problem.cost.VehicleRoutingActivityCosts;
//import com.graphhopper.jsprit.core.problem.cost.VehicleRoutingTransportCosts;
//import com.graphhopper.jsprit.core.problem.job.Service;
//import com.graphhopper.jsprit.core.problem.job.Service.Builder;
//import com.graphhopper.jsprit.core.problem.job.Shipment;
//import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
//import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
//import com.graphhopper.jsprit.core.problem.solution.route.activity.*;
//import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity.JobActivity;
//import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
//import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
//import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
//import com.graphhopper.jsprit.core.problem.vehicle.VehicleTypeImpl;
//import com.graphhopper.jsprit.core.util.Coordinate;
//import org.apache.log4j.Logger;
//import org.matsim.api.core.v01.Coord;
//import org.matsim.api.core.v01.Id;
//import org.matsim.api.core.v01.network.Link;
//import org.matsim.api.core.v01.network.Network;
//import org.matsim.contrib.freight.carrier.*;
//import org.matsim.contrib.freight.carrier.TimeWindow;
//import org.matsim.contrib.freight.carrier.Tour.Leg;
//import org.matsim.contrib.freight.carrier.Tour.TourElement;
//import org.matsim.contrib.freight.jsprit.NetworkBasedTransportCosts;
//import ovgu.pave.model.solution.RouteElement;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//
///**
// * A factory that creates MATSim-object from algorithms provided from OVGU Magdeburg and vice versa.
// *
// * This bases on the {@Link MatsimJspritFactory} written by Stefan Schröder
// *
// * @author kturner
// *
// */
//class MatsimOvguFactory{
//
//	private static Logger log = Logger.getLogger( MatsimOvguFactory.class );
//
//	//Convert results from ovgu-algorithm to MATSim
//	/**
//	 * Creates a {@link CarrierPlan} from {@link VehicleRoutingProblemSolution}.
//	 *
//	 * <p>Note that the costs of the solution are multiplied by -1 to represent the corresponding score of a carrierPlan.
//	 * <p>The input parameter {@link Carrier} is just required to initialize the plan.
//	 * </br>
//	 */
//	public static CarrierPlan createPlan( Carrier carrier, ArrayList<ArrayList<RouteElement>> solution ){
//		Collection<ScheduledTour> tours = new ArrayList<ScheduledTour>();
//		for (ArrayList<RouteElement> route : solution) {
//			ScheduledTour scheduledTour = createTour(route);
//			tours.add(scheduledTour);
//		}
//		CarrierPlan carrierPlan = new CarrierPlan(carrier, tours);
////		carrierPlan.setScore(solution.getCost()*(-1));
//		return carrierPlan;
//	}
//
//	/**
//	 * Creates {@link ScheduledTour} from {@link RouteElement}.
//	 *
//	 * @param route to be transformed
//	 * @return ScheduledTour
////	 * @throws IllegalStateException if tourActivity is NOT {@link ServiceActivity}.
//	 */
//	public static ScheduledTour createTour(ArrayList<RouteElement> solution) {
//		for (RouteElement routeElement : solution) {
//			route = routeElement.getRoute();
//		}
//		assert route.getDepartureTime() == route.getStart().getEndTime() : "at this point route.getDepartureTime and route.getStart().getEndTime() must be equal";
//		TourActivities tour = route.getTourActivities();
//		CarrierVehicle carrierVehicle = createCarrierVehicle(route.getVehicle());
//		double depTime = route.getStart().getEndTime();
//
//		Tour.Builder tourBuilder = Tour.Builder.newInstance();
//		tourBuilder.scheduleStart(Id.create(route.getStart().getLocation().getId(), Link.class));
//		for (TourActivity act : tour.getActivities()) {
//			if(act instanceof ServiceActivity || act instanceof PickupService){
//				log.debug("Found ServiceActivity or PickupService : " + act.getName() + " at location " +  act.getLocation().getId() + " : " + act.getLocation().getCoordinate() );
//				Service job = (Service) ((JobActivity) act).getJob();
//				CarrierService carrierService = createCarrierService(job);
//				tourBuilder.addLeg(new Leg());
//				tourBuilder.scheduleService(carrierService);
//			}
////			else if (act instanceof DeliverShipment){
////				log.debug("Found DeliveryShipment: " + act.getName() + " at location " +  act.getLocation().getId() + " : " + act.getLocation().getCoordinate() );
////				Shipment job = (Shipment) ((JobActivity) act).getJob();
////				CarrierShipment carrierShipment = createCarrierShipment(job);
////				tourBuilder.addLeg(new Leg());
////				tourBuilder.scheduleDelivery(carrierShipment);
////			}
////			else if (act instanceof PickupShipment){
////				log.debug("Found PickupShipment: " + act.getName() + " at location " +  act.getLocation().getId() + " : " + act.getLocation().getCoordinate() );
////				Shipment job = (Shipment) ((JobActivity) act).getJob();
////				CarrierShipment carrierShipment = createCarrierShipment(job);
////				tourBuilder.addLeg(new Leg());
////				tourBuilder.schedulePickup(carrierShipment);
////			}
//			else
//				throw new IllegalStateException("unknown tourActivity occurred. this cannot be");
//		}
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleEnd(Id.create(route.getEnd().getLocation().getId(), Link.class));
//		Tour vehicleTour = tourBuilder.build();
//		ScheduledTour sTour = ScheduledTour.newInstance(vehicleTour, carrierVehicle, depTime);
//		assert route.getDepartureTime() == sTour.getDeparture() : "departureTime of both route and scheduledTour must be equal";
//		return sTour;
//	}
//
//
//	/**
//	 *
//	 * 	Alles ab hier stammt vor allem aus der MATSimJspritFactory
//	 *
// 	 */
//
//
//	/**
//	 * Creates (MATSim) CarrierShipment from a (jsprit) service
//	 *
//	 * @param service to be transformed to Shipment
//	 * @param depotLink as from-link for Shipment
//	 * @return CarrierShipment
//	 * @see CarrierShipment, Service
//	 */
//	@Deprecated
//	static CarrierShipment createCarrierShipmentFromService(Service service, String depotLink){
//		return CarrierShipment.Builder.newInstance(Id.create(service.getId(), CarrierShipment.class), Id.create(depotLink, Link.class),
//				Id.create(service.getLocation().getId(), Link.class), service.getSize().get(0)).
//				setDeliveryServiceTime(service.getServiceDuration()).
//				setDeliveryTimeWindow(TimeWindow.newInstance(service.getTimeWindow().getStart(),service.getTimeWindow().getEnd())).build();
//	}
//
//	/**
//	 * Creates (MATSim) {@link CarrierShipment} from a (jsprit) {@link Shipment}
//	 *
//	 * @param shipment to be transformed to MATSim
//	 * @return CarrierShipment
//	 * @see CarrierShipment, Shipment
//	 */
//	@Deprecated
//	static CarrierShipment createCarrierShipment(Shipment shipment) {
//		return CarrierShipment.Builder.newInstance(Id.create(shipment.getId(), CarrierShipment.class), Id.createLinkId(shipment.getPickupLocation().getId()),
//				Id.createLinkId(shipment.getDeliveryLocation().getId()), shipment.getSize().get(0))
//				.setDeliveryServiceTime(shipment.getDeliveryServiceTime())
//				.setDeliveryTimeWindow(TimeWindow.newInstance(shipment.getDeliveryTimeWindow().getStart(), shipment.getDeliveryTimeWindow().getEnd()))
//				.setPickupServiceTime(shipment.getPickupServiceTime())
//				.setPickupTimeWindow(TimeWindow.newInstance(shipment.getPickupTimeWindow().getStart(), shipment.getPickupTimeWindow().getEnd()))
//				.build();
//	}
//
//
//	/**
//	 * Creates (jsprit) {@link Shipment} from a (MATSim) {@link CarrierShipment}
//	 *
//	 * @param carrierShipment to be transformed to jsprit
//	 * @return Shipment
//	 * @see CarrierShipment, Shipment
//	 */
//	@Deprecated
//	static Shipment createShipment (CarrierShipment carrierShipment) {
//		return Shipment.Builder.newInstance(carrierShipment.getId().toString())
//				.setDeliveryLocation(Location.newInstance(carrierShipment.getTo().toString()))
//				.setDeliveryServiceTime(carrierShipment.getDeliveryServiceTime())
//				.setDeliveryTimeWindow(com.graphhopper.jsprit.core.problem.solution.route.activity.TimeWindow.newInstance(carrierShipment.getDeliveryTimeWindow().getStart(), carrierShipment.getDeliveryTimeWindow().getEnd()))
//				.setPickupServiceTime(carrierShipment.getPickupServiceTime())
//				.setPickupLocation(Location.newInstance(carrierShipment.getFrom().toString()))
//				.setPickupTimeWindow(com.graphhopper.jsprit.core.problem.solution.route.activity.TimeWindow.newInstance(carrierShipment.getPickupTimeWindow().getStart(), carrierShipment.getPickupTimeWindow().getEnd()))
//				.addSizeDimension(0, carrierShipment.getSize())
//				.build();
//	}
//
//	@Deprecated
//	static Shipment createShipment(CarrierShipment carrierShipment, Coord fromCoord, Coord toCoord) {
//		Location.Builder fromLocationBuilder = Location.Builder.newInstance();
//		fromLocationBuilder.setId(carrierShipment.getFrom().toString());
//		if(fromCoord != null) {
//			fromLocationBuilder.setCoordinate(Coordinate.newInstance(fromCoord.getX(), fromCoord.getY()));
//		}
//		Location fromLocation = fromLocationBuilder.build();
//
//		Location.Builder toLocationBuilder = Location.Builder.newInstance();
//		toLocationBuilder.setId(carrierShipment.getTo().toString());
//		if(toCoord != null) {
//			toLocationBuilder.setCoordinate(Coordinate.newInstance(toCoord.getX(), toCoord.getY()));
//		}
//		Location toLocation = toLocationBuilder.build();
//
//		return Shipment.Builder.newInstance(carrierShipment.getId().toString())
//		.setDeliveryLocation(toLocation)
//		.setDeliveryServiceTime(carrierShipment.getDeliveryServiceTime())
//		.setDeliveryTimeWindow(com.graphhopper.jsprit.core.problem.solution.route.activity.TimeWindow.newInstance(carrierShipment.getDeliveryTimeWindow().getStart(), carrierShipment.getDeliveryTimeWindow().getEnd()))
//		.setPickupServiceTime(carrierShipment.getPickupServiceTime())
//		.setPickupLocation(fromLocation)
//		.setPickupTimeWindow(com.graphhopper.jsprit.core.problem.solution.route.activity.TimeWindow.newInstance(carrierShipment.getPickupTimeWindow().getStart(), carrierShipment.getPickupTimeWindow().getEnd()))
//		.addSizeDimension(0, carrierShipment.getSize())
//		.build();
//	}
//
//	@Deprecated
//	static Service createService(CarrierService carrierService, Coord locationCoord) {
//		Location.Builder locationBuilder = Location.Builder.newInstance();
//		locationBuilder.setId(carrierService.getLocationLinkId().toString());
//		if(locationCoord != null) {
//			locationBuilder.setCoordinate(Coordinate.newInstance(locationCoord.getX(), locationCoord.getY()));
//		}
//		Location location = locationBuilder.build();
//		Builder serviceBuilder = Builder.newInstance(carrierService.getId().toString());
//		serviceBuilder.addSizeDimension(0, carrierService.getCapacityDemand());
//		serviceBuilder.setLocation(location).setServiceTime(carrierService.getServiceDuration())
//			.setTimeWindow(com.graphhopper.jsprit.core.problem.solution.route.activity.TimeWindow.newInstance(carrierService.getServiceStartTimeWindow().getStart(), carrierService.getServiceStartTimeWindow().getEnd()));
//		return serviceBuilder.build();
//	}
//
//	@Deprecated
//	static CarrierService createCarrierService(Service service) {
//		CarrierService.Builder serviceBuilder = CarrierService.Builder.newInstance(Id.create(service.getId(), CarrierService.class), Id.create(service.getLocation().getId(), Link.class));
//		serviceBuilder.setCapacityDemand(service.getSize().get(0));
//		serviceBuilder.setServiceDuration(service.getServiceDuration());
//		serviceBuilder.setServiceStartTimeWindow(TimeWindow.newInstance(service.getTimeWindow().getStart(), service.getTimeWindow().getEnd()));
//		return serviceBuilder.build();
//	}
//
//	/**
//	 * Creates jsprit-vehicle from a matsim-carrier-vehicle.
//	 *
//	 * @param carrierVehicle to be transformed to jsprit
//	 * @param locationCoord to use as start location
//	 * @return jsprit vehicle
//	 * @see Vehicle, CarrierVehicle
//	 */
//	@Deprecated
//	static Vehicle createVehicle(CarrierVehicle carrierVehicle, Coord locationCoord){
//		Location.Builder vehicleLocationBuilder = Location.Builder.newInstance();
//		vehicleLocationBuilder.setId(carrierVehicle.getLocation().toString());
//		if(locationCoord != null) {
//			vehicleLocationBuilder.setCoordinate(Coordinate.newInstance(locationCoord.getX(), locationCoord.getY()));
//		}
//		Location vehicleLocation = vehicleLocationBuilder.build();
//		VehicleType vehicleType = createVehicleType(carrierVehicle.getVehicleType());
//		VehicleImpl.Builder vehicleBuilder = VehicleImpl.Builder.newInstance(carrierVehicle.getVehicleId().toString());
//		vehicleBuilder.setEarliestStart(carrierVehicle.getEarliestStartTime())
//		.setLatestArrival(carrierVehicle.getLatestEndTime())
//		.setStartLocation(vehicleLocation)
//		.setType(vehicleType);
//
//		VehicleImpl vehicle = vehicleBuilder.build();
//		assert carrierVehicle.getEarliestStartTime() == vehicle.getEarliestDeparture() : "carrierVeh must have the same earliestDep as vrpVeh";
//		assert carrierVehicle.getLatestEndTime() == vehicle.getLatestArrival() : "carrierVeh must have the same latestArr as vrpVeh";
//		assert carrierVehicle.getLocation().toString() == vehicle.getStartLocation().getId() : "locations must be equal";
//		return vehicle;
//	}
//
//	/**
//	 * Creates {@link CarrierVehicle} from a {basics.route.Vehicle}
//	 *
//	 * @param vehicle to be transformed to CarrierVehicle
//	 * @return carrierVehicle
//	 * @see CarrierVehicle, Vehicle
//	 */
//	@Deprecated
//	static CarrierVehicle createCarrierVehicle(Vehicle vehicle){
//		String vehicleId = vehicle.getId();
//		CarrierVehicle.Builder vehicleBuilder = CarrierVehicle.Builder.newInstance(Id.create(vehicleId, org.matsim.vehicles.Vehicle.class),
//				Id.create(vehicle.getStartLocation().getId(), Link.class));
//		CarrierVehicleType carrierVehicleType = createCarrierVehicleType(vehicle.getType());
//		vehicleBuilder.setType(carrierVehicleType);
//		vehicleBuilder.setEarliestStart(vehicle.getEarliestDeparture());
//		vehicleBuilder.setLatestEnd(vehicle.getLatestArrival());
//		CarrierVehicle carrierVehicle = vehicleBuilder.build();
//		assert vehicle.getEarliestDeparture() == carrierVehicle.getEarliestStartTime() : "vehicles must have the same earliestStartTime";
//		assert vehicle.getLatestArrival() == carrierVehicle.getLatestEndTime() : "vehicles must have the same latestEndTime";
//		assert vehicle.getStartLocation().getId() == carrierVehicle.getLocation().toString() : "locs must be the same";
//		return carrierVehicle;
//	}
//
//	/**
//	 * Creates {@link CarrierVehicleType} from {@link VehicleType}.
//	 *
//	 * <p>No description and engineInformation can be set here. Do it by calling setEngineInforation(engineInfo) from the returned
//	 * object.
//	 *
//	 * @param type to be transformed
//	 * @return CarrierVehicleType
//	 */
//	@Deprecated
//	static CarrierVehicleType createCarrierVehicleType(VehicleType type){
//		CarrierVehicleType.Builder typeBuilder = CarrierVehicleType.Builder.newInstance(Id.create(type.getTypeId(), org.matsim.vehicles.VehicleType.class));
//		typeBuilder.setCapacity(type.getCapacityDimensions().get(0));
//		typeBuilder.setCostPerDistanceUnit(type.getVehicleCostParams().perDistanceUnit).setCostPerTimeUnit(type.getVehicleCostParams().perTransportTimeUnit)
//		.setFixCost(type.getVehicleCostParams().fix);
//		typeBuilder.setMaxVelocity(type.getMaxVelocity());
//		return typeBuilder.build();
//	}
//
//	/**
//	 * Creates {@link VehicleType} from {@link CarrierVehicleType}.
//	 */
//	@Deprecated
//	static VehicleType createVehicleType(CarrierVehicleType carrierVehicleType){
//		if(carrierVehicleType == null) throw new IllegalStateException("carrierVehicleType is null");
//		VehicleTypeImpl.Builder typeBuilder = VehicleTypeImpl.Builder.newInstance(carrierVehicleType.getId().toString());
//		typeBuilder.addCapacityDimension(0, carrierVehicleType.getCarrierVehicleCapacity());
//		typeBuilder.setCostPerDistance(carrierVehicleType.getVehicleCostInformation().getPerDistanceUnit());
//		typeBuilder.setCostPerTransportTime(carrierVehicleType.getVehicleCostInformation().getPerTimeUnit());
//		typeBuilder.setFixedCost(carrierVehicleType.getVehicleCostInformation().getFix());
//		typeBuilder.setMaxVelocity(carrierVehicleType.getMaximumVelocity());
//		return typeBuilder.build();
//	}
//
//
//
//
//	/**
//	 * Creates {@link VehicleRoute} from {@link ScheduledTour}.
//	 *
//	 * <p>The {@link Network} is required to retrieve coordinates.
//	 *
//	 * @param scheduledTour to be transformed
//	 * @param vehicleRoutingProblem the routing problem
//     * @return VehicleRoute
//	 */
//	@Deprecated
//	public static VehicleRoute createRoute(ScheduledTour scheduledTour, VehicleRoutingProblem vehicleRoutingProblem){
//		CarrierVehicle carrierVehicle = scheduledTour.getVehicle();
//		double depTime = scheduledTour.getDeparture();
//		Tour tour = scheduledTour.getTour();
//        Id<org.matsim.vehicles.Vehicle> vehicleId = carrierVehicle.getVehicleId();
//        Vehicle jspritVehicle = getVehicle(vehicleId.toString(),vehicleRoutingProblem);
//		if(jspritVehicle == null) throw new IllegalStateException("jsprit-vehicle to id=" + vehicleId.toString() + " is missing");
//
//		VehicleRoute.Builder routeBuilder = VehicleRoute.Builder.newInstance(jspritVehicle);
//		routeBuilder.setJobActivityFactory(vehicleRoutingProblem.getJobActivityFactory());
//        routeBuilder.setDepartureTime(depTime);
//
//		for(TourElement e : tour.getTourElements()){
//			if(e instanceof Tour.TourActivity){
//				if(e instanceof Tour.ServiceActivity){
//					CarrierService carrierService = ((Tour.ServiceActivity) e).getService();
//					Service service = (Service) vehicleRoutingProblem.getJobs().get(carrierService.getId().toString());
//                    if(service == null) throw new IllegalStateException("service to id="+carrierService.getId()+" is missing");
//					routeBuilder.addService(service);
//				}
//			}
//		}
//		VehicleRoute route = routeBuilder.build();
//        log.debug("jsprit route: " + route);
//        log.debug("start-location: " + route.getStart().getLocation() + " endTime: " + route.getDepartureTime() + "(" + route.getStart().getEndTime() + ")");
//        for(TourActivity act : route.getActivities()){
//            log.debug("act: " + act);
//        }
//        log.debug("end: " + route.getEnd());
//        assert route.getDepartureTime() == scheduledTour.getDeparture() : "departureTimes of both routes must be equal";
//		return route;
//	}
//
//	@Deprecated
//    private static Vehicle getVehicle(String id, VehicleRoutingProblem vehicleRoutingProblem) {
//        for(Vehicle v : vehicleRoutingProblem.getVehicles()){
//            if(v.getId().equals(id)) return v;
//        }
//        return null;
//    }
//
//    /**
//	 * Creates an immutable {@link VehicleRoutingProblem} from {@link Carrier}.
//	 *
//	 * <p>For creation it takes only the information needed to setup the problem (not the solution, i.e. predefined plans are ignored).
//	 * <p>The network is required to retrieve coordinates of locations.
//	 * <p>Note that currently only services ({@link Service}) and Shipments ({@link Shipment}) are supported.
//     * <p>Pickups and deliveries can be defined as shipments with only one location (toLocation for delivery and fromLocation for pickup). Implementation follows
//	 *
//	 */
//    @Deprecated
//	public static VehicleRoutingProblem createRoutingProblem(Carrier carrier, Network network, VehicleRoutingTransportCosts transportCosts, VehicleRoutingActivityCosts activityCosts){
//		VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
//		boolean serviceInVrp = false;
//		boolean shipmentInVrp = false;
//
//		FleetSize fleetSize;
//		CarrierCapabilities carrierCapabilities = carrier.getCarrierCapabilities();
//		if(carrierCapabilities.getFleetSize().equals( CarrierCapabilities.FleetSize.INFINITE )){
//			fleetSize = FleetSize.INFINITE;
//			vrpBuilder.setFleetSize(fleetSize);
//		}
//		else{
//			fleetSize = FleetSize.FINITE;
//			vrpBuilder.setFleetSize(fleetSize);
//		}
//		for(CarrierVehicle v : carrierCapabilities.getCarrierVehicles()){
//			Coord coordinate = null;
//			if(network != null) {
//				Link link = network.getLinks().get(v.getLocation());
//				if(link == null) throw new IllegalStateException("vehicle.locationId cannot be found in network [vehicleId=" + v.getVehicleId() + "][locationId=" + v.getLocation() + "]");
//				coordinate = link.getCoord();
//			} else log.warn("cannot find linkId " + v.getVehicleId());
//			Vehicle veh = createVehicle(v, coordinate);
//			assert veh.getEarliestDeparture() == v.getEarliestStartTime() : "earliestDeparture of both vehicles must be equal";
//			assert veh.getLatestArrival() == v.getLatestEndTime() : "latestArrTime of both vehicles must be equal";
//			vrpBuilder.addVehicle(veh);
//		}
//
//		for(CarrierService service : carrier.getServices()){
//			if (shipmentInVrp) {
//				throw new UnsupportedOperationException("VRP with miexed Services and Shipments may lead to invalid solutions because of vehicle capacity handling are different");
//			}
//			Coord coordinate = null;
//			if(network != null){
//				Link link = network.getLinks().get(service.getLocationLinkId());
//				if(link != null) {
//					coordinate = link.getCoord();
//				}
//				else log.warn("cannot find linkId " + service.getLocationLinkId());
//			}
//			serviceInVrp = true;
//			vrpBuilder.addJob(createService(service, coordinate));
//		}
//
//
//		for(CarrierShipment carrierShipment : carrier.getShipments()) {
//			if (serviceInVrp) {
//				throw new UnsupportedOperationException("VRP with miexed Services and Shipments may lead to invalid solutions because of vehicle capacity handling are different");
//			}
//			Coord fromCoordinate = null;
//			Coord toCoordinate = null;
//			if(network != null){
//				Link fromLink = network.getLinks().get(carrierShipment.getFrom());
//				Link toLink = network.getLinks().get(carrierShipment.getTo());
//
//				if(fromLink != null && toLink != null) { //Shipment to be delivered from specified location to specified location
//					fromCoordinate = fromLink.getCoord();
//					toCoordinate = toLink.getCoord();
//					vrpBuilder.addJob(createShipment(carrierShipment, fromCoordinate, toCoordinate));
//				} else
//					throw new IllegalStateException("cannot create shipment since neither fromLinkId " + carrierShipment.getTo() + " nor toLinkId " + carrierShipment.getTo() + " exists in network.");
//
//			}
//			shipmentInVrp = true;
//			vrpBuilder.addJob(createShipment(carrierShipment, fromCoordinate, toCoordinate));
//		}
//
//
//		if(transportCosts != null) vrpBuilder.setRoutingCost(transportCosts);
//		if(activityCosts != null) vrpBuilder.setActivityCosts(activityCosts);
//		return vrpBuilder.build();
//	}
//
//
//	/**
//	 * Creates {@link VehicleRoutingProblem.Builder} from {@link Carrier} for later building of the {@link VehicleRoutingProblem}. This is required if you need to
//	 * add stuff to the problem later, e.g. because it cannot solely be retrieved from network and carrier such as {@link NetworkBasedTransportCosts}.
//	 *
//	 * <p>For creation it takes only the information needed to setup the problem (not the solution, i.e. predefined plans are ignored).
//	 * <p>The network is required to retrieve coordinates of locations.
//	 * <p>Note that currently only services ({@link Service}) and Shipments ({@link Shipment}) are supported.
//     * <p>Pickups and deliveries can be defined as shipments with only one location (toLocation for delivery and fromLocation for pickup). Implementation follows
//	 *
//	 */
//	@Deprecated
//	public static VehicleRoutingProblem.Builder createRoutingProblemBuilder(Carrier carrier, Network network){
//		VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
//		boolean serviceInVrp = false;
//		boolean shipmentInVrp = false;
//
//		FleetSize fleetSize;
//		CarrierCapabilities carrierCapabilities = carrier.getCarrierCapabilities();
//		if(carrierCapabilities.getFleetSize().equals( CarrierCapabilities.FleetSize.INFINITE )){
//			fleetSize = FleetSize.INFINITE;
//			vrpBuilder.setFleetSize(fleetSize);
//		}
//		else{
//			fleetSize = FleetSize.FINITE;
//			vrpBuilder.setFleetSize(fleetSize);
//		}
//		for(CarrierVehicle v : carrierCapabilities.getCarrierVehicles()){
//			Coord coordinate = null;
//			if(network != null) {
//				Link link = network.getLinks().get(v.getLocation());
//				if(link == null) throw new IllegalStateException("vehicle.locationId cannot be found in network [vehicleId=" + v.getVehicleId() + "][locationId=" + v.getLocation() + "]");
//				coordinate = link.getCoord();
//			} else log.warn("cannot find linkId " + v.getVehicleId());
//			vrpBuilder.addVehicle(createVehicle(v, coordinate));
//		}
//
//		for(CarrierService service : carrier.getServices()){
//			log.debug("Handle CarrierService: " + service.toString());
//			if (shipmentInVrp) {
//				throw new UnsupportedOperationException("VRP with miexed Services and Shipments may lead to invalid solutions because of vehicle capacity handling are different");
//			}
//			Coord coordinate = null;
//			if(network != null){
//				Link link = network.getLinks().get(service.getLocationLinkId());
//				if(link == null) {
//					throw new IllegalStateException("cannot create service since linkId " + service.getLocationLinkId() + " does not exists in network.");
//				}
//				else coordinate = link.getCoord();
//			}
//			serviceInVrp = true;
//			vrpBuilder.addJob(createService(service, coordinate));
//		}
//
//
//		for(CarrierShipment carrierShipment : carrier.getShipments()) {
//			log.debug("Handle CarrierShipment: " + carrierShipment.toString());
//			if (serviceInVrp) {
//				throw new UnsupportedOperationException("VRP with miexed Services and Shipments may lead to invalid solutions because of vehicle capacity handling are different");
//			}
//
//			Coord fromCoordinate = null;
//			Coord toCoordinate = null;
//			if(network != null){
//				Link fromLink = network.getLinks().get(carrierShipment.getFrom());
//				Link toLink = network.getLinks().get(carrierShipment.getTo());
//
//				if(fromLink != null && toLink != null) { //Shipment to be delivered from specified location to specified location
//					log.debug("Shipment identified as Shipment: " + carrierShipment.getId().toString());
//					fromCoordinate = fromLink.getCoord();
//					toCoordinate = toLink.getCoord();
//				}  else
//					throw new IllegalStateException("cannot create shipment " + carrierShipment.getId().toString() + " since either fromLinkId " + carrierShipment.getTo() + " or toLinkId " + carrierShipment.getTo() + " exists in network.");
//
//			}
//			shipmentInVrp = true;
//			vrpBuilder.addJob(createShipment(carrierShipment, fromCoordinate, toCoordinate));
//		}
//
//		return vrpBuilder;
//	}
//
//
//
//	/**
//	 * Creates a {@link VehicleRoutingProblemSolution} from {@link CarrierPlan}.
//	 *
//	 * <p>To retrieve coordinates the {@link Network} is required.
//	 * </br>
//	 */
//	@Deprecated
//	public static VehicleRoutingProblemSolution createSolution(CarrierPlan plan, VehicleRoutingProblem vehicleRoutingProblem) {
//		List<VehicleRoute> routes = new ArrayList<VehicleRoute>();
//		for(ScheduledTour tour : plan.getScheduledTours()){
//			VehicleRoute route = createRoute(tour, vehicleRoutingProblem);
//			routes.add(route);
//		}
//		double costs;
//		if(plan.getScore() == null) costs = -9999.0;
//		else costs = plan.getScore() * -1.0;
//		return new VehicleRoutingProblemSolution(routes, costs);
//	}
//
//	/**
//	 * Creates a {@link Carrier} from {@link VehicleRoutingProblem}. The carrier is initialized with the carrierId, i.e. <code>carrier.getId()</code> returns carrierId.
//	 * </br>
//	 */
//	@Deprecated
//	public static Carrier createCarrier(String carrierId, VehicleRoutingProblem vrp){
//		Id<Carrier> id = Id.create(carrierId, Carrier.class);
//		Carrier carrier = CarrierImpl.newInstance(id);
//		CarrierCapabilities.Builder capabilityBuilder = CarrierCapabilities.Builder.newInstance();
//
//		//fleet and vehicles
//		if(vrp.getFleetSize().equals(FleetSize.FINITE)){
//			capabilityBuilder.setFleetSize( CarrierCapabilities.FleetSize.FINITE );
//		}
//		else capabilityBuilder.setFleetSize( CarrierCapabilities.FleetSize.INFINITE );
//		for(VehicleType type : vrp.getTypes()){
//			capabilityBuilder.addType(createCarrierVehicleType(type));
//		}
//		for(Vehicle vehicle : vrp.getVehicles()){
//			capabilityBuilder.addVehicle(createCarrierVehicle(vehicle));
//		}
//		carrier.setCarrierCapabilities(capabilityBuilder.build());
//
//		return carrier;
//	}
//
//
//
//}
