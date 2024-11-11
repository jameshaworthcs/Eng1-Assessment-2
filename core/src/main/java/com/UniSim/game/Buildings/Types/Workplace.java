package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

/**
 * Workplace class represents a type of Building where players can earn currency at the cost of increased fatigue.
 * Extends the Building class and includes attributes for fatigue increase and currency increase.
 */
public class Workplace extends Building {

    private int increaseFatigue;
    private int increaseCurrency;

    public Workplace(String name, float cost, String picture, float lakeBonus, float width, float height, int increaseFatigue, int increaseCurrency) {
        super(name, cost, picture, lakeBonus, width, height);
        this.increaseFatigue = increaseFatigue;
        this.increaseCurrency = increaseCurrency;
    }

    /**
     * Returns the building type as a string.
     * @return "Workplace", representing the type of this building.
     */
    @Override
    public String getType(){
        return "Workplace";
    }

    public int getIncreaseFatigue() {
        return increaseFatigue;
    }

    public int getIncreaseCurrency() {
        return increaseCurrency;
    }
}
