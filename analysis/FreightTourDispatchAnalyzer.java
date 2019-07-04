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
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.events.ActivityEndEvent;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.LinkEnterEvent;
import org.matsim.api.core.v01.events.handler.ActivityEndEventHandler;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
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
import org.matsim.core.utils.collections.Tuple;
import org.matsim.core.utils.io.IOUtils;
import privateAV.FreightTourDataDispatched;
import privateAV.PFAVActionCreator;
import privateAV.events.FreightTourCompletedEvent;
import privateAV.events.FreightTourRequestRejectedEvent;
import privateAV.events.FreightTourScheduledEvent;

import java.util.*;

/**
 */
public class FreightTourDispatchAnalyzer implements FreightTourRequestEventHandler, QSimScopeObjectListener<Fleet>,
        LinkEnterEventHandler, ActivityStartEventHandler, ActivityEndEventHandler, IterationEndsListener {

    private Map<Id<DvrpVehicle>, FreightTourDataDispatched> begunFreightTours = new HashMap<>();
    private Set<FreightTourDataDispatched> completedFreightTours = new HashSet<>();
    private Map<Id<DvrpVehicle>, Double> waitTimesAtDepot = new HashMap<>();

    private Map<Tuple<Id<DvrpVehicle>,Double>,Double> freeTimesOfPFAVWhenRequestDenied = new HashMap<>();

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
    public void handleEvent(FreightTourRequestRejectedEvent event) {
        Id<DvrpVehicle> veh = event.getVehicleId();
        Double dispatchTime = event.getTime();
        Double mustReturnTime = event.getMustReturnTime();
        this.freeTimesOfPFAVWhenRequestDenied.put(new Tuple<>(veh,dispatchTime), mustReturnTime - dispatchTime);
    }

    @Override
    public void handleEvent(FreightTourCompletedEvent event) {
        FreightTourDataDispatched data = this.begunFreightTours.get(event.getVehicleId());
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

            FreightTourDataDispatched data = this.begunFreightTours.get(event.getVehicleId());
            data.addToActualTourLength(link.getLength());

            TaxiTask t = (TaxiTask) (fleet.getVehicles().get(event.getVehicleId())).getSchedule().getCurrentTask();
            if (t.getTaxiTaskType().equals(TaxiTask.TaxiTaskType.EMPTY_DRIVE)) {
                data.addToActualEmptyMeters(link.getLength());
            }
        }
    }

    @Override
    public void handleEvent(ActivityEndEvent event) {
        Id<DvrpVehicle> vehicleId = Id.create(event.getPersonId().toString(), DvrpVehicle.class);
        if (event.getActType().equals(PFAVActionCreator.STAY_ACTIVITY_TYPE) && this.begunFreightTours.containsKey(vehicleId)) {
            this.begunFreightTours.get(vehicleId).setWaitTimeAtDepot(event.getTime() - this.waitTimesAtDepot.remove(vehicleId));
        }
    }

    @Override
    public void handleEvent(ActivityStartEvent event) {
        Id<DvrpVehicle> vehicleId = Id.create(event.getPersonId().toString(), DvrpVehicle.class);
        if (event.getActType().equals(PFAVActionCreator.STAY_ACTIVITY_TYPE) && this.begunFreightTours.containsKey(vehicleId)) {
            this.waitTimesAtDepot.put(vehicleId, event.getTime());
        }

        if (event.getActType().contains(PFAVActionCreator.SERVICE_ACTIVITY_TYPE)) {

            this.begunFreightTours.get(vehicleId).notifyNextServiceTaskStarted(event.getTime());
        }
    }

    /**
     * Notifies all observers of the Controler that a iteration is finished
     *
     * @param event
     */
    @Override
    public void notifyIterationEnds(IterationEndsEvent event) {
        String dispatchStatsFile = event.getServices().getConfig().controler().getOutputDirectory();
        String deniedStatsFile = dispatchStatsFile + "/ITERS/it." + event.getIteration() + "/DeniedRequestsStats_it" + event.getIteration() + ".csv";
        dispatchStatsFile += "/ITERS/it." + event.getIteration() + "/FreightTourStats_it" + event.getIteration() + ".csv";

        String string = "%s";
        String dbl = "%.1f";

        writeDispatchStats(dispatchStatsFile, string, dbl);
        writeDeniedStats(deniedStatsFile, dbl);
    }

    @Override
    public void reset(int iteration) {
        this.begunFreightTours.clear();
        this.completedFreightTours.clear();
        this.freeTimesOfPFAVWhenRequestDenied.clear();
    }

    @Override
    public void objectCreated(Fleet fleet) {
        this.fleet = fleet;
    }

    private void writeDispatchStats(String file, String string, String dbl) {
        try (CompactCSVWriter writer = new CompactCSVWriter(IOUtils.getBufferedWriter(file), ';')) {
            writeHeader(writer);
            writeTourData(this.completedFreightTours, writer, string, dbl);
            writeTourData(this.begunFreightTours.values(), writer, string, dbl);
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
                .add("WaitTimeAtDepot")
                .add("DistanceBackToOwner")

                .add("PlannedTourDuration")
                .add("ActualTourDuration")

                .add("PlannedTourLength")
                .add("ActualTourLength")

                .add("PlannedEmptyMeters")
                .add("ActualEmptyMeters")

                .add("PlannedServicesCount")
                .add("HandledServicesCount")

                .add("PlannedTotalCapacityDemand")
                .add("ActualTotalCapacityDemand")

                .add("TotalServiceDelay");
        writer.writeNext(lineBuilder);
    }

    private void writeTourData(Collection<FreightTourDataDispatched> dataCollection, CompactCSVWriter writer, String stringFormat, String dblFormat) {
        for (FreightTourDataDispatched data : dataCollection) {
            CSVLineBuilder lineBuilder = new CSVLineBuilder()
                    .add(data.getVehicleId().toString())
                    .addf(dblFormat, data.getDispatchTime())
                    .addf(dblFormat, data.getMustReturnLog().getTime())
                    .addf(stringFormat, data.getMustReturnLog().getLinkId())
                    .addf(stringFormat, data.getRequestLink())
                    .addf(stringFormat, data.getDepotLinkId())
                    .addf(dblFormat, data.getDistanceToDepot())
                    .addf(dblFormat, data.getWaitTimeAtDepot())
                    .addf(dblFormat, data.getDistanceBackToOwner())

                    .addf(dblFormat, data.getPlannedTourDuration())
                    .addf(dblFormat, data.getActualTourDuration())

                    .addf(dblFormat, data.getPlannedTourLength())
                    .addf(dblFormat, data.getActualTourLength())

                    .addf(dblFormat, data.getPlannedEmptyMeters())
                    .addf(dblFormat, data.getActualEmptyMeters())

                    .addf("%d", data.getAmountOfServicesPlanned())
                    .addf("%d", data.getAmountOfServicesHandled())

                    .addf("%d", data.getPlannedTotalCapacityDemand())
                    .addf("%d", data.getActualServedCapacityDemand())

                    .addf(dblFormat, data.getTotalServiceDelay());

            writer.writeNext(lineBuilder);
        }
    }

    private void writeDeniedStats(String file, String dblFormat) {
        try (CompactCSVWriter writer = new CompactCSVWriter(IOUtils.getBufferedWriter(file), ';')) {

            writer.writeNext("vehID;requestTime;freeTime");

            for(Tuple<Id<DvrpVehicle>,Double> tuple : this.freeTimesOfPFAVWhenRequestDenied.keySet()){
                CSVLineBuilder lineBuilder = new CSVLineBuilder()
                        .add(tuple.getFirst().toString())
                        .addf(dblFormat, tuple.getSecond())
                        .addf(dblFormat, this.freeTimesOfPFAVWhenRequestDenied.get(tuple));
                writer.writeNext(lineBuilder);
            }

        }

    }


}
