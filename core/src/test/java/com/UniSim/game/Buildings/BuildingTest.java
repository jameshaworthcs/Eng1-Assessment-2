package com.UniSim.game.Buildings;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BuildingTest {
    private Building basicBuilding;
    private Building texturedBuilding;

    @BeforeEach
    public void setUp() {
        basicBuilding = new Building("Library", 100, 50);
        texturedBuilding = new Building("Dorm", 1000, 100);
    }

    @Test
    public void testBasicBuildingProperties() {
        assertEquals("Library", basicBuilding.getName(), "Building name should match");
        assertEquals(100, basicBuilding.getCost(), "Building cost should match");
        assertEquals(50, basicBuilding.getCapacity(), "Building capacity should match");
        assertEquals(100, basicBuilding.getHealth(), "Initial health should be 100");
        assertTrue(basicBuilding.isOperational(), "Building should be operational when created");
    }

    @Test
    public void testTexturedBuildingProperties() {
        assertEquals("Dorm", texturedBuilding.getName(), "Building name should match");
        assertEquals(1000f, texturedBuilding.getCost(), "Building cost should match");
        assertEquals(100, texturedBuilding.getCapacity(), "Building capacity should match");
        assertEquals(100, texturedBuilding.getHealth(), "Initial health should be 100");
        assertTrue(texturedBuilding.isOperational(), "Building should be operational when created");
    }

    @Test
    public void testDamageAndRepair() {
        basicBuilding.damage(30);
        assertEquals(70, basicBuilding.getHealth(), "Health should decrease by damage amount");
        assertTrue(basicBuilding.isOperational(), "Building should still be operational");

        basicBuilding.damage(70);
        assertEquals(0, basicBuilding.getHealth(), "Health should not go below 0");
        assertFalse(basicBuilding.isOperational(), "Building should not be operational at 0 health");

        basicBuilding.repair(50);
        assertEquals(50, basicBuilding.getHealth(), "Health should increase after repair");
        assertTrue(basicBuilding.isOperational(), "Building should be operational after repair");
    }

    @Test
    public void testUpgrade() {
        int initialCapacity = basicBuilding.getCapacity();
        basicBuilding.upgrade();
        assertEquals((int)(initialCapacity * 1.5), basicBuilding.getCapacity(), 
            "Capacity should increase by 50% after upgrade");
    }

    @Test
    public void testBuildingType() {
        assertEquals("Building", basicBuilding.getType(), "Default building type should be 'Building'");
        assertEquals("Building", texturedBuilding.getType(), "Default building type should be 'Building'");
    }
} 