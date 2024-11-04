package com.UniSim.game;

import com.badlogic.gdx.Gdx;
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

public class PauseMenu {
    private Window pauseMenu;
    private Image blurOverlay;
    private boolean isPaused = false;

    public PauseMenu(Stage stage, Skin skin) {
        // Create a blur overlay
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0, 0, 0, 0.5f)); // Semi-transparent black for blurring effect
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

        // Add buttons to the pause menu
        TextButton resumeButton = new TextButton("Resume", skin);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause();
            }
        });
        pauseMenu.add(resumeButton).pad(10).row();

        TextButton settingsButton = new TextButton("Settings", skin);
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Placeholder for settings button
            }
        });
        pauseMenu.add(settingsButton).pad(10).row();

        TextButton howToPlayButton = new TextButton("How to Play", skin);
        howToPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Placeholder for how to play button
            }
        });
        pauseMenu.add(howToPlayButton).pad(10).row();

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
}
