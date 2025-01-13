package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

/**
 * Accommodation building for rest and recovery.
 * Effects when used:
 * - Significantly reduces fatigue
 * - Provides essential rest areas
 * Examples: Dorms, rest areas, lounges
 */
public class Accommodation extends Building {
    // Rest effect
    private int fatigueDecrease;  // Energy restored per rest

    /**
     * Creates a new accommodation building.
     * Configures rest and recovery effects.
     *
     * @param name Building's display name
     * @param cost Construction cost in currency
     * @param picture Texture file path
     * @param lakeBonus Bonus for lake proximity
     * @param width Building width in units
     * @param height Building height in units
     * @param fatigueDecrease Energy restored per rest
     */
    public Accommodation(String name, float cost, String picture, float lakeBonus, float width, float height, int fatigueDecrease) {
        super(name, cost, picture, lakeBonus, width, height);
        this.fatigueDecrease = fatigueDecrease;
    }

    /**
     * Gets the building's type identifier.
     * @return "Accommodation" as building type
     */
    @Override
    public String getType() {
        return "Accommodation";
    }

    /**
     * Gets energy restored from resting.
     * @return Fatigue points reduced
     */
    public int getFatigueDecrease() {
        return fatigueDecrease;
    }
}
