package privateAV.infrastructure.delegated;

import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.PersonStuckEvent;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.data.Fleet;
import org.matsim.contrib.dvrp.data.Request;
import org.matsim.contrib.dvrp.data.Vehicle;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.taxi.data.TaxiRequest;
import org.matsim.contrib.taxi.data.validator.TaxiRequestValidator;
import org.matsim.contrib.taxi.optimizer.DefaultTaxiOptimizerParams;
import org.matsim.contrib.taxi.optimizer.TaxiOptimizer;
import org.matsim.contrib.taxi.passenger.TaxiRequestRejectedEvent;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.contrib.taxi.schedule.TaxiTask;
import org.matsim.contrib.taxi.schedule.TaxiTask.TaxiTaskType;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.controler.events.BeforeMobsimEvent;
import org.matsim.core.controler.listener.BeforeMobsimListener;
import org.matsim.core.mobsim.framework.events.MobsimBeforeSimStepEvent;

import freight.manager.PrivateAVFreightTourManager;
import freight.manager.SimpleFreightTourManager;

public class PrivateAV4FreightOptimizer implements TaxiOptimizer, BeforeMobsimListener {

	private static final Logger log = Logger.getLogger(PrivateAVFreightSchedulerV2.class);
	
	private Fleet fleet;
	private PrivateAVFreightSchedulerV2 scheduler;
	private DefaultTaxiOptimizerParams params;
	private TaxiRequestValidator requestValidator;
	private EventsManager eventsManager;
	private boolean printDetailedWarnings;
	
	public PrivateAV4FreightOptimizer(TaxiConfigGroup taxiCfg, Fleet fleet, PrivateAVFreightSchedulerV2 scheduler,
			DefaultTaxiOptimizerParams params, 	TaxiRequestValidator requestValidator, EventsManager eventsManager) {
		
		this.fleet = fleet;
		this.scheduler = scheduler;
		this.params = params;
		this.requestValidator = requestValidator;
		this.eventsManager = eventsManager;
		
		this.printDetailedWarnings = taxiCfg.isPrintDetailedWarnings();
	}
	

	@Override
	public void vehicleEnteredNextLink(Vehicle vehicle, Link nextLink) {
		//TODO if vehicle is working on freight tour and arrival at owner is delayed, cancel freight tour and go back to depot
//		scheduler.updateTimeline(vehicle);// TODO comment this out...

	}

	@Override
	public void requestSubmitted(Request request) {
		
		TaxiRequest req = (TaxiRequest) request;
		if(isValidRequest(req)) {
			
			//get the vehicle
			Id<Vehicle> personalAV = Id.create(req.getPassenger().getId().toString() + "_av", Vehicle.class);
			Vehicle veh = fleet.getVehicles().get(personalAV);
			
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
		}
		

	}

	private boolean isValidRequest(TaxiRequest request) {
	Set<String> violations = requestValidator.validateTaxiRequest(request);
		
		if (!violations.isEmpty()) {
			String causes = violations.stream().collect(Collectors.joining(", "));
			if (printDetailedWarnings) log.warn("Request " + request.getId() + " will not be served. The agent will get stuck. Causes: " + causes);
			request.setRejected(true);
			eventsManager.processEvent(
					new TaxiRequestRejectedEvent(request.getSubmissionTime(), request.getId(), causes));
			eventsManager.processEvent(
					new PersonStuckEvent(request.getSubmissionTime(), request.getPassenger().getId(),
							request.getFromLink().getId(), request.getPassenger().getMode()));
			return false;
		}
		
		return true;
	}


	@Override
	public void nextTask(Vehicle vehicle) {
		scheduler.updateBeforeNextTask(vehicle);
		Task newCurrentTask = vehicle.getSchedule().nextTask();

	}

	@Override
	public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent e) {
		// TODO update timeline only if the algo really wants to reschedule in this time step,
		// perhaps by checking if there are any unplanned requests??
		if (params.doUpdateTimelines) {
			for (Vehicle v : fleet.getVehicles().values()) {
				scheduler.updateTimeline(v);
			}
		}
	}
	
	private boolean isCurrentlyOnOrWillPerformPerformFreightTour(Vehicle veh) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean isWaitStayOrEmptyDrive(TaxiTask task) {
		return task.getTaxiTaskType() == TaxiTaskType.STAY || task.getTaxiTaskType() == TaxiTaskType.EMPTY_DRIVE;
	}


	@Override
	public void notifyBeforeMobsim(BeforeMobsimEvent event) {
		if(event.getIteration() == 0) {
		}
		
	}

}
