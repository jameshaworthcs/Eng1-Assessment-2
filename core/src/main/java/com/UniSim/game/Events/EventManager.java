package com.UniSim.game.Events;

import java.util.Random;

import com.UniSim.game.Hud;
import com.UniSim.game.Stats.PlayerStats;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class EventManager {
    private Hud hud;
    private PlayerStats playerStats;
    private Random random;
    private Event[] events;

    public EventManager(Hud hud) {
        this.hud = hud;
        this.playerStats = hud.stats;
        this.random = new Random();
        this.events = new Event[] {
                new Event(Event.EventType.POSITIVE, "Positive event", 5, 100, "assets/music/testSound.mp3"),
                new Event(Event.EventType.NEGATIVE, "Negative event", -3, -50, "assets/music/testSound.mp3"),
                new Event(Event.EventType.NEUTRAL, "Neutral event", 0, 0, "assets/music/testSound.mp3")
                // Add more events as needed
        };
        scheduleNextEvent();
    }

    /**
     * Schedules the next event to occur at a random interval
     */
    private void scheduleNextEvent() {
        int delay = 10 + random.nextInt(9) * 10;
        Timer.schedule(new Task() {
            @Override
            public void run() {
                triggerEvent();
                scheduleNextEvent();
            }
        }, delay);
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
     * @return A random event from the predefined events.
     */
    private Event chooseRandomEvent() {
        return events[random.nextInt(events.length)];
    }
}
