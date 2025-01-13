package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

/**
 * Recreational building for leisure and entertainment.
 * Effects when used:
 * - Reduces stress and fatigue
 * - Costs money for activities
 * - Boosts satisfaction significantly
 * Examples: Gyms, game rooms, social spaces
 */
public class Recreational extends Building {
    // Usage effects
    private int decreaseCurrency;      // Activity cost
    private int decreaseFatigue;       // Energy restored
    private int increaseSatisfaction;  // Fun gained

    /**
     * Creates a new recreational building.
     * Configures costs and benefits of activities.
     *
     * @param name Building's display name
     * @param cost Construction cost in currency
     * @param picture Texture file path
     * @param lakeBonus Bonus for lake proximity
     * @param width Building width in units
     * @param height Building height in units
     * @param decreaseCurrency Cost per activity
     * @param decreaseFatigue Energy restored per use
     * @param increaseSatisfaction Fun gained per visit
     */
    public Recreational(String name, float cost, String picture, float lakeBonus, float width, float height, 
                       int decreaseCurrency, int decreaseFatigue, int increaseSatisfaction) {
        super(name, cost, picture, lakeBonus, width, height);
        this.decreaseCurrency = decreaseCurrency;
        this.decreaseFatigue = decreaseFatigue;
        this.increaseSatisfaction = increaseSatisfaction;
    }

    /**
     * Gets the building's type identifier.
     * @return "Recreational" as building type
     */
    @Override
    public String getType() {
        return "Recreational";
    }

    /**
     * Gets cost per activity.
     * @return Currency spent on recreation
     */
    public int getDecreaseCurrency() {
        return decreaseCurrency;
    }

    /**
     * Gets energy restored from activities.
     * @return Fatigue points reduced
     */
    public int getDecreaseFatigue() {
        return decreaseFatigue;
    }

    /**
     * Gets happiness gained from recreation.
     * @return Satisfaction points added
     */
    public int getIncreaseSatisfaction() {
        return increaseSatisfaction;
    }
}
