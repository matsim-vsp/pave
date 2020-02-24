/*
  * *********************************************************************** *
  * project: org.matsim.*
  * *********************************************************************** *
  *                                                                         *
  * copyright       : (C) 2020 by the members listed in the COPYING,        *
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
  * *********************************************************************** *
 */
package org.matsim.ovgu.berlin;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.Freight;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.carrier.Tour.Leg;
import org.matsim.contrib.freight.controler.CarrierModule;
import org.matsim.contrib.freight.jsprit.NetworkBasedTransportCosts;
import org.matsim.contrib.freight.jsprit.NetworkRouter;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.groups.PlansConfigGroup;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleUtils;

import javax.management.InvalidAttributeValueException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * This is a short an easy version to run MATSim freight scenarios .
 * 
 * Optional it is possible to run MATSim after tour planning.
 * 
 * @author kturner
 * 
 */
public class RunFreightOnlyMatsim {

	private static final Logger log = Logger.getLogger(RunFreightOnlyMatsim.class);

	public static void main(String[] args) throws IOException, InvalidAttributeValueException {

		 for (String arg : args) {
	            log.info( arg );
	        }

	        if ( args.length==0 ) {
	            String inputPath = "../input/"; 	//InputOrt, wo die NetworkChangeEvents liegen.
	            args = new String[] {
//	                    inputPath + "networkChangeEvents.xml.gz",
	            		"",
	                    "../OutputKMT/OVGU/DemoRun"}  ;
	        }

	        Config config = prepareConfig( args ) ;
	        Scenario scenario = prepareScenario( config ) ;
	        Controler controler = prepareControler( scenario ) ;

	        controler.run();
	}

    private static Config prepareConfig(String[] args) {
        String networkChangeEventsFileLocation = args[0];
        String outputLocation = args[1];


        Config config = ConfigUtils.createConfig();
        config.controler().setOverwriteFileSetting(OutputDirectoryHierarchy.OverwriteFileSetting.deleteDirectoryIfExists);
        config.global().setRandomSeed(4177);
        config.controler().setLastIteration(0);
        config.controler().setOutputDirectory(outputLocation);

        config.network().setInputFile("https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.4-10pct/input/berlin-v5-network.xml.gz");

        if (networkChangeEventsFileLocation != ""){
        log.info("Setting networkChangeEventsInput file: " + networkChangeEventsFileLocation);
            config.network().setTimeVariantNetwork(true);
            config.network().setChangeEventsInputFile(networkChangeEventsFileLocation);
        }


        config.plans().setActivityDurationInterpretation(PlansConfigGroup.ActivityDurationInterpretation.tryEndTimeThenDuration );
        //freight configstuff
        FreightConfigGroup freightConfigGroup = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
        freightConfigGroup.setTravelTimeSliceWidth(900);
        freightConfigGroup.setTimeWindowHandling(FreightConfigGroup.TimeWindowHandling.enforceBeginnings);

        return config;
    }

    private static Scenario prepareScenario(Config config) {
        Scenario scenario = ScenarioUtils.loadScenario(config);
        
        Carriers carriers = FreightUtils.getOrCreateCarriers(scenario);
      
        createAndaddCarriers(scenario.getNetwork(), carriers);
         
    	createAndLoadVehicleType(scenario);
    	
    	routePlans(scenario); 

        return scenario;
    }

	

	private static void routePlans(Scenario scenario) {		
		Carriers carriers = FreightUtils.getOrCreateCarriers(scenario);
		
		for (Carrier carrier: carriers.getCarriers().values()) {
			NetworkBasedTransportCosts.Builder tpcostsBuilder = NetworkBasedTransportCosts.Builder.newInstance(scenario.getNetwork(), carrier.getCarrierCapabilities().getVehicleTypes() );
			tpcostsBuilder.setTimeSliceWidth(900);
	        //assign netBasedCosts to RoutingProblem
	        NetworkBasedTransportCosts netbasedTransportcosts = tpcostsBuilder.build();
			NetworkRouter.routePlan(carrier.getSelectedPlan(), netbasedTransportcosts);
		}	
	}

