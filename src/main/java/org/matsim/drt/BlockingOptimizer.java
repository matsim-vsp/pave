package org.matsim.drt;

import org.matsim.contrib.drt.optimizer.DrtOptimizer;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;

interface BlockingOptimizer extends DrtOptimizer {

    /**
     * this method is called every time a reservation request is submitted so that the ReservationOptimizer is notified of it
     * @param drtBlocking
     */
    void blockingRequestSubmitted(DrtBlocking drtBlocking);

    boolean isVehicleBlocked(DvrpVehicle vehicle);

}
