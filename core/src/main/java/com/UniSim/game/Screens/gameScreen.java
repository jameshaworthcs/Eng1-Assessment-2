package com.UniSim.game.Screens;

import com.UniSim.game.*;
import com.UniSim.game.Sprites.Character;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import java.util.ArrayList;
import static com.UniSim.game.Constants.*;

public class gameScreen implements Screen {
    private UniSim game;
    private Stage stage;

    private BuildingList buildingList;

    private Texture characterTexture;

    private World world;
    private Character player;
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

    private PauseMenu pauseMenu;
    private Texture pauseIconTexture;

    public gameScreen(UniSim game){
        this.game = game;
        characterTexture = new Texture("character-1.png");
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())); // Initialize the stage
        Gdx.input.setInputProcessor(stage); // Set the stage as the input processor

        boxes = new ArrayList<>();

        world = new World(new Vector2(0, 0), false);

        b2dr = new Box2DDebugRenderer();
        player = new Character(world, this);

        //boxes.add(new BoxEntity(world, stage, skin, 1500, 1500, 64, 32, true, "TEST"));
        //boxes.add(new BoxEntity(world, stage, skin, 1000, 1000, 64, 32, true, "TEST"));

        camera = new OrthographicCamera();
        fitViewport = new FitViewport(Gdx.graphics.getWidth() / SCALE / PPM, Gdx.graphics.getHeight() / SCALE / PPM, camera);

        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("SimMap.tmx");
        renderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / PPM);
        camera.position.set(fitViewport.getWorldWidth() / 2, fitViewport.getWorldHeight() / 2, 0);

        buildingList = new BuildingList(stage, skin, world, tiledMap);

        boxes = new ArrayList<>();

        manager = new AssetManager();
        manager.load("music/sakura.mp3", Music.class);
        manager.finishLoading();

        music = manager.get("music/sakura.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(1.0f);
        music.play();

        hud = new Hud(game.batch, skin, world);

        // Load the pause icon texture
        pauseIconTexture = new Texture(Gdx.files.internal("pause.png"));
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(pauseIconTexture));
        ImageButton pauseButton = new ImageButton(drawable);
        pauseButton.setPosition(10, Gdx.graphics.getHeight() - pauseButton.getHeight() - 10);
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseMenu.togglePause();
            }
        });
        stage.addActor(pauseButton);

        // Initialize pause menu
        pauseMenu = new PauseMenu(stage, skin, this);



        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for(MapObject object : tiledMap.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

        for(MapObject object : tiledMap.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM, (rect.getY() + rect.getHeight() / 2) / PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
            fdef.shape = shape;
            body.createFixture(fdef);
        }

    }

    // Method to set the music volume
    public void setMusicVolume(float volume) {
        if (music != null) {
            music.setVolume(volume);
        }
    }

    // Method to get the current music volume
    public float getMusicVolume() {
        if (music != null) {
            return music.getVolume();
        }
        return 0;
    }


    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseMenu.togglePause();
        }

        if (!pauseMenu.isPaused()) {
            update(delta);
        }


        // Clear the screen and start rendering
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //Draw the map and character
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.end();

        renderer.render();

        buildingList.handleInput();
        buildingList.handleBuildingPlacement(game.batch, camera, fitViewport);

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
        game.batch.end();

        // Update the stage to process UI events
        stage.act(delta);
        stage.draw();
    }

    public void update(float delta) {
        world.step(1 / 60f, 6, 2);
        player.update(delta);
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
    private void moveRequest(){
        Vector2 vector = player.b2body.getLinearVelocity();
        if(!vector.isZero()){
            if(vector.x < 0 && player.b2body.getPosition().x * PPM - (CHARACTER_SIZE_X / 2) - 1 <= 0){
                vector.x = 0;
            } else if (vector.x > 0 && player.b2body.getPosition().x * PPM + (CHARACTER_SIZE_X / 2) + 1 >= MAP_SIZE_X) {
                vector.x = 0;
            }
            if(vector.y < 0 && player.b2body.getPosition().y * PPM - (CHARACTER_SIZE_Y / 2) - 1 <= 0){
                vector.y = 0;
            } else if (vector.y > 0 && player.b2body.getPosition().y * PPM + (CHARACTER_SIZE_Y / 2 ) >= MAP_SIZE_Y) {
                vector.y = 0;
            }
            player.b2body.setLinearVelocity(vector);
        }


    }

    private void checkProximityToPlatform() {
        // Get player position (center of the player body)
        Vector2 playerPosition = player.b2body.getPosition().scl(PPM); // Scale to pixels
        // Get platform position (center of the platform body)
        for (BoxEntity box : boxes){
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
    }

    // Update camera position if the character moves into the border area
    private void updateCamera() {
        // Calculate the player's position in the game world and adjust by PPM to convert to screen pixels
        float playerScreenX = player.b2body.getPosition().x * PPM; 
        float playerScreenY = player.b2body.getPosition().y * PPM;
    
        // Calculate the current screen position offset of the camera (in terms of pixels)
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
        float startX = (camera.viewportWidth / 2) / PPM;
        float startY = (camera.viewportHeight / 2) / PPM;
    
        // Apply boundary restrictions to the camera to prevent it from going out of bounds
        boundary(camera, startX, startY, (MAP_SIZE_X / PPM) - startX * 2, (MAP_SIZE_Y / PPM) - startY * 2);
    
        // Update the camera to apply the changes
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
        player.b2body.setLinearVelocity(horizontalForce, verticalForce);
    }


//    public Body createBox(int x, int y, int width, int height, boolean isStatic, boolean isBuilding) {  // creates boxes that can interact
//        Body pBody;
//        BodyDef def = new BodyDef();
//        if (isStatic) {
//            def.type = BodyDef.BodyType.StaticBody;
//        } else {
//            def.type = BodyDef.BodyType.DynamicBody;
//        }
//        def.position.set(x / PPM, y / PPM);
//        def.fixedRotation = true;
//        pBody = world.createBody(def);
//
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(width / 2 / PPM, height / 2 / PPM); // 2 as 32 in each diriection
//
//        pBody.createFixture(shape, 1.0f);
//        shape.dispose();
//
//        return pBody;
//    }
}
