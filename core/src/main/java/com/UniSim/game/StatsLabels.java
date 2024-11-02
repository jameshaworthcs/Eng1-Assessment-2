package com.UniSim.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import static com.UniSim.game.Constants.PPM;

public class StatsLabels {
    private Body body;
    private Table table;
    private Label label;
    private Skin skin;
    private World world;
    private Stage stage;
    private Label messageLabel;

    public StatsLabels(World world, Stage stage, Skin skin, int x, int y, String labelText) {

        this.world = world;
        this.stage = stage;
        this.skin = skin;

        createStatsLabel(x, y, labelText);
    }

    private void createStatsLabel(int x, int y, String stat) {
        messageLabel = new Label(stat, skin);
        messageLabel.setPosition(x, y); // Set the position for the message
        messageLabel.setVisible(true); // Initially hidden
        stage.addActor(messageLabel); // Add the label to the stage
    }
}

