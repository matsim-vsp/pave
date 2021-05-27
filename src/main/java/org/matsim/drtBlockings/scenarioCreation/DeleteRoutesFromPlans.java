package org.matsim.drtBlockings.scenarioCreation;

import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.population.*;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.PopulationUtils;
import org.matsim.core.router.TripStructureUtils;

import java.util.List;
import java.util.Map;

public class DeleteRoutesFromPlans {

    private static final String INPUT_POPULATION = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/Lichtenberg Nord_Carrier/p2-23.output_plans_drtUsersOnly_selectedPlans.xml.gz";
    private static final String INPUT_CONFIG = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/InputDRT/Lichtenberg Nord_Carrier/p2-23.output_config.xml";
    private static final String OUTPUT_DRTPOP = "C:/Users/simon/tubCloud/Shared/MA-Meinhardt/p2-23.output_plans_drtUsersOnly_selectedPlans_noRoutes.xml.gz";

    public static void main(String[] args) {

        Population originalPop = PopulationUtils.readPopulation(INPUT_POPULATION);
        Config config = ConfigUtils.loadConfig(INPUT_CONFIG);

        Population outputPop = PopulationUtils.createPopulation(config);

        originalPop.getPersons().values().stream().map(HasPlansAndId::getSelectedPlan).forEach(plan -> {
            List<TripStructureUtils.Trip> trips = TripStructureUtils.getTrips(plan);

            for(TripStructureUtils.Trip trip : trips) {

                for(Leg leg : trip.getLegsOnly()) {
                    leg.setRoute(null);
                }
            }

            if(!outputPop.getPersons().containsKey(plan.getPerson().getId())) {
                outputPop.addPerson(plan.getPerson());
            }
        });

        PopulationUtils.writePopulation(outputPop, OUTPUT_DRTPOP);
        System.out.println("size of new population: " + outputPop.getPersons().size());
        System.out.println("New pop was written to " + OUTPUT_DRTPOP);
    }
}
