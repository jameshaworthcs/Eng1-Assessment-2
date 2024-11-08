package com.UniSim.game.Screens;

import com.UniSim.game.UniSim;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LandingScreen implements Screen {
    private UniSim game;  // Reference to the main game
    private Texture backgroundTexture;
    private Label.LabelStyle labelStyle;
    private Label.LabelStyle titleLabelStyle;

    private Stage stage;
    private Skin skin;


    private BitmapFont font; // Add a BitmapFont
    private BitmapFont titleFont; // Add a BitmapFont

    // Sample leaderboard times for demonstration (replace this with actual data)
    private List<Float> leaderboardTimes = List.of(15.3f, 10.5f, 20.7f, 12.0f, 8.5f, 11.2f, 14.0f, 9.1f, 17.5f, 13.3f, 19.8f, 7.4f);

    public LandingScreen(UniSim game) {
        this.game = game;
        //stage = new Stage(new ScreenViewport());
        stage = new Stage(new FitViewport(2560, 1440));  // Use FitViewport here
        Gdx.input.setInputProcessor(stage);  // Set the input processor to the stage
        System.out.println(Gdx.graphics.getWidth());
        System.out.println(Gdx.graphics.getHeight());


        // Load skin for UI elements
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        backgroundTexture = new Texture("LoadScreenBackground.png");
        //font = new BitmapFont(); // You may want to load a specific font file here

        font = new BitmapFont(Gdx.files.internal("Font1.fnt"));
        font.getData().setScale(0.6f);
        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        titleFont = new BitmapFont(Gdx.files.internal("titleFont.fnt"));
        titleFont.getData().setScale(1.2f);
        titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = titleFont;




        //font.getData().setScale(0.02F);


        // Update font size for leaderboard

        skin.getFont("default-font").getData().setScale(2); // Use the font from the skin

        // Load the logo image (this is the "UniSim" image)


        // Create buttons
        TextButton playGameButton = new TextButton("Play Game", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton howToPlayButton = new TextButton("How to Play", skin);
        TextButton creditsButton = new TextButton("Credits", skin);
        TextButton quitButton = new TextButton("Quit", skin);

        // Set up button listeners
        playGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));  // Switch to game screen
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game));  // Switch to settings screen
            }
        });

        howToPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HowToPlayScreen(game));  // Switch to how to play screen
            }
        });

        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CreditsScreen(game));  // Switch to credits screen
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Create a layout table for the main UI
        Table mainTable = new Table();
        //mainTable.setFillParent(true);
        //mainTable.left();  // Align table to the left

        // Add logo image and buttons to the main table
        //logoImage.setSize(1000, 200);  // Set width and height
        //mainTable.add(logoImage).expandX().padTop(10).row();
        //mainTable.add(playGameButton).size(500, 140).padBottom(10).row();
        //mainTable.add(settingsButton).size(500, 140).padBottom(10).row();
        //mainTable.add(howToPlayButton).size(500, 140).padBottom(10).row();
        //mainTable.add(creditsButton).size(500, 140).padBottom(10).row();

        // Create leaderboard table
        // Create leaderboard table
        Table leaderboardTable = new Table();
        leaderboardTable.top().left(); // Align the leaderboard to the top-left

// Sort and limit the leaderboard times to the top ten
        List<Float> sortedTimes = new ArrayList<>(leaderboardTimes);
        sortedTimes.sort(Comparator.naturalOrder());
        int limit = Math.min(10, sortedTimes.size()); // Limit to 10 entries

// Add header row with ranking, time, and set styles

        Label rankHeader = new Label("Rank", skin);
        Label timeHeader = new Label("Time", skin);

        leaderboardTable.add(rankHeader).pad(5).left();
        leaderboardTable.add(timeHeader).pad(5).expandX().center();
        leaderboardTable.row();

// Add dividing line after header
        //leaderboardTable.add(new Label("---------", skin)).colspan(2).center().row();

        for (int i = 0; i < limit; i++) {
            // Add ranking
            Label rankLabel = new Label((i + 1) + ".", skin); // Rank starts from 1
            Label timeLabel = new Label(String.format("%.1f seconds", sortedTimes.get(i)), skin);

            // Style the labels for color and padding
            rankLabel.setColor(0, 1f, 0.5f, 1);
            timeLabel.setColor(0, 1f, 0.5f, 1);

            // Add rank and time labels to the row with padding for separation
            leaderboardTable.add(rankLabel).pad(5).left();
            leaderboardTable.add(timeLabel).pad(5).expandX().center();
            leaderboardTable.row();

            // Add dividing line after each entry for clarity
            //leaderboardTable.add(new Label("---------", skin)).colspan(2).center().row();
        }

// Create a ScrollPane for the leaderboard
        ScrollPane scrollPane = new ScrollPane(leaderboardTable, skin);
        scrollPane.setSize(1070, 580); // Set the size of the scroll pane
        scrollPane.setPosition(800, 250); // Position it on the right side of the screen
        scrollPane.setScrollBarPositions(false, true); // Show scrollbar on the right side
        scrollPane.setFadeScrollBars(false); // Always show scrollbars

// Add header for the leaderboard outside the scrollable part

        Label headerLabel = new Label("Leaderboard", labelStyle);
        headerLabel.setColor(0, 0.5f, 1, 1); // Set the header color (e.g., a shade of blue)
        headerLabel.setPosition(800, 850); // Position the header above the scroll pane
        stage.addActor(headerLabel); // Add header to the stage

        // Add all actors to the stage
        // Add logo image and buttons to the table
        // Set size and position of the logo image
        //logoImage.setSize(1000, 200);  // Set width and height
        //logoImage.setPosition(50, 1130);  // Set x, y position

        Label gameTitle = new Label("UniSim", titleLabelStyle);
        //gameTitle.setColor(0, 0.5f, 1, 1); // Set the header color (e.g., a shade of blue)
        gameTitle.setPosition(50, 1100); // Position the header above the scroll pane
        stage.addActor(gameTitle);

        // Set size and position for Play Game button
        playGameButton.setSize(500, 140);  // Set width and height
        playGameButton.setPosition(50, 850);  // Set x, y position

        // Set size and position for Settings button
        settingsButton.setSize(500, 140);
        settingsButton.setPosition(50, 700);

        // Set size and position for How to Play button
        howToPlayButton.setSize(500, 140);
        howToPlayButton.setPosition(50, 550);

        // Set size and position for Credits button
        creditsButton.setSize(500, 140);
        creditsButton.setPosition(50, 400);

        //set size and position for Quit button
        quitButton.setSize(500, 140);
        quitButton.setPosition(50, 250);

        // Add all actors to the stage

        stage.addActor(playGameButton);
        stage.addActor(settingsButton);
        stage.addActor(howToPlayButton);
        stage.addActor(creditsButton);
        stage.addActor(quitButton);
        stage.addActor(scrollPane); // Add the scrollable leaderboard

        // Add main UI table to the stage
        stage.addActor(mainTable);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(1, 1, 1, 1); // Set clear color (white in this case)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clear the color buffer

        // Draw the background texture
        SpriteBatch batch = new SpriteBatch();
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        // Draw the stage (which includes all UI elements)
        stage.act(delta);
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
        skin.dispose();
        //uniSimLogo.dispose();
        backgroundTexture.dispose();
    }
}
