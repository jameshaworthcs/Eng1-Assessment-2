package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

/**
 * Accommodation class represents a type of Building that helps decrease player fatigue.
 * Extends the Building class and includes a unique attribute for fatigue reduction.
 */
public class Accommodation extends Building {

    private int fatigueDecrease;

    /**
     * Constructor to initialize an Accommodation building with specific properties including
     * fatigue decrease along with other general building attributes.
     * @param name Name of the accommodation building.
     * @param cost Cost of the building in in-game currency.
     * @param picture Path to the texture image file for the building.
     * @param lakeBonus Bonus granted when the building is placed near a lake.
     * @param width Width of the building in game units.
     * @param height Height of the building in game units.
     * @param fatigueDecrease Amount of fatigue reduced when interacting with this building.
     */
    public Accommodation(String name, float cost, String picture, float lakeBonus, float width, float height, int fatigueDecrease) {
        super(name, cost, picture, lakeBonus, width, height);
        this.fatigueDecrease = fatigueDecrease;
    }

    /**
     * Returns the building type as a string.
     * @return "Accommodation", representing the type of this building.
     */
    @Override
    public String getType(){
        return "Accommodation";
    }

    public int getFatigueDecrease() {
        return fatigueDecrease;
    }
}
