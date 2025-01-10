package com.UniSim.game.Screens;

import java.util.List;

import com.UniSim.game.UniSim;
import com.UniSim.game.Stats.PlayerStats;
import com.UniSim.game.Stats.Achievement;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * EndScreen class represents the screen shown when the game ends.
 * It displays the final stats of the game, including satisfaction and other
 * metrics,
 * and allows the player to return to the main screen.
 */
public class EndScreen implements Screen {

    private static final float FONT_SCALE = 0.8f;
    private static final float TITLE_FONT_SCALE = 1f;

    private final UniSim game;
    private final AssetManager manager;

    private Stage stage;
    private Texture backgroundTexture;
    private Music music;
    private Skin skin;
    private BitmapFont font;
    private BitmapFont titleFont;
    private PlayerStats finalStats;
    private int satisfactionLeft;
    private List<Achievement> unlockedAchievements;

    /**
     * Constructor for EndScreen.
     *
     * @param game       The game instance.
     * @param music      The background music to play during the EndScreen.
     * @param finalStats The final player stats to display on the EndScreen.
     */
    public EndScreen(UniSim game, Music music, PlayerStats finalStats, String username, List<Achievement> unlockedAchievements) {
        this.game = game;
        this.manager = new AssetManager();
        this.finalStats = finalStats;
        this.satisfactionLeft = finalStats.getSatisfaction();
        this.unlockedAchievements = unlockedAchievements;
        PlayerStats.getUsername();

        initializeMusic(music);
        initializeStage();
        initializeUI();
    }

    /**
     * Initializes the background music for the EndScreen.
     *
     * @param music The background music to be played.
     */
    private void initializeMusic(Music music) {
        this.music = music;
        manager.load("music/Chippytoon.mp3", Music.class);
        manager.finishLoading();

        this.music = manager.get("music/Chippytoon.mp3", Music.class);
        this.music.setLooping(true);
        this.music.setVolume(music.getVolume());
        this.music.play();
    }

    private void initializeStage() {
        stage = new Stage(new FitViewport(2560, 1440));
        Gdx.input.setInputProcessor(stage);
        backgroundTexture = new Texture("LoadScreenBackground.png");

    }

    /**
     * Initializes the user interface for the EndScreen, including fonts, button
     * styles, and labels.
     */
    private void initializeUI() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        font = new BitmapFont(Gdx.files.internal("Font1.fnt"));
        font.getData().setScale(FONT_SCALE);
        new Label.LabelStyle(font, null);

        titleFont = new BitmapFont(Gdx.files.internal("titleFont.fnt"));
        titleFont.getData().setScale(TITLE_FONT_SCALE);
        new Label.LabelStyle(titleFont, null);

        // Adjust default skin font size
        skin.getFont("default-font").getData().setScale(2f);
        // show();
    }

    /**
     * Initializes and displays UI elements such as labels and buttons on the
     * screen.
     */
    @Override
    public void show() {
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
                music.stop();
                game.setScreen(new LandingScreen(game));
            }
        });

        BitmapFont customFont = new BitmapFont(Gdx.files.internal("Font1.fnt"));
        customFont.getData().setScale(0.9f);

        Label.LabelStyle customLabelStyle = new Label.LabelStyle();
        customLabelStyle.font = customFont;
        customLabelStyle.fontColor = Color.BLACK;

        BitmapFont customFont1 = new BitmapFont(Gdx.files.internal("Font1.fnt"));
        customFont1.getData().setScale(0.15f);

        Label.LabelStyle customLabelStyle1 = new Label.LabelStyle();
        customLabelStyle1.font = customFont1;
        customLabelStyle1.fontColor = Color.BLACK;

        float currencyLeft = finalStats.getCurrency();
        float fatigueLeft = finalStats.getFatigue();
        float knowledgeLeft = finalStats.getKnowledge();

        float currencyAddition = currencyLeft / 100;
        float knowledgeAddition = knowledgeLeft * 2;
        float fatigueSubtraction = fatigueLeft;

        float beforeSatisfactionLeft = this.satisfactionLeft;

        this.satisfactionLeft += currencyAddition + knowledgeAddition - fatigueSubtraction;

        // Calculate achievement bonuses
        int achievementBonus = 0;
        StringBuilder achievementText = new StringBuilder();
        if (!unlockedAchievements.isEmpty()) {
            achievementText.append("\n\nAchievements Unlocked:\n");
            for (Achievement achievement : unlockedAchievements) {
                achievementText.append("- ").append(achievement.getName())
                    .append(" (").append(achievement.getDescription()).append("): +")
                    .append(achievement.getSatisfactionBonus()).append(" satisfaction\n");
                achievementBonus += achievement.getSatisfactionBonus();
            }
        }

        Label CongratulationsLabel = new Label("Congratulations", customLabelStyle);
        Label endGameDetailLabel = new Label(
                "You Finished with:\n" +
                        "You finished with " + beforeSatisfactionLeft + " satisfaction but you had\n" +
                        currencyLeft + " currency left which gave an additional " + currencyAddition + " satisfaction\n" +
                        fatigueLeft + " fatigue which subtracted " + fatigueSubtraction + " satisfaction\n" +
                        knowledgeLeft + " knowledge which gave an additional " + knowledgeAddition + " satisfaction" +
                        achievementText.toString() +
                        "\nResulting in a total of " + this.satisfactionLeft + " satisfaction",
                customLabelStyle1);
        endGameDetailLabel.setWrap(true);
        endGameDetailLabel.setAlignment(Align.center);

        // Positioning and adding elements to the stage
        CongratulationsLabel.setPosition((stage.getViewport().getWorldWidth() - CongratulationsLabel.getWidth()) / 2,
                1000);
        endGameDetailLabel.setPosition((stage.getViewport().getWorldWidth() - endGameDetailLabel.getWidth()) / 2, 460);

        backButton.setPosition(10, 1360);
        backButton.setSize(150, 70);

        stage.addActor(CongratulationsLabel);
        stage.addActor(endGameDetailLabel);
        stage.addActor(backButton);

        saveSatisfaction();
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
    public void pause() {
        // Pause logic here, if needed
    }

    @Override
    public void resume() {
        // Resume logic here, if needed
    }

    @Override
    public void hide() {
        // Hide logic here, if needed
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        music.dispose();
        font.dispose();
        titleFont.dispose();
        skin.dispose();
        manager.dispose();
    }

    /**
     * Saves the final satisfaction score to a leaderboard file.
     * This score is appended to the file for future reference.
     */
    private void saveSatisfaction() {
        try {
            // Create a .unisim directory in user's home directory if it doesn't exist
            FileHandle gameDir = Gdx.files.external(".unisim");
            if (!gameDir.exists()) {
                gameDir.mkdirs();
            }
            
            // Save to .unisim/leaderboard.txt in user's home directory
            FileHandle file = Gdx.files.external(".unisim/leaderboard.txt");
            // Append the username satisfaction value to the file, followed by a newline
            file.writeString(satisfactionLeft + ", " + PlayerStats.getUsername() + "\n", true);
        } catch (Exception e) {
            System.err.println("Failed to save leaderboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
