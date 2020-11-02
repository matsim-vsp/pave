package org.matsim.drtBlockings;

import org.junit.BeforeClass;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.Controler;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;

import javax.management.InvalidAttributeValueException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class NeverStartedDrtToursTest {

    private static final String PATH = "chessboard/";
    private static final String INPUT_CONFIG = "chessboard_drtBlocking_config.xml";
    private static final String INPUT_CARRIERS = "chessboard_carriers_drtBlocking.xml";
    private static final String INPUT_VEHICLE_TYPES = "chessboard_vehicleTypes.xml";

    static Logger logger = Logger.getLogger(NeverStartedDrtToursTest.class.getName());

    private static DrtBlockingTest.DrtBlockingDispatchLogicHandler dispatchLogicHandler;
    private static DrtBlockingTest.DrtBlockingTimeLogicHandler timeLogicHandler;

    @BeforeClass
    public static final void runChessboardTestScenario() throws InvalidAttributeValueException {
        dispatchLogicHandler = new DrtBlockingTest.DrtBlockingDispatchLogicHandler();
        timeLogicHandler = new DrtBlockingTest.DrtBlockingTimeLogicHandler();
        Controler controler = DrtBlockingTestControlerCreator.createControlerForChessboardTest(Arrays.asList(dispatchLogicHandler, timeLogicHandler));
        Config config = controler.getConfig();
//        Network network = NetworkUtils.readNetwork(config.network().getInputFile());
        Network network = NetworkUtils.createNetwork();
        new MatsimNetworkReader(network).readFile(config.network().getInputFile());

//        Iterator iterator = network.getLinks().entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry pair = (Map.Entry)iterator.next();
//            Id<Link> linkId = pair.getKey();
//
//        }

        network.getLinks().values().forEach(link -> link.setLength(10000.0));
//        this is the same as above!
//        for (Link link : network.getLinks().values()) {
//            link.setLength(10000.0);
//        }



        controler.run();
    }
}
