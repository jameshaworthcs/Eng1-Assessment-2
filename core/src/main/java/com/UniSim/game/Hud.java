package com.UniSim.game;

import java.util.ArrayList;

import com.UniSim.game.Events.Event;
import com.UniSim.game.Screens.EndScreen;
import com.UniSim.game.Screens.GameScreen;
import com.UniSim.game.Stats.PlayerStats;
import com.UniSim.game.Stats.StatsLabels;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The Hud class handles the display of the game's heads-up display,
 * including player statistics (e.g., satisfaction, currency, fatigue),
 * the countdown timer, and messages for the player.
 * It also manages updates to the player's stats and the game timer.
 */
public class Hud {
    public Stage stage;
    private Viewport viewport;
    private Skin skin;
    private World world;

    private Integer worldTimer;
    private float timeCount;
    private boolean timeUp;
    private GameScreen gameScreen;
    private boolean satUpdateOnce;
    private boolean endOnce;
    private UniSim game;
    private Music music;

    Label countdownLabel;
    Label timeLabel;

    public PlayerStats stats;

    private ArrayList<StatsLabels> playerStatLabels;
    private Label messageLabel;

    /**
     * Creates a new Hud instance.
     *
     * @param sb         the SpriteBatch used for rendering the HUD elements
     * @param skin       the Skin used for styling the labels and UI elements
     * @param world      the Box2D world used for game physics
     * @param gameScreen the current game screen
     * @param game       the main game instance
     * @param music      the background music for the game
     */
    public Hud(SpriteBatch sb, Skin skin, World world, GameScreen gameScreen, UniSim game, Music music) {
        this.world = world;
        this.skin = skin;
        this.gameScreen = gameScreen;
        this.satUpdateOnce = false;
        this.endOnce = false;
        this.game = game;
        this.music = music;
        setTimer(sb);
        setStats(skin, world);
        createMessageLabel(skin);
    }

    public PlayerStats getStats() {
        return stats;
    }

    /**
     * Sets up and initializes the player statistics display.
     *
     * @param skin  the Skin used for styling the labels
     * @param world the Box2D world used for physics
     */
    private void setStats(Skin skin, World world) {
        stats = new PlayerStats();
        playerStatLabels = new ArrayList<>();

        playerStatLabels.add(new StatsLabels(world, stage, skin, 10, 220, "BUILDINGS: " + stats.getBuildingCounter()));
        playerStatLabels.add(new StatsLabels(world, stage, skin, 10, 200, "SATISFACTION: " + stats.getSatisfaction()));
        playerStatLabels.add(new StatsLabels(world, stage, skin, 10, 180,
                "CURRENCY: " + String.format("$%.2f", stats.getCurrency())));
        playerStatLabels.add(new StatsLabels(world, stage, skin, 10, 160, "FATIGUE: " + stats.getFatigue() + "/ 50"));
        playerStatLabels.add(new StatsLabels(world, stage, skin, 10, 140, "KNOWLEDGE: " + stats.getKnowledge()));
    }

    public void updateStats() {
        playerStatLabels.get(0).setText("BUILDINGS: " + stats.getBuildingCounter());
        playerStatLabels.get(1).setText("SATISFACTION: " + stats.getSatisfaction());
        playerStatLabels.get(2).setText("CURRENCY: " + String.format("$%.2f", stats.getCurrency()));
        playerStatLabels.get(3).setText("FATIGUE: " + stats.getFatigue() + "/50");
        playerStatLabels.get(4).setText("KNOWLEDGE: " + stats.getKnowledge());
    }

    /**
     * Initializes the timer and UI elements for tracking and displaying time.
     *
     * @param sb the SpriteBatch used for rendering the timer
     */
    private void setTimer(SpriteBatch sb) {
        worldTimer = 300;
        timeCount = 0;

        viewport = new FitViewport(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f,
                new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%03d", worldTimer),
                new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(timeLabel).expandX().pad(10);
        table.row();
        table.add(countdownLabel).expandX();

        stage.addActor(table);
    }

    /**
     * Creates the message label for displaying player notifications.
     *
     * @param skin the Skin used for styling the label
     */
    private void createMessageLabel(Skin skin) {
        messageLabel = new Label("", skin);
        messageLabel.setPosition(10, 20); // Set the position for the message
        messageLabel.setVisible(false); // Initially hidden
        stage.addActor(messageLabel); // Add the label to the stage
    }

    public void update(float dt) {
        updateStats();
        satisfactionUpdate(dt);
        // fatigueUpdaate(dt);
        timeCount += dt;
        if (timeCount >= 1) {
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
            checkIfEnd();
        }
    }

    public float getTimeCount() {
        return worldTimer;
    }

    public boolean isTimeUp() {
        return timeUp;
    }

    public void sendMessage(String message) {
        messageLabel.setText(message);
        messageLabel.setVisible(true); // Show error message
    }

    public void hideMessage() {
        messageLabel.setVisible(false);
    }

    /**
     * Displays a pop-up for the given event
     *
     * @param event
     *
     */

    /**
     * Updates the satisfaction value of the player every 30 seconds.
     *
     * @param dt the time delta between frames
     */
    public void showEventPopup(Event event) {
        sendMessage(event.getDescription());
    }

    private void satisfactionUpdate(float dt) {
        if (worldTimer % 30 == 0 && worldTimer != 300 && !satUpdateOnce) {
            int increase = stats.calculateSatisfaction();
            gameScreen.popUp("+" + increase + " Satisfaction", 3);
            stats.increaseSatisfaction(increase);
            satUpdateOnce = true;
        }

        // Reset `satUpdateOnce` once we're past the 30-second mark
        if (worldTimer % 30 != 0) {
            satUpdateOnce = false;
        }
    }

    /**
     * Checks if the game has ended (when the timer runs out).
     */
    private void checkIfEnd() {
        if (worldTimer == 0 && !endOnce) {
            endOnce = true;
            music.stop();
            game.setScreen(new EndScreen(game, music, stats));
        }
    }

}
