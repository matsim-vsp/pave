package org.matsim.drtBlockings.analysis;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeTimeWindowAnalysis {
    String inputPath = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/scenarios/berlin-v5.5-10pct/input/drtBlocking/";
//    String inputConfig = inputPath + "blckBase1.output_config.xml";
    String inputConfig = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/1pct/noIncDRT.output_config.xml";
    String inputPlans = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/1pct/noIncDRT.output_plans.xml.gz";
    Config config = ConfigUtils.loadConfig(inputConfig);

    private List<ActivityData> homeActivities = new ArrayList<>();

    public static void main(String[] args) {
        String outputFile = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/homeActivities.csv";
        String outputFile2 = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/homeActivitiesAggregated.csv";

        HomeTimeWindowAnalysis analysis = new HomeTimeWindowAnalysis();
        analysis.runAnalysis();
        analysis.writeStats(outputFile);
        analysis.writeTable(outputFile2);
        System.out.println("Writing of Home Activities to " + outputFile + " and " + outputFile2 + " was successful!");

    }

    private void writeStats(String file) {
        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING STATS!");
            int i = 0;
            writer.write("No;personId;startTime;endTime;duration;type");
            writer.newLine();
            for (ActivityData data : this.homeActivities) {

                writer.write(i + ";" + data.personId + ";" + data.startTime+ ";" + data.endTime + ";" + data.getDuration() + ";" + data.type);
                writer.newLine();
                i++;
            }
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTable(String file) {
        int[][] allTimes = createTableData();
        int[] startTimes = allTimes[0];
        int[] endTimes = allTimes[1];

        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING TABLE!");
            int i = 0;
            writer.write("Times;0:00 to 3:59;4:00 to 7:59;8:00 to 11:59;12:00 to 15:59;16:00 to 19:59;20:00 to 24:00+");
            writer.newLine();
            writer.write("startTimes" + ";" + startTimes[0] + ";" + startTimes[1] + ";" + startTimes[2] + ";" + startTimes[3] + ";" + startTimes[4] + ";" + startTimes[5]);
            writer.newLine();
            writer.write("endTimes" + ";" + endTimes[0] + ";" + endTimes[1] + ";" + endTimes[2] + ";" + endTimes[3] + ";" + endTimes[4] + ";" + endTimes[5]);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public int[][] createTableData() {

        int[] startTimes = new int[6];
        int[] endTimes = new int[6];
        int[][] allTimes = new int[2][];

        for(ActivityData data : this.homeActivities) {

            if(data.startTime <= 14399.0) {
                startTimes[0] = startTimes[0] + 1;
            } else if(data.startTime > 14399.0 && data.startTime <=28799.0) {
                startTimes[1] = startTimes[1] + 1;
            } else if(data.startTime > 28799.0 && data.startTime <=43199.0) {
                startTimes[2] = startTimes[2] + 1;
            } else if(data.startTime > 43199.0 && data.startTime <=57599.0) {
                startTimes[3] = startTimes[3] + 1;
            } else if(data.startTime > 57599.0 && data.startTime <=71999.0) {
                startTimes[4] = startTimes[4] + 1;
            } else if(data.startTime > 71999.0) {
                startTimes[5] = startTimes[5] + 1;
            }

            if(data.endTime <= 14399.0) {
                endTimes[0] = endTimes[0] + 1;
            } else if(data.endTime > 14399.1 && data.endTime <=28799.0) {
                endTimes[1] = endTimes[1] + 1;
            } else if(data.endTime > 28799.0 && data.endTime <=43199.0) {
                endTimes[2] = endTimes[2] + 1;
            } else if(data.endTime > 43199.0 && data.endTime <=57599.0) {
                endTimes[3] = endTimes[3] + 1;
            } else if(data.endTime > 57599.0 && data.endTime <=71999.0) {
                endTimes[4] = endTimes[4] + 1;
            } else if(data.endTime > 71999.0) {
                endTimes[5] = endTimes[5] + 1;
            }
        }
        allTimes[0] = startTimes;
        allTimes[1] = endTimes;

        return allTimes;
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
                        int index = selectedPlan.getPlanElements().indexOf(element);

                        double startTime = 0.0;
//                        double endTime = PlanRouter.calcEndOfActivity(((Activity) element), selectedPlan, config);
                        double endTime = 0.0;
                        if(((Activity) element).getStartTime().isDefined()) {
                            //if startTime is defined we can use it
                            startTime = ((Activity) element).getStartTime().seconds();
//                            endTime = 60 * 60.0 * 24;
                        } else {
                            //if startTime is not defined we need to calculate it from the earlier activities + legs
                            if(index != 0) {
                                PlanElement pe = selectedPlan.getPlanElements().get(index - 1);
                                //here we double-check if the planElement before home is a leg, which should always be the case
                                if(pe instanceof Leg) {
                                    startTime = ((Leg) pe).getDepartureTime().seconds() + ((Leg) pe).getTravelTime().seconds();
                                } else {
                                    System.out.println(pe + " is not a leg!");
                                }
                            }
                        }
                        if(((Activity) element).getEndTime().isDefined()) {
                            if(((Activity) element).getEndTime().seconds() >= startTime) {
                                endTime = ((Activity) element).getEndTime().seconds();
                            } else {
                                PlanElement pe = selectedPlan.getPlanElements().get(index  + 1);
                                //double check, see above
                                if(pe instanceof Leg) {
                                    endTime = ((Leg) pe).getDepartureTime().seconds() + 60.0;
                                    startTime = endTime;
                                } else {
                                    System.out.println(pe + " is not a leg!");
                                }
                            }

                        } else {
                            if(index == selectedPlan.getPlanElements().size() - 1) {
                                endTime = 60.0 * 60 * 100;
                            } else {
                                //TODO: Resolve why we get into this else-part while handling home-activites which
                                // actually DO have a defined end-time when you print the plan's PlanElements but
                                // when you print the endTime itself, it's undefined (see prints to console below)
                                PlanElement pe = selectedPlan.getPlanElements().get(index  + 1);
                                //double check, see above
                                if(pe instanceof Leg) {
                                    endTime = ((Leg) pe).getDepartureTime().seconds() + 60.0;
                                } else {
                                    System.out.println(pe + " is not a leg!");
                                }


//                                System.out.println(((Activity) element).getEndTime());
//                                System.out.println(selectedPlan.getPlanElements());
                            }
                        }
                        Id<Person> personId = selectedPlan.getPerson().getId();
                        String type = ((Activity) element).getType();
                        ActivityData data = new ActivityData(personId, type);
                        if (startTime <= endTime) {
                            data.startTime = startTime;
                            data.endTime = endTime;
                            this.homeActivities.add(data);
//                            System.out.println("FINE!");
                        } else {
                            System.out.println("ID: " + personId);
                            System.out.println("START: " + startTime);
                            System.out.println("END: " + endTime);
                            System.out.println("startTime and endTime for person " + personId + " is not time-consistent!/n It won't be written to the output-data!");
                        }
                    }
                }
            }
        }
    }
    private class ActivityData {

        private Id<Person> personId;
        private double startTime;
        private double endTime;
        private String type;

        private ActivityData(Id<Person> personId, String type) {
            this.personId = personId;
            this.type = type;
        }

        double getDuration() { return this.endTime - this.startTime; }
    }
}
