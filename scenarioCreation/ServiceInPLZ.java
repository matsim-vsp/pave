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
import org.locationtech.jts.geom.Point;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlReader;
import org.matsim.contrib.freight.carrier.CarrierService;
import org.matsim.contrib.freight.carrier.Carriers;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.geotools.MGC;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.matsim.core.utils.io.IOUtils;
import org.opengis.feature.simple.SimpleFeature;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServiceInPLZ {

    private final Carriers carriers;
    private final String pathToPLZFile;
    private HashMap<Long, SimpleFeature> plzFeatures = new HashMap<>();
    private Network network;

    ServiceInPLZ(Network network, Carriers carriers, String pathToPLZShapeFile) {
        this.network = network;
        this.carriers = carriers;
        this.pathToPLZFile = pathToPLZShapeFile;
    }

    public static void main(String[] args) {
        String carriersFile = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/PrivatkundenDirekt/carriers_woSolution.xml.gz";
        String netFile = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/PrivatkundenDirekt/network_Schroeder_slow.xml";
        String shapeFile = "C:/Users/Work/svn/shared-svn/studies/tschlenther/PAVE/Daten/PLZ/plz-gebiete.shp";
        String outputFile = "C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/PrivatkundenDirekt/TSanalyse/servicesProPLZ.csv";


        Network network = ScenarioUtils.createScenario(ConfigUtils.createConfig()).getNetwork();
        new MatsimNetworkReader(network).readFile(netFile);

        Carriers carriers = new Carriers();
        new CarrierPlanXmlReader(carriers).readFile(carriersFile);
        ServiceInPLZ serviceInPLZ = new ServiceInPLZ(network, carriers, shapeFile);
        serviceInPLZ.writeNumberOfServicesPerPLZToCSV(outputFile);

    }

    void writeNumberOfServicesPerPLZToCSV(String path) {
        readShapeFile();

        Map<Long, Integer> numberOfServicesPerPLZ = new HashMap<>();
        for (Carrier carrier : carriers.getCarriers().values()) {
            for (CarrierService service : carrier.getServices().values()) {
                long plz = getPlzForService(service);
                Integer newValue = numberOfServicesPerPLZ.containsKey(plz) ? numberOfServicesPerPLZ.get(plz) + 1 : 1;
                numberOfServicesPerPLZ.put(plz, newValue);
            }
        }
        try {
            String header = "PLZ;AnzahlServices\n";
            BufferedWriter writer = IOUtils.getBufferedWriter(path);
            writer.write(header);
            for (Long plz : numberOfServicesPerPLZ.keySet()) {
                writer.write(plz + ";" + numberOfServicesPerPLZ.get(plz) + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    long getPlzForService(CarrierService service) {
        Coord coord = this.network.getLinks().get(service.getLocationLinkId()).getCoord();
        Long plz = getPLZArea(coord);
        if (plz == null) plz = 999999l;
        return plz;
    }

    private void readShapeFile() {
        Collection<SimpleFeature> allLORs = ShapeFileReader.getAllFeatures(pathToPLZFile);
        for (SimpleFeature feature : allLORs) {
            Long plz = Long.valueOf((String) feature.getAttribute("plz"));
            this.plzFeatures.put(plz, feature);
        }
    }

    private Long getPLZArea(Coord c) {
        CoordinateTransformation ct = TransformationFactory.getCoordinateTransformation(TransformationFactory.DHDN_GK4, "EPSG:4326");
        Point point = MGC.coord2Point(ct.transform(c));
        for (Long plz : this.plzFeatures.keySet()) {
            Geometry geom = (Geometry) this.plzFeatures.get(plz).getDefaultGeometry();
            if (geom.contains(point)) return plz;
        }
        return null;
    }

}
