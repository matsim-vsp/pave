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

import org.matsim.core.utils.io.IOUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;

public class TaxiTimeProfileConverter {


    private final int hours;
    private int nrOfSlotsPerHour;
    private int[] emptyDrive;
    private int[] pickup;
    private int[] occupiedDrive;
    private int[] dropoff;
    private int[] stay;


    private TaxiTimeProfileConverter(int hours, int nrOfSlotsPerHour) {
        this.hours = hours;
        this.nrOfSlotsPerHour = nrOfSlotsPerHour;
        this.emptyDrive = new int[hours * nrOfSlotsPerHour + 1];
        this.occupiedDrive = new int[hours * nrOfSlotsPerHour + 1];
        this.stay = new int[hours * nrOfSlotsPerHour + 1];
        this.pickup = new int[hours * nrOfSlotsPerHour + 1];
        this.dropoff = new int[hours * nrOfSlotsPerHour + 1];
    }

    public static void main(String[] args) {
        String input;
        String output;
        if (args.length < 1) {
            input = "C:/Users/Work/svn/runs-svn/pfav/berlin/newPattern/NoReroute_Transporter_13k_newPattern/ITERS/it.0/berlin-v5.3-1pct.0.taxi_status_time_profiles_taxi.txt";
            output = "C:/Users/Work/svn/runs-svn/pfav/berlin/newPattern/NoReroute_Transporter_13k_newPattern/ITERS/it.0/taxi_status_time_profiles_taxi_converted.csv";
        } else {
            input = args[0];
            output = args[1];
        }
        TaxiTimeProfileConverter converter = new TaxiTimeProfileConverter(36, 12);
        converter.readStats(input);

        converter.writeStats(output);
    }

    private void readStats(String file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            boolean header = true;
            String line = reader.readLine();

            line = reader.readLine();

            int slot = 0;

            while (line != null) {
                String[] lineArr = line.split("\t");
                this.emptyDrive[slot] = Integer.parseInt(lineArr[1]);
                this.pickup[slot] = Integer.parseInt(lineArr[2]);
                this.occupiedDrive[slot] = Integer.parseInt(lineArr[3]);
                this.dropoff[slot] = Integer.parseInt(lineArr[4]);
                this.stay[slot] = Integer.parseInt(lineArr[5]);
                slot++;
                line = reader.readLine();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeStats(String file) {
        BufferedWriter bw = IOUtils.getBufferedWriter(file);
        try {
            bw.write("timeSlot;cat;value");
            for (int i = 0; i < emptyDrive.length; i++) {
                bw.newLine();
                bw.write("" + i + ";" + "EmptyDrive;" + emptyDrive[i]);
                bw.newLine();
                bw.write("" + i + ";" + "OccupiedDrive;" + occupiedDrive[i]);
                bw.newLine();
                bw.write("" + i + ";" + "Pickup;" + pickup[i]);
                bw.newLine();
                bw.write("" + i + ";" + "DropOff;" + dropoff[i]);
                bw.newLine();
                bw.write("" + i + ";" + "Stay;" + stay[i]);
            }

            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
