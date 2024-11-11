package com.UniSim.game.Buildings;

import java.util.ArrayList;
import java.util.Objects;

import com.UniSim.game.Buildings.Types.Academic;
import com.UniSim.game.Buildings.Types.Accommodation;
import com.UniSim.game.Buildings.Types.Food;
import com.UniSim.game.Buildings.Types.Recreational;
import com.UniSim.game.Buildings.Types.Workplace;
import com.UniSim.game.Screens.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.math.Vector2;

import static com.UniSim.game.Constants.*;

/**
 * BuildingManager handles the creation, placement, and interaction with buildings in the game.
 * It manages different building types, their placement on the map, and player interactions.
 */
public class BuildingManager {
    private Stage stage;

    private Skin skin;
    private World world;
    private Window buildingWindow;
    private boolean isWindowOpen = false;

    private static ArrayList<Accommodation> accommodations;
    private static ArrayList<Academic> academics;
    private static ArrayList<Food> foods;
    private static ArrayList<Recreational> recreationals;
    private static ArrayList<Workplace> workplaces;

    private ArrayList<Placed> placed;

    private boolean isPlacingBuilding;
    private Label messageLabel;

    private Building placingBuilding;

    private TiledMap tiledMap;

    private GameScreen gameScreen;
    private TextButton interactButton;
    private boolean isInteractable;

    private Building buildingPressed;

    /**
     * Constructor initializes the BuildingManager with required game objects.
     * It sets up building types, stages, world, and maps for building placement and interaction.
     * @param stage The stage for UI elements.
     * @param skin The skin for UI elements.
     * @param world The physics world for Box2D interactions.
     * @param tiledMap The map containing the game world.
     * @param gameScreen The screen managing the gameplay.
     */
    public BuildingManager(Stage stage, Skin skin, World world, TiledMap tiledMap, GameScreen gameScreen) {
        accommodations = new ArrayList<Accommodation>();
        academics = new ArrayList<Academic>();
        foods = new ArrayList<Food>();
        recreationals = new ArrayList<Recreational>();
        workplaces= new ArrayList<Workplace>();
        placed =  new ArrayList<Placed>();

        this.gameScreen = gameScreen;
        this.stage = stage;
        this.skin = skin;
        this.world = world;
        this.tiledMap = tiledMap;
        this.isWindowOpen = false;
        this.placingBuilding = null;

        makeBuildingTypes();
    }

    /**
     * Updates the interactions with buildings, checking proximity to the player and returning a building if the player is near it.
     * @param playerPosition The player's current position in the world.
     * @param camera The camera to calculate the view.
     * @param deltaTime Time passed since the last frame.
     * @return The building the player is interacting with, or null if no interaction.
     */
    public Building updateBuildingInteractions(Vector2 playerPosition, OrthographicCamera camera, float deltaTime) {
        buildingPressed = null;
        for (Placed building : placed) {
            buildingPressed = building.updateInteraction(playerPosition, camera, deltaTime);
            if (!Objects.equals(buildingPressed, null)){
                break;
            }

        }

        return buildingPressed;
    }

    /**
     * Creates instances of various building types and adds them to respective lists.
     */
    private void makeBuildingTypes() {
        accommodations.add(new Accommodation("David Kato", 8000f, "accommodation_3.png", 4f, 64f, 64f, 10));
        foods.add(new Food("Piazza Restaurant", 5000f, "accommodation_3.png", 2f, 128f,128f, 200, 2, 2));
        recreationals.add(new Recreational("Glasshouse Bar", 5000f, "accommodation_3.png", 2f, 128f,128f, 500, 5, 5));
        academics.add(new Academic("Library", 1000f, "lectureroom.png", 1.5f, 96f, 96f, 5, 10));
        workplaces.add(new Workplace("Greggs", 5000f, "accommodation_3.png", 1.5f, 80f, 80f, 10, 1000));
    }

