package com.UniSim.game.Player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player();
    }

    @Test
    public void testInitialStats() {
        assertEquals(100, player.getHealth(), "Initial health should be 100");
        assertEquals(0, player.getScore(), "Initial score should be 0");
        assertTrue(player.isAlive(), "Player should be alive initially");
    }

    @Test
    public void testDamageCalculation() {
        int initialHealth = player.getHealth();
        int damage = 30;
        player.takeDamage(damage);
        assertEquals(initialHealth - damage, player.getHealth(), "Health should decrease by damage amount");
    }

    @Test
    public void testScoreSystem() {
        int scoreIncrease = 50;
        player.addScore(scoreIncrease);
        assertEquals(scoreIncrease, player.getScore(), "Score should increase correctly");
    }

    @Test
    public void testPlayerDeath() {
        player.takeDamage(100); // Lethal damage
        assertFalse(player.isAlive(), "Player should die when health reaches 0");
        assertEquals(0, player.getHealth(), "Health should not go below 0");
    }
} 