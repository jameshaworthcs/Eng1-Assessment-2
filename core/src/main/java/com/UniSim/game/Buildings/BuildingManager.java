package com.UniSim.game.Buildings;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.Viewport;

import static com.UniSim.game.Constants.*;

public class BuildingManager {
    private Stage stage;
    private Skin skin;
    private World world;
    private Window buildingWindow;
    private boolean isWindowOpen = false;

    private static ArrayList<Accommodation> accommodations;
    private static ArrayList<Academic> academics;
    private static ArrayList<Recreational> recreationals;

    private ArrayList<Placed> placed;

    private boolean isPlacingBuilding;
    private Label messageLabel;

    private Building placingBuilding;

    private TiledMap tiledMap;

    public BuildingManager(Stage stage, Skin skin, World world, TiledMap tiledMap) {
        accommodations = new ArrayList<Accommodation>();
        academics = new ArrayList<Academic>();
        recreationals = new ArrayList<Recreational>();
        placed =  new ArrayList<Placed>();

        this.stage = stage;
        this.skin = skin;
        this.world = world;
        this.tiledMap = tiledMap;
        this.isWindowOpen = false;
        this.placingBuilding = null;

        makeBuildingTypes();

        //createBuildingButton();
        createMessageLabel(); // Initialize the message label
    }

    // Create the button in the game that opens the building selection window
//    private void createBuildingButton() {
//        TextButton buildingButton = new TextButton("Building", skin);
//        buildingButton.setSize(200, 50);
//        buildingButton.setPosition(10, 10);
//
//        buildingButton.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                if (!isWindowOpen) {  // Prevent opening multiple windows
//                    showBuildingSelectionWindow();
//                }
//            }
//        });
//
//        stage.addActor(buildingButton);
//    }

    private void makeBuildingTypes() {
        accommodations.add(new Accommodation("Langwith", 24000f, "Accommodation_3.png", 4f, 64f, 64f, 200));
        recreationals.add(new Recreational("Food Hall", 50000f, "accommodation_3.png", 2f, 120f,120f));
        academics.add(new Academic("Lecture Theater", 20000f, "accommodation_3.png", 1.5f, 100f, 100f, 50));
    }

    // Create the message label for invalid placement
    private void createMessageLabel() {
        messageLabel = new Label("", skin);
        messageLabel.setPosition(10, 20); // Set the position for the message
        messageLabel.setVisible(false); // Initially hidden
        stage.addActor(messageLabel); // Add the label to the stage
    }

