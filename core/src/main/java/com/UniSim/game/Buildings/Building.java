package com.UniSim.game.Buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
/**
 * Abstract Building class representing a general structure in the game with basic properties
 * like name, cost, and texture. It also defines the bonuses associated with the building and
 * provides a method to get the building's type.
 */
public abstract class Building {

    protected String name;
    protected float cost;
    protected Texture texture;
    protected float width;
    protected float height;

    private float lakeBonus;

    /**
     * Constructor to initialize a Building with specified properties including name, cost, texture,
     * lake bonus, width, and height.
     * @param name Name of the building.
     * @param cost Cost of the building in in-game currency.
     * @param picture Path to the texture image file for the building.
     * @param lakeBonus Bonus granted when the building is placed near a lake.
     * @param width Width of the building in game units.
     * @param height Height of the building in game units.
     */
    public Building(String name, float cost, String picture, float lakeBonus, float width, float height) {
        this.cost = cost;
        this.name = name;
        this.lakeBonus = lakeBonus;

        this.width = width;
        this.height = height;
        try {
            this.texture = new Texture(picture);
        }catch (Exception e) {
            Gdx.app.error("BuildingList", "Error loading texture: " + picture, e);
        }
    }

    /**
     * Abstract method to get the type of the building. Each building type provideS its own
     * implementation of this method.
     * @return The type of the building as a String.
     */
    abstract public String getType();
}
