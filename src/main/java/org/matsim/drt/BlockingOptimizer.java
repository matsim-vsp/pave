package org.matsim.drt;

import org.matsim.contrib.drt.optimizer.DrtOptimizer;

interface BlockingOptimizer extends DrtOptimizer {

    /**
     * this method is called every time a reservation request is submitted so that the ReservationOptimizer is notified of it
     * @param drtBlockingRequest
     */
    void blockingRequestSubmitted(DrtBlockingRequest drtBlockingRequest);

}
