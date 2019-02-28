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

import com.vividsolutions.jts.geom.Geometry;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.filter.NetworkFilterManager;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.opengis.feature.simple.SimpleFeature;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServiceMapMatch {

    private final String outputNewLinksNet = "C:/Users/Work/git/freightAV/input/FrachtNachfrage/KEP/MapMatch/newUsedLinks.xml.gz";
    private final String outputOldLinksNet = "C:/Users/Work/git/freightAV/input/FrachtNachfrage/KEP/MapMatch/oldUsedLinks.xml.gz";

    private final String inputNewNet = "C:/Users/Work/git/freightAV/input/Network/berlin-v5.2-1pct.output_network.xml";
    private final String inputOldNet = "C:/Users/Work/git/freightAV/input/FrachtNachfrage/KEP/PrivatkundenDirekt/network_Schroeder_slow.xml";

    private final String inputOldCarriers = "C:/Users/Work/git/freightAV/input/FrachtNachfrage/KEP/PrivatkundenDirekt/carriers_woSolution.xml.gz";
    private final String outputNewCarriers = "C:/Users/Work/git/freightAV/input/FrachtNachfrage/KEP/MapMatch/carriers_services_openBerlinNet_woVehicles.xml.gz";

    private final String shapeFile = "C:/Users/Work/git/freightAV/input/Shape/Senatverwaltung/Prognoseraum_EPSG_25833.shp";


    private Map<Geometry,Carrier> carrierMap = new HashMap();


    public static final void main(String[] args){
        new ServiceMapMatch().run();
    }


    public void run(){
        mapGeomToCarrier(shapeFile);

        //should be referenced in GK 4 after having a look at the net in via
        Network oldSchroederNet = NetworkUtils.createNetwork();
        //is referenced in GK4
        Network openBerlinNet = NetworkUtils.createNetwork();


        MatsimNetworkReader oldNetReader = new MatsimNetworkReader(oldSchroederNet);
        MatsimNetworkReader openBerlinNetReader = new MatsimNetworkReader(openBerlinNet);

        oldNetReader.readFile(inputOldNet);
        openBerlinNetReader.readFile(inputNewNet);

        Carriers oldCarriers = new Carriers();
        CarrierPlanXmlReaderV2 carrierReader = new CarrierPlanXmlReaderV2(oldCarriers);
        carrierReader.readFile(inputOldCarriers);

        Map<Id<Link>,Id<Link>> handledOldLinksToNewLink = new HashMap<>();
        Map<Id<Link>, Carrier> handledNewLinksToCarrier = new HashMap<>();

        NetworkFilterManager mng = new NetworkFilterManager(openBerlinNet);
        mng.addLinkFilter(l -> {
            if (l.getAllowedModes().contains("car")) return true;
            else return false;
        });
        Network newNetOnlyCar = mng.applyFilters();

        Carrier outOfGeomsCarrier = CarrierImpl.newInstance(Id.create("outOfGeomCarrier", Carrier.class));
        for(Carrier carrier: oldCarriers.getCarriers().values()){
            //we only have services in this carriers file, no shipments
            for(CarrierService service : carrier.getServices()){
                Id<Link> newLink =handledOldLinksToNewLink.get(service.getLocationLinkId());
                if(newLink == null) {
                    Coord oldLinkC = oldSchroederNet.getLinks().get(service.getLocationLinkId()).getCoord();
                    newLink = NetworkUtils.getNearestLink(newNetOnlyCar, oldLinkC).getId();
                    handledOldLinksToNewLink.put(service.getLocationLinkId(), newLink);

                    Carrier newCarrier = getResponsibleCarrier(oldLinkC);
                    if (newCarrier == null) newCarrier = outOfGeomsCarrier;
                    handledNewLinksToCarrier.put(newLink, newCarrier);
                }
                CarrierService newService =
                        CarrierService.Builder.newInstance(Id.create(service.getId(), CarrierService.class), newLink)
                        .setCapacityDemand(service.getCapacityDemand())
                        .setServiceDuration(service.getServiceDuration())
                        .setServiceStartTimeWindow(service.getServiceStartTimeWindow())
                        .build();
                handledNewLinksToCarrier.get(newLink).getServices().add(newService);
            }
        }

        Carriers newCarriers = new Carriers();
        newCarriers.addCarrier(outOfGeomsCarrier);
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

    private void mapGeomToCarrier(String shapeFile){
        Collection<SimpleFeature> allLORs = ShapeFileReader.getAllFeatures(shapeFile);

        for(SimpleFeature feature : allLORs){
            String key = (String) feature.getAttribute("SCHLUESSEL");
            String name = (String) feature.getAttribute("PRG_NAME");

            Carrier carrier = CarrierImpl.newInstance(Id.create(key + "_" + name, Carrier.class));

            Geometry geom = (Geometry) feature.getDefaultGeometry();
            this.carrierMap.put(geom,carrier);
        }
    }
}
