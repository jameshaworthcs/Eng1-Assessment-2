package com.UniSim.game.Buildings;

import com.UniSim.game.Hud;
import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.UniSim.game.Constants.PPM;
import static com.UniSim.game.Hud.*;

import java.util.ArrayList;

/**
 * Represents a placed building in the game world. The class handles the position,
 * size, and interaction logic for buildings, including showing interaction buttons
 * when the player is nearby.
 */
public class Placed {

    protected String name;
    protected Vector2 position;
    protected Vector2 cornerPosition;
    protected float width;
    protected float height;
    private TextButton interactButton;
    private boolean isInteractable;
    private Stage stage;
    private Skin skin;
    private String type;

    private boolean isPressed;
    private float cooldownTimer;

    String buttonText;

    private Vector3 buildingScreenPosition = new Vector3();

    /**
     * Constructor to initialize a placed building in the game.
     *
     * @param name      The name of the building.
     * @param x         The x-coordinate of the building's position.
     * @param y         The y-coordinate of the building's position.
     * @param width     The width of the building.
     * @param height    The height of the building.
     * @param stage     The stage where UI elements (like buttons) are displayed.
     */
    public Placed(String name, float x, float y, float width, float height, Stage stage) {
        this.name = name;
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.interactButton = interactButton;
        this.isInteractable = isInteractable;
        this.stage = stage;
        this.isPressed = false;
        this.cooldownTimer = 0;



        //this.stage = new Stage(new FitViewport(1280, 720));



        //this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())); // Initialize the stage
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.cornerPosition = new Vector2(position.x - width / 2, position.y - height / 2);
        this.type = getType();

        switch (type) {
            case "Accommodation":
                this.buttonText = "Sleep";
                break;
            case "Workplace":
                this.buttonText = "Work";
                break;
            case "Academic":
                this.buttonText = "Learn";
                break;
            case "Food":
                this.buttonText = "Eat";
                break;
            case "Recreational":
                this.buttonText = "Relax";
        }

        initializeInteractButton(skin);
    }

    /**
     * Initializes the interact button, positioning it above the building and
     * setting its properties.
     *
     * @param skin The skin used for the UI components.
     */
    private void initializeInteractButton(Skin skin) {

        interactButton = new TextButton(buttonText, skin);
        interactButton.setPosition(position.x, position.y + height / 2); // Position above the building
        interactButton.setSize(80, 30);

        interactButton.setVisible(false); // Initially hidden
        interactButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(cooldownTimer<=0){
                    isPressed = true;
                    cooldownTimer = 10;}



            }
        });
        stage.addActor(interactButton);
    }

    /**
     * Updates the interaction logic, checking if the player is close enough
     * to interact with the building, and updating the button visibility and text.
     *
     * @param playerPosition The position of the player in the game world.
     * @param camera         The camera used to convert world coordinates to screen coordinates.
     * @param deltaTime      The time elapsed since the last update.
     * @return The building object if the interaction was successful, otherwise null.
     */
    public Building updateInteraction(Vector2 playerPosition, OrthographicCamera camera, float deltaTime) {

        float distance = playerPosition.dst(position);
        isInteractable = distance < ((width / 2) + 50 / PPM); // Set proximity range (adjust as needed)
        interactButton.setVisible(isInteractable);

        if (isInteractable) {
            // Convert world position to screen position for the button, factoring in screen size
            Vector3 screenPosition = camera.project(new Vector3(position.x, position.y + height / 2, 0));

            // Adjust interactButton to match stage coordinates
            interactButton.setPosition(screenPosition.x / stage.getViewport().getScreenWidth() * stage.getWidth(),
                screenPosition.y / stage.getViewport().getScreenHeight() * stage.getHeight());
        }

        if (cooldownTimer > 0) {
            cooldownTimer -= deltaTime;
            interactButton.setText(String.format("Wait %.1f", Math.max(0, cooldownTimer)));
        } else {
            interactButton.setText(buttonText);
        }

        if (isPressed) {
            isPressed = false;
            return getPlacedBuilding();
        }
        return null;
    }

    /**
     * Retrieves the building corresponding to this placed object by name.
     *
     * @return The building associated with this placed instance.
     * @throws RuntimeException if no building with the specified name is found.
     */
    public Building getPlacedBuilding(){
        ArrayList<Building> allBuildings = BuildingManager.combineBuildings();

        for (Building building : allBuildings) {
            if (building.name.equals(this.name)) {
                return building;
            }
        }
        throw new RuntimeException("No building with name " + this.name + " found");
    }

    /**
     * Retrieves the texture associated with this building.
     *
     * @return The texture of the building.
     */
    private Texture getTexture() {
        ArrayList<Building> allBuildings = BuildingManager.combineBuildings();

        for (Building building : allBuildings) {
            if (building.name.equals(this.name)) {
                return building.texture;
            }
        }
        return null;
    }

    /**
     * Draws the building
     *
     * @param batch The SpriteBatch used to draw the building.
     */
    public void drawBuilding(SpriteBatch batch) {
        batch.draw(getTexture(), this.cornerPosition.x, this.cornerPosition.y, width, height);

    }

    /**
     * Checks whether this building overlaps with another placed building.
     *
     * @param p The other placed building to check for overlap.
     * @return true if the buildings overlap, otherwise false.
     */
    public boolean overlaps (Placed p) {
        return this.cornerPosition.x < p.cornerPosition.x + p.width
            && this.cornerPosition.x + width > p.cornerPosition.x
            && this.cornerPosition.y < p.cornerPosition.y + p.height
            && this.cornerPosition.y + height > p.cornerPosition.y;
    }

    /**
     * Retrieves the type of this building (e.g., "Accommodation," "Workplace").
     *
     * @return The type of the building.
     */
    private String getType(){
        ArrayList<Building> allBuildings = BuildingManager.combineBuildings();

        for (Building building : allBuildings) {
            if (building.name.equals(this.name)) {
                return building.getType();
            }
        }
        return "null";
    }
}
