package com.UniSim.game.Stats;

import java.util.ArrayList;
import java.util.List;

import com.UniSim.game.Hud;

/**
 * Manages the game's achievement system.
 * Tracks progress towards achievements, handles unlocking them,
 * and calculates achievement-based bonuses.
 */
public class AchievementManager {
    private List<Achievement> achievements;         // All available achievements
    private Hud hud;                               // For displaying notifications
    private int highSatisfactionTimer;             // Time with satisfaction > 80%

    /**
     * Creates a new achievement manager and sets up all achievements.
     * 
     * @param hud HUD for displaying achievement notifications
     */
    public AchievementManager(Hud hud) {
        this.hud = hud;
        this.achievements = new ArrayList<>();
        this.highSatisfactionTimer = 0;
        initializeAchievements();
    }

    /**
     * Sets up all available achievements in the game.
     * Defines names, descriptions, and unlock criteria.
     */
    private void initializeAchievements() {
        // Satisfaction-based achievement
        achievements.add(new Achievement(
            "I Heart Uni",
            "Maintain satisfaction above 80% for 3 minutes",
            50,
            Achievement.AchievementType.HIGH_SATISFACTION
        ));

        // Building milestones
        achievements.add(new Achievement(
            "Minimalist",
            "Place a total of five buildings",
            30,
            Achievement.AchievementType.BUILDING_COUNT
        ));

        achievements.add(new Achievement(
            "Jam Packed",
            "Place the maximum number of buildings",
            100,
            Achievement.AchievementType.BUILDING_COUNT
        ));

        // Academic progress
        achievements.add(new Achievement(
            "Knowledge Master",
            "Reach a knowledge score of 20 or higher",
            50,
            Achievement.AchievementType.KNOWLEDGE_MASTER
        ));
    }

    /**
     * Updates achievement progress based on current game state.
     * Checks various stats and unlocks achievements when criteria are met.
     * 
     * @param dt Time elapsed since last update
     */
    public void update(float dt) {
        PlayerStats stats = hud.getStats();
        
        // Track time with high satisfaction
        if (stats.getSatisfaction() >= 80) {
            highSatisfactionTimer += dt;
            if (highSatisfactionTimer >= 180) {  // 3 minutes
                unlockAchievement("I Heart Uni");
            }
        } else {
            highSatisfactionTimer = 0;
        }

        // Check building milestones
        if (stats.getBuildingCounter() >= 5) {
            unlockAchievement("Minimalist");
        }
        if (stats.getBuildingCounter() >= 20) {  // Max buildings
            unlockAchievement("Jam Packed");
        }

        // Check academic progress
        if (stats.getKnowledge() >= 20) {
            unlockAchievement("Knowledge Master");
        }
    }

    /**
     * Unlocks an achievement and shows a notification.
     * Only unlocks if the achievement exists and isn't already unlocked.
     * 
     * @param name Name of the achievement to unlock
     */
    private void unlockAchievement(String name) {
        for (Achievement achievement : achievements) {
            if (achievement.getName().equals(name) && !achievement.isUnlocked()) {
                achievement.unlock();
                hud.sendMessage("Achievement Unlocked: " + name + "\n" + achievement.getDescription());
                break;
            }
        }
    }

    /**
     * Gets a list of all unlocked achievements.
     * Used for displaying achievement progress to the player.
     */
    public List<Achievement> getUnlockedAchievements() {
        List<Achievement> unlockedAchievements = new ArrayList<>();
        for (Achievement achievement : achievements) {
            if (achievement.isUnlocked()) {
                unlockedAchievements.add(achievement);
            }
        }
        return unlockedAchievements;
    }

    /**
     * Calculates total satisfaction bonus from unlocked achievements.
     * Used to boost player satisfaction based on achievement progress.
     */
    public int calculateAchievementBonus() {
        int totalBonus = 0;
        for (Achievement achievement : achievements) {
            if (achievement.isUnlocked()) {
                totalBonus += achievement.getSatisfactionBonus();
            }
        }
        return totalBonus;
    }
} 