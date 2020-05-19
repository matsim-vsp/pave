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

import org.apache.log4j.Logger;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Population;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigGroup;
import org.matsim.core.config.groups.PlanCalcScoreConfigGroup;
import org.matsim.core.config.groups.StrategyConfigGroup;
import org.matsim.core.config.groups.SubtourModeChoiceConfigGroup;
import org.matsim.core.gbl.MatsimRandom;
import org.matsim.core.population.PopulationUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * this class should provide functionality to configure and assign mobility types to the berlin population as they are
 * defined in the PAVE project. It is specifically designed for this particular use case! You should not copy or use this
 * anywhere else.
 */
final class PAVEBerlinModifier {

    private static final String SUBPOP_FLEXIBLE = "flexible";
    //pre-Corona = 0.28, post-corona (april '20) = 0.11
    private static final double SUBPOP_FLEXIBLE_DEFAULT_WEIGHT = 0.28;

    private static final String SUBPOP_PRICE_SENSITIVE = "priceSensitive";
    //pre-Corona = 0.18, post-corona (april '20) = 0.32
    private static final double SUBPOP_PRICE_SENSITIVE_DEFAULT_WEIGHT = 0.18;

    private static String SUBPOP_SENSATIONSEEKER = "sensationSeeker";
    //pre-Corona = 0.23, post-corona (april '20) = 0.12
    private static final double SUBPOP_SENSATIONSEEKER_DEFAULT_WEIGHT = 0.23;

    /* The subpopulation SUBPOP_FIXED will get split up further, i.e. into one subsubpopulation per mode entry in subtourModeChoice config group.
     * During the agent assignment, the split sizes correspond to the initial share of these modes (HARDCODED!).
     * That means: we have people being 'fixed' on car, people being 'fixed' on pt etc.
    */
    private static final String SUBPOP_FIXED = "fixed";
    //pre-Corona = 0.18, post-corona (april '20) = 0.27
    private static final double SUBPOP_FIXED_DEFAULT_WEIGHT = 0.18;

    /**
     * This is the sensitivity factor for the mobility-type-specific parameters. Each specific parameter is multiplied with this global factor.
     * That means, for the price sensitivity, we use the same factor as for the inflexibility concerning modes...
     */
    private static final double GLOBAL_SENSIVITY_FACTOR = 2.0;
    private static Logger log = Logger.getLogger(PAVEBerlinModifier.class);

