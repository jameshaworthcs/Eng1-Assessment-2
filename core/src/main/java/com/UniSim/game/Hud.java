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
 * Manages the game's heads-up display (HUD) and UI elements.
 * Handles displaying and updating:
 * - Player stats (satisfaction, currency, fatigue)
 * - Game timer and countdown
 * - Notification messages and popups
 * - Achievement notifications
 */
public class Hud {
    public Stage stage;                // UI rendering stage
    private Viewport viewport;         // Screen viewport for UI scaling
    private Skin skin;                 // UI theme/styling
    private World world;               // Physics world reference

    private Integer worldTimer;        // Game time remaining
    private float timeCount;           // Time accumulator
    private boolean timeUp;            // Whether game time has expired
    private GameScreen gameScreen;     // Reference to main game screen
    private boolean satUpdateOnce;     // Track satisfaction update
    private boolean endOnce;           // Prevent multiple end triggers
    private UniSim game;               // Main game instance
    private Music music;               // Background music
    private AchievementManager achievementManager;  // Handles achievements

    Label countdownLabel;              // Displays time remaining
    Label timeLabel;                   // "TIME" text label
    public PlayerStats stats;          // Player's game statistics
    private ArrayList<StatsLabels> playerStatLabels;  // UI labels for stats
    private Label messageLabel;        // General message display

    private Queue<NotificationMessage> messageQueue;    // Pending notifications
    private ArrayList<NotificationMessage> activeMessages;  // Currently shown
    private static final float MESSAGE_DURATION = 5f;   // Message display time
    private static final float MESSAGE_SPACING = 30f;   // Pixels between messages

    /**
     * Represents a notification message in the HUD.
     * Can be temporary or persistent, with different display types.
     */
    private class NotificationMessage {
        String text;           // Message content
        float timeLeft;        // Display time remaining
        Label label;          // UI label component
        String type;          // Message category
        boolean isPersistent; // Whether message stays until hidden

        /**
         * Creates a new notification message.
         *
         * @param text Message to display
         * @param duration How long to show (seconds)
         * @param label UI label for rendering
         * @param type Category of message
         */
        NotificationMessage(String text, float duration, Label label, String type) {
            this.text = text;
            this.timeLeft = duration;
            this.label = label;
            this.type = type;
            this.isPersistent = type.equals("initialHint");
        }
    }

    /**
     * Creates a new HUD with all UI components.
     * Sets up the viewport, stats display, timer, and message system.
     *
     * @param sb Sprite batch for rendering
     * @param skin UI theme/styling
     * @param world Physics world reference
     * @param gameScreen Main game screen
     * @param game Main game instance
     * @param music Background music
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

    /**
     * Gets the player's current stats.
     * @return PlayerStats object with current game statistics
     */
    public PlayerStats getStats() {
        return stats;
    }

    /**
     * Initializes player stats display.
     * Creates and positions labels for each stat type.
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

    /**
     * Updates all player stat displays.
     * Called each frame to refresh UI with current values.
     */
    public void updateStats() {
        playerStatLabels.get(0).setText("BUILDINGS: " + stats.getBuildingCounter());
        playerStatLabels.get(1).setText("SATISFACTION: " + stats.getSatisfaction());
        playerStatLabels.get(2).setText("CURRENCY: " + String.format("$%.2f", stats.getCurrency()));
        playerStatLabels.get(3).setText("FATIGUE: " + stats.getFatigue() + "/50");
        playerStatLabels.get(4).setText("KNOWLEDGE: " + stats.getKnowledge());
    }

    /**
     * Sets up the game timer display.
     * Creates labels for countdown and configures their position.
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
     * Creates the message label for notifications.
     * Positions it at the bottom of the screen.
     */
    private void createMessageLabel(Skin skin) {
        messageLabel = new Label("", skin);
        messageLabel.setVisible(false);
        stage.addActor(messageLabel);
    }

    /**
     * Displays a temporary message in the HUD.
     * Message will fade after default duration.
     *
     * @param message Text to display
     */
    public void sendMessage(String message) {
        sendMessage(message, "default");
    }

    /**
     * Displays a message of specific type in the HUD.
     * Different types can have different behaviors.
     *
     * @param message Text to display
     * @param type Category of message
     */
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

    /**
     * Updates vertical positions of active messages.
     * Ensures proper spacing between messages.
     */
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

    /**
     * Main update method called each frame.
     * Updates timer, messages, and checks game end conditions.
     *
     * @param dt Time elapsed since last frame
     */
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

    /**
     * Updates active notifications.
     * Removes expired messages and updates timers.
     *
     * @param dt Time elapsed since last frame
     */
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

    /**
     * Gets current game timer value.
     * @return Seconds remaining in game
     */
    public float getWorldTimer() {
        return worldTimer;
    }

    /**
     * Checks if game time has expired.
     * @return true if time is up, false otherwise
     */
    public boolean isTimeUp() {
        return timeUp;
    }

    /**
     * Hides messages of a specific type.
     * Useful for clearing certain categories of messages.
     *
     * @param type Category of messages to hide
     */
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

    /**
     * Hides all active messages.
     * Clears both persistent and temporary messages.
     */
    public void hideAllMessages() {
        // Remove all messages and their labels
        for (NotificationMessage message : activeMessages) {
            message.label.remove();
        }
        activeMessages.clear();
    }

    /**
     * Shows a popup for a game event.
     * Displays event description and effects.
     *
     * @param event Event to display
     */
    public void showEventPopup(Event event) {
        sendMessage(event.getDescription());
    }

    /**
     * Updates player satisfaction.
     * Checks conditions and updates score periodically.
     *
     * @param dt Time elapsed since last frame
     */
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
     * Checks if game end conditions are met.
     * Transitions to end screen if necessary.
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
