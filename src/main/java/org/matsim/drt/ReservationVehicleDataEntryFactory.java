package org.matsim.drt;

import com.google.inject.Provider;
import org.matsim.contrib.drt.optimizer.VehicleData;
import org.matsim.contrib.drt.optimizer.VehicleDataEntryFactoryImpl;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.core.config.Config;

import javax.inject.Inject;

 class ReservationVehicleDataEntryFactory  implements VehicleData.EntryFactory {

    private final VehicleDataEntryFactoryImpl delegate;
    ReservationOptimizer reservationOptimizer;

    ReservationVehicleDataEntryFactory(DrtConfigGroup drtCfg, ReservationOptimizer optimizer) {
        this.delegate = new VehicleDataEntryFactoryImpl(drtCfg);
        this.reservationOptimizer = optimizer;
    }

    @Override
    public VehicleData.Entry create(DvrpVehicle vehicle, double currentTime) {
        if(! reservationOptimizer.isVehicleReserved(vehicle)){
            return delegate.create(vehicle, currentTime);
        }
        return null;
    }

    static class ReservationVehicleDataEntryFactoryProvider implements Provider<ReservationVehicleDataEntryFactory> {
        @Inject
        private Config config;

        @Inject
        ReservationOptimizer optimizer;

        @Override
        public ReservationVehicleDataEntryFactory get() {
            return new ReservationVehicleDataEntryFactory(DrtConfigGroup.getSingleModeDrtConfig(config), optimizer);
        }
    }
}
