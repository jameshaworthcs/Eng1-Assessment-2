package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

public class Workplace extends Building {

    private int increaseFatigue;
    private int increaseCurrency;

    public Workplace(String name, float cost, String picture, float lakeBonus, float width, float height, int increaseFatigue, int increaseCurrency) {
        super(name, cost, picture, lakeBonus, width, height);
        this.increaseFatigue = increaseFatigue;
        this.increaseCurrency = increaseCurrency;
    }

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