    /**
     * Displays the building selection window to allow the player to choose a building type.
     */
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

        TextButton foodButton = new TextButton("Food Hall", skin);
        foodButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFoodOptions();
            }
        });
        buildingWindow.add(foodButton).padBottom(10).row();

        TextButton recreationalButton = new TextButton("Recreational", skin);
        recreationalButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showRecreationalOptions();
            }
        });
        buildingWindow.add(recreationalButton).padBottom(10).row();

        // **Declare and initialize the Workplace Button**
        TextButton WorkplaceButton = new TextButton("Workplace", skin);
        WorkplaceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showWorkOptions();
            }
        });
        buildingWindow.add(WorkplaceButton).padBottom(10).row();

        // Add the Back button at the bottom
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeBuildingWindow();
                gameScreen.showFullMapView();
                gameScreen.hud.sendMessage("Press ENTER to go into build mode.");
            }
        });

        buildingWindow.add(backButton).padTop(100).row();  // Add the "Back" button at the bottom


        if (!stage.getActors().contains(buildingWindow, true)) {
            stage.addActor(buildingWindow);
        }
    }

    /**
     * Creates a scrollable window for the building options and a back button.
     * @param buildingTable The table containing building options.
     */
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

    /**
     * Displays the options for selecting an accommodation building.
     */
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

    /**
     * Displays the options for selecting a workplace building.
     */
    private void showWorkOptions(){
        buildingWindow.clear();
        buildingWindow.add(new Label("Select Workplace", skin)).padBottom(20).row();

        // Create a Table for building options
        Table buildingTable = new Table();

        // Add building options
        for (Workplace workplace : workplaces)
        {
            addBuildingOption(buildingTable, workplace);
        }
        setScrollAndBack(buildingTable);
    }

    /**
     * Displays the options for selecting an academic building.
     */
    private void showAcademicOptions() {
        buildingWindow.clear();
        buildingWindow.add(new Label("Select Academic Building", skin)).padBottom(20).row();

        Table buildingTable = new Table();
        for(Academic academic : academics) {
            addBuildingOption(buildingTable, academic);
        }
        setScrollAndBack(buildingTable);
    }

    /**
     * Displays the options for selecting a food hall.
     */
    private void showFoodOptions() {
        buildingWindow.clear();
        buildingWindow.add(new Label("Select Food Hall", skin)).padBottom(20).row();

        Table buildingTable = new Table();
        for(Food food : foods) {
            addBuildingOption(buildingTable, food);
        }
        setScrollAndBack(buildingTable);
    }

    /**
     * Displays the options for selecting a recreational facility.
     */
    private void showRecreationalOptions() {
        buildingWindow.clear();
        buildingWindow.add(new Label("Select Recreational Facility", skin)).padBottom(20).row();

        Table buildingTable = new Table();
        for(Recreational recreational : recreationals) {
            addBuildingOption(buildingTable, recreational);
        }
        setScrollAndBack(buildingTable);
    }

    /**
     * Adds a building to the table, displaying an image and selecting button.
     * @param buildingTable The table to add the building option to.
     * @param building The building to display.
     */
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

    /**
     * Snaps the given position to the nearest grid point to ensure buildings align with the grid.
     * @param x The x-coordinate to snap.
     * @param y The y-coordinate to snap.
     * @return The snapped position as a Vector3.
     */
    private Vector3 snapToGrid(float x, float y) {
        float snappedX = Math.round(x / GRID_SIZE) * GRID_SIZE;
        float snappedY = Math.round(y / GRID_SIZE) * GRID_SIZE;
        return new Vector3(snappedX, snappedY, 0);
    }

    /**
     * Handles the logic for placing buildings in the game, rendering of placed buildings and input handling.
     * @param batch The SpriteBatch for drawing buildings.
     * @param camera The camera to calculate the position for placing buildings.
     * @param viewport The viewport to map the camera's view.
     */
    public void handleBuildingPlacement(SpriteBatch batch, OrthographicCamera camera, Viewport viewport) {
        if (isPlacingBuilding && placingBuilding != null) {
            Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePosition , viewport.getScreenX(), viewport.getScreenY(),
                    viewport.getScreenWidth() , viewport.getScreenHeight());

            batch.begin();
            batch.setColor(1, 1, 1, 0.5f);  // Semi-transparent

            Vector3 snappedPosition = snapToGrid(mousePosition.x, mousePosition.y);

            // Draw the building using the custom width and height
            batch.draw(placingBuilding.texture, snappedPosition.x - placingBuilding.width / 2 / PPM,
                snappedPosition.y - placingBuilding.height / 2 / PPM,
                       placingBuilding.width / PPM, placingBuilding.height / PPM);

            batch.setColor(1, 1, 1, 1);  // Reset to full opacity
            batch.end();

            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (!checkOverlap(snappedPosition.x, snappedPosition.y)) {
                    if(canAffordBuilding()) {
                        placed.add(new Placed(placingBuilding.name, snappedPosition.x, snappedPosition.y, placingBuilding.width / PPM, placingBuilding.height / PPM, stage));
                        createBuildingBody(snappedPosition.x, snappedPosition.y, placingBuilding.width / PPM, placingBuilding.height / PPM); // Create Box2D body
                        gameScreen.hud.hideMessage(); // Hide error message after successful placement
                        showBuildingSelectionWindow();
                        gameScreen.hud.stats.takeOffBuildingCost(placingBuilding.cost);
                        gameScreen.hud.stats.incrementBuildingCounter();
                    }
                    else {

                        gameScreen.popUp("Cannot Afford Building", 4);
                        showBuildingSelectionWindow();
                    }
                    isPlacingBuilding = false;
                } else {

                    gameScreen.popUp("Cannot place building here!", 4);
                }
            }
        }

        batch.begin();
        for (Placed building : placed) {
            building.drawBuilding(batch);
        }
        batch.end();
    }

    /**
     * Checks if the player can afford the building being placed.
     * @return True if the player can afford the building, false otherwise.
     */
    private boolean canAffordBuilding() {
        return !(placingBuilding.cost > gameScreen.hud.stats.getCurrency());
    }

    /**
     * Checks if the new building overlaps with any existing buildings or map objects.
     * @param x The x-coordinate to check for overlap.
     * @param y The y-coordinate to check for overlap.
     * @return True if there is overlap, false otherwise.
     */
    private boolean checkOverlap(float x, float y) {
        boolean overlap = false;
        Placed newBuildingPla = new Placed (placingBuilding.name, x, y, placingBuilding.width / PPM, placingBuilding.height / PPM, stage);
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
                        (rect.getX() + (rect.getWidth() / 2)) / PPM,
                        (rect.getY() + (rect.getHeight() / 2))  / PPM,
                        rect.getWidth()  / PPM,
                        rect.getHeight()  / PPM,
                        stage
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

    /**
     * Creates a Box2D body for the building to interact with the game world.
     * @param x The x-coordinate to place the body.
     * @param y The y-coordinate to place the body.
     * @param width The width of the body.
     * @param height The height of the body.
     */
    private void createBuildingBody(float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set((x), (y)); // Adjust position correctly for PPM
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

    /**
     * Combines all building lists into a single list for easy access.
     * @return A list containing all buildings of all types.
     */
    public static ArrayList<Building> combineBuildings() {
        ArrayList<Building> allBuildings = new ArrayList<>();
        allBuildings.addAll(accommodations);
        allBuildings.addAll(academics);
        allBuildings.addAll(recreationals);
        allBuildings.addAll(foods);
        allBuildings.addAll(workplaces);
        return allBuildings;
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

    public void setIsPlacingBuilding(boolean isPlacingBuilding) {
        this.isPlacingBuilding = isPlacingBuilding;
    }
}

