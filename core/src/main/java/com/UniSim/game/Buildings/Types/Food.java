package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

/**
 * Food service building for eating and refreshment.
 * Effects when used:
 * - Reduces fatigue through rest
 * - Costs money for food
 * - Increases satisfaction
 * Examples: Cafes, restaurants, food courts
 */
public class Food extends Building {
    // Usage effects
    private int decreaseCurrency;      // Money spent on food
    private int decreaseFatigue;       // Energy restored
    private int increaseSatisfaction;  // Happiness gained

    /**
     * Creates a new food service building.
     * Configures costs and benefits of eating.
     *
     * @param name Building's display name
     * @param cost Construction cost in currency
     * @param picture Texture file path
     * @param lakeBonus Bonus for lake proximity
     * @param width Building width in units
     * @param height Building height in units
     * @param decreaseCurrency Cost per meal
     * @param decreaseFatigue Energy restored per meal
     * @param increaseSatisfaction Happiness gained per meal
     */
    public Food(String name, float cost, String picture, float lakeBonus, float width, float height, 
               int decreaseCurrency, int decreaseFatigue, int increaseSatisfaction) {
        super(name, cost, picture, lakeBonus, width, height);
        this.decreaseCurrency = decreaseCurrency;
        this.decreaseFatigue = decreaseFatigue;
        this.increaseSatisfaction = increaseSatisfaction;
    }

    /**
     * Gets cost per meal.
     * @return Currency spent on food
     */
    public int getDecreaseCurrency() {
        return decreaseCurrency;
    }

    /**
     * Gets the building's type identifier.
     * @return "Food" as building type
     */
    @Override
    public String getType() {
        return "Food";
    }

    /**
     * Gets happiness gained from eating.
     * @return Satisfaction points added
     */
    public int getIncreaseSatisfaction() {
        return increaseSatisfaction;
    }

    /**
     * Gets energy restored from eating.
     * @return Fatigue points reduced
     */
    public int getDecreaseFatigue() {
        return decreaseFatigue;
    }
}
