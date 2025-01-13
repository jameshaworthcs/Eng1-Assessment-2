package com.UniSim.game.Player;

public class Player {
    private int health;
    private int score;
    private boolean alive;

    public Player() {
        this.health = 100;
        this.score = 0;
        this.alive = true;
    }

    public int getHealth() {
        return health;
    }

    public int getScore() {
        return score;
    }

    public boolean isAlive() {
        return alive;
    }

    public void takeDamage(int damage) {
        health = Math.max(0, health - damage);
        if (health == 0) {
            alive = false;
        }
    }

    public void addScore(int points) {
        score += points;
    }
} 