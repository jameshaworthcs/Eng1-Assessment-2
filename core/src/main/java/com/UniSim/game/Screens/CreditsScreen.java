package com.UniSim.game.Screens;

import com.UniSim.game.UniSim;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class CreditsScreen implements Screen {
    private UniSim game;
    private Stage stage;
    private Skin skin;
    private Music music;
    private LandingScreen landingScreen;
    private Texture backgroundTexture;

    // Constructor for CreditsScreen accessed from LandingScreen
    public CreditsScreen(UniSim game, LandingScreen landingScreen, Music music) {
        this.game = game;
        this.landingScreen = landingScreen;
        this.music = music;
        this.stage = new Stage(new ScreenViewport());
        this.skin = new Skin(Gdx.files.internal("uiskin.json")); // Load the skin for UI elements
        Gdx.input.setInputProcessor(stage);  // Set input processor to this screen's stage

        // Initialize UI elements
        initializeUI();
    }

    // UI initialization logic in a separate method
    private void initializeUI() {
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
                game.setScreen(landingScreen); // Return to LandingScreen
                Gdx.input.setInputProcessor(landingScreen.getStage());  // Reset input processor for LandingScreen
            }
        });

        // Credits text
        Label creditsLabel = new Label("Credits:\n\n" +
            "WRITE CREDITS HERE", skin);
        creditsLabel.setWrap(true); // Enable text wrapping

        // Add elements to the table
        table.add(backButton).pad(10).top().left(); // Position back button at the top left
        table.row();
        table.add(creditsLabel).expand().fill(); // Fill the rest of the screen with credits
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);  // Set a background color for the Credits screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose(); // Dispose the skin
    }
}