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

import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.events.ActivityEndEvent;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.PersonArrivalEvent;
import org.matsim.api.core.v01.events.PersonDepartureEvent;
import org.matsim.api.core.v01.events.handler.ActivityEndEventHandler;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.api.core.v01.events.handler.PersonArrivalEventHandler;
import org.matsim.api.core.v01.events.handler.PersonDepartureEventHandler;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.population.io.PopulationReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import privateAV.PFAVActionCreator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class BaseCaseVehicleOccupancyListener implements PersonDepartureEventHandler, PersonArrivalEventHandler,
        ActivityStartEventHandler, ActivityEndEventHandler {

    private int totalAmountOfVehicles;

    private int nrOfSlotsPerHour;
    private Population population;
    private int[] departures;
    private int[] arrivals;
    private int[] atService;

    private BaseCaseVehicleOccupancyListener(int totalAmountOfVehicles, int nrOfSlotsPerHour, Population pop) {
        this.totalAmountOfVehicles = totalAmountOfVehicles;
        this.nrOfSlotsPerHour = nrOfSlotsPerHour;
        this.departures = new int[36 * nrOfSlotsPerHour + 1];
        this.arrivals = new int[36 * nrOfSlotsPerHour + 1];
        this.atService = new int[36 * nrOfSlotsPerHour + 1];
        this.population = pop;
    }

    public static void main(String[] args) {

        String inputPop = "C:/Users/Work/tubCloud/MasterArbeit/Runs/serious/bCs_gzBln_11k_Pkw/berlin-v5.3-1pct.output_plans.xml.gz";

        Scenario scenario = ScenarioUtils.createScenario(ConfigUtils.createConfig());
        new PopulationReader(scenario).readFile(inputPop);
        Population pop = scenario.getPopulation();
        EventsManager manager = EventsUtils.createEventsManager();


        BaseCaseVehicleOccupancyListener handler = new BaseCaseVehicleOccupancyListener(11000, 12, pop);
        manager.addHandler(handler);

        MatsimEventsReader reader = new MatsimEventsReader(manager);


        String input = "C:/Users/Work/tubCloud/MasterArbeit/Runs/serious/bCs_gzBln_11k_Pkw/berlin-v5.3-1pct.output_events.xml.gz";
        reader.readFile(input);
        String output = "C:/Users/Work/tubCloud/MasterArbeit/Runs/serious/bCs_gzBln_11k_Pkw/vehicleOccupancy.csv";
        handler.writeStats(output);
    }

    @Override
    public void handleEvent(PersonArrivalEvent event) {

        if ( ( (this.population.getPersons().containsKey(event.getPersonId())) &&
                event.getLegMode().equals("car"))
        || event.getPersonId().toString().contains("freight") )  {
            int slot = (int) (event.getTime() / (3600 / nrOfSlotsPerHour));
            this.arrivals[slot]++;
        }
    }

    @Override
    public void handleEvent(ActivityStartEvent event) {
        if(event.getPersonId().toString().contains("freight")
            && event.getActType().contains(PFAVActionCreator.SERVICE_ACTIVITY_TYPE)){
            int slot = (int) (event.getTime() / (3600 / nrOfSlotsPerHour));
            this.atService[slot] ++;
        }
    }


    @Override
    public void handleEvent(ActivityEndEvent event) {
        if(event.getPersonId().toString().contains("freight")
                && event.getActType().contains(PFAVActionCreator.SERVICE_ACTIVITY_TYPE)){
            int slot = (int) (event.getTime() / (3600 / nrOfSlotsPerHour));
            this.atService[slot] --;
        }
    }

    @Override
    public void handleEvent(PersonDepartureEvent event) {
        if ( ( (this.population.getPersons().containsKey(event.getPersonId())) &&
                event.getLegMode().equals("car") )
            || event.getPersonId().toString().contains("freight") )   {
            int slot = (int) (event.getTime() / (3600 / nrOfSlotsPerHour));
            this.departures[slot]++;
        }
    }

    private void writeStats(String filename) {
        BufferedWriter bw = IOUtils.getBufferedWriter(filename);
        DecimalFormat df = new DecimalFormat("##.##");
        try {
            bw.write("timeSlot;cat;value");
            int currentVehiclesEnRoute = 0;
            int currentVehiclesAtService = 0;
            for (int i = 0; i < nrOfSlotsPerHour * 36 + 1; i++) {
                currentVehiclesEnRoute = currentVehiclesEnRoute + departures[i] - arrivals[i];
                currentVehiclesAtService = currentVehiclesAtService + atService[i];
                if (currentVehiclesEnRoute < 0) {
                    throw new IllegalStateException("i = " + i +
                            " and departures = " + departures[i] + " and arrivals=" + arrivals[i]);
                } if( currentVehiclesAtService < 0){
                    throw new IllegalStateException("i = " + i +
                            " and atServices = " + atService[i] );
                }
                bw.newLine();
                bw.write("" + i + ";" + "parked;" + (totalAmountOfVehicles - currentVehiclesEnRoute));
                bw.newLine();
                bw.write("" + i + ";" + "enRoute;" + currentVehiclesEnRoute);
                bw.newLine();
                bw.write("" + i + ";" + "atService;" + currentVehiclesAtService );
            }

            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
