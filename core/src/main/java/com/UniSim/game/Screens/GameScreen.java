package com.UniSim.game.Screens;

import static com.UniSim.game.Constants.*;

import com.UniSim.game.Buildings.Building;
import com.UniSim.game.Buildings.BuildingManager;
import com.UniSim.game.Buildings.Types.Academic;
import com.UniSim.game.Buildings.Types.Accommodation;
import com.UniSim.game.Buildings.Types.Food;
import com.UniSim.game.Buildings.Types.Recreational;
import com.UniSim.game.Buildings.Types.Workplace;
import com.UniSim.game.Sprites.Character;
import com.UniSim.game.Events.EventManager;
import com.UniSim.game.Hud;
import com.UniSim.game.PauseMenu;
import com.UniSim.game.Settings.GameSettings;
import com.UniSim.game.Sprites.SpeechBubble;
import com.UniSim.game.UniSim;

import java.util.ArrayList;
import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Main gameplay screen of UniSim.
 * Handles:
 * - Game world rendering and physics
 * - Player character movement and interactions
 * - Building placement and management
 * - Camera control and UI elements
 * - Event system and notifications
 */
public class GameScreen implements Screen {
    // Core game components
    private UniSim game;              // Main game instance
    private SpriteBatch batch;        // Sprite rendering
    private Stage stage;              // UI stage
    private BuildingManager buildingManager;  // Manages building placement/interaction
    
    // Textures and visuals
    private Texture characterTexture;     // Player sprite
    private Texture speechBubbleTexture;  // Interaction indicator
    private Texture map;                  // Game world background
    private Texture pauseIconTexture;     // Pause menu button
    
    // Physics and world
    private World world;              // Box2D physics world
    private Character player;         // Player character
    private SpeechBubble speechBubbleReception;  // Building interaction indicator
    private OrthographicCamera camera;    // Game view camera
    private Box2DDebugRenderer b2dr;      // Physics debug rendering
    private FitViewport fitViewport;      // Screen scaling
    
    // Map and rendering
    private Skin skin;                    // UI styling
    private TmxMapLoader mapLoader;       // Tiled map loading
    private TiledMap tiledMap;            // Game world map
    private OrthogonalTiledMapRenderer renderer;  // Map rendering
    
    // Game systems
    public static AssetManager manager;   // Resource management
    private Music music;                  // Background music
    public Hud hud;                      // Heads-up display
    private PauseMenu pauseMenu;         // Pause menu
    private EventManager eventManager;    // Random events
    
    // Game state
    private boolean playerNearReseption;  // Reception interaction flag
    private boolean showFullMap = false;  // Map view toggle
    private Vector3 originalCameraPosition;  // For map view
    private float originalZoom;             // For map view
    private ArrayList<Float> loggedMinutes;  // Time tracking
    private String buildingInteractedWith;   // Current interaction
    
