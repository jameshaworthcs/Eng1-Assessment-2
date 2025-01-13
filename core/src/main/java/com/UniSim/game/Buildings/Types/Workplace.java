package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

/**
 * Workplace building for earning money.
 * Effects when used:
 * - Earns currency through work
 * - Increases fatigue from labor
 * Examples: Part-time jobs, campus work
 */
public class Workplace extends Building {
    // Work effects
    private int increaseFatigue;   // Energy cost per shift
    private int increaseCurrency;  // Money earned per shift

    /**
     * Creates a new workplace building.
     * Configures work rewards and costs.
     *
     * @param name Building's display name
     * @param cost Construction cost in currency
     * @param picture Texture file path
     * @param lakeBonus Bonus for lake proximity
     * @param width Building width in units
     * @param height Building height in units
     * @param increaseFatigue Energy cost per shift
     * @param increaseCurrency Money earned per shift
     */
    public Workplace(String name, float cost, String picture, float lakeBonus, float width, float height, 
                    int increaseFatigue, int increaseCurrency) {
        super(name, cost, picture, lakeBonus, width, height);
        this.increaseFatigue = increaseFatigue;
        this.increaseCurrency = increaseCurrency;
    }

    /**
     * Gets the building's type identifier.
     * @return "Workplace" as building type
     */
    @Override
    public String getType() {
        return "Workplace";
    }

    /**
     * Gets energy cost of working.
     * @return Fatigue points added
     */
    public int getIncreaseFatigue() {
        return increaseFatigue;
    }

    /**
     * Gets money earned from work.
     * @return Currency points added
     */
    public int getIncreaseCurrency() {
        return increaseCurrency;
    }
}
