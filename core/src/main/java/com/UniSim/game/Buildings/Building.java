package com.UniSim.game.Buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
/**
 * Base class for all buildings in UniSim.
 * Defines common properties and behaviors for:
 * - Academic buildings
 * - Food services
 * - Accommodation
 * - Recreational facilities
 * - Workplaces
 */
public abstract class Building {

    // Basic properties
    protected String name;      // Building name
    protected float cost;       // Construction cost
    protected Texture texture;  // Visual representation
    protected float width;      // Building width
    protected float height;     // Building height

    // Placement bonuses
    private float lakeBonus;    // Bonus for lakeside placement

    /**
     * Creates a new building with specified attributes.
     * Loads the building's texture and sets its properties.
     *
     * @param name Building's display name
     * @param cost Construction cost in currency
     * @param picture Texture file path
     * @param lakeBonus Bonus for lake proximity
     * @param width Building width in units
     * @param height Building height in units
     */
    public Building(String name, float cost, String picture, float lakeBonus, float width, float height) {
        this.cost = cost;
        this.name = name;
        this.lakeBonus = lakeBonus;
        this.width = width;
        this.height = height;
        
        try {
            this.texture = new Texture(picture);
        } catch (Exception e) {
            Gdx.app.error("BuildingList", "Error loading texture: " + picture, e);
        }
    }

    /**
     * Gets the building's category.
     * Used for:
     * - Interaction handling
     * - Effect application
     * - UI display
     *
     * @return Building type identifier
     */
    abstract public String getType();
}