	private static void createAndaddCarriers(Network network, Carriers carriers) {

    	//create one carrier for each our
    	for (int i = 0; i<1 ; i++) { //TODO  for debug only 1 our... set to 24 h later again.
    		Carrier carrier = CarrierUtils.createCarrier(Id.create("carrier" + i, Carrier.class));
    		
    		createAndAddCarrierSerivces(carrier, i);
    		
    		CarrierVehicle carrierVehicle = createCarrierVehicle("vehicle", "9826", i);
			CarrierUtils.addCarrierVehicle(carrier, carrierVehicle );
    		
    		CarrierPlan carrierPlan = createPlan(network, carrier, i);
			carrier.setSelectedPlan(carrierPlan);
		
    		carriers.addCarrier(carrier);
    	}
        
		
	}

	private static void createAndAddCarrierSerivces(Carrier carrier, int hour) {
		double tourStartInSec = hour * 3600. ;
		CarrierUtils.addService(carrier, createService(Id.create("Service1",CarrierService.class), "10837", tourStartInSec + 1387));
		CarrierUtils.addService(carrier, createService(Id.create("Service2",CarrierService.class), "37615", tourStartInSec + 1848));
		CarrierUtils.addService(carrier, createService(Id.create("Service3",CarrierService.class), "122985", tourStartInSec + 2509));
		CarrierUtils.addService(carrier, createService(Id.create("Service4",CarrierService.class), "67649", tourStartInSec + 2991));
//		CarrierUtils.addService(carrier, createService(Id.create("Service5",CarrierService.class), "linkId", tourStartInSec + 3523));
//		CarrierUtils.addService(carrier, createService(Id.create("Service6",CarrierService.class), "linkId", tourStartInSec + 4138));
//		CarrierUtils.addService(carrier, createService(Id.create("Service7",CarrierService.class), "linkId", tourStartInSec + 4775));
//		CarrierUtils.addService(carrier, createService(Id.create("Service8",CarrierService.class), "linkId", tourStartInSec + 5299));
//		CarrierUtils.addService(carrier, createService(Id.create("Service9",CarrierService.class), "linkId", tourStartInSec + 6072));
//		CarrierUtils.addService(carrier, createService(Id.create("Service10",CarrierService.class), "linkId", tourStartInSec + 6873));
//		CarrierUtils.addService(carrier, createService(Id.create("Service11",CarrierService.class), "linkId", tourStartInSec + 7277));
//		CarrierUtils.addService(carrier, createService(Id.create("Service12",CarrierService.class), "linkId", tourStartInSec + 7802));
//		CarrierUtils.addService(carrier, createService(Id.create("Service13",CarrierService.class), "linkId", tourStartInSec + 8401));
//		CarrierUtils.addService(carrier, createService(Id.create("Service14",CarrierService.class), "linkId", tourStartInSec + 8995));
//		CarrierUtils.addService(carrier, createService(Id.create("Service15",CarrierService.class), "linkId", tourStartInSec + 9755));
//		CarrierUtils.addService(carrier, createService(Id.create("Service16",CarrierService.class), "linkId", tourStartInSec + 10145));
//		CarrierUtils.addService(carrier, createService(Id.create("Service17",CarrierService.class), "linkId", tourStartInSec + 10898));
//		CarrierUtils.addService(carrier, createService(Id.create("Service18",CarrierService.class), "linkId", tourStartInSec + 11504));
//		CarrierUtils.addService(carrier, createService(Id.create("Service19",CarrierService.class), "linkId", tourStartInSec + 12341));	
		}

