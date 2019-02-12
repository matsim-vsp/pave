package privateAV.optimizer;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.MapConfiguration;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.PersonStuckEvent;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.taxi.optimizer.DefaultTaxiOptimizerParams;
import org.matsim.contrib.taxi.optimizer.TaxiOptimizer;
import org.matsim.contrib.taxi.optimizer.rules.RuleBasedTaxiOptimizerParams;
import org.matsim.contrib.taxi.passenger.TaxiRequest;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.contrib.taxi.schedule.TaxiTask;
import org.matsim.contrib.taxi.schedule.TaxiTask.TaxiTaskType;
import org.matsim.contrib.taxi.scheduler.TaxiScheduleInquiry;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.controler.events.BeforeMobsimEvent;
import org.matsim.core.controler.events.IterationStartsEvent;
import org.matsim.core.controler.listener.BeforeMobsimListener;
import org.matsim.core.controler.listener.IterationStartsListener;
import org.matsim.core.mobsim.framework.events.MobsimBeforeSimStepEvent;

import com.google.inject.Inject;

import freight.manager.PrivateAVFreightTourManager;
import freight.manager.SimpleFreightTourManager;
import privateAV.PFAVScheduler;

public class PFAVOptimizer implements TaxiOptimizer, IterationStartsListener {

	private static final Logger log = Logger.getLogger(PFAVScheduler.class);
	
	private Fleet fleet;
	private PFAVScheduler scheduler;
	private DefaultTaxiOptimizerParams params;
//	private EventsManager eventsManager;
	private boolean printDetailedWarnings;
	
	/**
	 * the freight contrib will be run before every iteration where iterationNumber % FREIGHTTOUR_PLANNING_INTERVAL == 0 
	 */
	private final int FREIGHTTOUR_PLANNING_INTERVAL = 5;
	
	@Inject
	public PFAVOptimizer(TaxiConfigGroup taxiCfg, Fleet fleet, TaxiScheduleInquiry scheduler) {
		
		if(!(scheduler instanceof PFAVScheduler)) {
			throw new IllegalArgumentException("this OptimizerPRovider can only work with a scheduler of type " + PFAVScheduler.class);
		} 
		
		Configuration optimizerConfig = new MapConfiguration(taxiCfg.getOptimizerConfigGroup().getParams());
		this.params = new RuleBasedTaxiOptimizerParams(optimizerConfig);
		
		this.fleet = fleet;
		this.scheduler = (PFAVScheduler) scheduler;
//		this.eventsManager = eventsManager;
		
		this.printDetailedWarnings = taxiCfg.isPrintDetailedWarnings();
	}
	
	public PFAVOptimizer(TaxiConfigGroup taxiCfg, Fleet fleet, PFAVScheduler scheduler,
			DefaultTaxiOptimizerParams params) {
		
		this.fleet = fleet;
		this.scheduler = scheduler;
		this.params = params;
//		this.eventsManager = eventsManager;
		
		this.printDetailedWarnings = taxiCfg.isPrintDetailedWarnings();
	}
	
	//TODO if vehicle is working on freight tour and arrival at owner is delayed, cancel freight tour and go back to depot
//	public void vehicleEnteredNextLink(DvrpVehicle vehicle, Link nextLink) {
////		scheduler.updateTimeline(vehicle);// TODO comment this out...
//
//	}

	@Override
	public void requestSubmitted(Request request) {
		
		TaxiRequest req = (TaxiRequest) request;
//		if(isValidRequest(req)) {
			
			//get the vehicle
			Id<DvrpVehicle> personalAV = Id.create(req.getPassengerId().toString() + "_av", DvrpVehicle.class);
			DvrpVehicle veh = fleet.getVehicles().get(personalAV);
			
			if (veh == null) {
				throw new RuntimeException("Vehicle " + personalAV.toString() + "does not exist.");
			}
			if(isCurrentlyOnOrWillPerformPerformFreightTour(veh)) {
				scheduler.cancelFreightTour(veh);
			} else {
				if (!isWaitStayOrEmptyDrive((TaxiTask)veh.getSchedule().getCurrentTask())) {
					throw new RuntimeException("Vehicle " + personalAV.toString() + "is not idle.");
				}
				if (((TaxiTask)veh.getSchedule().getCurrentTask()).getTaxiTaskType() == TaxiTaskType.EMPTY_DRIVE) {
					scheduler.stopCruisingVehicle(veh);
				}
			}
			
			scheduler.scheduleRequest(veh, req);
//		}
		

	}

//	private boolean isValidRequest(TaxiRequest request) {
//	Set<String> violations = requestValidator.validateTaxiRequest(request);
//		
//		if (!violations.isEmpty()) {
//			String causes = violations.stream().collect(Collectors.joining(", "));
//			if (printDetailedWarnings) log.warn("Request " + request.getId() + " will not be served. The agent will get stuck. Causes: " + causes);
//			request.setRejected(true);
//			eventsManager.processEvent(
//					new TaxiRequestRejectedEvent(request.getSubmissionTime(), request.getId(), causes));
//			eventsManager.processEvent(
//					new PersonStuckEvent(request.getSubmissionTime(), request.getPassenger().getId(),
//							request.getFromLink().getId(), request.getPassenger().getMode()));
//			return false;
//		}
//		
//		return true;
//	}


	@Override
	public void nextTask(DvrpVehicle vehicle) {
		scheduler.updateBeforeNextTask(vehicle);
		Task newCurrentTask = vehicle.getSchedule().nextTask();

	}

	@Override
	public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent e) {
		// TODO update timeline only if the algo really wants to reschedule in this time step,
		// perhaps by checking if there are any unplanned requests??
		if (params.doUpdateTimelines) {
			for (DvrpVehicle v : fleet.getVehicles().values()) {
				scheduler.updateTimeline(v);
			}
		}
	}
	
	private boolean isCurrentlyOnOrWillPerformPerformFreightTour(DvrpVehicle veh) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isWaitStayOrEmptyDrive(TaxiTask task) {
		return task.getTaxiTaskType() == TaxiTaskType.STAY || task.getTaxiTaskType() == TaxiTaskType.EMPTY_DRIVE;
	}

	@Override
	public void notifyIterationStarts(IterationStartsEvent event) {
		// TODO: 1) : run the freight contrib? read in a carrier plan file? => don't initialize the freightTourManager with a set of tours !?
		// TODO: 2) : let the freight contrib run again periodically? => yes, since we need to give it appropiate travel times, which we only have after a few iterations..
		if(event.getIteration() % FREIGHTTOUR_PLANNING_INTERVAL == 0) {
			log.info("RUNNING FREIGHT CONTRIB TO CALCULATE FREIGHT TOURS BASED ON CURRENT TRAVEL TIMES");
			scheduler.calculateFreightTours();
		}
	}



}
