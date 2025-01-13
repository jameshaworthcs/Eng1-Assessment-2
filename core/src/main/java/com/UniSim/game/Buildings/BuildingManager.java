package com.UniSim.game.Buildings;

import static com.UniSim.game.Constants.*;

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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Manages the creation, placement, and interaction with buildings in the game.
 * Handles different building types, their placement on the map, and player interactions.
 * Provides UI for building selection and placement validation.
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
     * Initializes the BuildingManager with required game components.
     *
     * @param stage      The stage for UI elements
     * @param skin       The skin for UI styling
     * @param world      The Box2D physics world
     * @param tiledMap   The game world map
     * @param gameScreen The parent game screen
     */
    public BuildingManager(Stage stage, Skin skin, World world, TiledMap tiledMap, GameScreen gameScreen) {
        accommodations = new ArrayList<Accommodation>();
        academics = new ArrayList<Academic>();
        foods = new ArrayList<Food>();
        recreationals = new ArrayList<Recreational>();
        workplaces = new ArrayList<Workplace>();
        placed = new ArrayList<Placed>();

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
     * Updates building interactions based on player proximity.
     * Checks if the player is close enough to interact with any building.
     *
     * @param playerPosition Player's current position
     * @param camera        Game camera for view calculations
     * @param deltaTime     Time since last frame
     * @return The building being interacted with, or null if none
     */
    public Building updateBuildingInteractions(Vector2 playerPosition, OrthographicCamera camera, float deltaTime) {
        buildingPressed = null;
        for (Placed building : placed) {
            buildingPressed = building.updateInteraction(playerPosition, camera, deltaTime);
            if (buildingPressed != null) {
                break;
            }
        }
        return buildingPressed;
    }

    /**
     * Initializes all available building types with their properties.
     * TODO: Update building images with final assets
     * TODO: Add building stats display in build menu
     */
    private void makeBuildingTypes() {
        accommodations.add(new Accommodation("Derwent", 1000f, "accommodation_3.png", 4f, 64f, 64f, 2));
        accommodations.add(new Accommodation("Anne Lister", 3000f, "accommodation_3.png", 4f, 96f, 96f, 5));
        accommodations.add(new Accommodation("David Kato", 8000f, "accommodation_3.png", 4f, 128f, 128f, 10));

        foods.add(new Food("Salt and Pepper", 1000f, "accommodation_3.png", 2f, 64f, 64f, 100, 1, 1));
        foods.add(new Food("Papa Johns", 3000f, "accommodation_3.png", 2f, 96f, 96f, 200, 2, 2));
        foods.add(new Food("Piazza Restaurant", 5000f, "accommodation_3.png", 2f, 128f, 128f, 300, 3, 3));

        recreationals.add(new Recreational("Long Boi Statue", 1000f, "accommodation_3.png", 2f, 64f, 64f, 100, 1, 1));
        recreationals.add(new Recreational("Campus West Gym", 3000f, "accommodation_3.png", 2f, 96f, 96f, 300, 3, 3));
        recreationals.add(new Recreational("Glasshouse Bar", 5000f, "accommodation_3.png", 2f, 128f, 128f, 500, 5, 5));

        academics.add(new Academic("CSE 168", 1000f, "lectureroom.png", 1.5f, 64f, 64f, 1, 3));
        academics.add(new Academic("Main Library", 3000f, "lectureroom.png", 1.5f, 96f, 96f, 3, 5));
        academics.add(new Academic("Piazza Lecture Theatre", 5000f, "lectureroom.png", 1.5f, 128f, 128f, 5, 10));

        workplaces.add(new Workplace("Postern Gate Spoons", 1000f, "accommodation_3.png", 1.5f, 64f, 64f, 1, 100));
        workplaces.add(new Workplace("Greggs", 3000f, "accommodation_3.png", 1.5f, 96f, 96f, 5, 500));
        workplaces.add(new Workplace("Nisa", 5000f, "accommodation_3.png", 1.5f, 128f, 128f, 10, 1000));
    }

    /**
     * Displays the main building selection window.
     * Creates buttons for each building category and handles navigation.
     */
    private void showBuildingSelectionWindow() {
        if (buildingWindow == null) {
            buildingWindow = new Window("Select Building Type", skin);
            buildingWindow.setSize(450, 600);
            buildingWindow.setPosition(1700, 400);
        } else {
            buildingWindow.clear();
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

        TextButton workplaceButton = new TextButton("Workplace", skin);
        workplaceButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showWorkOptions();
            }
        });
        buildingWindow.add(workplaceButton).padBottom(10).row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeBuildingWindow();
                gameScreen.showFullMapView();
                gameScreen.hud.sendMessage("Press ENTER to go into build mode.");
            }
        });

        buildingWindow.add(backButton).padTop(100).row();

        if (!stage.getActors().contains(buildingWindow, true)) {
            stage.addActor(buildingWindow);
        }
    }

    /**
     * Creates a scrollable window for building options.
     * Sets up scrolling behavior and adds a back button.
     *
     * @param buildingTable The table containing building options
     */
    private void setScrollAndBack(Table buildingTable) {
        ScrollPane scrollPane = new ScrollPane(buildingTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setForceScroll(false, true);

        buildingWindow.add(scrollPane).expand().fill().row();

        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showBuildingSelectionWindow();
            }
        });

        buildingWindow.add(backButton).padTop(20).row();
    }

    /**
     * Displays accommodation building options in a scrollable window.
     */
    private void showAccommodationOptions() {
        buildingWindow.clear();
        buildingWindow.add(new Label("Select Accommodation", skin)).padBottom(20).row();
        
        Table buildingTable = new Table();
        for (Accommodation accommodation : accommodations) {
            addBuildingOption(buildingTable, accommodation);
        }
        setScrollAndBack(buildingTable);
    }

    /**
     * Displays workplace building options in a scrollable window.
     */
    private void showWorkOptions() {
        buildingWindow.clear();
        buildingWindow.add(new Label("Select Workplace", skin)).padBottom(20).row();
        
        Table buildingTable = new Table();
        for (Workplace workplace : workplaces) {
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
        for (Academic academic : academics) {
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
        for (Food food : foods) {
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
        for (Recreational recreational : recreationals) {
            addBuildingOption(buildingTable, recreational);
        }
        setScrollAndBack(buildingTable);
    }

    /**
     * Adds a building option to the selection table with image, label, and select button.
     * Maintains aspect ratio of building images and handles selection events.
     *
     * @param buildingTable The table to add the building option to
     * @param building The building to add
     */
    private void addBuildingOption(Table buildingTable, Building building) {
        Image buildingImage = new Image(building.texture);
        
        float maxWidth = 100;
        float maxHeight = 100;
        float aspectRatio = (float) building.texture.getWidth() / building.texture.getHeight();
        
        float displayWidth, displayHeight;
        if (aspectRatio > 1) {
            displayWidth = maxWidth;
            displayHeight = maxWidth / aspectRatio;
        } else {
            displayHeight = maxHeight;
            displayWidth = maxHeight * aspectRatio;
        }
        
        buildingImage.setSize(displayWidth, displayHeight);
        buildingTable.add(buildingImage).size(displayWidth, displayHeight).padBottom(10).row();
        Label buildingLabel = new Label(building.name + " - " + String.format("$%.2f", building.cost), skin);
        buildingTable.add(buildingLabel).padBottom(10).row();
        
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
     * Snaps the given position to the nearest grid point to ensure buildings align
     * with the grid.
     *
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
     * Handles the building placement process.
     * Shows placement preview, validates position, and creates the building when placed.
     *
     * @param batch The sprite batch for rendering
     * @param camera The game camera
     * @param viewport The game viewport
     */
    public void handleBuildingPlacement(SpriteBatch batch, OrthographicCamera camera, Viewport viewport) {
        if (isPlacingBuilding && placingBuilding != null) {
            gameScreen.hud.sendMessage("Press SPACEBAR to cancel building placement", "buildMode");
            
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                isPlacingBuilding = false;
                placingBuilding = null;
                gameScreen.hud.hideMessage("buildMode");
                showBuildingSelectionWindow();
                return;
            }

            Vector3 mousePosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePosition,
                    viewport.getScreenX(), viewport.getScreenY(),
                    viewport.getScreenWidth(), viewport.getScreenHeight());

            batch.begin();
            batch.setColor(1, 1, 1, 0.5f);
            Vector3 snappedPosition = snapToGrid(mousePosition.x, mousePosition.y);
            batch.draw(placingBuilding.texture, snappedPosition.x - placingBuilding.width / 2 / PPM,
                    snappedPosition.y - placingBuilding.height / 2 / PPM,
                    placingBuilding.width / PPM, placingBuilding.height / PPM);
            batch.setColor(1, 1, 1, 1);
            batch.end();

            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (!checkOverlap(snappedPosition.x, snappedPosition.y)) {
                    if (canAffordBuilding()) {
                        placed.add(new Placed(placingBuilding.name,
                                snappedPosition.x, snappedPosition.y,
                                placingBuilding.width / PPM, placingBuilding.height / PPM, stage));
                        createBuildingBody(snappedPosition.x, snappedPosition.y, placingBuilding.width / PPM,
                                placingBuilding.height / PPM);
                        gameScreen.hud.hideMessage("buildMode");
                        showBuildingSelectionWindow();
                        gameScreen.hud.stats.takeOffBuildingCost(placingBuilding.cost);
                        gameScreen.hud.stats.incrementBuildingCounter();
                    } else {
                        gameScreen.popUp("Cannot Afford Building", 4);
                        showBuildingSelectionWindow();
                    }
                    isPlacingBuilding = false;
                } else {
                    gameScreen.popUp("Cannot place building here!", 4);
                }
            }
        }
    }

    /**
     * Checks if the player can afford the currently selected building.
     *
     * @return true if the player has enough money, false otherwise
     */
    private boolean canAffordBuilding() {
        return gameScreen.hud.stats.getCurrency() >= placingBuilding.cost;
    }

    /**
     * Checks if a building placement would overlap with existing buildings or map objects.
     *
     * @param x The x coordinate to check
     * @param y The y coordinate to check
     * @return true if there is an overlap, false otherwise
     */
    private boolean checkOverlap(float x, float y) {
        boolean overlap = false;
        Placed newBuildingPla = new Placed(placingBuilding.name,
                x, y,
                placingBuilding.width / PPM, placingBuilding.height / PPM, stage);

        for (Placed building : placed) {
            if (newBuildingPla.overlaps(building)) {
                overlap = true;
                break;
            }
        }

        for (MapLayer layer : tiledMap.getLayers()) {
            if (layer.getObjects().getCount() > 0) {
                for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();
                    Placed temp = new Placed(
                            "Borders",
                            rect.x / PPM + rect.width / PPM / 2,
                            rect.y / PPM + rect.height / PPM / 2,
                            rect.width / PPM,
                            rect.height / PPM,
                            stage);

                    if (newBuildingPla.overlaps(temp)) {
                        overlap = true;
                        break;
                    }
                }
            }
            if (overlap) break;
        }
        return overlap;
    }

    /**
     * Creates a Box2D body for a placed building.
     */
    private void createBuildingBody(float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x, y);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;

        body.createFixture(fixtureDef);
        shape.dispose();
    }

    /**
     * Combines all building types into a single list.
     *
     * @return A list containing all available buildings
     */
    public static ArrayList<Building> combineBuildings() {
        ArrayList<Building> buildings = new ArrayList<Building>();
        buildings.addAll(accommodations);
        buildings.addAll(academics);
        buildings.addAll(foods);
        buildings.addAll(recreationals);
        buildings.addAll(workplaces);
        return buildings;
    }

    // Window management methods
    public void closeBuildingWindow() {
        if (buildingWindow != null) {
            buildingWindow.remove();
            isWindowOpen = false;
        }
    }

    public void openBuildingWindow() {
        showBuildingSelectionWindow();
    }

    public boolean getIsWindowOpen() {
        return isWindowOpen;
    }

    public void setIsPlacingBuilding(boolean isPlacingBuilding) {
        this.isPlacingBuilding = isPlacingBuilding;
    }
}
