package org.matsim.drtBlockings;

import org.matsim.contrib.drt.optimizer.DrtOptimizer;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;

interface BlockingOptimizer extends DrtOptimizer {

    /**
     * this method is called every time a reservation request is submitted so that the ReservationOptimizer is notified of it
     * @param drtBlockingRequest
     */
    void blockingRequestSubmitted(DrtBlockingRequest drtBlockingRequest);

    /**
     *
     * determines whether {@code vehicle} is available for passenger transport.
     * @param vehicle
     * @return true, if {@code vehicle} is <i>not</i> available for passenger transport
     *
     */
    boolean isVehicleBlocked(DvrpVehicle vehicle);
}
