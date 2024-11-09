package com.UniSim.game.Buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public abstract class Building {

    protected String name;
    protected float cost;
    protected Texture texture;
    protected float width;
    protected float height;

    private float lakeBonus;




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

    abstract public String getType();
}
