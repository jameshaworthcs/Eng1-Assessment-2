package com.UniSim.game.Stats;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Manages UI labels for displaying game statistics and messages.
 * Creates and positions text labels on the game screen.
 * Provides methods to update label contents dynamically.
 */
public class StatsLabels {
    private Body body;          // Physics body (if needed for positioning)
    private Table table;        // Layout container
    private Label label;        // Unused - consider removing
    private Skin skin;          // UI styling
    private World world;        // Physics world reference
    private Stage stage;        // UI stage
    private Label messageLabel; // Main display label

    /**
     * Creates a new stats label at specified position.
     * Places the label on the UI stage with given styling.
     *
     * @param world Game's physics world
     * @param stage UI stage for rendering
     * @param skin UI theme/styling
     * @param x X position on screen
     * @param y Y position on screen
     * @param labelText Initial text to display
     */
    public StatsLabels(World world, Stage stage, Skin skin, int x, int y, String labelText) {
        this.world = world;
        this.stage = stage;
        this.skin = skin;
        createStatsLabel(x, y, labelText);
    }

    /**
     * Sets up the label with initial position and text.
     * Adds it to the stage for rendering.
     *
     * @param x X position on screen
     * @param y Y position on screen
     * @param stat Text to display
     */
    private void createStatsLabel(int x, int y, String stat) {
        messageLabel = new Label(stat, skin);
        messageLabel.setPosition(x, y);
        messageLabel.setVisible(true);
        stage.addActor(messageLabel);
    }

    /**
     * Updates the label's displayed text.
     *
     * @param newText New text to display
     */
    public void setText(String newText) {
        messageLabel.setText(newText);
    }
}

