package com.UniSim.game.Events;

import com.UniSim.game.Screens.SettingsScreen;
import com.UniSim.game.Stats.PlayerStats;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * The Event class represents an event that can occur in the game.
 * Events can affect the player's stats, trigger other events, or change the
 * game state.
 */
public class Event {
    public enum EventType {
        POSITIVE, NEGATIVE, NEUTRAL
    }

    private EventType type;
    private String description;
    private int scoreEffect;
    private int moneyEffect;
    private Sound eventSound;

    /**
     * Constructor for the Event class.
     *
     * @param type          The type of the event (positive, negative, neutral).
     * @param description   The description of the event.
     * @param scoreEffect   The effect on the player's score.
     * @param moneyEffect   The effect on the player's money.
     * @param soundFilePath The file path to the sound associated with the event.
     */
    public Event(EventType type, String description, int scoreEffect, int moneyEffect, String soundFilePath) {
        this.type = type;
        this.description = description;
        this.scoreEffect = scoreEffect;
        this.moneyEffect = moneyEffect;
        this.eventSound = Gdx.audio.newSound(Gdx.files.internal(soundFilePath));
    }

    public EventType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getScoreEffect() {
        return scoreEffect;
    }

    public float getMoneyEffect() {
        return moneyEffect;
    }

    public void applyEffects(PlayerStats playerStats) {
        playerStats.increaseSatisfaction(scoreEffect);
        playerStats.increaseCurrency(moneyEffect);
    }

    public void playSound() {
        float soundEffectsVolume = (float) (SettingsScreen.getSoundEffectsVolume() * 0.2);
        eventSound.play(soundEffectsVolume);
    }
}
