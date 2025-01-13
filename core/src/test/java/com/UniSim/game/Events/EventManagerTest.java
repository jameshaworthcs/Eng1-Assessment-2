package com.UniSim.game.Events;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EventManagerTest {
    private EventManager eventManager;

    @BeforeEach
    public void setUp() {
        eventManager = new EventManager();
    }

    @Test
    public void testCreateEvent() {
        GameEvent event = eventManager.createEvent("Test Event", "Test Description", 10);
        assertNotNull(event, "Event should be created");
        assertEquals("Test Event", event.getName(), "Event name should match");
        assertEquals("Test Description", event.getDescription(), "Event description should match");
        assertEquals(10, event.getImpact(), "Event impact should match");
        assertFalse(event.isActive(), "Event should not be active when created");
    }

    @Test
    public void testTriggerAndResolveEvent() {
        GameEvent event = eventManager.createEvent("Test Event", "Test Description", 10);
        eventManager.triggerEvent(event);
        assertTrue(eventManager.hasActiveEvents(), "Should have active events after triggering");

        eventManager.resolveEvent(event);
        assertFalse(event.isActive(), "Event should be inactive after resolving");
        assertFalse(eventManager.hasActiveEvents(), "Should have no active events after resolving");
    }

    @Test
    public void testMultipleEvents() {
        eventManager.createEvent("Event 1", "Description 1", 10);
        eventManager.createEvent("Event 2", "Description 2", 20);
        assertEquals(2, eventManager.getTotalEvents(), "Should have created 2 events");
    }

    @Test
    public void testRandomEventGeneration() {
        GameEvent randomEvent = eventManager.generateRandomEvent();
        assertNotNull(randomEvent, "Random event should be created");
        assertFalse(randomEvent.isActive(), "Random event should not be active when created");
    }
} 