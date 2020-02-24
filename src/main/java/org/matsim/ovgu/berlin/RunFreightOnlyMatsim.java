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

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.Freight;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.controler.CarrierModule;
import org.matsim.contrib.freight.controler.CarrierPlanStrategyManagerFactory;
import org.matsim.contrib.freight.controler.CarrierScoringFunctionFactory;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.config.consistency.VspConfigConsistencyCheckerImpl;
import org.matsim.core.config.groups.ControlerConfigGroup.CompressionType;
import org.matsim.core.config.groups.PlansConfigGroup;
import org.matsim.core.config.groups.QSimConfigGroup.TrafficDynamics;
import org.matsim.core.config.groups.StrategyConfigGroup.StrategySettings;
import org.matsim.core.config.groups.VspExperimentalConfigGroup.VspDefaultsCheckingLevel;
import org.matsim.core.controler.AbstractModule;
import org.matsim.core.controler.Controler;
import org.matsim.core.controler.ControlerUtils;
import org.matsim.core.controler.OutputDirectoryHierarchy;
import org.matsim.core.controler.OutputDirectoryHierarchy.OverwriteFileSetting;
import org.matsim.core.controler.OutputDirectoryLogging;
import org.matsim.core.replanning.GenericStrategyManager;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.scoring.ScoringFunction;
import org.matsim.core.scoring.SumScoringFunction;
import org.matsim.vehicles.VehicleType;
import org.matsim.vehicles.VehicleUtils;
import org.osgeo.proj4j.UnsupportedParameterException;

import javax.management.InvalidAttributeValueException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
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
	                    inputPath + "networkChangeEvents.xml.gz",
	                    "../Output/MyOutput"}  ;
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
        freightConfigGroup.setTravelTimeSliceWidth(1800);
        freightConfigGroup.setTimeWindowHandling(FreightConfigGroup.TimeWindowHandling.enforceBeginnings);

        return config;
    }

    private static Scenario prepareScenario(Config config) {
        Scenario scenario = ScenarioUtils.loadScenario(config);
        
        Carriers carriers = FreightUtils.getOrCreateCarriers(scenario);
      
        createAndaddCarriers(carriers);
         
    	createAndLoadVehicleType(scenario);

        return scenario;
    }


	
	
    private static void createAndaddCarriers(Carriers carriers) {

    	//create one carrier for each our
    	for (int i = 0; i<24 ; i++) {
    		Carrier carrier = CarrierUtils.createCarrier(Id.create("carrier" + i, Carrier.class));
    		
    		createAndAddCarrierSerivces(carrier, i);
    		
    		CarrierVehicle carrierVehicle = createCarrierVehicle("vehicle", "linkId", i);
			CarrierUtils.addCarrierVehicle(carrier, carrierVehicle );
    		
    		CarrierPlan carrierPlan = createPlan(carrier, i);
			carrier.addPlan(carrierPlan);
		
    		carriers.addCarrier(carrier);
    	}
        
		
	}

	private static void createAndAddCarrierSerivces(Carrier carrier, int i) {
		CarrierUtils.addService(carrier, createService(Id.create("id",CarrierService.class), "linkId", "time" ,i)); //time = planned time after leaving depot in sec.
	}

	//Erstellt Service entsprechend der vorgebenen Werte. Ermittelt auch aitomatishc die korrekte "ServiceBegin"Zeit -> Fahrzeuge, die eher ankommen, sollen warten!
	private static CarrierService createService(Id<CarrierService> id, String linkId, String time, int i) {
		CarrierService service = CarrierService.Builder.newInstance(id, Id.createLinkId(linkId))
				.setCapacityDemand(1)
				.setServiceDuration(0)
				.setServiceStartTimeWindow(TimeWindow.newInstance(i*3600 + Double.parseDouble(time), 36.*3600))
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
    	
        FreightUtils.getCarrierVehicleTypes(scenario).getVehicleTypes().put(vehType.getId(), vehType);
        
        new CarrierVehicleTypeLoader( FreightUtils.getCarriers(scenario) ).loadVehicleTypes( FreightUtils.getCarrierVehicleTypes(scenario) );
	}

	private static CarrierPlan createPlan(Carrier carrier, int i) {
		
		Collection<ScheduledTour> scheduledTours;
		Tour tour = Tour.Builder.newInstance()
				.scheduleStart(CarrierUtils.getCarrierVehicle(carrier, Id.createVehicleId("vehicle")).getLocation())
				//TODO: Create Tour
				.scheduleEnd(CarrierUtils.getCarrierVehicle(carrier, Id.createVehicleId("vehicle")).getLocation())
				.build();
		ScheduledTour scheduledTour = ScheduledTour.newInstance(tour, vehicle, i*3600); 
		CarrierPlan plan = new CarrierPlan(carrier, scheduledTours)
		// TODO Auto-generated method stub
		return null;
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
}

