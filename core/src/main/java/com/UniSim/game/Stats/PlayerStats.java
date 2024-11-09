package com.UniSim.game.Stats;

public class PlayerStats {

    private int buildingCounter;
    private int satisfaction;
    private float currency;
    private int fatigue;
    private int knowledge;

    public PlayerStats(){
        this.buildingCounter = 0;
        satisfaction = 0;
        currency = 10000;
        fatigue = 0;
        knowledge = 0;
    }

    //get stats
    public String getSatisfaction(){
        return Integer.toString(satisfaction);
    }

    public float getCurrency(){
        return currency;
    }

    public int getFatigue(){
        return fatigue;
    }

    public int getKnowledge(){
        return knowledge;
    }

    public int getBuildingCounter(){return buildingCounter;}

    //change stats
    public void increaseSatisfaction(int amount){
        satisfaction = satisfaction + amount;
    }

    public void decreaseSatisfaction(int amount){
        satisfaction = satisfaction - amount;
    }

    public void increaseCurrency(int amount){
        currency = currency + amount;
    }

    public void decreaseCurrency(int amount){
        currency = currency - amount;
    }

    public void increaseFatigue(int amount){
        fatigue = fatigue + amount;
    }

    public void decreaseFatigue(int amount){
        fatigue = fatigue - amount;
    }

    //assume knowledge doesn't decrease
    public void increaseKnowledge(int amount){
        knowledge = knowledge + amount;
    }

    public void incrementBuildingCounter(){
        buildingCounter++;
    }

    private float calculateSatisfaction(){
        return 1f;
    }

    public void takeOffBuildingCost(float currency){
        this.currency -= currency;
    }


}
