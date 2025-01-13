package com.UniSim.game;

import com.UniSim.game.Screens.LandingScreen;
import com.UniSim.game.Settings.GameSettings;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Main entry point for the UniSim game.
 * Handles core game initialization and lifecycle:
 * - Sets up rendering pipeline
 * - Configures display resolution
 * - Manages screen transitions
 * - Handles resource cleanup
 */
public class UniSim extends Game {
    /** Shared sprite batch for efficient rendering */
    public SpriteBatch batch;

    /**
     * Initializes the game on startup.
     * Creates rendering resources and sets initial screen.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        
        int width = GameSettings.getResolutionWidth();
        int height = GameSettings.getResolutionHeight();
        Gdx.graphics.setWindowedMode(width, height);

        setScreen(new LandingScreen(this));
    }

    /**
     * Main game loop.
     * Delegates rendering to active screen.
     */
    @Override
    public void render() {
        super.render();
    }

    /**
     * Cleans up resources when game closes.
     * Ensures proper disposal of rendering assets.
     */
    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}
