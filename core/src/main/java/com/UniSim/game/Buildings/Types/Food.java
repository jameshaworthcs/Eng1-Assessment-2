package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

/**
 * Food class represents a type of Building that influences player currency, fatigue, and satisfaction.
 * Extends the Building class and includes attributes for currency decrease, fatigue decrease, and satisfaction increase.
 */
public class Food extends Building {

    private int decreaseCurrency;
    private int decreaseFatigue;
    private int increaseSatisfaction;

    /**
     * Constructor to initialize a Food building with specified properties, including currency decrease,
     * fatigue decrease, and satisfaction increase, in addition to general building attributes.
     * @param name Name of the food building.
     * @param cost Cost of the building in in-game currency.
     * @param picture Path to the texture image file for the building.
     * @param lakeBonus Bonus granted when the building is placed near a lake.
     * @param width Width of the building in game units.
     * @param height Height of the building in game units.
     * @param decreaseCurrency Amount of currency decreased when interacting with this building.
     * @param decreaseFatigue Amount of fatigue decreased when interacting with this building.
     * @param increaseSatisfaction Amount of satisfaction increased when interacting with this building.
     */
    public Food(String name, float cost, String picture, float lakeBonus, float width, float height, int decreaseCurrency, int decreaseFatigue, int increaseSatisfaction) {
        super(name, cost, picture, lakeBonus, width, height);
        this.decreaseCurrency = decreaseCurrency;
        this.decreaseFatigue = decreaseFatigue;
        this.increaseSatisfaction = increaseSatisfaction;
    }

    public int getDecreaseCurrency() {
        return decreaseCurrency;
    }

    /**
     * Returns the building type as a string.
     * @return "Food", representing the type of this building.
     */
    @Override
    public String getType(){
        return "Food";
    }

    public int getIncreaseSatisfaction() {
        return increaseSatisfaction;
    }

    public int getDecreaseFatigue() {
        return decreaseFatigue;
    }
}
