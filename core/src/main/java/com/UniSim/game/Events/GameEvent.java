package com.UniSim.game.Events;

public class GameEvent {
    private String name;
    private String description;
    private int impact;
    private boolean active;

    public GameEvent(String name, String description, int impact) {
        this.name = name;
        this.description = description;
        this.impact = impact;
        this.active = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getImpact() {
        return impact;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
} 