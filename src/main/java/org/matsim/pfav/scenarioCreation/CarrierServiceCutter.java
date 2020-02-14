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

import org.matsim.api.core.v01.Id;
import org.matsim.contrib.freight.carrier.Carrier;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlReader;
import org.matsim.contrib.freight.carrier.CarrierPlanXmlWriterV2;
import org.matsim.contrib.freight.carrier.Carriers;

import java.util.ArrayList;
import java.util.List;

public class CarrierServiceCutter {

    private static final List<Id<Carrier>> investigatedCarriers = new ArrayList<>();

    public static void main(String[] args) {

        //Neukoelln
        investigatedCarriers.add(Id.create("0801_Neukölln", Carrier.class));
        investigatedCarriers.add(Id.create("0802_Britz/Buckow", Carrier.class));
        investigatedCarriers.add(Id.create("0803_Gropiusstadt", Carrier.class));

//        Tempelhof
//        investigatedCarriers.add(Id.create("0704_Tempelhof", Carrier.class));
//        investigatedCarriers.add(Id.create("0705_Mariendorf", Carrier.class));
//        investigatedCarriers.add(Id.create("0706_Marienfelde", Carrier.class));
//        investigatedCarriers.add(Id.create("0707_Lichtenrade", Carrier.class));



        Carriers original = new Carriers();
        CarrierPlanXmlReader reader = new CarrierPlanXmlReader(original);
        reader.readFile("C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/MapMatch/carriers_services_openBerlinNet_withInfiniteTrucks_TWsfitted.xml.gz");

        for (Id<Carrier> id : original.getCarriers().keySet()) {
            if (!(investigatedCarriers.contains(id))) {
                original.getCarriers().get(id).getServices().clear();
            }
        }

        new CarrierPlanXmlWriterV2(original).write("C:/Users/Work/svn/shared-svn/studies/tschlenther/freightAV/FrachtNachfrage/KEP/MapMatch/ganzNeukoelln_TWfitted.xml");
    }
}
