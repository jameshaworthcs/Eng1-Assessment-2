package com.UniSim.game.Screens;

import java.util.ArrayList;
import java.util.List;

import com.UniSim.game.UniSim;
import com.UniSim.game.Settings.GameSettings;
import com.UniSim.game.Stats.PlayerStats;
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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.Color;

/**
 * This screen is the landing page of the game. It allows the player to choose
 * between starting a new game, accessing settings, viewing instructions,
 * checking credits,
 * or quitting the game. It also displays the leaderboard of players'
 * satisfaction scores.
 */
public class LandingScreen implements Screen {
    private UniSim game;
    private Texture backgroundTexture;
    private Label.LabelStyle labelStyle;
    private Label.LabelStyle titleLabelStyle;
    private Label.LabelStyle subtextLabelStyle;
    private Music music; // Initialize music
    public Table leaderboardTable;
    public Stage stage;
    private Skin skin;
    private BitmapFont font;
    private BitmapFont titleFont;

    private List<String> leaderboardSat;
    private List<String> leaderboardNames;

    private TextField usernameTextField;

    public Stage getStage() {
        return stage;
    }

    /**
     * Constructor for the LandingScreen.
     * Initializes the background, music, and all UI elements.
     *
     * @param game The game instance for screen switching
     */
    public LandingScreen(UniSim game) {
        this.game = game;

        // Initialize music
        music = Gdx.audio.newMusic(Gdx.files.internal("music/awesomeness.wav"));
        music.setLooping(true);
        music.setVolume(GameSettings.getMusicVolume()); // Use saved volume setting
        music.play();

        // Set up stage and UI elements
        // stage = new Stage(new FitViewport(Gdx.graphics.getWidth(),
        // Gdx.graphics.getHeight()));
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
        TextButton howToPlayButton = new TextButton("How To Play", skin);
        TextButton achievementsButton = new TextButton("Achievements", skin);
        TextButton creditsButton = new TextButton("Credits", skin);
        TextButton quitButton = new TextButton("Quit", skin);
        TextButton clearLeaderboardButton = new TextButton("Clear Leaderboard", skin);

        class PlaceholderTextField extends TextField {
            private final String placeholder;
            private final Color placeholderColor;

            public PlaceholderTextField(String placeholder, Skin skin) {
                super("", skin);
                this.placeholder = placeholder;
                this.placeholderColor = new Color(0.5f, 0.5f, 0.5f, 1f);
            }

            @Override
            public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                if (getText().isEmpty()) {
                    BitmapFont font = getStyle().font;
                    Color oldColor = font.getColor();
                    font.setColor(placeholderColor);
                    font.draw(batch, placeholder, getX() + 10, getY() + (getHeight() + font.getCapHeight()) / 2);
                    font.setColor(oldColor);
                }
            }
        }

        usernameTextField = new PlaceholderTextField("Enter your username", skin);
        usernameTextField.setSize(500, 60);
        usernameTextField.setPosition(50, 1000);

