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
    //pre-Corona = 0.28, post-corona (april '20) = 0.11
    private static double SUBPOP_FLEXIBLE_DEFAULT_WEIGHT = 0.28;

    private static String SUBPOP_PRICE_SENSITIVE = "priceSensitive";
    //pre-Corona = 0.18, post-corona (april '20) = 0.32
    private static double SUBPOP_PRICE_SENSITIVE_DEFAULT_WEIGHT = 0.18;

    private static String SUBPOP_SENSATIONSEEKER = "sensationSeeker";
    //pre-Corona = 0.23, post-corona (april '20) = 0.12
    private static double SUBPOP_SENSATIONSEEKER_DEFAULT_WEIGHT = 0.23;

    /* TODO the subpopulation fixed will probably have to get split up further, by mode (share)..
     * that means: we have people being 'fixed' on car, people being 'fixed' on pt etc.
    */
    private static String SUBPOP_FIXED = "fixed";
    //pre-Corona = 0.18, post-corona (april '20) = 0.27
    private static double SUBPOP_FIXED_DEFAULT_WEIGHT = 0.18;

    static Set<String> getMobilityTypes(){
        return new HashSet<>(Arrays.asList(SUBPOP_FLEXIBLE,SUBPOP_PRICE_SENSITIVE,SUBPOP_FIXED,SUBPOP_SENSATIONSEEKER));
    }

    static Map<String, Double> getMobilityTypesWithDefaulWeights(){
        Map<String,Double> mobilityType2Weight = new HashMap<>();
        mobilityType2Weight.put(SUBPOP_FLEXIBLE, SUBPOP_FLEXIBLE_DEFAULT_WEIGHT);
        mobilityType2Weight.put(SUBPOP_FIXED, SUBPOP_FIXED_DEFAULT_WEIGHT);
        mobilityType2Weight.put(SUBPOP_PRICE_SENSITIVE, SUBPOP_PRICE_SENSITIVE_DEFAULT_WEIGHT);
        mobilityType2Weight.put(SUBPOP_SENSATIONSEEKER, SUBPOP_SENSATIONSEEKER_DEFAULT_WEIGHT);
        return mobilityType2Weight;
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
            PlanCalcScoreConfigGroup.ScoringParameterSet params = config.planCalcScore().getOrCreateScoringParameters("person_" + SUBPOP_FIXED);
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
