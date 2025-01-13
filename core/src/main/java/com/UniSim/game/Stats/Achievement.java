package com.UniSim.game.Stats;

/**
 * Represents an in-game achievement that players can unlock.
 * Each achievement has a name, description, and satisfaction bonus when unlocked.
 * Achievements are grouped into different types based on their unlock criteria.
 */
public class Achievement {
    private String name;              // Display name of the achievement
    private String description;       // Description of how to unlock it
    private boolean isUnlocked;       // Whether it's been unlocked
    private int satisfactionBonus;    // Bonus given when unlocked
    private AchievementType type;     // Category of achievement

    /** Categories of achievements based on gameplay aspects */
    public enum AchievementType {
        HIGH_SATISFACTION,    // Maintaining high student satisfaction
        BUILDING_COUNT,      // Building specific numbers of structures
        KNOWLEDGE_MASTER     // Reaching academic milestones
    }

    /**
     * Creates a new locked achievement.
     * 
     * @param name Display name shown to player
     * @param description How to unlock the achievement
     * @param satisfactionBonus Points awarded when unlocked
     * @param type Category of achievement
     */
    public Achievement(String name, String description, int satisfactionBonus, AchievementType type) {
        this.name = name;
        this.description = description;
        this.satisfactionBonus = satisfactionBonus;
        this.type = type;
        this.isUnlocked = false;
    }

    /** Gets the achievement's display name */
    public String getName() {
        return name;
    }

    /** Gets the achievement's unlock description */
    public String getDescription() {
        return description;
    }

    /** Checks if the achievement has been unlocked */
    public boolean isUnlocked() {
        return isUnlocked;
    }

    /** Unlocks the achievement */
    public void unlock() {
        this.isUnlocked = true;
    }

    /** Gets the satisfaction bonus awarded when unlocked */
    public int getSatisfactionBonus() {
        return satisfactionBonus;
    }

    /** Gets the achievement's category */
    public AchievementType getType() {
        return type;
    }
} 