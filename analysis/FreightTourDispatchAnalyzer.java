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
import freight.tour.DispatchedPFAVTourData;
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
import org.matsim.contrib.taxi.schedule.TaxiTask;
import org.matsim.contrib.util.CSVLineBuilder;
import org.matsim.contrib.util.CompactCSVWriter;
import org.matsim.core.controler.events.IterationEndsEvent;
import org.matsim.core.controler.listener.IterationEndsListener;
import org.matsim.core.utils.io.IOUtils;
import privateAV.events.FreightTourCompletedEvent;
import privateAV.events.FreightTourRequestDeniedEvent;
import privateAV.events.FreightTourScheduledEvent;
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

    private Map<Id<DvrpVehicle>, DispatchedPFAVTourData> begunFreightTours = new HashMap<>();
    private Set<DispatchedPFAVTourData> completedFreightTours = new HashSet<>();

    private Fleet fleet;

    @Inject
    @Named(DvrpRoutingNetworkProvider.DVRP_ROUTING)
    private Network network;

    @Override
    public void handleEvent(final FreightTourScheduledEvent event) {
        if (this.begunFreightTours.get(event.getTourData().getVehicleId()) != null) {
            throw new IllegalStateException("a vehicle cannot perform two freight tours at the same time!");
        }
        this.begunFreightTours.put(event.getTourData().getVehicleId(), event.getTourData());
    }

    @Override
    public void handleEvent(FreightTourRequestDeniedEvent event) {

    }

    @Override
    public void handleEvent(FreightTourCompletedEvent event) {
        DispatchedPFAVTourData data = this.begunFreightTours.get(event.getVehicleId());
        if (data == null) {
            throw new RuntimeException("vehicle " + event.getVehicleId() + " completed a freight tour that has not begun?");
        }
        data.setActualTourDuration(event.getTime() - data.getDispatchTime());
        this.completedFreightTours.add(data);
        this.begunFreightTours.remove(event.getVehicleId());
    }

    @Override
    public void handleEvent(LinkEnterEvent event) {
        Link link = network.getLinks().get(event.getLinkId());
        if (this.begunFreightTours.keySet().contains(event.getVehicleId())) {

            DispatchedPFAVTourData data = this.begunFreightTours.get(event.getVehicleId());
            data.addToActualTourLength(link.getLength());

            TaxiTask t = (TaxiTask) (fleet.getVehicles().get(event.getVehicleId())).getSchedule().getCurrentTask();
            if (t.getTaxiTaskType().equals(TaxiTask.TaxiTaskType.EMPTY_DRIVE)) {
                data.addToActualEmptyMeters(link.getLength());
            }

            //TODO: also account for empty kilometers on the way back to owner!!!!
        }
    }

    // i use ActivityStartEvent instead of ActivityEndEvent, because i did get problems with the currentTask pointer of the schedule changing while
    // i casted here in this method.... see below...
    //for some reason, the current task in the vehicle's schedule is SOMETIMES already set to the next drive task, and SOMETIMES it is still pointing
    //to the PFAVServiceTask... - i don't really understand why.... due to parallelisation ??
    //tschlenther 14th of march '19
    @Override
    public void handleEvent(ActivityEndEvent event) {

        if (event.getActType().equals(PFAVActionCreator.SERVICE_ACTIVITY_TYPE)) {
            Id<DvrpVehicle> vehicleId = Id.create(event.getPersonId().toString(), DvrpVehicle.class);

            this.begunFreightTours.get(vehicleId).notifyNextServiceTaskPerformed();
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
        outDir += "/ITERS/it." + event.getIteration() + "/FreightTourStats_it" + event.getIteration() + ".csv";

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
                .add("MustReturnTime")
                .add("MustReturnLink")
                .add("RequestLink")
                .add("DepotLink")
                .add("DistanceToDepot")
                .add("DistanceBackToOwner")

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
        for (DispatchedPFAVTourData data : this.completedFreightTours) {
            CSVLineBuilder lineBuilder = new CSVLineBuilder()
                    .add(data.getVehicleId().toString())
                    .addf(dblFormat, data.getDispatchTime())
                    .addf(dblFormat, data.getMustReturnLog().getTime())
                    .addf(stringFormat, data.getMustReturnLog().getLinkId())
                    .addf(stringFormat, data.getRequestLink())
                    .addf(stringFormat, data.getDepotLink())
                    .addf(dblFormat, data.getDistanceToDepot())
                    .addf(dblFormat, data.getDistanceBackToOwner())

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
        for (DispatchedPFAVTourData data : this.begunFreightTours.values()) {
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
