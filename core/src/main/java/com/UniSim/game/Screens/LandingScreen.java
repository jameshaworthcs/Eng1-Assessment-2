package com.UniSim.game.Screens;

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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LandingScreen implements Screen {
    private UniSim game;
    private Texture backgroundTexture;
    private Label.LabelStyle labelStyle;
    private Label.LabelStyle titleLabelStyle;
    private Music music;  // Initialize music

    public Stage stage;
    private Skin skin;
    private BitmapFont font;
    private BitmapFont titleFont;

    private List<Float> leaderboardTimes = List.of(15.3f, 10.5f, 20.7f, 12.0f, 8.5f, 11.2f, 14.0f, 9.1f, 17.5f, 13.3f, 19.8f, 7.4f);

    public Stage getStage() {
        return stage;
    }


    public LandingScreen(UniSim game) {
        this.game = game;

        // Initialize music
        music = Gdx.audio.newMusic(Gdx.files.internal("music/awesomeness.wav"));
        music.setLooping(true);
        music.setVolume(1.0f);
        music.play();

        // Set up stage and UI elements
        stage = new Stage(new FitViewport(2560, 1440));
        Gdx.input.setInputProcessor(stage);
        
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        backgroundTexture = new Texture("LoadScreenBackground.png");

        font = new BitmapFont(Gdx.files.internal("Font1.fnt"));
        font.getData().setScale(0.8f); // Adjusted scale to increase button text size
        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
    
        titleFont = new BitmapFont(Gdx.files.internal("titleFont.fnt"));
        titleFont.getData().setScale(1f);
        titleLabelStyle = new Label.LabelStyle();
        titleLabelStyle.font = titleFont;
    
        // Apply this font to your buttons
        skin.getFont("default-font").getData().setScale(2f); // Scale up default font size for all buttons in skin

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
                if (music.isPlaying()) {
                    music.stop();  // Stop LandingScreen's music completely before switching to GameScreen
                }
                game.setScreen(new GameScreen(game));
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game, LandingScreen.this, music));  // Pass music to SettingsScreen
            }
        });

        howToPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HowToPlayScreen(game, LandingScreen.this, music));
            }
        });

        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new CreditsScreen(game, LandingScreen.this, music));
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Positioning and layout code for the UI (unchanged)
        Table mainTable = new Table();
        Table leaderboardTable = new Table();
        leaderboardTable.top().left();

        // Sort and limit leaderboard times to top ten
        List<Float> sortedTimes = new ArrayList<>(leaderboardTimes);
        sortedTimes.sort(Comparator.naturalOrder());
        int limit = Math.min(10, sortedTimes.size());

        Label rankHeader = new Label("Rank", skin);
        Label timeHeader = new Label("Time", skin);

        leaderboardTable.add(rankHeader).pad(5).left();
        leaderboardTable.add(timeHeader).pad(5).expandX().center();
        leaderboardTable.row();

        for (int i = 0; i < limit; i++) {
            Label rankLabel = new Label((i + 1) + ".", skin);
            Label timeLabel = new Label(String.format("%.1f seconds", sortedTimes.get(i)), skin);
            rankLabel.setColor(0, 1f, 0.5f, 1);
            timeLabel.setColor(0, 1f, 0.5f, 1);

            leaderboardTable.add(rankLabel).pad(5).left();
            leaderboardTable.add(timeLabel).pad(5).expandX().center();
            leaderboardTable.row();
        }

        ScrollPane scrollPane = new ScrollPane(leaderboardTable, skin);
        scrollPane.setSize(1070, 580);
        scrollPane.setPosition(800, 250);
        scrollPane.setFadeScrollBars(false);

        Label headerLabel = new Label("Leaderboard", labelStyle);
        headerLabel.setColor(0, 0.5f, 1, 1);
        headerLabel.setPosition(800, 850);
        stage.addActor(headerLabel);

        Label gameTitle = new Label("UniSim", titleLabelStyle);
        gameTitle.setPosition(50, 1100);
        stage.addActor(gameTitle);

        playGameButton.setSize(500, 140);
        playGameButton.setPosition(50, 850);

        settingsButton.setSize(500, 140);
        settingsButton.setPosition(50, 700);

        howToPlayButton.setSize(500, 140);
        howToPlayButton.setPosition(50, 550);

        creditsButton.setSize(500, 140);
        creditsButton.setPosition(50, 400);

        quitButton.setSize(500, 140);
        quitButton.setPosition(50, 250);

        stage.addActor(playGameButton);
        stage.addActor(settingsButton);
        stage.addActor(howToPlayButton);
        stage.addActor(creditsButton);
        stage.addActor(quitButton);
        stage.addActor(scrollPane);
        stage.addActor(mainTable);
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

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        music.dispose();  // Dispose music when LandingScreen is no longer needed
    }

    @Override public void hide() {}
    @Override public void pause() {}
    @Override public void resume() {}
}
