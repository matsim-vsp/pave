package privateAV;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.taxi.optimizer.AbstractTaxiOptimizerParams;
import org.matsim.contrib.taxi.optimizer.TaxiOptimizer;
import org.matsim.contrib.taxi.passenger.TaxiRequest;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.mobsim.framework.MobsimTimer;
import org.matsim.core.mobsim.framework.events.MobsimBeforeSimStepEvent;
import privateAV.events.PFAVOwnerWaitsForVehicleEvent;

final class PFAVOptimizer implements TaxiOptimizer {

	private static final Logger log = Logger.getLogger(PFAVScheduler.class);
	private final EventsManager eventsManager;
	private final MobsimTimer timer;

	private Fleet fleet;
	private PFAVScheduler scheduler;
	private AbstractTaxiOptimizerParams params;
	//TODO: use the setting for log messages
	private boolean printDetailedWarnings;

	PFAVOptimizer(TaxiConfigGroup taxiCfg, Fleet fleet, PFAVScheduler scheduler, EventsManager eventsManager,
				  MobsimTimer timer) {
		
		this.fleet = fleet;
		this.scheduler = scheduler;
		this.params = taxiCfg.getTaxiOptimizerParams();
		this.eventsManager = eventsManager;
		this.timer = timer;
		
		this.printDetailedWarnings = taxiCfg.isPrintDetailedWarnings();
	}

	@Override
	public void requestSubmitted(Request request) {
		
		TaxiRequest req = (TaxiRequest) request;

			//get the vehicle
		Id<DvrpVehicle> personalAV = Id.create(req.getPassengerId().toString() + PFAVUtils.PFAV_ID_SUFFIX, DvrpVehicle.class);

        log.info("agent " + req.getPassengerId() + " submitted a request at time= " + req.getSubmissionTime());
        DvrpVehicle veh = fleet.getVehicles().get(personalAV);

        if (veh == null) {
				throw new RuntimeException("Vehicle " + personalAV.toString() + "does not exist.");
			}
			if(scheduler.isCurrentlyOnOrWillPerformPerformFreightTour(veh)) {

				log.warn("agent " + req.getPassengerId() + " submitted request at time=" + req.getSubmissionTime() + " for it's own vehicle " + personalAV +
						" which is still on a freight tour (or maybe on the way back). currently, freight tours do not get canceled...");
				eventsManager.processEvent(new PFAVOwnerWaitsForVehicleEvent(timer.getTimeOfDay(), personalAV, req.getPassengerId()));
//				scheduler.cancelFreightTour(veh);

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
        // TODO we do not need this !! DELETE this in order to speed up simulation
		// in fact, doUpdateTimelines is currently always false, since we use RuleBasedOptimizerParams, see PFAVProvider and constructor of RuleBasedOptimizerParams
		if (params.doUpdateTimelines) {
			for (DvrpVehicle v : fleet.getVehicles().values()) {
				scheduler.updateTimeline(v);
			}
		}
	}


}
