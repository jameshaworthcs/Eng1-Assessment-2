package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

/**
 * Academic building for studying and learning.
 * Provides:
 * - Knowledge gain through study
 * - Increased fatigue from mental work
 * Examples: Libraries, lecture halls, labs
 */
public class Academic extends Building {
    // Study effects
    private int intelegenceGain;  // Knowledge gained per use
    private int fatigueGain;      // Fatigue added per use

    /**
     * Creates a new academic building.
     * Configures study and fatigue effects.
     *
     * @param name Building's display name
     * @param cost Construction cost in currency
     * @param picture Texture file path
     * @param lakeBonus Bonus for lake proximity
     * @param width Building width in units
     * @param height Building height in units
     * @param intelegenceGain Knowledge points per study
     * @param fatigueGain Fatigue points per study
     */
    public Academic(String name, float cost, String picture, float lakeBonus, float width, float height, int intelegenceGain, int fatigueGain) {
        super(name, cost, picture, lakeBonus, width, height);
        this.intelegenceGain = intelegenceGain;
        this.fatigueGain = fatigueGain;
    }

    /**
     * Gets the building's type identifier.
     * @return "Academic" as building type
     */
    @Override
    public String getType() {
        return "Academic";
    }

    /**
     * Gets knowledge gain per study session.
     * @return Intelligence points added
     */
    public int getIntelligenceGain() {
        return intelegenceGain;
    }

    /**
     * Gets fatigue increase per study session.
     * @return Fatigue points added
     */
    public int getFatigueGain() {
        return fatigueGain;
    }
}
