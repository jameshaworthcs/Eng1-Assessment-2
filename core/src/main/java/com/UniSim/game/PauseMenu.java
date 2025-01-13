package com.UniSim.game;

import com.UniSim.game.Events.EventManager;
import com.UniSim.game.Screens.GameScreen;
import com.UniSim.game.Screens.HowToPlayScreen;
import com.UniSim.game.Screens.LandingScreen;
import com.UniSim.game.Screens.SettingsScreen;
import com.UniSim.game.Screens.AchievementsScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The PauseMenu class handles the creation and functionality of the pause menu
 * in the game.
 * It allows the player to pause the game, navigate to settings, see the 'How to
 * Play' screen, or quit to the main menu.
 * The menu is overlaid with a semi-transparent blur effect when active.
 */
public class PauseMenu {
    private Window pauseMenu;
    private Image blurOverlay;
    private boolean isPaused = false;
    private GameScreen gameScreen;
    private UniSim game;
    private Music music;
    private Stage stage; // Declare stage here
    private EventManager eventManager;

    /**
     * Constructs a new PauseMenu instance with minimal initialization.
     *
     * @param gameScreen the current GameScreen to return to when resuming the game
     */
    public PauseMenu(GameScreen gameScreen) {
        this.stage = gameScreen.getStage();
        this.gameScreen = gameScreen;
        this.game = gameScreen.game;
        this.music = gameScreen.music;
        this.eventManager = gameScreen.getEventManager();

        // Create a blur overlay (semi-transparent black for a blurring effect)
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 0, 0.5f)); // Semi-transparent black
        pixmap.fill();
        blurOverlay = new Image(new Texture(pixmap));
        blurOverlay.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        blurOverlay.setVisible(false);
        stage.addActor(blurOverlay);

        // Create pause menu window
        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        pauseMenu = new Window("Paused", skin);
        pauseMenu.setSize(300, 400);
        pauseMenu.setPosition((Gdx.graphics.getWidth() - pauseMenu.getWidth()) / 2,
                (Gdx.graphics.getHeight() - pauseMenu.getHeight()) / 2);
        pauseMenu.setVisible(false);

        // Add resume button
        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause();
            }
        });
        pauseMenu.add(resumeButton).pad(10).row();

        // Add settings button
        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game, gameScreen, music));
            }
        });
        pauseMenu.add(settingsButton).pad(10).row();

        // Add how to play button
        TextButton howToPlayButton = new TextButton("How to Play", skin);
        howToPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HowToPlayScreen(game, gameScreen, music));
            }
        });
        pauseMenu.add(howToPlayButton).pad(10).row();

        // Add achievements button
        TextButton achievementsButton = new TextButton("Achievements", skin);
        achievementsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new AchievementsScreen(game, gameScreen, music));
            }
        });
        pauseMenu.add(achievementsButton).pad(10).row();

        // Add quit button
        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                music.pause();
                game.setScreen(new LandingScreen(game)); // Navigate to main menu
            }
        });
        pauseMenu.add(mainMenuButton).pad(10).row();

        stage.addActor(pauseMenu);
    }

    public void togglePause() {
        isPaused = !isPaused;
        pauseMenu.setVisible(isPaused);
        blurOverlay.setVisible(isPaused);
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void returnToPauseMenu() {
        game.setScreen(gameScreen); // Return to GameScreen to resume the game
        togglePause(); // Show pause menu again
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    // Getter for stage
    public Stage getStage() {
        return stage;
    }
}
