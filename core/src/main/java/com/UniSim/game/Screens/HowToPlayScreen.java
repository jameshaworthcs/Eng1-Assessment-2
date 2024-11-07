package com.UniSim.game.Screens;

import com.UniSim.game.UniSim;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class HowToPlayScreen implements Screen {
    private UniSim game;
    private Stage stage;
    private Skin skin;

    public HowToPlayScreen(UniSim game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json")); // Load the skin for UI elements
        Gdx.input.setInputProcessor(stage);

        // Create a table to organize UI elements
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Back button
        Button backButton = new Button(skin);
        backButton.add(new Label("Back", skin)); // Add label to the button
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LandingScreen(game)); // Go back to the settings screen or any other screen
            }
        });

        // Large text box with instructions
        Label instructionsLabel = new Label("How to Play:\n\n" +
            "1. Start by selecting a game mode.\n" +
            "2. Follow the on-screen instructions to navigate through the game.\n" +
            "3. Use the mouse or keyboard to interact with various elements.\n" +
            "4. Manage resources effectively to succeed.\n" +
            "5. Enjoy and explore the university simulation experience!", skin);
        instructionsLabel.setWrap(true); // Enable text wrapping

        // Add elements to the table
        table.add(backButton).pad(10).top().left(); // Position back button at the top left
        table.row();
        table.add(instructionsLabel).expand().fill(); // Fill the rest of the screen with instructions

    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);  // Set a background color for the How to Play screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose(); // Dispose the skin
    }
}
