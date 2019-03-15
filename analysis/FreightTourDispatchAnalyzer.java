/* *********************************************************************** *
 * project: org.matsim.*
 * Controler.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
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

package analysis;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.collections.map.HashedMap;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.ActivityEndEvent;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.handler.ActivityEndEventHandler;
import org.matsim.api.core.v01.events.handler.LinkEnterEventHandler;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.fleet.DvrpVehicle;
import org.matsim.contrib.dvrp.fleet.Fleet;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.contrib.dvrp.run.QSimScopeObjectListener;
import org.matsim.contrib.dvrp.schedule.DriveTaskImpl;
import org.matsim.contrib.dvrp.schedule.Schedule;
import org.matsim.contrib.dvrp.schedule.Schedules;
import org.matsim.contrib.dvrp.schedule.Task;
import org.matsim.contrib.taxi.schedule.TaxiEmptyDriveTask;
import org.matsim.contrib.taxi.schedule.TaxiTask;
import org.matsim.contrib.util.CSVLineBuilder;
import org.matsim.contrib.util.CompactCSVWriter;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.utils.io.IOUtils;
import privateAV.events.FreightTourCompletedEvent;
import privateAV.events.FreightTourRequestDeniedEvent;
import privateAV.events.FreightTourScheduledEvent;
import privateAV.schedule.PFAVServiceTask;
import privateAV.vrpagent.PFAVActionCreator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * antizipierte Zeit der Frachttour (mit oder ohne Retool am Ende? schließlich kann vorher eine neue begonnen werden)
 * leerfahrten - dauer und distanz (freightTourDispatch -> activityStart retool und freightTourCompleted -> retool)
 * insgesamt distanz der frachttour (aufsummieren der Wege/LinkLängen)
 */
public class FreightTourDispatchAnalyzer implements FreightTourRequestEventHandler, QSimScopeObjectListener<Fleet>, LinkEnterEventHandler, ActivityEndEventHandler, IterationEndsListener {

    private Map<Id<DvrpVehicle>, FreightTourDispatchData> begunFreightTours = new HashMap<>();
    private Map<Id<DvrpVehicle>, Double> startTimes = new HashedMap();
    private Set<FreightTourDispatchData> completedFreightTours = new HashSet<>();

    private Fleet fleet;


    @Inject
    @Named(DvrpRoutingNetworkProvider.DVRP_ROUTING)
    private Network network;

    private static FreightTourDispatchData generateFreightTourDispatchData(FreightTourScheduledEvent event) {
        double emptyMeters = 0.;
        double distanceToDepot = 0;
        int plannedTotalCapacityDemand = 0;
        Id<Link> depotLink = null;
        boolean firstEmptyDrive = true;

        for (Task t : event.getFreightTour()) {
            if (t instanceof TaxiEmptyDriveTask) {
                DriveTaskImpl driveTask = (DriveTaskImpl) t;

                //do not count the first link since it is not really driven
                for (int z = 1; z < driveTask.getPath().getLinkCount(); z++) {
                    emptyMeters += driveTask.getPath().getLink(z).getLength();
                    if (firstEmptyDrive) {
                        distanceToDepot += driveTask.getPath().getLink(z).getLength();
                        depotLink = driveTask.getPath().getToLink().getId();
                    }
                }
                firstEmptyDrive = false;
            } else if (t instanceof PFAVServiceTask) {
                plannedTotalCapacityDemand += ((PFAVServiceTask) t).getCarrierService().getCapacityDemand();
            }
        }

        return FreightTourDispatchData.newBuilder().
                vehicleId(event.getVehicleId())
                .depotLink(depotLink)
                .requestLink(event.getRequestLink())
                .dispatchTime(event.getTime())
                .plannedTourDuration(event.getFreightTourDuration())
                .plannedTourLength(event.getFreightTourDistance())
                .plannedEmptyMeters(emptyMeters)
                .distanceToDepot(distanceToDepot)
                .plannedTotalCapacityDemand(plannedTotalCapacityDemand)
                .build();
    }

    @Override
    public void handleEvent(final FreightTourScheduledEvent event) {
        if (this.begunFreightTours.get(event.getVehicleId()) != null) {
            throw new IllegalStateException("a vehicle cannot perform two freight tours at the same time!");
        }
        this.begunFreightTours.put(event.getVehicleId(), generateFreightTourDispatchData(event));
        this.startTimes.put(event.getVehicleId(), event.getTime());
    }

    @Override
    public void handleEvent(FreightTourRequestDeniedEvent event) {

    }

    @Override
    public void handleEvent(FreightTourCompletedEvent event) {
        FreightTourDispatchData data = this.begunFreightTours.get(event.getVehicleId());
        if (data == null) {
            throw new RuntimeException("vehicle " + event.getVehicleId() + " completed a freight tour that has not begun?");
        }
        data.setActualTourDuration(event.getTime() - this.startTimes.get(event.getVehicleId()));
        this.completedFreightTours.add(data);
        this.begunFreightTours.remove(event.getVehicleId());
        this.startTimes.remove(event.getVehicleId());
    }

