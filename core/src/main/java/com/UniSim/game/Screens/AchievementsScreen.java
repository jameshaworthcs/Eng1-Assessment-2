package com.UniSim.game.Screens;

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
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Achievement tracking and display screen.
 * Features:
 * - List of available achievements
 * - Achievement descriptions and rewards
 * - Satisfaction point bonuses
 * - Scrollable interface
 * Can be accessed from main menu or pause menu.
 */
public class AchievementsScreen implements Screen {
    // Core components
    private final UniSim game;              // Main game instance
    private final Screen previousScreen;     // Screen to return to
    private Stage stage;                     // UI stage
    private Skin skin;                       // UI styling
    private Texture backgroundTexture;       // Menu background
    private Music music;                     // Background music

    /**
     * Creates the achievements screen.
     * Sets up UI components and loads achievements.
     *
     * @param game Main game instance
     * @param previousScreen Screen to return to
     * @param music Background music track
     */
    public AchievementsScreen(UniSim game, Screen previousScreen, Music music) {
        this.game = game;
        this.previousScreen = previousScreen;
        this.music = music;
        
        stage = new Stage(new FitViewport(2560, 1440));
        Gdx.input.setInputProcessor(stage);
        
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        backgroundTexture = new Texture("LoadScreenBackground.png");
        
        createUI();
    }

    /**
     * Sets up the screen's UI elements.
     * Creates:
     * - Title and header
     * - Scrollable achievement list
     * - Back button
     */
    private void createUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        // Create title
        Label.LabelStyle titleStyle = new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Font1.fnt")), Color.BLACK);
        titleStyle.font.getData().setScale(0.4f);
        Label titleLabel = new Label("Achievements", titleStyle);
        titleLabel.setPosition((stage.getViewport().getWorldWidth() - titleLabel.getWidth()) / 2, 1000);

        // Create achievement entries
        Table achievementsTable = new Table();
        achievementsTable.top();
        
        // Custom style for achievement entries
        Label.LabelStyle achievementStyle = new Label.LabelStyle(new BitmapFont(Gdx.files.internal("Font1.fnt")), Color.BLACK);
        achievementStyle.font.getData().setScale(0.14f);

        // Add achievements
        addAchievement(achievementsTable, achievementStyle,
            "I Heart Uni", 
            "Maintain satisfaction above 80% for 3 minutes",
            "+50 satisfaction");

        addAchievement(achievementsTable, achievementStyle,
            "Minimalist",
            "Place a total of five buildings",
            "+30 satisfaction");

        addAchievement(achievementsTable, achievementStyle,
            "Jam Packed",
            "Place the maximum number of buildings",
            "+100 satisfaction");

        addAchievement(achievementsTable, achievementStyle,
            "Knowledge Master",
            "Reach a knowledge score of 20 or higher",
            "+50 satisfaction");

        // Create ScrollPane for achievements
        ScrollPane scrollPane = new ScrollPane(achievementsTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setBounds(stage.getViewport().getWorldWidth() / 4, 100, 
                           stage.getViewport().getWorldWidth() / 2, stage.getViewport().getWorldHeight() - 300);

        // Back button
        Button backButton = new Button(skin);
        backButton.add(new Label("Back", skin));
        backButton.setPosition(10, 1360);
        backButton.setSize(150, 70);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                game.setScreen(previousScreen);
                if (previousScreen instanceof GameScreen) {
                    Gdx.input.setInputProcessor(((GameScreen) previousScreen).getStage());
                } else if (previousScreen instanceof LandingScreen) {
                    Gdx.input.setInputProcessor(((LandingScreen) previousScreen).getStage());
                }
            }
        });

        // Add actors to stage directly instead of using table
        stage.addActor(titleLabel);
        stage.addActor(scrollPane);
        stage.addActor(backButton);
    }

    /**
     * Adds an achievement entry to the achievements list.
     * Creates a styled table entry with title, description, and reward.
     *
     * @param table Parent table to add achievement to
     * @param style Text styling for achievement labels
     * @param title Achievement name
     * @param description Achievement unlock condition
     * @param reward Satisfaction bonus when unlocked
     */
    private void addAchievement(Table table, Label.LabelStyle style, String title, String description, String reward) {
        Table achievementEntry = new Table();
        achievementEntry.top().left();
        achievementEntry.pad(10);
        achievementEntry.setBackground(skin.newDrawable("white", new Color(1, 1, 1, 0.3f)));

        Label titleLabel = new Label(title, style);
        Label descLabel = new Label(description, style);
        Label rewardLabel = new Label(reward, style);

        achievementEntry.add(titleLabel).left().padBottom(5).row();
        achievementEntry.add(descLabel).left().padBottom(3).row();
        achievementEntry.add(rewardLabel).left();

        table.add(achievementEntry).expandX().fill().pad(5).row();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
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
    public void show() {}

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
        backgroundTexture.dispose();
    }
} 