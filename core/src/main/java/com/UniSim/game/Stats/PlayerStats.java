package com.UniSim.game.Stats;

import static java.lang.Math.*;

/**
 * The PlayerStats class is responsible for managing and updating the player's
 * statistics in the game.
 * It tracks values like satisfaction, currency, fatigue, knowledge, and
 * building count.
 * It provides methods to get, increase, and decrease these statistics based on
 * in-game actions.
 */
public class PlayerStats {

    private int buildingCounter;
    private int satisfaction;
    private float currency;
    private int fatigue;
    private int knowledge;

    /**
     * Initializes the PlayerStats object with default values.
     * - buildingCounter: 0
     * - satisfaction: 0
     * - currency: 10,000
     * - fatigue: 0
     * - knowledge: 0
     */
    public PlayerStats() {
        this.buildingCounter = 0;
        satisfaction = 0;
        currency = 10000;
        fatigue = 0;
        knowledge = 0;
    }

    // get stats
    public String getSatisfaction() {
        return Integer.toString(satisfaction);
    }

    public float getCurrency() {
        return currency;
    }

    public int getFatigue() {
        return fatigue;
    }

    public int getKnowledge() {
        return knowledge;
    }

    public int getBuildingCounter() {
        return buildingCounter;
    }

    // change stats
    public void increaseSatisfaction(int amount) {
        satisfaction = satisfaction + amount;
    }

    public void decreaseSatisfaction(int amount) {
        satisfaction = max(satisfaction - amount, 0);
    }

    public void increaseCurrency(int amount) {
        currency = currency + amount;
    }

    /**
     * Decreases the player's currency by the given amount.
     * If the player has insufficient currency, it returns false.
     *
     * @param amount The amount to decrease currency by
     * @return true if the operation was successful, false if there was not enough
     *         currency
     */
    public boolean decreaseCurrency(int amount) {
        if (currency - amount < 0) {
            return false;
        } else {
            currency -= amount;
            return true;
        }
    }

    /**
     * Increases the player's fatigue by the given amount.
     * Fatigue cannot exceed 50. If the increase would cause the fatigue to exceed
     * 50, it returns false.
     *
     * @param amount The amount to increase fatigue by
     * @return true if the operation was successful, false if the fatigue would
     *         exceed 50
     */
    public boolean increaseFatigue(int amount) {
        if (fatigue + amount > 50) {
            return false;
        } else {
            fatigue += amount;
            return true;
        }
    }

    /**
     * Decreases the player's fatigue by the given amount.
     * Fatigue cannot go below 0.
     *
     * @param amount The amount to decrease fatigue by
     */
    public void decreaseFatigue(int amount) {
        fatigue = max(fatigue - amount, 0);
    }

    // assume knowledge doesn't decrease
    public void increaseKnowledge(int amount) {
        knowledge = knowledge + amount;
    }

    public void applyEventEffects(int scoreEffect, int moneyEffect) {
        increaseSatisfaction(scoreEffect);
        increaseCurrency(moneyEffect);
    }

    public void incrementBuildingCounter() {
        buildingCounter++;
    }

    /**
     * Calculates the player's current satisfaction based on several factors:
     * - Building count: Each building adds 1.5 satisfaction points
     * - Knowledge: Each point of knowledge adds 2 satisfaction points
     * - Fatigue: Fatigue causes a penalty to satisfaction, calculated as a fraction
     * of the fatigue level
     *
     * @return The calculated satisfaction level
     */
    public int calculateSatisfaction() {
        double fatiguePenalty = (fatigue / 50.0) * 10;
        double increase = (buildingCounter * 1.5) + (knowledge * 2) - (fatiguePenalty);
        return (int) increase;
    }

    /**
     * Reduces the player's currency by building cost.
     *
     * @param currency The cost to subtract from the player's current currency
     */
    public void takeOffBuildingCost(float currency) {
        this.currency -= currency;
    }

}