	//Erstellt Service entsprechend der vorgebenen Werte. Ermittelt auch automatisch die korrekte "ServiceBeginn" Zeit -> Fahrzeuge, die eher ankommen, sollen warten!
	private static CarrierService createService(Id<CarrierService> id, String linkId, Double startTimeWindowBegin) {
		CarrierService service = CarrierService.Builder.newInstance(id, Id.createLinkId(linkId))
				.setCapacityDemand(1)
				.setServiceDuration(0)
				.setServiceStartTimeWindow(TimeWindow.newInstance( startTimeWindowBegin, 36.*3600))
				.build();
		return service;
	}

	private static CarrierVehicle createCarrierVehicle(String vehicleId, String locationId, int i) {
		CarrierVehicle carrierVehicle = CarrierVehicle.Builder.newInstance(Id.createVehicleId(vehicleId), Id.createLinkId(locationId))
				.setEarliestStart(i*3600)
				.setTypeId(Id.create("vehType", VehicleType.class))
				.build();
		return carrierVehicle;
	}
	
	private static void createAndLoadVehicleType(Scenario scenario) {
		VehicleType vehType = VehicleUtils.createVehicleType(Id.create("vehType", VehicleType.class));
    	vehType.setMaximumVelocity(50.0); // m/s -> soll nicht limitiert sein
    	vehType.getCapacity().setOther(30);
    	vehType.getCostInformation().setCostsPerMeter(0.00033);
    	vehType.getCostInformation().setCostsPerSecond(0.0049);
    	vehType.getCostInformation().setFixedCost(42.33);
    	
        FreightUtils.getCarrierVehicleTypes(scenario).getVehicleTypes().put(vehType.getId(), vehType);
        
        new CarrierVehicleTypeLoader( FreightUtils.getCarriers(scenario) ).loadVehicleTypes( FreightUtils.getCarrierVehicleTypes(scenario) );
	}

	private static CarrierPlan createPlan(Network network, Carrier carrier, int i) {
		
		CarrierVehicle vehicle = CarrierUtils.getCarrierVehicle(carrier, Id.createVehicleId("vehicle"));
		Id<Link> depotLocation = vehicle.getLocation();
		double depTime = i * 3600. ;
		
		Tour.Builder tourBuilder = Tour.Builder.newInstance();
		
		tourBuilder.scheduleStart(depotLocation, TimeWindow.newInstance(depTime, Double.MAX_VALUE));
		tourBuilder.addLeg(new Leg());
		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service1", CarrierService.class)));
		tourBuilder.addLeg(new Leg());
		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service2", CarrierService.class)));
		tourBuilder.addLeg(new Leg());
		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service3", CarrierService.class)));
		tourBuilder.addLeg(new Leg());
		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service4", CarrierService.class)));
		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service5", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service6", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service7", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service8", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service9", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service10", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service11", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service12", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service13", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service14", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service15", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service16", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service17", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service18", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service19", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
//		tourBuilder.scheduleService(CarrierUtils.getService(carrier, Id.create("Service20", CarrierService.class)));
//		tourBuilder.addLeg(new Leg());
		tourBuilder.scheduleEnd(depotLocation);
		
		
		Tour vehicleTour = tourBuilder.build();
		
		assert(vehicleTour.getTourElements().size() == carrier.getServices().size()+2); // Alle Services + Start und Ende
		
		ScheduledTour scheduledTour = ScheduledTour.newInstance(vehicleTour, vehicle, depTime); 
		
		Collection<ScheduledTour> scheduledTours = new ArrayList<>();
		scheduledTours.add(scheduledTour);
		CarrierPlan plan = new CarrierPlan(carrier, scheduledTours);
		
		return plan;
	}