    static Set<String> getMobilityTypeSubPopulationNames(SubtourModeChoiceConfigGroup smcCfg){
        HashSet<String> set = new HashSet<>(
                Arrays.asList("person_" + SUBPOP_FLEXIBLE,
                "person_" + SUBPOP_PRICE_SENSITIVE,
                "person_" + SUBPOP_SENSATIONSEEKER));
        //subpopulation 'fixed' is further split into subsubpopulations, one for each mode..
        for (String mode : smcCfg.getModes()) {
            set.add("person_"+ SUBPOP_FIXED + "_" + mode);
        }
        return set;
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
            //change nothing....
        }
        {   //Fixed
            List<String> modes = Arrays.asList(config.subtourModeChoice().getModes());
            modes.remove("drt"); //do not create a fixed population for drt! (basically, this is what the sensation seekers represent..)

            //consistency check
            if(modes.size() != 4){
                throw new IllegalArgumentException("unexpected number of modes in subtourModeChoice. number of modes = " + modes.size());
            }

            //for each mode that can be altered via subtourModeChoice, create one 'fixed' subpopulation (except drt)
            //split the SUBPOP_FIXED_DEFAULT_WEIGHT according to initial mode share into these subpopulations...
            for (String mode : modes) {
                PlanCalcScoreConfigGroup.ScoringParameterSet params = config.planCalcScore().getOrCreateScoringParameters("person_" + SUBPOP_FIXED + "_" + mode);
                copyAllScoringParameters(defaultScoringParams, params);

                PlanCalcScoreConfigGroup.ModeParams modeParams = params.getOrCreateModeParams(mode);
                modeParams.setConstant(modeParams.getConstant() * GLOBAL_SENSIVITY_FACTOR); //TODO: problem when ASC = 0 (which is almost everywhere the case..)
            }
        }
        {   //price sensitive
            PlanCalcScoreConfigGroup.ScoringParameterSet params = config.planCalcScore().getOrCreateScoringParameters("person_" + SUBPOP_PRICE_SENSITIVE);
            copyAllScoringParameters(defaultScoringParams, params);
            params.setMarginalUtilityOfMoney(params.getMarginalUtilityOfMoney() * GLOBAL_SENSIVITY_FACTOR);
        }
        {   //sensation seeker
            PlanCalcScoreConfigGroup.ScoringParameterSet params = config.planCalcScore().getOrCreateScoringParameters("person_" + SUBPOP_SENSATIONSEEKER);
            copyAllScoringParameters(defaultScoringParams, params);
            //TODO: if no drt scoring params existed before, this is not a problem, right?
            PlanCalcScoreConfigGroup.ModeParams drtParams = params.getOrCreateModeParams("drt");
            drtParams.setConstant(drtParams.getConstant() * GLOBAL_SENSIVITY_FACTOR);
        }
    }

    private static void configureStrategies(Config config) {
        //get strategies for persons and copy them for all mobility type subpopulations. Then remove the old settings for persons
        Set<StrategyConfigGroup.StrategySettings> settingsToRemove = new HashSet<>();
        Set<StrategyConfigGroup.StrategySettings> personStratSettings = config.strategy().getStrategySettings().stream()
                .filter(s -> s.getSubpopulation().equals("person"))
                .collect(Collectors.toSet());
        personStratSettings.forEach(strategySettings -> {
                    for (String subPopulation : getMobilityTypeSubPopulationNames(config.subtourModeChoice())) {
                        StrategyConfigGroup.StrategySettings settingsCopy = new StrategyConfigGroup.StrategySettings();
                        settingsCopy.setStrategyName(strategySettings.getStrategyName());
                        settingsCopy.setWeight(strategySettings.getWeight());
                        settingsCopy.setDisableAfter(strategySettings.getDisableAfter());
                        settingsCopy.setSubpopulation(subPopulation);
                        config.strategy().addStrategySettings(settingsCopy);
                        //remove old settings
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

    final static void randomlyAssignMobilityTypes (Population population, Map<String, Double> mobilityType2Weight){

        //calc the sum of all weights
        double weightSum = mobilityType2Weight.values().stream().mapToDouble(weight -> weight).sum();

        Random r = MatsimRandom.getLocalInstance();

        population.getPersons().values().stream()
                .filter(p -> PopulationUtils.getSubpopulation(p).equals("person"))
                .forEach(person -> {
                    double mobilityTypeRnd = r.nextDouble() * weightSum;
                    double sum = 0.0;
                    for (String mobilityType : mobilityType2Weight.keySet()) {
                        sum += mobilityType2Weight.get(mobilityType);
                        if(mobilityTypeRnd <= sum){
                            //the fixed subpopulation is split up further according to the mode shares
                            if(mobilityType.endsWith(SUBPOP_FIXED)){

                                //these values are taken from the results of the (uncontinued!) base case (berlinv5.4 !! 10pct) TODO: take modeStats from berlinv5.5 10 pct
                                double carModeShare = 0.348313824315758;
                                double ptModeShare = 0.20184540611436;
                                double bicycleModeShare = 0.172047821767194;
                                double walkModeShare = 0.182968046444402;

                                //TODO: we do not allow subtourModeChoice for ride! does that mean, we shoul calculate 'riders' as fixed people? atm, we do not!
                                // that also means, we will keep the mode share of 'ride' when introducing drt (which is not totally obvious to me), tschlenther may '20

                                double fixedModesShareSum = carModeShare + ptModeShare + bicycleModeShare + walkModeShare;
                                double fixedModeRnd = r.nextDouble() * fixedModesShareSum;

                                if(fixedModeRnd <= carModeShare){
                                    PopulationUtils.putSubpopulation(person, "person_" + mobilityType + "_car");
                                    break;
                                } else if (fixedModeRnd <= carModeShare + ptModeShare){
                                    PopulationUtils.putSubpopulation(person, "person_" + mobilityType + "_pt");
                                    break;
                                } else if (fixedModeRnd <= carModeShare + ptModeShare + bicycleModeShare) {
                                    PopulationUtils.putSubpopulation(person, "person_" + mobilityType + "_bicycle");
                                    break;
                                } else {
                                    PopulationUtils.putSubpopulation(person, "person_" + mobilityType + "_walk");
                                    break;
                                }

                            } else {
                                PopulationUtils.putSubpopulation(person, "person_" + mobilityType);
                                break;
                            }
                        }
                    }
                });
        printAssignmentResults(population);
    }

    private static final void printAssignmentResults(Population population) {
        Map<String, Integer> subPopulationMap = new HashMap<>();
        population.getPersons().values().stream()
        .filter(person -> PopulationUtils.getSubpopulation(person).contains("person"))
        .forEach(person -> {
            String subPop = PopulationUtils.getSubpopulation(person);
                subPopulationMap.compute(subPop, (k,v) ->  (v == null)? 1 : v+1);
        });
        int sum = 0;
        for (Integer value : subPopulationMap.values()) {
            sum += value;
        }
        for (Map.Entry<String, Integer> entry : subPopulationMap.entrySet()) {
            log.info("nrOf people in subPopulation " + entry.getKey() + " = " + entry.getValue() + "\t = " + String.format("%d", entry.getValue()*100/sum) + "%");
        }
    }
}
