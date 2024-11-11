package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

/**
 * Academic class represents a specific type of Building that contributes to intelligence
 * and fatigue gains for the player. Extends the Building class and adds unique attributes.
 */
public class Academic extends Building {

    private int intelegenceGain;
    private int fatigueGain;

    /**
     * Constructor to initialize an Academic building with specific intelligence and fatigue gains
     * along with other general building properties.
     * @param name Name of the academic building.
     * @param cost Cost of the building in in-game currency.
     * @param picture Path to the texture image file for the building.
     * @param lakeBonus Bonus granted when the building is placed near a lake.
     * @param width Width of the building in game units.
     * @param height Height of the building in game units.
     * @param intelegenceGain Amount of intelligence gained when interacting with this building.
     * @param fatigueGain Amount of fatigue gained when interacting with this building.
     */
    public Academic(String name, float cost, String picture, float lakeBonus, float width, float height, int intelegenceGain, int fatigueGain) {
        super(name, cost, picture, lakeBonus, width, height);
        this.intelegenceGain = intelegenceGain;
        this.fatigueGain = fatigueGain;
    }

    /**
     * Gets the type of the building, which is "Academic" for this class.
     * @return A string representing the building type.
     */
    @Override
    public String getType(){
        return "Academic";
    }

    public int getIntelligenceGain() {
        return intelegenceGain;
    }

    public int getFatigueGain() {
        return fatigueGain;
    }
}
