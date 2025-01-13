package com.UniSim.game.Buildings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;

public class Building {
    protected String name;
    protected float cost;
    protected float width;
    protected float height;
    protected float lakeBonus;
    protected Texture texture;
    protected int capacity;
    protected int health;
    protected boolean operational;

    public Building(String name, float cost, String picture, float lakeBonus, float width, float height) {
        this.name = name;
        this.cost = cost;
        this.width = width;
        this.height = height;
        this.lakeBonus = lakeBonus;
        this.texture = new Texture(Gdx.files.internal(picture));
        this.health = 100;
        this.operational = true;
    }

    public Building(String name, int cost, int capacity) {
        this.name = name;
        this.cost = cost;
        this.capacity = capacity;
        this.health = 100;
        this.operational = true;
    }

    public String getName() {
        return name;
    }

    public float getCost() {
        return cost;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getHealth() {
        return health;
    }

    public boolean isOperational() {
        return operational;
    }

    public void damage(int amount) {
        health = Math.max(0, health - amount);
        operational = health > 0;
    }

    public void repair(int amount) {
        health = Math.min(100, health + amount);
        operational = true;
    }

    public void upgrade() {
        capacity = (int)(capacity * 1.5);
    }

    public String getType() {
        return "Building";
    }

    public Texture getTexture() {
        return texture;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getLakeBonus() {
        return lakeBonus;
    }
}
