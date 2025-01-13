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
 * Manages the game's pause menu interface.
 * Provides options to:
 * - Resume game
 * - Access settings
 * - View achievements
 * - Check how to play
 * - Return to main menu
 * Includes a blur overlay effect when menu is active.
 */
public class PauseMenu {
    private Window pauseMenu;         // Main menu window
    private Image blurOverlay;        // Background blur effect
    private boolean isPaused = false; // Current pause state
    private GameScreen gameScreen;    // Reference to game screen
    private UniSim game;              // Main game instance
    private Music music;              // Background music
    private Stage stage;              // UI rendering stage
    private EventManager eventManager; // Handles game events

    /**
     * Creates a new pause menu with all UI components.
     * Sets up the menu window, buttons, and blur overlay.
     *
     * @param stage UI rendering stage
     * @param skin UI theme/styling
     * @param gameScreen Current game screen
     * @param game Main game instance
     * @param music Background music
     */
    public PauseMenu(Stage stage, Skin skin, GameScreen gameScreen, UniSim game, Music music) {
        this.stage = stage; // Initialize stage here
        this.gameScreen = gameScreen;
        this.game = game;
        this.music = music;
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
        pauseMenu = new Window("Paused", skin);
        pauseMenu.setSize(300, 400); // Increased height to accommodate new button
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

    /**
     * Toggles the pause menu visibility.
     * Shows/hides menu and blur overlay.
     */
    public void togglePause() {
        isPaused = !isPaused;
        pauseMenu.setVisible(isPaused);
        blurOverlay.setVisible(isPaused);
    }

    /**
     * Checks if game is currently paused.
     * @return true if paused, false if running
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Returns to pause menu from sub-screens.
     * Used when backing out of settings/achievements.
     */
    public void returnToPauseMenu() {
        game.setScreen(gameScreen); // Return to GameScreen to resume the game
        togglePause(); // Show pause menu again
    }

    /**
     * Gets the current game screen.
     * @return Active GameScreen instance
     */
    public GameScreen getGameScreen() {
        return gameScreen;
    }

    /**
     * Gets the UI stage.
     * @return Stage used for rendering menu
     */
    public Stage getStage() {
        return stage;
    }
}
