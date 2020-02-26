package org.matsim.drt;

import com.google.inject.Provider;
import org.matsim.contrib.drt.optimizer.VehicleData;
import org.matsim.contrib.drt.optimizer.VehicleDataEntryFactoryImpl;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.core.config.Config;

import javax.inject.Inject;

 class BlockingVehicleDataEntryFactory implements VehicleData.EntryFactory {

    private final VehicleDataEntryFactoryImpl delegate;
    DrtBlockingManager blockingManager;

    BlockingVehicleDataEntryFactory(DrtConfigGroup drtCfg, DrtBlockingManager blockingManager) {
        this.delegate = new VehicleDataEntryFactoryImpl(drtCfg);
        this.blockingManager = blockingManager;
    }

    @Override
    public VehicleData.Entry create(DvrpVehicle vehicle, double currentTime) {
        if(! blockingManager.isVehicleBlocked(vehicle, currentTime)){
            return delegate.create(vehicle, currentTime);
        }
        return null;
    }

    static class ReservationVehicleDataEntryFactoryProvider implements Provider<BlockingVehicleDataEntryFactory> {
        @Inject
        private Config config;

        @Inject
        DrtBlockingManager blockingManager;

        @Override
        public BlockingVehicleDataEntryFactory get() {
            return new BlockingVehicleDataEntryFactory(DrtConfigGroup.getSingleModeDrtConfig(config), blockingManager);
        }
    }
}
