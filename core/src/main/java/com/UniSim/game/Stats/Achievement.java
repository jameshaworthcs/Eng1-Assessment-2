package com.UniSim.game.Stats;

/**
 * The Achievement class represents game achievements that players can earn
 * during gameplay. Each achievement has specific criteria and rewards.
 */
public class Achievement {
    private String name;
    private String description;
    private boolean isUnlocked;
    private int satisfactionBonus;
    private AchievementType type;

    public enum AchievementType {
        HIGH_SATISFACTION,    // For maintaining high satisfaction
        BUILDING_COUNT,      // For building-related achievements
        KNOWLEDGE_MASTER     // For knowledge-related achievements
    }

    public Achievement(String name, String description, int satisfactionBonus, AchievementType type) {
        this.name = name;
        this.description = description;
        this.satisfactionBonus = satisfactionBonus;
        this.type = type;
        this.isUnlocked = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void unlock() {
        this.isUnlocked = true;
    }

    public int getSatisfactionBonus() {
        return satisfactionBonus;
    }

    public AchievementType getType() {
        return type;
    }
} 