    private void showBuildingSelectionWindow() {
        if (buildingWindow == null) {
            buildingWindow = new Window("Select Building Type", skin);
            buildingWindow.setSize(450, 600);
            buildingWindow.setPosition(1700, 400);
        } else {
            buildingWindow.clear(); // Clear existing content
        }

        isWindowOpen = true;

        TextButton accommodationButton = new TextButton("Accommodation", skin);
        accommodationButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAccommodationOptions();
            }
        });
        buildingWindow.add(accommodationButton).padBottom(10).row();

        TextButton academicButton = new TextButton("Academic", skin);
        academicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAcademicOptions();
            }
        });
        buildingWindow.add(academicButton).padBottom(10).row();

        TextButton recreationalButton = new TextButton("Recreational", skin);
        recreationalButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showRecreationalOptions();
            }
        });
        buildingWindow.add(recreationalButton).padBottom(10).row();

        if (!stage.getActors().contains(buildingWindow, true)) {
            stage.addActor(buildingWindow);
        }
    }

    private void setScrollAndBack(Table buildingTable){
        // Create a ScrollPane and set it to only show vertical scrollbars
        ScrollPane scrollPane = new ScrollPane(buildingTable, skin);
        scrollPane.setFadeScrollBars(false);         // Disable fade so scrollbars are always visible
        scrollPane.setScrollingDisabled(true, false); // Disable horizontal scrolling, enable vertical scrolling
        scrollPane.setForceScroll(false, true);       // Only force vertical scroll

        // Add the scroll pane to the window
        buildingWindow.add(scrollPane).expand().fill().row();

        // Add the Back button at the bottom
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showBuildingSelectionWindow();  // Go back to the main menu
            }
        });

        buildingWindow.add(backButton).padTop(20).row();  // Add the "Back" button at the bottom
    }

    private void showAccommodationOptions() {
        buildingWindow.clear();  // Clear previous content
        buildingWindow.add(new Label("Select Accommodation", skin)).padBottom(20).row();

        // Create a Table for building options
        Table buildingTable = new Table();

        // Add building options
        for (Accommodation accommodation : accommodations)
        {
            addBuildingOption(buildingTable, accommodation);
        }
        setScrollAndBack(buildingTable);
    }

    // Add Academic options
    private void showAcademicOptions() {
        buildingWindow.clear();
        buildingWindow.add(new Label("Select Academic Building", skin)).padBottom(20).row();

        Table buildingTable = new Table();
        for(Academic academic : academics) {
            addBuildingOption(buildingTable, academic);
        }
        setScrollAndBack(buildingTable);
    }

    // Add Food options
    private void showRecreationalOptions() {
        buildingWindow.clear();
        buildingWindow.add(new Label("Select Recreational Facility", skin)).padBottom(20).row();

        Table buildingTable = new Table();
        for(Recreational recreational : recreationals) {
            addBuildingOption(buildingTable, recreational);
        }
        setScrollAndBack(buildingTable);
    }

    private void addBuildingOption(Table buildingTable, Building building) {
        // Create the Image and maintain its aspect ratio
        Image buildingImage = new Image(building.texture);

        // Set maximum width and height (for display in the selection window)
        float maxWidth = 100;
        float maxHeight = 100;

        // Calculate aspect ratio
        float aspectRatio = (float) building.texture.getWidth() / building.texture.getHeight();

        // Adjust width and height to maintain the aspect ratio but within the max size
        float displayWidth, displayHeight;
        if (aspectRatio > 1) {
            displayWidth = maxWidth;
            displayHeight = maxWidth / aspectRatio;
        } else {
            displayHeight = maxHeight;
            displayWidth = maxHeight * aspectRatio;
        }

        // Set the size of the image for the selection window
        buildingImage.setSize(displayWidth, displayHeight);

        // Add the building image and label to the table
        buildingTable.add(buildingImage).size(displayWidth, displayHeight).padBottom(10).row();
        Label buildingLabel = new Label(building.name + " - " + String.format("$%.2f", building.cost)  , skin);
        buildingTable.add(buildingLabel).padBottom(10).row();

        // Create the Select button
        TextButton selectButton = new TextButton("Select", skin);
        selectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                placingBuilding = building;
                isPlacingBuilding = true;
                closeBuildingWindow();
            }
        });

        buildingTable.add(selectButton).padBottom(20).row();
    }

    private Vector3 snapToGrid(float x, float y) {
        float snappedX = Math.round(x / GRID_SIZE) * GRID_SIZE;
        float snappedY = Math.round(y / GRID_SIZE) * GRID_SIZE;
        return new Vector3(snappedX, snappedY, 0);
    }

    public void handleBuildingPlacement(SpriteBatch batch, OrthographicCamera camera, Viewport viewport) {
        if (isPlacingBuilding && placingBuilding != null) {
            Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePosition, viewport.getScreenX(), viewport.getScreenY(),
                    viewport.getScreenWidth(), viewport.getScreenHeight());

            batch.begin();
            batch.setColor(1, 1, 1, 0.5f);  // Semi-transparent

            // Draw the building using the custom width and height
            batch.draw(placingBuilding.texture, mousePosition.x - placingBuilding.width / 2,
                       mousePosition.y - placingBuilding.height / 2,
                       placingBuilding.width, placingBuilding.height);

            batch.setColor(1, 1, 1, 1);  // Reset to full opacity
            batch.end();

            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (!checkOverlap(mousePosition.x, mousePosition.y)) {
                    placed.add(new Placed(placingBuilding.name, mousePosition.x, mousePosition.y, placingBuilding.width, placingBuilding.height));
                    createBuildingBody(mousePosition.x, mousePosition.y, placingBuilding.width, placingBuilding.height); // Create Box2D body
                    isPlacingBuilding = false;
                    messageLabel.setVisible(false); // Hide error message after successful placement
                } else {
                    messageLabel.setText("Cannot place building here!");
                    messageLabel.setVisible(true); // Show error message
                }
            }
        }

        batch.begin();
        for (Placed building : placed) {
            // Draw placed buildings using the stored size
            building.drawBuilding(batch);
        }
        batch.end();
    }

    private boolean checkOverlap(float x, float y) {
        boolean overlap = false;
        Placed newBuildingPla = new Placed (placingBuilding.name, x, y, placingBuilding.width, placingBuilding.height);
        for (Placed building : placed) {
            if (newBuildingPla.overlaps(building)) {
                overlap =  true; // Overlap detected
                break;
            }
        }

        for (MapLayer layer : tiledMap.getLayers()) {
            if (layer.getObjects().getCount() > 0) { // Add any other relevant layer names here
                for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();

                    // Define object rectangle for overlap check
                    Placed temp = new Placed(
                        "Borders",
                        rect.getX() / PPM,
                        rect.getY() / PPM,
                        rect.getWidth() / PPM,
                        rect.getHeight() / PPM
                    );

                    // Check for overlap
                    if (newBuildingPla.overlaps(temp)) {
                        overlap = true; // Overlap detected
                        break;          // Stop further checks within this layer
                    }
                }
            }
            if (overlap) break; // Stop checking other layers if overlap is detected
        }

        return overlap;
    }

    private void createBuildingBody(float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((x + width / 2), (y + height / 2)); // Adjust position correctly for PPM
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body buildingBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox((width / 2), (height / 2));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;

        buildingBody.createFixture(fixtureDef);
        shape.dispose();
    }

    public static ArrayList<Building> combineBuildings() {
        ArrayList<Building> allBuildings = new ArrayList<>();
        allBuildings.addAll(accommodations);
        allBuildings.addAll(academics);
        allBuildings.addAll(recreationals);
        return allBuildings;
    }

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if (!isWindowOpen) {
                showBuildingSelectionWindow();
            } else {
                closeBuildingWindow();
            }
        }
    }

    public void closeBuildingWindow() {
        if (buildingWindow != null) {
            buildingWindow.remove();
        }
        isWindowOpen = false;
    }

    public void openBuildingWindow() {
        if (!isWindowOpen) {
            isWindowOpen = true;
            showBuildingSelectionWindow();
        }
    }
    public boolean getIsWindowOpen(){
        return isWindowOpen;
    }
}

class PlacedBuilding {
    public Texture texture;
    public Rectangle rectangle;

    public PlacedBuilding(Texture texture, float x, float y, float width, float height) {
        this.texture = texture;
        this.rectangle = new Rectangle(x, y, width, height);
    }
}
