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

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.population.io.PopulationReader;
import org.matsim.core.population.io.PopulationWriter;
import org.matsim.core.scenario.ScenarioUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

public class PopulationCreator {

    public static void main(String[] args) {
        Scenario scenario = ScenarioUtils.createMutableScenario(ConfigUtils.createConfig());
        PopulationReader reader = new PopulationReader(scenario);
        reader.readFile("C:/Users/Work/svn/public-svn/matsim/scenarios/countries/de/berlin/berlin-v5.2-1pct/output-berlin-v5.2-1pct/ITERS/it.500/berlin-v5.2-1pct.500.plans.xml.gz");

//        Scenario scenario = ScenarioUtils.createMutableScenario(ConfigUtils.createConfig());
//        PopulationReader reader = new PopulationReader(scenario);
//
//        reader.readFile("C:/Users/Work/git/freightAV/input/Plans/berlin1pctScenario_5pctCarLegsNowPFAV.xml.gz");

        new PopulationWriter(cutPopulationTo100ForEachMode(convertAgentsToPFAVOwners(scenario))).write("C:/Users/Work/git/freightAV/input/BerlinScenario/5.3/berlin100PersonsPerMode.xml");
    }

    private Population defineAndGet1000SpatialEquallyDistributedPFAVOwners(Population population) {
        Population pfavOwners = ScenarioUtils.createScenario(ConfigUtils.createConfig()).getPopulation();
        Random rand = MatsimRandom.getLocalInstance();
        for (int i = 0; i < 1000; i++) {
            boolean isCarUser = false;
            while (!isCarUser) {
                int randIdx = rand.nextInt(population.getPersons().size());
                Id<Person> personID = (Id<Person>) population.getPersons().keySet().toArray()[randIdx];

                for (PlanElement elem : population.getPersons().get(personID).getSelectedPlan().getPlanElements()) {
                    if (elem instanceof Leg) {
                        if (((Leg) elem).getMode().equals("car")) {
                            isCarUser = true;
                            ((Leg) elem).setMode("taxi");
                        }
                    }
                }
                if (isCarUser) {
                    pfavOwners.addPerson(population.getPersons().get(personID));
                    break;
                }
            }
        }
        return pfavOwners;
    }

    public static Population convertAgentsToPFAVOwners(Scenario scenario) {

        for (Person p : scenario.getPopulation().getPersons().values()) {
            if (MatsimRandom.getRandom().nextDouble() <= 0.05) {
                //TODO: i only care about the selected plans, right?
                for (PlanElement pe : p.getSelectedPlan().getPlanElements()) {
                    if (pe instanceof Leg) {
                        if (((Leg) pe).getMode().equals("car"))
                            ((Leg) pe).setMode("taxi");
                    }
                }
            }
        }
        return scenario.getPopulation();
    }

    public static Population cutPopulationTo100ForEachMode(Population pop) {

        Map<String, Integer> nrOfPersonsPerMode = new HashMap<>();
        Population newPop = ScenarioUtils.createScenario(ConfigUtils.createConfig()).getPopulation();
        for (Person p : pop.getPersons().values()) {
            for (PlanElement pe : p.getSelectedPlan().getPlanElements()) {
                if (pe instanceof Leg) {
                    String mode = ((Leg) pe).getMode();
                    Integer nrOfPersonsForThisMode = nrOfPersonsPerMode.get(mode);
                    if (nrOfPersonsForThisMode == null) {
                        nrOfPersonsForThisMode = 1;
                    } else {
                        nrOfPersonsForThisMode++;
                    }
                    if (nrOfPersonsForThisMode < 100) {
                        newPop.addPerson(p);
                        nrOfPersonsPerMode.put(mode, nrOfPersonsForThisMode);
                        break;
                    }
                }
            }
            Iterator<? extends Plan> it = p.getPlans().iterator();
            while (it.hasNext()) {
                Plan plan = it.next();
                if (!plan.equals(p.getSelectedPlan())) it.remove();
            }
        }

        return newPop;

    }

    public static void cutPopulationTo100ForEachMode() {
        Scenario scenario = ScenarioUtils.createMutableScenario(ConfigUtils.createConfig());
        PopulationReader reader = new PopulationReader(scenario);

        reader.readFile("C:/Users/Work/git/freightAV/input/Plans/berlin1pctScenario_5pctCarLegsNowPFAV.xml.gz");

        Map<String, Integer> nrOfPersonsPerMode = new HashMap<>();
        Population newPop = ScenarioUtils.createScenario(ConfigUtils.createConfig()).getPopulation();
        for (Person p : scenario.getPopulation().getPersons().values()) {
            for (PlanElement pe : p.getSelectedPlan().getPlanElements()) {
                if (pe instanceof Leg) {
                    String mode = ((Leg) pe).getMode();
                    Integer nrOfPersonsForThisMode = nrOfPersonsPerMode.get(mode);
                    if (nrOfPersonsForThisMode == null) {
                        nrOfPersonsForThisMode = 1;
                    } else {
                        nrOfPersonsForThisMode++;
                    }
                    if (nrOfPersonsForThisMode < 100) {
                        newPop.addPerson(p);
                        nrOfPersonsPerMode.put(mode, nrOfPersonsForThisMode);
                        break;
                    }
                }
            }
        }
        new PopulationWriter(newPop).write("C:/Users/Work/git/freightAV/input/Plans/berlin100PersonsPerMode.xml.gz");
    }
}
