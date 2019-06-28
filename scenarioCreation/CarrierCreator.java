/* *********************************************************************** *
 * project: org.matsim.*												   *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2008 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */
package scenarioCreation;

import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.carrier.CarrierCapabilities.FleetSize;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.router.util.TravelTime;
import org.matsim.core.router.util.TravelTimeUtils;
import privateAV.FreightTourPlanning;

/**
 * @author tschlenther
 */
public class CarrierCreator {

    private static final FleetSize FLEET_SIZE = FleetSize.INFINITE;
    private static final int NR_OF_CARRIERS = 2;
    private static final int NR_OF_VEH_PER_CARRIER_PER_VEH_TYPE = 5;
    private static final int NR_OF_SERVICES_PER_CARRIER = 100;

    private static final String OUTDIR = "input/Scenarios/mielec/freight/upToDate/";

    private static final String INPUT_NETWORK = OUTDIR + "../../" + "network.xml";
    private static final String OUTPUT_CARRIERS = OUTDIR + NR_OF_CARRIERS + "carriers_a_" +
            NR_OF_VEH_PER_CARRIER_PER_VEH_TYPE + "vehicles_" +
            FLEET_SIZE + ".xml";
    private static final String OUT_VTYPES = OUTDIR + "PFAV" + "vehicleTypes.xml";


    /**
     * @param args
     */
    public static void main(String[] args) {

        Network network = NetworkUtils.createNetwork();
        new MatsimNetworkReader(network).readFile(INPUT_NETWORK);

        CarrierVehicleType privateAVCarrierVehType = FreightSetUp.createPrivateFreightAVVehicleType();
        CarrierVehicleTypes vTypes = new CarrierVehicleTypes();
        vTypes.getVehicleTypes().put(privateAVCarrierVehType.getId(), privateAVCarrierVehType);

        Carriers carriers = FreightSetUp.createCarriersWithRandomDepotAndServices(vTypes.getVehicleTypes().values(), FLEET_SIZE, network, NR_OF_CARRIERS, NR_OF_VEH_PER_CARRIER_PER_VEH_TYPE, NR_OF_SERVICES_PER_CARRIER);

        TravelTime travelTime = TravelTimeUtils.createFreeSpeedTravelTime();
        FreightTourPlanning.runTourPlanningForCarriers(carriers, vTypes, network, travelTime, 1800);
        CarrierPlanXmlWriterV2 planWriter = new CarrierPlanXmlWriterV2(carriers);
        planWriter.write(OUTPUT_CARRIERS);
        new CarrierVehicleTypeWriter(vTypes).write(OUT_VTYPES);
    }

}
