package org.matsim.pfav.run;

import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.router.util.TravelTimeUtils;
import org.matsim.pfav.privateAV.FreightTourPlanning;
import org.matsim.pfav.privateAV.PFAVUtils;

class RunTourPlanning {



    public static void main(String[] args) {

        String inputDir = "C:/Users/tschlenther/Desktop/input/";
        String carriersInput = "freight/revisedVehCosts_112019/carriers_gzBerlin_TruckWithDriver.xml";
        String carriersOutput = "freight/revisedVehCosts_112019/carriers_gzBerlin_TruckWithDriver_PLANNED.xml";
        String carrierVehtypes = "freight/revisedVehCosts_112019/vehicleTypes_PFAV_Revised112019.xml";
        String networkFile = "https://svn.vsp.tu-berlin.de/repos/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.2-10pct/input/berlin-v5-network.xml.gz";
        if(args.length != 0){
            inputDir = args[0];
            carriersInput = args[1];
            carriersOutput = args[2];
            carrierVehtypes = args[3];
            networkFile = args[4];
        }

        Carriers carriers = new Carriers();
        new CarrierPlanXmlReader(carriers).readFile(inputDir + carriersInput);

        CarrierVehicleTypes vehicleTypes = new CarrierVehicleTypes();
        new CarrierVehicleTypeReader(vehicleTypes).readFile(inputDir + carrierVehtypes);

        new CarrierVehicleTypeLoader(carriers).loadVehicleTypes(vehicleTypes);

        Network network = NetworkUtils.createNetwork();
        new MatsimNetworkReader(network).readFile(networkFile);

        FreightTourPlanning.runTourPlanningForCarriers(carriers, vehicleTypes, network, null, 1800, 100);

        new CarrierPlanXmlWriterV2(carriers).write(inputDir + carriersOutput);
    }
}
