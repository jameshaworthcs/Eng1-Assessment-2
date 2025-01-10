package com.UniSim.game.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * The GameSettings class manages game settings persistence using LibGDX's Preferences API.
 * Settings are saved to local storage and can be loaded between game sessions.
 */
public class GameSettings {
    private static final String PREFS_NAME = "unisim_settings";
    private static final String MUSIC_VOLUME_KEY = "music_volume";
    private static final String SOUND_EFFECTS_VOLUME_KEY = "sound_effects_volume";
    private static final String RESOLUTION_WIDTH_KEY = "resolution_width";
    private static final String RESOLUTION_HEIGHT_KEY = "resolution_height";

    private static Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    // Music volume
    public static void setMusicVolume(float volume) {
        getPrefs().putFloat(MUSIC_VOLUME_KEY, volume);
        getPrefs().flush(); // Save changes immediately
    }

    public static float getMusicVolume() {
        return getPrefs().getFloat(MUSIC_VOLUME_KEY, 1.0f); // Default to full volume
    }

    // Sound effects volume
    public static void setSoundEffectsVolume(float volume) {
        getPrefs().putFloat(SOUND_EFFECTS_VOLUME_KEY, volume);
        getPrefs().flush();
    }

    public static float getSoundEffectsVolume() {
        return getPrefs().getFloat(SOUND_EFFECTS_VOLUME_KEY, 1.0f);
    }

    // Resolution
    public static void setResolution(int width, int height) {
        getPrefs().putInteger(RESOLUTION_WIDTH_KEY, width);
        getPrefs().putInteger(RESOLUTION_HEIGHT_KEY, height);
        getPrefs().flush();
    }

    public static int getResolutionWidth() {
        return getPrefs().getInteger(RESOLUTION_WIDTH_KEY, 2560); // Default to 2560x1440
    }

    public static int getResolutionHeight() {
        return getPrefs().getInteger(RESOLUTION_HEIGHT_KEY, 1440);
    }
} 