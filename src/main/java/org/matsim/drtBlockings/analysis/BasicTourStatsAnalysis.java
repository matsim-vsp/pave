package org.matsim.drtBlockings.analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.drt.schedule.DrtDriveTask;
import org.matsim.contrib.drt.schedule.DrtStayTask;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.optimizer.Request;
import org.matsim.contrib.dvrp.vrpagent.TaskEndedEvent;
import org.matsim.contrib.dvrp.vrpagent.TaskEndedEventHandler;
import org.matsim.contrib.dvrp.vrpagent.TaskStartedEvent;
import org.matsim.contrib.dvrp.vrpagent.TaskStartedEventHandler;
import org.matsim.contrib.freight.carrier.CarrierService;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.drtBlockings.events.*;
import org.matsim.drtBlockings.tasks.FreightDriveTask;
import org.matsim.drtBlockings.tasks.FreightRetoolTask;
import org.matsim.drtBlockings.tasks.FreightServiceTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicTourStatsAnalysis implements DrtBlockingRequestScheduledEventHandler, TaskStartedEventHandler,
        TaskEndedEventHandler, DrtBlockingEndedEventHandler, LinkEnterEventHandler, IterationEndsListener {

    private Network network;
    private Map<Id<DvrpVehicle>, DrtBlockingTourData> currentTours = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToDistance = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToDeparture = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToArrival = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToAccessDistance = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> vehToAccessDuration = new HashMap<>();
    private Map<Id<DvrpVehicle>, Id<Request>> vehToRequest = new HashMap<>();
    private Map<Id<DvrpVehicle>, Integer> vehToTaskNo = new HashMap<>();
    private Map<Id<DvrpVehicle>, Integer> vehToServiceNo = new HashMap<>();
    private Map<Id<DvrpVehicle>, List<Id<CarrierService>>> vehToServices = new HashMap<>();
    private Map<Id<DvrpVehicle>, List<TaskData>> vehToTasks = new HashMap<>();

    private List<DrtBlockingTourData> finishedTours = new ArrayList<>();

    public int serviceCount = 0;
    public int scheduleCount = 0;

    public BasicTourStatsAnalysis(Network network) { this.network = network; }

    public static void main(String[] args) {
//        String dir = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/berlin-v5.5-1pct/drtBlockingTest_30Blockings_realisticServiceTimeWindows/";
        String dir = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/Analysis/TaskComp/";
        String eventsFile = dir + "P2.2.2_p2-23DRTBlockingPolicyCase.2.events.xml.gz";
        String inputNetwork = dir + "p2-23.output_network.xml.gz";
        String outputFile = dir + "BasicTourStats.csv";

        EventsManager manager = EventsUtils.createEventsManager();
        Network network = NetworkUtils.createNetwork();
        new MatsimNetworkReader(network).readFile(inputNetwork);

            BasicTourStatsAnalysis handler = new BasicTourStatsAnalysis(network);
        manager.addHandler(handler);
        manager.initProcessing();
        MatsimEventsReader reader = DrtBlockingEventsReader.create(manager);
        reader.readFile(eventsFile);
        manager.finishProcessing();
//        handler.writeStats(outputFile);
        handler.writeStatsTaskWise(outputFile);
        System.out.println("Writing of DrtBlocking TourStats to " + outputFile + " was successful!");
    }

    public void writeStatsTaskWise(String file) {

        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING TOUR STATS FOR POLICY CASE!");
            int i =1;
            writer.write("no;vehId;totalDistance [m];accessLegDistance [m];departureTime [s];arrivalTime [s];" +
                    "tourDuration [s];accessLegDuration [s];requestId;numberOfServices;numberOfTasks;" +
                    "task;taskIdx;beginTime [s];endTime[s]");

            for (DrtBlockingTourData data  : this.finishedTours) {
                for(TaskData taskData : data.tasks) {
                    if(taskData.type.equals(FreightDriveTask.FREIGHT_DRIVE_TASK_TYPE.name())
                            || taskData.type.equals(FreightServiceTask.FREIGHT_SERVICE_TASK_TYPE.name())) {

                        writer.newLine();
                        writer.write(i + ";" + data.veh + ";" + data.tourDistance + ";" + data.accessDistance + ";"
                                + data.departure + ";" + data.arrival + ";" + data.tourDuration + ";" + data.accessDuration + ";"
                                + data.requestId + ";" + data.serviceNo + ";" + data.taskNo + ";" + taskData.type
                                + ";" + taskData.index + ";" + taskData.start + ";" + taskData.end);
                    }
                }
                i++;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeStats(String file) {

        System.out.println("NUMBER OF REGISTRATED SERVICES: " + this.serviceCount);
        System.out.println("NUMBER OF REGISTRATED SCHEDULINGS: " + this.scheduleCount);

        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING TOUR STATS FOR POLICY CASE!");
            int i =1;
            writer.write("no;vehId;totalDistance [m];accessLegDistance [m];departureTime [s];arrivalTime [s];tourDuration [s];accessLegDuration [s];requestId;numberOfServices;numberOfTasks");
            writer.newLine();

            for (DrtBlockingTourData data  : this.finishedTours) {
                writer.write(i + ";" + data.veh + ";" + data.tourDistance + ";" + data.accessDistance + ";"
                + data.departure + ";" + data.arrival + ";" + data.tourDuration + ";" + data.accessDuration + ";"
                        + data.requestId + ";" + data.serviceNo + ";" + data.taskNo);
                writer.newLine();
                i++;
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void handleEvent(LinkEnterEvent event) {
        //check if veh has a running tour

        //could be that the vehicle will not be recognized because its VehicleId and not vId
        //maybe its necessary to ad d .toString after getVehicleId() but should work like this too
        //maybe like that
        Id<DvrpVehicle> dvrpVehicleId = Id.create(event.getVehicleId(), DvrpVehicle.class);
        if (this.currentTours.containsKey(dvrpVehicleId)) {
            //add up linkLength to distance travelled so far
            Double distanceSoFar = this.vehToDistance.computeIfAbsent(dvrpVehicleId, v -> 0.);
            this.vehToDeparture.putIfAbsent(dvrpVehicleId, event.getTime());
            this.vehToDistance.replace(dvrpVehicleId,
                        distanceSoFar + network.getLinks().get(event.getLinkId()).getLength());
        }
    }

    @Override
    public void handleEvent(TaskEndedEvent event) {

        Id<DvrpVehicle> dvrpVehicleId = event.getDvrpVehicleId();
        if(this.currentTours.containsKey(dvrpVehicleId)) {
            //STAY tasks need to be excluded because they usually begin before the tour can be registered, triggers logical error
            if(!event.getTaskType().name().equals("STAY")) {

                if (!this.vehToTaskNo.containsKey(dvrpVehicleId)) {
                    System.out.println("ERROR: Task of type " + event.getTaskType().name() + " ending at " + event.getTime() + " did not have a TaskStartEvent");
                } else {
                    for(TaskData taskData : this.vehToTasks.get(dvrpVehicleId)) {
                        if(event.getTaskIndex()==taskData.index && event.getTaskType().name()==taskData.type && event.getTime() >= taskData.start) {
                            taskData.end = event.getTime();
                            break;
                        }
                    }
                }
            }
        }


    }

    @Override
    public void handleEvent(TaskStartedEvent event) {
        //here we register the access leg + count vehicle's tasks
        //check if veh has a running tour
        Id<DvrpVehicle> dvrpVehicleId = event.getDvrpVehicleId();
        if (this.currentTours.containsKey(dvrpVehicleId)) {

            TaskData taskData = new TaskData(event.getTime(), event.getTaskIndex(), event.getTaskType().name());

            if (!this.vehToTaskNo.containsKey(dvrpVehicleId)) {
                this.vehToTaskNo.put(dvrpVehicleId, 1);
                List<TaskData> taskDataList = new ArrayList<>();
                taskDataList.add(taskData);
                this.vehToTasks.put(dvrpVehicleId, taskDataList);
            } else {
                this.vehToTaskNo.replace(dvrpVehicleId, this.vehToTaskNo.get(dvrpVehicleId),
                        this.vehToTaskNo.get(dvrpVehicleId) + 1);
                this.vehToTasks.get(dvrpVehicleId).add(taskData);
            }

            if (event.getTaskType()==FreightRetoolTask.RETOOL_TASK_TYPE) {

//                System.out.println(event.getTime() + " " + event.getTaskType() + " " + event.getLinkId());

                if(this.vehToDistance.containsKey(dvrpVehicleId)) {
                    this.vehToAccessDistance.putIfAbsent(dvrpVehicleId, this.vehToDistance.get(dvrpVehicleId));
                } else {
                    this.vehToAccessDistance.put(dvrpVehicleId, 0.);
                }


                //Before computing the accessDuration we need to check if the vehicle drove at all = had an LinkEnterEvent
                if(!this.vehToDeparture.containsKey(dvrpVehicleId)) {
                    this.vehToDeparture.put(dvrpVehicleId, event.getTime());
                }
                Double accessDuration = event.getTime() - this.vehToDeparture.get(dvrpVehicleId);
                if (accessDuration >= 0) {
                    this.vehToAccessDuration.putIfAbsent(dvrpVehicleId, accessDuration);
                } else {
                    System.out.println("Access leg duration for vehicle " + dvrpVehicleId + " is " + accessDuration + " (< 0!");
                }
            } else if(event.getTaskType()== FreightServiceTask.FREIGHT_SERVICE_TASK_TYPE) {

                List<Id<CarrierService>> services = new ArrayList<>();
                if(!this.vehToServiceNo.containsKey(dvrpVehicleId)) {
                    this.vehToServiceNo.put(dvrpVehicleId, 1);
                } else {
                    this.vehToServiceNo.replace(dvrpVehicleId, this.vehToServiceNo.get(dvrpVehicleId) + 1);
                }
                serviceCount = serviceCount +1;

            } else if(event.getTaskType()== DrtDriveTask.TYPE) {

            } else if (event.getTaskType()==FreightDriveTask.FREIGHT_DRIVE_TASK_TYPE) {
//                System.out.println(event.getTime() + " " + event.getTaskType() + " " + event.getLinkId());
            }
            else {
//                System.out.println(event.getTaskType());
            }
        }
    }

    @Override
    public void handleEvent(DrtBlockingEndedEvent event) {
        if (this.currentTours.containsKey(event.getVehicleId())) {

            //it seems like there are tours without legs (=all services are located on depot link)
            //in this case the vehicle does not have any linkEnterEvents, so its not registrated into vehToDistance
            //therefore we need to check if its null here
            Double distanceSoFar;
            if(this.vehToDistance.containsKey(event.getVehicleId())) {
                distanceSoFar = this.vehToDistance.remove(event.getVehicleId());
            } else {
                distanceSoFar = 0.;
                System.out.println("INFO: vehicle " + event.getVehicleId() + " did not enter any link! " +
                        "Therefore the distance for request " + this.vehToRequest.get(event.getVehicleId()) + " is set to 0!" );
            }

            DrtBlockingTourData data = this.currentTours.remove(event.getVehicleId());

            //get eventTime and calculate tourDuration
            //The following should be the case for every tour!
            if (event.getTime() > this.vehToDeparture.get(event.getVehicleId())) {
                this.vehToArrival.put(event.getVehicleId(), event.getTime());
                Double tourDuration = event.getTime() - vehToDeparture.get(event.getVehicleId());
                data.tourDuration = tourDuration;
                data.tourDistance = distanceSoFar + network.getLinks().get(event.getLinkId()).getLength();
                data.accessDistance = this.vehToAccessDistance.remove(event.getVehicleId());
                data.accessDuration = this.vehToAccessDuration.remove(event.getVehicleId());
                data.departure = this.vehToDeparture.remove(event.getVehicleId());
                data.arrival = this.vehToArrival.remove(event.getVehicleId());
                Id<Request> requestId = this.vehToRequest.remove(event.getVehicleId());
                data.requestId = requestId;
                data.taskNo = this.vehToTaskNo.remove(event.getVehicleId());
                data.tasks = this.vehToTasks.remove(event.getVehicleId());
                if(this.vehToServiceNo.containsKey(event.getVehicleId())) {
                    data.serviceNo = this.vehToServiceNo.remove(event.getVehicleId());
                } else {
                    System.out.println("WARN: Tour with requestId " + requestId + " seems to have no services at all, please check carriers file!");
                    data.serviceNo = 0;
                }

            } else {
                System.out.println("The tours of vehicle " + event.getVehicleId() + " are not correctly handled!");
            }
            //remove  veh from currentTours and out it onto finishedTours
            this.finishedTours.add(data);
        }
    }

    @Override
    public void handleEvent(DrtBlockingRequestScheduledEvent event) {
        //put veh into map of current tours when Request is requested
        DrtBlockingTourData data = new DrtBlockingTourData(event.getVehicleId(), 0.);
        this.currentTours.put(event.getVehicleId(), data);
        this.vehToRequest.put(event.getVehicleId(), event.getRequestId());

        scheduleCount = scheduleCount + 1;
    }

    @Override
    public void notifyIterationEnds(IterationEndsEvent event) {
        writeStats(event.getServices().getControlerIO().getIterationFilename(event.getIteration(), "BasicTourStats.csv"));
    }

    @Override
    public void reset(int iteration) {
        this.currentTours.clear();
        this.vehToDistance.clear();
        this.vehToDeparture.clear();
        this.vehToArrival.clear();
        this.vehToAccessDistance.clear();
        this.vehToAccessDuration.clear();
        this.vehToRequest.clear();
        this.vehToTaskNo.clear();
        this.vehToServiceNo.clear();
        this.vehToServices.clear();
        this.serviceCount = 0;
        this.scheduleCount = 0;
        this.vehToTasks.clear();

        this.finishedTours.clear();
    }

    private class DrtBlockingTourData {
        private final Id<DvrpVehicle> veh;
        private double departure;
        private double arrival;
        private double tourDuration;
        private double accessDuration;
        private double tourDistance = 0.;
        private double accessDistance = 0.;
        private Id<Request> requestId;
        private int taskNo;
        private int serviceNo;
        private List<TaskData> tasks;

        private DrtBlockingTourData(Id<DvrpVehicle> veh, double departure) {
            this.veh = veh;
            this.departure = departure;
        }
    }

    private class TaskData {
        private double start;
        private double end;
        private int index;
        private String type;

        private TaskData(double start, int index, String type) {
            this.start = start;
            this.index = index;
            this.type = type;
        }
    }
}
