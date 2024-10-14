package com.UniSim.game;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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

public class BuildingList {
    private Stage stage;
    private Skin skin;
    private World world;
    private Window buildingWindow;
    private boolean isWindowOpen = false;

    private Texture selectedBuildingTexture = null;
    private boolean isPlacingBuilding = false;
    private Vector3 mousePosition = new Vector3();

    private ArrayList<Rectangle> placedBuildings = new ArrayList<>(); // Store placed buildings' rectangles
    private Label messageLabel;

    private float selectedBuildingWidth;
    private float selectedBuildingHeight;

    public BuildingList(Stage stage, Skin skin, World world) {
        this.stage = stage;
        this.skin = skin;
        this.world = world;

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
        
        // **Declare and initialize the Academic Button**
        TextButton academicButton = new TextButton("Academic", skin);
        academicButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAcademicOptions();
            }
        });
        buildingWindow.add(academicButton).padBottom(10).row();

        // **Declare and initialize the Food Button**
        TextButton foodButton = new TextButton("Food", skin);
        foodButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFoodOptions();
            }
        });
        buildingWindow.add(foodButton).padBottom(10).row();

        // **Declare and initialize the Food Button**
        TextButton RecreationalButton = new TextButton("Recreational", skin);
        foodButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFoodOptions();
            }
        });
        buildingWindow.add(RecreationalButton).padBottom(10).row();
        

        if (!stage.getActors().contains(buildingWindow, true)) {
            stage.addActor(buildingWindow);
        }
    }

    private void showAccommodationOptions() {
        buildingWindow.clear();  // Clear previous content
        buildingWindow.add(new Label("Select Accommodation", skin)).padBottom(20).row();
    
        // Create a Table for building options
        Table buildingTable = new Table();
        
        // Add building options
        addBuildingOption(buildingTable, "Accommodation 1", "accommodation_3.png", 5000, 64.0f, 64.0f);
        addBuildingOption(buildingTable, "Accommodation 2", "accommodation_3.png", 6000, 32.0f, 32.0f);
        addBuildingOption(buildingTable, "Accommodation 3", "accommodation_3.png", 6000, 128.0f, 128.0f);
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


    // Add Academic options
    private void showAcademicOptions() {
        buildingWindow.clear();
        buildingWindow.add(new Label("Select Academic Building", skin)).padBottom(20).row();

        Table buildingTable = new Table();
        addBuildingOption(buildingTable, "Library", "accommodation_3.png", 10000, 100.0f, 100.0f);
        addBuildingOption(buildingTable, "Lab", "accommodation_3.png", 8000, 80.0f, 80.0f);

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

    // Add Food options
    private void showFoodOptions() {
        buildingWindow.clear();
        buildingWindow.add(new Label("Select Food Facility", skin)).padBottom(20).row();

        Table buildingTable = new Table();
        addBuildingOption(buildingTable, "Cafeteria", "accommodation_3.png", 7000, 70.0f, 70.0f);
        addBuildingOption(buildingTable, "Restaurant", "accommodation_3.png", 12000, 120.0f, 120.0f);

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


    
    private void addBuildingOption(Table buildingTable, String buildingName, String imagePath, int price, float width, float height) {
        // Load the texture for the building
        Texture buildingTexture = new Texture(Gdx.files.internal(imagePath));
    
        // Create the Image and maintain its aspect ratio
        Image buildingImage = new Image(buildingTexture);
    
        // Set maximum width and height (for display in the selection window)
        float maxWidth = 100; 
        float maxHeight = 100; 
    
        // Calculate aspect ratio
        float aspectRatio = (float) buildingTexture.getWidth() / buildingTexture.getHeight();
    
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
        Label buildingLabel = new Label(buildingName + " - $" + price, skin);
        buildingTable.add(buildingLabel).padBottom(10).row();
    
        // Create the Select button
        TextButton selectButton = new TextButton("Select", skin);
        selectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Pass the size (width and height) when selecting the building
                placeBuilding(buildingName, imagePath, price, width, height);
            }
        });
    
        buildingTable.add(selectButton).padBottom(20).row();
    }

    private void placeBuilding(String buildingName, String imagePath, int price, float width, float height) {
        try {
            selectedBuildingTexture = new Texture(Gdx.files.internal(imagePath));
            selectedBuildingWidth = width;  // Store the custom width
            selectedBuildingHeight = height;  // Store the custom height
            isPlacingBuilding = true;
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
                    placedBuildings.add(new Rectangle(mousePosition.x - selectedBuildingWidth / 2, 
                                                      mousePosition.y - selectedBuildingHeight / 2, 
                                                      selectedBuildingWidth, selectedBuildingHeight));
                    createBuildingBody(mousePosition.x, mousePosition.y, selectedBuildingWidth, selectedBuildingHeight); // Create Box2D body
                    isPlacingBuilding = false;
                    messageLabel.setVisible(false); // Hide error message after successful placement
                } else {
                    messageLabel.setText("Cannot place building here!");
                    messageLabel.setVisible(true); // Show error message
                }
            }
        }
    
        batch.begin();
        for (Rectangle buildingPos : placedBuildings) {
            // Draw placed buildings using the stored size
            batch.draw(selectedBuildingTexture, buildingPos.x, buildingPos.y, buildingPos.width, buildingPos.height);
        }
        batch.end();
    }

    private boolean checkOverlap(float x, float y, float width, float height) {
        Rectangle newBuildingRect = new Rectangle(x - width / 2, y - height / 2, width, height);
        for (Rectangle placedBuilding : placedBuildings) {
            if (newBuildingRect.overlaps(placedBuilding)) {
                return true; // Overlap detected
            }
        }
        return false;
    }

    private void createBuildingBody(float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(x / Constants.PPM, y / Constants.PPM);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body buildingBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / Constants.PPM, height / 2 / Constants.PPM);

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