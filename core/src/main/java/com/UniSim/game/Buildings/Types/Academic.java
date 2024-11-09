package com.UniSim.game.Buildings.Types;

import com.UniSim.game.Buildings.Building;

public class Academic extends Building {

    private int intelegenceGain;
    private int fatigueGain;

    public Academic(String name, float cost, String picture, float lakeBonus, float width, float height, int intelegenceGain, int fatigueGain) {
        super(name, cost, picture, lakeBonus, width, height);
        this.intelegenceGain = intelegenceGain;
        this.fatigueGain = fatigueGain;
    }

    @Override
    public String getType(){
        return "Academic";
    }

    public int getIntelligenceGain() {
        return intelegenceGain;
    }

    public int getFatigueGain() {
        return fatigueGain;
    }
}
