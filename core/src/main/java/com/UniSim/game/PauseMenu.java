package com.UniSim.game;

import com.UniSim.game.Screens.gameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class PauseMenu {
    private Window pauseMenu;
    private Window settingsMenu;
    private Window howToPlayMenu;
    private Image blurOverlay;
    private boolean isPaused = false;
    private gameScreen gameScreen;

    public PauseMenu(Stage stage, Skin skin, gameScreen gameScreen) {
        this.gameScreen = gameScreen;

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
        pauseMenu.setSize(300, 200);
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
                pauseMenu.setVisible(false);
                settingsMenu.setVisible(true);
            }
        });
        pauseMenu.add(settingsButton).pad(10).row();

        // Add how to play button
        TextButton howToPlayButton = new TextButton("How to Play", skin);
        howToPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseMenu.setVisible(false);
                howToPlayMenu.setVisible(true);
            }
        });
        pauseMenu.add(howToPlayButton).pad(10).row();

        stage.addActor(pauseMenu);

        // Create settings menu window
        settingsMenu = new Window("Settings", skin);
        settingsMenu.setSize(400, 300);
        settingsMenu.setPosition((Gdx.graphics.getWidth() - settingsMenu.getWidth()) / 2,
                (Gdx.graphics.getHeight() - settingsMenu.getHeight()) / 2);
        settingsMenu.setVisible(false);

        // Add volume slider label
        Label volumeLabel = new Label("Music Volume", skin);
        settingsMenu.add(volumeLabel).pad(10).row();

        // Create a volume slider
        Slider volumeSlider = new Slider(0, 1, 0.01f, false, skin);
        volumeSlider.setValue(gameScreen.getMusicVolume()); // Set initial value
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                gameScreen.setMusicVolume(volumeSlider.getValue());
            }
        });
        settingsMenu.add(volumeSlider).width(300).pad(10).row();

        // Add back button in settings menu
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingsMenu.setVisible(false);
                pauseMenu.setVisible(true);
            }
        });
        settingsMenu.add(backButton).pad(10).row();

        stage.addActor(settingsMenu);

        // Create how to play menu window
        howToPlayMenu = new Window("How to Play", skin);
        howToPlayMenu.setSize(400, 300);
        howToPlayMenu.setPosition((Gdx.graphics.getWidth() - howToPlayMenu.getWidth()) / 2,
                (Gdx.graphics.getHeight() - howToPlayMenu.getHeight()) / 2);
        howToPlayMenu.setVisible(false);

        // Add information label
        Label infoLabel = new Label("Use WASD or Arrow keys to move around.\nClick on buttons to interact with elements in the game.", skin);
        infoLabel.setWrap(true);
        infoLabel.setAlignment(Align.center);
        howToPlayMenu.add(infoLabel).width(350).pad(10).row();

        // Add back button in how to play menu
        TextButton howToPlayBackButton = new TextButton("Back", skin);
        howToPlayBackButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                howToPlayMenu.setVisible(false);
                pauseMenu.setVisible(true);
            }
        });
        howToPlayMenu.add(howToPlayBackButton).pad(10).row();

        stage.addActor(howToPlayMenu);
    }

    public void togglePause() {
        isPaused = !isPaused;
        pauseMenu.setVisible(isPaused);
        blurOverlay.setVisible(isPaused);
    }

    public boolean isPaused() {
        return isPaused;
    }
}