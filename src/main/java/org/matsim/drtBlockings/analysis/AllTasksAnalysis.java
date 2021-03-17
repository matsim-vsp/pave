package org.matsim.drtBlockings.analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.drt.run.DrtConfigGroup;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelData;
import org.matsim.contrib.dvrp.path.VrpPathWithTravelDataImpl;
import org.matsim.contrib.dvrp.path.VrpPaths;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.dvrp.vrpagent.TaskEndedEvent;
import org.matsim.contrib.dvrp.vrpagent.TaskEndedEventHandler;
import org.matsim.contrib.dvrp.vrpagent.TaskStartedEvent;
import org.matsim.contrib.dvrp.vrpagent.TaskStartedEventHandler;
import org.matsim.contrib.freight.FreightConfigGroup;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.freight.utils.FreightUtils;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.drtBlockings.DrtBlockingRequest;
import org.matsim.drtBlockings.FreightDrtActionCreator;
import org.matsim.drtBlockings.events.*;
import org.matsim.drtBlockings.tasks.FreightDriveTask;
import org.matsim.drtBlockings.tasks.FreightPickupTask;
import org.matsim.drtBlockings.tasks.FreightRetoolTask;
import org.matsim.drtBlockings.tasks.FreightServiceTask;
import org.matsim.vehicles.Vehicle;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class AllTasksAnalysis implements ActivityStartEventHandler, DrtBlockingRequestScheduledEventHandler, DrtBlockingEndedEventHandler,TaskStartedEventHandler, TaskEndedEventHandler, IterationEndsListener {

    private Carriers carriers;
    private String mode = "drt";
    static final double RETOOL_DURATION = 1.5 * 60;
    static final double SUBMISSION_LOOK_AHEAD = 15 * 60;

    private Map<Id<CarrierService>, ActivityStartEvent> serviceStartEvents = new HashMap<>();
    private Map<Id<DvrpVehicle>, TaskStartedEvent> taskStartEvents = new HashMap<>();
    private Map<Id<DvrpVehicle>, TaskEndedEvent> taskEndEvents = new HashMap<>();
    private Map<Id<DvrpVehicle>, RequestData> currentTours = new HashMap<>();
    private List<RequestData> finishedTours = new ArrayList<>();

    private List<DrtBlockingRequest> requests = new ArrayList<>();


    public AllTasksAnalysis(Carriers carriers){
        this.carriers = carriers;
    }

    public static void main(String args[]) {


        String dir = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/berlin-v5.5-10pct/policy_cases/test/";
        String inputConfig = dir + "p2-23.output_config.xml";
        String inputNetwork = dir + "p2-23.output_network.xml.gz";
//        String carrierPlans = dir + "carriers_4hTimeWindows_openBerlinNet_8-24_PLANNED.xml";
        String carrierPlans = dir + "carriers_4hTimeWindows_openBerlinNet_LichtenbergNord_8-24_PLANNED.xml";
        String carrierVehTypes = dir + "carrier_vehicleTypes.xml";
        String eventsFile = dir + "p2-23DRTBlockingPolicyCase.2.events.xml.gz";
        String outputFile = dir + "TaskAnalysis.csv";

        Config config = ConfigUtils.loadConfig(inputConfig);
        config.network().setInputFile(inputNetwork);
        FreightConfigGroup freightConfig = ConfigUtils.addOrGetModule(config, FreightConfigGroup.class);
        freightConfig.setCarriersFile(carrierPlans);
        freightConfig.setCarriersVehicleTypesFile(carrierVehTypes);
        freightConfig.setTimeWindowHandling(FreightConfigGroup.TimeWindowHandling.enforceBeginnings);

        Scenario scenario = ScenarioUtils.loadScenario(config);

        FreightUtils.loadCarriersAccordingToFreightConfig(scenario);
        Carriers carriers = FreightUtils.getCarriers(scenario);
        Network network = scenario.getNetwork();



        EventsManager manager = EventsUtils.createEventsManager();
        AllTasksAnalysis analysis = new AllTasksAnalysis(carriers);
        manager.addHandler(analysis);
        manager.initProcessing();
        MatsimEventsReader reader = DrtBlockingEventsReader.create(manager);
        reader.readFile(eventsFile);
        manager.finishProcessing();
        analysis.createSchedulesAndFindCorrespondingTour(carriers, network);
        try {
            analysis.writeAnalysis(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleEvent(ActivityStartEvent event) {
        if(event.getActType().startsWith(FreightDrtActionCreator.SERVICE_ACTTYPE_PREFIX)){
            Id<CarrierService> serviceId = Id.create(event.getActType().substring(event.getActType().indexOf("_") + 1), CarrierService.class);
            if(!serviceStartEvents.containsKey(serviceId))
                this.serviceStartEvents.putIfAbsent(serviceId, event);
        }
    }

    @Override
    public void handleEvent(TaskStartedEvent event) {

        Task.TaskType type = event.getTaskType();
        Id<DvrpVehicle> vehicleId = event.getDvrpVehicleId();

        if(currentTours.containsKey(vehicleId)) {

            if(type.equals(FreightDriveTask.FREIGHT_DRIVE_TASK_TYPE) || type.equals(FreightRetoolTask.RETOOL_TASK_TYPE)
                    || type.equals(FreightServiceTask.FREIGHT_SERVICE_TASK_TYPE)) {

                RequestData data = currentTours.get(vehicleId);
                if(!(data.taskStartedEvents == null)) {
                    data.taskStartedEvents.add(event);
                } else {
                    List<TaskStartedEvent> newList = new ArrayList<>();
                    newList.add(event);
                    data.taskStartedEvents = newList;
                }

            }
        }
    }

    @Override
    public void handleEvent(TaskEndedEvent event) {

        Task.TaskType type = event.getTaskType();
        Id<DvrpVehicle> vehicleId = event.getDvrpVehicleId();

        if(currentTours.containsKey(vehicleId)) {

            if(type.equals(FreightDriveTask.FREIGHT_DRIVE_TASK_TYPE) || type.equals(FreightRetoolTask.RETOOL_TASK_TYPE)
            || type.equals(FreightServiceTask.FREIGHT_SERVICE_TASK_TYPE)) {

                RequestData data = currentTours.get(vehicleId);
                if(!(data.taskEndedEvents == null)) {
                    data.taskEndedEvents.add(event);
                } else {
                    List<TaskEndedEvent> newList = new ArrayList<>();
                    newList.add(event);
                    data.taskEndedEvents = newList;
                }

            }
        }
    }

    @Override
    public void handleEvent(DrtBlockingRequestScheduledEvent event) {

        RequestData data = new RequestData(event.getVehicleId());
        data.scheduledEvent = event;
        this.currentTours.put(event.getVehicleId(), data);

    }

    @Override
    public void handleEvent(DrtBlockingEndedEvent event) {

        Id<DvrpVehicle> vehicleId = event.getVehicleId();

        if(this.currentTours.containsKey(vehicleId)) {
            RequestData data = this.currentTours.remove(vehicleId);
            this.finishedTours.add(data);
        }

    }

    @Override
    public void reset(int iteration) {
        this.serviceStartEvents.clear();
    }

    @Override
    public void notifyIterationEnds(IterationEndsEvent event) {
        try {
            writeAnalysis(event.getServices().getControlerIO().getIterationFilename(event.getIteration(), "conductedServices.csv"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createSchedulesAndFindCorrespondingTour(Carriers carriers, Network network) {
        this.requests.addAll(createBlockingRequests(carriers, network));
        for(DrtBlockingRequest request : this.requests) {
            Id<Request> requestId = request.getId();
            for(RequestData data : this.finishedTours) {
                if(data.scheduledEvent.getRequestId().equals(requestId)) {
                    data.request = request;
                    break;
                }
            }
        }
    }

    public Set<DrtBlockingRequest> createBlockingRequests(Carriers carriers, Network network) {
        Set<DrtBlockingRequest> requests = new HashSet<>();

        carriers.getCarriers().values().forEach(carrier -> {
                System.out.println("CARRIER: " + carrier.getId());
                requests.addAll(createBlockingRequestsForCarrier(carrier, network));
        });
        return requests;
    }

    private Set<DrtBlockingRequest> createBlockingRequestsForCarrier(Carrier carrier, Network network){
        Set<DrtBlockingRequest> requests = new HashSet<>();
        Map<Id<Vehicle>, Integer> vehicleCount = new HashMap<>();

//        int count = 0;
        int vehCount;
        for (ScheduledTour tour : carrier.getSelectedPlan().getScheduledTours()){

            if(vehicleCount.get(tour.getVehicle().getId()) == null){
                vehCount = 1;
            } else{
                vehCount = vehicleCount.get(tour.getVehicle().getId()) + 1;
                vehicleCount.replace(tour.getVehicle().getId(), vehCount);
            }

            //TODO add noOfServices to tourId!
            //or better Id of first service!
//            String tourID = carrier.getId() + "" + tour.getVehicle().getId() + "_" + vehCount; /* + "_" + count;*/
            String tourID = carrier.getId() + "" + tour.getVehicle().getId() + "_" + getFirstTourServiceOrShipmentForId(tour.getTour());
            requests.add(createRequest(carrier.getId(), Id.create(tour.getVehicle().getId(), DvrpVehicle.class), tour, tourID, network));
//            count++;
        }
        return requests;
    }

    private DrtBlockingRequest createRequest(Id<Carrier> carrierId, Id<DvrpVehicle> vehicleId, ScheduledTour scheduledTour, String tourID, Network network) {
        Id<Request> id = Id.create(tourID, Request.class);
        String mode = this.mode;
        double blockingStart = determineStartOfBlocking(scheduledTour);
        double submissionTime = blockingStart - SUBMISSION_LOOK_AHEAD;

        List<Task> tourTasks = convertScheduledTour2DvrpTasks(scheduledTour, blockingStart, network);
        double blockingEnd = tourTasks.get(tourTasks.size() - 1).getEndTime();

        DrtBlockingRequest request = DrtBlockingRequest.newBuilder()
                .id(id)
                .mode(mode)
                .carrierId(carrierId)
                .submissionTime(submissionTime)
                .duration(blockingEnd - blockingStart)
                .tasks(tourTasks)
                .startTime(blockingStart)
                .build();

        return request;
    }

    private double determineStartOfBlocking(ScheduledTour scheduledTour) {
        double vehicleEarliestStart = scheduledTour.getVehicle().getEarliestStartTime();
        double ttDepot2FirstDelivery = ((Tour.Leg) scheduledTour.getTour().getTourElements().get(0)).getExpectedTransportTime();
        double firstDeliveryEarliestStart =((Tour.TourActivity) scheduledTour.getTour().getTourElements().get(1)).getTimeWindow().getStart();

        double tourStart = scheduledTour.getDeparture();
        double calculatedStart;
//        double bufferFactor = config.plansCalcRoute().getTeleportedModeFreespeedFactors().get(TransportMode.ride); //TODO
        double bufferFactor = 1.25;

        //if jsprit scheduled the tour start way too early, just account for the ttDepot2FirstDelivery * 1.5 and ignore the original start time
        if(tourStart + ttDepot2FirstDelivery * bufferFactor <= firstDeliveryEarliestStart){
            calculatedStart = firstDeliveryEarliestStart - ttDepot2FirstDelivery * bufferFactor - RETOOL_DURATION;
        } else {
            calculatedStart = tourStart - RETOOL_DURATION;
        }
        return Math.max(0., Math.max(vehicleEarliestStart, calculatedStart));
    }

    private List<Task> convertScheduledTour2DvrpTasks(ScheduledTour scheduledTour, double blockingStart, Network network) {
        List<Task> tourTasks = new ArrayList<>();
        double previousTaskEndTime = blockingStart + RETOOL_DURATION;
        tourTasks.add(new FreightRetoolTask(blockingStart, previousTaskEndTime, network.getLinks().get(scheduledTour.getTour().getStartLinkId())));

        List<Tour.TourElement> tourElements = scheduledTour.getTour().getTourElements();
        for (Tour.TourElement tourElement : tourElements) {
            if(tourElement instanceof Tour.Leg){
                NetworkRoute route = (NetworkRoute) ((Tour.Leg) tourElement).getRoute();
                VrpPathWithTravelData path;
                if (route.getStartLinkId().equals(route.getEndLinkId()))
                    path = VrpPaths.createZeroLengthPath(network.getLinks().get(route.getStartLinkId()), previousTaskEndTime);
                else {
                    path = createVrpPath(route, previousTaskEndTime, network);
                }
                tourTasks.add(new FreightDriveTask(path));
                previousTaskEndTime = path.getArrivalTime();
            } else {
                double currentTaskEndTime = previousTaskEndTime + ((Tour.TourActivity) tourElement).getDuration();
                if (tourElement instanceof Tour.ServiceActivity){
                    Tour.ServiceActivity serviceAct = (Tour.ServiceActivity) tourElement;
                    tourTasks.add(new FreightServiceTask(serviceAct, previousTaskEndTime, currentTaskEndTime,  network.getLinks().get(serviceAct.getLocation())));
                } else if(tourElement instanceof Tour.Pickup){
                    Tour.Pickup pickup = (Tour.Pickup) tourElement;
                    tourTasks.add(new FreightPickupTask(pickup, previousTaskEndTime, currentTaskEndTime, network.getLinks().get(pickup.getLocation())));
                } else if (tourElement instanceof Tour.Delivery){
                    throw new RuntimeException("no drt task type implemented yet for freight delivery activities..."); //TODO
                } else {
                    throw new RuntimeException();
                }
                previousTaskEndTime = currentTaskEndTime;
            }
        }
        tourTasks.add(new FreightRetoolTask(previousTaskEndTime, previousTaskEndTime + RETOOL_DURATION , network.getLinks().get(scheduledTour.getTour().getEndLinkId())));

        return tourTasks;
    }

    private VrpPathWithTravelDataImpl createVrpPath(NetworkRoute networkRoute, double departureTime, Network network) {
        int count = networkRoute.getLinkIds().size();

        Link[] links = new Link[count + 2];
        double[] linkTTs = new double[count + 2];
        links[0] = network.getLinks().get(networkRoute.getStartLinkId());
        double linkTT = 1.0D;
        linkTTs[0] = linkTT;
        double currentTime = departureTime + linkTT;

        for (int i = 0; i < count; ++i) {
            Link link = network.getLinks().get(networkRoute.getLinkIds().get(i));
            links[i + 1] = link;
            linkTT = link.getLength() / link.getFreespeed(currentTime);

//                    travelTime.getLinkTravelTime(link, currentTime, null, null);
            linkTTs[i + 1] = linkTT;
            currentTime += linkTT;
        }
        links[count + 1] = network.getLinks().get(networkRoute.getEndLinkId());
        Link lastLink = network.getLinks().get(networkRoute.getEndLinkId());
        linkTT = Math.floor(lastLink.getLength() / lastLink.getFreespeed(currentTime));
        linkTTs[count + 1] = linkTT;
//        double totalTT = 1.0D + networkRoute.getTravelTime() + linkTT;
        double totalTT = (currentTime + linkTT) - departureTime;
        return new VrpPathWithTravelDataImpl(departureTime, totalTT, links, linkTTs);
    }

    private Id<CarrierService> getFirstTourServiceOrShipmentForId(Tour tour) {

        Id<CarrierService> firstServiceOrShipmentId = null;
        for(Tour.TourElement e : tour.getTourElements()) {
            if(e instanceof Tour.ServiceActivity) {
                firstServiceOrShipmentId = ((Tour.ServiceActivity) e).getService().getId();
                break;
            } else if(e instanceof Tour.ShipmentBasedActivity) {
                firstServiceOrShipmentId = Id.create(((Tour.ShipmentBasedActivity) e).getShipment().getId(), CarrierService.class);
                break;
            }
        }

        return firstServiceOrShipmentId;
    }

    void writeAnalysis(String outputFilePath) throws IOException {
        BufferedWriter writer = IOUtils.getBufferedWriter(outputFilePath);
//        writer.write("vehicleId;requestId;task;taskIdx;actualStart;plannedStart;plannedEnd");
        writer.write("vehicleId;requestId;task;taskIdx;actualEnd;plannedStart;plannedEnd");
        for(RequestData data : this.finishedTours) {
//            for(TaskStartedEvent event : data.taskStartedEvents) {
            for(TaskEndedEvent event : data.taskEndedEvents) {

                writer.newLine();
                writer.write(data.vehId + ";" + data.request.getId()  + ";" + event.getTaskType() + ";" +
                        event.getTaskIndex() + ";" + event.getTime() + ";");

                writer.write(data.request.getTasks().get(0).getBeginTime() + ";" +
                        data.request.getTasks().get(0).getEndTime());

                data.request.getTasks().remove(0);
            }
        }
        writer.close();
    }


    private class RequestData {
        private Id<DvrpVehicle> vehId;
        private DrtBlockingRequest request;
        private List<TaskStartedEvent> taskStartedEvents;
        private List<TaskEndedEvent> taskEndedEvents;
        private DrtBlockingRequestScheduledEvent scheduledEvent;

        private RequestData(Id<DvrpVehicle> vehId) {
            this.vehId = vehId;
        }


    }
}