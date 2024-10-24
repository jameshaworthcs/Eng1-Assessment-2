package com.UniSim.game.Screens;

import com.UniSim.game.BoxEntity;
import com.UniSim.game.UniSim;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import java.util.ArrayList;
import static com.UniSim.game.Constants.PPM;

public class gameScreen implements Screen {
    private UniSim game;
    private Stage stage;

    private Texture characterTexture;

    private World world;
    private Body player;
    private Texture map;
    private OrthographicCamera camera;

    private Box2DDebugRenderer b2dr;

    private BoxEntity mapBorder;


    private final float SCALE = 2;
    private final float CHARACTER_SPEED = 20;  // Movement speed in pixels per second
    private final float CHARACTER_SIZE = 50;    // Character size in pixels (50x50)

    // Map size (resized to 4400x2000)
    private final float MAP_SIZE_X = 8800;
    private final float MAP_SIZE_Y = 4000;

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

    private ArrayList<BoxEntity> boxes;

    private Skin skin;           // Skin for UI styling


    public gameScreen(UniSim game){
        this.game = game;
        characterTexture = new Texture("character.jpg");
        map = new Texture("tempbg2.png");

        skin = new Skin(Gdx.files.internal("uiskin.json")); // Load the skin file
        stage = new Stage(new ScreenViewport()); // Initialize the stage
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor



        boxes = new ArrayList<>();

        world = new World(new Vector2(0, 0), false);
        //mapBorder = new BoxEntity(world, stage, skin, (int)MAP_SIZE_X / 2, (int)MAP_SIZE_Y / 2, (int)MAP_SIZE_X, (int)MAP_SIZE_Y, true, "Map Border");
        b2dr = new Box2DDebugRenderer();
        player = createBox(1508, 1510, (int)CHARACTER_SIZE, (int)CHARACTER_SIZE, false, false);

        boxes.add(new BoxEntity(world, stage, skin, 1500, 1500, 64, 32, true, "TEST"));
        boxes.add(new BoxEntity(world, stage, skin, 1000, 1000, 64, 32, true, "TEST"));


        camera = new OrthographicCamera();

        fitViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        camera.setToOrtho(false, Gdx.graphics.getWidth() / SCALE / PPM, Gdx.graphics.getHeight() / SCALE / PPM);  // Screen size is 640, 480 pixels
        camera.position.set(1500, 1500, 0);  // Initially set the camera to the character's position
        camera.update();



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
        //batch.draw(tex, player.getPosition().x * PPM - (16), player.getPosition().y * PPM - (16), 32, 32);
        game.batch.draw(map, 0, 0, MAP_SIZE_X, MAP_SIZE_Y);  // Draw the map resized to 3000x3000
        game.batch.draw(characterTexture, player.getPosition().x * PPM - (25), player.getPosition().y * PPM - (25), CHARACTER_SIZE, CHARACTER_SIZE);
        //batch.draw(character, charX - CHARACTER_SIZE / 2, charY - CHARACTER_SIZE / 2, CHARACTER_SIZE, CHARACTER_SIZE);  // Draw character resized to 50x50
        game.batch.end();

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
        moveRequest();
        checkProximityToPlatform();
    }

    /***
     * Checks if it is within the map borders
     */
    private void moveRequest(){
        Vector2 vector = player.getLinearVelocity();
        if(!vector.isZero()){
            if(vector.x < 0 && player.getPosition().x * PPM - (CHARACTER_SIZE / 2) - 1 <= 0){
                vector.x = 0;
            } else if (vector.x > 0 && player.getPosition().x * PPM + (CHARACTER_SIZE / 2) + 1 >= MAP_SIZE_X) {
                vector.x = 0;
            }
            if(vector.y < 0 && player.getPosition().y * PPM - (CHARACTER_SIZE / 2) - 1 <= 0){
                vector.y = 0;
            } else if (vector.y > 0 && player.getPosition().y * PPM + (CHARACTER_SIZE / 2 ) >= MAP_SIZE_Y) {
                vector.y = 0;
            }
            player.setLinearVelocity(vector);
        }


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
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height); // updates screen in fitViewport way
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
    public void boundary(Camera camera, float startX, float startY, float width, float height){
        Vector3 position = camera.position;

        if(position.x < startX){
            position.x = startX;
        }
        if(position.y < startY){
            position.y = startY;
        }
        if(position.x > startX + width){
            position.x = startX + width;
        }
        if(position.y > startY + height){
            position.y = startY + height;
        }
        camera.position.set(position);
        camera.update();
    }

    // Update camera position if the character moves into the border area
    private void updateCamera() {
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
        float startX = camera.viewportWidth / 2;
        float startY = camera.viewportHeight / 2;
        boundary(camera, startX, startY, MAP_SIZE_X - startX * 2, MAP_SIZE_Y - startY * 2);
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
