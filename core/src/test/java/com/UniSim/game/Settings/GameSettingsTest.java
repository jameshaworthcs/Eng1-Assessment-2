package com.UniSim.game.Settings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for GameSettings functionality.
 * Verifies resolution settings, aspect ratios, and volume controls.
 */
public class GameSettingsTest {

    /**
     * Tests basic resolution settings and aspect ratio.
     * Ensures width and height are positive and maintain widescreen ratio.
     */
    @Test
    public void testResolutionSettings() {
        int width = GameSettings.getResolutionWidth();
        int height = GameSettings.getResolutionHeight();
        
        assertTrue(width > 0, "Resolution width should be positive");
        assertTrue(height > 0, "Resolution height should be positive");
        
        double aspectRatio = (double) width / height;
        assertTrue(aspectRatio > 1.0, "Aspect ratio should be widescreen (> 1.0)");
    }

    /**
     * Tests that resolution settings are within valid ranges.
     * Checks against minimum required resolution and maximum supported resolution.
     */
    @Test
    public void testValidResolutions() {
        int width = GameSettings.getResolutionWidth();
        int height = GameSettings.getResolutionHeight();
        
        assertTrue(width >= 800, "Width should be at least 800 pixels");
        assertTrue(height >= 600, "Height should be at least 600 pixels");
        
        assertTrue(width <= 3840, "Width should not exceed 4K resolution");
        assertTrue(height <= 2160, "Height should not exceed 4K resolution");
    }

    /**
     * Tests that music volume settings are within valid range.
     * Volume should be between 0.0 (muted) and 1.0 (maximum).
     */
    @Test
    public void testMusicVolume() {
        float volume = GameSettings.getMusicVolume();
        assertTrue(volume >= 0.0f && volume <= 1.0f, "Music volume should be between 0.0 and 1.0");
    }
} 