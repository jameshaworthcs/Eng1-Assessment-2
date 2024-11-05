package com.UniSim.game;

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

import static com.UniSim.game.Constants.PPM;

public class BuildingList {
    private Stage stage;
    private Skin skin;
    private World world;
    private Window buildingWindow;
    private boolean isWindowOpen = false;

    private Texture selectedBuildingTexture = null;
    private boolean isPlacingBuilding = false;
    private Vector3 mousePosition = new Vector3();

    private ArrayList<PlacedBuilding> placedBuildings = new ArrayList<>(); // Store placed buildings
    private Label messageLabel;

    private float selectedBuildingWidth;
    private float selectedBuildingHeight;

    private TiledMap tiledMap;

    public BuildingList(Stage stage, Skin skin, World world, TiledMap tiledMap) {
        this.stage = stage;
        this.skin = skin;
        this.world = world;
        this.tiledMap = tiledMap;

        createBuildingButton();
        createMessageLabel(); // Initialize the message label
    }

    // Create the button in the game that opens the building selection window
    private void createBuildingButton() {
        TextButton buildingButton = new TextButton("Building", skin);
        buildingButton.setSize(200, 50);
        buildingButton.setPosition(10, 10);

        buildingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isWindowOpen) {  // Prevent opening multiple windows
                    showBuildingSelectionWindow();
                }
            }
        });

        stage.addActor(buildingButton);
    }

    // Create the message label for invalid placement
    private void createMessageLabel() {
        messageLabel = new Label("", skin);
        messageLabel.setPosition(10, 70); // Set the position for the message
        messageLabel.setVisible(false); // Initially hidden
        stage.addActor(messageLabel); // Add the label to the stage
    }

    private void showBuildingSelectionWindow() {
        if (buildingWindow == null) {
            buildingWindow = new Window("Select Building Type", skin);
            buildingWindow.setSize(300, 400);
            buildingWindow.setPosition(300, 200);
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
                showFoodOptions();
            }
        });
        buildingWindow.add(recreationalButton).padBottom(10).row();

        if (!stage.getActors().contains(buildingWindow, true)) {
            stage.addActor(buildingWindow);
        }
    }

    private void addBuildingOption(Table buildingTable, String buildingName, String imagePath, int price, float scaleFactor) {
        // Load the texture for the building
        Texture buildingTexture = new Texture(Gdx.files.internal(imagePath));

        // Get the original width and height of the image
        float originalWidth = buildingTexture.getWidth();
        float originalHeight = buildingTexture.getHeight();

        // Calculate the display size by scaling the original dimensions
        float displayWidth = originalWidth * scaleFactor;
        float displayHeight = originalHeight * scaleFactor;

        // Create the Image and set its size
        Image buildingImage = new Image(buildingTexture);
        buildingImage.setSize(displayWidth, displayHeight);

        // Add the building image and label to the table
        buildingTable.add(buildingImage).size(displayWidth, displayHeight).padBottom(10).row();
        Label buildingLabel = new Label(buildingName + " - $" + price, skin);
        buildingTable.add(buildingLabel).padBottom(10).row();

        // Create the Select button
        TextButton selectButton = new TextButton("Select", skin);
        selectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Pass the size (original width and height scaled by the factor) when selecting the building
                placeBuilding(buildingName, imagePath, price, displayWidth, displayHeight);
            }
        });

        buildingTable.add(selectButton).padBottom(20).row();
    }

    // Usage examples with the modified addBuildingOption method
    private void showAccommodationOptions() {
        buildingWindow.clear();  // Clear previous content
        buildingWindow.add(new Label("Select Accommodation", skin)).padBottom(20).row();

        // Create a Table for building options
        Table buildingTable = new Table();

        // Add building options with the scale factor instead of specific sizes
        addBuildingOption(buildingTable, "Accommodation 1", "accommodation_3.png", 5000, 0.08f);
        addBuildingOption(buildingTable, "Accommodation 2", "accommodation_3.png", 6000, 0.06f);
        addBuildingOption(buildingTable, "Accommodation 3", "accommodation_3.png", 6000, 0.09f);

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

    // Update other similar methods to use the new addBuildingOption signature
    private void showAcademicOptions() {
        buildingWindow.clear();
        buildingWindow.add(new Label("Select Academic Building", skin)).padBottom(20).row();

        Table buildingTable = new Table();
        addBuildingOption(buildingTable, "Library", "lectureroom.png", 10000, 0.07f);
        addBuildingOption(buildingTable, "Lab", "accommodation_3.png", 8000, 0.08f);

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

    // Similarly, update the showFoodOptions method to use the new addBuildingOption signature
    private void showFoodOptions() {
        buildingWindow.clear();
        buildingWindow.add(new Label("Select Recreational Facility", skin)).padBottom(20).row();

        Table buildingTable = new Table();
        addBuildingOption(buildingTable, "Cafeteria", "accommodation_3.png", 7000, 0.07f);
        addBuildingOption(buildingTable, "Restaurant", "accommodation_3.png", 12000, 0.09f);

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

    private void placeBuilding(String buildingName, String imagePath, int price, float width, float height) {
        try {
            Texture newBuildingTexture = new Texture(Gdx.files.internal(imagePath));
            selectedBuildingWidth = width / PPM;  // Store the custom width, adjusted for PPM
            selectedBuildingHeight = height / PPM;  // Store the custom height, adjusted for PPM
            System.out.println("Bounding box width: " + selectedBuildingWidth + ", height: " + selectedBuildingHeight);
            isPlacingBuilding = true;
            selectedBuildingTexture = newBuildingTexture;
            closeBuildingWindow();
        } catch (Exception e) {
            Gdx.app.error("BuildingList", "Error loading texture: " + imagePath, e);
        }
    }

    public void handleBuildingPlacement(SpriteBatch batch, OrthographicCamera camera, Viewport viewport) {
        if (isPlacingBuilding && selectedBuildingTexture != null) {
            mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(mousePosition, viewport.getScreenX(), viewport.getScreenY(),
                    viewport.getScreenWidth(), viewport.getScreenHeight());

            batch.begin();
            batch.setColor(1, 1, 1, 0.5f);  // Semi-transparent

            // Draw the building using the custom width and height
            batch.draw(selectedBuildingTexture, mousePosition.x - selectedBuildingWidth / 2,
                    mousePosition.y - selectedBuildingHeight / 2,
                    selectedBuildingWidth, selectedBuildingHeight);

            batch.setColor(1, 1, 1, 1);  // Reset to full opacity
            batch.end();

            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                if (!checkOverlap(mousePosition.x, mousePosition.y, selectedBuildingWidth, selectedBuildingHeight)) {
                    PlacedBuilding newBuilding = new PlacedBuilding(
                            selectedBuildingTexture,
                            mousePosition.x - selectedBuildingWidth / 2,
                            mousePosition.y - selectedBuildingHeight / 2,
                            selectedBuildingWidth,
                            selectedBuildingHeight
                    );

                    placedBuildings.add(newBuilding);
                    createBuildingBody(mousePosition.x - selectedBuildingWidth / 2,
                            mousePosition.y - selectedBuildingHeight / 2,
                            selectedBuildingWidth,
                            selectedBuildingHeight); // Create Box2D body

                    isPlacingBuilding = false;
                    messageLabel.setVisible(false); // Hide error message after successful placement
                } else {
                    messageLabel.setText("Cannot place building here!");
                    messageLabel.setVisible(true); // Show error message
                }
            }
        }

        batch.begin();
        for (PlacedBuilding building : placedBuildings) {
            // Draw each placed building individually using its own texture and size
            batch.draw(building.texture, building.rectangle.x, building.rectangle.y,
                    building.rectangle.width, building.rectangle.height);
        }
        batch.end();
    }

    private boolean checkOverlap(float x, float y, float width, float height) {
        boolean overlap = false;
        Rectangle newBuildingRect = new Rectangle(x - width / 2, y - height / 2, width, height);
        for (PlacedBuilding placedBuilding : placedBuildings) {
            if (newBuildingRect.overlaps(placedBuilding.rectangle)) {
                overlap = true; // Overlap detected
            }
        }

        for (MapLayer layer : tiledMap.getLayers()) {
            if (layer.getObjects().getCount() > 0) { // Add any other relevant layer names here
                for (MapObject object : layer.getObjects().getByType(RectangleMapObject.class)) {
                    Rectangle rect = ((RectangleMapObject) object).getRectangle();

                    // Define object rectangle for overlap check
                    Rectangle objectRect = new Rectangle(
                            rect.getX() / PPM,
                            rect.getY() / PPM,
                            rect.getWidth() / PPM,
                            rect.getHeight() / PPM
                    );

                    // Check for overlap
                    if (newBuildingRect.overlaps(objectRect)) {
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

    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if (!isWindowOpen) {
                showBuildingSelectionWindow();
            } else {
                closeBuildingWindow();
            }
        }
    }

    private void closeBuildingWindow() {
        if (buildingWindow != null) {
            buildingWindow.remove();
        }
        isWindowOpen = false;
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
