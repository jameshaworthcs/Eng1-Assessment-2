package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

public class Academic extends Building {

    private int intelegence;

    public Academic(String name, float cost, String picture, float lakeBonus, float width, float height, int intelegence) {
        super(name, cost, picture, lakeBonus, width, height);
        this.intelegence = intelegence;
    }
}
