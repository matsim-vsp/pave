package privateAV.optimizer;

import com.google.inject.Inject;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.MapConfiguration;
import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
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
import org.matsim.core.mobsim.framework.events.MobsimBeforeSimStepEvent;
import privateAV.PFAVScheduler;

public class PFAVOptimizer implements TaxiOptimizer {

	private static final Logger log = Logger.getLogger(PFAVScheduler.class);
	
	private Fleet fleet;
	private PFAVScheduler scheduler;
	private DefaultTaxiOptimizerParams params;
	//TODO: use the setting for log messages
	private boolean printDetailedWarnings;

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

			//get the vehicle
			Id<DvrpVehicle> personalAV = Id.create(req.getPassengerId().toString() + "_av", DvrpVehicle.class);
			DvrpVehicle veh = fleet.getVehicles().get(personalAV);
			
			if (veh == null) {
				throw new RuntimeException("Vehicle " + personalAV.toString() + "does not exist.");
			}
			if(scheduler.isCurrentlyOnOrWillPerformPerformFreightTour(veh)) {
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


    }

	@Override
	public void nextTask(DvrpVehicle vehicle) {
		scheduler.updateBeforeNextTask(vehicle);
		Task newCurrentTask = vehicle.getSchedule().nextTask();
	}

	@Override
	public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent e) {
        // TODO i think we do not need this !! DELETE this in order to speed up simulation
		// perhaps by checking if there are any unplanned requests??
		if (params.doUpdateTimelines) {
			for (DvrpVehicle v : fleet.getVehicles().values()) {
				scheduler.updateTimeline(v);
			}
		}
	}

	private boolean isWaitStayOrEmptyDrive(TaxiTask task) {
		return task.getTaxiTaskType() == TaxiTaskType.STAY || task.getTaxiTaskType() == TaxiTaskType.EMPTY_DRIVE;
	}

}