    @Override
    public void handleEvent(LinkEnterEvent event) {
        Link link = network.getLinks().get(event.getLinkId());
        if (this.begunFreightTours.keySet().contains(event.getVehicleId())) {

            FreightTourDispatchData data = this.begunFreightTours.get(event.getVehicleId());
            data.addToActualTourLength(link.getLength());

            TaxiTask t = (TaxiTask) (fleet.getVehicles().get(event.getVehicleId())).getSchedule().getCurrentTask();
            if (t.getTaxiTaskType().equals(TaxiTask.TaxiTaskType.EMPTY_DRIVE)) {
                data.addToActualEmptyMeters(link.getLength());
            }
        }
    }

    @Override
    public void handleEvent(ActivityEndEvent event) {
        if (event.getActType().equals(PFAVActionCreator.SERVICE_ACTIVITY_TYPE)) {
            Id<DvrpVehicle> vehicleId = Id.create(event.getPersonId().toString(), DvrpVehicle.class);

            //for some reason, the current task in the vehicle's schedule is SOMETIMES already set to the next drive task, and SOMETIMES it is still pointing
            //to the PFAVServiceTask... - i don't really understand why.... due to parallelisation ??
            //tschlenther 14th of march '19
            Schedule schedule = this.fleet.getVehicles().get(vehicleId).getSchedule();
            if (schedule.getCurrentTask() instanceof PFAVServiceTask) {
                this.begunFreightTours.get(vehicleId)
                        .addToActualServedCapacityDemand(((PFAVServiceTask) schedule.getCurrentTask()).getCarrierService().getCapacityDemand());
            } else if (Schedules.getPreviousTask(schedule) instanceof PFAVServiceTask) {
                this.begunFreightTours.get(vehicleId)
                        .addToActualServedCapacityDemand(((PFAVServiceTask) (Schedules.getPreviousTask(schedule))).getCarrierService().getCapacityDemand());
            } else {
                throw new IllegalStateException("activityEndEvent does not fit to schedule status..");
            }
        }
    }

    /**
     * Notifies all observers of the Controler that a iteration is finished
     *
     * @param event
     */
    @Override
    public void notifyIterationEnds(IterationEndsEvent event) {
        String outDir = event.getServices().getConfig().controler().getOutputDirectory();
        outDir += "ITERS/it." + event.getIteration() + "/FreightTourStats_it" + event.getIteration() + ".csv";

        writeStats(outDir);
    }

    @Override
    public void reset(int iteration) {
        this.begunFreightTours.clear();
        this.completedFreightTours.clear();
    }

    @Override
    public void objectCreated(Fleet fleet) {
        this.fleet = fleet;
    }

    private void writeStats(String file) {
        try (CompactCSVWriter writer = new CompactCSVWriter(IOUtils.getBufferedWriter(file), ';')) {
            writeHeader(writer);

            String string = "%s";
            String dbl = "%.1f";

            writeCompletedToursData(writer, string, dbl);
            writeBegunToursData(writer, string, dbl);
        }

    }

    private void writeHeader(CompactCSVWriter writer) {
        CSVLineBuilder lineBuilder = new CSVLineBuilder()
                .add("VehicleID")
                .add("DispatchTime")
                .add("RequestLink")
                .add("DepotLink")
                .add("DistanceToDepot")

                .add("PlannedTourDuration")
                .add("ActualTourDuration")

                .add("PlannedTourLength")
                .add("ActualTourLength")

                .add("PlannedEmptyMeters")
                .add("ActualEmptyMeters")

                .add("PlannedTotalCapacityDemand")
                .add("ActualTotalCapacityDemand");
        writer.writeNext(lineBuilder);
    }

    private void writeCompletedToursData(CompactCSVWriter writer, String stringFormat, String dblFormat) {
        for (FreightTourDispatchData data : this.completedFreightTours) {
            CSVLineBuilder lineBuilder = new CSVLineBuilder()
                    .add(data.getVehicleId().toString())
                    .addf(dblFormat, data.getDispatchTime())
                    .addf(stringFormat, data.getRequestLink())
                    .addf(stringFormat, data.getDepotLink())
                    .addf(dblFormat, data.getDistanceToDepot())

                    .addf(dblFormat, data.getPlannedTourDuration())
                    .addf(dblFormat, data.getActualTourDuration())

                    .addf(dblFormat, data.getPlannedTourLength())
                    .addf(dblFormat, data.getActualTourLength())

                    .addf(dblFormat, data.getPlannedEmptyMeters())
                    .addf(dblFormat, data.getActualEmptyMeters())

                    .addf("%d", data.getPlannedTotalCapacityDemand())
                    .addf("%d", data.getActualServedCapacityDemand());

            writer.writeNext(lineBuilder);
        }
        writer.writeNextEmpty();
    }

    private void writeBegunToursData(CompactCSVWriter writer, String stringFormat, String dblFormat) {
        for (FreightTourDispatchData data : this.begunFreightTours.values()) {
            CSVLineBuilder lineBuilder = new CSVLineBuilder()
                    .add(data.getVehicleId().toString())
                    .addf(dblFormat, data.getDispatchTime())
                    .addf(stringFormat, data.getRequestLink())
                    .addf(stringFormat, data.getDepotLink())
                    .addf(dblFormat, data.getDistanceToDepot())

                    .addf(dblFormat, data.getPlannedTourDuration())
                    .add("-")

                    .addf(dblFormat, data.getPlannedTourLength())
                    .add("-")

                    .addf(dblFormat, data.getPlannedEmptyMeters())
                    .add("-")

                    .addf("%d", data.getPlannedTotalCapacityDemand())
                    .add("-");

            writer.writeNext(lineBuilder);
        }
        writer.writeNextEmpty();
    }
}
