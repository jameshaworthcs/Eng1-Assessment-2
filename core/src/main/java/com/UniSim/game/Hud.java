package com.UniSim.game;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import com.UniSim.game.Events.Event;
import com.UniSim.game.Screens.EndScreen;
import com.UniSim.game.Screens.GameScreen;
import com.UniSim.game.Stats.PlayerStats;
import com.UniSim.game.Stats.StatsLabels;
import com.UniSim.game.Stats.AchievementManager;
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
    private AchievementManager achievementManager;

    Label countdownLabel;
    Label timeLabel;

    public PlayerStats stats;

    private ArrayList<StatsLabels> playerStatLabels;
    private Label messageLabel;

    private Queue<NotificationMessage> messageQueue;
    private ArrayList<NotificationMessage> activeMessages;
    private static final float MESSAGE_DURATION = 5f; // Messages last for 5 seconds
    private static final float MESSAGE_SPACING = 30f; // Vertical space between messages

    private class NotificationMessage {
        String text;
        float timeLeft;
        Label label;
        String type;
        boolean isPersistent;

        NotificationMessage(String text, float duration, Label label, String type) {
            this.text = text;
            this.timeLeft = duration;
            this.label = label;
            this.type = type;
            this.isPersistent = type.equals("initialHint");
        }
    }

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
        this.messageQueue = new LinkedList<>();
        this.activeMessages = new ArrayList<>();
        setTimer(sb);
        setStats(skin, world);
        createMessageLabel(skin);
        this.achievementManager = new AchievementManager(this);
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
        worldTimer = (int) Constants.TIME_LIMIT;
        timeCount = 0;

        viewport = new FitViewport(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f,
                new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        countdownLabel = new Label(String.format("%02d:%02d", worldTimer / 60, worldTimer % 60),
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
        messageLabel.setVisible(false);
        stage.addActor(messageLabel);
    }

    public void sendMessage(String message) {
        sendMessage(message, "default");
    }

    public void sendMessage(String message, String type) {
        // Remove any existing messages of the same type
        ArrayList<NotificationMessage> messagesToRemove = new ArrayList<>();
        for (NotificationMessage msg : activeMessages) {
            if (msg.type.equals(type)) {
                msg.label.remove();
                messagesToRemove.add(msg);
            }
        }
        activeMessages.removeAll(messagesToRemove);

        Label newLabel = new Label(message, skin);
        // Position buildMode messages in bottom right, others in bottom left
        if (type.equals("buildMode")) {
            float stageWidth = stage.getViewport().getWorldWidth();
            newLabel.setPosition(stageWidth - newLabel.getWidth() - 20, 20);
        } else {
            newLabel.setPosition(10, 20);
        }
        newLabel.setVisible(true);
        stage.addActor(newLabel);
        
        NotificationMessage notification = new NotificationMessage(message, MESSAGE_DURATION, newLabel, type);
        activeMessages.add(notification);
        
        // Update positions of all messages
        updateMessagePositions();
    }

    private void updateMessagePositions() {
        float currentY = 20;
        float stageWidth = stage.getViewport().getWorldWidth();
        for (NotificationMessage msg : activeMessages) {
            if (msg.type.equals("buildMode")) {
                msg.label.setPosition(stageWidth - msg.label.getWidth() - 20, currentY);
            } else {
                msg.label.setPosition(10, currentY);
            }
            currentY += MESSAGE_SPACING;
        }
    }

    public void update(float dt) {
        updateStats();
        satisfactionUpdate(dt);
        achievementManager.update(dt);
        updateNotifications(dt);
        // fatigueUpdate(dt);
        timeCount += dt;
        if (timeCount >= 1) {
            if (worldTimer > 0) {
                worldTimer--;
            } else {
                timeUp = true;
            }
            countdownLabel.setText(String.format("%02d:%02d", worldTimer / 60, worldTimer % 60));
            timeCount = 0;
            checkIfEnd();
        }
    }

    private void updateNotifications(float dt) {
        ArrayList<NotificationMessage> messagesToRemove = new ArrayList<>();
        
        for (NotificationMessage message : activeMessages) {
            if (!message.isPersistent) {
                message.timeLeft -= dt;
                if (message.timeLeft <= 0) {
                    message.label.remove();
                    messagesToRemove.add(message);
                }
            }
        }
        
        activeMessages.removeAll(messagesToRemove);
        
        if (!messagesToRemove.isEmpty()) {
            updateMessagePositions();
        }
    }

    public float getWorldTimer() {
        return worldTimer;
    }

    public boolean isTimeUp() {
        return timeUp;
    }

    public void hideMessage(String type) {
        // Remove only messages of the specified type
        ArrayList<NotificationMessage> messagesToRemove = new ArrayList<>();
        for (NotificationMessage message : activeMessages) {
            if (message.type.equals(type)) {
                message.label.remove();
                messagesToRemove.add(message);
            }
        }
        activeMessages.removeAll(messagesToRemove);
        updateMessagePositions();
    }

    public void hideAllMessages() {
        // Remove all messages and their labels
        for (NotificationMessage message : activeMessages) {
            message.label.remove();
        }
        activeMessages.clear();
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
        if (worldTimer % 30 == 0 && worldTimer != Constants.TIME_LIMIT && !satUpdateOnce) {
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
            int achievementBonus = achievementManager.calculateAchievementBonus();
            stats.increaseSatisfaction(achievementBonus); // Add achievement bonuses to final score
            game.setScreen(new EndScreen(game, music, stats, PlayerStats.getUsername(), achievementManager.getUnlockedAchievements()));
        }
    }

}
