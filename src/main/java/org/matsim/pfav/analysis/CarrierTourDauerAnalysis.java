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

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.network.Link;
import org.matsim.contrib.freight.carrier.*;
import org.matsim.core.utils.io.IOUtils;
import privateAV.FreightAVConfigGroup;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarrierTourDauerAnalysis {

    private Carriers carriers = new Carriers();

    private Map<Id<Link>, List<CarrierTourAnalysisData>> depotTourData = new HashMap<>();
    private FreightAVConfigGroup pfavConfigGroup;

    public CarrierTourDauerAnalysis(String carriersFile, FreightAVConfigGroup pfavConfigGroup) {
        this.pfavConfigGroup = pfavConfigGroup;
        this.readCarriers(carriersFile);
        this.deriveTotalRetoolAndServiceTimePerCarrier();
    }

    public static void main(String[] args) {
        String input = "D:/svn/runs-svn/pfav/berlin/finalTRBRuns2020/ownTrucksWDrivers_14k/output_carriers.xml";
        String output = "D:/svn/runs-svn/pfav/berlin/finalTRBRuns2020/ownTrucksWDrivers_14k/ITERS/it.0/carriers_it0_stats.csv";

        new CarrierTourDauerAnalysis(input, new FreightAVConfigGroup()).writeStats(output);
        System.out.println("Done");

    }


    public void writeStats(String output) {
        BufferedWriter writer = IOUtils.getBufferedWriter(output);
        try {
            int index = 1;
            writer.write("index;depot;travelTime;serviceTime;serviceCount;serviceCapacity;retoolTime");


            for (Id<Link> depot : this.depotTourData.keySet()) {
                List<CarrierTourAnalysisData> dataList = this.depotTourData.get(depot);
                for (CarrierTourAnalysisData data : dataList) {
                    writer.newLine();
                    writer.write("" + index + ";" + depot.toString() + ";" + data.travelTime + ";" + data.serviceTime + ";" + data.serviceCount + ";" + data.serviceCapacity + ";" + data.retoolTime);
                    index++;
                }
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void deriveTotalRetoolAndServiceTimePerCarrier() {
        for (Carrier carrier : this.carriers.getCarriers().values()) {
            CarrierPlan plan = carrier.getSelectedPlan();

            for (ScheduledTour tour : plan.getScheduledTours()) {


                double travelTime = 0;
                double serviceTime = 0;
                double serviceCount = 0;
                double serviceCapacity = 0;
                for (Tour.TourElement elem : tour.getTour().getTourElements()) {
                    if (elem instanceof Tour.ServiceActivity) {
                        serviceTime += ((Tour.ServiceActivity) elem).getDuration();
                        serviceCapacity += ((Tour.ServiceActivity) elem).getService().getCapacityDemand();
                        serviceCount++;
                    } else if (elem instanceof Tour.Leg) {
                        travelTime += ((Tour.Leg) elem).getExpectedTransportTime();
                    }
                }
                Id<Link> depot = tour.getVehicle().getLocation();
                CarrierTourAnalysisData data = new CarrierTourAnalysisData(travelTime, serviceTime, serviceCount, serviceCapacity);
                if (this.depotTourData.get(depot) == null) {
                    List<CarrierTourAnalysisData> tourDataList = new ArrayList<>();
                    tourDataList.add(data);
                    this.depotTourData.put(depot, tourDataList);
                } else {
                    this.depotTourData.get(depot).add(data);
                }
            }


        }

    }

    private void readCarriers(String file) {
        CarrierPlanXmlReader reader = new CarrierPlanXmlReader(carriers);
        reader.readFile(file);
    }

    private class CarrierTourAnalysisData {
        final double travelTime;
        final double retoolTime;
        final double serviceTime;
        final double serviceCount;
        final double serviceCapacity;

        CarrierTourAnalysisData(double travelTime, double serviceTime, double serviceCount, double serviceCapacity) {
            this.travelTime = travelTime;
            this.retoolTime = 2 * pfavConfigGroup.getPfavReToolTime();
            this.serviceTime = serviceTime;
            this.serviceCount = serviceCount;
            this.serviceCapacity = serviceCapacity;
        }
    }

}
