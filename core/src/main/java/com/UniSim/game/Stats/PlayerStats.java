package com.UniSim.game.Stats;

import static java.lang.Math.*;

/**
 * Manages all player statistics and attributes in the game.
 * Tracks resources like currency and satisfaction, as well as
 * player state like fatigue and knowledge levels.
 * Provides methods to modify stats while enforcing game rules.
 */
public class PlayerStats {
    private int buildingCounter;   // Number of buildings placed
    private int satisfaction;      // Overall player satisfaction
    private float currency;        // Available money for spending
    private int fatigue;          // Current fatigue level (0-50)
    private int knowledge;        // Academic progress
    private static String username;  // Player's chosen name

    /**
     * Creates new player stats with starting values.
     * Players begin with 10,000 currency and all other stats at zero.
     */
    public PlayerStats() {
        this.buildingCounter = 0;
        satisfaction = 0;
        currency = 10000;
        fatigue = 0;
        knowledge = 0;
    }

    // Getters for all stats
    /** Gets current satisfaction level */
    public int getSatisfaction() {
        return satisfaction;
    }

    /** Gets available currency */
    public float getCurrency() {
        return currency;
    }

    /** Gets current fatigue level */
    public int getFatigue() {
        return fatigue;
    }

    /** Gets accumulated knowledge */
    public int getKnowledge() {
        return knowledge;
    }

    /** Gets number of buildings placed */
    public int getBuildingCounter() {
        return buildingCounter;
    }

    /** Gets player's username */
    public static String getUsername() {
        return PlayerStats.username;
    }

    // Stat modification methods
    /** Increases satisfaction by given amount */
    public void increaseSatisfaction(int amount) {
        satisfaction = satisfaction + amount;
    }

    /** Decreases satisfaction, won't go below zero */
    public void decreaseSatisfaction(int amount) {
        satisfaction = max(satisfaction - amount, 0);
    }

    /** Adds to available currency */
    public void increaseCurrency(int amount) {
        currency = currency + amount;
    }

    /**
     * Attempts to spend currency if enough is available.
     * 
     * @param amount Amount to spend
     * @return true if purchase successful, false if insufficient funds
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
     * Attempts to increase fatigue level.
     * Fatigue is capped at 50 to prevent excessive penalties.
     * 
     * @param amount Fatigue to add
     * @return true if fatigue was increased, false if at max
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
     * Reduces fatigue level through rest.
     * Fatigue cannot go below zero.
     */
    public void decreaseFatigue(int amount) {
        fatigue = max(fatigue - amount, 0);
    }

    /** Increases knowledge from studying or activities */
    public void increaseKnowledge(int amount) {
        knowledge = knowledge + amount;
    }

    /** Applies effects from random events */
    public void applyEventEffects(int scoreEffect, int moneyEffect) {
        increaseSatisfaction(scoreEffect);
        increaseCurrency(moneyEffect);
    }

    /** Increments building count when new building is placed */
    public void incrementBuildingCounter() {
        buildingCounter++;
    }

    /**
     * Calculates overall satisfaction based on various factors:
     * - Each building adds 1.5 satisfaction
     * - Each knowledge point adds 2 satisfaction
     * - High fatigue reduces satisfaction
     */
    public int calculateSatisfaction() {
        double fatiguePenalty = (fatigue / 50.0) * 10;
        double increase = (buildingCounter * 1.5) + (knowledge * 2) - (fatiguePenalty);
        return (int) increase;
    }

    /** Deducts cost when purchasing a building */
    public void takeOffBuildingCost(float cost) {
        this.currency -= cost;
    }

    /** Sets the player's username */
    public static void setUsername(String username) {
        PlayerStats.username = username;
    }

    /**
     * Gets movement speed modifier based on fatigue.
     * Speed decreases linearly with fatigue:
     * - 100% speed at 0 fatigue
     * - 50% speed at max fatigue (50)
     */
    public float getSpeedModifier() {
        return 1.0f - (fatigue / 100.0f);
    }
}
