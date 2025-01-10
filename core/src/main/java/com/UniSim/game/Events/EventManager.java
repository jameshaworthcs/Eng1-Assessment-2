package com.UniSim.game.Events;

import java.util.Random;

import com.UniSim.game.Hud;
import com.UniSim.game.Stats.PlayerStats;

public class EventManager {
    private Hud hud;
    private PlayerStats playerStats;
    private Random random;
    private Event[] events;
    private int nextEventTime;

    public EventManager(Hud hud) {
        this.hud = hud;
        this.playerStats = hud.stats;
        this.random = new Random();
        this.events = new Event[] {
                new Event(Event.EventType.POSITIVE, "Positive event", 5, 100, "music/positive.mp3"),
                new Event(Event.EventType.NEGATIVE, "Negative event", -3, -50, "music/negative.mp3"),
                new Event(Event.EventType.NEUTRAL, "Neutral event", 0, 0, "music/neutral.mp3")
                // More events can be added here
        };
        scheduleNextEvent();
    }

    /**
     * Schedules the next event to occur at a random interval
     */
    private void scheduleNextEvent() {
        int delay = 10; // Example delay in seconds
        nextEventTime = (int) (hud.getWorldTimer() - delay);
    }

    public void update() {
        if (hud.getWorldTimer() <= nextEventTime) {
            triggerEvent();
            scheduleNextEvent();
        }
    }

    /**
     * Triggers a random event and applies its effects to the player's stats.
     */
    private void triggerEvent() {
        Event event = chooseRandomEvent();
        event.applyEffects(playerStats);
        hud.showEventPopup(event);
        event.playSound();
    }

    /**
     * Chooses a random event from the predefined events.
     *
     *
     * @return A random event from the predefined events.
     */
    private Event chooseRandomEvent() {
        return events[random.nextInt(events.length)];
    }
}
