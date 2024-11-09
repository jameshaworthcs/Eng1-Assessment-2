package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

public class Recreational extends Building {

    private int decreaseCurrency;
    private int decreaseFatigue;
    private int increaseSatisfaction;

    public Recreational(String name, float cost, String picture, float lakeBonus, float width, float height, int decreaseCurrency, int decreaseFatigue, int increaseSatisfaction) {
        super(name, cost, picture, lakeBonus, width, height);
        this.decreaseCurrency = decreaseCurrency;
        this.decreaseFatigue = decreaseFatigue;
        this.increaseSatisfaction = increaseSatisfaction;
    }

    @Override
    public String getType(){
        return "Recreational";
    }

    public int getDecreaseCurrency() {
        return decreaseCurrency;
    }

    public int getDecreaseFatigue() {
        return decreaseFatigue;
    }

    public int getIncreaseSatisfaction() {
        return increaseSatisfaction;
    }
}
