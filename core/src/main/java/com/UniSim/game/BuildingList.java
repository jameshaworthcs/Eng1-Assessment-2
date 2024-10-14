package com.UniSim.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class BuildingList {
    private Stage stage;
    private Skin skin;
    private Table table;
    private Window buildingWindow;
    private boolean isWindowOpen = false; // Track if the window is open

    // Constructor to initialize stage and skin
    public BuildingList(Stage stage, Skin skin) {
        this.stage = stage;
        this.skin = skin;

        // Create the initial UI for the building list
        createBuildingButton();
    }

    private void closeBuildingWindow() {
        if (buildingWindow != null) {
            buildingWindow.remove(); // Remove the window from the stage
        }
        isWindowOpen = false; // Allow opening new windows again
    }

    // Create the button in the game that opens the building selection window
    private void createBuildingButton() {
        // Button to open the building selection window
        TextButton buildingButton = new TextButton("Building", skin);
        buildingButton.setSize(200, 50);
        buildingButton.setPosition(10, 10);

        // Add a ClickListener to open the window when clicked
        buildingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isWindowOpen) {  // Prevent opening multiple windows
                    showBuildingSelectionWindow();
                }
            }
        });

        // Add the button to the stage
        stage.addActor(buildingButton);
    }

    // Method to show the building selection window
    private void showBuildingSelectionWindow() {
        isWindowOpen = true; // Mark that the window is now open

        // Create a window
        buildingWindow = new Window("Select Building Type", skin);
        buildingWindow.setSize(300, 400);
        buildingWindow.setPosition(300, 200);

        // Create buttons for each building type
        TextButton accommodationButton = new TextButton("Accommodation", skin);
        TextButton academicButton = new TextButton("Academic", skin);
        TextButton foodButton = new TextButton("Food", skin);

        // Add listeners for each building type
        accommodationButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showAccommodationOptions();
            }
        });

        // Add buttons to the window
        buildingWindow.add(accommodationButton).padBottom(10).row();
        buildingWindow.add(academicButton).padBottom(10).row();
        buildingWindow.add(foodButton).padBottom(10).row();

        // Add a "Close" button to close the window
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeBuildingWindow();
            }
        });

        buildingWindow.add(closeButton).padTop(20).row(); // Add the close button at the bottom

        // Add the window to the stage
        stage.addActor(buildingWindow);
    }

    // Show accommodation options when the "Accommodation" button is clicked
    private void showAccommodationOptions() {
        buildingWindow.clear();  // Clear the window to show new content
        // Add a label at the top of the window to represent the title
        Label titleLabel = new Label("Select Accommodation", skin);
        buildingWindow.add(titleLabel).padBottom(20).row();  // Add some padding and move to the next row

        // Add different accommodations
        addBuildingOption("Accommodation 1", "reception.png", 5000);
        addBuildingOption("Accommodation 2", "reception.png", 6000);
   
        // Add a "Back" button to return to the main building selection window
        TextButton backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showBuildingSelectionWindow();  // Go back to the main menu
            }
        });

        buildingWindow.add(backButton).padTop(20).row();  // Add the "Back" button at the bottom
    }

    // Utility method to create an option for each building with a button
    private void addBuildingOption(String buildingName, String imagePath, int price) {
        // Create an image for the building
        Texture buildingTexture = new Texture(Gdx.files.internal("reception.png"));
        Image buildingImage = new Image(buildingTexture);

        // Create a label to display the building name and price
        Label buildingLabel = new Label(buildingName + " - $" + price, skin);

        // Create a button for selecting the building
        TextButton selectButton = new TextButton("Select", skin);

        // Add a listener to handle building selection
        selectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                placeBuilding(buildingName, imagePath, price);
            }
        });

        // Add building image, label, and button to the window
        buildingWindow.add(buildingImage).padBottom(10).row();
        buildingWindow.add(buildingLabel).padBottom(10).row();
        buildingWindow.add(selectButton).padBottom(10).row();
    }

    // Method to place the building on the map
    private void placeBuilding(String buildingName, String imagePath, int price) {
        System.out.println("Placing " + buildingName + " with price $" + price);

        // TODO: Add logic here to allow placing the building on the map
        // You can use this part to place the building where the player clicks or at a specific location
        closeBuildingWindow(); // Close the window after placing the building
    }

    // Handle keyboard input for the X key
    public void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            if (!isWindowOpen) {
                showBuildingSelectionWindow(); // Open the window if not already open
            } else {
                closeBuildingWindow(); // Close the window if it's already open
            }
        }
    }
}
