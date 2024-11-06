package com.UniSim.game.Buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class Placed {

    protected String name;
    protected Vector2 position;
    protected Vector2 cornerPosition;
    protected float width;
    protected float height;

    public Placed(String name, float x, float y, float width, float height) {
        this.name = name;
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.cornerPosition = new Vector2(position.x - width / 2, position.y - height / 2);
    }

    private Texture getTexture() {
        ArrayList<Building> allBuildings = BuildingManager.combineBuildings();

        for (Building building : allBuildings) {
            if (building.name.equals(this.name)) {
                return building.texture;
            }
        }
        return null;
    }

    public void drawBuilding(SpriteBatch batch) {
        batch.draw(getTexture(), this.cornerPosition.x, this.cornerPosition.y, width, height);
    }

    public boolean overlaps (Placed p) {
        return this.cornerPosition.x < p.cornerPosition.x + p.width
            && this.cornerPosition.x + width > p.cornerPosition.x
            && this.cornerPosition.y < p.cornerPosition.y + p.height
            && this.cornerPosition.y + height > p.cornerPosition.y;
    }
}
