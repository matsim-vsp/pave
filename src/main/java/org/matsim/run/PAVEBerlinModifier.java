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

package org.matsim.run;

import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigGroup;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.StrategyConfigGroup;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.population.PopulationUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * this class should provide functionality to configure and assign mobility types to the berlin population as they are
 * defined in the PAVE project. It is specifically designed for this particular use case! You should not copy or use this
 * anywhere else.
 */
class PAVEBerlinModifier {

    private static String SUBPOP_FLEXIBLE = "flexible";
    private static String SUBPOP_PRICE_SENSITIVE = "priceSensitive";
    private static String SUBPOP_SENSATIONSEEKER = "sensationSeeker";

    /* TODO the subpopulation fix will probably have to get split up further, by mode (share)..
     * that means: we have people being 'fixed' on car, people being 'fixed' on pt etc.
    */
    private static String SUBPOP_FIX = "fix";

    static Set<String> getMobilityTypes(){
        return new HashSet<>(Arrays.asList(SUBPOP_FLEXIBLE,SUBPOP_PRICE_SENSITIVE,SUBPOP_FIX,SUBPOP_SENSATIONSEEKER));
    }

    static void configureMobilityTypeSubPopulations(Config config){
        configureStrategies(config);
        configureScoring(config);
    }

    private static void configureScoring(Config config) {
        //for some reason, the key in the scoringParametersPerSubpopulation map is null if no subPopulation is provided (which is the case in the default berlin config)
        PlanCalcScoreConfigGroup.ScoringParameterSet defaultScoringParams = config.planCalcScore().getScoringParametersPerSubpopulation().get(null);

        //set the subpopulation to default
        defaultScoringParams.setSubpopulation(PlanCalcScoreConfigGroup.DEFAULT_SUBPOPULATION);

        //copy the scoring parameters for the freight subpopulation
        PlanCalcScoreConfigGroup.ScoringParameterSet freightParams = config.planCalcScore().getOrCreateScoringParameters("freight");
        copyAllScoringParameters(defaultScoringParams, freightParams);

        //set the scoring parameters for each mobilityType
        {   //Flexible
            PlanCalcScoreConfigGroup.ScoringParameterSet params = config.planCalcScore().getOrCreateScoringParameters("person_" + SUBPOP_FLEXIBLE);
            copyAllScoringParameters(defaultScoringParams, params);

            //TODO: keep the default params?
        }
        {   //Fix
            /* TODO: the subpopulation fix will probably have to get split up further, by mode (share)..
             * that means: we have people being 'fixed' on car, people being 'fixed' on pt etc.
             */
            PlanCalcScoreConfigGroup.ScoringParameterSet params = config.planCalcScore().getOrCreateScoringParameters("person_" + SUBPOP_FIX);
            copyAllScoringParameters(defaultScoringParams, params);

            //TODO: increase ASC of the corresponding mode (when subpopulation 'fix' is further split up)
        }
        {   //price sensitive
            PlanCalcScoreConfigGroup.ScoringParameterSet params = config.planCalcScore().getOrCreateScoringParameters("person_" + SUBPOP_PRICE_SENSITIVE);
            copyAllScoringParameters(defaultScoringParams, params);

            //TODO: increase utility of money
//            params.setMarginalUtilityOfMoney();
        }
        {   //sensation seeker
            PlanCalcScoreConfigGroup.ScoringParameterSet params = config.planCalcScore().getOrCreateScoringParameters("person_" + SUBPOP_SENSATIONSEEKER);
            copyAllScoringParameters(defaultScoringParams, params);

            //TODO: increase ASC of innovative modes (drt, taxi)
        }
    }

    private static void configureStrategies(Config config) {
        //get strategies for persons and copy them for all mobility type subpopulations. Then remove the old settings for persons
        Set<StrategyConfigGroup.StrategySettings> settingsToRemove = new HashSet<>();
        Set<StrategyConfigGroup.StrategySettings> personStratSettings = config.strategy().getStrategySettings().stream()
                .filter(s -> s.getSubpopulation().equals("person"))
                .collect(Collectors.toSet());
        personStratSettings.forEach(strategySettings -> {
                    for (String mobilityTypeName : getMobilityTypes()) {
                        StrategyConfigGroup.StrategySettings settingsCopy = new StrategyConfigGroup.StrategySettings();
                        settingsCopy.setStrategyName(strategySettings.getStrategyName());
                        settingsCopy.setWeight(strategySettings.getWeight());
                        settingsCopy.setDisableAfter(strategySettings.getDisableAfter());
                        settingsCopy.setSubpopulation("person" + "_" + mobilityTypeName);
                        config.strategy().addStrategySettings(settingsCopy);

                        config.strategy().removeParameterSet(strategySettings);
                    }
                });
    }

    private static void copyAllScoringParameters(PlanCalcScoreConfigGroup.ScoringParameterSet fromScoringParameterSet, PlanCalcScoreConfigGroup.ScoringParameterSet toScoringParameterSet) {
        for (Collection<? extends ConfigGroup> parameterSets : fromScoringParameterSet.getParameterSets().values()) {
            for (ConfigGroup parameterSet : parameterSets) {
                toScoringParameterSet.addParameterSet(parameterSet);
            }
        }
    }

    static void randomlyAssignMobilityTypes (Population population, Map<String, Double> mobilityType2Weight){

        //calc the sum of all weights
        double weightSum = mobilityType2Weight.values().stream().mapToDouble(weight -> weight).sum();

        Random r = MatsimRandom.getLocalInstance();

        population.getPersons().values().stream()

                .filter(p -> PopulationUtils.getSubpopulation(p).equals("person"))
                .forEach(person -> {
                    double drawnRnd = r.nextDouble() * weightSum;
                    double sum = 0.0;
                    for (String mobilityType : mobilityType2Weight.keySet()) {
                        sum += mobilityType2Weight.get(mobilityType);
                        if(drawnRnd <= sum){
                            PopulationUtils.putSubpopulation(person, "person_" + mobilityType);
                            break;
                        }
                    }
                });
    }
}
