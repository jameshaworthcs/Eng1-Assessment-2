package com.UniSim.game.Screens;

import com.UniSim.game.PauseMenu;
import com.UniSim.game.UniSim;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * The SettingsScreen class represents the settings menu of the game,
 * where the player can adjust game settings like resolution and music volume.
 * This screen can be accessed either from the landing screen or the pause menu.
 */
public class SettingsScreen implements Screen {
    private UniSim game;
    private Stage stage;
    private Skin skin;
    private Texture backgroundTexture;
    private Music music; // Music instance to control
    private LandingScreen landingScreen;
    private PauseMenu pauseMenu;
    private static float soundEffectsVolume = 1.0f; // Default sound effects volume

    /**
     * Constructor for SettingsScreen accessed from LandingScreen.
     * Initializes the screen with the game, landing screen, and music reference.
     *
     * @param game          The game instance for screen switching
     * @param landingScreen The landing screen to return to
     * @param music         The music instance to control volume
     */
    public SettingsScreen(UniSim game, LandingScreen landingScreen, Music music) {
        this.game = game;
        this.landingScreen = landingScreen;
        this.music = music; // Store the reference to the music instance
        initialize();
    }

    /**
     * Constructor for SettingsScreen accessed from PauseMenu.
     * Initializes the screen with the game, pause menu, and music reference.
     *
     * @param game      The game instance for screen switching
     * @param pauseMenu The pause menu to return to
     * @param music     The music instance to control volume
     */
    public SettingsScreen(UniSim game, PauseMenu pauseMenu, Music music) {
        this.game = game;
        this.pauseMenu = pauseMenu;
        this.music = music; // Store the reference to the music instance
        initialize();
    }

    /**
     * Initializes the UI elements for the settings screen including buttons,
     * sliders, and labels for resolution and music volume settings.
     */
    private void initialize() {
        stage = new Stage(new FitViewport(2560, 1440));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        Gdx.input.setInputProcessor(stage);

        // Load background texture
        backgroundTexture = new Texture("LoadScreenBackground.png");

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        BitmapFont customFont = new BitmapFont();
        customFont.getData().setScale(2.5f);
        LabelStyle customLabelStyle = new LabelStyle();
        customLabelStyle.font = customFont;

        // Back button to return to previous screen
        Button backButton = new Button(skin);
        backButton.add(new Label("Back", customLabelStyle));
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (pauseMenu != null) {
                    // Return to PauseMenu if accessed from there
                    pauseMenu.returnToPauseMenu(); // Return to PauseMenu
                    Gdx.input.setInputProcessor(pauseMenu.getStage()); // Show the PauseMenu overlay
                } else if (landingScreen != null) {
                    game.setScreen(landingScreen); // Return to LandingScreen
                    Gdx.input.setInputProcessor(landingScreen.getStage()); // Reset input processor for LandingScreen
                }
            }
        });

        // Resolution buttons
        Button res720Button = new Button(skin);
        res720Button.add(new Label("1280x720", customLabelStyle));
        res720Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setWindowedMode(1280, 720); // Set resolution
            }
        });

        Button res1080Button = new Button(skin);
        res1080Button.add(new Label("1920x1080", customLabelStyle));
        res1080Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setWindowedMode(1920, 1080);
            }
        });

        Button res1440Button = new Button(skin);
        res1440Button.add(new Label("2560x1440", customLabelStyle));
        res1440Button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.graphics.setWindowedMode(2560, 1440);
            }
        });

        // Music volume slider
        LabelStyle musicLabelStyle = new LabelStyle();
        musicLabelStyle.font = customFont;
        musicLabelStyle.fontColor = Color.BLACK;
        Label musicLabel = new Label("Music Volume", musicLabelStyle);
        Label resolutionLabel = new Label("Resolution", musicLabelStyle);

        Slider musicSlider = new Slider(0, 1, 0.01f, false, skin);
        musicSlider.setValue(music.getVolume()); // Initialize slider with current volume
        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                music.setVolume(musicSlider.getValue()); // Adjust volume
            }
        });

        // Sound effects volume slider
        Label soundEffectsLabel = new Label("Sound Effects Volume", musicLabelStyle);
        Slider soundEffectsSlider = new Slider(0, 1, 0.01f, false, skin);
        soundEffectsSlider.setValue(soundEffectsVolume); // Initialize slider with current volume
        soundEffectsSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                soundEffectsVolume = soundEffectsSlider.getValue(); // Adjust volume
            }
        });

        // Positioning and adding UI elements to the stage
        backButton.setSize(200, 100);
        backButton.setPosition(10, 1320);
        res720Button.setSize(300, 100);
        res720Button.setPosition(703, 1100);
        res1080Button.setSize(300, 100);
        res1080Button.setPosition(1130, 1100);
        res1440Button.setSize(300, 100);
        res1440Button.setPosition(1556, 1100);
        musicLabel.setPosition(1170, 900);
        musicSlider.setSize(703, 100);
        musicSlider.setPosition(930, 830);
        soundEffectsLabel.setPosition(1170, 700);
        soundEffectsSlider.setSize(703, 100);
        soundEffectsSlider.setPosition(930, 630);
        resolutionLabel.setPosition(1190, 1250);

        stage.addActor(backButton);
        stage.addActor(res720Button);
        stage.addActor(res1080Button);
        stage.addActor(res1440Button);
        stage.addActor(musicLabel);
        stage.addActor(resolutionLabel);
        stage.addActor(musicSlider);
        stage.addActor(soundEffectsLabel);
        stage.addActor(soundEffectsSlider);
    }

    // Standard Screen interface methods
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1); // Set background color
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        SpriteBatch batch = new SpriteBatch();
        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

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
        backgroundTexture.dispose();
    }

    public static float getSoundEffectsVolume() {
        return soundEffectsVolume;
    }
}
