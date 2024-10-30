package com.UniSim.game.Sprites;

import com.UniSim.game.Screens.gameScreen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import static com.UniSim.game.Constants.*;

public class Character extends Sprite {
    public enum State {RUN_UP, RUN_DOWN, RUN_X, STAND_UP, STAND_DOWN, STAND_X}
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private TextureRegion characterStandUp;
    private TextureRegion characterStandDown;
    private TextureRegion characterStandX;
    private Animation <TextureRegion> characterRunUp;
    private Animation <TextureRegion> characterRunDown;
    private Animation <TextureRegion> characterRunX;
    private boolean isGoingRight;
    private float stateTimer;

    public Character(World world, gameScreen screen) {
        super(screen.getCharacterTexture());
        this.world = world;
        currentState = State.STAND_DOWN;
        previousState = State.STAND_DOWN;
        stateTimer = 0.0f;
        isGoingRight = false;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(getTexture(), 17, 4, 14, 18));
        frames.add(new TextureRegion(getTexture(), 33, 4, 14, 18));
        characterRunDown = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        frames.add(new TextureRegion(getTexture(), 17, 28, 14, 18));
        frames.add(new TextureRegion(getTexture(), 33, 28, 14, 18));
        characterRunUp = new Animation<TextureRegion>(0.1f, frames);
        frames.clear();
        frames.add(new TextureRegion(getTexture(), 17, 52, 14, 18));
        frames.add(new TextureRegion(getTexture(), 33, 52, 14, 18));
        characterRunX = new Animation<TextureRegion>(0.1f, frames);


        characterStandDown = new TextureRegion(getTexture(), 1, 4, 14, 18);
        characterStandX = new TextureRegion(getTexture(), 1, 51, 14, 18);
        characterStandUp = new TextureRegion(getTexture(), 1, 28, 14, 18);

        defineCharacter();
        setBounds(0, 0, CHARACTER_SIZE_X / PPM, CHARACTER_SIZE_Y / PPM);
        setRegion(characterStandDown);
    }

    private void defineCharacter() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(1508 / PPM, 1510 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        bdef.fixedRotation = true;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox( CHARACTER_SIZE_X / 2 / PPM, CHARACTER_SIZE_Y / 2 / PPM); // 2 as 32 in each diriection

        fdef.shape = shape;
        b2body.createFixture(fdef);
        shape.dispose();
    }

    public void update(float delta) {
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y- getHeight() / 2);
        setRegion(getFrame(delta));
    }

    public TextureRegion getFrame(float delta) {
        currentState = getState();

        TextureRegion region;

        switch (currentState){
            case RUN_X:
                region = characterRunX.getKeyFrame(stateTimer, true);
                if (isGoingRight && !region.isFlipX()) {
                    region.flip(true, false);
                } else if (!isGoingRight && region.isFlipX()) {
                    region.flip(true, false);
                }
                break;
            case RUN_UP:
                region = characterRunUp.getKeyFrame(stateTimer, true);
                break;
            case RUN_DOWN:
                region = characterRunDown.getKeyFrame(stateTimer, true);
                break;
            case STAND_DOWN:
                region = characterStandDown;
                break;
            case STAND_X:
                region = characterStandX;
                if (isGoingRight && !region.isFlipX()) {
                    region.flip(true, false);
                } else if (!isGoingRight && region.isFlipX()) {
                    region.flip(true, false);
                }
                break;
            default:
                region = characterStandUp;
                break;
        }

        stateTimer = currentState == previousState ? stateTimer + delta : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (b2body.getLinearVelocity().y > 0) {
            return State.RUN_UP;
        } else if (b2body.getLinearVelocity().y < 0) {
            return State.RUN_DOWN;
        } else if (b2body.getLinearVelocity().x > 0) {
            isGoingRight = true;
            return State.RUN_X;
        } else if (b2body.getLinearVelocity().x < 0) {
            isGoingRight = false;
            return State.RUN_X;
        } else {
            if (previousState == State.RUN_DOWN || previousState == State.STAND_DOWN) {
                return State.STAND_DOWN;
            } else if (previousState == State.RUN_X || previousState == State.STAND_X) {
                return State.STAND_X;
            } else {
                return State.STAND_UP;
            }
        }
    }
}