    /**
     * Creates a new game screen and initializes all components.
     * Sets up:
     * - Physics world and collision detection
     * - Map loading and rendering
     * - Player character and camera
     * - UI elements and menus
     * - Building system
     *
     * @param game Main game instance
     * @param music Background music track
     */
    public GameScreen(UniSim game, Music music) {
        this.game = game;
        this.batch = new SpriteBatch();
        loggedMinutes = new ArrayList<>();
        playerNearReseption = false;
        characterTexture = new Texture("character-1.png");
        speechBubbleTexture = new Texture("question.png");
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())); // Initialize the stage
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        world = new World(new Vector2(0, 0), false);

        b2dr = new Box2DDebugRenderer();
        player = new Character(world, this);
        speechBubbleReception = new SpeechBubble(world, this, 845 / PPM, 365 / PPM);

        camera = new OrthographicCamera();

        fitViewport = new FitViewport(Gdx.graphics.getWidth() / SCALE / PPM, Gdx.graphics.getHeight() / SCALE / PPM,
                camera);

        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("SimMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / PPM);
        camera.position.set(fitViewport.getWorldWidth() / 2, fitViewport.getWorldHeight() / 2, 0);

        buildingManager = new BuildingManager(stage, skin, world, tiledMap, this);

        this.music = music;
        float volume = GameSettings.getMusicVolume();
        music.setVolume(volume);
        manager = new AssetManager();
        manager.load("music/harbor.mp3", Music.class);
        manager.finishLoading();

        music = manager.get("music/harbor.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(volume);
        music.play();

        hud = new Hud(game.batch, skin, world, this, game, music);
        eventManager = new EventManager(hud);

        // Add initial hint message
        hud.sendMessage("Walk to the house to start placing buildings", "initialHint");

        // Load the pause icon texture
        pauseIconTexture = new Texture(Gdx.files.internal("pause.png"));
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(pauseIconTexture));
        ImageButton pauseButton = new ImageButton(drawable);
        pauseButton.setPosition(10, Gdx.graphics.getHeight() - pauseButton.getHeight() - 10);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (showFullMap) {
                    showFullMapView();
                }
                if (buildingManager.getIsWindowOpen()) {
                    buildingManager.closeBuildingWindow();
                }
                pauseMenu.togglePause();
            }
        });
        stage.addActor(pauseButton);

        // Initialize pause menu
        pauseMenu = new PauseMenu(stage, skin, this, game, music);

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        makeHitBoxes(bdef, shape, fdef);
        Body body;

        for (MapObject object : tiledMap.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fdef.shape = shape;
            fdef.isSensor = true;
            Fixture sensorFixture = body.createFixture(fdef);
            sensorFixture.setUserData("sensor");
            shape.dispose();
        }
        setupCollisionListener();
    }

    /**
     * Creates collision boxes for map objects.
     * Converts Tiled map objects to Box2D physics bodies.
     */
    private void makeHitBoxes(BodyDef bdef, PolygonShape shape, FixtureDef fdef) {
        Body body;
        for (int i = 3; i < 6; i++) {
            for (MapObject object : tiledMap.getLayers().get(i).getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();

                bdef.type = BodyDef.BodyType.StaticBody;
                bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM,
                        (rect.getY() + rect.getHeight() / 2) / PPM);

                body = world.createBody(bdef);

                shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
                fdef.shape = shape;
                body.createFixture(fdef);
            }
        }
    }

    /**
     * Updates music volume from settings.
     * @param volume New volume level (0-1)
     */
    public void setMusicVolume(float volume) {
        if (music != null) {
            music.setVolume(volume);
        }
    }

    /**
     * Main game loop.
     * Updates physics, handles input, and renders world.
     *
     * @param delta Time since last frame
     */
    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            buildingManager.closeBuildingWindow();
            if (showFullMap) {
                showFullMapView();
            }
            pauseMenu.togglePause();
        }

        if (!pauseMenu.isPaused()) {
            update(delta);
        }

        // Clear the screen and start rendering
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the map and character
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.end();

        renderer.render();

        buildingManager.handleBuildingPlacement(game.batch, camera, fitViewport);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        // b2dr.render(world, camera.combined);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        player.draw(game.batch);
        if (playerNearReseption) {
            speechBubbleReception.draw(game.batch);
        }
        game.batch.end();

        // Update the stage to process UI events
        stage.act(delta);
        stage.draw();
    }

    /**
     * Updates game state each frame.
     * Handles:
     * - Physics world stepping
     * - Camera movement
     * - UI updates
     * - Building interactions
     * - Event processing
     *
     * @param delta Time since last frame
     */
    public void update(float delta) {
        world.step(1 / 60f, 6, 2);
        player.update(delta);
        speechBubbleReception.update(delta);
        updateCamera();
        renderer.setView(camera);
        handleInput(delta);
        moveRequest();
        manager.update();
        hud.update(delta);
        eventManager.update();
        float deltaTime = Gdx.graphics.getDeltaTime();
        buildingInteraction(buildingManager.updateBuildingInteractions(player.b2body.getPosition(), camera, deltaTime));

        float worldTime = hud.getWorldTimer();
        if (worldTime % 60 == 0 && !loggedMinutes.contains(worldTime) && worldTime != 0 && worldTime != 300) {
            loggedMinutes.add(worldTime);
            studentLoan();
        }

    }

    /**
     * Shows a temporary popup message.
     * Used for notifications and feedback.
     *
     * @param text Message to display
     * @param time How long to show message
     */
    public void popUp(String text, float time) {
        Label loanMessage = new Label(text, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        loanMessage.setFontScale(4); // Make the text larger for visibility

        loanMessage.setPosition((float) (fitViewport.getWorldWidth() * SCALE * PPM) / 2 - loanMessage.getWidth(),
                (float) (fitViewport.getWorldHeight() * PPM * SCALE) / 4 * 3);
        stage.addActor(loanMessage);

        // Create a temporary action to fade out the label
        loanMessage.addAction(Actions.sequence(
                Actions.alpha(1f, 0f), // Initial alpha 1 (fully visible)
                Actions.fadeOut(time), // Fades out over the specified duration
                Actions.removeActor() // Removes the label after fading out
        ));

    }

    /**
     * Triggers the student loan event by increasing the player's currency.
     * This event will also display a pop-up message informing the player of the
     * loan amount.
     */
    private void studentLoan() {
        // Increase the currency
        hud.getStats().increaseCurrency(10000);

        // Create the pop-up label with the message
        popUp("Student Loan: \n+   £10,000", 5);

    }

    /**
     * Processes building interactions based on type.
     * Triggers appropriate effects for:
     * - Academic buildings (study)
     * - Food buildings (eat)
     * - Accommodation (sleep)
     * - Recreational (relax)
     * - Workplace (work)
     */
    private void buildingInteraction(Building building) {
        if (building != null) {
            if (Objects.equals(building.getType(), "Accommodation")) {
                sleep(building);
            } else if (Objects.equals(building.getType(), "Food")) {
                eat(building);
            } else if (Objects.equals(building.getType(), "Academic")) {
                learn(building);
            } else if (Objects.equals(building.getType(), "Workplace")) {
                work(building);
            } else if (Objects.equals(building.getType(), "Recreational")) {
                relax(building);
            }
        }
    }

    /**
     * Handles the action of sleeping in an accommodation building, decreasing the
     * player's fatigue.
     * Displays a pop-up message indicating the amount of fatigue reduced.
     *
     * @param building The accommodation building the player interacts with.
     */
    private void sleep(Building building) {
        Accommodation accommodation = (Accommodation) building;
        int fatigueDecrease = accommodation.getFatigueDecrease();
        hud.getStats().decreaseFatigue(fatigueDecrease);
        popUp("-" + fatigueDecrease + " Fatigue", 3);
    }

    /**
     * Handles the action of relaxing in a recreational building, reducing the
     * player's fatigue,
     * increasing satisfaction, and decreasing currency.
     * Displays a pop-up message indicating the changes in fatigue, satisfaction,
     * and currency.
     *
     * @param building The recreational building the player interacts with.
     */
    private void relax(Building building) {
        Recreational relax = (Recreational) building;
        int currencyDecrease = relax.getDecreaseCurrency();
        int fatigueDecrease = relax.getDecreaseFatigue();
        int satisfactionIncrease = relax.getIncreaseSatisfaction();

        if (hud.getStats().decreaseCurrency(currencyDecrease)) {
            hud.getStats().decreaseFatigue(fatigueDecrease);
            hud.getStats().increaseSatisfaction(satisfactionIncrease);
            popUp("-" + fatigueDecrease + " Fatigue\n" +
                    "+" + satisfactionIncrease + " Satisfaction\n" +
                    "-" + currencyDecrease + " Currency", 3);
        } else {
            popUp("Not enough money!", 3);
        }
    }

    /**
     * Handles the action of eating in a food building, decreasing the player's
     * fatigue,
     * increasing satisfaction, and decreasing currency.
     * Displays a pop-up message indicating the changes in fatigue, satisfaction,
     * and currency.
     *
     * @param building The food building the player interacts with.
     */
    private void eat(Building building) {
        Food food = (Food) building;
        int currencyDecrease = food.getDecreaseCurrency();
        int fatigueDecrease = food.getDecreaseFatigue();
        int satisfactionIncrease = food.getIncreaseSatisfaction();

        if (hud.getStats().decreaseCurrency(currencyDecrease)) {
            hud.getStats().decreaseFatigue(fatigueDecrease);
            hud.getStats().increaseSatisfaction(satisfactionIncrease);
            popUp("-" + fatigueDecrease + " Fatigue\n" +
                    "+" + satisfactionIncrease + " Satisfaction\n" +
                    "-" + currencyDecrease + " Currency", 3);
        } else {
            popUp("Not enough money!", 3);
        }
    }

    /**
     * Handles the action of learning in an academic building, increasing the
     * player's knowledge
     * and increasing fatigue.
     * Displays a pop-up message indicating the amount of knowledge gained and
     * fatigue increased.
     *
     * @param building The academic building the player interacts with.
     */
    private void learn(Building building) {
        Academic academic = (Academic) building;
        int fatigueIncrease = academic.getFatigueGain();
        int knowledgeIncrease = academic.getIntelligenceGain();

        if (hud.getStats().increaseFatigue(fatigueIncrease)) {
            hud.getStats().increaseKnowledge(knowledgeIncrease);
            popUp("+" + knowledgeIncrease + " Knowledge\n" +
                    "+" + fatigueIncrease + " Fatigue", 3);
        } else {
            popUp("Too tired!", 3);
        }
    }

    /**
     * Handles the action of working in a workplace building, increasing the
     * player's currency
     * and increasing fatigue.
     * Displays a pop-up message indicating the amount of currency earned and
     * fatigue increased.
     *
     * @param building The workplace building the player interacts with.
     */
    private void work(Building building) {
        Workplace workplace = (Workplace) building;
        int fatigueIncrease = workplace.getIncreaseFatigue();
        int currencyIncrease = workplace.getIncreaseCurrency();

        if (hud.getStats().increaseFatigue(fatigueIncrease)) {
            hud.getStats().increaseCurrency(currencyIncrease);
            popUp("+" + currencyIncrease + " Currency\n" +
                    "+" + fatigueIncrease + " Fatigue", 3);
        } else {
            popUp("Too tired!", 3);
        }

    }

    /**
     * Checks whether the player is within the boundaries of the map and adjusts the
     * player's velocity
     * to ensure they do not move outside the map.
     */
    private void moveRequest() {
        Vector2 vector = player.b2body.getLinearVelocity();
        if (!vector.isZero()) {
            if (vector.x < 0 && player.b2body.getPosition().x * PPM - (CHARACTER_SIZE_X / 2) - 1 <= 0) {
                vector.x = 0;
            } else if (vector.x > 0
                    && player.b2body.getPosition().x * PPM + (CHARACTER_SIZE_X / 2) + 2 >= MAP_SIZE_X / PPM) {
                vector.x = 0;
            }
            if (vector.y < 0 && player.b2body.getPosition().y * PPM - (CHARACTER_SIZE_Y / 2) - 1 <= 0) {
                vector.y = 0;
            } else if (vector.y > 0
                    && player.b2body.getPosition().y * PPM + (CHARACTER_SIZE_Y / 2) >= MAP_SIZE_Y / PPM) {
                vector.y = 0;
            }
            player.b2body.setLinearVelocity(vector);
        }

    }

    @Override
    public void show() {

    }

    /**
     * Updates the camera's viewport and resolution when the screen size changes.
     * Ensures the camera's projection is updated accordingly.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // 'true' centers the camera

        // Update the camera's viewport if you're using a camera for the game world
        if (camera != null) {
            camera.viewportWidth = width;
            camera.viewportHeight = height;
            camera.update(); // Important to call update to apply the changes
        }

        // Update game world viewport (like FitViewport) if you're using one
        if (fitViewport != null) {
            fitViewport.update(width, height, true); // 'true' ensures the camera stays centered
        }

        // if showing full map resize correctly
        if (showFullMap) {
            camera.zoom = Math.min(MAP_SIZE_X / camera.viewportWidth, MAP_SIZE_Y / camera.viewportHeight);
            camera.position.set(MAP_SIZE_X / 2, MAP_SIZE_Y / 2, 0);
            camera.update();
        }

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    public Texture getCharacterTexture() {
        return characterTexture;
    }

    public Texture getSpeechBubbleTexture() {
        return speechBubbleTexture;
    }

    @Override
    public void dispose() {
        game.batch.dispose();
        map.dispose();
        b2dr.dispose();
        world.dispose();
        stage.dispose();
        skin.dispose();
        batch.dispose();
    }

    /**
     * Keeps the camera within the boundaries of the map to prevent it from going
     * out of bounds.
     *
     * @param camera The camera that is being controlled.
     * @param startX The Viewport width.
     * @param startY The Viewport height.
     * @param width  The width of the map.
     * @param height The height of the map.
     */
    public void boundary(Camera camera, float startX, float startY, float width, float height) {
        Vector3 position = camera.position;

        if (position.x < startX) {
            position.x = startX;
        }
        if (position.y < startY) {
            position.y = startY;
        }
        if (position.x > width - startX) {
            position.x = width - startX;
        }
        if (position.y > height - startY) {
            position.y = height - startY;
        }
        camera.position.set(position);
    }

    /**
     * Handles camera movement and boundaries.
     * Keeps camera within map limits and follows player.
     */
    private void updateCamera() {
        if (!showFullMap) {
            // Calculate the player's position in the game world and adjust by PPM to
            // convert to screen pixels
            float playerScreenX = player.b2body.getPosition().x * PPM;
            float playerScreenY = player.b2body.getPosition().y * PPM;

            // Calculate the current screen position offset of the camera (in terms of
            // pixels)
            float screenX = playerScreenX - (camera.position.x * PPM - SCREEN_SIZE_X / 2);
            float screenY = playerScreenY - (camera.position.y * PPM - SCREEN_SIZE_Y / 2);

            // Adjust the camera position based on screen bounds and player movement
            if (screenX < CENTER_MIN_X) {
                camera.position.x -= (CENTER_MIN_X - screenX) / PPM;
            } else if (screenX > CENTER_MAX_X) {
                camera.position.x += (screenX - CENTER_MAX_X) / PPM;
            }
            if (screenY < CENTER_MIN_Y) {
                camera.position.y -= (CENTER_MIN_Y - screenY) / PPM;
            } else if (screenY > CENTER_MAX_Y) {
                camera.position.y += (screenY - CENTER_MAX_Y) / PPM;
            }
            // Set the boundaries within which the camera can move
            float startX = (camera.viewportWidth / 2);
            float startY = (camera.viewportHeight / 2);

            // Apply boundary restrictions to the camera to prevent it from going out of
            // bounds
            boundary(camera, startX, startY, 100, 50);

            // Update the camera to apply the changes
            camera.update();
        }
    }

    /**
     * Processes player input for movement and actions.
     * Handles:
     * - WASD movement
     * - Building placement
     * - Menu controls
     * - Interaction triggers
     */
    private void handleInput(float deltaTime) {
        float horizontalForce = 0;
        float verticalForce = 0;
        float speedModifier = hud.getStats().getSpeedModifier();
        float modifiedSpeed = CHARACTER_SPEED * speedModifier;

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            verticalForce = modifiedSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            verticalForce = -modifiedSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            horizontalForce = -modifiedSpeed;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            horizontalForce = modifiedSpeed;
        }
        player.b2body.setLinearVelocity(horizontalForce, verticalForce);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && playerNearReseption) {
            placingBuilding();
        }
    }

    /**
     * Sets up collision detection between objects.
     * Handles:
     * - Player-building collisions
     * - Interaction zones
     * - Map boundaries
     */
    private void setupCollisionListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if (fixtureA.getUserData() != null && fixtureA.getUserData().equals("sensor")) {
                    playerNearReseption = true;
                    // Hide the initial hint message when player reaches the house
                    hud.hideMessage("initialHint");
                    // Show build mode message when near reception
                    hud.sendMessage("Press ENTER to go into build mode.", "buildMode");
                }

                if (fixtureB.getUserData() != null && fixtureB.getUserData().equals("sensor")) {
                    playerNearReseption = true;
                    // Hide the initial hint message when player reaches the house
                    hud.hideMessage("initialHint");
                    // Show build mode message when near reception
                    hud.sendMessage("Press ENTER to go into build mode.", "buildMode");
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if ((fixtureA.getUserData() == "sensor" && fixtureB.getUserData() == "player") ||
                        (fixtureA.getUserData() == "player" && fixtureB.getUserData() == "sensor")) {
                    playerNearReseption = false;
                    hud.hideMessage("buildMode"); // Only clear building mode messages
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }
        });
    }

    /**
     * Toggles the building placement mode. Displays the full map and opens or
     * closes the building placement window
     * depending on the current state.
     *
     * When the map is shown in full, the building placement window is opened. If
     * the window is already open,
     * it is closed, and a message prompting the player to press ENTER to go into
     * build mode is displayed.
     */
    private void placingBuilding() {
        showFullMapView();
        camera.update();
        hud.hideMessage("buildMode");
        if (showFullMap && !buildingManager.getIsWindowOpen()) {
            buildingManager.openBuildingWindow();
        } else {
            buildingManager.closeBuildingWindow();
            hud.sendMessage("Press ENTER to go into build mode.", "buildMode");
        }
    }

    /**
     * Toggles the camera view between a full map view and the original player view.
     * In the full map view, the camera shows the entire map with zoom adjusted to
     * fit the whole map.
     * When returning to the original view, the camera zoom and position are
     * reverted to their previous state.
     */
    public void showFullMapView() {
        if (!showFullMap) {
            // Save original position and zoom level to revert back
            originalCameraPosition = new Vector3(camera.position);
            originalZoom = camera.zoom;

            // Adjust the camera to show the whole map
            camera.zoom = Math.min(MAP_SIZE_X / PPM / camera.viewportWidth / PPM,
                    MAP_SIZE_Y / PPM / camera.viewportHeight / PPM);
            camera.position.set(MAP_SIZE_X / PPM / 2 / PPM, MAP_SIZE_Y / PPM / 2 / PPM, 0);
            camera.update();
            showFullMap = true;
        } else {
            // Revert to original camera view (player view)
            camera.zoom = originalZoom;
            camera.position.set(originalCameraPosition);
            camera.update();
            showFullMap = false;
        }
    }

    /**
     * Gets the UI stage.
     * @return Stage used for UI rendering
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Gets the event manager for random events.
     * @return EventManager instance
     */
    public EventManager getEventManager() {
        return eventManager;
    }
}
