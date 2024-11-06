package com.UniSim.game.Screens;

import com.UniSim.game.*;
import com.UniSim.game.Buildings.BuildingManager;
import com.UniSim.game.Sprites.Character;
import com.UniSim.game.Sprites.SpeechBubble;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.ArrayList;
import static com.UniSim.game.Constants.*;

public class GameScreen implements Screen {
    private UniSim game;
    private Stage stage;

    private BuildingManager buildingManager;

    private Texture characterTexture;
    private Texture speechBubbleTexture;

    private World world;
    private Character player;
    private SpeechBubble speechBubbleReception;
    private Texture map;
    private OrthographicCamera camera;

    private Box2DDebugRenderer b2dr;

    private FitViewport fitViewport;

    private ArrayList<BoxEntity> boxes;

    private Skin skin;           // Skin for UI styling

    private TmxMapLoader mapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer renderer;

    public static AssetManager manager;
    private Music music;

    private Hud hud;

    private boolean playerNearReseption;

    private boolean showFullMap = false;
    private Vector3 originalCameraPosition;
    private float originalZoom;

    public GameScreen(UniSim game) {
        this.game = game;
        playerNearReseption = false;
        characterTexture = new Texture("character-1.png");
        speechBubbleTexture = new Texture("question.png");
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())); // Initialize the stage
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        boxes = new ArrayList<>();

        world = new World(new Vector2(0, 0), false);

        b2dr = new Box2DDebugRenderer();
        player = new Character(world, this);
        speechBubbleReception = new SpeechBubble(world, this, 845, 365);

        //boxes.add(new BoxEntity(world, stage, skin, 1500, 1500, 64, 32, true, "TEST"));
        //boxes.add(new BoxEntity(world, stage, skin, 1000, 1000, 64, 32, true, "TEST"));

        camera = new OrthographicCamera();
        fitViewport = new FitViewport(Gdx.graphics.getWidth() / SCALE / PPM, Gdx.graphics.getHeight() / SCALE / PPM, camera);

        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("SimMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / PPM);
        camera.position.set(fitViewport.getWorldWidth() / 2, fitViewport.getWorldHeight() / 2, 0);

        buildingManager = new BuildingManager(stage, skin, world, tiledMap);

        boxes = new ArrayList<>();

        manager = new AssetManager();
        manager.load("music/sakura.mp3", Music.class);
        manager.finishLoading();

        music = manager.get("music/sakura.mp3", Music.class);
        music.setLooping(true);
        //dwmusic.play();

        hud = new Hud(game.batch, skin, world);

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (MapObject object : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        for (MapObject object : tiledMap.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        for (MapObject object : tiledMap.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
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

    @Override
    public void render(float delta) {
        // Update character movement
        update(delta);


        // Clear the screen and start rendering
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //Draw the map and character
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.end();

        renderer.render();

        buildingManager.handleInput();
        buildingManager.handleBuildingPlacement(game.batch, camera, fitViewport);

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        b2dr.render(world, camera.combined);

        // Update the text box positions to be static and fixed above the bodies
        for (BoxEntity textBox : boxes) {
            textBox.updatePosition(camera);
        }

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
        //checkProximityToPlatform();
    }

    /***
     * Checks if it is within the map borders
     */
    private void moveRequest() {
        Vector2 vector = player.b2body.getLinearVelocity();
        if (!vector.isZero()) {
            if (vector.x < 0 && player.b2body.getPosition().x * PPM - (CHARACTER_SIZE_X / 2) - 1 <= 0) {
                vector.x = 0;
            } else if (vector.x > 0 && player.b2body.getPosition().x * PPM + (CHARACTER_SIZE_X / 2) + 1 >= MAP_SIZE_X) {
                vector.x = 0;
            }
            if (vector.y < 0 && player.b2body.getPosition().y * PPM - (CHARACTER_SIZE_Y / 2) - 1 <= 0) {
                vector.y = 0;
            } else if (vector.y > 0 && player.b2body.getPosition().y * PPM + (CHARACTER_SIZE_Y / 2) >= MAP_SIZE_Y) {
                vector.y = 0;
            }
            player.b2body.setLinearVelocity(vector);
        }


    }

    private void checkProximityToPlatform() {
        // Get player position (center of the player body)
        Vector2 playerPosition = player.b2body.getPosition().scl(PPM); // Scale to pixels
        // Get platform position (center of the platform body)
        for (BoxEntity box : boxes) {
            Vector2 platformPosition = box.getBody().getPosition().scl(PPM); // Scale to pixels
            // Calculate the distance between the centers of player and platform
            float distance = playerPosition.dst(platformPosition);

            // Check if the distance is within 10 pixels
            box.setIsVisible(distance <= 50);

        }

    }

    @Override
    public void show() {

    }

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
    }

    /***
     * Keeps camera in the map
     */
    public void boundary(Camera camera, float startX, float startY, float width, float height) {
        Vector3 position = camera.position;

        if (position.x < startX) {
            position.x = startX;
        }
        if (position.y < startY) {
            position.y = startY;
        }
        if (position.x > startX + width) {
            position.x = startX + width;
        }
        if (position.y > startY + height) {
            position.y = startY + height;
        }
        camera.position.set(position);
    }

    // Update camera position if the character moves into the border area
    private void updateCamera() {
        if(!showFullMap) {
            float screenX = (player.b2body.getPosition().x * PPM) - (camera.position.x - SCREEN_SIZE_X / 2);
            float screenY = (player.b2body.getPosition().y * PPM) - (camera.position.y - SCREEN_SIZE_Y / 2);
            //Adjust the camera if the character is outside the center area
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
            float startX = camera.viewportWidth / 2;
            float startY = camera.viewportHeight / 2;
            boundary(camera, startX, startY, MAP_SIZE_X - startX * 2, MAP_SIZE_Y - startY * 2);
            camera.update();
        }
    }


    // Handle character movement using arrow keys or WASD
    private void handleInput(float deltaTime) {
        float horizontalForce = 0;
        float verticalForce = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            verticalForce = CHARACTER_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            verticalForce = -CHARACTER_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            horizontalForce = -CHARACTER_SPEED;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            horizontalForce = CHARACTER_SPEED;
        }
        player.b2body.setLinearVelocity(horizontalForce, verticalForce);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && playerNearReseption) {
            placingBuilding();
        }
    }

    private void setupCollisionListener() {
        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                // Check if one of the fixtures is a sensor and the other is the player
                if ((fixtureA.getUserData() == "sensor" && fixtureB.getUserData() == "player") ||
                    (fixtureA.getUserData() == "player" && fixtureB.getUserData() == "sensor")) {
                    // Trigger an action here, e.g., display a message or perform some event
                    playerNearReseption = true;
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                // Check if the player is exiting the sensor area
                if ((fixtureA.getUserData() == "sensor" && fixtureB.getUserData() == "player") ||
                    (fixtureA.getUserData() == "player" && fixtureB.getUserData() == "sensor")) {
                    // Trigger an action for when the player exits the sensor area
                    playerNearReseption = false;
                    showFullMapView();

                    if (buildingManager.getIsWindowOpen()) {
                        buildingManager.closeBuildingWindow();
                    }
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

    private void placingBuilding() {
        showFullMapView();
        camera.update();
        buildingManager.openBuildingWindow();
    }

    private void showFullMapView() {
        if (!showFullMap) {
            // Save original position and zoom level to revert back
            originalCameraPosition = new Vector3(camera.position);
            originalZoom = camera.zoom;

            // Adjust the camera to show the whole map
            camera.zoom = Math.min(MAP_SIZE_X / camera.viewportWidth, MAP_SIZE_Y / camera.viewportHeight);
            camera.position.set(MAP_SIZE_X / 2, MAP_SIZE_Y / 2, 0);
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
}