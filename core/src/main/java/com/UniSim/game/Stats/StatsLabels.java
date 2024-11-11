package com.UniSim.game.Stats;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * The StatsLabels class is responsible for creating and managing on-screen labels that display various game stats or messages.
 * It allows the creation of a label with customizable text and position, which can be updated during gameplay.
 */
public class StatsLabels {
    private Body body;
    private Table table;
    private Label label;
    private Skin skin;
    private World world;
    private Stage stage;
    private Label messageLabel;

    /**
     * Constructor that initializes the StatsLabels object, creating a label with specified text at a given position.
     *
     * @param world The game world
     * @param stage The stage where the label will be added
     * @param skin The skin used to style the label
     * @param x The x-coordinate of the label's position
     * @param y The y-coordinate of the label's position
     * @param labelText The text to be displayed in the label
     */
    public StatsLabels(World world, Stage stage, Skin skin, int x, int y, String labelText) {

        this.world = world;
        this.stage = stage;
        this.skin = skin;

        createStatsLabel(x, y, labelText);
    }

    /**
     * Creates the label and adds it to the stage.
     * Initially, the label is set to be visible, and its position is determined by the provided coordinates.
     *
     * @param x The x-coordinate of the label's position
     * @param y The y-coordinate of the label's position
     * @param stat The text to be displayed in the label
     */
    private void createStatsLabel(int x, int y, String stat) {
        messageLabel = new Label(stat, skin);
        messageLabel.setPosition(x, y); // Set the position for the message
        messageLabel.setVisible(true); // Initially hidden
        stage.addActor(messageLabel); // Add the label to the stage
    }

    /**
     * Sets the text of the label to the specified new text.
     *
     * @param newText The new text to be displayed in the label
     */
    public void setText(String newText) {
        messageLabel.setText(newText);
    }
}

