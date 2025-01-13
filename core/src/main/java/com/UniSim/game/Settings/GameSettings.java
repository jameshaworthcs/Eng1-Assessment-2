package com.UniSim.game.Settings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Manages persistent game settings using LibGDX's Preferences API.
 * Handles:
 * - Audio volumes (music and sound effects)
 * - Display resolution
 * Settings are saved to local storage between sessions.
 */
public class GameSettings {
    // Preference keys for settings storage
    private static final String PREFS_NAME = "unisim_settings";           // Settings file name
    private static final String MUSIC_VOLUME_KEY = "music_volume";        // Background music volume
    private static final String SOUND_EFFECTS_VOLUME_KEY = "sound_effects_volume";  // SFX volume
    private static final String RESOLUTION_WIDTH_KEY = "resolution_width";          // Screen width
    private static final String RESOLUTION_HEIGHT_KEY = "resolution_height";        // Screen height

    /**
     * Gets the preferences instance for settings storage.
     * @return Preferences object for reading/writing settings
     */
    private static Preferences getPrefs() {
        return Gdx.app.getPreferences(PREFS_NAME);
    }

    /**
     * Sets the background music volume.
     * @param volume Volume level between 0 (mute) and 1 (full)
     */
    public static void setMusicVolume(float volume) {
        getPrefs().putFloat(MUSIC_VOLUME_KEY, volume);
        getPrefs().flush(); // Save changes immediately
    }

    /**
     * Gets the current music volume setting.
     * @return Volume level between 0 and 1, defaults to 1
     */
    public static float getMusicVolume() {
        return getPrefs().getFloat(MUSIC_VOLUME_KEY, 1.0f); // Default to full volume
    }

    /**
     * Sets the sound effects volume.
     * @param volume Volume level between 0 (mute) and 1 (full)
     */
    public static void setSoundEffectsVolume(float volume) {
        getPrefs().putFloat(SOUND_EFFECTS_VOLUME_KEY, volume);
        getPrefs().flush();
    }

    /**
     * Gets the current sound effects volume setting.
     * @return Volume level between 0 and 1, defaults to 1
     */
    public static float getSoundEffectsVolume() {
        return getPrefs().getFloat(SOUND_EFFECTS_VOLUME_KEY, 1.0f);
    }

    /**
     * Sets the display resolution.
     * @param width Screen width in pixels
     * @param height Screen height in pixels
     */
    public static void setResolution(int width, int height) {
        getPrefs().putInteger(RESOLUTION_WIDTH_KEY, width);
        getPrefs().putInteger(RESOLUTION_HEIGHT_KEY, height);
        getPrefs().flush();
    }

    /**
     * Gets the current resolution width setting.
     * @return Screen width in pixels, defaults to 2560
     */
    public static int getResolutionWidth() {
        return getPrefs().getInteger(RESOLUTION_WIDTH_KEY, 2560); // Default to 2560x1440
    }

    /**
     * Gets the current resolution height setting.
     * @return Screen height in pixels, defaults to 1440
     */
    public static int getResolutionHeight() {
        return getPrefs().getInteger(RESOLUTION_HEIGHT_KEY, 1440);
    }
} 