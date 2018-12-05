package privateAV.infrastructure.delegated;

import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.dvrp.data.Fleet;
import org.matsim.contrib.dvrp.data.Request;
import org.matsim.contrib.dvrp.data.Vehicle;
import org.matsim.contrib.taxi.data.validator.TaxiRequestValidator;
import org.matsim.contrib.taxi.optimizer.DefaultTaxiOptimizer;
import org.matsim.contrib.taxi.optimizer.DefaultTaxiOptimizerParams;
import org.matsim.contrib.taxi.optimizer.TaxiOptimizer;
import org.matsim.contrib.taxi.optimizer.UnplannedRequestInserter;
import org.matsim.contrib.taxi.run.TaxiConfigGroup;
import org.matsim.contrib.taxi.scheduler.TaxiScheduler;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.mobsim.framework.events.MobsimBeforeSimStepEvent;

public class PrivateAV4FreightOptimizer implements TaxiOptimizer {

	DefaultTaxiOptimizer delegate;
	
	public PrivateAV4FreightOptimizer(TaxiConfigGroup taxiCfg, Fleet fleet, TaxiScheduler scheduler,
			DefaultTaxiOptimizerParams params, UnplannedRequestInserter requestInserter,
			TaxiRequestValidator requestValidator, EventsManager eventsManager) {
		delegate = new DefaultTaxiOptimizer(taxiCfg, fleet, scheduler, params, requestInserter, requestValidator, eventsManager);
	}
	

	@Override
	public void vehicleEnteredNextLink(Vehicle vehicle, Link nextLink) {
		//TODO if vehicle is working on freight tour and arrival at owner is delayed, cancel freight tour and go back to depot
		
	}

	@Override
	public void requestSubmitted(Request request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void nextTask(Vehicle vehicle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyMobsimBeforeSimStep(MobsimBeforeSimStepEvent e) {
		// TODO Auto-generated method stub

	}

}
