package org.matsim.drtBlockings.analysis;

import gnu.trove.map.TByteCharMap;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.Scenario;
import org.matsim.api.core.v01.events.ActivityEndEvent;
import org.matsim.api.core.v01.events.ActivityStartEvent;
import org.matsim.api.core.v01.events.handler.ActivityEndEventHandler;
import org.matsim.api.core.v01.events.handler.ActivityStartEventHandler;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.events.EventsUtils;
import org.matsim.core.events.MatsimEventsReader;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.core.utils.misc.OptionalTime;
import org.matsim.drtBlockings.events.DrtBlockingEventsReader;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.DoubleSupplier;

public class HomeTimeWindowAnalysis implements ActivityStartEventHandler, ActivityEndEventHandler {

//    String inputConfig = inputPath + "blckBase1.output_config.xml";
//    String inputPlans = inputPath + "blckBase1.output_plans_drtOnly_splitAgents.xml.gz";
//    Config config = ConfigUtils.loadConfig(inputConfig);

    private Map<Id<Person>, Double> startTimes = new HashMap<>();
    private Map<Id<Person>, Double> endTimes = new HashMap<>();
    private Map<Id<Person>, String> activities = new HashMap<>();
    private List<Id<Person>> ids =  new ArrayList<>();
    private List<ActivityData> homeActivities = new ArrayList<>();

    public static void main(String[] args) {
//        String inputPath = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/chessboard/drtBlocking/";
//        String inputPath = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/berlin-v5.5-10pct/drtBlockingTest111/";
        String eventsFile = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/berlin-v5.5-10pct/drtBlockingTest111/blckBase1.output_events.xml.gz";
        String outputFile = "C:/Users/simon/Documents/UNI/MA/Projects/paveFork/output/homeActivities.csv";

        EventsManager manager = EventsUtils.createEventsManager();
        HomeTimeWindowAnalysis analysis = new HomeTimeWindowAnalysis();
        manager.addHandler(analysis);
        manager.initProcessing();
        MatsimEventsReader reader = DrtBlockingEventsReader.create(manager);
        reader.readFile(eventsFile);
        manager.finishProcessing();
        analysis.fillData();
        analysis.writeStats(outputFile);
        System.out.println("Writing of Home Activities to " + outputFile + " was successful!");

    }

    private void writeStats(String file) {
        BufferedWriter writer = IOUtils.getBufferedWriter(file);
        try {
            System.out.println("WRITING!");
            int i = 0;
            writer.write("No;personId;startTime;endTime;duration;type");
            writer.newLine();

            for (ActivityData data : this.homeActivities) {

                writer.write(i + ";" + data.personId + ";" + data.startTime+ ";" + data.endTime + ";" + data.duration + ";" + data.type);
                writer.newLine();
                i++;
            }

            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

//    public void runAnalysis() {
//        config.plans().setInputFile(inputPlans);
//        Scenario scenario = ScenarioUtils.loadScenario(config);
//
//        Population pop = scenario.getPopulation();
//
//        for(Person person : pop.getPersons().values()) {
//            Plan selectedPlan = person.getSelectedPlan();
//
//            for( PlanElement element: selectedPlan.getPlanElements()) {
//                if(element instanceof Activity) {
//                    if(((Activity) element).getType().contains("home")) {
//
//                        Id<Person> personId = selectedPlan.getPerson().getId();
////                        double startTime = Double.parseDouble(((Activity) element).getStartTime().toString());
//                        OptionalTime endTime = ((Activity) element).getEndTime();
//                        String type = ((Activity) element).getType();
//
//                        ActivityData data = new ActivityData(personId, endTime);
//
//                        data.endTime = endTime;
//                        data.type = type;
//                        homeActivities.add(data);
//
//                    }
//
//                }
//            }
//        }
//    }
    public void fillData() {

        for(Id<Person> personId : this.ids) {
            ActivityData data = new ActivityData(personId, this.activities.get(personId));
            if(this.startTimes.containsKey(personId)) {
                data.startTime = this.startTimes.get(personId);
            } else {
                data.startTime = 999999999.99;
            }
            if(this.endTimes.containsKey(personId)) {
                data.endTime = this.endTimes.get(personId);
            } else {
                data.endTime = 999999999.99;
            }
            this.homeActivities.add(data);
        }

    }

    @Override
    public void handleEvent(ActivityStartEvent event) {
        if(event.getActType().contains("home")) {
            this.startTimes.putIfAbsent(event.getPersonId(), event.getTime());
            this.activities.putIfAbsent(event.getPersonId(), event.getActType());
            if(!this.ids.contains(event.getPersonId())) {
                this.ids.add(event.getPersonId());
            } else {
                System.out.println("The list already contains person " + event.getPersonId());
            }
        }
    }

    @Override
    public void handleEvent(ActivityEndEvent event) {
        if(event.getActType().contains("home")) {
            this.endTimes.putIfAbsent(event.getPersonId(), event.getTime());
            this.activities.putIfAbsent(event.getPersonId(), event.getActType());
            if(!this.ids.contains(event.getPersonId())) {
                this.ids.add(event.getPersonId());
            } else {
                System.out.println("The list already contains person " + event.getPersonId());
            }
        }
    }

    private class ActivityData {

        private Id<Person> personId;
        private double startTime;
        private double endTime;
        private String type;
        private double duration = endTime - startTime;




        private ActivityData(Id<Person> personId, String type) {
            this.personId = personId;
            this.type = type;
        }
    }
}
