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

package org.matsim.pfav.scenarioCreation;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.matsim.api.core.v01.Coord;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.network.Network;
import org.matsim.api.core.v01.network.NetworkWriter;
import org.matsim.core.network.NetworkUtils;
import org.matsim.core.network.filter.NetworkFilterManager;
import org.matsim.core.network.io.MatsimNetworkReader;
import org.matsim.core.utils.geometry.CoordinateTransformation;
import org.matsim.core.utils.geometry.transformations.TransformationFactory;

import java.io.*;
import java.util.HashSet;
import java.util.Set;


/**
 * this is based on GenerateChargersFromGasStations in org.matsim.vsp.ers.bev.testscenario
 */
public class PostOfficeCreator {

    private static final String JSON_FILE = "C:/Users/Work/git/freightAV/input/OSM/Postfilialen/22_02_2019_overpass_post_office_amenities_berlin.json";
    private static final String NET_FILE = "C:/Users/Work/svn/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.2-1pct/output-berlin-v5.2-1pct/berlin-v5.2-1pct.output_network.xml.gz";
    private static final String OUTPUT_CSV = "C:/Users/Work/git/freightAV/input/OSM/Postfilialen/24_02_2019_post_offices_berlin_linkIDs_GK4net.csv";
    private static final String OUTPUT_DEPOT_NET = "C:/Users/Work/git/freightAV/input/OSM/Postfilialen/24_02_2019_post_offices_berlin_linkIDs_GK4net.xml";

    public static void main(String[] args) throws IOException, ParseException {

        //transform to DHDN GK4
        CoordinateTransformation ct = TransformationFactory.getCoordinateTransformation(TransformationFactory.WGS84, "EPSG:31464");
        Network network = NetworkUtils.createNetwork();
        new MatsimNetworkReader(network).readFile(NET_FILE);

        NetworkFilterManager filterCarLinks = new NetworkFilterManager(network);
        filterCarLinks.addLinkFilter(l -> {
            if (l.getAllowedModes().contains(TransportMode.car)) return true;
            else return false;
        });
        Network filteredNet = filterCarLinks.applyFilters();

        Set<Id<Link>> depotLinks = new HashSet<>();


        BufferedReader in = new BufferedReader(new FileReader(JSON_FILE));
        JSONParser jp = new JSONParser();
        JSONObject jsonObject = (JSONObject) jp.parse(in);
        JSONArray elements = ((JSONArray) (jsonObject.get("elements")));

        int count = 1;
        BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_CSV));

        writer.write("id;lat;lon;linkID;commentOrTagsIfIsLink");
        writer.newLine();
        for (Object o : elements) {
            JSONObject jo = (JSONObject) o;
            long elementID = Long.parseLong(jo.get("id").toString());

            String s;

            if (jo.get("type").toString().equals("node")) {
                double y = Double.parseDouble(jo.get("lat").toString());
                double x = Double.parseDouble(jo.get("lon").toString());
                Coord c = ct.transform(new Coord(x, y));
                Link l = NetworkUtils.getNearestLink(filteredNet, c);

                s = "" + elementID + ";" + c.getY() + ";" + c.getX() + ";" + l.getId() + ";";
                if (depotLinks.contains(l.getId())) {
                    s = s + "together on the link with at least 1 other office";
                } else {
                    depotLinks.add(l.getId());
                }
            } else {
                s = "" + elementID + ";;;;" + jo.get("tags").toString();
            }
            System.out.println("writing element number " + count);
            writer.write(s);
            writer.newLine();

            count++;
        }
        writer.close();
        NetworkFilterManager filterDepotLinks = new NetworkFilterManager(filteredNet);
        filterDepotLinks.addLinkFilter(l -> {
            if (depotLinks.contains(l.getId())) return true;
            else return false;
        });
        new NetworkWriter(filterDepotLinks.applyFilters()).writeV2(OUTPUT_DEPOT_NET);
    }


}
