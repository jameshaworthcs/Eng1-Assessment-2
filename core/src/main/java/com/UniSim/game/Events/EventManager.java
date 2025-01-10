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
                // Positive Events
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

                // Negative Events
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

                // Neutral Events
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

    private void scheduleNextEvent() {
        // Random delay between 15 and 30 seconds
        int delay = 15 + random.nextInt(16);
        nextEventTime = (int) (hud.getWorldTimer() - delay);
    }

    public void update() {
        if (hud.getWorldTimer() <= nextEventTime) {
            triggerEvent();
            scheduleNextEvent();
        }
    }

    private void triggerEvent() {
        Event event = chooseRandomEvent();
        event.applyEffects(playerStats);
        hud.showEventPopup(event);
        event.playSound();
    }

    private Event chooseRandomEvent() {
        return events[random.nextInt(events.length)];
    }
}
