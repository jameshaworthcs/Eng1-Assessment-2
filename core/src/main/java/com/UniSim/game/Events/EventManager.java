package com.UniSim.game.Events;

import java.util.Random;

import com.UniSim.game.Hud;
import com.UniSim.game.Stats.PlayerStats;

/**
 * Manages random events that occur during gameplay to add variety and challenge.
 * Handles scheduling, triggering, and applying effects of various game events.
 * Events can affect player stats, currency, and satisfaction levels.
 */
public class EventManager {
    private Hud hud;                  // Game HUD for displaying events
    private PlayerStats playerStats;   // Player stats to modify with events
    private Random random;            // RNG for event timing and selection
    private Event[] events;           // Pool of possible events
    private int nextEventTime;        // When the next event will trigger

    /**
     * Creates a new event manager and initializes the event pool.
     * Sets up positive, negative, and neutral events with their effects.
     */
    public EventManager(Hud hud) {
        this.hud = hud;
        this.playerStats = hud.stats;
        this.random = new Random();
        this.events = new Event[] {
                // Positive Events - Boost satisfaction and/or currency
                new Event(Event.EventType.POSITIVE, 
                    "Research Grant Awarded!\nThe university received additional funding. (+$500, +5 satisfaction)", 
                    5, 500, "music/positive.mp3"),
                new Event(Event.EventType.POSITIVE, 
                    "Successful Student Society Event!\nA major student event was a huge success. (+$200, +8 satisfaction)", 
                    8, 200, "music/positive.mp3"),
                new Event(Event.EventType.POSITIVE, 
                    "Alumni Donation!\nA generous alumni made a donation. (+$1000, +3 satisfaction)", 
                    3, 1000, "music/positive.mp3"),
                new Event(Event.EventType.POSITIVE, 
                    "High Student Attendance!\nLecture attendance is up. (+$0, +10 satisfaction)", 
                    10, 0, "music/positive.mp3"),

                // Negative Events - Reduce satisfaction and/or currency
                new Event(Event.EventType.NEGATIVE, 
                    "Building Maintenance Required!\nUnexpected repairs needed. (-$300, -5 satisfaction)", 
                    -5, -300, "music/negative.mp3"),
                new Event(Event.EventType.NEGATIVE, 
                    "Student Protest!\nStudents are unhappy with facilities. (-$0, -8 satisfaction)", 
                    -8, 0, "music/negative.mp3"),
                new Event(Event.EventType.NEGATIVE, 
                    "Budget Cuts!\nGovernment reduced university funding. (-$800, -3 satisfaction)", 
                    -3, -800, "music/negative.mp3"),
                new Event(Event.EventType.NEGATIVE, 
                    "Failed Equipment!\nSome academic equipment needs replacement. (-$400, -4 satisfaction)", 
                    -4, -400, "music/negative.mp3"),

                // Neutral Events - Add flavor without direct stat impact
                new Event(Event.EventType.NEUTRAL, 
                    "Campus Tour Day!\nProspective students are visiting. (No immediate effect)", 
                    0, 0, "music/neutral.mp3"),
                new Event(Event.EventType.NEUTRAL, 
                    "Student Exchange Program!\nNew international students arrived. (No immediate effect)", 
                    0, 0, "music/neutral.mp3"),
                new Event(Event.EventType.NEUTRAL, 
                    "Academic Conference!\nScholars gathering for discussions. (No immediate effect)", 
                    0, 0, "music/neutral.mp3")
        };
        scheduleNextEvent();
    }

    /**
     * Schedules the next random event.
     * Events occur between 15-30 seconds apart.
     */
    private void scheduleNextEvent() {
        int delay = 15 + random.nextInt(16);
        nextEventTime = (int) (hud.getWorldTimer() - delay);
    }

    /**
     * Checks if it's time for a new event and triggers one if needed.
     * Should be called each frame to maintain event timing.
     */
    public void update() {
        if (hud.getWorldTimer() <= nextEventTime) {
            triggerEvent();
            scheduleNextEvent();
        }
    }

    /**
     * Triggers a random event, applying its effects and showing feedback.
     * Plays the event's sound and displays a popup message.
     */
    private void triggerEvent() {
        Event event = chooseRandomEvent();
        event.applyEffects(playerStats);
        hud.showEventPopup(event);
        event.playSound();
    }

    /**
     * Picks a random event from the event pool.
     */
    private Event chooseRandomEvent() {
        return events[random.nextInt(events.length)];
    }
}
