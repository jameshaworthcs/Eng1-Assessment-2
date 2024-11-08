package com.UniSim.game.Buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Building {

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


    /*
    need to find a way to match the box with the building (idk if needed)
    true if in lake bonus
    class that holds classes with position and see if it has a bonus. if so then good.
     */

}
