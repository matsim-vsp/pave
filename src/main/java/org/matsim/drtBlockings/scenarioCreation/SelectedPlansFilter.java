package org.matsim.drtBlockings.scenarioCreation;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.router.TripStructureUtils;
import org.matsim.utils.objectattributes.attributable.Attributes;

import javax.print.attribute.Attribute;
import java.util.List;
import java.util.Map;

public class SelectedPlansFilter {

//    private static final String INPUT_POPULATION = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/Lichtenberg Nord_Carrier/p2-23.output_plans_drtUsersOnly.xml.gz";
    private static final String INPUT_POPULATION = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/1pct/noIncDRT.output_plans.xml.gz";
    private static final String INPUT_CONFIG = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/Lichtenberg Nord_Carrier/p2-23.output_config.xml";
    private static final String OUTPUT_DRTPOP = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/pnoIncDRT.output_plans_drtUsersOnly_selectedPlans.xml.gz";
//    private static final String OUTPUT_DRTPOP = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/p2-23.output_plans_drtUsersOnly_selectedPlans.xml.gz";

    public static void main(String[] args) {

        Population originalPop = PopulationUtils.readPopulation(INPUT_POPULATION);
        Config config = ConfigUtils.loadConfig(INPUT_CONFIG);

        Population outputPop = PopulationUtils.createPopulation(config);
        PopulationFactory fac = outputPop.getFactory();
//        for(Person person : originalPop.getPersons().values()) {
//            Plan selectedPlan = person.getSelectedPlan();
//            Map<String, Object> attr = person.getAttributes().getAsMap();
//            Person drtPerson = fac.createPerson(Id.createPersonId(selectedPlan.getPerson().getId()));
//            drtPerson.addPlan(selectedPlan);
//            drtPerson.setSelectedPlan(selectedPlan);
//
//            for(String attribute : attr.keySet()) {
//                drtPerson.getAttributes().putAttribute(attribute, attr.get(attribute));
//            }
//            outputPop.addPerson(drtPerson);
//        }
        originalPop.getPersons().values().stream().map(HasPlansAndId::getSelectedPlan).forEach(plan -> {
            List<TripStructureUtils.Trip> trips = TripStructureUtils.getTrips(plan);
            Map<String, Object> attr = plan.getPerson().getAttributes().getAsMap();

            for(TripStructureUtils.Trip trip : trips) {
                if(trip.getLegsOnly().stream().anyMatch(leg -> leg.getMode().equals("drt"))) {
                    Person drtPerson = fac.createPerson(Id.createPersonId(plan.getPerson().getId()));
                    drtPerson.addPlan(plan);
                    drtPerson.setSelectedPlan(plan);

                    if(!outputPop.getPersons().containsKey(drtPerson.getId())) {

                        for(String attribute : attr.keySet()) {
                            drtPerson.getAttributes().putAttribute(attribute, attr.get(attribute));
                        }
//                        String name = "income";
//                        double income = 1234.;
//
//                        drtPerson.getAttributes().putAttribute(name, income);
//                        outputPop.addPerson(drtPerson);
                    }
                }
            }
        });

        PopulationUtils.writePopulation(outputPop, OUTPUT_DRTPOP);
        System.out.println("size of new population: " + outputPop.getPersons().size());
        System.out.println("New pop was written to " + OUTPUT_DRTPOP);
    }
}