	private static Controler prepareControler(Scenario scenario) {
        Controler controler = new Controler(scenario);

        Freight.configure(controler);

        controler.addOverridingModule(new AbstractModule() {
            @Override
            public void install() {
                install(new CarrierModule());
//                bind(CarrierPlanStrategyManagerFactory.class).toInstance( null );
//                bind(CarrierScoringFunctionFactory.class).toInstance(null );
            }
        });

        return controler;
    }


//	/**
//	 * Run the MATSim simulation
//	 * 
//	 * @param scenario
//	 * @param carriers
//	 */
//	private static void matsimRun(Scenario scenario, Carriers carriers) {
//		final Controler controler = new Controler( scenario ) ;
//
//
//		CarrierScoringFunctionFactory scoringFunctionFactory = createMyScoringFunction2(scenario);
//		CarrierPlanStrategyManagerFactory planStrategyManagerFactory =  createMyStrategymanager(); //Benötigt, da listener kein "Null" als StrategyFactory mehr erlaubt, KT 17.04.2015
//
//
//		CarrierModule listener = new CarrierModule(carriers, planStrategyManagerFactory, scoringFunctionFactory) ;
//		controler.addOverridingModule(listener) ;
//		controler.run();
//	}
//
//
//	//Benötigt, da listener kein "Null" als StrategyFactory mehr erlaubt, KT 17.04.2015
//	//Da keine Strategy notwendig, hier zunächst eine "leere" Factory
//	private static CarrierPlanStrategyManagerFactory createMyStrategymanager(){
//		return new CarrierPlanStrategyManagerFactory() {
//			@Override
//			public GenericStrategyManager<CarrierPlan, Carrier> createStrategyManager() {
//				return null;
//			}
//		};
//	}


//	/**
//	 * TODO: Default CarrierScoringFunctionFactoryImpl in Freight contrib hinterlegen
//	 */
//	private static CarrierScoringFunctionFactoryImpl_KT createMyScoringFunction2 (final Scenario scenario) {
//
//		return new CarrierScoringFunctionFactoryImpl_KT(scenario, scenario.getConfig().controler().getOutputDirectory()) {
//
//			public ScoringFunction createScoringFunction(final Carrier carrier){
//				SumScoringFunction sumSf = new SumScoringFunction() ;
//
//				VehicleFixCostScoring fixCost = new VehicleFixCostScoring(carrier);
//				sumSf.addScoringFunction(fixCost);
//
//				LegScoring legScoring = new LegScoring(carrier);
//				sumSf.addScoringFunction(legScoring);
//
//				//Score Activity w/o correction of waitingTime @ 1st Service. -> Waiting time before first service will be scored
//				ActivityScoring actScoring = new ActivityScoring(carrier);
//				sumSf.addScoringFunction(actScoring);
//
//				//Alternativ:
//				//Score Activity with correction of waitingTime @ 1st Service. -> Ignore waiting time before first service 
////				ActivityScoringWithCorrection actScoring = new ActivityScoringWithCorrection(carrier);
////				sumSf.addScoringFunction(actScoring);
//
//				return sumSf;
//			}
//		};
//	}
	
//	private static Leg createLeg(Scenario scenario, Id<Link> fromLinkId, Id<Link> toLinkId, double departureTime ){
//		Leg leg = null;
//		
//		Module module = new org.matsim.core.controler.AbstractModule() {
//			
//			@Override
//			public void install() {
//				install ( new NewControlerModule() );
//				install ( new ControlerDefaultCoreListenersModule() );
//				install ( new ControlerDefaultsModule() );
//				install ( new ScenarioByInstanceModule(scenario) );
//			}
//		};
//		com.google.inject.Injector injector = Injector.createInjector(scenario.getConfig(), module);
//		TripRouter tripRouter = injector.getInstance(TripRouter.class);
//		
//		Facility fromFacility = FacilitiesUtils.wrapLink(scenario.getNetwork().getLinks().get(fromLinkId));
//		Facility toFacility = FacilitiesUtils.wrapLink(scenario.getNetwork().getLinks().get(toLinkId));
//		
//		List<? extends PlanElement> result = tripRouter.calcRoute(TransportMode.car, fromFacility, toFacility, departureTime, null);
//		
//		for (PlanElement pE : result) {
//			if (pE instanceof Leg) {
//				leg = (Leg) pE;
//			}
//		}
//		
//		return leg;
//		
//	}
}

