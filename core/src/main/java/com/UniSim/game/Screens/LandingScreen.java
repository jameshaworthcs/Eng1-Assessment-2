package com.UniSim.game.Screens;

import com.UniSim.game.UniSim;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
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
import java.util.logging.FileHandler;

public class LandingScreen implements Screen {
    private UniSim game;
    private Texture backgroundTexture;
    private Label.LabelStyle labelStyle;
    private Label.LabelStyle titleLabelStyle;
    private Music music;  // Initialize music
    public Table leaderboardTable;
    public Stage stage;
    private Skin skin;
    private BitmapFont font;
    private BitmapFont titleFont;

    private List<Float> leaderboardSat;

    public Stage getStage() {
        return stage;
    }


    public LandingScreen(UniSim game) {
        this.game = game;
        this.leaderboardSat = getLeaderboardSat();

        // Initialize music
        music = Gdx.audio.newMusic(Gdx.files.internal("music/awesomeness.wav"));
        music.setLooping(true);
        music.setVolume(1.0f);
        music.play();

        // Set up stage and UI elements
        //stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
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
        TextButton clearLeaderboardButton = new TextButton("Clear Leaderboard", skin);

        // Set up button listeners
        playGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (music.isPlaying()) {
                    music.stop();  // Stop LandingScreen's music completely before switching to GameScreen
                }
                game.setScreen(new GameScreen(game, music));
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

        clearLeaderboardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearLeaderboardSat();
                leaderboardTable.clear();
                Label rankHeader = new Label("Rank", skin);
                Label timeHeader = new Label("Time", skin);

                leaderboardTable.add(rankHeader).pad(5).left();
                leaderboardTable.add(timeHeader).pad(5).expandX().center();
                leaderboardTable.row();
            }
        });

        // Positioning and layout code for the UI (unchanged)
        Table mainTable = new Table();
        this.leaderboardTable = new Table();
        leaderboardTable.top().left();

        // Sort and limit leaderboard times to top ten
        List<Float> sortedTimes = new ArrayList<>(leaderboardSat);
        sortedTimes.sort(Comparator.reverseOrder());
        int limit = Math.min(10, sortedTimes.size());

        Label rankHeader = new Label("Rank", skin);
        Label timeHeader = new Label("Time", skin);

        leaderboardTable.add(rankHeader).pad(5).left();
        leaderboardTable.add(timeHeader).pad(5).expandX().center();
        leaderboardTable.row();

        for (int i = 0; i < limit; i++) {
            Label rankLabel = new Label((i + 1) + ".", skin);
            Label timeLabel = new Label(String.format("%.1f satisfaction", sortedTimes.get(i)), skin);
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

        clearLeaderboardButton.setSize(300, 50);
        clearLeaderboardButton.setPosition(1550, 190);

        stage.addActor(playGameButton);
        stage.addActor(settingsButton);
        stage.addActor(howToPlayButton);
        stage.addActor(creditsButton);
        stage.addActor(quitButton);
        stage.addActor(scrollPane);
        stage.addActor(mainTable);
        stage.addActor(clearLeaderboardButton);
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(backgroundTexture, 0, 0, 2560, 1440);
        game.batch.end();

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

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    private List<Float> getLeaderboardSat() {
        List<Float> leaderboardSat = new ArrayList<>();
        FileHandle file = Gdx.files.local("leaderboard.txt");

        if (file.exists()) {
            // Read the entire file as a string
            String fileContents = file.readString();
            // Split file into lines (assuming each line is a satisfaction value)
            String[] lines = fileContents.split("\n");

            // Parse each line as a float and add it to the list
            for (String line : lines) {
                try {
                    float satisfaction = Float.parseFloat(line.trim());  // Convert line to float
                    leaderboardSat.add(satisfaction);
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing value: " + line);
                }
            }
        }
        return leaderboardSat;
    }

    public void clearLeaderboardSat() {
        // Get a handle to the file
        FileHandle file = Gdx.files.local("leaderboard.txt");

        // Write an empty string to clear the file's content
        file.writeString("", false);
    }
}
