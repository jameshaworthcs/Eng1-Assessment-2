package com.UniSim.game.Events;

import com.UniSim.game.Screens.SettingsScreen;
import com.UniSim.game.Stats.PlayerStats;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Represents a game event that can affect player stats and trigger sound effects.
 * Events can be positive (beneficial), negative (detrimental), or neutral.
 * Each event has effects on player satisfaction and currency, along with an associated sound.
 */
public class Event {
    /** Types of events and their general impact on gameplay */
    public enum EventType {
        POSITIVE,   // Beneficial events that help the player
        NEGATIVE,   // Harmful events that challenge the player
        NEUTRAL     // Events with balanced or minimal impact
    }

    private EventType type;
    private String description;
    private int scoreEffect;      // Impact on player satisfaction
    private int moneyEffect;      // Impact on player currency
    private Sound eventSound;     // Sound played when event occurs

    /**
     * Creates a new event with specified effects and associated sound.
     * 
     * @param type Type of event (positive/negative/neutral)
     * @param description Text describing the event
     * @param scoreEffect Change to player satisfaction
     * @param moneyEffect Change to player currency
     * @param soundFilePath Path to the event's sound file
     */
    public Event(EventType type, String description, int scoreEffect, int moneyEffect, String soundFilePath) {
        this.type = type;
        this.description = description;
        this.scoreEffect = scoreEffect;
        this.moneyEffect = moneyEffect;
        this.eventSound = Gdx.audio.newSound(Gdx.files.internal(soundFilePath));
    }

    /** Gets the event's type */
    public EventType getType() {
        return type;
    }

    /** Gets the event's description text */
    public String getDescription() {
        return description;
    }

    /** Gets the event's effect on player satisfaction */
    public int getScoreEffect() {
        return scoreEffect;
    }

    /** Gets the event's effect on player currency */
    public float getMoneyEffect() {
        return moneyEffect;
    }

    /**
     * Applies this event's effects to the player's stats.
     * Updates both satisfaction and currency values.
     */
    public void applyEffects(PlayerStats playerStats) {
        playerStats.increaseSatisfaction(scoreEffect);
        playerStats.increaseCurrency(moneyEffect);
    }

    /**
     * Plays the event's sound effect at the appropriate volume.
     * Volume is scaled based on user's sound effect settings.
     */
    public void playSound() {
        float soundEffectsVolume = (float) (SettingsScreen.getSoundEffectsVolume() * 0.2);
        eventSound.play(soundEffectsVolume);
    }
}
