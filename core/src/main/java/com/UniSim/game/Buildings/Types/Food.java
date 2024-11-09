package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

public class Food extends Building {
    public Food(String name, float cost, String picture, float lakeBonus, float width, float height) {
        super(name, cost, picture, lakeBonus, width, height);
    }

    @Override
    public String getType(){
        return "Food";
    }
}
