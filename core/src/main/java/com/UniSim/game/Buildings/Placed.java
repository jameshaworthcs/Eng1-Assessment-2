package com.UniSim.game.Buildings;

import com.UniSim.game.Hud;
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
import static com.UniSim.game.Hud.*;

import java.util.ArrayList;

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

    private boolean isPressed;




    public Placed(String name, float x, float y, float width, float height, Stage stage) {
        this.name = name;
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.interactButton = interactButton;
        this.isInteractable = isInteractable;
        this.stage = stage;
        this.isPressed = false;


        //stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())); // Initialize the stage
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        this.cornerPosition = new Vector2(position.x - width / 2, position.y - height / 2);
        initializeInteractButton(skin);
    }
    private void initializeInteractButton(Skin skin) {
        interactButton = new TextButton("Interact", skin);
        interactButton.setPosition(position.x, position.y + height / 2); // Position above the building
        interactButton.setVisible(false); // Initially hidden
        interactButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                isPressed = true;



            }
        });
        stage.addActor(interactButton);
    }

    public String updateInteraction(Vector2 playerPosition, OrthographicCamera camera) {
        float distance = playerPosition.dst(position);
        isInteractable = distance < (width + 50); // Set proximity range (adjust as needed)
        interactButton.setVisible(isInteractable);

        if (isInteractable) {
            // Convert world position to screen position for the button
            Vector3 screenPosition = camera.project(new Vector3(position.x, position.y + height / 2, 0));
            interactButton.setPosition(screenPosition.x, screenPosition.y);
        }
        if (isPressed){
            isPressed = false;
            return name;
        }
        return "none";
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
