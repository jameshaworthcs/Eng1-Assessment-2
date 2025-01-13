package com.UniSim.game.Settings;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameSettingsTest {

    @Test
    public void testResolutionSettings() {
        int width = GameSettings.getResolutionWidth();
        int height = GameSettings.getResolutionHeight();
        
        // Test that resolution values are positive
        assertTrue(width > 0, "Resolution width should be positive");
        assertTrue(height > 0, "Resolution height should be positive");
        
        // Test resolution aspect ratio
        double aspectRatio = (double) width / height;
        assertTrue(aspectRatio > 1.0, "Aspect ratio should be widescreen (> 1.0)");
    }

    @Test
    public void testValidResolutions() {
        int width = GameSettings.getResolutionWidth();
        int height = GameSettings.getResolutionHeight();
        
        // Common minimum resolution checks
        assertTrue(width >= 800, "Width should be at least 800 pixels");
        assertTrue(height >= 600, "Height should be at least 600 pixels");
        
        // Check if resolution is reasonable for modern displays
        assertTrue(width <= 3840, "Width should not exceed 4K resolution");
        assertTrue(height <= 2160, "Height should not exceed 4K resolution");
    }

    @Test
    public void testMusicVolume() {
        float volume = GameSettings.getMusicVolume();
        assertTrue(volume >= 0.0f && volume <= 1.0f, "Music volume should be between 0.0 and 1.0");
    }
} 