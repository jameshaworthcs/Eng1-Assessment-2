package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

public class Recreational extends Building {

    public Recreational(String name, float cost, String picture, float lakeBonus, float width, float height) {
        super(name, cost, picture, lakeBonus, width, height);
    }

    @Override
    public String getType(){
        return "Recreational";
    }
}
