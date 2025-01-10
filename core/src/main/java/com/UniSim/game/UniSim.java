package com.UniSim.game;

import com.UniSim.game.Screens.LandingScreen;
import com.UniSim.game.Settings.GameSettings;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The UniSim class is the main game class that extends LibGDX's Game class.
 * It initializes the game and manages screen transitions.
 */
public class UniSim extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        
        // Load and apply saved settings
        int width = GameSettings.getResolutionWidth();
        int height = GameSettings.getResolutionHeight();
        Gdx.graphics.setWindowedMode(width, height);

        // Set initial screen
        setScreen(new LandingScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        super.dispose();
    }
}
