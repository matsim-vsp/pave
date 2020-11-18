package org.matsim.drtBlockings.analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.core.utils.misc.OptionalTime;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeTimeWindowAnalysis {
    String inputPath = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/scenarios/berlin-v5.5-10pct/input/drtBlocking/";
    String inputConfig = inputPath + "blckBase1.output_config.xml";
    String inputPlans = inputPath + "blckBase1.output_plans_drtOnly_splitAgents.xml.gz";
    Config config = ConfigUtils.loadConfig(inputConfig);

    private List<ActivityData> homeActivities = new ArrayList<>();

    public static void main(String[] args) {

        String outputFile = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/homeActivities.csv";

        HomeTimeWindowAnalysis analysis = new HomeTimeWindowAnalysis();
        analysis.runAnalysis();
        analysis.writeStats(outputFile);
        System.out.println("Writing of Home Activities to " + outputFile + " was successful!");

    }

    private void writeStats(String file) {
        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING!");
            int i = 0;
            writer.write("No;personId;startTime;endTime;duration");
            writer.newLine();

            for (ActivityData data : this.homeActivities) {

                writer.write(i + ";" + data.personId + ";" + data.startTime + ";" + data.endTime + ";" + data.duration);
                writer.newLine();
                i++;
            }

            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void runAnalysis() {
        config.plans().setInputFile(inputPlans);
        Scenario scenario = ScenarioUtils.loadScenario(config);

        Population pop = scenario.getPopulation();

        for(Person person : pop.getPersons().values()) {
            Plan selectedPlan = person.getSelectedPlan();

            for( PlanElement element: selectedPlan.getPlanElements()) {
                if(element instanceof Activity) {
                    if(((Activity) element).getType().contains("home")) {

                        Id<Person> personId = selectedPlan.getPerson().getId();
                        OptionalTime startTime = ((Activity) element).getStartTime();
                        OptionalTime endTime = ((Activity) element).getEndTime();
                        String type = ((Activity) element).getType();

                        ActivityData data = new ActivityData(personId, startTime);

                        data.endTime = endTime;
                        data.type = type;
                        homeActivities.add(data);

                    }

                }
            }
        }
    }

    private class ActivityData {
        private Id<Person> personId;
        private OptionalTime startTime;
        private OptionalTime endTime;
        private String type;
        private double duration = Double.parseDouble(endTime.toString()) - Double.parseDouble(startTime.toString());

        private ActivityData(Id<Person> personId, OptionalTime startTime) {
            this.personId = personId;
            this.startTime = startTime;
        }
    }
}