        // Set up button listeners
        playGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String username = usernameTextField.getText();
                PlayerStats.setUsername(username);
                if (music.isPlaying()) {
                    music.stop(); // Stop LandingScreen's music completely before switching to GameScreen
                }
                game.setScreen(new GameScreen(game, music));
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SettingsScreen(game, LandingScreen.this, music)); // Pass music to SettingsScreen
            }
        });

        howToPlayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new HowToPlayScreen(game, LandingScreen.this, music));
            }
        });

        achievementsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new AchievementsScreen(game, LandingScreen.this, music));
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

        // Get leaderboard data
        List<List<String>> leaderboard = getLeaderboard();
        int limit = Math.min(5, leaderboard.size());

        Label rankHeader = new Label("Rank", skin);
        Label scoreHeader = new Label("Score", skin);

        leaderboardTable.add(rankHeader).pad(5).left();
        leaderboardTable.add(scoreHeader).pad(5).expandX().center();
        leaderboardTable.row();

        for (int i = 0; i < limit; i++) {
            List<String> entry = leaderboard.get(i);
            String name = entry.get(0);
            String score = entry.get(1);

            Label rankLabel = new Label((i + 1) + ".  " + name, skin);
            Label scoreLabel = new Label(score, skin);
            rankLabel.setColor(0, 1f, 0.5f, 1);
            scoreLabel.setColor(0, 1f, 0.5f, 1);

            leaderboardTable.add(rankLabel).pad(5).left();
            leaderboardTable.add(scoreLabel).pad(5).expandX().center();
            leaderboardTable.row();
        }

        ScrollPane scrollPane = new ScrollPane(leaderboardTable, skin);
        scrollPane.setSize(1070, 360);
        scrollPane.setPosition(800, 400);
        scrollPane.setFadeScrollBars(false);

        // Create larger style for the header
        Label.LabelStyle headerLabelStyle = new Label.LabelStyle(font, Color.valueOf("007FFF"));
        headerLabelStyle.font = font;
        headerLabelStyle.font.getData().setScale(0.7f);  // Larger scale for header

        Label headerLabel = new Label("Leaderboard", headerLabelStyle);
        headerLabel.setPosition(800, 750);
        stage.addActor(headerLabel);

        // Create smaller style for the subtext with its own font instance
        BitmapFont subtextFont = new BitmapFont(Gdx.files.internal("Font1.fnt"));
        subtextFont.getData().setScale(0.25f);  // Much smaller scale for subtext
        this.subtextLabelStyle = new Label.LabelStyle(subtextFont, Color.valueOf("007FFF"));

        Label topPlayersLabel = new Label("(Top 5 Players Shown)", this.subtextLabelStyle);
        topPlayersLabel.setPosition(800, 320);
        stage.addActor(topPlayersLabel);

        Label gameTitle = new Label("UniSim", titleLabelStyle);
        gameTitle.setPosition(50, 1100);
        stage.addActor(gameTitle);

        playGameButton.setSize(500, 140);
        playGameButton.setPosition(50, 850);

        settingsButton.setSize(500, 140);
        settingsButton.setPosition(50, 700);

        howToPlayButton.setSize(500, 140);
        howToPlayButton.setPosition(50, 550);

        achievementsButton.setSize(500, 140);
        achievementsButton.setPosition(50, 400);

        creditsButton.setSize(500, 140);
        creditsButton.setPosition(50, 250);

        quitButton.setSize(500, 140);
        quitButton.setPosition(50, 100);

        clearLeaderboardButton.setSize(300, 50);
        clearLeaderboardButton.setPosition(1550, 190);

        stage.addActor(usernameTextField);
        stage.addActor(playGameButton);
        stage.addActor(settingsButton);
        stage.addActor(howToPlayButton);
        stage.addActor(achievementsButton);
        stage.addActor(creditsButton);
        stage.addActor(quitButton);
        stage.addActor(scrollPane);
        stage.addActor(mainTable);
        stage.addActor(clearLeaderboardButton);
    }

    @Override
    public void show() {
        stage.setKeyboardFocus(usernameTextField);
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
    public void dispose() {
        stage.dispose();
        skin.dispose();
        backgroundTexture.dispose();
        music.dispose(); // Dispose music when LandingScreen is no longer needed
        font.dispose();
        titleFont.dispose();
        if (subtextLabelStyle != null && subtextLabelStyle.font != null) {
            subtextLabelStyle.font.dispose();
        }
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

    /**
     * Retrieves the leaderboard satisfaction scores from a file.
     *
     * @return A list of satisfaction scores from the leaderboard
     */
    private List<List<String>> getLeaderboard() {
        List<List<String>> leaderboard = new ArrayList<>();
        FileHandle file = Gdx.files.external(".unisim/leaderboard.txt");

        if (file.exists()) {
            try {
                // Read the entire file as a string
                String fileContents = file.readString();
                // Split file into lines (assuming each line is a satisfaction value)
                String[] lines = fileContents.split("\n");

                // Parse each line and add it to the list
                for (String line : lines) {
                    String[] parts = line.split(",");
                    if (parts.length >= 2) {
                        try {
                            int satisfaction = Integer.parseInt(parts[0].trim());
                            String name = parts[1].trim();
                            List<String> entry = new ArrayList<>();
                            entry.add(name);
                            entry.add(String.valueOf(satisfaction));
                            leaderboard.add(entry);
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing value: " + line);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error reading leaderboard: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Sort the entries by satisfaction score in descending order
        leaderboard.sort((a, b) -> Float.compare(Float.parseFloat(b.get(1)), Float.parseFloat(a.get(1))));

        return leaderboard;
    }

    /**
     * Clears the leaderboard satisfaction scores from the file.
     */
    public void clearLeaderboardSat() {
        try {
            // Get a handle to the file
            FileHandle file = Gdx.files.external(".unisim/leaderboard.txt");
            // Write an empty string to clear the file's content
            file.writeString("", false);
        } catch (Exception e) {
            System.err.println("Error clearing leaderboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
