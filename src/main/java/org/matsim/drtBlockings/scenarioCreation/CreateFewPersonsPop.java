package org.matsim.drtBlockings.scenarioCreation;

import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.PopulationUtils;

public class CreateFewPersonsPop {

    //Size of new pop
    private static final int SIZE = 200;

    private static final String INPUT_POPULATION = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/Lichtenberg Nord_Carrier/p2-23.output_plans_drtUsersOnly_selectedPlans_noRoutes.xml.gz";
    private static final String INPUT_CONFIG = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/Lichtenberg Nord_Carrier/p2-23.output_config.xml";
    private static final String OUTPUT_DRTPOP = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/p2-23.output_plans_" + SIZE + "Persons.xml.gz";

    public static void main(String[] args) {

        Population originalPop = PopulationUtils.readPopulation(INPUT_POPULATION);
        Config config = ConfigUtils.loadConfig(INPUT_CONFIG);

        Population outputPop = PopulationUtils.createPopulation(config);

        for (int i = 0;i < SIZE; i++) {
            Person person = originalPop.getPersons().values().stream().findAny().get();
            originalPop.removePerson(person.getId());

            if(!outputPop.getPersons().containsKey(person.getId())) {
                outputPop.addPerson(person);
            }
        }

        PopulationUtils.writePopulation(outputPop, OUTPUT_DRTPOP);
        System.out.println("size of new population: " + outputPop.getPersons().size());
        System.out.println("New pop was written to " + OUTPUT_DRTPOP);
    }
}
