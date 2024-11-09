package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

public class Food extends Building {

    private int decreaseCurrency;
    private int decreaseFatigue;
    private int increaseSatisfaction;

    public Food(String name, float cost, String picture, float lakeBonus, float width, float height, int decreaseCurrency, int decreaseFatugue, int increaseSatisfaction) {
        super(name, cost, picture, lakeBonus, width, height);
        this.decreaseCurrency = decreaseCurrency;
        this.decreaseFatigue = decreaseFatugue;
        this.increaseSatisfaction = increaseSatisfaction;
    }

    public int getDecreaseCurrency() {
        return decreaseCurrency;
    }

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
