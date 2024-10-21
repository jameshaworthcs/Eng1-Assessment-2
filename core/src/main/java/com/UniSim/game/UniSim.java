package com.UniSim.game;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import static com.UniSim.game.Constants.PPM;

public class UniSim extends ApplicationAdapter {
    private SpriteBatch batch;
    private Stage stage;


    private Texture tex;

    private World world;
    private Body player;
    private Texture map;
    private OrthographicCamera camera;

    private Box2DDebugRenderer b2dr;


    private final float SCALE = 2.0f;
    private final float CHARACTER_SPEED = 5f;  // Movement speed in pixels per second
    private final float CHARACTER_SIZE = 50f;    // Character size in pixels (50x50)

    // Map size (resized to 3000x3000)
    private final float MAP_SIZE = 3000f;

    private FitViewport fitViewport;

    // Screen size and camera border calculations
    private final float SCREEN_SIZE_X = 640f;
    private final float BORDER_SIZE_X = SCREEN_SIZE_X * 0.2f; // 20% border is 200 pixels
    private final float CENTER_MIN_X = BORDER_SIZE_X;         // 200 pixels from edge
    private final float CENTER_MAX_X = SCREEN_SIZE_X - BORDER_SIZE_X; // 800 pixels from edge

    private final float SCREEN_SIZE_Y = 480f;
    private final float BORDER_SIZE_Y = SCREEN_SIZE_Y * 0.3f; // 20% border is 200 pixels
    private final float CENTER_MIN_Y = BORDER_SIZE_Y;         // 200 pixels from edge
    private final float CENTER_MAX_Y = SCREEN_SIZE_Y - BORDER_SIZE_Y; // 800 pixels from edge

    //private BoxEntity platform2;

    private ArrayList<BoxEntity> boxes;

    //private Stage uiStage;       // Stage for UI components
    private Skin skin;           // Skin for UI styling

    private int satisfactionLvl;

    private int currencyLvl;
    private int fatigueLvl;
    private int knowledgeLvl;

    public static AssetManager manager;
    private Music music;

    @Override
    public void create() {
        batch = new SpriteBatch();
        tex = new Texture("character.jpg");
        map = new Texture("tempbg2.png");

        manager = new AssetManager();
        manager.load("music/sakura.mp3", Music.class);
        manager.finishLoading();
        
        music = manager.get("music/sakura.mp3", Music.class);
        music.setLooping(true);
        music.play();

        skin = new Skin(Gdx.files.internal("uiskin.json")); // Load the skin file
        stage = new Stage(new ScreenViewport()); // Initialize the stage
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        boxes = new ArrayList<>();


        world = new World(new Vector2(0, 0), false);
        b2dr = new Box2DDebugRenderer();
        player = createBox(1508, 1510, 32, 32, false, false);



        boxes.add(new BoxEntity(world, stage, skin, 1500, 1500, 64, 32, true, "TEST"));


        camera = new OrthographicCamera();

        fitViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        camera.setToOrtho(false, 640 / SCALE / PPM, 480 / SCALE / PPM);  // Screen size is 640, 480 pixels
        camera.position.set(1500, 1500, 0);  // Initially set the camera to the character's position
        camera.update();



    }

    @Override
    public void render() {
        // Update character movement
        update(Gdx.graphics.getDeltaTime());
        manager.update();


        // Clear the screen and start rendering
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //Draw the map and character
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        //batch.draw(tex, player.getPosition().x * PPM - (16), player.getPosition().y * PPM - (16), 32, 32);
        batch.draw(map, 0, 0, MAP_SIZE, MAP_SIZE);  // Draw the map resized to 3000x3000
        batch.draw(tex, player.getPosition().x * PPM - (16), player.getPosition().y * PPM - (16), 32, 32);
        //batch.draw(character, charX - CHARACTER_SIZE / 2, charY - CHARACTER_SIZE / 2, CHARACTER_SIZE, CHARACTER_SIZE);  // Draw character resized to 50x50
        batch.end();

        b2dr.render(world, camera.combined.scl(PPM));

        // Update the text box positions to be static and fixed above the bodies
        for (BoxEntity textBox : boxes) {
            textBox.updatePosition(camera);
        }

        // Update the stage to process UI events
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void update(float delta) {
        world.step(1 / 60f, 6, 2);
        updateCamera();
        handleInput(delta);
        checkProximityToPlatform();


    }

    private void checkProximityToPlatform() {
        // Get player position (center of the player body)
        Vector2 playerPosition = player.getPosition().scl(PPM); // Scale to pixels
        // Get platform position (center of the platform body)
        for (BoxEntity box : boxes){
            Vector2 platformPosition = box.getBody().getPosition().scl(PPM); // Scale to pixels
        // Calculate the distance between the centers of player and platform
            float distance = playerPosition.dst(platformPosition);

        // Check if the distance is within 10 pixels
            if (distance <= 50) {
                //System.out.println("HII");
                int hello = 1; // If nearby a platform
        }}
    }


    @Override
    public void resize(int width, int height) {
        //camera.setToOrtho(false, width / SCALE, height / SCALE);
        fitViewport.update(width, height); // updates screen in fitViewport way
    }

    // Update camera position if the character moves into the border area
    private void updateCamera() {

        // TODO: Ensure camera does not go beyond map boundaries - alter if statements
        float screenX = (player.getPosition().x * PPM) - (camera.position.x - SCREEN_SIZE_X / 2);
        float screenY = (player.getPosition().y * PPM) - (camera.position.y - SCREEN_SIZE_Y / 2);



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

        //Ensure the camera doesn't go beyond the map boundaries
        //camera.position.x = Math.max(SCREEN_SIZE / 2 / PPM, Math.min(camera.position.x, MAP_SIZE - SCREEN_SIZE / 2 / PPM));
        //camera.position.y = Math.max(SCREEN_SIZE / 2 / PPM, Math.min(camera.position.y, MAP_SIZE - SCREEN_SIZE / 2 / PPM));
        camera.update();


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
        player.setLinearVelocity(horizontalForce, verticalForce);

        // Prevent the character from going out of the map bounds (adjust for character size)

        // TODO: the below is old - stop the character from going out of bounds
        //charX = Math.max(CHARACTER_SIZE / 2, Math.min(charX, MAP_SIZE - CHARACTER_SIZE / 2));
        //charY = Math.max(CHARACTER_SIZE / 2, Math.min(charY, MAP_SIZE - CHARACTER_SIZE / 2));
    }

    @Override
    public void dispose() {
        batch.dispose();
        map.dispose();
        b2dr.dispose();
        world.dispose();
        stage.dispose();
        skin.dispose();
    }


    public Body createBox(int x, int y, int width, int height, boolean isStatic, boolean isBuilding) {  // creates boxes that can interact
        Body pBody;
        BodyDef def = new BodyDef();
        if (isStatic) {
            def.type = BodyDef.BodyType.StaticBody;
        } else {
            def.type = BodyDef.BodyType.DynamicBody;
        }
        def.position.set(x / PPM, y / PPM);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM); // 2 as 32 in each diriection

        pBody.createFixture(shape, 1.0f);
        shape.dispose();

        return pBody;
    }

}
