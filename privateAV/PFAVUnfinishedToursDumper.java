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

package privateAV;

import org.matsim.contrib.util.CSVLineBuilder;
import org.matsim.contrib.util.CompactCSVWriter;
import org.matsim.core.utils.io.IOUtils;

import java.util.Collection;

class PFAVUnfinishedToursDumper {

    private final Collection<FreightTourDataPlanned> unfinishedTours;

    PFAVUnfinishedToursDumper(Collection<FreightTourDataPlanned> tours) {
        this.unfinishedTours = tours;
    }

    void writeStats(String file) {
        try (CompactCSVWriter writer = new CompactCSVWriter(IOUtils.getBufferedWriter(file), ';')) {
            writeHeader(writer);

            String stringFormat = "%s";
            String dblFormat = "%.1f";

            writeData(writer, stringFormat, dblFormat);
        }

    }

    private void writeHeader(CompactCSVWriter writer) {
        CSVLineBuilder lineBuilder = new CSVLineBuilder()
                .add("DepotLink")

                .add("PlannedTourDuration")
                .add("PlannedTourLength")
                .add("PlannedTotalCapacityDemand")
                .add("NumberOfRejections");
        writer.writeNext(lineBuilder);
    }

    private void writeData(CompactCSVWriter writer, String stringFormat, String dblFormat) {
        for (FreightTourDataPlanned data : this.unfinishedTours) {
            CSVLineBuilder lineBuilder = new CSVLineBuilder()
                    .addf(stringFormat, data.getDepotLink())
                    .addf(dblFormat, data.getPlannedTourDuration())
//                    .addf(dblFormat, data.getPlannedTourLength())
                    .addf("%d", data.getPlannedTotalCapacityDemand())
                    .addf("%d", data.getAmountOfRejections())
                    .add("-");

            writer.writeNext(lineBuilder);
        }
        writer.writeNextEmpty();
    }

}
