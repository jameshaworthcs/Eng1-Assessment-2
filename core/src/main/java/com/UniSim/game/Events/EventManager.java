package com.UniSim.game.Events;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EventManager {
    private List<GameEvent> events;
    private Random random;

    public EventManager() {
        this.events = new ArrayList<>();
        this.random = new Random();
    }

    public GameEvent createEvent(String name, String description, int impact) {
        GameEvent event = new GameEvent(name, description, impact);
        events.add(event);
        return event;
    }

    public void triggerEvent(GameEvent event) {
        if (event != null && events.contains(event)) {
            event.setActive(true);
        }
    }

    public void resolveEvent(GameEvent event) {
        if (event != null && events.contains(event)) {
            event.setActive(false);
        }
    }

    public boolean hasActiveEvents() {
        return events.stream().anyMatch(GameEvent::isActive);
    }

    public int getTotalEvents() {
        return events.size();
    }

    public GameEvent generateRandomEvent() {
        String[] eventNames = {"Funding Cut", "Research Grant", "Student Protest", "Alumni Donation"};
        String[] eventDescriptions = {
            "University funding has been reduced",
            "Received a research grant",
            "Students are protesting",
            "Alumni made a generous donation"
        };
        int[] eventImpacts = {-500, 1000, -200, 800};

        int index = random.nextInt(eventNames.length);
        return createEvent(eventNames[index], eventDescriptions[index], eventImpacts[index]);
    }

    /**
     * Updates the event manager state.
     * This method is called every frame to handle any time-based event logic.
     */
    public void update() {
        // Check for any events that need to be triggered
        if (random.nextFloat() < 0.001f && !hasActiveEvents()) { // 0.1% chance per frame to trigger a random event
            GameEvent event = generateRandomEvent();
            triggerEvent(event);
        }
    }
}
