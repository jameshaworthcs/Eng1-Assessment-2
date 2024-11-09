package com.UniSim.game.Screens;

import com.UniSim.game.PauseMenu;
import com.UniSim.game.UniSim;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class HowToPlayScreen implements Screen {
    private UniSim game;
    private Stage stage;
    private Skin skin;

    private Texture backgroundTexture;

    private LandingScreen landingScreen; // Reference to LandingScreen for returning
    private PauseMenu pauseMenu;         // Reference to PauseMenu for returning
    private Music music;

    // Constructor for HowToPlayScreen accessed from LandingScreen
    public HowToPlayScreen(UniSim game, LandingScreen landingScreen, Music music) {
        this.game = game;
        this.landingScreen = landingScreen;
        this.music = music;
        initialize();
    }

  

    // Constructor for HowToPlayScreen accessed from PauseMenu
    public HowToPlayScreen(UniSim game, PauseMenu pauseMenu, Music music) {
        this.game = game;
        this.pauseMenu = pauseMenu;
        this.music = music;
        initialize();
    }

    // Common initialization method
    private void initialize() {
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture("LoadScreenBackground.png");

        // Create a table to organize UI elements
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Back button
        Button backButton = new Button(skin);
        backButton.add(new Label("Back", skin));
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (pauseMenu != null) {
                    pauseMenu.returnToPauseMenu();  // Return to PauseMenu
                    Gdx.input.setInputProcessor(pauseMenu.getStage());  // Reset input processor for PauseMenu
                } else if (landingScreen != null) {
                    game.setScreen(landingScreen);  // Return to LandingScreen
                    Gdx.input.setInputProcessor(landingScreen.getStage());  // Reset input processor for LandingScreen
                }
            }
        });

        // Large text box with instructions
        BitmapFont customFont = new BitmapFont(Gdx.files.internal("Font1.fnt"));
        customFont.getData().setScale(0.5f);

        LabelStyle customLabelStyle = new LabelStyle();
        customLabelStyle.font = customFont;

        BitmapFont customFont1 = new BitmapFont(Gdx.files.internal("Font1.fnt"));
        customFont1.getData().setScale(0.08f);

        LabelStyle customLabelStyle1 = new LabelStyle();
        customLabelStyle1.font = customFont1;

        Label howToPlayLabel = new Label("How to play:", customLabelStyle);
        Label instructionsLabel = new Label(
            "You are given £10,000 to start with \n" +
            "An additional £10,000 is given each minute \n" +
            "You have five minutes to get the highest satisfaction possible\n" +
            "This can be achieved by placing buildings and interacting with them\n" +
            "Buildings can be bought and placed by pressing escape near the reception building\n" +
            "You can work, increasing currency, by placing work buildings and then interacting with them\n" +
            "You can sleep, decreasing fatigue, by placing accommodation buildings and interacting with them\n" +
            "You can study, increasing knowledge, by placing academic building and interacting with them\n" +
            "You can eat, decreasing fatigue and increasing satisfaction, by placing Food halls and interacting with them\n" +
            "You can relax, increasing satisfaction, by placing recreational buildings and interacting with them\n" +
            "Each building has a five second cooldown between each use ",
            customLabelStyle1);
        instructionsLabel.setWrap(true);

        // Positioning and adding elements to the stage
        howToPlayLabel.setPosition(600, 600);

        stage.addActor(howToPlayLabel);

        // Add elements to the table for layout management
        table.add(backButton).pad(10).top().left(); // Position back button at the top left
        table.row();
        table.add(instructionsLabel).expand().fill(); // Fill the rest of the screen with instructions
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        SpriteBatch batch = new SpriteBatch();
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

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
        skin.dispose();
    }
}