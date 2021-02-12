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
//    String inputConfig = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/1pct/noIncDRT.output_config.xml";
    String inputConfig = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/CarrierCreationInput/HomeTimeWindowAnalysis/berlin-v5.5-10pct.config.xml";
//    String inputPlans = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/1pct/noIncDRT.output_plans.xml.gz";
    Config config = ConfigUtils.loadConfig(inputConfig);
    String inputPlans = config.plans().getInputFile();

    private List<ActivityData> homeActivities = new ArrayList<>();

    public static void main(String[] args) {
        String outputFile = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/CarrierCreationInput/HomeTimeWindowAnalysis/homeActivities.csv";
        String outputFile2 = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/CarrierCreationInput/HomeTimeWindowAnalysis/homeActivitiesAggregated4hBins.csv";
        String outputFile3 = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/CarrierCreationInput/HomeTimeWindowAnalysis/homeActivitiesAggregated2hBins.csv";

        HomeTimeWindowAnalysis analysis = new HomeTimeWindowAnalysis();
        analysis.runAnalysis();
        analysis.writeStats(outputFile);
        analysis.writeTable4h(outputFile2);
        analysis.writeTable2h(outputFile3);
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

    private void writeTable4h(String file) {
        int[][] allTimes4h = createTableData4h();
        int[] startTimes4h = allTimes4h[0];
        int[] endTimes4h = allTimes4h[1];

        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING TABLE FOR 4H BINS!");
            int i = 0;
            writer.write("Times;0:00 to 3:59;4:00 to 7:59;8:00 to 11:59;12:00 to 15:59;16:00 to 19:59;20:00 to 24:00+");
            writer.newLine();
            writer.write("startTimes" + ";" + startTimes4h[0] + ";" + startTimes4h[1] + ";" + startTimes4h[2] + ";" + startTimes4h[3] + ";" + startTimes4h[4] + ";" + startTimes4h[5]);
            writer.newLine();
            writer.write("endTimes" + ";" + endTimes4h[0] + ";" + endTimes4h[1] + ";" + endTimes4h[2] + ";" + endTimes4h[3] + ";" + endTimes4h[4] + ";" + endTimes4h[5]);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void writeTable2h(String file) {
        int[][] allTimes2h = createTableData2h();
        int[] startTimes2h = allTimes2h[0];
        int[] endTimes2h = allTimes2h[1];

        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING TABLE FOR 2H BINS!");
            int i = 0;
            writer.write("Times;0:00 to 1:59;2:00 to 3:59;4:00 to 5:59;6:00 to 7:59;8:00 to 9:59;10:00 to 11:59;12:00 to 13:59;14:00 to 15:59;16:00 to 17:59;18:00 to 19:59;20:00 to 21:59;22:00 to 24:00+");
            writer.newLine();
            writer.write("startTimes" + ";" + startTimes2h[0] + ";" + startTimes2h[1] + ";" + startTimes2h[2] + ";" + startTimes2h[3] + ";" + startTimes2h[4] + ";" + startTimes2h[5] + ";" + startTimes2h[6] + ";" + startTimes2h[7] + ";" + startTimes2h[8] + ";" + startTimes2h[9] + ";" + startTimes2h[10] + ";" + startTimes2h[11]);
            writer.newLine();
            writer.write("endTimes" + ";" + endTimes2h[0] + ";" + endTimes2h[1] + ";" + endTimes2h[2] + ";" + endTimes2h[3] + ";" + endTimes2h[4] + ";" + endTimes2h[5] + ";" + endTimes2h[6] + ";" + endTimes2h[7] + ";" + endTimes2h[8] + ";" + endTimes2h[9] + ";" + endTimes2h[10] + ";" + endTimes2h[11]);
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public int[][] createTableData4h() {

        int[] startTimes4h = new int[6];
        int[] endTimes4h = new int[6];
        int[][] allTimes4h = new int[2][];

        for(ActivityData data : this.homeActivities) {

            if(data.startTime <= 14399.0) {
                startTimes4h[0] = startTimes4h[0] + 1;
            } else if(data.startTime > 14399.0 && data.startTime <=28799.0) {
                startTimes4h[1] = startTimes4h[1] + 1;
            } else if(data.startTime > 28799.0 && data.startTime <=43199.0) {
                startTimes4h[2] = startTimes4h[2] + 1;
            } else if(data.startTime > 43199.0 && data.startTime <=57599.0) {
                startTimes4h[3] = startTimes4h[3] + 1;
            } else if(data.startTime > 57599.0 && data.startTime <=71999.0) {
                startTimes4h[4] = startTimes4h[4] + 1;
            } else if(data.startTime > 71999.0) {
                startTimes4h[5] = startTimes4h[5] + 1;
            }

            if(data.endTime <= 14399.0) {
                endTimes4h[0] = endTimes4h[0] + 1;
            } else if(data.endTime > 14399.1 && data.endTime <=28799.0) {
                endTimes4h[1] = endTimes4h[1] + 1;
            } else if(data.endTime > 28799.0 && data.endTime <=43199.0) {
                endTimes4h[2] = endTimes4h[2] + 1;
            } else if(data.endTime > 43199.0 && data.endTime <=57599.0) {
                endTimes4h[3] = endTimes4h[3] + 1;
            } else if(data.endTime > 57599.0 && data.endTime <=71999.0) {
                endTimes4h[4] = endTimes4h[4] + 1;
            } else if(data.endTime > 71999.0) {
                endTimes4h[5] = endTimes4h[5] + 1;
            }
        }
        allTimes4h[0] = startTimes4h;
        allTimes4h[1] = endTimes4h;

        return allTimes4h;
    }

    public int[][] createTableData2h() {

        int[] startTimes2h = new int[12];
        int[] endTimes2h = new int[12];
        int[][] allTimes2h = new int[2][];

        for(ActivityData data : this.homeActivities) {

            if(data.startTime <= 7199.0) {
                startTimes2h[0] = startTimes2h[0] + 1;
            } else if(data.startTime > 7199.0 && data.startTime <=14399.0) {
                startTimes2h[1] = startTimes2h[1] + 1;
            } else if(data.startTime > 14399.0 && data.startTime <=21599.0) {
                startTimes2h[2] = startTimes2h[2] + 1;
            } else if(data.startTime > 21599.0 && data.startTime <=28799.0) {
                startTimes2h[3] = startTimes2h[3] + 1;
            } else if(data.startTime > 28799.0 && data.startTime <=35999.0) {
                startTimes2h[4] = startTimes2h[4] + 1;
            } else if(data.startTime > 35999.0 && data.startTime <=43199.0) {
                startTimes2h[5] = startTimes2h[5] + 1;
            } else if(data.startTime > 43199.0 && data.startTime <=50399.0) {
                startTimes2h[6] = startTimes2h[6] + 1;
            } else if(data.startTime > 50399.0 && data.startTime <=57599.0) {
                startTimes2h[7] = startTimes2h[7] + 1;
            } else if(data.startTime > 57599.0 && data.startTime <=64799.0) {
                startTimes2h[8] = startTimes2h[8] + 1;
            } else if(data.startTime > 64799.0 && data.startTime <=71999.0) {
                startTimes2h[9] = startTimes2h[9] + 1;
            } else if(data.startTime > 71999.0 && data.startTime <=79199.0) {
                startTimes2h[10] = startTimes2h[10] + 1;
            } else if(data.startTime > 79199.0) {
                startTimes2h[11] = startTimes2h[11] + 1;
            }

            if(data.endTime <= 7199.0) {
                endTimes2h[0] = endTimes2h[0] + 1;
            } else if(data.endTime > 7199.0 && data.endTime <=14399.0) {
                endTimes2h[1] = endTimes2h[1] + 1;
            } else if(data.endTime > 14399.0 && data.endTime <=21599.0) {
                endTimes2h[2] = endTimes2h[2] + 1;
            } else if(data.endTime > 21599.0 && data.endTime <=28799.0) {
                endTimes2h[3] = endTimes2h[3] + 1;
            } else if(data.endTime > 28799.0 && data.endTime <=35999.0) {
                endTimes2h[4] = endTimes2h[4] + 1;
            } else if(data.endTime > 35999.0 && data.endTime <=43199.0) {
                endTimes2h[5] = endTimes2h[5] + 1;
            } else if(data.endTime > 43199.0 && data.endTime <=50399.0) {
                endTimes2h[6] = endTimes2h[6] + 1;
            } else if(data.endTime > 50399.0 && data.endTime <=57599.0) {
                endTimes2h[7] = endTimes2h[7] + 1;
            } else if(data.endTime > 57599.0 && data.endTime <=64799.0) {
                endTimes2h[8] = endTimes2h[8] + 1;
            } else if(data.endTime > 64799.0 && data.endTime <=71999.0) {
                endTimes2h[9] = endTimes2h[9] + 1;
            } else if(data.endTime > 71999.0 && data.endTime <=79199.0) {
                endTimes2h[10] = endTimes2h[10] + 1;
            } else if(data.endTime > 79199.0) {
                endTimes2h[11] = endTimes2h[11] + 1;
            }
        }
        allTimes2h[0] = startTimes2h;
        allTimes2h[1] = endTimes2h;

        return allTimes2h;
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
