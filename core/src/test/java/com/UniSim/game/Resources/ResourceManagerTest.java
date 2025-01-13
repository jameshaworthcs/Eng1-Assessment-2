package com.UniSim.game.Resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResourceManagerTest {
    private ResourceManager resourceManager;

    @BeforeEach
    public void setUp() {
        resourceManager = new ResourceManager();
    }

    @Test
    public void testInitialResources() {
        assertEquals(1000, resourceManager.getMoney(), "Initial money should be 1000");
        assertEquals(0, resourceManager.getStudents(), "Initial students should be 0");
        assertEquals(0, resourceManager.getStaff(), "Initial staff should be 0");
    }

    @Test
    public void testAddResources() {
        resourceManager.addMoney(500);
        assertEquals(1500, resourceManager.getMoney(), "Money should increase by 500");

        resourceManager.addStudents(10);
        assertEquals(10, resourceManager.getStudents(), "Students should increase by 10");

        resourceManager.addStaff(5);
        assertEquals(5, resourceManager.getStaff(), "Staff should increase by 5");
    }

    @Test
    public void testSpendMoney() {
        // Test successful spending
        assertTrue(resourceManager.spendMoney(500), "Should be able to spend 500 when having 1000");
        assertEquals(500, resourceManager.getMoney(), "Money should decrease by 500");

        // Test unsuccessful spending
        assertFalse(resourceManager.spendMoney(1000), "Should not be able to spend 1000 when having 500");
        assertEquals(500, resourceManager.getMoney(), "Money should remain unchanged after failed spend");
    }

    @Test
    public void testResourceLimits() {
        // Test student limits
        assertTrue(resourceManager.addStudents(900), "Should be able to add 900 students");
        assertEquals(900, resourceManager.getStudents(), "Students should be 900");
        assertFalse(resourceManager.addStudents(200), "Should not be able to exceed student limit");
        assertEquals(900, resourceManager.getStudents(), "Students should remain at 900");

        // Test staff limits
        assertTrue(resourceManager.addStaff(90), "Should be able to add 90 staff");
        assertEquals(90, resourceManager.getStaff(), "Staff should be 90");
        assertFalse(resourceManager.addStaff(20), "Should not be able to exceed staff limit");
        assertEquals(90, resourceManager.getStaff(), "Staff should remain at 90");
    }
} 