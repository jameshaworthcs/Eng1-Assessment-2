package com.UniSim.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import static com.UniSim.game.Constants.PPM;

public class BoxEntity {
    private Body body;
    private Table table;
    private Label label;
    private World world;
    private Skin skin;
    private Stage stage;
    private Boolean isVisible;

    // TODO: the textbox should only be displayed when checkProximityToPlayform in UniSim is true
    // TODO: feel free to move it/use the code etc to do so



    public BoxEntity(World world, Stage stage, Skin skin, int x, int y, int width, int height, boolean isStatic, String labelText) {


        this.world = world;
        this.stage = stage;
        this.skin = skin;
        this.isVisible = false;

        // Create the Box2D body
        this.body = createBox(x, y, width, height, isStatic);

        // Create the UI text above the box
        createTextBox(x, y, labelText);
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public Boolean getIsVisible() {
        return isVisible;
    }

    // Create a Box2D body
    private Body createBox(int x, int y, int width, int height, boolean isStatic) {
        BodyDef bodyDef = new BodyDef();
        if (isStatic) {
            bodyDef.type = BodyDef.BodyType.StaticBody;
        } else {
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }
        bodyDef.position.set(x / PPM, y / PPM);
        bodyDef.fixedRotation = true;

        Body boxBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;

        boxBody.createFixture(fixtureDef);
        shape.dispose();

        return boxBody;
    }

    // Create the text box above the Box2D body
    private void createTextBox(int x, int y, String text) {
        label = new Label(text, skin);

        // Create buttons with listeners
        TextButton yesButton = new TextButton("Yes", skin);
        TextButton noButton = new TextButton("No", skin);

         //Add listeners to the buttons
        // TODO: find a way to relate the ClickListeners back to main game class??
        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Yes button clicked");
                //buttonResponse = "yes";

            }
        });

        noButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("No button clicked");
                //buttonResponse = "no";


            }
        });

        table = new Table();
        //table.add(label).padBottom(10);
        table.setPosition(x, y);
        table.setSize(200, 100);

        table.add(label).padBottom(10).colspan(2);
        table.row(); // Move to the next row in the table

        // Add the "Yes" and "No" buttons to the table
        table.add(yesButton).padRight(10); // Add padding between buttons
        table.add(noButton);

        stage.addActor(table);
    }

    // Update the position of the text box to stay static on the screen
    public void updatePosition(OrthographicCamera camera) { //
        // TODO: temp fix the y coordinate is not static - logic works - maths is wrong
        // TODO: also does not scale with resize
        Vector3 worldPosition = new Vector3(body.getPosition().x, body.getPosition().y + 40, 0);
        Vector3 screenPosition = camera.project(worldPosition);
        table.setPosition(screenPosition.x, screenPosition.y - 1300);
        //System.out.println(screenPosition.x);
        //System.out.println(screenPosition.y);
    }


    public Body getBody() {
        return body;
    }
}
