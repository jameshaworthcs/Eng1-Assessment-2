package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

public class Accommodation extends Building {

    private int fatigueDecrease;

    public Accommodation(String name, float cost, String picture, float lakeBonus, float width, float height, int fatigueDecrease) {
        super(name, cost, picture, lakeBonus, width, height);
        this.fatigueDecrease = fatigueDecrease;
    }

    @Override
    public String getType(){
        return "Accommodation";
    }

    public int getFatigueDecrease() {
        return fatigueDecrease;
    }
}
