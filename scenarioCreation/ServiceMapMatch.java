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

package scenarioCreation;

import org.locationtech.jts.geom.Geometry;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.contrib.util.CSVLineBuilder;
import org.matsim.contrib.util.CompactCSVWriter;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.filter.NetworkFilterManager;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.run.RunBerlinScenario;
import org.matsim.vehicles.VehicleType;
import org.opengis.feature.simple.SimpleFeature;
import privateAV.PFAVUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServiceMapMatch {
    //    private final String inputNewNet = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/Network/berlin-v5.2-1pct.output_network.xml";
    private static final String CONFIG_v53_1pct = "input/BerlinScenario/5.3/berlin-v5.3-1pct.config.xml";
    private final String outputNewLinksNet = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/MapMatch/newUsedLinks.xml.gz";
    private final String outputOldLinksNet = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/MapMatch/oldUsedLinks.xml.gz";
    private final String inputOldNet = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/PrivatkundenDirekt/network_Schroeder_slow.xml";

    private final String inputOldCarriers = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/PrivatkundenDirekt/carriers_woSolution.xml.gz";
    private static final String outputCSVFile = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/MapMatch/carriers_services_openBerlinNet_withInfiniteTrucks_TWsfitted_ServiceStats.csv";

    private final String shapeFile = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/BerlinScenario/Depots/PFAV_CarrierAreas.shp";
    //    private final String outputNewCarriers = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/MapMatch/carriers_services_openBerlinNet_withInfiniteTrucks.xml.gz";
    private final String outputNewCarriers = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/MapMatch/carriers_services_openBerlinNet_withInfiniteTrucks_TWsfitted_Truck.xml.gz";

    private Map<Geometry,Carrier> carrierMap = new HashMap();

    public static void main(String[] args) {
//        new ServiceMapMatch().run();
        new ServiceMapMatch().writeServiceStatsCSV(outputCSVFile);
    }


    public void writeServiceStatsCSV(String file) {
        try (CompactCSVWriter writer = new CompactCSVWriter(IOUtils.getBufferedWriter(file), ';')) {
            writeHeader(writer);
            String string = "%s";
            String dbl = "%.1f";

            Carriers oldCarriers = new Carriers();
            CarrierPlanXmlReaderV2 carrierReader = new CarrierPlanXmlReaderV2(oldCarriers);
            carrierReader.readFile(outputNewCarriers);

            int totalCapacityDemand = 0;
            int totalAmountOfServices = 0;
            for (Carrier carrier : oldCarriers.getCarriers().values()) {
                if (carrier.getCarrierCapabilities().getCarrierVehicles().size() > 1) {
                    throw new RuntimeException("why does carrier " + carrier.getId() + " have more than one vehicle ?");
                }
                String depot = "";
                for (CarrierVehicle v : carrier.getCarrierCapabilities().getCarrierVehicles()) {
                    depot += v.getLocation();
                }
                for (CarrierService service : carrier.getServices()) {
                    totalCapacityDemand += service.getCapacityDemand();
                    totalAmountOfServices++;
                    writeServiceData(writer, service, string, dbl, depot);
                }
            }
            System.out.println("totalCapacityDemand=" + totalCapacityDemand);
            System.out.println("totalAmountOfServices=" + totalAmountOfServices);
        }
    }

    private void writeHeader(CompactCSVWriter writer) {
        CSVLineBuilder lineBuilder = new CSVLineBuilder()
                .add("ServiceID")
                .add("EarliestStart")
                .add("LatestStart")
                .add("CapacityDemand")
                .add("Duration")
                .add("Type")
                .add("LocationId")
                .add("Depot");
        writer.writeNext(lineBuilder);
    }

    private void writeServiceData(CompactCSVWriter writer, CarrierService service, String stringFormat, String dblFormat, String depot) {
        CSVLineBuilder lineBuilder = new CSVLineBuilder()
                .add("" + service.getId())
                .addf(dblFormat, service.getServiceStartTimeWindow().getStart())
                .addf(dblFormat, service.getServiceStartTimeWindow().getEnd())
                .addf("%d", service.getCapacityDemand())
                .addf(dblFormat, service.getServiceDuration())
                .addf(stringFormat, service.getType())
                .addf(stringFormat, "" + service.getLocationLinkId())
                .addf(stringFormat, depot);
        writer.writeNext(lineBuilder);
    }

    public void run(){
        mapGeomToCarrier(shapeFile);

        //should be referenced in GK 4 after having a look at the net in via
        Network oldSchroederNet = NetworkUtils.createNetwork();
        //is referenced in GK4
        Network openBerlinNet = new RunBerlinScenario(CONFIG_v53_1pct).prepareScenario().getNetwork();

        MatsimNetworkReader oldNetReader = new MatsimNetworkReader(oldSchroederNet);
//        MatsimNetworkReader openBerlinNetReader = new MatsimNetworkReader(openBerlinNet);

        oldNetReader.readFile(inputOldNet);
//        openBerlinNetReader.readFile(inputNewNet);

        Carriers oldCarriers = new Carriers();
        CarrierPlanXmlReaderV2 carrierReader = new CarrierPlanXmlReaderV2(oldCarriers);
        carrierReader.readFile(inputOldCarriers);

        Map<Id<Link>,Id<Link>> handledOldLinksToNewLink = new HashMap<>();
        Map<Id<Link>, Carrier> handledNewLinksToCarrier = new HashMap<>();

        NetworkFilterManager mng = new NetworkFilterManager(openBerlinNet);
        mng.addLinkFilter(l -> {
            return l.getAllowedModes().contains("car");
        });
        Network newNetOnlyCar = mng.applyFilters();

        for(Carrier carrier: oldCarriers.getCarriers().values()){
            //we only have services in this carriers file, no shipments
            for(CarrierService service : carrier.getServices()){
                Id<Link> newLink =handledOldLinksToNewLink.get(service.getLocationLinkId());
                Coord oldLinkC = oldSchroederNet.getLinks().get(service.getLocationLinkId()).getCoord();
                if(newLink == null) {
                    newLink = NetworkUtils.getNearestLink(newNetOnlyCar, oldLinkC).getId();
                    handledOldLinksToNewLink.put(service.getLocationLinkId(), newLink);
                }
                Carrier newCarrier = getResponsibleCarrier(oldLinkC);
                if (newCarrier != null) {
                    CarrierService newService =
                            CarrierService.Builder.newInstance(Id.create(service.getId(), CarrierService.class), newLink)
                                    .setCapacityDemand(service.getCapacityDemand())
                                    .setServiceDuration(service.getServiceDuration())
                                    .setServiceStartTimeWindow(getFittedTimeWindow(service.getServiceStartTimeWindow()))
                                    .build();
                    newCarrier.getServices().add(newService);
                    handledNewLinksToCarrier.put(newLink, newCarrier);
                }
            }
        }

        Carriers newCarriers = new Carriers();
//        newCarriers.addCarrier(outOfGeomsCarrier);
        for(Carrier c : this.carrierMap.values()){
            newCarriers.addCarrier(c);
        }
        new CarrierPlanXmlWriterV2(newCarriers).write(outputNewCarriers);


        NetworkFilterManager oldNetFMng = new NetworkFilterManager(oldSchroederNet);
        oldNetFMng.addLinkFilter(l -> handledOldLinksToNewLink.keySet().contains(l.getId())  );

        new NetworkWriter(oldNetFMng.applyFilters()).writeV2(outputOldLinksNet);

        NetworkFilterManager newNetFMng = new NetworkFilterManager(openBerlinNet);
        newNetFMng.addLinkFilter(l -> handledNewLinksToCarrier.keySet().contains(l.getId())  );

        new NetworkWriter(newNetFMng.applyFilters()).writeV2(outputNewLinksNet);
    }

    private Carrier getResponsibleCarrier(Coord c){
        CoordinateTransformation ct = TransformationFactory.getCoordinateTransformation(TransformationFactory.DHDN_GK4, "EPSG:25833");
        for(Geometry geom : this.carrierMap.keySet()){
            if(geom.contains(MGC.coord2Point(ct.transform(c)))) return this.carrierMap.get(geom);
        }
        return null;
    }

    private TimeWindow getFittedTimeWindow(TimeWindow oldTimeWindow) {
        double start = oldTimeWindow.getStart();
        double end = oldTimeWindow.getEnd();

        double oldDuration = end - start;
        if (start < PFAVUtils.FREIGHTTOUR_EARLIEST_START) {
            start = PFAVUtils.FREIGHTTOUR_EARLIEST_START;
            if ((start + oldDuration) > PFAVUtils.FREIGHTTOUR_LATEST_START) {
                end = PFAVUtils.FREIGHTTOUR_LATEST_START;
            } else {
                end = start + oldDuration;
            }
        } else if (end > PFAVUtils.FREIGHTTOUR_LATEST_START) {
            end = PFAVUtils.FREIGHTTOUR_LATEST_START;
            if ((end - oldDuration) < PFAVUtils.FREIGHTTOUR_EARLIEST_START) {
                start = PFAVUtils.FREIGHTTOUR_EARLIEST_START;
            } else {
                start = end - oldDuration;
            }
        }
        return TimeWindow.newInstance(start, end);
    }

    private void mapGeomToCarrier(String shapeFile){
        Collection<SimpleFeature> allLORs = ShapeFileReader.getAllFeatures(shapeFile);

        for(SimpleFeature feature : allLORs){
            String key = (String) feature.getAttribute("SCHLUESSEL");
            String name = (String) feature.getAttribute("PRG_NAME");
            Long depotLink = (Long) feature.getAttribute("DEPOT_LINK");

            Carrier carrier = CarrierImpl.newInstance(Id.create(key + "_" + name, Carrier.class));


            Id<VehicleType> typeId = Id.create("Truck", VehicleType.class);

            CarrierVehicle veh = CarrierVehicle.Builder.newInstance(Id.createVehicleId(key + "_" + name + "_v"),
                    Id.createLinkId(depotLink))
                    .setEarliestStart(PFAVUtils.FREIGHTTOUR_EARLIEST_START).
                            setLatestEnd(PFAVUtils.FREIGHTTOUR_LATEST_START)
                    .setTypeId(typeId)
                    .setType(CarrierVehicleType.Builder.newInstance(typeId).build())
                    .build();
            carrier.getCarrierCapabilities().getCarrierVehicles().add(veh);
            carrier.getCarrierCapabilities().setFleetSize(CarrierCapabilities.FleetSize.INFINITE);

            Geometry geom = (Geometry) feature.getDefaultGeometry();
            this.carrierMap.put(geom,carrier);
        }
    }

}
