package com.UniSim.game.Stats;

import java.util.ArrayList;
import java.util.List;

import com.UniSim.game.Hud;

/**
 * The AchievementManager class manages all achievements in the game,
 * tracks their progress, and handles unlocking achievements.
 */
public class AchievementManager {
    private List<Achievement> achievements;
    private Hud hud;
    private int highSatisfactionTimer; // Tracks time spent with high satisfaction

    public AchievementManager(Hud hud) {
        this.hud = hud;
        this.achievements = new ArrayList<>();
        this.highSatisfactionTimer = 0;
        initializeAchievements();
    }

    private void initializeAchievements() {
        // High satisfaction achievement
        achievements.add(new Achievement(
            "I Heart Uni",
            "Maintain satisfaction above 80% for 3 minutes",
            50,
            Achievement.AchievementType.HIGH_SATISFACTION
        ));

        // Building count achievements
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

        // Knowledge achievement
        achievements.add(new Achievement(
            "Knowledge Master",
            "Reach a knowledge score of 20 or higher",
            50,
            Achievement.AchievementType.KNOWLEDGE_MASTER
        ));
    }

    public void update(float dt) {
        PlayerStats stats = hud.getStats();
        
        // Check high satisfaction achievement
        if (stats.getSatisfaction() >= 80) {
            highSatisfactionTimer += dt;
            if (highSatisfactionTimer >= 180) { // 3 minutes = 180 seconds
                unlockAchievement("I Heart Uni");
            }
        } else {
            highSatisfactionTimer = 0;
        }

        // Check building count achievements
        if (stats.getBuildingCounter() >= 5) {
            unlockAchievement("Minimalist");
        }
        if (stats.getBuildingCounter() >= 20) { // Assuming 20 is max buildings
            unlockAchievement("Jam Packed");
        }

        // Check knowledge achievement
        if (stats.getKnowledge() >= 20) {
            unlockAchievement("Knowledge Master");
        }
    }

    private void unlockAchievement(String name) {
        for (Achievement achievement : achievements) {
            if (achievement.getName().equals(name) && !achievement.isUnlocked()) {
                achievement.unlock();
                hud.sendMessage("Achievement Unlocked: " + name + "\n" + achievement.getDescription());
                break;
            }
        }
    }

    public List<Achievement> getUnlockedAchievements() {
        List<Achievement> unlockedAchievements = new ArrayList<>();
        for (Achievement achievement : achievements) {
            if (achievement.isUnlocked()) {
                unlockedAchievements.add(achievement);
            }
        }
        return unlockedAchievements;
    }

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